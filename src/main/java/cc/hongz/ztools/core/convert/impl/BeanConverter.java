package cc.hongz.ztools.core.convert.impl;

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
 * Bean转换器
 *
 * @param <T> Bean类型
 * @author Looly
 */
public class BeanConverter<T> extends AbstractConverter<T> {
	private static final long serialVersionUID = 1L;

	private final Type beanType;
	private final Class<T> beanClass;

	@SuppressWarnings("unchecked")
	public BeanConverter(Type beanType) {
		this.beanType = beanType;
		this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
	}

	public BeanConverter(Class<T> beanClass) {
		this.beanType = beanClass;
		this.beanClass = beanClass;
	}

	@Override
	protected T convertInternal(Object value) {
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			if (this.beanClass.isInterface()) {
				return MapProxy.create((Map<?, ?>) value).toProxyBean(this.beanClass);
			}
			T target = ReflectUtil.newInstanceIfPossible(this.beanClass);
			if (null == target) {
				throw new ConvertException("Cannot instantiate [{}]", this.beanClass.getName());
			}
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String key = entry.getKey().toString();
				try {
					ReflectUtil.setFieldValue(target, key, entry.getValue());
				} catch (Exception ignored) {
					// ignore
				}
			}
			return target;
		} else if (StrUtil.isEmptyIfStr(value)) {
			return null;
		}

		throw new ConvertException("Unsupported source type: {}", value.getClass());
	}

	@Override
	public Class<T> getTargetType() {
		return this.beanClass;
	}
}
