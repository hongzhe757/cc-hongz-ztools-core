package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link ManifestUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ManifestUtil Manifest 工具类")
class ManifestUtilTest {

    @Test
    @DisplayName("非 jar 路径返回 null")
    void nonJarPathReturnsNull() {
        assertNull(ManifestUtil.getManifest(new File("nonexistent-dir")));
    }
}
