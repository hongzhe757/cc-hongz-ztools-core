package cc.hongz.ztools.core.img.gif;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link NeuQuant} 单元测试.
 * <p>
 * 覆盖主要 public 方法.
 *
 * @author hongzhe.qin
 */
class NeuQuantTest {

    /**
     * 验证 {@link NeuQuant#process()} 对小型像素数组生成调色板.
     */
    @Test
    void processReturnsColorMap() {
        final byte[] pixels = new byte[3 * 4 * 4];
        for (int i = 0; i < pixels.length; i += 3) {
            pixels[i] = (byte) (i % 256);
            pixels[i + 1] = (byte) ((i + 1) % 256);
            pixels[i + 2] = (byte) ((i + 2) % 256);
        }
        final NeuQuant quant = new NeuQuant(pixels, pixels.length, 1);
        final byte[] map = quant.process();
        assertNotNull(map);
        assertEquals(3 * NeuQuant.NETSIZE, map.length);
    }

    /**
     * 验证 {@link NeuQuant#colorMap()} 返回 768 字节 RGB 调色板.
     */
    @Test
    void colorMapLength() {
        final byte[] pixels = new byte[3 * 2 * 2];
        java.util.Arrays.fill(pixels, (byte) 128);
        final NeuQuant quant = new NeuQuant(pixels, pixels.length, 1);
        quant.learn();
        quant.unbiasnet();
        quant.inxbuild();
        assertEquals(768, quant.colorMap().length);
    }

    /**
     * 验证 {@link NeuQuant#map(int, int, int)} 返回有效调色板索引.
     */
    @Test
    void mapReturnsIndexInRange() {
        final byte[] pixels = new byte[3 * 2 * 2];
        pixels[0] = 10;
        pixels[1] = 20;
        pixels[2] = 30;
        final NeuQuant quant = new NeuQuant(pixels, pixels.length, 1);
        quant.process();
        final int index = quant.map(10, 20, 30);
        assertTrue(index >= 0 && index < NeuQuant.NETSIZE);
    }
}
