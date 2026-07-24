package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableShort} 单元测试.
 */
class MutableShortTest {

    @Test
    void defaultConstructor() {
        MutableShort m = new MutableShort();
        assertEquals((short) 0, m.shortValue());
    }

    @Test
    void shortConstructor() {
        MutableShort m = new MutableShort((short) 100);
        assertEquals((short) 100, m.shortValue());
        assertEquals(Short.valueOf((short) 100), m.get());
    }

    @Test
    void numberConstructor() {
        MutableShort m = new MutableShort(Short.valueOf((short) 50));
        assertEquals((short) 50, m.shortValue());
    }

    @Test
    void stringConstructor() {
        MutableShort m = new MutableShort("123");
        assertEquals((short) 123, m.shortValue());
    }

    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableShort("abc"));
    }

    @Test
    void setAndGet() {
        MutableShort m = new MutableShort();
        m.set((short) 999);
        assertEquals((short) 999, m.get());
    }

    @Test
    void incrementAndDecrement() {
        MutableShort m = new MutableShort((short) 5);
        assertSame(m, m.increment());
        assertEquals((short) 6, m.shortValue());
        assertSame(m, m.decrement());
        assertEquals((short) 5, m.shortValue());
    }

    @Test
    void addAndSubtract() {
        MutableShort m = new MutableShort((short) 10);
        m.add((short) 5);
        assertEquals((short) 15, m.shortValue());
        m.subtract((short) 3);
        assertEquals((short) 12, m.shortValue());
        m.add(Short.valueOf((short) 1));
        assertEquals((short) 13, m.shortValue());
        m.subtract(Short.valueOf((short) 3));
        assertEquals((short) 10, m.shortValue());
    }

    @Test
    void numberValues() {
        MutableShort m = new MutableShort((short) 7);
        assertEquals((short) 7, m.shortValue());
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.0f, m.floatValue());
        assertEquals(7.0d, m.doubleValue());
    }

    @Test
    void equalsAndHashCode() {
        MutableShort a = new MutableShort((short) 10);
        MutableShort b = new MutableShort((short) 10);
        MutableShort c = new MutableShort((short) 20);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
    }

    @Test
    void compareTo() {
        MutableShort a = new MutableShort((short) 10);
        MutableShort b = new MutableShort((short) 20);
        MutableShort c = new MutableShort((short) 10);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @Test
    void toStringMethod() {
        assertEquals("42", new MutableShort((short) 42).toString());
    }
}
