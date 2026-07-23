package cc.hongz.ztools.core.lang.ref;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.ref.ReferenceQueue;

/**
 * {@link StrongObj}、{@link SoftObj}、{@link WeakObj} 单元测试.
 */
class RefTest {

	// ==================== StrongObj ====================

	@Test
	void strongObjGet() {
		StrongObj<String> ref = new StrongObj<>("hello");
		assertEquals("hello", ref.get());
	}

	@Test
	void strongObjEquals() {
		StrongObj<String> a = new StrongObj<>("hello");
		StrongObj<String> b = new StrongObj<>("hello");
		StrongObj<String> c = new StrongObj<>("world");

		assertEquals(a, b);
		assertNotEquals(a, c);
		assertEquals(a, a);
		assertNotEquals(a, null);
		assertNotEquals(a, "hello");
	}

	@Test
	void strongObjHashCode() {
		StrongObj<String> a = new StrongObj<>("hello");
		StrongObj<String> b = new StrongObj<>("hello");
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals("hello".hashCode(), a.hashCode());
	}

	// ==================== SoftObj ====================

	@Test
	void softObjGet() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		SoftObj<String> ref = new SoftObj<>("hello", queue);
		assertEquals("hello", ref.get());
	}

	@Test
	void softObjEquals() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		SoftObj<String> a = new SoftObj<>("hello", queue);
		SoftObj<String> b = new SoftObj<>("hello", queue);
		SoftObj<String> c = new SoftObj<>("world", queue);

		assertEquals(a, b);
		assertNotEquals(a, c);
		assertEquals(a, a);
		assertNotEquals(a, null);
		assertNotEquals(a, "hello");
	}

	@Test
	void softObjHashCode() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		SoftObj<String> ref = new SoftObj<>("hello", queue);
		assertEquals("hello".hashCode(), ref.hashCode());
	}

	// ==================== WeakObj ====================

	@Test
	void weakObjGet() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		WeakObj<String> ref = new WeakObj<>("hello", queue);
		assertEquals("hello", ref.get());
	}

	@Test
	void weakObjEquals() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		WeakObj<String> a = new WeakObj<>("hello", queue);
		WeakObj<String> b = new WeakObj<>("hello", queue);
		WeakObj<String> c = new WeakObj<>("world", queue);

		assertEquals(a, b);
		assertNotEquals(a, c);
		assertEquals(a, a);
		assertNotEquals(a, null);
		assertNotEquals(a, "hello");
	}

	@Test
	void weakObjHashCode() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		WeakObj<String> ref = new WeakObj<>("hello", queue);
		assertEquals("hello".hashCode(), ref.hashCode());
	}

	// ==================== Ref 接口 lambda ====================

	@Test
	void refFunctionalInterface() {
		Ref<String> ref = () -> "lambda";
		assertEquals("lambda", ref.get());
	}
}
