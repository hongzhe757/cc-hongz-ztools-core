package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ThreadException 线程异常")
class ThreadExceptionTest {

	@Test
	@DisplayName("构造 - Throwable")
	void constructWithThrowable() {
		RuntimeException cause = new RuntimeException("cause");
		ThreadException ex = new ThreadException(cause);
		assertNotNull(ex.getMessage());
		assertSame(cause, ex.getCause());
	}

	@Test
	@DisplayName("构造 - String message")
	void constructWithMessage() {
		ThreadException ex = new ThreadException("test message");
		assertEquals("test message", ex.getMessage());
	}

	@Test
	@DisplayName("构造 - 模板消息")
	void constructWithTemplate() {
		ThreadException ex = new ThreadException("error: {}", "detail");
		assertEquals("error: detail", ex.getMessage());
	}

	@Test
	@DisplayName("构造 - message + throwable")
	void constructWithMessageAndThrowable() {
		RuntimeException cause = new RuntimeException("cause");
		ThreadException ex = new ThreadException("msg", cause);
		assertEquals("msg", ex.getMessage());
		assertSame(cause, ex.getCause());
	}

	@Test
	@DisplayName("构造 - 完整参数")
	void constructWithFullParams() {
		RuntimeException cause = new RuntimeException("cause");
		ThreadException ex = new ThreadException("msg", cause, true, true);
		assertEquals("msg", ex.getMessage());
		assertSame(cause, ex.getCause());
	}

	@Test
	@DisplayName("构造 - throwable + 模板消息")
	void constructWithThrowableAndTemplate() {
		RuntimeException cause = new RuntimeException("cause");
		ThreadException ex = new ThreadException(cause, "error: {}", "detail");
		assertEquals("error: detail", ex.getMessage());
		assertSame(cause, ex.getCause());
	}
}
