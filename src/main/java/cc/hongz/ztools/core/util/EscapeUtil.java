package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.text.escape.Html4Escape;
import cc.hongz.ztools.core.text.escape.Html4Unescape;
import cc.hongz.ztools.core.text.escape.XmlEscape;
import cc.hongz.ztools.core.text.escape.XmlUnescape;

/**
 * 转义和反转义工具类Escape / Unescape.
 * escape采用ISO Latin字符集对指定的字符串进行编码.<br>
 * 所有的空格符、标点符号、特殊字符以及其他非ASCII字符都将被转化成%xx格式的字符编码
 * (xx等于该字符在字符集表里面的编码的16进制数字).
 * @author xiaoleilu
 */
public final class EscapeUtil {

    /**
     * 十六进制基数.
     */
    private static final int HEX_RADIX = 16;
    /**
     * ASCII单字节最大值.
     */
    private static final int BYTE_MAX = 256;
    /**
     * Unicode低位掩码.
     */
    private static final int UNICODE_LOW_MASK = 0xfff;
    /**
     * StringBuilder最大容量因子.
     */
    private static final int MAX_BUFFER_FACTOR = 6;
    /**
     * 十六进制字节长度.
     */
    private static final int HEX_BYTE_LEN = 2;
    /**
     * 十六进制Unicode编码长度.
     */
    private static final int HEX_UNICODE_LEN = 3;
    /**
     * Unicode转义前缀长度.
     */
    private static final int UNICODE_PREFIX_LEN = 6;

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

    private EscapeUtil() {
    }

    /**
     * 转义XML中的特殊字符.
     * <pre>
     *   &amp;amp; (ampersand) 替换为 &amp;amp;amp;
     *   &amp;lt; (less than) 替换为 &amp;amp;lt;
     *   &amp;gt; (greater than) 替换为 &amp;amp;gt;
     *   &amp;quot; (double quote) 替换为 &amp;amp;quot;
     *   ' (single quote / apostrophe) 替换为 &amp;amp;apos;
     * </pre>
     *
     * @param xml XML文本
     * @return 转义后的文本
     * @since 5.7.2
     */
    public static String escapeXml(final CharSequence xml) {
        XmlEscape escape = new XmlEscape();
        return escape.replace(xml).toString();
    }

    /**
     * 反转义XML中的特殊字符.
     *
     * @param xml XML文本
     * @return 转义后的文本
     * @since 5.7.2
     */
    public static String unescapeXml(final CharSequence xml) {
        XmlUnescape unescape = new XmlUnescape();
        return unescape.replace(xml).toString();
    }

    /**
     * 转义HTML4中的特殊字符.
     *
     * @param html HTML文本
     * @return 转义后的文本
     * @since 4.1.5
     */
    public static String escapeHtml4(final CharSequence html) {
        Html4Escape escape = new Html4Escape();
        return escape.replace(html).toString();
    }

    /**
     * 反转义HTML4中的特殊字符.
     *
     * @param html HTML文本
     * @return 转义后的文本
     * @since 4.1.5
     */
    public static String unescapeHtml4(final CharSequence html) {
        Html4Unescape unescape = new Html4Unescape();
        return unescape.replace(html).toString();
    }

    /**
     * Escape编码（Unicode）（等同于JS的escape()方法）.
     * 该方法不会对 ASCII 字母和数字进行编码，也不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . / <br>
     * 其他所有的字符都会被转义序列替换。
     *
     * @param content 被转义的内容
     * @return 编码后的字符串
     */
    public static String escape(final CharSequence content) {
        return escape(content, JS_ESCAPE_FILTER);
    }

    /**
     * Escape编码（Unicode）.
     * 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
     *
     * @param content 被转义的内容
     * @return 编码后的字符串
     */
    public static String escapeAll(final CharSequence content) {
        return escape(content, c -> true);
    }

    /**
     * Escape编码（Unicode）.
     * 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
     *
     * @param content 被转义的内容
     * @param filter  编码过滤器，对于过滤器中accept为false的字符不做编码
     * @return 编码后的字符串
     */
    public static String escape(final CharSequence content, final Filter<Character> filter) {
        if (StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }

        final StringBuilder tmp = new StringBuilder(content.length() * MAX_BUFFER_FACTOR);
        char c;
        for (int i = 0; i < content.length(); i++) {
            c = content.charAt(i);
            if (!filter.accept(c)) {
                tmp.append(c);
            } else if (c < BYTE_MAX) {
                tmp.append("%");
                if (c < HEX_RADIX) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, HEX_RADIX));
            } else {
                tmp.append("%u");
                if (c <= UNICODE_LOW_MASK) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, HEX_RADIX));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码支持两种转义格式的解码.
     * <ul>
     *     <li>%XX - 两位十六进制数字，用于表示ASCII字符（0-255）</li>
     *     <li>%uXXXX - 四位十六进制数字，用于表示Unicode字符</li>
     * </ul>
     * <p>
     * 对于不完整的转义序列，本方法会将其原样保留而不抛出异常：
     * <ul>
     *     <li>字符串末尾的单独"%"字符会被原样保留</li>
     *     <li>"%u"后面不足4位十六进制数字时，整个不完整序列会被原样保留</li>
     *     <li>"%"后面不足2位十六进制数字时（非%u格式），整个不完整序列会被原样保留</li>
     * </ul>
     * 例如：
     * <pre>
     * unescape("test%")      = "test%"
     * unescape("test%u12")   = "test%u12"
     * unescape("test%2")     = "test%2"
     * unescape("test%20")    = "test "
     * unescape("test%u4E2D") = "test中"
     * </pre>
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
                    if (pos + UNICODE_PREFIX_LEN <= len) {
                        ch = (char) Integer.parseInt(
                                content.substring(pos + HEX_BYTE_LEN, pos + UNICODE_PREFIX_LEN), HEX_RADIX);
                        tmp.append(ch);
                        lastPos = pos + UNICODE_PREFIX_LEN;
                    } else {
                        tmp.append(content.substring(pos));
                        lastPos = len;
                    }
                } else {
                    if (pos + HEX_UNICODE_LEN <= len) {
                        ch = (char) Integer.parseInt(content.substring(pos + 1, pos + HEX_UNICODE_LEN), HEX_RADIX);
                        tmp.append(ch);
                        lastPos = pos + HEX_UNICODE_LEN;
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
    public static String safeUnescape(final String content) {
        try {
            return unescape(content);
        } catch (Exception e) {
            // Ignore Exception
        }
        return content;
    }
}
