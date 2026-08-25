package cc.hongz.ztools.core.date.chinese;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LunarFestival 单元测试.
 */
@DisplayName("LunarFestival 农历节日")
class LunarFestivalTest {

    @Test
    @DisplayName("获取节日")
    void getFestivals() {
        assertEquals(List.of("春节"), LunarFestival.getFestivals(1, 1));
        assertEquals(List.of("元宵节 上元节"), LunarFestival.getFestivals(1, 15));
        assertEquals(List.of("端午节 端阳节"), LunarFestival.getFestivals(5, 5));
        assertEquals(List.of("中秋节"), LunarFestival.getFestivals(8, 15));
        assertEquals(List.of("重阳节"), LunarFestival.getFestivals(9, 9));
    }

    @Test
    @DisplayName("获取除夕")
    void getFestivalsEve() {
        assertEquals(List.of("除夕"), LunarFestival.getFestivals(12, 30));
    }
}
