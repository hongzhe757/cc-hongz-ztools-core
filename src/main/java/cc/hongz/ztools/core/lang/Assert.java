package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.StrUtil;

import java.util.function.Supplier;

/**
 * 断言工具类（codec 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化.
 *
 * @author looly
 */
public final class Assert {

    /**
     * 私有构造，禁止实例化.
     */
    private Assert() {
    }

    // --------------------------------------- Static method start

    /**
     * 断言对象非 null.
     *
     * @param object 被检查对象
     * @param <T>    对象类型
     * @return 非空对象
     * @throws IllegalArgumentException 对象为 null 时
     */
    public static <T> T notNull(final T object) throws IllegalArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * 断言对象非 null.
     *
     * @param object           被检查对象
     * @param errorMsgTemplate 错误消息模板
     * @param params           模板参数
     * @param <T>              对象类型
     * @return 非空对象
     * @throws IllegalArgumentException 对象为 null 时
     */
    public static <T> T notNull(final T object,
                                final String errorMsgTemplate,
                                final Object... params) throws IllegalArgumentException {
        return notNull(object, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
    }

    /**
     * 断言对象非 null.
     *
     * @param object        被检查对象
     * @param errorSupplier 异常供应器
     * @param <T>           对象类型
     * @param <X>           异常类型
     * @return 非空对象
     * @throws X 对象为 null 时
     */
    public static <T, X extends Throwable> T notNull(final T object,
                                                     final Supplier<X> errorSupplier) throws X {
        if (object == null) {
            throw errorSupplier.get();
        }
        return object;
    }

    /**
     * 检查整数值是否在指定范围内（含边界）.
     *
     * @param value 值
     * @param min   最小值
     * @param max   最大值
     * @return 检查后的值
     * @throws IllegalArgumentException 越界时
     */
    public static int checkBetween(final int value, final int min, final int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    StrUtil.format("The value must be between {} and {}.", min, max));
        }
        return value;
    }

    // --------------------------------------- Static method end
}
