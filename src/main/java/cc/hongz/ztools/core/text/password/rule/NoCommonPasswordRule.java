package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 禁止常见弱密码规则.
 * 检测密码是否命中弱密码字典或包含字典中的弱密码片段.
 *
 * <p>支持多种词典来源：
 * <ul>
 *   <li>默认内置词典（常用弱密码）</li>
 *   <li>自定义字符串数组</li>
 *   <li>从单个文件加载（每行一个弱密码）</li>
 *   <li>从目录加载（扫描目录下所有 .txt 文件）</li>
 * </ul>
 *
 * <p>文件格式说明：
 * <ul>
 *   <li>每行一个弱密码</li>
 *   <li>以 # 开头的行为注释，会被忽略</li>
 *   <li>空行和纯空白行会被忽略</li>
 *   <li>自动去除首尾空白</li>
 * </ul>
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoCommonPasswordRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "NoCommonPasswordRule";

    /**
     * 默认弱密码字典.
     */
    private static final String[] DEFAULT_DICTIONARY = {"password", "abc123", "iloveyou", "adobe123", "123123", "sunshine", "1314520", "a1b2c3", "123qwe", "qweasd", "admin", "passwd"};

    /**
     * 注释行前缀.
     */
    private static final String COMMENT_PREFIX = "#";

    /**
     * 文本文件扩展名.
     */
    private static final String TXT_EXTENSION = ".txt";

    /**
     * 弱密码集合.
     */
    private final Set<String> dictionary;

    /**
     * 构造规则，使用默认字典.
     */
    public NoCommonPasswordRule() {
        this(DEFAULT_DICTIONARY);
    }

    /**
     * 构造规则.
     *
     * @param dictionary 自定义弱密码字典
     */
    public NoCommonPasswordRule(final String[] dictionary) {
        this.dictionary = new HashSet<>(); for (final String s : dictionary) {
            this.dictionary.add(s.toLowerCase());
        }
    }

    /**
     * 从文件中加载弱密码词典.
     * 文件每行一个弱密码，# 开头为注释.
     *
     * @param file 弱密码字典文件
     */
    public NoCommonPasswordRule(final File file) {
        this.dictionary = new HashSet<>(); loadFromFile(file, this.dictionary);
    }

    /**
     * 从文件路径加载弱密码词典.
     *
     * @param filePath 弱密码字典文件路径
     */
    public NoCommonPasswordRule(final String filePath) {
        this(new File(filePath));
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 从目录中加载弱密码词典（扫描目录下所有 .txt 文件）.
     *
     * @param dir 弱密码字典目录
     * @return NoCommonPasswordRule
     */
    public static NoCommonPasswordRule fromDirectory(final File dir) {
        if (dir == null || !dir.isDirectory()) {
            throw new IllegalArgumentException("目录不存在或不是有效目录: " + dir);
        } final Set<String> dict = new HashSet<>(); final File[] files = dir.listFiles(
            (f, name) -> name.toLowerCase().endsWith(TXT_EXTENSION)); if (files != null) {
            for (final File file : files) {
                loadFromFile(file, dict);
            }
        } return new NoCommonPasswordRule(dict);
    }

    /**
     * 从目录路径加载弱密码词典（扫描目录下所有 .txt 文件）.
     *
     * @param dirPath 弱密码字典目录路径
     * @return NoCommonPasswordRule
     */
    public static NoCommonPasswordRule fromDirectory(final String dirPath) {
        return fromDirectory(new File(dirPath));
    }

    /**
     * 内部构造器：直接传入 Set.
     *
     * @param dictionary 弱密码集合
     */
    private NoCommonPasswordRule(final Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    // ==================== 文件加载 ====================

    /**
     * 从文件中加载弱密码到集合中.
     *
     * @param file 弱密码文件
     * @param dict 目标集合
     */
    private static void loadFromFile(final File file, final Set<String> dict) {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("弱密码字典文件不存在: " + file);
        } try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; while ((line = reader.readLine()) != null) {
                final String trimmed = line.trim(); if (trimmed.isEmpty() || trimmed.startsWith(COMMENT_PREFIX)) {
                    continue;
                } dict.add(trimmed.toLowerCase());
            }
        } catch (final IOException e) {
            throw new UncheckedIOException("读取弱密码字典文件失败: " + file.getAbsolutePath(), e);
        }
    }

    // ==================== 校验 ====================

    @Override
    public RuleResult validate(final String password) {
        final String lower = password.toLowerCase(); for (final String weak : dictionary) {
            if (lower.equals(weak) || weak.contains(lower)) {
                return RuleResult.failed(RULE_NAME, "密码为常见弱密码");
            }
        } return RuleResult.passed(RULE_NAME);
    }
}
