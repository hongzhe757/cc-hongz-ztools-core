package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JdkUtil 单元测试
 */
class JdkUtilTest {

	@Test
	void jvmVersionShouldBePositive() {
		assertTrue(JdkUtil.JVM_VERSION > 0, "JVM_VERSION 应大于0");
	}

	@Test
	void jvmVersionShouldMatchCurrentJdk() {
		// 项目使用 JDK 17，版本号应为 17
		assertEquals(17, JdkUtil.JVM_VERSION);
	}

	@Test
	void isJdk8ShouldBeFalse() {
		// 运行在 JDK17 上，不应为 JDK8
		assertFalse(JdkUtil.IS_JDK8);
	}

	@Test
	void isAtLeastJdk17ShouldBeTrue() {
		assertTrue(JdkUtil.IS_AT_LEAST_JDK17);
	}

	@Test
	void isAtLeastJdk25ShouldBeFalse() {
		// JDK17 环境下不应满足 >=25
		assertFalse(JdkUtil.IS_AT_LEAST_JDK25);
	}

	@Test
	void isAndroidShouldBeFalse() {
		// 非 Android 环境
		assertFalse(JdkUtil.IS_ANDROID);
	}
}
