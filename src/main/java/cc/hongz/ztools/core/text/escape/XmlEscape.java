package cc.hongz.ztools.core.text.escape;

import cc.hongz.ztools.core.text.replacer.LookupReplacer;
import cc.hongz.ztools.core.text.replacer.ReplacerChain;

/**
 * XML特殊字符转义.
 * @author looly
 */
public class XmlEscape extends ReplacerChain {
    private static final long serialVersionUID = 1L;

    protected static final String[][] BASIC_ESCAPE = { //
            {"'", "&apos;"}, // ' - single-quote
            {"\"", "&quot;"}, // " - double-quote
            {"&", "&amp;"}, // & - ampersand
            {"<", "&lt;"}, // < - less-than
            {">", "&gt;"}, // > - greater-than
    };

    /**
     * 构造.
     */
    public XmlEscape() {
        addChain(new LookupReplacer(BASIC_ESCAPE));
    }
}
