package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 命名规则封装，主要是针对驼峰风格命名、连接符命名等的封装.
 *
 * @author looly
 * @since 5.7.10
 */
public final class NamingCase {

    private NamingCase() {
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式.
     *
     * @param str 字符串
     * @return 下划线方式字符串
     */
    public static String toUnderlineCase(final CharSequence str) {
        return toSymbolCase(str, CharUtil.UNDERLINE);
    }

    /**
     * 将驼峰式命名的字符串转换为短横连接方式.
     *
     * @param str 字符串
     * @return 短横连接方式字符串
     */
    public static String toKebabCase(final CharSequence str) {
        return toSymbolCase(str, CharUtil.DASHED);
    }

    /**
     * 将驼峰式命名的字符串转换为指定连接符方式.
     *
     * @param str    字符串
     * @param symbol 连接符
     * @return 连接符方式字符串
     */
    public static String toSymbolCase(final CharSequence str, final char symbol) {
        if (str == null) {
            return null;
        }

        final int length = str.length();
        final StrBuilder sb = new StrBuilder();
        char c;
        for (int i = 0; i < length; i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
                final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;

                if (null != preChar) {
                    if (symbol == preChar) {
                        if (null == nextChar || Character.isLowerCase(nextChar)) {
                            c = Character.toLowerCase(c);
                        }
                    } else if (Character.isLowerCase(preChar)) {
                        sb.append(symbol);
                        if (null == nextChar || Character.isLowerCase(nextChar) || CharUtil.isNumber(nextChar)) {
                            c = Character.toLowerCase(c);
                        }
                    } else {
                        if (null != nextChar && Character.isLowerCase(nextChar)) {
                            sb.append(symbol);
                            c = Character.toLowerCase(c);
                        }
                    }
                } else {
                    if (null == nextChar || Character.isLowerCase(nextChar)) {
                        c = Character.toLowerCase(c);
                    }
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 将下划线方式命名的字符串转换为帕斯卡式.
     *
     * @param name 字符串
     * @return 帕斯卡方式字符串
     */
    public static String toPascalCase(final CharSequence name) {
        return StrUtil.upperFirst(toCamelCase(name));
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式.
     *
     * @param name 字符串
     * @return 驼峰式字符串
     */
    public static String toCamelCase(final CharSequence name) {
        return toCamelCase(name, CharUtil.UNDERLINE);
    }

    /**
     * 将指定连接符方式命名的字符串转换为驼峰式.
     *
     * @param name   字符串
     * @param symbol 连接符
     * @return 驼峰式字符串
     */
    public static String toCamelCase(final CharSequence name, final char symbol) {
        return toCamelCase(name, symbol, true);
    }

    /**
     * 将指定连接符方式命名的字符串转换为驼峰式.
     *
     * @param name             字符串
     * @param symbol           连接符
     * @param otherCharToLower 其他字符是否转换为小写
     * @return 驼峰式字符串
     */
    public static String toCamelCase(final CharSequence name, final char symbol, final boolean otherCharToLower) {
        if (null == name) {
            return null;
        }

        final String name2 = name.toString();
        if (StrUtil.contains(name2, symbol)) {
            final int length = name2.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);

                if (c == symbol) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(otherCharToLower ? Character.toLowerCase(c) : c);
                }
            }
            return sb.toString();
        } else {
            return name2;
        }
    }
}
