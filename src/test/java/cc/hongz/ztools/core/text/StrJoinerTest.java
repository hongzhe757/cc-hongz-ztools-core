package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link StrJoiner} 单元测试.
 *
 * @author Looly
 */
@DisplayName("StrJoiner 字符串连接器")
public class StrJoinerTest {

    @Test
    @DisplayName("of(delimiter) - 基本分隔符拼接")
    public void ofDelimiter() {
        assertEquals("a,b,c", StrJoiner.of(",").append("a").append("b").append("c").toString());
    }

    @Test
    @DisplayName("of(delimiter, prefix, suffix) - 带前后缀")
    public void ofPrefixSuffix() {
        assertEquals("[a,b,c]", StrJoiner.of(",", "[", "]").append("a").append("b").append("c").toString());
    }

    @Test
    @DisplayName("append(Iterable) - 追加集合元素")
    public void appendIterable() {
        assertEquals("1,2,3", StrJoiner.of(",").append(Arrays.asList(1, 2, 3)).toString());
    }

    @Test
    @DisplayName("append(Array) - 追加数组元素")
    public void appendArray() {
        assertEquals("a-b-c", StrJoiner.of("-").append(new String[]{"a", "b", "c"}).toString());
    }

    @Test
    @DisplayName("wrapElement - 前后缀包装每个元素")
    public void wrapElement() {
        String result = StrJoiner.of(",", "[", "]")
                .setWrapElement(true)
                .append("a")
                .append("b")
                .toString();
        assertEquals("[a],[b]", result);
    }

    @Test
    @DisplayName("NullMode.IGNORE - 忽略null")
    public void nullModeIgnore() {
        String result = StrJoiner.of(",")
                .setNullMode(StrJoiner.NullMode.IGNORE)
                .append("a")
                .append((CharSequence) null)
                .append("c")
                .toString();
        assertEquals("a,c", result);
    }

    @Test
    @DisplayName("NullMode.TO_EMPTY - null转空串")
    public void nullModeToEmpty() {
        String result = StrJoiner.of(",")
                .setNullMode(StrJoiner.NullMode.TO_EMPTY)
                .append("a")
                .append((CharSequence) null)
                .append("c")
                .toString();
        assertEquals("a,,c", result);
    }

    @Test
    @DisplayName("NullMode.NULL_STRING - null转为null字符串")
    public void nullModeNullString() {
        String result = StrJoiner.of(",")
                .append("a")
                .append((CharSequence) null)
                .append("c")
                .toString();
        assertEquals("a,null,c", result);
    }

    @Test
    @DisplayName("emptyResult - 无元素时返回默认值")
    public void emptyResult() {
        String result = StrJoiner.of(",")
                .setEmptyResult("empty")
                .toString();
        assertEquals("empty", result);
    }

    @Test
    @DisplayName("length - 返回拼接后长度")
    public void length() {
        StrJoiner joiner = StrJoiner.of(",").append("abc");
        assertEquals(3, joiner.length());
    }

    @Test
    @DisplayName("of(StrJoiner) - 从已有StrJoiner复制配置")
    public void ofStrJoiner() {
        StrJoiner original = StrJoiner.of(",", "[", "]").setWrapElement(true);
        StrJoiner copy = StrJoiner.of(original);
        assertEquals("[a],[b]", copy.append("a").append("b").toString());
    }

    @Test
    @DisplayName("merge - 合并两个StrJoiner")
    public void merge() {
        StrJoiner joiner1 = StrJoiner.of(",", "[", "]").append("a").append("b");
        StrJoiner joiner2 = StrJoiner.of(",", "[", "]").append("c").append("d");
        joiner1.merge(joiner2);
        // merge在wrapElement=false时，跳过other的prefix但保留suffix，toString再追加自身suffix
        assertEquals("[a,b,c,d]]", joiner1.toString());
    }

    @Test
    @DisplayName("append with Function - 元素转换后拼接")
    public void appendWithFunction() {
        String result = StrJoiner.of(",")
                .append(Arrays.asList(1, 2, 3), i -> "v" + i)
                .toString();
        assertEquals("v1,v2,v3", result);
    }
}
