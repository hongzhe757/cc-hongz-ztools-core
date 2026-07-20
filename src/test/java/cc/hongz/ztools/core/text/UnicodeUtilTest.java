package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link UnicodeUtil} 单元测试.
 */
@DisplayName("UnicodeUtil Unicode工具类")
class UnicodeUtilTest {

    @Test
    @DisplayName("toString - Unicode字符串转普通字符串")
    void toStringUnicode() {
        assertEquals("中文", UnicodeUtil.toString("\\u4e2d\\u6587"));
    }

    @Test
    @DisplayName("toString - 空白字符串返回原值")
    void toStringBlank() {
        assertNull(UnicodeUtil.toString(null));
        assertEquals("", UnicodeUtil.toString(""));
    }

    @Test
    @DisplayName("toString - 混合内容")
    void toStringMixed() {
        assertEquals("a中b", UnicodeUtil.toString("a\\u4e2db"));
    }

    @Test
    @DisplayName("toUnicode(String) - 字符串转Unicode")
    void toUnicodeString() {
        String result = UnicodeUtil.toUnicode("中文");
        assertEquals("\\u4e2d\\u6587", result);
    }

    @Test
    @DisplayName("toUnicode(String, false) - 包含ASCII字符")
    void toUnicodeIncludeAscii() {
        String result = UnicodeUtil.toUnicode("a中", false);
        assertEquals("\\u0061\\u4e2d", result);
    }

    @Test
    @DisplayName("toUnicode(String, true) - 跳过ASCII字符")
    void toUnicodeSkipAscii() {
        String result = UnicodeUtil.toUnicode("a中", true);
        assertEquals("a\\u4e2d", result);
    }

    @Test
    @DisplayName("toUnicode(char) - 字符转Unicode")
    void toUnicodeChar() {
        String result = UnicodeUtil.toUnicode('中');
        assertEquals("\\u4e2d", result);
    }

    @Test
    @DisplayName("toUnicode(int) - 整型转Unicode")
    void toUnicodeInt() {
        String result = UnicodeUtil.toUnicode(0x4e2d);
        assertEquals("\\u4e2d", result);
    }

    @Test
    @DisplayName("toUnicode - 空串返回原值")
    void toUnicodeEmpty() {
        assertNull(UnicodeUtil.toUnicode((String) null));
        assertEquals("", UnicodeUtil.toUnicode(""));
    }
}
