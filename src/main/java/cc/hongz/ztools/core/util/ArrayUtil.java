package cc.hongz.ztools.core.util;

import java.lang.reflect.Array;

/**
 * 数组工具类（codec 最小子集）.
 *
 * @author Looly
 */
public final class ArrayUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private ArrayUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 数组是否为空.
     *
     * @param array 数组
     * @param <T>   元素类型
     * @return 是否为空
     */
    public static <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空（含原始类型数组）.
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final Object array) {
        if (array != null) {
            if (array.getClass().isArray()) {
                return Array.getLength(array) == 0;
            }
            return false;
        }
        return true;
    }

    /**
     * 数组复制.
     *
     * @param src    源数组
     * @param dest   目标数组
     * @param length 拷贝长度
     * @return 目标数组
     */
    public static Object copy(final Object src, final Object dest, final int length) {
        if (src == null || dest == null) {
            throw new NullPointerException("Source array and destination array must not be null");
        }
        System.arraycopy(src, 0, dest, 0, length);
        return dest;
    }

    /**
     * char 数组是否包含指定字符.
     *
     * @param array 数组
     * @param value 字符
     * @return 是否包含
     */
    public static boolean contains(final char[] array, final char value) {
        if (array == null) {
            return false;
        }
        for (char c : array) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * byte 数组是否包含指定值.
     *
     * @param array 数组
     * @param value 字节
     * @return 是否包含
     */
    public static boolean contains(final byte[] array, final byte value) {
        if (array == null) {
            return false;
        }
        for (byte b : array) {
            if (b == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 反转 byte 数组（会变更原数组）.
     *
     * @param array 数组
     * @return 变更后的原数组
     */
    public static byte[] reverse(final byte[] array) {
        if (isEmpty(array)) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    // --------------------------------------- Static method end
}
