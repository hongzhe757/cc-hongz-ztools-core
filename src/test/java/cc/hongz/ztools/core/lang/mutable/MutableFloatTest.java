package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableFloat} 单元测试.
 */
class MutableFloatTest {

    @Test
    void defaultConstructor() {
        MutableFloat m = new MutableFloat();
        assertEquals(0.0f, m.floatValue());
    }

    @Test
    void floatConstructor() {
        MutableFloat m = new MutableFloat(3.14f);
        assertEquals(3.14f, m.floatValue());
        assertEquals(Float.valueOf(3.14f), m.get());
    }

    @Test
    void numberConstructor() {
        MutableFloat m = new MutableFloat(Float.valueOf(2.5f));
        assertEquals(2.5f, m.floatValue());
    }

    @Test
    void stringConstructor() {
        MutableFloat m = new MutableFloat("1.5");
        assertEquals(1.5f, m.floatValue());
    }

    @Test
    void stringConstructorInvalid() {
        assertThrows(NumberFormatException.class, () -> new MutableFloat("abc"));
    }

    @Test
    void setAndGet() {
        MutableFloat m = new MutableFloat();
        m.set(9.9f);
        assertEquals(9.9f, m.get());
    }

    @Test
    void incrementAndDecrement() {
        MutableFloat m = new MutableFloat(5.0f);
        assertSame(m, m.increment());
        assertEquals(6.0f, m.floatValue());
        assertSame(m, m.decrement());
        assertEquals(5.0f, m.floatValue());
    }

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

    @Test
    void numberValues() {
        MutableFloat m = new MutableFloat(7.5f);
        assertEquals(7, m.intValue());
        assertEquals(7L, m.longValue());
        assertEquals(7.5f, m.floatValue());
        assertEquals(7.5, m.doubleValue(), 0.001);
    }

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

    @Test
    void compareTo() {
        MutableFloat a = new MutableFloat(10.0f);
        MutableFloat b = new MutableFloat(20.0f);
        MutableFloat c = new MutableFloat(10.0f);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @Test
    void toStringMethod() {
        assertEquals("3.14", new MutableFloat(3.14f).toString());
    }
}
