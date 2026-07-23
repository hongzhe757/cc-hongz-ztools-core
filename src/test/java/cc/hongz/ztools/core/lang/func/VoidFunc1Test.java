package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link VoidFunc1} 单元测试
 */
public class VoidFunc1Test {

	@Test
	public void callTest() throws Exception {
		AtomicReference<String> ref = new AtomicReference<>();
		VoidFunc1<String> func = ref::set;
		func.call("hello");
		assertEquals("hello", ref.get());
	}

	@Test
	public void callWithRuntimeExceptionTest() {
		VoidFunc1<String> func = (param) -> {
			throw new Exception("test exception");
		};
		assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
	}

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
