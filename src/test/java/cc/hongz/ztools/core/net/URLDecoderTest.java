package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link URLDecoder} 单元测试.
 */
class URLDecoderTest {

    // --------------------------------------- decode (String, Charset)

    @Test
    void decodeBasic() {
        assertEquals("测试", URLDecoder.decode("%E6%B5%8B%E8%AF%95", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodePlusToSpace() {
        assertEquals(" ", URLDecoder.decode("+", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodePercent20ToSpace() {
        assertEquals(" ", URLDecoder.decode("%20", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodePlusNotToSpace() {
        assertEquals("+", URLDecoder.decode("+", CharsetUtil.CHARSET_UTF_8, false));
    }

    @Test
    void decodeForPath() {
        assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeNull() {
        assertNull(URLDecoder.decode(null, CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeNullCharset() {
        assertEquals("test", URLDecoder.decode("test", null));
    }

    @Test
    void decodeEmpty() {
        assertEquals("", URLDecoder.decode("", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeNoEncodingNeeded() {
        assertEquals("hello world", URLDecoder.decode("hello world", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeMixedEncodedAndPlain() {
        assertEquals("测试你好", URLDecoder.decode("%E6%B5%8B%E8%AF%95你好", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeInvalidPercentSequence() {
        // 不符合规范的 % 形式直接输出
        assertEquals("%zz", URLDecoder.decode("%zz", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    void decodeIncompletePercentSequence() {
        assertEquals("%2", URLDecoder.decode("%2", CharsetUtil.CHARSET_UTF_8));
    }

    // --------------------------------------- UTF-16 roundtrip (issue#3063)

    @Test
    void decodeUtf16Roundtrip() {
        final String s = "测试";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        final String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_16);
        assertEquals(s, decoded);
    }

    @Test
    void decodeUtf16Mixed() {
        final String s = "测试你好";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        final String mixed = encoded + "你好";
        final String decoded = URLDecoder.decode(mixed, StandardCharsets.UTF_16);
        assertEquals("测试你好你好", decoded);
    }

    // --------------------------------------- decode (byte[])

    @Test
    void decodeBytesBasic() {
        final byte[] bytes = "%E6%B5%8B".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals("测", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    void decodeBytesPlusToSpace() {
        final byte[] bytes = "+".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals(" ", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    @Test
    void decodeBytesPlusNotToSpace() {
        final byte[] bytes = "+".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes, false);
        assertEquals("+", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    @Test
    void decodeBytesNull() {
        assertNull(URLDecoder.decode((byte[]) null));
    }

    @Test
    void decodeBytesInvalidPercent() {
        final byte[] bytes = "%zz".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals("%zz", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    // --------------------------------------- encode → decode roundtrip

    @Test
    void roundtripEncodeDecode() {
        final String original = "测试/path?query=value&name=中文";
        final String encoded = URLEncodeUtil.encode(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        assertEquals(original, decoded);
    }

    @Test
    void roundtripEncodeQueryDecode() {
        final String original = "key=value+中文";
        final String encoded = URLEncodeUtil.encodeQuery(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        // encodeQuery 保留 +，decode 将 + 转为空格
        assertNotEquals(original, decoded);
        assertEquals("key=value 中文", decoded);
    }

    @Test
    void roundtripEncodePathSegmentDecode() {
        final String original = "a/b";
        final String encoded = URLEncodeUtil.encodePathSegment(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        assertEquals(original, decoded);
    }
}
