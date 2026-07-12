package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.codec.PercentCodec;
import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RFC3986} 单元测试.
 */
class RFC3986Test {

    @Test
    void encodeQueryParamValue() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.CHARSET_UTF_8);
        assertEquals("a=b", encode);

        encode = RFC3986.QUERY_PARAM_VALUE.encode("a+1=b", CharsetUtil.CHARSET_UTF_8);
        assertEquals("a+1=b", encode);
    }

    @Test
    void encodeQueryParamValuePercent() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%b", CharsetUtil.CHARSET_UTF_8);
        assertEquals("a=%25b", encode);
    }

    @Test
    void encodeQueryParamValueWithSafe() {
        String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%25", CharsetUtil.CHARSET_UTF_8, '%');
        assertEquals("a=%25", encode);
    }

    @Test
    void encodeAllWithSpaceAsPlus() throws UnsupportedEncodingException {
        String toVerifyText = "行吧行吧 cargo:1.0,\"Deta-ils:[{";
        final String encode = PercentCodec.of(RFC3986.UNRESERVED).setEncodeSpaceAsPlus(true).encode(toVerifyText, CharsetUtil.CHARSET_UTF_8);
        final String encodeJdk = URLEncoder.encode(toVerifyText, "UTF-8");
        assertEquals(encode, encodeJdk);
    }

    @Test
    void unreservedContainsAlphaDigit() {
        // ALPHA / DIGIT / "-" / "." / "_" / "~"
        assertEquals("abcXYZ012", RFC3986.UNRESERVED.encode("abcXYZ012", CharsetUtil.CHARSET_UTF_8));
        assertEquals("-._~", RFC3986.UNRESERVED.encode("-._~", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void pathPreservesSlash() {
        assertEquals("a/b", RFC3986.PATH.encode("a/b", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void segmentEncodesSlash() {
        assertEquals("a%2Fb", RFC3986.SEGMENT.encode("a/b", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void queryPreservesQuestionMark() {
        assertEquals("a?b", RFC3986.QUERY.encode("a?b", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void fragmentEqualsQuery() {
        final String input = "a/b?c+d=e";
        assertEquals(
                RFC3986.QUERY.encode(input, CharsetUtil.CHARSET_UTF_8),
                RFC3986.FRAGMENT.encode(input, CharsetUtil.CHARSET_UTF_8)
        );
    }
}
