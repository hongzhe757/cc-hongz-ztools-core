package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Matcher} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Matcher 匹配器")
class MatcherTest {

    @Test
    @DisplayName("match 匹配条件")
    void matchTest() {
        final Matcher<String> matcher = s -> s != null && s.startsWith("h");
        assertTrue(matcher.match("hutool"));
        assertFalse(matcher.match("world"));
        assertFalse(matcher.match(null));
    }
}
