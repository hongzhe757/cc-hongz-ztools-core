package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BOMInputStream} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("BOMInputStream BOM 输入流")
class BOMInputStreamTest {

    @Test
    @DisplayName("检测 UTF-8 BOM")
    void detectUtf8Bom() {
        final byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        final byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        final byte[] all = new byte[bom.length + data.length];
        System.arraycopy(bom, 0, all, 0, bom.length);
        System.arraycopy(data, 0, all, bom.length, data.length);
        final BOMInputStream in = new BOMInputStream(new ByteArrayInputStream(all));
        assertEquals("UTF-8", in.getCharset());
    }
}
