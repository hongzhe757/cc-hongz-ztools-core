package cc.hongz.ztools.core.img;

import cc.hongz.ztools.core.util.RandomUtil;

import java.awt.Color;
import java.util.Random;

/**
 * 颜色工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，RGB 随机色与色差计算.
 * @author hongzhe.qin
 */
public final class ColorUtil {

    /**
     * RGB 分量上界.
     */
    private static final int RGB_COLOR_BOUND = 256;

    /**
     * 私有构造，禁止实例化.
     */
    private ColorUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 生成随机颜色.
     *
     * @return 随机颜色
     */
    public static Color randomColor() {
        return randomColor(null);
    }

    /**
     * 使用指定随机源生成随机颜色.
     *
     * @param random 随机对象，null 时使用默认随机源
     * @return 随机颜色
     */
    public static Color randomColor(final Random random) {
        Random r = random == null ? RandomUtil.getRandom() : random;
        return new Color(
                r.nextInt(RGB_COLOR_BOUND),
                r.nextInt(RGB_COLOR_BOUND),
                r.nextInt(RGB_COLOR_BOUND));
    }

    /**
     * 计算给定颜色在 RGB 空间中的最大可达色差.
     *
     * @param color 参考颜色
     * @return 最大色差
     */
    public static int maxDistance(final Color color) {
        if (color == null) {
            return 443;
        }
        final int maxX = RGB_COLOR_BOUND - 2 * color.getRed();
        final int maxY = RGB_COLOR_BOUND - 2 * color.getGreen();
        final int maxZ = RGB_COLOR_BOUND - 2 * color.getBlue();
        return (int) Math.sqrt(maxX * maxX + maxY * maxY + maxZ * maxZ);
    }

    /**
     * 计算两个颜色之间的三维欧氏距离.
     *
     * @param color1 颜色1
     * @param color2 颜色2
     * @return 色差
     */
    public static int computeColorDistance(final Color color1, final Color color2) {
        if (color1 == null || color2 == null) {
            return 443;
        }
        return (int) Math.sqrt(Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2));
    }

    /**
     * 生成与指定颜色有足够色差的随机颜色.
     *
     * @param compareColor 比较颜色
     * @param minDistance  最小色差
     * @return 随机颜色
     * @throws IllegalArgumentException minDistance 过大
     */
    public static Color randomColor(final Color compareColor, final int minDistance) {
        if (minDistance >= maxDistance(compareColor) / 3 * 2) {
            throw new IllegalArgumentException("minDistance is too large");
        }
        Color color = randomColor();
        while (computeColorDistance(compareColor, color) < minDistance) {
            color = randomColor();
        }
        return color;
    }

    // --------------------------------------- Static method end
}
