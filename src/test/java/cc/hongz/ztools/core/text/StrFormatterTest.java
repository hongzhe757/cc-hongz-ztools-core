package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link StrFormatter} 单元测试.
 *
 * @author Looly
 */
public class StrFormatterTest {

    @Test
    @DisplayName("format - 基本占位符替换")
    public void formatTest() {
        //通常使用
        String result1 = StrFormatter.format("this is {} for {}", "a", "b");
        assertEquals("this is a for b", result1);

        //转义{}
        String result2 = StrFormatter.format("this is \\{} for {}", "a", "b");
        assertEquals("this is {} for a", result2);

        //转义\
        String result3 = StrFormatter.format("this is \\\\{} for {}", "a", "b");
        assertEquals("this is \\a for b", result3);
    }

    @Test
    @DisplayName("formatWith - 自定义占位符替换")
    public void formatWithTest() {
        //通常使用
        String result1 = StrFormatter.formatWith("this is ? for ?", "?", "a", "b");
        assertEquals("this is a for b", result1);

        //转义?
        String result2 = StrFormatter.formatWith("this is \\? for ?", "?", "a", "b");
        assertEquals("this is ? for a", result2);

        //转义\
        String result3 = StrFormatter.formatWith("this is \\\\? for ?", "?", "a", "b");
        assertEquals("this is \\a for b", result3);
    }

    @Test
    @DisplayName("formatWith - 多字符占位符替换")
    public void formatWithTest2() {
        //通常使用
        String result1 = StrFormatter.formatWith("this is $$$ for $$$", "$$$", "a", "b");
        assertEquals("this is a for b", result1);

        //转义?
        String result2 = StrFormatter.formatWith("this is \\$$$ for $$$", "$$$", "a", "b");
        assertEquals("this is $$$ for a", result2);

        //转义\
        String result3 = StrFormatter.formatWith("this is \\\\$$$ for $$$", "$$$", "a", "b");
        assertEquals("this is \\a for b", result3);
    }
}
