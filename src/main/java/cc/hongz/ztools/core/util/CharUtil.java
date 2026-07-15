package cc.hongz.ztools.core.util;

/**
 * 字符工具类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.CharUtil}，
 * 合并了 {@code CharPool} 常量和 {@code ASCIIStrCache} 缓存能力。
 * 部分工具来自于 Apache Commons 系列。
 *
 * @author looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 * @since 4.0.1
 */
public final class CharUtil {

    // --------------------------------------- 字符常量（来自 CharPool）

    /**
     * 空格字符 {@code ' '}.
     */
    public static final char SPACE = ' ';

    /**
     * 制表符 {@code '\t'}.
     */
    public static final char TAB = '\t';

    /**
     * 点号 {@code '.'}.
     */
    public static final char DOT = '.';

    /**
     * 斜杠 {@code '/'}.
     */
    public static final char SLASH = '/';

    /**
     * 反斜杠 {@code '\\'}.
     */
    public static final char BACKSLASH = '\\';

    /**
     * 回车符 {@code '\r'}.
     */
    public static final char CR = '\r';

    /**
     * 换行符 {@code '\n'}.
     */
    public static final char LF = '\n';

    /**
     * 连字符（减号） {@code '-'}.
     */
    public static final char DASHED = '-';

    /**
     * 下划线 {@code '_'}.
     */
    public static final char UNDERLINE = '_';

    /**
     * 逗号 {@code ','}.
     */
    public static final char COMMA = ',';

    /**
     * 花括号（左） {@code '{'}.
     */
    public static final char DELIM_START = '{';

    /**
     * 花括号（右） {@code '}'}.
     */
    public static final char DELIM_END = '}';

    /**
     * 中括号（左） {@code '['}.
     */
    public static final char BRACKET_START = '[';

    /**
     * 中括号（右） {@code ']'}.
     */
    public static final char BRACKET_END = ']';

    /**
     * 双引号 {@code '"'}.
     */
    public static final char DOUBLE_QUOTES = '"';

    /**
     * 单引号 {@code '\''}.
     */
    public static final char SINGLE_QUOTE = '\'';

    /**
     * 与符号 {@code '&'}.
     */
    public static final char AMP = '&';

    /**
     * 冒号 {@code ':'}.
     */
    public static final char COLON = ':';

    /**
     * 艾特 {@code '@'}.
     */
    public static final char AT = '@';

    // --------------------------------------- ASCII 字符串缓存

    /**
     * ASCII 字符缓存长度.
     */
    private static final int ASCII_LENGTH = 128;

    /**
     * ASCII 字符转字符串缓存.
     */
    private static final String[] ASCII_CACHE = new String[ASCII_LENGTH];

    static {
        for (char c = 0; c < ASCII_LENGTH; c++) {
            ASCII_CACHE[c] = String.valueOf(c);
        }
    }

    /**
     * 私有构造，禁止实例化.
     */
    private CharUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 是否为 ASCII 字符，ASCII 字符位于 0~127 之间.
     *
     * <pre>
     *   CharUtil.isAscii('a')  = true
     *   CharUtil.isAscii('A')  = true
     *   CharUtil.isAscii('3')  = true
     *   CharUtil.isAscii('-')  = true
     *   CharUtil.isAscii('\n') = true
     *   CharUtil.isAscii('\u00A9') = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为 ASCII 字符
     */
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    /**
     * 是否为可见 ASCII 字符，可见字符位于 32~126 之间.
     *
     * <pre>
     *   CharUtil.isAsciiPrintable('a')  = true
     *   CharUtil.isAsciiPrintable('A')  = true
     *   CharUtil.isAsciiPrintable('3')  = true
     *   CharUtil.isAsciiPrintable('-')  = true
     *   CharUtil.isAsciiPrintable('\n') = false
     *   CharUtil.isAsciiPrintable('\u00A9') = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为 ASCII 可见字符
     */
    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * 是否为 ASCII 控制符（不可见字符），控制符位于 0~31 和 127.
     *
     * <pre>
     *   CharUtil.isAsciiControl('a')  = false
     *   CharUtil.isAsciiControl('A')  = false
     *   CharUtil.isAsciiControl('3')  = false
     *   CharUtil.isAsciiControl('-')  = false
     *   CharUtil.isAsciiControl('\n') = true
     *   CharUtil.isAsciiControl('\u00A9') = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为控制符
     */
    public static boolean isAsciiControl(final char ch) {
        return ch < 32 || ch == 127;
    }

    /**
     * 判断是否为字母（包括大写字母和小写字母）.
     * <p>字母包括 A~Z 和 a~z。
     *
     * <pre>
     *   CharUtil.isLetter('a')  = true
     *   CharUtil.isLetter('A')  = true
     *   CharUtil.isLetter('3')  = false
     *   CharUtil.isLetter('-')  = false
     *   CharUtil.isLetter('\n') = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为字母
     */
    public static boolean isLetter(final char ch) {
        return isLetterUpper(ch) || isLetterLower(ch);
    }

    /**
     * 判断是否为大写字母，大写字母包括 A~Z.
     *
     * <pre>
     *   CharUtil.isLetterUpper('a')  = false
     *   CharUtil.isLetterUpper('A')  = true
     *   CharUtil.isLetterUpper('3')  = false
     *   CharUtil.isLetterUpper('-')  = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为大写字母
     */
    public static boolean isLetterUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * 检查字符是否为小写字母，小写字母指 a~z.
     *
     * <pre>
     *   CharUtil.isLetterLower('a')  = true
     *   CharUtil.isLetterLower('A')  = false
     *   CharUtil.isLetterLower('3')  = false
     *   CharUtil.isLetterLower('-')  = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为小写字母
     */
    public static boolean isLetterLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * 检查是否为数字字符，数字字符指 0~9.
     *
     * <pre>
     *   CharUtil.isNumber('a')  = false
     *   CharUtil.isNumber('A')  = false
     *   CharUtil.isNumber('3')  = true
     *   CharUtil.isNumber('-')  = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为数字字符
     */
    public static boolean isNumber(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 是否为 16 进制规范的字符.
     * <pre>
     * 1. 0~9
     * 2. a~f
     * 3. A~F
     * </pre>
     *
     * @param c 字符
     * @return 是否为 16 进制规范的字符
     * @since 4.1.5
     */
    public static boolean isHexChar(final char c) {
        return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * 是否为字母或数字，包括 A~Z、a~z、0~9.
     *
     * <pre>
     *   CharUtil.isLetterOrNumber('a')  = true
     *   CharUtil.isLetterOrNumber('A')  = true
     *   CharUtil.isLetterOrNumber('3')  = true
     *   CharUtil.isLetterOrNumber('-')  = false
     * </pre>
     *
     * @param ch 被检查的字符
     * @return true 表示为字母或数字
     */
    public static boolean isLetterOrNumber(final char ch) {
        return isLetter(ch) || isNumber(ch);
    }

    /**
     * 字符转为字符串.
     * <p>如果为 ASCII 字符，使用缓存以提高性能。
     *
     * @param c 字符
     * @return 字符串
     */
    public static String toString(final char c) {
        return c < ASCII_LENGTH ? ASCII_CACHE[c] : String.valueOf(c);
    }

    /**
     * 给定类名是否为字符类.
     * <p>字符类包括：{@code Character.class} 和 {@code char.class}。
     *
     * @param clazz 被检查的类
     * @return true 表示为字符类
     */
    public static boolean isCharClass(final Class<?> clazz) {
        return clazz == Character.class || clazz == char.class;
    }

    /**
     * 给定对象对应的类是否为字符类.
     * <p>字符类包括：{@code Character.class} 和 {@code char.class}。
     *
     * @param value 被检查的对象
     * @return true 表示为字符类
     */
    public static boolean isChar(final Object value) {
        return value instanceof Character || value.getClass() == char.class;
    }

    /**
     * 是否空白符.
     * <p>空白符包括空格、制表符、全角空格和不间断空格等。
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    public static boolean isBlankChar(final char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否空白符.
     * <p>空白符包括空格、制表符、全角空格和不间断空格等。
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    public static boolean isBlankChar(final int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a'
                || c == '\u0000'
                // Hangul Filler
                || c == '\u3164'
                // Braille Pattern Blank
                || c == '\u2800'
                // Zero Width Non-Joiner (ZWNJ)
                || c == '\u200c'
                // MONGOLIAN VOWEL SEPARATOR
                || c == '\u180e';
    }

    /**
     * 判断是否为 emoji 表情符.
     *
     * @param c 字符
     * @return 是否为 emoji
     * @since 4.0.8
     */
    public static boolean isEmoji(final char c) {
        return !((c == 0x0) ||
                (c == 0x9) ||
                (c == 0xA) ||
                (c == 0xD) ||
                ((c >= 0x20) && (c <= 0xD7FF)) ||
                ((c >= 0xE000) && (c <= 0xFFFD)) ||
                ((c >= 0x100000) && (c <= 0x10FFFF)));
    }

    /**
     * 是否为 Windows 或者 Linux（Unix）文件分隔符.
     * <p>Windows 平台下分隔符为 {@code \}，Linux（Unix）为 {@code /}。
     *
     * @param c 字符
     * @return 是否为文件分隔符
     * @since 4.1.11
     */
    public static boolean isFileSeparator(final char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * 比较两个字符是否相同.
     *
     * @param c1              字符 1
     * @param c2              字符 2
     * @param caseInsensitive 是否忽略大小写
     * @return 是否相同
     * @since 4.0.3
     */
    public static boolean equals(final char c1, final char c2, final boolean caseInsensitive) {
        if (caseInsensitive) {
            return Character.toLowerCase(c1) == Character.toLowerCase(c2);
        }
        return c1 == c2;
    }

    /**
     * 获取字符类型.
     *
     * @param c 字符
     * @return 字符类型
     * @since 5.2.3
     */
    public static int getType(final int c) {
        return Character.getType(c);
    }

    /**
     * 获取给定字符的 16 进制数值.
     *
     * @param b 字符
     * @return 16 进制数值
     * @since 5.3.1
     */
    public static int digit16(final int b) {
        return Character.digit(b, 16);
    }

    /**
     * 将字母、数字转换为带圈的字符.
     * <pre>
     *     '1' → '①'
     *     'A' → 'Ⓐ'
     *     'a' → 'ⓐ'
     * </pre>
     * <p>
     * 如果字符不支持转换，返回原字符。
     *
     * @param c 被转换的字符
     * @return 转换后的字符
     * @see <a href="https://en.wikipedia.org/wiki/Enclosed_Alphanumerics">Enclosed Alphanumerics</a>
     * @since 5.6.2
     */
    public static char toCloseChar(final char c) {
        int result = c;
        if (c >= '1' && c <= '9') {
            result = '\u2460' + c - '1';
        } else if (c >= 'A' && c <= 'Z') {
            result = '\u24B6' + c - 'A';
        } else if (c >= 'a' && c <= 'z') {
            result = '\u24D0' + c - 'a';
        }
        return (char) result;
    }

    /**
     * 将 [1-20] 数字转换为带圈的字符.
     * <pre>
     *     1  → '①'
     *     12 → '⑫'
     *     20 → '⑳'
     * </pre>
     *
     * @param number 被转换的数字，必须在 [1-20] 范围内
     * @return 转换后的字符
     * @throws IllegalArgumentException 如果 number 不在 [1-20] 范围内
     * @since 5.6.2
     */
    public static char toCloseByNumber(final int number) {
        if (number < 1 || number > 20) {
            throw new IllegalArgumentException("Number must be [1-20]");
        }
        return (char) ('\u2460' + number - 1);
    }

    // --------------------------------------- Static method end
}
