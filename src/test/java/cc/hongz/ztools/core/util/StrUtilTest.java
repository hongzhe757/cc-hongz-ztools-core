package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link StrUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法与常量。
 *
 * @author hongzhe.qin
 */
@DisplayName("StrUtil 字符串工具")
class StrUtilTest {

    /**
     * 验证 {@link StrUtil#isBlank(CharSequence)} 对 null、空串与空白字符的判断.
     */
    @Test
    @DisplayName("isBlank 空白判断")
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
    @DisplayName("isNotBlank 非空白判断")
    void isNotBlank() {
        assertFalse(StrUtil.isNotBlank(" "));
        assertTrue(StrUtil.isNotBlank("x"));
    }

    /**
     * 验证 {@link StrUtil#equalsIgnoreCase(CharSequence, CharSequence)} 忽略大小写及 null 处理.
     */
    @Test
    @DisplayName("equalsIgnoreCase 忽略大小写比较")
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
    @DisplayName("cleanBlank 去除空白字符")
    void cleanBlank() {
        assertNull(StrUtil.cleanBlank(null));
        assertEquals("ab", StrUtil.cleanBlank(" a b "));
    }

    /**
     * 验证 {@link StrUtil#removeSuffix(CharSequence, CharSequence)} 移除指定后缀.
     */
    @Test
    @DisplayName("removeSuffix 移除后缀")
    void removeSuffix() {
        assertEquals("abc", StrUtil.removeSuffix("abc=", "="));
        assertEquals("abc", StrUtil.removeSuffix("abc", "="));
        assertNull(StrUtil.removeSuffix(null, "="));
    }

    /**
     * 验证 {@link StrUtil#removeSuffix(CharSequence, CharSequence)} 在 suffix 为 null 时返回原串.
     */
    @Test
    @DisplayName("removeSuffix suffix为null返回原串")
    void removeSuffixNullSuffix() {
        assertEquals("abc", StrUtil.removeSuffix("abc", null));
    }

    /**
     * 验证 {@link StrUtil#padAfter(CharSequence, int, char)} 右侧填充与边界条件.
     */
    @Test
    @DisplayName("padAfter 右侧填充")
    void padAfter() {
        assertEquals("12   ", StrUtil.padAfter("12", 5, CharUtil.SPACE));
        assertNull(StrUtil.padAfter(null, 3, '0'));
        assertEquals("345", StrUtil.padAfter("12345", 3, '0'));
    }

    /**
     * 验证 {@link StrUtil#repeat(char, int)} 重复字符.
     */
    @Test
    @DisplayName("repeat 重复字符")
    void repeat() {
        assertEquals("", StrUtil.repeat('x', 0));
        assertEquals("aaa", StrUtil.repeat('a', 3));
    }

    /**
     * 验证 {@link StrUtil#builder()} 链式 append 拼接表达式字符串.
     */
    @Test
    @DisplayName("builder 链式append拼接")
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
     * 验证 {@link StrUtil#builder(int)} 创建指定容量的 {@link StringBuilder}.
     */
    @Test
    @DisplayName("stringBuilder 指定容量")
    void stringBuilderCapacity() {
        final StringBuilder sb = StrUtil.builder(32);
        assertEquals(32, sb.capacity());
    }

    // ==================== isEmpty / isNotEmpty ====================

    @Test
    @DisplayName("isEmpty 空串判断")
    void isEmpty() {
        assertTrue(StrUtil.isEmpty(null));
        assertTrue(StrUtil.isEmpty(""));
        assertFalse(StrUtil.isEmpty(" "));
        assertFalse(StrUtil.isEmpty("abc"));
    }

    @Test
    @DisplayName("isNotEmpty 非空判断")
    void isNotEmpty() {
        assertFalse(StrUtil.isNotEmpty(null));
        assertFalse(StrUtil.isNotEmpty(""));
        assertTrue(StrUtil.isNotEmpty(" "));
        assertTrue(StrUtil.isNotEmpty("abc"));
    }

    // ==================== null/empty/blank 转换 ====================

    @Test
    @DisplayName("nullToEmpty null转空串")
    void nullToEmpty() {
        assertEquals("", StrUtil.nullToEmpty(null));
        assertEquals("abc", StrUtil.nullToEmpty("abc"));
    }

    @Test
    @DisplayName("emptyIfNull null转空串")
    void emptyIfNull() {
        assertEquals("", StrUtil.emptyIfNull(null));
        assertEquals("abc", StrUtil.emptyIfNull("abc"));
    }

    @Test
    @DisplayName("nullToDefault null转默认值")
    void nullToDefault() {
        assertEquals("default", StrUtil.nullToDefault(null, "default"));
        assertEquals("", StrUtil.nullToDefault("", "default"));
        assertEquals("abc", StrUtil.nullToDefault("abc", "default"));
    }

    @Test
    @DisplayName("emptyToDefault 空串转默认值")
    void emptyToDefault() {
        assertEquals("default", StrUtil.emptyToDefault(null, "default"));
        assertEquals("default", StrUtil.emptyToDefault("", "default"));
        assertEquals("abc", StrUtil.emptyToDefault("abc", "default"));
    }

    @Test
    @DisplayName("blankToDefault 空白转默认值")
    void blankToDefault() {
        assertEquals("d", StrUtil.blankToDefault(null, "d"));
        assertEquals("d", StrUtil.blankToDefault("", "d"));
        assertEquals("d", StrUtil.blankToDefault("  ", "d"));
        assertEquals("abc", StrUtil.blankToDefault("abc", "d"));
    }

    @Test
    @DisplayName("emptyToNull 空串转null")
    void emptyToNull() {
        assertNull(StrUtil.emptyToNull(null));
        assertNull(StrUtil.emptyToNull(""));
        assertEquals("abc", StrUtil.emptyToNull("abc"));
    }

    @Test
    @DisplayName("hasEmpty 是否包含空串")
    void hasEmpty() {
        assertTrue(StrUtil.hasEmpty());
        assertTrue(StrUtil.hasEmpty("", null));
        assertTrue(StrUtil.hasEmpty("123", ""));
        assertFalse(StrUtil.hasEmpty("123", "abc"));
        assertFalse(StrUtil.hasEmpty(" ", "\t"));
    }

    @Test
    @DisplayName("isAllEmpty 是否全部为空串")
    void isAllEmpty() {
        assertTrue(StrUtil.isAllEmpty());
        assertTrue(StrUtil.isAllEmpty("", null));
        assertFalse(StrUtil.isAllEmpty("123", ""));
        assertFalse(StrUtil.isAllEmpty(" "));
    }

    // ==================== trim 家族 ====================

    @Test
    @DisplayName("trim 去除首尾空白")
    void trim() {
        assertNull(StrUtil.trim((CharSequence) null));
        assertEquals("", StrUtil.trim(""));
        assertEquals("abc", StrUtil.trim("  abc  "));
    }

    @Test
    @DisplayName("trimToEmpty 去除首尾空白null转空串")
    void trimToEmpty() {
        assertEquals("", StrUtil.trimToEmpty((CharSequence) null));
        assertEquals("", StrUtil.trimToEmpty(""));
        assertEquals("abc", StrUtil.trimToEmpty("  abc  "));
    }

    @Test
    @DisplayName("trimToNull 去除首尾空白空串转null")
    void trimToNull() {
        assertNull(StrUtil.trimToNull((CharSequence) null));
        assertNull(StrUtil.trimToNull(""));
        assertEquals("abc", StrUtil.trimToNull("  abc  "));
    }

    @Test
    @DisplayName("trimStart 去除首部空白")
    void trimStart() {
        assertNull(StrUtil.trimStart((CharSequence) null));
        assertEquals("abc  ", StrUtil.trimStart("  abc  "));
    }

    @Test
    @DisplayName("trimEnd 去除尾部空白")
    void trimEnd() {
        assertNull(StrUtil.trimEnd((CharSequence) null));
        assertEquals("  abc", StrUtil.trimEnd("  abc  "));
    }

    // ==================== startWith / endWith ====================

    @Test
    @DisplayName("startWith 前缀判断")
    void startWith() {
        assertTrue(StrUtil.startWith("abc", "a"));
        assertTrue(StrUtil.startWith("abc", "ab"));
        assertFalse(StrUtil.startWith("abc", "bc"));
        assertFalse(StrUtil.startWith(null, "a"));
    }

    @Test
    @DisplayName("startWithIgnoreCase 忽略大小写前缀判断")
    void startWithIgnoreCase() {
        assertTrue(StrUtil.startWithIgnoreCase("Abc", "a"));
        assertTrue(StrUtil.startWithIgnoreCase("ABC", "ab"));
        assertFalse(StrUtil.startWithIgnoreCase("abc", "bc"));
    }

    @Test
    @DisplayName("endWith 后缀判断")
    void endWith() {
        assertTrue(StrUtil.endWith("abc", "c"));
        assertTrue(StrUtil.endWith("abc", "bc"));
        assertFalse(StrUtil.endWith("abc", "ab"));
        assertFalse(StrUtil.endWith(null, "c"));
    }

    @Test
    @DisplayName("endWithIgnoreCase 忽略大小写后缀判断")
    void endWithIgnoreCase() {
        assertTrue(StrUtil.endWithIgnoreCase("abC", "c"));
        assertTrue(StrUtil.endWithIgnoreCase("ABC", "bc"));
        assertFalse(StrUtil.endWithIgnoreCase("abc", "cd"));
    }

    @Test
    @DisplayName("startWithAny 是否匹配任一前缀")
    void startWithAny() {
        assertTrue(StrUtil.startWithAny("abc", "x", "a"));
        assertFalse(StrUtil.startWithAny("abc", "x", "y"));
        assertFalse(StrUtil.startWithAny(null, "a"));
    }

    // ==================== contains ====================

    @Test
    @DisplayName("contains 包含字符判断")
    void containsChar() {
        assertTrue(StrUtil.contains("abc", 'b'));
        assertFalse(StrUtil.contains("abc", 'd'));
        assertFalse(StrUtil.contains(null, 'a'));
    }

    @Test
    @DisplayName("contains 包含字符串判断")
    void containsStr() {
        assertTrue(StrUtil.contains("abc", "bc"));
        assertFalse(StrUtil.contains("abc", "bd"));
        assertFalse(StrUtil.contains(null, "a"));
    }

    @Test
    @DisplayName("containsIgnoreCase 忽略大小写包含判断")
    void containsIgnoreCase() {
        assertTrue(StrUtil.containsIgnoreCase("Abc", "bC"));
        assertFalse(StrUtil.containsIgnoreCase("abc", "bd"));
    }

    // ==================== indexOf ====================

    @Test
    @DisplayName("indexOf 查找字符位置")
    void indexOfChar() {
        assertEquals(1, StrUtil.indexOf("abc", 'b'));
        assertEquals(-1, StrUtil.indexOf("abc", 'd'));
    }

    @Test
    @DisplayName("indexOfIgnoreCase 忽略大小写查找位置")
    void indexOfIgnoreCase() {
        assertEquals(0, StrUtil.indexOfIgnoreCase("aAbc", "A"));
        assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
    }

    // ==================== sub 截取 ====================

    @Test
    @DisplayName("sub 截取子串")
    void sub() {
        assertNull(StrUtil.sub(null, 0, 3));
        assertEquals("b", StrUtil.sub("abc", 1, 2));
        assertEquals("abc", StrUtil.sub("abc", 0, 3));
    }

    @Test
    @DisplayName("subPre 截取前缀")
    void subPre() {
        assertNull(StrUtil.subPre(null, 3));
        assertEquals("ab", StrUtil.subPre("abc", 2));
        assertEquals("", StrUtil.subPre("abc", 0));
    }

    @Test
    @DisplayName("subSuf 截取后缀")
    void subSuf() {
        assertNull(StrUtil.subSuf(null, 1));
        assertEquals("bc", StrUtil.subSuf("abc", 1));
        assertEquals("", StrUtil.subSuf("abc", 3));
    }

    // ==================== equals ====================

    @Test
    @DisplayName("equals 字符串相等判断")
    void equalsStr() {
        assertTrue(StrUtil.equals("abc", "abc"));
        assertFalse(StrUtil.equals("abc", "ABC"));
        assertTrue(StrUtil.equals(null, null));
        assertFalse(StrUtil.equals("abc", null));
    }

    @Test
    @DisplayName("equals 忽略大小写相等判断")
    void equalsIgnoreCase2() {
        assertTrue(StrUtil.equals("abc", "ABC", true));
        assertFalse(StrUtil.equals("abc", "ABCD", true));
        assertTrue(StrUtil.equals(null, null, true));
    }

    // ==================== remove 家族 ====================

    @Test
    @DisplayName("removePrefix 移除前缀")
    void removePrefix() {
        assertEquals("bc", StrUtil.removePrefix("abc", "a"));
        assertEquals("abc", StrUtil.removePrefix("abc", "x"));
        assertNull(StrUtil.removePrefix(null, "a"));
    }

    @Test
    @DisplayName("removePrefixIgnoreCase 忽略大小写移除前缀")
    void removePrefixIgnoreCase() {
        assertEquals("bc", StrUtil.removePrefixIgnoreCase("Abc", "a"));
        assertEquals("abc", StrUtil.removePrefixIgnoreCase("abc", "x"));
    }

    @Test
    @DisplayName("removeAll 移除所有指定字符串")
    void removeAll() {
        assertEquals("aabbccdd", StrUtil.removeAll("aa-bb-cc-dd", "-"));
        assertEquals("abc", StrUtil.removeAll("abc", "x"));
    }

    @Test
    @DisplayName("removeAny 移除任意指定字符串")
    void removeAny() {
        assertEquals("--cc-dd", StrUtil.removeAny("aa-bb-cc-dd", "a", "b"));
    }

    // ==================== replace ====================

    @Test
    @DisplayName("replace 替换字符串")
    void replace() {
        assertEquals("xbc", StrUtil.replace("abc", "a", "x"));
        assertEquals("abc", StrUtil.replace("abc", "x", "y"));
        assertNull(StrUtil.replace(null, "a", "b"));
    }

    // ==================== strip 家族 ====================

    @Test
    @DisplayName("strip 去除两边指定字符串")
    void strip() {
        assertEquals("aa_STRIPPED_bbb", StrUtil.strip("aaa_STRIPPED_bbb", "a"));
        assertNull(StrUtil.strip(null, "a"));
    }

    @Test
    @DisplayName("stripAll 去除两边所有指定字符串")
    void stripAll() {
        assertEquals("_STRIPPED_", StrUtil.stripAll("aaa_STRIPPED_bbb", "a", "b"));
    }

    // ==================== split 家族 ====================

    @Test
    @DisplayName("split 切分字符串")
    void split() {
        final List<String> list = StrUtil.split("a,b,c", ',');
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("splitTrim 切分并去除空白")
    void splitTrim() {
        final List<String> list = StrUtil.splitTrim("a, b , c", ',');
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("splitToArray 切分为数组")
    void splitToArray() {
        assertArrayEquals(new String[]{"a", "b", "c"}, StrUtil.splitToArray("a,b,c", ','));
    }

    // ==================== addPrefixIfNot / addSuffixIfNot ====================

    @Test
    @DisplayName("addPrefixIfNot 补充前缀")
    void addPrefixIfNot() {
        assertEquals("http://abc", StrUtil.addPrefixIfNot("abc", "http://"));
        assertEquals("http://abc", StrUtil.addPrefixIfNot("http://abc", "http://"));
    }

    @Test
    @DisplayName("addSuffixIfNot 补充后缀")
    void addSuffixIfNot() {
        assertEquals("abc/", StrUtil.addSuffixIfNot("abc", "/"));
        assertEquals("abc/", StrUtil.addSuffixIfNot("abc/", "/"));
    }

    // ==================== wrap ====================

    @Test
    @DisplayName("wrap 包裹字符串")
    void wrap() {
        assertEquals("[abc]", StrUtil.wrap("abc", "[", "]"));
        assertEquals("(abc(", StrUtil.wrap("abc", "("));
        assertEquals("[[", StrUtil.wrap(null, "["));
    }

    // ==================== count ====================

    @Test
    @DisplayName("count 统计出现次数(字符串)")
    void countStr() {
        assertEquals(6, StrUtil.count("aabaabaa", "a"));
        assertEquals(0, StrUtil.count("abc", "x"));
    }

    @Test
    @DisplayName("count 统计出现次数(字符)")
    void countChar() {
        assertEquals(6, StrUtil.count("aabaabaa", 'a'));
        assertEquals(2, StrUtil.count("aabaabaa", 'b'));
    }

    // ==================== format ====================

    @Test
    @DisplayName("format 格式化字符串")
    void format() {
        assertEquals("Hello, world!", StrUtil.format("Hello, {}!", "world"));
        assertEquals("a-b-c", StrUtil.format("{}-{}-{}", "a", "b", "c"));
    }

    // ==================== upperFirst / lowerFirst ====================

    @Test
    @DisplayName("upperFirst 首字母大写")
    void upperFirst() {
        assertEquals("Name", StrUtil.upperFirst("name"));
        assertEquals("Name", StrUtil.upperFirst("Name"));
        assertNull(StrUtil.upperFirst(null));
    }

    @Test
    @DisplayName("lowerFirst 首字母小写")
    void lowerFirst() {
        assertEquals("name", StrUtil.lowerFirst("Name"));
        assertEquals("name", StrUtil.lowerFirst("name"));
        assertNull(StrUtil.lowerFirst(null));
    }

    // ==================== toUpperCase / toLowerCase ====================

    @Test
    @DisplayName("toUpperCase 转大写")
    void toUpperCase() {
        assertEquals("ABC", StrUtil.toUpperCase("abc"));
        assertNull(StrUtil.toUpperCase(null));
        assertEquals("", StrUtil.toUpperCase(""));
    }

    @Test
    @DisplayName("toLowerCase 转小写")
    void toLowerCase() {
        assertEquals("abc", StrUtil.toLowerCase("ABC"));
        assertNull(StrUtil.toLowerCase(null));
        assertEquals("", StrUtil.toLowerCase(""));
    }

    // ==================== maxLength / minLength ====================

    @Test
    @DisplayName("maxLength 限制最大长度")
    void maxLength() {
        assertEquals("ab...", StrUtil.maxLength("abcdef", 2));
        assertEquals("abc", StrUtil.maxLength("abc", 5));
        assertNull(StrUtil.maxLength(null, 3));
    }

    // ==================== length ====================

    @Test
    @DisplayName("length 获取字符串长度")
    void length() {
        assertEquals(0, StrUtil.length(null));
        assertEquals(0, StrUtil.length(""));
        assertEquals(3, StrUtil.length("abc"));
    }

    // ==================== isNumeric ====================

    @Test
    @DisplayName("isNumeric 是否全为数字")
    void isNumeric() {
        assertTrue(StrUtil.isNumeric("12345"));
        assertFalse(StrUtil.isNumeric("12.3"));
        assertFalse(StrUtil.isNumeric("abc"));
        assertFalse(StrUtil.isNumeric(null));
        assertFalse(StrUtil.isNumeric(""));
    }

    // ==================== commonPrefix / commonSuffix ====================

    @Test
    @DisplayName("commonPrefix 公共前缀")
    void commonPrefix() {
        assertEquals("ab", StrUtil.commonPrefix("abcdef", "abxyz").toString());
        assertEquals("", StrUtil.commonPrefix("abc", "xyz").toString());
        assertEquals("", StrUtil.commonPrefix(null, "abc").toString());
    }

    @Test
    @DisplayName("commonSuffix 公共后缀")
    void commonSuffix() {
        assertEquals("ef", StrUtil.commonSuffix("abcdef", "xyzef").toString());
        assertEquals("", StrUtil.commonSuffix("abc", "xyz").toString());
    }

    // ==================== hasLetter / hasDigit ====================

    @Test
    @DisplayName("hasLetter 是否包含字母")
    void hasLetter() {
        assertTrue(StrUtil.hasLetter("abc"));
        assertTrue(StrUtil.hasLetter("123a"));
        assertFalse(StrUtil.hasLetter("12345"));
        assertFalse(StrUtil.hasLetter(null));
    }

    // ==================== stringBuilder() ====================

    @Test
    @DisplayName("stringBuilder 无参创建")
    void stringBuilderNoArg() {
        final StringBuilder sb = StrUtil.builder();
        assertNotNull(sb);
        assertEquals("", sb.toString());
    }

    // ==================== isBlankIfStr / isEmptyIfStr ====================

    @Test
    @DisplayName("isBlankIfStr 对象空白判断")
    void isBlankIfStr() {
        assertTrue(StrUtil.isBlankIfStr(null));
        assertTrue(StrUtil.isBlankIfStr(""));
        assertTrue(StrUtil.isBlankIfStr("  "));
        assertFalse(StrUtil.isBlankIfStr("abc"));
    }

    @Test
    @DisplayName("isEmptyIfStr 对象空串判断")
    void isEmptyIfStr() {
        assertTrue(StrUtil.isEmptyIfStr(null));
        assertTrue(StrUtil.isEmptyIfStr(""));
        assertFalse(StrUtil.isEmptyIfStr(" "));
        assertFalse(StrUtil.isEmptyIfStr("abc"));
    }
}
