package cc.hongz.ztools.core.text.replacer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link LookupReplacer} 单元测试.
 */
class LookupReplacerTest {

    @Test
    void simpleReplaceTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"hello", "你好"},
                new String[]{"world", "世界"}
        );
        assertEquals("你好 世界", replacer.replace("hello world").toString());
    }

    @Test
    void longestMatchTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"ab", "AB"},
                new String[]{"abc", "ABC"}
        );
        assertEquals("ABC", replacer.replace("abc").toString());
    }

    @Test
    void noMatchTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"foo", "bar"}
        );
        assertEquals("hello", replacer.replace("hello").toString());
    }

    @Test
    void multipleReplaceTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"a", "1"},
                new String[]{"b", "2"},
                new String[]{"c", "3"}
        );
        assertEquals("123", replacer.replace("abc").toString());
    }

    @Test
    void partialReplaceTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"abc", "ABC"}
        );
        assertEquals("ABCdef", replacer.replace("abcdef").toString());
    }

    @Test
    void emptyInputTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"a", "A"}
        );
        assertEquals("", replacer.replace("").toString());
    }

    @Test
    void nullInputTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"a", "A"}
        );
        assertNull(replacer.replace(null));
    }

    @Test
    void boundaryTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"test", "测试"}
        );
        // 末尾匹配
        assertEquals("prefix测试", replacer.replace("prefixtest").toString());
        // 开头匹配
        assertEquals("测试suffix", replacer.replace("testsuffix").toString());
    }

    @Test
    void chineseReplaceTest() {
        final LookupReplacer replacer = new LookupReplacer(
                new String[]{"苹果", "apple"},
                new String[]{"香蕉", "banana"}
        );
        assertEquals("apple banana", replacer.replace("苹果 香蕉").toString());
    }
}
