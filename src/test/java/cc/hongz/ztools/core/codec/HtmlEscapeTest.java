package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link HtmlEscape} 单元测试.
 *
 * @author hongzhe.qin
 */
class HtmlEscapeTest {

    /**
     * 验证 null 输入返回 null.
     */
    @Test
    void nullReturnsNull() {
        assertNull(HtmlEscape.escape(null));
        assertNull(HtmlEscape.unescape(null));
    }

    /**
     * 验证 HTML4 不转义单引号.
     */
    @Test
    void escapeKeepsApostrophe() {
        assertEquals("it's &lt;ok&gt;", HtmlEscape.escape("it's <ok>"));
    }

    /**
     * 验证反转义往返.
     */
    @Test
    void unescapeRoundTrip() {
        final String raw = "<div>&amp;</div>";
        assertEquals(raw, HtmlEscape.unescape(HtmlEscape.escape(raw)));
    }
}
