package cc.hongz.ztools.core.util;

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

    // ----------------------------------------------------------------------- Gzip

    /**
     * 验证 gzip/unGzip 对称性.
     */
    @Test
    void gzipAndUnGzip() {
        final String content = "Hello, ZipUtil Gzip!";
        final byte[] compressed = ZipUtil.gzip(content, "UTF-8");
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        final String decompressed = ZipUtil.unGzip(compressed, "UTF-8");
        assertEquals(content, decompressed);
    }

    /**
     * 验证 gzip(byte[]) / unGzip(byte[]) 对称性.
     */
    @Test
    void gzipBytesAndUnGzipBytes() {
        final byte[] buf = "test data for gzip".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.gzip(buf);
        final byte[] decompressed = ZipUtil.unGzip(compressed);
        assertArrayEquals(buf, decompressed);
    }

    /**
     * 验证 gzip(InputStream) 返回非空.
     */
    @Test
    void gzipInputStream() {
        final byte[] data = "stream gzip test".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.gzip(new ByteArrayInputStream(data));
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    /**
     * 验证 gzip(File) 对文件进行压缩.
     */
    @Test
    void gzipFile(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("gzip_test.txt").toFile();
        Files.write(src.toPath(), "file content for gzip".getBytes(StandardCharsets.UTF_8));
        final byte[] compressed = ZipUtil.gzip(src);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    // ----------------------------------------------------------------------- Zlib

    /**
     * 验证 zlib/unZlib 对称性.
     */
    @Test
    void zlibAndUnZlib() {
        final String content = "Hello, ZipUtil Zlib!";
        final byte[] compressed = ZipUtil.zlib(content, "UTF-8", 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        final String decompressed = ZipUtil.unZlib(compressed, "UTF-8");
        assertEquals(content, decompressed);
    }

    /**
     * 验证 zlib(byte[]) / unZlib(byte[]) 对称性.
     */
    @Test
    void zlibBytesAndUnZlibBytes() {
        final byte[] buf = "test data for zlib".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.zlib(buf, 6);
        final byte[] decompressed = ZipUtil.unZlib(compressed);
        assertArrayEquals(buf, decompressed);
    }

    /**
     * 验证 zlib(InputStream) 返回非空.
     */
    @Test
    void zlibInputStream() {
        final byte[] data = "stream zlib test".getBytes(StandardCharsets.UTF_8);
        final byte[] compressed = ZipUtil.zlib(new ByteArrayInputStream(data), 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    /**
     * 验证 zlib(File) 对文件压缩.
     */
    @Test
    void zlibFile(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("zlib_test.txt").toFile();
        Files.write(src.toPath(), "file content for zlib".getBytes(StandardCharsets.UTF_8));
        final byte[] compressed = ZipUtil.zlib(src, 6);
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);
    }

    // ----------------------------------------------------------------------- Zip

    /**
     * 验证 zip(File) 生成 zip 文件.
     */
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

    /**
     * 验证 zip/unzip 对称性.
     */
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

        // 验证解压后的文件存在
        final File[] files = unzipDir.listFiles();
        assertNotNull(files);
        assertTrue(files.length > 0);
    }

    /**
     * 验证 zip(OutputStream...) 写入流.
     */
    @Test
    void zipToOutputStream(@TempDir final Path tempDir) throws IOException {
        final File src = tempDir.resolve("os_test.txt").toFile();
        Files.write(src.toPath(), "output stream zip".getBytes(StandardCharsets.UTF_8));

        final java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        ZipUtil.zip(bos, StandardCharsets.UTF_8, false, null, src);
        assertTrue(bos.size() > 0);
    }

    /**
     * 验证 zip(File, String, String) 将字符串写入 zip 指定条目.
     */
    @Test
    void zipWithData(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("data.zip").toFile();
        ZipUtil.zip(zipFile, "entry.txt", "hello entry");
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    /**
     * 验证 zip(File, String, InputStream) 将流写入 zip 指定条目.
     */
    @Test
    void zipWithInputStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("stream.zip").toFile();
        final byte[] data = "stream entry".getBytes(StandardCharsets.UTF_8);
        ZipUtil.zip(zipFile, "stream_entry.txt", new ByteArrayInputStream(data));
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    // ----------------------------------------------------------------------- Unzip

    /**
     * 验证 unzip(String) 解压到默认目录.
     */
    @Test
    void unzipByPath(@TempDir final Path tempDir) throws IOException {
        // 先创建 zip
        final File srcFile = tempDir.resolve("unzip_src.txt").toFile();
        Files.write(srcFile.toPath(), "unzip test".getBytes(StandardCharsets.UTF_8));
        final File zipFile = ZipUtil.zip(srcFile);

        final File unzipDir = ZipUtil.unzip(zipFile.getAbsolutePath());
        assertNotNull(unzipDir);
        assertTrue(unzipDir.exists());
    }

    /**
     * 验证 unzip(InputStream, File, Charset).
     */
    @Test
    void unzipFromStream(@TempDir final Path tempDir) throws IOException {
        final File srcFile = tempDir.resolve("stream_unzip.txt").toFile();
        Files.write(srcFile.toPath(), "stream unzip".getBytes(StandardCharsets.UTF_8));
        final File zipFile = ZipUtil.zip(srcFile);

        final File outDir = tempDir.resolve("stream_out").toFile();
        try (java.io.FileInputStream fis = new java.io.FileInputStream(zipFile)) {
            ZipUtil.unzip(fis, outDir, StandardCharsets.UTF_8);
        }
        assertTrue(outDir.exists());
    }

    // ----------------------------------------------------------------------- get / listFileNames

    /**
     * 验证 get(ZipFile, String) 获取 zip 中指定条目的流.
     */
    @Test
    void getEntryStream(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("get_test.zip").toFile();
        ZipUtil.zip(zipFile, "readme.txt", "get entry content");

        try (ZipFile zf = new ZipFile(zipFile)) {
            final java.io.InputStream is = ZipUtil.get(zf, "readme.txt");
            assertNotNull(is);
            final String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals("get entry content", content);
        }
    }

    /**
     * 验证 get(ZipFile, String) 对不存在的条目返回 null.
     */
    @Test
    void getEntryStreamNotFound(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("not_found.zip").toFile();
        ZipUtil.zip(zipFile, "exists.txt", "data");

        try (ZipFile zf = new ZipFile(zipFile)) {
            assertNull(ZipUtil.get(zf, "not_exists.txt"));
        }
    }

    /**
     * 验证 listFileNames 列出 zip 中指定目录下的文件名.
     */
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

    /**
     * 验证 listFileNames 对 null/空目录列出所有顶层文件名.
     */
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

    // ----------------------------------------------------------------------- toZipFile

    /**
     * 验证 toZipFile 正常返回 ZipFile.
     */
    @Test
    void toZipFile(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("toZip.zip").toFile();
        ZipUtil.zip(zipFile, "test.txt", "data");

        try (ZipFile zf = ZipUtil.toZipFile(zipFile, StandardCharsets.UTF_8)) {
            assertNotNull(zf);
            assertNotNull(zf.getEntry("test.txt"));
        }
    }

    /**
     * 验证 toZipFile 对 null charset 使用 UTF-8.
     */
    @Test
    void toZipFileNullCharset(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("null_charset.zip").toFile();
        ZipUtil.zip(zipFile, "test.txt", "data");

        try (ZipFile zf = ZipUtil.toZipFile(zipFile, null)) {
            assertNotNull(zf);
        }
    }

    // ----------------------------------------------------------------------- unzipFileBytes

    /**
     * 验证 unzipFileBytes 获取 zip 中指定条目的字节数组.
     */
    @Test
    void unzipFileBytes(@TempDir final Path tempDir) throws IOException {
        final File zipFile = tempDir.resolve("bytes.zip").toFile();
        final byte[] expected = "byte content".getBytes(StandardCharsets.UTF_8);
        ZipUtil.zip(zipFile, "bytes.txt", new ByteArrayInputStream(expected));

        final byte[] actual = ZipUtil.unzipFileBytes(zipFile, "bytes.txt");
        assertArrayEquals(expected, actual);
    }
}
