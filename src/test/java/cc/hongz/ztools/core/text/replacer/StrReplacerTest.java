package cc.hongz.ztools.core.text.replacer;

import cc.hongz.ztools.core.text.StrBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StrReplacer} 单元测试.
 */
class StrReplacerTest {

    /**
     * 替换所有数字为 "*".
     */
    private static class DigitReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            final char c = str.charAt(pos);
            if (Character.isDigit(c)) {
                out.append('*');
                return 1;
            }
            return 0;
        }
    }

    /**
     * 跳过空格（消耗但不输出）.
     */
    private static class SkipSpaceReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            if (str.charAt(pos) == ' ') {
                return 1;
            }
            return 0;
        }
    }

    /**
     * 始终不匹配.
     */
    private static class NoMatchReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            return 0;
        }
    }

    /**
     * 替换 "ab" 为 "XYZ".
     */
    private static class ExpandReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            if (str.length() >= pos + 2) {
                final String sub = str.subSequence(pos, pos + 2).toString();
                if ("ab".equals(sub)) {
                    out.append("XYZ");
                    return 2;
                }
            }
            return 0;
        }
    }

    @Test
    void nullTextTest() {
        final StrReplacer replacer = new NoMatchReplacer();
        assertNull(replacer.replace(null));
    }

    @Test
    void emptyTextTest() {
        final StrReplacer replacer = new NoMatchReplacer();
        assertEquals("", replacer.replace("").toString());
    }

    @Test
    void digitReplaceTest() {
        final StrReplacer replacer = new DigitReplacer();
        assertEquals("abc**def**", replacer.replace("abc12def34").toString());
    }

    @Test
    void noMatchTest() {
        final StrReplacer replacer = new NoMatchReplacer();
        assertEquals("abc123", replacer.replace("abc123").toString());
    }

    @Test
    void skipSpaceTest() {
        final StrReplacer replacer = new SkipSpaceReplacer();
        assertEquals("abc123", replacer.replace("a b c 1 2 3").toString());
    }

    @Test
    void expandReplaceTest() {
        final StrReplacer replacer = new ExpandReplacer();
        assertEquals("XYZcXYZ", replacer.replace("abcab").toString());
    }

    @Test
    void mixedReplaceTest() {
        final StrReplacer replacer = new DigitReplacer();
        assertEquals("abc***def", replacer.replace("abc123def").toString());
    }

    @Test
    void consecutiveMatchTest() {
        final StrReplacer replacer = new DigitReplacer();
        assertEquals("******", replacer.replace("123456").toString());
    }

    @Test
    void singleCharTextTest() {
        final StrReplacer replacer = new DigitReplacer();
        assertEquals("*", replacer.replace("5").toString());
    }

    @Test
    void noDigitsTextTest() {
        final StrReplacer replacer = new DigitReplacer();
        assertEquals("abcdef", replacer.replace("abcdef").toString());
    }
}
