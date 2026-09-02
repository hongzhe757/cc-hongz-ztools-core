package cc.hongz.ztools.core.lang.mutable;

/**
 * 可变值类型接口.
 *
 * @param <T> 值类型
 * @author looly (原作者)
 * @since 3.0.1
 */
public interface Mutable<T> {

    /**
     * 获得原始值.
     *
     * @return 原始值
     */
    T get();

    /**
     * 设置值.
     *
     * @param value 值
     */
    void set(T value);
}
