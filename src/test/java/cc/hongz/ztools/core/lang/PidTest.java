package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Pid} 单元测试.
 * <p>
 * 验证进程 ID 获取与缓存.
 *
 * @author hongzhe.qin
 */
@DisplayName("Pid 进程 ID")
class PidTest {

    /**
     * 验证 {@link Pid#get()} 返回并缓存进程 ID.
     */
    @Test
    @DisplayName("获取 PID")
    void getPid() {
        final int pid = Pid.INSTANCE.get();
        assertEquals(pid, Pid.INSTANCE.get());
    }
}
