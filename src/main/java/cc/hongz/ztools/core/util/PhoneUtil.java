package cc.hongz.ztools.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电话号码工具类，包括：.
 * <ul>
 *     <li>手机号码</li>
 *     <li>400、800号码</li>
 *     <li>座机号码</li>
 * </ul>
 *
 * @author dahuoyzs
 * @since 5.3.11
 */
public final class PhoneUtil {

    /** 中国大陆手机号正则. */
    private static final Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3-9]\\d{9}");
    /** 中国香港手机号正则. */
    private static final Pattern MOBILE_HK = Pattern.compile("(?:0|852|\\+852)?\\d{8}");
    /** 中国台湾手机号正则. */
    private static final Pattern MOBILE_TW = Pattern.compile("(?:0|886|\\+886)?(?:|-)09\\d{8}");
    /** 中国澳门手机号正则. */
    private static final Pattern MOBILE_MO = Pattern.compile("(?:0|853|\\+853)?(?:|-)6\\d{7}");
    /** 中国大陆座机号正则. */
    private static final Pattern TEL = Pattern.compile("(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})");
    /** 400/800电话正则. */
    private static final Pattern TEL_400_800 = Pattern.compile(
            "0\\d{2,3}[\\- ]?[0-9]\\d{6,7}|[48]00[\\- ]?[0-9]\\d{2}[\\- ]?\\d{4}");

    /** 手机号隐藏前段结束位置. */
    private static final int HIDE_BEFORE_END = 7;
    /** 手机号隐藏中段开始位置. */
    private static final int HIDE_BETWEEN_START = 3;
    /** 手机号隐藏中段结束位置. */
    private static final int HIDE_BETWEEN_END = 7;
    /** 手机号隐藏后段开始位置. */
    private static final int HIDE_AFTER_START = 7;
    /** 手机号隐藏后段结束位置. */
    private static final int HIDE_AFTER_END = 11;

    /**
     * 验证是否为手机号码（中国大陆）.
     *
     * @param value 值
     * @return 是否为手机号码（中国大陆）
     * @since 5.3.11
     */
    public static boolean isMobile(final CharSequence value) {
        return isMatch(MOBILE, value);
    }

    /**
     * 验证是否为手机号码（中国香港）.
     *
     * @param value 手机号码
     * @return 是否为中国香港手机号码
     * @since 5.6.3
     */
    public static boolean isMobileHk(final CharSequence value) {
        return isMatch(MOBILE_HK, value);
    }

    /**
     * 验证是否为手机号码（中国台湾）.
     *
     * @param value 手机号码
     * @return 是否为中国台湾手机号码
     * @since 5.6.6
     */
    public static boolean isMobileTw(final CharSequence value) {
        return isMatch(MOBILE_TW, value);
    }

    /**
     * 验证是否为手机号码（中国澳门）.
     *
     * @param value 手机号码
     * @return 是否为中国澳门手机号码
     * @since 5.6.6
     */
    public static boolean isMobileMo(final CharSequence value) {
        return isMatch(MOBILE_MO, value);
    }

    /**
     * 验证是否为座机号码（中国大陆）.
     *
     * @param value 值
     * @return 是否为座机号码（中国大陆）
     * @since 5.3.11
     */
    public static boolean isTel(final CharSequence value) {
        return isMatch(TEL, value);
    }

    /**
     * 验证是否为座机号码（中国大陆）+ 400 + 800.
     *
     * @param value 值
     * @return 是否为座机号码（中国大陆）
     * @since 5.6.3
     */
    public static boolean isTel400800(final CharSequence value) {
        return isMatch(TEL_400_800, value);
    }

    /**
     * 验证是否为座机号码+手机号码（CharUtil中国）+ 400 + 800电话 + 手机号号码（中国香港）.
     *
     * @param value 值
     * @return 是否为座机号码+手机号码（中国大陆）+手机号码（中国香港）+手机号码（中国台湾）+手机号码（中国澳门）
     * @since 5.3.11
     */
    public static boolean isPhone(final CharSequence value) {
        return isMobile(value) || isTel400800(value) || isMobileHk(value)
                || isMobileTw(value) || isMobileMo(value);
    }

    /**
     * 隐藏手机号前7位  替换字符为"*".
     *
     * @param phone 手机号码
     * @return 替换后的字符串
     * @since 5.3.11
     */
    public static CharSequence hideBefore(final CharSequence phone) {
        return StrUtil.hide(phone, 0, HIDE_BEFORE_END);
    }

    /**
     * 隐藏手机号中间4位  替换字符为"*".
     *
     * @param phone 手机号码
     * @return 替换后的字符串
     * @since 5.3.11
     */
    public static CharSequence hideBetween(final CharSequence phone) {
        return StrUtil.hide(phone, HIDE_BETWEEN_START, HIDE_BETWEEN_END);
    }

    /**
     * 隐藏手机号最后4位  替换字符为"*".
     *
     * @param phone 手机号码
     * @return 替换后的字符串
     * @since 5.3.11
     */
    public static CharSequence hideAfter(final CharSequence phone) {
        return StrUtil.hide(phone, HIDE_AFTER_START, HIDE_AFTER_END);
    }

    /**
     * 获取手机号前3位.
     *
     * @param phone 手机号码
     * @return 手机号前3位
     * @since 5.3.11
     */
    public static CharSequence subBefore(final CharSequence phone) {
        return sub(phone, 0, 3);
    }

    /**
     * 获取手机号中间4位.
     *
     * @param phone 手机号码
     * @return 手机号中间4位
     * @since 5.3.11
     */
    public static CharSequence subBetween(final CharSequence phone) {
        return sub(phone, HIDE_BETWEEN_START, HIDE_BETWEEN_END);
    }

    /**
     * 获取手机号后4位.
     *
     * @param phone 手机号码
     * @return 手机号后4位
     * @since 5.3.11
     */
    public static CharSequence subAfter(final CharSequence phone) {
        return sub(phone, HIDE_AFTER_START, HIDE_AFTER_END);
    }

    /**
     * 获取固话号码中的区号.
     *
     * @param value 完整的固话号码
     * @return 固话号码的区号部分
     * @since 5.7.7
     */
    public static CharSequence subTelBefore(final CharSequence value) {
        return getGroup(TEL, value, 1);
    }

    /**
     * 获取固话号码中的号码.
     *
     * @param value 完整的固话号码
     * @return 固话号码的号码部分
     * @since 5.7.7
     */
    public static CharSequence subTelAfter(final CharSequence value) {
        return getGroup(TEL, value, 2);
    }

    // ------------------------------------------------------------------------- Private method start

    /**
     * 判断是否匹配正则.
     *
     * @param pattern 正则模式
     * @param value   值
     * @return 是否匹配
     */
    private static boolean isMatch(final Pattern pattern, final CharSequence value) {
        if (value == null) {
            return false;
        }
        return pattern.matcher(value).matches();
    }

    /**
     * 截取子串.
     *
     * @param str   字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 子串
     */
    private static CharSequence sub(final CharSequence str, final int start, final int end) {
        if (str == null) {
            return null;
        }
        return str.toString().substring(start, Math.min(end, str.length()));
    }

    /**
     * 获取正则匹配的第n个分组.
     *
     * @param pattern    正则模式
     * @param value      值
     * @param groupIndex 分组索引
     * @return 匹配的分组
     */
    private static CharSequence getGroup(final Pattern pattern, final CharSequence value,
            final int groupIndex) {
        if (value == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    // ------------------------------------------------------------------------- Private method end

    /**
     * 私有构造器.
     */
    private PhoneUtil() {
    }
}
