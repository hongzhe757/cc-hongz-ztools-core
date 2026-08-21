package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BomReader} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("BomReader BOM 流读取器")
class BomReaderTest {

    @Test
    @DisplayName("读取 UTF-8 BOM 流")
    void readUtf8Bom() throws Exception {
        final byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        final byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        final byte[] all = new byte[bom.length + data.length];
        System.arraycopy(bom, 0, all, 0, bom.length);
        System.arraycopy(data, 0, all, bom.length, data.length);
        try (BomReader reader = new BomReader(new ByteArrayInputStream(all))) {
            final char[] buf = new char[16];
            final int n = reader.read(buf, 0, buf.length);
            assertEquals("hello", new String(buf, 0, n));
        }
    }
}
