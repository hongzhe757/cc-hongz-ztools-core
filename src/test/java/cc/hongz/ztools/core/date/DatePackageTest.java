package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * date 包单元测试.
 * 覆盖 DateField, DateUnit, SystemClock, DateTime, TimeInterval.
 *
 * @author hongzhe.qin
 */
@DisplayName("Date 包综合")
class DatePackageTest {

	// ==================== DateField ====================

	@Test
	@DisplayName("纪元字段")
	void dateField_era() {
		assertEquals(Calendar.ERA, DateField.ERA.getValue());
	}

	@Test
	@DisplayName("年字段")
	void dateField_year() {
		assertEquals(Calendar.YEAR, DateField.YEAR.getValue());
	}

	@Test
	@DisplayName("月字段")
	void dateField_month() {
		assertEquals(Calendar.MONTH, DateField.MONTH.getValue());
	}

	@Test
	@DisplayName("日字段")
	void dateField_dayOfMonth() {
		assertEquals(Calendar.DAY_OF_MONTH, DateField.DAY_OF_MONTH.getValue());
	}

	@Test
	@DisplayName("时字段")
	void dateField_hourOfDay() {
		assertEquals(Calendar.HOUR_OF_DAY, DateField.HOUR_OF_DAY.getValue());
	}

	@Test
	@DisplayName("分字段")
	void dateField_minute() {
		assertEquals(Calendar.MINUTE, DateField.MINUTE.getValue());
	}

	@Test
	@DisplayName("秒字段")
	void dateField_second() {
		assertEquals(Calendar.SECOND, DateField.SECOND.getValue());
	}

	@Test
	@DisplayName("毫秒字段")
	void dateField_millisecond() {
		assertEquals(Calendar.MILLISECOND, DateField.MILLISECOND.getValue());
	}

	@Test
	@DisplayName("字段 of 年")
	void dateField_of_year() {
		assertEquals(DateField.YEAR, DateField.of(Calendar.YEAR));
	}

	@Test
	@DisplayName("字段 of 月")
	void dateField_of_month() {
		assertEquals(DateField.MONTH, DateField.of(Calendar.MONTH));
	}

	@Test
	@DisplayName("字段 of 非法返回空")
	void dateField_of_invalid_returnsNull() {
		assertNull(DateField.of(999));
	}

	@Test
	@DisplayName("字段枚举数")
	void dateField_enumCount() {
		assertEquals(15, DateField.values().length);
	}

	// ==================== DateUnit ====================

	@Test
	@DisplayName("毫秒单位")
	void dateUnit_ms() {
		assertEquals(1L, DateUnit.MS.getMillis());
	}

	@Test
	@DisplayName("秒单位")
	void dateUnit_second() {
		assertEquals(1000L, DateUnit.SECOND.getMillis());
	}

	@Test
	@DisplayName("分单位")
	void dateUnit_minute() {
		assertEquals(60000L, DateUnit.MINUTE.getMillis());
	}

	@Test
	@DisplayName("时单位")
	void dateUnit_hour() {
		assertEquals(3600000L, DateUnit.HOUR.getMillis());
	}

	@Test
	@DisplayName("日单位")
	void dateUnit_day() {
		assertEquals(86400000L, DateUnit.DAY.getMillis());
	}

	@Test
	@DisplayName("周单位")
	void dateUnit_week() {
		assertEquals(604800000L, DateUnit.WEEK.getMillis());
	}

	@Test
	@DisplayName("转 ChronoUnit")
	void dateUnit_toChronoUnit() {
		assertEquals(ChronoUnit.MILLIS, DateUnit.MS.toChronoUnit());
		assertEquals(ChronoUnit.SECONDS, DateUnit.SECOND.toChronoUnit());
		assertEquals(ChronoUnit.MINUTES, DateUnit.MINUTE.toChronoUnit());
		assertEquals(ChronoUnit.HOURS, DateUnit.HOUR.toChronoUnit());
		assertEquals(ChronoUnit.DAYS, DateUnit.DAY.toChronoUnit());
		assertEquals(ChronoUnit.WEEKS, DateUnit.WEEK.toChronoUnit());
	}

	@Test
	@DisplayName("从 ChronoUnit")
	void dateUnit_of_chronoUnit() {
		assertEquals(DateUnit.MS, DateUnit.of(ChronoUnit.MILLIS));
		assertEquals(DateUnit.SECOND, DateUnit.of(ChronoUnit.SECONDS));
		assertEquals(DateUnit.DAY, DateUnit.of(ChronoUnit.DAYS));
	}

	@Test
	@DisplayName("不支持返回空")
	void dateUnit_of_unsupported_returnsNull() {
		assertNull(DateUnit.of(ChronoUnit.MONTHS));
	}

	@Test
	@DisplayName("单位枚举数")
	void dateUnit_enumCount() {
		assertEquals(6, DateUnit.values().length);
	}

	// ==================== SystemClock ====================

	@Test
	@DisplayName("系统时钟当前时间")
	void systemClock_now_returnsCurrentTime() {
		// SystemClock uses a cached value updated by background thread, allow small tolerance
		long now = SystemClock.now();
		long current = System.currentTimeMillis();
		assertTrue(Math.abs(now - current) < 100, "SystemClock.now() should be close to currentTimeMillis()");
	}

	@Test
	@DisplayName("系统时钟日期非空")
	void systemClock_nowDate_returnsNonEmpty() {
		String date = SystemClock.nowDate();
		assertNotNull(date);
		assertFalse(date.isEmpty());
	}

	// ==================== DateTime ====================

	@Test
	@DisplayName("DateTime 默认构造")
	void dateTime_defaultConstructor() {
		DateTime dt = new DateTime();
		assertNotNull(dt);
		assertTrue(Math.abs(dt.getTime() - System.currentTimeMillis()) < 1000);
	}

	@Test
	@DisplayName("DateTime 从 Date")
	void dateTime_fromDate() {
		java.util.Date now = new java.util.Date();
		DateTime dt = new DateTime(now);
		assertEquals(now.getTime(), dt.getTime());
	}

	@Test
	@DisplayName("DateTime 从字符串")
	void dateTime_fromString() {
		DateTime dt = new DateTime("2023-01-15 10:30:00", "yyyy-MM-dd HH:mm:ss");
		assertNotNull(dt);
	}

	@Test
	@DisplayName("DateTime 默认格式")
	void dateTime_fromStringDefaultFormat() {
		DateTime dt = new DateTime("2023-01-15 10:30:00");
		assertNotNull(dt);
	}

	// ==================== TimeInterval ====================

	@Test
	@DisplayName("时间间隔启动")
	void timeInterval_startAndInterval() {
		TimeInterval interval = new TimeInterval();
		interval.start();
		// Small sleep to ensure time passes
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		long elapsed = interval.interval();
		assertTrue(elapsed >= 0);
	}

	@Test
	@DisplayName("时间间隔重启")
	void timeInterval_intervalRestart() {
		TimeInterval interval = new TimeInterval();
		interval.start();
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		long elapsed = interval.intervalRestart();
		assertTrue(elapsed >= 0);
		// After restart, interval should be small
		assertTrue(interval.interval() < elapsed + 1000);
	}

	// ==================== GroupTimeInterval ====================

	@Test
	@DisplayName("分组计时多计时器")
	void groupTimeInterval_multipleTimers() {
		GroupTimeInterval group = new GroupTimeInterval(false);
		group.start("timer1");
		group.start("timer2");
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		assertTrue(group.interval("timer1") >= 0);
		assertTrue(group.interval("timer2") >= 0);
	}

	@Test
	@DisplayName("分组计时清空")
	void groupTimeInterval_clear() {
		GroupTimeInterval group = new GroupTimeInterval(false);
		group.start("timer1");
		group.clear();
		// After clear, interval for non-existent id should return 0
		long elapsed = group.interval("timer1");
		assertEquals(0L, elapsed);
	}
}
