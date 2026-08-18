package cc.hongz.ztools.core.bean;

/**
 * 为了解决反射过程中，需要传递 null 参数，但是会丢失参数类型而设立的包装类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.bean.NullWrapperBean}。
 *
 * @param <T> Null 值对应的类型
 * @author Lillls
 * @since 5.5.0
 */
public class NullWrapperBean<T> {

    private final Class<T> clazz;

    /**
     * 构造.
     *
     * @param clazz null 的类型
     */
    public NullWrapperBean(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取 null 值对应的类型.
     *
     * @return 类型
     */
    public Class<T> getWrappedClass() {
        return clazz;
    }
}
