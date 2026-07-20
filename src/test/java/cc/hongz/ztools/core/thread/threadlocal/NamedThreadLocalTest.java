package cc.hongz.ztools.core.thread.threadlocal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NamedThreadLocal 命名ThreadLocal")
class NamedThreadLocalTest {

	@Test
	@DisplayName("toString - 返回name")
	void toStringReturnsName() {
		NamedThreadLocal<String> tl = new NamedThreadLocal<>("myThreadLocal");
		assertEquals("myThreadLocal", tl.toString());
	}

	@Test
	@DisplayName("set/get - 正常存取")
	void setAndGet() {
		NamedThreadLocal<String> tl = new NamedThreadLocal<>("test");
		tl.set("hello");
		assertEquals("hello", tl.get());
		tl.remove();
	}
}
