package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Editor;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.lang.Matcher;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数组工具类.
 *
 * @author Looly
 */
public class ArrayUtil extends PrimitiveArrayUtil {

    // ---------------------------------------------------------------------- isEmpty

    /**
     * 数组是否为空.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 如果给定数组为空，返回默认数组.
     *
     * @param <T>          数组元素类型
     * @param array        数组
     * @param defaultArray 默认数组
     * @return 非空（empty）的原数组或默认数组
     */
    public static <T> T[] defaultIfEmpty(T[] array, T[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    /**
     * 数组是否为空.
     * <p>
     * 此方法会匹配单一对象，如果此对象为 {@code null} 则返回true.<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false.<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true.
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(Object array) {
        if (array != null) {
            if (isArray(array)) {
                return 0 == Array.getLength(array);
            }
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 数组是否为非空.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return (null != array && array.length != 0);
    }

    /**
     * 数组是否为非空.
     * <p>
     * 此方法会匹配单一对象，如果此对象为 {@code null} 则返回false.<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true.<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回true，否则返回false.
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Object array) {
        return !isEmpty(array);
    }

    /**
     * 是否包含 {@code null} 元素.
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 是否包含 {@code null} 元素
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(T... array) {
        if (isNotEmpty(array)) {
            for (T element : array) {
                if (ObjectUtil.isNull(element)) {
                    return true;
                }
            }
        }
        return array == null;
    }

    /**
     * 多个字段是否全为 null.
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 多个字段是否全为 null
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isAllNull(T... array) {
        return null == firstNonNull(array);
    }

    /**
     * 返回数组中第一个非空元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 非空元素，如果不存在非空元素或数组为空，返回 {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T firstNonNull(T... array) {
        return firstMatch(ObjectUtil::isNotNull, array);
    }

    /**
     * 返回数组中第一个匹配规则的值.
     *
     * @param <T>     数组元素类型
     * @param matcher 匹配接口
     * @param array   数组
     * @return 匹配元素，如果不存在匹配元素或数组为空，返回 {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T firstMatch(Matcher<T> matcher, T... array) {
        final int index = matchIndex(matcher, array);
        if (index < 0) {
            return null;
        }
        return array[index];
    }

    /**
     * 返回数组中第一个匹配规则的值的位置.
     *
     * @param <T>     数组元素类型
     * @param matcher 匹配接口
     * @param array   数组
     * @return 匹配到元素的位置，-1表示未匹配到
     */
    @SuppressWarnings("unchecked")
    public static <T> int matchIndex(Matcher<T> matcher, T... array) {
        return matchIndex(matcher, 0, array);
    }

    /**
     * 返回数组中第一个匹配规则的值的位置.
     *
     * @param <T>               数组元素类型
     * @param matcher           匹配接口
     * @param beginIndexInclude 检索开始的位置
     * @param array             数组
     * @return 匹配到元素的位置，-1表示未匹配到
     */
    @SuppressWarnings("unchecked")
    public static <T> int matchIndex(Matcher<T> matcher, int beginIndexInclude, T... array) {
        Assert.notNull(matcher, "Matcher must be not null !");
        if (isNotEmpty(array)) {
            for (int i = beginIndexInclude; i < array.length; i++) {
                if (matcher.match(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    // ---------------------------------------------------------------------- newArray

    /**
     * 新建一个空数组.
     *
     * @param <T>           数组元素类型
     * @param componentType 元素类型
     * @param newSize       大小
     * @return 空数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    /**
     * 新建一个空数组.
     *
     * @param newSize 大小
     * @return 空数组
     */
    public static Object[] newArray(int newSize) {
        return new Object[newSize];
    }

    /**
     * 获取数组对象的元素类型.
     *
     * @param array 数组对象
     * @return 元素类型
     */
    public static Class<?> getComponentType(Object array) {
        return null == array ? null : array.getClass().getComponentType();
    }

    /**
     * 获取数组对象的元素类型.
     *
     * @param arrayClass 数组类
     * @return 元素类型
     */
    public static Class<?> getComponentType(Class<?> arrayClass) {
        return null == arrayClass ? null : arrayClass.getComponentType();
    }

    /**
     * 根据数组元素类型，获取数组的类型.
     *
     * @param componentType 数组元素类型
     * @return 数组类型
     */
    public static Class<?> getArrayType(Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }

    /**
     * 强转数组类型.
     *
     * @param type     数组类型或数组元素类型
     * @param arrayObj 原数组
     * @return 转换后的数组类型
     * @throws NullPointerException     提供参数为空
     * @throws IllegalArgumentException 参数 arrayObj 不是数组
     */
    public static Object[] cast(Class<?> type, Object arrayObj) throws NullPointerException, IllegalArgumentException {
        if (null == arrayObj) {
            throw new NullPointerException("Argument [arrayObj] is null !");
        }
        if (false == arrayObj.getClass().isArray()) {
            throw new IllegalArgumentException("Argument [arrayObj] is not array !");
        }
        if (null == type) {
            return (Object[]) arrayObj;
        }
        final Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        final Object[] array = (Object[]) arrayObj;
        final Object[] result = ArrayUtil.newArray(componentType, array.length);
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    // ---------------------------------------------------------------------- append / insert / setOrAppend / replace

    /**
     * 将新元素添加到已有数组中.
     *
     * @param <T>         数组元素类型
     * @param buffer       已有数组
     * @param newElements 新元素
     * @return 新数组
     */
    @SafeVarargs
    public static <T> T[] append(T[] buffer, T... newElements) {
        if (isEmpty(buffer)) {
            return newElements;
        }
        return insert(buffer, buffer.length, newElements);
    }

    /**
     * 将新元素添加到已有数组中.
     *
     * @param <T>         数组元素类型
     * @param array        已有数组
     * @param newElements 新元素
     * @return 新数组
     */
    @SafeVarargs
    public static <T> Object append(Object array, T... newElements) {
        if (isEmpty(array)) {
            return newElements;
        }
        return insert(array, length(array), newElements);
    }

    /**
     * 将元素值设置为数组的某个位置，当给定的 index 大于数组长度，则追加.
     *
     * @param <T>    数组元素类型
     * @param buffer 已有数组
     * @param index  位置，大于长度追加，否则替换
     * @param value  新值
     * @return 新数组或原有数组
     */
    public static <T> T[] setOrAppend(T[] buffer, int index, T value) {
        if (index < buffer.length) {
            Array.set(buffer, index, value);
            return buffer;
        } else {
            if (ArrayUtil.isEmpty(buffer)) {
                final T[] values = newArray(value.getClass(), 1);
                values[0] = value;
                return append(buffer, values);
            }
            return append(buffer, value);
        }
    }

    /**
     * 将元素值设置为数组的某个位置，当给定的 index 大于数组长度，则追加.
     *
     * @param array 已有数组
     * @param index 位置，大于长度追加，否则替换
     * @param value 新值
     * @return 新数组或原有数组
     */
    public static Object setOrAppend(Object array, int index, Object value) {
        if (index < length(array)) {
            Array.set(array, index, value);
            return array;
        } else {
            return append(array, value);
        }
    }

    /**
     * 将新元素插入到已有数组中的某个位置.
     *
     * @param <T>    数组元素类型
     * @param buffer 已有数组
     * @param index  位置，大于长度追加，否则替换，&lt;0表示从头部追加
     * @param values 新值
     * @return 新数组或原有数组
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] replace(T[] buffer, int index, T... values) {
        if (isEmpty(values)) {
            return buffer;
        }
        if (isEmpty(buffer)) {
            return values;
        }
        if (index < 0) {
            return insert(buffer, 0, values);
        }
        if (index >= buffer.length) {
            return append(buffer, values);
        }
        if (buffer.length >= values.length + index) {
            System.arraycopy(values, 0, buffer, index, values.length);
            return buffer;
        }
        int newArrayLength = index + values.length;
        final T[] result = newArray(buffer.getClass().getComponentType(), newArrayLength);
        System.arraycopy(buffer, 0, result, 0, index);
        System.arraycopy(values, 0, result, index, values.length);
        return result;
    }

    /**
     * 将新元素插入到已有数组中的某个位置.
     *
     * @param <T>         数组元素类型
     * @param buffer      已有数组
     * @param index       插入位置
     * @param newElements 新元素
     * @return 新数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T[] buffer, int index, T... newElements) {
        return (T[]) insert((Object) buffer, index, newElements);
    }

    /**
     * 将新元素插入到已有数组中的某个位置.
     *
     * @param <T>         数组元素类型
     * @param array       已有数组
     * @param index       插入位置
     * @param newElements 新元素
     * @return 新数组
     */
    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    public static <T> Object insert(Object array, int index, T... newElements) {
        if (isEmpty(newElements)) {
            return array;
        }
        if (isEmpty(array)) {
            return newElements;
        }
        final int len = length(array);
        if (index < 0) {
            index = (index % len) + len;
        }
        final Class<?> originComponentType = array.getClass().getComponentType();
        Object newEleArr = newElements;
        // 如果已有数组的元素类型是原始类型，则需要转换新元素数组为该类型
        if (originComponentType.isPrimitive()) {
            newEleArr = convertToPrimitiveArray(newElements, originComponentType);
        }
        final Object result = Array.newInstance(originComponentType, Math.max(len, index) + newElements.length);
        System.arraycopy(array, 0, result, 0, Math.min(len, index));
        System.arraycopy(newEleArr, 0, result, index, newElements.length);
        if (index < len) {
            System.arraycopy(array, index, result, index + newElements.length, len - index);
        }
        return result;
    }

    /**
     * 将包装类型数组转换为原始类型数组.
     *
     * @param newElements    包装类型数组
     * @param componentType  目标原始类型
     * @return 原始类型数组
     */
    private static Object convertToPrimitiveArray(Object[] newElements, Class<?> componentType) {
        if (componentType == int.class) {
            int[] arr = new int[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).intValue();
            }
            return arr;
        } else if (componentType == long.class) {
            long[] arr = new long[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).longValue();
            }
            return arr;
        } else if (componentType == short.class) {
            short[] arr = new short[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).shortValue();
            }
            return arr;
        } else if (componentType == byte.class) {
            byte[] arr = new byte[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).byteValue();
            }
            return arr;
        } else if (componentType == double.class) {
            double[] arr = new double[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).doubleValue();
            }
            return arr;
        } else if (componentType == float.class) {
            float[] arr = new float[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = ((Number) newElements[i]).floatValue();
            }
            return arr;
        } else if (componentType == char.class) {
            char[] arr = new char[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = (Character) newElements[i];
            }
            return arr;
        } else if (componentType == boolean.class) {
            boolean[] arr = new boolean[newElements.length];
            for (int i = 0; i < newElements.length; i++) {
                arr[i] = (Boolean) newElements[i];
            }
            return arr;
        }
        return newElements;
    }

    // ---------------------------------------------------------------------- resize

    /**
     * 生成一个新的重新设置大小的数组.
     *
     * @param <T>           数组元素类型
     * @param data          原数组
     * @param newSize       新的数组大小
     * @param componentType 数组元素类型
     * @return 调整后的新数组
     */
    public static <T> T[] resize(T[] data, int newSize, Class<?> componentType) {
        if (newSize < 0) {
            return data;
        }
        final T[] newArray = newArray(componentType, newSize);
        if (newSize > 0 && isNotEmpty(data)) {
            System.arraycopy(data, 0, newArray, 0, Math.min(data.length, newSize));
        }
        return newArray;
    }

    /**
     * 生成一个新的重新设置大小的数组.
     *
     * @param array   原数组
     * @param newSize 新的数组大小
     * @return 调整后的新数组
     */
    public static Object resize(Object array, int newSize) {
        if (newSize < 0) {
            return array;
        }
        if (null == array) {
            return null;
        }
        final int length = length(array);
        final Object newArray = Array.newInstance(array.getClass().getComponentType(), newSize);
        if (newSize > 0 && isNotEmpty(array)) {
            System.arraycopy(array, 0, newArray, 0, Math.min(length, newSize));
        }
        return newArray;
    }

    /**
     * 生成一个新的重新设置大小的数组.
     *
     * @param <T>     数组元素类型
     * @param buffer  原数组
     * @param newSize 新的数组大小
     * @return 调整后的新数组
     */
    public static <T> T[] resize(T[] buffer, int newSize) {
        return resize(buffer, newSize, buffer.getClass().getComponentType());
    }

    // ---------------------------------------------------------------------- addAll

    /**
     * 将多个数组合并在一起，忽略 null 的数组.
     *
     * @param <T>    数组元素类型
     * @param arrays 数组集合
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] addAll(T[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        int length = 0;
        for (final T[] array : arrays) {
            if (isNotEmpty(array)) {
                length += array.length;
            }
        }
        final T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);
        length = 0;
        for (final T[] array : arrays) {
            if (isNotEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    // ---------------------------------------------------------------------- copy

    /**
     * 包装 {@link System#arraycopy(Object, int, Object, int, int)}.
     *
     * @param src     源数组
     * @param srcPos  源数组开始位置
     * @param dest    目标数组
     * @param destPos 目标数组开始位置
     * @param length  拷贝数组长度
     * @return 目标数组
     */
    public static Object copy(Object src, int srcPos, Object dest, int destPos, int length) {
        if (null == src || null == dest) {
            throw new NullPointerException("Source array and destination array must not be null");
        }
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    /**
     * 数组复制，源数组和目标数组都是从位置0开始复制.
     *
     * @param src    源数组
     * @param dest   目标数组
     * @param length 拷贝数组长度
     * @return 目标数组
     */
    public static Object copy(Object src, Object dest, int length) {
        if (null == src || null == dest) {
            throw new NullPointerException("Source array and destination array must not be null");
        }
        System.arraycopy(src, 0, dest, 0, length);
        return dest;
    }

    // ---------------------------------------------------------------------- clone

    /**
     * 克隆数组.
     *
     * @param <T>   数组元素类型
     * @param array 被克隆的数组
     * @return 新数组
     */
    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * 克隆数组，如果非数组返回 {@code null}.
     *
     * @param <T> 数组元素类型
     * @param obj 数组对象
     * @return 克隆后的数组对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(final T obj) {
        if (null == obj) {
            return null;
        }
        if (isArray(obj)) {
            final Object result;
            final Class<?> componentType = obj.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                int length = Array.getLength(obj);
                result = Array.newInstance(componentType, length);
                while (length-- > 0) {
                    Array.set(result, length, Array.get(obj, length));
                }
            } else {
                result = ((Object[]) obj).clone();
            }
            return (T) result;
        }
        return null;
    }

    // ---------------------------------------------------------------------- edit / filter / removeNull / removeEmpty / removeBlank / nullToEmpty

    /**
     * 编辑数组.
     *
     * @param <T>    数组元素类型
     * @param array  数组
     * @param editor 编辑器接口，{@code null} 返回原集合
     * @return 编辑后的数组
     */
    public static <T> T[] edit(T[] array, Editor<T> editor) {
        if (null == editor) {
            return array;
        }
        final ArrayList<T> list = new ArrayList<>(array.length);
        T modified;
        for (T t : array) {
            modified = editor.edit(t);
            if (null != modified) {
                list.add(modified);
            }
        }
        final T[] result = newArray(array.getClass().getComponentType(), list.size());
        return list.toArray(result);
    }

    /**
     * 过滤.
     *
     * @param <T>    数组元素类型
     * @param array  数组
     * @param filter 过滤器接口，{@code null} 返回原集合
     * @return 过滤后的数组
     */
    public static <T> T[] filter(T[] array, Filter<T> filter) {
        if (null == array || null == filter) {
            return array;
        }
        return edit(array, t -> filter.accept(t) ? t : null);
    }

    /**
     * 去除 {@code null} 元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 处理后的数组
     */
    public static <T> T[] removeNull(T[] array) {
        return edit(array, t -> t);
    }

    /**
     * 去除 {@code null} 或者 "" 元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 处理后的数组
     */
    public static <T extends CharSequence> T[] removeEmpty(T[] array) {
        return filter(array, StrUtil::isNotEmpty);
    }

    /**
     * 去除 {@code null} 或者 "" 或者空白字符串元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 处理后的数组
     */
    public static <T extends CharSequence> T[] removeBlank(T[] array) {
        return filter(array, StrUtil::isNotBlank);
    }

    /**
     * 数组元素中的 null 转换为 "".
     *
     * @param array 数组
     * @return 新数组
     */
    public static String[] nullToEmpty(String[] array) {
        return edit(array, t -> null == t ? StrUtil.EMPTY : t);
    }

    // ---------------------------------------------------------------------- zip

    /**
     * 映射键值（参考 Python 的 zip() 函数）.
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param keys    键列表
     * @param values  值列表
     * @param isOrder 是否有序
     * @return Map
     */
    public static <K, V> Map<K, V> zip(K[] keys, V[] values, boolean isOrder) {
        if (isEmpty(keys) || isEmpty(values)) {
            return null;
        }
        final int size = Math.min(keys.length, values.length);
        final Map<K, V> map = MapUtil.newHashMap(size, isOrder);
        for (int i = 0; i < size; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    /**
     * 映射键值（参考 Python 的 zip() 函数），返回 Map 无序.
     *
     * @param <K>    Key类型
     * @param <V>    Value类型
     * @param keys   键列表
     * @param values 值列表
     * @return Map
     */
    public static <K, V> Map<K, V> zip(K[] keys, V[] values) {
        return zip(keys, values, false);
    }

    // ------------------------------------------------------------------- indexOf and lastIndexOf and contains

    /**
     * 返回数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}.
     *
     * @param <T>               数组类型
     * @param array             数组
     * @param value             被检查的元素
     * @param beginIndexInclude 检索开始的位置
     * @return 数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}
     */
    public static <T> int indexOf(T[] array, Object value, int beginIndexInclude) {
        return matchIndex((obj) -> ObjectUtil.equal(value, obj), beginIndexInclude, array);
    }

    /**
     * 返回数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}.
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}
     */
    public static <T> int indexOf(T[] array, Object value) {
        return matchIndex((obj) -> ObjectUtil.equal(value, obj), array);
    }

    /**
     * 返回数组中指定元素所在位置，忽略大小写，未找到返回 {@link #INDEX_NOT_FOUND}.
     *
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}
     */
    public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (StrUtil.equalsIgnoreCase(array[i], value)) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在最后的位置，未找到返回 {@link #INDEX_NOT_FOUND}.
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}
     */
    public static <T> int lastIndexOf(T[] array, Object value) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        return lastIndexOf(array, value, array.length - 1);
    }

    /**
     * 返回数组中指定元素所在最后的位置，未找到返回 {@link #INDEX_NOT_FOUND}.
     *
     * @param <T>        数组类型
     * @param array      数组
     * @param value      被检查的元素
     * @param endInclude 查找方式为从后向前查找，查找的数组结束位置
     * @return 数组中指定元素所在位置，未找到返回 {@link #INDEX_NOT_FOUND}
     */
    public static <T> int lastIndexOf(T[] array, Object value, int endInclude) {
        if (isNotEmpty(array)) {
            for (int i = endInclude; i >= 0; i--) {
                if (ObjectUtil.equal(value, array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 数组中是否包含元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) > INDEX_NOT_FOUND;
    }

    /**
     * 数组中是否包含指定元素中的任意一个.
     *
     * @param <T>    数组元素类型
     * @param array  数组
     * @param values 被检查的多个元素
     * @return 是否包含指定元素中的任意一个
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean containsAny(T[] array, T... values) {
        for (T value : values) {
            if (contains(array, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组中是否包含指定元素中的全部.
     *
     * @param <T>    数组元素类型
     * @param array  数组
     * @param values 被检查的多个元素
     * @return 是否包含指定元素中的全部
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean containsAll(T[] array, T... values) {
        for (T value : values) {
            if (false == contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组中是否包含元素，忽略大小写.
     *
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static boolean containsIgnoreCase(CharSequence[] array, CharSequence value) {
        return indexOfIgnoreCase(array, value) > INDEX_NOT_FOUND;
    }

    // ------------------------------------------------------------------- Wrap and unwrap

    /**
     * 包装数组对象.
     *
     * @param obj 对象，可以是对象数组或者基本类型数组
     * @return 包装类型数组或对象数组
     * @throws UtilException 对象为非数组
     */
    public static Object[] wrap(Object obj) {
        if (null == obj) {
            return null;
        }
        if (isArray(obj)) {
            try {
                return (Object[]) obj;
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return wrap((long[]) obj);
                    case "int":
                        return wrap((int[]) obj);
                    case "short":
                        return wrap((short[]) obj);
                    case "char":
                        return wrap((char[]) obj);
                    case "byte":
                        return wrap((byte[]) obj);
                    case "boolean":
                        return wrap((boolean[]) obj);
                    case "float":
                        return wrap((float[]) obj);
                    case "double":
                        return wrap((double[]) obj);
                    default:
                        throw new UtilException(e);
                }
            }
        }
        throw new UtilException(StrUtil.format("[{}] is not Array!", obj.getClass()));
    }

    /**
     * 对象是否为数组对象.
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为 {@code null} 返回 false
     */
    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    /**
     * 获取数组对象中指定 index 的值，支持负数，例如 -1 表示倒数第一个值.
     * 如果数组下标越界，返回 null.
     *
     * @param <T>   数组元素类型
     * @param array 数组对象
     * @param index 下标，支持负数
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object array, int index) {
        if (null == array) {
            return null;
        }
        if (index < 0) {
            index += Array.getLength(array);
        }
        try {
            return (T) Array.get(array, index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * 获取数组中指定多个下标元素值，组成新数组.
     *
     * @param <T>     数组元素类型
     * @param array   数组，如果提供为 {@code null} 则返回 {@code null}
     * @param indexes 下标列表
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] getAny(Object array, int... indexes) {
        if (null == array) {
            return null;
        }
        if (null == indexes) {
            return newArray(array.getClass().getComponentType(), 0);
        }
        final T[] result = newArray(array.getClass().getComponentType(), indexes.length);
        for (int i = 0; i < indexes.length; i++) {
            result[i] = ArrayUtil.get(array, indexes[i]);
        }
        return result;
    }

    // ---------------------------------------------------------------------- sub

    /**
     * 获取子数组.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param start 开始位置（包括）
     * @param end   结束位置（不包括）
     * @return 新的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] sub(T[] array, int start, int end) {
        int length = length(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return newArray(array.getClass().getComponentType(), 0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return newArray(array.getClass().getComponentType(), 0);
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    /**
     * 获取子数组.
     *
     * @param array 数组
     * @param start 开始位置（包括）
     * @param end   结束位置（不包括）
     * @return 新的数组
     */
    public static Object[] sub(Object array, int start, int end) {
        return sub(array, start, end, 1);
    }

    /**
     * 获取子数组.
     *
     * @param array 数组
     * @param start 开始位置（包括）
     * @param end   结束位置（不包括）
     * @param step  步进
     * @return 新的数组
     */
    public static Object[] sub(Object array, int start, int end, int step) {
        int length = length(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new Object[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new Object[0];
            }
            end = length;
        }
        if (step <= 1) {
            step = 1;
        }
        final ArrayList<Object> list = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            list.add(get(array, i));
        }
        return list.toArray();
    }

    // ---------------------------------------------------------------------- toString / length

    /**
     * 数组或集合转 String.
     *
     * @param obj 集合或数组对象
     * @return 数组字符串
     */
    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        } else if (obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        } else if (obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        } else if (obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        } else if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        } else if (obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        } else if (obj instanceof double[]) {
            return Arrays.toString((double[]) obj);
        } else if (ArrayUtil.isArray(obj)) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception ignore) {
                //ignore
            }
        }
        return obj.toString();
    }

    /**
     * 获取数组长度，如果参数为 {@code null}，返回 0.
     *
     * @param array 数组对象
     * @return 数组长度
     * @throws IllegalArgumentException 如果参数不为数组
     */
    public static int length(Object array) throws IllegalArgumentException {
        if (null == array) {
            return 0;
        }
        return Array.getLength(array);
    }

    // ---------------------------------------------------------------------- join

    /**
     * 以 conjunction 为分隔符将数组转换为字符串.
     *
     * @param <T>         被处理的集合
     * @param array        数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(T[] array, CharSequence conjunction) {
        return join(array, conjunction, null, null);
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串.
     *
     * @param <T>       被处理的集合
     * @param array     数组
     * @param delimiter 分隔符
     * @param prefix    每个元素添加的前缀，null表示不添加
     * @param suffix    每个元素添加的后缀，null表示不添加
     * @return 连接后的字符串
     */
    public static <T> String join(T[] array, CharSequence delimiter, String prefix, String suffix) {
        if (null == array) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T element : array) {
            if (!isFirst) {
                sb.append(delimiter);
            }
            isFirst = false;
            if (null != prefix) {
                sb.append(prefix);
            }
            sb.append(element);
            if (null != suffix) {
                sb.append(suffix);
            }
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串.
     *
     * @param <T>         被处理的集合
     * @param array        数组
     * @param conjunction 分隔符
     * @param editor      每个元素的编辑器，null表示不编辑
     * @return 连接后的字符串
     */
    public static <T> String join(T[] array, CharSequence conjunction, Editor<T> editor) {
        if (null == array) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T element : array) {
            T edited = null == editor ? element : editor.edit(element);
            if (null == edited) {
                continue;
            }
            if (!isFirst) {
                sb.append(conjunction);
            }
            isFirst = false;
            sb.append(edited);
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串.
     *
     * @param array        数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static String join(Object array, CharSequence conjunction) {
        if (null == array) {
            return null;
        }
        if (false == isArray(array)) {
            throw new IllegalArgumentException(StrUtil.format("[{}] is not a Array!", array.getClass()));
        }
        final int len = length(array);
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < len; i++) {
            if (!isFirst) {
                sb.append(conjunction);
            }
            isFirst = false;
            sb.append(String.valueOf((Object) get(array, i)));
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------------- toArray

    /**
     * {@link ByteBuffer} 转byte数组.
     *
     * @param bytebuffer {@link ByteBuffer}
     * @return byte数组
     */
    public static byte[] toArray(ByteBuffer bytebuffer) {
        if (bytebuffer.hasArray()) {
            return Arrays.copyOfRange(bytebuffer.array(), bytebuffer.position(), bytebuffer.limit());
        } else {
            int oldPosition = bytebuffer.position();
            bytebuffer.position(0);
            int size = bytebuffer.limit();
            byte[] buffers = new byte[size];
            bytebuffer.get(buffers);
            bytebuffer.position(oldPosition);
            return buffers;
        }
    }

    /**
     * 将迭代器转为数组.
     *
     * @param <T>           数组元素类型
     * @param iterator      {@link Iterator}
     * @param componentType 集合元素类型
     * @return 数组
     */
    public static <T> T[] toArray(Iterator<T> iterator, Class<T> componentType) {
        final List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return toArray(list, componentType);
    }

    /**
     * 将 Iterable 转为数组.
     *
     * @param <T>           数组元素类型
     * @param iterable      {@link Iterable}
     * @param componentType 集合元素类型
     * @return 数组
     */
    public static <T> T[] toArray(Iterable<T> iterable, Class<T> componentType) {
        final List<T> list = new ArrayList<>();
        for (T t : iterable) {
            list.add(t);
        }
        return toArray(list, componentType);
    }

    /**
     * 将集合转为数组.
     *
     * @param <T>           数组元素类型
     * @param collection    集合
     * @param componentType 集合元素类型
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection, Class<T> componentType) {
        return collection.toArray(newArray(componentType, 0));
    }

    // ---------------------------------------------------------------------- remove / removeEle

    /**
     * 移除数组中对应位置的元素.
     *
     * @param <T>   数组元素类型
     * @param array 数组对象
     * @param index 位置，如果位置小于0或者大于长度，返回原数组
     * @return 去掉指定元素后的新数组或原数组
     * @throws IllegalArgumentException 参数对象不为数组对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] array, int index) throws IllegalArgumentException {
        return (T[]) remove((Object) array, index);
    }

    /**
     * 移除数组中指定的元素，只会移除匹配到的第一个元素.
     *
     * @param <T>     数组元素类型
     * @param array   数组对象
     * @param element 要移除的元素
     * @return 去掉指定元素后的新数组或原数组
     * @throws IllegalArgumentException 参数对象不为数组对象
     */
    public static <T> T[] removeEle(T[] array, T element) throws IllegalArgumentException {
        return remove(array, indexOf(array, element));
    }

    // ---------------------------------------------------------------------- reverse

    /**
     * 反转数组，会变更原数组.
     *
     * @param <T>                 数组元素类型
     * @param array               数组，会变更
     * @param startIndexInclusive 开始位置（包含）
     * @param endIndexExclusive   结束位置（不包含）
     * @return 变更后的原数组
     */
    public static <T> T[] reverse(T[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (isEmpty(array)) {
            return array;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        T tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    /**
     * 反转数组，会变更原数组.
     *
     * @param <T>   数组元素类型
     * @param array 数组，会变更
     * @return 变更后的原数组
     */
    public static <T> T[] reverse(T[] array) {
        return reverse(array, 0, array.length);
    }

    // ---------------------------------------------------------------------- min / max

    /**
     * 取最小值.
     *
     * @param <T>         元素类型
     * @param numberArray 数字数组
     * @return 最小值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Comparable<? super T>> T min(T[] numberArray) {
        return (T) min(numberArray, null);
    }

    /**
     * 取最小值.
     *
     * @param <T>         元素类型
     * @param numberArray 数字数组
     * @param comparator  比较器，null按照默认比较
     * @return 最小值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Comparable<? super T>> T min(T[] numberArray, Comparator<T> comparator) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        }
        T min = numberArray[0];
        for (T t : numberArray) {
            int cmp;
            if (comparator == null) {
                cmp = ((Comparable) min).compareTo(t);
            } else {
                cmp = comparator.compare(min, t);
            }
            if (cmp > 0) {
                min = t;
            }
        }
        return min;
    }

    /**
     * 取最大值.
     *
     * @param <T>         元素类型
     * @param numberArray 数字数组
     * @return 最大值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Comparable<? super T>> T max(T[] numberArray) {
        return (T) max(numberArray, null);
    }

    /**
     * 取最大值.
     *
     * @param <T>         元素类型
     * @param numberArray 数字数组
     * @param comparator  比较器，null表示默认比较器
     * @return 最大值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Comparable<? super T>> T max(T[] numberArray, Comparator<T> comparator) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        }
        T max = numberArray[0];
        for (int i = 1; i < numberArray.length; i++) {
            int cmp;
            if (comparator == null) {
                cmp = ((Comparable) max).compareTo(numberArray[i]);
            } else {
                cmp = comparator.compare(max, numberArray[i]);
            }
            if (cmp < 0) {
                max = numberArray[i];
            }
        }
        return max;
    }

    // ---------------------------------------------------------------------- shuffle

    /**
     * 打乱数组顺序，会变更原数组.
     *
     * @param <T>   元素类型
     * @param array 数组，会变更
     * @return 打乱后的数组
     */
    public static <T> T[] shuffle(T[] array) {
        return shuffle(array, RandomUtil.getRandom());
    }

    /**
     * 打乱数组顺序，会变更原数组.
     *
     * @param <T>    元素类型
     * @param array  数组，会变更
     * @param random 随机数生成器
     * @return 打乱后的数组
     */
    public static <T> T[] shuffle(T[] array, Random random) {
        if (array == null || random == null || array.length <= 1) {
            return array;
        }
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    // ---------------------------------------------------------------------- swap

    /**
     * 交换数组中两个位置的值.
     *
     * @param <T>    元素类型
     * @param array  数组
     * @param index1 位置1
     * @param index2 位置2
     * @return 交换后的数组，与传入数组为同一对象
     */
    public static <T> T[] swap(T[] array, int index1, int index2) {
        if (isEmpty(array)) {
            throw new IllegalArgumentException("Array must not empty !");
        }
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
        return array;
    }

    /**
     * 交换数组中两个位置的值.
     *
     * @param array  数组对象
     * @param index1 位置1
     * @param index2 位置2
     * @return 交换后的数组，与传入数组为同一对象
     */
    public static Object swap(Object array, int index1, int index2) {
        if (isEmpty(array)) {
            throw new IllegalArgumentException("Array must not empty !");
        }
        Object tmp = get(array, index1);
        Array.set(array, index1, Array.get(array, index2));
        Array.set(array, index2, tmp);
        return array;
    }

    // ---------------------------------------------------------------------- emptyCount / hasEmpty / isAllEmpty / isAllNotEmpty / isAllNotNull

    /**
     * 计算 {@code null} 或空元素对象的个数.
     *
     * @param args 被检查的对象
     * @return 存在 {@code null} 的数量
     */
    public static int emptyCount(Object... args) {
        int count = 0;
        if (isNotEmpty(args)) {
            for (Object element : args) {
                if (ObjectUtil.isEmpty(element)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 是否存在 {@code null} 或空对象.
     *
     * @param args 被检查对象
     * @return 是否存在
     */
    public static boolean hasEmpty(Object... args) {
        if (isNotEmpty(args)) {
            for (Object element : args) {
                if (ObjectUtil.isEmpty(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否全都为 {@code null} 或空对象.
     *
     * @param args 被检查的对象
     * @return 是否都为空
     */
    public static boolean isAllEmpty(Object... args) {
        for (Object obj : args) {
            if (false == ObjectUtil.isEmpty(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全都不为 {@code null} 或空对象.
     *
     * @param args 被检查的对象
     * @return 是否都不为空
     */
    public static boolean isAllNotEmpty(Object... args) {
        return false == hasEmpty(args);
    }

    /**
     * 多个字段是否全部不为 null.
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 多个字段是否全部不为 null
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isAllNotNull(T... array) {
        return false == hasNull(array);
    }

    // ---------------------------------------------------------------------- distinct

    /**
     * 去重数组中的元素，去重后生成新的数组，原数组不变.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 去重后的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] distinct(T[] array) {
        if (isEmpty(array)) {
            return array;
        }
        final Set<T> set = new LinkedHashSet<>(array.length, 1);
        Collections.addAll(set, array);
        return toArray(set, (Class<T>) getComponentType(array));
    }

    /**
     * 去重数组中的元素，去重后生成新的数组，原数组不变.
     *
     * @param <T>             数组元素类型
     * @param <K>             唯一键类型
     * @param array           数组
     * @param uniqueGenerator 唯一键生成器
     * @param override        是否覆盖模式
     * @return 去重后的数组
     */
    @SuppressWarnings("unchecked")
    public static <T, K> T[] distinct(T[] array, Function<T, K> uniqueGenerator, boolean override) {
        if (isEmpty(array)) {
            return array;
        }
        // 内联 UniqueKeySet 逻辑：使用 LinkedHashMap 保持顺序
        final LinkedHashMap<K, T> map = new LinkedHashMap<>(array.length, 1);
        if (override) {
            for (T t : array) {
                map.put(uniqueGenerator.apply(t), t);
            }
        } else {
            for (T t : array) {
                map.putIfAbsent(uniqueGenerator.apply(t), t);
            }
        }
        return toArray(map.values(), (Class<T>) getComponentType(array));
    }

    // ---------------------------------------------------------------------- map

    /**
     * 按照指定规则，将一种类型的数组转换为另一种类型.
     *
     * @param array               被转换的数组
     * @param targetComponentType 目标的元素类型
     * @param func                转换规则函数
     * @param <T>                 原数组类型
     * @param <R>                 目标数组类型
     * @return 转换后的数组
     */
    public static <T, R> R[] map(T[] array, Class<R> targetComponentType, Function<? super T, ? extends R> func) {
        final R[] result = newArray(targetComponentType, array.length);
        for (int i = 0; i < array.length; i++) {
            result[i] = func.apply(array[i]);
        }
        return result;
    }

    /**
     * 按照指定规则，将一种类型的数组转换为另一种类型.
     *
     * @param array               被转换的数组
     * @param targetComponentType 目标的元素类型
     * @param func                转换规则函数
     * @param <T>                 原数组类型
     * @param <R>                 目标数组类型
     * @return 转换后的数组
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R[] map(Object array, Class<R> targetComponentType, Function<? super T, ? extends R> func) {
        final int length = length(array);
        final R[] result = newArray(targetComponentType, length);
        for (int i = 0; i < length; i++) {
            result[i] = func.apply(get(array, i));
        }
        return result;
    }

    /**
     * 按照指定规则，将一种类型的数组元素提取后转换为 List.
     *
     * @param array 被转换的数组
     * @param func  转换规则函数
     * @param <T>   原数组类型
     * @param <R>   目标数组类型
     * @return 转换后的 List
     */
    public static <T, R> List<R> map(T[] array, Function<? super T, ? extends R> func) {
        return Arrays.stream(array).map(func).collect(Collectors.toList());
    }

    /**
     * 按照指定规则，将一种类型的数组元素提取后转换为 Set.
     *
     * @param array 被转换的数组
     * @param func  转换规则函数
     * @param <T>   原数组类型
     * @param <R>   目标数组类型
     * @return 转换后的 Set
     */
    public static <T, R> Set<R> mapToSet(T[] array, Function<? super T, ? extends R> func) {
        return Arrays.stream(array).map(func).collect(Collectors.toSet());
    }

    // ---------------------------------------------------------------------- equals

    /**
     * 判断两个数组是否相等.
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 是否相等
     */
    public static boolean equals(Object array1, Object array2) {
        if (array1 == array2) {
            return true;
        }
        if (hasNull(array1, array2)) {
            return false;
        }
        Assert.isTrue(isArray(array1), "First is not a Array !");
        Assert.isTrue(isArray(array2), "Second is not a Array !");
        if (array1 instanceof long[]) {
            return Arrays.equals((long[]) array1, (long[]) array2);
        } else if (array1 instanceof int[]) {
            return Arrays.equals((int[]) array1, (int[]) array2);
        } else if (array1 instanceof short[]) {
            return Arrays.equals((short[]) array1, (short[]) array2);
        } else if (array1 instanceof char[]) {
            return Arrays.equals((char[]) array1, (char[]) array2);
        } else if (array1 instanceof byte[]) {
            return Arrays.equals((byte[]) array1, (byte[]) array2);
        } else if (array1 instanceof double[]) {
            return Arrays.equals((double[]) array1, (double[]) array2);
        } else if (array1 instanceof float[]) {
            return Arrays.equals((float[]) array1, (float[]) array2);
        } else if (array1 instanceof boolean[]) {
            return Arrays.equals((boolean[]) array1, (boolean[]) array2);
        } else {
            return Arrays.deepEquals((Object[]) array1, (Object[]) array2);
        }
    }

    // ---------------------------------------------------------------------- isSub / indexOfSub / lastIndexOfSub

    /**
     * 查找子数组的位置.
     *
     * @param array    数组
     * @param subArray 子数组
     * @param <T>      数组元素类型
     * @return 子数组的开始位置
     */
    public static <T> boolean isSub(T[] array, T[] subArray) {
        return indexOfSub(array, subArray) > INDEX_NOT_FOUND;
    }

    /**
     * 查找子数组的位置.
     *
     * @param array    数组
     * @param subArray 子数组
     * @param <T>      数组元素类型
     * @return 子数组的开始位置
     */
    public static <T> int indexOfSub(T[] array, T[] subArray) {
        return indexOfSub(array, 0, subArray);
    }

    /**
     * 查找子数组的位置.
     *
     * @param array        数组
     * @param beginInclude 查找开始的位置（包含）
     * @param subArray     子数组
     * @param <T>          数组元素类型
     * @return 子数组的开始位置
     */
    public static <T> int indexOfSub(T[] array, int beginInclude, T[] subArray) {
        if (isEmpty(array) || isEmpty(subArray)) {
            return INDEX_NOT_FOUND;
        }
        if (beginInclude < 0) {
            beginInclude += array.length;
        }
        if (beginInclude < 0 || beginInclude > array.length - 1) {
            return INDEX_NOT_FOUND;
        }
        if (array.length - beginInclude < subArray.length) {
            return INDEX_NOT_FOUND;
        }
        for (int i = beginInclude; i <= array.length - subArray.length; i++) {
            boolean found = true;
            for (int j = 0; j < subArray.length; j++) {
                if (ObjectUtil.notEqual(array[i + j], subArray[j])) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 查找最后一个子数组的开始位置.
     *
     * @param array    数组
     * @param subArray 子数组
     * @param <T>      数组元素类型
     * @return 最后一个子数组的开始位置
     */
    public static <T> int lastIndexOfSub(T[] array, T[] subArray) {
        if (isEmpty(array) || isEmpty(subArray)) {
            return INDEX_NOT_FOUND;
        }
        return lastIndexOfSub(array, array.length - 1, subArray);
    }

    /**
     * 查找最后一个子数组的开始位置.
     *
     * @param array      数组
     * @param endInclude 查找结束的位置（包含），-1表示最后一位
     * @param subArray   子数组
     * @param <T>        数组元素类型
     * @return 最后一个子数组的开始位置
     */
    public static <T> int lastIndexOfSub(T[] array, int endInclude, T[] subArray) {
        if (isEmpty(array) || isEmpty(subArray)) {
            return INDEX_NOT_FOUND;
        }
        if (endInclude < 0) {
            endInclude += array.length;
        }
        if (endInclude < 0) {
            return INDEX_NOT_FOUND;
        }
        if (endInclude > array.length - 1) {
            endInclude = array.length - 1;
        }
        if (subArray.length - 1 > endInclude) {
            return INDEX_NOT_FOUND;
        }
        for (int i = Math.min(array.length - subArray.length, endInclude); i >= 0; i--) {
            boolean found = true;
            for (int j = 0; j < subArray.length; j++) {
                if (ObjectUtil.notEqual(array[i + j], subArray[j])) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // ---------------------------------------------------------------------- isSorted / isSortedASC / isSortedDESC

    /**
     * 检查数组是否有序.
     *
     * @param array      数组
     * @param comparator 比较器
     * @param <T>        数组元素类型
     * @return 数组是否有序
     */
    public static <T> boolean isSorted(T[] array, Comparator<? super T> comparator) {
        if (array == null || comparator == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (comparator.compare(array[i], array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查数组是否升序.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 数组是否升序
     */
    public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
        return isSortedASC(array);
    }

    /**
     * 检查数组是否升序.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 数组是否升序
     */
    public static <T extends Comparable<? super T>> boolean isSortedASC(T[] array) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查数组是否降序.
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 数组是否降序
     */
    public static <T extends Comparable<? super T>> boolean isSortedDESC(T[] array) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) < 0) {
                return false;
            }
        }
        return true;
    }
}
