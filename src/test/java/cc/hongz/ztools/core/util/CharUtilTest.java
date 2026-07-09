package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link CharUtil} 单元测试.
 * <p>
 * 覆盖字符比较与常量定义.
 *
 * @author hongzhe.qin
 */
class CharUtilTest {

    /**
     * 验证 {@link CharUtil#equals(char, char, boolean)} 区分大小写比较.
     */
    @Test
    void equalsCaseSensitive() {
        assertTrue(CharUtil.equals('a', 'a', false));
        assertFalse(CharUtil.equals('a', 'A', false));
    }

    /**
     * 验证 {@link CharUtil#equals(char, char, boolean)} 忽略大小写比较.
     */
    @Test
    void equalsCaseInsensitive() {
        assertTrue(CharUtil.equals('a', 'A', true));
        assertFalse(CharUtil.equals('a', 'b', true));
    }

    /**
     * 验证 {@link CharUtil#SPACE} 常量值为空格.
     */
    @Test
    void spaceConstant() {
        assertEquals(' ', CharUtil.SPACE);
    }
}
