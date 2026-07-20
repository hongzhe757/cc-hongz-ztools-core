package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.InvocationTargetRuntimeException;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.lang.NullWrapperBean;
import cc.hongz.ztools.core.text.Convert;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 反射工具类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.ReflectUtil}。
 * <br>
 * 聚焦 Method 查找与调用、构造方法查找与实例化，不含缓存机制。
 *
 * @author Looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 * @since 3.0.9
 */
public final class ReflectUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private ReflectUtil() {
    }

    // --------------------------------------------------------------------------------------------------------- Constructor

    /**
     * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为 true.
     *
     * @param <T>            对象类型
     * @param clazz          类
     * @param parameterTypes 参数类型
     * @return 构造方法，如果未找到返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
        if (null == clazz) {
            return null;
        }
        final Constructor<?>[] constructors = getConstructors(clazz);
        for (Constructor<?> constructor : constructors) {
            final Class<?>[] pts = constructor.getParameterTypes();
            if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
                setAccessible(constructor);
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    /**
     * 获得一个类中所有构造列表.
     *
     * @param <T>       构造的对象类型
     * @param beanClass 类，非 {@code null}
     * @return 构造列表
     * @throws SecurityException 安全检查异常
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructors(final Class<T> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        return (Constructor<T>[]) getConstructorsDirectly(beanClass);
    }

    /**
     * 获得一个类中所有构造列表，直接反射获取，无缓存.
     *
     * @param beanClass 类
     * @return 构造列表
     * @throws SecurityException 安全检查异常
     */
    public static Constructor<?>[] getConstructorsDirectly(final Class<?> beanClass) throws SecurityException {
        return beanClass.getDeclaredConstructors();
    }

    // --------------------------------------------------------------------------------------------------------- Field

    /**
     * 查找指定类中是否包含指定名称对应的字段，包括所有字段（包括非 public 字段），也包括父类和 Object 类的字段.
     *
     * @param beanClass 被查找字段的类，不能为 null
     * @param name      字段名
     * @return 是否包含字段
     * @throws SecurityException 安全异常
     */
    public static boolean hasField(final Class<?> beanClass, final String name) throws SecurityException {
        return null != getField(beanClass, name);
    }

    /**
     * 获取字段名.
     *
     * @param field 字段
     * @return 字段名
     */
    public static String getFieldName(final Field field) {
        if (null == field) {
            return null;
        }
        return field.getName();
    }

    /**
     * 获取指定类中字段名和字段对应的有序 Map，包括其父类中的字段.
     * 如果子类与父类中存在同名字段，则后者覆盖前者。
     *
     * @param beanClass 类
     * @return 字段名和字段对应的 Map，有序
     */
    public static Map<String, Field> getFieldMap(final Class<?> beanClass) {
        final Field[] fields = getFields(beanClass);
        final LinkedHashMap<String, Field> map = new LinkedHashMap<>(fields.length);
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        return map;
    }

    // --------------------------------------------------------------------------------------------------------- Method - Public

    /**
     * 获得指定类本类及其父类中的 Public 方法名，去重重载的方法.
     *
     * @param clazz 类
     * @return 方法名 Set
     */
    public static Set<String> getPublicMethodNames(final Class<?> clazz) {
        final HashSet<String> methodSet = new HashSet<>();
        final Method[] methodArray = getPublicMethods(clazz);
        if (ArrayUtil.isNotEmpty(methodArray)) {
            for (Method method : methodArray) {
                methodSet.add(method.getName());
            }
        }
        return methodSet;
    }

    /**
     * 获得本类及其父类所有 Public 方法.
     *
     * @param clazz 查找方法的类
     * @return 方法数组
     */
    public static Method[] getPublicMethods(final Class<?> clazz) {
        return null == clazz ? null : clazz.getMethods();
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz  查找方法的类
     * @param filter 过滤器
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final Filter<Method> filter) {
        if (null == clazz) {
            return null;
        }
        final Method[] methods = getPublicMethods(clazz);
        final List<Method> methodList = new ArrayList<>();
        if (null != filter) {
            for (Method method : methods) {
                if (filter.accept(method)) {
                    methodList.add(method);
                }
            }
        } else {
            methodList.addAll(Arrays.asList(methods));
        }
        return methodList;
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz          查找方法的类
     * @param excludeMethods 不包括的方法
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final Method... excludeMethods) {
        final HashSet<Method> excludeMethodSet = new HashSet<>(Arrays.asList(excludeMethods));
        return getPublicMethods(clazz, method -> !excludeMethodSet.contains(method));
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz              查找方法的类
     * @param excludeMethodNames 不包括的方法名列表
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final String... excludeMethodNames) {
        final HashSet<String> excludeMethodNameSet = new HashSet<>(Arrays.asList(excludeMethodNames));
        return getPublicMethods(clazz, method -> !excludeMethodNameSet.contains(method.getName()));
    }

    /**
     * 查找指定 Public 方法，如果找不到对应的方法或方法不为 public 的则返回 {@code null}.
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getPublicMethod(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    // --------------------------------------------------------------------------------------------------------- Method - Declared

    /**
     * 获得指定类中的方法名，去重重载的方法.
     *
     * @param clazz 类
     * @return 方法名 Set
     * @throws SecurityException 安全异常
     */
    public static Set<String> getMethodNames(final Class<?> clazz) throws SecurityException {
        final HashSet<String> methodSet = new HashSet<>();
        final Method[] methods = getMethods(clazz);
        for (Method method : methods) {
            methodSet.add(method.getName());
        }
        return methodSet;
    }

    /**
     * 获得一个类中所有方法列表，包括其父类中的方法.
     *
     * @param beanClass 类，非 {@code null}
     * @return 方法列表
     * @throws SecurityException 安全检查异常
     */
    public static Method[] getMethods(final Class<?> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        return getMethodsDirectly(beanClass, true, true);
    }

    /**
     * 获得一个类中所有方法列表，直接反射获取，无缓存.
     *
     * @param beanClass            类或接口
     * @param withSupers           是否包括父类或接口的方法列表
     * @param withMethodFromObject 是否包括 Object 中的方法
     * @return 方法列表
     * @throws SecurityException 安全检查异常
     */
    public static Method[] getMethodsDirectly(final Class<?> beanClass, final boolean withSupers, final boolean withMethodFromObject) throws SecurityException {
        Assert.notNull(beanClass);

        if (beanClass.isInterface()) {
            return withSupers ? beanClass.getMethods() : beanClass.getDeclaredMethods();
        }

        final LinkedHashSet<String> seenKeys = new LinkedHashSet<>();
        final List<Method> result = new ArrayList<>();
        Class<?> searchType = beanClass;
        while (searchType != null) {
            if (!withMethodFromObject && Object.class == searchType) {
                break;
            }
            final Method[] declaredMethods = searchType.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final String key = getUniqueKey(method);
                if (seenKeys.add(key)) {
                    result.add(method);
                }
            }
            // 获取接口的 default 方法
            for (Class<?> ifc : searchType.getInterfaces()) {
                for (Method m : ifc.getMethods()) {
                    if (!Modifier.isAbstract(m.getModifiers())) {
                        final String key = getUniqueKey(m);
                        if (seenKeys.add(key)) {
                            result.add(m);
                        }
                    }
                }
            }
            searchType = (withSupers && !searchType.isInterface()) ? searchType.getSuperclass() : null;
        }
        return result.toArray(new Method[0]);
    }

    /**
     * 查找指定方法，如果找不到对应的方法则返回 {@code null}.
     * <p>此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回 {@code null}。
     *
     * @param clazz      类，如果为 {@code null} 返回 {@code null}
     * @param methodName 方法名，如果为空字符串返回 {@code null}
     * @param paramTypes 参数类型
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
        return getMethod(clazz, false, methodName, paramTypes);
    }

    /**
     * 查找指定方法，如果找不到对应的方法则返回 {@code null}.
     * <p>如果查找的方法有多个同参数类型重载，查找第一个找到的方法。
     *
     * @param clazz      类
     * @param ignoreCase 是否忽略大小写
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethod(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>... paramTypes) throws SecurityException {
        if (null == clazz || StrUtil.isBlank(methodName)) {
            return null;
        }
        Method res = null;
        final Method[] methods = getMethods(clazz);
        if (ArrayUtil.isNotEmpty(methods)) {
            for (Method method : methods) {
                if (StrUtil.equals(methodName, method.getName(), ignoreCase)
                        && ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
                        // 排除协变桥接方法
                        && (res == null || res.getReturnType().isAssignableFrom(method.getReturnType()))) {
                    res = method;
                }
            }
        }
        return res;
    }

    /**
     * 查找指定对象中的所有方法（包括非 public 方法），也包括父对象和 Object 类的方法.
     *
     * @param obj        被查找的对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法
     * @throws SecurityException 无访问权限抛出异常
     */
    public static Method getMethodOfObj(final Object obj, final String methodName, final Object... args) throws SecurityException {
        if (null == obj || StrUtil.isBlank(methodName)) {
            return null;
        }
        return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
    }

    /**
     * 忽略大小写查找指定方法，如果找不到对应的方法则返回 {@code null}.
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethodIgnoreCase(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
        return getMethod(clazz, true, methodName, paramTypes);
    }

    /**
     * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法.
     * 此方法只检查方法名是否一致，并不检查参数的一致性。
     *
     * @param clazz      类
     * @param methodName 方法名
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethodByName(final Class<?> clazz, final String methodName) throws SecurityException {
        return getMethodByName(clazz, false, methodName);
    }

    /**
     * 按照方法名查找指定方法名的方法（忽略大小写），只返回匹配到的第一个方法.
     *
     * @param clazz      类
     * @param methodName 方法名
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethodByNameIgnoreCase(final Class<?> clazz, final String methodName) throws SecurityException {
        return getMethodByName(clazz, true, methodName);
    }

    /**
     * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法.
     *
     * @param clazz      类
     * @param ignoreCase 是否忽略大小写
     * @param methodName 方法名
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getMethodByName(final Class<?> clazz, final boolean ignoreCase, final String methodName) throws SecurityException {
        if (null == clazz || StrUtil.isBlank(methodName)) {
            return null;
        }
        Method res = null;
        final Method[] methods = getMethods(clazz);
        if (ArrayUtil.isNotEmpty(methods)) {
            for (Method method : methods) {
                if (StrUtil.equals(methodName, method.getName(), ignoreCase)
                        && (res == null || res.getReturnType().isAssignableFrom(method.getReturnType()))) {
                    res = method;
                }
            }
        }
        return res;
    }

    /**
     * 获得指定类过滤后的方法列表.
     *
     * @param clazz  查找方法的类
     * @param filter 过滤器
     * @return 过滤后的方法列表
     * @throws SecurityException 安全异常
     */
    public static Method[] getMethods(final Class<?> clazz, final Filter<Method> filter) throws SecurityException {
        if (null == clazz) {
            return null;
        }
        return ArrayUtil.filter(getMethods(clazz), filter);
    }

    /**
     * 是否为 equals 方法.
     *
     * @param method 方法
     * @return 是否为 equals 方法
     */
    public static boolean isEqualsMethod(final Method method) {
        if (method == null || 1 != method.getParameterCount() || !"equals".equals(method.getName())) {
            return false;
        }
        return method.getParameterTypes()[0] == Object.class;
    }

    /**
     * 是否为 hashCode 方法.
     *
     * @param method 方法
     * @return 是否为 hashCode 方法
     */
    public static boolean isHashCodeMethod(final Method method) {
        return method != null && "hashCode".equals(method.getName()) && isEmptyParam(method);
    }

    /**
     * 是否为 toString 方法.
     *
     * @param method 方法
     * @return 是否为 toString 方法
     */
    public static boolean isToStringMethod(final Method method) {
        return method != null && "toString".equals(method.getName()) && isEmptyParam(method);
    }

    /**
     * 是否为无参数方法.
     *
     * @param method 方法
     * @return 是否为无参数方法
     */
    public static boolean isEmptyParam(final Method method) {
        return method.getParameterCount() == 0;
    }

    /**
     * 检查给定方法是否为 Getter 或者 Setter 方法.
     *
     * @param method     方法
     * @param ignoreCase 是否忽略方法名的大小写
     * @return 是否为 Getter 或者 Setter 方法
     */
    public static boolean isGetterOrSetter(final Method method, final boolean ignoreCase) {
        if (null == method) {
            return false;
        }
        final int parameterCount = method.getParameterCount();
        if (parameterCount > 1) {
            return false;
        }
        String name = method.getName();
        if ("getClass".equals(name)) {
            return false;
        }
        if (ignoreCase) {
            name = name.toLowerCase();
        }
        switch (parameterCount) {
            case 0:
                return name.startsWith("get") || name.startsWith("is");
            case 1:
                return name.startsWith("set");
            default:
                return false;
        }
    }

    /**
     * 检查给定方法是否为 Getter 或者 Setter 方法（忽略大小写）.
     *
     * @param method 方法
     * @return 是否为 Getter 或者 Setter 方法
     */
    public static boolean isGetterOrSetterIgnoreCase(final Method method) {
        return isGetterOrSetter(method, true);
    }

    // --------------------------------------------------------------------------------------------------------- newInstance

    /**
     * 实例化对象.
     *
     * @param <T>   对象类型
     * @param clazz 类
     * @return 对象
     * @throws UtilException 包装各类异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String clazz) throws UtilException {
        try {
            return (T) Class.forName(clazz).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new UtilException(e, "Instance class [{}] error!", clazz);
        }
    }

    /**
     * 实例化对象.
     *
     * @param <T>    对象类型
     * @param clazz  类
     * @param params 构造函数参数
     * @return 对象
     * @throws UtilException 包装各类异常
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... params) throws UtilException {
        if (ArrayUtil.isEmpty(params)) {
            try {
                final Constructor<T> constructor = getConstructor(clazz);
                if (null == constructor) {
                    throw new UtilException("No constructor for [{}]", clazz);
                }
                return constructor.newInstance();
            } catch (Exception e) {
                throw new UtilException(e, "Instance class [{}] error!", clazz);
            }
        }

        final Class<?>[] paramTypes = ClassUtil.getClasses(params);
        final Constructor<T> constructor = getConstructor(clazz, paramTypes);
        if (null == constructor) {
            throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[]{paramTypes});
        }
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new UtilException(e, "Instance class [{}] error!", clazz);
        }
    }

    /**
     * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回.
     * 对于某些特殊的接口，按照其默认实现实例化.
     *
     * @param <T>  对象类型
     * @param type 被构造的类
     * @return 构造后的对象，构造失败返回 {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceIfPossible(Class<T> type) {
        Assert.notNull(type);

        // 原始类型
        if (type.isPrimitive()) {
            return (T) ClassUtil.getPrimitiveDefaultValue(type);
        }

        Class<?> actualType = type;
        if (Object.class != actualType) {
            if (actualType.isAssignableFrom(AbstractMap.class)) {
                actualType = HashMap.class;
            } else if (actualType.isAssignableFrom(List.class)) {
                actualType = ArrayList.class;
            } else if (actualType.isAssignableFrom(Set.class)) {
                actualType = HashSet.class;
            } else if (actualType.isAssignableFrom(Queue.class)) {
                actualType = java.util.LinkedList.class;
            } else if (actualType.isAssignableFrom(Deque.class)) {
                actualType = ArrayDeque.class;
            }
        }

        try {
            return (T) newInstance(actualType);
        } catch (Exception e) {
            // ignore，默认构造不存在的情况下查找其它构造
        }

        // 枚举
        if (actualType.isEnum()) {
            return (T) actualType.getEnumConstants()[0];
        }

        // 数组
        if (actualType.isArray()) {
            return (T) java.lang.reflect.Array.newInstance(actualType.getComponentType(), 0);
        }

        final Constructor<?>[] constructors = getConstructors(actualType);
        Class<?>[] parameterTypes;
        for (Constructor<?> constructor : constructors) {
            parameterTypes = constructor.getParameterTypes();
            if (0 == parameterTypes.length) {
                continue;
            }
            setAccessible(constructor);
            try {
                return (T) constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
            } catch (Exception ignore) {
                // 构造出错时继续尝试下一种构造方式
            }
        }
        return null;
    }

    /**
     * 执行静态方法.
     *
     * @param <T>    对象类型
     * @param method 方法
     * @param args   参数对象
     * @return 结果
     * @throws UtilException 多种异常包装
     */
    public static <T> T invokeStatic(final Method method, final Object... args) throws UtilException {
        return invoke(null, method, args);
    }

    /**
     * 执行方法，执行前要检查给定参数.
     *
     * @param <T>    返回对象类型
     * @param obj    对象，如果执行静态方法，此值为 {@code null}
     * @param method 方法
     * @param args   参数对象
     * @return 结果
     * @throws UtilException 一些列异常的包装
     */
    public static <T> T invokeWithCheck(final Object obj, final Method method, final Object... args) throws UtilException {
        final Class<?>[] types = method.getParameterTypes();
        if (null != args) {
            Assert.isTrue(args.length == types.length, "Params length [{}] is not fit for param length [{}] of method !", args.length, types.length);
            Class<?> type;
            for (int i = 0; i < args.length; i++) {
                type = types[i];
                if (type.isPrimitive() && null == args[i]) {
                    args[i] = ClassUtil.getDefaultValue(type);
                }
            }
        }
        return invoke(obj, method, args);
    }

    /**
     * 执行方法，原始方法，不包装异常.
     *
     * @param <T>    返回对象类型
     * @param obj    对象
     * @param method 方法
     * @param args   参数
     * @return 结果
     * @throws InvocationTargetException 目标方法执行异常
     * @throws IllegalAccessException    访问异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeRaw(final Object obj, final Method method, final Object... args) throws InvocationTargetException, IllegalAccessException {
        setAccessible(method);

        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] actualArgs = new Object[parameterTypes.length];
        if (null != args) {
            for (int i = 0; i < actualArgs.length; i++) {
                if (i >= args.length || null == args[i]) {
                    actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
                } else if (args[i] instanceof NullWrapperBean) {
                    actualArgs[i] = null;
                } else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                    try {
                        final Object targetValue = Convert.convert(parameterTypes[i], args[i]);
                        actualArgs[i] = (null != targetValue) ? targetValue : args[i];
                    } catch (Exception e) {
                        actualArgs[i] = parseBasicType(parameterTypes[i], args[i]);
                    }
                } else {
                    actualArgs[i] = args[i];
                }
            }
        }

        return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
    }

    /**
     * 执行方法.
     *
     * @param <T>    返回对象类型
     * @param obj    对象，如果执行静态方法，此值为 {@code null}
     * @param method 方法
     * @param args   参数对象
     * @return 结果
     * @throws InvocationTargetRuntimeException 目标方法执行异常
     * @throws UtilException                    {@link IllegalAccessException} 异常的包装
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Object obj, final Method method, final Object... args) throws InvocationTargetRuntimeException, UtilException {
        setAccessible(method);

        // 检查用户传入参数：
        // 1、忽略多余的参数
        // 2、参数不够补齐默认值
        // 3、通过 NullWrapperBean 传递的参数，直接赋值 null
        // 4、传入参数为 null，但是目标参数类型为原始类型，做转换
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] actualArgs = new Object[parameterTypes.length];
        if (null != args) {
            for (int i = 0; i < actualArgs.length; i++) {
                if (i >= args.length || null == args[i]) {
                    actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
                } else if (args[i] instanceof NullWrapperBean) {
                    actualArgs[i] = null;
                } else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                    actualArgs[i] = args[i];
                } else {
                    actualArgs[i] = args[i];
                }
            }
        }

        try {
            return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 执行对象中指定方法.
     *
     * @param <T>        返回对象类型
     * @param obj        方法所在对象
     * @param methodName 方法名
     * @param args       参数列表
     * @return 执行结果
     * @throws UtilException IllegalAccessException 等异常包装
     */
    public static <T> T invoke(final Object obj, final String methodName, final Object... args) throws UtilException {
        Assert.notNull(obj, "Object to get method must be not null!");
        Assert.notBlank(methodName, "Method name must be not blank!");

        final Method method = getMethodOfObj(obj, methodName, args);
        if (null == method) {
            throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
        }
        return invoke(obj, method, args);
    }

    // --------------------------------------------------------------------------------------------------------- setAccessible

    /**
     * 设置方法为可访问（私有方法可以被外部调用）.
     *
     * @param <T>              AccessibleObject 的子类
     * @param accessibleObject 可设置访问权限的对象
     * @return 被设置可访问的对象
     */
    public static <T extends AccessibleObject> T setAccessible(final T accessibleObject) {
        if (null != accessibleObject && !accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }

    // --------------------------------------------------------------------------------------------------------- Field

    /**
     * 获得一个类中所有字段列表，包括其父类中的字段.
     * <br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
     *
     * @param beanClass 类
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static Field[] getFields(final Class<?> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        return getFieldsDirectly(beanClass, true);
    }

    /**
     * 获得一个类中所有满足条件的字段列表，包括其父类中的字段.
     *
     * @param beanClass   类
     * @param fieldFilter field 过滤器，过滤掉不需要的 field，{@code null} 返回原集合
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static Field[] getFields(final Class<?> beanClass, final Filter<Field> fieldFilter) throws SecurityException {
        return ArrayUtil.filter(getFields(beanClass), fieldFilter);
    }

    /**
     * 获得一个类中所有字段列表，直接反射获取，无缓存.
     * <br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
     *
     * @param beanClass            类
     * @param withSuperClassFields 是否包括父类的字段列表
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public static Field[] getFieldsDirectly(final Class<?> beanClass, final boolean withSuperClassFields) throws SecurityException {
        Assert.notNull(beanClass);

        Field[] allFields = null;
        Class<?> searchType = beanClass;
        Field[] declaredFields;
        while (searchType != null) {
            declaredFields = searchType.getDeclaredFields();
            if (null == allFields) {
                allFields = declaredFields;
            } else {
                allFields = ArrayUtil.append(allFields, declaredFields);
            }
            searchType = withSuperClassFields ? searchType.getSuperclass() : null;
        }

        return allFields;
    }

    /**
     * 查找指定类中的指定 name 的字段（包括非 public 字段），也包括父类和 Object 类的字段，字段不存在则返回 {@code null}.
     *
     * @param beanClass 被查找字段的类，不能为 null
     * @param name      字段名
     * @return 字段
     * @throws SecurityException 安全异常
     */
    public static Field getField(final Class<?> beanClass, final String name) throws SecurityException {
        final Field[] fields = getFields(beanClass);
        return ArrayUtil.firstMatch((field) -> name.equals(getFieldName(field)), fields);
    }

    /**
     * 获取字段值.
     *
     * @param obj       对象，如果 static 字段，此处为类
     * @param fieldName 字段名
     * @return 字段值
     * @throws UtilException 包装 IllegalAccessException 异常
     */
    public static Object getFieldValue(final Object obj, final String fieldName) throws UtilException {
        if (null == obj || StrUtil.isBlank(fieldName)) {
            return null;
        }
        return getFieldValue(obj, getField(obj instanceof Class ? (Class<?>) obj : obj.getClass(), fieldName));
    }

    /**
     * 获取字段值.
     *
     * @param obj   对象，static 字段则此字段为 null
     * @param field 字段
     * @return 字段值
     * @throws UtilException 包装 IllegalAccessException 异常
     */
    public static Object getFieldValue(final Object obj, final Field field) throws UtilException {
        if (null == field) {
            return null;
        }
        Object target = obj;
        if (obj instanceof Class) {
            // 静态字段获取时对象为 null
            target = null;
        }

        setAccessible(field);
        Object result;
        try {
            result = field.get(target);
        } catch (IllegalAccessException e) {
            throw new UtilException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
        }
        return result;
    }

    /**
     * 获取静态字段值.
     *
     * @param field 字段
     * @return 字段值
     * @throws UtilException 包装 IllegalAccessException 异常
     */
    public static Object getStaticFieldValue(final Field field) throws UtilException {
        return getFieldValue(null, field);
    }

    /**
     * 获取所有字段的值.
     *
     * @param obj bean 对象，如果是 static 字段，此处为类 class
     * @return 字段值数组
     */
    public static Object[] getFieldsValue(final Object obj) {
        return getFieldsValue(obj, null);
    }

    /**
     * 获取所有字段的值.
     *
     * @param obj    bean 对象，如果是 static 字段，此处为类 class
     * @param filter 字段过滤器，{@code null} 返回原集合
     * @return 字段值数组
     */
    public static Object[] getFieldsValue(final Object obj, final Filter<Field> filter) {
        if (null != obj) {
            final Field[] fields = getFields(obj instanceof Class ? (Class<?>) obj : obj.getClass(), filter);
            if (null != fields) {
                final Object[] result = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    result[i] = getFieldValue(obj, fields[i]);
                }
                return result;
            }
        }
        return null;
    }

    /**
     * 设置字段值.
     * 若值类型与字段类型不一致，则会尝试通过 {@link Convert} 进行转换.
     * 若字段类型是原始类型而传入的值是 null，则会将字段设置为对应原始类型的默认值.
     *
     * @param obj       对象，static 字段则此处传 Class
     * @param fieldName 字段名
     * @param value     值
     * @throws UtilException 包装 IllegalAccessException 异常
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws UtilException {
        Assert.notNull(obj);
        Assert.notBlank(fieldName);

        final Field field = getField((obj instanceof Class) ? (Class<?>) obj : obj.getClass(), fieldName);
        Assert.notNull(field, "Field [{}] is not exist in [{}]", fieldName, obj.getClass().getName());
        setFieldValue(obj, field, value);
    }

    /**
     * 设置字段值.
     * 若值类型与字段类型不一致，则会尝试通过 {@link Convert} 进行转换.
     * 若字段类型是原始类型而传入的值是 null，则会将字段设置为对应原始类型的默认值.
     *
     * @param obj   对象，如果是 static 字段，此参数为 null
     * @param field 字段
     * @param value 值
     * @throws UtilException 包装 IllegalAccessException 异常
     */
    public static void setFieldValue(final Object obj, final Field field, final Object value) throws UtilException {
        Assert.notNull(field, "Field in [{}] not exist !", obj);

        final Class<?> fieldType = field.getType();
        Object actualValue = value;
        if (null != actualValue) {
            if (!fieldType.isAssignableFrom(actualValue.getClass())) {
                try {
                    final Object targetValue = Convert.convert(fieldType, actualValue);
                    if (null != targetValue) {
                        actualValue = targetValue;
                    }
                } catch (Exception ignored) {
                    // Convert 不支持的转换（如 String→Number），尝试基本解析
                    actualValue = parseBasicType(fieldType, actualValue);
                }
            }
        } else {
            actualValue = ClassUtil.getDefaultValue(fieldType);
        }

        setAccessible(field);
        try {
            field.set(obj instanceof Class ? null : obj, actualValue);
        } catch (IllegalAccessException e) {
            throw new UtilException(e, "IllegalAccess for {}.{}", obj, field.getName());
        }
    }

    /**
     * 是否为父类引用字段.
     * 当字段所在类是对象子类时，会自动生成一个以 "this$0" 为名称的字段，指向父类对象.
     *
     * @param field 字段
     * @return 是否为父类引用字段
     */
    public static boolean isOuterClassField(final Field field) {
        return "this$0".equals(field.getName());
    }

    /**
     * 去除字段的 final 修饰符.
     *
     * <p>移除后可以通过反射修改 final 字段的值。
     * 注意：此方法仅对非 JVM 内部管理的 final 字段有效。
     *
     * @param field 被修改的 field，不可以为空
     * @throws UtilException IllegalAccessException 等异常包装
     * @since 5.8.8
     */
    public static void removeFinalModify(final Field field) {
        if (field == null) {
            return;
        }
        if ((field.getModifiers() & Modifier.FINAL) != 0) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            } catch (final NoSuchFieldException | IllegalAccessException e) {
                throw new UtilException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
            }
        }
    }

    // --------------------------------------------------------------------------------------------------------- Private

    /**
     * 获取方法的唯一键.
     * <pre>
     *     返回类型#方法名:参数1类型,参数2类型...
     * </pre>
     *
     * @param method 方法
     * @return 方法唯一键
     */
    private static String getUniqueKey(final Method method) {
        final StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getName()).append('#');
        sb.append(method.getName());
        final Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            sb.append(i == 0 ? ':' : ',');
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    /**
     * 基本类型解析，用于 Convert.convert 不支持的转换场景（如 String→Number）.
     *
     * @param targetType 目标类型
     * @param value      值
     * @return 解析后的值，解析失败返回原值
     */
    private static Object parseBasicType(final Class<?> targetType, final Object value) {
        if (!(value instanceof String)) {
            return value;
        }
        final String str = (String) value;
        try {
            if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(str);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(str);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(str);
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(str);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(str);
            } else if (targetType == short.class || targetType == Short.class) {
                return Short.parseShort(str);
            } else if (targetType == byte.class || targetType == Byte.class) {
                return Byte.parseByte(str);
            }
        } catch (NumberFormatException ignored) {
            // 解析失败返回原值
        }
        return value;
    }
}
