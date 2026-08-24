package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link NumberChineseFormatter} 单元测试.
 */
@DisplayName("NumberChineseFormatter 中文数字格式化")
class NumberChineseFormatterTest {

    @Test
    @DisplayName("简单整数")
    void format_simpleInteger() {
        assertEquals("一百二十一", NumberChineseFormatter.format(121, false));
    }

    @Test
    @DisplayName("零")
    void format_zero() {
        assertEquals("零", NumberChineseFormatter.format(0, false));
    }

    @Test
    @DisplayName("负数")
    void format_negative() {
        assertEquals("负一百二十一", NumberChineseFormatter.format(-121, false));
    }

    @Test
    @DisplayName("繁体")
    void format_traditional() {
        assertEquals("壹佰贰拾壹", NumberChineseFormatter.format(121, true));
    }

    @Test
    @DisplayName("大数字")
    void format_largeNumber() {
        assertEquals("一万二千三百四十五", NumberChineseFormatter.format(12345, false));
    }

    @Test
    @DisplayName("带小数")
    void format_withDecimal() {
        String result = NumberChineseFormatter.format(123.45, false);
        assertTrue(result.contains("一百二十三"));
    }

    @Test
    @DisplayName("金额模式")
    void format_moneyMode() {
        String result = NumberChineseFormatter.format(100.00, false, true);
        assertEquals("一百元整", result);
    }

    @Test
    @DisplayName("金额模式（带角分）")
    void format_moneyModeWithJiaoFen() {
        String result = NumberChineseFormatter.format(123.45, false, true);
        assertTrue(result.contains("元"));
        assertTrue(result.contains("角"));
        assertTrue(result.contains("分"));
    }

    @Test
    @DisplayName("formatSimple - 千位")
    void formatSimple_thousands() {
        assertEquals("1234", NumberChineseFormatter.formatSimple(1234));
    }

    @Test
    @DisplayName("formatSimple - 万")
    void formatSimple_wan() {
        String result = NumberChineseFormatter.formatSimple(12345);
        assertTrue(result.contains("万"));
    }

    @Test
    @DisplayName("formatThousand - 小数字")
    void formatThousand_smallNumber() {
        assertEquals("十一", NumberChineseFormatter.formatThousand(11, false));
    }

    @Test
    @DisplayName("formatThousand - 大数字")
    void formatThousand_largeNumber() {
        assertEquals("九百九十九", NumberChineseFormatter.formatThousand(999, false));
    }

    @Test
    @DisplayName("中文转数字")
    void chineseToNumber_simple() {
        assertEquals(121, NumberChineseFormatter.chineseToNumber("一百二十一"));
    }

    @Test
    @DisplayName("中文转数字（带零）")
    void chineseToNumber_withZero() {
        assertEquals(1012, NumberChineseFormatter.chineseToNumber("一千零一十二"));
    }

    @Test
    @DisplayName("中文金额转数字")
    void chineseMoneyToNumber() {
        BigDecimal result = NumberChineseFormatter.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元叁角贰分");
        assertEquals(0, new BigDecimal("67556.32").compareTo(result));
    }

    @Test
    @DisplayName("BigDecimal 格式化")
    void formatBigDecimal() {
        BigDecimal amount = new BigDecimal("123.456");
        String result = NumberChineseFormatter.format(amount, false, false);
        assertNotNull(result);
        assertTrue(result.contains("一百二十三"));
    }
}
