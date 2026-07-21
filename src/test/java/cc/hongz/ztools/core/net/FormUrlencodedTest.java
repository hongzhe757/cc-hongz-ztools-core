package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FormUrlencoded 单元测试
 */
class FormUrlencodedTest {

	@Test
	void allCodecNotNullTest() {
		assertNotNull(FormUrlencoded.ALL);
	}

	@Test
	void allCodecEncodeTest() {
		// 空格应编码为+
		String encoded = FormUrlencoded.ALL.encode("hello world", StandardCharsets.UTF_8);
		assertEquals("hello+world", encoded);
	}

	@Test
	void allCodecSafeCharTest() {
		// 安全字符（-_.）不应被编码
		String encoded = FormUrlencoded.ALL.encode("abc-_.", StandardCharsets.UTF_8);
		assertEquals("abc-_.", encoded);
	}

	@Test
	void allCodecSpecialCharTest() {
		// ~ 被移除安全列表，应被编码
		String encoded = FormUrlencoded.ALL.encode("~test", StandardCharsets.UTF_8);
		assertTrue(encoded.startsWith("%7E") || encoded.startsWith("%7e"));
	}
}
