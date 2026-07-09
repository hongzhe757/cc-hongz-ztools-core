package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * HTML4 基础特殊字符转义（不含扩展命名实体表）.
 *
 * <p>
 * 与 XML 相比，HTML4 不转义单引号 {@code '}.
 *
 * @author hongzhe.qin
 */
public final class HtmlEscape {

    /**
     * 私有构造，禁止实例化.
     */
    private HtmlEscape() {
    }

    // --------------------------------------- Static method start

    /**
     * 转义 HTML4 基础特殊字符.
     *
     * @param html 原文
     * @return 转义后文本，html 为 null 时返回 null
     */
    public static String escape(final CharSequence html) {
        if (html == null) {
            return null;
        }
        if (StrUtil.isEmpty(html)) {
            return html.toString();
        }
        final String s = html.toString();
        final StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            XmlEscape.appendEscaped(sb, s.charAt(i), false);
        }
        return sb.toString();
    }

    /**
     * 反转义 HTML 基础实体.
     *
     * @param html 转义文本
     * @return 原文，html 为 null 时返回 null
     */
    public static String unescape(final CharSequence html) {
        return XmlEscape.unescape(html);
    }

    // --------------------------------------- Static method end
}
