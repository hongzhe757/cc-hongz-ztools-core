package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PathUtil} 单元测试.
 */
@DisplayName("PathUtil 单元测试")
class PathUtilTest {

    @DisplayName("exists")
    @Test
    void existsTest(@TempDir final Path tempDir) throws Exception {
        final Path file = tempDir.resolve("a.txt");
        Files.createFile(file);
        assertTrue(PathUtil.exists(file, false));
        assertFalse(PathUtil.exists(tempDir.resolve("non.txt"), false));
    }

    @DisplayName("isDirectory")
    @Test
    void isDirectoryTest(@TempDir final Path tempDir) throws Exception {
        assertTrue(PathUtil.isDirectory(tempDir));
        final Path file = tempDir.resolve("a.txt");
        Files.createFile(file);
        assertFalse(PathUtil.isDirectory(file));
    }

    @DisplayName("mkdir")
    @Test
    void mkdirTest(@TempDir final Path tempDir) {
        final Path dir = tempDir.resolve("sub1/sub2/sub3");
        PathUtil.mkdir(dir);
        assertTrue(Files.isDirectory(dir));
    }

    @DisplayName("拷贝文件")
    @Test
    void copyFileTest(@TempDir final Path tempDir) throws Exception {
        final Path src = tempDir.resolve("src.txt");
        Files.write(src, "hello".getBytes());
        final Path dest = tempDir.resolve("dest.txt");
        PathUtil.copyFile(src, dest);
        assertTrue(Files.exists(dest));
        assertArrayEquals(Files.readAllBytes(src), Files.readAllBytes(dest));
    }

    @DisplayName("移动")
    @Test
    void moveTest(@TempDir final Path tempDir) throws Exception {
        final Path src = tempDir.resolve("move_src.txt");
        Files.write(src, "data".getBytes());
        final Path dest = tempDir.resolve("move_dest.txt");
        PathUtil.move(src, dest, false);
        assertFalse(Files.exists(src));
        assertTrue(Files.exists(dest));
        assertEquals("data", new String(Files.readAllBytes(dest)));
    }

    @DisplayName("删除文件")
    @Test
    void delFileTest(@TempDir final Path tempDir) throws Exception {
        final Path file = tempDir.resolve("del.txt");
        Files.createFile(file);
        assertTrue(Files.exists(file));
        PathUtil.del(file);
        assertFalse(Files.exists(file));
    }

    @DisplayName("遍历文件")
    @Test
    void loopFilesTest(@TempDir final Path tempDir) throws Exception {
        Files.createFile(tempDir.resolve("a.txt"));
        Files.createFile(tempDir.resolve("b.txt"));
        final Path subDir = tempDir.resolve("sub");
        Files.createDirectories(subDir);
        Files.createFile(subDir.resolve("c.txt"));

        final java.util.List<java.io.File> files = PathUtil.loopFiles(tempDir, null);
        assertEquals(3, files.size());
    }

    @DisplayName("equals")
    @Test
    void equalsTest(@TempDir final Path tempDir) throws Exception {
        final Path file1 = tempDir.resolve("f.txt");
        Files.write(file1, "same".getBytes());
        // 同一文件路径返回 true
        assertTrue(PathUtil.equals(file1, file1));

        // 不同文件路径即使内容相同也返回 false（isSameFile 比较路径而非内容）
        final Path file2 = tempDir.resolve("f2.txt");
        Files.write(file2, "same".getBytes());
        assertFalse(PathUtil.equals(file1, file2));
    }
}
