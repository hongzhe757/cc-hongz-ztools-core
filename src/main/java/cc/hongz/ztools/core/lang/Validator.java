package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.DateUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类（最小子集）.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Validator}.
 *
 * @author looly
 */
public final class Validator {

	/**
	 * 生日正则.
	 */
	private static final Pattern BIRTHDAY = Pattern.compile(
			"^\\d{4}[/\\-.]?\\d{1,2}[/\\-.]?\\d{1,2}$");

	/**
	 * 私有构造.
	 */
	private Validator() {
	}

	/**
	 * 验证是否为生日（yyyy-MM-dd 或 yyyyMMdd 等格式）.
	 *
	 * @param value 日期字符串
	 * @return 是否为生日
	 */
	public static boolean isBirthday(CharSequence value) {
		if (StrUtil.isBlank(value)) {
			return false;
		}
		final Matcher matcher = BIRTHDAY.matcher(value);
		if (matcher.find()) {
			// 提取年月日
			String dateStr = matcher.group();
			// 去除分隔符
			String pure = dateStr.replaceAll("[/\\-.]", "");
			int year = Integer.parseInt(pure.substring(0, 4));
			int month = Integer.parseInt(pure.substring(4, 6));
			int day = Integer.parseInt(pure.substring(6, 8));
			return isBirthday(year, month, day);
		}
		return false;
	}

	/**
	 * 验证是否为生日.
	 *
	 * @param year  年
	 * @param month 月
	 * @param day   日
	 * @return 是否为生日
	 */
	public static boolean isBirthday(int year, int month, int day) {
		// 验证年
		int thisYear = DateUtil.year(DateUtil.date());
		if (year < 1900 || year > thisYear) {
			return false;
		}

		// 验证月
		if (month < 1 || month > 12) {
			return false;
		}

		// 验证日
		if (day < 1 || day > 31) {
			return false;
		}
		// 检查几个特殊月的最大天数
		if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			return false;
		}
		if (month == 2) {
			// 在2月，非闰年最大28，闰年最大29
			return day < 29 || (day == 29 && DateUtil.isLeapYear(year));
		}
		return true;
	}
}
