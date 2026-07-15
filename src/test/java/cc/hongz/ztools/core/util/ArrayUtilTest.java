package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ArrayUtil 单元测试.
 */
class ArrayUtilTest {

    // ==================================================================== isEmpty / isNotEmpty / defaultIfEmpty

    /**
     * 泛型数组 null 和空数组判定.
     */
    @Test
    void isEmptyGeneric() {
        assertTrue(ArrayUtil.isEmpty((Object[]) null));
        assertTrue(ArrayUtil.isEmpty(new Object[0]));
        assertFalse(ArrayUtil.isEmpty(new Object[]{1}));
    }

    /**
     * defaultIfEmpty 返回默认数组.
     */
    @Test
    void defaultIfEmpty() {
        Object[] def = {"default"};
        assertSame(def, ArrayUtil.defaultIfEmpty(null, def));
        assertSame(def, ArrayUtil.defaultIfEmpty(new Object[0], def));
        Object[] data = {"a"};
        assertSame(data, ArrayUtil.defaultIfEmpty(data, def));
    }

    /**
     * Object 类型数组判空（含原始类型数组）.
     */
    @Test
    void isEmptyObject() {
        assertTrue(ArrayUtil.isEmpty((Object) null));
        assertTrue(ArrayUtil.isEmpty(new int[0]));
        assertFalse(ArrayUtil.isEmpty(new int[]{1}));
        assertFalse(ArrayUtil.isEmpty("hello"));
    }

    /**
     * 泛型数组非空判定.
     */
    @Test
    void isNotEmptyGeneric() {
        assertFalse(ArrayUtil.isNotEmpty((Object[]) null));
        assertFalse(ArrayUtil.isNotEmpty(new Object[0]));
        assertTrue(ArrayUtil.isNotEmpty(new Object[]{1}));
    }

    /**
     * Object 类型数组非空判定.
     */
    @Test
    void isNotEmptyObject() {
        assertFalse(ArrayUtil.isNotEmpty((Object) null));
        assertFalse(ArrayUtil.isNotEmpty(new int[0]));
        assertTrue(ArrayUtil.isNotEmpty(new int[]{1}));
    }

    // ==================================================================== hasNull / isAllNull / firstNonNull / firstMatch / matchIndex

    /**
     * 检查数组是否包含 null 元素.
     */
    @Test
    void hasNull() {
        assertTrue(ArrayUtil.hasNull((Object[]) null));
        assertFalse(ArrayUtil.hasNull());
        assertFalse(ArrayUtil.hasNull("a", "b"));
        assertTrue(ArrayUtil.hasNull("a", null, "b"));
    }

    /**
     * 检查数组是否全为 null.
     */
    @Test
    void isAllNull() {
        assertTrue(ArrayUtil.isAllNull((Object[]) null));
        assertTrue(ArrayUtil.isAllNull(null, null));
        assertFalse(ArrayUtil.isAllNull("a", null));
    }

    /**
     * 返回第一个非 null 元素.
     */
    @Test
    void firstNonNull() {
        assertNull(ArrayUtil.firstNonNull((Object[]) null));
        assertNull(ArrayUtil.firstNonNull(null, null));
        assertEquals("a", ArrayUtil.firstNonNull(null, "a", "b"));
    }

    /**
     * 返回第一个匹配元素.
     */
    @Test
    void firstMatch() {
        assertNull(ArrayUtil.firstMatch(x -> x != null && x.length() > 5, "a", "bb", "ccc"));
        assertEquals("ccc", ArrayUtil.firstMatch(x -> x != null && x.length() > 2, "a", "bb", "ccc", "dddd"));
    }

    /**
     * 返回第一个匹配位置.
     */
    @Test
    void matchIndex() {
        assertEquals(-1, ArrayUtil.<String>matchIndex(x -> "b".equals(x), "a", "c"));
        assertEquals(1, ArrayUtil.<String>matchIndex(x -> "b".equals(x), "a", "b", "c"));
        assertEquals(2, ArrayUtil.<String>matchIndex(x -> "c".equals(x), 1, "a", "b", "c"));
    }

    // ==================================================================== newArray / getComponentType / getArrayType / cast

    /**
     * 新建空数组.
     */
    @Test
    void newArrayWithComponentType() {
        String[] arr = ArrayUtil.newArray(String.class, 3);
        assertEquals(3, arr.length);
        assertNull(arr[0]);
    }

    /**
     * 新建 Object 数组.
     */
    @Test
    void newArrayWithSize() {
        Object[] arr = ArrayUtil.newArray(5);
        assertEquals(5, arr.length);
    }

    /**
     * 获取数组元素类型.
     */
    @Test
    void getComponentType() {
        assertEquals(int.class, ArrayUtil.getComponentType(new int[0]));
        assertEquals(String.class, ArrayUtil.getComponentType(new String[0]));
        assertNull(ArrayUtil.getComponentType(null));
        assertNull(ArrayUtil.getComponentType((Class<?>) null));
        assertEquals(int.class, ArrayUtil.getComponentType(int[].class));
    }

    /**
     * 获取数组类型.
     */
    @Test
    void getArrayType() {
        assertEquals(int[].class, ArrayUtil.getArrayType(int.class));
    }

    /**
     * 强转数组类型.
     */
    @Test
    void cast() {
        Object[] original = {"a", "b"};
        Object[] casted = ArrayUtil.cast(String.class, original);
        assertEquals(2, casted.length);
        assertEquals("a", casted[0]);
        assertEquals("b", casted[1]);
    }

    /**
     * cast 参数为 null 抛出异常.
     */
    @Test
    void castNullThrows() {
        assertThrows(NullPointerException.class, () -> ArrayUtil.cast(String.class, null));
    }

    /**
     * cast 非数组对象抛出异常.
     */
    @Test
    void castNonArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.cast(String.class, "hello"));
    }

    // ==================================================================== append / insert / setOrAppend / replace

    /**
     * 追加元素到数组末尾.
     */
    @Test
    void append() {
        String[] result = ArrayUtil.append(new String[]{"a"}, "b");
        assertArrayEquals(new String[]{"a", "b"}, result);
    }

    /**
     * 追加到空数组返回新元素数组.
     */
    @Test
    void appendToEmpty() {
        String[] result = ArrayUtil.append((String[]) null, "a");
        assertArrayEquals(new String[]{"a"}, result);
    }

    /**
     * 插入元素到指定位置.
     */
    @Test
    void insert() {
        String[] result = ArrayUtil.insert(new String[]{"a", "c"}, 1, "b");
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    /**
     * 插入到空数组返回新元素数组.
     */
    @Test
    void insertToEmpty() {
        String[] result = ArrayUtil.insert((String[]) null, 0, "a");
        assertArrayEquals(new String[]{"a"}, result);
    }

    /**
     * 插入到负数位置（从尾部计数）.
     */
    @Test
    void insertNegativeIndex() {
        String[] result = ArrayUtil.insert(new String[]{"a", "b", "c"}, -1, "x");
        assertEquals(4, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("x", result[2]);
        assertEquals("c", result[3]);
    }

    /**
     * setOrAppend 在有效位置替换.
     */
    @Test
    void setOrAppendReplace() {
        String[] arr = {"a", "b", "c"};
        String[] result = ArrayUtil.setOrAppend(arr, 1, "x");
        assertSame(arr, result);
        assertEquals("x", arr[1]);
    }

    /**
     * setOrAppend 超出长度追加.
     */
    @Test
    void setOrAppendAppend() {
        String[] arr = {"a", "b"};
        String[] result = ArrayUtil.setOrAppend(arr, 5, "c");
        assertEquals(3, result.length);
        assertEquals("c", result[2]);
    }

    /**
     * replace 替换中间段.
     */
    @Test
    void replaceMiddle() {
        String[] result = ArrayUtil.replace(new String[]{"a", "b", "c", "d"}, 1, "x", "y");
        assertArrayEquals(new String[]{"a", "x", "y", "d"}, result);
    }

    /**
     * replace 在空数组上返回新元素数组.
     */
    @Test
    void replaceEmpty() {
        String[] result = ArrayUtil.replace((String[]) null, 0, "a");
        assertArrayEquals(new String[]{"a"}, result);
    }

    /**
     * replace 超出长度追加.
     */
    @Test
    void replaceBeyondLength() {
        String[] result = ArrayUtil.replace(new String[]{"a", "b"}, 1, "x", "y", "z");
        assertEquals(4, result.length);
        assertEquals("a", result[0]);
        assertEquals("x", result[1]);
    }

    // ==================================================================== resize / addAll / copy / clone

    /**
     * 泛型数组 resize.
     */
    @Test
    void resizeGeneric() {
        String[] original = {"a", "b", "c"};
        String[] result = ArrayUtil.resize(original, 5, String.class);
        assertEquals(5, result.length);
        assertEquals("a", result[0]);
        assertNull(result[3]);
    }

    /**
     * resize 泛型数组缩小.
     */
    @Test
    void resizeGenericShrink() {
        String[] original = {"a", "b", "c"};
        String[] result = ArrayUtil.resize(original, 1, String.class);
        assertEquals(1, result.length);
        assertEquals("a", result[0]);
    }

    /**
     * addAll 合并多个数组.
     */
    @Test
    void addAllGeneric() {
        String[] result = ArrayUtil.addAll(new String[]{"a"}, new String[]{"b", "c"});
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    /**
     * addAll 单数组返回原数组.
     */
    @Test
    void addAllSingleArray() {
        String[] arr = {"a"};
        assertSame(arr, ArrayUtil.addAll(arr));
    }

    /**
     * copy 数组复制.
     */
    @Test
    void copy() {
        int[] src = {1, 2, 3};
        int[] dest = new int[3];
        ArrayUtil.copy(src, dest, 3);
        assertArrayEquals(new int[]{1, 2, 3}, dest);
    }

    /**
     * copy null 抛出异常.
     */
    @Test
    void copyNullThrows() {
        assertThrows(NullPointerException.class, () -> ArrayUtil.copy(null, new int[1], 1));
    }

    /**
     * clone 泛型数组.
     */
    @Test
    void cloneGeneric() {
        String[] arr = {"a", "b"};
        String[] cloned = ArrayUtil.clone(arr);
        assertNotSame(arr, cloned);
        assertArrayEquals(arr, cloned);
    }

    /**
     * clone null 返回 null.
     */
    @Test
    void cloneNull() {
        assertNull(ArrayUtil.clone((String[]) null));
    }

    /**
     * clone 原始类型数组.
     */
    @Test
    void clonePrimitive() {
        int[] arr = {1, 2, 3};
        int[] cloned = ArrayUtil.clone(arr);
        assertNotSame(arr, cloned);
        assertArrayEquals(arr, cloned);
    }

    // ==================================================================== edit / filter / removeNull / removeEmpty / removeBlank / nullToEmpty

    /**
     * edit 编辑数组元素.
     */
    @Test
    void edit() {
        String[] result = ArrayUtil.edit(new String[]{"a", "b", "c"}, String::toUpperCase);
        assertArrayEquals(new String[]{"A", "B", "C"}, result);
    }

    /**
     * edit 返回 null 的元素被过滤.
     */
    @Test
    void editFiltersNull() {
        String[] result = ArrayUtil.edit(new String[]{"a", "b", "c"}, t -> "b".equals(t) ? null : t);
        assertArrayEquals(new String[]{"a", "c"}, result);
    }

    /**
     * filter 过滤数组.
     */
    @Test
    void filter() {
        Integer[] result = ArrayUtil.filter(new Integer[]{1, 2, 3, 4, 5}, x -> x % 2 == 0);
        assertArrayEquals(new Integer[]{2, 4}, result);
    }

    /**
     * removeNull 去除 null 元素.
     */
    @Test
    void removeNull() {
        String[] result = ArrayUtil.removeNull(new String[]{"a", null, "b"});
        assertArrayEquals(new String[]{"a", "b"}, result);
    }

    /**
     * nullToEmpty 将 null 替换为空字符串.
     */
    @Test
    void nullToEmpty() {
        String[] result = ArrayUtil.nullToEmpty(new String[]{"a", null, "b"});
        assertArrayEquals(new String[]{"a", "", "b"}, result);
    }

    // ==================================================================== zip

    /**
     * zip 映射键值.
     */
    @Test
    void zip() {
        String[] keys = {"a", "b", "c"};
        Integer[] values = {1, 2, 3};
        Map<String, Integer> map = ArrayUtil.zip(keys, values);
        assertEquals(3, map.size());
        assertEquals(1, map.get("a"));
        assertEquals(3, map.get("c"));
    }

    /**
     * zip 有序 Map.
     */
    @Test
    void zipOrdered() {
        String[] keys = {"a", "b", "c"};
        Integer[] values = {1, 2, 3};
        Map<String, Integer> map = ArrayUtil.zip(keys, values, true);
        assertTrue(map instanceof java.util.LinkedHashMap);
    }

    /**
     * zip 长度不等取最短.
     */
    @Test
    void zipDifferentLength() {
        String[] keys = {"a", "b", "c"};
        Integer[] values = {1};
        Map<String, Integer> map = ArrayUtil.zip(keys, values);
        assertEquals(1, map.size());
    }

    /**
     * zip 空数组返回 null.
     */
    @Test
    void zipEmpty() {
        assertNull(ArrayUtil.zip(null, new Integer[]{1}));
    }

    // ==================================================================== indexOf / lastIndexOf / contains

    /**
     * indexOf 查找元素位置.
     */
    @Test
    void indexOf() {
        assertEquals(1, ArrayUtil.indexOf(new String[]{"a", "b", "c"}, "b"));
        assertEquals(-1, ArrayUtil.indexOf(new String[]{"a", "b", "c"}, "z"));
    }

    /**
     * indexOf 从指定位置查找.
     */
    @Test
    void indexOfFromIndex() {
        assertEquals(2, ArrayUtil.indexOf(new String[]{"a", "b", "b", "c"}, "b", 2));
        assertEquals(1, ArrayUtil.indexOf(new String[]{"a", "b", "b", "c"}, "b", 0));
    }

    /**
     * indexOfIgnoreCase 忽略大小写查找.
     */
    @Test
    void indexOfIgnoreCase() {
        assertEquals(1, ArrayUtil.indexOfIgnoreCase(new String[]{"Hello", "World"}, "world"));
        assertEquals(-1, ArrayUtil.indexOfIgnoreCase(new String[]{"Hello"}, "xyz"));
    }

    /**
     * lastIndexOf 从后向前查找.
     */
    @Test
    void lastIndexOf() {
        assertEquals(2, ArrayUtil.lastIndexOf(new String[]{"a", "b", "a"}, "a"));
        assertEquals(-1, ArrayUtil.lastIndexOf(new String[]{"a", "b"}, "z"));
    }

    /**
     * contains 检查是否包含.
     */
    @Test
    void contains() {
        assertTrue(ArrayUtil.contains(new String[]{"a", "b"}, "a"));
        assertFalse(ArrayUtil.contains(new String[]{"a", "b"}, "z"));
    }

    /**
     * containsAny 是否包含任一.
     */
    @Test
    void containsAny() {
        assertTrue(ArrayUtil.containsAny(new String[]{"a", "b", "c"}, "b", "z"));
        assertFalse(ArrayUtil.containsAny(new String[]{"a", "b"}, "x", "y"));
    }

    /**
     * containsAll 是否包含全部.
     */
    @Test
    void containsAll() {
        assertTrue(ArrayUtil.containsAll(new String[]{"a", "b", "c"}, "a", "b"));
        assertFalse(ArrayUtil.containsAll(new String[]{"a", "b", "c"}, "a", "z"));
    }

    /**
     * containsIgnoreCase 忽略大小写.
     */
    @Test
    void containsIgnoreCase() {
        assertTrue(ArrayUtil.containsIgnoreCase(new String[]{"Hello"}, "hello"));
    }

    // ==================================================================== wrap / isArray / get / getAny / sub

    /**
     * wrap 包装原始类型数组.
     */
    @Test
    void wrap() {
        Integer[] arr = {1, 2, 3};
        Object[] wrapped = ArrayUtil.wrap((Object) arr);
        assertEquals(3, wrapped.length);
        assertEquals(1, wrapped[0]);
    }

    /**
     * wrap null 返回 null.
     */
    @Test
    void wrapNull() {
        Object nullObj = null;
        assertNull(ArrayUtil.wrap(nullObj));
    }

    /**
     * wrap 非数组抛出异常.
     */
    @Test
    void wrapNonArrayThrows() {
        assertThrows(RuntimeException.class, () -> ArrayUtil.wrap("hello"));
    }

    /**
     * isArray 判定.
     */
    @Test
    void isArray() {
        assertTrue(ArrayUtil.isArray(new int[0]));
        assertTrue(ArrayUtil.isArray(new String[0]));
        assertFalse(ArrayUtil.isArray("hello"));
        assertFalse(ArrayUtil.isArray(null));
    }

    /**
     * get 获取指定位置元素.
     */
    @Test
    void get() {
        assertEquals("b", ArrayUtil.get(new String[]{"a", "b", "c"}, 1));
        assertNull(ArrayUtil.get(new String[]{"a"}, 5));
    }

    /**
     * get 负数索引.
     */
    @Test
    void getNegativeIndex() {
        assertEquals("c", ArrayUtil.get(new String[]{"a", "b", "c"}, -1));
        assertEquals("a", ArrayUtil.get(new String[]{"a", "b", "c"}, -3));
    }

    /**
     * get 获取 null 数组返回 null.
     */
    @Test
    void getNullArray() {
        assertNull(ArrayUtil.get(null, 0));
    }

    /**
     * getAny 获取多个位置元素.
     */
    @Test
    void getAny() {
        String[] result = ArrayUtil.getAny(new String[]{"a", "b", "c", "d"}, 0, 2, 3);
        assertArrayEquals(new String[]{"a", "c", "d"}, result);
    }

    /**
     * sub 获取子数组.
     */
    @Test
    void sub() {
        assertArrayEquals(new String[]{"b", "c"}, ArrayUtil.sub(new String[]{"a", "b", "c", "d"}, 1, 3));
    }

    /**
     * sub 负数索引.
     */
    @Test
    void subNegative() {
        assertArrayEquals(new String[]{"c", "d"}, ArrayUtil.sub(new String[]{"a", "b", "c", "d"}, -2, 4));
    }

    /**
     * sub 起始大于结束自动交换.
     */
    @Test
    void subSwap() {
        assertArrayEquals(new String[]{"b", "c"}, ArrayUtil.sub(new String[]{"a", "b", "c", "d"}, 3, 1));
    }

    /**
     * sub 带步进.
     */
    @Test
    void subWithStep() {
        Object[] result = ArrayUtil.sub(new String[]{"a", "b", "c", "d", "e"}, 0, 5, 2);
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("c", result[1]);
        assertEquals("e", result[2]);
    }

    // ==================================================================== toString / length / join

    /**
     * toString 原始类型数组转字符串.
     */
    @Test
    void toStringPrimitive() {
        assertEquals("[1, 2, 3]", ArrayUtil.toString(new int[]{1, 2, 3}));
        assertEquals("[a, b]", ArrayUtil.toString(new char[]{'a', 'b'}));
    }

    /**
     * toString 对象数组转字符串.
     */
    @Test
    void toStringObject() {
        assertEquals("[a, b]", ArrayUtil.toString(new String[]{"a", "b"}));
    }

    /**
     * toString null 返回 null.
     */
    @Test
    void toStringNull() {
        assertNull(ArrayUtil.toString(null));
    }

    /**
     * length 获取数组长度.
     */
    @Test
    void length() {
        assertEquals(3, ArrayUtil.length(new int[]{1, 2, 3}));
        assertEquals(0, ArrayUtil.length(null));
        assertEquals(2, ArrayUtil.length(new String[]{"a", "b"}));
    }

    /**
     * join 以分隔符连接数组.
     */
    @Test
    void join() {
        assertEquals("a,b,c", ArrayUtil.join(new String[]{"a", "b", "c"}, ","));
    }

    /**
     * join null 返回 null.
     */
    @Test
    void joinNull() {
        assertNull(ArrayUtil.join((String[]) null, ","));
    }

    /**
     * join 带前后缀.
     */
    @Test
    void joinWithPrefixSuffix() {
        assertEquals("[a],[b]", ArrayUtil.join(new String[]{"a", "b"}, ",", "[", "]"));
    }

    /**
     * join 带编辑器.
     */
    @Test
    void joinWithEditor() {
        String result = ArrayUtil.join(new String[]{"a", "b", "c"}, ",", String::toUpperCase);
        assertEquals("A,B,C", result);
    }

    /**
     * join 原始类型数组.
     */
    @Test
    void joinObjectArray() {
        assertEquals("1,2,3", ArrayUtil.join(new int[]{1, 2, 3}, ","));
    }

    /**
     * join 非数组抛出异常.
     */
    @Test
    void joinNonArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.join("hello", ","));
    }

    // ==================================================================== toArray

    /**
     * ByteBuffer 转 byte 数组.
     */
    @Test
    void toArrayByteBuffer() {
        ByteBuffer buf = ByteBuffer.wrap(new byte[]{1, 2, 3});
        byte[] arr = ArrayUtil.toArray(buf);
        assertArrayEquals(new byte[]{1, 2, 3}, arr);
    }

    /**
     * Iterator 转数组.
     */
    @Test
    void toArrayFromIterator() {
        List<String> list = Arrays.asList("a", "b", "c");
        String[] arr = ArrayUtil.toArray(list.iterator(), String.class);
        assertArrayEquals(new String[]{"a", "b", "c"}, arr);
    }

    /**
     * Iterable 转数组.
     */
    @Test
    void toArrayFromIterable() {
        List<String> list = Arrays.asList("a", "b");
        String[] arr = ArrayUtil.toArray(list, String.class);
        assertArrayEquals(new String[]{"a", "b"}, arr);
    }

    /**
     * Collection 转数组.
     */
    @Test
    void toArrayFromCollection() {
        List<String> list = Arrays.asList("a", "b", "c");
        String[] arr = ArrayUtil.toArray(list, String.class);
        assertArrayEquals(new String[]{"a", "b", "c"}, arr);
    }

    // ==================================================================== remove / removeEle

    /**
     * remove 移除指定位置元素.
     */
    @Test
    void remove() {
        String[] result = ArrayUtil.remove(new String[]{"a", "b", "c"}, 1);
        assertArrayEquals(new String[]{"a", "c"}, result);
    }

    /**
     * removeEle 移除指定元素.
     */
    @Test
    void removeEle() {
        String[] result = ArrayUtil.removeEle(new String[]{"a", "b", "c"}, "b");
        assertArrayEquals(new String[]{"a", "c"}, result);
    }

    /**
     * removeEle 只移除第一个匹配.
     */
    @Test
    void removeEleFirstOnly() {
        String[] result = ArrayUtil.removeEle(new String[]{"a", "b", "b", "c"}, "b");
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    // ==================================================================== reverse

    /**
     * reverse 反转整个数组.
     */
    @Test
    void reverse() {
        String[] arr = {"a", "b", "c"};
        ArrayUtil.reverse(arr);
        assertArrayEquals(new String[]{"c", "b", "a"}, arr);
    }

    /**
     * reverse 指定范围.
     */
    @Test
    void reverseRange() {
        String[] arr = {"a", "b", "c", "d", "e"};
        ArrayUtil.reverse(arr, 1, 4);
        assertArrayEquals(new String[]{"a", "d", "c", "b", "e"}, arr);
    }

    // ==================================================================== min / max

    /**
     * min 取最小值.
     */
    @Test
    void min() {
        assertEquals(1, ArrayUtil.min(new Integer[]{3, 1, 2}));
    }

    /**
     * min 空数组抛出异常.
     */
    @Test
    void minEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.min(new Integer[0]));
    }

    /**
     * min 带比较器.
     */
    @Test
    void minWithComparator() {
        assertEquals(3, ArrayUtil.min(new Integer[]{3, 1, 2}, Comparator.reverseOrder()));
    }

    /**
     * max 取最大值.
     */
    @Test
    void max() {
        assertEquals(3, ArrayUtil.max(new Integer[]{1, 3, 2}));
    }

    /**
     * max 空数组抛出异常.
     */
    @Test
    void maxEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.max(new Integer[0]));
    }

    /**
     * max 带比较器.
     */
    @Test
    void maxWithComparator() {
        assertEquals(1, ArrayUtil.max(new Integer[]{1, 3, 2}, Comparator.reverseOrder()));
    }

    // ==================================================================== shuffle / swap

    /**
     * shuffle 保留元素不变（顺序可能变）.
     */
    @Test
    void shufflePreservesElements() {
        Integer[] arr = {1, 2, 3, 4, 5};
        ArrayUtil.shuffle(arr);
        assertEquals(5, arr.length);
        java.util.Set<Integer> set = new java.util.HashSet<>(Arrays.asList(arr));
        assertEquals(5, set.size());
    }

    /**
     * shuffle 指定 Random.
     */
    @Test
    void shuffleWithRandom() {
        Integer[] arr = {1, 2, 3, 4, 5};
        ArrayUtil.shuffle(arr, new Random(42));
        assertEquals(5, arr.length);
    }

    /**
     * shuffle null 数组返回 null.
     */
    @Test
    void shuffleNull() {
        assertNull(ArrayUtil.shuffle((Integer[]) null));
    }

    /**
     * swap 交换位置.
     */
    @Test
    void swap() {
        String[] arr = {"a", "b", "c"};
        ArrayUtil.swap(arr, 0, 2);
        assertArrayEquals(new String[]{"c", "b", "a"}, arr);
    }

    /**
     * swap 空数组抛出异常.
     */
    @Test
    void swapEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.swap(new String[0], 0, 0));
    }

    // ==================================================================== emptyCount / hasEmpty / isAllEmpty / isAllNotEmpty / isAllNotNull

    /**
     * emptyCount 计算空元素个数.
     */
    @Test
    void emptyCount() {
        assertEquals(2, ArrayUtil.emptyCount("a", null, "", "b"));
        assertEquals(0, ArrayUtil.emptyCount("a", "b"));
    }

    /**
     * hasEmpty 是否存在空.
     */
    @Test
    void hasEmpty() {
        assertTrue(ArrayUtil.hasEmpty("a", null));
        assertTrue(ArrayUtil.hasEmpty("a", ""));
        assertFalse(ArrayUtil.hasEmpty("a", "b"));
    }

    /**
     * isAllEmpty 是否全为空.
     */
    @Test
    void isAllEmpty() {
        assertTrue(ArrayUtil.isAllEmpty(null, ""));
        assertFalse(ArrayUtil.isAllEmpty("a", null));
    }

    /**
     * isAllNotEmpty 是否全非空.
     */
    @Test
    void isAllNotEmpty() {
        assertTrue(ArrayUtil.isAllNotEmpty("a", "b"));
        assertFalse(ArrayUtil.isAllNotEmpty("a", null));
    }

    /**
     * isAllNotNull 是否全非 null.
     */
    @Test
    void isAllNotNull() {
        assertTrue(ArrayUtil.isAllNotNull("a", "b"));
        assertFalse(ArrayUtil.isAllNotNull("a", null));
    }

    // ==================================================================== distinct / map / mapToSet

    /**
     * distinct 去重.
     */
    @Test
    void distinct() {
        String[] result = ArrayUtil.distinct(new String[]{"a", "b", "a", "c", "b"});
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    /**
     * distinct 空数组返回原数组.
     */
    @Test
    void distinctEmpty() {
        assertNull(ArrayUtil.distinct(null));
    }

    /**
     * distinct 带键生成器（覆盖模式）.
     */
    @Test
    void distinctWithKeyOverride() {
        String[] result = ArrayUtil.distinct(new String[]{"A", "a", "B"}, String::toLowerCase, true);
        assertArrayEquals(new String[]{"a", "B"}, result);
    }

    /**
     * distinct 带键生成器（非覆盖模式）.
     */
    @Test
    void distinctWithKeyNoOverride() {
        String[] result = ArrayUtil.distinct(new String[]{"A", "a", "B"}, String::toLowerCase, false);
        assertArrayEquals(new String[]{"A", "B"}, result);
    }

    /**
     * map 转换类型并生成数组.
     */
    @Test
    void mapToArray() {
        Integer[] result = ArrayUtil.map(new String[]{"1", "2", "3"}, Integer.class, Integer::parseInt);
        assertArrayEquals(new Integer[]{1, 2, 3}, result);
    }

    /**
     * map 转换为 List.
     */
    @Test
    void mapToList() {
        List<Integer> result = ArrayUtil.map(new String[]{"1", "2"}, Integer::parseInt);
        assertEquals(Arrays.asList(1, 2), result);
    }

    /**
     * mapToSet 转换为 Set.
     */
    @Test
    void mapToSet() {
        Set<Integer> result = ArrayUtil.mapToSet(new String[]{"1", "2", "1"}, Integer::parseInt);
        assertEquals(2, result.size());
    }

    // ==================================================================== equals / isSub / indexOfSub / lastIndexOfSub

    /**
     * equals 判断数组相等.
     */
    @Test
    void equalsArrays() {
        assertTrue(ArrayUtil.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(ArrayUtil.equals(new int[]{1, 2}, new int[]{1, 2, 3}));
        assertTrue(ArrayUtil.equals(new String[]{"a", "b"}, new String[]{"a", "b"}));
    }

    /**
     * equals 同一对象.
     */
    @Test
    void equalsSame() {
        int[] arr = {1, 2};
        assertTrue(ArrayUtil.equals(arr, arr));
    }

    /**
     * equals 含 null.
     */
    @Test
    void equalsWithNull() {
        assertFalse(ArrayUtil.equals(null, new int[]{1}));
        assertFalse(ArrayUtil.equals(new int[]{1}, null));
        assertTrue(ArrayUtil.equals(null, null));
    }

    /**
     * isSub 是否为子数组.
     */
    @Test
    void isSub() {
        assertTrue(ArrayUtil.isSub(new String[]{"a", "b", "c", "d"}, new String[]{"b", "c"}));
        assertFalse(ArrayUtil.isSub(new String[]{"a", "b", "c"}, new String[]{"a", "c"}));
    }

    /**
     * indexOfSub 查找子数组位置.
     */
    @Test
    void indexOfSub() {
        assertEquals(1, ArrayUtil.indexOfSub(new String[]{"a", "b", "c", "d"}, new String[]{"b", "c"}));
        assertEquals(-1, ArrayUtil.indexOfSub(new String[]{"a", "b"}, new String[]{"c"}));
    }

    /**
     * lastIndexOfSub 查找最后一个子数组位置.
     */
    @Test
    void lastIndexOfSub() {
        assertEquals(2, ArrayUtil.lastIndexOfSub(new String[]{"a", "b", "a", "b"}, new String[]{"a", "b"}));
    }

    // ==================================================================== isSorted / isSortedASC / isSortedDESC

    /**
     * isSorted 带比较器.
     */
    @Test
    void isSortedWithComparator() {
        assertTrue(ArrayUtil.isSorted(new Integer[]{1, 2, 3}, Comparator.naturalOrder()));
        assertFalse(ArrayUtil.isSorted(new Integer[]{3, 1, 2}, Comparator.naturalOrder()));
        assertFalse(ArrayUtil.isSorted(null, Comparator.naturalOrder()));
    }

    /**
     * isSorted 升序判定.
     */
    @Test
    void isSortedASC() {
        assertTrue(ArrayUtil.isSortedASC(new Integer[]{1, 2, 3}));
        assertTrue(ArrayUtil.isSortedASC(new Integer[]{1, 1, 2}));
        assertFalse(ArrayUtil.isSortedASC(new Integer[]{3, 1}));
        assertFalse(ArrayUtil.isSortedASC((Integer[]) null));
    }

    /**
     * isSorted 降序判定.
     */
    @Test
    void isSortedDESC() {
        assertTrue(ArrayUtil.isSortedDESC(new Integer[]{3, 2, 1}));
        assertTrue(ArrayUtil.isSortedDESC(new Integer[]{3, 3, 2}));
        assertFalse(ArrayUtil.isSortedDESC(new Integer[]{1, 2}));
        assertFalse(ArrayUtil.isSortedDESC((Integer[]) null));
    }

    /**
     * isSorted 等价于 isSortedASC.
     */
    @Test
    void isSortedEqualsASC() {
        assertTrue(ArrayUtil.isSorted(new Integer[]{1, 2, 3}));
        assertFalse(ArrayUtil.isSorted(new Integer[]{3, 1}));
    }
}
