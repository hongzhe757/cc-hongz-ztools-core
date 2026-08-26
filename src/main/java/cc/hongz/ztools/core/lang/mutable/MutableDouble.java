package cc.hongz.ztools.core.lang.mutable;

/**
 * 可变 {@code double} 类型.
 *
 * @see Double
 * @since 3.0.1
 */
public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {
    private static final long serialVersionUID = 1L;

    /**


     * 值。


     */


    private double value;

    /**
     * 构造，默认值0.
     */
    public MutableDouble() {
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableDouble(final double value) {
        this.value = value;
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableDouble(final Number value) {
        this(value.doubleValue());
    }

    /**
     * 构造.
     *
     * @param value String值
     * @throws NumberFormatException 数字转换错误
     */
    public MutableDouble(final String value) throws NumberFormatException {
        this.value = Double.parseDouble(value);
    }

    /**
     * 获得原始值。
     */
    @Override
    public Double get() {
        return this.value;
    }

    /**
     * 设置值.
     *
     * @param newValue 值
     */
    public void set(final double newValue) {
        this.value = newValue;
    }

    /**
     * 设置值。
     */
    @Override
    public void set(final Number newValue) {
        this.value = newValue.doubleValue();
    }

    // -----------------------------------------------------------------------
    /**
     * 值+1.
     *
     * @return this
     */
    public MutableDouble increment() {
        value++;
        return this;
    }

    /**
     * 值减一.
     *
     * @return this
     */
    public MutableDouble decrement() {
        value--;
        return this;
    }

    // -----------------------------------------------------------------------
    /**
     * 增加值.
     *
     * @param operand 被增加的值
     * @return this
     */
    public MutableDouble add(final double operand) {
        this.value += operand;
        return this;
    }

    /**
     * 增加值.
     *
     * @param operand 被增加的值，非空
     * @return this
     */
    public MutableDouble add(final Number operand) {
        this.value += operand.doubleValue();
        return this;
    }

    /**
     * 减去值.
     *
     * @param operand 被减的值
     * @return this
     */
    public MutableDouble subtract(final double operand) {
        this.value -= operand;
        return this;
    }

    /**
     * 减去值.
     *
     * @param operand 被减的值，非空
     * @return this
     */
    public MutableDouble subtract(final Number operand) {
        this.value -= operand.doubleValue();
        return this;
    }

    // -----------------------------------------------------------------------
    /**
     * 转换为 int 值。
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * 转换为 long 值。
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * 转换为 float 值。
     */
    @Override
    public float floatValue() {
        return (float) value;
    }

    /**
     * 转换为 double 值。
     */
    @Override
    public double doubleValue() {
        return value;
    }

    // -----------------------------------------------------------------------
    /**
     * 相等需同时满足如下条件.
     * <ol>
     *     <li>非空</li>
     *     <li>类型为 {@code MutableDouble}</li>
     *     <li>值相等</li>
     * </ol>
     *
     * @param obj 比对的对象
     * @return 相同返回{@code true}，否则 {@code false}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableDouble) {
            return (Double.doubleToLongBits(((MutableDouble) obj).value) == Double.doubleToLongBits(value));
        }
        return false;
    }

    /**
     * 计算哈希值。
     */
    @Override
    public int hashCode() {
        final long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ bits >>> Integer.SIZE);
    }

    // -----------------------------------------------------------------------
    /**
     * 比较.
     *
     * @param other 其它 {@code MutableDouble} 对象
     * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
     */
    @Override
    public int compareTo(final MutableDouble other) {
        return Double.compare(this.value, other.value);
    }

    // -----------------------------------------------------------------------
    /**
     * 转换为字符串。
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
