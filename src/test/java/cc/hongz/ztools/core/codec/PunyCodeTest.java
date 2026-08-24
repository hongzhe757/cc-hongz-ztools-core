package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link PunyCode} 单元测试.
 * <p>
 * 覆盖标签与国际化域名的 Punycode 编解码.
 *
 * @author hongzhe.qin
 */
@DisplayName("Punycode 编码")
class PunyCodeTest {

    /**
     * 验证含中文标签的编解码.
     */
    @DisplayName("含中文标签的编解码")
    @Test
    void encodeDecodeLabel() {
        final String text = "Hutool编码器";
        final String puny = PunyCode.encode(text);
        assertEquals("Hutool-ux9js33tgln", puny);
        assertEquals(text, PunyCode.decode("Hutool-ux9js33tgln"));
        assertEquals(text, PunyCode.decode("xn--Hutool-ux9js33tgln"));
    }

    /**
     * 验证纯 ASCII 标签无需编码.
     */
    @DisplayName("纯 ASCII 标签无需编码")
    @Test
    void encodeAsciiUnchanged() {
        assertEquals("Hutool", PunyCode.encode("Hutool"));
    }

    /**
     * 验证中文顶级域完整域名编解码.
     */
    @DisplayName("中文顶级域完整域名编解码")
    @Test
    void encodeDecodeDomainChineseTld() {
        final String domain = "赵新虎.中国";
        assertEquals(domain, PunyCode.decodeDomain(PunyCode.encodeDomain(domain)));
    }

    /**
     * 验证中文标签 + {@code .com} 域名编码格式.
     */
    @DisplayName("中文标签 + .com 域名编码格式")
    @Test
    void encodeDecodeDomainCom() {
        final String domain = "赵新虎.com";
        assertEquals("xn--efvz93e52e.com", PunyCode.encodeDomain(domain));
        assertEquals(domain, PunyCode.decodeDomain("xn--efvz93e52e.com"));
    }

    /**
     * 验证大写 {@code .COM} 后缀保留大小写.
     */
    @DisplayName("大写 .COM 后缀保留大小写")
    @Test
    void encodeDecodeDomainComUpperCase() {
        final String domain = "赵新虎.COM";
        assertEquals("xn--efvz93e52e.COM", PunyCode.encodeDomain(domain));
        assertEquals(domain, PunyCode.decodeDomain("xn--efvz93e52e.COM"));
    }
}
