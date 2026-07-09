package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ColorUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
class ColorUtilTest {

    /**
     * 验证 {@link ColorUtil#randomColor()} 返回非 null 颜色.
     */
    @Test
    void randomColorNotNull() {
        assertNotNull(ColorUtil.randomColor());
    }

    /**
     * 验证 {@link ColorUtil#randomColor(Random)} 使用指定 Random 生成颜色.
     */
    @Test
    void randomColorWithRandom() {
        final Color c1 = ColorUtil.randomColor(new Random(42));
        final Color c2 = ColorUtil.randomColor(new Random(42));
        assertEquals(c1.getRGB(), c2.getRGB());
    }

    /**
     * 验证 {@link ColorUtil#randomColor(Random)} 在 random 为 null 时使用默认随机源.
     */
    @Test
    void randomColorWithNullRandom() {
        assertNotNull(ColorUtil.randomColor(null));
    }

    /**
     * 验证 {@link ColorUtil#maxDistance(Color)} 对 null 返回默认最大色差.
     */
    @Test
    void maxDistanceNullColor() {
        assertEquals(443, ColorUtil.maxDistance(null));
    }

    /**
     * 验证 {@link ColorUtil#maxDistance(Color)} 对具体颜色返回正数.
     */
    @Test
    void maxDistanceWithColor() {
        assertTrue(ColorUtil.maxDistance(Color.RED) > 0);
    }

    /**
     * 验证 {@link ColorUtil#computeColorDistance(Color, Color)} 同色距离为 0.
     */
    @Test
    void computeColorDistanceSameColor() {
        final Color red = Color.RED;
        assertEquals(0, ColorUtil.computeColorDistance(red, red));
    }

    /**
     * 验证 {@link ColorUtil#computeColorDistance(Color, Color)} 对 null 返回默认色差.
     */
    @Test
    void computeColorDistanceNull() {
        assertEquals(443, ColorUtil.computeColorDistance(null, Color.RED));
        assertEquals(443, ColorUtil.computeColorDistance(Color.RED, null));
    }

    /**
     * 验证 {@link ColorUtil#randomColor(Color, int)} 在合理色差下返回与背景有足够距离的颜色.
     */
    @Test
    void randomColorWithMinDistanceSuccess() {
        final Color bg = Color.WHITE;
        final Color fg = ColorUtil.randomColor(bg, 50);
        assertTrue(ColorUtil.computeColorDistance(bg, fg) >= 50);
    }

    /**
     * 验证最小色差不小于阈值时 {@link ColorUtil#randomColor(Color, int)} 抛出异常.
     */
    @Test
    void randomColorWithMinDistanceTooLarge() {
        assertThrows(IllegalArgumentException.class,
                () -> ColorUtil.randomColor(Color.RED, ColorUtil.maxDistance(Color.RED)));
    }
}
