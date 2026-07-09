package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link YEnc} 单元测试.
 * <p>
 * 覆盖转义字节、CRC 校验、行宽折行、null/空数据及异常路径.
 *
 * @author hongzhe.qin
 */
class YEncTest {

    /**
     * 验证 {@link YEnc#encode(byte[])} 对 null 返回 null.
     */
    @Test
    void encodeNullReturnsNull() {
        assertNull(YEnc.encode(null));
        assertNull(YEnc.encode(null, "a.bin"));
        assertNull(YEnc.encode(null, "a.bin", 64));
    }

    /**
     * 验证 {@link YEnc#decode(CharSequence)} / {@link YEnc#decodeBody(CharSequence)} 对 null 返回 null.
     */
    @Test
    void decodeNullReturnsNull() {
        assertNull(YEnc.decode(null));
        assertNull(YEnc.decodeBody(null));
    }

    /**
     * 验证空数据体编码为空串.
     */
    @Test
    void encodeEmptyBody() {
        assertEquals("", YEnc.encodeBody(new byte[0]));
        assertArrayEquals(new byte[0], YEnc.decodeBody(""));
    }

    /**
     * 验证含需转义字节的 yEnc 数据体往返.
     */
    @Test
    void bodyRoundTripWithEscapes() {
        final byte[] raw = {0, 9, 10, 13, 61, 4, 100, (byte) 0xFF};
        assertArrayEquals(raw, YEnc.decodeBody(YEnc.encodeBody(raw)));
    }

    /**
     * 验证行首 {@code .} 需转义（原始字节 4 在行首时）.
     */
    @Test
    void lineStartDotEscape() {
        final byte[] raw = {4, 5, 6};
        final String body = YEnc.encodeBody(raw, 2);
        assertTrue(body.contains("=D"), "line-start byte 4 should be escaped as =D");
        assertArrayEquals(raw, YEnc.decodeBody(body));
    }

    /**
     * 验证自定义行宽折行后仍可解码.
     */
    @Test
    void customLineLength() {
        final byte[] raw = new byte[20];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) (i + 50);
        }
        final String body = YEnc.encodeBody(raw, 8);
        assertTrue(body.contains("\n"));
        assertArrayEquals(raw, YEnc.decodeBody(body));
    }

    /**
     * 验证完整 yEnc 文件（含 CRC）往返.
     */
    @Test
    void fileRoundTrip() {
        final byte[] raw = new byte[256];
        for (int i = 0; i < raw.length; i++) {
            raw[i] = (byte) i;
        }
        final String encoded = YEnc.encode(raw, "data.bin");
        assertTrue(encoded.startsWith("=ybegin line=128 size=256 name=data.bin"));
        assertTrue(encoded.contains("=yend size=256 crc="));
        assertArrayEquals(raw, YEnc.decode(encoded));
    }

    /**
     * 验证仅数据体解码（无头尾）.
     */
    @Test
    void decodeBodyWithoutHeaders() {
        final byte[] raw = "plain body".getBytes();
        assertArrayEquals(raw, YEnc.decode(YEnc.encodeBody(raw)));
    }

    /**
     * 验证 CRC32 与头尾声明一致.
     */
    @Test
    void crc32MatchesHeader() {
        final byte[] raw = "yEnc sample".getBytes();
        final long crc = YEnc.crc32(raw);
        final String encoded = YEnc.encode(raw, "sample.txt");
        assertTrue(encoded.toUpperCase().contains(String.format("%08X", crc)));
    }

    /**
     * 验证 CRC 不匹配时抛出 {@link UtilException}.
     */
    @Test
    void crcMismatchThrows() {
        final byte[] raw = "bad crc".getBytes();
        final String tampered = YEnc.encode(raw, "x").replaceAll("crc=[0-9A-Fa-f]{8}", "crc=00000000");
        assertThrows(UtilException.class, () -> YEnc.decode(tampered));
    }

    /**
     * 验证非法转义序列抛出异常.
     */
    @Test
    void invalidEscapeThrows() {
        assertThrows(UtilException.class, () -> YEnc.decodeBody("abc="));
    }

    /**
     * 验证多种字节值往返.
     */
    @Test
    void roundTripVariousSizes() {
        final byte[] raw = {0x00, 0x01, 0x2E, 0x3D, 0x7F};
        assertArrayEquals(raw, YEnc.decode(YEnc.encode(raw)));
    }
}
