package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link Calculator} 单元测试.
 * <p>
 * 覆盖算术验证码表达式解析、一元负号、括号与除零异常.
 *
 * @author hongzhe.qin
 */
class CalculatorTest {

    /**
     * 验证 {@link Calculator#conversion(String)} 基本四则运算与末尾等号.
     */
    @Test
    void conversionBasic() {
        assertEquals(10.0, Calculator.conversion("7+3="), 0.001);
        assertEquals(924.0, Calculator.conversion("77 * 12"), 0.001);
        assertEquals(1.0, Calculator.conversion("1"), 0.001);
    }

    /**
     * 验证含括号与小数的复杂表达式求值.
     */
    @Test
    void conversionWithParentheses() {
        assertEquals(10.64, Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))"), 0.01);
    }

    /**
     * 验证一元负号与连续符号折叠（如 {@code --3}）.
     */
    @Test
    void conversionUnaryMinus() {
        assertEquals(3.0, Calculator.conversion("--3"), 0.001);
        assertEquals(-3.0, Calculator.conversion("-3"), 0.001);
    }

    /**
     * 验证省略乘号写法（如 {@code (11+2)12}）与 x 作乘法符.
     */
    @Test
    void conversionMultiplyX() {
        assertEquals(156.0, Calculator.conversion("(11+2)12"), 0.001);
    }

    /**
     * 验证实例方法 {@link Calculator#calculate(String)} 与静态入口一致.
     */
    @Test
    void calculateInstance() {
        final Calculator calculator = new Calculator();
        assertEquals(1.0, calculator.calculate("0+50/100x(1/0.5)"), 0.001);
    }

    /**
     * 验证除零时抛出 {@link ArithmeticException}.
     */
    @Test
    void divisionByZero() {
        assertThrows(ArithmeticException.class, () -> Calculator.conversion("1/0"));
    }
}
