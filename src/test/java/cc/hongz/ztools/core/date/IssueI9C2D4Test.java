package cc.hongz.ztools.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IssueI9C2D4 解析 HTTP 时间")

public class IssueI9C2D4Test {
    @Test
    @DisplayName("解析 HTTP 日期")
    public void parseHttpTest() {
        String dateStr = "Thu, 28 Mar 2024 14:33:49 GMT";
        final DateTime parse = DateUtil.parse(dateStr);
        assertEquals("2024-03-28 14:33:49", parse.toString());
    }

    @Test
    @DisplayName("解析 HTTP 日期（另一种）")
    public void parseHttpTest2() {
        String dateStr = "星期四, 28 三月 2024 14:33:49 GMT";
        final DateTime parse = DateUtil.parse(dateStr);
        assertEquals("2024-03-28 14:33:49", parse.toString());
    }

    @Test
    @DisplayName("解析时间")
    public void parseTimeTest() {
        String dateStr = "15时45分59秒";
        final DateTime parse = DateUtil.parse(dateStr);
        assertEquals("15:45:59", parse.toString().split(" ")[1]);
    }

    @Test
    @DisplayName("解析时间（另一种）")
    public void parseTimeTest2() {
        String dateStr = "15:45:59";
        final DateTime parse = DateUtil.parse(dateStr);
        assertEquals("15:45:59", parse.toString().split(" ")[1]);
    }
}
