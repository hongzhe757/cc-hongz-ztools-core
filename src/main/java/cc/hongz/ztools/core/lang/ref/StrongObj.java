package cc.hongz.ztools.core.lang.ref;

import java.util.Objects;

/**
 * 强引用对象，不回收.
 *
 * @param <T> 键类型
 */
public class StrongObj<T> implements Ref<T> {

    /**
     * 原始对象。
     */
    private final T obj;

    /**
     * 构造.
     *
     * @param obj 原始对象
     */
    public StrongObj(final T obj) {
        this.obj = obj;
    }

    /**
     * 获取引用的原始对象。
     *
     * @return 原始对象
     */
    @Override
    public T get() {
        return this.obj;
    }

    /**
     * 计算哈希值。
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(obj);
    }

    /**
     * 判断是否相等。
     *
     * @param other 待比较对象
     * @return 相等返回 true，否则 false
     */
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof StrongObj) {
            return Objects.equals(((StrongObj<?>) other).get(), get());
        }
        return false;
    }
}
