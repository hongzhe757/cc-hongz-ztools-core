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

/**
 * {@link OptNullBasicTypeGetter} 单元测试.
 * <p>
 * 该接口的 14 个 default 方法全部委托到 OptBasicTypeGetter 的 (key, null) 方法.
 * 通过 HashMap 实现 OptBasicTypeGetter 来覆盖全部 default 方法.
 *
 * @author hongzhe.qin
 */
class OptNullBasicTypeGetterTest {

	private TestGetter getter;

	@BeforeEach
	void setUp() {
		getter = new TestGetter();
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
		getter.map.put("enum", TestEnum.B);
	}

	// ==================== getObj(key) ====================

	@Test
	void getObj() {
		assertEquals("hello", getter.getObj("str"));
	}

	@Test
	void getObjNull() {
		assertNull(getter.getObj("nonExist"));
	}

	// ==================== getStr(key) ====================

	@Test
	void getStr() {
		assertEquals("hello", getter.getStr("str"));
	}

	@Test
	void getStrNull() {
		assertNull(getter.getStr("nonExist"));
	}

	// ==================== getInt(key) ====================

	@Test
	void getInt() {
		assertEquals(42, getter.getInt("int"));
	}

	@Test
	void getIntNull() {
		assertNull(getter.getInt("nonExist"));
	}

	// ==================== getShort(key) ====================

	@Test
	void getShort() {
		assertEquals((short) 7, getter.getShort("short"));
	}

	@Test
	void getShortNull() {
		assertNull(getter.getShort("nonExist"));
	}

	// ==================== getBool(key) ====================

	@Test
	void getBool() {
		assertEquals(true, getter.getBool("bool"));
	}

	@Test
	void getBoolNull() {
		assertNull(getter.getBool("nonExist"));
	}

	// ==================== getLong(key) ====================

	@Test
	void getLong() {
		assertEquals(123456789L, getter.getLong("long"));
	}

	@Test
	void getLongNull() {
		assertNull(getter.getLong("nonExist"));
	}

	// ==================== getChar(key) ====================

	@Test
	void getChar() {
		assertEquals('A', getter.getChar("char"));
	}

	@Test
	void getCharNull() {
		assertNull(getter.getChar("nonExist"));
	}

	// ==================== getFloat(key) ====================

	@Test
	void getFloat() {
		assertEquals(3.14f, getter.getFloat("float"), 0.001f);
	}

	@Test
	void getFloatNull() {
		assertNull(getter.getFloat("nonExist"));
	}

	// ==================== getDouble(key) ====================

	@Test
	void getDouble() {
		assertEquals(2.718, getter.getDouble("double"), 0.001);
	}

	@Test
	void getDoubleNull() {
		assertNull(getter.getDouble("nonExist"));
	}

	// ==================== getByte(key) ====================

	@Test
	void getByte() {
		assertEquals((byte) 0x0F, getter.getByte("byte"));
	}

	@Test
	void getByteNull() {
		assertNull(getter.getByte("nonExist"));
	}

	// ==================== getBigDecimal(key) ====================

	@Test
	void getBigDecimal() {
		assertEquals(new BigDecimal("99.99"), getter.getBigDecimal("bigDecimal"));
	}

	@Test
	void getBigDecimalNull() {
		assertNull(getter.getBigDecimal("nonExist"));
	}

	// ==================== getBigInteger(key) ====================

	@Test
	void getBigInteger() {
		assertEquals(BigInteger.valueOf(100), getter.getBigInteger("bigInteger"));
	}

	@Test
	void getBigIntegerNull() {
		assertNull(getter.getBigInteger("nonExist"));
	}

	// ==================== getEnum(key) ====================

	@Test
	void getEnum() {
		assertEquals(TestEnum.B, getter.getEnum(TestEnum.class, "enum"));
	}

	@Test
	void getEnumNull() {
		assertNull(getter.getEnum(TestEnum.class, "nonExist"));
	}

	// ==================== getDate(key) ====================

	@Test
	void getDate() {
		assertEquals(new Date(1000L), getter.getDate("date"));
	}

	@Test
	void getDateNull() {
		assertNull(getter.getDate("nonExist"));
	}

	// ==================== 辅助类 ====================

	enum TestEnum {
		A, B, C
	}

	/**
	 * 基于 HashMap 的 OptNullBasicTypeGetter 实现.
	 * 只需实现 OptBasicTypeGetter 的抽象方法，default 方法由接口提供.
	 */
	static class TestGetter implements OptNullBasicTypeGetter<String> {
		final Map<String, Object> map = new HashMap<>();

		// --- OptBasicTypeGetter 抽象方法实现 ---

		@Override
		public Object getObj(String key, Object defaultValue) {
			Object val = map.get(key);
			return val != null ? val : defaultValue;
		}

		@Override
		public String getStr(String key, String defaultValue) {
			Object val = map.get(key);
			return val instanceof String ? (String) val : defaultValue;
		}

		@Override
		public Integer getInt(String key, Integer defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Integer.valueOf(((Number) val).intValue()) : defaultValue;
		}

		@Override
		public Short getShort(String key, Short defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Short.valueOf(((Number) val).shortValue()) : defaultValue;
		}

		@Override
		public Boolean getBool(String key, Boolean defaultValue) {
			Object val = map.get(key);
			return val instanceof Boolean ? (Boolean) val : defaultValue;
		}

		@Override
		public Long getLong(String key, Long defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Long.valueOf(((Number) val).longValue()) : defaultValue;
		}

		@Override
		public Character getChar(String key, Character defaultValue) {
			Object val = map.get(key);
			return val instanceof Character ? (Character) val : defaultValue;
		}

		@Override
		public Float getFloat(String key, Float defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Float.valueOf(((Number) val).floatValue()) : defaultValue;
		}

		@Override
		public Double getDouble(String key, Double defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Double.valueOf(((Number) val).doubleValue()) : defaultValue;
		}

		@Override
		public Byte getByte(String key, Byte defaultValue) {
			Object val = map.get(key);
			return val instanceof Number ? Byte.valueOf(((Number) val).byteValue()) : defaultValue;
		}

		@Override
		public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
			Object val = map.get(key);
			return val instanceof BigDecimal ? (BigDecimal) val : defaultValue;
		}

		@Override
		public BigInteger getBigInteger(String key, BigInteger defaultValue) {
			Object val = map.get(key);
			return val instanceof BigInteger ? (BigInteger) val : defaultValue;
		}

		@Override
		public <E extends Enum<E>> E getEnum(Class<E> clazz, String key, E defaultValue) {
			Object val = map.get(key);
			return clazz.isInstance(val) ? clazz.cast(val) : defaultValue;
		}

		@Override
		public Date getDate(String key, Date defaultValue) {
			Object val = map.get(key);
			return val instanceof Date ? (Date) val : defaultValue;
		}
	}
}
