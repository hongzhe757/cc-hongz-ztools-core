package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.io.resource.BytesResource;
import cc.hongz.ztools.core.io.resource.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link ResourceClassLoader} 单元测试.
 * <p>
 * 覆盖 addResource 链式返回及加载未知类时抛出 ClassNotFoundException.
 *
 * @author hongzhe.qin
 */
@DisplayName("ResourceClassLoader 资源类加载器")
class ResourceClassLoaderTest {

    /**
     * 验证 {@link ResourceClassLoader#addResource(Resource)} 返回 this 以支持链式调用.
     */
    @Test
    @DisplayName("addResource 链式返回")
    void addResourceReturnsThis() {
        final ResourceClassLoader<Resource> loader = new ResourceClassLoader<>(null, new HashMap<>());
        final Resource resource = new BytesResource(new byte[0], "test.Dummy");
        assertSame(loader, loader.addResource(resource));
    }

    /**
     * 验证加载资源中不存在的类时委托给父加载器并抛出 ClassNotFoundException.
     */
    @Test
    @DisplayName("加载未知类抛异常")
    void loadUnknownClassThrows() {
        final Map<String, Resource> map = new HashMap<>();
        final ResourceClassLoader<Resource> loader = new ResourceClassLoader<>(null, map);
        assertThrows(ClassNotFoundException.class, () -> loader.loadClass("nonexistent.Dummy"));
    }
}
