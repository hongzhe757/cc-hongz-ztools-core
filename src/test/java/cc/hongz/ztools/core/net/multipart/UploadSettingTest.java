package cc.hongz.ztools.core.net.multipart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UploadSetting 单元测试
 */
class UploadSettingTest {

	@Test
	void defaultValuesTest() {
		UploadSetting setting = new UploadSetting();
		assertEquals(-1, setting.getMaxFileSize());
		assertEquals(8192, setting.getMemoryThreshold());
		assertNull(setting.getTmpUploadPath());
		assertNull(setting.getFileExts());
		assertTrue(setting.isAllowFileExts());
	}

	@Test
	void setMaxFileSizeTest() {
		UploadSetting setting = new UploadSetting();
		setting.setMaxFileSize(1024 * 1024);
		assertEquals(1024 * 1024, setting.getMaxFileSize());
	}

	@Test
	void setMemoryThresholdTest() {
		UploadSetting setting = new UploadSetting();
		setting.setMemoryThreshold(4096);
		assertEquals(4096, setting.getMemoryThreshold());
	}

	@Test
	void setTmpUploadPathTest() {
		UploadSetting setting = new UploadSetting();
		setting.setTmpUploadPath("/tmp/upload");
		assertEquals("/tmp/upload", setting.getTmpUploadPath());
	}

	@Test
	void setFileExtsTest() {
		UploadSetting setting = new UploadSetting();
		String[] exts = {"jpg", "png", "gif"};
		setting.setFileExts(exts);
		assertArrayEquals(exts, setting.getFileExts());
	}

	@Test
	void setAllowFileExtsTest() {
		UploadSetting setting = new UploadSetting();
		setting.setAllowFileExts(false);
		assertFalse(setting.isAllowFileExts());
	}
}
