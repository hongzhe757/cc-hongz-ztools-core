package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link CreditCodeUtil} 单元测试.
 *
 * @author looly
 */
@DisplayName("CreditCodeUtil 统一社会信用代码")
class CreditCodeUtilTest {

    @Test
    @DisplayName("正则校验合法信用代码")
    void isCreditCodeSimple() {
        String testCreditCode = "91310115591693856A";
        assertTrue(CreditCodeUtil.isCreditCodeSimple(testCreditCode));
    }

    @Test
    @DisplayName("校验码校验合法信用代码")
    void isCreditCode() {
        String testCreditCode = "91310110666007217T";
        assertTrue(CreditCodeUtil.isCreditCode(testCreditCode));
    }

    @Test
    @DisplayName("早期试点地区不合规信用代码应校验失败")
    void isCreditCodeNotCompliant() {
        // 由于早期部分试点地区推行 法人和其他组织统一社会信用代码 较早，会存在部分代码不符合国家标准的情况。
        // 见：https://github.com/bluesky335/IDCheck
        String testCreditCode = "91350211M00013FA1N";
        assertFalse(CreditCodeUtil.isCreditCode(testCreditCode));
    }

    @Test
    @DisplayName("随机生成的信用代码可通过校验")
    void randomCreditCode() {
        final String s = CreditCodeUtil.randomCreditCode();
        assertTrue(CreditCodeUtil.isCreditCode(s));
    }
}
