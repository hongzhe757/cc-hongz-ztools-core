package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link GzipBase64} 单元测试.
 *
 * @author hongzhe.qin
 */
class GzipBase64Test {

    /**
     * 验证 {@link GzipBase64#encode(byte[])} 对 null 返回 null.
     */
    @Test
    void encodeNullReturnsNull() {
        assertNull(GzipBase64.encode((byte[]) null));
        assertNull(GzipBase64.encode((CharSequence) null));
    }

    /**
     * 验证字符串 GZIP+Base64 往返.
     */
    @Test
    void roundTripText() {
        final String text = "重复文本用于验证 GZIP 压缩后再 Base64 编码。";
        final String encoded = GzipBase64.encode(text);
        assertNotEquals(Base64.encode(text), encoded);
        assertEquals(text, GzipBase64.decodeStr(encoded));
    }

    /**
     * 验证字节数组往返.
     */
    @Test
    void roundTripBytes() {
        final byte[] raw = new byte[256];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) i;
        }
        assertArrayEquals(raw, GzipBase64.decode(GzipBase64.encode(raw)));
    }

    /**
     * 验证空字节数组往返.
     */
    @Test
    void roundTripEmpty() {
        final byte[] raw = new byte[0];
        assertArrayEquals(raw, GzipBase64.decode(GzipBase64.encode(raw)));
    }
}
