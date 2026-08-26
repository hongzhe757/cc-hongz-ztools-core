package cc.hongz.ztools.core.lang.hash;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("Number128 单元测试")
class Number128Test {

    @DisplayName("构造与获取")
    @Test
    public void constructorAndGetTest() {
        Number128 num = new Number128(100L, 200L);
        assertEquals(100L, num.getLowValue());
        assertEquals(200L, num.getHighValue());
    }

    @DisplayName("setter")
    @Test
    public void setterTest() {
        Number128 num = new Number128(0L, 0L);
        num.setLowValue(123L);
        num.setHighValue(456L);
        assertEquals(123L, num.getLowValue());
        assertEquals(456L, num.getHighValue());
    }

    @DisplayName("获取高低位数组")
    @Test
    public void getLongArrayTest() {
        Number128 num = new Number128(10L, 20L);
        long[] arr = num.getLongArray();
        assertNotNull(arr);
        assertEquals(2, arr.length);
        assertEquals(10L, arr[0]); // low
        assertEquals(20L, arr[1]); // high
    }

    @DisplayName("Number 方法")
    @Test
    public void numberMethodsTest() {
        Number128 num = new Number128(42L, 0L);
        assertEquals(42, num.intValue());
        assertEquals(42L, num.longValue());
        assertEquals(42.0f, num.floatValue(), 0.001f);
        assertEquals(42.0d, num.doubleValue(), 0.001d);
    }

    @DisplayName("equals 与 hashCode")
    @Test
    public void equalsAndHashCodeTest() {
        Number128 a = new Number128(1L, 2L);
        Number128 b = new Number128(1L, 2L);
        Number128 c = new Number128(3L, 4L);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
        assertEquals(a, a); // 自身引用
    }

    @DisplayName("同一实例 equals")
    @Test
    public void equalsSameInstanceTest() {
        Number128 num = new Number128(1L, 2L);
        assertEquals(num, num);
    }
}
