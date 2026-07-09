package cc.hongz.ztools.core.img;

import cc.hongz.ztools.core.io.IORuntimeException;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * 图片处理工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：门面（Facade），对外统一图片 API，内部分派至 GraphicsUtil、ColorUtil 与 ImageIO.
 * @author hongzhe.qin
 */
public final class ImgUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private ImgUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 创建 {@link Graphics2D} 并可选填充背景色.
     *
     * @param image 图片
     * @param color 背景色，null 表示不填充
     * @return Graphics2D
     */
    public static Graphics2D createGraphics(final BufferedImage image, final Color color) {
        return GraphicsUtil.createGraphics(image, color);
    }

    /**
     * 写出 PNG 图片到输出流.
     *
     * @param image 图片
     * @param out   输出流
     * @throws IORuntimeException IO 异常
     */
    public static void writePng(final Image image, final OutputStream out) throws IORuntimeException {
        try {
            ImageIO.write(toBufferedImage(image), "png", out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 从输入流读取图片.
     *
     * @param imageStream 图片流
     * @return BufferedImage
     * @throws IORuntimeException       IO 异常
     * @throws IllegalArgumentException 不支持的图片类型
     */
    public static BufferedImage read(final InputStream imageStream) {
        try {
            BufferedImage result = ImageIO.read(imageStream);
            if (result == null) {
                throw new IllegalArgumentException("Image type is not supported!");
            }
            return result;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 生成随机颜色.
     *
     * @return 随机颜色
     */
    public static Color randomColor() {
        return ColorUtil.randomColor();
    }

    /**
     * 生成与指定颜色有足够色差的随机颜色.
     *
     * @param compareColor 比较颜色
     * @param minDistance  最小色差
     * @return 随机颜色
     */
    public static Color randomColor(final Color compareColor, final int minDistance) {
        return ColorUtil.randomColor(compareColor, minDistance);
    }

    /**
     * 使用指定随机源生成随机颜色.
     *
     * @param random 随机对象
     * @return 随机颜色
     */
    public static Color randomColor(final Random random) {
        return ColorUtil.randomColor(random);
    }

    /**
     * 将矩形坐标修正为以背景中心为基准.
     *
     * @param rectangle        矩形
     * @param backgroundWidth  背景宽
     * @param backgroundHeight 背景高
     * @return 修正后的坐标点
     */
    public static Point getPointBaseCentre(final Rectangle rectangle,
                                           final int backgroundWidth,
                                           final int backgroundHeight) {
        return new Point(
                rectangle.x + (Math.abs(backgroundWidth - rectangle.width) / 2),
                rectangle.y + (Math.abs(backgroundHeight - rectangle.height) / 2)
        );
    }

    // --------------------------------------- Static method end

    /**
     * 将 {@link Image} 转为 {@link BufferedImage}.
     *
     * @param image 原图
     * @return BufferedImage
     */
    private static BufferedImage toBufferedImage(final Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bufferedImage;
    }
}
