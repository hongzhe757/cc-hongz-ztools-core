package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Base64Decoder} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base64 解码器")
class Base64DecoderTest {

    /**
     * 验证字符串解码与 {@link Base64#decodeStr(CharSequence)} 一致.
     */
    @DisplayName("验证字符串解码与 Base64.decodeStr 一致")
    @Test
    void decodeStrMatchesFacade() {
        final String encoded = "aGVsbG8=";
        assertEquals("hello", Base64Decoder.decodeStr(encoded));
        assertEquals("hello", Base64.decodeStr(encoded));
    }

    /**
     * 验证 {@link Base64Decoder#isBase64Code(byte)} 对 padding 字符.
     */
    @DisplayName("验证 isBase64Code 对 padding 字符")
    @Test
    void isBase64CodePadding() {
        assertTrue(Base64Decoder.isBase64Code((byte) '='));
        assertTrue(Base64Decoder.isBase64Code((byte) 'A'));
        assertFalse(Base64Decoder.isBase64Code((byte) '?'));
    }

    /**
     * 验证带非 Base64 杂质时可截断解码.
     */
    @DisplayName("验证带非 Base64 杂质时可截断解码")
    @Test
    void decodeWithTrailingGarbage() {
        final byte[] in = "aGVsbG8=!!!".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), Base64Decoder.decode(in));
    }

    /**
     * 验证子数组解码.
     */
    @DisplayName("验证子数组解码")
    @Test
    void decodeSlice() {
        final byte[] in = "XXaGVsbG8=YY".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), Base64Decoder.decode(in, 2, 8));
    }
}
