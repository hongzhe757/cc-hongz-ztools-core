package cc.hongz.ztools.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电话号码工具类，包括：
 * <ul>
 *     <li>手机号码</li>
 *     <li>400、800号码</li>
 *     <li>座机号码</li>
 * </ul>
 *
 * @author dahuoyzs
 * @since 5.3.11
 */
public class PhoneUtil {

	/** 中国大陆手机号正则 */
	private static final Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3-9]\\d{9}");
	/** 中国香港手机号正则 */
	private static final Pattern MOBILE_HK = Pattern.compile("(?:0|852|\\+852)?\\d{8}");
	/** 中国台湾手机号正则 */
	private static final Pattern MOBILE_TW = Pattern.compile("(?:0|886|\\+886)?(?:|-)09\\d{8}");
	/** 中国澳门手机号正则 */
	private static final Pattern MOBILE_MO = Pattern.compile("(?:0|853|\\+853)?(?:|-)6\\d{7}");
	/** 中国大陆座机号正则 */
	private static final Pattern TEL = Pattern.compile("(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})");
	/** 400/800电话正则 */
	private static final Pattern TEL_400_800 = Pattern.compile("0\\d{2,3}[\\- ]?[0-9]\\d{6,7}|[48]00[\\- ]?[0-9]\\d{2}[\\- ]?\\d{4}");

	/**
	 * 验证是否为手机号码（中国大陆）
	 *
	 * @param value 值
	 * @return 是否为手机号码（中国大陆）
	 * @since 5.3.11
	 */
	public static boolean isMobile(CharSequence value) {
		return isMatch(MOBILE, value);
	}

	/**
	 * 验证是否为手机号码（中国香港）
	 * @param value 手机号码
	 * @return 是否为中国香港手机号码
	 * @since 5.6.3
	 */
	public static boolean isMobileHk(CharSequence value) {
		return isMatch(MOBILE_HK, value);
	}

	/**
	 * 验证是否为手机号码（中国台湾）
	 * @param value 手机号码
	 * @return 是否为中国台湾手机号码
	 * @since 5.6.6
	 */
	public static boolean isMobileTw(CharSequence value) {
		return isMatch(MOBILE_TW, value);
	}

	/**
	 * 验证是否为手机号码（中国澳门）
	 * @param value 手机号码
	 * @return 是否为中国澳门手机号码
	 * @since 5.6.6
	 */
	public static boolean isMobileMo(CharSequence value) {
		return isMatch(MOBILE_MO, value);
	}

	/**
	 * 验证是否为座机号码（中国大陆）
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国大陆）
	 * @since 5.3.11
	 */
	public static boolean isTel(CharSequence value) {
		return isMatch(TEL, value);
	}

	/**
	 * 验证是否为座机号码（中国大陆）+ 400 + 800
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国大陆）
	 * @since 5.6.3
	 */
	public static boolean isTel400800(CharSequence value) {
		return isMatch(TEL_400_800, value);
	}

	/**
	 * 验证是否为座机号码+手机号码（CharUtil中国）+ 400 + 800电话 + 手机号号码（中国香港）
	 *
	 * @param value 值
	 * @return 是否为座机号码+手机号码（中国大陆）+手机号码（中国香港）+手机号码（中国台湾）+手机号码（中国澳门）
	 * @since 5.3.11
	 */
	public static boolean isPhone(CharSequence value) {
		return isMobile(value) || isTel400800(value) || isMobileHk(value) || isMobileTw(value) || isMobileMo(value);
	}

	/**
	 * 隐藏手机号前7位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideBefore(CharSequence phone) {
		return StrUtil.hide(phone, 0, 7);
	}

	/**
	 * 隐藏手机号中间4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideBetween(CharSequence phone) {
		return StrUtil.hide(phone, 3, 7);
	}

	/**
	 * 隐藏手机号最后4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideAfter(CharSequence phone) {
		return StrUtil.hide(phone, 7, 11);
	}

	/**
	 * 获取手机号前3位
	 *
	 * @param phone 手机号码
	 * @return 手机号前3位
	 * @since 5.3.11
	 */
	public static CharSequence subBefore(CharSequence phone) {
		return sub(phone, 0, 3);
	}

	/**
	 * 获取手机号中间4位
	 *
	 * @param phone 手机号码
	 * @return 手机号中间4位
	 * @since 5.3.11
	 */
	public static CharSequence subBetween(CharSequence phone) {
		return sub(phone, 3, 7);
	}

	/**
	 * 获取手机号后4位
	 *
	 * @param phone 手机号码
	 * @return 手机号后4位
	 * @since 5.3.11
	 */
	public static CharSequence subAfter(CharSequence phone) {
		return sub(phone, 7, 11);
	}

	/**
	 * 获取固话号码中的区号
	 *
	 * @param value 完整的固话号码
	 * @return 固话号码的区号部分
	 * @since 5.7.7
	 */
	public static CharSequence subTelBefore(CharSequence value) {
		return getGroup(TEL, value, 1);
	}

	/**
	 * 获取固话号码中的号码
	 *
	 * @param value 完整的固话号码
	 * @return 固话号码的号码部分
	 * @since 5.7.7
	 */
	public static CharSequence subTelAfter(CharSequence value) {
		return getGroup(TEL, value, 2);
	}

	// ------------------------------------------------------------------------- Private method start

	/**
	 * 判断是否匹配正则
	 */
	private static boolean isMatch(Pattern pattern, CharSequence value) {
		if (value == null) {
			return false;
		}
		return pattern.matcher(value).matches();
	}

	/**
	 * 截取子串
	 */
	private static CharSequence sub(CharSequence str, int start, int end) {
		if (str == null) {
			return null;
		}
		return str.toString().substring(start, Math.min(end, str.length()));
	}

	/**
	 * 获取正则匹配的第n个分组
	 */
	private static CharSequence getGroup(Pattern pattern, CharSequence value, int groupIndex) {
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
}
