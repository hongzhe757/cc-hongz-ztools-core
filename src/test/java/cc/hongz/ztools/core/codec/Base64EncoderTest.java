package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Base64Encoder} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base64 编码器")
class Base64EncoderTest {

    /**
     * 验证字符串编码与 {@link Base64#encode(CharSequence)} 一致.
     */
    @DisplayName("验证字符串编码与 Base64.encode 一致")
    @Test
    void encodeStringMatchesFacade() {
        final String text = "hello";
        assertEquals(Base64.encode(text), Base64Encoder.encode(text));
    }

    /**
     * 验证 URL Safe 编码不含 {@code +} / {@code /}.
     */
    @DisplayName("验证 URL Safe 编码不含 + 与 /")
    @Test
    void encodeUrlSafeBytes() {
        final byte[] raw = new byte[] {(byte) 0xFB, (byte) 0xEF, (byte) 0xBE};
        final String encoded = Base64Encoder.encodeUrlSafe(raw);
        assertFalse(encoded.contains("+"));
        assertFalse(encoded.contains("/"));
    }

    /**
     * 验证多行 MIME 编码含换行.
     */
    @DisplayName("验证多行 MIME 编码含换行")
    @Test
    void encodeMultiLine() {
        final byte[] raw = new byte[80];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) i;
        }
        final String encoded = Base64Encoder.encodeStr(raw, true, false);
        assertTrue(encoded.contains("\r\n") || encoded.contains("\n"));
    }

    /**
     * 验证字节数组编码往返（经 {@link Base64Decoder}）.
     */
    @DisplayName("验证字节数组编码往返")
    @Test
    void encodeBytesRoundTrip() {
        final byte[] raw = "encoder test".getBytes(StandardCharsets.UTF_8);
        final String encoded = Base64Encoder.encode(raw);
        assertArrayEquals(raw, Base64Decoder.decode(encoded));
    }
}
