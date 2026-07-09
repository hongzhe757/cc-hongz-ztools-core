package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link NumberUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法，含阶段 1 {@link Segment} 依赖的 Number 减法.
 *
 * @author hongzhe.qin
 */
@DisplayName("NumberUtil 精确运算")
class NumberUtilTest {

    /**
     * 验证 {@link NumberUtil#toBigDecimal(String)} 对 null 与空串返回零.
     */
    @Test
    void toBigDecimalEmpty() {
        assertEquals(BigDecimal.ZERO, NumberUtil.toBigDecimal(""));
        assertEquals(BigDecimal.ZERO, NumberUtil.toBigDecimal((String) null));
    }

    /**
     * 验证 {@link NumberUtil#toBigDecimal(String)} 解析合法数字.
     */
    @Test
    void toBigDecimalValidNumber() {
        assertEquals(new BigDecimal("3.14"), NumberUtil.toBigDecimal("3.14"));
    }

    /**
     * 验证 {@link NumberUtil#add(String, String)}、{@link NumberUtil#sub(String, String)}、
     * {@link NumberUtil#mul(String, String)}、{@link NumberUtil#div(String, String)} 基本运算.
     */
    @Test
    void addSubMulDiv() {
        assertEquals(new BigDecimal("7"), NumberUtil.add("3", "4"));
        assertEquals(new BigDecimal("-1"), NumberUtil.sub("3", "4"));
        assertEquals(new BigDecimal("12"), NumberUtil.mul("3", "4"));
        assertEquals(new BigDecimal("0.75"), NumberUtil.div("3", "4"));
    }

    /**
     * 验证 {@link NumberUtil#mul(String...)} 可变参数连乘与空参数.
     */
    @Test
    void mulVarargs() {
        assertEquals(new BigDecimal("24"), NumberUtil.mul("2", "3", "4"));
        assertEquals(BigDecimal.ZERO, NumberUtil.mul());
        assertEquals(new BigDecimal("5"), NumberUtil.mul("5"));
    }

    /**
     * 验证 {@link NumberUtil#sub(Number, Number)} 与 {@link NumberUtil#sub(Number...)}.
     */
    @Test
    void subNumber() {
        assertEquals(new BigDecimal("7"), NumberUtil.sub(10, 3));
        assertEquals(new BigDecimal("5"), NumberUtil.sub(10, 3, 2));
    }

    /**
     * 验证 {@link NumberUtil#toBigDecimal(Number)} 对 null 返回零.
     */
    @Test
    void toBigDecimalNumberNull() {
        assertEquals(BigDecimal.ZERO, NumberUtil.toBigDecimal((Number) null));
    }
}
