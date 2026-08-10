package cc.hongz.ztools.core.text.password;

import cc.hongz.ztools.core.text.password.rule.CharGroupRule;
import cc.hongz.ztools.core.text.password.rule.LengthRule;
import cc.hongz.ztools.core.text.password.rule.NoSequentialCharRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Password 包单元测试.
 */
class PasswordTest {

    // ==================== RuleResult ====================

    @Test
    @DisplayName("RuleResult 通过状态测试")
    void ruleResultPassedTest() {
        final RuleResult result = RuleResult.passed("TestRule");
        assertTrue(result.isPassed());
        assertEquals("TestRule", result.getRuleName());
        assertEquals("", result.getMessage());
        assertEquals("TestRule: PASSED", result.toString());
    }

    @Test
    @DisplayName("RuleResult 失败状态测试")
    void ruleResultFailedTest() {
        final RuleResult result = RuleResult.failed("TestRule", "密码太短");
        assertFalse(result.isPassed());
        assertEquals("TestRule", result.getRuleName());
        assertEquals("密码太短", result.getMessage());
        assertEquals("TestRule: FAILED - 密码太短", result.toString());
    }

    // ==================== LengthRule ====================

    @Test
    @DisplayName("LengthRule 通过测试")
    void lengthRulePassedTest() {
        final LengthRule rule = new LengthRule(6, 20);
        assertTrue(rule.validate("123456").isPassed());
        assertTrue(rule.validate("12345678901234567890").isPassed());
    }

    @Test
    @DisplayName("LengthRule 长度过短测试")
    void lengthRuleTooShortTest() {
        final LengthRule rule = new LengthRule(6, 20);
        final RuleResult result = rule.validate("12345");
        assertFalse(result.isPassed());
        assertTrue(result.getMessage().contains("不能少于"));
    }

    @Test
    @DisplayName("LengthRule 长度过长测试")
    void lengthRuleTooLongTest() {
        final LengthRule rule = new LengthRule(1, 5);
        final RuleResult result = rule.validate("123456");
        assertFalse(result.isPassed());
        assertTrue(result.getMessage().contains("不能超过"));
    }

    @Test
    @DisplayName("LengthRule 空密码测试")
    void lengthRuleNullTest() {
        final LengthRule rule = new LengthRule();
        final RuleResult result = rule.validate(null);
        assertFalse(result.isPassed());
    }

    // ==================== CharGroupRule ====================

    @Test
    @DisplayName("CharGroupRule 通过测试")
    void charGroupRulePassedTest() {
        final CharGroupRule rule = new CharGroupRule(2, 2, 2, 2);
        assertTrue(rule.validate("12abAB!@").isPassed());
    }

    @Test
    @DisplayName("CharGroupRule 缺少大写字母失败测试")
    void charGroupRuleFailedTest() {
        final CharGroupRule rule = new CharGroupRule(2, 2, 2, 2);
        final RuleResult result = rule.validate("abc123");
        assertFalse(result.isPassed());
        assertTrue(result.getMessage().contains("大写字母"));
    }

    // ==================== NoSequentialCharRule ====================

    @Test
    @DisplayName("NoSequentialCharRule 无序列密码通过测试")
    void noSequentialPassedTest() {
        final NoSequentialCharRule rule = new NoSequentialCharRule(4);
        assertTrue(rule.validate("a1b2c3d4").isPassed());
    }

    @Test
    @DisplayName("NoSequentialCharRule 数字序列失败测试")
    void noSequentialDigitTest() {
        final NoSequentialCharRule rule = new NoSequentialCharRule(4);
        assertFalse(rule.validate("abc12345").isPassed());
    }

    @Test
    @DisplayName("NoSequentialCharRule 字母序列失败测试")
    void noSequentialLetterTest() {
        final NoSequentialCharRule rule = new NoSequentialCharRule(4);
        assertFalse(rule.validate("abcd123").isPassed());
    }

    @Test
    @DisplayName("NoSequentialCharRule 默认配置通过测试")
    void noSequentialDefaultTest() {
        final NoSequentialCharRule rule = new NoSequentialCharRule();
        assertTrue(rule.validate("abc123").isPassed());
    }

    // ==================== PasswordPolicy ====================

    @Test
    @DisplayName("PasswordPolicy 全部通过测试")
    void policyAllPassedTest() {
        final PasswordPolicy policy = new PasswordPolicy(
                Collections.singletonList(new LengthRule(3, 20)));
        final List<RuleResult> failures = policy.validateAll("test123");
        assertTrue(failures.isEmpty());
        assertTrue(policy.isPassed("test123"));
    }

    @Test
    @DisplayName("PasswordPolicy 全量校验收集所有失败")
    void policyValidateAllTest() {
        final PasswordPolicy policy = new PasswordPolicy(List.of(
                new LengthRule(10, 20),
                new CharGroupRule(2, 0, 0, 0)
        ));
        final List<RuleResult> failures = policy.validateAll("abc");
        assertEquals(2, failures.size());
    }

    @Test
    @DisplayName("PasswordPolicy 作为 PasswordRule 使用")
    void policyValidateAsRuleTest() {
        final PasswordPolicy policy = new PasswordPolicy(
                Collections.singletonList(new LengthRule(3, 20)));
        final RuleResult result = policy.validate("ab");
        assertFalse(result.isPassed());
    }

    // ==================== PasswordChain ====================

    @Test
    @DisplayName("PasswordChain 第一个规则失败即短路")
    void chainFirstFailTest() {
        final PasswordChain chain = PasswordChain.of(List.of(
                new LengthRule(10, 20),
                new CharGroupRule(2, 0, 0, 0)
        ));
        final RuleResult result = chain.validate("abc");
        assertFalse(result.isPassed());
        assertTrue(result.getMessage().contains("不能少于"));
    }

    @Test
    @DisplayName("PasswordChain 全部通过测试")
    void chainAllPassedTest() {
        final PasswordChain chain = PasswordChain.of(List.of(
                new LengthRule(3, 20),
                new CharGroupRule(1, 0, 0, 0)
        ));
        assertTrue(chain.isPassed("abc1"));
    }

    @Test
    @DisplayName("PasswordChain 通过时返回 PasswordChain 名称")
    void chainPassedResultTest() {
        final PasswordChain chain = PasswordChain.of(
                Collections.singletonList(new LengthRule(1, 20)));
        final RuleResult result = chain.validate("abc");
        assertTrue(result.isPassed());
        assertEquals("PasswordChain", result.getRuleName());
    }

    // ==================== PasswordPolicyBuilder ====================

    @Test
    @DisplayName("PasswordPolicyBuilder 最小长度测试")
    void builderMinLengthTest() {
        final PasswordPolicy policy = PasswordPolicy.builder()
                .minLength(6)
                .build();
        assertFalse(policy.isPassed("12345"));
        assertTrue(policy.isPassed("123456"));
    }

    @Test
    @DisplayName("PasswordPolicyBuilder 企业级强密码策略测试")
    void builderCharGroupTest() {
        final PasswordPolicy policy = PasswordPolicy.builder()
                .minLength(8)
                .requireDigit(1)
                .requireLowercase(1)
                .requireUppercase(1)
                .requireSpecial(1)
                .build();
        assertTrue(policy.isPassed("Abc123!@#"));
        assertFalse(policy.isPassed("abcdefgh"));
    }

    @Test
    @DisplayName("PasswordPolicyBuilder 禁止序列字符测试")
    void builderNoSequentialTest() {
        final PasswordPolicy policy = PasswordPolicy.builder()
                .minLength(4)
                .noSequential(4)
                .build();
        assertTrue(policy.isPassed("a1b2c3"));
        assertFalse(policy.isPassed("abcd1234"));
    }

    @Test
    @DisplayName("PasswordPolicyBuilder 空策略任意密码通过")
    void builderEmptyTest() {
        final PasswordPolicy policy = PasswordPolicy.builder().build();
        assertTrue(policy.isPassed("anything"));
    }
}
