package cc.hongz.ztools.core.lang.loader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link LazyFunLoader} 单元测试.
 *
 * <p>迁移自 hutool {@code cn.hutool.core.lang.loader.LazyFunLoaderTest}。
 */
class LazyFunLoaderTest {

	static class BigObject {

		private boolean isDestroy = false;

		public void destroy() {
			this.isDestroy = true;
		}
	}

	@Test
	void testLazyLoad() {
		LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

		assertNotNull(loader.get());
		assertTrue(loader.isInitialize());

		// 对于某些对象，在程序关闭时，需要进行销毁操作
		loader.ifInitialized(BigObject::destroy);

		assertTrue(loader.get().isDestroy);
	}

	@Test
	void testNotInitialized() {
		LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

		// 若从未使用，则可以避免不必要的初始化
		loader.ifInitialized(it -> {
			fail();
			it.destroy();
		});

		assertFalse(loader.isInitialize());
	}

	@Test
	void testOnStaticFactoryMethod() {
		LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

		assertNotNull(loader.get());
		assertTrue(loader.isInitialize());

		// 对于某些对象，在程序关闭时，需要进行销毁操作
		loader.ifInitialized(BigObject::destroy);

		assertTrue(loader.get().isDestroy);
	}

	@Test
	void testOnStaticFactoryMethodNotInitialized() {
		LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

		// 若从未使用，则可以避免不必要的初始化
		loader.ifInitialized(it -> {
			fail();
			it.destroy();
		});
		assertFalse(loader.isInitialize());
	}

	@Test
	void testOnNullSupplierThrows() {
		assertThrows(IllegalArgumentException.class, () -> LazyFunLoader.on(null));
	}
}
