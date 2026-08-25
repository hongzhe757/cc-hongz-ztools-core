package cc.hongz.ztools.core.text.escape;

import cc.hongz.ztools.core.text.replacer.LookupReplacer;
import cc.hongz.ztools.core.text.replacer.ReplacerChain;

/**
 * XML的UNESCAPE.
 * <p>
 * 迁移自 hutool {@code cc.hongz.ztools.core.text.escape.XmlUnescape}.
 *
 * @author looly
 */
public class XmlUnescape extends ReplacerChain {
    private static final long serialVersionUID = 1L;

    protected static final String[][] BASIC_UNESCAPE = InternalEscapeUtil.invert(XmlEscape.BASIC_ESCAPE);

    /**
     * 构造.
     */
    public XmlUnescape() {
        addChain(new LookupReplacer(BASIC_UNESCAPE));
        addChain(new NumericEntityUnescaper());
    }
}
