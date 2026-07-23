package cc.hongz.ztools.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * {@link StackTraceCaller} 单元测试
 */
public class StackTraceCallerTest {

	private final StackTraceCaller caller = new StackTraceCaller();

	@Test
	public void getCallerTest() {
		Class<?> callerClass = caller.getCaller();
		assertEquals(this.getClass(), callerClass);
	}

	@Test
	public void getCallerCallerTest() {
		// 通过内部类间接调用，验证 getCallerCaller 能获取到外层类
		Class<?> callerCaller = InnerHelper.getCallerCaller(new StackTraceCaller());
		assertEquals(this.getClass(), callerCaller);
	}

	@Test
	public void getCallerWithDepthTest() {
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
		static Class<?> getCallerCaller(StackTraceCaller stc) {
			return stc.getCallerCaller();
		}
	}
}
