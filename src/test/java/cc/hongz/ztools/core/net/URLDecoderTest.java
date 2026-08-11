package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link URLDecoder} 单元测试.
 */
class URLDecoderTest {

    // --------------------------------------- decode (String, Charset)

    @Test
    @DisplayName("decode 基本中文解码")
    void decodeBasic() {
        assertEquals("测试",
                URLDecoder.decode("%E6%B5%8B%E8%AF%95", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 加号转空格")
    void decodePlusToSpace() {
        assertEquals(" ", URLDecoder.decode("+", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode %20 转空格")
    void decodePercent20ToSpace() {
        assertEquals(" ", URLDecoder.decode("%20", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 加号不转空格")
    void decodePlusNotToSpace() {
        assertEquals("+", URLDecoder.decode("+", CharsetUtil.CHARSET_UTF_8, false));
    }

    @Test
    @DisplayName("decodeForPath 不对加号解码")
    void decodeForPath() {
        assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode null 返回 null")
    void decodeNull() {
        assertNull(URLDecoder.decode(null, CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode null charset 返回原字符串")
    void decodeNullCharset() {
        assertEquals("test", URLDecoder.decode("test", null));
    }

    @Test
    @DisplayName("decode 空字符串")
    void decodeEmpty() {
        assertEquals("", URLDecoder.decode("", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 无需编码的普通字符串")
    void decodeNoEncodingNeeded() {
        assertEquals("hello world",
                URLDecoder.decode("hello world", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 混合编码与明文")
    void decodeMixedEncodedAndPlain() {
        assertEquals("测试你好",
                URLDecoder.decode("%E6%B5%8B%E8%AF%95你好", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 非法百分号序列跳过")
    void decodeInvalidPercentSequence() {
        assertEquals("%zz", URLDecoder.decode("%zz", CharsetUtil.CHARSET_UTF_8));
    }

    @Test
    @DisplayName("decode 不完整百分号序列跳过")
    void decodeIncompletePercentSequence() {
        assertEquals("%2", URLDecoder.decode("%2", CharsetUtil.CHARSET_UTF_8));
    }

    // --------------------------------------- UTF-16 roundtrip (issue#3063)

    @Test
    @DisplayName("decode UTF-16 编码往返测试")
    void decodeUtf16Roundtrip() {
        final String s = "测试";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        final String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_16);
        assertEquals(s, decoded);
    }

    @Test
    @DisplayName("decode UTF-16 混合明文解码")
    void decodeUtf16Mixed() {
        final String s = "测试你好";
        final String encoded = URLEncodeUtil.encode(s, StandardCharsets.UTF_16);
        final String mixed = encoded + "你好";
        final String decoded = URLDecoder.decode(mixed, StandardCharsets.UTF_16);
        assertEquals("测试你好你好", decoded);
    }

    // --------------------------------------- decode (byte[])

    @Test
    @DisplayName("decode byte 数组基本解码")
    void decodeBytesBasic() {
        final byte[] bytes = "%E6%B5%8B".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals("测", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("decode byte 加号转空格")
    void decodeBytesPlusToSpace() {
        final byte[] bytes = "+".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals(" ", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    @Test
    @DisplayName("decode byte 加号不转空格")
    void decodeBytesPlusNotToSpace() {
        final byte[] bytes = "+".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes, false);
        assertEquals("+", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    @Test
    @DisplayName("decode byte null 返回 null")
    void decodeBytesNull() {
        assertNull(URLDecoder.decode((byte[]) null));
    }

    @Test
    @DisplayName("decode byte 非法百分号跳过")
    void decodeBytesInvalidPercent() {
        final byte[] bytes = "%zz".getBytes(StandardCharsets.ISO_8859_1);
        final byte[] decoded = URLDecoder.decode(bytes);
        assertEquals("%zz", new String(decoded, StandardCharsets.ISO_8859_1));
    }

    // --------------------------------------- encode -> decode roundtrip

    @Test
    @DisplayName("encode/decode 往返测试")
    void roundtripEncodeDecode() {
        final String original = "测试/path?query=value&name=中文";
        final String encoded = URLEncodeUtil.encode(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        assertEquals(original, decoded);
    }

    @Test
    @DisplayName("encodeQuery/decode 往返测试")
    void roundtripEncodeQueryDecode() {
        final String original = "key=value+中文";
        final String encoded = URLEncodeUtil.encodeQuery(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        assertEquals("key=value 中文", decoded);
    }

    @Test
    @DisplayName("encodePathSegment/decode 往返测试")
    void roundtripEncodePathSegmentDecode() {
        final String original = "a/b";
        final String encoded = URLEncodeUtil.encodePathSegment(original);
        final String decoded = URLDecoder.decode(encoded, CharsetUtil.CHARSET_UTF_8);
        assertEquals(original, decoded);
    }
}
