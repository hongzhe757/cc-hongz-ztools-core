package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MutableFloat} 单元测试.
 */
@DisplayName("MutableFloat 单元测试")
class MutableFloatTest {

    @DisplayName("默认构造")
    @Test
    void defaultConstructor() {
        MutableFloat m = new MutableFloat();
        assertEquals(0.0f, m.floatValue());
    }

    @DisplayName("float 构造")
    @Test
    void floatConstructor() {
        MutableFloat m = new MutableFloat(3.14f);
        assertEquals(3.14f, m.floatValue());
        assertEquals(Float.valueOf(3.14f), m.get());
    }

    @DisplayName("Number 构造")
    @Test
    void numberConstructor() {
        MutableFloat m = new MutableFloat(Float.valueOf(2.5f));
        assertEquals(2.5f, m.floatValue());
    }

    @DisplayName("字符串构造")
    @Test
    void stringConstructor() {
        MutableFloat m = new MutableFloat("1.5");
        assertEquals(1.5f, m.floatValue());
    }

    @DisplayName("非法字符串构造")
    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableFloat("abc"));
    }

    @DisplayName("设置与获取")
    @Test
    void setAndGet() {
        MutableFloat m = new MutableFloat();
        m.set(9.9f);
        assertEquals(9.9f, m.get());
    }

    @DisplayName("自增与自减")
    @Test
    void incrementAndDecrement() {
        MutableFloat m = new MutableFloat(5.0f);
        assertSame(m, m.increment());
        assertEquals(6.0f, m.floatValue());
        assertSame(m, m.decrement());
        assertEquals(5.0f, m.floatValue());
    }

    @DisplayName("加与减")
    @Test
    void addAndSubtract() {
        MutableFloat m = new MutableFloat(10.0f);
        m.add(5.0f);
        assertEquals(15.0f, m.floatValue());
        m.subtract(3.0f);
        assertEquals(12.0f, m.floatValue());
        m.add(Float.valueOf(1.0f));
        assertEquals(13.0f, m.floatValue());
        m.subtract(Float.valueOf(3.0f));
        assertEquals(10.0f, m.floatValue());
    }

    @DisplayName("Number 各类型值")
    @Test
    void numberValues() {
        MutableFloat m = new MutableFloat(7.5f);
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.5f, m.floatValue());
        assertEquals(7.5, m.doubleValue(), 0.001);
    }

    @DisplayName("equals 与 hashCode")
    @Test
    void equalsAndHashCode() {
        MutableFloat a = new MutableFloat(10.0f);
        MutableFloat b = new MutableFloat(10.0f);
        MutableFloat c = new MutableFloat(20.0f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
    }

    @DisplayName("比较大小")
    @Test
    void compareTo() {
        MutableFloat a = new MutableFloat(10.0f);
        MutableFloat b = new MutableFloat(20.0f);
        MutableFloat c = new MutableFloat(10.0f);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @DisplayName("toString")
    @Test
    void toStringMethod() {
        assertEquals("3.14", new MutableFloat(3.14f).toString());
    }
}
