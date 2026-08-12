package cc.hongz.ztools.core.map;

import cc.hongz.ztools.core.convert.Convert;
import cc.hongz.ztools.core.getter.OptNullBasicTypeFromObjectGetter;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.BooleanUtil;
import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map代理，提供各种getXXX方法，并提供默认值支持.
 *
 * @author looly
 * @since 3.2.0
 */
public class MapProxy implements Map<Object, Object>,
        OptNullBasicTypeFromObjectGetter<Object>,
        InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 被代理的Map.
     */
    @SuppressWarnings("rawtypes")
    Map map;

    /**
     * 创建代理Map.<br>
     * 此类对Map做一次包装，提供各种getXXX方法.
     *
     * @param map 被代理的Map
     * @return {@link MapProxy}
     */
    public static MapProxy create(final Map<?, ?> map) {
        return (map instanceof MapProxy) ? (MapProxy) map
                : new MapProxy(map);
    }

    /**
     * 构造.
     *
     * @param map 被代理的Map
     */
    public MapProxy(final Map<?, ?> map) {
        this.map = map;
    }

    @Override
    public Object getObj(final Object key, final Object defaultValue) {
        final Object value = map.get(key);
        return null != value ? value : defaultValue;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(final Object key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(final Object key, final Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(final Object key) {
        return map.remove(key);
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public void putAll(final Map<?, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public Set<Object> keySet() {
        return map.keySet();
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (ArrayUtil.isEmpty(parameterTypes)) {
            final Class<?> returnType = method.getReturnType();
            if (void.class != returnType) {
                final String methodName = method.getName();
                String fieldName = null;
                if (methodName.startsWith("get")) {
                    fieldName = StrUtil.removePreAndLowerFirst(
                            methodName, 3);
                } else if (BooleanUtil.isBoolean(returnType)
                        && methodName.startsWith("is")) {
                    fieldName = StrUtil.removePreAndLowerFirst(
                            methodName, 2);
                } else if ("hashCode".equals(methodName)) {
                    return this.hashCode();
                } else if ("toString".equals(methodName)) {
                    return this.toString();
                }

                if (StrUtil.isNotBlank(fieldName)) {
                    if (false == this.containsKey(fieldName)) {
                        fieldName = StrUtil.toUnderlineCase(fieldName);
                    }
                    return Convert.convert(method.getGenericReturnType(),
                            this.get(fieldName));
                }
            }

        } else if (1 == parameterTypes.length) {
            final String methodName = method.getName();
            if (methodName.startsWith("set")) {
                final String fieldName = StrUtil.removePreAndLowerFirst(
                        methodName, 3);
                if (StrUtil.isNotBlank(fieldName)) {
                    this.put(fieldName, args[0]);
                    final Class<?> returnType = method.getReturnType();
                    if (returnType.isInstance(proxy)) {
                        return proxy;
                    }
                }
            } else if ("equals".equals(methodName)) {
                return this.equals(args[0]);
            }
        }

        throw new UnsupportedOperationException(
                method.toGenericString());
    }

    /**
     * 将Map代理为指定接口的动态代理对象.
     *
     * @param <T> 代理的Bean类型
     * @param interfaceClass 接口
     * @return 代理对象
     * @since 4.5.2
     */
    @SuppressWarnings("unchecked")
    public <T> T toProxyBean(final Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(ClassLoaderUtil.getClassLoader(),
                new Class<?>[]{interfaceClass}, this);
    }
}
