package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.exceptions.UtilException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本对比工具.
 * 对 {@link cc.hongz.ztools.core.comparator.VersionComparator}
 * 的封装，最主要功能包括：
 *
 * <pre>
 * 1. 版本表达式匹配
 * 2. 单个版本匹配
 * </pre>
 *
 * @author winlans
 * @see cc.hongz.ztools.core.comparator.VersionComparator
 */
public final class VersionUtil {

    /**
     * 版本比较运算符正则.
     */
    private static final Pattern COMPARE_REG = Pattern.compile("^[<>≥≤]=?");

    /**
     * 默认多版本分隔符.
     */
    private static final String DEFAULT_VERSIONS_DELIMITER = ";";

    private VersionUtil() {
    }

    /**
     * 是否匹配任意一个版本.
     *
     * @param currentVersion  当前版本
     * @param compareVersions 待匹配的版本列表
     * @return true 包含待匹配的版本
     */
    public static boolean anyMatch(final String currentVersion, final Collection<String> compareVersions) {
        return matchEl(currentVersion, CollUtil.join(compareVersions, DEFAULT_VERSIONS_DELIMITER));
    }

    /**
     * 是否匹配任意一个版本.
     *
     * @param currentVersion  当前版本
     * @param compareVersions 待匹配的版本列表
     * @return true 包含待匹配的版本
     */
    public static boolean anyMatch(final String currentVersion, final String... compareVersions) {
        return matchEl(currentVersion, ArrayUtil.join(compareVersions, DEFAULT_VERSIONS_DELIMITER));
    }

    /**
     * 当前版本大于待比较版本.
     *
     * @param currentVersion 当前版本
     * @param compareVersion 待比较版本
     * @return true  当前版本大于待比较版本
     */
    public static boolean isGreaterThan(final String currentVersion, final String compareVersion) {
        return matchEl(currentVersion, ">" + compareVersion);
    }

    /**
     * 当前版本大于等于待比较版本.
     *
     * @param currentVersion 当前版本
     * @param compareVersion 待比较版本
     * @return true  当前版本大于等于待比较版本
     */
    public static boolean isGreaterThanOrEqual(final String currentVersion, final String compareVersion) {
        return matchEl(currentVersion, ">=" + compareVersion);
    }

    /**
     * 当前版本小于待比较版本.
     *
     * @param currentVersion 当前版本
     * @param compareVersion 待比较版本
     * @return true  当前版本小于待比较版本
     */
    public static boolean isLessThan(final String currentVersion, final String compareVersion) {
        return matchEl(currentVersion, "<" + compareVersion);
    }

    /**
     * 当前版本小于等于待比较版本.
     *
     * @param currentVersion 当前版本
     * @param compareVersion 待比较版本
     * @return true  当前版本小于等于待比较版本
     */
    public static boolean isLessThanOrEqual(final String currentVersion, final String compareVersion) {
        return matchEl(currentVersion, "<=" + compareVersion);
    }

    /**
     * 当前版本是否满足版本表达式.
     * <pre>{@code
     *     matchEl("1.0.2", ">=1.0.2") == true
     *     matchEl("1.0.2", "<1.0.1;1.0.2") == true
     *     matchEl("1.0.2", "<1.0.2") == false
     *     matchEl("1.0.2", "1.0.0-1.1.1") == true
     * }</pre>
     *
     * @param currentVersion 当前版本
     * @param versionEl      版本表达式
     * @return true  当前版本是否满足版本表达式
     */
    public static boolean matchEl(final String currentVersion, final String versionEl) {
        return matchEl(currentVersion, versionEl, DEFAULT_VERSIONS_DELIMITER);
    }

    /**
     * 当前版本是否满足版本表达式.
     * <pre>{@code
     * matchEl("1.0.2", ">=1.0.2", ";") == true
     * matchEl("1.0.2", "<1.0.1,1.0.2", ",") == true
     * matchEl("1.0.2", "<1.0.2", ";") == false
     * matchEl("1.0.2", "1.0.0-1.1.1", ",") == true
     * matchEl("1.0.2", "1.0.1,1.0.2-1.1.1", ",") == true
     * }</pre>
     *
     * @param currentVersion    当前版本
     * @param versionEl         版本表达式
     * @param versionsDelimiter 多表达式分隔符
     * @return true  当前版本是否满足版本表达式
     */
    public static boolean matchEl(final String currentVersion, final String versionEl, final String versionsDelimiter) {
        if (StrUtil.isBlank(versionsDelimiter) || StrUtil.equals("-", versionsDelimiter, false) || ReUtil.isMatch(COMPARE_REG, versionsDelimiter)) {
            throw new UtilException("非法的版本分隔符：" + versionsDelimiter);
        }

        if (StrUtil.isBlank(versionEl)) {
            return false;
        }
        final String trimmedVersion = StrUtil.trim(currentVersion);

        final List<String> els = StrUtil.split(versionEl, versionsDelimiter, true, true);
        if (CollUtil.isEmpty(els)) {
            return false;
        }

        for (final String element : els) {
            final String el = element.trim();
            final Matcher matcher = COMPARE_REG.matcher(el);
            if (matcher.find()) {
                final String op = matcher.group();
                String ver = StrUtil.removePrefix(el, op);
                if ("null".equalsIgnoreCase(ver)) {
                    ver = null;
                }
                switch (op) {
                    case ">=":
                    case "≥":
                        if (StrUtil.compareVersion(trimmedVersion, ver) >= 0) {
                            return true;
                        }
                        break;
                    case "<=":
                    case "≤":
                        if (StrUtil.compareVersion(trimmedVersion, ver) <= 0) {
                            return true;
                        }
                        break;
                    case "<":
                        if (StrUtil.compareVersion(trimmedVersion, ver) < 0) {
                            return true;
                        }
                        break;
                    case ">":
                        if (StrUtil.compareVersion(trimmedVersion, ver) > 0) {
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
            } else if (StrUtil.contains(el, "-")) {
                final int index = el.indexOf('-');
                final String left = StrUtil.blankToDefault(StrUtil.trim(el.substring(0, index)), "");
                final String right = StrUtil.blankToDefault(StrUtil.trim(el.substring(index + 1)), "");
                final boolean leftMatch = StrUtil.isBlank(left) || StrUtil.compareVersion(left, trimmedVersion) <= 0;
                final boolean rightMatch = StrUtil.isBlank(right) || StrUtil.compareVersion(right, trimmedVersion) >= 0;
                if (leftMatch && rightMatch) {
                    return true;
                }
            } else if (Objects.equals(trimmedVersion, el)) {
                return true;
            }
        }
        return false;
    }
}
