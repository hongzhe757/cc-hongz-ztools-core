package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Pair} 单元测试.
 */
@DisplayName("Pair 键值对")
class PairTest {

    @Test
    @DisplayName("of - 静态工厂方法")
    void of() {
        Pair<String, Integer> pair = Pair.of("key", 1);
        assertNotNull(pair);
        assertEquals("key", pair.getKey());
        assertEquals(1, pair.getValue());
    }

    @Test
    @DisplayName("构造 - 直接创建")
    void constructor() {
        Pair<String, String> pair = new Pair<>("hello", "world");
        assertEquals("hello", pair.getKey());
        assertEquals("world", pair.getValue());
    }

    @Test
    @DisplayName("null键值")
    void nullKeyValue() {
        Pair<String, String> pair = new Pair<>(null, null);
        assertNull(pair.getKey());
        assertNull(pair.getValue());
    }

    @Test
    @DisplayName("toString - 格式化输出")
    void toStringTest() {
        Pair<String, Integer> pair = Pair.of("a", 1);
        assertEquals("Pair [key=a, value=1]", pair.toString());
    }

    @Test
    @DisplayName("equals - 相等判断")
    void equalsTest() {
        Pair<String, Integer> p1 = Pair.of("a", 1);
        Pair<String, Integer> p2 = Pair.of("a", 1);
        Pair<String, Integer> p3 = Pair.of("b", 1);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
        assertNotEquals(p1, "string");
    }

    @Test
    @DisplayName("hashCode - 相同内容相同hash")
    void hashCodeTest() {
        Pair<String, Integer> p1 = Pair.of("a", 1);
        Pair<String, Integer> p2 = Pair.of("a", 1);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
