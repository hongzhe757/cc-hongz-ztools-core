package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("IssueIALV38 带空格字符串转数字")
public class IssueIALV38Test {

    @Test
    @DisplayName("带空格字符串转 BigDecimal")
    void name() {
        final Object o = Convert.convertWithCheck(BigDecimal.class, " 111啊", null, false);
        Assertions.assertEquals(new BigDecimal("111"), o);
    }
}
