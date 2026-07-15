package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Assert;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对 {@link Type} 的工具类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.TypeUtil}。
 * <br>
 * 聚焦泛型参数解析和 Type → Class 转换。
 *
 * @author Looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 * @since 3.0.8
 */
public final class TypeUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private TypeUtil() {
    }

    /**
     * 获得 Type 对应的原始类.
     *
     * @param type {@link Type}
     * @return 原始类，如果无法获取原始类，返回 {@code null}
     */
    public static Class<?> getClass(final Type type) {
        if (null != type) {
            if (type instanceof Class) {
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            } else if (type instanceof GenericArrayType) {
                final Type componentType = ((GenericArrayType) type).getGenericComponentType();
                final Class<?> componentClass = getClass(componentType);
                if (componentClass != null) {
                    return Array.newInstance(componentClass, 0).getClass();
                }
            } else if (type instanceof TypeVariable) {
                final Type[] bounds = ((TypeVariable<?>) type).getBounds();
                if (bounds.length == 1) {
                    return getClass(bounds[0]);
                }
            } else if (type instanceof WildcardType) {
                final Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getClass(upperBounds[0]);
                }
            }
        }
        return null;
    }

    /**
     * 获得给定类的第一个泛型参数.
     *
     * @param type 被检查的类型
     * @return {@link Type}，可能为 {@code null}
     */
    public static Type getTypeArgument(final Type type) {
        return getTypeArgument(type, 0);
    }

    /**
     * 获得给定类的泛型参数.
     *
     * @param type  被检查的类型
     * @param index 泛型类型的索引号
     * @return {@link Type}
     */
    public static Type getTypeArgument(final Type type, final int index) {
        final Type[] typeArguments = getTypeArguments(type);
        if (null != typeArguments && typeArguments.length > index) {
            return typeArguments[index];
        }
        return null;
    }

    /**
     * 获得指定类型中所有泛型参数类型.
     *
     * @param type 指定类型
     * @return 所有泛型参数类型
     */
    public static Type[] getTypeArguments(final Type type) {
        if (null == type) {
            return null;
        }
        final ParameterizedType parameterizedType = toParameterizedType(type);
        return (null == parameterizedType) ? null : parameterizedType.getActualTypeArguments();
    }

    /**
     * 将 {@link Type} 转换为 {@link ParameterizedType}.
     *
     * @param type {@link Type}
     * @return {@link ParameterizedType}
     */
    public static ParameterizedType toParameterizedType(final Type type) {
        return toParameterizedType(type, 0);
    }

    /**
     * 将 {@link Type} 转换为 {@link ParameterizedType}.
     *
     * @param type           {@link Type}
     * @param interfaceIndex 实现的第几个接口
     * @return {@link ParameterizedType}
     */
    public static ParameterizedType toParameterizedType(final Type type, final int interfaceIndex) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        }
        if (type instanceof Class) {
            final ParameterizedType[] generics = getGenerics((Class<?>) type);
            if (generics.length > interfaceIndex) {
                return generics[interfaceIndex];
            }
        }
        return null;
    }

    /**
     * 获取指定类所有泛型父类和泛型接口.
     *
     * @param clazz 类
     * @return 泛型父类或接口数组
     */
    public static ParameterizedType[] getGenerics(final Class<?> clazz) {
        final List<ParameterizedType> result = new ArrayList<>();

        // 泛型父类
        final Type genericSuper = clazz.getGenericSuperclass();
        if (null != genericSuper && !Object.class.equals(genericSuper)) {
            final ParameterizedType parameterizedType = toParameterizedType(genericSuper);
            if (null != parameterizedType) {
                result.add(parameterizedType);
            }
        }

        // 泛型接口
        final Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ArrayUtil.isNotEmpty(genericInterfaces)) {
            for (final Type genericInterface : genericInterfaces) {
                final ParameterizedType parameterizedType = toParameterizedType(genericInterface);
                if (null != parameterizedType) {
                    result.add(parameterizedType);
                }
            }
        }
        return result.toArray(new ParameterizedType[0]);
    }

    /**
     * 是否未知类型.
     *
     * @param type Type 类型
     * @return 是否未知类型
     */
    public static boolean isUnknown(final Type type) {
        return null == type || type instanceof TypeVariable;
    }
}
