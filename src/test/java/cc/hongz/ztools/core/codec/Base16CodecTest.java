package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link Base16Codec} 单元测试.
 * <p>
 * 覆盖大小写 Hex 编解码、奇数位补零及 Unicode 转义.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base16 编码器")
class Base16CodecTest {

    /**
     * 验证小写 Hex 往返编解码.
     */
    @DisplayName("验证小写 Hex 往返编解码")
    @Test
    void encodeDecodeLower() {
        final byte[] raw = "hello".getBytes(StandardCharsets.UTF_8);
        final char[] encoded = Base16Codec.CODEC_LOWER.encode(raw);
        assertEquals("68656c6c6f", new String(encoded));
        assertArrayEquals(raw, Base16Codec.CODEC_LOWER.decode("68656c6c6f"));
    }

    /**
     * 验证大写 Hex 往返编解码.
     */
    @DisplayName("验证大写 Hex 往返编解码")
    @Test
    void encodeDecodeUpper() {
        final byte[] raw = new byte[] {0x01, (byte) 0xFF};
        final char[] encoded = Base16Codec.CODEC_UPPER.encode(raw);
        assertEquals("01FF", new String(encoded));
        assertArrayEquals(raw, Base16Codec.CODEC_UPPER.decode("01FF"));
    }

    /**
     * 验证奇数位 Hex 解码时前补 0.
     */
    @DisplayName("验证奇数位 Hex 解码前补 0")
    @Test
    void decodeOddLengthHex() {
        assertArrayEquals(new byte[] {0x0A}, Base16Codec.CODEC_LOWER.decode("a"));
    }

    /**
     * 验证 {@link Base16Codec#toUnicodeHex(char)} 输出格式.
     */
    @DisplayName("验证 Unicode 转义输出格式")
    @Test
    void toUnicodeHex() {
        assertEquals("\\u4f60", Base16Codec.CODEC_LOWER.toUnicodeHex('你'));
    }

    /**
     * 验证解码前去除空白.
     */
    @DisplayName("验证解码前去除空白")
    @Test
    void decodeIgnoresWhitespace() {
        final byte[] raw = new byte[] {0x12, 0x34};
        assertArrayEquals(raw, Base16Codec.CODEC_LOWER.decode("12 34"));
    }

    /**
     * 验证空输入解码返回 null.
     */
    @DisplayName("验证空输入解码返回 null")
    @Test
    void decodeEmptyReturnsNull() {
        assertNull(Base16Codec.CODEC_LOWER.decode(StrUtil.EMPTY));
        assertNull(Base16Codec.CODEC_LOWER.decode(null));
    }
}
