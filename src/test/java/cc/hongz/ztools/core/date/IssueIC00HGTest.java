package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IssueIC00HG 日期转字符串")

public class IssueIC00HGTest {
    @Test
    @Disabled
    @DisplayName("日期转字符串")
    void dateToStringTest(){
        Console.log(DateUtil.date().toString());
    }
}
