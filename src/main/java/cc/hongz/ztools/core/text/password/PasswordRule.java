package cc.hongz.ztools.core.text.password;

/**
 * 密码校验规则接口（策略模式）.
 * 每个实现类代表一种独立的密码约束检查.
 *
 * @author hongz
 * @since 5.8.0
 */
@FunctionalInterface
public interface PasswordRule {

	/**
	 * 校验密码是否满足当前规则.
	 *
	 * @param password 待校验的密码
	 * @return 校验结果，包含是否通过及失败原因
	 */
	RuleResult validate(final String password);
}
