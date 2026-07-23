package cc.hongz.ztools.core.lang.intern;

import cc.hongz.ztools.core.util.RandomUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InternUtilTest {

	/**
	 * 检查规范字符串是否相同
	 */
	@SuppressWarnings("StringOperationCanBeSimplified")
	@Test
	public void weakTest(){
		final Interner<String> interner = InternUtil.createWeakInterner();
		String a1 = RandomUtil.randomString(RandomUtil.randomInt(100));
		String a2 = new String(a1);

		assertNotSame(a1, a2);

		assertSame(interner.intern(a1), interner.intern(a2));
	}

	@Test
	public void createJdkInternerTest() {
		Interner<String> interner = InternUtil.createJdkInterner();
		assertNotNull(interner);
		assertTrue(interner instanceof JdkStringInterner);
	}

	@Test
	public void createStringInternerWeakTest() {
		Interner<String> interner = InternUtil.createStringInterner(true);
		assertNotNull(interner);
		assertTrue(interner instanceof WeakInterner);
	}

	@Test
	public void createStringInternerJdkTest() {
		Interner<String> interner = InternUtil.createStringInterner(false);
		assertNotNull(interner);
		assertTrue(interner instanceof JdkStringInterner);
	}
}
