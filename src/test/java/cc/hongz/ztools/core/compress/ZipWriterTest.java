package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.resource.FileResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ZipWriter 单元测试（自 Hutool {@code cn.hutool.core.compress.ZipWriterTest} 迁入）.
 *
 * @author looly
 * @author hongzhe.qin
 */
class ZipWriterTest {

    // ==================== 基础功能（对应 hutool zipDirTest / addTest） ====================

    @Test
    @DisplayName("zipDirTest - 压缩目录到zip")
    void zipDirTest(@TempDir final Path tempDir) throws IOException {
        Path srcDir = tempDir.resolve("src");
        Files.createDirectories(srcDir);
        Files.write(srcDir.resolve("file1.txt"),
                "content1".getBytes());
        Files.write(srcDir.resolve("file2.txt"),
                "content2".getBytes());

        File zipFile = tempDir.resolve("dir.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add(false, null, srcDir.toFile());
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(2, count.get());
    }

    @Test
    @DisplayName("addTest - 添加FileResource到zip")
    void addTest(@TempDir final Path tempDir) throws IOException {
        Path resFile = tempDir.resolve("test.png");
        Files.write(resFile, "fake png data".getBytes());

        File zipFile = tempDir.resolve("res.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add(new FileResource(resFile.toFile(), "test.png"));
        writer.close();

        assertTrue(zipFile.exists());
        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        assertNotNull(reader.get("test.png"));
        reader.close();
    }

    // ==================== add 方法 ====================

    @Test
    @DisplayName("add - 添加单个文件流")
    void addSingleFileTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("single.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("hello.txt",
                new ByteArrayInputStream(
                        "Hello World".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @Test
    @DisplayName("add - 添加多个文件")
    void addMultipleFilesTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("multi.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt",
                new ByteArrayInputStream("A".getBytes()));
        writer.add("b.txt",
                new ByteArrayInputStream("B".getBytes()));
        writer.add("sub/c.txt",
                new ByteArrayInputStream("C".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(3, count.get());
    }

    @Test
    @DisplayName("add - 添加空目录")
    void addEmptyDirTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("emptydir.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("emptyDir/", null);
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("add - null路径+null流创建空目录")
    void addNullPathTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("nullpath.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        // null路径+null流 → 创建空目录
        writer.add("emptyDir/", null);
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("add - 路径数组和流数组")
    void addArraysTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("arrays.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        String[] paths = {"x.txt", "y.txt"};
        InputStream[] streams = {
            new ByteArrayInputStream("X".getBytes()),
            new ByteArrayInputStream("Y".getBytes())
        };
        writer.add(paths, streams);
        writer.close();

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(2, count.get());
    }

    @Test
    @DisplayName("add - 空路径数组抛出异常")
    void addEmptyArraysTest() {
        ZipWriter writer = ZipWriter.of(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> writer.add(new String[0], new InputStream[0]));
    }

    @Test
    @DisplayName("add - 数组长度不匹配抛出异常")
    void addMismatchArraysTest() {
        ZipWriter writer = ZipWriter.of(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> writer.add(
                        new String[]{"a.txt"},
                        new InputStream[]{
                            new ByteArrayInputStream(new byte[0]),
                            new ByteArrayInputStream(new byte[0])
                        }));
    }

    // ==================== 构造器 ====================

    @Test
    @DisplayName("构造 - OutputStream构造器")
    void outputStreamConstructorTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("os.zip").toFile();
        OutputStream os = Files.newOutputStream(zipFile.toPath());
        ZipWriter writer = new ZipWriter(os, StandardCharsets.UTF_8);
        writer.add("t.txt",
                new ByteArrayInputStream("data".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @Test
    @DisplayName("构造 - ZipOutputStream构造器")
    void zipOutputStreamConstructorTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("zos.zip").toFile();
        ZipOutputStream zos = new ZipOutputStream(
                Files.newOutputStream(zipFile.toPath()),
                StandardCharsets.UTF_8);
        ZipWriter writer = new ZipWriter(zos);
        writer.add("entry.txt",
                new ByteArrayInputStream("entry".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("构造 - ByteArrayOutputStream")
    void byteArrayOutputStreamTest() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipWriter writer = ZipWriter.of(baos, StandardCharsets.UTF_8);
        writer.add("test.txt",
                new ByteArrayInputStream("test".getBytes()));
        writer.close();

        assertTrue(baos.size() > 0);
    }

    // ==================== setLevel / setComment ====================

    @Test
    @DisplayName("setLevel - 设置压缩级别")
    void setLevelTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("level.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.setLevel(9);
        writer.add("f.txt",
                new ByteArrayInputStream("x".getBytes()));
        writer.close();
        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("setComment - 设置Zip注释")
    void setCommentTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("comment.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.setComment("test comment");
        writer.add("f.txt",
                new ByteArrayInputStream("x".getBytes()));
        writer.close();
        assertTrue(zipFile.exists());
    }

    // ==================== getOut ====================

    @Test
    @DisplayName("getOut - 获取底层ZipOutputStream")
    void getOutTest() {
        ZipOutputStream zos = new ZipOutputStream(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        ZipWriter writer = new ZipWriter(zos);
        assertSame(zos, writer.getOut());
    }

    // ==================== add withSrcDir ====================

    @Test
    @DisplayName("add - withSrcDir包含打包目录")
    void addWithSrcDirTest(@TempDir final Path tempDir)
            throws IOException {
        Path srcDir = tempDir.resolve("srcDir");
        Files.createDirectories(srcDir);
        Files.write(srcDir.resolve("a.txt"),
                "A".getBytes());

        File zipFile = tempDir.resolve("srcdir.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        // withSrcDir=true → 目录名本身也被打包
        writer.add(true, null, srcDir.toFile());
        writer.close();

        assertTrue(zipFile.exists());
    }

}
