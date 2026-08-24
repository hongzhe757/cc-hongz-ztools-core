package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.convert.Convert;
import cc.hongz.ztools.core.convert.ConvertException;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.function.Function;

/**
 * 原始类型转换器。
 * 支持类型为：<br>
 * <ul>
 *         <li> {@code byte}</li>
 *         <li> {@code short}</li>
 *          <li> {@code int}</li>
 *          <li> {@code long}</li>
 *         <li> {@code float}</li>
 *         <li> {@code double}</li>
 *         <li> {@code char}</li>
 *         <li> {@code boolean}</li>
 * </ul>
 *
 * @author Looly
 */
public class PrimitiveConverter extends AbstractConverter<Object> {
    private static final long serialVersionUID = 1L;

    private final Class<?> targetType;

    /**
     * 构造。
     *
     * @param clazz 需要转换的原始
     * @throws IllegalArgumentException 传入的转换类型非原始类型时抛出
     */
    public PrimitiveConverter(final Class<?> clazz) {
        if (null == clazz) {
            throw new NullPointerException("PrimitiveConverter not allow null target type!");
        } else if (false == clazz.isPrimitive()) {
            throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
        }
        this.targetType = clazz;
    }

    /**
     * 内部转换。
     */
    @Override
    protected Object convertInternal(final Object value) {
        return PrimitiveConverter.convert(value, this.targetType, this::convertToStr);
    }

    @Override
    protected String convertToStr(final Object value) {
        return StrUtil.trim(super.convertToStr(value));
    }

    /**
     * 获取目标类型。
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<Object> getTargetType() {
        return (Class<Object>) this.targetType;
    }

    /**
     * 将指定值转换为原始类型的值。
     * @param value 值
     * @param primitiveClass 原始类型
     * @param toStringFunc 当无法直接转换时，转为字符串后再转换的函数
     * @return 转换结果
     * @since 5.5.0
     */
    protected static Object convert(final Object value, final Class<?> primitiveClass,
            final Function<Object, String> toStringFunc) {
        if (byte.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Byte.class, toStringFunc), 0);
        } else if (short.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Short.class, toStringFunc), 0);
        } else if (int.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Integer.class, toStringFunc), 0);
        } else if (long.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Long.class, toStringFunc), 0);
        } else if (float.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Float.class, toStringFunc), 0);
        } else if (double.class == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Double.class, toStringFunc), 0);
        } else if (char.class == primitiveClass) {
            return Convert.convert(Character.class, value);
        } else if (boolean.class == primitiveClass) {
            return Convert.convert(Boolean.class, value);
        }

        throw new ConvertException("Unsupported target type: {}", primitiveClass);
    }
}
