package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link Ascii85} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ASCII85 编码")
class Ascii85Test {

    /**
     * 验证 {@link Ascii85#decode(CharSequence)} 对 null 返回 null.
     */
    @DisplayName("验证 null 输入解码返回 null")
    @Test
    void decodeNullReturnsNull() {
        assertNull(Ascii85.decode(null));
    }

    /**
     * 验证与 Python {@code base64.a85encode} 一致的标准向量.
     */
    @DisplayName("验证标准编码向量与 Python base64.a85encode 一致")
    @Test
    void encodeKnownVector() {
        assertEquals("BOu!rDZ", Ascii85.encode("hello".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }

    /**
     * 验证 {@code <~ ~>} 包装符解码.
     */
    @DisplayName("验证 <~ ~> 包装符解码")
    @Test
    void decodeWithWrapper() {
        final byte[] raw = "hi".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        assertArrayEquals(raw, Ascii85.decode("<~" + Ascii85.encode(raw) + "~>"));
    }

    /**
     * 验证往返编解码.
     */
    @DisplayName("验证往返编解码")
    @Test
    void roundTrip() {
        final byte[] raw = {0, 0, 0, 0, 1, 2, 3, 4, 5, (byte) 0xFF};
        assertArrayEquals(raw, Ascii85.decode(Ascii85.encode(raw)));
    }

    /**
     * 验证 {@code z} 表示四个零字节.
     */
    @DisplayName("验证 z 表示四个零字节")
    @Test
    void zeroBlockUsesZ() {
        final byte[] fourZeros = {0, 0, 0, 0};
        assertEquals("z", Ascii85.encode(fourZeros));
        assertArrayEquals(fourZeros, Ascii85.decode("z"));
    }

    /**
     * 验证空数组编码为空串.
     */
    @DisplayName("验证空数组编码为空串")
    @Test
    void encodeEmpty() {
        assertEquals("", Ascii85.encode(new byte[0]));
    }
}
