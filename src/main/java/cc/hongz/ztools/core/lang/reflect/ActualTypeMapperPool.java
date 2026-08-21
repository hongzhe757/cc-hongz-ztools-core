package cc.hongz.ztools.core.lang.reflect;

import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 泛型变量和泛型实际类型映射关系缓存.
 *
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.reflect.ActualTypeMapperPool}。
 *
 * @author Looly
 * @since 5.4.2
 */
public final class ActualTypeMapperPool {

    private static final ConcurrentHashMap<Type, Map<Type, Type>> CACHE = new ConcurrentHashMap<>();

    /**
     * 私有构造，禁止实例化.
     */
    private ActualTypeMapperPool() {
    }

    /**
     * 获取泛型变量和泛型实际类型的对应关系Map.
     *
     * @param type 被解析的包含泛型参数的类
     * @return 泛型对应关系Map
     */
    public static Map<Type, Type> get(final Type type) {
        return CACHE.computeIfAbsent(type, (key) -> createTypeMap(type));
    }

    /**
     * 获取泛型变量名（字符串）和泛型实际类型的对应关系Map.
     *
     * @param type 被解析的包含泛型参数的类
     * @return 泛型对应关系Map
     * @since 5.7.16
     */
    public static Map<String, Type> getStrKeyMap(final Type type) {
        final Map<Type, Type> typeMap = get(type);
        final Map<String, Type> result = new HashMap<>(typeMap.size());
        for (Map.Entry<Type, Type> entry : typeMap.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    /**
     * 获得泛型变量对应的泛型实际类型，如果此变量没有对应的实际类型，返回null.
     *
     * @param type         类
     * @param typeVariable 泛型变量，例如T等
     * @return 实际类型，可能为Class等
     */
    public static Type getActualType(final Type type, final TypeVariable<?> typeVariable) {
        final Map<Type, Type> typeTypeMap = get(type);
        Type result = typeTypeMap.get(typeVariable);
        while (result instanceof TypeVariable) {
            result = typeTypeMap.get(result);
        }
        return result;
    }

    /**
     * 获得泛型变量对应的泛型实际类型，如果此变量没有对应的实际类型，返回null.
     *
     * @param type             类
     * @param genericArrayType 泛型数组类型
     * @return 实际类型，可能为Class等
     * @since 5.8.37
     */
    public static Type getActualType(final Type type, final GenericArrayType genericArrayType) {
        final Map<Type, Type> typeTypeMap = get(type);
        Type actualType = typeTypeMap.get(genericArrayType);

        if (actualType == null) {
            Type componentType = typeTypeMap.get(genericArrayType.getGenericComponentType());
            if (!(componentType instanceof Class<?>)) {
                return null;
            }
            actualType = ArrayUtil.getArrayType((Class<?>) componentType);
            typeTypeMap.put(genericArrayType, actualType);
        }

        return actualType;
    }

    /**
     * 获取指定泛型变量对应的真实类型.
     * <p>
     * 由于子类中泛型参数实现和父类（接口）中泛型定义位置是一一对应的，
     * 因此可以通过对应关系找到泛型实现类型。
     *
     * @param type          真实类型所在类，此类中记录了泛型参数对应的实际类型
     * @param typeVariables 泛型变量，需要的实际类型对应的泛型参数
     * @return 给定泛型参数对应的实际类型，如果无对应类型，对应位置返回null
     */
    public static Type[] getActualTypes(final Type type, final Type... typeVariables) {
        // 查找方法定义所在类或接口中此泛型参数的位置
        final Type[] result = new Type[typeVariables.length];
        for (int i = 0; i < typeVariables.length; i++) {
            result[i] = (typeVariables[i] instanceof TypeVariable)
                ? getActualType(type, (TypeVariable<?>) typeVariables[i])
                : typeVariables[i];
        }
        return result;
    }

    /**
     * 创建类中所有的泛型变量和泛型实际类型的对应关系Map.
     *
     * @param type 被解析的包含泛型参数的类
     * @return 泛型对应关系Map
     */
    private static Map<Type, Type> createTypeMap(final Type type) {
        final Map<Type, Type> typeMap = new HashMap<>();

        Type currentType = type;
        // 按继承层级寻找泛型变量和实际类型的对应关系
        while (null != currentType) {
            final ParameterizedType parameterizedType = TypeUtil.toParameterizedType(currentType);
            if (null == parameterizedType) {
                break;
            }
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            final Type[] typeParameters = rawType.getTypeParameters();

            Type value;
            for (int i = 0; i < typeParameters.length; i++) {
                value = typeArguments[i];
                // 跳过泛型变量对应泛型变量的情况
                if (false == value instanceof TypeVariable) {
                    typeMap.put(typeParameters[i], value);
                }
            }

            currentType = rawType;
        }

        return typeMap;
    }
}
