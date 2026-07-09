package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FontUtil} 单元测试.
 * <p>
 * 覆盖字体度量尺寸计算.
 *
 * @author hongzhe.qin
 */
class FontUtilTest {

    /**
     * 验证 {@link FontUtil#getDimension(FontMetrics, String)} 返回正宽与高.
     */
    @Test
    void getDimension() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final FontMetrics metrics = image.createGraphics().getFontMetrics(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        final Dimension dimension = FontUtil.getDimension(metrics, "AB");
        assertTrue(dimension.width > 0);
        assertTrue(dimension.height > 0);
    }
}
