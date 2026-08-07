package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.text.password.PasswordChain;
import cc.hongz.ztools.core.text.password.PasswordPolicy;
import cc.hongz.ztools.core.text.password.PasswordPolicyBuilder;
import cc.hongz.ztools.core.text.password.RuleResult;

import java.util.List;

/**
 * 密码校验工具类（策略模式 + 组合模式 + 责任链模式）.
 * 支持多业务场景下不同密码策略的灵活配置：
 * <ul>
 *   <li>{@link #validate(String)}：组合模式，全量收集所有违规项</li>
 *   <li>{@link #validateChain(String)}：责任链模式，第一个失败即短路返回</li>
 *   <li>{@link #policyBuilder()}：构建器模式，流式 API 自定义策略</li>
 * </ul>
 *
 * <p>
 * 典型使用示例：
 * <pre>
 *     // 全量校验（评分/分析用）
 *     List&lt;RuleResult&gt; failures = PasswordValidator.validate("123456");
 *
 *     // 短路校验（注册表单用）
 *     RuleResult first = PasswordValidator.validateChain("123456");
 *
 *     // 自定义策略（企业级强密码）
 *     PasswordPolicy policy = PasswordValidator.policyBuilder()
 *         .minLength(8)
 *         .requireDigit(1)
 *         .requireLowercase(1)
 *         .requireUppercase(1)
 *         .requireSpecial(1)
 *         .noKeyboardPattern()
 *         .noCommonPassword()
 *         .build();
 * </pre>
 *
 * <p>
 * 与 {@link PasswdStrength} 的区别：
 * <ul>
 *   <li>PasswdStrength：传统评分制，返回分数（0-13+）和等级枚举</li>
 *   <li>PasswordValidator：策略制，返回具体的规则校验结果，支持自定义策略</li>
 * </ul>
 *
 * @author hongz
 * @since 5.8.0
 */
public final class PasswordValidator {

    /**
     * 私有构造器（工具类）.
     */
    private PasswordValidator() {
    }

    /**
     * 使用组合模式全量校验密码，返回所有未通过的规则结果.
     * 空列表表示密码通过所有规则.
     *
     * @param passwd 密码
     * @return 未通过的规则结果列表
     */
    public static List<RuleResult> validate(final String passwd) {
        return createDefaultPolicy().validateAll(passwd);
    }

    /**
     * 使用责任链模式短路校验密码，返回第一个未通过的规则结果.
     * 返回 passed 结果表示密码通过所有规则.
     *
     * @param passwd 密码
     * @return 第一个未通过的规则结果
     */
    public static RuleResult validateChain(final String passwd) {
        return createDefaultChain().validate(passwd);
    }

    /**
     * 检查密码是否通过所有默认规则.
     *
     * @param passwd 密码
     * @return true 表示通过所有规则
     */
    public static boolean isPassed(final String passwd) {
        return createDefaultPolicy().isPassed(passwd);
    }

    /**
     * 创建默认的密码策略（组合模式）, 全量收集所有违规项.
     * 适用于需要完整校验结果的分析/评分场景.
     *
     * @return PasswordPolicy
     */
    public static PasswordPolicy createDefaultPolicy() {
        return PasswordPolicy.builder()
            .minLength(6)
            .noKeyboardPattern()
            .noRepeatedPattern()
            .noDatePattern()
            .noCommonPassword()
            .build();
    }

    /**
     * 创建默认的密码校验责任链，第一个失败即返回（短路）.
     * 适用于注册表单等需要即时反馈的场景.
     *
     * @return PasswordChain
     */
    public static PasswordChain createDefaultChain() {
        return PasswordChain.of(createDefaultPolicy().getRules());
    }

    /**
     * 创建密码策略构建器，用于构建自定义密码策略.
     *
     * <pre>
     *     // 纯数字密码策略（部分业务系统允许纯数字）
     *     PasswordPolicy numericOnly = PasswordValidator.policyBuilder()
     *         .minLength(6)
     *         .noSequential(4)
     *         .build();
     *
     *     // 企业级强密码策略
     *     PasswordPolicy enterprise = PasswordValidator.policyBuilder()
     *         .minLength(8)
     *         .requireDigit(1)
     *         .requireLowercase(1)
     *         .requireUppercase(1)
     *         .requireSpecial(1)
     *         .noConsecutive(3)
     *         .noSequential(4)
     *         .noKeyboardPattern()
     *         .noRepeatedPattern()
     *         .noDatePattern()
     *         .noCommonPassword()
     *         .build();
     * </pre>
     *
     * @return PasswordPolicyBuilder
     */
    public static PasswordPolicyBuilder policyBuilder() {
        return PasswordPolicy.builder();
    }
}
