package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MutableLong} 单元测试.
 */
@DisplayName("MutableLong 单元测试")
class MutableLongTest {

    @DisplayName("默认构造")
    @Test
    void defaultConstructor() {
        MutableLong m = new MutableLong();
        assertEquals(0L, m.longValue());
    }

    @DisplayName("long 构造")
    @Test
    void longConstructor() {
        MutableLong m = new MutableLong(100L);
        assertEquals(100L, m.longValue());
        assertEquals(Long.valueOf(100L), m.get());
    }

    @DisplayName("Number 构造")
    @Test
    void numberConstructor() {
        MutableLong m = new MutableLong(Long.valueOf(50L));
        assertEquals(50L, m.longValue());
    }

    @DisplayName("字符串构造")
    @Test
    void stringConstructor() {
        MutableLong m = new MutableLong("12345");
        assertEquals(12345L, m.longValue());
    }

    @DisplayName("非法字符串构造")
    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableLong("abc"));
    }

    @DisplayName("设置与获取")
    @Test
    void setAndGet() {
        MutableLong m = new MutableLong();
        m.set(999L);
        assertEquals(999L, m.get());
    }

    @DisplayName("自增与自减")
    @Test
    void incrementAndDecrement() {
        MutableLong m = new MutableLong(5L);
        assertSame(m, m.increment());
        assertEquals(6L, m.longValue());
        assertSame(m, m.decrement());
        assertEquals(5L, m.longValue());
    }

    @DisplayName("加与减")
    @Test
    void addAndSubtract() {
        MutableLong m = new MutableLong(10L);
        m.add(5L);
        assertEquals(15L, m.longValue());
        m.subtract(3L);
        assertEquals(12L, m.longValue());
        m.add(Long.valueOf(1L));
        assertEquals(13L, m.longValue());
        m.subtract(Long.valueOf(3L));
        assertEquals(10L, m.longValue());
    }

    @DisplayName("Number 各类型值")
    @Test
    void numberValues() {
        MutableLong m = new MutableLong(7L);
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.0f, m.floatValue());
        assertEquals(7.0d, m.doubleValue());
    }

    @DisplayName("equals 与 hashCode")
    @Test
    void equalsAndHashCode() {
        MutableLong a = new MutableLong(10L);
        MutableLong b = new MutableLong(10L);
        MutableLong c = new MutableLong(20L);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
    }

    @DisplayName("比较大小")
    @Test
    void compareTo() {
        MutableLong a = new MutableLong(10L);
        MutableLong b = new MutableLong(20L);
        MutableLong c = new MutableLong(10L);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @DisplayName("toString")
    @Test
    void toStringMethod() {
        assertEquals("42", new MutableLong(42L).toString());
    }
}
