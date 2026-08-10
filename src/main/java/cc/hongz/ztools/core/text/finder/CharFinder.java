package cc.hongz.ztools.core.text.finder;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.NumberUtil;

/**
 * 字符查找器.<br>
 * 查找指定字符在字符串中的位置信息.
 *
 * @author looly
 * @since 5.7.14
 */
public class CharFinder extends TextFinder {
    private static final long serialVersionUID = 1L;

    /**
     * 被查找的字符.
     */
    private final char c;
    /**
     * 是否忽略大小写.
     */
    private final boolean caseInsensitive;

    /**
     * 构造，不忽略字符大小写.
     *
     * @param c 被查找的字符
     */
    public CharFinder(final char c) {
        this(c, false);
    }

    /**
     * 构造.
     *
     * @param c               被查找的字符
     * @param caseInsensitive 是否忽略大小写
     */
    public CharFinder(final char c, final boolean caseInsensitive) {
        this.c = c;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public int start(final int from) {
        Assert.notNull(this.text, "Text to find must be not null!");
        final int limit = getValidEndIndex();
        if (negative) {
            for (int i = from; i > limit; i--) {
                if (NumberUtil.equals(c, text.charAt(i), caseInsensitive)) {
                    return i;
                }
            }
        } else {
            for (int i = from; i < limit; i++) {
                if (NumberUtil.equals(c, text.charAt(i), caseInsensitive)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int end(final int start) {
        if (start < 0) {
            return -1;
        }
        return start + 1;
    }
}
