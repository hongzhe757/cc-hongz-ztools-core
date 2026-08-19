package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.ObjectUtil;
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
     * 断言字符串非空白.
     *
     * @param str           被检查字符串
     * @param errorMsgTemplate 错误消息模板
     * @param params        模板参数
     * @return 非空字符串
     * @throws IllegalArgumentException 字符串为空白时
     */
    public static String notBlank(final CharSequence str,
                                  final String errorMsgTemplate,
                                  final Object... params) throws IllegalArgumentException {
        if (StrUtil.isBlank(str)) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
        return str.toString();
    }

    /**
     * 断言字符串非空白.
     *
     * @param str 被检查字符串
     * @return 非空字符串
     * @throws IllegalArgumentException 字符串为空白时
     */
    public static String notBlank(final CharSequence str) throws IllegalArgumentException {
        return notBlank(str, "[Assertion failed] - this string argument must not be blank");
    }

    /**
     * 断言字符串非空（非 null 且长度大于 0）.
     *
     * @param str              被检查字符串
     * @param errorMsgTemplate 错误消息模板
     * @param params           模板参数
     * @return 非空字符串
     * @throws IllegalArgumentException 字符串为空时
     */
    public static String notEmpty(final CharSequence str,
                                  final String errorMsgTemplate,
                                  final Object... params) throws IllegalArgumentException {
        if (StrUtil.isEmpty(str)) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
        return str.toString();
    }

    /**
     * 断言字符串非空（非 null 且长度大于 0）.
     *
     * @param str 被检查字符串
     * @return 非空字符串
     * @throws IllegalArgumentException 字符串为空时
     */
    public static String notEmpty(final CharSequence str) throws IllegalArgumentException {
        return notEmpty(str, "[Assertion failed] - this string argument must not be empty");
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

    /**
     * 检查 long 值是否在指定范围内（含边界）.
     *
     * @param value 值
     * @param min   最小值
     * @param max   最大值
     * @return 检查后的值
     * @throws IllegalArgumentException 越界时
     */
    public static long checkBetween(final long value, final long min, final long max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    StrUtil.format("The value must be between {} and {}.", min, max));
        }
        return value;
    }

    /**
     * 检查整数值是否在指定范围内（含边界），自定义错误消息.
     *
     * @param value    值
     * @param min      最小值
     * @param max      最大值
     * @param errorMsg 错误消息
     * @return 检查后的值
     * @throws IllegalArgumentException 越界时
     */
    public static int checkBetween(final int value, final int min, final int max, final String errorMsg) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(errorMsg);
        }
        return value;
    }

    /**
     * 检查 long 值是否在指定范围内（含边界），自定义错误消息.
     *
     * @param value    值
     * @param min      最小值
     * @param max      最大值
     * @param errorMsg 错误消息
     * @return 检查后的值
     * @throws IllegalArgumentException 越界时
     */
    public static long checkBetween(final long value, final long min, final long max, final String errorMsg) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(errorMsg);
        }
        return value;
    }

    /**
     * 检查 double 值是否在指定范围内（含边界），自定义错误消息.
     *
     * @param value    值
     * @param min      最小值
     * @param max      最大值
     * @param errorMsg 错误消息
     * @return 检查后的值
     * @throws IllegalArgumentException 越界时
     */
    public static double checkBetween(final double value, final double min, final double max, final String errorMsg) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(errorMsg);
        }
        return value;
    }

    // --------------------------------------- Static method end

    /**
     * 断言表达式为 true.
     *
     * @param expression       表达式
     * @param errorMsgTemplate 错误消息模板
     * @param params           模板参数
     * @throws IllegalArgumentException 表达式为 false 时
     */
    public static void isTrue(final boolean expression,
                              final String errorMsgTemplate,
                              final Object... params) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    /**
     * 断言表达式为 true.
     *
     * @param expression 表达式
     * @throws IllegalArgumentException 表达式为 false 时
     */
    public static void isTrue(final boolean expression) throws IllegalArgumentException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    /**
     * 断言表达式为 true，使用 Supplier 提供异常.
     *
     * @param expression  表达式
     * @param errorSupplier 异常供应器
     * @param <X>         异常类型
     * @throws X 表达式为 false 时
     */
    public static <X extends Throwable> void isTrue(final boolean expression,
                                                     final Supplier<X> errorSupplier) throws X {
        if (!expression) {
            throw errorSupplier.get();
        }
    }

    /**
     * 断言表达式为 false.
     *
     * @param expression       表达式
     * @param errorMsgTemplate 错误消息模板
     * @param params           模板参数
     * @throws IllegalArgumentException 表达式为 true 时
     */
    public static void isFalse(final boolean expression,
                               final String errorMsgTemplate,
                               final Object... params) throws IllegalArgumentException {
        if (expression) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    /**
     * 断言表达式为 false.
     *
     * @param expression 表达式
     * @throws IllegalArgumentException 表达式为 true 时
     */
    public static void isFalse(final boolean expression) throws IllegalArgumentException {
        isFalse(expression, "[Assertion failed] - this expression must be false");
    }

    /**
     * 断言表达式为 true.
     *
     * @param expression       表达式
     * @param errorMsgTemplate 错误消息模板
     * @param params           模板参数
     * @throws IllegalStateException 表达式为 false 时
     */
    public static void state(final boolean expression, final String errorMsgTemplate,
            final Object... params) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    /**
     * 断言表达式为 true.
     *
     * @param expression 表达式
     * @throws IllegalStateException 表达式为 false 时
     */
    public static void state(final boolean expression) throws IllegalStateException {
        state(expression, "[Assertion failed] - this state must be true");
    }

    /**
     * 断言表达式为 true.
     *
     * @param expression       表达式
     * @param errorMsgSupplier 错误消息供应器
     * @throws IllegalStateException 表达式为 false 时
     */
    public static void state(final boolean expression,
            final Supplier<String> errorMsgSupplier) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(errorMsgSupplier.get());
        }
    }

    // ------------------------------------------------ Check not equals

    /**
     * 断言两个对象是否不相等,如果两个对象相等 抛出IllegalArgumentException 异常。
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @throws IllegalArgumentException obj1 must be not equals obj2
     */
    public static void notEquals(final Object obj1, final Object obj2) {
        notEquals(obj1, obj2, "({}) must be not equals ({})", obj1, obj2);
    }

    /**
     * 断言两个对象是否不相等,如果两个对象相等 抛出IllegalArgumentException 异常。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param errorMsgTemplate 异常信息模板，类似于"aa{}bb{}cc"
     * @param params           异常信息参数，用于替换"{}"占位符
     * @throws IllegalArgumentException obj1 must be not equals obj2
     */
    public static void notEquals(final Object obj1, final Object obj2, final String errorMsgTemplate,
            final Object... params) throws IllegalArgumentException {
        notEquals(obj1, obj2, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
    }

    /**
     * 断言两个对象是否不相等,如果两个对象相等,抛出指定类型异常,并使用指定的函数获取错误信息返回。
     *
     * @param obj1          对象1
     * @param obj2          对象2
     * @param errorSupplier 错误抛出异常附带的消息生产接口
     * @param <X>           异常类型
     * @throws X obj1 must be not equals obj2
     */
    public static <X extends Throwable> void notEquals(final Object obj1, final Object obj2,
            final Supplier<X> errorSupplier) throws X {
        if (ObjectUtil.equals(obj1, obj2)) {
            throw errorSupplier.get();
        }
    }
}
