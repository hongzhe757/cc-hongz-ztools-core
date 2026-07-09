package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link QuotedPrintable} 单元测试.
 *
 * @author hongzhe.qin
 */
class QuotedPrintableTest {

    /**
     * 验证 {@link QuotedPrintable#encode(byte[])} 对 null 返回 null.
     */
    @Test
    void encodeNullReturnsNull() {
        assertNull(QuotedPrintable.encode((byte[]) null));
        assertNull(QuotedPrintable.encode((CharSequence) null));
    }

    /**
     * 验证空格与等号被编码.
     */
    @Test
    void encodeSpecialChars() {
        assertEquals("hello=20world=3D", QuotedPrintable.encode("hello world="));
    }

    /**
     * 验证软换行 {@code =\r\n} 解码忽略.
     */
    @Test
    void decodeSoftLineBreak() {
        assertEquals("hello world", QuotedPrintable.decodeStr("hello=20=\r\nworld"));
    }

    /**
     * 验证 UTF-8 中文往返编解码.
     */
    @Test
    void roundTripUtf8() {
        final String text = "MIME 邮件编码测试";
        final String encoded = QuotedPrintable.encode(text);
        assertEquals(text, QuotedPrintable.decodeStr(encoded));
    }

    /**
     * 验证字节级解码.
     */
    @Test
    void decodeBytes() {
        final byte[] raw = "A".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals(raw, QuotedPrintable.decode("A"));
    }
}
