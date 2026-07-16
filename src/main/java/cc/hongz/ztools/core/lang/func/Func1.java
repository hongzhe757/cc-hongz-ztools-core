package cc.hongz.ztools.core.lang.func;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;

import java.io.Serializable;

/**
 * 只有一个参数的函数对象.
 * <p>
 * 接口灵感来自于 <a href="http://actframework.org/">ActFramework</a>.
 * 一个函数接口代表一个函数，用于包装一个函数为对象.
 *
 * @param <P> 参数类型
 * @param <R> 返回值类型
 * @author looly
 */
@FunctionalInterface
public interface Func1<P, R> extends Serializable {

    /**
     * 执行函数.
     *
     * @param parameter 参数
     * @return 函数执行结果
     * @throws Exception 自定义异常
     */
    R call(P parameter) throws Exception;

    /**
     * 执行函数，异常包装为 RuntimeException.
     *
     * @param parameter 参数
     * @return 函数执行结果
     */
    default R callWithRuntimeException(final P parameter) {
        try {
            return call(parameter);
        } catch (final Exception e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }
}
