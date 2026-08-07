package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.text.password.PasswordChain;
import cc.hongz.ztools.core.text.password.PasswordPolicy;
import cc.hongz.ztools.core.text.password.RuleResult;
import cc.hongz.ztools.core.text.password.rule.NoCommonPasswordRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link PasswordValidator} 单元测试.
 *
 * @author hongz
 * @since 5.8.0
 */
public class PasswordValidatorTest {

    // ==================== 组合模式：全量校验 ====================

    @Test
    @DisplayName("组合模式：强密码全量通过")
    public void compositeStrongPasswordTest() {
        final List<RuleResult> failures = PasswordValidator.validate("MyP@ssw0rd!2025");
        assertTrue(failures.isEmpty(), "强密码不应有任何违规: " + failures);
    }

    @Test
    @DisplayName("组合模式：弱密码返回多个违规项")
    public void compositeWeakPasswordTest() {
        final List<RuleResult> failures = PasswordValidator.validate("password");
        assertFalse(failures.isEmpty(), "弱密码应有违规项");
        // 默认策略包含键盘模式、重复模式、日期格式、常见弱密码检测
        assertTrue(failures.size() >= 1);
    }

    @Test
    @DisplayName("组合模式：常见弱密码被检测")
    public void compositeCommonPasswordTest() {
        final List<RuleResult> failures = PasswordValidator.validate("password");
        assertFalse(failures.isEmpty());
    }

    @Test
    @DisplayName("组合模式：重复模式被检测")
    public void compositeRepeatedPatternTest() {
        final List<RuleResult> failures = PasswordValidator.validate("abcabc");
        assertFalse(failures.isEmpty());
    }

    @Test
    @DisplayName("组合模式：日期格式被检测")
    public void compositeDatePatternTest() {
        final List<RuleResult> failures = PasswordValidator.validate("19881010");
        assertFalse(failures.isEmpty());
    }

    // ==================== 责任链模式：短路校验 ====================

    @Test
    @DisplayName("责任链模式：强密码通过")
    public void chainStrongPasswordTest() {
        final RuleResult result = PasswordValidator.validateChain("MyP@ssw0rd!2025");
        assertTrue(result.isPassed(), "强密码应通过: " + result.getMessage());
    }

    @Test
    @DisplayName("责任链模式：短密码第一个失败即返回")
    public void chainShortPasswordTest() {
        final RuleResult result = PasswordValidator.validateChain("123");
        assertFalse(result.isPassed(), "短密码应不通过");
    }

    @Test
    @DisplayName("责任链模式：常见弱密码第一个失败即返回")
    public void chainCommonPasswordTest() {
        final RuleResult result = PasswordValidator.validateChain("password");
        assertFalse(result.isPassed());
        assertEquals("NoCommonPasswordRule", result.getRuleName());
    }

    // ==================== isPassed ====================

    @Test
    @DisplayName("isPassed：强密码返回true")
    public void isPassedStrongTest() {
        assertTrue(PasswordValidator.isPassed("MyP@ssw0rd!2025"));
    }

    @Test
    @DisplayName("isPassed：弱密码返回false")
    public void isPassedWeakTest() {
        assertFalse(PasswordValidator.isPassed("123"));
    }

    // ==================== 构建器模式：自定义策略 ====================

    @Test
    @DisplayName("构建器：纯数字密码策略")
    public void builderNumericOnlyTest() {
        final PasswordPolicy policy = PasswordValidator.policyBuilder()
            .minLength(6)
            .noSequential(4)
            .build();
        assertTrue(policy.isPassed("147258"));
        assertFalse(policy.isPassed("12345"));
        assertFalse(policy.isPassed("123456"));
    }

    @Test
    @DisplayName("构建器：企业级强密码策略")
    public void builderEnterprisePolicyTest() {
        final PasswordPolicy policy = PasswordValidator.policyBuilder()
            .minLength(8)
            .requireDigit(1)
            .requireLowercase(1)
            .requireUppercase(1)
            .requireSpecial(1)
            .noConsecutive(3)
            .noSequential(4)
            .noKeyboardPattern()
            .noRepeatedPattern()
            .noDatePattern()
            .noCommonPassword()
            .build();
        assertTrue(policy.isPassed("MyP@ssw0rd!2025"));
        assertFalse(policy.isPassed("password"));
        assertFalse(policy.isPassed("12345678"));
    }

    @Test
    @DisplayName("构建器：最小长度约束")
    public void builderMinLengthTest() {
        final PasswordPolicy policy = PasswordValidator.policyBuilder()
            .minLength(8)
            .build();
        assertTrue(policy.isPassed("12345678"));
        assertFalse(policy.isPassed("1234567"));
    }

    // ==================== createDefaultPolicy / createDefaultChain ====================

    @Test
    @DisplayName("createDefaultPolicy：返回可用的策略")
    public void createDefaultPolicyTest() {
        final PasswordPolicy policy = PasswordValidator.createDefaultPolicy();
        assertTrue(policy.isPassed("MyP@ssw0rd!2025"));
        assertFalse(policy.isPassed("123"));
    }

    @Test
    @DisplayName("createDefaultChain：返回可用的责任链")
    public void createDefaultChainTest() {
        final PasswordChain chain = PasswordValidator.createDefaultChain();
        assertTrue(chain.isPassed("MyP@ssw0rd!2025"));
        assertFalse(chain.isPassed("123"));
    }

    // ==================== RuleResult ====================

    @Test
    @DisplayName("RuleResult：passed 工厂方法")
    public void ruleResultPassedTest() {
        final RuleResult result = RuleResult.passed("TestRule");
        assertTrue(result.isPassed());
        assertEquals("TestRule", result.getRuleName());
        assertTrue(result.getMessage().isEmpty());
    }

    @Test
    @DisplayName("RuleResult：failed 工厂方法")
    public void ruleResultFailedTest() {
        final RuleResult result = RuleResult.failed("TestRule", "密码太弱");
        assertFalse(result.isPassed());
        assertEquals("TestRule", result.getRuleName());
        assertEquals("密码太弱", result.getMessage());
    }

    @Test
    @DisplayName("RuleResult：toString")
    public void ruleResultToStringTest() {
        assertEquals("TestRule: PASSED", RuleResult.passed("TestRule").toString());
        assertEquals("TestRule: FAILED - 密码太弱", RuleResult.failed("TestRule", "密码太弱").toString());
    }

    // ==================== PasswordPolicy 组合 ====================

    @Test
    @DisplayName("PasswordPolicy：validate 作为组合规则")
    public void policyAsRuleTest() {
        final PasswordPolicy policy = PasswordValidator.createDefaultPolicy();
        final RuleResult result = policy.validate("MyP@ssw0rd!2025");
        assertTrue(result.isPassed());
    }

    // ==================== PasswordChain 责任链 ====================

    @Test
    @DisplayName("PasswordChain：of 静态工厂")
    public void chainOfTest() {
        final PasswordChain chain = PasswordChain.of(PasswordValidator.createDefaultPolicy().getRules());
        assertTrue(chain.isPassed("MyP@ssw0rd!2025"));
    }

    // ==================== NoCommonPasswordRule 文件/目录加载 ====================

    @Test
    @DisplayName("文件加载：从文件加载弱密码并检测")
    public void noCommonPasswordFromFileTest() {
        final File file = new File("src/test/resources/weakpasswords/common_passwords.txt");
        final NoCommonPasswordRule rule = new NoCommonPasswordRule(file);
        assertFalse(rule.validate("123456").isPassed(), "123456应在文件中");
        assertFalse(rule.validate("password").isPassed(), "password应在文件中");
        assertFalse(rule.validate("admin123").isPassed(), "admin123应在文件中");
        assertFalse(rule.validate("qwerty").isPassed(), "qwerty应在文件中");
        assertTrue(rule.validate("MyStr0ng!P@ss").isPassed(), "强密码应通过");
    }

    @Test
    @DisplayName("文件加载：注释行和空行被忽略")
    public void noCommonPasswordSkipCommentsTest() {
        final File file = new File("src/test/resources/weakpasswords/common_passwords.txt");
        final NoCommonPasswordRule rule = new NoCommonPasswordRule(file);
        // # 开头的行为注释，不会被加载为弱密码
        assertTrue(rule.validate("#").isPassed(), "# 是注释前缀，不应被当作密码");
    }

    @Test
    @DisplayName("目录加载：从目录加载多文件词典")
    public void noCommonPasswordFromDirectoryTest() {
        final File dir = new File("src/test/resources/weakpasswords/dict");
        final NoCommonPasswordRule rule = NoCommonPasswordRule.fromDirectory(dir);
        // dict_part1.txt 中的密码
        assertFalse(rule.validate("111111").isPassed());
        assertFalse(rule.validate("222222").isPassed());
        assertFalse(rule.validate("abc123").isPassed());
        assertFalse(rule.validate("test123").isPassed());
        // dict_part2.txt 中的密码
        assertFalse(rule.validate("hello123").isPassed());
        assertFalse(rule.validate("world123").isPassed());
        assertFalse(rule.validate("changeme").isPassed());
        // 强密码通过
        assertTrue(rule.validate("MyStr0ng!P@ss").isPassed());
    }

    @Test
    @DisplayName("目录加载：字符串路径方式")
    public void noCommonPasswordFromDirectoryPathTest() {
        final NoCommonPasswordRule rule = NoCommonPasswordRule.fromDirectory(
            "src/test/resources/weakpasswords/dict");
        assertFalse(rule.validate("111111").isPassed());
        assertFalse(rule.validate("hello123").isPassed());
    }

    @Test
    @DisplayName("文件加载：文件不存在抛异常")
    public void noCommonPasswordFileNotFoundTest() {
        assertThrows(IllegalArgumentException.class,
            () -> new NoCommonPasswordRule(new File("nonexistent_file.txt")));
    }

    @Test
    @DisplayName("目录加载：目录不存在抛异常")
    public void noCommonPasswordDirectoryNotFoundTest() {
        assertThrows(IllegalArgumentException.class,
            () -> NoCommonPasswordRule.fromDirectory(new File("nonexistent_dir")));
    }

    @Test
    @DisplayName("构建器：从文件加载弱密码")
    public void builderNoCommonPasswordFromFileTest() {
        final PasswordPolicy policy = PasswordValidator.policyBuilder()
            .minLength(6)
            .noCommonPassword(new File("src/test/resources/weakpasswords/common_passwords.txt"))
            .build();
        assertFalse(policy.isPassed("123456"));
        assertFalse(policy.isPassed("qwerty"));
        assertTrue(policy.isPassed("MyStr0ng!P@ss"));
    }

    @Test
    @DisplayName("构建器：从目录加载弱密码")
    public void builderNoCommonPasswordFromDirectoryTest() {
        final PasswordPolicy policy = PasswordValidator.policyBuilder()
            .minLength(6)
            .noCommonPasswordFromDirectory(new File("src/test/resources/weakpasswords/dict"))
            .build();
        assertFalse(policy.isPassed("111111"));
        assertFalse(policy.isPassed("changeme"));
        assertTrue(policy.isPassed("MyStr0ng!P@ss"));
    }

    // ==================== 与 PasswdStrength 对比 ====================

    @Test
    @DisplayName("新旧API对比：PasswdStrength检查强密码")
    public void legacyStrongPasswordTest() {
        // 旧 API 评分
        final int score = PasswdStrength.check("2hAj5#mne-ix.86H");
        assertEquals(13, score);
        // 新 API 验证
        assertTrue(PasswordValidator.isPassed("2hAj5#mne-ix.86H"));
    }

    @Test
    @DisplayName("新旧API对比：PasswdStrength检查弱密码")
    public void legacyWeakPasswordTest() {
        // 旧 API：纯数字弱密码得分为 0
        assertEquals(0, PasswdStrength.check("123456"));
        assertEquals(0, PasswdStrength.check("password"));
    }
}
