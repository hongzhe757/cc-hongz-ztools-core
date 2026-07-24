package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ImgUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
class ImgUtilTest {

    /**
     * 验证 {@link ImgUtil#createGraphics(BufferedImage, Color)} 创建 Graphics2D.
     */
    @Test
    void createGraphicsWithBackground() {
        final BufferedImage image = new BufferedImage(20, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = ImgUtil.createGraphics(image, Color.WHITE);
        assertNotNull(g);
        g.dispose();
    }

    /**
     * 验证 {@link ImgUtil#createGraphics(BufferedImage, Color)} 在 color 为 null 时不填充背景.
     */
    @Test
    void createGraphicsWithoutBackground() {
        final BufferedImage image = new BufferedImage(20, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = ImgUtil.createGraphics(image, null);
        assertNotNull(g);
        g.dispose();
    }

    /**
     * 验证 {@link ImgUtil#writePng(java.awt.Image, java.io.OutputStream)} 与 {@link ImgUtil#read(java.io.InputStream)} 往返一致.
     */
    @Test
    void writePngAndReadRoundTrip() throws Exception {
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImgUtil.writePng(image, out);
        assertTrue(out.size() > 0);

        final BufferedImage read = ImgUtil.read(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(10, read.getWidth());
        assertEquals(10, read.getHeight());
    }

    /**
     * 验证 {@link ImgUtil#writePng(java.awt.Image, java.io.OutputStream)} 对非 BufferedImage 走转换分支.
     */
    @Test
    void writePngNonBufferedImage() throws Exception {
        final int[] pixels = new int[100];
        java.util.Arrays.fill(pixels, Color.BLUE.getRGB());
        final MemoryImageSource source = new MemoryImageSource(10, 10, pixels, 0, 10);
        final java.awt.Image image = Toolkit.getDefaultToolkit().createImage(source);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImgUtil.writePng(image, out);
        assertTrue(out.size() > 0);
    }

    /**
     * 验证 {@link ImgUtil#read(java.io.InputStream)} 对非法内容抛出 {@link IllegalArgumentException}.
     */
    @Test
    void readUnsupportedImage() {
        assertThrows(IllegalArgumentException.class,
                () -> ImgUtil.read(new ByteArrayInputStream("not-image".getBytes())));
    }

    /**
     * 验证 {@link ImgUtil#getPointBaseCentre(Rectangle, int, int)} 计算居中偏移坐标.
     */
    @Test
    void getPointBaseCentre() {
        final Rectangle rect = new Rectangle(0, 0, 4, 4);
        assertEquals(8, ImgUtil.getPointBaseCentre(rect, 20, 20).x);
    }

    /**
     * 验证 {@link ImgUtil#randomColor()} 返回非 null.
     */
    @Test
    void randomColorDefault() {
        assertNotNull(ImgUtil.randomColor());
    }

    /**
     * 验证 {@link ImgUtil#randomColor(Color, int)} 与背景保持足够色差.
     */
    @Test
    void randomColorWithMinDistance() {
        final Color fg = ImgUtil.randomColor(Color.BLACK, 30);
        assertTrue(ColorUtil.computeColorDistance(Color.BLACK, fg) >= 30);
    }

    /**
     * 验证 {@link ImgUtil#randomColor(Random)} 使用指定 Random.
     */
    @Test
    void randomColorWithRandom() {
        assertNotNull(ImgUtil.randomColor(new Random(1)));
    }

    // ========================= toHex =========================

    /**
     * 验证 {@link ImgUtil#toHex(Color)} 将 Color 转为 16 进制.
     */
    @Test
    void toHex() {
        assertEquals("#FF0000", ImgUtil.toHex(Color.RED));
        assertEquals("#00FF00", ImgUtil.toHex(Color.GREEN));
        assertEquals("#0000FF", ImgUtil.toHex(Color.BLUE));
    }

    // ========================= scale =========================

    /**
     * 验证 {@link ImgUtil#scale(Image, int, int, Color)} 缩放图片.
     */
    @Test
    void scaleToSize() {
        final BufferedImage src = createRedImage(100, 80);
        final Image scaled = ImgUtil.scale(src, 50, 40, null);
        assertNotNull(scaled);
    }

    // ========================= cut =========================

    /**
     * 验证 {@link ImgUtil#cut(Image, int, int, int)} 切割圆形区域.
     */
    @Test
    void cutCircle() {
        final BufferedImage src = createRedImage(100, 80);
        final Image cut = ImgUtil.cut(src, 50, 40, 20);
        assertNotNull(cut);
    }

    // ========================= rotate =========================

    /**
     * 验证 {@link ImgUtil#rotate(Image, int)} 旋转图片.
     */
    @Test
    void rotate() {
        final BufferedImage src = createRedImage(100, 80);
        final Image rotated = ImgUtil.rotate(src, 90);
        assertNotNull(rotated);
    }

    // ========================= toBytes =========================

    /**
     * 验证 {@link ImgUtil#toBytes(Image, String)} 将图片转为字节数组.
     */
    @Test
    void toBytes() {
        final BufferedImage src = createRedImage(10, 10);
        final byte[] bytes = ImgUtil.toBytes(src, "png");
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    // ========================= copyImage =========================

    /**
     * 验证 {@link ImgUtil#copyImage(BufferedImage, int)} 复制图片.
     */
    @Test
    void copyImage() {
        final BufferedImage src = createRedImage(10, 10);
        final BufferedImage copy = ImgUtil.copyImage(src, BufferedImage.TYPE_INT_ARGB);
        assertNotNull(copy);
        assertEquals(10, copy.getWidth());
        assertEquals(10, copy.getHeight());
    }

    // ========================= createImage =========================

    /**
     * 验证 {@link ImgUtil#createImage(String, Font, Color, Color, javax.imageio.stream.ImageOutputStream)} 创建文字图片.
     */
    @Test
    void createImage() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(out);
        ImgUtil.createImage("test", FontUtil.createSansSerifFont(12),
                Color.WHITE, Color.BLACK, ios);
        assertTrue(out.size() > 0);
    }

    // ========================= helper =========================

    private static BufferedImage createRedImage(int w, int h) {
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return image;
    }
}
