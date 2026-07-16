package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

import java.lang.management.ManagementFactory;

/**
 * 进程ID单例封装.
 * <p>
 * 第一次访问时调用 {@link ManagementFactory#getRuntimeMXBean()} 获取PID信息，之后直接使用缓存值。
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Pid}。
 *
 * @author looly
 */
public enum Pid {

    /**
     * 单例实例.
     */
    INSTANCE;

    private final int pid;

    Pid() {
        this.pid = getPid();
    }

    /**
     * 获取PID值.
     *
     * @return pid
     */
    public int get() {
        return this.pid;
    }

    /**
     * 获取当前进程ID.
     *
     * @return 进程ID
     * @throws UtilException 进程名称为空
     */
    private static int getPid() throws UtilException {
        final String processName = ManagementFactory.getRuntimeMXBean().getName();
        if (StrUtil.isBlank(processName)) {
            throw new UtilException("Process name is blank!");
        }
        final int atIndex = processName.indexOf('@');
        if (atIndex > 0) {
            return Integer.parseInt(processName.substring(0, atIndex));
        } else {
            return processName.hashCode();
        }
    }
}
