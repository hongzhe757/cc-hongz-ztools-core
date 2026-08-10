package cc.hongz.ztools.core.text.replacer;

import cc.hongz.ztools.core.text.StrBuilder;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ReplacerChain} 单元测试.
 */
class ReplacerChainTest {

    /**
     * 测试替换器：将 "abc" 替换为 "ABC".
     */
    private static class TestReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            if (str.length() >= pos + 3) {
                final String sub = str.subSequence(pos, pos + 3).toString();
                if ("abc".equals(sub)) {
                    out.append("ABC");
                    return 3;
                }
            }
            return 0;
        }
    }

    /**
     * 测试替换器：将 "123" 替换为 "456".
     */
    private static class NumberReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            if (str.length() >= pos + 3) {
                final String sub = str.subSequence(pos, pos + 3).toString();
                if ("123".equals(sub)) {
                    out.append("456");
                    return 3;
                }
            }
            return 0;
        }
    }

    /**
     * 测试替换器：始终不匹配.
     */
    private static class NoMatchReplacer extends StrReplacer {
        private static final long serialVersionUID = 1L;

        @Override
        protected int replace(final CharSequence str, final int pos, final StrBuilder out) {
            return 0;
        }
    }

    @Test
    void emptyChainTest() {
        final ReplacerChain chain = new ReplacerChain();
        final String result = chain.replace("abc123").toString();
        assertEquals("abc123", result);
    }

    @Test
    void singleReplacerTest() {
        final ReplacerChain chain = new ReplacerChain(new TestReplacer());
        final String result = chain.replace("abc123").toString();
        assertEquals("ABC123", result);
    }

    @Test
    void firstMatchWinsTest() {
        // 两个都能匹配 "abc"，但第一个先匹配
        final ReplacerChain chain = new ReplacerChain(new TestReplacer(), new TestReplacer());
        final String result = chain.replace("abcabc").toString();
        assertEquals("ABCABC", result);
    }

    @Test
    void multiReplacerTest() {
        final ReplacerChain chain = new ReplacerChain(new TestReplacer(), new NumberReplacer());
        final String result = chain.replace("abc123").toString();
        assertEquals("ABC456", result);
    }

    @Test
    void noMatchTest() {
        final ReplacerChain chain = new ReplacerChain(new NoMatchReplacer());
        final String result = chain.replace("abc123").toString();
        assertEquals("abc123", result);
    }

    @Test
    void chainOrderTest() {
        // 第一个能匹配 "abc"
        final ReplacerChain chain1 = new ReplacerChain(new TestReplacer(), new NoMatchReplacer());
        assertEquals("ABC123", chain1.replace("abc123").toString());

        // 第一个不能匹配，第二个能匹配
        final ReplacerChain chain2 = new ReplacerChain(new NoMatchReplacer(), new TestReplacer());
        assertEquals("ABC123", chain2.replace("abc123").toString());
    }

    @Test
    void addChainTest() {
        final ReplacerChain chain = new ReplacerChain();
        chain.addChain(new TestReplacer());
        chain.addChain(new NumberReplacer());
        final String result = chain.replace("abc123").toString();
        assertEquals("ABC456", result);
    }

    @Test
    void iteratorTest() {
        final TestReplacer r1 = new TestReplacer();
        final NumberReplacer r2 = new NumberReplacer();
        final ReplacerChain chain = new ReplacerChain(r1, r2);

        final Iterator<StrReplacer> it = chain.iterator();
        assertTrue(it.hasNext());
        assertSame(r1, it.next());
        assertTrue(it.hasNext());
        assertSame(r2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void nullTextTest() {
        final ReplacerChain chain = new ReplacerChain(new TestReplacer());
        assertDoesNotThrow(() -> {
            final CharSequence result = chain.replace(null);
            assertNull(result);
        });
    }

    @Test
    void emptyTextTest() {
        final ReplacerChain chain = new ReplacerChain(new TestReplacer());
        final String result = chain.replace("").toString();
        assertEquals("", result);
    }
}
