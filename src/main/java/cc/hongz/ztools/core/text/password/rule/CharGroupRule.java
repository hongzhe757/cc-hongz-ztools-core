package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 字符组约束规则.
 * 可配置每种字符类型（数字、小写字母、大写字母、特殊字符）的最少出现次数.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class CharGroupRule implements PasswordRule {

	/**
	 * 规则名称.
	 */
	private static final String RULE_NAME = "CharGroupRule";

	/**
	 * 最少数字个数.
	 */
	private final int minDigit;

	/**
	 * 最少小写字母个数.
	 */
	private final int minLowercase;

	/**
	 * 最少大写字母个数.
	 */
	private final int minUppercase;

	/**
	 * 最少特殊字符个数.
	 */
	private final int minSpecial;

	/**
	 * 构造字符组规则.
	 *
	 * @param minDigit     最少数字个数
	 * @param minLowercase 最少小写字母个数
	 * @param minUppercase 最少大写字母个数
	 * @param minSpecial   最少特殊字符个数
	 */
	public CharGroupRule(final int minDigit, final int minLowercase, final int minUppercase, final int minSpecial) {
		this.minDigit = minDigit; this.minLowercase = minLowercase; this.minUppercase = minUppercase;
		this.minSpecial = minSpecial;
	}

	@Override
	public RuleResult validate(final String password) {
		int digitCount = 0; int lowerCount = 0; int upperCount = 0; int specialCount = 0;

		for (int i = 0; i < password.length(); i++) {
			final char c = password.charAt(i); if (c >= '0' && c <= '9') {
				digitCount++;
			} else if (c >= 'a' && c <= 'z') {
				lowerCount++;
			} else if (c >= 'A' && c <= 'Z') {
				upperCount++;
			} else {
				specialCount++;
			}
		}

		if (digitCount < minDigit) {
			return RuleResult.failed(RULE_NAME, "至少需要" + minDigit + "个数字");
		} if (lowerCount < minLowercase) {
			return RuleResult.failed(RULE_NAME, "至少需要" + minLowercase + "个小写字母");
		} if (upperCount < minUppercase) {
			return RuleResult.failed(RULE_NAME, "至少需要" + minUppercase + "个大写字母");
		} if (specialCount < minSpecial) {
			return RuleResult.failed(RULE_NAME, "至少需要" + minSpecial + "个特殊字符");
		} return RuleResult.passed(RULE_NAME);
	}
}
