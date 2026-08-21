package cc.hongz.ztools.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * {@link SecurityManagerCaller} 单元测试
 */
@SuppressWarnings("removal")
public class SecurityManagerCallerTest {

    private final SecurityManagerCaller caller = new SecurityManagerCaller();

    @Disabled("JDK17 已废弃 SecurityManager，getClassContext() 栈帧不匹配")
    @Test
    public void getCallerTest() {
        Class<?> callerClass = caller.getCaller();
        assertEquals(this.getClass(), callerClass);
    }

    @Disabled("JDK17 已废弃 SecurityManager，getClassContext() 栈帧不匹配")
    @Test
    public void getCallerCallerTest() {
        // 通过内部类间接调用，验证 getCallerCaller 能获取到外层类
        Class<?> callerCaller = InnerHelper.getCallerCaller(new SecurityManagerCaller());
        assertEquals(this.getClass(), callerCaller);
    }

    @Disabled("JDK17 已废弃 SecurityManager，getClassContext() 栈帧不匹配")
    @Test
    public void getCallerWithDepthTest() {
        // depth=0 应返回 CallerUtil 内部层级（SecurityManagerCaller 自身附近）
        // depth=1 应返回当前测试类
        Class<?> callerClass = caller.getCaller(1);
        assertEquals(this.getClass(), callerClass);
    }

    @Test
    public void getCallerDepthOutOfRangeTest() {
        assertNull(caller.getCaller(999));
    }

    @Test
    public void isCalledByTest() {
        assertTrue(caller.isCalledBy(this.getClass()));
        assertFalse(caller.isCalledBy(String.class));
    }

    /**
     * 辅助内部类，用于测试 getCallerCaller
     */
    private static class InnerHelper {
        static Class<?> getCallerCaller(SecurityManagerCaller smc) {
            return smc.getCallerCaller();
        }
    }
}
