package cc.hongz.ztools.core.text.finder;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 字符串查找器.<br>
 * 通过传入字符串，查找其在指定文本中的位置.
 *
 * @author looly
 * @since 5.7.14
 */
public class StrFinder extends TextFinder {
    private static final long serialVersionUID = 1L;

    /**
     * 被查找的字符串.
     */
    private final CharSequence strToFind;
    /**
     * 是否忽略大小写.
     */
    private final boolean caseInsensitive;

    /**
     * 构造.
     *
     * @param strToFind       被查找的字符串
     * @param caseInsensitive 是否忽略大小写
     */
    public StrFinder(final CharSequence strToFind, final boolean caseInsensitive) {
        Assert.notNull(strToFind, "String to find must be not null!");
        if (strToFind.length() == 0) {
            throw new IllegalArgumentException("String to find must not be empty");
        }
        this.strToFind = strToFind;
        this.caseInsensitive = caseInsensitive;
    }

    @Override
    public int start(final int from) {
        Assert.notNull(this.text, "Text to find must be not null!");
        final int subLen = strToFind.length();

        int begin = from;
        if (begin < 0) {
            begin = 0;
        }
        int endLimit = getValidEndIndex();
        if (negative) {
            for (int i = begin; i > endLimit; i--) {
                if (StrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
                    return i;
                }
            }
        } else {
            endLimit = endLimit - subLen + 1;
            for (int i = begin; i < endLimit; i++) {
                if (StrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
                    return i;
                }
            }
        }

        return INDEX_NOT_FOUND;
    }

    @Override
    public int end(final int start) {
        if (start < 0) {
            return -1;
        }
        return start + strToFind.length();
    }
}
