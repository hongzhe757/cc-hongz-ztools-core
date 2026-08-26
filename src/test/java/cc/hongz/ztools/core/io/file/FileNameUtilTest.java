package cc.hongz.ztools.core.io.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileNameUtil} 单元测试.
 */
@DisplayName("FileNameUtil 单元测试")
class FileNameUtilTest {

    @DisplayName("getName")
    @Test
    void getNameTest() {
        assertEquals("ddd", FileNameUtil.getName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("ddd.jpg", FileNameUtil.getName("d:/aaa/bbb/cc/ddd.jpg"));
    }

    @DisplayName("mainName")
    @Test
    void mainNameTest() {
        assertEquals("ddd", FileNameUtil.mainName("d:/aaa/bbb/cc/ddd.jpg"));
        assertEquals("ddd", FileNameUtil.mainName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("file", FileNameUtil.mainName("file.tar.gz"));
    }

    @DisplayName("extName")
    @Test
    void extNameTest() {
        assertEquals("jpg", FileNameUtil.extName("d:/aaa/bbb/cc/ddd.jpg"));
        assertEquals("tar.gz", FileNameUtil.extName("file.tar.gz"));
        assertEquals("", FileNameUtil.extName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("", FileNameUtil.extName(""));
        assertNull(FileNameUtil.extName((String) null));
    }

    @DisplayName("getPrefix")
    @Test
    void getPrefixTest() {
        assertEquals("file", FileNameUtil.getPrefix("file.tar.gz"));
        assertEquals("file", FileNameUtil.getPrefix("file"));
        assertEquals("file.name", FileNameUtil.getPrefix("file.name.txt"));
    }

    @DisplayName("getSuffix")
    @Test
    void getSuffixTest() {
        assertEquals("tar.gz", FileNameUtil.getSuffix("file.tar.gz"));
        assertEquals("txt", FileNameUtil.getSuffix("file.txt"));
        assertEquals("", FileNameUtil.getSuffix("file"));
    }

    @DisplayName("getName(File)")
    @Test
    void getNameWithFileTest() {
        java.io.File file = new java.io.File("d:/test/hello.txt");
        assertEquals("hello.txt", FileNameUtil.getName(file));
    }

    @DisplayName("extName(File)")
    @Test
    void extNameWithFileTest() {
        java.io.File file = new java.io.File("test.pdf");
        assertEquals("pdf", FileNameUtil.extName(file));
    }
}
