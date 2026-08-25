package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FontUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
@DisplayName("FontUtil 字体工具单元测试")
class FontUtilTest {

    /**
     * 验证 {@link FontUtil#createFont()} 返回默认字体.
     */
    @DisplayName("返回默认字体")
    @Test
    void createFontDefault() {
        assertNotNull(FontUtil.createFont());
    }

    /**
     * 验证 {@link FontUtil#createSansSerifFont(int)} 返回指定大小的 SansSerif 字体.
     */
    @DisplayName("返回指定大小 SansSerif 字体")
    @Test
    void createSansSerifFont() {
        final Font font = FontUtil.createSansSerifFont(24);
        assertNotNull(font);
        assertEquals(24, font.getSize());
        assertEquals(Font.SANS_SERIF, font.getFamily());
    }

    /**
     * 验证 {@link FontUtil#createFont(String, int)} 返回指定名称和大小的字体.
     */
    @DisplayName("返回指定名称和大小字体")
    @Test
    void createFontWithNameAndSize() {
        final Font font = FontUtil.createFont("Serif", 18);
        assertNotNull(font);
        assertEquals(18, font.getSize());
        assertEquals("Serif", font.getFamily());
    }

    /**
     * 验证 {@link FontUtil#getDimension(FontMetrics, String)} 返回正宽与高.
     */
    @DisplayName("返回正宽与高")
    @Test
    void getDimension() {
        final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        final FontMetrics metrics = image.createGraphics().getFontMetrics(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        final Dimension dimension = FontUtil.getDimension(metrics, "AB");
        assertTrue(dimension.width > 0);
        assertTrue(dimension.height > 0);
    }
}
