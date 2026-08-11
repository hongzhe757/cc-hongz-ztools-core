package cc.hongz.ztools.core.net.multipart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MultipartFormData 单元测试
 */
class MultipartFormDataTest {

	@Test
	void constructorTest() {
		MultipartFormData formData = new MultipartFormData();
		assertNotNull(formData);
		assertFalse(formData.isLoaded());
	}

	@Test
	void constructorWithSettingTest() {
		UploadSetting setting = new UploadSetting();
		setting.setMaxFileSize(1024 * 1024);
		
		MultipartFormData formData = new MultipartFormData(setting);
		assertNotNull(formData);
	}

	@Test
	void getParamNamesTest() {
		MultipartFormData formData = new MultipartFormData();
		assertNotNull(formData.getParamNames());
		assertTrue(formData.getParamNames().isEmpty());
	}

	@Test
	void getFileParamNamesTest() {
		MultipartFormData formData = new MultipartFormData();
		assertNotNull(formData.getFileParamNames());
		assertTrue(formData.getFileParamNames().isEmpty());
	}

	@Test
	void getParamTest() {
		MultipartFormData formData = new MultipartFormData();
		assertNull(formData.getParam("nonexistent"));
	}

	@Test
	void getFileTest() {
		MultipartFormData formData = new MultipartFormData();
		assertNull(formData.getFile("nonexistent"));
	}
}
