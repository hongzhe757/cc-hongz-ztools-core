package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link XmlEscape} 单元测试.
 *
 * @author hongzhe.qin
 */
class XmlEscapeTest {

    /**
     * 验证 null 输入返回 null.
     */
    @Test
    void nullReturnsNull() {
        assertNull(XmlEscape.escape(null));
        assertNull(XmlEscape.unescape(null));
    }

    /**
     * 验证 XML 五类特殊字符转义.
     */
    @Test
    void escapeSpecialChars() {
        assertEquals("&lt;a&amp;&quot;&apos;&gt;", XmlEscape.escape("<a&\"'>"));
    }

    /**
     * 验证反转义往返.
     */
    @Test
    void unescapeRoundTrip() {
        final String raw = "<tag attr=\"v\">&</tag>";
        assertEquals(raw, XmlEscape.unescape(XmlEscape.escape(raw)));
    }
}
