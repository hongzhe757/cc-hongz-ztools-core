package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.date.chinese.GanZhi;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GanZhi 干支")

public class GanzhiTest {

    @Test
    @DisplayName("获取年份干支")
    public void getGanzhiOfYearTest(){
        assertEquals("庚子", GanZhi.getGanzhiOfYear(2020));
    }

    @Test
    @DisplayName("公历转干支年月日")
    public void getCyclicalYMDTest(){
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1993-01-06"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
    }

    @Test
    @DisplayName("农历转干支年月日")
    public void getCyclicalYMDTest2(){
        //通过农历构建
        ChineseDate chineseDate = new ChineseDate(1992,12,14);
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        assertEquals("壬申年癸丑月丁亥日",cyclicalYMD);
    }

    @Test
    @DisplayName("公历转干支年月日（2020）")
    public void getCyclicalYMDTest3(){
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2020-08-28"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        assertEquals("庚子年甲申月癸卯日",cyclicalYMD);
    }

    @Test
    @DisplayName("公历转干支年月日（1905）")
    public void getCyclicalYMDTest4(){
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1905-08-28"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        assertEquals("乙巳年甲申月己亥日",cyclicalYMD);
    }
}
