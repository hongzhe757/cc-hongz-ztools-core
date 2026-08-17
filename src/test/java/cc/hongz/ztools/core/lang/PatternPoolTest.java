package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link PatternPool} 单元测试.
 * <p>
 * 覆盖常用 Pattern 常量匹配与池的缓存、移除、清空行为.
 *
 * @author hongzhe.qin
 */
@DisplayName("PatternPool 正则池")
class PatternPoolTest {

    /**
     * 验证 {@link PatternPool#EMAIL}、{@link PatternPool#MOBILE}、{@link PatternPool#URL_HTTP}、{@link PatternPool#UUID}.
     */
    @Test
    @DisplayName("常用 Pattern 常量")
    void commonPatterns() {
        assertTrue(PatternPool.EMAIL.matcher("test@example.com").matches());
        assertFalse(PatternPool.EMAIL.matcher("not-an-email").matches());
        assertTrue(PatternPool.MOBILE.matcher("13900221432").matches());
        assertTrue(PatternPool.URL_HTTP.matcher("https://example.com").matches());
        assertTrue(PatternPool.UUID.matcher("123e4567-e89b-12d3-a456-426614174000").matches());
    }

    /**
     * 验证 {@link PatternPool#get(String)} 命中缓存时返回同一实例.
     */
    @Test
    @DisplayName("get 命中缓存")
    void getCache() {
        final Pattern p1 = PatternPool.get("\\d+");
        final Pattern p2 = PatternPool.get("\\d+");
        assertSame(p1, p2);
    }

    /**
     * 验证 {@link PatternPool#get(String, int)} 按 flag 区分缓存.
     */
    @Test
    @DisplayName("get 按 flag 区分")
    void getWithFlag() {
        final Pattern p1 = PatternPool.get("abc", 0);
        final Pattern p2 = PatternPool.get("abc", Pattern.CASE_INSENSITIVE);
        assertNotSame(p1, p2);
        assertSame(p1, PatternPool.get("abc", 0));
    }

    /**
     * 验证 {@link PatternPool#remove(String, int)} 与 {@link PatternPool#clear()}.
     */
    @Test
    @DisplayName("移除与清空")
    void removeAndClear() {
        final String regex = "\\w+";
        final Pattern p1 = PatternPool.get(regex);
        PatternPool.remove(regex, 0);
        final Pattern p2 = PatternPool.get(regex);
        assertNotSame(p1, p2);

        PatternPool.clear();
    }
}
