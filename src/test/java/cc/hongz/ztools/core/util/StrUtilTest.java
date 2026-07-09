package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link StrUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法与常量；含阶段 1 补充的 {@link StrUtil#NULL}、{@link StrUtil#stringBuilder(int)}.
 *
 * @author hongzhe.qin
 */
@DisplayName("StrUtil 字符串工具")
class StrUtilTest {

    /**
     * 验证 {@link StrUtil#isBlank(CharSequence)} 对 null、空串与空白字符的判断.
     */
    @Test
    void isBlank() {
        assertTrue(StrUtil.isBlank(null));
        assertTrue(StrUtil.isBlank(""));
        assertTrue(StrUtil.isBlank("  \t"));
        assertFalse(StrUtil.isBlank("a"));
    }

    /**
     * 验证 {@link StrUtil#isNotBlank(CharSequence)} 与 isBlank 互为否定.
     */
    @Test
    void isNotBlank() {
        assertFalse(StrUtil.isNotBlank(" "));
        assertTrue(StrUtil.isNotBlank("x"));
    }

    /**
     * 验证 {@link StrUtil#equalsIgnoreCase(CharSequence, CharSequence)} 忽略大小写及 null 处理.
     */
    @Test
    void equalsIgnoreCase() {
        assertTrue(StrUtil.equalsIgnoreCase("Ab", "aB"));
        assertFalse(StrUtil.equalsIgnoreCase("ab", "abc"));
        assertTrue(StrUtil.equalsIgnoreCase(null, null));
        assertFalse(StrUtil.equalsIgnoreCase("a", null));
    }

    /**
     * 验证 {@link StrUtil#cleanBlank(String)} 去除空白字符.
     */
    @Test
    void cleanBlank() {
        assertNull(StrUtil.cleanBlank(null));
        assertEquals("ab", StrUtil.cleanBlank(" a b "));
    }

    /**
     * 验证 {@link StrUtil#removeSuffix(CharSequence, CharSequence)} 移除指定后缀.
     */
    @Test
    void removeSuffix() {
        assertEquals("abc", StrUtil.removeSuffix("abc=", "="));
        assertEquals("abc", StrUtil.removeSuffix("abc", "="));
        assertNull(StrUtil.removeSuffix(null, "="));
    }

    /**
     * 验证 {@link StrUtil#removeSuffix(CharSequence, CharSequence)} 在 suffix 为 null 时返回原串.
     */
    @Test
    void removeSuffixNullSuffix() {
        assertEquals("abc", StrUtil.removeSuffix("abc", null));
    }

    /**
     * 验证 {@link StrUtil#padAfter(CharSequence, int, char)} 右侧填充与边界条件.
     */
    @Test
    void padAfter() {
        assertEquals("12   ", StrUtil.padAfter("12", 5, CharUtil.SPACE));
        assertNull(StrUtil.padAfter(null, 3, '0'));
        assertEquals("12345", StrUtil.padAfter("12345", 3, '0'));
    }

    /**
     * 验证 {@link StrUtil#repeat(char, int)} 重复字符.
     */
    @Test
    void repeat() {
        assertEquals("", StrUtil.repeat('x', 0));
        assertEquals("aaa", StrUtil.repeat('a', 3));
    }

    /**
     * 验证 {@link StrUtil#builder()} 链式 append 拼接表达式字符串.
     */
    @Test
    void builderAppend() {
        assertEquals("1+2=", StrUtil.builder().append(1).append('+').append(2).append('=').toString());
    }

    /**
     * 验证 {@link StrUtil#EMPTY} 为空字符串.
     */
    @Test
    @DisplayName("EMPTY 常量")
    void emptyConstant() {
        assertEquals("", StrUtil.EMPTY);
    }

    /**
     * 验证 {@link StrUtil#NULL} 为字面量 "null".
     */
    @Test
    @DisplayName("NULL 常量")
    void nullConstant() {
        assertEquals("null", StrUtil.NULL);
    }

    /**
     * 验证 {@link StrUtil#stringBuilder(int)} 创建指定容量的 {@link StringBuilder}.
     */
    @Test
    @DisplayName("stringBuilder 指定容量")
    void stringBuilderCapacity() {
        final StringBuilder sb = StrUtil.stringBuilder(32);
        assertEquals(32, sb.capacity());
    }
}
