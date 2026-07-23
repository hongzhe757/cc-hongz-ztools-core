package cc.hongz.ztools.core.lang.intern;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WeakInternerTest {

	@Test
	public void internSameEqualsTest() {
		WeakInterner<String> interner = new WeakInterner<>();
		String a1 = new String("weakTest");
		String a2 = new String("weakTest");
		assertNotSame(a1, a2);
		assertSame(interner.intern(a1), interner.intern(a2));
	}

	@Test
	public void internNullTest() {
		WeakInterner<String> interner = new WeakInterner<>();
		assertNull(interner.intern(null));
	}

	@Test
	public void internDifferentStringsTest() {
		WeakInterner<String> interner = new WeakInterner<>();
		String a = new String("abc");
		String b = new String("def");
		assertNotSame(interner.intern(a), interner.intern(b));
	}

	@Test
	public void internMultipleTimesTest() {
		WeakInterner<String> interner = new WeakInterner<>();
		String s1 = new String("multi");
		String s2 = new String("multi");
		String s3 = new String("multi");
		String result = interner.intern(s1);
		assertSame(result, interner.intern(s2));
		assertSame(result, interner.intern(s3));
	}

	@Test
	public void internWithIntegerTest() {
		WeakInterner<Integer> interner = new WeakInterner<>();
		Integer a = new Integer(42);
		Integer b = new Integer(42);
		// equals 但可能不是同一实例
		assertEquals(a, b);
		// 经过 intern 后应返回同一实例
		assertSame(interner.intern(a), interner.intern(b));
	}
}
