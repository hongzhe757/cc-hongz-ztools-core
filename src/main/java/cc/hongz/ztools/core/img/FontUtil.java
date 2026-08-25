package cc.hongz.ztools.core.img;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.IORuntimeException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * AWT 字体工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，字体度量辅助.
 * @author hongzhe.qin
 */
public final class FontUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private FontUtil() {
    }

    /**
     * 创建默认字体.
     *
     * @return 默认字体
     */
    public static Font createFont() {
        return new Font(null);
    }

    /**
     * 创建SansSerif字体.
     *
     * @param size 字体大小
     * @return 字体
     */
    public static Font createSansSerifFont(final int size) {
        return createFont(Font.SANS_SERIF, size);
    }

    /**
     * 创建指定名称的字体.
     *
     * @param name 字体名称
     * @param size 字体大小
     * @return 字体
     */
    public static Font createFont(final String name, final int size) {
        return new Font(name, Font.PLAIN, size);
    }

    /**
     * 根据文件创建字体.
     * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
     *
     * @param fontFile 字体文件
     * @return {@link Font}
     */
    public static Font createFont(final File fontFile) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException e) {
            try {
                return Font.createFont(Font.TYPE1_FONT, fontFile);
            } catch (Exception e1) {
                throw new UtilException(e);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 根据文件创建字体.
     * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
     *
     * @param fontStream 字体流
     * @return {@link Font}
     */
    public static Font createFont(final InputStream fontStream) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (FontFormatException e) {
            try {
                return Font.createFont(Font.TYPE1_FONT, fontStream);
            } catch (Exception e1) {
                throw new UtilException(e1);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得字体对应字符串的长宽信息.
     *
     * @param metrics 字体度量
     * @param str     字符串
     * @return 宽高
     */
    public static Dimension getDimension(final FontMetrics metrics, final String str) {
        final int width = metrics.stringWidth(str);
        final int height = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();
        return new Dimension(width, height);
    }
}
