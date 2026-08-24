package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Base58Codec} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base58 编码器")
class Base58CodecTest {

    /**
     * 验证 {@link Base58Codec#INSTANCE} 往返编解码.
     */
    @DisplayName("验证 INSTANCE 往返编解码")
    @Test
    void instanceRoundTrip() {
        final byte[] raw = "Base58Codec test".getBytes(StandardCharsets.UTF_8);
        final String encoded = Base58Codec.INSTANCE.encode(raw);
        assertArrayEquals(raw, Base58Codec.INSTANCE.decode(encoded));
    }

    /**
     * 验证与 {@link Base58#encode(byte[])} 结果一致.
     */
    @DisplayName("验证与 Base58 编码结果一致")
    @Test
    void matchesFacade() {
        final byte[] raw = "hello world".getBytes(StandardCharsets.UTF_8);
        assertEquals(Base58.encode(raw), Base58Codec.INSTANCE.encode(raw));
    }
}
