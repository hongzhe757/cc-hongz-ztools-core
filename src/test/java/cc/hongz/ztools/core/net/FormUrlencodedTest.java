package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FormUrlencoded 单元测试.
 */
class FormUrlencodedTest {

    @Test
    @DisplayName("ALL 编码器不为 null")
    void allCodecNotNullTest() {
        assertNotNull(FormUrlencoded.ALL);
    }

    @Test
    @DisplayName("空格编码为 +")
    void allCodecEncodeTest() {
        String encoded = FormUrlencoded.ALL.encode("hello world",
                StandardCharsets.UTF_8);
        assertEquals("hello+world", encoded);
    }

    @Test
    @DisplayName("安全字符 -_. 不编码")
    void allCodecSafeCharTest() {
        String encoded = FormUrlencoded.ALL.encode("abc-_.",
                StandardCharsets.UTF_8);
        assertEquals("abc-_.", encoded);
    }

    @Test
    @DisplayName("~ 被移除安全列表应编码")
    void allCodecSpecialCharTest() {
        String encoded = FormUrlencoded.ALL.encode("~test",
                StandardCharsets.UTF_8);
        assertTrue(encoded.startsWith("%7E") || encoded.startsWith("%7e"));
    }
}
