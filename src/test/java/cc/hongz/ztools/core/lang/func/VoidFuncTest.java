package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link VoidFunc} 单元测试
 */
public class VoidFuncTest {

	@Test
	public void callTest() throws Exception {
		AtomicBoolean called = new AtomicBoolean(false);
		VoidFunc<String> func = (params) -> called.set(true);
		func.call("a");
		assertTrue(called.get());
	}

	@Test
	public void callWithRuntimeExceptionTest() {
		VoidFunc<String> func = (params) -> {
			throw new Exception("test exception");
		};
		assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
	}

	@Test
	public void callWithRuntimeExceptionAlreadyRuntimeTest() {
		RuntimeException re = new RuntimeException("already runtime");
		VoidFunc<String> func = (params) -> {
			throw re;
		};
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
		assertSame(re, thrown);
	}
}
