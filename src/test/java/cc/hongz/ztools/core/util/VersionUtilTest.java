package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.comparator.VersionComparator;
import cc.hongz.ztools.core.exceptions.UtilException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VersionUtilTest {

    @Test
    @DisplayName("isGreaterThan - 当前版本大于待比较版本")
    void isGreaterThan() {
        String currentVersion = " 1.0.2";
        assertTrue(VersionUtil.isGreaterThan(currentVersion, "1.0.1"));
        assertTrue(VersionUtil.isGreaterThan(currentVersion, "1"));
        assertFalse(VersionUtil.isGreaterThan(currentVersion, "1.1"));
    }

    @Test
    @DisplayName("isGreaterThanOrEqual - 当前版本大于等于待比较版本")
    void isGreaterThanOrEqual() {
        String currentVersion = "1.0.2 ";
        assertTrue(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.0.1"));
        assertTrue(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.0.2"));
        assertFalse(VersionUtil.isGreaterThanOrEqual(currentVersion, "1.1"));
    }

    @Test
    @DisplayName("isLessThan - 当前版本小于待比较版本")
    void isLessThan() {
        String currentVersion = "1.0.2";
        assertTrue(VersionUtil.isLessThan(currentVersion, "1.0.3"));
        assertFalse(VersionUtil.isLessThan(currentVersion, "1"));
        assertTrue(VersionUtil.isLessThan(currentVersion, "1.1"));
        assertFalse(VersionUtil.isLessThan(currentVersion, "1.0.2"));
    }

    @Test
    @DisplayName("isLessThanOrEqual - 当前版本小于等于待比较版本")
    void isLessThanOrEqual() {
        String currentVersion = "1.0.2";
        assertTrue(VersionUtil.isLessThanOrEqual(currentVersion, "1.0.2"));
        assertFalse(VersionUtil.isLessThanOrEqual(currentVersion, "1.0.1"));
        assertTrue(VersionUtil.isLessThanOrEqual(currentVersion, "1.1"));
    }

    @Test
    @DisplayName("matchEl - 版本表达式匹配")
    void matchEl() {
        String currentVersion = "1.0.2";
        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.1;1.0.2"));
        assertFalse(VersionUtil.matchEl(currentVersion, "1.0.1;1.0.3"));
        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9;1.0.1-1.0.2"));
        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9;1.0.1-1.0.3"));

        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.9,1.0.1-1.0.3", ","));
    }

    @Test
    @DisplayName("matchEl - 非法分隔符抛出异常")
    void matchElExceptionWhenVersionDelimiterIllegal() {
        List<String> illegalDelimiters = ListUtil.of("-", ">", ">=", "<",
                "<=", "≥", "≤", null, "", " ");

        for (String illegalDelimiter : illegalDelimiters) {
            assertThrows(UtilException.class, () -> {
                String currentVersion = "1.0.2";
                VersionUtil.matchEl(currentVersion,
                        "1.0.1;1.0.2", illegalDelimiter);
            });
        }
    }

    @Test
    @DisplayName("anyMatch - 匹配任意版本")
    void anyMatch() {
        String currentVersion = "1.0.2";
        assertTrue(VersionUtil.anyMatch(currentVersion,
                ListUtil.of("1.0.1", "1.0.3", "1.0.2")));
        assertTrue(VersionUtil.anyMatch(currentVersion,
                "1.0.1", "1.0.2"));
    }

    @Test
    @DisplayName("anyMatch - 无匹配版本")
    void anyMatchNone() {
        String currentVersion = "1.0.2";
        assertFalse(VersionUtil.anyMatch(currentVersion,
                "1.0.1", "1.0.3"));
    }

    @Test
    @DisplayName("matchEl - 版本范围边界情况（左边界空、右边界空、双边空）")
    void matchElRangeBoundaryCases() {
        String currentVersion = "1.0.2";

        // 测试左边界为空的情况: "-1.0.3" 应该匹配小于等于1.0.3的版本
        assertTrue(VersionUtil.matchEl(currentVersion, "-1.0.3"));
        assertTrue(VersionUtil.matchEl(currentVersion, "-1.0.2"));
        assertFalse(VersionUtil.matchEl(currentVersion, "-1.0.0"));

        // 测试右边界为空的情况: "1.0.0-" 应该匹配大于等于1.0.0的版本
        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.0-"));
        assertTrue(VersionUtil.matchEl(currentVersion, "1.0.2-"));
        assertFalse(VersionUtil.matchEl(currentVersion, "1.0.3-"));

        // 测试双边为空的情况: "-" 应该匹配所有版本
        assertTrue(VersionUtil.matchEl(currentVersion, "-"));
        assertTrue(VersionUtil.matchEl("0.0.1", "-"));
        assertTrue(VersionUtil.matchEl("999.999.999", "-"));
    }

    @Test
    @DisplayName("compareVersion - null版本比较和isGreaterThan兼容性")
    void issueIJNFQZTest() {
        assertEquals(1, VersionComparator.INSTANCE.compare("1.0", null));
        assertEquals(1, StrUtil.compareVersion("1.0", null));
        assertTrue(VersionUtil.isGreaterThan("1.0", null));
    }
}
