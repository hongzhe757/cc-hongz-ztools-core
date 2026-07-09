package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link MimeTypeUtils} 单元测试.
 */
class MimeTypeUtilsTest {

    @Test
    void getExtensionFromMime() {
        assertEquals("png", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_PNG));
        assertEquals("jpeg", MimeTypeUtils.getExtension(MimeTypeUtils.IMAGE_JPEG));
    }
}
