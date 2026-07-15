package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CharUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("CharUtil 字符工具")
class CharUtilTest {

    // ==================================================================== 常量验证

    @Test
    @DisplayName("字符常量值验证")
    void charConstants() {
        assertEquals(' ', CharUtil.SPACE);
        assertEquals('\t', CharUtil.TAB);
        assertEquals('.', CharUtil.DOT);
        assertEquals('/', CharUtil.SLASH);
        assertEquals('\\', CharUtil.BACKSLASH);
        assertEquals('\r', CharUtil.CR);
        assertEquals('\n', CharUtil.LF);
        assertEquals('-', CharUtil.DASHED);
        assertEquals('_', CharUtil.UNDERLINE);
        assertEquals(',', CharUtil.COMMA);
        assertEquals('{', CharUtil.DELIM_START);
        assertEquals('}', CharUtil.DELIM_END);
        assertEquals('[', CharUtil.BRACKET_START);
        assertEquals(']', CharUtil.BRACKET_END);
        assertEquals('"', CharUtil.DOUBLE_QUOTES);
        assertEquals('\'', CharUtil.SINGLE_QUOTE);
        assertEquals('&', CharUtil.AMP);
        assertEquals(':', CharUtil.COLON);
        assertEquals('@', CharUtil.AT);
    }

    // ==================================================================== isAscii

    @Test
    @DisplayName("isAscii 正常范围")
    void isAscii() {
        assertTrue(CharUtil.isAscii('a'));
        assertTrue(CharUtil.isAscii('A'));
        assertTrue(CharUtil.isAscii('3'));
        assertTrue(CharUtil.isAscii('-'));
        assertTrue(CharUtil.isAscii('\n'));
        assertFalse(CharUtil.isAscii('\u00A9')); // copyright symbol
    }

    // ==================================================================== isAsciiPrintable

    @Test
    @DisplayName("isAsciiPrintable 可见字符范围")
    void isAsciiPrintable() {
        assertTrue(CharUtil.isAsciiPrintable('a'));
        assertTrue(CharUtil.isAsciiPrintable('A'));
        assertTrue(CharUtil.isAsciiPrintable('3'));
        assertTrue(CharUtil.isAsciiPrintable('-'));
        assertFalse(CharUtil.isAsciiPrintable('\n'));
        assertFalse(CharUtil.isAsciiPrintable('\u00A9'));
    }

    // ==================================================================== isAsciiControl

    @Test
    @DisplayName("isAsciiControl 控制字符")
    void isAsciiControl() {
        assertFalse(CharUtil.isAsciiControl('a'));
        assertFalse(CharUtil.isAsciiControl('A'));
        assertFalse(CharUtil.isAsciiControl('3'));
        assertFalse(CharUtil.isAsciiControl('-'));
        assertTrue(CharUtil.isAsciiControl('\n'));
        assertTrue(CharUtil.isAsciiControl((char) 127));
        assertFalse(CharUtil.isAsciiControl('\u00A9'));
    }

    // ==================================================================== isLetter / isLetterUpper / isLetterLower

    @Test
    @DisplayName("isLetter 字母判断")
    void isLetter() {
        assertTrue(CharUtil.isLetter('a'));
        assertTrue(CharUtil.isLetter('A'));
        assertTrue(CharUtil.isLetter('z'));
        assertTrue(CharUtil.isLetter('Z'));
        assertFalse(CharUtil.isLetter('3'));
        assertFalse(CharUtil.isLetter('-'));
        assertFalse(CharUtil.isLetter('\n'));
    }

    @Test
    @DisplayName("isLetterUpper 大写字母判断")
    void isLetterUpper() {
        assertFalse(CharUtil.isLetterUpper('a'));
        assertTrue(CharUtil.isLetterUpper('A'));
        assertFalse(CharUtil.isLetterUpper('3'));
        assertFalse(CharUtil.isLetterUpper('-'));
    }

    @Test
    @DisplayName("isLetterLower 小写字母判断")
    void isLetterLower() {
        assertTrue(CharUtil.isLetterLower('a'));
        assertFalse(CharUtil.isLetterLower('A'));
        assertFalse(CharUtil.isLetterLower('3'));
        assertFalse(CharUtil.isLetterLower('-'));
    }

    // ==================================================================== isNumber

    @Test
    @DisplayName("isNumber 数字字符判断")
    void isNumber() {
        assertFalse(CharUtil.isNumber('a'));
        assertFalse(CharUtil.isNumber('A'));
        assertTrue(CharUtil.isNumber('3'));
        assertTrue(CharUtil.isNumber('0'));
        assertTrue(CharUtil.isNumber('9'));
        assertFalse(CharUtil.isNumber('-'));
    }

    // ==================================================================== isHexChar

    @Test
    @DisplayName("isHexChar 十六进制字符判断")
    void isHexChar() {
        assertTrue(CharUtil.isHexChar('0'));
        assertTrue(CharUtil.isHexChar('9'));
        assertTrue(CharUtil.isHexChar('a'));
        assertTrue(CharUtil.isHexChar('f'));
        assertTrue(CharUtil.isHexChar('A'));
        assertTrue(CharUtil.isHexChar('F'));
        assertFalse(CharUtil.isHexChar('g'));
        assertFalse(CharUtil.isHexChar('G'));
        assertFalse(CharUtil.isHexChar('-'));
    }

    // ==================================================================== isLetterOrNumber

    @Test
    @DisplayName("isLetterOrNumber 字母或数字判断")
    void isLetterOrNumber() {
        assertTrue(CharUtil.isLetterOrNumber('a'));
        assertTrue(CharUtil.isLetterOrNumber('A'));
        assertTrue(CharUtil.isLetterOrNumber('3'));
        assertFalse(CharUtil.isLetterOrNumber('-'));
        assertFalse(CharUtil.isLetterOrNumber('\n'));
    }

    // ==================================================================== toString

    @Test
    @DisplayName("toString 字符转字符串（ASCII 缓存）")
    void toStringChar() {
        assertEquals("a", CharUtil.toString('a'));
        assertEquals("A", CharUtil.toString('A'));
        assertEquals("0", CharUtil.toString('0'));
    }

    @Test
    @DisplayName("toString 非 ASCII 字符")
    void toStringNonAscii() {
        assertEquals("中", CharUtil.toString('中'));
    }

    // ==================================================================== isCharClass / isChar

    @Test
    @DisplayName("isCharClass 判断字符类")
    void isCharClass() {
        assertTrue(CharUtil.isCharClass(Character.class));
        assertTrue(CharUtil.isCharClass(char.class));
        assertFalse(CharUtil.isCharClass(String.class));
        assertFalse(CharUtil.isCharClass(int.class));
    }

    @Test
    @DisplayName("isChar 判断字符对象")
    void isChar() {
        assertTrue(CharUtil.isChar('a'));
        assertTrue(CharUtil.isChar(Character.valueOf('Z')));
        assertFalse(CharUtil.isChar("string"));
        assertFalse(CharUtil.isChar(123));
    }

    // ==================================================================== isBlankChar

    @Test
    @DisplayName("isBlankChar 空白字符判断")
    void isBlankChar() {
        assertTrue(CharUtil.isBlankChar(' '));
        assertTrue(CharUtil.isBlankChar('\t'));
        assertTrue(CharUtil.isBlankChar('\n'));
        assertTrue(CharUtil.isBlankChar('\ufeff')); // BOM
        assertTrue(CharUtil.isBlankChar('\u200c')); // ZWNJ
        assertTrue(CharUtil.isBlankChar('\u3164')); // Hangul Filler
        assertTrue(CharUtil.isBlankChar('\u2800')); // Braille Pattern Blank
        assertTrue(CharUtil.isBlankChar('\u180e')); // MONGOLIAN VOWEL SEPARATOR
        assertFalse(CharUtil.isBlankChar('a'));
        assertFalse(CharUtil.isBlankChar('0'));
    }

    @Test
    @DisplayName("isBlankChar(int) 重载")
    void isBlankCharInt() {
        assertTrue(CharUtil.isBlankChar((int) ' '));
        assertFalse(CharUtil.isBlankChar((int) 'a'));
    }

    // ==================================================================== isEmoji

    @Test
    @DisplayName("isEmoji emoji 判断")
    void isEmoji() {
        assertFalse(CharUtil.isEmoji('a'));
        assertFalse(CharUtil.isEmoji('A'));
        assertFalse(CharUtil.isEmoji('0'));
        // emoji 范围测试（超出基本多文种平面的字符）
    }

    // ==================================================================== isFileSeparator

    @Test
    @DisplayName("isFileSeparator 文件分隔符判断")
    void isFileSeparator() {
        assertTrue(CharUtil.isFileSeparator('/'));
        assertTrue(CharUtil.isFileSeparator('\\'));
        assertFalse(CharUtil.isFileSeparator('-'));
        assertFalse(CharUtil.isFileSeparator('a'));
    }

    // ==================================================================== equals

    @Test
    @DisplayName("equals 字符比较（区分大小写）")
    void equalsCaseSensitive() {
        assertTrue(CharUtil.equals('a', 'a', false));
        assertFalse(CharUtil.equals('a', 'A', false));
    }

    @Test
    @DisplayName("equals 字符比较（忽略大小写）")
    void equalsCaseInsensitive() {
        assertTrue(CharUtil.equals('a', 'A', true));
        assertTrue(CharUtil.equals('Z', 'z', true));
        assertFalse(CharUtil.equals('a', 'b', true));
    }

    // ==================================================================== getType / digit16

    @Test
    @DisplayName("getType 获取字符类型")
    void getType() {
        assertEquals(Character.LOWERCASE_LETTER, CharUtil.getType('a'));
        assertEquals(Character.UPPERCASE_LETTER, CharUtil.getType('A'));
        assertEquals(Character.DECIMAL_DIGIT_NUMBER, CharUtil.getType('0'));
    }

    @Test
    @DisplayName("digit16 十六进制数值")
    void digit16() {
        assertEquals(0, CharUtil.digit16('0'));
        assertEquals(9, CharUtil.digit16('9'));
        assertEquals(10, CharUtil.digit16('a'));
        assertEquals(15, CharUtil.digit16('F'));
        assertEquals(-1, CharUtil.digit16('g'));
    }

    // ==================================================================== toCloseChar / toCloseByNumber

    @Test
    @DisplayName("toCloseChar 数字转带圈字符")
    void toCloseCharNumber() {
        assertEquals('\u2460', CharUtil.toCloseChar('1')); // ①
        assertEquals('\u2468', CharUtil.toCloseChar('9')); // ⑨
        assertEquals('0', CharUtil.toCloseChar('0')); // 不在范围内，返回原字符
    }

    @Test
    @DisplayName("toCloseChar 大写字母转带圈字符")
    void toCloseCharUpper() {
        assertEquals('\u24B6', CharUtil.toCloseChar('A')); // Ⓐ
        assertEquals('\u24CF', CharUtil.toCloseChar('Z')); // Ⓩ
    }

    @Test
    @DisplayName("toCloseChar 小写字母转带圈字符")
    void toCloseCharLower() {
        assertEquals('\u24D0', CharUtil.toCloseChar('a')); // ⓐ
        assertEquals('\u24E9', CharUtil.toCloseChar('z')); // ⓩ
    }

    @Test
    @DisplayName("toCloseByNumber 数字转带圈字符")
    void toCloseByNumber() {
        assertEquals('\u2460', CharUtil.toCloseByNumber(1));  // ①
        assertEquals('\u246B', CharUtil.toCloseByNumber(12)); // ⑫
        assertEquals('\u2473', CharUtil.toCloseByNumber(20)); // ⑳
    }

    @Test
    @DisplayName("toCloseByNumber 超出范围抛异常")
    void toCloseByNumberOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> CharUtil.toCloseByNumber(0));
        assertThrows(IllegalArgumentException.class, () -> CharUtil.toCloseByNumber(21));
    }
}
