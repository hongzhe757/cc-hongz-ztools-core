package cc.hongz.ztools.core.date.chinese;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LunarInfo 单元测试.
 */
@DisplayName("LunarInfo 农历信息")
class LunarInfoTest {

    @Test
    @DisplayName("闰月")
    void leapMonth() {
        assertEquals(2, LunarInfo.leapMonth(2023));
        assertEquals(0, LunarInfo.leapMonth(2024));
    }

    @Test
    @DisplayName("农历年天数")
    void yearDays() {
        assertEquals(384, LunarInfo.yearDays(2023));
        assertEquals(354, LunarInfo.yearDays(2024));
    }

    @Test
    @DisplayName("闰月天数")
    void leapDays() {
        assertTrue(LunarInfo.leapDays(2023) >= 29);
        assertEquals(0, LunarInfo.leapDays(2024));
    }

    @Test
    @DisplayName("月天数")
    void monthDays() {
        assertTrue(LunarInfo.monthDays(2023, 1) >= 29 && LunarInfo.monthDays(2023, 1) <= 30);
    }
}
