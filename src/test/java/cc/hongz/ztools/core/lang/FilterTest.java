package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Filter} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Filter 过滤器")
class FilterTest {

    @Test
    @DisplayName("accept 过滤条件")
    void acceptTest() {
        final Filter<String> filter = s -> s != null && s.contains("h");
        assertTrue(filter.accept("hutool"));
        assertFalse(filter.accept("world"));
        assertFalse(filter.accept(null));
    }
}
