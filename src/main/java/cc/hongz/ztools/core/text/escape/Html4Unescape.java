package cc.hongz.ztools.core.text.escape;

import cc.hongz.ztools.core.text.replacer.LookupReplacer;

/**
 * HTML4的UNESCAPE.
 *
 * @author looly
 * @since 5.7.14
 */
public class Html4Unescape extends XmlUnescape {
    private static final long serialVersionUID = 1L;

    /**
     * ISO8859-1 反向转义映射.
     */
    protected static final String[][] ISO8859_1_UNESCAPE =
            InternalEscapeUtil.invert(Html4Escape.ISO8859_1_ESCAPE);
    /**
     * HTML4.0 扩展反向转义映射.
     */
    protected static final String[][] HTML40_EXTENDED_UNESCAPE =
            InternalEscapeUtil.invert(Html4Escape.HTML40_EXTENDED_ESCAPE);

    /**
     * 构造，加载HTML4反向转义规则.
     */
    public Html4Unescape() {
        super();
        addChain(new LookupReplacer(ISO8859_1_UNESCAPE));
        addChain(new LookupReplacer(HTML40_EXTENDED_UNESCAPE));
    }
}
