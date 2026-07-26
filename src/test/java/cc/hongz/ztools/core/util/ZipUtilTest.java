package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ZipUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
class ZipUtilTest {

    // ------------------------------------------------------------------- Gzip

    @DisplayName("Gzip 压缩字符串后解压恢复原内容")
    @Test
    void gzipAndUnGzip() {
        final String content = "Hello, ZipUtil Gzip!";
        final byte[] compressed = ZipUtil.gzip(content, "UTF-8");
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        final String decompressed = ZipUtil.unGzip(compressed, "UTF-8");
        assertEquals(content, decompressed);
    }

    @DisplayName("Gzip 压缩字节数组后解压恢复原内容")
    @Test
    void gzipBytesAndUnGzipBytes() {
        final byte[] buf = "test data for gzip".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.gzip(buf);
        final byte[] decompressed = ZipUtil.unGzip(compressed);
        assertArrayEquals(buf, decompressed);
    }

    @DisplayName("Gzip 压缩输入流返回非空字节数组")
    @Test
    void gzipInputStream() {
        final byte[] data = "stream gzip test".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.gzip(new ByteArrayInputStream(data));
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    @DisplayName("Gzip 压缩文件返回非空字节数组")
    @Test
    void gzipFile(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("gzip_test.txt").toFile();
        Files.write(src.toPath(),
                "file content for gzip".getBytes(StandardCharsets.UTF_8));
        final byte[] compressed = ZipUtil.gzip(src);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    // ------------------------------------------------------------------- Zlib

    @DisplayName("Zlib 压缩字符串后解压恢复原内容")
    @Test
    void zlibAndUnZlib() {
        final String content = "Hello, ZipUtil Zlib!";
        final byte[] compressed = ZipUtil.zlib(content, "UTF-8", 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        final String decompressed = ZipUtil.unZlib(compressed, "UTF-8");
        assertEquals(content, decompressed);
    }

    @DisplayName("Zlib 压缩字节数组后解压恢复原内容")
    @Test
    void zlibBytesAndUnZlibBytes() {
        final byte[] buf = "test data for zlib".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.zlib(buf, 6);
        final byte[] decompressed = ZipUtil.unZlib(compressed);
        assertArrayEquals(buf, decompressed);
    }

    @DisplayName("Zlib 压缩输入流返回非空字节数组")
    @Test
    void zlibInputStream() {
        final byte[] data = "stream zlib test".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.zlib(new ByteArrayInputStream(data), 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    @DisplayName("Zlib 压缩文件返回非空字节数组")
    @Test
    void zlibFile(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("zlib_test.txt").toFile();
        Files.write(src.toPath(),
                "file content for zlib".getBytes(StandardCharsets.UTF_8));
        final byte[] compressed = ZipUtil.zlib(src, 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    // -------------------------------------------------------------------- Zip

    @DisplayName("压缩单个文件生成 .zip 文件")
    @Test
    void zipSingleFile(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("zip_src.txt").toFile();
        Files.write(src.toPath(), "zip content".getBytes(StandardCharsets.UTF_8));

        final File zipFile = ZipUtil.zip(src);
        assertNotNull(zipFile);
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
        assertTrue(zipFile.getName().endsWith(".zip"));
    }

    @DisplayName("压缩目录后解压可恢复原文件")
    @Test
    void zipAndUnzip(@TempDir final Path tempDir) throws IOException {
        final File srcDir = tempDir.resolve("srcDir").toFile();
        srcDir.mkdirs();
        final File srcFile = new File(srcDir, "test.txt");
        Files.write(srcFile.toPath(), "hello zip".getBytes(StandardCharsets.UTF_8));

        final File zipFile = ZipUtil.zip(srcDir);
        assertTrue(zipFile.exists());

        final File unzipDir = tempDir.resolve("unzipOut").toFile();
        ZipUtil.unzip(zipFile, unzipDir);
        assertTrue(unzipDir.exists());

        final File[] files = unzipDir.listFiles();
        assertNotNull(files);
        assertTrue(files.length > 0);
    }

    @DisplayName("压缩文件到输出流")
    @Test
    void zipToOutputStream(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("os_test.txt").toFile();
        Files.write(src.toPath(),
                "output stream zip".getBytes(StandardCharsets.UTF_8));

        final java.io.ByteArrayOutputStream bos =
                new java.io.ByteArrayOutputStream();
        ZipUtil.zip(bos, StandardCharsets.UTF_8, false, null, src);
        assertTrue(bos.size() > 0);
    }

    @DisplayName("将字符串写入 zip 文件指定条目")
    @Test
    void zipWithData(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("data.zip").toFile();
        ZipUtil.zip(zipFile, "entry.txt", "hello entry");
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @DisplayName("将输入流写入 zip 文件指定条目")
    @Test
    void zipWithInputStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("stream.zip").toFile();
        final byte[] data = "stream entry".getBytes(StandardCharsets.UTF_8);
        ZipUtil.zip(zipFile, "stream_entry.txt", new ByteArrayInputStream(data));
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    // ------------------------------------------------------------------ Unzip

    @DisplayName("解压 zip 文件到默认目录")
    @Test
    void unzipByPath(@TempDir final Path tempDir) throws IOException {
        final File srcFile = tempDir.resolve("unzip_src.txt").toFile();
        Files.write(srcFile.toPath(),
                "unzip test".getBytes(StandardCharsets.UTF_8));
        final File zipFile = ZipUtil.zip(srcFile);

        final File unzipDir = ZipUtil.unzip(zipFile.getAbsolutePath());
        assertNotNull(unzipDir);
        assertTrue(unzipDir.exists());
    }

    @DisplayName("从输入流解压到指定目录")
    @Test
    void unzipFromStream(@TempDir final Path tempDir) throws IOException {
        final File srcFile = tempDir.resolve("stream_unzip.txt").toFile();
        Files.write(srcFile.toPath(),
                "stream unzip".getBytes(StandardCharsets.UTF_8));
        final File zipFile = ZipUtil.zip(srcFile);

        final File outDir = tempDir.resolve("stream_out").toFile();
        try (java.io.FileInputStream fis =
                new java.io.FileInputStream(zipFile)) {
            ZipUtil.unzip(fis, outDir, StandardCharsets.UTF_8);
        }
        assertTrue(outDir.exists());
    }

    // --------------------------------------------------- get / listFileNames

    @DisplayName("从 zip 中获取指定条目的输入流")
    @Test
    void getEntryStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("get_test.zip").toFile();
        ZipUtil.zip(zipFile, "readme.txt", "get entry content");

        try (ZipFile zf = new ZipFile(zipFile)) {
            final java.io.InputStream is = ZipUtil.get(zf, "readme.txt");
            assertNotNull(is);
            final String content =
                    new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("get entry content", content);
        }
    }

    @DisplayName("获取不存在的条目返回 null")
    @Test
    void getEntryStreamNotFound(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("not_found.zip").toFile();
        ZipUtil.zip(zipFile, "exists.txt", "data");

        try (ZipFile zf = new ZipFile(zipFile)) {
            assertNull(ZipUtil.get(zf, "not_exists.txt"));
        }
    }

    @DisplayName("列出 zip 中指定目录下的文件名")
    @Test
    void listFileNames(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("list.zip").toFile();
        try (ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(zipFile), StandardCharsets.UTF_8)) {
            zos.putNextEntry(new ZipEntry("dir/"));
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry("dir/a.txt"));
            zos.write("aaa".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry("dir/b.txt"));
            zos.write("bbb".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry("other.txt"));
            zos.write("other".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try (ZipFile zf = new ZipFile(zipFile)) {
            final List<String> names = ZipUtil.listFileNames(zf, "dir/");
            assertNotNull(names);
            assertEquals(2, names.size());
            assertTrue(names.contains("a.txt"));
            assertTrue(names.contains("b.txt"));
        }
    }

    @DisplayName("不指定目录时列出 zip 中所有顶层文件")
    @Test
    void listFileNamesNoDir(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("list_all.zip").toFile();
        try (ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(zipFile), StandardCharsets.UTF_8)) {
            zos.putNextEntry(new ZipEntry("x.txt"));
            zos.write("x".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry("y.txt"));
            zos.write("y".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try (ZipFile zf = new ZipFile(zipFile)) {
            final List<String> names = ZipUtil.listFileNames(zf, null);
            assertNotNull(names);
            assertEquals(2, names.size());
        }
    }

    // --------------------------------------------------------------- toZipFile

    @DisplayName("toZipFile 正常返回 ZipFile 并包含指定条目")
    @Test
    void toZipFile(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("toZip.zip").toFile();
        ZipUtil.zip(zipFile, "test.txt", "data");

        try (ZipFile zf = ZipUtil.toZipFile(zipFile, StandardCharsets.UTF_8)) {
            assertNotNull(zf);
            assertNotNull(zf.getEntry("test.txt"));
        }
    }

    @DisplayName("toZipFile 使用 null charset 时默认 UTF-8")
    @Test
    void toZipFileNullCharset(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("null_charset.zip").toFile();
        ZipUtil.zip(zipFile, "test.txt", "data");

        try (ZipFile zf = ZipUtil.toZipFile(zipFile, null)) {
            assertNotNull(zf);
        }
    }

    // ---------------------------------------------------------- unzipFileBytes

    @DisplayName("unzipFileBytes 从 zip 中读取指定条目字节内容")
    @Test
    void unzipFileBytes(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("bytes.zip").toFile();
        final byte[] expected = "byte content".getBytes(StandardCharsets.UTF_8);
        ZipUtil.zip(zipFile, "bytes.txt", new ByteArrayInputStream(expected));

        final byte[] actual = ZipUtil.unzipFileBytes(zipFile, "bytes.txt");
        assertArrayEquals(expected, actual);
    }

    // --------------------------------------------------------- getZipOutputStream

    @DisplayName("getZipOutputStream 从 OutputStream 创建 ZipOutputStream")
    @Test
    void getZipOutputStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("gos.zip").toFile();
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(zipFile);
             ZipOutputStream zos = ZipUtil.getZipOutputStream(
                     fos, StandardCharsets.UTF_8)) {
            assertNotNull(zos);
            zos.putNextEntry(new ZipEntry("test"));
            zos.write("data".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }
        assertTrue(zipFile.length() > 0);
    }

    // --------------------------------------------------------------- getStream

    @DisplayName("getStream 从 ZipFile/ZipEntry 获取输入流")
    @Test
    void getStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("stream_test.zip").toFile();
        ZipUtil.zip(zipFile, "entry.txt", "entry data");

        try (ZipFile zf = new ZipFile(zipFile)) {
            final ZipEntry entry = zf.getEntry("entry.txt");
            assertNotNull(entry);
            final java.io.InputStream is = ZipUtil.getStream(zf, entry);
            assertNotNull(is);
            final String content =
                    new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("entry data", content);
        }
    }

    // ------------------------------------------------------------------ append

    @DisplayName("append 向现有 zip 文件中追加文件")
    @Test
    void appendToZip(@TempDir final Path tempDir) throws IOException {
        // 先创建一个 zip
        final File zipFile = tempDir.resolve("append.zip").toFile();
        ZipUtil.zip(zipFile, "existing.txt", "existing content");

        // 追加新文件
        final File appendFile = tempDir.resolve("append_file.txt").toFile();
        Files.write(appendFile.toPath(),
                "appended".getBytes(StandardCharsets.UTF_8));
        ZipUtil.append(zipFile.toPath(), appendFile.toPath());

        // 验证 zip 中有两个文件
        try (ZipFile zf = new ZipFile(zipFile)) {
            assertNotNull(zf.getEntry("existing.txt"));
            assertNotNull(zf.getEntry("append_file.txt"));
        }
    }

    // ---------------------------------------------------- read(ZipFile)

    @DisplayName("read(ZipFile) 遍历 zip 中所有条目")
    @Test
    void readZipFile(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("read_test.zip").toFile();
        try (ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(zipFile), StandardCharsets.UTF_8)) {
            zos.putNextEntry(new ZipEntry("a.txt"));
            zos.write("a".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry("b.txt"));
            zos.write("b".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        final java.util.concurrent.atomic.AtomicInteger count =
                new java.util.concurrent.atomic.AtomicInteger(0);
        try (ZipFile zf = new ZipFile(zipFile)) {
            ZipUtil.read(zf, entry -> count.incrementAndGet());
        }
        assertEquals(2, count.get());
    }

}
