package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BooleanUtil 单元测试.
 */
class BooleanUtilTest {

    // ==================================================================== negate

    /**
     * negate 包装类型 Boolean 取反.
     */
    @Test
    void negateBoolean() {
        assertEquals(Boolean.FALSE, BooleanUtil.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtil.negate(Boolean.FALSE));
        assertNull(BooleanUtil.negate((Boolean) null));
    }

    /**
     * negate 基本类型 boolean 取反.
     */
    @Test
    void negatePrimitive() {
        assertFalse(BooleanUtil.negate(true));
        assertTrue(BooleanUtil.negate(false));
    }

    // ==================================================================== isTrue / isFalse

    /**
     * isTrue 判断 Boolean 是否为 true.
     */
    @Test
    void isTrue() {
        assertTrue(BooleanUtil.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtil.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtil.isTrue((Boolean) null));
    }

    /**
     * isFalse 判断 Boolean 是否为 false.
     */
    @Test
    void isFalse() {
        assertFalse(BooleanUtil.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtil.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtil.isFalse((Boolean) null));
    }

    // ==================================================================== toBoolean

    /**
     * toBoolean 识别英文真值字符串.
     */
    @Test
    void toBooleanEnglishTrue() {
        assertTrue(BooleanUtil.toBoolean("true"));
        assertTrue(BooleanUtil.toBoolean("TRUE"));
        assertTrue(BooleanUtil.toBoolean("yes"));
        assertTrue(BooleanUtil.toBoolean("y"));
        assertTrue(BooleanUtil.toBoolean("t"));
        assertTrue(BooleanUtil.toBoolean("ok"));
        assertTrue(BooleanUtil.toBoolean("correct"));
        assertTrue(BooleanUtil.toBoolean("success"));
        assertTrue(BooleanUtil.toBoolean("on"));
        assertTrue(BooleanUtil.toBoolean("1"));
    }

    /**
     * toBoolean 识别中文真值字符串.
     */
    @Test
    void toBooleanChineseTrue() {
        assertTrue(BooleanUtil.toBoolean("是"));
        assertTrue(BooleanUtil.toBoolean("对"));
        assertTrue(BooleanUtil.toBoolean("真"));
        assertTrue(BooleanUtil.toBoolean("正确"));  // TRUE_SET 中为简体 "正确"
        assertTrue(BooleanUtil.toBoolean("开"));
        assertTrue(BooleanUtil.toBoolean("开启"));
        assertTrue(BooleanUtil.toBoolean("√"));
        assertTrue(BooleanUtil.toBoolean("☑"));
    }

    /**
     * toBoolean 非真值返回 false.
     */
    @Test
    void toBooleanFalseAndNull() {
        assertFalse(BooleanUtil.toBoolean("false"));
        assertFalse(BooleanUtil.toBoolean("no"));
        assertFalse(BooleanUtil.toBoolean("abc"));
        assertFalse(BooleanUtil.toBoolean(null));
        assertFalse(BooleanUtil.toBoolean(""));
        assertFalse(BooleanUtil.toBoolean("  "));
    }

    /**
     * toBoolean 前后空白自动 trim.
     */
    @Test
    void toBooleanTrimWhitespace() {
        assertTrue(BooleanUtil.toBoolean("  true  "));
        assertTrue(BooleanUtil.toBoolean("  yes  "));
    }

    // ==================================================================== toBooleanObject

    /**
     * toBooleanObject 三态转换.
     */
    @Test
    void toBooleanObjectThreeStates() {
        assertEquals(Boolean.TRUE, BooleanUtil.toBooleanObject("true"));
        assertEquals(Boolean.TRUE, BooleanUtil.toBooleanObject("是"));
        assertEquals(Boolean.FALSE, BooleanUtil.toBooleanObject("false"));
        assertEquals(Boolean.FALSE, BooleanUtil.toBooleanObject("否"));
        assertNull(BooleanUtil.toBooleanObject("abc"));
        assertNull(BooleanUtil.toBooleanObject(null));
        assertNull(BooleanUtil.toBooleanObject(""));
    }

    // ==================================================================== toInt / toInteger

    /**
     * toInt boolean 转 int.
     */
    @Test
    void toInt() {
        assertEquals(1, BooleanUtil.toInt(true));
        assertEquals(0, BooleanUtil.toInt(false));
    }

    /**
     * toInteger boolean 转 Integer.
     */
    @Test
    void toInteger() {
        assertEquals(Integer.valueOf(1), BooleanUtil.toInteger(true));
        assertEquals(Integer.valueOf(0), BooleanUtil.toInteger(false));
    }

    // ==================================================================== toChar / toCharacter

    /**
     * toChar boolean 转 char.
     */
    @Test
    void toChar() {
        assertEquals((char) 1, BooleanUtil.toChar(true));
        assertEquals((char) 0, BooleanUtil.toChar(false));
    }

    /**
     * toCharacter boolean 转 Character.
     */
    @Test
    void toCharacter() {
        assertEquals(Character.valueOf((char) 1), BooleanUtil.toCharacter(true));
        assertEquals(Character.valueOf((char) 0), BooleanUtil.toCharacter(false));
    }

    // ==================================================================== toByte / toByteObj

    /**
     * toByte boolean 转 byte.
     */
    @Test
    void toByte() {
        assertEquals((byte) 1, BooleanUtil.toByte(true));
        assertEquals((byte) 0, BooleanUtil.toByte(false));
    }

    /**
     * toByteObj boolean 转 Byte.
     */
    @Test
    void toByteObj() {
        assertEquals(Byte.valueOf((byte) 1), BooleanUtil.toByteObj(true));
        assertEquals(Byte.valueOf((byte) 0), BooleanUtil.toByteObj(false));
    }

    // ==================================================================== toLong / toLongObj

    /**
     * toLong boolean 转 long.
     */
    @Test
    void toLong() {
        assertEquals(1L, BooleanUtil.toLong(true));
        assertEquals(0L, BooleanUtil.toLong(false));
    }

    /**
     * toLongObj boolean 转 Long.
     */
    @Test
    void toLongObj() {
        assertEquals(Long.valueOf(1L), BooleanUtil.toLongObj(true));
        assertEquals(Long.valueOf(0L), BooleanUtil.toLongObj(false));
    }

    // ==================================================================== toShort / toShortObj

    /**
     * toShort boolean 转 short.
     */
    @Test
    void toShort() {
        assertEquals((short) 1, BooleanUtil.toShort(true));
        assertEquals((short) 0, BooleanUtil.toShort(false));
    }

    /**
     * toShortObj boolean 转 Short.
     */
    @Test
    void toShortObj() {
        assertEquals(Short.valueOf((short) 1), BooleanUtil.toShortObj(true));
        assertEquals(Short.valueOf((short) 0), BooleanUtil.toShortObj(false));
    }

    // ==================================================================== toFloat / toFloatObj

    /**
     * toFloat boolean 转 float.
     */
    @Test
    void toFloat() {
        assertEquals(1.0f, BooleanUtil.toFloat(true), 0.001f);
        assertEquals(0.0f, BooleanUtil.toFloat(false), 0.001f);
    }

    /**
     * toFloatObj boolean 转 Float.
     */
    @Test
    void toFloatObj() {
        assertEquals(Float.valueOf(1.0f), BooleanUtil.toFloatObj(true));
        assertEquals(Float.valueOf(0.0f), BooleanUtil.toFloatObj(false));
    }

    // ==================================================================== toDouble / toDoubleObj

    /**
     * toDouble boolean 转 double.
     */
    @Test
    void toDouble() {
        assertEquals(1.0, BooleanUtil.toDouble(true), 0.001);
        assertEquals(0.0, BooleanUtil.toDouble(false), 0.001);
    }

    /**
     * toDoubleObj boolean 转 Double.
     */
    @Test
    void toDoubleObj() {
        assertEquals(Double.valueOf(1.0), BooleanUtil.toDoubleObj(true));
        assertEquals(Double.valueOf(0.0), BooleanUtil.toDoubleObj(false));
    }

    // ==================================================================== toStringTrueFalse / toStringOnOff / toStringYesNo

    /**
     * toStringTrueFalse 转为 "true"/"false".
     */
    @Test
    void toStringTrueFalse() {
        assertEquals("true", BooleanUtil.toStringTrueFalse(true));
        assertEquals("false", BooleanUtil.toStringTrueFalse(false));
    }

    /**
     * toStringOnOff 转为 "on"/"off".
     */
    @Test
    void toStringOnOff() {
        assertEquals("on", BooleanUtil.toStringOnOff(true));
        assertEquals("off", BooleanUtil.toStringOnOff(false));
    }

    /**
     * toStringYesNo 转为 "yes"/"no".
     */
    @Test
    void toStringYesNo() {
        assertEquals("yes", BooleanUtil.toStringYesNo(true));
        assertEquals("no", BooleanUtil.toStringYesNo(false));
    }

    // ==================================================================== toString (自定义字符串)

    /**
     * toString 基本类型自定义转换.
     */
    @Test
    void toStringPrimitive() {
        assertEquals("Y", BooleanUtil.toString(true, "Y", "N"));
        assertEquals("N", BooleanUtil.toString(false, "Y", "N"));
    }

    /**
     * toString 包装类型三态自定义转换.
     */
    @Test
    void toStringWrapper() {
        assertEquals("Y", BooleanUtil.toString(Boolean.TRUE, "Y", "N", "U"));
        assertEquals("N", BooleanUtil.toString(Boolean.FALSE, "Y", "N", "U"));
        assertEquals("U", BooleanUtil.toString((Boolean) null, "Y", "N", "U"));
    }

    // ==================================================================== and

    /**
     * and 基本类型逻辑与.
     */
    @Test
    void andPrimitive() {
        assertTrue(BooleanUtil.and(true, true));
        assertTrue(BooleanUtil.and(true, true, true));
        assertFalse(BooleanUtil.and(true, false));
        assertFalse(BooleanUtil.and(false, false));
        assertFalse(BooleanUtil.and(true, true, false));
    }

    /**
     * and 空数组抛出异常.
     */
    @Test
    void andPrimitiveEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::and);
    }

    /**
     * andOfWrap 包装类型逻辑与.
     */
    @Test
    void andOfWrap() {
        assertEquals(Boolean.TRUE, BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtil.andOfWrap(Boolean.FALSE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    /**
     * andOfWrap null 元素视为 false.
     */
    @Test
    void andOfWrapWithNull() {
        assertEquals(Boolean.FALSE, BooleanUtil.andOfWrap(Boolean.TRUE, (Boolean) null));
    }

    /**
     * andOfWrap 空数组抛出异常.
     */
    @Test
    void andOfWrapEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::andOfWrap);
    }

    // ==================================================================== or

    /**
     * or 基本类型逻辑或.
     */
    @Test
    void orPrimitive() {
        assertTrue(BooleanUtil.or(true, true));
        assertTrue(BooleanUtil.or(true, false));
        assertTrue(BooleanUtil.or(false, false, true));
        assertFalse(BooleanUtil.or(false, false));
        assertFalse(BooleanUtil.or(false, false, false));
    }

    /**
     * or 空数组抛出异常.
     */
    @Test
    void orPrimitiveEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::or);
    }

    /**
     * orOfWrap 包装类型逻辑或.
     */
    @Test
    void orOfWrap() {
        assertEquals(Boolean.TRUE, BooleanUtil.orOfWrap(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtil.orOfWrap(Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
    }

    /**
     * orOfWrap null 元素视为 false.
     */
    @Test
    void orOfWrapWithNull() {
        assertEquals(Boolean.FALSE, BooleanUtil.orOfWrap((Boolean) null, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtil.orOfWrap((Boolean) null, Boolean.TRUE));
    }

    /**
     * orOfWrap 空数组抛出异常.
     */
    @Test
    void orOfWrapEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::orOfWrap);
    }

    // ==================================================================== xor

    /**
     * xor 基本类型逻辑异或.
     */
    @Test
    void xorPrimitive() {
        assertFalse(BooleanUtil.xor(true, true));
        assertFalse(BooleanUtil.xor(false, false));
        assertTrue(BooleanUtil.xor(true, false));
        assertTrue(BooleanUtil.xor(false, true));
        // 三个 true → 奇数个 true → true
        assertTrue(BooleanUtil.xor(true, true, true));
        // 两个 true → 偶数个 true → false
        assertFalse(BooleanUtil.xor(true, true, false));
    }

    /**
     * xor 空数组抛出异常.
     */
    @Test
    void xorPrimitiveEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::xor);
    }

    // ==================================================================== exactlyOneTrue

    /**
     * exactlyOneTrue 恰好只有一个 true.
     */
    @Test
    void exactlyOneTrue() {
        assertTrue(BooleanUtil.exactlyOneTrue(true, false, false));
        assertTrue(BooleanUtil.exactlyOneTrue(false, true, false));
        assertTrue(BooleanUtil.exactlyOneTrue(false, false, true));
        // 两个 true
        assertFalse(BooleanUtil.exactlyOneTrue(true, true, false));
        // 三个 true
        assertFalse(BooleanUtil.exactlyOneTrue(true, true, true));
        // 全部 false
        assertFalse(BooleanUtil.exactlyOneTrue(false, false, false));
        // 单元素
        assertTrue(BooleanUtil.exactlyOneTrue(true));
        assertFalse(BooleanUtil.exactlyOneTrue(false));
    }

    /**
     * exactlyOneTrue 空数组抛出异常.
     */
    @Test
    void exactlyOneTrueEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::exactlyOneTrue);
    }

    // ==================================================================== xorOfWrap

    /**
     * xorOfWrap 包装类型逻辑异或.
     */
    @Test
    void xorOfWrap() {
        assertEquals(Boolean.FALSE, BooleanUtil.xorOfWrap(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtil.xorOfWrap(Boolean.FALSE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtil.xorOfWrap(Boolean.TRUE, Boolean.FALSE));
    }

    /**
     * xorOfWrap null 视为 false.
     */
    @Test
    void xorOfWrapWithNull() {
        // null → false, so xor(false, true) = true
        assertEquals(Boolean.TRUE, BooleanUtil.xorOfWrap(null, Boolean.TRUE));
        // null → false, so xor(false, false) = false
        assertEquals(Boolean.FALSE, BooleanUtil.xorOfWrap(null, Boolean.FALSE));
    }

    /**
     * xorOfWrap 空数组抛出异常.
     */
    @Test
    void xorOfWrapEmptyThrows() {
        assertThrows(IllegalArgumentException.class, BooleanUtil::xorOfWrap);
    }

    // ==================================================================== isBoolean

    /**
     * isBoolean 判断类型.
     */
    @Test
    void isBoolean() {
        assertTrue(BooleanUtil.isBoolean(boolean.class));
        assertTrue(BooleanUtil.isBoolean(Boolean.class));
        assertFalse(BooleanUtil.isBoolean(String.class));
        assertFalse(BooleanUtil.isBoolean(int.class));
        assertFalse(BooleanUtil.isBoolean(null));
    }
}
