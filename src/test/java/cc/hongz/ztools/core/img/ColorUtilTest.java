package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ColorUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
@DisplayName("ColorUtil 颜色工具单元测试")
class ColorUtilTest {

    // ========================= toHex =========================

    /**
     * 验证 {@link ColorUtil#toHex(Color)} 将 Color 转为 16 进制.
     */
    @DisplayName("Color 转 16 进制")
    @Test
    void toHexFromColor() {
        assertEquals("#FF0000", ColorUtil.toHex(Color.RED));
        assertEquals("#00FF00", ColorUtil.toHex(Color.GREEN));
        assertEquals("#0000FF", ColorUtil.toHex(Color.BLUE));
        assertEquals("#FFFFFF", ColorUtil.toHex(Color.WHITE));
        assertEquals("#000000", ColorUtil.toHex(Color.BLACK));
    }

    /**
     * 验证 {@link ColorUtil#toHex(int, int, int)} 将 RGB 分量转为 16 进制.
     */
    @DisplayName("RGB 分量转 16 进制")
    @Test
    void toHexFromRgb() {
        assertEquals("#FCF6D6", ColorUtil.toHex(252, 246, 214));
        assertEquals("#000000", ColorUtil.toHex(0, 0, 0));
        assertEquals("#FFFFFF", ColorUtil.toHex(255, 255, 255));
    }

    /**
     * 验证 {@link ColorUtil#toHex(int, int, int)} 对越界值抛出异常.
     */
    @DisplayName("越界 RGB 抛异常")
    @Test
    void toHexOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> ColorUtil.toHex(-1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> ColorUtil.toHex(0, 256, 0));
        assertThrows(IllegalArgumentException.class, () -> ColorUtil.toHex(0, 0, 999));
    }

    // ========================= hexToColor =========================

    /**
     * 验证 {@link ColorUtil#hexToColor(String)} 将 16 进制转为 Color.
     */
    @DisplayName("16 进制转 Color")
    @Test
    void hexToColorBasic() {
        assertEquals(Color.RED, ColorUtil.hexToColor("#FF0000"));
        assertEquals(Color.WHITE, ColorUtil.hexToColor("#FFFFFF"));
    }

    /**
     * 验证 {@link ColorUtil#hexToColor(String)} 不带 # 前缀也能解析.
     */
    @DisplayName("不带 # 前缀解析")
    @Test
    void hexToColorWithoutHash() {
        final Color c = ColorUtil.hexToColor("00FF00");
        assertEquals(0, c.getRed());
        assertEquals(255, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    // ========================= getColor(int) =========================

    /**
     * 验证 {@link ColorUtil#getColor(int)} 将 RGB 整数值转为 Color.
     */
    @DisplayName("RGB 整数值转 Color")
    @Test
    void getColorFromInt() {
        final Color c = ColorUtil.getColor(0xFF0000);
        assertEquals(255, c.getRed());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    // ========================= getColor(String) =========================

    /**
     * 验证 {@link ColorUtil#getColor(String)} 解析颜色英文名.
     */
    @DisplayName("按颜色英文名解析")
    @Test
    void getColorByName() {
        assertEquals(Color.RED, ColorUtil.getColor("red"));
        assertEquals(Color.BLUE, ColorUtil.getColor("BLUE"));
        assertEquals(Color.WHITE, ColorUtil.getColor("White"));
        assertEquals(Color.BLACK, ColorUtil.getColor("black"));
        assertEquals(Color.GRAY, ColorUtil.getColor("gray"));
        assertEquals(Color.PINK, ColorUtil.getColor("pink"));
        assertEquals(Color.ORANGE, ColorUtil.getColor("orange"));
        assertEquals(Color.YELLOW, ColorUtil.getColor("yellow"));
        assertEquals(Color.GREEN, ColorUtil.getColor("green"));
        assertEquals(Color.MAGENTA, ColorUtil.getColor("magenta"));
        assertEquals(Color.CYAN, ColorUtil.getColor("cyan"));
        assertEquals(Color.LIGHT_GRAY, ColorUtil.getColor("LIGHTGRAY"));
        assertEquals(Color.LIGHT_GRAY, ColorUtil.getColor("LIGHT_GRAY"));
        assertEquals(Color.DARK_GRAY, ColorUtil.getColor("DARKGRAY"));
    }

    /**
     * 验证 {@link ColorUtil#getColor(String)} 解析 16 进制形式.
     */
    @DisplayName("按 16 进制形式解析")
    @Test
    void getColorByHex() {
        assertEquals(Color.RED, ColorUtil.getColor("#FF0000"));
        assertEquals(Color.RED, ColorUtil.getColor("$FF0000"));
    }

    /**
     * 验证 {@link ColorUtil#getColor(String)} 解析 RGB 逗号形式.
     */
    @DisplayName("按 RGB 逗号形式解析")
    @Test
    void getColorByRgbString() {
        final Color c = ColorUtil.getColor("255,0,0");
        assertNotNull(c);
        assertEquals(255, c.getRed());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    /**
     * 验证 {@link ColorUtil#getColor(String)} 对空白/null 返回 null.
     */
    @DisplayName("空白或 null 返回 null")
    @Test
    void getColorByBlankName() {
        assertNull(ColorUtil.getColor(""));
        assertNull(ColorUtil.getColor("  "));
        assertNull(ColorUtil.getColor(null));
    }

    /**
     * 验证 {@link ColorUtil#getColor(String)} 解析暗金色和亮金色.
     */
    @DisplayName("解析暗金色和亮金色")
    @Test
    void getColorGold() {
        assertNotNull(ColorUtil.getColor("DARKGOLD"));
        assertNotNull(ColorUtil.getColor("LIGHTGOLD"));
    }

    // ========================= add =========================

    /**
     * 验证 {@link ColorUtil#add(Color, Color)} 叠加两色.
     */
    @DisplayName("叠加两色")
    @Test
    void addColors() {
        final Color result = ColorUtil.add(Color.RED, Color.BLUE);
        assertNotNull(result);
        assertTrue(result.getRed() >= 0 && result.getRed() <= 255);
        assertTrue(result.getBlue() >= 0 && result.getBlue() <= 255);
    }

    // ========================= getMainColor =========================

    /**
     * 验证 {@link ColorUtil#getMainColor(BufferedImage, int[]...)} 获取主色调.
     */
    @DisplayName("获取主色调")
    @Test
    void getMainColorBasic() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.setRGB(x, y, Color.RED.getRGB());
            }
        }
        final String mainColor = ColorUtil.getMainColor(image);
        assertEquals("#ff0000", mainColor.toLowerCase());
    }

    /**
     * 验证 {@link ColorUtil#getMainColor(BufferedImage, int[]...)} 带过滤色.
     */
    @DisplayName("带过滤色获取主色调")
    @Test
    void getMainColorWithFilter() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.setRGB(x, y, (x < 5) ? Color.RED.getRGB() : Color.BLUE.getRGB());
            }
        }
        // 过滤红色，主色调应为蓝色
        final String mainColor = ColorUtil.getMainColor(image, new int[]{255, 0, 0});
        assertEquals("#0000ff", mainColor.toLowerCase());
    }

    // ========================= randomColor =========================

    /**
     * 验证 {@link ColorUtil#randomColor()} 返回非 null 颜色.
     */
    @DisplayName("随机颜色非 null")
    @Test
    void randomColorNotNull() {
        assertNotNull(ColorUtil.randomColor());
    }

    /**
     * 验证 {@link ColorUtil#randomColor(Random)} 使用指定 Random 生成颜色.
     */
    @DisplayName("使用指定 Random 生成颜色")
    @Test
    void randomColorWithRandom() {
        final Color c1 = ColorUtil.randomColor(new Random(42));
        final Color c2 = ColorUtil.randomColor(new Random(42));
        assertEquals(c1.getRGB(), c2.getRGB());
    }

    /**
     * 验证 {@link ColorUtil#randomColor(Random)} 在 random 为 null 时使用默认随机源.
     */
    @DisplayName("null Random 使用默认随机源")
    @Test
    void randomColorWithNullRandom() {
        assertNotNull(ColorUtil.randomColor(null));
    }

    // ========================= maxDistance =========================

    /**
     * 验证 {@link ColorUtil#maxDistance(Color)} 对 null 返回默认最大色差.
     */
    @DisplayName("null 颜色返回默认最大色差")
    @Test
    void maxDistanceNullColor() {
        assertEquals(443, ColorUtil.maxDistance(null));
    }

    /**
     * 验证 {@link ColorUtil#maxDistance(Color)} 对具体颜色返回正数.
     */
    @DisplayName("具体颜色返回正数")
    @Test
    void maxDistanceWithColor() {
        assertTrue(ColorUtil.maxDistance(Color.RED) > 0);
    }

    // ========================= computeColorDistance =========================

    /**
     * 验证 {@link ColorUtil#computeColorDistance(Color, Color)} 同色距离为 0.
     */
    @DisplayName("同色距离为 0")
    @Test
    void computeColorDistanceSameColor() {
        final Color red = Color.RED;
        assertEquals(0, ColorUtil.computeColorDistance(red, red));
    }

    /**
     * 验证 {@link ColorUtil#computeColorDistance(Color, Color)} 对 null 返回默认色差.
     */
    @DisplayName("null 颜色返回默认色差")
    @Test
    void computeColorDistanceNull() {
        assertEquals(443, ColorUtil.computeColorDistance(null, Color.RED));
        assertEquals(443, ColorUtil.computeColorDistance(Color.RED, null));
    }

    // ========================= randomColor(Color, int) =========================

    /**
     * 验证 {@link ColorUtil#randomColor(Color, int)} 在合理色差下返回与背景有足够距离的颜色.
     */
    @DisplayName("合理色差下返回有足够距离的颜色")
    @Test
    void randomColorWithMinDistanceSuccess() {
        final Color bg = Color.WHITE;
        final Color fg = ColorUtil.randomColor(bg, 50);
        assertTrue(ColorUtil.computeColorDistance(bg, fg) >= 50);
    }

    /**
     * 验证最小色差不小于阈值时 {@link ColorUtil#randomColor(Color, int)} 抛出异常.
     */
    @DisplayName("色差阈值过大抛异常")
    @Test
    void randomColorWithMinDistanceTooLarge() {
        assertThrows(IllegalArgumentException.class,
                () -> ColorUtil.randomColor(Color.RED, ColorUtil.maxDistance(Color.RED)));
    }
}
