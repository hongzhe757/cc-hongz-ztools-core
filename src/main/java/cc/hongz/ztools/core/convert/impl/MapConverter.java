package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.convert.ConverterRegistry;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link Map} 转换器。
 *
 * @author Looly
 * @since 3.0.8
 */
public class MapConverter extends AbstractConverter<Map<?, ?>> {
    private static final long serialVersionUID = 1L;

    private final Type mapType;
    private final Type keyType;
    private final Type valueType;

    /**
     * 构造。
     *
     * @param mapType Map类型
     */
    public MapConverter(final Type mapType) {
        this(mapType, TypeUtil.getTypeArgument(mapType, 0), TypeUtil.getTypeArgument(mapType, 1));
    }

    /**
     * 构造。
     *
     * @param mapType   Map类型
     * @param keyType   键类型
     * @param valueType 值类型
     */
    public MapConverter(final Type mapType, final Type keyType, final Type valueType) {
        this.mapType = mapType;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    /**
     * 内部转换。
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Map<?, ?> convertInternal(final Object value) {
        Map map;
        if (value instanceof Map) {
            final Class<?> valueClass = value.getClass();
            if (valueClass.equals(this.mapType)) {
                final Type[] typeArguments = TypeUtil.getTypeArguments(valueClass);
                if (null != typeArguments && 2 == typeArguments.length
                        && Objects.equals(this.keyType, typeArguments[0])
                        && Objects.equals(this.valueType, typeArguments[1])) {
                    return (Map) value;
                }
            }

            final Class<?> mapClass = TypeUtil.getClass(this.mapType);
            if (null == mapClass || mapClass.isAssignableFrom(AbstractMap.class)) {
                map = new LinkedHashMap<>();
            } else {
                map = MapUtil.createMap(mapClass);
            }
            convertMapToMap((Map) value, map);
        } else {
            throw new UnsupportedOperationException(StrUtil.format("Unsupported toMap value type: {}",
                    value.getClass().getName()));
        }
        return map;
    }

    /**
     * Map 转 Map。
     */
    private void convertMapToMap(final Map<?, ?> srcMap, final Map<Object, Object> targetMap) {
        final ConverterRegistry convert = ConverterRegistry.getInstance();
        srcMap.forEach((key, value) -> {
            key = TypeUtil.isUnknown(this.keyType) ? key : convert.convert(this.keyType, key);
            value = TypeUtil.isUnknown(this.valueType) ? value : convert.convert(this.valueType, value);
            targetMap.put(key, value);
        });
    }

    /**
     * 获取目标类型。
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<?, ?>> getTargetType() {
        return (Class<Map<?, ?>>) TypeUtil.getClass(this.mapType);
    }
}
