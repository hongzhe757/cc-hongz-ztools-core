package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.ReflectUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bean 工具类（自 Hutool {@code cn.hutool.core.bean.BeanUtil} 迁入，精简版）.
 *
 * @author Looly
 */
public final class BeanUtil {

    private BeanUtil() {
    }

    /**
     * Map 转 Bean.
     *
     * @param <T>     Bean 类型
     * @param source  Map 或 Bean
     * @param clazz   目标 Bean 类
     * @param options 拷贝选项
     * @return Bean 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(final Object source,
            final Class<T> clazz, CopyOptions options) {
        if (null == source || null == clazz) {
            return null;
        }
        CopyOptions opts = options;
        if (null == opts) {
            opts = CopyOptions.create();
        }
        try {
            final T bean =
                    ReflectUtil.newInstanceIfPossible(clazz);
            if (source instanceof Map) {
                final Map<String, Object> map =
                        (Map<String, Object>) source;
                final BeanInfo beanInfo =
                        Introspector.getBeanInfo(clazz);
                final PropertyDescriptor[] pds =
                        beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor pd : pds) {
                    final String name = pd.getName();
                    if ("class".equals(name)) {
                        continue;
                    }
                    Object value = map.get(name);
                    if (opts.isIgnoreCase() && null == value) {
                        for (final Map.Entry<String, Object> e
                                : map.entrySet()) {
                            if (name.equalsIgnoreCase(
                                    e.getKey())) {
                                value = e.getValue();
                                break;
                            }
                        }
                    }
                    if (null == value
                            && opts.isIgnoreNullValue()) {
                        continue;
                    }
                    final Method setter = pd.getWriteMethod();
                    if (null != setter) {
                        try {
                            setter.invoke(bean, value);
                        } catch (final Exception e) {
                            if (!opts.isIgnoreError()) {
                                throw new UtilException(e,
                                    "Set bean property [{}]"
                                        + " error!",
                                    name);
                            }
                        }
                    }
                }
            }
            return bean;
        } catch (final UtilException e) {
            throw e;
        } catch (final Exception e) {
            throw new UtilException(
                    e, "Convert map to bean error!");
        }
    }

    /**
     * Bean 转 Map.
     *
     * @param bean              Bean 对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Map
     */
    public static Map<String, Object> beanToMap(final Object bean,
            final boolean isToUnderlineCase,
            final boolean ignoreNullValue) {
        if (null == bean) {
            return null;
        }
        return beanToMap(bean, new LinkedHashMap<>(),
                ignoreNullValue,
                key -> isToUnderlineCase
                        ? StrUtil.toUnderlineCase(key) : key);
    }

    /**
     * Bean 转 Map.
     *
     * @param bean            Bean 对象
     * @param targetMap       目标的 Map
     * @param ignoreNullValue 是否忽略值为空的字段
     * @param keyEditor       属性字段编辑器
     * @return Map
     */
    public static Map<String, Object> beanToMap(final Object bean,
            final Map<String, Object> targetMap,
            final boolean ignoreNullValue,
            final Editor<String> keyEditor) {
        if (null == bean) {
            return null;
        }
        try {
            final BeanInfo beanInfo =
                    Introspector.getBeanInfo(bean.getClass());
            final PropertyDescriptor[] pds =
                    beanInfo.getPropertyDescriptors();
            for (final PropertyDescriptor pd : pds) {
                final String name = pd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                final Method getter = pd.getReadMethod();
                if (null == getter) {
                    continue;
                }
                final Object value = getter.invoke(bean);
                if (null == value && ignoreNullValue) {
                    continue;
                }
                final String key;
                if (null != keyEditor) {
                    key = keyEditor.edit(name);
                } else {
                    key = name;
                }
                if (null != key) {
                    targetMap.put(key, value);
                }
            }
        } catch (final Exception e) {
            throw new UtilException(
                    e, "Convert bean to map error!");
        }
        return targetMap;
    }

    /**
     * 编辑器接口，用于自定义字段key转换.
     *
     * @param <T> 编辑对象类型
     */
    @FunctionalInterface
    public interface Editor<T> {
        /**
         * 编辑字段名.
         *
         * @param source 源字段名
         * @return 编辑后的字段名
         */
        T edit(T source);
    }
}
