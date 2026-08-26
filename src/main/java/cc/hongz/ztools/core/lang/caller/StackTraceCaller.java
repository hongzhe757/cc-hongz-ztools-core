package cc.hongz.ztools.core.lang.caller;

import java.io.Serializable;

import cc.hongz.ztools.core.exceptions.UtilException;

/**
 * 通过StackTrace方式获取调用者。此方式效率最低，不推荐使用.
 *
 * @author Looly
 */
public class StackTraceCaller implements Caller, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 偏移量。
     */
    private static final int OFFSET = 2;

    /**
     * 获得调用者。
     *
     * @return 调用者
     */
    @Override
    public Class<?> getCaller() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (OFFSET + 1 >= stackTrace.length) {
            return null;
        }
        final String className = stackTrace[OFFSET + 1].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    /**
     * 获得调用者的调用者。
     *
     * @return 调用者的调用者
     */
    @Override
    public Class<?> getCallerCaller() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (OFFSET + 2 >= stackTrace.length) {
            return null;
        }
        final String className = stackTrace[OFFSET + 2].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    /**
     * 获得指定层级的调用者。
     *
     * @param depth 层级
     * @return 第几级调用者
     */
    @Override
    public Class<?> getCaller(final int depth) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (OFFSET + depth >= stackTrace.length) {
            return null;
        }
        final String className = stackTrace[OFFSET + depth].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    /**
     * 是否被指定类调用。
     *
     * @param clazz 调用者类
     * @return 是否被调用
     */
    @Override
    public boolean isCalledBy(final Class<?> clazz) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (final StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }
}
