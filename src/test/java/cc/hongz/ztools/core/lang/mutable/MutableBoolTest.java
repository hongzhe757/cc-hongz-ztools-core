package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MutableBool} 单元测试.
 */
@DisplayName("MutableBool 单元测试")
class MutableBoolTest {

    @DisplayName("默认构造")
    @Test
    void defaultConstructor() {
        MutableBool mutableBool = new MutableBool();
        assertFalse(mutableBool.get());
    }

    @DisplayName("boolean 构造")
    @Test
    void booleanConstructor() {
        MutableBool t = new MutableBool(true);
        MutableBool f = new MutableBool(false);
        assertTrue(t.get());
        assertFalse(f.get());
    }

    @DisplayName("字符串构造")
    @Test
    void stringConstructor() {
        MutableBool t = new MutableBool("true");
        MutableBool f = new MutableBool("false");
        assertTrue(t.get());
        assertFalse(f.get());
    }

    @DisplayName("设置与获取")
    @Test
    void setAndGet() {
        MutableBool mutableBool = new MutableBool();
        mutableBool.set(true);
        assertTrue(mutableBool.get());
        mutableBool.set(false);
        assertFalse(mutableBool.get());
    }

    @DisplayName("设置 Boolean 对象")
    @Test
    void setBooleanObject() {
        MutableBool mutableBool = new MutableBool();
        mutableBool.set(Boolean.TRUE);
        assertTrue(mutableBool.get());
    }

    @DisplayName("equals 与 hashCode")
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

    @DisplayName("比较大小")
    @Test
    void compareTo() {
        MutableBool t = new MutableBool(true);
        MutableBool f = new MutableBool(false);
        MutableBool t2 = new MutableBool(true);

        assertTrue(f.compareTo(t) < 0);
        assertTrue(t.compareTo(f) > 0);
        assertEquals(0, t.compareTo(t2));
    }

    @DisplayName("toString")
    @Test
    void toStringMethod() {
        assertEquals("true", new MutableBool(true).toString());
        assertEquals("false", new MutableBool(false).toString());
    }
}
