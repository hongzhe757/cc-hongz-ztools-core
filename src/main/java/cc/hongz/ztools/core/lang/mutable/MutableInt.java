package cc.hongz.ztools.core.lang.mutable;

/**
 * 可变 {@code int} 类型.
 *
 * @see Integer
 * @since 3.0.1
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    private static final long serialVersionUID = 1L;

    /**


     * 值。


     */


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

    /**
     * 获得原始值。
     */
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

    /**
     * 设置值。
     */
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

    /**
     * 转换为 int 值。
     */
    @Override
    public int intValue() {
        return value;
    }

    /**
     * 转换为 long 值。
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * 转换为 float 值。
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * 转换为 double 值。
     */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * 判断是否相等。
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableInt) {
            return value == ((MutableInt) obj).intValue();
        }
        return false;
    }

    /**
     * 计算哈希值。
     */
    @Override
    public int hashCode() {
        return this.value;
    }

    /**
     * 比较大小。
     */
    @Override
    public int compareTo(final MutableInt other) {
        return Integer.compare(this.value, other.value);
    }

    /**
     * 转换为字符串。
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
