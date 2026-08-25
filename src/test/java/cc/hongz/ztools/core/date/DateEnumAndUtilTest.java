package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Zodiac, Quarter, Month, Week 枚举/工具 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Date 枚举与工具")
class DateEnumAndUtilTest {

	// ==================== Zodiac ====================

	@Test
	@DisplayName("星座按月日")
	void zodiac_getZodiac_byMonthDay() {
		// month=0(Jan), day=15 -> 摩羯座
		assertEquals("摩羯座", Zodiac.getZodiac(0, 15));
		// month=1(Feb), day=25 -> 双鱼座
		assertEquals("双鱼座", Zodiac.getZodiac(1, 25));
		// month=6(Jul), day=25 -> 狮子座
		assertEquals("狮子座", Zodiac.getZodiac(6, 25));
	}

	@Test
	@DisplayName("生肖")
	void zodiac_chineseZodiac() {
		// 2024 -> 龙
		assertEquals("龙", Zodiac.getChineseZodiac(2024));
		// 2023 -> 兔
		assertEquals("兔", Zodiac.getChineseZodiac(2023));
		// 1900 -> 鼠
		assertEquals("鼠", Zodiac.getChineseZodiac(1900));
	}

	@Test
	@DisplayName("1900 前生肖为空")
	void zodiac_chineseZodiac_before1900_returnsNull() {
		assertNull(Zodiac.getChineseZodiac(1899));
	}

	// ==================== Quarter ====================

	@Test
	@DisplayName("季度 of")
	void quarter_of() {
		assertEquals(Quarter.Q1, Quarter.of(1));
		assertEquals(Quarter.Q2, Quarter.of(2));
		assertEquals(Quarter.Q3, Quarter.of(3));
		assertEquals(Quarter.Q4, Quarter.of(4));
		assertNull(Quarter.of(0));
		assertNull(Quarter.of(5));
	}

	@Test
	@DisplayName("季度从月")
	void quarter_fromMonth() {
		assertEquals(Quarter.Q1, Quarter.fromMonth(1));
		assertEquals(Quarter.Q1, Quarter.fromMonth(3));
		assertEquals(Quarter.Q2, Quarter.fromMonth(4));
		assertEquals(Quarter.Q3, Quarter.fromMonth(7));
		assertEquals(Quarter.Q4, Quarter.fromMonth(12));
	}

	@Test
	@DisplayName("季度值")
	void quarter_getValue() {
		assertEquals(1, Quarter.Q1.getValue());
		assertEquals(4, Quarter.Q4.getValue());
	}

	@Test
	@DisplayName("季度首月")
	void quarter_firstMonth() {
		assertEquals(1, Quarter.Q1.firstMonthValue());
		assertEquals(4, Quarter.Q2.firstMonthValue());
		assertEquals(7, Quarter.Q3.firstMonthValue());
		assertEquals(10, Quarter.Q4.firstMonthValue());
	}

	@Test
	@DisplayName("季度末月")
	void quarter_lastMonth() {
		assertEquals(3, Quarter.Q1.lastMonthValue());
		assertEquals(6, Quarter.Q2.lastMonthValue());
		assertEquals(9, Quarter.Q3.lastMonthValue());
		assertEquals(12, Quarter.Q4.lastMonthValue());
	}

	@Test
	@DisplayName("季度加")
	void quarter_plus() {
		assertEquals(Quarter.Q2, Quarter.Q1.plus(1));
		assertEquals(Quarter.Q1, Quarter.Q4.plus(1));
		assertEquals(Quarter.Q3, Quarter.Q1.plus(2));
	}

	@Test
	@DisplayName("季度校验")
	void quarter_checkValidIntValue() {
		assertEquals(1, Quarter.checkValidIntValue(1));
		assertEquals(4, Quarter.checkValidIntValue(4));
		assertThrows(DateTimeException.class, () -> Quarter.checkValidIntValue(0));
		assertThrows(DateTimeException.class, () -> Quarter.checkValidIntValue(5));
	}

	// ==================== Month ====================

	@Test
	@DisplayName("月份 of")
	void month_of() {
		assertEquals(Month.FEBRUARY, Month.of(1));
		assertEquals(Month.JANUARY, Month.of(0));
		assertNull(Month.of(-1));
		assertNull(Month.of(13));
	}

	@Test
	@DisplayName("月份值")
	void month_getValue() {
		assertEquals(0, Month.JANUARY.getValue());
		assertEquals(11, Month.DECEMBER.getValue());
	}

	@Test
	@DisplayName("月份值（从1）")
	void month_getValueBaseOne() {
		assertEquals(1, Month.JANUARY.getValueBaseOne());
		assertEquals(12, Month.DECEMBER.getValueBaseOne());
	}

	@Test
	@DisplayName("月份最后一天")
	void month_getLastDay() {
		assertEquals(31, Month.JANUARY.getLastDay(false));
		assertEquals(28, Month.FEBRUARY.getLastDay(false));
		assertEquals(29, Month.FEBRUARY.getLastDay(true));
		assertEquals(30, Month.APRIL.getLastDay(false));
	}

	// ==================== Week ====================

	@Test
	@DisplayName("星期 of")
	void week_of() {
		assertEquals(Week.SUNDAY, Week.of(1));
		assertEquals(Week.MONDAY, Week.of(2));
		assertEquals(Week.SATURDAY, Week.of(7));
	}

	@Test
	@DisplayName("星期值")
	void week_getValue() {
		assertEquals(1, Week.SUNDAY.getValue());
		assertEquals(2, Week.MONDAY.getValue());
		assertEquals(7, Week.SATURDAY.getValue());
	}

	@Test
	@DisplayName("星期 toString")
	void week_toString_name() {
		// 测试中文名称
		assertNotNull(Week.MONDAY.name());
	}

	// ==================== StopWatch ====================

	@Test
	@DisplayName("秒表启停")
	void stopWatch_startStop() {
		StopWatch sw = new StopWatch("test");
		sw.start();
		try { Thread.sleep(20); } catch (InterruptedException ignored) {}
		sw.stop();
		long elapsed = sw.getTotalTimeMillis();
		assertTrue(elapsed >= 15, "Elapsed should be at least 15ms, was: " + elapsed);
	}

	@Test
	@DisplayName("秒表总时间")
	void stopWatch_totalTime() {
		StopWatch sw = new StopWatch();
		sw.start();
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		sw.stop();
		assertTrue(sw.getTotalTimeMillis() > 0);
		assertTrue(sw.getTotalTimeNanos() > 0);
	}

	@Test
	@DisplayName("秒表任务数")
	void stopWatch_taskCount() {
		StopWatch sw = new StopWatch();
		sw.start("task1");
		sw.stop();
		sw.start("task2");
		sw.stop();
		assertEquals(2, sw.getTaskCount());
	}

	@Test
	@DisplayName("秒表 toString")
	void stopWatch_toString_nonEmpty() {
		StopWatch sw = new StopWatch("myWatch");
		sw.start("task1");
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		sw.stop();
		String str = sw.toString();
		assertNotNull(str);
		assertTrue(str.contains("myWatch"));
	}

	@Test
	@DisplayName("秒表运行中")
	void stopWatch_isRunning() {
		StopWatch sw = new StopWatch();
		assertFalse(sw.isRunning());
		sw.start();
		assertTrue(sw.isRunning());
		sw.stop();
		assertFalse(sw.isRunning());
	}

	@Test
	@DisplayName("秒表 ID")
	void stopWatch_getId() {
		StopWatch sw = new StopWatch("myId");
		assertEquals("myId", sw.getId());
	}

	@Test
	@DisplayName("秒表最后任务")
	void stopWatch_lastTaskInfo() {
		StopWatch sw = new StopWatch();
		sw.start("task1");
		try { Thread.sleep(10); } catch (InterruptedException ignored) {}
		sw.stop();
		assertEquals("task1", sw.getLastTaskName());
		assertTrue(sw.getLastTaskTimeMillis() >= 0);
		assertTrue(sw.getLastTaskTimeNanos() >= 0);
	}
}
