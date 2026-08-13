package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BiMap 单元测试.
 */
public class BiMapTest {

    @Test
    @DisplayName("get/getKey 正向反向查询")
    public void getTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        assertEquals(Integer.valueOf(111), biMap.get("aaa"));
        assertEquals(Integer.valueOf(222), biMap.get("bbb"));

        assertEquals("aaa", biMap.getKey(111));
        assertEquals("bbb", biMap.getKey(222));
    }

    @Test
    @DisplayName("computeIfAbsent 同步更新反向Map")
    public void computeIfAbsentTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        biMap.computeIfAbsent("ccc", s -> 333);
        assertEquals(Integer.valueOf(333), biMap.get("ccc"));
        assertEquals("ccc", biMap.getKey(333));
    }

    @Test
    @DisplayName("putIfAbsent 同步更新反向Map")
    public void putIfAbsentTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        biMap.putIfAbsent("ccc", 333);
        assertEquals(Integer.valueOf(333), biMap.get("ccc"));
        assertEquals("ccc", biMap.getKey(333));
    }
}
