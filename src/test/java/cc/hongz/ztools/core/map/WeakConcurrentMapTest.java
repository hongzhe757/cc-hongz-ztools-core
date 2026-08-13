package cc.hongz.ztools.core.map;

import cc.hongz.ztools.core.thread.ConcurrencyTester;
import cc.hongz.ztools.core.thread.ThreadUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.RandomUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WeakConcurrentMapTest {

    @Test
    @DisplayName("put/get 基本操作")
    public void putAndGetTest(){
        final WeakConcurrentMap<Object, Object> map = new WeakConcurrentMap<>();
        Object
                key1 = new Object(), value1 = new Object(),
                key2 = new Object(), value2 = new Object(),
                key3 = new Object(), value3 = new Object(),
                key4 = new Object(), value4 = new Object();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);

        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
        assertEquals(value3, map.get(key3));
        assertEquals(value4, map.get(key4));

        // 清空引用
        //noinspection UnusedAssignment
        key1 = null;
        //noinspection UnusedAssignment
        key2 = null;

        System.gc();
        ThreadUtil.sleep(200L);

        assertEquals(2, map.size());
    }

    @Test
    @DisplayName("并发 get 测试")
    public void getConcurrencyTest(){
        final WeakConcurrentMap<String, String> cache = new WeakConcurrentMap<>();
        final ConcurrencyTester tester = new ConcurrencyTester(9000);
        tester.test(()-> cache.computeIfAbsent("aaa" + RandomUtil.randomInt(2), (key)-> "aaaValue"));

        assertTrue(tester.getInterval() > 0);
        String value = ObjectUtil.defaultIfNull(cache.get("aaa0"), cache.get("aaa1"));
        assertEquals("aaaValue", value);
    }
}
