package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.io.resource.FileObjectResource;
import cc.hongz.ztools.core.lang.ResourceClassLoader;
import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.ObjectUtil;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.util.HashMap;
import java.util.Map;

/**
 * Java 字节码文件对象管理器.
 *
 * @author lzpeng
 * @since 5.5.2
 */
class JavaClassFileManager
        extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, FileObjectResource> classFileObjectMap =
            new HashMap<>();
    private final ClassLoader parent;

    /**
     * 构造.
     *
     * @param parent      父类加载器
     * @param fileManager 字节码文件管理器
     */
    protected JavaClassFileManager(final ClassLoader parent,
            final JavaFileManager fileManager) {
        super(fileManager);
        this.parent = ObjectUtil.defaultIfNull(parent,
                ClassLoaderUtil::getClassLoader);
    }

    @Override
    public ClassLoader getClassLoader(final Location location) {
        return new ResourceClassLoader<>(
                this.parent, this.classFileObjectMap);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(final Location location,
            final String className, final Kind kind,
            final FileObject sibling) {
        final JavaFileObject javaFileObject =
                new JavaClassFileObject(className);
        this.classFileObjectMap.put(className,
                new FileObjectResource(javaFileObject));
        return javaFileObject;
    }
}
