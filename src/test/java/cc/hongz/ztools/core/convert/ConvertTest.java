package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Convert} 单元测试.
 *
 * @author hongzhe.qin
 */
class ConvertTest {

	// ==================== convert(Type, Object) ====================

	@Test
	void convert_nullValue_returnsNull() {
		assertNull(Convert.convert(String.class, null));
	}

	@Test
	void convert_nullType_returnsValue() {
		Object val = "hello";
		assertEquals(val, Convert.convert((java.lang.reflect.Type) null, val));
	}

	@Test
	void convert_sameType_returnsSameValue() {
		String val = "test";
		assertEquals("test", Convert.convert(String.class, val));
	}

	@Test
	void convert_numberToInt() {
		assertEquals(42, Convert.convert(Integer.class, 42L));
	}

	@Test
	void convert_numberToLong() {
		assertEquals(42L, Convert.convert(Long.class, 42));
	}

	@Test
	void convert_numberToDouble() {
		assertEquals(3.14, Convert.convert(Double.class, 3.14f), 0.01);
	}

	@Test
	void convert_numberToFloat() {
		assertEquals(3.14f, Convert.convert(Float.class, 3.14), 0.01);
	}

	@Test
	void convert_numberToShort() {
		assertEquals((short) 5, Convert.convert(Short.class, 5));
	}

	@Test
	void convert_numberToByte() {
		assertEquals((byte) 1, Convert.convert(Byte.class, 1));
	}

	@Test
	void convert_numberToBoolean() {
		assertEquals(Boolean.TRUE, Convert.convert(Boolean.class, 1));
		assertEquals(Boolean.FALSE, Convert.convert(Boolean.class, 0));
	}

	@Test
	void convert_numberToBigDecimal() {
		BigDecimal result = Convert.convert(BigDecimal.class, 42);
		assertEquals(new BigDecimal("42"), result);
	}

	@Test
	void convert_numberToBigInteger() {
		BigInteger result = Convert.convert(BigInteger.class, 42L);
		assertEquals(BigInteger.valueOf(42L), result);
	}

	@Test
	void convert_stringToInt() {
		assertEquals(123, Convert.convert(Integer.class, "123"));
	}

	@Test
	void convert_stringToLong() {
		assertEquals(123L, Convert.convert(Long.class, "123"));
	}

	@Test
	void convert_stringToDouble() {
		assertEquals(1.5, Convert.convert(Double.class, "1.5"), 0.001);
	}

	@Test
	void convert_stringToFloat() {
		assertEquals(1.5f, Convert.convert(Float.class, "1.5"), 0.001);
	}

	@Test
	void convert_stringToBoolean() {
		assertEquals(Boolean.TRUE, Convert.convert(Boolean.class, "true"));
	}

	@Test
	void convert_stringToBigDecimal() {
		assertEquals(new BigDecimal("99.99"), Convert.convert(BigDecimal.class, "99.99"));
	}

	@Test
	void convert_stringToBigInteger() {
		assertEquals(new BigInteger("100"), Convert.convert(BigInteger.class, "100"));
	}

	@Test
	void convert_intToString() {
		assertEquals("42", Convert.convert(String.class, 42));
	}

	// ==================== toStr ====================

	@Test
	void toStr_null_returnsDefault() {
		assertEquals("default", Convert.toStr(null, "default"));
	}

	@Test
	void toStr_string_returnsSame() {
		assertEquals("hello", Convert.toStr("hello", null));
	}

	@Test
	void toStr_nonString_callsToString() {
		assertEquals("42", Convert.toStr(42, null));
	}

	// ==================== toInt ====================

	@Test
	void toInt_null_returnsDefault() {
		assertEquals(Integer.valueOf(0), Convert.toInt(null, 0));
	}

	@Test
	void toInt_integer_returnsSame() {
		assertEquals(Integer.valueOf(5), Convert.toInt(5, 0));
	}

	@Test
	void toInt_number_converts() {
		assertEquals(Integer.valueOf(3), Convert.toInt(3L, 0));
	}

	@Test
	void toInt_string_parses() {
		assertEquals(Integer.valueOf(99), Convert.toInt("99", 0));
	}

	@Test
	void toInt_invalidString_returnsDefault() {
		assertEquals(Integer.valueOf(-1), Convert.toInt("abc", -1));
	}

	// ==================== toShort ====================

	@Test
	void toShort_null_returnsDefault() {
		assertEquals(Short.valueOf((short) 0), Convert.toShort(null, (short) 0));
	}

	@Test
	void toShort_number_converts() {
		assertEquals(Short.valueOf((short) 7), Convert.toShort(7, (short) 0));
	}

	// ==================== toBool ====================

	@Test
	void toBool_null_returnsDefault() {
		assertEquals(Boolean.FALSE, Convert.toBool(null, Boolean.FALSE));
	}

	@Test
	void toBool_boolean_returnsSame() {
		assertEquals(Boolean.TRUE, Convert.toBool(Boolean.TRUE, null));
	}

	@Test
	void toBool_number_nonZeroIsTrue() {
		assertEquals(Boolean.TRUE, Convert.toBool(1, false));
		assertEquals(Boolean.FALSE, Convert.toBool(0, true));
	}

	@Test
	void toBool_string_trueValues() {
		assertEquals(Boolean.TRUE, Convert.toBool("true", false));
		assertEquals(Boolean.TRUE, Convert.toBool("yes", false));
		assertEquals(Boolean.TRUE, Convert.toBool("1", false));
	}

	@Test
	void toBool_string_falseValues() {
		assertEquals(Boolean.FALSE, Convert.toBool("false", true));
		assertEquals(Boolean.FALSE, Convert.toBool("no", true));
		assertEquals(Boolean.FALSE, Convert.toBool("0", true));
	}

	@Test
	void toBool_string_unrecognized_returnsDefault() {
		assertEquals(Boolean.FALSE, Convert.toBool("maybe", true));
	}

	// ==================== toLong ====================

	@Test
	void toLong_null_returnsDefault() {
		assertEquals(Long.valueOf(0L), Convert.toLong(null, 0L));
	}

	@Test
	void toLong_string_parses() {
		assertEquals(Long.valueOf(999L), Convert.toLong("999", 0L));
	}

	@Test
	void toLong_invalidString_returnsDefault() {
		assertEquals(Long.valueOf(-1L), Convert.toLong("xyz", -1L));
	}

	// ==================== toChar ====================

	@Test
	void toChar_null_returnsDefault() {
		assertEquals(Character.valueOf('x'), Convert.toChar(null, 'x'));
	}

	@Test
	void toChar_char_returnsSame() {
		assertEquals(Character.valueOf('A'), Convert.toChar('A', 'x'));
	}

	@Test
	void toChar_string_returnsFirstChar() {
		assertEquals(Character.valueOf('H'), Convert.toChar("Hello", 'x'));
	}

	@Test
	void toChar_emptyString_returnsDefault() {
		assertEquals(Character.valueOf('x'), Convert.toChar("", 'x'));
	}

	// ==================== toFloat ====================

	@Test
	void toFloat_null_returnsDefault() {
		assertEquals(Float.valueOf(0f), Convert.toFloat(null, 0f));
	}

	@Test
	void toFloat_string_parses() {
		assertEquals(2.5f, Convert.toFloat("2.5", 0f), 0.001);
	}

	// ==================== toDouble ====================

	@Test
	void toDouble_null_returnsDefault() {
		assertEquals(Double.valueOf(0.0), Convert.toDouble(null, 0.0));
	}

	@Test
	void toDouble_string_parses() {
		assertEquals(3.14, Convert.toDouble("3.14", 0.0), 0.001);
	}

	// ==================== toByte ====================

	@Test
	void toByte_null_returnsDefault() {
		assertEquals(Byte.valueOf((byte) 0), Convert.toByte(null, (byte) 0));
	}

	@Test
	void toByte_string_parses() {
		assertEquals(Byte.valueOf((byte) 10), Convert.toByte("10", (byte) 0));
	}

	// ==================== toBigDecimal ====================

	@Test
	void toBigDecimal_null_returnsDefault() {
		assertNull(Convert.toBigDecimal(null, null));
	}

	@Test
	void toBigDecimal_string_parses() {
		assertEquals(new BigDecimal("123.45"), Convert.toBigDecimal("123.45", null));
	}

	// ==================== toBigInteger ====================

	@Test
	void toBigInteger_null_returnsDefault() {
		assertNull(Convert.toBigInteger(null, null));
	}

	@Test
	void toBigInteger_string_parses() {
		assertEquals(new BigInteger("999"), Convert.toBigInteger("999", null));
	}

	// ==================== toEnum ====================

	enum TestEnum { A, B, C }

	@Test
	void toEnum_null_returnsDefault() {
		assertEquals(TestEnum.A, Convert.toEnum(TestEnum.class, null, TestEnum.A));
	}

	@Test
	void toEnum_enumValue_returnsSame() {
		assertEquals(TestEnum.B, Convert.toEnum(TestEnum.class, TestEnum.B, TestEnum.A));
	}

	@Test
	void toEnum_string_parses() {
		assertEquals(TestEnum.C, Convert.toEnum(TestEnum.class, "C", TestEnum.A));
	}

	@Test
	void toEnum_invalidString_returnsDefault() {
		assertEquals(TestEnum.A, Convert.toEnum(TestEnum.class, "INVALID", TestEnum.A));
	}

	// ==================== toDate ====================

	@Test
	void toDate_null_returnsDefault() {
		assertNull(Convert.toDate(null, null));
	}

	@Test
	void toDate_date_returnsSame() {
		Date now = new Date();
		assertEquals(now, Convert.toDate(now, null));
	}

	@Test
	void toDate_calendar_converts() {
		Calendar cal = Calendar.getInstance();
		Date result = Convert.toDate(cal, null);
		assertEquals(cal.getTime(), result);
	}

	@Test
	void toDate_number_converts() {
		long millis = 1000000L;
		Date result = Convert.toDate(millis, null);
		assertEquals(new Date(millis), result);
	}
}
