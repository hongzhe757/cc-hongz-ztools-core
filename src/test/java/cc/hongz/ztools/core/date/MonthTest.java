package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Month 月份")

public class MonthTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    @DisplayName("获取每月最后一天")
    public void getLastDayTest(){
        int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.FEBRUARY).getLastDay(false);
        assertEquals(28, lastDay);
        lastDay = Month.of(Calendar.FEBRUARY).getLastDay(true);
        assertEquals(29, lastDay);
        lastDay = Month.of(Calendar.MARCH).getLastDay(true);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.APRIL).getLastDay(true);
        assertEquals(30, lastDay);
        lastDay = Month.of(Calendar.MAY).getLastDay(true);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.JUNE).getLastDay(true);
        assertEquals(30, lastDay);
        lastDay = Month.of(Calendar.JULY).getLastDay(true);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.AUGUST).getLastDay(true);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.SEPTEMBER).getLastDay(true);
        assertEquals(30, lastDay);
        lastDay = Month.of(Calendar.OCTOBER).getLastDay(true);
        assertEquals(31, lastDay);
        lastDay = Month.of(Calendar.NOVEMBER).getLastDay(true);
        assertEquals(30, lastDay);
        lastDay = Month.of(Calendar.DECEMBER).getLastDay(true);
        assertEquals(31, lastDay);
    }

    @Test
    @DisplayName("转 JDK Month")
    public void toJdkMonthTest(){
        final java.time.Month month = Month.AUGUST.toJdkMonth();
        assertEquals(java.time.Month.AUGUST, month);
    }

    @Test
    @DisplayName("非法月份转 JDK Month 抛异常")
    public void toJdkMonthTest2(){
        assertThrows(IllegalArgumentException.class, Month.UNDECIMBER::toJdkMonth);
    }

    @Test
    @DisplayName("of 解析月份")
    public void ofTest(){
        Month month = Month.of("Jan");
        assertEquals(Month.JANUARY, month);

        month = Month.of("JAN");
        assertEquals(Month.JANUARY, month);

        month = Month.of("FEBRUARY");
        assertEquals(Month.FEBRUARY, month);

        month = Month.of("February");
        assertEquals(Month.FEBRUARY, month);

        month = Month.of(java.time.Month.FEBRUARY);
        assertEquals(Month.FEBRUARY, month);
    }

    @Test
    @DisplayName("获取显示名称")
    void getDisplayNameTest() {
        final String displayName = Month.FEBRUARY.getDisplayName(TextStyle.SHORT, Locale.US);
        Assertions.assertEquals("Feb", displayName);
    }
}
