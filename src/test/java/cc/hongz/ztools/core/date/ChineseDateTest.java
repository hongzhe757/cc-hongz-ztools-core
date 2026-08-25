package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.date.chinese.GanZhi;
import cc.hongz.ztools.core.date.chinese.SolarTerms;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ChineseDate / GanZhi / SolarTerms 单元测试
 */
@DisplayName("ChineseDate 农历日期")
public class ChineseDateTest {

	// ==================== ChineseDate ====================

	@Test
	@DisplayName("农历基础")
	public void chineseDate_basic() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-01-25"));
		assertEquals("2020-01-25 00:00:00", date.getGregorianDate().toString());
		assertEquals(2020, date.getChineseYear());
		assertEquals(1, date.getMonth());
		assertEquals("一月", date.getChineseMonth());
		assertEquals("正月", date.getChineseMonthName());
		assertEquals(1, date.getDay());
		assertEquals("初一", date.getChineseDay());
		assertEquals("庚子", date.getCyclical());
		assertEquals("鼠", date.getChineseZodiac());
		assertEquals("春节", date.getFestivals());
		assertEquals("庚子鼠年 正月初一", date.toString());
	}

	@Test
	@DisplayName("除夕")
	public void chineseDate_lunarNewYearEve() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-01-14"));
		assertEquals("己亥猪年 腊月二十", date.toString());
		date = new ChineseDate(DateUtil.parseDate("2020-01-24"));
		assertEquals("己亥猪年 腊月三十", date.toString());
		assertEquals("2019-12-30", date.toStringNormal());
	}

	@Test
	@DisplayName("toString 普通")
	public void chineseDate_toStringNormal() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("2020-03-1"));
		assertEquals("2020-02-08", date.toStringNormal());
	}

	@Test
	@DisplayName("解析 1996")
	public void chineseDate_parse1996() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1996-07-14"));
		assertEquals("丙子鼠年 五月廿九", date.toString());
		date = new ChineseDate(DateUtil.parseDate("1996-07-15"));
		assertEquals("丙子鼠年 五月三十", date.toString());
	}

	@Test
	@DisplayName("获取农历月")
	public void chineseDate_getChineseMonth() {
		ChineseDate chineseDate = new ChineseDate(2020, 6, 15);
		assertEquals("2020-08-04 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("六月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020, 4, 15);
		assertEquals("2020-06-06 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("闰四月", chineseDate.getChineseMonth());

		chineseDate = new ChineseDate(2020, 5, 15);
		assertEquals("2020-07-05 00:00:00", chineseDate.getGregorianDate().toString());
		assertEquals("五月", chineseDate.getChineseMonth());
	}

	@Test
	@DisplayName("1991 农历")
	public void chineseDate_1991() {
		ChineseDate date = new ChineseDate(DateUtil.parseDate("1991-09-14"));
		assertEquals("辛未羊年 八月初七", date.toString());
		date = new ChineseDate(DateUtil.parseDate("1991-09-15"));
		assertEquals("辛未羊年 八月初八", date.toString());
	}

	@Test
	@DisplayName("闰月")
	public void chineseDate_leapMonth() {
		ChineseDate c1 = new ChineseDate(DateUtil.parse("2028-05-28"));
		ChineseDate c2 = new ChineseDate(DateUtil.parse("2028-06-27"));
		assertEquals("戊申猴年 五月初五", c1.toString());
		assertEquals("戊申猴年 闰五月初五", c2.toString());
	}

	@Test
	@DisplayName("获取农历月（二）")
	public void chineseDate_getChineseMonthTest2() {
		ChineseDate springFestival = new ChineseDate(DateUtil.parseDate("2022-02-01"));
		assertEquals("一月", springFestival.getChineseMonth());
	}

	@Test
	@DisplayName("1970-01-01")
	public void chineseDate_day19700101() {
		Date date = DateUtil.parse("1970-01-01");
		ChineseDate chineseDate = new ChineseDate(date);
		assertEquals("己酉鸡年 冬月廿四", chineseDate.toString());

		date = DateUtil.parse("1970-01-02");
		chineseDate = new ChineseDate(date);
		assertEquals("己酉鸡年 冬月廿五", chineseDate.toString());
	}

	@Test
	@DisplayName("1900-01-31")
	public void chineseDate_day19000131() {
		Date date = DateUtil.parse("1900-01-31");
		ChineseDate chineseDate = new ChineseDate(date);
		assertEquals("庚子鼠年 正月初一", chineseDate.toString());
	}

	@Test
	@DisplayName("获取公历日期")
	public void chineseDate_getGregorianDate() {
		ChineseDate chineseDate = new ChineseDate(1998, 5, 1);
		assertEquals("1998-06-24 00:00:00", chineseDate.getGregorianDate().toString());

		chineseDate = new ChineseDate(1998, 5, 1, false);
		assertEquals("1998-05-26 00:00:00", chineseDate.getGregorianDate().toString());
	}

	// ==================== GanZhi ====================

	@Test
	@DisplayName("干支年份")
	public void ganzhi_ofYear() {
		assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
	}

	@Test
	@DisplayName("干支公历")
	public void ganzhi_cyclicalYMD_gregorian() {
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1993-01-06"));
		assertEquals("壬申年癸丑月丁亥日", chineseDate.getCyclicalYMD());
	}

	@Test
	@DisplayName("干支农历")
	public void ganzhi_cyclicalYMD_lunar() {
		ChineseDate chineseDate = new ChineseDate(1992, 12, 14);
		assertEquals("壬申年癸丑月丁亥日", chineseDate.getCyclicalYMD());
	}

	@Test
	@DisplayName("干支 2020")
	public void ganzhi_cyclicalYMD_2020() {
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2020-08-28"));
		assertEquals("庚子年甲申月癸卯日", chineseDate.getCyclicalYMD());
	}

	@Test
	@DisplayName("干支 1905")
	public void ganzhi_cyclicalYMD_1905() {
		ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1905-08-28"));
		assertEquals("乙巳年甲申月己亥日", chineseDate.getCyclicalYMD());
	}

	// ==================== SolarTerms ====================

	@Test
	@DisplayName("节气序号")
	public void solarTerms_getTerm_int() {
		assertEquals(4, SolarTerms.getTerm(1987, 3));
	}

	@Test
	@DisplayName("2021 全节气")
	public void solarTerms_getTerm_all2021() {
		assertEquals("小寒", SolarTerms.getTerm(2021, 1, 5));
		assertEquals("大寒", SolarTerms.getTerm(2021, 1, 20));
		assertEquals("立春", SolarTerms.getTerm(2021, 2, 3));
		assertEquals("雨水", SolarTerms.getTerm(2021, 2, 18));
		assertEquals("惊蛰", SolarTerms.getTerm(2021, 3, 5));
		assertEquals("春分", SolarTerms.getTerm(2021, 3, 20));
		assertEquals("清明", SolarTerms.getTerm(2021, 4, 4));
		assertEquals("谷雨", SolarTerms.getTerm(2021, 4, 20));
		assertEquals("立夏", SolarTerms.getTerm(2021, 5, 5));
		assertEquals("小满", SolarTerms.getTerm(2021, 5, 21));
		assertEquals("芒种", SolarTerms.getTerm(2021, 6, 5));
		assertEquals("夏至", SolarTerms.getTerm(2021, 6, 21));
		assertEquals("小暑", SolarTerms.getTerm(2021, 7, 7));
		assertEquals("大暑", SolarTerms.getTerm(2021, 7, 22));
		assertEquals("立秋", SolarTerms.getTerm(2021, 8, 7));
		assertEquals("处暑", SolarTerms.getTerm(2021, 8, 23));
		assertEquals("白露", SolarTerms.getTerm(2021, 9, 7));
		assertEquals("秋分", SolarTerms.getTerm(2021, 9, 23));
		assertEquals("寒露", SolarTerms.getTerm(2021, 10, 8));
		assertEquals("霜降", SolarTerms.getTerm(2021, 10, 23));
		assertEquals("立冬", SolarTerms.getTerm(2021, 11, 7));
		assertEquals("小雪", SolarTerms.getTerm(2021, 11, 22));
		assertEquals("大雪", SolarTerms.getTerm(2021, 12, 7));
		assertEquals("冬至", SolarTerms.getTerm(2021, 12, 21));
	}

	@Test
	@DisplayName("按日期节气")
	public void solarTerms_getTerm_byDate() {
		assertEquals("春分", SolarTerms.getTerm(DateUtil.parseDate("2021-03-20")));
		assertEquals("处暑", SolarTerms.getTerm(DateUtil.parseDate("2022-08-23")));
	}

	@Test
	@DisplayName("按农历节气")
	public void solarTerms_getTerm_byChineseDate() {
		assertEquals("清明", SolarTerms.getTerm(new ChineseDate(2021, 2, 23)));
		assertEquals("秋分", SolarTerms.getTerm(new ChineseDate(2022, 8, 28)));
	}
}
