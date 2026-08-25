package cc.hongz.ztools.core.img;

import cc.hongz.ztools.core.convert.Convert;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 颜色工具类.
 *
 * @author hongzhe.qin
 * @since 4.5.2
 */
@SuppressWarnings("checkstyle:MagicNumber")
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
     * Color对象转16进制表示，例如#fcf6d6.
     *
     * @param color {@link Color}
     * @return 16进制的颜色值，例如#fcf6d6
     */
    public static String toHex(final Color color) {
        return toHex(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * RGB颜色值转换成十六进制颜色码.
     *
     * @param r 红(R)
     * @param g 绿(G)
     * @param b 蓝(B)
     * @return 返回字符串形式的 十六进制颜色码
     */
    public static String toHex(final int r, final int g, final int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("RGB must be 0~255!");
        }
        return String.format("#%02X%02X%02X", r, g, b);
    }

    /**
     * 16进制的颜色值转换为Color对象，例如#fcf6d6.
     *
     * @param hex 16进制的颜色值，例如#fcf6d6
     * @return {@link Color}
     */
    public static Color hexToColor(final String hex) {
        return getColor(Integer.parseInt(StrUtil.removePrefix(hex, "#"), 16));
    }

    /**
     * 获取一个RGB值对应的颜色.
     *
     * @param rgb RGB值
     * @return {@link Color}
     */
    public static Color getColor(final int rgb) {
        return new Color(rgb);
    }

    /**
     * 将颜色值转换成具体的颜色类型.
     * 汇集了常用的颜色集，支持以下几种形式：
     * <pre>
     * 1. 颜色的英文名（大小写皆可）
     * 2. 16进制表示，例如：#fcf6d6或者$fcf6d6
     * 3. RGB形式，例如：13,148,252
     * </pre>
     *
     * @param colorName 颜色的英文名，16进制表示或RGB表示
     * @return {@link Color}
     */
    public static Color getColor(final String colorName) {
        if (StrUtil.isBlank(colorName)) {
            return null;
        }
        String resolvedColorName = colorName.toUpperCase();

        if ("BLACK".equals(resolvedColorName)) {
            return Color.BLACK;
        } else if ("WHITE".equals(resolvedColorName)) {
            return Color.WHITE;
        } else if ("LIGHTGRAY".equals(resolvedColorName) || "LIGHT_GRAY".equals(resolvedColorName)) {
            return Color.LIGHT_GRAY;
        } else if ("GRAY".equals(resolvedColorName)) {
            return Color.GRAY;
        } else if ("DARKGRAY".equals(resolvedColorName) || "DARK_GRAY".equals(resolvedColorName)) {
            return Color.DARK_GRAY;
        } else if ("RED".equals(resolvedColorName)) {
            return Color.RED;
        } else if ("PINK".equals(resolvedColorName)) {
            return Color.PINK;
        } else if ("ORANGE".equals(resolvedColorName)) {
            return Color.ORANGE;
        } else if ("YELLOW".equals(resolvedColorName)) {
            return Color.YELLOW;
        } else if ("GREEN".equals(resolvedColorName)) {
            return Color.GREEN;
        } else if ("MAGENTA".equals(resolvedColorName)) {
            return Color.MAGENTA;
        } else if ("CYAN".equals(resolvedColorName)) {
            return Color.CYAN;
        } else if ("BLUE".equals(resolvedColorName)) {
            return Color.BLUE;
        } else if ("DARKGOLD".equals(resolvedColorName) || "DARK_GOLD".equals(resolvedColorName)) {
            return hexToColor("#9e7e67");
        } else if ("LIGHTGOLD".equals(resolvedColorName) || "LIGHT_GOLD".equals(resolvedColorName)) {
            return hexToColor("#ac9c85");
        } else if (StrUtil.startWith(resolvedColorName, '#')) {
            return hexToColor(resolvedColorName);
        } else if (StrUtil.startWith(resolvedColorName, '$')) {
            return hexToColor("#" + resolvedColorName.substring(1));
        } else {
            final List<String> rgb = StrUtil.split(resolvedColorName, ',');
            if (3 == rgb.size()) {
                final Integer r = Convert.toInt(rgb.get(0), null);
                final Integer g = Convert.toInt(rgb.get(1), null);
                final Integer b = Convert.toInt(rgb.get(2), null);
                if (!ArrayUtil.hasNull(r, g, b)) {
                    return new Color(r, g, b);
                }
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 叠加颜色.
     *
     * @param color1 颜色1
     * @param color2 颜色2
     * @return 叠加后的颜色
     */
    public static Color add(final Color color1, final Color color2) {
        double r1 = color1.getRed();
        double g1 = color1.getGreen();
        double b1 = color1.getBlue();
        double a1 = color1.getAlpha();
        double r2 = color2.getRed();
        double g2 = color2.getGreen();
        double b2 = color2.getBlue();
        double a2 = color2.getAlpha();
        int r = (int) ((r1 * a1 / 255 + r2 * a2 / 255) / (a1 / 255 + a2 / 255));
        int g = (int) ((g1 * a1 / 255 + g2 * a2 / 255) / (a1 / 255 + a2 / 255));
        int b = (int) ((b1 * a1 / 255 + b2 * a2 / 255) / (a1 / 255 + a2 / 255));
        return new Color(r, g, b);
    }

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
        Assert.isTrue(minDistance < maxDistance(compareColor) / 3 * 2,
                "minDistance is too large, there are too few remaining colors!");
        Color color = randomColor();
        while (computeColorDistance(compareColor, color) < minDistance) {
            color = randomColor();
        }
        return color;
    }

    /**
     * 获取给定图片的主色调，背景填充用.
     *
     * @param image      {@link BufferedImage}
     * @param rgbFilters 过滤多种颜色
     * @return {@link String} #ffffff
     */
    public static String getMainColor(final BufferedImage image, final int[]... rgbFilters) {
        int r, g, b;
        Map<String, Long> countMap = new HashMap<>();
        int width = image.getWidth();
        int height = image.getHeight();
        int minx = image.getMinX();
        int miny = image.getMinY();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = image.getRGB(i, j);
                r = (pixel & 0xff0000) >> 16;
                g = (pixel & 0xff00) >> 8;
                b = (pixel & 0xff);
                if (matchFilters(r, g, b, rgbFilters)) {
                    continue;
                }
                countMap.merge(r + "-" + g + "-" + b, 1L, Long::sum);
            }
        }
        String maxColor = null;
        long maxCount = 0;
        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            String key = entry.getKey();
            Long count = entry.getValue();
            if (count > maxCount) {
                maxColor = key;
                maxCount = count;
            }
        }
        final String[] splitRgbStr = StrUtil.splitToArray(maxColor, '-');
        String rHex = Integer.toHexString(Integer.parseInt(splitRgbStr[0]));
        String gHex = Integer.toHexString(Integer.parseInt(splitRgbStr[1]));
        String bHex = Integer.toHexString(Integer.parseInt(splitRgbStr[2]));
        rHex = rHex.length() == 1 ? "0" + rHex : rHex;
        gHex = gHex.length() == 1 ? "0" + gHex : gHex;
        bHex = bHex.length() == 1 ? "0" + bHex : bHex;
        return "#" + rHex + gHex + bHex;
    }

    /**
     * 给定RGB是否匹配过滤器中任何一个RGB颜色.
     *
     * @param r          R
     * @param g          G
     * @param b          B
     * @param rgbFilters 颜色过滤器
     * @return 是否匹配
     */
    private static boolean matchFilters(final int r, final int g, final int b, final int[]... rgbFilters) {
        if (ArrayUtil.isNotEmpty(rgbFilters)) {
            for (int[] rgbFilter : rgbFilters) {
                if (r == rgbFilter[0] && g == rgbFilter[1] && b == rgbFilter[2]) {
                    return true;
                }
            }
        }
        return false;
    }

    // --------------------------------------- Static method end
}
