package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceLoaderUtil 单元测试.
 */
class ServiceLoaderUtilTest {

    /**
     * 测试 loadFirst 加载不存在的 SPI 服务返回 null.
     */
    @Test
    void testLoadFirstNoService() {
        // Runnable 没有 SPI 配置，应返回 null
        final Object result = ServiceLoaderUtil.loadFirst(Runnable.class);
        assertNull(result);
    }

    /**
     * 测试 loadFirstAvailable 加载不存在的 SPI 服务返回 null.
     */
    @Test
    void testLoadFirstAvailableNoService() {
        final Object result = ServiceLoaderUtil.loadFirstAvailable(Runnable.class);
        assertNull(result);
    }

    /**
     * 测试 load 返回 ServiceLoader 不为 null.
     */
    @Test
    void testLoadNotNull() {
        final ServiceLoader<Runnable> loader = ServiceLoaderUtil.load(Runnable.class);
        assertNotNull(loader);
    }

    /**
     * 测试 load 指定 ClassLoader.
     */
    @Test
    void testLoadWithClassLoader() {
        final ServiceLoader<Runnable> loader =
                ServiceLoaderUtil.load(Runnable.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(loader);
    }

    /**
     * 测试 loadList 返回空列表（无 SPI 配置时）.
     */
    @Test
    void testLoadListEmpty() {
        final List<Runnable> list = ServiceLoaderUtil.loadList(Runnable.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * 测试 loadList 指定 ClassLoader.
     */
    @Test
    void testLoadListWithClassLoader() {
        final List<Runnable> list =
                ServiceLoaderUtil.loadList(Runnable.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
