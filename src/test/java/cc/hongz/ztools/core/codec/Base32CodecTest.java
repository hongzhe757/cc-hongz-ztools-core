package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Base32Codec} 单元测试.
 *
 * @author hongzhe.qin
 */
class Base32CodecTest {

    /**
     * 验证标准 Base32 往返.
     */
    @Test
    void standardRoundTrip() {
        final byte[] raw = "Base32Codec".getBytes(StandardCharsets.UTF_8);
        final String encoded = Base32Codec.INSTANCE.encode(raw);
        assertArrayEquals(raw, Base32Codec.INSTANCE.decode(encoded));
    }

    /**
     * 验证 Hex 字母表变体往返.
     */
    @Test
    void hexAlphabetRoundTrip() {
        final byte[] raw = new byte[] {0x01, 0x02, (byte) 0xFE};
        final String encoded = Base32Codec.INSTANCE.encode(raw, true);
        assertArrayEquals(raw, Base32Codec.INSTANCE.decode(encoded, true));
    }

    /**
     * 验证与 {@link Base32} 门面结果一致.
     */
    @Test
    void matchesFacade() {
        final String text = "facade";
        assertEquals(Base32.encode(text), Base32Codec.INSTANCE.encode(text.getBytes(StandardCharsets.UTF_8)));
    }
}
