package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link Func} 单元测试
 */
public @DisplayName("Func 函数对象单元测试")
class FuncTest {

    @DisplayName("执行函数")
    @Test
    public void callTest() throws Exception {
        Func<Integer, Integer> func = (params) -> {
            int sum = 0;
            for (Integer p : params) {
                sum += p;
            }
            return sum;
        };
        assertEquals(6, func.call(1, 2, 3));
    }

    @DisplayName("异常包装为 RuntimeException")
    @Test
    public void callWithRuntimeExceptionTest() {
        Func<String, String> func = (params) -> {
            throw new Exception("test exception");
        };
        assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
    }

    @DisplayName("已是 RuntimeException 不再包装")
    @Test
    public void callWithRuntimeExceptionAlreadyRuntimeTest() {
        RuntimeException re = new RuntimeException("already runtime");
        Func<String, String> func = (params) -> {
            throw re;
        };
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
        assertSame(re, thrown);
    }
}
