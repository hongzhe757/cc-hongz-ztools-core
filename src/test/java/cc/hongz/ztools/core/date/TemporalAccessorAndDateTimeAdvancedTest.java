package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.date.format.FastDateFormat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TemporalAccessorUtil / DateTime高级 / TimeZone 单元测试
 */
public class TemporalAccessorAndDateTimeAdvancedTest {

	// ==================== TemporalAccessorUtil ====================

	@Test
	public void formatLocalDateTest() {
		final String format = TemporalAccessorUtil.format(LocalDate.of(2020, 12, 7), DatePattern.NORM_DATETIME_PATTERN);
		assertEquals("2020-12-07 00:00:00", format);
	}

	@Test
	public void formatLocalTimeTest() {
		final String today = TemporalAccessorUtil.format(LocalDate.now(), DatePattern.NORM_DATE_PATTERN);
		final String format = TemporalAccessorUtil.format(LocalTime.MIN, DatePattern.NORM_DATETIME_PATTERN);
		assertEquals(today + " 00:00:00", format);
	}

	@Test
	public void formatCustomTest() {
		final String today = TemporalAccessorUtil.format(LocalDate.of(2021, 6, 26), "#sss");
		assertEquals("1624636800", today);

		final String today2 = TemporalAccessorUtil.format(LocalDate.of(2021, 6, 26), "#SSS");
		assertEquals("1624636800000", today2);
	}

	@Test
	public void isInTest() {
		final String sourceStr = "2022-04-19 00:00:00";
		final String startTimeStr = "2022-04-19 00:00:00";
		final String endTimeStr = "2022-04-19 23:59:59";
		final boolean between = TemporalAccessorUtil.isIn(
				LocalDateTimeUtil.parse(sourceStr, DatePattern.NORM_DATETIME_FORMATTER),
				LocalDateTimeUtil.parse(startTimeStr, DatePattern.NORM_DATETIME_FORMATTER),
				LocalDateTimeUtil.parse(endTimeStr, DatePattern.NORM_DATETIME_FORMATTER));
		assertTrue(between);
	}

	// ==================== DateTime 高级 ====================

	@Test
	public void datetimeTest() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals(2017, dateTime.year());
		assertEquals(Quarter.Q1, dateTime.quarterEnum());
		assertEquals(Month.JANUARY, dateTime.monthEnum());
		assertEquals(5, dateTime.dayOfMonth());
	}

	@Test
	public void datetimeTest2() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23");
		assertEquals(2017, dateTime.year());
		assertEquals(Quarter.Q1, dateTime.quarterEnum());
		assertEquals(Month.JANUARY, dateTime.monthEnum());
		assertEquals(5, dateTime.dayOfMonth());
	}

	@Test
	public void quarterTest() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals(Quarter.Q1, dateTime.quarterEnum());

		dateTime = new DateTime("2017-04-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals(Quarter.Q2, dateTime.quarterEnum());

		dateTime = new DateTime("2017-07-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals(Quarter.Q3, dateTime.quarterEnum());

		dateTime = new DateTime("2017-10-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals(Quarter.Q4, dateTime.quarterEnum());

		// 精确到毫秒
		DateTime beginTime = new DateTime("2017-10-01 00:00:00.000", DatePattern.NORM_DATETIME_MS_FORMAT);
		dateTime = DateUtil.beginOfQuarter(dateTime);
		assertEquals(beginTime, dateTime);

		DateTime endTime = new DateTime("2017-12-31 23:59:59.999", DatePattern.NORM_DATETIME_MS_FORMAT);
		dateTime = DateUtil.endOfQuarter(dateTime);
		assertEquals(endTime, dateTime);
	}

	@Test
	public void mutableTest() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);

		// 默认情况下DateTime为可变对象
		DateTime offsite = dateTime.offset(DateField.YEAR, 0);
		assertSame(offsite, dateTime);

		// 设置为不可变对象后变动将返回新对象
		dateTime.setMutable(false);
		offsite = dateTime.offset(DateField.YEAR, 0);
		assertNotSame(offsite, dateTime);
	}

	@Test
	public void toStringTest() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		assertEquals("2017-01-05 12:34:23", dateTime.toString());
		String dateStr = dateTime.toString("yyyy/MM/dd");
		assertEquals("2017/01/05", dateStr);
	}

	@Test
	public void toStringTest2() {
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		String dateStr = dateTime.toString(DatePattern.UTC_WITH_ZONE_OFFSET_PATTERN);
		assertEquals("2017-01-05T12:34:23+0800", dateStr);

		dateStr = dateTime.toString(DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
		assertEquals("2017-01-05T12:34:23+08:00", dateStr);
	}

	@Test
	public void monthTest() {
		@SuppressWarnings("ConstantConditions")
		int month = DateUtil.parse("2017-07-01").month();
		assertEquals(6, month);
	}

	@Test
	public void weekOfYearTest() {
		DateTime date = DateUtil.parse("2016-12-27");
		assertEquals(2016, date.year());
		assertEquals(1, date.weekOfYear());
	}

	@Test
	public void strictParseTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			String a = "2021-09-27 00:00:99";
			new DateTime(a, DatePattern.NORM_DATETIME_FORMAT, false);
		});
	}

	// ==================== TimeZone ====================

	@Test
	public void timeZoneConvertTest() {
		DateTime dt = DateUtil.parse("2018-07-10 21:44:32",
				FastDateFormat.getInstance(DatePattern.NORM_DATETIME_PATTERN, TimeZone.getTimeZone("GMT+8:00")));
		assertEquals("2018-07-10 21:44:32", dt.toString());

		dt.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		int hour = dt.getField(DateField.HOUR_OF_DAY);
		assertEquals(14, hour);
		assertEquals("2018-07-10 14:44:32", dt.toString());
	}
}
