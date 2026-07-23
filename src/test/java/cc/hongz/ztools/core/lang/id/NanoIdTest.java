package cc.hongz.ztools.core.lang.id;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

public class NanoIdTest {

	@Test
	public void randomNanoIdDefaultTest() {
		String id = NanoId.randomNanoId();
		assertNotNull(id);
		assertEquals(NanoId.DEFAULT_SIZE, id.length());
	}

	@Test
	public void randomNanoIdCustomSizeTest() {
		int customSize = 10;
		String id = NanoId.randomNanoId(customSize);
		assertNotNull(id);
		assertEquals(customSize, id.length());
	}

	@Test
	public void randomNanoIdWithAlphabetTest() {
		char[] alphabet = "abcdef".toCharArray();
		String id = NanoId.randomNanoId(null, alphabet, 20);
		assertNotNull(id);
		assertEquals(20, id.length());
		// 生成的ID应只包含指定的字母表字符
		for (char c : id.toCharArray()) {
			assertTrue(c >= 'a' && c <= 'f', "Character " + c + " not in alphabet");
		}
	}

	@Test
	public void randomNanoIdWithCustomRandomTest() {
		SecureRandom random = new SecureRandom();
		String id = NanoId.randomNanoId(random, null, 15);
		assertNotNull(id);
		assertEquals(15, id.length());
	}

	@Test
	public void uniquenessTest() {
		// 生成多个ID，应互不相同
		java.util.Set<String> ids = new java.util.HashSet<>();
		for (int i = 0; i < 100; i++) {
			ids.add(NanoId.randomNanoId());
		}
		assertEquals(100, ids.size());
	}

	@Test
	public void invalidAlphabetTest() {
		// 空字母表应抛异常
		assertThrows(IllegalArgumentException.class, () ->
				NanoId.randomNanoId(null, new char[0], 10));
	}

	@Test
	public void invalidSizeTest() {
		// 长度<=0应抛异常
		assertThrows(IllegalArgumentException.class, () ->
				NanoId.randomNanoId(0));
		assertThrows(IllegalArgumentException.class, () ->
				NanoId.randomNanoId(-1));
	}
}
