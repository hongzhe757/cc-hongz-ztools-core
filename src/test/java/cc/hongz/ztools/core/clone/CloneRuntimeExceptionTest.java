package cc.hongz.ztools.core.clone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CloneRuntimeException 克隆异常")
class CloneRuntimeExceptionTest {

	@Test
	@DisplayName("构造方法 - Throwable")
	void constructWithThrowable() {
		Exception cause = new RuntimeException("cause");
		CloneRuntimeException e = new CloneRuntimeException(cause);
		assertNotNull(e.getMessage());
		assertSame(cause, e.getCause());
	}

	@Test
	@DisplayName("构造方法 - String message")
	void constructWithMessage() {
		CloneRuntimeException e = new CloneRuntimeException("test message");
		assertEquals("test message", e.getMessage());
	}

	@Test
	@DisplayName("构造方法 - messageTemplate + params")
	void constructWithTemplate() {
		CloneRuntimeException e = new CloneRuntimeException("hello {}", "world");
		assertEquals("hello world", e.getMessage());
	}

	@Test
	@DisplayName("构造方法 - message + Throwable")
	void constructWithMessageAndThrowable() {
		Throwable cause = new RuntimeException("cause");
		CloneRuntimeException e = new CloneRuntimeException("msg", cause);
		assertEquals("msg", e.getMessage());
		assertSame(cause, e.getCause());
	}

	@Test
	@DisplayName("构造方法 - Throwable + messageTemplate + params")
	void constructWithThrowableAndTemplate() {
		Throwable cause = new RuntimeException("cause");
		CloneRuntimeException e = new CloneRuntimeException(cause, "err: {}", "detail");
		assertEquals("err: detail", e.getMessage());
		assertSame(cause, e.getCause());
	}
}
