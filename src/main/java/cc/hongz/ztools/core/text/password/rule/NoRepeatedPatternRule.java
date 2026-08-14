package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 禁止重复模式规则.
 * 检测密码是否由重复片段组成，如 abcabc（2段相同）、ababab（3段相同）.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoRepeatedPatternRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "NoRepeatedPatternRule";

    /**
     * 默认最小重复段数.
     */
    public static final int DEFAULT_MIN_SEGMENTS = 2;

    /**
     * 最小重复段数.
     */
    private final int minSegments;

    /**
     * 构造规则，使用默认值.
     */
    public NoRepeatedPatternRule() {
        this(DEFAULT_MIN_SEGMENTS);
    }

    /**
     * 构造规则.
     *
     * @param minSegments 触发告警的最小重复段数
     */
    public NoRepeatedPatternRule(final int minSegments) {
        this.minSegments = minSegments;
    }

    @Override
    public RuleResult validate(final String password) {
        final int len = password.length(); for (int segments = minSegments; segments <= 3; segments++) {
            if (len % segments == 0) {
                final int partLen = len / segments; if (isRepeatedPattern(password, segments, partLen)) {
                    return RuleResult.failed(RULE_NAME, "密码包含重复模式");
                }
            }
        } return RuleResult.passed(RULE_NAME);
    }

    /**
     * 检测密码是否由 segments 个相同片段组成.
     *
     * @param password 密码
     * @param segments 段数
     * @param partLen  每段长度
     * @return 是否为重复模式
     */
    private boolean isRepeatedPattern(final String password, final int segments, final int partLen) {
        final String first = password.substring(0, partLen); for (int i = 1; i < segments; i++) {
            final String part = password.substring(i * partLen, (i + 1) * partLen); if (!first.equals(part)) {
                return false;
            }
        } return true;
    }
}
