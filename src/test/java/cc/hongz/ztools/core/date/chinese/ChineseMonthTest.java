package cc.hongz.ztools.core.date.chinese;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ChineseMonth 单元测试.
 */
@DisplayName("ChineseMonth 农历月份")
class ChineseMonthTest {

    @Test
    @DisplayName("是否闰月")
    void isLeapMonth() {
        assertTrue(ChineseMonth.isLeapMonth(2023, 2));
        assertFalse(ChineseMonth.isLeapMonth(2023, 1));
        assertFalse(ChineseMonth.isLeapMonth(2024, 2));
    }

    @Test
    @DisplayName("获取农历月称呼")
    void getChineseMonthName() {
        assertEquals("一月", ChineseMonth.getChineseMonthName(false, 1, false));
        assertEquals("正月", ChineseMonth.getChineseMonthName(false, 1, true));
        assertEquals("十二月", ChineseMonth.getChineseMonthName(false, 12, false));
        assertEquals("腊月", ChineseMonth.getChineseMonthName(false, 12, true));
        assertEquals("闰二月", ChineseMonth.getChineseMonthName(true, 2, false));
    }
}
