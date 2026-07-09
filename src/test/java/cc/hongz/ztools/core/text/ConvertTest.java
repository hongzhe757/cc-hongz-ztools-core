package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link Convert} 单元测试.
 * <p>
 * 覆盖常用标量转换及阶段 1 {@link Segment} 依赖的 {@link Convert#convert(java.lang.reflect.Type, Object)}.
 *
 * @author hongzhe.qin
 */
@DisplayName("Convert 类型转换")
class ConvertTest {

    /**
     * 验证 {@link Convert#toInt(String)} 解析整数字符串.
     */
    @Test
    @DisplayName("toInt 解析字符串")
    void toIntFromString() {
        assertEquals(42, Convert.toInt("42"));
    }

    /**
     * 验证 {@link Convert#toStr(Object)} 对 null 返回 null.
     */
    @Test
    @DisplayName("toStr null 返回 null")
    void toStrNullReturnsNull() {
        assertNull(Convert.toStr(null));
    }

    /**
     * 验证 {@link Convert#toBool(String)} 识别常见真值字面量.
     */
    @Test
    @DisplayName("toBool 真值变体")
    void toBoolTrueVariants() {
        assertEquals(Boolean.TRUE, Convert.toBool("yes"));
        assertEquals(Boolean.TRUE, Convert.toBool("1"));
    }

    /**
     * 验证 {@link Convert#convert(java.lang.reflect.Type, Object)} 将 {@link java.math.BigDecimal} 转为 {@link Integer}.
     */
    @Test
    @DisplayName("convert Type → Integer")
    void convertTypeToInteger() {
        assertEquals(Integer.valueOf(15), Convert.convert(Integer.class, new java.math.BigDecimal("15")));
    }
}
