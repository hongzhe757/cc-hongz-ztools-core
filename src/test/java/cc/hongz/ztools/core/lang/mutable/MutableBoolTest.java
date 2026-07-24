package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableBool} 单元测试.
 */
class MutableBoolTest {

    @Test
    void defaultConstructor() {
        MutableBool mutableBool = new MutableBool();
        assertFalse(mutableBool.get());
    }

    @Test
    void booleanConstructor() {
        MutableBool t = new MutableBool(true);
        MutableBool f = new MutableBool(false);
        assertTrue(t.get());
        assertFalse(f.get());
    }

    @Test
    void stringConstructor() {
        MutableBool t = new MutableBool("true");
        MutableBool f = new MutableBool("false");
        assertTrue(t.get());
        assertFalse(f.get());
    }

    @Test
    void setAndGet() {
        MutableBool mutableBool = new MutableBool();
        mutableBool.set(true);
        assertTrue(mutableBool.get());
        mutableBool.set(false);
        assertFalse(mutableBool.get());
    }

    @Test
    void setBooleanObject() {
        MutableBool mutableBool = new MutableBool();
        mutableBool.set(Boolean.TRUE);
        assertTrue(mutableBool.get());
    }

    @Test
    void equalsAndHashCode() {
        MutableBool a = new MutableBool(true);
        MutableBool b = new MutableBool(true);
        MutableBool c = new MutableBool(false);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "true");
    }

    @Test
    void compareTo() {
        MutableBool t = new MutableBool(true);
        MutableBool f = new MutableBool(false);
        MutableBool t2 = new MutableBool(true);

        assertTrue(f.compareTo(t) < 0);
        assertTrue(t.compareTo(f) > 0);
        assertEquals(0, t.compareTo(t2));
    }

    @Test
    void toStringMethod() {
        assertEquals("true", new MutableBool(true).toString());
        assertEquals("false", new MutableBool(false).toString());
    }
}
