package cc.hongz.ztools.core.lang.hash;

import java.util.Objects;

/**
 * 128位数字表示，分高位和低位.
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class Number128 extends Number {
    private static final long serialVersionUID = 1L;

    /**
     * 低位值。
     */
    private long lowValue;

    /**
     * 高位值。
     */
    private long highValue;

    /**
     * 构造.
     *
     * @param lowValue  低位
     * @param highValue 高位
     */
    public Number128(final long lowValue, final long highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    /**
     * 获取低位值.
     *
     * @return 地位值
     */
    public long getLowValue() {
        return lowValue;
    }

    /**
     * 设置低位值.
     *
     * @param lowValue 低位值
     */
    public void setLowValue(final long lowValue) {
        this.lowValue = lowValue;
    }

    /**
     * 获取高位值.
     *
     * @return 高位值
     */
    public long getHighValue() {
        return highValue;
    }

    /**
     * 设置高位值.
     *
     * @param hiValue 高位值
     */
    public void setHighValue(final long hiValue) {
        this.highValue = hiValue;
    }

    /**
     * 获取高低位数组，long[0]：低位，long[1]：高位.
     *
     * @return 高低位数组，long[0]：低位，long[1]：高位
     */
    public long[] getLongArray() {
        return new long[]{lowValue, highValue};
    }

    /**
     * 转换为 int 值。
     *
     * @return int 值
     */
    @Override
    public int intValue() {
        return (int) longValue();
    }

    /**
     * 转换为 long 值。
     *
     * @return long 值
     */
    @Override
    public long longValue() {
        return this.lowValue;
    }

    /**
     * 转换为 float 值。
     *
     * @return float 值
     */
    @Override
    public float floatValue() {
        return longValue();
    }

    /**
     * 转换为 double 值。
     *
     * @return double 值
     */
    @Override
    public double doubleValue() {
        return longValue();
    }

    /**
     * 判断两个 128 位数字是否相等。
     *
     * @param o 待比较对象
     * @return 相等返回 true，否则 false
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Number128 number128 = (Number128) o;
        return lowValue == number128.lowValue && highValue == number128.highValue;
    }

    /**
     * 计算哈希值。
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hash(lowValue, highValue);
    }
}
