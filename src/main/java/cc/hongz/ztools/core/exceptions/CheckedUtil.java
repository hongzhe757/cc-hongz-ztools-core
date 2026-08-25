package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.lang.func.Func;
import cc.hongz.ztools.core.lang.func.Func0;
import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.lang.func.Supplier1;
import cc.hongz.ztools.core.lang.func.VoidFunc;
import cc.hongz.ztools.core.lang.func.VoidFunc0;
import cc.hongz.ztools.core.lang.func.VoidFunc1;

import java.util.Objects;

/**
 * 方便的执行会抛出受检查类型异常的方法调用或者代码段。
 * <p>
 * 该工具通过函数式的方式，将那些需要抛出受检查异常的表达式或者代码段，转化成一个 {@code Func*} 对象，
 * 执行表达式抛出任何异常时，都会转化成运行时异常，代码中就不用显示的 try-catch。
 * </p>
 *
 * @author conder
 * @since 5.7.19
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class CheckedUtil {

    /**
     * 接收一个可以转化成 {@code Func} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param <P>        运行时传入的参数类型
     * @param <R>        最终返回的数据类型
     * @return {@link FuncRt}
     */
    public static <P, R> FuncRt<P, R> uncheck(final Func<P, R> expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code Func0} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param <R>        最终返回的数据类型
     * @return {@link Func0Rt}
     */
    public static <R> Func0Rt<R> uncheck(final Func0<R> expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code Func1} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param <P>        运行时传入的参数类型
     * @param <R>        最终返回的数据类型
     * @return {@link Func1Rt}
     */
    public static <P, R> Func1Rt<P, R> uncheck(final Func1<P, R> expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param <P>        运行时传入的参数类型
     * @return {@link VoidFuncRt}
     */
    public static <P> VoidFuncRt<P> uncheck(final VoidFunc<P> expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc0} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @return {@link VoidFunc0Rt}
     */
    public static VoidFunc0Rt uncheck(final VoidFunc0 expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc1} 的 Lambda 表达式，当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param <P>        运行时传入的参数类型
     * @return {@link VoidFunc1Rt}
     */
    public static <P> VoidFunc1Rt<P> uncheck(final VoidFunc1<P> expression) {
        return uncheck(expression, RuntimeException::new);
    }

    /**
     * 接收一个可以转化成 {@code Func} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @param <P>          运行时传入的参数类型
     * @param <R>          最终返回的数据类型
     * @return {@link FuncRt}
     */
    public static <P, R> FuncRt<P, R> uncheck(final Func<P, R> expression,
                                              final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return t -> {
            try {
                return expression.call(t);
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code Func0} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @param <R>          最终返回的数据类型
     * @return {@link Func0Rt}
     */
    public static <R> Func0Rt<R> uncheck(final Func0<R> expression,
                                         final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return () -> {
            try {
                return expression.call();
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code Func1} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @param <P>          运行时传入的参数类型
     * @param <R>          最终返回的数据类型
     * @return {@link Func1Rt}
     */
    public static <P, R> Func1Rt<P, R> uncheck(final Func1<P, R> expression,
                                               final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return t -> {
            try {
                return expression.call(t);
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @param <P>          运行时传入的参数类型
     * @return {@link VoidFuncRt}
     */
    public static <P> VoidFuncRt<P> uncheck(final VoidFunc<P> expression,
                                            final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return t -> {
            try {
                expression.call(t);
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc0} 的 Lambda 表达式，和一个 RuntimeException，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression Lambda 表达式
     * @param rte        期望抛出的运行时异常
     * @return {@link VoidFunc0Rt}
     */
    public static VoidFunc0Rt uncheck(final VoidFunc0 expression, final RuntimeException rte) {
        Objects.requireNonNull(expression, "expression can not be null");
        return () -> {
            try {
                expression.call();
            } catch (Exception e) {
                if (rte == null) {
                    throw new RuntimeException(e);
                } else {
                    rte.initCause(e);
                    throw rte;
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc0} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @return {@link VoidFunc0Rt}
     */
    public static VoidFunc0Rt uncheck(final VoidFunc0 expression,
                                      final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return () -> {
            try {
                expression.call();
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 接收一个可以转化成 {@code VoidFunc1} 的 Lambda 表达式，和一个可以把 Exception 转化成 RuntimeException 的表达式，
     * 当执行表达式抛出任何异常时，都会转化成运行时异常。
     *
     * @param expression   Lambda 表达式
     * @param rteSupplier  转化运行时异常的表达式
     * @param <P>          运行时传入的参数类型
     * @return {@link VoidFunc1Rt}
     */
    public static <P> VoidFunc1Rt<P> uncheck(final VoidFunc1<P> expression,
                                             final Supplier1<RuntimeException, Exception> rteSupplier) {
        Objects.requireNonNull(expression, "expression can not be null");
        return t -> {
            try {
                expression.call(t);
            } catch (Exception e) {
                if (rteSupplier == null) {
                    throw new RuntimeException(e);
                } else {
                    throw rteSupplier.get(e);
                }
            }
        };
    }

    /**
     * 可变参数 Func 的运行时异常版本。
     *
     * @param <P> 参数类型
     * @param <R> 返回类型
     */
    public interface FuncRt<P, R> extends Func<P, R> {
        @SuppressWarnings("unchecked")
        @Override
        R call(P... parameters) throws RuntimeException;
    }

    /**
     * 无参 Func 的运行时异常版本。
     *
     * @param <R> 返回类型
     */
    public interface Func0Rt<R> extends Func0<R> {
        @Override
        R call() throws RuntimeException;
    }

    /**
     * 单参 Func 的运行时异常版本。
     *
     * @param <P> 参数类型
     * @param <R> 返回类型
     */
    public interface Func1Rt<P, R> extends Func1<P, R> {
        @Override
        R call(P parameter) throws RuntimeException;
    }

    /**
     * 可变参数 VoidFunc 的运行时异常版本。
     *
     * @param <P> 参数类型
     */
    public interface VoidFuncRt<P> extends VoidFunc<P> {
        @SuppressWarnings("unchecked")
        @Override
        void call(P... parameters) throws RuntimeException;
    }

    /**
     * 无参 VoidFunc 的运行时异常版本。
     */
    public interface VoidFunc0Rt extends VoidFunc0 {
        @Override
        void call() throws RuntimeException;
    }

    /**
     * 单参 VoidFunc 的运行时异常版本。
     *
     * @param <P> 参数类型
     */
    public interface VoidFunc1Rt<P> extends VoidFunc1<P> {
        @Override
        void call(P parameter) throws RuntimeException;
    }
}
