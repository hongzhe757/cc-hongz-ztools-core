package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 禁止连续相同字符规则.
 * 可配置允许的最大连续相同字符数.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoConsecutiveCharRule implements PasswordRule {

	/**
	 * 规则名称.
	 */
	private static final String RULE_NAME = "NoConsecutiveCharRule";

	/**
	 * 默认最大允许连续相同字符数.
	 */
	public static final int DEFAULT_MAX_CONSECUTIVE = 2;

	/**
	 * 最大允许连续相同字符数.
	 */
	private final int maxConsecutive;

	/**
	 * 构造规则，使用默认值.
	 */
	public NoConsecutiveCharRule() {
		this(DEFAULT_MAX_CONSECUTIVE);
	}

	/**
	 * 构造规则.
	 *
	 * @param maxConsecutive 允许的最大连续相同字符数
	 */
	public NoConsecutiveCharRule(final int maxConsecutive) {
		this.maxConsecutive = maxConsecutive;
	}

	@Override
	public RuleResult validate(final String password) {
		int consecutive = 1; char prev = password.charAt(0); for (int i = 1; i < password.length(); i++) {
			final char current = password.charAt(i); if (current == prev) {
				consecutive++; if (consecutive > maxConsecutive) {
					return RuleResult.failed(RULE_NAME, "不允许连续" + consecutive + "个相同字符");
				}
			} else {
				consecutive = 1; prev = current;
			}
		} return RuleResult.passed(RULE_NAME);
	}
}
