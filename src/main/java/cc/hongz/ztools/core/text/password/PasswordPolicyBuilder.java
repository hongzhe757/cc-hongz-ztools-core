package cc.hongz.ztools.core.text.password;

import cc.hongz.ztools.core.text.password.rule.CharGroupRule;
import cc.hongz.ztools.core.text.password.rule.LengthRule;
import cc.hongz.ztools.core.text.password.rule.NoCommonPasswordRule;
import cc.hongz.ztools.core.text.password.rule.NoConsecutiveCharRule;
import cc.hongz.ztools.core.text.password.rule.NoDatePatternRule;
import cc.hongz.ztools.core.text.password.rule.NoKeyboardPatternRule;
import cc.hongz.ztools.core.text.password.rule.NoRepeatedPatternRule;
import cc.hongz.ztools.core.text.password.rule.NoSequentialCharRule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 密码策略流式构建器（构建器模式）.
 * 提供链式调用 API，以自然语言风格构建针对不同业务场景的密码策略.
 *
 * <pre>
 *     // 纯数字密码策略
 *     PasswordPolicy numericOnly = PasswordPolicy.builder()
 *         .minLength(6)
 *         .noSequential(4)
 *         .build();
 *
 *     // 企业级强密码策略
 *     PasswordPolicy enterprise = PasswordPolicy.builder()
 *         .minLength(8)
 *         .requireUppercase(1)
 *         .requireLowercase(1)
 *         .requireDigit(1)
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
 * @author hongz
 * @since 5.8.0
 */
public final class PasswordPolicyBuilder {

	/**
	 * 规则列表.
	 */
	private final List<PasswordRule> rules = new ArrayList<>();

	/**
	 * 构造构建器.
	 */
	PasswordPolicyBuilder() {
	}

	// ==================== 长度规则 ====================

	/**
	 * 设置密码最小长度.
	 *
	 * @param minLength 最小长度
	 * @return this
	 */
	public PasswordPolicyBuilder minLength(final int minLength) {
		rules.add(new LengthRule(minLength, LengthRule.DEFAULT_MAX_LENGTH)); return this;
	}

	/**
	 * 设置密码最大长度.
	 *
	 * @param maxLength 最大长度
	 * @return this
	 */
	public PasswordPolicyBuilder maxLength(final int maxLength) {
		rules.add(new LengthRule(LengthRule.DEFAULT_MIN_LENGTH, maxLength)); return this;
	}

	/**
	 * 设置密码长度范围.
	 *
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @return this
	 */
	public PasswordPolicyBuilder length(final int minLength, final int maxLength) {
		rules.add(new LengthRule(minLength, maxLength)); return this;
	}

	// ==================== 字符组规则 ====================

	/**
	 * 要求至少包含指定数量的数字.
	 *
	 * @param count 最少个数
	 * @return this
	 */
	public PasswordPolicyBuilder requireDigit(final int count) {
		rules.add(new CharGroupRule(count, 0, 0, 0)); return this;
	}

	/**
	 * 要求至少包含指定数量的小写字母.
	 *
	 * @param count 最少个数
	 * @return this
	 */
	public PasswordPolicyBuilder requireLowercase(final int count) {
		rules.add(new CharGroupRule(0, count, 0, 0)); return this;
	}

	/**
	 * 要求至少包含指定数量的大写字母.
	 *
	 * @param count 最少个数
	 * @return this
	 */
	public PasswordPolicyBuilder requireUppercase(final int count) {
		rules.add(new CharGroupRule(0, 0, count, 0)); return this;
	}

	/**
	 * 要求至少包含指定数量的特殊字符.
	 *
	 * @param count 最少个数
	 * @return this
	 */
	public PasswordPolicyBuilder requireSpecial(final int count) {
		rules.add(new CharGroupRule(0, 0, 0, count)); return this;
	}

	/**
	 * 设置字符组要求.
	 *
	 * @param minDigit     最少数字个数
	 * @param minLowercase 最少小写字母个数
	 * @param minUppercase 最少大写字母个数
	 * @param minSpecial   最少特殊字符个数
	 * @return this
	 */
	public PasswordPolicyBuilder charGroup(final int minDigit, final int minLowercase, final int minUppercase, final int minSpecial) {
		rules.add(new CharGroupRule(minDigit, minLowercase, minUppercase, minSpecial)); return this;
	}

	// ==================== 禁止规则 ====================

	/**
	 * 禁止连续相同字符.
	 *
	 * @param maxConsecutive 允许的最大连续相同字符数
	 * @return this
	 */
	public PasswordPolicyBuilder noConsecutive(final int maxConsecutive) {
		rules.add(new NoConsecutiveCharRule(maxConsecutive)); return this;
	}

	/**
	 * 禁止序列字符（如 abcde、12345）.
	 *
	 * @param minSeqLength 触发告警的最小连续序列长度
	 * @return this
	 */
	public PasswordPolicyBuilder noSequential(final int minSeqLength) {
		rules.add(new NoSequentialCharRule(minSeqLength)); return this;
	}

	/**
	 * 禁止键盘横向连续模式（如 qwerty、asdfg）.
	 * 使用默认模式长度 4.
	 *
	 * @return this
	 */
	public PasswordPolicyBuilder noKeyboardPattern() {
		rules.add(new NoKeyboardPatternRule()); return this;
	}

	/**
	 * 禁止键盘横向连续模式.
	 *
	 * @param minPatternLength 触发告警的最小模式长度
	 * @return this
	 */
	public PasswordPolicyBuilder noKeyboardPattern(final int minPatternLength) {
		rules.add(new NoKeyboardPatternRule(minPatternLength)); return this;
	}

	/**
	 * 禁止重复模式（如 abcabc、ababab）.
	 * 使用默认最小段数 2.
	 *
	 * @return this
	 */
	public PasswordPolicyBuilder noRepeatedPattern() {
		rules.add(new NoRepeatedPatternRule()); return this;
	}

	/**
	 * 禁止重复模式.
	 *
	 * @param minSegments 触发告警的最小重复段数
	 * @return this
	 */
	public PasswordPolicyBuilder noRepeatedPattern(final int minSegments) {
		rules.add(new NoRepeatedPatternRule(minSegments)); return this;
	}

	/**
	 * 禁止日期格式密码.
	 * 使用默认年份范围 1950-2050.
	 *
	 * @return this
	 */
	public PasswordPolicyBuilder noDatePattern() {
		rules.add(new NoDatePatternRule()); return this;
	}

	/**
	 * 禁止日期格式密码.
	 *
	 * @param minYear 最小有效年份
	 * @param maxYear 最大有效年份
	 * @return this
	 */
	public PasswordPolicyBuilder noDatePattern(final int minYear, final int maxYear) {
		rules.add(new NoDatePatternRule(minYear, maxYear)); return this;
	}

	/**
	 * 禁止常见弱密码.
	 * 使用默认弱密码字典.
	 *
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPassword() {
		rules.add(new NoCommonPasswordRule()); return this;
	}

	/**
	 * 禁止常见弱密码.
	 *
	 * @param dictionary 自定义弱密码字典
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPassword(final String[] dictionary) {
		rules.add(new NoCommonPasswordRule(dictionary)); return this;
	}

	/**
	 * 禁止常见弱密码，从文件中加载词典.
	 * 文件每行一个弱密码，# 开头为注释.
	 *
	 * @param file 弱密码字典文件
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPassword(final File file) {
		rules.add(new NoCommonPasswordRule(file)); return this;
	}

	/**
	 * 禁止常见弱密码，从文件路径加载词典.
	 *
	 * @param filePath 弱密码字典文件路径
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPassword(final String filePath) {
		rules.add(new NoCommonPasswordRule(filePath)); return this;
	}

	/**
	 * 禁止常见弱密码，从目录加载词典（扫描所有 .txt 文件）.
	 *
	 * @param dir 弱密码字典目录
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPasswordFromDirectory(final File dir) {
		rules.add(NoCommonPasswordRule.fromDirectory(dir)); return this;
	}

	/**
	 * 禁止常见弱密码，从目录路径加载词典（扫描所有 .txt 文件）.
	 *
	 * @param dirPath 弱密码字典目录路径
	 * @return this
	 */
	public PasswordPolicyBuilder noCommonPasswordFromDirectory(final String dirPath) {
		rules.add(NoCommonPasswordRule.fromDirectory(dirPath)); return this;
	}

	/**
	 * 直接添加自定义规则.
	 *
	 * @param rule 自定义规则
	 * @return this
	 */
	public PasswordPolicyBuilder addRule(final PasswordRule rule) {
		rules.add(rule); return this;
	}

	/**
	 * 构建密码策略.
	 *
	 * @return PasswordPolicy
	 */
	public PasswordPolicy build() {
		return new PasswordPolicy(rules);
	}
}
