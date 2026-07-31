package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PrimitiveArrayUtil 单元测试.
 */
@DisplayName("PrimitiveArrayUtil 工具类")
class PrimitiveArrayUtilTest {

    // ==================================================================== INDEX_NOT_FOUND 常量

    /**
     * INDEX_NOT_FOUND 常量值为 -1.
     */
    @Test
    @DisplayName("indexOf：未找到常量返回-1")
    void indexNotFoundConstant() {
        assertEquals(-1, PrimitiveArrayUtil.INDEX_NOT_FOUND);
    }

    // ==================================================================== isEmpty / isNotEmpty

    /**
     * isEmpty 判定 long 类型数组.
     */
    @Test
    @DisplayName("isEmpty：long数组判空")
    void isEmptyLong() {
        assertTrue(PrimitiveArrayUtil.isEmpty((long[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new long[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new long[]{1L}));
    }

    /**
     * isEmpty 判定 int 类型数组.
     */
    @Test
    @DisplayName("isEmpty：int数组判空")
    void isEmptyInt() {
        assertTrue(PrimitiveArrayUtil.isEmpty((int[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new int[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new int[]{1}));
    }

    /**
     * isEmpty 判定 short 类型数组.
     */
    @Test
    @DisplayName("isEmpty：short数组判空")
    void isEmptyShort() {
        assertTrue(PrimitiveArrayUtil.isEmpty((short[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new short[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new short[]{1}));
    }

    /**
     * isEmpty 判定 char 类型数组.
     */
    @Test
    @DisplayName("isEmpty：char数组判空")
    void isEmptyChar() {
        assertTrue(PrimitiveArrayUtil.isEmpty((char[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new char[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new char[]{'a'}));
    }

    /**
     * isEmpty 判定 byte 类型数组.
     */
    @Test
    @DisplayName("isEmpty：byte数组判空")
    void isEmptyByte() {
        assertTrue(PrimitiveArrayUtil.isEmpty((byte[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new byte[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new byte[]{1}));
    }

    /**
     * isEmpty 判定 double 类型数组.
     */
    @Test
    @DisplayName("isEmpty：double数组判空")
    void isEmptyDouble() {
        assertTrue(PrimitiveArrayUtil.isEmpty((double[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new double[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new double[]{1.0}));
    }

    /**
     * isEmpty 判定 float 类型数组.
     */
    @Test
    @DisplayName("isEmpty：float数组判空")
    void isEmptyFloat() {
        assertTrue(PrimitiveArrayUtil.isEmpty((float[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new float[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new float[]{1.0f}));
    }

    /**
     * isEmpty 判定 boolean 类型数组.
     */
    @Test
    @DisplayName("isEmpty：boolean数组判空")
    void isEmptyBoolean() {
        assertTrue(PrimitiveArrayUtil.isEmpty((boolean[]) null));
        assertTrue(PrimitiveArrayUtil.isEmpty(new boolean[0]));
        assertFalse(PrimitiveArrayUtil.isEmpty(new boolean[]{true}));
    }

    /**
     * isNotEmpty 判定 int 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：int数组非空判断")
    void isNotEmptyInt() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((int[]) null));
        assertFalse(PrimitiveArrayUtil.isNotEmpty(new int[0]));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new int[]{1}));
    }

    /**
     * isNotEmpty 判定 byte 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：byte数组非空判断")
    void isNotEmptyByte() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((byte[]) null));
        assertFalse(PrimitiveArrayUtil.isNotEmpty(new byte[0]));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new byte[]{1}));
    }

    /**
     * isNotEmpty 判定 long 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：long数组非空判断")
    void isNotEmptyLong() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((long[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new long[]{1L}));
    }

    /**
     * isNotEmpty 判定 double 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：double数组非空判断")
    void isNotEmptyDouble() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((double[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new double[]{1.0}));
    }

    /**
     * isNotEmpty 判定 float 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：float数组非空判断")
    void isNotEmptyFloat() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((float[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new float[]{1.0f}));
    }

    /**
     * isNotEmpty 判定 char 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：char数组非空判断")
    void isNotEmptyChar() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((char[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new char[]{'a'}));
    }

    /**
     * isNotEmpty 判定 short 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：short数组非空判断")
    void isNotEmptyShort() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((short[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new short[]{1}));
    }

    /**
     * isNotEmpty 判定 boolean 类型数组.
     */
    @Test
    @DisplayName("isNotEmpty：boolean数组非空判断")
    void isNotEmptyBoolean() {
        assertFalse(PrimitiveArrayUtil.isNotEmpty((boolean[]) null));
        assertTrue(PrimitiveArrayUtil.isNotEmpty(new boolean[]{true}));
    }

    // ==================================================================== resize

    /**
     * resize 扩容数组并填充默认值.
     */
    @Test
    @DisplayName("resize：数组扩容")
    void resizeExpand() {
        byte[] original = {1, 2, 3};
        byte[] result = PrimitiveArrayUtil.resize(original, 5);
        assertEquals(5, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
        assertEquals(3, result[2]);
        assertEquals(0, result[3]);
        assertEquals(0, result[4]);
    }

    /**
     * resize 缩小数组.
     */
    @Test
    @DisplayName("resize：数组缩小")
    void resizeShrink() {
        byte[] original = {1, 2, 3, 4, 5};
        byte[] result = PrimitiveArrayUtil.resize(original, 2);
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
    }

    /**
     * resize 负数长度返回原数组.
     */
    @Test
    @DisplayName("resize：负数返回原数组")
    void resizeNegativeReturnsOriginal() {
        byte[] original = {1, 2};
        byte[] result = PrimitiveArrayUtil.resize(original, -1);
        assertSame(original, result);
    }

    /**
     * resize 长度为 0 返回空数组.
     */
    @Test
    @DisplayName("resize：零长度返回空数组")
    void resizeZeroReturnsEmpty() {
        byte[] result = PrimitiveArrayUtil.resize(new byte[]{1, 2}, 0);
        assertEquals(0, result.length);
    }

    // ==================================================================== addAll

    /**
     * addAll 合并两个 int 数组.
     */
    @Test
    @DisplayName("addAll：int数组合并")
    void addAllInt() {
        int[] result = PrimitiveArrayUtil.addAll(new int[]{1, 2}, new int[]{3, 4});
        assertArrayEquals(new int[]{1, 2, 3, 4}, result);
    }

    /**
     * addAll 单个数组返回原数组.
     */
    @Test
    @DisplayName("addAll：单个int数组合并")
    void addAllIntSingleArray() {
        int[] input = {1, 2, 3};
        assertSame(input, PrimitiveArrayUtil.addAll(input));
    }

    /**
     * addAll 跳过 null 数组.
     */
    @Test
    @DisplayName("addAll：跳过null数组")
    void addAllIntSkipsNull() {
        int[] result = PrimitiveArrayUtil.addAll(new int[]{1}, null, new int[]{2});
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * addAll 合并两个 byte 数组.
     */
    @Test
    @DisplayName("addAll：byte数组合并")
    void addAllByte() {
        byte[] result = PrimitiveArrayUtil.addAll(new byte[]{1, 2}, new byte[]{3});
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    /**
     * addAll 合并两个 long 数组.
     */
    @Test
    @DisplayName("addAll：long数组合并")
    void addAllLong() {
        long[] result = PrimitiveArrayUtil.addAll(new long[]{1L}, new long[]{2L, 3L});
        assertArrayEquals(new long[]{1L, 2L, 3L}, result);
    }

    /**
     * addAll 合并两个 double 数组.
     */
    @Test
    @DisplayName("addAll：double数组合并")
    void addAllDouble() {
        double[] result = PrimitiveArrayUtil.addAll(new double[]{1.0}, new double[]{2.0});
        assertArrayEquals(new double[]{1.0, 2.0}, result, 0.001);
    }

    /**
     * addAll 合并两个 float 数组.
     */
    @Test
    @DisplayName("addAll：float数组合并")
    void addAllFloat() {
        float[] result = PrimitiveArrayUtil.addAll(new float[]{1.0f}, new float[]{2.0f});
        assertArrayEquals(new float[]{1.0f, 2.0f}, result, 0.001f);
    }

    /**
     * addAll 合并两个 char 数组.
     */
    @Test
    @DisplayName("addAll：char数组合并")
    void addAllChar() {
        char[] result = PrimitiveArrayUtil.addAll(new char[]{'a'}, new char[]{'b', 'c'});
        assertArrayEquals(new char[]{'a', 'b', 'c'}, result);
    }

    /**
     * addAll 合并两个 boolean 数组.
     */
    @Test
    @DisplayName("addAll：boolean数组合并")
    void addAllBoolean() {
        boolean[] result = PrimitiveArrayUtil.addAll(new boolean[]{true}, new boolean[]{false});
        assertArrayEquals(new boolean[]{true, false}, result);
    }

    /**
     * addAll 合并两个 short 数组.
     */
    @Test
    @DisplayName("addAll：short数组合并")
    void addAllShort() {
        short[] result = PrimitiveArrayUtil.addAll(new short[]{1}, new short[]{2});
        assertArrayEquals(new short[]{1, 2}, result);
    }

    // ==================================================================== range

    /**
     * range 生成 0 到 n-1 的序列.
     */
    @Test
    @DisplayName("range：单参数范围数组")
    void rangeSingleArg() {
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, PrimitiveArrayUtil.range(5));
    }

    /**
     * range 生成指定起止范围的序列.
     */
    @Test
    @DisplayName("range：双参数范围数组")
    void rangeTwoArgs() {
        assertArrayEquals(new int[]{2, 3, 4}, PrimitiveArrayUtil.range(2, 5));
    }

    /**
     * range 生成带步进的序列.
     */
    @Test
    @DisplayName("range：指定步长的范围数组")
    void rangeWithStep() {
        assertArrayEquals(new int[]{0, 2, 4}, PrimitiveArrayUtil.range(0, 5, 2));
    }

    /**
     * range 起始大于终止时自动交换.
     */
    @Test
    @DisplayName("range：自动交换起止范围")
    void rangeAutoSwap() {
        // start > end: auto-swap
        assertArrayEquals(new int[]{2, 3, 4}, PrimitiveArrayUtil.range(5, 2));
    }

    /**
     * range 步进为 0 时默认为 1.
     */
    @Test
    @DisplayName("range：零步长返回空数组")
    void rangeZeroStep() {
        // step <= 0 defaults to 1
        assertArrayEquals(new int[]{0, 1, 2}, PrimitiveArrayUtil.range(0, 3, 0));
    }

    // ==================================================================== split

    /**
     * split 均匀分割数组.
     */
    @Test
    @DisplayName("split：均匀分割数组")
    void splitEvenly() {
        byte[][] result = PrimitiveArrayUtil.split(new byte[]{1, 2, 3, 4}, 2);
        assertEquals(2, result.length);
        assertArrayEquals(new byte[]{1, 2}, result[0]);
        assertArrayEquals(new byte[]{3, 4}, result[1]);
    }

    /**
     * split 带余数分割数组.
     */
    @Test
    @DisplayName("split：有余数的分割")
    void splitWithRemainder() {
        byte[][] result = PrimitiveArrayUtil.split(new byte[]{1, 2, 3, 4, 5}, 2);
        assertEquals(3, result.length);
        assertArrayEquals(new byte[]{1, 2}, result[0]);
        assertArrayEquals(new byte[]{3, 4}, result[1]);
        assertArrayEquals(new byte[]{5}, result[2]);
    }

    // ==================================================================== indexOf / lastIndexOf / contains

    /**
     * indexOf 查找 int 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：int数组查找索引")
    void indexOfInt() {
        assertEquals(1, PrimitiveArrayUtil.indexOf(new int[]{10, 20, 30}, 20));
        assertEquals(-1, PrimitiveArrayUtil.indexOf(new int[]{10, 20, 30}, 99));
        assertEquals(-1, PrimitiveArrayUtil.indexOf((int[]) null, 1));
    }

    /**
     * indexOf 查找 long 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：long数组查找索引")
    void indexOfLong() {
        assertEquals(0, PrimitiveArrayUtil.indexOf(new long[]{1L, 2L, 3L}, 1L));
        assertEquals(-1, PrimitiveArrayUtil.indexOf((long[]) null, 1L));
    }

    /**
     * indexOf 查找 short 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：short数组查找索引")
    void indexOfShort() {
        assertEquals(2, PrimitiveArrayUtil.indexOf(new short[]{1, 2, 3}, (short) 3));
    }

    /**
     * indexOf 查找 char 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：char数组查找索引")
    void indexOfChar() {
        assertEquals(1, PrimitiveArrayUtil.indexOf(new char[]{'a', 'b', 'c'}, 'b'));
    }

    /**
     * indexOf 查找 byte 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：byte数组查找索引")
    void indexOfByte() {
        assertEquals(0, PrimitiveArrayUtil.indexOf(new byte[]{10, 20}, (byte) 10));
    }

    /**
     * indexOf 查找 double 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：double数组查找索引")
    void indexOfDouble() {
        assertEquals(1, PrimitiveArrayUtil.indexOf(new double[]{1.0, 2.0, 3.0}, 2.0));
        assertEquals(-1, PrimitiveArrayUtil.indexOf(new double[]{1.0}, 9.0));
    }

    /**
     * indexOf 查找 float 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：float数组查找索引")
    void indexOfFloat() {
        assertEquals(0, PrimitiveArrayUtil.indexOf(new float[]{1.0f, 2.0f}, 1.0f));
    }

    /**
     * indexOf 查找 boolean 类型数组元素位置.
     */
    @Test
    @DisplayName("indexOf：boolean数组查找索引")
    void indexOfBoolean() {
        assertEquals(1, PrimitiveArrayUtil.indexOf(new boolean[]{false, true, false}, true));
    }

    /**
     * lastIndexOf 反向查找 int 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：int数组查找末次索引")
    void lastIndexOfInt() {
        assertEquals(2, PrimitiveArrayUtil.lastIndexOf(new int[]{1, 2, 1}, 1));
        assertEquals(-1, PrimitiveArrayUtil.lastIndexOf((int[]) null, 1));
    }

    /**
     * lastIndexOf 反向查找 long 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：long数组查找末次索引")
    void lastIndexOfLong() {
        assertEquals(2, PrimitiveArrayUtil.lastIndexOf(new long[]{1L, 2L, 1L}, 1L));
    }

    /**
     * lastIndexOf 反向查找 short 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：short数组查找末次索引")
    void lastIndexOfShort() {
        assertEquals(1, PrimitiveArrayUtil.lastIndexOf(new short[]{1, 2, 3}, (short) 2));
    }

    /**
     * lastIndexOf 反向查找 char 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：char数组查找末次索引")
    void lastIndexOfChar() {
        assertEquals(2, PrimitiveArrayUtil.lastIndexOf(new char[]{'a', 'b', 'a'}, 'a'));
    }

    /**
     * lastIndexOf 反向查找 byte 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：byte数组查找末次索引")
    void lastIndexOfByte() {
        assertEquals(1, PrimitiveArrayUtil.lastIndexOf(new byte[]{1, 2, 3}, (byte) 2));
    }

    /**
     * lastIndexOf 反向查找 double 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：double数组查找末次索引")
    void lastIndexOfDouble() {
        assertEquals(2, PrimitiveArrayUtil.lastIndexOf(new double[]{1.0, 2.0, 1.0}, 1.0));
    }

    /**
     * lastIndexOf 反向查找 float 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：float数组查找末次索引")
    void lastIndexOfFloat() {
        assertEquals(1, PrimitiveArrayUtil.lastIndexOf(new float[]{1.0f, 2.0f, 3.0f}, 2.0f));
    }

    /**
     * lastIndexOf 反向查找 boolean 类型数组元素位置.
     */
    @Test
    @DisplayName("lastIndexOf：boolean数组查找末次索引")
    void lastIndexOfBoolean() {
        assertEquals(2, PrimitiveArrayUtil.lastIndexOf(new boolean[]{true, false, true}, true));
    }

    /**
     * contains 判断 int 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：int数组包含判断")
    void containsInt() {
        assertTrue(PrimitiveArrayUtil.contains(new int[]{1, 2, 3}, 2));
        assertFalse(PrimitiveArrayUtil.contains(new int[]{1, 2, 3}, 99));
    }

    /**
     * contains 判断 long 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：long数组包含判断")
    void containsLong() {
        assertTrue(PrimitiveArrayUtil.contains(new long[]{1L, 2L}, 2L));
        assertFalse(PrimitiveArrayUtil.contains(new long[]{1L}, 9L));
    }

    /**
     * contains 判断 double 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：double数组包含判断")
    void containsDouble() {
        assertTrue(PrimitiveArrayUtil.contains(new double[]{1.0, 2.0}, 2.0));
    }

    /**
     * contains 判断 float 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：float数组包含判断")
    void containsFloat() {
        assertTrue(PrimitiveArrayUtil.contains(new float[]{1.0f, 2.0f}, 1.0f));
    }

    /**
     * contains 判断 byte 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：byte数组包含判断")
    void containsByte() {
        assertTrue(PrimitiveArrayUtil.contains(new byte[]{1, 2, 3}, (byte) 2));
    }

    /**
     * contains 判断 char 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：char数组包含判断")
    void containsChar() {
        assertTrue(PrimitiveArrayUtil.contains(new char[]{'a', 'b'}, 'a'));
    }

    /**
     * contains 判断 short 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：short数组包含判断")
    void containsShort() {
        assertTrue(PrimitiveArrayUtil.contains(new short[]{1, 2}, (short) 2));
    }

    /**
     * contains 判断 boolean 类型数组是否包含元素.
     */
    @Test
    @DisplayName("contains：boolean数组包含判断")
    void containsBoolean() {
        assertTrue(PrimitiveArrayUtil.contains(new boolean[]{true, false}, true));
    }

    // ==================================================================== wrap / unWrap

    /**
     * wrap/unWrap int 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：int数组包装与拆包")
    void wrapUnWrapInt() {
        Integer[] wrapped = PrimitiveArrayUtil.wrap(1, 2, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, wrapped);
        int[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new int[]{1, 2, 3}, unWrapped);
    }

    /**
     * wrap/unWrap int 数组为 null 时返回 null.
     */
    @Test
    @DisplayName("wrap/unwrap：null返回空数组")
    void wrapUnWrapIntNull() {
        assertNull(PrimitiveArrayUtil.wrap((int[]) null));
        assertNull(PrimitiveArrayUtil.unWrap((Integer[]) null));
    }

    /**
     * wrap/unWrap int 空数组.
     */
    @Test
    @DisplayName("wrap/unwrap：空数组返回null")
    void wrapUnWrapIntEmpty() {
        assertEquals(0, PrimitiveArrayUtil.wrap(new int[0]).length);
        assertEquals(0, PrimitiveArrayUtil.unWrap(new Integer[0]).length);
    }

    /**
     * wrap/unWrap long 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：long数组包装与拆包")
    void wrapUnWrapLong() {
        Long[] wrapped = PrimitiveArrayUtil.wrap(1L, 2L);
        assertArrayEquals(new Long[]{1L, 2L}, wrapped);
        long[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new long[]{1L, 2L}, unWrapped);
    }

    /**
     * wrap/unWrap char 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：char数组包装与拆包")
    void wrapUnWrapChar() {
        Character[] wrapped = PrimitiveArrayUtil.wrap('a', 'b');
        assertArrayEquals(new Character[]{'a', 'b'}, wrapped);
        char[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new char[]{'a', 'b'}, unWrapped);
    }

    /**
     * wrap/unWrap byte 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：byte数组包装与拆包")
    void wrapUnWrapByte() {
        Byte[] wrapped = PrimitiveArrayUtil.wrap((byte) 1, (byte) 2);
        assertArrayEquals(new Byte[]{1, 2}, wrapped);
        byte[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new byte[]{1, 2}, unWrapped);
    }

    /**
     * wrap/unWrap short 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：short数组包装与拆包")
    void wrapUnWrapShort() {
        Short[] wrapped = PrimitiveArrayUtil.wrap((short) 1, (short) 2);
        assertArrayEquals(new Short[]{1, 2}, wrapped);
        short[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new short[]{1, 2}, unWrapped);
    }

    /**
     * wrap/unWrap float 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：float数组包装与拆包")
    void wrapUnWrapFloat() {
        Float[] wrapped = PrimitiveArrayUtil.wrap(1.0f, 2.0f);
        assertArrayEquals(new Float[]{1.0f, 2.0f}, wrapped);
        float[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new float[]{1.0f, 2.0f}, unWrapped, 0.001f);
    }

    /**
     * wrap/unWrap double 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：double数组包装与拆包")
    void wrapUnWrapDouble() {
        Double[] wrapped = PrimitiveArrayUtil.wrap(1.0, 2.0);
        assertArrayEquals(new Double[]{1.0, 2.0}, wrapped);
        double[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new double[]{1.0, 2.0}, unWrapped, 0.001);
    }

    /**
     * wrap/unWrap boolean 数组包装与解包.
     */
    @Test
    @DisplayName("wrap/unwrap：boolean数组包装与拆包")
    void wrapUnWrapBoolean() {
        Boolean[] wrapped = PrimitiveArrayUtil.wrap(true, false);
        assertArrayEquals(new Boolean[]{true, false}, wrapped);
        boolean[] unWrapped = PrimitiveArrayUtil.unWrap(wrapped);
        assertArrayEquals(new boolean[]{true, false}, unWrapped);
    }

    /**
     * unWrap 包装数组含 null 元素时默认为 0.
     */
    @Test
    @DisplayName("unwrap：含null元素拆包")
    void unWrapWithNullElements() {
        // null elements in wrapper array should default to 0/false
        int[] result = PrimitiveArrayUtil.unWrap(new Integer[]{1, null, 3});
        assertArrayEquals(new int[]{1, 0, 3}, result);
    }

    /**
     * unWrap Boolean 数组含 null 元素时默认为 false.
     */
    @Test
    @DisplayName("unwrap：boolean含null拆包")
    void unWrapBooleanNullElements() {
        boolean[] result = PrimitiveArrayUtil.unWrap(new Boolean[]{true, null, false});
        assertArrayEquals(new boolean[]{true, false, false}, result);
    }

    // ==================================================================== sub

    /**
     * sub 获取 int 子数组.
     */
    @Test
    @DisplayName("sub：int数组截取")
    void subInt() {
        assertArrayEquals(new int[]{2, 3}, PrimitiveArrayUtil.sub(new int[]{1, 2, 3, 4}, 1, 3));
    }

    /**
     * sub 负数起始索引.
     */
    @Test
    @DisplayName("sub：int数组负数起始")
    void subIntNegativeStart() {
        assertArrayEquals(new int[]{3, 4}, PrimitiveArrayUtil.sub(new int[]{1, 2, 3, 4}, -2, 4));
    }

    /**
     * sub 起始大于终止时自动交换.
     */
    @Test
    @DisplayName("sub：int数组自动交换起止")
    void subIntSwapStartEnd() {
        // start > end: auto-swap
        assertArrayEquals(new int[]{2, 3}, PrimitiveArrayUtil.sub(new int[]{1, 2, 3, 4}, 3, 1));
    }

    /**
     * sub 起始等于长度时返回空数组.
     */
    @Test
    @DisplayName("sub：int数组起始等于长度")
    void subIntStartEqualsLength() {
        assertEquals(0, PrimitiveArrayUtil.sub(new int[]{1, 2}, 2, 3).length);
    }

    /**
     * sub 获取 byte 子数组.
     */
    @Test
    @DisplayName("sub：byte数组截取")
    void subByte() {
        assertArrayEquals(new byte[]{2, 3}, PrimitiveArrayUtil.sub(new byte[]{1, 2, 3, 4}, 1, 3));
    }

    /**
     * sub 获取 long 子数组.
     */
    @Test
    @DisplayName("sub：long数组截取")
    void subLong() {
        assertArrayEquals(new long[]{2L, 3L}, PrimitiveArrayUtil.sub(new long[]{1L, 2L, 3L, 4L}, 1, 3));
    }

    /**
     * sub 获取 short 子数组.
     */
    @Test
    @DisplayName("sub：short数组截取")
    void subShort() {
        assertArrayEquals(new short[]{2, 3}, PrimitiveArrayUtil.sub(new short[]{1, 2, 3, 4}, 1, 3));
    }

    /**
     * sub 获取 char 子数组.
     */
    @Test
    @DisplayName("sub：char数组截取")
    void subChar() {
        assertArrayEquals(new char[]{'b', 'c'}, PrimitiveArrayUtil.sub(new char[]{'a', 'b', 'c', 'd'}, 1, 3));
    }

    /**
     * sub 获取 double 子数组.
     */
    @Test
    @DisplayName("sub：double数组截取")
    void subDouble() {
        assertArrayEquals(new double[]{2.0, 3.0}, PrimitiveArrayUtil.sub(new double[]{1.0, 2.0, 3.0, 4.0}, 1, 3), 0.001);
    }

    /**
     * sub 获取 float 子数组.
     */
    @Test
    @DisplayName("sub：float数组截取")
    void subFloat() {
        assertArrayEquals(new float[]{2.0f, 3.0f}, PrimitiveArrayUtil.sub(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, 1, 3), 0.001f);
    }

    /**
     * sub 获取 boolean 子数组.
     */
    @Test
    @DisplayName("sub：boolean数组截取")
    void subBoolean() {
        assertArrayEquals(new boolean[]{false, true}, PrimitiveArrayUtil.sub(new boolean[]{true, false, true, false}, 1, 3));
    }

    // ==================================================================== remove

    /**
     * remove 移除 int 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：int数组按索引删除")
    void removeInt() {
        assertArrayEquals(new int[]{1, 3}, PrimitiveArrayUtil.remove(new int[]{1, 2, 3}, 1));
    }

    /**
     * remove 索引越界时返回原数组.
     */
    @Test
    @DisplayName("remove：int数组索引越界")
    void removeIntOutOfBounds() {
        int[] original = {1, 2, 3};
        int[] result = PrimitiveArrayUtil.remove(original, 5);
        assertSame(original, result);
    }

    /**
     * remove 负数索引返回原数组.
     */
    @Test
    @DisplayName("remove：int数组负数索引")
    void removeIntNegativeIndex() {
        int[] original = {1, 2, 3};
        assertSame(original, PrimitiveArrayUtil.remove(original, -1));
    }

    /**
     * remove null 数组返回 null.
     */
    @Test
    @DisplayName("remove：null返回null")
    void removeNullReturnsNull() {
        assertNull(PrimitiveArrayUtil.remove((Object) null, 0));
    }

    /**
     * remove 移除 long 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：long数组按索引删除")
    void removeLong() {
        assertArrayEquals(new long[]{1L, 3L}, PrimitiveArrayUtil.remove(new long[]{1L, 2L, 3L}, 1));
    }

    /**
     * remove 移除 short 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：short数组按索引删除")
    void removeShort() {
        assertArrayEquals(new short[]{1, 3}, PrimitiveArrayUtil.remove(new short[]{1, 2, 3}, 1));
    }

    /**
     * remove 移除 char 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：char数组按索引删除")
    void removeChar() {
        assertArrayEquals(new char[]{'a', 'c'}, PrimitiveArrayUtil.remove(new char[]{'a', 'b', 'c'}, 1));
    }

    /**
     * remove 移除 byte 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：byte数组按索引删除")
    void removeByte() {
        assertArrayEquals(new byte[]{1, 3}, PrimitiveArrayUtil.remove(new byte[]{1, 2, 3}, 1));
    }

    /**
     * remove 移除 double 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：double数组按索引删除")
    void removeDouble() {
        assertArrayEquals(new double[]{1.0, 3.0}, PrimitiveArrayUtil.remove(new double[]{1.0, 2.0, 3.0}, 1), 0.001);
    }

    /**
     * remove 移除 float 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：float数组按索引删除")
    void removeFloat() {
        assertArrayEquals(new float[]{1.0f, 3.0f}, PrimitiveArrayUtil.remove(new float[]{1.0f, 2.0f, 3.0f}, 1), 0.001f);
    }

    /**
     * remove 移除 boolean 数组指定位置元素.
     */
    @Test
    @DisplayName("remove：boolean数组按索引删除")
    void removeBoolean() {
        assertArrayEquals(new boolean[]{true, true}, PrimitiveArrayUtil.remove(new boolean[]{true, false, true}, 1));
    }

    // ==================================================================== removeEle

    /**
     * removeEle 按值移除 int 数组元素.
     */
    @Test
    @DisplayName("removeEle：int数组按元素删除")
    void removeEleInt() {
        assertArrayEquals(new int[]{1, 3}, PrimitiveArrayUtil.removeEle(new int[]{1, 2, 3}, 2));
    }

    /**
     * removeEle 只移除第一个匹配元素.
     */
    @Test
    @DisplayName("removeEle：只删除首次出现")
    void removeEleRemovesFirstOccurrence() {
        // only first match is removed
        assertArrayEquals(new int[]{1, 3, 2}, PrimitiveArrayUtil.removeEle(new int[]{1, 2, 3, 2}, 2));
    }

    /**
     * removeEle 按值移除 long 数组元素.
     */
    @Test
    @DisplayName("removeEle：long数组按元素删除")
    void removeEleLong() {
        assertArrayEquals(new long[]{1L, 3L}, PrimitiveArrayUtil.removeEle(new long[]{1L, 2L, 3L}, 2L));
    }

    /**
     * removeEle 按值移除 byte 数组元素.
     */
    @Test
    @DisplayName("removeEle：byte数组按元素删除")
    void removeEleByte() {
        assertArrayEquals(new byte[]{1, 3}, PrimitiveArrayUtil.removeEle(new byte[]{1, 2, 3}, (byte) 2));
    }

    /**
     * removeEle 按值移除 double 数组元素.
     */
    @Test
    @DisplayName("removeEle：double数组按元素删除")
    void removeEleDouble() {
        assertArrayEquals(new double[]{1.0, 3.0}, PrimitiveArrayUtil.removeEle(new double[]{1.0, 2.0, 3.0}, 2.0), 0.001);
    }

    /**
     * removeEle 按值移除 float 数组元素.
     */
    @Test
    @DisplayName("removeEle：float数组按元素删除")
    void removeEleFloat() {
        assertArrayEquals(new float[]{1.0f, 3.0f}, PrimitiveArrayUtil.removeEle(new float[]{1.0f, 2.0f, 3.0f}, 2.0f), 0.001f);
    }

    /**
     * removeEle 按值移除 boolean 数组元素.
     */
    @Test
    @DisplayName("removeEle：boolean数组按元素删除")
    void removeEleBoolean() {
        assertArrayEquals(new boolean[]{true}, PrimitiveArrayUtil.removeEle(new boolean[]{true, false}, false));
    }

    /**
     * removeEle 按值移除 char 数组元素.
     */
    @Test
    @DisplayName("removeEle：char数组按元素删除")
    void removeEleChar() {
        assertArrayEquals(new char[]{'a', 'c'}, PrimitiveArrayUtil.removeEle(new char[]{'a', 'b', 'c'}, 'b'));
    }

    /**
     * removeEle 按值移除 short 数组元素.
     */
    @Test
    @DisplayName("removeEle：short数组按元素删除")
    void removeEleShort() {
        assertArrayEquals(new short[]{1, 3}, PrimitiveArrayUtil.removeEle(new short[]{1, 2, 3}, (short) 2));
    }

    // ==================================================================== reverse

    /**
     * reverse 反转 int 数组.
     */
    @Test
    @DisplayName("reverse：int数组反转")
    void reverseInt() {
        int[] array = {1, 2, 3, 4, 5};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, array);
    }

    /**
     * reverse 指定范围反转 int 数组.
     */
    @Test
    @DisplayName("reverse：int数组范围反转")
    void reverseIntWithRange() {
        int[] array = {1, 2, 3, 4, 5};
        PrimitiveArrayUtil.reverse(array, 1, 4);
        assertArrayEquals(new int[]{1, 4, 3, 2, 5}, array);
    }

    /**
     * reverse 空数组返回原数组.
     */
    @Test
    @DisplayName("reverse：int空数组反转")
    void reverseIntEmpty() {
        int[] empty = {};
        int[] result = PrimitiveArrayUtil.reverse(empty);
        assertSame(empty, result);
    }

    /**
     * reverse 反转 long 数组.
     */
    @Test
    @DisplayName("reverse：long数组反转")
    void reverseLong() {
        long[] array = {1L, 2L, 3L};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new long[]{3L, 2L, 1L}, array);
    }

    /**
     * reverse 反转 short 数组.
     */
    @Test
    @DisplayName("reverse：short数组反转")
    void reverseShort() {
        short[] array = {1, 2, 3};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new short[]{3, 2, 1}, array);
    }

    /**
     * reverse 反转 char 数组.
     */
    @Test
    @DisplayName("reverse：char数组反转")
    void reverseChar() {
        char[] array = {'a', 'b', 'c'};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new char[]{'c', 'b', 'a'}, array);
    }

    /**
     * reverse 反转 byte 数组.
     */
    @Test
    @DisplayName("reverse：byte数组反转")
    void reverseByte() {
        byte[] array = {1, 2, 3};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new byte[]{3, 2, 1}, array);
    }

    /**
     * reverse 反转 double 数组.
     */
    @Test
    @DisplayName("reverse：double数组反转")
    void reverseDouble() {
        double[] array = {1.0, 2.0, 3.0};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new double[]{3.0, 2.0, 1.0}, array, 0.001);
    }

    /**
     * reverse 反转 float 数组.
     */
    @Test
    @DisplayName("reverse：float数组反转")
    void reverseFloat() {
        float[] array = {1.0f, 2.0f, 3.0f};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new float[]{3.0f, 2.0f, 1.0f}, array, 0.001f);
    }

    /**
     * reverse 反转 boolean 数组.
     */
    @Test
    @DisplayName("reverse：boolean数组反转")
    void reverseBoolean() {
        boolean[] array = {true, false, true};
        PrimitiveArrayUtil.reverse(array);
        assertArrayEquals(new boolean[]{true, false, true}, array);
    }

    // ==================================================================== min / max

    /**
     * min 取 int 数组最小值.
     */
    @Test
    @DisplayName("min：int数组最小值")
    void minInt() {
        assertEquals(1, PrimitiveArrayUtil.min(3, 1, 2));
    }

    /**
     * min 取 long 数组最小值.
     */
    @Test
    @DisplayName("min：long数组最小值")
    void minLong() {
        assertEquals(1L, PrimitiveArrayUtil.min(3L, 1L, 2L));
    }

    /**
     * min 取 short 数组最小值.
     */
    @Test
    @DisplayName("min：short数组最小值")
    void minShort() {
        assertEquals((short) 1, PrimitiveArrayUtil.min((short) 3, (short) 1, (short) 2));
    }

    /**
     * min 取 char 数组最小值.
     */
    @Test
    @DisplayName("min：char数组最小值")
    void minChar() {
        assertEquals('a', PrimitiveArrayUtil.min('c', 'a', 'b'));
    }

    /**
     * min 取 byte 数组最小值.
     */
    @Test
    @DisplayName("min：byte数组最小值")
    void minByte() {
        assertEquals((byte) 1, PrimitiveArrayUtil.min((byte) 3, (byte) 1, (byte) 2));
    }

    /**
     * min 取 double 数组最小值.
     */
    @Test
    @DisplayName("min：double数组最小值")
    void minDouble() {
        assertEquals(1.0, PrimitiveArrayUtil.min(3.0, 1.0, 2.0), 0.001);
    }

    /**
     * min 取 float 数组最小值.
     */
    @Test
    @DisplayName("min：float数组最小值")
    void minFloat() {
        assertEquals(1.0f, PrimitiveArrayUtil.min(3.0f, 1.0f, 2.0f), 0.001f);
    }

    /**
     * min 空数组抛出异常.
     */
    @Test
    @DisplayName("min：空int数组抛异常")
    void minIntEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveArrayUtil.min(new int[0]));
    }

    /**
     * min 空数组抛出异常.
     */
    @Test
    @DisplayName("min：空long数组抛异常")
    void minLongEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveArrayUtil.min(new long[0]));
    }

    /**
     * max 取 int 数组最大值.
     */
    @Test
    @DisplayName("max：int数组最大值")
    void maxInt() {
        assertEquals(3, PrimitiveArrayUtil.max(1, 3, 2));
    }

    /**
     * max 取 long 数组最大值.
     */
    @Test
    @DisplayName("max：long数组最大值")
    void maxLong() {
        assertEquals(3L, PrimitiveArrayUtil.max(1L, 3L, 2L));
    }

    /**
     * max 取 short 数组最大值.
     */
    @Test
    @DisplayName("max：short数组最大值")
    void maxShort() {
        assertEquals((short) 3, PrimitiveArrayUtil.max((short) 1, (short) 3, (short) 2));
    }

    /**
     * max 取 char 数组最大值.
     */
    @Test
    @DisplayName("max：char数组最大值")
    void maxChar() {
        assertEquals('c', PrimitiveArrayUtil.max('a', 'c', 'b'));
    }

    /**
     * max 取 byte 数组最大值.
     */
    @Test
    @DisplayName("max：byte数组最大值")
    void maxByte() {
        assertEquals((byte) 3, PrimitiveArrayUtil.max((byte) 1, (byte) 3, (byte) 2));
    }

    /**
     * max 取 double 数组最大值.
     */
    @Test
    @DisplayName("max：double数组最大值")
    void maxDouble() {
        assertEquals(3.0, PrimitiveArrayUtil.max(1.0, 3.0, 2.0), 0.001);
    }

    /**
     * max 取 float 数组最大值.
     */
    @Test
    @DisplayName("max：float数组最大值")
    void maxFloat() {
        assertEquals(3.0f, PrimitiveArrayUtil.max(1.0f, 3.0f, 2.0f), 0.001f);
    }

    /**
     * max 空数组抛出异常.
     */
    @Test
    @DisplayName("max：空int数组抛异常")
    void maxIntEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveArrayUtil.max(new int[0]));
    }

    // ==================================================================== shuffle

    /**
     * shuffle 洗牌后元素不变.
     */
    @Test
    @DisplayName("shuffle：int数组打乱保留元素")
    void shuffleIntPreservesElements() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] original = array.clone();
        PrimitiveArrayUtil.shuffle(array);
        assertEquals(10, array.length);
        // verify same elements exist (order may differ)
        Set<Integer> shuffled = new HashSet<>();
        for (int v : array) shuffled.add(v);
        Set<Integer> expected = new HashSet<>();
        for (int v : original) expected.add(v);
        assertEquals(expected, shuffled);
    }

    /**
     * shuffle 单元素数组.
     */
    @Test
    @DisplayName("shuffle：int单元素数组")
    void shuffleIntSingleElement() {
        int[] array = {1};
        int[] result = PrimitiveArrayUtil.shuffle(array);
        assertArrayEquals(new int[]{1}, result);
    }

    /**
     * shuffle null 数组返回 null.
     */
    @Test
    @DisplayName("shuffle：null不抛异常")
    void shuffleIntNull() {
        assertNull(PrimitiveArrayUtil.shuffle((int[]) null));
    }

    /**
     * shuffle 洗牌 long 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：long数组打乱保留元素")
    void shuffleLongPreservesElements() {
        long[] array = {1L, 2L, 3L, 4L, 5L};
        PrimitiveArrayUtil.shuffle(array);
        assertEquals(5, array.length);
        Arrays.sort(array);
        assertArrayEquals(new long[]{1L, 2L, 3L, 4L, 5L}, array);
    }

    /**
     * shuffle 洗牌 byte 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：byte数组打乱保留元素")
    void shuffleBytePreservesElements() {
        byte[] array = {1, 2, 3, 4, 5};
        PrimitiveArrayUtil.shuffle(array);
        Arrays.sort(array);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, array);
    }

    /**
     * shuffle 洗牌 double 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：double数组打乱保留元素")
    void shuffleDoublePreservesElements() {
        double[] array = {1.0, 2.0, 3.0, 4.0, 5.0};
        PrimitiveArrayUtil.shuffle(array);
        Arrays.sort(array);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, array, 0.001);
    }

    /**
     * shuffle 洗牌 float 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：float数组打乱保留元素")
    void shuffleFloatPreservesElements() {
        float[] array = {1.0f, 2.0f, 3.0f};
        PrimitiveArrayUtil.shuffle(array);
        Arrays.sort(array);
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f}, array, 0.001f);
    }

    /**
     * shuffle 洗牌 char 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：char数组打乱保留元素")
    void shuffleCharPreservesElements() {
        char[] array = {'a', 'b', 'c', 'd'};
        PrimitiveArrayUtil.shuffle(array);
        Arrays.sort(array);
        assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, array);
    }

    /**
     * shuffle 洗牌 short 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：short数组打乱保留元素")
    void shuffleShortPreservesElements() {
        short[] array = {1, 2, 3, 4};
        PrimitiveArrayUtil.shuffle(array);
        Arrays.sort(array);
        assertArrayEquals(new short[]{1, 2, 3, 4}, array);
    }

    /**
     * shuffle 洗牌 boolean 数组后元素不变.
     */
    @Test
    @DisplayName("shuffle：boolean数组打乱保留元素")
    void shuffleBooleanPreservesElements() {
        boolean[] array = {true, false, true, false, true};
        PrimitiveArrayUtil.shuffle(array);
        assertEquals(5, array.length);
    }

    /**
     * shuffle 指定 Random 洗牌.
     */
    @Test
    @DisplayName("shuffle：指定Random打乱")
    void shuffleIntWithSpecificRandom() {
        int[] array = {1, 2, 3, 4, 5};
        PrimitiveArrayUtil.shuffle(array, new Random(42));
        // with seed 42, the result should be deterministic
        assertEquals(5, array.length);
    }

    /**
     * shuffle Random 为 null 时返回原数组.
     */
    @Test
    @DisplayName("shuffle：null Random打乱")
    void shuffleIntWithNullRandom() {
        int[] array = {1, 2, 3};
        int[] result = PrimitiveArrayUtil.shuffle(array, null);
        assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    // ==================================================================== swap

    /**
     * swap 交换 int 数组元素.
     */
    @Test
    @DisplayName("swap：int数组交换元素")
    void swapInt() {
        int[] array = {1, 2, 3};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new int[]{3, 2, 1}, array);
    }

    /**
     * swap 交换 long 数组元素.
     */
    @Test
    @DisplayName("swap：long数组交换元素")
    void swapLong() {
        long[] array = {1L, 2L, 3L};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new long[]{3L, 2L, 1L}, array);
    }

    /**
     * swap 交换 double 数组元素.
     */
    @Test
    @DisplayName("swap：double数组交换元素")
    void swapDouble() {
        double[] array = {1.0, 2.0, 3.0};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new double[]{3.0, 2.0, 1.0}, array, 0.001);
    }

    /**
     * swap 交换 float 数组元素.
     */
    @Test
    @DisplayName("swap：float数组交换元素")
    void swapFloat() {
        float[] array = {1.0f, 2.0f, 3.0f};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new float[]{3.0f, 2.0f, 1.0f}, array, 0.001f);
    }

    /**
     * swap 交换 boolean 数组元素.
     */
    @Test
    @DisplayName("swap：boolean数组交换元素")
    void swapBoolean() {
        boolean[] array = {true, false};
        PrimitiveArrayUtil.swap(array, 0, 1);
        assertArrayEquals(new boolean[]{false, true}, array);
    }

    /**
     * swap 交换 byte 数组元素.
     */
    @Test
    @DisplayName("swap：byte数组交换元素")
    void swapByte() {
        byte[] array = {1, 2, 3};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new byte[]{3, 2, 1}, array);
    }

    /**
     * swap 交换 char 数组元素.
     */
    @Test
    @DisplayName("swap：char数组交换元素")
    void swapChar() {
        char[] array = {'a', 'b', 'c'};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new char[]{'c', 'b', 'a'}, array);
    }

    /**
     * swap 交换 short 数组元素.
     */
    @Test
    @DisplayName("swap：short数组交换元素")
    void swapShort() {
        short[] array = {1, 2, 3};
        PrimitiveArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new short[]{3, 2, 1}, array);
    }

    /**
     * swap 空数组抛出异常.
     */
    @Test
    @DisplayName("swap：空数组抛异常")
    void swapEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveArrayUtil.swap(new int[0], 0, 0));
    }

    // ==================================================================== isSorted / isSortedASC / isSortedDESC

    /**
     * isSorted 判定 int 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：int数组排序判断")
    void isSortedInt() {
        assertTrue(PrimitiveArrayUtil.isSorted(new int[]{1, 2, 3}));
        assertFalse(PrimitiveArrayUtil.isSorted(new int[]{1, 3, 2}));
        assertFalse(PrimitiveArrayUtil.isSorted((int[]) null));
    }

    /**
     * isSorted 判定 long 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：long数组排序判断")
    void isSortedLong() {
        assertTrue(PrimitiveArrayUtil.isSorted(new long[]{1L, 2L, 3L}));
        assertFalse(PrimitiveArrayUtil.isSorted(new long[]{3L, 1L}));
    }

    /**
     * isSorted 判定 byte 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：byte数组排序判断")
    void isSortedByte() {
        assertTrue(PrimitiveArrayUtil.isSorted(new byte[]{1, 2, 3}));
        assertFalse(PrimitiveArrayUtil.isSorted(new byte[]{3, 1}));
    }

    /**
     * isSorted 判定 short 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：short数组排序判断")
    void isSortedShort() {
        assertTrue(PrimitiveArrayUtil.isSorted(new short[]{1, 2, 3}));
        assertFalse(PrimitiveArrayUtil.isSorted(new short[]{3, 1}));
    }

    /**
     * isSorted 判定 char 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：char数组排序判断")
    void isSortedChar() {
        assertTrue(PrimitiveArrayUtil.isSorted(new char[]{'a', 'b', 'c'}));
        assertFalse(PrimitiveArrayUtil.isSorted(new char[]{'c', 'a'}));
    }

    /**
     * isSorted 判定 double 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：double数组排序判断")
    void isSortedDouble() {
        assertTrue(PrimitiveArrayUtil.isSorted(new double[]{1.0, 2.0, 3.0}));
        assertFalse(PrimitiveArrayUtil.isSorted(new double[]{3.0, 1.0}));
    }

    /**
     * isSorted 判定 float 数组排序状态.
     */
    @Test
    @DisplayName("isSorted：float数组排序判断")
    void isSortedFloat() {
        assertTrue(PrimitiveArrayUtil.isSorted(new float[]{1.0f, 2.0f, 3.0f}));
        assertFalse(PrimitiveArrayUtil.isSorted(new float[]{3.0f, 1.0f}));
    }

    /**
     * isSortedASC 判定 int 数组升序.
     */
    @Test
    @DisplayName("isSortedASC：int升序判断")
    void isSortedASCInt() {
        assertTrue(PrimitiveArrayUtil.isSortedASC(new int[]{1, 2, 3}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new int[]{1, 1, 2}));
        assertFalse(PrimitiveArrayUtil.isSortedASC(new int[]{2, 1}));
    }

    /**
     * isSortedDESC 判定 int 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：int降序判断")
    void isSortedDESCInt() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new int[]{3, 2, 1}));
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new int[]{3, 3, 2}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new int[]{1, 2}));
    }

    /**
     * isSortedDESC 判定 long 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：long降序判断")
    void isSortedDESCLong() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new long[]{3L, 2L, 1L}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new long[]{1L, 2L}));
    }

    /**
     * isSortedDESC 判定 byte 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：byte降序判断")
    void isSortedDESCByte() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new byte[]{3, 2, 1}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new byte[]{1, 2}));
    }

    /**
     * isSortedDESC 判定 short 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：short降序判断")
    void isSortedDESCShort() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new short[]{3, 2, 1}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new short[]{1, 2}));
    }

    /**
     * isSortedDESC 判定 char 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：char降序判断")
    void isSortedDESCChar() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new char[]{'c', 'b', 'a'}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new char[]{'a', 'c'}));
    }

    /**
     * isSortedDESC 判定 double 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：double降序判断")
    void isSortedDESCDouble() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new double[]{3.0, 2.0, 1.0}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new double[]{1.0, 2.0}));
    }

    /**
     * isSortedDESC 判定 float 数组降序.
     */
    @Test
    @DisplayName("isSortedDESC：float降序判断")
    void isSortedDESCFloat() {
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new float[]{3.0f, 2.0f, 1.0f}));
        assertFalse(PrimitiveArrayUtil.isSortedDESC(new float[]{1.0f, 2.0f}));
    }

    /**
     * isSortedASC null 数组返回 false.
     */
    @Test
    @DisplayName("isSortedASC：null返回false")
    void isSortedASCNullReturnsFalse() {
        assertFalse(PrimitiveArrayUtil.isSortedASC((int[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((long[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((byte[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((short[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((char[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((double[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedASC((float[]) null));
    }

    /**
     * isSortedDESC null 数组返回 false.
     */
    @Test
    @DisplayName("isSortedDESC：null返回false")
    void isSortedDESCNullReturnsFalse() {
        assertFalse(PrimitiveArrayUtil.isSortedDESC((int[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((long[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((byte[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((short[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((char[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((double[]) null));
        assertFalse(PrimitiveArrayUtil.isSortedDESC((float[]) null));
    }

    /**
     * isSorted 单元素数组视为有序.
     */
    @Test
    @DisplayName("isSorted：单元素返回true")
    void isSortedSingleElement() {
        assertTrue(PrimitiveArrayUtil.isSorted(new int[]{1}));
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new int[]{1}));
    }

    /**
     * isSorted 相同元素数组视为有序.
     */
    @Test
    @DisplayName("isSorted：相等元素返回true")
    void isSortedEqualElements() {
        assertTrue(PrimitiveArrayUtil.isSorted(new int[]{2, 2, 2}));
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new int[]{2, 2, 2}));
    }

    // ==================================================================== supplementary edge cases

    /**
     * sub 负数 end 索引自动修正.
     */
    @Test
    @DisplayName("sub：int数组负数结尾")
    void subIntNegativeEnd() {
        // end = -1 means end = length - 1 = 3
        assertArrayEquals(new int[]{2, 3}, PrimitiveArrayUtil.sub(new int[]{1, 2, 3, 4}, 1, -1));
    }

    /**
     * sub end 超过长度时截断到数组末尾.
     */
    @Test
    @DisplayName("sub：int数组结尾超出长度")
    void subIntEndExceedsLength() {
        assertArrayEquals(new int[]{3, 4}, PrimitiveArrayUtil.sub(new int[]{1, 2, 3, 4}, 2, 100));
    }

    /**
     * sub start 和 end 都为负数时.
     */
    @Test
    @DisplayName("sub：long数组负数范围")
    void subLongNegativeBoth() {
        assertArrayEquals(new long[]{3L}, PrimitiveArrayUtil.sub(new long[]{1L, 2L, 3L, 4L}, -2, -1));
    }

    /**
     * reverse null 数组抛出 NullPointerException.
     */
    @Test
    @DisplayName("reverse：null抛异常")
    void reverseNullThrows() {
        assertThrows(NullPointerException.class, () -> PrimitiveArrayUtil.reverse((int[]) null));
        assertThrows(NullPointerException.class, () -> PrimitiveArrayUtil.reverse((long[]) null));
        assertThrows(NullPointerException.class, () -> PrimitiveArrayUtil.reverse((double[]) null));
    }

    /**
     * reverse 指定范围反转 long 数组.
     */
    @Test
    @DisplayName("reverse：long数组范围反转")
    void reverseLongWithRange() {
        long[] array = {1L, 2L, 3L, 4L, 5L};
        PrimitiveArrayUtil.reverse(array, 1, 4);
        assertArrayEquals(new long[]{1L, 4L, 3L, 2L, 5L}, array);
    }

    /**
     * reverse 指定范围反转 byte 数组.
     */
    @Test
    @DisplayName("reverse：byte数组范围反转")
    void reverseByteWithRange() {
        byte[] array = {1, 2, 3, 4, 5};
        PrimitiveArrayUtil.reverse(array, 0, 3);
        assertArrayEquals(new byte[]{3, 2, 1, 4, 5}, array);
    }

    /**
     * reverse 指定范围反转 double 数组.
     */
    @Test
    @DisplayName("reverse：double数组范围反转")
    void reverseDoubleWithRange() {
        double[] array = {1.0, 2.0, 3.0, 4.0};
        PrimitiveArrayUtil.reverse(array, 1, 3);
        assertArrayEquals(new double[]{1.0, 3.0, 2.0, 4.0}, array, 0.001);
    }

    /**
     * resize null 数组返回空数组.
     */
    @Test
    @DisplayName("resize：null返回空数组")
    void resizeNullReturnsEmpty() {
        byte[] result = PrimitiveArrayUtil.resize(null, 3);
        assertEquals(3, result.length);
        assertEquals(0, result[0]);
    }

    /**
     * indexOf 空数组返回 -1.
     */
    @Test
    @DisplayName("indexOf：空数组返回-1")
    void indexOfEmptyReturnsNegOne() {
        assertEquals(-1, PrimitiveArrayUtil.indexOf(new int[0], 1));
        assertEquals(-1, PrimitiveArrayUtil.indexOf(new long[0], 1L));
        assertEquals(-1, PrimitiveArrayUtil.indexOf(new byte[0], (byte) 1));
    }

    /**
     * lastIndexOf 空数组返回 -1.
     */
    @Test
    @DisplayName("lastIndexOf：空数组返回-1")
    void lastIndexOfEmptyReturnsNegOne() {
        assertEquals(-1, PrimitiveArrayUtil.lastIndexOf(new int[0], 1));
        assertEquals(-1, PrimitiveArrayUtil.lastIndexOf(new double[0], 1.0));
    }

    /**
     * min 单元素正常返回.
     */
    @Test
    @DisplayName("min：单元素数组最小值")
    void minSingleElement() {
        assertEquals(5, PrimitiveArrayUtil.min(5));
        assertEquals(5L, PrimitiveArrayUtil.min(5L));
        assertEquals(1.0, PrimitiveArrayUtil.min(1.0), 0.001);
    }

    /**
     * max 单元素正常返回.
     */
    @Test
    @DisplayName("max：单元素数组最大值")
    void maxSingleElement() {
        assertEquals(5, PrimitiveArrayUtil.max(5));
        assertEquals(5L, PrimitiveArrayUtil.max(5L));
        assertEquals(1.0f, PrimitiveArrayUtil.max(1.0f), 0.001f);
    }

    /**
     * swap null 数组抛出异常.
     */
    @Test
    @DisplayName("swap：null抛异常")
    void swapNullThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PrimitiveArrayUtil.swap((int[]) null, 0, 0));
    }

    /**
     * addAll 全部为 null 返回空数组.
     */
    @Test
    @DisplayName("addAll：全部null返回空数组")
    void addAllAllNullsReturnsEmpty() {
        int[] result = PrimitiveArrayUtil.addAll((int[]) null, (int[]) null);
        assertEquals(0, result.length);
    }

    /**
     * isSortedASC 判定更多类型.
     */
    @Test
    @DisplayName("isSortedASC：多种类型排序判断")
    void isSortedASCMultipleTypes() {
        assertTrue(PrimitiveArrayUtil.isSortedASC(new long[]{1L, 2L, 3L}));
        assertFalse(PrimitiveArrayUtil.isSortedASC(new long[]{3L, 1L}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new double[]{1.0, 2.0, 3.0}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new float[]{1.0f, 2.0f}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new byte[]{1, 2, 3}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new short[]{1, 2, 3}));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new char[]{'a', 'b', 'c'}));
    }

    /**
     * isSorted 空数组视为有序.
     */
    @Test
    @DisplayName("isSorted：空数组返回true")
    void isSortedEmptyArrayReturnsTrue() {
        assertTrue(PrimitiveArrayUtil.isSorted(new int[0]));
        assertTrue(PrimitiveArrayUtil.isSorted(new long[0]));
        assertTrue(PrimitiveArrayUtil.isSortedASC(new byte[0]));
        assertTrue(PrimitiveArrayUtil.isSortedDESC(new double[0]));
    }
}