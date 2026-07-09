package cc.hongz.ztools.core.img;

import java.awt.Dimension;
import java.awt.FontMetrics;

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
     * 获得字符串在指定 {@link FontMetrics} 下的宽高.
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
