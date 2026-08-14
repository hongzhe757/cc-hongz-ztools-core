package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 禁止序列字符规则.
 * 禁止字母序列（abcde/ABCDE）、数字序列（12345）、反向序列（54321/edcba）.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoSequentialCharRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "NoSequentialCharRule";

    /**
     * 字母序列.
     */
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 数字序列.
     */
    private static final String DIGITS = "01234567890";

    /**
     * 默认最小序列长度.
     */
    public static final int DEFAULT_MIN_SEQ_LENGTH = 4;

    /**
     * 最小序列长度阈值.
     */
    private final int minSeqLength;

    /**
     * 构造规则，使用默认值.
     */
    public NoSequentialCharRule() {
        this(DEFAULT_MIN_SEQ_LENGTH);
    }

    /**
     * 构造规则.
     *
     * @param minSeqLength 触发告警的最小连续序列长度
     */
    public NoSequentialCharRule(final int minSeqLength) {
        this.minSeqLength = minSeqLength;
    }

    @Override
    public RuleResult validate(final String password) {
        if (hasSequential(password)) {
            return RuleResult.failed(RULE_NAME, "密码包含连续序列字符");
        }
        return RuleResult.passed(RULE_NAME);
    }

    /**
     * 检测密码中是否包含长度为 minSeqLength 的连续序列.
     *
     * @param password 密码
     * @return 是否包含序列
     */
    private boolean hasSequential(final String password) {
        final int len = password.length(); for (int start = 0; start < len - minSeqLength + 1; start++) {
            final String sub = password.substring(start, start + minSeqLength); final String lower = sub.toLowerCase();

            // 检测是否为字母连续序列
            if (ALPHABET.contains(lower)) {
                return true;
            }

            // 检测是否为纯数字升序或降序序列
            if (isAllDigit(sub)
                    && (DIGITS.contains(sub) || new StringBuilder(DIGITS).reverse().toString().contains(sub))) {
                return true;
            }
        } return false;
    }

    /**
     * 检测字符串是否全为数字.
     *
     * @param s 字符串
     * @return 是否全为数字
     */
    private boolean isAllDigit(final String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        } return true;
    }
}
