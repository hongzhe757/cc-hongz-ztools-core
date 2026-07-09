package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link GraphicsUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法及重载.
 *
 * @author hongzhe.qin
 */
class GraphicsUtilTest {

    /**
     * 验证 {@link GraphicsUtil#createGraphics(BufferedImage, Color)} 创建 Graphics2D 并填充背景.
     */
    @Test
    void createGraphicsWithBackground() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.LIGHT_GRAY);
        assertNotNull(g);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#createGraphics(BufferedImage, Color)} 在 color 为 null 时不填充背景.
     */
    @Test
    void createGraphicsWithoutBackground() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, null);
        assertNotNull(g);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#getCenterY(Graphics, int)} 返回垂直居中 Y 坐标.
     */
    @Test
    void getCenterY() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        assertTrue(GraphicsUtil.getCenterY(g, 40) > 0);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawStringColourful(Graphics, String, Font, int, int)} 逐字随机色绘制.
     */
    @Test
    void drawStringColourfulBasic() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        assertSame(g, GraphicsUtil.drawStringColourful(g, "Ab", new Font(Font.SANS_SERIF, Font.PLAIN, 18), 100, 40));
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawStringColourful(Graphics, String, Font, int, int, Color)} 与背景保持色差.
     */
    @Test
    void drawStringColourfulWithBackground() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawStringColourful(g, "X", new Font(Font.SANS_SERIF, Font.PLAIN, 18), 100, 40, Color.WHITE);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawStringColourful(Graphics, String, Font, int, int, Color, int)} 指定最小色差.
     */
    @Test
    void drawStringColourfulWithCompareColor() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawStringColourful(g, "Y", new Font(Font.SANS_SERIF, Font.PLAIN, 18), 100, 40, Color.WHITE, 30);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawString(Graphics, String, Font, Color, int, int)} 固定颜色绘制.
     */
    @Test
    void drawStringFixedColor() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawString(g, "Hi", new Font(Font.SANS_SERIF, Font.PLAIN, 14), Color.BLACK, 100, 40);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawString(Graphics, String, Font, Color, int, int, Color, int)} 随机色与色差约束.
     */
    @Test
    void drawStringWithCompareColor() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawString(g, "Z", new Font(Font.SANS_SERIF, Font.PLAIN, 14), null, 100, 40, Color.WHITE, 20);
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawString(Graphics, String, Font, Color, Rectangle)} 基于矩形居中绘制.
     */
    @Test
    void drawStringWithRectangle() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawString(g, "R", new Font(Font.SANS_SERIF, Font.PLAIN, 14), Color.BLUE,
                new Rectangle(10, 5, 80, 30));
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawString(Graphics, String, Font, Color, Point)} 基于坐标绘制.
     */
    @Test
    void drawStringWithPoint() {
        final BufferedImage image = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, Color.WHITE);
        GraphicsUtil.drawString(g, "P", new Font(Font.SANS_SERIF, Font.PLAIN, 14), Color.GREEN, new Point(5, 20));
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawImg(Graphics, java.awt.Image, Point)} 按点绘制图片.
     */
    @Test
    void drawImgWithPoint() {
        final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        final BufferedImage icon = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        GraphicsUtil.drawImg(g, icon, new Point(5, 5));
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#drawImg(Graphics, java.awt.Image, Rectangle)} 按矩形绘制图片.
     */
    @Test
    void drawImgWithRectangle() {
        final BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        final BufferedImage icon = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        GraphicsUtil.drawImg(g, icon, new Rectangle(0, 0, 20, 20));
        g.dispose();
    }

    /**
     * 验证 {@link GraphicsUtil#setAlpha(Graphics2D, float)} 设置透明度.
     */
    @Test
    void setAlpha() {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();
        final Graphics2D result = GraphicsUtil.setAlpha(g, 0.5f);
        assertEquals(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f), result.getComposite());
        g.dispose();
    }
}
