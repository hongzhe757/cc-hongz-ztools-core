package cc.hongz.ztools.core.text.password;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码校验责任链（责任链模式）.
 * 按顺序执行规则，遇到第一个失败即短路返回.
 * 适用于注册表单等需要即时反馈的场景，给用户明确的第一个失败原因.
 *
 * <pre>
 *     PasswordChain chain = PasswordChain.of(policy.getRules());
 *     RuleResult firstFailure = chain.validate("password");
 *     if (firstFailure != null) {
 *         System.out.println("密码不符合要求: " + firstFailure.getMessage());
 *     }
 * </pre>
 *
 * @author hongz
 * @since 5.8.0
 */
public final class PasswordChain implements PasswordRule {

    /**
     * 有序规则列表.
     */
    private final List<PasswordRule> rules;

    /**
     * 构造责任链.
     *
     * @param rules 有序规则列表
     */
    public PasswordChain(final List<PasswordRule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    /**
     * 从规则列表创建责任链.
     *
     * @param rules 规则列表
     * @return PasswordChain
     */
    public static PasswordChain of(final List<PasswordRule> rules) {
        return new PasswordChain(rules);
    }

    /**
     * 按顺序校验，第一个失败即返回.
     * 全部通过则返回 passed 结果.
     *
     * @param password 待校验密码
     * @return 第一个失败的规则结果，全部通过则返回 passed
     */
    @Override
    public RuleResult validate(final String password) {
        for (final PasswordRule rule : rules) {
            final RuleResult result = rule.validate(password); if (!result.isPassed()) {
                return result;
            }
        } return RuleResult.passed("PasswordChain");
    }

    /**
     * 校验密码是否通过所有规则.
     *
     * @param password 待校验密码
     * @return true 表示通过全部规则
     */
    public boolean isPassed(final String password) {
        return validate(password).isPassed();
    }
}
