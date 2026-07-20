package cc.hongz.ztools.core.io.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileNameUtil} 单元测试.
 */
class FileNameUtilTest {

    @Test
    void getNameTest() {
        assertEquals("ddd", FileNameUtil.getName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("ddd.jpg", FileNameUtil.getName("d:/aaa/bbb/cc/ddd.jpg"));
    }

    @Test
    void mainNameTest() {
        assertEquals("ddd", FileNameUtil.mainName("d:/aaa/bbb/cc/ddd.jpg"));
        assertEquals("ddd", FileNameUtil.mainName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("file", FileNameUtil.mainName("file.tar.gz"));
    }

    @Test
    void extNameTest() {
        assertEquals("jpg", FileNameUtil.extName("d:/aaa/bbb/cc/ddd.jpg"));
        assertEquals("tar.gz", FileNameUtil.extName("file.tar.gz"));
        assertEquals("", FileNameUtil.extName("d:/aaa/bbb/cc/ddd/"));
        assertEquals("", FileNameUtil.extName(""));
        assertEquals("", FileNameUtil.extName((String) null));
    }

    @Test
    void getPrefixTest() {
        assertEquals("file", FileNameUtil.getPrefix("file.tar.gz"));
        assertEquals("file", FileNameUtil.getPrefix("file"));
        assertEquals("file.name", FileNameUtil.getPrefix("file.name.txt"));
    }

    @Test
    void getSuffixTest() {
        assertEquals("gz", FileNameUtil.getSuffix("file.tar.gz"));
        assertEquals("txt", FileNameUtil.getSuffix("file.txt"));
        assertEquals("", FileNameUtil.getSuffix("file"));
    }

    @Test
    void getNameWithFileTest() {
        java.io.File file = new java.io.File("d:/test/hello.txt");
        assertEquals("hello.txt", FileNameUtil.getName(file));
    }

    @Test
    void extNameWithFileTest() {
        java.io.File file = new java.io.File("test.pdf");
        assertEquals("pdf", FileNameUtil.extName(file));
    }
}
