package cc.hongz.ztools.core.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlUtil 单元测试.
 */
public class XmlUtilTest {

    // ==================== readXML / parseXml ====================

    @Test
    @DisplayName("readXML - 从XML字符串读取Document")
    void testReadXmlFromString() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><name>test</name></root>";
        Document doc = XmlUtil.readXML(xml);
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getTagName());
    }

    @Test
    @DisplayName("readXML - 从InputStream读取Document")
    void testReadXmlFromInputStream() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><value>123</value></root>";
        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = XmlUtil.readXML(bis);
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getTagName());
    }

    @Test
    @DisplayName("readXML - 从Reader读取Document")
    void testReadXmlFromReader() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data>hello</data>";
        Document doc = XmlUtil.readXML(new StringReader(xml));
        assertNotNull(doc);
        assertEquals("data", doc.getDocumentElement().getTagName());
    }

    @Test
    @DisplayName("parseXml - 解析XML字符串")
    void testParseXml() {
        String xml = "<root><child attr=\"val\">text</child></root>";
        Document doc = XmlUtil.parseXml(xml);
        assertNotNull(doc);
        Element root = doc.getDocumentElement();
        assertEquals("root", root.getTagName());
    }

    @Test
    @DisplayName("parseXml - 空字符串抛出异常")
    void testParseXmlBlank() {
        assertThrows(IllegalArgumentException.class, () -> XmlUtil.parseXml(""));
        assertThrows(IllegalArgumentException.class, () -> XmlUtil.parseXml("   "));
    }

    // ==================== toStr / format ====================

    @Test
    @DisplayName("toStr - Document转字符串")
    void testToStr() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><a>1</a></root>";
        Document doc = XmlUtil.readXML(xml);
        String result = XmlUtil.toStr(doc);
        assertNotNull(result);
        assertTrue(result.contains("<root>"));
        assertTrue(result.contains("<a>1</a>"));
    }

    @Test
    @DisplayName("format - 格式化XML字符串")
    void testFormat() {
        String xml = "<root><a>1</a><b>2</b></root>";
        String formatted = XmlUtil.format(xml);
        assertNotNull(formatted);
        assertTrue(formatted.contains("root"));
    }

    // ==================== createXml ====================

    @Test
    @DisplayName("createXml - 创建空Document")
    void testCreateXmlEmpty() {
        Document doc = XmlUtil.createXml();
        assertNotNull(doc);
    }

    @Test
    @DisplayName("createXml - 创建带根元素的Document")
    void testCreateXmlWithRootElement() {
        Document doc = XmlUtil.createXml("root");
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getTagName());
    }

    @Test
    @DisplayName("createXml - 创建带命名空间的Document")
    void testCreateXmlWithNamespace() {
        Document doc = XmlUtil.createXml("root", "http://example.com/ns");
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getTagName());
    }

    // ==================== getRootElement ====================

    @Test
    @DisplayName("getRootElement - 获取根元素")
    void testGetRootElement() {
        Document doc = XmlUtil.readXML("<myRoot><child/></myRoot>");
        Element root = XmlUtil.getRootElement(doc);
        assertNotNull(root);
        assertEquals("myRoot", root.getTagName());
    }

    // ==================== cleanInvalid / cleanComment ====================

    @Test
    @DisplayName("cleanInvalid - 清除无效XML字符")
    void testCleanInvalid() {
        String xml = "hello\u0001world\u0008test";
        String cleaned = XmlUtil.cleanInvalid(xml);
        assertEquals("helloworldtest", cleaned);
    }

    @Test
    @DisplayName("cleanComment - 清除XML注释")
    void testCleanComment() {
        String xml = "<root><!-- 这是注释 --><a>1</a></root>";
        String cleaned = XmlUtil.cleanComment(xml);
        assertFalse(cleaned.contains("<!--"));
        assertTrue(cleaned.contains("<a>1</a>"));
    }

    // ==================== getElements / getElement ====================

    @Test
    @DisplayName("getElements - 获取指定标签名的元素列表")
    void testGetElements() {
        Document doc = XmlUtil.readXML("<root><item>a</item><item>b</item><other>c</other></root>");
        List<Element> items = XmlUtil.getElements(doc.getDocumentElement(), "item");
        assertEquals(2, items.size());
    }

    @Test
    @DisplayName("getElements - 空标签名返回所有子元素")
    void testGetElementsEmptyTagName() {
        Document doc = XmlUtil.readXML("<root><a/><b/><c/></root>");
        List<Element> all = XmlUtil.getElements(doc.getDocumentElement(), "");
        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("getElement - 获取第一个匹配的元素")
    void testGetElement() {
        Document doc = XmlUtil.readXML("<root><first>1</first><second>2</second></root>");
        Element ele = XmlUtil.getElement(doc.getDocumentElement(), "second");
        assertNotNull(ele);
        assertEquals("2", ele.getTextContent());
    }

    // ==================== elementText ====================

    @Test
    @DisplayName("elementText - 获取子元素文本")
    void testElementText() {
        Document doc = XmlUtil.readXML("<root><name>hello</name></root>");
        String text = XmlUtil.elementText(doc.getDocumentElement(), "name");
        assertEquals("hello", text);
    }

    @Test
    @DisplayName("elementText - 不存在时返回默认值")
    void testElementTextDefault() {
        Document doc = XmlUtil.readXML("<root><name>hello</name></root>");
        String text = XmlUtil.elementText(doc.getDocumentElement(), "missing", "default");
        assertEquals("default", text);
    }

    // ==================== escape / unescape ====================

    @Test
    @DisplayName("escape - XML转义")
    void testEscape() {
        String result = XmlUtil.escape("<tag attr=\"value\">&");
        assertEquals("&lt;tag attr=&quot;value&quot;&gt;&amp;", result);
    }

    @Test
    @DisplayName("unescape - XML反转义")
    void testUnescape() {
        String result = XmlUtil.unescape("&lt;tag&gt;&amp;&quot;");
        assertEquals("<tag>&\"", result);
    }

    // ==================== xmlToMap ====================

    @Test
    @DisplayName("xmlToMap - XML转Map")
    void testXmlToMap() {
        String xml = "<root><name>test</name><age>18</age></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertNotNull(map);
        assertEquals("test", map.get("name"));
        assertEquals("18", map.get("age"));
    }

    @Test
    @DisplayName("xmlToMap - 嵌套XML转Map")
    void testXmlToMapNested() {
        String xml = "<root><person><name>test</name></person></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertNotNull(map);
        // 嵌套元素应该被递归处理
        assertTrue(map.containsKey("person"));
    }

    // ==================== mapToXmlStr ====================

    @Test
    @DisplayName("mapToXmlStr - Map转XML字符串")
    void testMapToXmlStr() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "test");
        map.put("age", "18");
        String xml = XmlUtil.mapToXmlStr(map);
        assertNotNull(xml);
        assertTrue(xml.contains("<name>"));
        assertTrue(xml.contains("test"));
        assertTrue(xml.contains("<age>"));
        assertTrue(xml.contains("18"));
    }

    @Test
    @DisplayName("mapToXmlStr - 自定义根元素名")
    void testMapToXmlStrCustomRoot() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        String xml = XmlUtil.mapToXmlStr(map, "data");
        assertNotNull(xml);
        assertTrue(xml.contains("<data>"));
    }

    // ==================== xmlToBean / beanToXml ====================

    @Test
    @DisplayName("xmlToBean - XML转Bean")
    void testXmlToBean() {
        String xml = "<root><name>test</name><value>123</value></root>";
        Document doc = XmlUtil.readXML(xml);
        TestBean bean = XmlUtil.xmlToBean(doc.getDocumentElement(), TestBean.class);
        assertNotNull(bean);
        assertEquals("test", bean.getName());
        assertEquals("123", bean.getValue());
    }

    @Test
    @DisplayName("beanToXml - Bean转XML Document")
    void testBeanToXml() {
        TestBean bean = new TestBean();
        bean.setName("hello");
        bean.setValue("world");
        Document doc = XmlUtil.beanToXml(bean);
        assertNotNull(doc);
        String xmlStr = XmlUtil.toStr(doc);
        assertTrue(xmlStr.contains("hello"));
        assertTrue(xmlStr.contains("world"));
    }

    // ==================== XPath ====================

    @Test
    @DisplayName("getElementByXPath - 通过XPath获取元素")
    void testGetElementByXPath() {
        String xml = "<root><a><b>target</b></a></root>";
        Document doc = XmlUtil.readXML(xml);
        Element ele = XmlUtil.getElementByXPath("//b", doc);
        assertNotNull(ele);
        assertEquals("target", ele.getTextContent());
    }

    @Test
    @DisplayName("getNodeByXPath - 通过XPath获取节点")
    void testGetNodeByXPath() {
        String xml = "<root><item>value</item></root>";
        Document doc = XmlUtil.readXML(xml);
        Node node = XmlUtil.getNodeByXPath("//item", doc);
        assertNotNull(node);
        assertEquals("value", node.getTextContent());
    }

    // ==================== appendChild / appendText ====================

    @Test
    @DisplayName("appendChild - 追加子元素")
    void testAppendChild() {
        Document doc = XmlUtil.createXml("root");
        Element child = XmlUtil.appendChild(doc.getDocumentElement(), "child");
        assertNotNull(child);
        assertEquals("child", child.getTagName());
        // 验证确实是子节点
        NodeList children = doc.getDocumentElement().getChildNodes();
        assertEquals(1, children.getLength());
    }

    @Test
    @DisplayName("appendText - 追加文本节点")
    void testAppendText() {
        Document doc = XmlUtil.createXml("root");
        Element child = XmlUtil.appendChild(doc.getDocumentElement(), "item");
        XmlUtil.appendText(child, "hello world");
        assertEquals("hello world", child.getTextContent());
    }

    // ==================== transElements ====================

    @Test
    @DisplayName("transElements - NodeList转Element列表")
    void testTransElements() {
        Document doc = XmlUtil.readXML("<root><a/><b/><c/></root>");
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        List<Element> elements = XmlUtil.transElements(doc.getDocumentElement(), nodeList);
        assertEquals(3, elements.size());
    }

    // ==================== isElement ====================

    @Test
    @DisplayName("isElement - 判断节点是否为Element")
    void testIsElement() {
        Document doc = XmlUtil.readXML("<root>text<child/></root>");
        Element root = doc.getDocumentElement();
        assertTrue(XmlUtil.isElement(root));
        // 文本节点不是Element
        Node textNode = root.getFirstChild();
        assertFalse(XmlUtil.isElement(textNode));
    }

    // ==================== 边界条件 ====================

    @Test
    @DisplayName("readXML - null文件抛出异常")
    void testReadXmlNullFile() {
        assertThrows(Exception.class, () -> XmlUtil.readXML((java.io.File) null));
    }

    @Test
    @DisplayName("escape - null抛出异常(底层XmlEscape不支持null)")
    void testEscapeNull() {
        assertThrows(Exception.class, () -> XmlUtil.escape(null));
    }

    @Test
    @DisplayName("unescape - null抛出异常(底层XmlUnescape不支持null)")
    void testUnescapeNull() {
        assertThrows(Exception.class, () -> XmlUtil.unescape(null));
    }

    @Test
    @DisplayName("xmlToMap - 带属性的XML")
    void testXmlToMapWithAttributes() {
        String xml = "<root><item id=\"1\">value</item></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertNotNull(map);
    }

    @Test
    @DisplayName("mapToXmlStr - 空Map")
    void testMapToXmlStrEmpty() {
        Map<String, Object> map = new HashMap<>();
        String xml = XmlUtil.mapToXmlStr(map);
        assertNotNull(xml);
    }

    // ==================== 测试用Bean类 ====================

    public static class TestBean {
        private String name;
        private String value;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
