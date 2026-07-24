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
 * {@link OptNullBasicTypeFromObjectGetter} 单元测试.
 * <p>
 * 通过 HashMap 实现覆盖全部 default 方法.
 *
 * @author hongzhe.qin
 */
class OptNullBasicTypeFromObjectGetterTest {

	/** 被测实例（基于 HashMap） */
	private TestFromObjectGetter getter;

	@BeforeEach
	void setUp() {
		getter = new TestFromObjectGetter();
		getter.map.put("str", "hello");
		getter.map.put("int", 42);
		getter.map.put("short", (short) 7);
		getter.map.put("bool", true);
		getter.map.put("long", 123456789L);
		getter.map.put("char", 'A');
		getter.map.put("float", 3.14f);
		getter.map.put("double", 2.718);
		getter.map.put("byte", (byte) 0x0F);
		getter.map.put("bigDecimal", new BigDecimal("99.99"));
		getter.map.put("bigInteger", BigInteger.valueOf(100));
		getter.map.put("date", new Date(1000L));
		getter.map.put("strInt", "123");
		getter.map.put("strBool", "true");
		getter.map.put("strLong", "9876543210");
	}

	// ==================== getObj ====================

	@Test
	void getObj() {
		assertEquals("hello", getter.getObj("str"));
	}

	@Test
	void getObjNull() {
		assertNull(getter.getObj("nonExist"));
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

	// ==================== getInt ====================

	@Test
	void getInt() {
		assertEquals(42, getter.getInt("int"));
	}

	@Test
	void getIntFromString() {
		assertEquals(123, getter.getInt("strInt"));
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
	void getBoolFromString() {
		assertTrue(getter.getBool("strBool"));
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
	void getLongFromString() {
		assertEquals(9876543210L, getter.getLong("strLong"));
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
		assertEquals((byte) 0x0F, getter.getByte("byte"));
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
	void getEnumFromNull() {
		assertNull(getter.getEnum(TestEnum.class, "nonExist"));
	}

	@Test
	void getEnumWithDefault() {
		assertEquals(TestEnum.B, getter.getEnum(TestEnum.class, "nonExist", TestEnum.B));
	}

	// ==================== getDate ====================

	@Test
	void getDate() {
		assertEquals(new Date(1000L), getter.getDate("date"));
	}

	@Test
	void getDateFromNull() {
		assertNull(getter.getDate("nonExist"));
	}

	@Test
	void getDateWithDefault() {
		Date defaultDate = new Date(0L);
		assertEquals(defaultDate, getter.getDate("nonExist", defaultDate));
	}

	// ==================== 辅助类 ====================

	enum TestEnum {
		A, B, C
	}

	/**
	 * 基于 HashMap 的 OptNullBasicTypeFromObjectGetter 实现.
	 */
	static class TestFromObjectGetter implements OptNullBasicTypeFromObjectGetter<String> {
		final Map<String, Object> map = new HashMap<>();

		@Override
		public Object getObj(String key) {
			return map.get(key);
		}

		@Override
		public Object getObj(String key, Object defaultValue) {
			Object val = map.get(key);
			return val != null ? val : defaultValue;
		}
	}
}
