package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Tuple} 单元测试.
 * <p>
 * 覆盖元素获取、不可变性（defensive copy）、包含判断、截取、转列表、流/迭代、
 * hashCode 缓存及 equals/hashCode/toString.
 *
 * @author hongzhe.qin
 */
@DisplayName("Tuple 元组")
class TupleTest {

    /**
     * 验证 {@link Tuple#get(int)} 返回指定位置元素且 {@link Tuple#size()} 正确.
     */
    @Test
    @DisplayName("get 与 size")
    void getAndSize() {
        final Tuple tuple = new Tuple("a", 2, true);
        assertEquals(3, tuple.size());
        assertEquals("a", tuple.get(0));
        assertEquals(Integer.valueOf(2), tuple.get(1));
        assertEquals(Boolean.TRUE, tuple.get(2));
    }

    /**
     * 验证 {@link Tuple#getMembers()} 返回副本，修改副本不影响内部状态.
     */
    @Test
    @DisplayName("getMembers 返回副本")
    void getMembersDefensiveCopy() {
        final Tuple tuple = new Tuple("a", "b");
        final Object[] members = tuple.getMembers();
        members[0] = "changed";
        assertEquals("a", tuple.get(0));
    }

    /**
     * 验证 {@link Tuple#contains(Object)} 判断是否包含元素.
     */
    @Test
    @DisplayName("contains 包含判断")
    void contains() {
        final Tuple tuple = new Tuple("a", "b", "c");
        assertTrue(tuple.contains("b"));
        assertFalse(tuple.contains("z"));
    }

    /**
     * 验证 {@link Tuple#sub(int, int)} 截取元组指定部分.
     */
    @Test
    @DisplayName("sub 截取")
    void sub() {
        final Tuple tuple = new Tuple("a", "b", "c", "d");
        final Tuple sub = tuple.sub(1, 3);
        assertEquals(2, sub.size());
        assertEquals("b", sub.get(0));
        assertEquals("c", sub.get(1));
    }

    /**
     * 验证 {@link Tuple#toList()} 转换为列表.
     */
    @Test
    @DisplayName("toList 转列表")
    void toList() {
        final Tuple tuple = new Tuple("a", "b", "c");
        final List<Object> list = tuple.toList();
        assertEquals(Arrays.asList("a", "b", "c"), list);
    }

    /**
     * 验证 {@link Tuple#setCacheHash(boolean)} 链式返回且开启后 hashCode 被缓存.
     */
    @Test
    @DisplayName("setCacheHash 缓存哈希")
    void setCacheHash() {
        final Tuple tuple = new Tuple("a", "b");
        assertSame(tuple, tuple.setCacheHash(true));
        final int hash = tuple.hashCode();
        assertEquals(hash, tuple.hashCode());
    }

    /**
     * 验证 {@link Tuple#stream()} 转换为流.
     */
    @Test
    @DisplayName("stream 转流")
    void stream() {
        final Tuple tuple = new Tuple("a", "b", "c");
        final List<Object> list = tuple.stream().collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c"), list);
    }

    /**
     * 验证 {@link Tuple#iterator()} 可迭代遍历所有元素.
     */
    @Test
    @DisplayName("iterator 迭代")
    void iterator() {
        final Tuple tuple = new Tuple("a", "b");
        final List<Object> list = new ArrayList<>();
        for (final Object o : tuple) {
            list.add(o);
        }
        assertEquals(Arrays.asList("a", "b"), list);
    }

    /**
     * 验证相同成员的 Tuple 相等且 hashCode 一致.
     */
    @Test
    @DisplayName("equals 与 hashCode")
    void equalsAndHashCode() {
        final Tuple tuple1 = new Tuple("a", 1);
        final Tuple tuple2 = new Tuple("a", 1);
        assertEquals(tuple1, tuple2);
        assertEquals(tuple1.hashCode(), tuple2.hashCode());
        assertNotSame(tuple1, tuple2);
    }

    /**
     * 验证 {@link Tuple#toString()} 返回数组字符串形式.
     */
    @Test
    @DisplayName("toString 字符串形式")
    void toStringTest() {
        final Tuple tuple = new Tuple("a", "b");
        assertEquals(Arrays.toString(new Object[]{"a", "b"}), tuple.toString());
    }
}
