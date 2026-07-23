package cc.hongz.ztools.core.lang.copier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cc.hongz.ztools.core.lang.Filter;

/**
 * {@link Copier} 和 {@link SrcToDestCopier} 单元测试
 */
public class CopierTest {

	@Test
	public void copierInterfaceTest() {
		// 测试 Copier 函数式接口
		Copier<String> copier = () -> "hello";
		assertEquals("hello", copier.copy());
	}

	@Test
	public void srcToDestCopierGetSetTest() {
		TestCopier copier = new TestCopier();

		// 测试 setSrc / getSrc
		copier.setSrc("source");
		assertEquals("source", copier.getSrc());

		// 测试 setDest / getDest
		copier.setDest("dest");
		assertEquals("dest", copier.getDest());

		// 测试流式编程（返回自身）
		TestCopier result = copier.setSrc("newSource");
		assertSame(copier, result);
	}

	@Test
	public void srcToDestCopierCopyFilterTest() {
		TestCopier copier = new TestCopier();

		// 默认过滤器为 null
		assertNull(copier.getCopyFilter());

		// 设置过滤器
		Filter<String> filter = t -> t != null;
		copier.setCopyFilter(filter);
		assertSame(filter, copier.getCopyFilter());

		// 流式返回
		TestCopier result = copier.setCopyFilter(filter);
		assertSame(copier, result);
	}

	@Test
	public void srcToDestCopierCopyTest() {
		TestCopier copier = new TestCopier();
		copier.setSrc("sourceValue");
		copier.setDest("destValue");

		// copy 返回 src
		assertEquals("sourceValue", copier.copy());
	}

	@Test
	public void srcToDestCopierFluentApiTest() {
		// 测试链式调用
		TestCopier copier = new TestCopier()
				.setSrc("src")
				.setDest("dst")
				.setCopyFilter(t -> true);

		assertEquals("src", copier.getSrc());
		assertEquals("dst", copier.getDest());
		assertNotNull(copier.getCopyFilter());
	}

	/**
	 * 具体实现用于测试
	 */
	private static class TestCopier extends SrcToDestCopier<String, TestCopier> {
		@Override
		public String copy() {
			return src;
		}
	}
}
