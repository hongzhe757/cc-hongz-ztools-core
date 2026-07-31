package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JdkUtil 单元测试.
 */
@DisplayName("JdkUtil JDK版本工具类")
class JdkUtilTest {

	@Test
	@DisplayName("JVM_VERSION - 版本号应大于0")
	void jvmVersionShouldBePositive() {
		assertTrue(JdkUtil.JVM_VERSION > 0, "JVM_VERSION 应大于0");
	}

	@Test
	@DisplayName("JVM_VERSION - 应匹配当前JDK17版本")
	void jvmVersionShouldMatchCurrentJdk() {
		// 项目使用 JDK 17，版本号应为 17
		assertEquals(17, JdkUtil.JVM_VERSION);
	}

	@Test
	@DisplayName("IS_JDK8 - JDK17环境下应为false")
	void isJdk8ShouldBeFalse() {
		// 运行在 JDK17 上，不应为 JDK8
		assertFalse(JdkUtil.IS_JDK8);
	}

	@Test
	@DisplayName("IS_AT_LEAST_JDK17 - JDK17环境下应为true")
	void isAtLeastJdk17ShouldBeTrue() {
		assertTrue(JdkUtil.IS_AT_LEAST_JDK17);
	}

	@Test
	@DisplayName("IS_AT_LEAST_JDK25 - JDK17环境下应为false")
	void isAtLeastJdk25ShouldBeFalse() {
		// JDK17 环境下不应满足 >=25
		assertFalse(JdkUtil.IS_AT_LEAST_JDK25);
	}

	@Test
	@DisplayName("IS_ANDROID - 非Android环境应为false")
	void isAndroidShouldBeFalse() {
		// 非 Android 环境
		assertFalse(JdkUtil.IS_ANDROID);
	}
}
