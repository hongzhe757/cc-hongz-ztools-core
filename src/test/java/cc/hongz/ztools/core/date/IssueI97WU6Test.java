package cc.hongz.ztools.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IssueI97WU6 节气")

public class IssueI97WU6Test {
    @Test
    @DisplayName("获取节气")
    public void getTermTest() {
        // 润十月没有三十，十月有三十
        final ChineseDate chineseDate = new ChineseDate(1984, 10, 30, false);
        assertEquals("甲子鼠年 寒月三十", chineseDate.toString());
        assertEquals("小雪", chineseDate.getTerm());
    }
}
