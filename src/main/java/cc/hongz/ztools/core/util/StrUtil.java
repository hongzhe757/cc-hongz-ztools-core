package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.comparator.VersionComparator;
import cc.hongz.ztools.core.comparator.VersionComparator;
import cc.hongz.ztools.core.text.StrBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.StringTokenizer;

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
     * 点号字符串 {@code "."}.
     */
    public static final String DOT = ".";

    /**
     * 斜杠字符串 {@code "/"}.
     */
    public static final String SLASH = "/";

    /**
     * 点号字符 {@code '.'}.
     */
    public static final char C_DOT = '.';

    /**
     * 斜杠字符 {@code '/'}.
     */
    public static final char C_SLASH = '/';

    /**
     * 空格字符串 {@code " "}.
     */
    public static final String SPACE = " ";

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
     * 判断字符串是否非空（非 null 且长度大于 0）.
     *
     * @param str 字符串
     * @return 是否非空
     */
    public static boolean isNotEmpty(final CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 去除字符串首尾空白.
     *
     * @param str 字符串
     * @return 去除空白后的字符串，str 为 null 时返回 null
     */
    public static String trim(final CharSequence str) {
        return str == null ? null : str.toString().trim();
    }

    /**
     * 如果字符串不是以 prefix 开头，则添加前缀.
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 添加前缀后的字符串
     */
    public static String addPrefixIfNot(final CharSequence str, final CharSequence prefix) {
        if (str == null || prefix == null) {
            return str == null ? null : str.toString();
        }
        final String s = str.toString();
        final String p = prefix.toString();
        if (s.startsWith(p)) {
            return s;
        }
        return p + s;
    }

    /**
     * 截取字符串从开头到指定索引（不含）.
     *
     * @param str     字符串
     * @param toIndex 截止索引（不含）
     * @return 截取后的字符串
     */
    public static String subPre(final CharSequence str, final int toIndex) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (toIndex <= 0) {
            return EMPTY;
        }
        if (toIndex >= len) {
            return str.toString();
        }
        return str.subSequence(0, toIndex).toString();
    }

    /**
     * 截取字符串从指定索引到结尾.
     *
     * @param str       字符串
     * @param fromIndex 起始索引（含）
     * @return 截取后的字符串
     */
    public static String subSuf(final CharSequence str, final int fromIndex) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (fromIndex <= 0) {
            return str.toString();
        }
        if (fromIndex >= len) {
            return EMPTY;
        }
        return str.subSequence(fromIndex, len).toString();
    }

    /**
     * 查找字符在字符串中首次出现的索引.
     *
     * @param str        字符串
     * @param searchChar 查找字符
     * @return 索引，未找到返回 -1
     */
    public static int indexOf(final CharSequence str, final char searchChar) {
        if (str == null) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 比较两个字符串是否相等.
     *
     * @param str1       字符串1
     * @param str2       字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 是否相等
     */
    public static boolean equals(final CharSequence str1, final CharSequence str2, final boolean ignoreCase) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        }
        return str1.toString().equals(str2.toString());
    }

    /**
     * 将 null 转为空字符串.
     *
     * @param str 字符串
     * @return 非空字符串
     */
    public static String nullToEmpty(final CharSequence str) {
        return str == null ? EMPTY : str.toString();
    }

    /**
     * 如果字符串为 {@code null}，则返回默认字符串，否则返回字符串本身.
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(final CharSequence str, final String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    /**
     * 去掉字符串前缀.
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 去掉前缀后的字符串
     */
    public static String removePrefix(final CharSequence str, final CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str == null ? null : str.toString();
        }
        final String s = str.toString();
        final String p = prefix.toString();
        if (s.startsWith(p)) {
            return s.substring(p.length());
        }
        return s;
    }

    /**
     * 如果字符串不是以 suffix 结尾，则添加后缀.
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 添加后缀后的字符串
     */
    public static String addSuffixIfNot(final CharSequence str, final CharSequence suffix) {
        if (str == null || suffix == null) {
            return str == null ? null : str.toString();
        }
        final String s = str.toString();
        final String suf = suffix.toString();
        if (s.endsWith(suf)) {
            return s;
        }
        return s + suf;
    }

    /**
     * 截取指定分隔符之前的字符串.
     *
     * @param string          字符串
     * @param separator       分隔符
     * @param isLastSeparator 是否从最后一个分隔符处截取
     * @return 分隔符之前的字符串
     */
    public static String subBefore(final CharSequence string, final CharSequence separator, final boolean isLastSeparator) {
        if (isEmpty(string) || separator == null) {
            return string == null ? null : string.toString();
        }
        final String str = string.toString();
        final String sep = separator.toString();
        if (sep.isEmpty()) {
            return EMPTY;
        }
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == -1) {
            return str;
        }
        if (pos == 0) {
            return EMPTY;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取指定分隔符之前的字符串.
     *
     * @param string          字符串
     * @param separator       分隔符字符
     * @param isLastSeparator 是否从最后一个分隔符处截取
     * @return 分隔符之前的字符串
     */
    public static String subBefore(final CharSequence string, final char separator, final boolean isLastSeparator) {
        if (isEmpty(string)) {
            return string == null ? null : EMPTY;
        }
        final String str = string.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        if (pos == 0) {
            return EMPTY;
        }
        return str.substring(0, pos);
    }

    /**
     * 字符串是否包含指定字符.
     *
     * @param str       字符串
     * @param searchChar 查找字符
     * @return 是否包含
     */
    public static boolean contains(final CharSequence str, final char searchChar) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == searchChar) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定字符串是否在字符串中出现过.
     *
     * @param str       字符串
     * @param searchStr 被查找的字符串
     * @return 是否包含
     */
    public static boolean contains(final CharSequence str, final CharSequence searchStr) {
        if (null == str || null == searchStr) {
            return false;
        }
        return str.toString().contains(searchStr);
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

    /**
     * 判断字符串是否以指定字符开头.
     *
     * @param str    字符串
     * @param prefix 前缀字符
     * @return 是否以指定字符开头
     */
    public static boolean startWith(final CharSequence str, final char prefix) {
        return str != null && str.length() > 0 && str.charAt(0) == prefix;
    }

    /**
     * 获取字符串长度，如果为 null 则返回 0.
     *
     * @param str 字符串
     * @return 字符串长度
     */
    public static int length(final CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 替换字符串中的指定字符序列.
     *
     * @param str        原字符串
     * @param searchFor  被替换的字符序列
     * @param replaceWith 替换的字符序列
     * @return 替换后的字符串
     */
    public static String replace(final CharSequence str, final CharSequence searchFor, final CharSequence replaceWith) {
        if (str == null || searchFor == null) {
            return str == null ? null : str.toString();
        }
        return str.toString().replace(searchFor, replaceWith);
    }

    /**
     * 隐藏字符串中指定区间的字符为“*”.
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @return 隐藏后的字符串
     */
    public static String hide(final CharSequence str, final int startInclude, final int endExclude) {
        if (str == null) {
            return null;
        }
        final String s = str.toString();
        final int len = s.length();
        final int start = Math.max(0, startInclude);
        final int end = Math.min(len, endExclude);
        if (start >= end || start >= len) {
            return s;
        }
        final StringBuilder sb = new StringBuilder(len);
        sb.append(s, 0, start);
        for (int i = start; i < end; i++) {
            sb.append('*');
        }
        sb.append(s, end, len);
        return sb.toString();
    }

    /**
     * 创建 JDK {@link StringBuilder}.
     *
     * @return StringBuilder
     */
    public static StringBuilder stringBuilder() {
        return new StringBuilder();
    }

    /**
     * 移除字符串中所有指定字符.
     *
     * @param str   原字符串
     * @param chars 要移除的字符
     * @return 移除后的字符串
     */
    public static String removeAll(final CharSequence str, final char... chars) {
        if (str == null || chars == null || chars.length == 0) {
            return str == null ? null : str.toString();
        }
        final int len = str.length();
        if (len == 0) {
            return str.toString();
        }
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            final char c = str.charAt(i);
            if (!ArrayUtil.contains(chars, c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 重复字符串并用分隔符连接.
     *
     * <pre>
     * StrUtil.repeatAndJoin("?", 5, ",")  = "?,?,?,?,?"
     * StrUtil.repeatAndJoin("?", 1, ",")  = "?"
     * StrUtil.repeatAndJoin("?", 0, ",")  = ""
     * StrUtil.repeatAndJoin("?", 5, null) = "?????"
     * </pre>
     *
     * @param str       被重复的字符串
     * @param count     数量
     * @param delimiter 分界符
     * @return 连接后的字符串
     */
    public static String repeatAndJoin(final CharSequence str, final int count, final CharSequence delimiter) {
        if (count <= 0) {
            return EMPTY;
        }
        final StringBuilder builder = new StringBuilder(str.length() * count);
        builder.append(str);
        int remaining = count - 1;

        final boolean isAppendDelimiter = isNotEmpty(delimiter);
        while (remaining-- > 0) {
            if (isAppendDelimiter) {
                builder.append(delimiter);
            }
            builder.append(str);
        }
        return builder.toString();
    }

    /**
     * 如果字符串为空白，则返回默认字符串，否则返回字符串本身.
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String blankToDefault(final CharSequence str, final String defaultStr) {
        return isBlank(str) ? defaultStr : str.toString();
    }

    /**
     * 使用字符串分隔符分割字符串.
     *
     * @param str            被分割的字符串
     * @param separator      分隔符
     * @param isTrim         是否去除每个元素的空白
     * @param ignoreEmpty    是否忽略空元素
     * @return 分割后的列表
     */
    public static List<String> split(final CharSequence str, final String separator,
                                     final boolean isTrim, final boolean ignoreEmpty) {
        if (str == null) {
            return null;
        }
        final String s = str.toString();
        if (separator == null || separator.isEmpty()) {
            final List<String> list = new ArrayList<>(1);
            list.add(s);
            return list;
        }
        final List<String> result = new ArrayList<>();
        final StringTokenizer tokenizer = new StringTokenizer(s, separator, false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (isTrim) {
                token = token.trim();
            }
            if (ignoreEmpty && token.isEmpty()) {
                continue;
            }
            result.add(token);
        }
        return result;
    }

    /**
     * 比较两个版本号的大小.
     *
     * @param version1 版本1
     * @param version2 版本2
     * @return 版本1大于版本2返回正数，等于返回0，小于返回负数
     */
    public static int compareVersion(final String version1, final String version2) {
        return VersionComparator.INSTANCE.compare(version1, version2);
    }

    /**
     * 将对象转为字符串.
     *
     * @param obj 对象
     * @return 字符串，null 返回 {@code "null"}
     * @see String#valueOf(Object)
     */
    public static String toString(final Object obj) {
        return String.valueOf(obj);
    }

    // --------------------------------------- Static method end
}
