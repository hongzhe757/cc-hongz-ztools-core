package cc.hongz.ztools.core.getter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link OptNullBasicTypeFromStringGetter} 单元测试.
 * <p>
 * 通过 String HashMap 实现覆盖全部 default 方法（所有类型从 String 转换）.
 *
 * @author hongzhe.qin
 */
class OptNullBasicTypeFromStringGetterTest {

	private TestFromStringGetter getter;

	@BeforeEach
	void setUp() {
		getter = new TestFromStringGetter();
		getter.map.put("str", "hello");
		getter.map.put("int", "42");
		getter.map.put("short", "7");
		getter.map.put("bool", "true");
		getter.map.put("long", "123456789");
		getter.map.put("char", "A");
		getter.map.put("float", "3.14");
		getter.map.put("double", "2.718");
		getter.map.put("byte", "15");
		getter.map.put("bigDecimal", "99.99");
		getter.map.put("bigInteger", "100");
		getter.map.put("enum", "B");
	}

	// ==================== getStr ====================

	@Test
	void getStr() {
		assertEquals("hello", getter.getStr("str"));
	}

	@Test
	void getStrFromNull() {
		assertNull(getter.getStr("nonExist"));
	}

	@Test
	void getStrWithDefault() {
		assertEquals("default", getter.getStr("nonExist", "default"));
	}

	// ==================== getObj (delegates to getStr) ====================

	@Test
	void getObj() {
		assertEquals("hello", getter.getObj("str"));
	}

	@Test
	void getObjFromNull() {
		assertNull(getter.getObj("nonExist"));
	}

	@Test
	void getObjWithDefault() {
		assertEquals("def", getter.getObj("nonExist", "def"));
	}

	// ==================== getInt ====================

	@Test
	void getInt() {
		assertEquals(42, getter.getInt("int"));
	}

	@Test
	void getIntFromNull() {
		assertNull(getter.getInt("nonExist"));
	}

	@Test
	void getIntWithDefault() {
		assertEquals(99, getter.getInt("nonExist", 99));
	}

	// ==================== getShort ====================

	@Test
	void getShort() {
		assertEquals((short) 7, getter.getShort("short"));
	}

	@Test
	void getShortFromNull() {
		assertNull(getter.getShort("nonExist"));
	}

	// ==================== getBool ====================

	@Test
	void getBool() {
		assertTrue(getter.getBool("bool"));
	}

	@Test
	void getBoolFromNull() {
		assertNull(getter.getBool("nonExist"));
	}

	// ==================== getLong ====================

	@Test
	void getLong() {
		assertEquals(123456789L, getter.getLong("long"));
	}

	@Test
	void getLongFromNull() {
		assertNull(getter.getLong("nonExist"));
	}

	// ==================== getChar ====================

	@Test
	void getChar() {
		assertEquals('A', getter.getChar("char"));
	}

	@Test
	void getCharFromNull() {
		assertNull(getter.getChar("nonExist"));
	}

	// ==================== getFloat ====================

	@Test
	void getFloat() {
		assertEquals(3.14f, getter.getFloat("float"), 0.001f);
	}

	@Test
	void getFloatFromNull() {
		assertNull(getter.getFloat("nonExist"));
	}

	// ==================== getDouble ====================

	@Test
	void getDouble() {
		assertEquals(2.718, getter.getDouble("double"), 0.001);
	}

	@Test
	void getDoubleFromNull() {
		assertNull(getter.getDouble("nonExist"));
	}

	// ==================== getByte ====================

	@Test
	void getByte() {
		assertEquals((byte) 15, getter.getByte("byte"));
	}

	@Test
	void getByteFromNull() {
		assertNull(getter.getByte("nonExist"));
	}

	// ==================== getBigDecimal ====================

	@Test
	void getBigDecimal() {
		assertEquals(new BigDecimal("99.99"), getter.getBigDecimal("bigDecimal"));
	}

	@Test
	void getBigDecimalFromNull() {
		assertNull(getter.getBigDecimal("nonExist"));
	}

	// ==================== getBigInteger ====================

	@Test
	void getBigInteger() {
		assertEquals(BigInteger.valueOf(100), getter.getBigInteger("bigInteger"));
	}

	@Test
	void getBigIntegerFromNull() {
		assertNull(getter.getBigInteger("nonExist"));
	}

	// ==================== getEnum ====================

	@Test
	void getEnum() {
		assertEquals(TestEnum.B, getter.getEnum(TestEnum.class, "enum"));
	}

	@Test
	void getEnumFromNull() {
		assertNull(getter.getEnum(TestEnum.class, "nonExist"));
	}

	@Test
	void getEnumWithDefault() {
		assertEquals(TestEnum.C, getter.getEnum(TestEnum.class, "nonExist", TestEnum.C));
	}

	// ==================== getDate ====================

	@Test
	void getDateFromNull() {
		assertNull(getter.getDate("nonExist"));
	}

	@Test
	void getDateWithDefault() {
		Date defaultDate = new Date(0L);
		// String 无法直接转 Date，返回 defaultValue
		assertEquals(defaultDate, getter.getDate("str", defaultDate));
	}

	// ==================== 辅助类 ====================

	enum TestEnum {
		A, B, C
	}

	/**
	 * 基于 String HashMap 的 OptNullBasicTypeFromStringGetter 实现.
	 */
	static class TestFromStringGetter implements OptNullBasicTypeFromStringGetter<String> {
		final Map<String, String> map = new HashMap<>();

		@Override
		public String getStr(String key, String defaultValue) {
			String val = map.get(key);
			return val != null ? val : defaultValue;
		}
	}
}
