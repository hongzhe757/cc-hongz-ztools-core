package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.date.BetweenFormatter.Level;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateBetween / DateRange / BetweenFormatter 单元测试
 */
public class DateBetweenAndRangeTest {

	// ==================== DateBetween ====================

	@Test
	public void betweenYearTest() {
		Date start = DateUtil.parse("2017-02-01 12:23:46");
		Date end = DateUtil.parse("2018-02-01 12:23:46");
		long betweenYear = new DateBetween(start, end).betweenYear(false);
		assertEquals(1, betweenYear);

		Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		long betweenYear1 = new DateBetween(start1, end1).betweenYear(false);
		assertEquals(1, betweenYear1);

		// 不足1年
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenYear2 = new DateBetween(start2, end2).betweenYear(false);
		assertEquals(0, betweenYear2);
	}

	@Test
	public void betweenYearTest2() {
		Date start = DateUtil.parse("2000-02-29");
		Date end = DateUtil.parse("2018-02-28");
		long betweenYear = new DateBetween(start, end).betweenYear(false);
		assertEquals(18, betweenYear);
	}

	@Test
	public void betweenYearTest3() {
		Date start = DateUtil.parse("20170301");
		Date end = DateUtil.parse("2024-02-29 14:56:18");
		long betweenYear = new DateBetween(start, end).betweenYear(false);
		assertEquals(6, betweenYear);
	}

	@Test
	public void betweenMonthTest() {
		Date start = DateUtil.parse("2017-02-01 12:23:46");
		Date end = DateUtil.parse("2018-02-01 12:23:46");
		long betweenMonth = new DateBetween(start, end).betweenMonth(false);
		assertEquals(12, betweenMonth);

		Date start1 = DateUtil.parse("2017-02-01 12:23:46");
		Date end1 = DateUtil.parse("2018-03-01 12:23:46");
		long betweenMonth1 = new DateBetween(start1, end1).betweenMonth(false);
		assertEquals(13, betweenMonth1);

		// 不足
		Date start2 = DateUtil.parse("2017-02-01 12:23:46");
		Date end2 = DateUtil.parse("2018-02-01 11:23:46");
		long betweenMonth2 = new DateBetween(start2, end2).betweenMonth(false);
		assertEquals(11, betweenMonth2);
	}

	@Test
	public void betweenMinuteTest() {
		Date date1 = DateUtil.parse("2017-03-01 20:33:23");
		Date date2 = DateUtil.parse("2017-03-01 23:33:23");
		String formatBetween = DateUtil.formatBetween(date1, date2, Level.SECOND);
		assertEquals("3小时", formatBetween);
	}

	@Test
	public void betweenWeeksTest() {
		final long betweenWeek = DateUtil.betweenWeek(
				DateUtil.parse("2020-11-21"),
				DateUtil.parse("2020-11-23"), false);

		final long betweenWeek2 = LocalDateTimeUtil.between(
				LocalDateTimeUtil.parse("2020-11-21", "yyy-MM-dd"),
				LocalDateTimeUtil.parse("2020-11-23", "yyy-MM-dd"),
				ChronoUnit.WEEKS);
		assertEquals(betweenWeek, betweenWeek2);
	}

	@Test
	public void betweenYear_issueI97U3J() {
		String dateStr1 = "2024-02-29 23:59:59";
		Date sdate = DateUtil.parse(dateStr1);
		String dateStr2 = "2023-03-01 00:00:00";
		Date edate = DateUtil.parse(dateStr2);
		long result = DateUtil.betweenYear(sdate, edate, false);
		assertEquals(0, result);
	}

	@Test
	public void between_defensiveCopy() {
		Date b = new Date(1609459200000L); // 2021-01-01 00:00:00
		Date e = new Date(1609545600000L); // 2021-01-02 00:00:00
		DateBetween db = new DateBetween(b, e);
		b.setTime(0L); // 修改原始 date
		assertEquals(1, db.between(DateUnit.DAY));
	}

	// ==================== BetweenFormatter ====================

	@Test
	public void betweenFormatter_formatTest() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.MILLISECOND, 1);
		assertEquals("1天", formater.toString());
	}

	@Test
	public void betweenFormatter_formatTestEn() {
		java.util.function.Function<Level, String> levelFormatterEn = level -> {
			switch (level) {
				case DAY: return " day";
				case HOUR: return " hour";
				case MINUTE: return " minute";
				case SECOND: return " second";
				case MILLISECOND: return " millisecond";
				default: return " " + level.name();
			}
		};
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, Level.MILLISECOND, 1);
		formatter.setLevelFormatter(levelFormatterEn);
		assertEquals("1 day", formatter.toString());
	}

	@Test
	public void betweenFormatter_formatTestEn2() {
		java.util.function.Function<Level, String> levelFormatterEn = level -> {
			switch (level) {
				case DAY: return " day";
				case HOUR: return " hour";
				case MINUTE: return " minute";
				case SECOND: return " second";
				case MILLISECOND: return " millisecond";
				default: return " " + level.name();
			}
		};
		final long betweenMs = 3610001;
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, Level.MILLISECOND, 5);
		formatter.setSeparator(",");
		formatter.setLevelFormatter(levelFormatterEn);
		assertEquals("1 hour,10 second,1 millisecond", formatter.toString());
	}

	@Test
	public void betweenFormatter_formatBetweenTest() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 1);
		assertEquals("1秒", formater.toString());
	}

	@Test
	public void betweenFormatter_formatBetweenTest2() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 5);
		assertEquals("1小时2分3秒", formater.toString());
	}

	@Test
	public void betweenFormatter_formatTest2() {
		BetweenFormatter formater = new BetweenFormatter(584, Level.SECOND, 1);
		assertEquals("0秒", formater.toString());
	}

	// ==================== DateRange (rangeToList) ====================

	@Test
	public void rangeToList_issue3783() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, 0);
		assertEquals(1, dateTimes.size());
		assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}

	@Test
	public void rangeToList_issue3783_negativeStep() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, -2);
		assertEquals(1, dateTimes.size());
		assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}
}
