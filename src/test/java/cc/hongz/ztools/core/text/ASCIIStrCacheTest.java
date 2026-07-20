package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ASCIIStrCache} 单元测试.
 */
class ASCIIStrCacheTest {

	@Test
	void cacheTest() {
		// 测试ASCII字符缓存
		assertEquals("a", ASCIIStrCache.toString('a'));
		assertEquals("0", ASCIIStrCache.toString('0'));
		// \0是ASCII字符0，缓存中是String.valueOf('\0')
		assertEquals(String.valueOf('\0'), ASCIIStrCache.toString('\0'));
	}

	@Test
	void nonASCIITest() {
		// 非ASCII字符应返回新字符串
		String str = ASCIIStrCache.toString('\u4e2d');
		assertEquals("\u4e2d", str);
	}
}
