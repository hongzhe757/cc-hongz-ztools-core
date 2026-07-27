package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.lang.Filter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ZipReader 单元测试（自 Hutool {@code cn.hutool.core.compress.ZipReaderTest} 迁入）.
 *
 * @author looly
 * @author hongzhe.qin
 */
class ZipReaderTest {

    // ==================== 基础功能（对应 hutool unzipTest） ====================

    @Test
    @DisplayName("unzipTest - 解压zip到目录")
    void unzipTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("unzip.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("sub/data.txt",
                new ByteArrayInputStream("hello zip".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("unzipOut").toFile();
        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        reader.readTo(outDir);
        reader.close();

        File extracted = new File(outDir, "sub/data.txt");
        assertTrue(extracted.exists());
    }

    // ==================== read / readTo ====================

    @Test
    @DisplayName("read - 遍历所有条目")
    void readTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("read.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt", new ByteArrayInputStream("A".getBytes()));
        writer.add("b.txt", new ByteArrayInputStream("B".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(2, count.get());
    }

    @Test
    @DisplayName("readTo - 解压到指定目录")
    void readToTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("readto2.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("x/y/file.txt",
                new ByteArrayInputStream("nested".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("out2").toFile();
        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        reader.readTo(outDir);
        reader.close();

        assertTrue(new File(outDir, "x/y/file.txt").exists());
    }

    @Test
    @DisplayName("readTo - 带过滤器排除文件")
    void readToWithFilterTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("filter.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("keep.txt",
                new ByteArrayInputStream("keep".getBytes()));
        writer.add("drop.log",
                new ByteArrayInputStream("drop".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("filterOut").toFile();
        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        Filter<ZipEntry> filter =
                entry -> !entry.getName().endsWith(".log");
        reader.readTo(outDir, filter);
        reader.close();

        assertTrue(new File(outDir, "keep.txt").exists());
        assertFalse(new File(outDir, "drop.log").exists());
    }

    // ==================== get ====================

    @Test
    @DisplayName("get - 获取指定路径的输入流")
    void getTest(@TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("get.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("target.txt",
                new ByteArrayInputStream("target content".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        InputStream in = reader.get("target.txt");
        assertNotNull(in);
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        String content = new String(buf, 0, len, StandardCharsets.UTF_8);
        reader.close();
        assertEquals("target content", content);
    }

    @Test
    @DisplayName("get - 不存在的路径返回null")
    void getNonExistentTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("none.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("real.txt",
                new ByteArrayInputStream("real".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
        assertNull(reader.get("fake.txt"));
        reader.close();
    }

    // ==================== 构造器 ====================

    @Test
    @DisplayName("构造 - ZipFile构造器")
    void zipFileConstructorTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("zf.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("entry.txt",
                new ByteArrayInputStream("entry".getBytes()));
        writer.close();

        ZipFile zf = new ZipFile(zipFile, StandardCharsets.UTF_8);
        ZipReader reader = new ZipReader(zf);
        assertNotNull(reader.get("entry.txt"));
        reader.close();
    }

    @Test
    @DisplayName("构造 - ZipInputStream构造器")
    void zipInputStreamConstructorTest(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("zis.zip").toFile();
        ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt",
                new ByteArrayInputStream("A".getBytes()));
        writer.close();

        ZipInputStream zis = new ZipInputStream(
                Files.newInputStream(zipFile.toPath()),
                StandardCharsets.UTF_8);
        ZipReader reader = new ZipReader(zis);
        reader.setMaxSizeDiff(-1);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(1, count.get());
    }

    // ==================== setMaxSizeDiff ====================

    @Test
    @DisplayName("setMaxSizeDiff - 设置ZipBomb检测阈值")
    void setMaxSizeDiffTest() {
        ZipReader reader = ZipReader.of(
                new ByteArrayInputStream(new byte[0]),
                StandardCharsets.UTF_8);
        ZipReader result = reader.setMaxSizeDiff(-1);
        assertSame(reader, result);
    }
}
