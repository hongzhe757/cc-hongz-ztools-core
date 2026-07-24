package cc.hongz.ztools.core.compress;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * compress 包单元测试.
 * 覆盖 Deflate 和 Gzip 的基本压缩/解压操作.
 *
 * @author hongzhe.qin
 */
class CompressTest {

	private static final String TEST_DATA = "Hello, this is a test string for compression! ";

	// ==================== Deflate ====================

	@Test
	void deflate_compressAndDecompress(@TempDir Path tempDir) throws IOException {
		byte[] input = TEST_DATA.getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream source = new ByteArrayInputStream(input);
		ByteArrayOutputStream target = new ByteArrayOutputStream();

		Deflate deflate = new Deflate(source, target, false);
		deflate.deflater(6);
		deflate.close();

		byte[] compressed = target.toByteArray();
		assertNotNull(compressed);
		assertTrue(compressed.length > 0);

		// Decompress
		ByteArrayInputStream compressedSource = new ByteArrayInputStream(compressed);
		ByteArrayOutputStream decompressedTarget = new ByteArrayOutputStream();
		Deflate inflate = new Deflate(compressedSource, decompressedTarget, false);
		inflate.inflater();
		inflate.close();

		assertEquals(TEST_DATA, decompressedTarget.toString(StandardCharsets.UTF_8.name()));
	}

	// ==================== Gzip ====================

	@Test
	void gzip_compressAndDecompress(@TempDir Path tempDir) throws IOException {
		byte[] input = TEST_DATA.getBytes(StandardCharsets.UTF_8);
		ByteArrayInputStream source = new ByteArrayInputStream(input);
		ByteArrayOutputStream target = new ByteArrayOutputStream();

		Gzip gzip = new Gzip(source, target);
		gzip.gzip();
		gzip.close();

		byte[] compressed = target.toByteArray();
		assertNotNull(compressed);
		assertTrue(compressed.length > 0);

		// Decompress
		ByteArrayInputStream compressedSource = new ByteArrayInputStream(compressed);
		ByteArrayOutputStream decompressedTarget = new ByteArrayOutputStream();
		Gzip gunzip = new Gzip(compressedSource, decompressedTarget);
		gunzip.unGzip();
		gunzip.close();

		assertEquals(TEST_DATA, decompressedTarget.toString(StandardCharsets.UTF_8.name()));
	}

	// ==================== ZipWriter/ZipReader ====================

	@Test
	void zipWriter_createZipFile(@TempDir Path tempDir) throws IOException {
		File zipFile = tempDir.resolve("test.zip").toFile();
		ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
		writer.add("hello.txt", new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8)));
		writer.close();

		assertTrue(zipFile.exists());
		assertTrue(zipFile.length() > 0);
	}

	@Test
	void zipReader_readZipFile(@TempDir Path tempDir) throws IOException {
		File zipFile = tempDir.resolve("test.zip").toFile();
		ZipWriter writer = ZipWriter.of(zipFile, StandardCharsets.UTF_8);
		writer.add("hello.txt", new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8)));
		writer.close();

		ZipReader reader = ZipReader.of(zipFile, StandardCharsets.UTF_8);
		assertNotNull(reader);
		reader.close();
	}
}
