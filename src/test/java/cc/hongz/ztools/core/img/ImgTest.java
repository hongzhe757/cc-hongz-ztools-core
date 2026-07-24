package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Img} 单元测试.
 * <p>
 * 覆盖 Img 图像编辑器的核心操作.
 *
 * @author hongzhe.qin
 */
class ImgTest {

    /**
     * 创建一个测试用的红色 100x80 图片.
     */
    private static BufferedImage createTestImage() {
        final BufferedImage image = new BufferedImage(100, 80, BufferedImage.TYPE_INT_RGB);
        final java.awt.Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 100, 80);
        g.dispose();
        return image;
    }

    /**
     * 将 Img 写出后读回为 BufferedImage.
     */
    private static BufferedImage writeAndRead(Img img) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        img.write(out);
        return ImgUtil.read(new ByteArrayInputStream(out.toByteArray()));
    }

    /**
     * 验证 {@link Img#from(Image)} 创建 Img 实例.
     */
    @Test
    void fromImage() {
        final Img img = Img.from((Image) createTestImage());
        assertNotNull(img);
    }

    /**
     * 验证 {@link Img#scale(float)} 缩放图片.
     */
    @Test
    void scaleByFactor() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).scale(0.5f));
        assertEquals(50, result.getWidth());
        assertEquals(40, result.getHeight());
    }

    /**
     * 验证 {@link Img#scale(int, int)} 按指定宽高缩放.
     */
    @Test
    void scaleToSize() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).scale(200, 160));
        assertEquals(200, result.getWidth());
        assertEquals(160, result.getHeight());
    }

    /**
     * 验证 {@link Img#cut(Rectangle)} 使用 Rectangle 切割.
     */
    @Test
    void cutWithRectangle() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).cut(new Rectangle(0, 0, 30, 20)));
        assertEquals(30, result.getWidth());
        assertEquals(20, result.getHeight());
    }

    /**
     * 验证 {@link Img#round} 圆角处理.
     */
    @Test
    void round() {
        final Img img = Img.from((Image) createTestImage());
        img.round(0.5);
        final BufferedImage result = writeAndRead(img);
        assertNotNull(result);
        assertEquals(100, result.getWidth());
    }

    /**
     * 验证 {@link Img#gray} 转灰度.
     */
    @Test
    void gray() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).gray());
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#binary} 二值化.
     */
    @Test
    void binary() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).binary());
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#rotate} 旋转图片.
     */
    @Test
    void rotate() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).rotate(90));
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#flip} 翻转图片.
     */
    @Test
    void flip() {
        final BufferedImage result = writeAndRead(Img.from((Image) createTestImage()).flip());
        assertNotNull(result);
        assertEquals(100, result.getWidth());
    }

    /**
     * 验证 {@link Img#pressText} 添加文字水印.
     */
    @Test
    void pressText() {
        final Img img = Img.from((Image) createTestImage());
        img.pressText("test", Color.WHITE,
                FontUtil.createSansSerifFont(12), 0, 0, 1.0f);
        final BufferedImage result = writeAndRead(img);
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#pressImage} 添加图片水印.
     */
    @Test
    void pressImage() {
        final BufferedImage watermark = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        final Img img = Img.from((Image) createTestImage());
        img.pressImage(watermark, new Rectangle(10, 10, 20, 20), 0.5f);
        final BufferedImage result = writeAndRead(img);
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#write(ByteArrayOutputStream)} 输出到流.
     */
    @Test
    void writeToStream() {
        final Img img = Img.from((Image) createTestImage());
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        img.write(out);
        assertTrue(out.size() > 0);
    }

    /**
     * 验证 {@link Img#setQuality} 设置质量参数.
     */
    @Test
    void setQuality() {
        final Img img = Img.from((Image) createTestImage());
        final Img result = img.setQuality(0.8f);
        assertNotNull(result);
    }

    /**
     * 验证 {@link Img#setBackgroundColor} 设置背景色.
     */
    @Test
    void setBackgroundColor() {
        final Img img = Img.from((Image) createTestImage());
        final Img result = img.setBackgroundColor(Color.WHITE);
        assertNotNull(result);
    }

    /**
     * 验证链式调用.
     */
    @Test
    void chainOperations() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Img.from((Image) createTestImage())
                .setQuality(0.8f)
                .scale(0.5f)
                .rotate(45)
                .write(out);
        assertTrue(out.size() > 0);
    }

    /**
     * 验证 {@link Img#stroke} 描边.
     */
    @Test
    void stroke() {
        final Img img = Img.from((Image) createTestImage());
        img.stroke(Color.BLACK, 2f);
        final BufferedImage result = writeAndRead(img);
        assertNotNull(result);
    }

    /**
     * 验证 {@link ScaleType} 枚举值.
     */
    @Test
    void scaleTypeEnum() {
        assertEquals(5, ScaleType.values().length);
        assertNotNull(ScaleType.valueOf("DEFAULT"));
        assertNotNull(ScaleType.valueOf("FAST"));
        assertNotNull(ScaleType.valueOf("SMOOTH"));
        assertNotNull(ScaleType.valueOf("REPLICATE"));
        assertNotNull(ScaleType.valueOf("AREA_AVERAGING"));
    }
}
