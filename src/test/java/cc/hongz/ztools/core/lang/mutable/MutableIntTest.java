package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableInt} 单元测试.
 */
class MutableIntTest {

	@Test
	void defaultConstructor() {
		MutableInt mutableInt = new MutableInt();
		assertEquals(0, mutableInt.intValue());
	}

	@Test
	void intConstructor() {
		MutableInt mutableInt = new MutableInt(42);
		assertEquals(42, mutableInt.intValue());
		assertEquals(42, mutableInt.get());
	}

	@Test
	void numberConstructor() {
		MutableInt mutableInt = new MutableInt(Integer.valueOf(10));
		assertEquals(10, mutableInt.intValue());
	}

	@Test
	void stringConstructor() {
		MutableInt mutableInt = new MutableInt("123");
		assertEquals(123, mutableInt.intValue());
	}

	@Test
	void stringConstructorInvalid() {
		assertThrows(NumberFormatException.class, () -> new MutableInt("abc"));
	}

	@Test
	void setAndGetInt() {
		MutableInt mutableInt = new MutableInt();
		mutableInt.set(99);
		assertEquals(99, mutableInt.get());
	}

	@Test
	void setNumber() {
		MutableInt mutableInt = new MutableInt();
		mutableInt.set(Integer.valueOf(55));
		assertEquals(55, mutableInt.intValue());
	}

	@Test
	void increment() {
		MutableInt mutableInt = new MutableInt(5);
		MutableInt result = mutableInt.increment();
		assertSame(mutableInt, result);
		assertEquals(6, mutableInt.intValue());
	}

	@Test
	void decrement() {
		MutableInt mutableInt = new MutableInt(5);
		MutableInt result = mutableInt.decrement();
		assertSame(mutableInt, result);
		assertEquals(4, mutableInt.intValue());
	}

	@Test
	void numberValues() {
		MutableInt mutableInt = new MutableInt(7);
		assertEquals(7, mutableInt.intValue());
		assertEquals(7L, mutableInt.longValue());
		assertEquals(7.0f, mutableInt.floatValue());
		assertEquals(7.0d, mutableInt.doubleValue());
	}

	@Test
	void equalsAndHashCode() {
		MutableInt a = new MutableInt(10);
		MutableInt b = new MutableInt(10);
		MutableInt c = new MutableInt(20);

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a, c);
		assertNotEquals(a, null);
		assertNotEquals(a, "10");
	}

	@Test
	void compareTo() {
		MutableInt a = new MutableInt(10);
		MutableInt b = new MutableInt(20);
		MutableInt c = new MutableInt(10);

		assertTrue(a.compareTo(b) < 0);
		assertTrue(b.compareTo(a) > 0);
		assertEquals(0, a.compareTo(c));
	}

	@Test
	void toStringMethod() {
		MutableInt mutableInt = new MutableInt(42);
		assertEquals("42", mutableInt.toString());
	}
}
