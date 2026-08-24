package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Convert 类型转换器")
class ConvertTest {

    // ==================== convert(Type, Object) ====================

    @Test
    @DisplayName("convert - null 值返回 null")
    void convert_nullValue_returnsNull() {
        assertNull(Convert.convert(String.class, null));
    }

    @Test
    @DisplayName("convert - null 类型返回原值")
    void convert_nullType_returnsValue() {
        Object val = "hello";
        assertEquals(val, Convert.convert((java.lang.reflect.Type) null, val));
    }

    @Test
    @DisplayName("convert - 同类型返回原值")
    void convert_sameType_returnsSameValue() {
        String val = "test";
        assertEquals("test", Convert.convert(String.class, val));
    }

    @Test
    @DisplayName("convert - 数字转 int")
    void convert_numberToInt() {
        assertEquals(42, Convert.convert(Integer.class, 42L));
    }

    @Test
    @DisplayName("convert - 数字转 long")
    void convert_numberToLong() {
        assertEquals(42L, Convert.convert(Long.class, 42));
    }

    @Test
    @DisplayName("convert - 数字转 double")
    void convert_numberToDouble() {
        assertEquals(3.14, Convert.convert(Double.class, 3.14f), 0.01);
    }

    @Test
    @DisplayName("convert - 数字转 float")
    void convert_numberToFloat() {
        assertEquals(3.14f, Convert.convert(Float.class, 3.14), 0.01);
    }

    @Test
    @DisplayName("convert - 数字转 short")
    void convert_numberToShort() {
        assertEquals((short) 5, Convert.convert(Short.class, 5));
    }

    @Test
    @DisplayName("convert - 数字转 byte")
    void convert_numberToByte() {
        assertEquals((byte) 1, Convert.convert(Byte.class, 1));
    }

    @Test
    @DisplayName("convert - 数字转 boolean")
    void convert_numberToBoolean() {
        assertEquals(Boolean.TRUE, Convert.convert(Boolean.class, 1));
        assertEquals(Boolean.FALSE, Convert.convert(Boolean.class, 0));
    }

    @Test
    @DisplayName("convert - 数字转 BigDecimal")
    void convert_numberToBigDecimal() {
        BigDecimal result = Convert.convert(BigDecimal.class, 42);
        assertEquals(new BigDecimal("42"), result);
    }

    @Test
    @DisplayName("convert - 数字转 BigInteger")
    void convert_numberToBigInteger() {
        BigInteger result = Convert.convert(BigInteger.class, 42L);
        assertEquals(BigInteger.valueOf(42L), result);
    }

    @Test
    @DisplayName("convert - 字符串转 int")
    void convert_stringToInt() {
        assertEquals(123, Convert.convert(Integer.class, "123"));
    }

    @Test
    @DisplayName("convert - 字符串转 long")
    void convert_stringToLong() {
        assertEquals(123L, Convert.convert(Long.class, "123"));
    }

    @Test
    @DisplayName("convert - 字符串转 double")
    void convert_stringToDouble() {
        assertEquals(1.5, Convert.convert(Double.class, "1.5"), 0.001);
    }

    @Test
    @DisplayName("convert - 字符串转 float")
    void convert_stringToFloat() {
        assertEquals(1.5f, Convert.convert(Float.class, "1.5"), 0.001);
    }

    @Test
    @DisplayName("convert - 字符串转 boolean")
    void convert_stringToBoolean() {
        assertEquals(Boolean.TRUE, Convert.convert(Boolean.class, "true"));
    }

    @Test
    @DisplayName("convert - 字符串转 BigDecimal")
    void convert_stringToBigDecimal() {
        assertEquals(new BigDecimal("99.99"), Convert.convert(BigDecimal.class, "99.99"));
    }

    @Test
    @DisplayName("convert - 字符串转 BigInteger")
    void convert_stringToBigInteger() {
        assertEquals(new BigInteger("100"), Convert.convert(BigInteger.class, "100"));
    }

    @Test
    @DisplayName("convert - int 转字符串")
    void convert_intToString() {
        assertEquals("42", Convert.convert(String.class, 42));
    }

    // ==================== toStr ====================

    @Test
    @DisplayName("toStr - null 返回默认值")
    void toStr_null_returnsDefault() {
        assertEquals("default", Convert.toStr(null, "default"));
    }

    @Test
    @DisplayName("toStr - 字符串返回原值")
    void toStr_string_returnsSame() {
        assertEquals("hello", Convert.toStr("hello", null));
    }

    @Test
    @DisplayName("toStr - 非字符串转字符串")
    void toStr_nonString_callsToString() {
        assertEquals("42", Convert.toStr(42, null));
    }

    // ==================== toInt ====================

    @Test
    @DisplayName("toInt - null 返回默认值")
    void toInt_null_returnsDefault() {
        assertEquals(Integer.valueOf(0), Convert.toInt(null, 0));
    }

    @Test
    @DisplayName("toInt - Integer 返回原值")
    void toInt_integer_returnsSame() {
        assertEquals(Integer.valueOf(5), Convert.toInt(5, 0));
    }

    @Test
    @DisplayName("toInt - 数字转换")
    void toInt_number_converts() {
        assertEquals(Integer.valueOf(3), Convert.toInt(3L, 0));
    }

    @Test
    @DisplayName("toInt - 字符串解析")
    void toInt_string_parses() {
        assertEquals(Integer.valueOf(99), Convert.toInt("99", 0));
    }

    @Test
    @DisplayName("toInt - 非法字符串返回默认值")
    void toInt_invalidString_returnsDefault() {
        assertEquals(Integer.valueOf(-1), Convert.toInt("abc", -1));
    }

    // ==================== toShort ====================

    @Test
    @DisplayName("toShort - null 返回默认值")
    void toShort_null_returnsDefault() {
        assertEquals(Short.valueOf((short) 0), Convert.toShort(null, (short) 0));
    }

    @Test
    @DisplayName("toShort - 数字转换")
    void toShort_number_converts() {
        assertEquals(Short.valueOf((short) 7), Convert.toShort(7, (short) 0));
    }

    // ==================== toBool ====================

    @Test
    @DisplayName("toBool - null 返回默认值")
    void toBool_null_returnsDefault() {
        assertEquals(Boolean.FALSE, Convert.toBool(null, Boolean.FALSE));
    }

    @Test
    @DisplayName("toBool - Boolean 返回原值")
    void toBool_boolean_returnsSame() {
        assertEquals(Boolean.TRUE, Convert.toBool(Boolean.TRUE, null));
    }

    @Test
    @DisplayName("toBool - 非零为 true")
    void toBool_number_nonZeroIsTrue() {
        assertEquals(Boolean.TRUE, Convert.toBool(1, false));
        assertEquals(Boolean.FALSE, Convert.toBool(0, true));
    }

    @Test
    @DisplayName("toBool - true 字符串")
    void toBool_string_trueValues() {
        assertEquals(Boolean.TRUE, Convert.toBool("true", false));
        assertEquals(Boolean.TRUE, Convert.toBool("yes", false));
        assertEquals(Boolean.TRUE, Convert.toBool("1", false));
    }

    @Test
    @DisplayName("toBool - false 字符串")
    void toBool_string_falseValues() {
        assertEquals(Boolean.FALSE, Convert.toBool("false", true));
        assertEquals(Boolean.FALSE, Convert.toBool("no", true));
        assertEquals(Boolean.FALSE, Convert.toBool("0", true));
    }

    @Test
    @DisplayName("toBool - 未识别返回默认值")
    void toBool_string_unrecognized_returnsDefault() {
        assertEquals(Boolean.FALSE, Convert.toBool("maybe", true));
    }

    // ==================== toLong ====================

    @Test
    @DisplayName("toLong - null 返回默认值")
    void toLong_null_returnsDefault() {
        assertEquals(Long.valueOf(0L), Convert.toLong(null, 0L));
    }

    @Test
    @DisplayName("toLong - 字符串解析")
    void toLong_string_parses() {
        assertEquals(Long.valueOf(999L), Convert.toLong("999", 0L));
    }

    @Test
    @DisplayName("toLong - 非法字符串返回默认值")
    void toLong_invalidString_returnsDefault() {
        assertEquals(Long.valueOf(-1L), Convert.toLong("xyz", -1L));
    }

    // ==================== toChar ====================

    @Test
    @DisplayName("toChar - null 返回默认值")
    void toChar_null_returnsDefault() {
        assertEquals(Character.valueOf('x'), Convert.toChar(null, 'x'));
    }

    @Test
    @DisplayName("toChar - char 返回原值")
    void toChar_char_returnsSame() {
        assertEquals(Character.valueOf('A'), Convert.toChar('A', 'x'));
    }

    @Test
    @DisplayName("toChar - 字符串取首字符")
    void toChar_string_returnsFirstChar() {
        assertEquals(Character.valueOf('H'), Convert.toChar("Hello", 'x'));
    }

    @Test
    @DisplayName("toChar - 空字符串返回默认值")
    void toChar_emptyString_returnsDefault() {
        assertEquals(Character.valueOf('x'), Convert.toChar("", 'x'));
    }

    // ==================== toFloat ====================

    @Test
    @DisplayName("toFloat - null 返回默认值")
    void toFloat_null_returnsDefault() {
        assertEquals(Float.valueOf(0f), Convert.toFloat(null, 0f));
    }

    @Test
    @DisplayName("toFloat - 字符串解析")
    void toFloat_string_parses() {
        assertEquals(2.5f, Convert.toFloat("2.5", 0f), 0.001);
    }

    // ==================== toDouble ====================

    @Test
    @DisplayName("toDouble - null 返回默认值")
    void toDouble_null_returnsDefault() {
        assertEquals(Double.valueOf(0.0), Convert.toDouble(null, 0.0));
    }

    @Test
    @DisplayName("toDouble - 字符串解析")
    void toDouble_string_parses() {
        assertEquals(3.14, Convert.toDouble("3.14", 0.0), 0.001);
    }

    // ==================== toByte ====================

    @Test
    @DisplayName("toByte - null 返回默认值")
    void toByte_null_returnsDefault() {
        assertEquals(Byte.valueOf((byte) 0), Convert.toByte(null, (byte) 0));
    }

    @Test
    @DisplayName("toByte - 字符串解析")
    void toByte_string_parses() {
        assertEquals(Byte.valueOf((byte) 10), Convert.toByte("10", (byte) 0));
    }

    // ==================== toBigDecimal ====================

    @Test
    @DisplayName("toBigDecimal - null 返回默认值")
    void toBigDecimal_null_returnsDefault() {
        assertNull(Convert.toBigDecimal(null, null));
    }

    @Test
    @DisplayName("toBigDecimal - 字符串解析")
    void toBigDecimal_string_parses() {
        assertEquals(new BigDecimal("123.45"), Convert.toBigDecimal("123.45", null));
    }

    // ==================== toBigInteger ====================

    @Test
    @DisplayName("toBigInteger - null 返回默认值")
    void toBigInteger_null_returnsDefault() {
        assertNull(Convert.toBigInteger(null, null));
    }

    @Test
    @DisplayName("toBigInteger - 字符串解析")
    void toBigInteger_string_parses() {
        assertEquals(new BigInteger("999"), Convert.toBigInteger("999", null));
    }

    // ==================== toEnum ====================

    enum TestEnum { A, B, C }

    @Test
    @DisplayName("toEnum - null 返回默认值")
    void toEnum_null_returnsDefault() {
        assertEquals(TestEnum.A, Convert.toEnum(TestEnum.class, null, TestEnum.A));
    }

    @Test
    @DisplayName("toEnum - 枚举值返回原值")
    void toEnum_enumValue_returnsSame() {
        assertEquals(TestEnum.B, Convert.toEnum(TestEnum.class, TestEnum.B, TestEnum.A));
    }

    @Test
    @DisplayName("toEnum - 字符串解析")
    void toEnum_string_parses() {
        assertEquals(TestEnum.C, Convert.toEnum(TestEnum.class, "C", TestEnum.A));
    }

    @Test
    @DisplayName("toEnum - 非法字符串返回默认值")
    void toEnum_invalidString_returnsDefault() {
        assertEquals(TestEnum.A, Convert.toEnum(TestEnum.class, "INVALID", TestEnum.A));
    }

    // ==================== toDate ====================

    @Test
    @DisplayName("toDate - null 返回默认值")
    void toDate_null_returnsDefault() {
        assertNull(Convert.toDate(null, null));
    }

    @Test
    @DisplayName("toDate - Date 返回原值")
    void toDate_date_returnsSame() {
        Date now = new Date();
        assertEquals(now, Convert.toDate(now, null));
    }

    @Test
    @DisplayName("toDate - Calendar 转换")
    void toDate_calendar_converts() {
        Calendar cal = Calendar.getInstance();
        Date result = Convert.toDate(cal, null);
        assertEquals(cal.getTime(), result);
    }

    @Test
    @DisplayName("toDate - 数字转换")
    void toDate_number_converts() {
        long millis = 1000000L;
        Date result = Convert.toDate(millis, null);
        assertEquals(new Date(millis), result);
    }
}
