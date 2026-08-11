package cc.hongz.ztools.core.stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CollectorUtil} 单元测试.
 */
class CollectorUtilTest {

    @Test
    @DisplayName("joining 分隔符拼接测试")
    void joiningTest() {
        String result = Stream.of("a", "b", "c").collect(CollectorUtil.joining(","));
        assertEquals("a,b,c", result);
    }

    @Test
    @DisplayName("joining 自定义toString函数测试")
    void joiningWithFuncTest() {
        String result = Stream.of(1, 2, 3)
            .collect(CollectorUtil.joining(",", Object::toString));
        assertEquals("1,2,3", result);
    }

    @Test
    @DisplayName("joining 前缀后缀测试")
    void joiningWithPrefixSuffixTest() {
        String result = Stream.of("a", "b", "c")
            .collect(CollectorUtil.joining(",", "[", "]", Object::toString));
        assertEquals("[a,b,c]", result);
    }

    @Test
    @DisplayName("groupingBy 分组测试")
    void groupingByTest() {
        List<String> list = Arrays.asList("apple", "banana", "apricot", "blueberry");
        Map<Character, List<String>> result = list.stream()
            .collect(CollectorUtil.groupingBy(s -> s.charAt(0)));
        assertEquals(2, result.size());
        assertEquals(Arrays.asList("apple", "apricot"), result.get('a'));
        assertEquals(Arrays.asList("banana", "blueberry"), result.get('b'));
    }

    @Test
    @DisplayName("groupingBy 下游统计操作测试")
    void groupingByWithDownstreamTest() {
        List<String> list = Arrays.asList("apple", "banana", "apricot");
        Map<Character, Long> result = list.stream()
            .collect(CollectorUtil.groupingBy(s -> s.charAt(0), Collectors.counting()));
        assertEquals(2L, result.get('a'));
        assertEquals(1L, result.get('b'));
    }

    @Test
    @DisplayName("groupingBy null键测试")
    void groupingByWithNullKeyTest() {
        List<String> list = Arrays.asList("a", null, "b");
        Map<String, List<String>> result = list.stream()
            .collect(CollectorUtil.groupingBy(s -> s));
        assertNotNull(result.get(null));
        assertEquals(1, result.get(null).size());
    }

    @Test
    @DisplayName("toMap 转换测试")
    void toMapTest() {
        List<String> list = Arrays.asList("a", "bb", "ccc");
        Map<Integer, String> result = list.stream()
            .collect(CollectorUtil.toMap(String::length, s -> s, (v1, v2) -> v1));
        assertEquals("a", result.get(1));
        assertEquals("bb", result.get(2));
        assertEquals("ccc", result.get(3));
    }

    @Test
    @DisplayName("toMap null值测试")
    void toMapWithNullValueTest() {
        List<String> list = Arrays.asList("a", "b");
        Map<String, String> result = list.stream()
            .collect(CollectorUtil.toMap(s -> s, s -> null, (v1, v2) -> v1));
        assertTrue(result.containsKey("a"));
        assertNull(result.get("a"));
    }

    @Test
    @DisplayName("reduceListMap 聚合测试")
    void reduceListMapTest() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("k1", "v1");
        Map<String, String> map2 = new HashMap<>();
        map2.put("k1", "v2");
        map2.put("k2", "v3");

        Map<String, List<String>> result = Stream.of(map1, map2)
            .collect(CollectorUtil.reduceListMap());
        assertEquals(2, result.get("k1").size());
        assertEquals(1, result.get("k2").size());
    }

    @Test
    @DisplayName("groupingBy 值映射测试")
    void groupingByWithValueMapperTest() {
        List<String> list = Arrays.asList("apple", "banana", "apricot");
        Map<Character, List<Integer>> result = list.stream()
            .collect(CollectorUtil.groupingBy(s -> s.charAt(0), String::length));
        assertEquals(Arrays.asList(5, 7), result.get('a'));
        assertEquals(Arrays.asList(6), result.get('b'));
    }

    @Test
    @DisplayName("mapMerger 合并测试")
    void mapMergerTest() {
        Map<String, Integer> m1 = new HashMap<>();
        m1.put("a", 1);
        Map<String, Integer> m2 = new HashMap<>();
        m2.put("a", 2);
        m2.put("b", 3);

        Map<String, Integer> merged = CollectorUtil.<String, Integer, Map<String, Integer>>mapMerger(Integer::sum)
            .apply(m1, m2);
        assertEquals(3, merged.get("a"));
        assertEquals(3, merged.get("b"));
    }
}
