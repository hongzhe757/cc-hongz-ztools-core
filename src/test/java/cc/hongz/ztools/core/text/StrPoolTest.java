package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link StrPool} 单元测试.
 * @author Looly
 *
 */
public class StrPoolTest {

    @DisplayName("字符串常量校验")
    @Test
    public void constantsTest() {
        assertEquals("\t", StrPool.TAB);
        assertEquals(".", StrPool.DOT);
        assertEquals("..", StrPool.DOUBLE_DOT);
        assertEquals("/", StrPool.SLASH);
        assertEquals("\\", StrPool.BACKSLASH);
        assertEquals("\r", StrPool.CR);
        assertEquals("\n", StrPool.LF);
        assertEquals("\r\n", StrPool.CRLF);
        assertEquals("_", StrPool.UNDERLINE);
        assertEquals("-", StrPool.DASHED);
        assertEquals(",", StrPool.COMMA);
        assertEquals("{", StrPool.DELIM_START);
        assertEquals("}", StrPool.DELIM_END);
        assertEquals("{}", StrPool.EMPTY_JSON);
    }
}
