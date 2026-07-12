package cc.hongz.ztools.core.util;

/**
 * 字符工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，封装字符比较与常量.
 * @author hongzhe.qin
 */
public final class CharUtil {

    /**
     * 空格字符.
     */
    public static final char SPACE = ' ';

    /**
     * 点号 {@code '.'}.
     */
    public static final char DOT = '.';

    /**
     * 斜杠 {@code '/'}.
     */
    public static final char SLASH = '/';

    /**
     * 连字符 {@code '-'}.
     */
    public static final char DASHED = '-';

    /**
     * 私有构造，禁止实例化.
     */
    private CharUtil() {
    }

    /**
     * 判断字符是否为空白字符（空格、制表符、换行等）.
     *
     * @param c 字符
     * @return 是否为空白字符
     */
    public static boolean isBlankChar(final char c) {
        return Character.isWhitespace(c) || c == '\u00A0' || c == '\uFEFF'
                || c == '\u2007' || c == '\u202F';
    }

    /**
     * 比较两个字符是否相等.
     *
     * @param c1              字符1
     * @param c2              字符2
     * @param caseInsensitive 是否忽略大小写
     * @return 是否相等
     */
    public static boolean equals(final char c1, final char c2, final boolean caseInsensitive) {
        if (caseInsensitive) {
            return Character.toLowerCase(c1) == Character.toLowerCase(c2);
        }
        return c1 == c2;
    }

    /**
     * 判断字符是否为数字（0-9）.
     *
     * @param ch 字符
     * @return 是否为数字
     */
    public static boolean isNumber(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 判断字符是否为十六进制字符（0-9, a-f, A-F）.
     *
     * @param c 字符
     * @return 是否为十六进制字符
     */
    public static boolean isHexChar(final char c) {
        return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * 将字符转换为十六进制数值.
     *
     * @param b 字符的 int 值
     * @return 十六进制数值（0-15），非法字符返回 -1
     */
    public static int digit16(final int b) {
        return Character.digit(b, 16);
    }
}
