package cc.hongz.ztools.core.net.multipart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UploadFileHeader 单元测试
 */
class UploadFileHeaderTest {

	@Test
	void fileHeaderTest() {
		String header = "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n"
				+ "Content-Type: text/plain";
		UploadFileHeader fileHeader = new UploadFileHeader(header);
		assertTrue(fileHeader.isFile());
		assertEquals("file", fileHeader.getFormFieldName());
		assertEquals("test.txt", fileHeader.getFormFileName());
		assertEquals("test.txt", fileHeader.getFileName());
		// Content-Type解析后包含前导空格
		assertTrue(fileHeader.getContentType().contains("text/plain"));
		assertTrue(fileHeader.getMimeType().contains("text"));
		assertEquals("plain", fileHeader.getMimeSubtype());
	}

	@Test
	void nonFileHeaderTest() {
		String header = "Content-Disposition: form-data; name=\"username\"";
		UploadFileHeader fileHeader = new UploadFileHeader(header);
		assertFalse(fileHeader.isFile());
		assertEquals("username", fileHeader.getFormFieldName());
	}

	@Test
	void fileWithPathTest() {
		String header = "Content-Disposition: form-data; name=\"file\"; filename=\"/path/to/upload.txt\"\r\n"
				+ "Content-Type: application/octet-stream";
		UploadFileHeader fileHeader = new UploadFileHeader(header);
		assertTrue(fileHeader.isFile());
		// fileName包含分隔符后的部分
		assertNotNull(fileHeader.getFileName());
		assertTrue(fileHeader.getFileName().contains("upload.txt"));
	}

	@Test
	void contentDispositionTest() {
		String header = "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n"
				+ "Content-Type: image/png";
		UploadFileHeader fileHeader = new UploadFileHeader(header);
		assertTrue(fileHeader.getContentType().contains("image/png"));
		assertTrue(fileHeader.getMimeType().contains("image"));
		assertEquals("png", fileHeader.getMimeSubtype());
	}
}
