package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link StrBuilder} 单元测试.
 *
 * @author Looly
 */
public class StrBuilderTest {

    /**
     * StrBuilder的性能测试.
     */
    @Test
    @Disabled
    @DisplayName("bench - StrBuilder性能测试（禁用）")
    public void benchTest() {
        StrBuilder builder = StrBuilder.create();
        for (int i = 0; i < 1000000; i++) {
            builder.append("test");
            builder.reset();
        }
    }

    @Test
    @DisplayName("append - 追加多种类型字符串")
    public void appendTest() {
        StrBuilder builder = StrBuilder.create();
        builder.append("aaa").append("你好").append('r');
        assertEquals("aaa你好r", builder.toString());
    }

    @Test
    @DisplayName("insert - 中间位置插入")
    public void insertTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        assertEquals("aaa数据插入你好r", builder.toString());
    }

    @Test
    @DisplayName("insert - 超出位置插入（补空格）")
    public void insertTest2() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(8, "数据插入");
        assertEquals("aaa你好r  数据插入", builder.toString());
    }

    @Test
    @DisplayName("reset - 重置后为空")
    public void resetTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        builder.reset();
        assertEquals("", builder.toString());
    }

    @Test
    @DisplayName("reset - 重置后追加字符数组")
    public void resetTest2() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        builder.reset();
        builder.append("bbb".toCharArray());
        assertEquals("bbb", builder.toString());
    }

    @Test
    @DisplayName("appendObject - 追加数字和布尔值")
    public void appendObjectTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append(123).append(456.123D).append(true).append('\n');
        assertEquals("123456.123true\n", builder.toString());
    }

    @Test
    @DisplayName("del - 删除全部")
    public void delTest() {
        // 删除全部测试
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");
        int length = strBuilder.length();
        StrBuilder builder = strBuilder.del(0, length);
        assertEquals("", builder.toString());
    }

    @Test
    @DisplayName("del - 删除中间部分")
    public void delTest2() {
        // 删除中间部分测试
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");
        StrBuilder builder = strBuilder.del(2, 6);
        assertEquals("ABG", builder.toString());
    }

    @Test
    @DisplayName("delTo - 删除到指定位置")
    public void delToTest() {
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");

        // 不处理
        StrBuilder builder = strBuilder.delTo(7);
        assertEquals("ABCDEFG", builder.toString());

        // 删除全部
        builder = strBuilder.delTo(0);
        assertEquals("", builder.toString());
    }

    @Test
    @DisplayName("insert - 带start/end的CharSequence插入")
    public void issueICTSRZTest() {
        final StrBuilder helloWorld = StrBuilder.create("Hello World");
        helloWorld.insert(6, "Beautiful ", 0, 10);
        Assertions.assertEquals("Hello Beautiful World", helloWorld.toString());
    }

    @Test
    @DisplayName("insert - 完整CharSequence插入")
    public void issueICTSRZTest2() {
        final StrBuilder helloWorld = StrBuilder.create("Hello World");
        helloWorld.insert(6, "Beautiful ");
        Assertions.assertEquals("Hello Beautiful World", helloWorld.toString());
    }

    @Test
    @DisplayName("charAt - 正负数索引及越界检查")
    public void charAtTest() {
        final StrBuilder helloWorld = StrBuilder.create("Hello World");
        Assertions.assertEquals('d', helloWorld.charAt(-1));
        Assertions.assertEquals('H', helloWorld.charAt(0));
        Assertions.assertEquals('d', helloWorld.charAt(10));
        Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> helloWorld.charAt(11));
    }

    @Test
    @DisplayName("appendAndToString - 链式append后toString验证")
    public void appendAndToString() {
        final StrBuilder builder = new StrBuilder();
        builder.append("a").append(1);
        assertEquals("a1", builder.toString());
    }
}
