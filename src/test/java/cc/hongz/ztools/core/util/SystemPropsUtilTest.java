package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SystemPropsUtil} 单元测试.
 */
class SystemPropsUtilTest {

	@Test
	void getWithDefault() {
		// 不存在的属性返回默认值
		assertEquals("defaultVal", SystemPropsUtil.get("non.existent.prop.xyz", "defaultVal"));
		// java.version 一定存在
		assertNotNull(SystemPropsUtil.get("java.version", null));
	}

	@Test
	void getQuiet() {
		// 不存在的属性返回null
		assertNull(SystemPropsUtil.get("non.existent.prop.xyz", true));
		assertNull(SystemPropsUtil.get("non.existent.prop.xyz", false));
	}

	@Test
	void get() {
		// java.version 系统属性存在
		assertNotNull(SystemPropsUtil.get("java.version"));
		// 不存在的属性返回null
		assertNull(SystemPropsUtil.get("non.existent.prop.xyz"));
	}

	@Test
	void getBoolean() {
		// 不存在的属性返回默认值
		assertTrue(SystemPropsUtil.getBoolean("non.existent.bool", true));
		assertFalse(SystemPropsUtil.getBoolean("non.existent.bool", false));

		// 设置一个系统属性然后读取
		SystemPropsUtil.set("test.bool.prop", "true");
		try {
			assertTrue(SystemPropsUtil.getBoolean("test.bool.prop", false));
		} finally {
			SystemPropsUtil.set("test.bool.prop", null);
		}
	}

	@Test
	void getInt() {
		// 不存在的属性返回默认值
		assertEquals(42, SystemPropsUtil.getInt("non.existent.int", 42));

		// 设置一个系统属性然后读取
		SystemPropsUtil.set("test.int.prop", "123");
		try {
			assertEquals(123, SystemPropsUtil.getInt("test.int.prop", 0));
		} finally {
			SystemPropsUtil.set("test.int.prop", null);
		}

		// 非数字返回默认值
		SystemPropsUtil.set("test.int.bad", "abc");
		try {
			assertEquals(99, SystemPropsUtil.getInt("test.int.bad", 99));
		} finally {
			SystemPropsUtil.set("test.int.bad", null);
		}
	}

	@Test
	void getLong() {
		// 不存在的属性返回默认值
		assertEquals(999L, SystemPropsUtil.getLong("non.existent.long", 999L));

		// 设置一个系统属性然后读取
		SystemPropsUtil.set("test.long.prop", "8888888888");
		try {
			assertEquals(8888888888L, SystemPropsUtil.getLong("test.long.prop", 0L));
		} finally {
			SystemPropsUtil.set("test.long.prop", null);
		}

		// 非数字返回默认值
		SystemPropsUtil.set("test.long.bad", "xyz");
		try {
			assertEquals(77L, SystemPropsUtil.getLong("test.long.bad", 77L));
		} finally {
			SystemPropsUtil.set("test.long.bad", null);
		}
	}

	@Test
	void getProps() {
		Properties props = SystemPropsUtil.getProps();
		assertNotNull(props);
		assertFalse(props.isEmpty());
	}

	@Test
	void setAndClear() {
		SystemPropsUtil.set("test.set.prop", "hello");
		assertEquals("hello", SystemPropsUtil.get("test.set.prop"));

		// 设为null移除属性
		SystemPropsUtil.set("test.set.prop", null);
		assertNull(SystemPropsUtil.get("test.set.prop"));
	}

	@Test
	void getEnv() {
		// PATH 环境变量在大多数系统上都存在
		String path = SystemPropsUtil.get("PATH");
		// 如果系统属性没有PATH，会尝试读环境变量
		// 这里只验证不抛异常
		assertDoesNotThrow(() -> SystemPropsUtil.get("PATH"));
	}
}
