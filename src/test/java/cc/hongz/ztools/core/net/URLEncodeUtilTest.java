package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link URLEncodeUtil} 单元测试.
 */
class URLEncodeUtilTest {

    // --------------------------------------- encode (PATH)

    @Test
    void encodeDefault() {
        assertEquals("test", URLEncodeUtil.encode("test"));
    }

    @Test
    void encodeChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encode("测试"));
    }

    @Test
    void encodeSpace() {
        assertEquals("a%20b", URLEncodeUtil.encode("a b"));
    }

    @Test
    void encodeSlash() {
        assertEquals("a/b", URLEncodeUtil.encode("a/b"));
    }

    @Test
    void encodePlus() {
        assertEquals("+", URLEncodeUtil.encode("+"));
    }

    @Test
    void encodeSubDelims() {
        assertEquals("!$&'()*+,;=", URLEncodeUtil.encode("!$&'()*+,;="));
    }

    @Test
    void encodeColon() {
        assertEquals("a:b", URLEncodeUtil.encode("a:b"));
    }

    @Test
    void encodeAt() {
        assertEquals("a@b", URLEncodeUtil.encode("a@b"));
    }

    @Test
    void encodeTilde() {
        assertEquals("~", URLEncodeUtil.encode("~"));
    }

    @Test
    void encodeNull() {
        assertNull(URLEncodeUtil.encode(null));
    }

    @Test
    void encodeEmpty() {
        assertEquals("", URLEncodeUtil.encode(""));
    }

    @Test
    void encodeWithCharset() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encode("测试", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void encodeUtf16() {
        final String s = "测试";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        assertEquals(java.net.URLEncoder.encode(s, StandardCharsets.UTF_16), encoded);
    }

    // --------------------------------------- encodeQuery (QUERY)

    @Test
    void encodeQuerySlash() {
        assertEquals("a/b", URLEncodeUtil.encodeQuery("a/b"));
    }

    @Test
    void encodeQueryQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeQuery("a?b"));
    }

    @Test
    void encodeQueryPlus() {
        assertEquals("+", URLEncodeUtil.encodeQuery("+"));
    }

    @Test
    void encodeQueryChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeQuery("测试"));
    }

    @Test
    void encodeQueryNull() {
        assertNull(URLEncodeUtil.encodeQuery(null));
    }

    // --------------------------------------- encodePathSegment (SEGMENT)

    @Test
    void encodePathSegmentSlash() {
        assertEquals("a%2Fb", URLEncodeUtil.encodePathSegment("a/b"));
    }

    @Test
    void encodePathSegmentPlus() {
        assertEquals("+", URLEncodeUtil.encodePathSegment("+"));
    }

    @Test
    void encodePathSegmentColon() {
        assertEquals("a:b", URLEncodeUtil.encodePathSegment("a:b"));
    }

    @Test
    void encodePathSegmentQuestionMark() {
        assertEquals("a%3Fb", URLEncodeUtil.encodePathSegment("a?b"));
    }

    @Test
    void encodePathSegmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodePathSegment("测试"));
    }

    @Test
    void encodePathSegmentNull() {
        assertNull(URLEncodeUtil.encodePathSegment(null));
    }

    @Test
    void encodePathSegmentEmpty() {
        assertEquals("", URLEncodeUtil.encodePathSegment(""));
    }

    // --------------------------------------- encodeFragment (FRAGMENT)

    @Test
    void encodeFragmentSlash() {
        assertEquals("a/b", URLEncodeUtil.encodeFragment("a/b"));
    }

    @Test
    void encodeFragmentQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeFragment("a?b"));
    }

    @Test
    void encodeFragmentPlus() {
        assertEquals("+", URLEncodeUtil.encodeFragment("+"));
    }

    @Test
    void encodeFragmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeFragment("测试"));
    }

    @Test
    void encodeFragmentNull() {
        assertNull(URLEncodeUtil.encodeFragment(null));
    }

    // --------------------------------------- encodeAll (UNRESERVED)

    @Test
    void encodeAllBasic() {
        assertEquals("abc123", URLEncodeUtil.encodeAll("abc123"));
    }

    @Test
    void encodeAllTilde() {
        assertEquals("~", URLEncodeUtil.encodeAll("~"));
    }

    @Test
    void encodeAllDash() {
        assertEquals("-", URLEncodeUtil.encodeAll("-"));
    }

    @Test
    void encodeAllDot() {
        assertEquals(".", URLEncodeUtil.encodeAll("."));
    }

    @Test
    void encodeAllUnderscore() {
        assertEquals("_", URLEncodeUtil.encodeAll("_"));
    }

    @Test
    void encodeAllStar() {
        assertEquals("%2A", URLEncodeUtil.encodeAll("*"));
    }

    @Test
    void encodeAllSlash() {
        assertEquals("%2F", URLEncodeUtil.encodeAll("/"));
    }

    @Test
    void encodeAllPlus() {
        assertEquals("%2B", URLEncodeUtil.encodeAll("+"));
    }

    @Test
    void encodeAllSpace() {
        assertEquals("%20", URLEncodeUtil.encodeAll(" "));
    }

    @Test
    void encodeAllChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLEncodeUtil.encodeAll("测试"));
    }

    @Test
    void encodeAllNull() {
        assertNull(URLEncodeUtil.encodeAll(null));
    }

    // --------------------------------------- 跨方法对比

    @Test
    void encodeVsPathSegmentSlash() {
        assertEquals("path/to", URLEncodeUtil.encode("path/to"));
        assertEquals("path%2Fto", URLEncodeUtil.encodePathSegment("path/to"));
    }

    @Test
    void encodeQueryVsFragment() {
        final String input = "a/b?c+d=e&f";
        assertEquals(URLEncodeUtil.encodeQuery(input), URLEncodeUtil.encodeFragment(input));
    }

    @Test
    void encodeQueryVsEncodeQuestionMark() {
        assertEquals("a?b", URLEncodeUtil.encodeQuery("a?b"));
        assertEquals("a%3Fb", URLEncodeUtil.encode("a?b"));
    }
}
