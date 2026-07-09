package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UuCodec} 单元测试.
 * <p>
 * 覆盖数据体/完整文件往返、多行切分、null 及 1～2 字节边界.
 *
 * @author hongzhe.qin
 */
class UuCodecTest {

    /**
     * 验证 {@link UuCodec#encode(byte[])} 对 null 返回 null.
     */
    @Test
    void encodeNullReturnsNull() {
        assertNull(UuCodec.encode(null));
        assertNull(UuCodec.encode(null, "x.bin"));
    }

    /**
     * 验证 {@link UuCodec#decode(CharSequence)} 对 null 返回 null.
     */
    @Test
    void decodeNullReturnsNull() {
        assertNull(UuCodec.decode(null));
    }

    /**
     * 验证数据体往返编解码.
     */
    @Test
    void bodyRoundTrip() {
        final byte[] raw = new byte[] {'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', 0, (byte) 0xFF};
        assertArrayEquals(raw, UuCodec.decodeBody(UuCodec.encodeBody(raw)));
    }

    /**
     * 验证 1 字节与 2 字节块往返.
     */
    @Test
    void shortChunkRoundTrip() {
        assertArrayEquals(new byte[] {0x41}, UuCodec.decodeBody(UuCodec.encodeBody(new byte[] {0x41})));
        assertArrayEquals(new byte[] {0x41, 0x42}, UuCodec.decodeBody(UuCodec.encodeBody(new byte[] {0x41, 0x42})));
    }

    /**
     * 验证超过 45 字节时拆成多行.
     */
    @Test
    void multiLineEncoding() {
        final byte[] raw = new byte[100];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) i;
        }
        final String body = UuCodec.encodeBody(raw);
        final long lineCount = body.chars().filter(ch -> ch == '\n').count();
        assertTrue(lineCount >= 2, "100 bytes should span multiple UU lines");
        assertArrayEquals(raw, UuCodec.decodeBody(body));
    }

    /**
     * 验证完整 UU 文件（含 begin/end）往返.
     */
    @Test
    void fileRoundTrip() {
        final byte[] raw = new byte[100];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) i;
        }
        final String encoded = UuCodec.encode(raw, "test.bin");
        assertTrue(encoded.startsWith("begin 644 test.bin"));
        assertTrue(encoded.contains("`\nend\n"));
        assertArrayEquals(raw, UuCodec.decode(encoded));
    }

    /**
     * 验证默认文件名 {@code file}.
     */
    @Test
    void defaultFileName() {
        final String encoded = UuCodec.encode(new byte[] {1, 2, 3});
        assertTrue(encoded.startsWith("begin 644 file\n"));
    }

    /**
     * 验证空数据编码含零长度行.
     */
    @Test
    void encodeEmpty() {
        assertEquals(" \n", UuCodec.encodeBody(new byte[0]));
        assertArrayEquals(new byte[0], UuCodec.decode(UuCodec.encode(new byte[0])));
    }

    /**
     * 验证随机长度数据往返.
     */
    @Test
    void randomRoundTrip() {
        final int len = RandomUtil.randomInt(1, 200);
        final byte[] raw = new byte[len];
        RandomUtil.getRandom().nextBytes(raw);
        assertArrayEquals(raw, UuCodec.decode(UuCodec.encode(raw, "rand.bin")));
    }
}
