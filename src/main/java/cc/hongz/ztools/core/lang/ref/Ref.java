package cc.hongz.ztools.core.lang.ref;

/**
 * 引用接口，提供获取被引用对象的能力.
 *
 * @param <T> 被引用对象类型
 * @author hongzhe.qin
 */
@FunctionalInterface
public interface Ref<T> {

    /**
     * 获取引用的原始对象.
     *
     * @return 原始对象
     */
    T get();
}
