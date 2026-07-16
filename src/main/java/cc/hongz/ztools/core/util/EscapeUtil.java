package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.text.escape.Html4Escape;
import cc.hongz.ztools.core.text.escape.Html4Unescape;
import cc.hongz.ztools.core.text.escape.XmlEscape;
import cc.hongz.ztools.core.text.escape.XmlUnescape;

/**
 * 转义和反转义工具类Escape / Unescape.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.EscapeUtil}.
 *
 * @author xiaoleilu
 */
public class EscapeUtil {

    /**
     * 不转义的符号编码.
     */
    private static final String NOT_ESCAPE_CHARS = "*@-_+./";
    private static final Filter<Character> JS_ESCAPE_FILTER = c -> !(
            Character.isDigit(c)
                    || Character.isLowerCase(c)
                    || Character.isUpperCase(c)
                    || StrUtil.contains(NOT_ESCAPE_CHARS, c)
    );

    /**
     * 转义XML中的特殊字符.
     *
     * @param xml XML文本
     * @return 转义后的文本
     */
    public static String escapeXml(CharSequence xml) {
        XmlEscape escape = new XmlEscape();
        return escape.replace(xml).toString();
    }

    /**
     * 反转义XML中的特殊字符.
     *
     * @param xml XML文本
     * @return 转义后的文本
     */
    public static String unescapeXml(CharSequence xml) {
        XmlUnescape unescape = new XmlUnescape();
        return unescape.replace(xml).toString();
    }

    /**
     * 转义HTML4中的特殊字符.
     *
     * @param html HTML文本
     * @return 转义后的文本
     */
    public static String escapeHtml4(CharSequence html) {
        Html4Escape escape = new Html4Escape();
        return escape.replace(html).toString();
    }

    /**
     * 反转义HTML4中的特殊字符.
     *
     * @param html HTML文本
     * @return 转义后的文本
     */
    public static String unescapeHtml4(CharSequence html) {
        Html4Unescape unescape = new Html4Unescape();
        return unescape.replace(html).toString();
    }

    /**
     * Escape编码（Unicode）（等同于JS的escape()方法）.
     * <p>
     * 该方法不会对 ASCII 字母和数字进行编码，也不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . /
     * 其他所有的字符都会被转义序列替换。
     *
     * @param content 被转义的内容
     * @return 编码后的字符串
     */
    public static String escape(CharSequence content) {
        return escape(content, JS_ESCAPE_FILTER);
    }

    /**
     * Escape编码（Unicode）.
     * <p>
     * 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
     *
     * @param content 被转义的内容
     * @return 编码后的字符串
     */
    public static String escapeAll(CharSequence content) {
        return escape(content, c -> true);
    }

    /**
     * Escape编码（Unicode）.
     *
     * @param content 被转义的内容
     * @param filter  编码过滤器，对于过滤器中accept为false的字符不做编码
     * @return 编码后的字符串
     */
    public static String escape(CharSequence content, Filter<Character> filter) {
        if (StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }

        final StringBuilder tmp = new StringBuilder(content.length() * 6);
        char c;
        for (int i = 0; i < content.length(); i++) {
            c = content.charAt(i);
            if (!filter.accept(c)) {
                tmp.append(c);
            } else if (c < 256) {
                tmp.append("%");
                if (c < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            } else {
                tmp.append("%u");
                if (c <= 0xfff) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码.
     *
     * @param content 被转义的内容
     * @return 解码后的字符串
     */
    public static String unescape(final String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }

        final int len = content.length();
        final StringBuilder tmp = new StringBuilder(len);
        int lastPos = 0;
        int pos;
        char ch;
        while (lastPos < len) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (pos + 1 < len && content.charAt(pos + 1) == 'u') {
                    if (pos + 6 <= len) {
                        ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                        tmp.append(ch);
                        lastPos = pos + 6;
                    } else {
                        tmp.append(content.substring(pos));
                        lastPos = len;
                    }
                } else {
                    if (pos + 3 <= len) {
                        ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                        tmp.append(ch);
                        lastPos = pos + 3;
                    } else {
                        tmp.append(content.substring(pos));
                        lastPos = len;
                    }
                }
            } else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = len;
                } else {
                    tmp.append(content, lastPos, pos);
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 安全的unescape文本，当文本不是被escape的时候，返回原文.
     *
     * @param content 内容
     * @return 解码后的字符串，如果解码失败返回原字符串
     */
    public static String safeUnescape(String content) {
        try {
            return unescape(content);
        } catch (Exception e) {
            // Ignore Exception
        }
        return content;
    }
}
