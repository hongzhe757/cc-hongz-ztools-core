package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * io 包根级别工具类综合测试
 * 覆盖：NullOutputStream、LimitedInputStream、FastByteArrayOutputStream、
 * FastStringWriter、AppendableWriter、BOMInputStream、BomReader、BufferUtil
 */
class IoMiscTest {

	// ============================== NullOutputStream ==============================

	/**
	 * 测试 NullOutputStream 单例常量
	 */
	@Test
	void nullOutputStreamSingletonTest() {
		assertNotNull(NullOutputStream.NULL_OUTPUT_STREAM);
		assertSame(NullOutputStream.NULL_OUTPUT_STREAM, NullOutputStream.NULL_OUTPUT_STREAM);
	}

	/**
	 * 测试 NullOutputStream write 方法不抛异常
	 */
	@Test
	void nullOutputStreamWriteTest() throws IOException {
		NullOutputStream nos = NullOutputStream.NULL_OUTPUT_STREAM;
		nos.write(65);
		nos.write(new byte[]{1, 2, 3});
		nos.write(new byte[]{1, 2, 3}, 0, 2);
		// 不抛异常即通过
	}

	// ============================== LimitedInputStream ==============================

	/**
	 * 测试 LimitedInputStream 正常读取
	 */
	@Test
	void limitedInputStreamReadTest() throws IOException {
		byte[] data = {1, 2, 3, 4, 5};
		try (LimitedInputStream lis = new LimitedInputStream(new ByteArrayInputStream(data), 5)) {
			assertEquals(1, lis.read());
			assertEquals(2, lis.read());
			assertEquals(3, lis.read());
		}
	}

	/**
	 * 测试 LimitedInputStream 批量读取
	 */
	@Test
	void limitedInputStreamBulkReadTest() throws IOException {
		byte[] data = {10, 20, 30, 40, 50};
		try (LimitedInputStream lis = new LimitedInputStream(new ByteArrayInputStream(data), 5)) {
			byte[] buf = new byte[3];
			int count = lis.read(buf, 0, 3);
			assertEquals(3, count);
			assertEquals(10, buf[0]);
			assertEquals(20, buf[1]);
			assertEquals(30, buf[2]);
		}
	}

	/**
	 * 测试 LimitedInputStream 超出限制抛异常
	 */
	@Test
	void limitedInputStreamExceedLimitTest() {
		byte[] data = {1, 2, 3, 4, 5};
		LimitedInputStream lis = new LimitedInputStream(new ByteArrayInputStream(data), 2);
		assertThrows(IllegalStateException.class, () -> {
			lis.read();
			lis.read();
			lis.read(); // 第3次读取超过 maxSize=2
		});
	}

	/**
	 * 测试 LimitedInputStream skip 方法
	 */
	@Test
	void limitedInputStreamSkipTest() throws IOException {
		byte[] data = {1, 2, 3, 4, 5};
		try (LimitedInputStream lis = new LimitedInputStream(new ByteArrayInputStream(data), 5)) {
			long skipped = lis.skip(3);
			assertEquals(3, skipped);
			assertEquals(4, lis.read());
		}
	}

	// ============================== FastByteArrayOutputStream ==============================

	/**
	 * 测试 FastByteArrayOutputStream 基本写入和读取
	 */
	@Test
	void fastByteArrayOutputStreamBasicTest() {
		FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
		fos.write(65); // 'A'
		fos.write(new byte[]{66, 67}, 0, 2); // 'B', 'C'
		assertEquals(3, fos.size());
		assertEquals("ABC", fos.toString(StandardCharsets.UTF_8));
	}

	/**
	 * 测试 FastByteArrayOutputStream toByteArray
	 */
	@Test
	void fastByteArrayOutputStreamToByteArrayTest() {
		FastByteArrayOutputStream fos = new FastByteArrayOutputStream(256);
		byte[] expected = {1, 2, 3, 4, 5};
		fos.write(expected, 0, expected.length);
		assertArrayEquals(expected, fos.toByteArray());
	}

	/**
	 * 测试 FastByteArrayOutputStream reset
	 */
	@Test
	void fastByteArrayOutputStreamResetTest() throws IOException {
		FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
		fos.write(new byte[]{1, 2, 3});
		assertEquals(3, fos.size());
		fos.reset();
		assertEquals(0, fos.size());
	}

	/**
	 * 测试 FastByteArrayOutputStream writeTo
	 */
	@Test
	void fastByteArrayOutputStreamWriteToTest() throws IOException {
		FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
		fos.write(new byte[]{10, 20, 30});
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		fos.writeTo(baos);
		assertArrayEquals(new byte[]{10, 20, 30}, baos.toByteArray());
	}

	/**
	 * 测试 FastByteArrayOutputStream close 无异常
	 */
	@Test
	void fastByteArrayOutputStreamCloseTest() {
		FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
		fos.write(1);
		assertDoesNotThrow(fos::close);
	}

	// ============================== FastStringWriter ==============================

	/**
	 * 测试 FastStringWriter 基本写入
	 */
	@Test
	void fastStringWriterBasicTest() {
		FastStringWriter fsw = new FastStringWriter();
		fsw.write('H');
		fsw.write("ello");
		fsw.write(" World", 0, 6);
		assertEquals("Hello World", fsw.toString());
	}

	/**
	 * 测试 FastStringWriter 字符数组写入
	 */
	@Test
	void fastStringWriterCharArrayTest() {
		FastStringWriter fsw = new FastStringWriter();
		fsw.write(new char[]{'A', 'B', 'C'});
		assertEquals("ABC", fsw.toString());
	}

	/**
	 * 测试 FastStringWriter 带偏移的字符数组写入
	 */
	@Test
	void fastStringWriterCharArrayOffsetTest() {
		FastStringWriter fsw = new FastStringWriter();
		fsw.write(new char[]{'A', 'B', 'C', 'D'}, 1, 2);
		assertEquals("BC", fsw.toString());
	}

	/**
	 * 测试 FastStringWriter 自定义初始容量
	 */
	@Test
	void fastStringWriterCustomCapacityTest() {
		FastStringWriter fsw = new FastStringWriter(16);
		fsw.write("test");
		assertEquals("test", fsw.toString());
	}

	/**
	 * 测试 FastStringWriter flush/close 无异常
	 */
	@Test
	void fastStringWriterFlushCloseTest() {
		FastStringWriter fsw = new FastStringWriter();
		fsw.write("data");
		assertDoesNotThrow(() -> {
			fsw.flush();
			fsw.close();
		});
	}

	// ============================== AppendableWriter ==============================

	/**
	 * 测试 AppendableWriter 基本写入
	 */
	@Test
	void appendableWriterBasicTest() throws IOException {
		StringWriter sw = new StringWriter();
		try (AppendableWriter writer = new AppendableWriter(sw)) {
			writer.write("Hello");
			writer.write(' ');
			writer.write("World!!!", 0, 5);
		}
		assertEquals("Hello World", sw.toString());
	}

	/**
	 * 测试 AppendableWriter append 方法
	 */
	@Test
	void appendableWriterAppendTest() throws IOException {
		StringWriter sw = new StringWriter();
		try (AppendableWriter writer = new AppendableWriter(sw)) {
			writer.append('A');
			writer.append("BCD");
			writer.append("EFGH", 0, 2);
		}
		assertEquals("ABCDEF", sw.toString());
	}

	/**
	 * 测试 AppendableWriter 关闭后写入抛异常
	 */
	@Test
	void appendableWriterWriteAfterCloseTest() throws IOException {
		StringWriter sw = new StringWriter();
		AppendableWriter writer = new AppendableWriter(sw);
		writer.close();
		assertThrows(IOException.class, () -> writer.write("test"));
	}

	// ============================== BOMInputStream ==============================

	/**
	 * 测试 BOMInputStream 无 BOM 头的流
	 */
	@Test
	void bomInputStreamNoBomTest() throws IOException {
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		try (BOMInputStream bis = new BOMInputStream(new ByteArrayInputStream(data))) {
			assertEquals("UTF-8", bis.getCharset());
			assertEquals("UTF-8", bis.getDefaultCharset());
			byte[] result = bis.readAllBytes();
			assertEquals("Hello", new String(result, StandardCharsets.UTF_8));
		}
	}

	/**
	 * 测试 BOMInputStream 带 UTF-8 BOM 头的流
	 */
	@Test
	void bomInputStreamUtf8BomTest() throws IOException {
		byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
		byte[] content = "Hi".getBytes(StandardCharsets.UTF_8);
		byte[] data = new byte[bom.length + content.length];
		System.arraycopy(bom, 0, data, 0, bom.length);
		System.arraycopy(content, 0, data, bom.length, content.length);

		try (BOMInputStream bis = new BOMInputStream(new ByteArrayInputStream(data))) {
			assertEquals("UTF-8", bis.getCharset());
			byte[] result = bis.readAllBytes();
			assertEquals("Hi", new String(result, StandardCharsets.UTF_8));
		}
	}

	/**
	 * 测试 BOMInputStream 自定义默认编码
	 */
	@Test
	void bomInputStreamCustomDefaultCharsetTest() throws IOException {
		byte[] data = {65, 66, 67};
		try (BOMInputStream bis = new BOMInputStream(new ByteArrayInputStream(data), "GBK")) {
			assertEquals("GBK", bis.getDefaultCharset());
		}
	}

	// ============================== BomReader ==============================

	/**
	 * 测试 BomReader 读取无 BOM 的流
	 */
	@Test
	void bomReaderNoBomTest() throws IOException {
		byte[] data = "Test".getBytes(StandardCharsets.UTF_8);
		try (BomReader reader = new BomReader(new ByteArrayInputStream(data))) {
			char[] buf = new char[10];
			int len = reader.read(buf, 0, buf.length);
			assertEquals("Test", new String(buf, 0, len));
		}
	}

	// ============================== BufferUtil ==============================

	/**
	 * 测试 BufferUtil readUtf8Str
	 */
	@Test
	void bufferUtilReadUtf8StrTest() {
		ByteBuffer buffer = ByteBuffer.wrap("Hello".getBytes(StandardCharsets.UTF_8));
		assertEquals("Hello", BufferUtil.readUtf8Str(buffer));
	}

	/**
	 * 测试 BufferUtil readStr
	 */
	@Test
	void bufferUtilReadStrTest() {
		ByteBuffer buffer = ByteBuffer.wrap("你好".getBytes(StandardCharsets.UTF_8));
		assertEquals("你好", BufferUtil.readStr(buffer, StandardCharsets.UTF_8));
	}

	/**
	 * 测试 BufferUtil readBytes
	 */
	@Test
	void bufferUtilReadBytesTest() {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
		byte[] bytes = BufferUtil.readBytes(buffer);
		assertEquals(5, bytes.length);
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, bytes);
	}

	/**
	 * 测试 BufferUtil readBytes 带最大长度限制
	 */
	@Test
	void bufferUtilReadBytesMaxLengthTest() {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
		byte[] bytes = BufferUtil.readBytes(buffer, 3);
		assertArrayEquals(new byte[]{1, 2, 3}, bytes);
	}

	/**
	 * 测试 BufferUtil copy 区间拷贝
	 */
	@Test
	void bufferUtilCopyRangeTest() {
		ByteBuffer src = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5});
		ByteBuffer copied = BufferUtil.copy(src, 1, 4);
		assertArrayEquals(new byte[]{2, 3, 4}, copied.array());
	}

	/**
	 * 测试 BufferUtil lineEnd 查找换行
	 */
	@Test
	void bufferUtilLineEndTest() {
		ByteBuffer buffer = ByteBuffer.wrap("Hello\nWorld".getBytes(StandardCharsets.UTF_8));
		int end = BufferUtil.lineEnd(buffer);
		assertEquals(5, end); // \n 在位置5
	}

	/**
	 * 测试 BufferUtil lineEnd 未找到换行符
	 */
	@Test
	void bufferUtilLineEndNoNewlineTest() {
		ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes(StandardCharsets.UTF_8));
		int end = BufferUtil.lineEnd(buffer);
		assertEquals(-1, end);
	}
}
