package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableDouble} 单元测试.
 */
class MutableDoubleTest {

    @Test
    void defaultConstructor() {
        MutableDouble m = new MutableDouble();
        assertEquals(0.0d, m.doubleValue());
    }

    @Test
    void doubleConstructor() {
        MutableDouble m = new MutableDouble(3.14);
        assertEquals(3.14, m.doubleValue());
        assertEquals(Double.valueOf(3.14), m.get());
    }

    @Test
    void numberConstructor() {
        MutableDouble m = new MutableDouble(Double.valueOf(2.5));
        assertEquals(2.5, m.doubleValue());
    }

    @Test
    void stringConstructor() {
        MutableDouble m = new MutableDouble("1.5");
        assertEquals(1.5, m.doubleValue());
    }

    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableDouble("abc"));
    }

    @Test
    void setAndGet() {
        MutableDouble m = new MutableDouble();
        m.set(9.9);
        assertEquals(9.9, m.get());
    }

    @Test
    void incrementAndDecrement() {
        MutableDouble m = new MutableDouble(5.0);
        assertSame(m, m.increment());
        assertEquals(6.0, m.doubleValue());
        assertSame(m, m.decrement());
        assertEquals(5.0, m.doubleValue());
    }

    @Test
    void addAndSubtract() {
        MutableDouble m = new MutableDouble(10.0);
        m.add(5.0);
        assertEquals(15.0, m.doubleValue());
        m.subtract(3.0);
        assertEquals(12.0, m.doubleValue());
        m.add(Double.valueOf(1.0));
        assertEquals(13.0, m.doubleValue());
        m.subtract(Double.valueOf(3.0));
        assertEquals(10.0, m.doubleValue());
    }

    @Test
    void numberValues() {
        MutableDouble m = new MutableDouble(7.5);
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.5f, m.floatValue(), 0.001);
        assertEquals(7.5, m.doubleValue());
    }

    @Test
    void equalsAndHashCode() {
        MutableDouble a = new MutableDouble(10.0);
        MutableDouble b = new MutableDouble(10.0);
        MutableDouble c = new MutableDouble(20.0);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
    }

    @Test
    void compareTo() {
        MutableDouble a = new MutableDouble(10.0);
        MutableDouble b = new MutableDouble(20.0);
        MutableDouble c = new MutableDouble(10.0);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @Test
    void toStringMethod() {
        assertEquals("3.14", new MutableDouble(3.14).toString());
    }
}
