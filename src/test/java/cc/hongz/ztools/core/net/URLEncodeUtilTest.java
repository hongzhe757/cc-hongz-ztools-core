package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link URLEncodeUtil} 单元测试.
 */
class URLEncodeUtilTest {

    // --------------------------------------- encode (PATH)

    @Test
    @DisplayName("encode 普通字符串")
    void encodeDefault() {
        assertEquals("test", URLEncodeUtil.encode("test"));
    }

    @Test
    @DisplayName("encode 中文字符")
    void encodeChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encode("测试"));
    }

    @Test
    @DisplayName("encode 空格编码为 %20")
    void encodeSpace() {
        assertEquals("a%20b", URLEncodeUtil.encode("a b"));
    }

    @Test
    @DisplayName("encode 保留斜杠")
    void encodeSlash() {
        assertEquals("a/b", URLEncodeUtil.encode("a/b"));
    }

    @Test
    @DisplayName("encode 保留加号")
    void encodePlus() {
        assertEquals("+", URLEncodeUtil.encode("+"));
    }

    @Test
    @DisplayName("encode 保留 sub-delims 字符")
    void encodeSubDelims() {
        assertEquals("!$&'()*+,;=", URLEncodeUtil.encode("!$&'()*+,;="));
    }

    @Test
    @DisplayName("encode 保留冒号")
    void encodeColon() {
        assertEquals("a:b", URLEncodeUtil.encode("a:b"));
    }

    @Test
    @DisplayName("encode 保留 @ 符号")
    void encodeAt() {
        assertEquals("a@b", URLEncodeUtil.encode("a@b"));
    }

    @Test
    @DisplayName("encode 保留波浪线")
    void encodeTilde() {
        assertEquals("~", URLEncodeUtil.encode("~"));
    }

    @Test
    @DisplayName("encode null 返回 null")
    void encodeNull() {
        assertNull(URLEncodeUtil.encode(null));
    }

    @Test
    @DisplayName("encode 空字符串")
    void encodeEmpty() {
        assertEquals("", URLEncodeUtil.encode(""));
    }

    @Test
    @DisplayName("encode 指定 UTF-8 编码")
    void encodeWithCharset() {
        assertEquals("%E6%B5%8B%E8%AF%95",
                URLEncodeUtil.encode("测试", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("encode UTF-16 编码")
    void encodeUtf16() {
        final String s = "测试";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        assertEquals(java.net.URLEncoder.encode(s, StandardCharsets.UTF_16), encoded);
    }

    // --------------------------------------- encodeQuery (QUERY)

    @Test
    @DisplayName("encodeQuery 保留斜杠")
    void encodeQuerySlash() {
        assertEquals("a/b", URLEncodeUtil.encodeQuery("a/b"));
    }

    @Test
    @DisplayName("encodeQuery 保留问号")
    void encodeQueryQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeQuery("a?b"));
    }

    @Test
    @DisplayName("encodeQuery 保留加号")
    void encodeQueryPlus() {
        assertEquals("+", URLEncodeUtil.encodeQuery("+"));
    }

    @Test
    @DisplayName("encodeQuery 中文字符")
    void encodeQueryChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeQuery("测试"));
    }

    @Test
    @DisplayName("encodeQuery null 返回 null")
    void encodeQueryNull() {
        assertNull(URLEncodeUtil.encodeQuery(null));
    }

    // --------------------------------------- encodePathSegment (SEGMENT)

    @Test
    @DisplayName("encodePathSegment 编码斜杠")
    void encodePathSegmentSlash() {
        assertEquals("a%2Fb", URLEncodeUtil.encodePathSegment("a/b"));
    }

    @Test
    @DisplayName("encodePathSegment 保留加号")
    void encodePathSegmentPlus() {
        assertEquals("+", URLEncodeUtil.encodePathSegment("+"));
    }

    @Test
    @DisplayName("encodePathSegment 保留冒号")
    void encodePathSegmentColon() {
        assertEquals("a:b", URLEncodeUtil.encodePathSegment("a:b"));
    }

    @Test
    @DisplayName("encodePathSegment 编码问号")
    void encodePathSegmentQuestionMark() {
        assertEquals("a%3Fb", URLEncodeUtil.encodePathSegment("a?b"));
    }

    @Test
    @DisplayName("encodePathSegment 中文字符")
    void encodePathSegmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95",
                URLEncodeUtil.encodePathSegment("测试"));
    }

    @Test
    @DisplayName("encodePathSegment null 返回 null")
    void encodePathSegmentNull() {
        assertNull(URLEncodeUtil.encodePathSegment(null));
    }

    @Test
    @DisplayName("encodePathSegment 空字符串")
    void encodePathSegmentEmpty() {
        assertEquals("", URLEncodeUtil.encodePathSegment(""));
    }

    // --------------------------------------- encodeFragment (FRAGMENT)

    @Test
    @DisplayName("encodeFragment 保留斜杠")
    void encodeFragmentSlash() {
        assertEquals("a/b", URLEncodeUtil.encodeFragment("a/b"));
    }

    @Test
    @DisplayName("encodeFragment 保留问号")
    void encodeFragmentQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeFragment("a?b"));
    }

    @Test
    @DisplayName("encodeFragment 保留加号")
    void encodeFragmentPlus() {
        assertEquals("+", URLEncodeUtil.encodeFragment("+"));
    }

    @Test
    @DisplayName("encodeFragment 中文字符")
    void encodeFragmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeFragment("测试"));
    }

    @Test
    @DisplayName("encodeFragment null 返回 null")
    void encodeFragmentNull() {
        assertNull(URLEncodeUtil.encodeFragment(null));
    }

    // --------------------------------------- encodeAll (UNRESERVED)

    @Test
    @DisplayName("encodeAll 保留字母数字")
    void encodeAllBasic() {
        assertEquals("abc123", URLEncodeUtil.encodeAll("abc123"));
    }

    @Test
    @DisplayName("encodeAll 保留波浪线")
    void encodeAllTilde() {
        assertEquals("~", URLEncodeUtil.encodeAll("~"));
    }

    @Test
    @DisplayName("encodeAll 保留连字符")
    void encodeAllDash() {
        assertEquals("-", URLEncodeUtil.encodeAll("-"));
    }

    @Test
    @DisplayName("encodeAll 保留点号")
    void encodeAllDot() {
        assertEquals(".", URLEncodeUtil.encodeAll("."));
    }

    @Test
    @DisplayName("encodeAll 保留下划线")
    void encodeAllUnderscore() {
        assertEquals("_", URLEncodeUtil.encodeAll("_"));
    }

    @Test
    @DisplayName("encodeAll 编码星号")
    void encodeAllStar() {
        assertEquals("%2A", URLEncodeUtil.encodeAll("*"));
    }

    @Test
    @DisplayName("encodeAll 编码斜杠")
    void encodeAllSlash() {
        assertEquals("%2F", URLEncodeUtil.encodeAll("/"));
    }

    @Test
    @DisplayName("encodeAll 编码加号")
    void encodeAllPlus() {
        assertEquals("%2B", URLEncodeUtil.encodeAll("+"));
    }

    @Test
    @DisplayName("encodeAll 编码空格")
    void encodeAllSpace() {
        assertEquals("%20", URLEncodeUtil.encodeAll(" "));
    }

    @Test
    @DisplayName("encodeAll 中文字符")
    void encodeAllChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeAll("测试"));
    }

    @Test
    @DisplayName("encodeAll null 返回 null")
    void encodeAllNull() {
        assertNull(URLEncodeUtil.encodeAll(null));
    }

    // --------------------------------------- 跨方法对比

    @Test
    @DisplayName("encode 保留斜杠 vs encodePathSegment 编码斜杠")
    void encodeVsPathSegmentSlash() {
        assertEquals("path/to", URLEncodeUtil.encode("path/to"));
        assertEquals("path%2Fto", URLEncodeUtil.encodePathSegment("path/to"));
    }

    @Test
    @DisplayName("encodeQuery 与 encodeFragment 结果一致")
    void encodeQueryVsFragment() {
        final String input = "a/b?c+d=e&f";
        assertEquals(URLEncodeUtil.encodeQuery(input),
                URLEncodeUtil.encodeFragment(input));
    }

    @Test
    @DisplayName("encodeQuery 保留问号 vs encode 编码问号")
    void encodeQueryVsEncodeQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeQuery("a?b"));
        assertEquals("a%3Fb", URLEncodeUtil.encode("a?b"));
    }
}
