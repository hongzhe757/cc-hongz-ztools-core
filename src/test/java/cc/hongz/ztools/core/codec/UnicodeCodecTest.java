package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link UnicodeCodec} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Unicode 编码")
class UnicodeCodecTest {

    /**
     * 验证 {@link UnicodeCodec#encode(CharSequence)} 对 null 返回 null.
     */
    @DisplayName("对 null 返回 null")
    @Test
    void encodeNullReturnsNull() {
        assertNull(UnicodeCodec.encode(null));
        assertNull(UnicodeCodec.decode(null));
    }

    /**
     * 验证中文 {@code \\uXXXX} 编解码.
     */
    @DisplayName("中文 \\uXXXX 编解码")
    @Test
    void encodeDecodeChinese() {
        assertEquals("\\u4f60\\u597d", UnicodeCodec.encode("你好"));
        assertEquals("你好", UnicodeCodec.decode("\\u4f60\\u597d"));
    }

    /**
     * 验证 ASCII 与反斜杠处理.
     */
    @DisplayName("ASCII 与反斜杠处理")
    @Test
    void encodeAsciiAndBackslash() {
        assertEquals("a\\\\b", UnicodeCodec.encode("a\\b"));
        assertEquals("a\\b", UnicodeCodec.decode("a\\\\b"));
    }

    /**
     * 验证空串往返.
     */
    @DisplayName("空串往返")
    @Test
    void emptyString() {
        assertEquals("", UnicodeCodec.encode(""));
        assertEquals("", UnicodeCodec.decode(""));
    }
}
