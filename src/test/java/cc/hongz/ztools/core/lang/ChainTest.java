package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Chain} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Chain 责任链")
class ChainTest {

    @Test
    void addChainTest() {
        final ListChain chain = new ListChain();
        chain.addChain("a").addChain("b").addChain("c");
        assertEquals(3, chain.size());
    }

    static class ListChain implements Chain<String, ListChain> {
        private final List<String> list = new ArrayList<>();

        @Override
        public ListChain addChain(final String element) {
            list.add(element);
            return this;
        }

        @Override
        public Iterator<String> iterator() {
            return list.iterator();
        }

        int size() {
            return list.size();
        }
    }
}
