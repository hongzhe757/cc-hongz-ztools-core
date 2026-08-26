package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MutableByte} 单元测试.
 */
@DisplayName("MutableByte 单元测试")
class MutableByteTest {

    @DisplayName("默认构造")
    @Test
    void defaultConstructor() {
        MutableByte m = new MutableByte();
        assertEquals((byte) 0, m.byteValue());
    }

    @DisplayName("byte 构造")
    @Test
    void byteConstructor() {
        MutableByte m = new MutableByte((byte) 10);
        assertEquals((byte) 10, m.byteValue());
        assertEquals(Byte.valueOf((byte) 10), m.get());
    }

    @DisplayName("Number 构造")
    @Test
    void numberConstructor() {
        MutableByte m = new MutableByte(Byte.valueOf((byte) 5));
        assertEquals((byte) 5, m.byteValue());
    }

    @DisplayName("字符串构造")
    @Test
    void stringConstructor() {
        MutableByte m = new MutableByte("12");
        assertEquals((byte) 12, m.byteValue());
    }

    @DisplayName("非法字符串构造")
    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableByte("abc"));
    }

    @DisplayName("设置与获取")
    @Test
    void setAndGet() {
        MutableByte m = new MutableByte();
        m.set((byte) 99);
        assertEquals((byte) 99, m.get());
    }

    @DisplayName("设置 Number")
    @Test
    void setNumber() {
        MutableByte m = new MutableByte();
        m.set(Byte.valueOf((byte) 55));
        assertEquals((byte) 55, m.byteValue());
    }

    @DisplayName("自增与自减")
    @Test
    void incrementAndDecrement() {
        MutableByte m = new MutableByte((byte) 5);
        assertSame(m, m.increment());
        assertEquals((byte) 6, m.byteValue());
        assertSame(m, m.decrement());
        assertEquals((byte) 5, m.byteValue());
    }

    @DisplayName("加与减")
    @Test
    void addAndSubtract() {
        MutableByte m = new MutableByte((byte) 10);
        m.add((byte) 5);
        assertEquals((byte) 15, m.byteValue());
        m.subtract((byte) 3);
        assertEquals((byte) 12, m.byteValue());
        m.add(Byte.valueOf((byte) 1));
        assertEquals((byte) 13, m.byteValue());
        m.subtract(Byte.valueOf((byte) 3));
        assertEquals((byte) 10, m.byteValue());
    }

    @DisplayName("Number 各类型值")
    @Test
    void numberValues() {
        MutableByte m = new MutableByte((byte) 7);
        assertEquals((byte) 7, m.byteValue());
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.0f, m.floatValue());
        assertEquals(7.0d, m.doubleValue());
    }

    @DisplayName("equals 与 hashCode")
    @Test
    void equalsAndHashCode() {
        MutableByte a = new MutableByte((byte) 10);
        MutableByte b = new MutableByte((byte) 10);
        MutableByte c = new MutableByte((byte) 20);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, Byte.valueOf((byte) 10));
    }

    @DisplayName("比较大小")
    @Test
    void compareTo() {
        MutableByte a = new MutableByte((byte) 10);
        MutableByte b = new MutableByte((byte) 20);
        MutableByte c = new MutableByte((byte) 10);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @DisplayName("toString")
    @Test
    void toStringMethod() {
        assertEquals("42", new MutableByte((byte) 42).toString());
    }
}
