package cc.hongz.ztools.core.text.replacer;

import cc.hongz.ztools.core.lang.Replacer;
import cc.hongz.ztools.core.text.StrBuilder;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;

/**
 * 抽象字符串替换类.
 * <p>
 * 通过实现replace方法，实现局部替换逻辑.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.text.replacer.StrReplacer}.
 *
 * @author looly
 */
public abstract class StrReplacer implements Replacer<CharSequence>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 抽象的字符串替换方法，通过传入原字符串和当前位置，执行替换逻辑，返回处理或替换的字符串长度部分.
     *
     * @param str 被处理的字符串
     * @param pos 当前位置
     * @param out 输出
     * @return 处理的原字符串长度，0表示跳过此字符
     */
    protected abstract int replace(CharSequence str, int pos, StrBuilder out);

    @Override
    public CharSequence replace(CharSequence t) {
        if (StrUtil.isEmpty(t)) {
            return t;
        }
        final int len = t.length();
        final StrBuilder builder = StrBuilder.create(len);
        int pos = 0;
        int consumed;
        while (pos < len) {
            consumed = replace(t, pos, builder);
            if (0 == consumed) {
                builder.append(t.charAt(pos));
                pos++;
            }
            pos += consumed;
        }
        return builder;
    }
}
