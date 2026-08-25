package cc.hongz.ztools.core.date.chinese;

import cc.hongz.ztools.core.date.ChineseDate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IssueI5YB1A 农历日期")

public class IssueI5YB1ATest {
    @Test
        @DisplayName("农历日期")
        public void chineseDateTest() {
        // 四月非闰月，因此isLeapMonth参数无效
        final ChineseDate date = new ChineseDate(2023, 4, 8, true);
        assertEquals("2023-05-26 00:00:00", date.getGregorianDate().toString());
    }
}
