package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.util.NumberUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("Issue2611 中文金额转数字")
public class Issue2611Test {

    @Test
    @DisplayName("中文金额转数字")
    public void chineseMoneyToNumberTest() {
        final BigDecimal value = Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾柒元");

        assertEquals("67,557.00", NumberUtil.decimalFormatMoney(value.doubleValue()));
    }
}
