package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link NumberChineseFormatter} 单元测试.
 */
class NumberChineseFormatterTest {

    @Test
    void format_simpleInteger() {
        assertEquals("一百二十一", NumberChineseFormatter.format(121, false));
    }

    @Test
    void format_zero() {
        assertEquals("零", NumberChineseFormatter.format(0, false));
    }

    @Test
    void format_negative() {
        assertEquals("负一百二十一", NumberChineseFormatter.format(-121, false));
    }

    @Test
    void format_traditional() {
        assertEquals("壹佰贰拾壹", NumberChineseFormatter.format(121, true));
    }

    @Test
    void format_largeNumber() {
        assertEquals("一万二千三百四十五", NumberChineseFormatter.format(12345, false));
    }

    @Test
    void format_withDecimal() {
        String result = NumberChineseFormatter.format(123.45, false);
        assertTrue(result.contains("一百二十三"));
    }

    @Test
    void format_moneyMode() {
        String result = NumberChineseFormatter.format(100.00, false, true);
        assertEquals("一百元整", result);
    }

    @Test
    void format_moneyModeWithJiaoFen() {
        String result = NumberChineseFormatter.format(123.45, false, true);
        assertTrue(result.contains("元"));
        assertTrue(result.contains("角"));
        assertTrue(result.contains("分"));
    }

    @Test
    void formatSimple_thousands() {
        assertEquals("1234", NumberChineseFormatter.formatSimple(1234));
    }

    @Test
    void formatSimple_wan() {
        String result = NumberChineseFormatter.formatSimple(12345);
        assertTrue(result.contains("万"));
    }

    @Test
    void formatThousand_smallNumber() {
        assertEquals("十一", NumberChineseFormatter.formatThousand(11, false));
    }

    @Test
    void formatThousand_largeNumber() {
        assertEquals("九百九十九", NumberChineseFormatter.formatThousand(999, false));
    }

    @Test
    void chineseToNumber_simple() {
        assertEquals(121, NumberChineseFormatter.chineseToNumber("一百二十一"));
    }

    @Test
    void chineseToNumber_withZero() {
        assertEquals(1012, NumberChineseFormatter.chineseToNumber("一千零一十二"));
    }

    @Test
    void chineseMoneyToNumber() {
        BigDecimal result = NumberChineseFormatter.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元叁角贰分");
        assertEquals(0, new BigDecimal("67556.32").compareTo(result));
    }

    @Test
    void formatBigDecimal() {
        BigDecimal amount = new BigDecimal("123.456");
        String result = NumberChineseFormatter.format(amount, false, false);
        assertNotNull(result);
        assertTrue(result.contains("一百二十三"));
    }
}
