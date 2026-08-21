package cc.hongz.ztools.core.lang.func;

import cc.hongz.ztools.core.util.ClassUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda 相关工具类.
 *
 * @author looly
 * @author VampireAchao
 */
public final class LambdaUtil {

    private static final Map<String, SerializedLambda> CACHE = new ConcurrentHashMap<>();

    private LambdaUtil() {
    }

    /**
     * 解析 lambda 表达式，加了缓存.
     *
     * @param <T>  Lambda 类型
     * @param func 需要解析的 lambda 对象
     * @return 返回解析后的结果
     */
    public static <T> SerializedLambda resolve(final Func1<T, ?> func) {
        return resolve0(func);
    }

    /**
     * 通过对象的方法或类的静态方法引用，获取 lambda 实现类.
     * <p>
     * 传入 lambda 有参数且含有返回值的情况能够匹配到此方法：
     * <ul>
     * <li>引用特定类型的任意对象的实例方法</li>
     * <li>引用静态带参方法</li>
     * </ul>
     *
     * @param func lambda
     * @param <P>  方法调用方类型
     * @param <R>  返回值类型
     * @return lambda 实现类
     * @throws IllegalArgumentException 如果是不支持的方法引用，抛出该异常
     */
    public static <P, R> Class<P> getRealClass(final Func1<P, R> func) {
        final SerializedLambda lambda = resolve(func);
        checkLambdaTypeCanGetClass(lambda.getImplMethodKind());
        final String instantiatedMethodType = lambda.getInstantiatedMethodType();
        // 从方法类型描述符中提取类名，如 "(Lcom/example/MyEnum;)Ljava/lang/Object;"
        // 取 '(' 后2个字符（跳过 "(L"）到 ';' 之间的部分
        final int semiIdx = StrUtil.indexOf(instantiatedMethodType, ';');
        final String internalName = instantiatedMethodType.substring(2, semiIdx);
        return ClassUtil.loadClass(internalName.replace('/', '.'));
    }

    /**
     * 获取 lambda 表达式函数（方法）名称.
     *
     * @param <P>  Lambda 参数类型
     * @param func 函数
     * @return 方法名称
     */
    public static <P> String getMethodName(final Func1<P, ?> func) {
        return resolve(func).getImplMethodName();
    }

    // region Private methods

    /**
     * 检查是否为支持的类型.
     *
     * @param implMethodKind 支持的 lambda 类型
     * @throws IllegalArgumentException 如果是不支持的方法引用，抛出该异常
     */
    private static void checkLambdaTypeCanGetClass(final int implMethodKind) {
        if (implMethodKind != MethodHandleInfo.REF_invokeVirtual
                && implMethodKind != MethodHandleInfo.REF_invokeStatic) {
            throw new IllegalArgumentException("该 lambda 不是合适的方法引用");
        }
    }

    /**
     * 解析 lambda 表达式，加了缓存.
     *
     * @param func 需要解析的 lambda 对象
     * @return 返回解析后的结果
     */
    private static SerializedLambda resolve0(final Serializable func) {
        return CACHE.computeIfAbsent(func.getClass().getName(),
                (key) -> ReflectUtil.invoke(func, "writeReplace"));
    }

    // endregion
}
