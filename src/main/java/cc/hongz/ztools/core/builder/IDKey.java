package cc.hongz.ztools.core.builder;

import java.io.Serializable;

/**
 * 包装唯一键（System.identityHashCode()）使对象只有和自己 equals.
 *
 * 此对象用于消除小概率下 System.identityHashCode() 产生的ID重复问题.
 *
 * 来自于 Apache-Commons-Lang3.
 * @author looly，Apache-Commons
 * @since 4.2.2
 */
final class IDKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Object value;
    private final int id;

    /**
     * 构造.
     *
     * @param obj 计算唯一ID的对象
     */
    public IDKey(final Object obj) {
        id = System.identityHashCode(obj);
        // 存在某些情况（LANG-459）不同对象返回相同的 identity hash code，
        // 因此额外添加 value 来消除歧义
        value = obj;
    }

    /**
     * 返回 hashcode，即系统的 identity hashcode.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * 检查两个实例是否相等.
     *
     * @param other 要比较的另一个对象
     * @return 两个实例是否为同一对象
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IDKey)) {
            return false;
        }
        final IDKey idKey = (IDKey) other;
        if (id != idKey.id) {
            return false;
        }
        // 注意此处使用 identity equals（引用相等）
        return value == idKey.value;
    }
}
