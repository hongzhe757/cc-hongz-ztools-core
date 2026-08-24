package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NodeListIter 单元测试.
 */
@DisplayName("NodeListIter 节点列表迭代器")
class NodeListIterTest {

    private NodeList parseXml(String xml) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        return doc.getElementsByTagName("item");
    }

    @Test
    @DisplayName("基本遍历")
    void basicIteration() throws Exception {
        NodeList nodeList = parseXml("<root><item>a</item><item>b</item><item>c</item></root>");
        NodeListIter iter = new NodeListIter(nodeList);
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next().getTextContent());
        assertEquals("b", iter.next().getTextContent());
        assertEquals("c", iter.next().getTextContent());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("空节点列表")
    void emptyNodeList() throws Exception {
        NodeList nodeList = parseXml("<root></root>");
        NodeListIter iter = new NodeListIter(nodeList);
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("耗尽后 next 抛异常")
    void nextThrowsWhenExhausted() throws Exception {
        NodeList nodeList = parseXml("<root><item>x</item></root>");
        NodeListIter iter = new NodeListIter(nodeList);
        assertEquals("x", iter.next().getTextContent());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    @DisplayName("remove 抛异常")
    void removeThrowsException() throws Exception {
        NodeList nodeList = parseXml("<root><item>a</item></root>");
        NodeListIter iter = new NodeListIter(nodeList);
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    @DisplayName("重置后重新遍历")
    void reset() throws Exception {
        NodeList nodeList = parseXml("<root><item>a</item><item>b</item></root>");
        NodeListIter iter = new NodeListIter(nodeList);
        assertEquals("a", iter.next().getTextContent());
        assertEquals("b", iter.next().getTextContent());
        assertFalse(iter.hasNext());
        iter.reset();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next().getTextContent());
    }
}
