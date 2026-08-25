package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.io.FastByteArrayOutputStream;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.text.CharPool;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常工具类。
 *
 * @author Looly
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    /**
     * 堆栈转字符串的默认最大长度。
     */
    private static final int DEFAULT_LIMIT = 3000;

    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}。
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(final Throwable e) {
        if (null == e) {
            return StrUtil.NULL;
        }
        return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }

    /**
     * 获得消息，调用异常类的 getMessage 方法。
     *
     * @param e 异常
     * @return 消息
     */
    public static String getSimpleMessage(final Throwable e) {
        return (null == e) ? StrUtil.NULL : e.getMessage();
    }

    /**
     * 使用运行时异常包装编译异常；若已是运行时异常则直接返回。
     *
     * @param throwable 异常
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(throwable);
    }

    /**
     * 将指定的消息包装为运行时异常。
     *
     * @param message 异常消息
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(final String message) {
        return new RuntimeException(message);
    }

    /**
     * 包装一个异常。
     *
     * @param <T>           被包装的异常类型
     * @param throwable     异常
     * @param wrapThrowable 包装后的异常类
     * @return 包装后的异常
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T wrap(final Throwable throwable, final Class<T> wrapThrowable) {
        if (wrapThrowable.isInstance(throwable)) {
            return (T) throwable;
        }
        return ReflectUtil.newInstance(wrapThrowable, throwable);
    }

    /**
     * 包装异常并重新抛出此异常；{@link RuntimeException} 和 {@link Error} 直接抛出，其它检查异常包装为
     * {@link UndeclaredThrowableException} 后抛出。
     *
     * @param throwable 异常
     */
    public static void wrapAndThrow(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        throw new UndeclaredThrowableException(throwable);
    }

    /**
     * 将消息包装为运行时异常并抛出。
     *
     * @param message 异常消息
     */
    public static void wrapRuntimeAndThrow(final String message) {
        throw new RuntimeException(message);
    }

    /**
     * 剥离反射引发的 InvocationTargetException、UndeclaredThrowableException 中间异常，返回业务本身的异常。
     *
     * @param wrapped 包装的异常
     * @return 剥离后的异常
     */
    public static Throwable unwrap(final Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 获取当前栈信息。
     *
     * @return 当前栈信息
     */
    public static StackTraceElement[] getStackElements() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 获取指定层的堆栈信息。
     *
     * @param i 层数
     * @return 指定层的堆栈信息
     */
    public static StackTraceElement getStackElement(final int i) {
        return Thread.currentThread().getStackTrace()[i];
    }

    /**
     * 获取指定层的堆栈信息。
     *
     * @param fqcn 指定类名为基础
     * @param i    指定类名的类堆栈相对层数
     * @return 指定层的堆栈信息
     */
    public static StackTraceElement getStackElement(final String fqcn, final int i) {
        final StackTraceElement[] stackTraceArray = Thread.currentThread().getStackTrace();
        final int index = ArrayUtil.matchIndex((ele) -> StrUtil.equals(fqcn, ele.getClassName()), stackTraceArray);
        if (index > 0) {
            return stackTraceArray[index + i];
        }
        return null;
    }

    /**
     * 获取入口堆栈信息。
     *
     * @return 入口堆栈信息
     */
    public static StackTraceElement getRootStackElement() {
        final StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
        return Thread.currentThread().getStackTrace()[stackElements.length - 1];
    }

    /**
     * 堆栈转为单行完整字符串。
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(final Throwable throwable) {
        return stacktraceToOneLineString(throwable, DEFAULT_LIMIT);
    }

    /**
     * 堆栈转为单行完整字符串。
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(final Throwable throwable, final int limit) {
        Map<Character, String> replaceCharToStrMap = new HashMap<>();
        replaceCharToStrMap.put(CharPool.CR, " ");
        replaceCharToStrMap.put(CharPool.LF, " ");
        replaceCharToStrMap.put(CharPool.TAB, " ");

        return stacktraceToString(throwable, limit, replaceCharToStrMap);
    }

    /**
     * 堆栈转为完整字符串。
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(final Throwable throwable) {
        return stacktraceToString(throwable, DEFAULT_LIMIT);
    }

    /**
     * 堆栈转为完整字符串。
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(final Throwable throwable, final int limit) {
        return stacktraceToString(throwable, limit, null);
    }

    /**
     * 堆栈转为完整字符串。
     *
     * @param throwable           异常对象
     * @param limit               限制最大长度，&lt;0 表示不限制长度
     * @param replaceCharToStrMap 替换字符为指定字符串
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(final Throwable throwable, final int limit,
                                            final Map<Character, String> replaceCharToStrMap) {
        final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));

        final String exceptionStr = baos.toString();
        final int length = exceptionStr.length();
        int resultLimit = limit;
        if (resultLimit < 0 || resultLimit > length) {
            resultLimit = length;
        }

        if (MapUtil.isNotEmpty(replaceCharToStrMap)) {
            final StringBuilder sb = StrUtil.builder();
            char c;
            String value;
            for (int i = 0; i < resultLimit; i++) {
                c = exceptionStr.charAt(i);
                value = replaceCharToStrMap.get(c);
                if (null != value) {
                    sb.append(value);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            if (resultLimit == length) {
                return exceptionStr;
            }
            return exceptionStr.substring(0, resultLimit);
        }
    }

    /**
     * 判断是否由指定异常类引起。
     *
     * @param throwable    异常
     * @param causeClasses 定义的引起异常的类
     * @return 是否由指定异常类引起
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(final Throwable throwable, final Class<? extends Exception>... causeClasses) {
        return null != getCausedBy(throwable, causeClasses);
    }

    /**
     * 获取由指定异常类引起的异常。
     *
     * @param throwable    异常
     * @param causeClasses 定义的引起异常的类
     * @return 由指定异常类引起的异常，未找到返回 null
     */
    @SuppressWarnings("unchecked")
    public static Throwable getCausedBy(final Throwable throwable, final Class<? extends Exception>... causeClasses) {
        Throwable cause = throwable;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeClasses) {
                if (causeClass.isInstance(cause)) {
                    return cause;
                }
            }
            cause = cause.getCause();
        }
        return null;
    }

    /**
     * 判断指定异常是否来自或者包含指定异常。
     *
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @return true 来自或者包含
     */
    public static boolean isFromOrSuppressedThrowable(final Throwable throwable,
                                                      final Class<? extends Throwable> exceptionClass) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, true) != null;
    }

    /**
     * 判断指定异常是否来自或者包含指定异常。
     *
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @param checkCause     判断 cause
     * @return true 来自或者包含
     */
    public static boolean isFromOrSuppressedThrowable(final Throwable throwable,
                                                      final Class<? extends Throwable> exceptionClass,
                                                      final boolean checkCause) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, checkCause) != null;
    }

    /**
     * 转化指定异常为来自或者包含指定异常。
     *
     * @param <T>            异常类型
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @return 结果为 null 不是来自或者包含
     */
    public static <T extends Throwable> T convertFromOrSuppressedThrowable(final Throwable throwable,
                                                                           final Class<T> exceptionClass) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, true);
    }

    /**
     * 转化指定异常为来自或者包含指定异常。
     *
     * @param <T>            异常类型
     * @param throwable      异常
     * @param exceptionClass 定义的引起异常的类
     * @param checkCause     判断 cause
     * @return 结果为 null 不是来自或者包含
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T convertFromOrSuppressedThrowable(final Throwable throwable,
                                                                           final Class<T> exceptionClass,
                                                                           final boolean checkCause) {
        if (throwable == null || exceptionClass == null) {
            return null;
        }
        if (exceptionClass.isAssignableFrom(throwable.getClass())) {
            return (T) throwable;
        }
        if (checkCause) {
            Throwable cause = throwable.getCause();
            if (cause != null && exceptionClass.isAssignableFrom(cause.getClass())) {
                return (T) cause;
            }
        }
        Throwable[] throwables = throwable.getSuppressed();
        if (ArrayUtil.isNotEmpty(throwables)) {
            for (Throwable throwable1 : throwables) {
                if (exceptionClass.isAssignableFrom(throwable1.getClass())) {
                    return (T) throwable1;
                }
            }
        }
        return null;
    }

    /**
     * 获取异常链上所有异常的集合，如果 {@link Throwable} 对象没有 cause，返回只有一个节点的 List；
     * 如果传入 null，返回空集合。
     *
     * @param throwable 异常对象，可以为 null
     * @return 异常链中所有异常集合
     */
    public static List<Throwable> getThrowableList(final Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        Throwable t = throwable;
        while (t != null && false == list.contains(t)) {
            list.add(t);
            t = t.getCause();
        }
        return list;
    }

    /**
     * 获取异常链中最尾端的异常，即异常最早发生的异常对象；传入 null 返回也为 null。
     *
     * @param throwable 异常对象，可能为 null
     * @return 最尾端异常，传入 null 参数返回也为 null
     */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.size() < 1 ? null : list.get(list.size() - 1);
    }

    /**
     * 获取异常链中最尾端的异常的消息，消息格式为：{SimpleClassName}: {ThrowableMessage}。
     *
     * @param th 异常
     * @return 消息
     */
    public static String getRootCauseMessage(final Throwable th) {
        return getMessage(getRootCause(th));
    }
}
