package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 禁止键盘连续模式规则.
 * 检测横向键盘序列：qwertyuiop、asdfghjkl、zxcvbnm.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoKeyboardPatternRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "NoKeyboardPatternRule";

    /**
     * 键盘横向行.
     */
    private static final String[] KEYBOARD_ROWS = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};

    /**
     * 默认最小模式长度.
     */
    public static final int DEFAULT_MIN_PATTERN_LENGTH = 4;

    /**
     * 最小模式长度.
     */
    private final int minPatternLength;

    /**
     * 构造规则，使用默认值.
     */
    public NoKeyboardPatternRule() {
        this(DEFAULT_MIN_PATTERN_LENGTH);
    }

    /**
     * 构造规则.
     *
     * @param minPatternLength 触发告警的最小模式长度
     */
    public NoKeyboardPatternRule(final int minPatternLength) {
        this.minPatternLength = minPatternLength;
    }

    @Override
    public RuleResult validate(final String password) {
        final String lower = password.toLowerCase(); for (final String row : KEYBOARD_ROWS) {
            for (int start = 0; start < row.length() - minPatternLength + 1; start++) {
                final String pattern = row.substring(start, start + minPatternLength); if (lower.contains(pattern)) {
                    return RuleResult.failed(RULE_NAME, "密码包含键盘连续模式");
                }
            }
        } return RuleResult.passed(RULE_NAME);
    }
}
