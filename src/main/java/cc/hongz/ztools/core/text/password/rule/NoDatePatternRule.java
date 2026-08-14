package cc.hongz.ztools.core.text.password.rule;

import cc.hongz.ztools.core.text.password.PasswordRule;
import cc.hongz.ztools.core.text.password.RuleResult;

/**
 * 禁止日期格式密码规则.
 * 检测 6 位或 8 位纯数字是否为有效日期格式（如 19881010、881010）.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class NoDatePatternRule implements PasswordRule {

    /**
     * 规则名称.
     */
    private static final String RULE_NAME = "NoDatePatternRule";
    private static final int SHORT_DATE_LENGTH = 6;
    private static final int LONG_DATE_LENGTH = 8;
    private static final int MONTHS_PER_YEAR = 12;
    private static final int MAX_DAYS_PER_MONTH = 31;

    /**
     * 数字长度查找表.
     */
    private static final int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999,
            9999999, 99999999, 999999999, Integer.MAX_VALUE};

    /**
     * 最小有效年份.
     */
    public static final int DEFAULT_MIN_YEAR = 1950;

    /**
     * 最大有效年份.
     */
    public static final int DEFAULT_MAX_YEAR = 2050;

    /**
     * 最小有效年份.
     */
    private final int minYear;

    /**
     * 最大有效年份.
     */
    private final int maxYear;

    /**
     * 构造规则，使用默认值.
     */
    public NoDatePatternRule() {
        this(DEFAULT_MIN_YEAR, DEFAULT_MAX_YEAR);
    }

    /**
     * 构造规则.
     *
     * @param minYear 最小有效年份
     * @param maxYear 最大有效年份
     */
    public NoDatePatternRule(final int minYear, final int maxYear) {
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    @Override
    public RuleResult validate(final String password) {
        if (!isAllDigit(password)) {
            return RuleResult.passed(RULE_NAME);
        }
        final int len = password.length();
        if (len != SHORT_DATE_LENGTH && len != LONG_DATE_LENGTH) {
            return RuleResult.passed(RULE_NAME);
        }
        int year = 0;
        if (len == LONG_DATE_LENGTH || len == SHORT_DATE_LENGTH) {
            year = Integer.parseInt(password.substring(0, len - 4));
        }
        final int size = sizeOfInt(year);
        final int month = Integer.parseInt(password.substring(size, size + 2));
        final int day = Integer.parseInt(password.substring(size + 2, len));
        if (year >= minYear && year < maxYear && month >= 1 && month <= MONTHS_PER_YEAR
                && day >= 1 && day <= MAX_DAYS_PER_MONTH) {
            return RuleResult.failed(RULE_NAME, "密码不能为日期格式");
        }
        return RuleResult.passed(RULE_NAME);
    }

    /**
     * 检测字符串是否全为数字.
     *
     * @param s 字符串
     * @return 是否全为数字
     */
    private boolean isAllDigit(final String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算整数位数.
     *
     * @param x 整数
     * @return 位数
     */
    private int sizeOfInt(final int x) {
        for (int i = 0; ; i++) {
            if (x <= SIZE_TABLE[i]) {
                return i + 1;
            }
        }
    }
}
