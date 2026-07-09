package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.text.StrBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化；builder() 为简单工厂（Factory Method）创建 StrBuilder.
 * @author hongzhe.qin
 */
public final class StrUtil {

    /**
     * 空字符串.
     */
    public static final String EMPTY = "";

    /**
     * 表示 null 的字符串字面量.
     */
    public static final String NULL = "null";

    /**
     * 私有构造，禁止实例化.
     */
    private StrUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 判断字符串是否为空白.
     *
     * @param str 字符串
     * @return 是否为空白
     */
    public static boolean isBlank(final CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否非空白.
     *
     * @param str 字符串
     * @return 是否非空白
     */
    public static boolean isNotBlank(final CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 忽略大小写比较两个字符串是否相等.
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 是否相等
     */
    public static boolean equalsIgnoreCase(final CharSequence s1, final CharSequence s2) {
        if (s1 == null || s2 == null) {
            return s1 == s2;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        return s1.toString().equalsIgnoreCase(s2.toString());
    }

    /**
     * 移除字符串中所有空白字符.
     *
     * @param str 原字符串
     * @return 清理后的字符串
     */
    public static String cleanBlank(final CharSequence str) {
        if (str == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 移除字符串后缀.
     *
     * @param str    原字符串
     * @param suffix 后缀
     * @return 处理后的字符串
     */
    public static String removeSuffix(final CharSequence str, final CharSequence suffix) {
        if (str == null || suffix == null) {
            return str == null ? null : str.toString();
        }
        final String s = str.toString();
        final String suf = suffix.toString();
        return s.endsWith(suf) ? s.substring(0, s.length() - suf.length()) : s;
    }

    /**
     * 在字符串后填充至指定长度.
     *
     * @param str     原字符串
     * @param length  目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String padAfter(final CharSequence str, final int length, final char padChar) {
        if (str == null) {
            return null;
        }
        final String s = str.toString();
        if (s.length() >= length) {
            return s;
        }
        final StringBuilder sb = new StringBuilder(length);
        sb.append(s);
        while (sb.length() < length) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 重复字符生成字符串.
     *
     * @param c     字符
     * @param count 重复次数
     * @return 结果字符串
     */
    public static String repeat(final char c, final int count) {
        if (count <= 0) {
            return EMPTY;
        }
        final char[] arr = new char[count];
        java.util.Arrays.fill(arr, c);
        return new String(arr);
    }

    /**
     * 创建 {@link StrBuilder}.
     *
     * @return 字符串构建器
     */
    public static StrBuilder builder() {
        return new StrBuilder();
    }

    /**
     * 创建 JDK {@link StringBuilder}（供 UUID 等需要 StringBuilder 的场景）.
     *
     * @param capacity 初始容量
     * @return StringBuilder
     */
    public static StringBuilder stringBuilder(final int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 判断字符串是否为空（null 或长度为 0）.
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * {@link CharSequence} 转字符串.
     *
     * @param cs 字符序列
     * @return 字符串，cs 为 null 时返回 null
     */
    public static String str(final CharSequence cs) {
        return cs == null ? null : cs.toString();
    }

    /**
     * 将 byte 数组转为字符串.
     *
     * @param bytes   字节数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(final byte[] bytes, final Charset charset) {
        if (bytes == null) {
            return null;
        }
        if (charset == null) {
            return new String(bytes);
        }
        return new String(bytes, charset);
    }

    /**
     * 将 char 数组转为字符串.
     *
     * @param chars 字符数组
     * @return 字符串
     */
    public static String str(final char[] chars) {
        if (chars == null) {
            return null;
        }
        return new String(chars);
    }

    /**
     * 编码为 UTF-8 字节数组.
     *
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] utf8Bytes(final CharSequence str) {
        return bytes(str, StandardCharsets.UTF_8);
    }

    /**
     * 按字符集编码字符串.
     *
     * @param str     字符串
     * @param charset 字符集
     * @return 字节数组
     */
    public static byte[] bytes(final CharSequence str, final Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    /**
     * 按字符集名称编码字符串.
     *
     * @param str         字符串
     * @param charsetName 字符集名称
     * @return 字节数组
     */
    public static byte[] bytes(final CharSequence str, final String charsetName) {
        return bytes(str, isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName));
    }

    /**
     * 格式化文本，占位符为 {@code {}}.
     *
     * @param template 模板
     * @param params   参数
     * @return 格式化后的文本
     */
    public static String format(final CharSequence template, final Object... params) {
        if (template == null) {
            return null;
        }
        if (params == null || params.length == 0) {
            return template.toString();
        }
        final String text = template.toString();
        final StringBuilder sb = new StringBuilder(text.length() + 16);
        int paramIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '{' && i + 1 < text.length() && text.charAt(i + 1) == '}') {
                if (paramIndex < params.length) {
                    sb.append(params[paramIndex++]);
                }
                i++;
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 按分隔符切分字符串.
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 切分结果
     */
    public static List<String> split(final CharSequence str, final char separator) {
        if (str == null) {
            return null;
        }
        final List<String> list = new ArrayList<>();
        int start = 0;
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == separator) {
                list.add(str.subSequence(start, i).toString());
                start = i + 1;
            }
        }
        list.add(str.subSequence(start, len).toString());
        return list;
    }

    /**
     * 是否以指定前缀开头且两者不完全相等（忽略大小写比较相等性）.
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 是否满足条件
     */
    public static boolean startWithIgnoreEquals(final CharSequence str, final CharSequence prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        final String s = str.toString();
        final String p = prefix.toString();
        if (!s.regionMatches(false, 0, p, 0, p.length())) {
            return false;
        }
        return !s.equalsIgnoreCase(p);
    }

    /**
     * 忽略大小写去掉前缀.
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 去掉前缀后的字符串
     */
    public static String removePrefixIgnoreCase(final CharSequence str, final CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str == null ? null : str.toString();
        }
        final String s = str.toString();
        final String p = prefix.toString();
        if (s.regionMatches(true, 0, p, 0, p.length())) {
            return s.substring(p.length());
        }
        return s;
    }

    /**
     * 字符串是否仅包含给定字符.
     *
     * @param str       字符串
     * @param testChars 允许的字符
     * @return 是否仅包含给定字符
     */
    public static boolean containsOnly(final CharSequence str, final char... testChars) {
        if (!isEmpty(str)) {
            final int len = str.length();
            for (int i = 0; i < len; i++) {
                if (!ArrayUtil.contains(testChars, str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否以指定前缀开头（忽略大小写）.
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 是否匹配
     */
    public static boolean startWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        final String s = str.toString();
        final String p = prefix.toString();
        return s.regionMatches(true, 0, p, 0, p.length()) && s.length() >= p.length();
    }

    /**
     * 是否与任一字符串相等（忽略大小写）.
     *
     * @param str  字符串
     * @param strs 候选
     * @return 是否匹配
     */
    public static boolean equalsAnyIgnoreCase(final CharSequence str, final CharSequence... strs) {
        if (str == null || strs == null) {
            return false;
        }
        for (CharSequence candidate : strs) {
            if (equalsIgnoreCase(str, candidate)) {
                return true;
            }
        }
        return false;
    }

    // --------------------------------------- Static method end
}
