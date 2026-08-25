package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Issue3348 农历格式化")

public class Issue3348Test {

    @Test
    @DisplayName("格式化农历日期")
    public void formatChineseDateTest() {
        final String formatChineseDate = DateUtil.formatChineseDate(
            DateUtil.parse("2023-10-23"), true, false);
        Console.log(formatChineseDate);
        assertEquals("二〇二三年十月二十三日", formatChineseDate);
    }
}
