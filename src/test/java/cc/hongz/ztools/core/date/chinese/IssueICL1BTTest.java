package cc.hongz.ztools.core.date.chinese;

import cc.hongz.ztools.core.date.ChineseDate;
import cc.hongz.ztools.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

@DisplayName("IssueICL1BT 农历节日")

public class IssueICL1BTTest {
    @Test
    @DisplayName("获取节日")
    void getFestivalsTest(){
        String date = "2025-07-31";
        Date productionDate = DateUtil.parseDate( date);
        ChineseDate chineseDate = new ChineseDate(productionDate);
        System.out.println(chineseDate.isLeapMonth());
        Assertions.assertTrue(chineseDate.isLeapMonth());
        String festivals = chineseDate.getFestivals();
        Assertions.assertEquals("", festivals);
    }
}
