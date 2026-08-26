package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileCopier} 单元测试.
 */
@DisplayName("FileCopier 单元测试")
class FileCopierTest {

    @DisplayName("拷贝文件")
    @Test
    void copyFileTest(@TempDir final Path tempDir) throws Exception {
        final File src = new File(tempDir.toFile(), "src.txt");
        FileUtil.writeString("copy me", src, StandardCharsets.UTF_8);

        final File dest = new File(tempDir.toFile(), "dest.txt");
        final FileCopier copier = FileCopier.create(src, dest);
        copier.copy();

        assertTrue(dest.exists());
        assertEquals("copy me", FileUtil.readString(dest, StandardCharsets.UTF_8));
    }

    @DisplayName("拷贝文件到目录")
    @Test
    void copyFileToDirTest(@TempDir final Path tempDir) throws Exception {
        final File src = new File(tempDir.toFile(), "src.txt");
        FileUtil.writeString("to dir", src, StandardCharsets.UTF_8);

        final File destDir = new File(tempDir.toFile(), "destdir");
        FileUtil.mkdir(destDir);

        final FileCopier copier = FileCopier.create(src, destDir);
        copier.copy();

        final File copiedFile = new File(destDir, "src.txt");
        assertTrue(copiedFile.exists());
        assertEquals("to dir", FileUtil.readString(copiedFile, StandardCharsets.UTF_8));
    }

    @DisplayName("拷贝目录")
    @Test
    void copyDirTest(@TempDir final Path tempDir) throws Exception {
        final File srcDir = new File(tempDir.toFile(), "srcdir");
        FileUtil.mkdir(srcDir);
        FileUtil.writeString("a", new File(srcDir, "a.txt"), StandardCharsets.UTF_8);
        FileUtil.writeString("b", new File(srcDir, "b.txt"), StandardCharsets.UTF_8);

        final File destDir = new File(tempDir.toFile(), "destdir");

        final FileCopier copier = FileCopier.create(srcDir, destDir);
        copier.setCopyContentIfDir(true);
        copier.copy();

        assertTrue(destDir.exists());
        assertEquals("a", FileUtil.readString(new File(destDir, "a.txt"), StandardCharsets.UTF_8));
        assertEquals("b", FileUtil.readString(new File(destDir, "b.txt"), StandardCharsets.UTF_8));
    }

    @DisplayName("拷贝子目录抛异常")
    @Test
    void copySubDirThrows(@TempDir final Path tempDir) throws Exception {
        final File parentDir = new File(tempDir.toFile(), "parent");
        FileUtil.mkdir(parentDir);
        final File childDir = new File(parentDir, "child");

        // 复制父目录到子目录应抛异常
        assertThrows(IORuntimeException.class, () -> {
            final FileCopier copier = FileCopier.create(parentDir, childDir);
            copier.copy();
        });
    }

    @DisplayName("从路径创建")
    @Test
    void createFromPathTest(@TempDir final Path tempDir) throws Exception {
        final Path src = tempDir.resolve("src.txt");
        java.nio.file.Files.write(src, "path copy".getBytes());
        final Path dest = tempDir.resolve("dest.txt");

        final FileCopier copier = FileCopier.create(src.toFile(), dest.toFile());
        copier.copy();

        assertTrue(java.nio.file.Files.exists(dest));
        assertEquals("path copy", new String(java.nio.file.Files.readAllBytes(dest)));
    }
}
