package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link TextSimilarity} 单元测试.
 */
@DisplayName("TextSimilarity 文本相似度工具类")
class TextSimilarityTest {

    @Test
    @DisplayName("similar - 中文文本相似度")
    void similarDegreeTest() {
        final String a = "我是一个文本，独一无二的文本";
        final String b = "一个文本，独一无二的文本";

        final double degree = TextSimilarity.similar(a, b);
        assertEquals(0.8461538462D, degree, 0.01);

        final String similarPercent = TextSimilarity.similar(a, b, 2);
        assertEquals("84.62%", similarPercent);
    }

    @Test
    @DisplayName("similar - 标点符号被过滤后相似度不变")
    void similarDegreeWithPunctuationTest() {
        final String a = "我是一个文本，独一无二的文本";
        final String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

        final double degree = TextSimilarity.similar(a, b);
        assertEquals(0.8461538462D, degree, 0.01);

        final String similarPercent = TextSimilarity.similar(a, b, 2);
        assertEquals("84.62%", similarPercent);
    }

    @Test
    @DisplayName("similar - 完全不同字符串返回0")
    void similarCompletelyDifferentTest() {
        final double abd = TextSimilarity.similar("abd", "1111");
        assertEquals(0, abd, 0);
    }

    @Test
    @DisplayName("similar - 两个空串相似度为1")
    void similarBothEmptyTest() {
        final double degree = TextSimilarity.similar("", "");
        assertEquals(1.0, degree, 0);
    }

    @Test
    @DisplayName("similar - 一个空串相似度为0")
    void similarOneEmptyTest() {
        assertEquals(0.0, TextSimilarity.similar("abc", ""), 0);
        assertEquals(0.0, TextSimilarity.similar("", "abc"), 0);
    }

    @Test
    @DisplayName("similar - 完全相同的字符串相似度为1")
    void similarIdenticalTest() {
        assertEquals(1.0, TextSimilarity.similar("hello", "hello"), 0);
        assertEquals(1.0, TextSimilarity.similar("中文测试", "中文测试"), 0);
    }

    @Test
    @DisplayName("longestCommonSubstring - 英文最长公共子串")
    void longestCommonSubstringEnglishTest() {
        final String result = TextSimilarity.longestCommonSubstring("abcde", "ace");
        assertEquals("ace", result);
    }

    @Test
    @DisplayName("longestCommonSubstring - 中文最长公共子串")
    void longestCommonSubstringChineseTest() {
        final String result = TextSimilarity.longestCommonSubstring("我是中国人", "我是中国");
        assertEquals("我是中国", result);
    }

    @Test
    @DisplayName("longestCommonSubstring - 无公共子串返回空串")
    void longestCommonSubstringNoCommonTest() {
        final String result = TextSimilarity.longestCommonSubstring("abc", "xyz");
        assertEquals("", result);
    }
}
