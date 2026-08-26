package cc.hongz.ztools.core.lang.caller;

import java.io.Serializable;

import cc.hongz.ztools.core.util.ArrayUtil;

/**
 * {@link SecurityManager} 方式获取调用者.
 *
 * @author Looly
 */
@SuppressWarnings("removal")
public class SecurityManagerCaller extends SecurityManager implements Caller, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 偏移量。
     */
    private static final int OFFSET = 1;

    /**
     * 获得调用者。
     *
     * @return 调用者
     */
    @Override
    public Class<?> getCaller() {
        final Class<?>[] context = getClassContext();
        if (null != context && (OFFSET + 1) < context.length) {
            return context[OFFSET + 1];
        }
        return null;
    }

    /**
     * 获得调用者的调用者。
     *
     * @return 调用者的调用者
     */
    @Override
    public Class<?> getCallerCaller() {
        final Class<?>[] context = getClassContext();
        if (null != context && (OFFSET + 2) < context.length) {
            return context[OFFSET + 2];
        }
        return null;
    }

    /**
     * 获得指定层级的调用者。
     *
     * @param depth 层级
     * @return 第几级调用者
     */
    @Override
    public Class<?> getCaller(final int depth) {
        final Class<?>[] context = getClassContext();
        if (null != context && (OFFSET + depth) < context.length) {
            return context[OFFSET + depth];
        }
        return null;
    }

    /**
     * 是否被指定类调用。
     *
     * @param clazz 调用者类
     * @return 是否被调用
     */
    @Override
    public boolean isCalledBy(final Class<?> clazz) {
        final Class<?>[] classes = getClassContext();
        if (ArrayUtil.isNotEmpty(classes)) {
            for (Class<?> contextClass : classes) {
                if (contextClass.equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }
}
