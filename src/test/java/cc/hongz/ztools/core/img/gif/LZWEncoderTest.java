package cc.hongz.ztools.core.img.gif;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link LZWEncoder} 单元测试.
 * <p>
 * 覆盖 encode 压缩输出.
 *
 * @author hongzhe.qin
 */
class LZWEncoderTest {

    /**
     * 验证 {@link LZWEncoder#encode(java.io.OutputStream)} 对索引像素写出 LZW 数据块.
     */
    @Test
    void encodeWritesCompressedData() throws Exception {
        final int width = 4;
        final int height = 4;
        final byte[] indexedPixels = new byte[width * height];
        for (int i = 0; i < indexedPixels.length; i++) {
            indexedPixels[i] = (byte) (i % 8);
        }
        final LZWEncoder encoder = new LZWEncoder(width, height, indexedPixels, 8);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(out);
        assertTrue(out.size() > 2);
    }
}
