package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link Editor} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Editor 编辑器")
class EditorTest {

    @Test
    void editModifyTest() {
        // 修改对象
        final Editor<String> editor = String::toUpperCase;
        assertEquals("HUTOOL", editor.edit("hutool"));
    }

    @Test
    void editDiscardTest() {
        // 返回 null 表示抛弃
        final Editor<String> editor = s -> s.startsWith("a") ? null : s;
        assertNull(editor.edit("abc"));
        assertEquals("xyz", editor.edit("xyz"));
    }
}
