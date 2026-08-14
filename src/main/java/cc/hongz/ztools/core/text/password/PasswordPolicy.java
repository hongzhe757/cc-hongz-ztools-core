package cc.hongz.ztools.core.text.password;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 密码策略组合容器（组合模式）.
 * 组合多个 {@link PasswordRule}，执行全量校验，收集所有未通过的规则结果.
 * 适用于密码强度评分/分析场景，需要获取完整检查结果.
 *
 * <pre>
 *     PasswordPolicy policy = PasswordPolicy.builder()
 *         .minLength(8)
 *         .requireDigit(1)
 *         .noKeyboardPattern()
 *         .build();
 *     List&lt;RuleResult&gt; failures = policy.validate("password");
 * </pre>
 *
 * @author hongz
 * @since 5.8.0
 */
public final class PasswordPolicy implements PasswordRule {

    /**
     * 规则列表.
     */
    private final List<PasswordRule> rules;

    /**
     * 构造密码策略.
     *
     * @param rules 规则列表
     */
    public PasswordPolicy(final List<PasswordRule> rules) {
        this.rules = Collections.unmodifiableList(new ArrayList<>(rules));
    }

    /**
     * 全量校验，返回所有未通过的规则结果.
     * 不会短路，始终遍历全部规则.
     *
     * @param password 待校验密码
     * @return 所有未通过的规则结果列表，全部通过时为空列表
     */
    public List<RuleResult> validateAll(final String password) {
        final List<RuleResult> failures = new ArrayList<>(); for (final PasswordRule rule : rules) {
            final RuleResult result = rule.validate(password); if (!result.isPassed()) {
                failures.add(result);
            }
        } return Collections.unmodifiableList(failures);
    }

    /**
     * 校验密码是否通过所有规则.
     *
     * @param password 待校验密码
     * @return true 表示通过全部规则
     */
    public boolean isPassed(final String password) {
        return validateAll(password).isEmpty();
    }

    /**
     * 实现 {@link PasswordRule} 接口，作为组合规则使用.
     * 密码通过所有子规则时返回 passed.
     *
     * @param password 待校验密码
     * @return 校验结果
     */
    @Override
    public RuleResult validate(final String password) {
        final List<RuleResult> failures = validateAll(password);
        if (failures.isEmpty()) {
            return RuleResult.passed("PasswordPolicy");
        }
        final StringBuilder msg = new StringBuilder();
        for (final RuleResult failure : failures) {
            if (msg.length() > 0) {
                msg.append("; ");
            }
            msg.append(failure.getMessage());
        }
        return RuleResult.failed("PasswordPolicy", msg.toString());
    }

    /**
     * 获取规则列表（供 PasswordChain 等使用）.
     *
     * @return 不可变规则列表
     */
    public List<PasswordRule> getRules() {
        return rules;
    }

    /**
     * 创建构建器.
     *
     * @return PasswordPolicyBuilder
     */
    public static PasswordPolicyBuilder builder() {
        return new PasswordPolicyBuilder();
    }
}
