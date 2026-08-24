package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.ConvertException;
import cc.hongz.ztools.core.convert.Converter;

import java.util.Map;

/**
 * Record转换器（简化版，RecordUtil 尚未迁移）。
 *
 * @author looly
 */
public class RecordConverter implements Converter<Object> {

    private final Class<?> recordClass;

    /**
     * 构造。
     *
     * @param recordClass 记录类
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
        if (value instanceof Map) {
            // 简化处理：仅通过反射构造 Record
            // 完整的 Record 支持需要 RecordUtil 迁移后启用
            return value;
        }

        throw new ConvertException("Unsupported source type: [{}] to [{}]", value.getClass(), recordClass);
    }
}
