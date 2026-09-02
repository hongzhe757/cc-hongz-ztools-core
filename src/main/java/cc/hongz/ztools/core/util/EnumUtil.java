package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.lang.func.LambdaUtil;
import cc.hongz.ztools.core.map.MapUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 枚举工具类.
 * @author looly
 */
public final class EnumUtil {

    private static final Map<Class<?>, Enum<?>[]> CACHE = new ConcurrentHashMap<>();

    private EnumUtil() {
    }

    /**
     * 清空缓存，重新加载枚举类.
     */
    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * 指定类是否为 Enum 类.
     *
     * @param clazz 类
     * @return 是否为 Enum 类
     */
    public static boolean isEnum(final Class<?> clazz) {
        return Assert.notNull(clazz).isEnum();
    }

    /**
     * 指定类是否为 Enum 类.
     *
     * @param obj 类
     * @return 是否为 Enum 类
     */
    public static boolean isEnum(final Object obj) {
        return Assert.notNull(obj).getClass().isEnum();
    }

    /**
     * Enum 对象转 String，调用 {@link Enum#name()} 方法.
     *
     * @param e Enum
     * @return name 值
     */
    public static String toString(final Enum<?> e) {
        return null != e ? e.name() : null;
    }

    /**
     * 获取给定位置的枚举值.
     *
     * @param <E>       枚举类型泛型
     * @param enumClass 枚举类
     * @param index     枚举索引
     * @return 枚举值，null 表示无此对应枚举
     */
    public static <E extends Enum<E>> E getEnumAt(final Class<E> enumClass, final int index) {
        if (null == enumClass) {
            return null;
        }
        final E[] enumConstants = getEnums(enumClass);
        int idx = index;
        if (idx < 0) {
            idx = enumConstants.length + idx;
        }

        return idx >= 0 && idx < enumConstants.length ? enumConstants[idx] : null;
    }

    /**
     * 字符串转枚举，调用 {@link Enum#valueOf(Class, String)}.
     *
     * @param <E>       枚举类型泛型
     * @param enumClass 枚举类
     * @param value     值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromString(final Class<E> enumClass, final String value) {
        if (null == enumClass || StrUtil.isBlank(value)) {
            return null;
        }
        return Enum.valueOf(enumClass, value);
    }

    /**
     * 字符串转枚举，调用 {@link Enum#valueOf(Class, String)}.
     * 如果无枚举值，返回默认值.
     *
     * @param <E>          枚举类型泛型
     * @param enumClass    枚举类
     * @param value        值
     * @param defaultValue 无对应枚举值返回的默认值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromString(final Class<E> enumClass, final String value, final E defaultValue) {
        return ObjectUtil.defaultIfNull(fromStringQuietly(enumClass, value), defaultValue);
    }

    /**
     * 字符串转枚举，调用 {@link Enum#valueOf(Class, String)}，转换失败返回 {@code null} 而非报错.
     *
     * @param <E>       枚举类型泛型
     * @param enumClass 枚举类
     * @param value     值
     * @return 枚举值
     */
    public static <E extends Enum<E>> E fromStringQuietly(final Class<E> enumClass, final String value) {
        try {
            return fromString(enumClass, value);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 模糊匹配转换为枚举，给定一个值，匹配枚举中定义的所有字段名（包括 name 属性），一旦匹配到返回这个枚举对象，
     * 否则返回 null.
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @param value     值
     * @return 匹配到的枚举对象，未匹配到返回 null
     */
    public static <E extends Enum<E>> E likeValueOf(final Class<E> enumClass, final Object value) {
        if (null == enumClass || null == value) {
            return null;
        }
        Object val = value;
        if (val instanceof CharSequence) {
            val = val.toString().trim();
        }

        final Field[] fields = ReflectUtil.getFields(enumClass);
        final E[] enums = getEnums(enumClass);
        String fieldName;
        for (final Field field : fields) {
            fieldName = field.getName();
            if (field.getType().isEnum() || "ENUM$VALUES".equals(fieldName) || "ordinal".equals(fieldName)) {
                // 跳过一些特殊字段
                continue;
            }
            for (final E enumObj : enums) {
                if (ObjectUtil.equal(val, ReflectUtil.getFieldValue(enumObj, field))) {
                    return enumObj;
                }
            }
        }
        return null;
    }

    /**
     * 枚举类中所有枚举对象的 name 列表.
     *
     * @param clazz 枚举类
     * @return name 列表
     */
    public static List<String> getNames(final Class<? extends Enum<?>> clazz) {
        if (null == clazz) {
            return null;
        }
        final Enum<?>[] enums = getEnums2(clazz);
        if (null == enums) {
            return null;
        }
        final List<String> list = new ArrayList<>(enums.length);
        for (final Enum<?> e : enums) {
            list.add(e.name());
        }
        return list;
    }

    /**
     * 获得枚举类中各枚举对象下指定字段的值.
     *
     * @param clazz     枚举类
     * @param fieldName 字段名，最终调用 getXXX 方法
     * @return 字段值列表
     */
    public static List<Object> getFieldValues(final Class<? extends Enum<?>> clazz, final String fieldName) {
        if (null == clazz || StrUtil.isBlank(fieldName)) {
            return null;
        }
        final Enum<?>[] enums = getEnums2(clazz);
        if (null == enums) {
            return null;
        }
        final List<Object> list = new ArrayList<>(enums.length);
        for (final Enum<?> e : enums) {
            list.add(ReflectUtil.getFieldValue(e, fieldName));
        }
        return list;
    }

    /**
     * 获得枚举类中所有的字段名.
     * 除用户自定义的字段名，也包括 "name" 字段，例如：
     *
     * <pre>
     *   EnumUtil.getFieldNames(Color.class) == ["name", "index"]
     * </pre>
     *
     * @param clazz 枚举类
     * @return 字段名列表
     */
    public static List<String> getFieldNames(final Class<? extends Enum<?>> clazz) {
        if (null == clazz) {
            return null;
        }
        final List<String> names = new ArrayList<>();
        final Field[] fields = ReflectUtil.getFields(clazz);
        String name;
        for (final Field field : fields) {
            name = field.getName();
            if (field.getType().isEnum() || name.contains("$VALUES") || "ordinal".equals(name)) {
                continue;
            }
            if (!names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code null}.
     *
     * @param enumClass 枚举类
     * @param predicate 条件
     * @param <E>       枚举类型
     * @return 对应枚举，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>> E getBy(final Class<E> enumClass, final Predicate<? super E> predicate) {
        return getBy(enumClass, predicate, null);
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code defaultEnum}.
     * 通过缓存减少反射带来的影响.
     *
     * @param enumClass   枚举类
     * @param predicate   条件
     * @param defaultEnum 获取不到时的默认枚举值
     * @param <E>         枚举类型
     * @return 对应枚举，获取不到时为 {@code defaultEnum}
     */
    public static <E extends Enum<E>> E getBy(final Class<E> enumClass,
            final Predicate<? super E> predicate, final E defaultEnum) {
        if (null == enumClass || null == predicate) {
            return null;
        }

        return Arrays.stream(getEnums(enumClass))
                .filter(predicate).findFirst().orElse(defaultEnum);
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code null}.
     *
     * @param enumClass 枚举类，为 {@code null} 返回 {@code null}
     * @param condition 条件字段，为 {@code null} 返回 {@code null}
     * @param value     条件字段值
     * @param <E>       枚举类型
     * @param <C>       字段类型
     * @return 对应枚举，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>, C> E getBy(final Class<E> enumClass, final Func1<E, C> condition, final C value) {
        if (null == condition) {
            return null;
        }
        return getBy(enumClass, constant -> ObjectUtil.equal(condition.callWithRuntimeException(constant), value));
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code defaultEnum}.
     *
     * @param enumClass   枚举类，为 {@code null} 返回 {@code null}
     * @param condition   条件字段，为 {@code null} 返回 {@code null}
     * @param value       条件字段值
     * @param defaultEnum 获取不到时的默认枚举值
     * @param <E>         枚举类型
     * @param <C>         字段类型
     * @return 对应枚举，获取不到时为 {@code defaultEnum}
     */
    public static <E extends Enum<E>, C> E getBy(final Class<E> enumClass,
            final Func1<E, C> condition, final C value, final E defaultEnum) {
        return ObjectUtil.defaultIfNull(getBy(enumClass, condition, value), defaultEnum);
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code null}.
     *
     * @param condition 条件字段，为 {@code null} 返回 {@code null}
     * @param value     条件字段值
     * @param <E>       枚举类型
     * @param <C>       字段类型
     * @return 对应枚举，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>, C> E getBy(final Func1<E, C> condition, final C value) {
        if (null == condition) {
            return null;
        }
        final Class<E> implClass = LambdaUtil.getRealClass(condition);
        return getBy(implClass, condition, value);
    }

    /**
     * 通过某字段对应值获取枚举，获取不到时为 {@code defaultEnum}.
     *
     * @param <E>         枚举类型
     * @param <C>         字段类型
     * @param condition   条件字段
     * @param value       条件字段值
     * @param defaultEnum 条件找不到则返回结果使用这个
     * @return 对应枚举，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>, C> E getBy(final Func1<E, C> condition, final C value, final E defaultEnum) {
        return ObjectUtil.defaultIfNull(getBy(condition, value), defaultEnum);
    }

    /**
     * 通过某字段对应值获取枚举中另一字段值，获取不到时为 {@code null}.
     *
     * @param field     你想要获取的字段，{@code null} 返回 {@code null}
     * @param condition 条件字段，{@code null} 返回 {@code null}
     * @param value     条件字段值
     * @param <E>       枚举类型
     * @param <F>       想要获取的字段类型
     * @param <C>       条件字段类型
     * @return 对应枚举中另一字段值，获取不到时为 {@code null}
     */
    public static <E extends Enum<E>, F, C> F getFieldBy(final Func1<E, F> field,
            final Function<E, C> condition, final C value) {
        if (null == field || null == condition) {
            return null;
        }
        final Class<E> implClass = LambdaUtil.getRealClass(field);
        return Arrays.stream(getEnums(implClass))
                // 过滤
                .filter(constant -> ObjectUtil.equal(condition.apply(constant), value))
                // 获取第一个并转换为结果
                .findFirst()
                .map(field::callWithRuntimeException)
                .orElse(null);
    }

    /**
     * 获取枚举字符串值和枚举对象的 Map 对应，使用 LinkedHashMap 保证有序.
     * 结果中键为枚举名，值为枚举对象.
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @return 枚举字符串值和枚举对象的 Map 对应，使用 LinkedHashMap 保证有序
     */
    public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(final Class<E> enumClass) {
        if (null == enumClass) {
            return null;
        }
        final LinkedHashMap<String, E> map = new LinkedHashMap<>();
        for (final E e : getEnums(enumClass)) {
            map.put(e.name(), e);
        }
        return map;
    }

    /**
     * 获得枚举名对应指定字段值的 Map.
     * 键为枚举名，值为字段值.
     *
     * @param clazz     枚举类
     * @param fieldName 字段名，最终调用 getXXX 方法
     * @return 枚举名对应指定字段值的 Map
     */
    public static Map<String, Object> getNameFieldMap(final Class<? extends Enum<?>> clazz, final String fieldName) {
        if (null == clazz || StrUtil.isBlank(fieldName)) {
            return null;
        }
        final Enum<?>[] enums = getEnums2(clazz);
        if (null == enums) {
            return null;
        }
        final Map<String, Object> map = MapUtil.newHashMap(enums.length, true);
        for (final Enum<?> e : enums) {
            map.put(e.name(), ReflectUtil.getFieldValue(e, fieldName));
        }
        return map;
    }

    /**
     * 判断某个值是存在枚举中.
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @param val       需要查找的值
     * @return 是否存在
     */
    public static <E extends Enum<E>> boolean contains(final Class<E> enumClass, final String val) {
        final LinkedHashMap<String, E> enumMap = getEnumMap(enumClass);
        if (enumMap == null || enumMap.isEmpty()) {
            return false;
        }
        return enumMap.containsKey(val);
    }

    /**
     * 判断某个值是不存在枚举中.
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @param val       需要查找的值
     * @return 是否不存在
     */
    public static <E extends Enum<E>> boolean notContains(final Class<E> enumClass, final String val) {
        return !contains(enumClass, val);
    }

    /**
     * 忽略大小检查某个枚举值是否匹配指定值.
     *
     * @param e   枚举值
     * @param val 需要判断的值
     * @return 是否匹配
     */
    public static boolean equalsIgnoreCase(final Enum<?> e, final String val) {
        return StrUtil.equalsIgnoreCase(toString(e), val);
    }

    /**
     * 检查某个枚举值是否匹配指定值.
     *
     * @param e   枚举值
     * @param val 需要判断的值
     * @return 是否匹配
     */
    public static boolean equals(final Enum<?> e, final String val) {
        return StrUtil.equals(toString(e), val, false);
    }

    /**
     * 获取枚举类中的枚举值，调用过的枚举值会缓存，下次调用会从缓存中获取.
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @return 枚举类中的枚举值
     */
    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> E[] getEnums(final Class<E> enumClass) {
        return (E[]) getEnums2(enumClass);
    }

    /**
     * 获取枚举类中的枚举值，调用过的枚举值会缓存，下次调用会从缓存中获取.
     *
     * @param enumClass 枚举类
     * @return 枚举类中的枚举值
     * @author hongz (原作者：looly)
     * @since 0.0.2
     */
    private static Enum<?>[] getEnums2(final Class<? extends Enum<?>> enumClass) {
        if (null == enumClass) {
            return null;
        }
        // fix issue#IDQYJK: 避免 ConcurrentHashMap.computeIfAbsent 在枚举类静态初始化时
        // 递归调用导致 IllegalStateException: Recursive update 的问题
        // 使用 get + putIfAbsent 替代 computeIfAbsent，安全支持递归场景
        Enum<?>[] enums = CACHE.get(enumClass);
        if (null != enums) {
            return enums;
        }
        enums = enumClass.getEnumConstants();
        if (null != enums) {
            CACHE.putIfAbsent(enumClass, enums);
        }
        return enums;
    }
}
