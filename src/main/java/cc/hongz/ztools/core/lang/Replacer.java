package cc.hongz.ztools.core.lang;

/**
 * 替换器.
 * <p>
 * 通过实现此接口完成指定类型对象的替换操作，替换后的目标类型依旧为指定类型.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Replacer}.
 *
 * @param <T> 被替换操作的类型
 * @author looly
 */
@FunctionalInterface
public interface Replacer<T> {

    /**
     * 替换指定类型为目标类型.
     *
     * @param t 被替换的对象
     * @return 替代后的对象
     */
    T replace(T t);
}
