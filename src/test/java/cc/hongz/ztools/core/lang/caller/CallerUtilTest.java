package cc.hongz.ztools.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("CallerUtil 单元测试")
class CallerUtilTest {

    @DisplayName("获取调用方法名")
    @Test
    public void getCallerMethodNameTest() {
        final String callerMethodName = CallerUtil.getCallerMethodName(false);
        assertEquals("getCallerMethodNameTest", callerMethodName);

        final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
        assertEquals("cc.hongz.ztools.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
    }
}
