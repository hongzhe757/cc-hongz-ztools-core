package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComparatorException 单元测试.
 *
 * @author looly
 */
class ComparatorExceptionTest {

	@Test
	void withMessage() {
		ComparatorException ex = new ComparatorException("test error");
		assertEquals("test error", ex.getMessage());
	}

	@Test
	void withTemplate() {
		ComparatorException ex = new ComparatorException("error: {}", "detail");
		assertEquals("error: detail", ex.getMessage());
	}

	@Test
	void withCause() {
		Exception cause = new RuntimeException("cause");
		ComparatorException ex = new ComparatorException(cause);
		assertNotNull(ex.getCause());
		assertEquals("cause", ex.getCause().getMessage());
	}

	@Test
	void withMessageAndCause() {
		Exception cause = new RuntimeException("root cause");
		ComparatorException ex = new ComparatorException("wrapper message", cause);
		assertEquals("wrapper message", ex.getMessage());
		assertSame(cause, ex.getCause());
	}

	@Test
	void withTemplateAndCause() {
		Exception cause = new RuntimeException("root");
		ComparatorException ex = new ComparatorException(cause, "error: {}", "param1");
		assertEquals("error: param1", ex.getMessage());
		assertSame(cause, ex.getCause());
	}

	@Test
	void isRuntimeException() {
		ComparatorException ex = new ComparatorException("test");
		assertInstanceOf(RuntimeException.class, ex);
	}

	@Test
	void withMultipleParams() {
		ComparatorException ex = new ComparatorException("{} and {} and {}", "a", "b", "c");
		assertEquals("a and b and c", ex.getMessage());
	}
}
