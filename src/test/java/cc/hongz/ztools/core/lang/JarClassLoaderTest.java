package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link JarClassLoader} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("JarClassLoader 类加载器")
class JarClassLoaderTest {

    @Test
    void constructorTest() {
        final JarClassLoader loader = new JarClassLoader();
        assertNotNull(loader);

        final JarClassLoader loaderWithUrls = new JarClassLoader(new URL[]{});
        assertNotNull(loaderWithUrls);
    }
}
