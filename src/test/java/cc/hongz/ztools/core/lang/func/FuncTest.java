package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * {@link Func} 单元测试
 */
public class FuncTest {

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

	@Test
	public void callWithRuntimeExceptionTest() {
		Func<String, String> func = (params) -> {
			throw new Exception("test exception");
		};
		assertThrows(RuntimeException.class, () -> func.callWithRuntimeException("a"));
	}

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
