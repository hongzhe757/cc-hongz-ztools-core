package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FileUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
class FileUtilTest {

    /**
     * 验证 {@link FileUtil#touch(String)} 对 null 路径返回 null.
     */
    @Test
    void touchNullPathReturnsNull() {
        assertNull(FileUtil.touch((String) null));
    }

    /**
     * 验证 {@link FileUtil#touch(File)} 对 null 返回 null.
     */
    @Test
    void touchNullFileReturnsNull() {
        assertNull(FileUtil.touch((File) null));
    }

    /**
     * 验证 {@link FileUtil#touch(String)} 在临时目录创建文件.
     *
     * @param tempDir JUnit 临时目录
     */
    @Test
    void touchCreatesFile(@TempDir final Path tempDir) {
        final File file = FileUtil.touch(tempDir.resolve("a.txt").toString());
        assertNotNull(file);
        assertTrue(file.isFile());
    }

    /**
     * 验证 {@link FileUtil#touch(File)} 对已存在文件幂等返回同一对象.
     *
     * @param tempDir JUnit 临时目录
     */
    @Test
    void touchExistingFile(@TempDir final Path tempDir) {
        final File file = FileUtil.touch(tempDir.resolve("b.txt").toFile());
        assertSame(file, FileUtil.touch(file));
    }

    /**
     * 验证 {@link FileUtil#getOutputStream(File)} 配合 {@link IoUtil#write} 写入磁盘.
     *
     * @param tempDir JUnit 临时目录
     */
    @Test
    void getOutputStreamWrites(@TempDir final Path tempDir) throws Exception {
        final File file = FileUtil.touch(tempDir.resolve("out.txt").toFile());
        final byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        try (ByteArrayOutputStream capture = new ByteArrayOutputStream()) {
            IoUtil.write(FileUtil.getOutputStream(file), true, data);
        }
        assertArrayEquals(data, java.nio.file.Files.readAllBytes(file.toPath()));
    }
}
