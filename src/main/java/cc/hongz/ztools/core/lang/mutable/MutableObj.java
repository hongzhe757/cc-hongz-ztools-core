package cc.hongz.ztools.core.lang.mutable;

import cc.hongz.ztools.core.util.ObjUtil;

import java.io.Serializable;

/**
 * 可变 {@code Object}.
 *
 * @param <T> 可变的类型
 * @since 3.0.1
 */
public class MutableObj<T> implements Mutable<T>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 构建MutableObj.
     *
     * @param value 被包装的值
     * @param <T>   值类型
     * @return MutableObj
     * @since 5.8.0
     */
    public static <T> MutableObj<T> of(final T value) {
        return new MutableObj<>(value);
    }

    /**


     * 值。


     */


    private T value;

    /**
     * 构造，空值.
     */
    public MutableObj() {
    }

    /**
     * 构造.
     *
     * @param value 值
     */
    public MutableObj(final T value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------
    /**
     * 获得原始值。
     */
    @Override
    public T get() {
        return this.value;
    }

    /**
     * 设置值。
     */
    @Override
    public void set(final T newValue) {
        this.value = newValue;
    }

    // -----------------------------------------------------------------------
    /**
     * 判断是否相等。
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() == obj.getClass()) {
            final MutableObj<?> that = (MutableObj<?>) obj;
            return ObjUtil.equals(this.value, that.value);
        }
        return false;
    }

    /**
     * 计算哈希值。
     */
    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    // -----------------------------------------------------------------------
    /**
     * 转换为字符串。
     */
    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

}
