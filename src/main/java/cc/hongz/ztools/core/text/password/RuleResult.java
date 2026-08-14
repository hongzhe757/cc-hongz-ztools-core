package cc.hongz.ztools.core.text.password;

/**
 * 单条规则的校验结果.
 *
 * @author hongz
 * @since 5.8.0
 */
public final class RuleResult {

    /**
     * 规则名称.
     */
    private final String ruleName;

    /**
     * 是否通过.
     */
    private final boolean passed;

    /**
     * 失败原因（通过时为空字符串）.
     */
    private final String message;

    /**
     * 构造校验结果.
     *
     * @param ruleName 规则名称
     * @param passed   是否通过
     * @param message  失败原因
     */
    private RuleResult(final String ruleName, final boolean passed, final String message) {
        this.ruleName = ruleName; this.passed = passed; this.message = message;
    }

    /**
     * 创建通过的校验结果.
     *
     * @param ruleName 规则名称
     * @return 通过的结果
     */
    public static RuleResult passed(final String ruleName) {
        return new RuleResult(ruleName, true, "");
    }

    /**
     * 创建未通过的校验结果.
     *
     * @param ruleName 规则名称
     * @param message  失败原因
     * @return 未通过的结果
     */
    public static RuleResult failed(final String ruleName, final String message) {
        return new RuleResult(ruleName, false, message);
    }

    /**
     * 获取规则名称.
     *
     * @return 规则名称
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * 是否通过校验.
     *
     * @return true 表示通过
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * 获取失败原因.
     *
     * @return 失败原因，通过时为空字符串
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (passed) {
            return ruleName + ": PASSED";
        }
        return ruleName + ": FAILED - " + message;
    }
}
