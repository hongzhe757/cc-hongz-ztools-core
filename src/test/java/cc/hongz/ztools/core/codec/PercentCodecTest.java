package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link PercentCodec} 单元测试.
 * <p>
 * 覆盖 URL 百分号编码及空格转 {@code +} 的表单模式.
 *
 * @author hongzhe.qin
 */
class PercentCodecTest {

    /**
     * RFC 3986 未保留字符集.
     */
    private static final String UNRESERVED = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";

    /**
     * 验证未保留字符不被编码.
     */
    @Test
    void encodeUnreservedChars() {
        final PercentCodec codec = PercentCodec.of(UNRESERVED);
        assertEquals("abc123-._~", codec.encode("abc123-._~", CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 验证空格与特殊字符被百分号编码.
     */
    @Test
    void encodeReservedChars() {
        final PercentCodec codec = PercentCodec.of(UNRESERVED);
        assertEquals("hello%20world%21", codec.encode("hello world!", CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 验证 {@code encodeSpaceAsPlus} 与 JDK {@link URLEncoder} 结果一致.
     */
    @Test
    void encodeSpaceAsPlusMatchesUrlEncoder() throws UnsupportedEncodingException {
        final String text = "行吧行吧 cargo:1.0,\"Deta-ils:[{";
        final PercentCodec codec = PercentCodec.of(UNRESERVED).setEncodeSpaceAsPlus(true);
        assertEquals(URLEncoder.encode(text, "UTF-8"), codec.encode(text, CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 验证自定义安全字符 {@code %} 不被二次编码.
     */
    @Test
    void encodeWithCustomSafeChar() {
        final PercentCodec codec = PercentCodec.of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=-._~");
        assertEquals("a=%25", codec.encode("a=%25", CharsetUtil.CHARSET_UTF_8, '%'));
    }
}
