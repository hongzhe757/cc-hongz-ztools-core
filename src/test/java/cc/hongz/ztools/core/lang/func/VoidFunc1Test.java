package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link VoidFunc1} 单元测试
 */
public @DisplayName("VoidFunc1 单元测试")
class VoidFunc1Test {

    @DisplayName("执行函数")
    @Test
    public void callTest() throws Exception {
        AtomicReference<String> ref = new AtomicReference<>();
        VoidFunc1<String> func = ref::set;
        func.call("hello");
        assertEquals("hello", ref.get());
    }

    @DisplayName("异常包装为 RuntimeException")
    @Test
    public void callWithRuntimeExceptionTest() {
        VoidFunc1<String> func = (param) -> {
            throw new Exception("test exception");
        };
        assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
    }

    @DisplayName("已是 RuntimeException 不再包装")
    @Test
    public void callWithRuntimeExceptionAlreadyRuntimeTest() {
        RuntimeException re = new RuntimeException("already runtime");
        VoidFunc1<String> func = (param) -> {
            throw re;
        };
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
        assertSame(re, thrown);
    }
}
