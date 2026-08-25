package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BackgroundRemoval} 单元测试.
 * <p>
 * 覆盖 public 方法: hexToRgb, areColorsWithinTolerance, getMainColor, backgroundRemoval.
 *
 * @author hongzhe.qin
 */
@DisplayName("BackgroundRemoval 背景移除单元测试")
class BackgroundRemovalTest {

    // -------------------------------------------------------------------- hexToRgb

    @DisplayName("hexToRgb 解析黑色")
    @Test
    void hexToRgbBlack() {
        Color c = BackgroundRemoval.hexToRgb("#000000");
        assertEquals(0, c.getRed());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    @DisplayName("hexToRgb 解析白色")
    @Test
    void hexToRgbWhite() {
        Color c = BackgroundRemoval.hexToRgb("#ffffff");
        assertEquals(255, c.getRed());
        assertEquals(255, c.getGreen());
        assertEquals(255, c.getBlue());
    }

    @DisplayName("hexToRgb 解析红色")
    @Test
    void hexToRgbRed() {
        Color c = BackgroundRemoval.hexToRgb("#ff0000");
        assertEquals(255, c.getRed());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getBlue());
    }

    // -------------------------------------------------------------------- areColorsWithinTolerance

    @DisplayName("同色小容差在容差内")
    @Test
    void areColorsWithinTolerance_sameColor_smallTolerance() {
        assertTrue(BackgroundRemoval.areColorsWithinTolerance(Color.RED, Color.RED, 1));
    }

    @DisplayName("相近色小容差在容差内")
    @Test
    void areColorsWithinTolerance_similarColors_smallTolerance() {
        Color c1 = new Color(100, 100, 100);
        Color c2 = new Color(110, 110, 110);
        assertTrue(BackgroundRemoval.areColorsWithinTolerance(c1, c2, 20));
    }

    @DisplayName("不同色小容差在容差外")
    @Test
    void areColorsWithinTolerance_differentColors_smallTolerance() {
        Color c1 = Color.RED;
        Color c2 = Color.BLUE;
        assertFalse(BackgroundRemoval.areColorsWithinTolerance(c1, c2, 10));
    }

    @DisplayName("使用 Color 容差比较")
    @Test
    void areColorsWithinTolerance_withColorTolerance() {
        Color c1 = new Color(50, 50, 50);
        Color c2 = new Color(55, 55, 55);
        Color tolerance = new Color(10, 10, 10);
        assertTrue(BackgroundRemoval.areColorsWithinTolerance(c1, c2, tolerance));
    }

    // -------------------------------------------------------------------- getMainColor

    @DisplayName("纯色图片获取主色")
    @Test
    void getMainColor_solidColorImage() {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                img.setRGB(x, y, Color.RED.getRGB());
            }
        }
        String mainColor = BackgroundRemoval.getMainColor(img);
        assertEquals("#FF0000", mainColor);
    }

    @DisplayName("null 图片抛异常")
    @Test
    void getMainColor_nullImage_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> BackgroundRemoval.getMainColor((BufferedImage) null));
    }

    // -------------------------------------------------------------------- backgroundRemoval (BufferedImage)

    @DisplayName("纯色图片移除为透明背景")
    @Test
    void backgroundRemoval_solidImage_transparentBg() {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                img.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        BufferedImage result = BackgroundRemoval.backgroundRemoval(img, null, 0);
        assertNotNull(result);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
    }

    @DisplayName("纯色图片移除并覆盖颜色")
    @Test
    void backgroundRemoval_solidImage_withOverrideColor() {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                img.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        BufferedImage result = BackgroundRemoval.backgroundRemoval(img, Color.BLACK, 10);
        assertNotNull(result);
    }

    @DisplayName("容差超界被截断")
    @Test
    void backgroundRemoval_toleranceClamped() {
        BufferedImage img = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                img.setRGB(x, y, Color.BLUE.getRGB());
            }
        }
        // tolerance > 255 should be clamped to 255
        BufferedImage result = BackgroundRemoval.backgroundRemoval(img, null, 999);
        assertNotNull(result);
    }

    // -------------------------------------------------------------------- IMAGES_TYPE

    @DisplayName("IMAGES_TYPE 包含 jpg 和 png")
    @Test
    void imagesType_containsJpgAndPng() {
        assertEquals(2, BackgroundRemoval.IMAGES_TYPE.length);
        assertEquals("jpg", BackgroundRemoval.IMAGES_TYPE[0]);
        assertEquals("png", BackgroundRemoval.IMAGES_TYPE[1]);
    }
}
