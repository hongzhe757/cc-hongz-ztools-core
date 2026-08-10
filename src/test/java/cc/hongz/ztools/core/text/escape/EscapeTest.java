package cc.hongz.ztools.core.text.escape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Escape/Unescape 单元测试.
 */
class EscapeTest {

    @Test
    void html4EscapeBasicTest() {
        final Html4Escape escape = new Html4Escape();
        assertEquals("&lt;div&gt;test&quot; &amp; 123&lt;/div&gt;",
                escape.replace("<div>test\" & 123</div>").toString());
    }

    @Test
    void html4UnescapeBasicTest() {
        final Html4Unescape unescape = new Html4Unescape();
        assertEquals("<div>test\" & 123</div>",
                unescape.replace("&lt;div&gt;test&quot; &amp; 123&lt;/div&gt;").toString());
    }

    @Test
    void numericEntityUnescapeTest() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals("A", unescaper.replace("&#65;").toString());
    }

    @Test
    void numericEntityHexUnescapeTest() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals("A", unescaper.replace("&#x41;").toString());
    }

    @Test
    void numericEntityNoMatchTest() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals("abc", unescaper.replace("abc").toString());
    }

    @Test
    void internalEscapeUtilInvertTest() {
        final String[][] input = {{"a", "A"}, {"b", "B"}};
        final String[][] result = InternalEscapeUtil.invert(input);
        assertEquals("A", result[0][0]);
        assertEquals("a", result[0][1]);
        assertEquals("B", result[1][0]);
        assertEquals("b", result[1][1]);
    }

    @Test
    void xmlEscapeTest() {
        final XmlEscape escape = new XmlEscape();
        assertEquals("&lt;div&gt;test&lt;/div&gt;",
                escape.replace("<div>test</div>").toString());
    }

    @Test
    void xmlUnescapeTest() {
        final XmlUnescape unescape = new XmlUnescape();
        assertEquals("<div>test</div>",
                unescape.replace("&lt;div&gt;test&lt;/div&gt;").toString());
    }
}
