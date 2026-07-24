package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link MutableObj} 单元测试.
 */
class MutableObjTest {

    @Test
    void defaultConstructor() {
        MutableObj<String> m = new MutableObj<>();
        assertNull(m.get());
    }

    @Test
    void valueConstructor() {
        MutableObj<String> m = new MutableObj<>("hello");
        assertEquals("hello", m.get());
    }

    @Test
    void ofFactory() {
        MutableObj<Integer> m = MutableObj.of(42);
        assertEquals(Integer.valueOf(42), m.get());
    }

    @Test
    void setAndGet() {
        MutableObj<String> m = new MutableObj<>();
        m.set("world");
        assertEquals("world", m.get());
        m.set(null);
        assertNull(m.get());
    }

    @Test
    void equalsAndHashCode() {
        MutableObj<String> a = new MutableObj<>("test");
        MutableObj<String> b = new MutableObj<>("test");
        MutableObj<String> c = new MutableObj<>("other");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "test");
    }

    @Test
    void equalsWithNull() {
        MutableObj<String> a = new MutableObj<>(null);
        MutableObj<String> b = new MutableObj<>(null);
        MutableObj<String> c = new MutableObj<>("value");

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void equalsSameInstance() {
        MutableObj<String> a = new MutableObj<>("test");
        assertEquals(a, a);
    }

    @Test
    void toStringMethod() {
        MutableObj<String> m = new MutableObj<>("hello");
        assertEquals("hello", m.toString());

        MutableObj<String> nullM = new MutableObj<>(null);
        assertEquals("null", nullM.toString());
    }
}
