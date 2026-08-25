package cc.hongz.ztools.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IssueI82Y1L 解析日期")

public class IssueI82Y1LTest {
    @Test
    @DisplayName("解析日期")
    public void parseTest() {
        final String dt1 = "2023-09-14T05:00:03.648519Z";
        assertEquals("2023-09-14 05:10:51", DateUtil.parse(dt1).toString());
    }
}
