package cc.hongz.ztools.core.lang;

/**
 * 过滤器接口.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Filter}.
 *
 * @param <T> 被过滤的类型
 * @author looly
 */
@FunctionalInterface
public interface Filter<T> {
    /**
     * 是否接受对象.
     *
     * @param t 检查的对象
     * @return 是否接受对象
     */
    boolean accept(T t);
}
