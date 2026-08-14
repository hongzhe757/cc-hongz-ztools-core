package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 密码长度约束规则.
 * 可配置最小和最大长度.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class LengthRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "LengthRule";

    /**
     * 默认最小长度.
     */
    public static final int DEFAULT_MIN_LENGTH = 1;

    /**
     * 默认最大长度.
     */
    public static final int DEFAULT_MAX_LENGTH = Integer.MAX_VALUE;

    /**
     * 最小长度.
     */
    private final int minLength;

    /**
     * 最大长度.
     */
    private final int maxLength;

    /**
     * 构造长度规则，使用默认值.
     */
    public LengthRule() {
        this(DEFAULT_MIN_LENGTH, DEFAULT_MAX_LENGTH);
    }

    /**
     * 构造长度规则.
     *
     * @param minLength 最小长度
     * @param maxLength 最大长度
     */
    public LengthRule(final int minLength, final int maxLength) {
        this.minLength = minLength; this.maxLength = maxLength;
    }

    @Override
    public RuleResult validate(final String password) {
        if (password == null) {
            return RuleResult.failed(RULE_NAME, "密码不能为空");
        } final int len = password.length(); if (len < minLength) {
            return RuleResult.failed(RULE_NAME, "密码长度不能少于" + minLength + "位");
        } if (len > maxLength) {
            return RuleResult.failed(RULE_NAME, "密码长度不能超过" + maxLength + "位");
        } return RuleResult.passed(RULE_NAME);
    }
}
