package cc.hongz.ztools.core.lang.mutable;

import java.io.Serializable;

/**
 * 可变 {@code boolean} 类型.
 *
 * @see Boolean
 * @author looly (原作者)
 * @since 3.0.1
 */
public class MutableBool implements Comparable<MutableBool>, Mutable<Boolean>, Serializable {
    private static final long serialVersionUID = 1L;

    /**


     * 值。


     */


    private boolean value;

    /**
     * 构造，默认值0.
     */
    public MutableBool() {
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableBool(final boolean value) {
        this.value = value;
    }

    /**
     * 构造.
     *
     * @param value String值
     * @throws NumberFormatException 转为Boolean错误
     */
    public MutableBool(final String value) throws NumberFormatException {
        this.value = Boolean.parseBoolean(value);
    }

    /**
     * 获得原始值。
     */
    @Override
    public Boolean get() {
        return this.value;
    }

    /**
     * 设置值.
     *
     * @param newValue 值
     */
    public void set(final boolean newValue) {
        this.value = newValue;
    }

    /**
     * 设置值。
     */
    @Override
    public void set(final Boolean newValue) {
        this.value = newValue;
    }

    // -----------------------------------------------------------------------
    /**
     * 相等需同时满足如下条件.
     * <ol>
     *     <li>非空</li>
     *     <li>类型为 MutableBool</li>
     *     <li>值相等</li>
     * </ol>
     *
     * @param obj 比对的对象
     * @return 相同返回{@code true}，否则 {@code false}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableBool) {
            return value == ((MutableBool) obj).value;
        }
        return false;
    }

    /**
     * 计算哈希值。
     */
    @Override
    public int hashCode() {
        return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    // -----------------------------------------------------------------------
    /**
     * 比较.
     *
     * @param other 其它 MutableBool 对象
     * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
     */
    @Override
    public int compareTo(final MutableBool other) {
        return Boolean.compare(this.value, other.value);
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
