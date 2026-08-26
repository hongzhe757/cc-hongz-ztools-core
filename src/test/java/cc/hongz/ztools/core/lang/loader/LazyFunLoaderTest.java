package cc.hongz.ztools.core.lang.loader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link LazyFunLoader} 单元测试.
 *
 * <p>迁移自 hutool {@code cn.hutool.core.lang.loader.LazyFunLoaderTest}。
 */
@DisplayName("LazyFunLoader 单元测试")
class LazyFunLoaderTest {

    static class BigObject {

        private boolean isDestroy = false;

        public void destroy() {
            this.isDestroy = true;
        }
    }

    @DisplayName("懒加载")
    @Test
    void testLazyLoad() {
        LazyFunLoader<BigObject> loader = new LazyFunLoader<>(BigObject::new);

        assertNotNull(loader.get());
        assertTrue(loader.isInitialize());

        // 对于某些对象，在程序关闭时，需要进行销毁操作
        loader.ifInitialized(BigObject::destroy);

        assertTrue(loader.get().isDestroy);
    }

    @DisplayName("未初始化")
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

    @DisplayName("on 静态工厂方法")
    @Test
    void testOnStaticFactoryMethod() {
        LazyFunLoader<BigObject> loader = LazyFunLoader.on(BigObject::new);

        assertNotNull(loader.get());
        assertTrue(loader.isInitialize());

        // 对于某些对象，在程序关闭时，需要进行销毁操作
        loader.ifInitialized(BigObject::destroy);

        assertTrue(loader.get().isDestroy);
    }

    @DisplayName("on 静态工厂未初始化")
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

    @DisplayName("on null supplier 抛异常")
    @Test
    void testOnNullSupplierThrows() {
        assertThrows(IllegalArgumentException.class, () -> LazyFunLoader.on(null));
    }
}
