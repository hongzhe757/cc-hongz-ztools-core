package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
class FileUtilTest {

    // ======================== touch ========================

    @Test
    void touchNullPathReturnsNull() {
        assertNull(FileUtil.touch((String) null));
    }

    @Test
    void touchNullFileReturnsNull() {
        assertNull(FileUtil.touch((File) null));
    }

    @Test
    void touchCreatesFile(@TempDir final Path tempDir) {
        final File file = FileUtil.touch(tempDir.resolve("a.txt").toString());
        assertNotNull(file);
        assertTrue(file.isFile());
    }

    @Test
    void touchExistingFile(@TempDir final Path tempDir) {
        final File file = FileUtil.touch(tempDir.resolve("b.txt").toFile());
        assertSame(file, FileUtil.touch(file));
    }

    // ======================== getOutputStream ========================

    @Test
    void getOutputStreamWrites(@TempDir final Path tempDir) throws Exception {
        final File file = FileUtil.touch(tempDir.resolve("out.txt").toFile());
        final byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        try (ByteArrayOutputStream capture = new ByteArrayOutputStream()) {
            IoUtil.write(FileUtil.getOutputStream(file), true, data);
        }
        assertArrayEquals(data, java.nio.file.Files.readAllBytes(file.toPath()));
    }

    // ======================== file ========================

    @Test
    void fileTest1() {
        final File file = FileUtil.file("d:/aaa", "bbb");
        assertNotNull(file);
    }

    @Test
    void fileTest2ThrowsOnParentTraversal() {
        assertThrows(IllegalArgumentException.class, () -> {
            final File file = FileUtil.file("d:/aaa", "bbb");
            FileUtil.file(file, "../ccc");
        });
    }

    // ======================== normalize ========================

    @Test
    void normalizeTest() {
        assertEquals("/foo/", FileUtil.normalize("/foo//"));
        assertEquals("/foo/", FileUtil.normalize("/foo/./"));
        assertEquals("/bar", FileUtil.normalize("/foo/../bar"));
        assertEquals("/bar/", FileUtil.normalize("/foo/../bar/"));
        assertEquals("/baz", FileUtil.normalize("/foo/../bar/../baz"));
        assertEquals("/", FileUtil.normalize("/../"));
        assertEquals("foo", FileUtil.normalize("foo/bar/.."));
        assertEquals("../bar", FileUtil.normalize("foo/../../bar"));
        assertEquals("bar", FileUtil.normalize("foo/../bar"));
    }

    @Test
    void normalizeWindowsPath() {
        assertEquals("C:/bar", FileUtil.normalize("C:\\foo\\..\\bar"));
        assertEquals("C:/bar", FileUtil.normalize("C:\\..\\bar"));
    }

    @Test
    void normalizeClassPath() {
        assertEquals("", FileUtil.normalize("classpath:"));
    }

    @Test
    void normalizeRelativePaths() {
        assertEquals("../../bar", FileUtil.normalize("../../bar"));
        assertEquals("../a/b.csv", FileUtil.normalize("../a/b.csv"));
    }

    @Test
    void normalizeHomePath() {
        final String home = FileUtil.getUserHomePath().replace('\\', '/');
        assertEquals(home + "/bar/", FileUtil.normalize("~/foo/../bar/"));
    }

    @Test
    void doubleNormalizeTest() {
        final String normalize = FileUtil.normalize("/aa/b:/c");
        final String normalize2 = FileUtil.normalize(normalize);
        assertEquals("/aa/b:/c", normalize);
        assertEquals(normalize, normalize2);
    }

    // ======================== getName / mainName / extName ========================

    @Test
    void getNameTest() {
        String path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "/aaa/bbb/cc/ddd/";
        assertEquals("ddd", FileUtil.getName(path));

        path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "/aaa/bbb/cc/ddd.jpg";
        assertEquals("ddd.jpg", FileUtil.getName(path));
    }

    @Test
    void mainNameTest() {
        String path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "/aaa/bbb/cc/ddd/";
        assertEquals("ddd", FileUtil.mainName(path));

        path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "/aaa/bbb/cc/ddd.jpg";
        assertEquals("ddd", FileUtil.mainName(path));
    }

    @Test
    void extNameTest() {
        String path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "~/Desktop/ddd.jpg";
        assertEquals("jpg", FileUtil.extName(path));

        path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.gz" : "~/Desktop/fff.tar.gz";
        assertEquals("tar.gz", FileUtil.extName(path));

        path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "~/Desktop/ddd/";
        assertEquals("", FileUtil.extName(path));
    }

    // ======================== subPath ========================

    @Test
    void subPathTest() {
        final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

        Path subPath = FileUtil.subPath(path, 5, 4);
        assertEquals("eee", subPath.toString());
        subPath = FileUtil.subPath(path, 0, 1);
        assertEquals("aaa", subPath.toString());
        subPath = FileUtil.subPath(path, 1, 0);
        assertEquals("aaa", subPath.toString());
    }

    @Test
    void subPathTest2() {
        String subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc/");
        assertEquals("ccc/", subPath);

        subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/test.txt");
        assertEquals("ccc/test.txt", subPath);

        subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb");
        assertEquals("", subPath);
    }

    // ======================== getPathEle ========================

    @Test
    void getPathEle() {
        final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

        Path ele = FileUtil.getPathEle(path, -1);
        assertEquals("fff", ele.toString());
        ele = FileUtil.getPathEle(path, 0);
        assertEquals("aaa", ele.toString());
        ele = FileUtil.getPathEle(path, -6);
        assertEquals("aaa", ele.toString());
    }

    // ======================== isSub ========================

    @Test
    void isSubTest() {
        final File file = new File("d:/test");
        final File file2 = new File("d:/test2/aaa");
        assertFalse(FileUtil.isSub(file, file2));
    }

    @Test
    void isSubSamePath() {
        File parent = new File("d:/home/user/docs");
        File sub = new File("d:/home/user/docs");
        assertTrue(FileUtil.isSub(parent, sub));
    }

    @Test
    void isSubNullParentThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.isSub(null, new File("d:/any/path"));
        });
    }

    @Test
    void isSubNullSubThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.isSub(new File("d:/any/path"), null);
        });
    }

    // ======================== isAbsolutePath ========================

    @Test
    void isAbsolutePathTest() {
        if (FileUtil.isWindows()) {
            assertTrue(FileUtil.isAbsolutePath("d:/test\\aaa.txt"));
        }
        assertFalse(FileUtil.isAbsolutePath("test\\aaa.txt"));
    }

    // ======================== lastIndexOfSeparator ========================

    @Test
    void lastIndexOfSeparatorTest() {
        final String dir = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd" : "/aaa/bbb/cc/ddd";
        final int index = FileUtil.lastIndexOfSeparator(dir);
        assertTrue(index > 0);

        final String file = "ddd.jpg";
        final int index2 = FileUtil.lastIndexOfSeparator(file);
        assertEquals(-1, index2);
    }

    // ======================== equals ========================

    @Test
    void equalsTest() {
        final File file1 = FileUtil.file("d:/hutool.jpg");
        final File file2 = FileUtil.file("d:/hutool.jpg");
        assertTrue(FileUtil.equals(file1, file2));
    }

    // ======================== mkdir ========================

    @Test
    void mkdirCreatesDir(@TempDir final Path tempDir) {
        final File newDir = new File(tempDir.toFile(), "sub1/sub2");
        assertNotNull(FileUtil.mkdir(newDir));
        assertTrue(newDir.isDirectory());
    }

    // ======================== write/read string ========================

    @Test
    void writeAndReadString(@TempDir final Path tempDir) {
        final File file = new File(tempDir.toFile(), "hello.txt");
        FileUtil.writeString("hello world", file, StandardCharsets.UTF_8);
        final String content = FileUtil.readString(file, StandardCharsets.UTF_8);
        assertEquals("hello world", content);
    }

    @Test
    void writeAndReadLines(@TempDir final Path tempDir) {
        final File file = new File(tempDir.toFile(), "lines.txt");
        final List<String> lines = java.util.Arrays.asList("line1", "line2", "line3");
        FileUtil.writeLines(lines, file, StandardCharsets.UTF_8);
        final List<String> readLines = FileUtil.readLines(file, StandardCharsets.UTF_8);
        assertEquals(lines, readLines);
    }

    // ======================== copy ========================

    @Test
    void copyFile(@TempDir final Path tempDir) {
        final File src = new File(tempDir.toFile(), "src.txt");
        FileUtil.writeString("copy me", src, StandardCharsets.UTF_8);
        final File dest = new File(tempDir.toFile(), "dest.txt");
        FileUtil.copy(src, dest, true);
        assertEquals("copy me", FileUtil.readString(dest, StandardCharsets.UTF_8));
    }

    // ======================== del ========================

    @Test
    void delNonExistentFileReturnsTrue() {
        assertTrue(FileUtil.del(new File("non_existent_file_xyz_12345.txt")));
    }

    @Test
    void delExistingFile(@TempDir final Path tempDir) {
        final File file = FileUtil.touch(tempDir.resolve("del.txt").toFile());
        assertTrue(file.exists());
        assertTrue(FileUtil.del(file));
        assertFalse(file.exists());
    }

    // ======================== rename ========================

    @Test
    void renameFile(@TempDir final Path tempDir) {
        final File src = FileUtil.touch(tempDir.resolve("old.txt").toFile());
        final File renamed = FileUtil.rename(src, "new.txt", false);
        assertEquals("new.txt", renamed.getName());
        assertTrue(renamed.exists());
    }

    // ======================== checkSlip ========================

    @Test
    void checkSlipTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.checkSlip(FileUtil.file("test/a"), FileUtil.file("test/../a"));
        });
    }

    // ======================== getMimeType ========================

    @Test
    void getMimeTypeTest() {
        assertEquals("image/jpeg", FileUtil.getMimeType("test.jpg"));
        assertEquals("text/html", FileUtil.getMimeType("test.html"));
        assertEquals("text/css", FileUtil.getMimeType("main.css"));
    }

    // ======================== loopFiles ========================

    @Test
    void loopFilesTest(@TempDir final Path tempDir) {
        FileUtil.touch(tempDir.resolve("a.txt").toFile());
        FileUtil.touch(tempDir.resolve("b.txt").toFile());
        final File subDir = tempDir.resolve("sub").toFile();
        FileUtil.mkdir(subDir);
        FileUtil.touch(new File(subDir, "c.txt"));

        final List<File> files = FileUtil.loopFiles(tempDir.toFile());
        assertEquals(3, files.size());
    }

    // ======================== getParent ========================

    @Test
    void getParentTest(@TempDir final Path tempDir) {
        final File dir = new File(tempDir.toFile(), "aaa/bbb/ccc");
        FileUtil.mkdir(dir);
        final File parent0 = FileUtil.getParent(dir, 0);
        assertEquals(dir, parent0);
        final File parent1 = FileUtil.getParent(dir, 1);
        assertEquals("bbb", parent1.getName());
    }

    // ======================== getUserHomePath ========================

    @Test
    void getUserHomePathTest() {
        final String homePath = FileUtil.getUserHomePath();
        assertNotNull(homePath);
        assertFalse(homePath.isEmpty());
    }
}
