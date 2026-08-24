package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CastUtil} 单元测试.
 */
@DisplayName("CastUtil 类型转换工具")
class CastUtilTest {

    @Test
    @DisplayName("castUp - Collection 向上转型")
    void castUp_collection() {
        java.util.Collection<Integer> ints = java.util.Arrays.asList(1, 2, 3);
        java.util.Collection<Number> numbers = CastUtil.castUp(ints);
        assertNotNull(numbers);
        assertEquals(3, numbers.size());
    }

    @Test
    @DisplayName("castDown - Collection 向下转型")
    void castDown_collection() {
        java.util.Collection<Number> numbers = java.util.Arrays.asList(1, 2, 3);
        java.util.Collection<Integer> ints = CastUtil.castDown(numbers);
        assertNotNull(ints);
    }

    @Test
    @DisplayName("castUp - List 向上转型")
    void castUp_list() {
        java.util.List<Integer> ints = java.util.Arrays.asList(1, 2, 3);
        java.util.List<Number> numbers = CastUtil.castUp(ints);
        assertNotNull(numbers);
    }

    @Test
    @DisplayName("castDown - List 向下转型")
    void castDown_list() {
        java.util.List<Number> numbers = java.util.Arrays.asList(1, 2, 3);
        java.util.List<Integer> ints = CastUtil.castDown(numbers);
        assertNotNull(ints);
    }

    @Test
    @DisplayName("castUp - Set 向上转型")
    void castUp_set() {
        java.util.Set<Integer> ints = new java.util.LinkedHashSet<>(java.util.Arrays.asList(1, 2, 3));
        java.util.Set<Number> numbers = CastUtil.castUp(ints);
        assertNotNull(numbers);
    }

    @Test
    @DisplayName("castUp - Map 向上转型")
    void castUp_map() {
        java.util.Map<Integer, String> map = new java.util.HashMap<>();
        map.put(1, "one");
        java.util.Map<Number, String> numbers = CastUtil.castUp(map);
        assertNotNull(numbers);
    }

    @Test
    @DisplayName("castDown - Map 向下转型")
    void castDown_map() {
        java.util.Map<Number, String> numbers = new java.util.HashMap<>();
        numbers.put(1, "one");
        java.util.Map<Integer, String> ints = CastUtil.castDown(numbers);
        assertNotNull(ints);
    }
}
