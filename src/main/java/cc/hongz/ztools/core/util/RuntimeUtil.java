package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Pid;

/**
 * 系统运行时工具类（最小化版本）.
 * <p>
 * 仅迁移 {@code getPid()} 方法供 IdUtil 使用。
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.RuntimeUtil}。
 *
 * @author Looly
 */
public final class RuntimeUtil {

    private RuntimeUtil() {
    }

    /**
     * 获取当前进程ID.
     *
     * @return 进程ID
     * @throws UtilException 获取失败时
     */
    public static int getPid() throws UtilException {
        return Pid.INSTANCE.get();
    }
}
