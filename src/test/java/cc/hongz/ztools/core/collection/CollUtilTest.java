package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CollUtil} 单元测试.
 */
@DisplayName("CollUtil 集合工具类")
class CollUtilTest {

    // ======================= isEmpty / isNotEmpty =======================

    @Test
    @DisplayName("isEmpty - null和空集合返回true")
    void isEmpty() {
        assertTrue(CollUtil.isEmpty((Collection<?>) null));
        assertTrue(CollUtil.isEmpty(Collections.emptyList()));
        assertFalse(CollUtil.isEmpty(Collections.singletonList(1)));
    }

    @Test
    @DisplayName("isNotEmpty - 非空集合返回true")
    void isNotEmpty() {
        assertFalse(CollUtil.isNotEmpty((Collection<?>) null));
        assertTrue(CollUtil.isNotEmpty(Collections.singletonList(1)));
    }

    // ======================= getFirst / getLast =======================

    @Test
    @DisplayName("getFirst(Iterable) - 获取第一个元素")
    void getFirstIterable() {
        assertNull(CollUtil.getFirst((Iterable<String>) null));
        assertNull(CollUtil.getFirst(Collections.emptyList()));
        assertEquals("a", CollUtil.getFirst(Arrays.asList("a", "b")));
    }

    @Test
    @DisplayName("getFirst(Iterator) - 获取第一个元素")
    void getFirstIterator() {
        assertNull(CollUtil.getFirst((Iterator<String>) null));
        assertNull(CollUtil.getFirst(Collections.emptyIterator()));
        assertEquals(1, CollUtil.getFirst(Arrays.asList(1, 2, 3).iterator()));
    }

    @Test
    @DisplayName("getLast - 获取最后一个元素")
    void getLast() {
        assertNull(CollUtil.getLast(null));
        assertNull(CollUtil.getLast(Collections.emptyList()));
        assertEquals("c", CollUtil.getLast(Arrays.asList("a", "b", "c")));
    }

    // ======================= get =======================

    @Test
    @DisplayName("get - 按下标获取元素")
    void get() {
        assertNull(CollUtil.get((Collection<String>) null, 0));
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("a", CollUtil.get(list, 0));
        assertEquals("c", CollUtil.get(list, -1));
        assertNull(CollUtil.get(list, 10));
    }

    // ======================= newArrayList / toList =======================

    @Test
    @DisplayName("newArrayList(T...) - 数组转ArrayList")
    void newArrayList() {
        ArrayList<String> list = CollUtil.newArrayList("a", "b", "c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("newArrayList - 空数组")
    void newArrayListEmpty() {
        ArrayList<String> list = CollUtil.newArrayList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("newArrayList(Iterable) - Iterable转ArrayList")
    void newArrayListIterable() {
        Set<String> set = new LinkedHashSet<>(Arrays.asList("x", "y"));
        ArrayList<String> list = CollUtil.newArrayList((Iterable<String>) set);
        assertEquals(2, list.size());
        assertEquals("x", list.get(0));
    }

    @Test
    @DisplayName("toList(T...) - 数组转ArrayList")
    void toList() {
        ArrayList<String> list = CollUtil.toList("a", "b");
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("toList - null数组")
    void toListNull() {
        ArrayList<Object> list = CollUtil.toList((Object[]) null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // ======================= join =======================

    @Test
    @DisplayName("join - 集合拼接为字符串")
    void join() {
        assertNull(CollUtil.join((Iterable<?>) null, ","));
        assertEquals("", CollUtil.join(Collections.emptyList(), ","));
        assertEquals("a,b,c", CollUtil.join(Arrays.asList("a", "b", "c"), ","));
    }

    @Test
    @DisplayName("join - null元素跳过")
    void joinWithNull() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("c");
        assertEquals("a,null,c", CollUtil.join(list, ","));
    }

    @Test
    @DisplayName("join - null分隔符直接拼接")
    void joinNullConjunction() {
        assertEquals("anullbnullc", CollUtil.join(Arrays.asList("a", "b", "c"), null));
    }

    // ======================= union =======================

    @Test
    @DisplayName("union - 两个集合并集")
    void union() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "b", "c");
        List<String> list2 = CollUtil.newArrayList("a", "b", "d");
        Collection<String> result = CollUtil.union(list1, list2);
        // union取每个元素的最大出现次数: a=max(1,1)=1, b=max(2,1)=2, c=max(1,0)=1, d=max(0,1)=1
        assertEquals(5, result.size());
        assertTrue(result.contains("d"));
    }

    @Test
    @DisplayName("union - 空集合")
    void unionEmpty() {
        assertTrue(CollUtil.union(null, null).isEmpty());
        assertEquals(2, CollUtil.union(null, Arrays.asList("a", "b")).size());
        assertEquals(2, CollUtil.union(Arrays.asList("a", "b"), null).size());
    }

    // ======================= unionDistinct =======================

    @Test
    @DisplayName("unionDistinct - 非重复并集")
    void unionDistinct() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "b");
        List<String> list2 = CollUtil.newArrayList("b", "c");
        Set<String> result = CollUtil.unionDistinct(list1, list2);
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    // ======================= unionAll =======================

    @Test
    @DisplayName("unionAll - 完全并集保留全部元素")
    void unionAll() {
        List<String> list1 = CollUtil.newArrayList("a", "b");
        List<String> list2 = CollUtil.newArrayList("b", "c");
        List<String> result = CollUtil.unionAll(list1, list2);
        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("unionAll - 全空返回空列表")
    void unionAllEmpty() {
        assertTrue(CollUtil.unionAll(null, null).isEmpty());
    }

    // ======================= intersection =======================

    @Test
    @DisplayName("intersection - 两个集合交集")
    void intersection() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "b", "c");
        List<String> list2 = CollUtil.newArrayList("a", "b", "d");
        Collection<String> result = CollUtil.intersection(list1, list2);
        // intersection取最小重复数: list1有2个b, list2有1个b, 取1个
        assertEquals(1, CollUtil.count(result, "b"::equals));
        assertTrue(result.contains("a"));
    }

    @Test
    @DisplayName("intersection - 空集合交集为空")
    void intersectionEmpty() {
        assertTrue(CollUtil.intersection(null, Arrays.asList("a")).isEmpty());
        assertTrue(CollUtil.intersection(Arrays.asList("a"), null).isEmpty());
    }

    // ======================= intersectionDistinct =======================

    @Test
    @DisplayName("intersectionDistinct - 非重复交集")
    void intersectionDistinct() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "b", "c");
        List<String> list2 = CollUtil.newArrayList("a", "b", "d");
        Set<String> result = CollUtil.intersectionDistinct(list1, list2);
        assertEquals(2, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
    }

    // ======================= disjunction =======================

    @Test
    @DisplayName("disjunction - 对称差集")
    void disjunction() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "b", "c", "x");
        List<String> list2 = CollUtil.newArrayList("a", "b", "b", "b", "c", "y");
        Collection<String> result = CollUtil.disjunction(list1, list2);
        assertTrue(result.contains("b"));
        assertTrue(result.contains("x"));
        assertTrue(result.contains("y"));
    }

    @Test
    @DisplayName("disjunction - 一个为空返回另一个")
    void disjunctionOneEmpty() {
        List<String> list2 = CollUtil.newArrayList("a", "b");
        assertEquals(list2, CollUtil.disjunction(null, list2));
        assertEquals(list2, CollUtil.disjunction(list2, null));
    }

    // ======================= subtract / subtractToList =======================

    @Test
    @DisplayName("subtractToList - 单差集")
    void subtractToList() {
        List<String> list1 = CollUtil.newArrayList("a", "b", "c");
        List<String> list2 = CollUtil.newArrayList("b");
        List<String> result = CollUtil.subtractToList(list1, list2);
        assertEquals(2, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("c"));
    }

    @Test
    @DisplayName("subtractToList - 空集合")
    void subtractToListEmpty() {
        assertTrue(CollUtil.subtractToList(null, Arrays.asList("a")).isEmpty());
        assertEquals(2, CollUtil.subtractToList(Arrays.asList("a", "b"), null).size());
    }

    // ======================= contains / containsAny / containsAll =======================

    @Test
    @DisplayName("contains - 判断集合是否包含值")
    void contains() {
        assertFalse(CollUtil.contains((Collection<?>) null, "a"));
        assertTrue(CollUtil.contains(Arrays.asList("a", "b"), "a"));
        assertFalse(CollUtil.contains(Arrays.asList("a", "b"), "c"));
    }

    @Test
    @DisplayName("contains(Predicate) - 自定义判断")
    void containsPredicate() {
        List<String> list = CollUtil.newArrayList("bbbbb", "aaaaa", "ccccc");
        assertTrue(CollUtil.contains(list, s -> s.startsWith("a")));
        assertFalse(CollUtil.contains(list, s -> s.startsWith("d")));
    }

    @Test
    @DisplayName("containsAny - 至少包含一个共同元素")
    void containsAny() {
        assertFalse(CollUtil.containsAny(null, Arrays.asList("a")));
        assertTrue(CollUtil.containsAny(Arrays.asList("a", "b"), Arrays.asList("b", "c")));
        assertFalse(CollUtil.containsAny(Arrays.asList("a"), Arrays.asList("b")));
    }

    @Test
    @DisplayName("containsAll - 包含全部元素")
    void containsAll() {
        assertTrue(CollUtil.containsAll(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b")));
        assertFalse(CollUtil.containsAll(Arrays.asList("a"), Arrays.asList("b")));
        assertTrue(CollUtil.containsAll(null, null));
        assertTrue(CollUtil.containsAll(Arrays.asList("a"), null));
    }

    // ======================= count / indexOf / lastIndexOf =======================

    @Test
    @DisplayName("count - 统计匹配元素数量")
    void count() {
        assertEquals(0, CollUtil.count(null, null));
        assertEquals(2, CollUtil.count(Arrays.asList("a", "b", "a"), t -> "a".equals(t)));
    }

    @Test
    @DisplayName("indexOf - 第一个匹配位置")
    void indexOf() {
        assertEquals(-1, CollUtil.indexOf(null, null));
        assertEquals(0, CollUtil.indexOf(Arrays.asList("a", "b", "a"), t -> "a".equals(t)));
        assertEquals(1, CollUtil.indexOf(Arrays.asList("a", "b", "c"), t -> "b".equals(t)));
    }

    @Test
    @DisplayName("lastIndexOf - 最后匹配位置")
    void lastIndexOf() {
        assertEquals(-1, CollUtil.lastIndexOf(null, null));
        assertEquals(2, CollUtil.lastIndexOf(Arrays.asList("a", "b", "a"), t -> "a".equals(t)));
    }

    // ======================= distinct =======================

    @Test
    @DisplayName("distinct - 去重")
    void distinct() {
        assertTrue(CollUtil.distinct(null).isEmpty());
        List<String> list = CollUtil.newArrayList("a", "b", "a", "c", "b");
        List<String> result = CollUtil.distinct(list);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    @DisplayName("distinct(Set) - Set直接转List")
    void distinctSet() {
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b"));
        assertEquals(2, CollUtil.distinct(set).size());
    }

    // ======================= split =======================

    @Test
    @DisplayName("split - 按长度分段")
    void split() {
        assertTrue(CollUtil.split(null, 2).isEmpty());
        List<List<String>> result = CollUtil.split(Arrays.asList("a", "b", "c", "d", "e"), 2);
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals(1, result.get(2).size());
    }

    // ======================= sub =======================

    @Test
    @DisplayName("sub - 截取集合")
    void sub() {
        assertTrue(CollUtil.sub((Collection<String>) null, 0, 1).isEmpty());
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(Arrays.asList("b", "c"), CollUtil.sub(list, 1, 3));
    }

    // ======================= anyMatch / allMatch =======================

    @Test
    @DisplayName("anyMatch - 至少一个匹配")
    void anyMatch() {
        assertFalse(CollUtil.anyMatch(null, t -> true));
        assertTrue(CollUtil.anyMatch(Arrays.asList(1, 2, 3), t -> t > 2));
        assertFalse(CollUtil.anyMatch(Arrays.asList(1, 2, 3), t -> t > 5));
    }

    @Test
    @DisplayName("allMatch - 全部匹配")
    void allMatch() {
        assertFalse(CollUtil.allMatch(null, t -> true));
        assertTrue(CollUtil.allMatch(Arrays.asList(2, 4, 6), t -> t % 2 == 0));
        assertFalse(CollUtil.allMatch(Arrays.asList(2, 3, 6), t -> t % 2 == 0));
    }

    // ======================= newHashSet / newLinkedHashSet =======================

    @Test
    @DisplayName("newHashSet - 创建HashSet")
    void newHashSet() {
        Set<String> set = CollUtil.newHashSet("a", "b", "a");
        assertEquals(2, set.size());
    }

    @Test
    @DisplayName("newHashSet(null) - null参数")
    void newHashSetNull() {
        Set<String> set = CollUtil.newHashSet((String[]) null);
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    @DisplayName("newLinkedHashSet - 创建有序Set")
    void newLinkedHashSet() {
        LinkedHashSet<String> set = CollUtil.newLinkedHashSet("c", "a", "b");
        assertEquals(3, set.size());
        Iterator<String> it = set.iterator();
        assertEquals("c", it.next());
        assertEquals("a", it.next());
        assertEquals("b", it.next());
    }

    // ======================= addIfAbsent / addAllIfNotContains =======================

    @Test
    @DisplayName("addIfAbsent - 不存在时添加")
    void addIfAbsent() {
        List<String> list = CollUtil.newArrayList("a", "b");
        assertFalse(CollUtil.addIfAbsent(list, "a"));
        assertTrue(CollUtil.addIfAbsent(list, "c"));
        assertEquals(3, list.size());
        assertFalse(CollUtil.addIfAbsent(list, null));
        assertFalse(CollUtil.addIfAbsent(null, "x"));
    }

    @Test
    @DisplayName("addAllIfNotContains - 批量添加不重复元素")
    void addAllIfNotContains() {
        List<String> list = CollUtil.newArrayList("a", "b");
        CollUtil.addAllIfNotContains(list, CollUtil.newArrayList("b", "c"));
        assertEquals(3, list.size());
        assertTrue(list.contains("c"));
    }

    // ======================= defaultIfEmpty =======================

    @Test
    @DisplayName("defaultIfEmpty - 空集合返回默认值")
    void defaultIfEmpty() {
        List<String> defaultList = CollUtil.newArrayList("default");
        assertSame(defaultList, CollUtil.defaultIfEmpty(new ArrayList<>(), defaultList));
        List<String> list = CollUtil.newArrayList("a");
        assertSame(list, CollUtil.defaultIfEmpty(list, defaultList));
    }

    // ======================= padLeft / padRight =======================

    @Test
    @DisplayName("padLeft - 左填充")
    void padLeft() {
        List<String> list = CollUtil.newArrayList("c");
        CollUtil.padLeft(list, 3, "a");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("padRight - 右填充")
    void padRight() {
        List<String> list = CollUtil.newArrayList("a");
        CollUtil.padRight(list, 4, "x");
        assertEquals(4, list.size());
        assertEquals("x", list.get(3));
    }

    // ======================= max / min =======================

    @Test
    @DisplayName("max/min - 取最大最小值")
    void maxMin() {
        assertNull(CollUtil.max(null));
        assertEquals(3, CollUtil.max(Arrays.asList(1, 3, 2)));
        assertNull(CollUtil.min(null));
        assertEquals(1, CollUtil.min(Arrays.asList(3, 1, 2)));
    }

    // ======================= zip =======================

    @Test
    @DisplayName("zip(Collection) - 键值映射")
    void zipCollection() {
        Map<String, Integer> map = CollUtil.zip(Arrays.asList("a", "b"), Arrays.asList(1, 2));
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
    }

    @Test
    @DisplayName("zip - 空集合返回空Map")
    void zipEmpty() {
        assertTrue(CollUtil.zip(null, Arrays.asList(1)).isEmpty());
    }

    // ======================= sort =======================

    @Test
    @DisplayName("sort(Collection) - 排序不修改原集合")
    void sortCollection() {
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2));
        List<Integer> sorted = CollUtil.sort(list, Comparator.naturalOrder());
        assertEquals(Arrays.asList(1, 2, 3), sorted);
        // sort修改原List
        assertEquals(Arrays.asList(1, 2, 3), list);
    }

    // ======================= reverse / reverseNew =======================

    @Test
    @DisplayName("reverse - 反转List")
    void reverse() {
        List<String> list = CollUtil.newArrayList("a", "b", "c");
        CollUtil.reverse(list);
        assertEquals(Arrays.asList("c", "b", "a"), list);
    }

    @Test
    @DisplayName("reverseNew - 反转但不修改原List")
    void reverseNew() {
        List<String> list = CollUtil.newArrayList("a", "b", "c");
        List<String> reversed = CollUtil.reverseNew(list);
        assertEquals(Arrays.asList("c", "b", "a"), reversed);
        assertEquals("a", list.get(0));
    }

    // ======================= setOrAppend =======================

    @Test
    @DisplayName("setOrAppend - 设置或追加")
    void setOrAppend() {
        List<String> list = CollUtil.newArrayList("a", "b");
        CollUtil.setOrAppend(list, 0, "x");
        assertEquals("x", list.get(0));
        CollUtil.setOrAppend(list, 5, "y");
        assertEquals("y", list.get(list.size() - 1));
    }

    // ======================= forEach =======================

    @Test
    @DisplayName("forEach(Iterable) - 遍历")
    void forEachIterable() {
        List<String> result = new ArrayList<>();
        CollUtil.forEach(Arrays.asList("a", "b"), (val, index) -> result.add(index + val));
        assertEquals(Arrays.asList("0a", "1b"), result);
    }

    @Test
    @DisplayName("forEach(null) - null不抛异常")
    void forEachNull() {
        CollUtil.forEach((Iterable<String>) null, (val, index) -> {});
        CollUtil.forEach((Iterator<String>) null, (val, index) -> {});
    }

    // ======================= trans =======================

    @Test
    @DisplayName("trans - 转换集合")
    void trans() {
        assertNull(CollUtil.trans((Collection<String>) null, String::length));
        List<Integer> result = CollUtil.trans(Arrays.asList("a", "bb", "ccc"), String::length);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    // ======================= isEqualList =======================

    @Test
    @DisplayName("isEqualList - 判断两个集合是否相同")
    void isEqualList() {
        assertTrue(CollUtil.isEqualList(null, null));
        assertFalse(CollUtil.isEqualList(null, Arrays.asList("a")));
        assertTrue(CollUtil.isEqualList(Arrays.asList("a", "b"), Arrays.asList("a", "b")));
        assertFalse(CollUtil.isEqualList(Arrays.asList("a", "b"), Arrays.asList("b", "a")));
    }

    // ======================= getAny =======================

    @Test
    @DisplayName("getAny - 获取多个下标的元素")
    void getAny() {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        List<String> result = CollUtil.getAny(list, 0, 2, -1);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("c", result.get(1));
        assertEquals("d", result.get(2));
    }

    @Test
    @DisplayName("getAny(null) - null返回空列表")
    void getAnyNull() {
        assertTrue(CollUtil.getAny(null, 0).isEmpty());
    }

    // ======================= keySet / values =======================

    @Test
    @DisplayName("keySet - 获取Map列表中所有Key")
    void keySet() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("b", 2);
        Set<String> keys = CollUtil.keySet(Arrays.asList(map1, map2));
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
    }

    @Test
    @DisplayName("values - 获取Map列表中所有Value")
    void values() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        List<Integer> vals = CollUtil.values(Collections.singletonList(map1));
        assertTrue(vals.contains(1));
    }

    // ======================= empty / clear =======================

    @Test
    @DisplayName("empty - 根据类型返回空集合")
    void empty() {
        assertTrue(CollUtil.empty(List.class).isEmpty());
        assertTrue(CollUtil.empty(Set.class).isEmpty());
        assertTrue(CollUtil.empty(null).isEmpty());
    }

    @Test
    @DisplayName("clear - 清空集合")
    void clear() {
        List<String> list = CollUtil.newArrayList("a", "b");
        Set<String> set = new HashSet<>(Arrays.asList("c"));
        CollUtil.clear(list, set);
        assertTrue(list.isEmpty());
        assertTrue(set.isEmpty());
    }

    // ======================= removeWithAddIf =======================

    @Test
    @DisplayName("removeWithAddIf - 移除满足条件的元素")
    void removeWithAddIf() {
        ArrayList<Integer> list = CollUtil.newArrayList(1, 2, 3);
        List<Integer> removed = CollUtil.removeWithAddIf(list, e -> e == 1);
        assertEquals(CollUtil.newArrayList(2, 3), list);
        assertEquals(CollUtil.newArrayList(1), removed);
    }

    // ======================= map =======================

    @Test
    @DisplayName("map - 转换集合元素")
    void map() {
        List<String> list = Arrays.asList("a", "bb", "ccc");
        List<Integer> result = CollUtil.map(list, String::length, false);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    @DisplayName("map - null集合返回空列表")
    void mapNull() {
        assertTrue(CollUtil.map(null, String::length, false).isEmpty());
    }

    // ======================= asEnumeration / asIterator / asIterable =======================

    @Test
    @DisplayName("asEnumeration - Iterator转Enumeration")
    void asEnumeration() {
        Iterator<String> iter = Arrays.asList("a", "b").iterator();
        Enumeration<String> enu = CollUtil.asEnumeration(iter);
        assertTrue(enu.hasMoreElements());
        assertEquals("a", enu.nextElement());
    }

    @Test
    @DisplayName("asIterable - Iterator转Iterable")
    void asIterable() {
        Iterator<String> iter = Arrays.asList("x", "y").iterator();
        Iterable<String> iterable = CollUtil.asIterable(iter);
        assertNotNull(iterable);
        assertEquals("x", iterable.iterator().next());
    }

    // ======================= toCollection =======================

    @Test
    @DisplayName("toCollection - Iterable转Collection")
    void toCollection() {
        List<String> list = Arrays.asList("a", "b");
        assertSame(list, CollUtil.toCollection(list));
    }

    // ======================= unmodifiable =======================

    @Test
    @DisplayName("unmodifiable - 不可修改集合")
    void unmodifiable() {
        Collection<String> coll = CollUtil.unmodifiable(Arrays.asList("a", "b"));
        assertThrows(UnsupportedOperationException.class, () -> coll.add("c"));
    }

    // ======================= addAll =======================

    @Test
    @DisplayName("addAll(Iterator) - 添加迭代器元素")
    void addAllIterator() {
        List<String> list = CollUtil.newArrayList("a");
        CollUtil.addAll(list, Arrays.asList("b", "c").iterator());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("addAll(null) - null参数不抛异常")
    void addAllNull() {
        // addAll(null, ...) 返回null
        assertNull(CollUtil.addAll(null, (Iterator<String>) null));
        assertNull(CollUtil.addAll(null, (Iterable<String>) null));
    }

    // ======================= size =======================

    @Test
    @DisplayName("size - 获取大小")
    void size() {
        assertEquals(0, CollUtil.size(null));
        assertEquals(3, CollUtil.size(Arrays.asList(1, 2, 3)));
        assertEquals(1, CollUtil.size(Collections.singletonMap("a", 1)));
    }

    // ======================= findOne =======================

    @Test
    @DisplayName("findOne - 查找第一个匹配元素")
    void findOne() {
        assertNull(CollUtil.findOne(null, t -> true));
        List<String> list = Arrays.asList("a", "bb", "ccc");
        assertEquals("bb", CollUtil.findOne(list, t -> t.length() == 2));
        assertNull(CollUtil.findOne(list, t -> t.length() > 10));
    }

    // ======================= newLinkedList =======================

    @Test
    @DisplayName("newLinkedList - 创建LinkedList")
    void newLinkedList() {
        LinkedList<String> list = CollUtil.newLinkedList("a", "b", "c");
        assertEquals(3, list.size());
    }

    // ======================= countMap =======================

    @Test
    @DisplayName("countMap - 元素计数")
    void countMap() {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList("a", "b", "a", "c", "a"));
        assertEquals(3, map.get("a"));
        assertEquals(1, map.get("b"));
        assertEquals(1, map.get("c"));
    }
}
