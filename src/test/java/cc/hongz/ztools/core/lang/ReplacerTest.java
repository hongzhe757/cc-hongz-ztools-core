package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Replacer} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Replacer 替换器")
class ReplacerTest {

    /**
     * 验证 {@link Replacer#replace(Object)} 通过 lambda 完成替换.
     */
    @Test
    @DisplayName("replace lambda 替换")
    void replaceByLambda() {
        final Replacer<String> replacer = s -> s.toUpperCase();
        assertEquals("HELLO", replacer.replace("hello"));
    }

    /**
     * 验证 {@link Replacer#replace(Object)} 返回原对象（恒等替换）.
     */
    @Test
    @DisplayName("replace 恒等替换")
    void replaceIdentity() {
        final Replacer<Integer> replacer = value -> value;
        assertEquals(Integer.valueOf(1), replacer.replace(1));
    }
}
