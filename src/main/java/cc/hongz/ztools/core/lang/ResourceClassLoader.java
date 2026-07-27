package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.io.resource.Resource;
import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.ObjectUtil;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源类加载器，可以加载任意类型的资源类.
 *
 * @param <T> {@link Resource}接口实现类
 * @author looly, lzpeng
 * @since 5.5.2
 */
public class ResourceClassLoader<T extends Resource>
        extends SecureClassLoader {

    private final Map<String, T> resourceMap;
    private final Map<String, Class<?>> cacheClassMap;

    /**
     * 构造.
     *
     * @param parentClassLoader 父类加载器
     * @param resourceMap       资源map
     */
    public ResourceClassLoader(
            final ClassLoader parentClassLoader,
            final Map<String, T> resourceMap) {
        super(ObjectUtil.defaultIfNull(parentClassLoader,
                ClassLoaderUtil::getClassLoader));
        this.resourceMap = ObjectUtil.defaultIfNull(
                resourceMap, () -> new HashMap<>());
        this.cacheClassMap = new HashMap<>();
    }

    /**
     * 增加需要加载的类资源.
     *
     * @param resource 资源
     * @return this
     */
    public ResourceClassLoader<T> addResource(
            final T resource) {
        this.resourceMap.put(resource.getName(), resource);
        return this;
    }

    @Override
    protected Class<?> findClass(final String name)
            throws ClassNotFoundException {
        final Class<?> clazz = cacheClassMap.computeIfAbsent(
                name, this::defineByName);
        if (clazz == null) {
            return super.findClass(name);
        }
        return clazz;
    }

    /**
     * 从给定资源中读取class的二进制流然后生成类.
     *
     * @param name 类名
     * @return 定义的类
     */
    private Class<?> defineByName(final String name) {
        final Resource resource = resourceMap.get(name);
        if (null != resource) {
            final byte[] bytes = resource.readBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        return null;
    }
}
