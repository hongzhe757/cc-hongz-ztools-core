package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.exceptions.ValidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ListUtil} 单元测试.
 */
@DisplayName("ListUtil List工具类")
class ListUtilTest {

    // ======================= list =======================

    @Test
    @DisplayName("list(isLinked) - 创建ArrayList/LinkedList")
    void listType() {
        assertTrue(ListUtil.list(false) instanceof ArrayList);
        assertTrue(ListUtil.list(true) instanceof LinkedList);
    }

    @Test
    @DisplayName("list(isLinked, values) - 带初始值创建List")
    void listValues() {
        List<String> list = ListUtil.list(false, "a", "b", "c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    @DisplayName("list(isLinked, values) - 空值创建空List")
    void listEmptyValues() {
        List<String> list = ListUtil.list(false);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("list(isLinked, collection) - 从Collection创建")
    void listCollection() {
        Set<String> set = new LinkedHashSet<>(Arrays.asList("x", "y"));
        List<String> list = ListUtil.list(false, set);
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("list(isLinked, null) - null创建空List")
    void listNull() {
        assertTrue(ListUtil.list(false, (Collection<String>) null).isEmpty());
        assertTrue(ListUtil.list(false, (Iterable<String>) null).isEmpty());
        assertTrue(ListUtil.list(false, (Iterator<String>) null).isEmpty());
        assertTrue(ListUtil.list(false, (Enumeration<String>) null).isEmpty());
    }

    // ======================= toList / toLinkedList / toCopyOnWriteArrayList =======================

    @Test
    @DisplayName("toList(T...) - 数组转ArrayList")
    void toList() {
        ArrayList<String> list = ListUtil.toList("a", "b");
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("toLinkedList(T...) - 数组转LinkedList")
    void toLinkedList() {
        LinkedList<String> list = ListUtil.toLinkedList("a", "b", "c");
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("toCopyOnWriteArrayList - 创建CopyOnWriteArrayList")
    void toCopyOnWriteArrayList() {
        assertNotNull(ListUtil.toCopyOnWriteArrayList(null));
        assertNotNull(ListUtil.toCopyOnWriteArrayList(Arrays.asList("a")));
    }

    // ======================= of =======================

    @Test
    @DisplayName("of - 创建不可变List")
    void of() {
        List<String> list = ListUtil.of("a", "b");
        assertEquals(2, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add("c"));
    }

    @Test
    @DisplayName("of - 空数组")
    void ofEmpty() {
        List<String> list = ListUtil.of();
        assertTrue(list.isEmpty());
    }

    // ======================= empty =======================

    @Test
    @DisplayName("empty - 返回不可变空List")
    void empty() {
        List<String> list = ListUtil.empty();
        assertTrue(list.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> list.add("a"));
    }

    // ======================= sub =======================

    @Test
    @DisplayName("sub - 截取List")
    void sub() {
        assertNull(ListUtil.sub(null, 0, 1));
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(Arrays.asList("b", "c"), ListUtil.sub(list, 1, 3));
    }

    @Test
    @DisplayName("sub - 负数索引")
    void subNegative() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(Arrays.asList("d", "e"), ListUtil.sub(list, -2, 5));
    }

    @Test
    @DisplayName("sub - 步进")
    void subStep() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(Arrays.asList("a", "c", "e"), ListUtil.sub(list, 0, 5, 2));
    }

    @Test
    @DisplayName("sub - 空List")
    void subEmpty() {
        assertTrue(ListUtil.sub(new ArrayList<>(), 0, 1).isEmpty());
    }

    // ======================= page =======================

    @Test
    @DisplayName("page - 分页取值")
    void page() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) { list.add(i); }
        // page从1开始计数，page(1,5,list)是第一页
        List<Integer> page1 = ListUtil.page(1, 5, list);
        assertEquals(5, page1.size());
        assertEquals(1, page1.get(0));
    }

    @Test
    @DisplayName("page - null/空List")
    void pageEmpty() {
        assertTrue(ListUtil.page(1, 5, null).isEmpty());
        assertTrue(ListUtil.page(1, 5, new ArrayList<>()).isEmpty());
    }

    @Test
    @DisplayName("page - 超出范围")
    void pageOutOfRange() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertTrue(ListUtil.page(100, 5, list).isEmpty());
    }

    // ======================= sort =======================

    @Test
    @DisplayName("sort - 排序List")
    void sort() {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2));
        ListUtil.sort(list, Comparator.naturalOrder());
        assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    @DisplayName("sort - null/空List")
    void sortEmpty() {
        assertNull(ListUtil.sort(null, Comparator.naturalOrder()));
        List<Integer> empty = new ArrayList<>();
        assertSame(empty, ListUtil.sort(empty, Comparator.naturalOrder()));
    }

    // ======================= reverse / reverseNew =======================

    @Test
    @DisplayName("reverse - 反转List")
    void reverse() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ListUtil.reverse(list);
        assertEquals(Arrays.asList("c", "b", "a"), list);
    }

    @Test
    @DisplayName("reverseNew - 反转但不修改原List")
    void reverseNew() {
        assertNull(ListUtil.reverseNew(null));
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> reversed = ListUtil.reverseNew(list);
        assertEquals(Arrays.asList("c", "b", "a"), reversed);
        assertEquals("a", list.get(0));
    }

    // ======================= setOrAppend =======================

    @Test
    @DisplayName("setOrAppend - 设置或追加元素")
    void setOrAppend() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
        ListUtil.setOrAppend(list, 0, "x");
        assertEquals("x", list.get(0));
        ListUtil.setOrAppend(list, 5, "y");
        assertEquals("y", list.get(list.size() - 1));
    }

    // ======================= setOrPadding =======================

    @Test
    @DisplayName("setOrPadding - 指定位置设置并填充")
    void setOrPadding() {
        List<String> list = new ArrayList<>(Arrays.asList("a"));
        ListUtil.setOrPadding(list, 3, "d", "x");
        assertEquals(4, list.size());
        assertEquals("x", list.get(1));
        assertEquals("d", list.get(3));
    }

    @Test
    @DisplayName("setOrPadding - 超出限制抛异常")
    void setOrPaddingLimit() {
        List<String> list = new ArrayList<>(Arrays.asList("a"));
        assertThrows(ValidateException.class, () -> ListUtil.setOrPadding(list, 100, "z", null, 10));
    }

    // ======================= lastIndexOf / indexOfAll =======================

    @Test
    @DisplayName("lastIndexOf - 最后匹配位置")
    void lastIndexOf() {
        assertEquals(-1, ListUtil.lastIndexOf(null, null));
        List<String> list = Arrays.asList("a", "b", "a", "c");
        assertEquals(2, ListUtil.lastIndexOf(list, t -> "a".equals(t)));
    }

    @Test
    @DisplayName("indexOfAll - 全部匹配位置")
    void indexOfAll() {
        assertArrayEquals(new int[0], ListUtil.indexOfAll(null, null));
        List<String> list = Arrays.asList("a", "b", "a", "c", "a");
        assertArrayEquals(new int[]{0, 2, 4}, ListUtil.indexOfAll(list, t -> "a".equals(t)));
    }

    // ======================= unmodifiable =======================

    @Test
    @DisplayName("unmodifiable - 不可修改List")
    void unmodifiable() {
        assertNull(ListUtil.unmodifiable(null));
        List<String> list = ListUtil.unmodifiable(Arrays.asList("a", "b"));
        assertThrows(UnsupportedOperationException.class, () -> list.add("c"));
    }

    // ======================= partition / split =======================

    @Test
    @DisplayName("partition - 按长度分段")
    void partition() {
        assertTrue(ListUtil.partition(null, 2).isEmpty());
        assertTrue(ListUtil.partition(new ArrayList<>(), 2).isEmpty());
        List<List<Integer>> result = ListUtil.partition(Arrays.asList(1, 2, 3, 4, 5), 2);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList(1, 2), result.get(0));
        assertEquals(Arrays.asList(3, 4), result.get(1));
        assertEquals(Collections.singletonList(5), result.get(2));
    }

    @Test
    @DisplayName("split - 按长度分段(同partition)")
    void split() {
        List<List<Integer>> result = ListUtil.split(Arrays.asList(1, 2, 3), 2);
        assertEquals(2, result.size());
    }

    // ======================= splitAvg =======================

    @Test
    @DisplayName("splitAvg - 平均分段")
    void splitAvg() {
        assertTrue(ListUtil.splitAvg(null, 2).isEmpty());
        List<List<Integer>> result = ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4, 5), 2);
        assertEquals(2, result.size());
    }

    // ======================= swapTo / swapElement =======================

    @Test
    @DisplayName("swapTo - 交换元素到指定位置")
    void swapTo() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ListUtil.swapTo(list, "a", 2);
        assertEquals("a", list.get(2));
    }

    @Test
    @DisplayName("swapTo - null/空List不抛异常")
    void swapToEmpty() {
        ListUtil.swapTo(null, "a", 0);
        ListUtil.swapTo(new ArrayList<>(), "a", 0);
    }

    @Test
    @DisplayName("swapElement - 交换两个元素位置")
    void swapElement() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ListUtil.swapElement(list, "a", "c");
        assertEquals("c", list.get(0));
        assertEquals("a", list.get(2));
    }

    // ======================= move =======================

    @Test
    @DisplayName("move - 移动元素到新位置")
    void move() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        // move("a", 2): 先remove("a")得["b","c"], 再add(2,"a")得["b","c","a"]
        ListUtil.move(list, "a", 2);
        assertEquals("a", list.get(2));
        assertEquals("b", list.get(0));
    }

    @Test
    @DisplayName("move - 不存在的元素追加")
    void moveNew() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
        ListUtil.move(list, "c", 0);
        assertEquals("c", list.get(0));
        assertEquals(3, list.size());
    }

    // ======================= zip =======================

    @Test
    @DisplayName("zip - 合并两个列表")
    void zip() {
        List<String> result = ListUtil.zip(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("1", "2", "3"),
                (x, y) -> x + y
        );
        assertEquals(Arrays.asList("a1", "b2", "c3"), result);
    }

    @Test
    @DisplayName("zip - 不等长取最短")
    void zipUnequal() {
        List<String> result = ListUtil.zip(
                Arrays.asList("a", "b"),
                Arrays.asList("1", "2", "3"),
                (x, y) -> x + y
        );
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("zip - null/空返回空列表")
    void zipEmpty() {
        assertTrue(ListUtil.zip(null, Arrays.asList("1"), (a, b) -> a).isEmpty());
        assertTrue(ListUtil.zip(Arrays.asList("a"), null, (a, b) -> a).isEmpty());
        assertTrue(ListUtil.zip(new ArrayList<>(), Arrays.asList("1"), (a, b) -> a).isEmpty());
    }
}
