package cc.hongz.ztools.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CallerUtil} 单元测试
 * @author Looly
 *
 */
public @DisplayName("Caller 接口单元测试")
class CallerTest {

    @DisplayName("获得调用者")
    @Test
    public void getCallerTest() {
        Class<?> caller = CallerUtil.getCaller();
        assertEquals(this.getClass(), caller);

        Class<?> caller0 = CallerUtil.getCaller(0);
        assertEquals(CallerUtil.class, caller0);

        Class<?> caller1 = CallerUtil.getCaller(1);
        assertEquals(this.getClass(), caller1);
    }

    @DisplayName("获得调用者的调用者")
    @Test
    public void getCallerCallerTest() {
        Class<?> callerCaller = CallerTestClass.getCaller();
        assertEquals(this.getClass(), callerCaller);
    }

    @DisplayName("是否被指定类调用")
    @Test
    public void isCalledByTest() {
        // 当前测试方法调用了 CallerUtil.isCalledBy，应返回 true
        assertTrue(CallerUtil.isCalledBy(this.getClass()));
        // 未调用 CallerUtil 的类不应在调用栈中
        assertFalse(CallerUtil.isCalledBy(String.class));
    }

    @DisplayName("层级越界")
    @Test
    public void getCallerDepthOutOfRangeTest() {
        // 超大 depth 应返回 null
        Class<?> caller = CallerUtil.getCaller(999);
        assertNull(caller);
    }

    private static class CallerTestClass{
        public static Class<?> getCaller(){
            return CallerUtil.getCallerCaller();
        }
    }
}
