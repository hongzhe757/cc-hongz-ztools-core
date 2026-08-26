package cc.hongz.ztools.core.lang.ref;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Objects;

/**
 * 软引用对象，在GC报告内存不足时会被GC回收.
 *
 * @param <T> 键类型
 */
public class SoftObj<T> extends SoftReference<T> implements Ref<T> {
    /**
     * 哈希值缓存。
     */
    private final int hashCode;

    /**
     * 构造.
     *
     * @param obj   原始对象
     * @param queue {@link ReferenceQueue}
     */
    public SoftObj(final T obj, final ReferenceQueue<? super T> queue) {
        super(obj, queue);
        hashCode = Objects.hashCode(obj);
    }

    /**
     * 计算哈希值。
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return hashCode;
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
        } else if (other instanceof SoftObj) {
            return Objects.equals(((SoftObj<?>) other).get(), get());
        }
        return false;
    }
}
