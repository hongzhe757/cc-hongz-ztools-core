package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateUtil 核心方法单元测试.
 *
 * @author hongzhe.qin
 */
class DateUtilTest {

	// ==================== date / now / today ====================

	@Test
	void date_returnsNonNull() {
		DateTime dt = DateUtil.date();
		assertNotNull(dt);
	}

	@Test
	void date_fromDate() {
		Date now = new Date();
		DateTime dt = DateUtil.date(now);
		assertEquals(now.getTime(), dt.getTime());
	}

	@Test
	void date_fromDateNull() {
		assertNull(DateUtil.date((Date) null));
	}

	@Test
	void date_fromLong() {
		long ts = System.currentTimeMillis();
		DateTime dt = DateUtil.date(ts);
		assertEquals(ts, dt.getTime());
	}

	@Test
	void date_fromCalendar() {
		Calendar cal = Calendar.getInstance();
		DateTime dt = DateUtil.date(cal);
		assertEquals(cal.getTimeInMillis(), dt.getTime());
	}

	@Test
	void date_fromCalendarNull() {
		assertNull(DateUtil.date((Calendar) null));
	}

	@Test
	void now_returnsNonEmpty() {
		String now = DateUtil.now();
		assertNotNull(now);
		assertFalse(now.isEmpty());
		// 格式: yyyy-MM-dd HH:mm:ss
		assertTrue(now.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
	}

	@Test
	void today_returnsNonEmpty() {
		String today = DateUtil.today();
		assertNotNull(today);
		// 格式: yyyy-MM-dd
		assertTrue(today.matches("\\d{4}-\\d{2}-\\d{2}"));
	}

	// ==================== current ====================

	@Test
	void current_closeToSystemTime() {
		long cur = DateUtil.current();
		assertTrue(Math.abs(cur - System.currentTimeMillis()) < 100);
	}

	@Test
	void currentSeconds_closeToSystemTime() {
		long curSec = DateUtil.currentSeconds();
		long expected = System.currentTimeMillis() / 1000;
		assertTrue(Math.abs(curSec - expected) < 1);
	}

	// ==================== year / month / day ====================

	@Test
	void year() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2023);
		assertEquals(2023, DateUtil.year(cal.getTime()));
	}

	@Test
	void month() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.MARCH);
		// DateUtil.month() 返回 0-based
		assertEquals(2, DateUtil.month(cal.getTime()));
	}

	@Test
	void dayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 15);
		assertEquals(15, DateUtil.dayOfMonth(cal.getTime()));
	}

	// ==================== format ====================

	@Test
	void formatDateTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		cal.set(Calendar.MILLISECOND, 0);
		String result = DateUtil.formatDateTime(cal.getTime());
		assertEquals("2023-06-15 10:30:45", result);
	}

	@Test
	void formatDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		String result = DateUtil.formatDate(cal.getTime());
		assertEquals("2023-06-15", result);
	}

	@Test
	void formatTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		String result = DateUtil.formatTime(cal.getTime());
		assertEquals("10:30:45", result);
	}

	@Test
	void format_customPattern() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		String result = DateUtil.format(cal.getTime(), "yyyy/MM/dd");
		assertEquals("2023/06/15", result);
	}

	// ==================== parse ====================

	@Test
	void parse_standardFormat() {
		Date date = DateUtil.parse("2023-06-15 10:30:45");
		assertNotNull(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(2023, cal.get(Calendar.YEAR));
		assertEquals(Calendar.JUNE, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	void parse_dateOnly() {
		Date date = DateUtil.parse("2023-06-15");
		assertNotNull(date);
	}

	@Test
	void parse_withFormat() {
		Date date = DateUtil.parse("2023/06/15", "yyyy/MM/dd");
		assertNotNull(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(2023, cal.get(Calendar.YEAR));
	}

	@Test
	void parse_null_returnsNull() {
		assertNull(DateUtil.parse((String) null));
	}

	// ==================== beginOf / endOf ====================

	@Test
	void beginOfDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		Date result = DateUtil.beginOfDay(cal.getTime());
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, res.get(Calendar.MINUTE));
		assertEquals(0, res.get(Calendar.SECOND));
		assertEquals(0, res.get(Calendar.MILLISECOND));
	}

	@Test
	void endOfDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		Date result = DateUtil.endOfDay(cal.getTime());
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(23, res.get(Calendar.HOUR_OF_DAY));
		assertEquals(59, res.get(Calendar.MINUTE));
		assertEquals(59, res.get(Calendar.SECOND));
		assertEquals(999, res.get(Calendar.MILLISECOND));
	}

	@Test
	void beginOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		Date result = DateUtil.beginOfMonth(cal.getTime());
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(1, res.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
	}

	@Test
	void endOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		Date result = DateUtil.endOfMonth(cal.getTime());
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(30, res.get(Calendar.DAY_OF_MONTH)); // June has 30 days
		assertEquals(23, res.get(Calendar.HOUR_OF_DAY));
	}

	@Test
	void beginOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15, 10, 30, 45);
		Date result = DateUtil.beginOfYear(cal.getTime());
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(Calendar.JANUARY, res.get(Calendar.MONTH));
		assertEquals(1, res.get(Calendar.DAY_OF_MONTH));
	}

	// ==================== offset ====================

	@Test
	void offset_day() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		Date result = DateUtil.offsetDay(cal.getTime(), 5);
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(20, res.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	void offset_day_negative() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		Date result = DateUtil.offsetDay(cal.getTime(), -5);
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(10, res.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	void offsetMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		Date result = DateUtil.offsetMonth(cal.getTime(), 2);
		Calendar res = Calendar.getInstance();
		res.setTime(result);
		assertEquals(Calendar.AUGUST, res.get(Calendar.MONTH));
	}

	// ==================== between ====================

	@Test
	void betweenDay() {
		Date d1 = DateUtil.parse("2023-06-01");
		Date d2 = DateUtil.parse("2023-06-15");
		long days = DateUtil.betweenDay(d1, d2, false);
		assertEquals(14, days);
	}

	@Test
	void betweenHour() {
		Date d1 = DateUtil.parse("2023-06-15 00:00:00");
		Date d2 = DateUtil.parse("2023-06-15 12:00:00");
		long hours = DateUtil.between(d1, d2, DateUnit.HOUR);
		assertEquals(12, hours);
	}

	@Test
	void betweenMinute() {
		Date d1 = DateUtil.parse("2023-06-15 10:00:00");
		Date d2 = DateUtil.parse("2023-06-15 10:30:00");
		long mins = DateUtil.between(d1, d2, DateUnit.MINUTE);
		assertEquals(30, mins);
	}

	// ==================== isLeapYear ====================

	@Test
	void isLeapYear_2024_true() {
		assertTrue(DateUtil.isLeapYear(2024));
	}

	@Test
	void isLeapYear_2023_false() {
		assertFalse(DateUtil.isLeapYear(2023));
	}

	@Test
	void isLeapYear_2000_true() {
		assertTrue(DateUtil.isLeapYear(2000));
	}

	@Test
	void isLeapYear_1900_false() {
		assertFalse(DateUtil.isLeapYear(1900));
	}

	// ==================== age ====================

	@Test
	void age() {
		Date birthday = DateUtil.parse("2000-01-01");
		Date now = DateUtil.parse("2023-06-15");
		int age = DateUtil.ageOfNow(birthday);
		// 到2026年应该是26岁, 但取决于当前时间
		assertTrue(age >= 23);
	}

	// ==================== isEqual ====================

	@Test
	void isEqual_sameDate() {
		Date d1 = DateUtil.parse("2023-06-15");
		Date d2 = DateUtil.parse("2023-06-15");
		assertTrue(DateUtil.isSameDay(d1, d2));
	}

	@Test
	void isEqual_differentDate() {
		Date d1 = DateUtil.parse("2023-06-15");
		Date d2 = DateUtil.parse("2023-06-16");
		assertFalse(DateUtil.isSameDay(d1, d2));
	}

	// ==================== getQuarter ====================

	@Test
	void quarter() {
		Date d1 = DateUtil.parse("2023-01-15");
		assertEquals(1, DateUtil.quarter(d1));

		Date d2 = DateUtil.parse("2023-04-15");
		assertEquals(2, DateUtil.quarter(d2));

		Date d3 = DateUtil.parse("2023-07-15");
		assertEquals(3, DateUtil.quarter(d3));

		Date d4 = DateUtil.parse("2023-10-15");
		assertEquals(4, DateUtil.quarter(d4));
	}

	// ==================== dayOfWeek ====================

	@Test
	void dayOfWeek() {
		// 2023-06-15 是星期四
		Date date = DateUtil.parse("2023-06-15");
		int dow = DateUtil.dayOfWeek(date);
		assertEquals(Calendar.THURSDAY, dow);
	}

	// ==================== lastDayOfMonth ====================

	@Test
	void lastDayOfMonth_june() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.JUNE, 15);
		int lastDay = DateUtil.getLastDayOfMonth(cal.getTime());
		assertEquals(30, lastDay);
	}

	@Test
	void lastDayOfMonth_february_leap() {
		Calendar cal = Calendar.getInstance();
		cal.set(2024, Calendar.FEBRUARY, 1);
		int lastDay = DateUtil.getLastDayOfMonth(cal.getTime());
		assertEquals(29, lastDay);
	}

	@Test
	void lastDayOfMonth_february_nonLeap() {
		Calendar cal = Calendar.getInstance();
		cal.set(2023, Calendar.FEBRUARY, 1);
		int lastDay = DateUtil.getLastDayOfMonth(cal.getTime());
		assertEquals(28, lastDay);
	}
}
