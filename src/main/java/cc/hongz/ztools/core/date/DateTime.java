package cc.hongz.ztools.core.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 包装 {@link Date}，提供更便捷的日期方法.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.date.DateTime}（最小子集）.
 *
 * @author looly
 */
public class DateTime extends Date {

	private static final long serialVersionUID = 1L;

	/**
	 * 时区.
	 */
	private TimeZone timeZone;

	/**
	 * 构造当前时间.
	 */
	public DateTime() {
		super();
	}

	/**
	 * 构造指定时间.
	 *
	 * @param date Date
	 */
	public DateTime(Date date) {
		this(date.getTime());
	}

	/**
	 * 构造指定时间.
	 *
	 * @param dateStr 日期字符串
	 * @param format  格式
	 */
	public DateTime(String dateStr, String format) {
		try {
			setTime(new SimpleDateFormat(format).parse(dateStr).getTime());
		} catch (Exception e) {
			throw new RuntimeException("Parse date failed: " + dateStr, e);
		}
	}

	/**
	 * 构造指定时间.
	 *
	 * @param dateStr 日期字符串，支持多种常见格式
	 */
	public DateTime(String dateStr) {
		this(parseAuto(dateStr));
	}

	/**
	 * 构造指定毫秒数.
	 *
	 * @param dateMs 毫秒数
	 */
	public DateTime(long dateMs) {
		super(dateMs);
	}

	/**
	 * 获得年的值.
	 *
	 * @return 年
	 */
	public int year() {
		return Calendar.getInstance(timeZone).toInstant().atZone(java.time.ZoneId.systemDefault()).getYear();
	}

	/**
	 * 获得月份，从 0 开始.
	 *
	 * @return 月份
	 */
	public int month() {
		return Calendar.getInstance(timeZone).get(Calendar.MONTH);
	}

	/**
	 * 获得日.
	 *
	 * @return 日
	 */
	public int dayOfMonth() {
		return Calendar.getInstance(timeZone).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取时区.
	 *
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * 是否为本月最后一天.
	 *
	 * @return 是否最后一天
	 */
	public boolean isLastDayOfMonth() {
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(this);
		return cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取本月最后一天.
	 *
	 * @return 最后一天
	 */
	public int getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(this);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 转为 Date.
	 *
	 * @return Date
	 */
	public Date toJdkDate() {
		return new Date(this.getTime());
	}

	/**
	 * 自动解析日期字符串，支持多种常见格式.
	 *
	 * @param dateStr 日期字符串
	 * @return Date
	 */
	private static Date parseAuto(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		final String[] patterns = {
				"yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd",
				"yyyy/MM/dd HH:mm:ss",
				"yyyy/MM/dd",
				"yyyyMMdd",
				"yyyyMMddHHmmss",
				"yyyy.MM.dd",
				"yyyy年MM月dd日"
		};
		for (String pattern : patterns) {
			try {
				return new SimpleDateFormat(pattern).parse(dateStr);
			} catch (Exception ignored) {
				// try next
			}
		}
		throw new RuntimeException("No format fit for date String: " + dateStr);
	}
}
