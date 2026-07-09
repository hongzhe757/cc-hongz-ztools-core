package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * XML 特殊字符转义（W3C 实体）.
 *
 * @author hongzhe.qin
 */
public final class XmlEscape {

    /**
     * 私有构造，禁止实例化.
     */
    private XmlEscape() {
    }

    // --------------------------------------- Static method start

    /**
     * 转义 XML 特殊字符.
     *
     * @param text 原文
     * @return 转义后文本，text 为 null 时返回 null
     */
    public static String escape(final CharSequence text) {
        if (text == null) {
            return null;
        }
        if (StrUtil.isEmpty(text)) {
            return text.toString();
        }
        final String s = text.toString();
        final StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            appendEscaped(sb, s.charAt(i), true);
        }
        return sb.toString();
    }

    /**
     * 反转义 XML 实体.
     *
     * @param text 转义文本
     * @return 原文，text 为 null 时返回 null
     */
    public static String unescape(final CharSequence text) {
        if (text == null) {
            return null;
        }
        String s = text.toString();
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", ">");
        s = s.replace("&quot;", "\"");
        s = s.replace("&apos;", "'");
        s = s.replace("&amp;", "&");
        return s;
    }

    // --------------------------------------- Static method end

    static void appendEscaped(final StringBuilder sb, final char c, final boolean escapeApostrophe) {
        switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                if (escapeApostrophe) {
                    sb.append("&apos;");
                } else {
                    sb.append(c);
                }
                break;
            default:
                sb.append(c);
        }
    }
}
