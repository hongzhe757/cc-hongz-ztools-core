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
}
