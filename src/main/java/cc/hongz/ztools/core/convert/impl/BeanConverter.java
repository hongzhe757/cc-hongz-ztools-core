package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.bean.BeanUtil;
import cc.hongz.ztools.core.bean.copier.BeanCopier;
import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.bean.copier.ValueProvider;
import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.convert.ConvertException;
import cc.hongz.ztools.core.map.MapProxy;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean转换器，支持：
 * <pre>
 * Map =》 Bean
 * Bean =》 Bean
 * ValueProvider =》 Bean
 * </pre>
 *
 * @param <T> Bean类型
 * @author Looly
 * @since 4.0.2
 */
public class BeanConverter<T> extends AbstractConverter<T> {
    private static final long serialVersionUID = 1L;

    private final Type beanType;
    private final Class<T> beanClass;
    private final CopyOptions copyOptions;

    /**
     * 构造，默认转换选项，注入失败的字段忽略。
     *
     * @param beanType 转换成的目标Bean类型
     */
    public BeanConverter(final Type beanType) {
        this(beanType, CopyOptions.create().setIgnoreError(true));
    }

    /**
     * 构造，默认转换选项，注入失败的字段忽略。
     *
     * @param beanClass 转换成的目标Bean类
     */
    public BeanConverter(final Class<T> beanClass) {
        this(beanClass, CopyOptions.create().setIgnoreError(true));
    }

    /**
     * 构造。
     *
     * @param beanType 转换成的目标Bean类
     * @param copyOptions Bean转换选项参数
     */
    @SuppressWarnings("unchecked")
    public BeanConverter(final Type beanType, final CopyOptions copyOptions) {
        this.beanType = beanType;
        this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
        this.copyOptions = copyOptions;
    }

    /**
     * 内部转换。
     */
    @Override
    protected T convertInternal(final Object value) {
        final Class<?>[] interfaces = this.beanClass.getInterfaces();
        for (final Class<?> anInterface : interfaces) {
            if ("cc.hongz.ztools.json.JSONBeanParser".equals(anInterface.getName())) {
                // issue#I7M2GZ
                final T obj = ReflectUtil.newInstanceIfPossible(this.beanClass);
                ReflectUtil.invoke(obj, "parse", value);
                return obj;
            }
        }

        if (value instanceof Map ||
                value instanceof ValueProvider ||
                BeanUtil.isReadableBean(value.getClass())) {
            if (value instanceof Map && this.beanClass.isInterface()) {
                // 将Map动态代理为Bean
                return MapProxy.create((Map<?, ?>) value).toProxyBean(this.beanClass);
            }

            // 限定被转换对象类型
            return BeanCopier.create(value, ReflectUtil.newInstanceIfPossible(this.beanClass), this.beanType, this.copyOptions).copy();
        } else if (value instanceof byte[]) {
            // 尝试反序列化
            return ObjectUtil.deserialize((byte[]) value);
        } else if (StrUtil.isEmptyIfStr(value)) {
            // issue#3136
            return null;
        }

        throw new ConvertException("Unsupported source type: {}", value.getClass());
    }

    /**
     * 获取目标类型。
     */
    @Override
    public Class<T> getTargetType() {
        return this.beanClass;
    }
}
