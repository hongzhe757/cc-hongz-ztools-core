package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.bean.BeanUtil;
import cc.hongz.ztools.core.bean.RecordUtil;
import cc.hongz.ztools.core.bean.copier.ValueProvider;
import cc.hongz.ztools.core.bean.copier.provider.BeanValueProvider;
import cc.hongz.ztools.core.bean.copier.provider.MapValueProvider;
import cc.hongz.ztools.core.convert.ConvertException;
import cc.hongz.ztools.core.convert.Converter;

import java.util.Map;

/**
 * Record转换器。
 *
 * @author looly
 */
public class RecordConverter implements Converter<Object> {

    private final Class<?> recordClass;

    /**
     * 构造。
     *
     * @param recordClass Record类
     */
    public RecordConverter(final Class<?> recordClass) {
        this.recordClass = recordClass;
    }

    /**
     * 转换。
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object convert(final Object value, final Object defaultValue) throws IllegalArgumentException {
        ValueProvider<String> valueProvider = null;
        if (value instanceof ValueProvider) {
            valueProvider = (ValueProvider<String>) value;
        } else if (value instanceof Map) {
            valueProvider = new MapValueProvider((Map<String, ?>) value);
        } else if (BeanUtil.isReadableBean(value.getClass())) {
            valueProvider = new BeanValueProvider(value, false, false);
        }

        if (null != valueProvider) {
            return RecordUtil.newInstance(recordClass, valueProvider);
        }

        throw new ConvertException("Unsupported source type: [{}] to [{}]", value.getClass(), recordClass);
    }
}
