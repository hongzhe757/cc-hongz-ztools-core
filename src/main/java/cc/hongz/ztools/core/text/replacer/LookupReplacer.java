package cc.hongz.ztools.core.text.replacer;

import cc.hongz.ztools.core.text.StrBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 查找替换器，通过查找指定关键字，替换对应的值.
 *
 * @author looly
 * @since 4.1.5
 */
public class LookupReplacer extends StrReplacer {
    private static final long serialVersionUID = 1L;

    /**
     * 查找映射.
     */
    private final Map<String, String> lookupMap;
    /**
     * 前缀集合，用于快速判断是否需要查找.
     */
    private final Set<Character> prefixSet;
    /**
     * 最小匹配长度.
     */
    private final int minLength;
    /**
     * 最大匹配长度.
     */
    private final int maxLength;

    /**
     * 构造.
     *
     * @param lookup 被查找的键值对
     */
    public LookupReplacer(final String[]... lookup) {
        this.lookupMap = new HashMap<>();
        this.prefixSet = new HashSet<>();

        int minLen = Integer.MAX_VALUE;
        int maxLen = 0;
        String key;
        int keySize;
        for (final String[] pair : lookup) {
            key = pair[0];
            lookupMap.put(key, pair[1]);
            this.prefixSet.add(key.charAt(0));
            keySize = key.length();
            if (keySize > maxLen) {
                maxLen = keySize;
            }
            if (keySize < minLen) {
                minLen = keySize;
            }
        }
        this.maxLength = maxLen;
        this.minLength = minLen;
    }

    @Override
    protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
        if (prefixSet.contains(str.charAt(pos))) {
            int max = this.maxLength;
            if (pos + this.maxLength > str.length()) {
                max = str.length() - pos;
            }
            CharSequence subSeq;
            String result;
            for (int i = max; i >= this.minLength; i--) {
                subSeq = str.subSequence(pos, pos + i);
                result = lookupMap.get(subSeq.toString());
                if (null != result) {
                    out.append(result);
                    return i;
                }
            }
        }
        return 0;
    }
}
