package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalendarUtil / DateModifier (truncate/ceiling) 单元测试
 */
@DisplayName("CalendarUtil 日历与修改器")
public class CalendarUtilAndModifierTest {

	// ==================== CalendarUtil ====================

	@Test
	@DisplayName("格式化农历日期")
	public void formatChineseDate() {
		Calendar calendar = Objects.requireNonNull(DateUtil.parse("2018-02-24 12:13:14")).toCalendar();
		final String chineseDate = CalendarUtil.formatChineseDate(calendar, false);
		assertEquals("二〇一八年二月二十四日", chineseDate);
		final String chineseDateTime = CalendarUtil.formatChineseDate(calendar, true);
		assertEquals("二〇一八年二月二十四日十二时十三分十四秒", chineseDateTime);
	}

	@Test
	@DisplayName("严格解析")
	public void parseStrictTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			final Calendar calendar = CalendarUtil.parse("2021-09-27 00:00:112323", false,
				DatePattern.NORM_DATETIME_FORMAT);
			DateUtil.date(calendar);
		});
	}

	// ==================== DateModifier - truncate ====================

	@Test
	@DisplayName("截断")
	public void truncateTest() {
		String dateStr = "2017-03-01 22:33:23.123";
		Date date = DateUtil.parse(dateStr);

		// 毫秒
		DateTime begin = DateUtil.truncate(date, DateField.MILLISECOND);
		assertEquals(dateStr, begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 秒
		begin = DateUtil.truncate(date, DateField.SECOND);
		assertEquals("2017-03-01 22:33:23.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 分
		begin = DateUtil.truncate(date, DateField.MINUTE);
		assertEquals("2017-03-01 22:33:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 小时
		begin = DateUtil.truncate(date, DateField.HOUR);
		assertEquals("2017-03-01 22:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.HOUR_OF_DAY);
		assertEquals("2017-03-01 22:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 上下午
		begin = DateUtil.truncate(date, DateField.AM_PM);
		assertEquals("2017-03-01 12:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 天
		begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK_IN_MONTH);
		assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK);
		assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.DAY_OF_MONTH);
		assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 星期
		begin = DateUtil.truncate(date, DateField.WEEK_OF_MONTH);
		assertEquals("2017-02-27 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.WEEK_OF_YEAR);
		assertEquals("2017-02-27 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 月
		begin = DateUtil.truncate(date, DateField.MONTH);
		assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 年
		begin = DateUtil.truncate(date, DateField.YEAR);
		assertEquals("2017-01-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	@DisplayName("截断到月内星期")
	public void truncateDayOfWeekInMonthTest() {
		String dateStr = "2017-03-01 22:33:23.123";
		Date date = DateUtil.parse(dateStr);
		DateTime begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK_IN_MONTH);
		assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	// ==================== DateModifier - ceiling ====================

	@Test
	@DisplayName("向上取整")
	public void ceilingTest() {
		String dateStr = "2017-03-01 22:33:23.123";
		Date date = DateUtil.parse(dateStr);

		// 毫秒
		DateTime begin = DateUtil.ceiling(date, DateField.MILLISECOND);
		assertEquals(dateStr, begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 秒
		begin = DateUtil.ceiling(date, DateField.SECOND);
		assertEquals("2017-03-01 22:33:23.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 分
		begin = DateUtil.ceiling(date, DateField.MINUTE);
		assertEquals("2017-03-01 22:33:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 小时
		begin = DateUtil.ceiling(date, DateField.HOUR);
		assertEquals("2017-03-01 22:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.HOUR_OF_DAY);
		assertEquals("2017-03-01 22:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 上下午
		begin = DateUtil.ceiling(date, DateField.AM_PM);
		assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 天
		begin = DateUtil.ceiling(date, DateField.DAY_OF_WEEK_IN_MONTH);
		assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.DAY_OF_WEEK);
		assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.DAY_OF_MONTH);
		assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 星期
		begin = DateUtil.ceiling(date, DateField.WEEK_OF_MONTH);
		assertEquals("2017-03-05 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.WEEK_OF_YEAR);
		assertEquals("2017-03-05 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 月
		begin = DateUtil.ceiling(date, DateField.MONTH);
		assertEquals("2017-03-31 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 年
		begin = DateUtil.ceiling(date, DateField.YEAR);
		assertEquals("2017-12-31 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}
}
