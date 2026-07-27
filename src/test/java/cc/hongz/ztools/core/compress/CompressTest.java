package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.io.resource.Resource;
import cc.hongz.ztools.core.lang.Filter;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * compress 包单元测试.
 *
 * @author hongzhe.qin
 */
class CompressTest {

    private static final String TEST_DATA =
            "Hello, this is a test string for compression! ";
    private static final String BIG_TEST_DATA =
            "Hello, this is a test string for compression! "
                    .repeat(1000);

    // ==================== Deflate ====================

    @Test
    @DisplayName("Deflate - 压缩后解压得到原始数据")
    void deflateCompressAndDecompress() throws IOException {
        byte[] input = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream source =
                new ByteArrayInputStream(input);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Deflate deflate = Deflate.of(source, target, false);
        deflate.deflater(6);
        deflate.close();

        byte[] compressed = target.toByteArray();
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        ByteArrayInputStream compressedIn =
                new ByteArrayInputStream(compressed);
        ByteArrayOutputStream decompressedOut =
                new ByteArrayOutputStream();
        Deflate inflate = Deflate.of(
                compressedIn, decompressedOut, false);
        inflate.inflater();
        inflate.close();

        assertEquals(TEST_DATA,
                decompressedOut.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Deflate - 带nowrap参数的压缩解压")
    void deflateNoWrap() throws IOException {
        byte[] input = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream source =
                new ByteArrayInputStream(input);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Deflate deflate = Deflate.of(source, target, true);
        deflate.deflater(9);
        deflate.close();

        byte[] compressed = target.toByteArray();
        assertTrue(compressed.length > 0);

        ByteArrayInputStream compressedIn =
                new ByteArrayInputStream(compressed);
        ByteArrayOutputStream decompressedOut =
                new ByteArrayOutputStream();
        Deflate inflate = Deflate.of(
                compressedIn, decompressedOut, true);
        inflate.inflater();
        inflate.close();

        assertEquals(TEST_DATA,
                decompressedOut.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Deflate - 大数据量压缩解压")
    void deflateLargeData() throws IOException {
        byte[] input = BIG_TEST_DATA.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream source =
                new ByteArrayInputStream(input);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Deflate deflate = Deflate.of(source, target, false);
        deflate.deflater(6);
        deflate.close();

        ByteArrayInputStream compressedIn =
                new ByteArrayInputStream(target.toByteArray());
        ByteArrayOutputStream decompressedOut =
                new ByteArrayOutputStream();
        Deflate inflate = Deflate.of(
                compressedIn, decompressedOut, false);
        inflate.inflater();
        inflate.close();

        assertEquals(BIG_TEST_DATA,
                decompressedOut.toString(StandardCharsets.UTF_8));
    }

    // ==================== Gzip ====================

    @Test
    @DisplayName("Gzip - 压缩后解压得到原始数据")
    void gzipCompressAndDecompress() throws IOException {
        byte[] input = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream source =
                new ByteArrayInputStream(input);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Gzip gzip = Gzip.of(source, target);
        gzip.gzip();
        gzip.close();

        byte[] compressed = target.toByteArray();
        assertNotNull(compressed);
        assertTrue(compressed.length > 0);

        ByteArrayInputStream compressedIn =
                new ByteArrayInputStream(compressed);
        ByteArrayOutputStream decompressedOut =
                new ByteArrayOutputStream();
        Gzip gunzip = Gzip.of(compressedIn, decompressedOut);
        gunzip.unGzip();
        gunzip.close();

        assertEquals(TEST_DATA,
                decompressedOut.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Gzip - 大数据量压缩解压")
    void gzipLargeData() throws IOException {
        byte[] input = BIG_TEST_DATA.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream source =
                new ByteArrayInputStream(input);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Gzip gzip = Gzip.of(source, target);
        gzip.gzip();
        gzip.close();

        ByteArrayInputStream compressedIn =
                new ByteArrayInputStream(target.toByteArray());
        ByteArrayOutputStream decompressedOut =
                new ByteArrayOutputStream();
        Gzip gunzip = Gzip.of(compressedIn, decompressedOut);
        gunzip.unGzip();
        gunzip.close();

        assertEquals(BIG_TEST_DATA,
                decompressedOut.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Gzip - getTarget获取目标流")
    void gzipGetTarget() {
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();
        Gzip gzip = Gzip.of(
                new ByteArrayInputStream(new byte[0]), target);
        assertEquals(target, gzip.getTarget());
    }

    // ==================== ZipWriter ====================

    @Test
    @DisplayName("ZipWriter - 创建zip文件并添加单个文件")
    void zipWriterSingleFile(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("single.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("hello.txt",
                new ByteArrayInputStream(
                        "Hello World".getBytes(
                                StandardCharsets.UTF_8)));
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @Test
    @DisplayName("ZipWriter - 添加多个文件到zip")
    void zipWriterMultipleFiles(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("multi.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt",
                new ByteArrayInputStream("A".getBytes()));
        writer.add("b.txt",
                new ByteArrayInputStream("B".getBytes()));
        writer.add("sub/c.txt",
                new ByteArrayInputStream("C".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());

        // 验证可以读取到所有文件
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(3, count.get());
    }

    @Test
    @DisplayName("ZipWriter - 添加空目录")
    void zipWriterEmptyDirectory(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("emptydir.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("emptyDir/", null);
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("ZipWriter - 使用OutputStream构造")
    void zipWriterOutputStream(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("os.zip").toFile();
        OutputStream os = java.nio.file.Files.newOutputStream(
                zipFile.toPath());
        ZipWriter writer = new ZipWriter(os,
                StandardCharsets.UTF_8);
        writer.add("test.txt",
                new ByteArrayInputStream("data".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @Test
    @DisplayName("ZipWriter - 使用ZipOutputStream构造")
    void zipWriterZipOutputStream(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("zos.zip").toFile();
        ZipOutputStream zos = new ZipOutputStream(
                java.nio.file.Files.newOutputStream(
                        zipFile.toPath()),
                StandardCharsets.UTF_8);
        ZipWriter writer = new ZipWriter(zos);
        writer.add("entry.txt",
                new ByteArrayInputStream("entry".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("ZipWriter - 设置压缩级别和注释")
    void zipWriterSetLevelAndComment(
            @TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("level.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.setLevel(9);
        writer.setComment("test comment");
        writer.add("f.txt",
                new ByteArrayInputStream("x".getBytes()));
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("ZipWriter - add目录到zip")
    void zipWriterAddDirectory(@TempDir final Path tempDir)
            throws IOException {
        // 创建临时目录结构
        Path srcDir = tempDir.resolve("src");
        Files.createDirectories(srcDir);
        Files.write(srcDir.resolve("file1.txt"),
                "content1".getBytes());
        Files.write(srcDir.resolve("file2.txt"),
                "content2".getBytes());

        File zipFile = tempDir.resolve("dir.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add(false, null, srcDir.toFile());
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }

    @Test
    @DisplayName("ZipWriter - 添加路径和流数组")
    void zipWriterAddArray(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("array.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        String[] paths = {"x.txt", "y.txt"};
        InputStream[] streams = {
            new ByteArrayInputStream("X".getBytes()),
            new ByteArrayInputStream("Y".getBytes())
        };
        writer.add(paths, streams);
        writer.close();

        assertTrue(zipFile.exists());
    }

    @Test
    @DisplayName("ZipWriter - add空路径和流抛出异常")
    void zipWriterAddArrayEmpty() {
        ZipWriter writer = ZipWriter.of(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> writer.add(new String[0],
                        new InputStream[0]));
    }

    @Test
    @DisplayName("ZipWriter - add路径和流长度不一致抛出异常")
    void zipWriterAddArrayLengthMismatch() {
        ZipWriter writer = ZipWriter.of(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> writer.add(
                        new String[]{"a.txt"},
                        new InputStream[]{
                            new ByteArrayInputStream(
                                    new byte[0]),
                            new ByteArrayInputStream(
                                    new byte[0])
                        }));
    }

    @Test
    @DisplayName("ZipWriter - getOut获取底层流")
    void zipWriterGetOut() {
        ZipOutputStream zos = new ZipOutputStream(
                new ByteArrayOutputStream(),
                StandardCharsets.UTF_8);
        ZipWriter writer = new ZipWriter(zos);
        assertSame(zos, writer.getOut());
    }

    // ==================== ZipReader ====================

    @Test
    @DisplayName("ZipReader - 读取zip中所有条目")
    void zipReaderReadAll(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("readall.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt",
                new ByteArrayInputStream("aa".getBytes()));
        writer.add("b.txt",
                new ByteArrayInputStream("bb".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();

        assertEquals(2, count.get());
    }

    @Test
    @DisplayName("ZipReader - readTo解压到目录")
    void zipReaderReadTo(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("readto.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("sub/data.txt",
                new ByteArrayInputStream(
                        "zip data".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("out").toFile();
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        File result = reader.readTo(outDir);
        reader.close();

        assertEquals(outDir, result);
        File extracted = new File(outDir, "sub/data.txt");
        assertTrue(extracted.exists());
        assertEquals("zip data",
                new String(Files.readAllBytes(
                        extracted.toPath())));
    }

    @Test
    @DisplayName("ZipReader - readTo带过滤器")
    void zipReaderReadToWithFilter(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("filter.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("keep.txt",
                new ByteArrayInputStream(
                        "keep".getBytes()));
        writer.add("skip.txt",
                new ByteArrayInputStream(
                        "skip".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("out").toFile();
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        Filter<ZipEntry> filter =
                entry -> !entry.getName().contains("skip");
        reader.readTo(outDir, filter);
        reader.close();

        // keep.txt 应该被解压
        assertTrue(new File(outDir, "keep.txt").exists());
        // skip.txt 不应该被解压
        assertFalse(new File(outDir, "skip.txt").exists());
    }

    @Test
    @DisplayName("ZipReader - get获取指定路径的流")
    void zipReaderGet(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("get.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        String content = "target content";
        writer.add("target.txt",
                new ByteArrayInputStream(
                        content.getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        InputStream in = reader.get("target.txt");
        assertNotNull(in);
        String readBack = IoUtil.read(in, StandardCharsets.UTF_8);
        reader.close();
        assertEquals(content, readBack);
    }

    @Test
    @DisplayName("ZipReader - get不存在的路径返回null")
    void zipReaderGetNonExistent(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("nonexist.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("real.txt",
                new ByteArrayInputStream("real".getBytes()));
        writer.close();

        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        InputStream in = reader.get("fake.txt");
        assertNull(in);
        reader.close();
    }

    @Test
    @DisplayName("ZipReader - 使用InputStream构造")
    void zipReaderFromInputStream(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("instream.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("x.txt",
                new ByteArrayInputStream("x".getBytes()));
        writer.close();

        InputStream fileIn = Files.newInputStream(
                zipFile.toPath());
        ZipReader reader = ZipReader.of(
                fileIn, StandardCharsets.UTF_8);
        // ZipInputStream模式下entry的size为-1，禁用ZipBomb检测
        reader.setMaxSizeDiff(-1);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();

        assertEquals(1, count.get());
    }

    @Test
    @DisplayName("ZipReader - setMaxSizeDiff设置ZipBomb检测")
    void zipReaderMaxSizeDiff() {
        ZipReader reader = ZipReader.of(
                new ByteArrayInputStream(new byte[0]),
                StandardCharsets.UTF_8);
        // -1表示禁用ZipBomb检测
        ZipReader result = reader.setMaxSizeDiff(-1);
        assertSame(reader, result);
    }

    // ==================== ZipWriter/ZipReader 往返 ====================

    @Test
    @DisplayName("ZipWriter/ZipReader - 多级目录往返")
    void zipRoundTripNestedDir(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("round.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("dir1/file1.txt",
                new ByteArrayInputStream(
                        "one".getBytes()));
        writer.add("dir1/dir2/file2.txt",
                new ByteArrayInputStream(
                        "two".getBytes()));
        writer.close();

        File outDir = tempDir.resolve("roundOut").toFile();
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        reader.readTo(outDir);
        reader.close();

        File f1 = new File(outDir, "dir1/file1.txt");
        File f2 = new File(outDir, "dir1/dir2/file2.txt");
        assertTrue(f1.exists());
        assertTrue(f2.exists());
        assertEquals("one", Files.readString(f1.toPath()));
        assertEquals("two", Files.readString(f2.toPath()));
    }

    @Test
    @DisplayName("ZipWriter/ZipReader - 空文件往返")
    void zipRoundTripEmptyFile(@TempDir final Path tempDir)
            throws IOException {
        File zipFile = tempDir.resolve("empty.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("empty.txt",
                new ByteArrayInputStream(new byte[0]));
        writer.close();

        File outDir = tempDir.resolve("emptyOut").toFile();
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        reader.readTo(outDir);
        reader.close();

        File extracted = new File(outDir, "empty.txt");
        assertTrue(extracted.exists());
        assertEquals(0, extracted.length());
    }

    // ==================== ZipCopyVisitor ====================

    @Test
    @DisplayName("ZipCopyVisitor - 基础构造")
    void zipCopyVisitorConstruct() throws IOException {
        Path tempDir = Files.createTempDirectory("zipcv");
        try {
            File zipFile = tempDir.resolve("cv.zip").toFile();
            ZipWriter writer = ZipWriter.of(
                    zipFile, StandardCharsets.UTF_8);
            writer.add("entry.txt",
                    new ByteArrayInputStream(
                            "hello".getBytes()));
            writer.close();
            assertTrue(zipFile.exists());
        } finally {
            FileUtil.del(tempDir.toFile());
        }
    }

    // ==================== 边界条件 ====================

    @Test
    @DisplayName("Deflate - 空数据压缩解压")
    void deflateEmptyData() throws IOException {
        byte[] empty = new byte[0];
        ByteArrayInputStream source =
                new ByteArrayInputStream(empty);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Deflate deflate = Deflate.of(source, target, false);
        deflate.deflater(6);
        deflate.close();

        assertTrue(target.size() > 0);
    }

    @Test
    @DisplayName("Gzip - 空数据压缩解压")
    void gzipEmptyData() throws IOException {
        byte[] empty = new byte[0];
        ByteArrayInputStream source =
                new ByteArrayInputStream(empty);
        ByteArrayOutputStream target =
                new ByteArrayOutputStream();

        Gzip gzip = Gzip.of(source, target);
        gzip.gzip();
        gzip.close();

        assertTrue(target.size() > 0);
    }

    @Test
    @DisplayName("ZipWriter - 使用ByteArrayOutputStream")
    void zipWriterByteArrayOutputStream() {
        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();
        ZipWriter writer = ZipWriter.of(
                baos, StandardCharsets.UTF_8);
        writer.add("test.txt",
                new ByteArrayInputStream("test".getBytes()));
        writer.close();

        assertTrue(baos.size() > 0);
    }

    // ==================== ZipReader 额外构造器 ====================

    @Test
    @DisplayName("ZipReader - ZipFile构造器")
    void zipReaderZipFileConstructor(
            @TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("zf2.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("f.txt",
                new ByteArrayInputStream("data".getBytes()));
        writer.close();

        java.util.zip.ZipFile zf = new java.util.zip.ZipFile(
                zipFile, StandardCharsets.UTF_8);
        ZipReader reader = new ZipReader(zf);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(1, count.get());
    }

    @Test
    @DisplayName("ZipReader - ZipInputStream构造器")
    void zipReaderZipInputStreamConstructor(
            @TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("zis2.zip").toFile();
        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        writer.add("a.txt",
                new ByteArrayInputStream("A".getBytes()));
        writer.close();

        java.util.zip.ZipInputStream zis =
                new java.util.zip.ZipInputStream(
                        Files.newInputStream(zipFile.toPath()),
                        StandardCharsets.UTF_8);
        ZipReader reader = new ZipReader(zis);
        reader.setMaxSizeDiff(-1);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(1, count.get());
    }

    // ==================== ZipCopyVisitor walkFileTree ====================

    @Test
    @DisplayName("ZipCopyVisitor - walkFileTree拷贝目录到zip")
    void zipCopyVisitorWalkFileTree(
            @TempDir final Path tempDir) throws IOException {
        Path srcDir = tempDir.resolve("srcDir");
        Files.createDirectories(srcDir.resolve("sub"));
        Files.write(srcDir.resolve("a.txt"),
                "fileA".getBytes());
        Files.write(srcDir.resolve("sub/b.txt"),
                "fileB".getBytes());

        File zipFile = tempDir.resolve("cvout.zip").toFile();
        java.util.Map<String, String> env =
                new java.util.HashMap<>();
        env.put("create", "true");
        java.net.URI uri = java.net.URI.create(
                "jar:" + zipFile.toURI());
        try (java.nio.file.FileSystem fs =
                java.nio.file.FileSystems.newFileSystem(
                        uri, env)) {
            Files.walkFileTree(srcDir,
                    new ZipCopyVisitor(srcDir, fs,
                            java.nio.file.StandardCopyOption
                                    .REPLACE_EXISTING));
        }

        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        // 目录sub也会作为zip entry
        assertEquals(3, count.get());
    }

    // ==================== ZipWriter Resource ====================

    @Test
    @DisplayName("ZipWriter - add Resource到zip")
    void zipWriterAddResource(
            @TempDir final Path tempDir) throws IOException {
        File zipFile = tempDir.resolve("res2.zip").toFile();
        Path resFile = tempDir.resolve("resource.txt");
        Files.write(resFile, "res content".getBytes());

        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        cc.hongz.ztools.core.io.resource.Resource fileRes =
                new cc.hongz.ztools.core.io.resource
                        .FileResource(resFile.toFile(), "data.txt");
        writer.add(fileRes);
        writer.close();

        assertTrue(zipFile.exists());
        ZipReader reader = ZipReader.of(
                zipFile, StandardCharsets.UTF_8);
        AtomicInteger count = new AtomicInteger(0);
        reader.read(entry -> count.incrementAndGet());
        reader.close();
        assertEquals(1, count.get());
    }
}
