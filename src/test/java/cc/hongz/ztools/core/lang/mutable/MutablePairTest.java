package cc.hongz.ztools.core.lang.mutable;

import static org.junit.jupiter.api.Assertions.*;

import cc.hongz.ztools.core.lang.Pair;
import org.junit.jupiter.api.Test;

/**
 * {@link MutablePair} 单元测试.
 */
class MutablePairTest {

    @Test
    void constructorAndGet() {
        MutablePair<String, Integer> pair = new MutablePair<>("key", 42);
        assertEquals("key", pair.getKey());
        assertEquals(Integer.valueOf(42), pair.getValue());
    }

    @Test
    void getReturnsSelf() {
        MutablePair<String, Integer> pair = new MutablePair<>("key", 42);
        assertSame(pair, pair.get());
    }

    @Test
    void setKey() {
        MutablePair<String, Integer> pair = new MutablePair<>("old", 1);
        MutablePair<String, Integer> result = pair.setKey("new");
        assertSame(pair, result);
        assertEquals("new", pair.getKey());
    }

    @Test
    void setValue() {
        MutablePair<String, Integer> pair = new MutablePair<>("key", 1);
        MutablePair<String, Integer> result = pair.setValue(99);
        assertSame(pair, result);
        assertEquals(Integer.valueOf(99), pair.getValue());
    }

    @Test
    void setPair() {
        MutablePair<String, Integer> pair = new MutablePair<>("old", 1);
        Pair<String, Integer> other = new Pair<>("new", 2);
        pair.set(other);
        assertEquals("new", pair.getKey());
        assertEquals(Integer.valueOf(2), pair.getValue());
    }

    @Test
    void implementsMutableInterface() {
        MutablePair<String, Integer> pair = new MutablePair<>("key", 1);
        Mutable<Pair<String, Integer>> mutable = pair;
        assertEquals(pair, mutable.get());

        Pair<String, Integer> newPair = new Pair<>("x", 10);
        mutable.set(newPair);
        assertEquals("x", pair.getKey());
        assertEquals(Integer.valueOf(10), pair.getValue());
    }
}
