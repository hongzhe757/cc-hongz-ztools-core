package cc.hongz.ztools.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CallerUtilTest {

	@Test
	public void getCallerMethodNameTest() {
		final String callerMethodName = CallerUtil.getCallerMethodName(false);
		assertEquals("getCallerMethodNameTest", callerMethodName);

		final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
		assertEquals("cc.hongz.ztools.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
	}
}
