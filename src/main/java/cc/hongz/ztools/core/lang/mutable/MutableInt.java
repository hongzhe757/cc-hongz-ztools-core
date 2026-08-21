package cc.hongz.ztools.core.lang.mutable;

/**
 * 可变 {@code int} 类型.
 *
 * @see Integer
 * @since 3.0.1
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    private static final long serialVersionUID = 1L;

    private int value;

    /**
     * 构造，默认值 0.
     */
    public MutableInt() {
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableInt(final int value) {
        this.value = value;
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableInt(final Number value) {
        this(value.intValue());
    }

    /**
     * 构造.
     *
     * @param value 字符串值
     * @throws NumberFormatException 数字转换错误
     */
    public MutableInt(final String value) throws NumberFormatException {
        this.value = Integer.parseInt(value);
    }

    @Override
    public Integer get() {
        return this.value;
    }

    /**
     * 设置值.
     *
     * @param newValue 值
     */
    public void set(final int newValue) {
        this.value = newValue;
    }

    @Override
    public void set(final Number newValue) {
        this.value = newValue.intValue();
    }

    /**
     * 值加一.
     *
     * @return this
     */
    public MutableInt increment() {
        value++;
        return this;
    }

    /**
     * 值减一.
     *
     * @return this
     */
    public MutableInt decrement() {
        value--;
        return this;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableInt) {
            return value == ((MutableInt) obj).intValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public int compareTo(final MutableInt other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
