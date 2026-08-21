package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link JarClassLoader} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("JarClassLoader 类加载器")
class JarClassLoaderTest {

    /** 测试用 jar，取自本地 maven 仓库的 junit-jupiter-api。 */
    private static File jarFile() {
        final String home = System.getProperty("user.home");
        final File jar = new File(home, ".m2/repository/org/junit/jupiter/junit-jupiter-api/5.10.5/junit-jupiter-api-5.10.5.jar");
        assertTrue(jar.isFile(), "测试依赖的 junit jar 不存在: " + jar.getAbsolutePath());
        return jar;
    }

    @Test
    void constructorTest() {
        final JarClassLoader loader = new JarClassLoader();
        assertNotNull(loader);

        final JarClassLoader loaderWithUrls = new JarClassLoader(new URL[]{});
        assertNotNull(loaderWithUrls);
    }

    @Test
    void loadJarTest() throws Exception {
        final JarClassLoader loader = JarClassLoader.loadJar(jarFile());
        final Class<?> clazz = loader.loadClass("org.junit.jupiter.api.Test");
        assertEquals("org.junit.jupiter.api.Test", clazz.getName());
    }

    @Test
    void loadDirTest() throws Exception {
        // 加载 jar 所在目录，应递归找到其中的 jar
        final JarClassLoader loader = JarClassLoader.load(jarFile().getParentFile());
        final Class<?> clazz = loader.loadClass("org.junit.jupiter.api.Test");
        assertEquals("org.junit.jupiter.api.Test", clazz.getName());
    }

    @Test
    void addJarTest() throws Exception {
        final JarClassLoader loader = new JarClassLoader();
        loader.addJar(jarFile());
        final Class<?> clazz = loader.loadClass("org.junit.jupiter.api.Test");
        assertEquals("org.junit.jupiter.api.Test", clazz.getName());
    }

    @Test
    void addURLTest() throws Exception {
        final JarClassLoader loader = new JarClassLoader();
        loader.addURL(jarFile());
        final Class<?> clazz = loader.loadClass("org.junit.jupiter.api.Test");
        assertEquals("org.junit.jupiter.api.Test", clazz.getName());
    }

    /**
     * JDK9+ 模块系统封锁了对 URLClassLoader.addURL 的反射访问，
     * loadJar(URLClassLoader, File) 在 JDK17 下无法运行，跳过。
     */
    @Test
    @Disabled("JDK9+ 模块系统封锁反射访问 URLClassLoader.addURL")
    void loadJarToUrlClassLoaderTest() throws Exception {
        final JarClassLoader loader = new JarClassLoader();
        JarClassLoader.loadJar(loader, jarFile());
        final Class<?> clazz = loader.loadClass("org.junit.jupiter.api.Test");
        assertEquals("org.junit.jupiter.api.Test", clazz.getName());
    }

    /**
     * JDK9+ 系统类加载器不再是 URLClassLoader，跳过。
     */
    @Test
    @Disabled("JDK9+ ClassLoader.getSystemClassLoader() 不再是 URLClassLoader")
    void loadJarToSystemClassLoaderTest() {
        JarClassLoader.loadJarToSystemClassLoader(jarFile());
    }
}
