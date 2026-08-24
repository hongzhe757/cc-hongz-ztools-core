package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.convert.ConverterRegistry;
import cc.hongz.ztools.core.convert.impl.DateConverter;
import cc.hongz.ztools.core.convert.impl.TemporalAccessorConverter;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 包含格式的数字转换器，主要针对带格式的时间戳。
 *
 * @author looly
 * @since 5.8.13
 */
public class NumberWithFormat extends Number implements TypeConverter, Comparable<NumberWithFormat> {
    private static final long serialVersionUID = 1L;

    private final Number number;
    private final String format;

    /**
     * 构造。
     *
     * @param number 数字
     * @param format 格式
     */
    public NumberWithFormat(final Number number, final String format) {
        this.number = number;
        this.format = format;
    }

    /**
     * 转换。
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object convert(final Type targetType, final Object value) {
        // 自定义日期格式
        if (null != this.format && targetType instanceof Class) {
            final Class<?> clazz = (Class<?>) targetType;
            // https://gitee.com/chinabugotech/hutool/issues/I6IS5B
            if (Date.class.isAssignableFrom(clazz)) {
                return new DateConverter((Class<? extends Date>) clazz, format).convert(this.number, null);
            } else if (TemporalAccessor.class.isAssignableFrom(clazz)) {
                return new TemporalAccessorConverter(clazz, format).convert(this.number, null);
            } else if (String.class == clazz) {
                return toString();
            }

            // 其他情况按照正常数字转换
        }

        // 按照正常数字转换
        return ConverterRegistry.getInstance().convert(targetType, this.number);
    }

    /**
     * 获取原始Number。
     *
     * @return 原始Number
     * @since 5.8.32
     */
    public Object getNumber() {
        return this.number;
    }

    /**
     * 返回 int 值。
     */
    @Override
    public int intValue() {
        return this.number.intValue();
    }

    /**
     * 返回 long 值。
     */
    @Override
    public long longValue() {
        return this.number.longValue();
    }

    /**
     * 返回 float 值。
     */
    @Override
    public float floatValue() {
        return this.number.floatValue();
    }

    /**
     * 返回 double 值。
     */
    @Override
    public double doubleValue() {
        return this.number.doubleValue();
    }

    /**
     * 转为字符串。
     */
    @Override
    public String toString() {
        return this.number.toString();
    }

    /**
     * 比较。
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int compareTo(final NumberWithFormat o) {
        if (this.number instanceof Comparable && o.getNumber() instanceof Comparable) {
            return ((Comparable) this.number).compareTo(o.getNumber());
        }
        return Double.compare(this.doubleValue(), o.doubleValue());
    }
}
