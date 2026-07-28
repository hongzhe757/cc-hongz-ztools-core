package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceLoaderUtil 单元测试.
 */
class ServiceLoaderUtilTest {

    // ==================== loadFirst 测试 ====================

    @Test
    @DisplayName("loadFirst 无 SPI 服务应返回 null")
    void testLoadFirstNoService() {
        final Object result = ServiceLoaderUtil.loadFirst(Runnable.class);
        assertNull(result);
    }

    @Test
    @DisplayName("loadFirst 有 SPI 服务应返回非空实例")
    void testLoadFirstFound() {
        final FileSystemProvider result = ServiceLoaderUtil.loadFirst(FileSystemProvider.class);
        assertNotNull(result);
    }

    // ==================== loadFirstAvailable 测试 ====================

    @Test
    @DisplayName("loadFirstAvailable 无 SPI 服务应返回 null")
    void testLoadFirstAvailableNoService() {
        final Object result = ServiceLoaderUtil.loadFirstAvailable(Runnable.class);
        assertNull(result);
    }

    @Test
    @DisplayName("loadFirstAvailable 有 SPI 服务应返回非空实例")
    void testLoadFirstAvailableFound() {
        final FileSystemProvider result = ServiceLoaderUtil.loadFirstAvailable(FileSystemProvider.class);
        assertNotNull(result);
    }

    // ==================== load 测试 ====================

    @Test
    @DisplayName("load 无 SPI 服务应返回非 null 的 ServiceLoader")
    void testLoadNotNull() {
        final ServiceLoader<Runnable> loader = ServiceLoaderUtil.load(Runnable.class);
        assertNotNull(loader);
    }

    @Test
    @DisplayName("load 有 SPI 服务应返回包含实现的 ServiceLoader")
    void testLoadHasElements() {
        final ServiceLoader<FileSystemProvider> loader = ServiceLoaderUtil.load(FileSystemProvider.class);
        assertNotNull(loader);
        assertTrue(loader.iterator().hasNext());
    }

    @Test
    @DisplayName("load 无 SPI 服务指定 ClassLoader 应返回非 null")
    void testLoadWithClassLoader() {
        final ServiceLoader<Runnable> loader =
                ServiceLoaderUtil.load(Runnable.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(loader);
    }

    @Test
    @DisplayName("load 有 SPI 服务指定 ClassLoader 应返回包含实现的 ServiceLoader")
    void testLoadWithClassLoaderFound() {
        final ServiceLoader<FileSystemProvider> loader =
                ServiceLoaderUtil.load(FileSystemProvider.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(loader);
        assertTrue(loader.iterator().hasNext());
    }

    // ==================== loadList 测试 ====================

    @Test
    @DisplayName("loadList 无 SPI 服务应返回空列表")
    void testLoadListEmpty() {
        final List<Runnable> list = ServiceLoaderUtil.loadList(Runnable.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("loadList 有 SPI 服务应返回非空列表")
    void testLoadListNonEmpty() {
        final List<FileSystemProvider> list = ServiceLoaderUtil.loadList(FileSystemProvider.class);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("loadList 无 SPI 服务指定 ClassLoader 应返回空列表")
    void testLoadListWithClassLoader() {
        final List<Runnable> list =
                ServiceLoaderUtil.loadList(Runnable.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("loadList 有 SPI 服务指定 ClassLoader 应返回非空列表")
    void testLoadListWithClassLoaderNonEmpty() {
        final List<FileSystemProvider> list =
                ServiceLoaderUtil.loadList(FileSystemProvider.class, Thread.currentThread().getContextClassLoader());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}
