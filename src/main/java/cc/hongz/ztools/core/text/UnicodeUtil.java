package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.HexUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 提供Unicode字符串和普通字符串之间的转换。
 *
 * @author 兜兜毛毛, looly
 * @since 4.0.0
 */
public final class UnicodeUtil {

    /**
     * Unicode转义前缀 {@code \\u} 的长度。
     */
    private static final int UNICODE_PREFIX_LEN = 2;
    /**
     * Unicode十六进制编码长度，如 {@code XXXX}。
     */
    private static final int UNICODE_HEX_LEN = 4;
    /**
     * 完整Unicode转义序列长度，即 {@code \\uXXXX} 为6个字符。
     */
    private static final int UNICODE_ESCAPE_LEN = UNICODE_PREFIX_LEN + UNICODE_HEX_LEN;
    /**
     * 十六进制基数。
     */
    private static final int HEX_RADIX = 16;

    /**
     * 私有构造，防止实例化工具类。
     */
    private UnicodeUtil() {
    }

    /**
     * Unicode字符串转为普通字符串。
     * <br>
     * Unicode字符串的表现方式为：\\uXXXX
     *
     * @param unicode Unicode字符串
     * @return 普通字符串
     */
    public static String toString(final String unicode) {
        if (StrUtil.isBlank(unicode)) {
            return unicode;
        }

        final int len = unicode.length();
        StringBuilder sb = new StringBuilder(len);
        int i;
        int pos = 0;
        while ((i = StrUtil.indexOfIgnoreCase(unicode, "\\u", pos)) != -1) {
            sb.append(unicode, pos, i); // 写入Unicode符之前的部分
            pos = i;
            if (i + UNICODE_ESCAPE_LEN <= len) {
                char c;
                try {
                    c = (char) Integer.parseInt(
                        unicode.substring(i + UNICODE_PREFIX_LEN, i + UNICODE_ESCAPE_LEN),
                        HEX_RADIX);
                    sb.append(c);
                    pos = i + UNICODE_ESCAPE_LEN; // 跳过整个Unicode符
                } catch (NumberFormatException e) {
                    // 非法Unicode符，跳过
                    sb.append(unicode, pos, i + UNICODE_PREFIX_LEN); // 写入"\\u"
                    pos = i + UNICODE_PREFIX_LEN;
                }
            } else {
                // 非Unicode符，结束
                break;
            }
        }

        if (pos < len) {
            sb.append(unicode, pos, len);
        }
        return sb.toString();
    }

    /**
     * 字符编码为Unicode形式。
     *
     * @param c 被编码的字符
     * @return Unicode字符串
     * @since 5.6.2
     * @see HexUtil#toUnicodeHex(char)
     */
    public static String toUnicode(final char c) {
        return HexUtil.toUnicodeHex(c);
    }

    /**
     * 字符编码为Unicode形式。
     *
     * @param c 被编码的字符
     * @return Unicode字符串
     * @since 5.6.2
     * @see HexUtil#toUnicodeHex(int)
     */
    public static String toUnicode(final int c) {
        return HexUtil.toUnicodeHex(c);
    }

    /**
     * 字符串编码为Unicode形式。
     *
     * @param str 被编码的字符串
     * @return Unicode字符串
     */
    public static String toUnicode(final String str) {
        return toUnicode(str, true);
    }

    /**
     * 字符串编码为Unicode形式。
     *
     * @param str         被编码的字符串
     * @param isSkipAscii 是否跳过ASCII字符（只跳过可见字符）
     * @return Unicode字符串
     */
    public static String toUnicode(final String str, final boolean isSkipAscii) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }

        final int len = str.length();
        final StringBuilder unicode = new StringBuilder(str.length() * UNICODE_ESCAPE_LEN);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (isSkipAscii && CharUtil.isAsciiPrintable(c)) {
                unicode.append(c);
            } else {
                unicode.append(HexUtil.toUnicodeHex(c));
            }
        }
        return unicode.toString();
    }
}
