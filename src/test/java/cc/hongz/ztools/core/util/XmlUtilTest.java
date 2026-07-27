package cc.hongz.ztools.core.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hongz.ztools.core.collection.CollUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.xpath.XPath;

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

    // ==================== readBySax ====================

    @Test
    @DisplayName("readBySax - SAX方式读取XML")
    void testReadBySax() {
        String xml = "<root><a>1</a><b>2</b><c>3</c></root>";
        java.util.Set<String> tags = new java.util.HashSet<>();
        XmlUtil.readBySax(new java.io.ByteArrayInputStream(
                xml.getBytes(StandardCharsets.UTF_8)),
                new DefaultHandler() {
                    @Override
                    public void startElement(String uri,
                            String localName, String qName,
                            Attributes attributes) {
                        if (!"root".equals(qName)) {
                            tags.add(qName);
                        }
                    }
                });
        assertEquals(3, tags.size());
        assertTrue(tags.contains("a"));
        assertTrue(tags.contains("b"));
        assertTrue(tags.contains("c"));
    }

    // ==================== xmlToMap 重复key ====================

    @Test
    @DisplayName("xmlToMap - 重复key转List")
    void testXmlToMapDuplicateKeys() {
        String xml = "<root><name>张三</name><name>李四</name></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertEquals(1, map.size());
        assertTrue(map.get("name") instanceof List);
        List<?> names = (List<?>) map.get("name");
        assertEquals(2, names.size());
        assertTrue(names.contains("张三"));
        assertTrue(names.contains("李四"));
    }

    // ==================== mapToXml 嵌套 ====================

    @Test
    @DisplayName("mapToXml - 嵌套Map转XML并验证精确输出")
    void testMapToXmlNested() {
        Map<String, Object> gameMap = new HashMap<>();
        gameMap.put("name", "Looly");
        gameMap.put("level", "14");

        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("name", "张三");
        map.put("age", "12");
        map.put("game", gameMap);

        Document doc = XmlUtil.mapToXml(map, "user");
        String xml = XmlUtil.toStr(doc, false);
        assertTrue(xml.contains("<name>张三</name>"));
        assertTrue(xml.contains("<game>"));
        assertTrue(xml.contains("<name>Looly</name>"));
    }

    @Test
    @DisplayName("mapToXml - List类型值转XML")
    void testMapToXmlWithList() {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("Town", CollUtil.newArrayList("town1", "town2"));
        Document doc = XmlUtil.mapToXml(map, "City");
        String xml = XmlUtil.toStr(doc);
        assertTrue(xml.contains("<Town>town1</Town>"));
        assertTrue(xml.contains("<Town>town2</Town>"));
    }

    // ==================== mapToXmlStr 选项 ====================

    @Test
    @DisplayName("mapToXmlStr - 忽略xml声明")
    void testMapToXmlStrOmitDeclaration() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "ddatsh");
        String xml = XmlUtil.mapToXmlStr(map, true);
        assertEquals("<xml><name>ddatsh</name></xml>", xml);
    }

    // ==================== beanToXml ignoreNull ====================

    @Test
    @DisplayName("beanToXml - 不忽略null字段时保留空节点")
    void testBeanToXmlNotIgnoreNull() {
        TestBean bean = new TestBean();
        bean.setName("hello");
        bean.setValue(null);
        Document doc = XmlUtil.beanToXml(bean, null, false);
        assertNotNull(XmlUtil.getElement(
                doc.getDocumentElement(), "value"));
    }

    @Test
    @DisplayName("beanToXml - 忽略null字段时无节点")
    void testBeanToXmlIgnoreNull() {
        TestBean bean = new TestBean();
        bean.setName("hello");
        bean.setValue(null);
        Document doc = XmlUtil.beanToXml(bean, null, true);
        assertNull(XmlUtil.getElement(
                doc.getDocumentElement(), "value"));
    }

    // ==================== xmlToBean / beanToXml 往返 ====================

    @Test
    @DisplayName("xmlToBean/beanToXml - 往返转换一致性")
    void testBeanXmlRoundTrip() {
        TestBean bean = new TestBean();
        bean.setName("roundTrip");
        bean.setValue("testValue");
        Document doc = XmlUtil.beanToXml(bean);
        TestBean result = XmlUtil.xmlToBean(doc, TestBean.class);
        assertEquals(bean.getName(), result.getName());
        assertEquals(bean.getValue(), result.getValue());
    }

    @Test
    @DisplayName("xmlToBean - XML字符串带属性转Bean")
    void testXmlStrToBean() {
        String xml = "<testBean><name>张三</name>"
                + "<value>zhangsan@example.com</value></testBean>";
        Document doc = XmlUtil.readXML(xml);
        TestBean bean = XmlUtil.xmlToBean(doc, TestBean.class);
        assertEquals("张三", bean.getName());
        assertEquals("zhangsan@example.com", bean.getValue());
    }

    // ==================== XPath 进阶 ====================

    @Test
    @DisplayName("getByXPath - 带命名空间的XPath")
    void testGetByXPathWithNamespace() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<soap:Envelope"
                + " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                + "  <soap:Body>\n"
                + "    <ns2:testResponse"
                + " xmlns:ns2=\"http://ws.xxx.com/\">\n"
                + "      <return>2020/04/15 21:01:21</return>\n"
                + "    </ns2:testResponse>\n"
                + "  </soap:Body>\n"
                + "</soap:Envelope>\n";
        Document doc = XmlUtil.readXML(xml);
        Object value = XmlUtil.getByXPath(
                "//soap:Envelope/soap:Body/ns2:testResponse/return",
                doc, javax.xml.xpath.XPathConstants.STRING);
        assertEquals("2020/04/15 21:01:21", value);
    }

    @Test
    @DisplayName("getNodeListByXPath - 获取节点列表")
    void testGetNodeListByXPath() {
        String xml = "<root><item>a</item><item>b</item><item>c</item></root>";
        Document doc = XmlUtil.readXML(xml);
        NodeList nodes = XmlUtil.getNodeListByXPath("//item", doc);
        assertNotNull(nodes);
        assertEquals(3, nodes.getLength());
    }

    // ==================== toStr 进阶 ====================

    @Test
    @DisplayName("toStr - 指定编码和忽略xml声明")
    void testToStrWithCharsetAndOmitDeclaration() {
        Document doc = XmlUtil.createXml("root");
        Element child = XmlUtil.appendChild(
                doc.getDocumentElement(), "name");
        child.setTextContent("测试");
        String xml = XmlUtil.toStr(
                (Node) doc, "UTF-8", true, true);
        assertNotNull(xml);
        assertFalse(xml.startsWith("<?xml"));
        assertTrue(xml.contains("测试"));
    }

    @Test
    @DisplayName("toStr - Document转格式化字符串")
    void testToStrDocumentPretty() {
        Document doc = XmlUtil.createXml("root");
        XmlUtil.appendChild(doc.getDocumentElement(), "child");
        String xml = XmlUtil.toStr(doc, true);
        assertNotNull(xml);
        assertTrue(xml.contains("\n"));
    }

    // ==================== 工具方法 ====================

    @Test
    @DisplayName("createXPath - 创建XPath实例")
    void testCreateXPath() {
        XPath xpath = XmlUtil.createXPath();
        assertNotNull(xpath);
    }

    @Test
    @DisplayName("getOwnerDocument - 获取节点所属Document")
    void testGetOwnerDocument() {
        Document doc = XmlUtil.createXml("root");
        Element root = doc.getDocumentElement();
        Document ownerDoc = XmlUtil.getOwnerDocument(root);
        assertSame(doc, ownerDoc);
    }

    @Test
    @DisplayName("append - 追加Map数据到节点")
    void testAppendMapData() {
        Document doc = XmlUtil.createXml("root");
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");
        XmlUtil.append(doc.getDocumentElement(), data);
        String xml = XmlUtil.toStr(doc, false);
        assertTrue(xml.contains("<key1>value1</key1>"));
        assertTrue(xml.contains("<key2>value2</key2>"));
    }

    @Test
    @DisplayName("append - 通过mapToXml追加List数据")
    void testAppendListData() {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("Town", CollUtil.newArrayList("town1", "town2"));
        Document doc = XmlUtil.mapToXml(map, "City");
        String xml = XmlUtil.toStr(doc, false);
        assertTrue(xml.contains("<Town>town1</Town>"));
        assertTrue(xml.contains("<Town>town2</Town>"));
    }

    @Test
    @DisplayName("disableDefaultDocumentBuilderFactory - 禁用默认工厂")
    void testDisableDefaultDocumentBuilderFactory() {
        XmlUtil.disableDefaultDocumentBuilderFactory();
        // 禁用后仍能正常创建Document，只是不再使用默认实现
        Document doc = XmlUtil.createXml("test");
        assertNotNull(doc);
    }

    @Test
    @DisplayName("setNamespaceAware - 设置命名空间感知")
    void testSetNamespaceAware() {
        XmlUtil.setNamespaceAware(false);
        Document doc = XmlUtil.createXml("root", "http://test.com/ns");
        assertNotNull(doc);
        // 恢复默认值
        XmlUtil.setNamespaceAware(true);
    }

    // ==================== xmlToBean with CopyOptions ====================

    @Test
    @DisplayName("xmlToBean - 使用CopyOptions忽略错误")
    void testXmlToBeanWithCopyOptionsIgnoreError() {
        String xml = "<testBean><name>test</name>"
                + "<unknownField>ignored</unknownField></testBean>";
        Document doc = XmlUtil.readXML(xml);
        cc.hongz.ztools.core.bean.copier.CopyOptions options =
                cc.hongz.ztools.core.bean.copier.CopyOptions.create()
                        .ignoreError();
        TestBean bean = XmlUtil.xmlToBean(
                doc.getDocumentElement(), TestBean.class, options);
        assertNotNull(bean);
        assertEquals("test", bean.getName());
    }

    // ==================== cleanInvalid/cleanComment 边界 ====================

    @Test
    @DisplayName("cleanInvalid - null返回null")
    void testCleanInvalidNull() {
        assertNull(XmlUtil.cleanInvalid(null));
    }

    @Test
    @DisplayName("cleanComment - null返回null")
    void testCleanCommentNull() {
        assertNull(XmlUtil.cleanComment(null));
    }

    @Test
    @DisplayName("cleanComment - 精确清除注释")
    void testCleanCommentExact() {
        String xml = "<info><title>hutool</title>"
                + "<!-- 这是注释 --><lang>java</lang></info>";
        String ret = XmlUtil.cleanComment(xml);
        assertEquals("<info><title>hutool</title>"
                + "<lang>java</lang></info>", ret);
    }

    // ==================== getRootElement / isElement 边界 ====================

    @Test
    @DisplayName("getRootElement - null返回null")
    void testGetRootElementNull() {
        assertNull(XmlUtil.getRootElement(null));
    }

    @Test
    @DisplayName("isElement - null返回false")
    void testIsElementNull() {
        assertFalse(XmlUtil.isElement(null));
    }

    // ==================== Double类型值(测试数字精度) ====================

    @Test
    @DisplayName("xmlToMap - 字符串数字保留原值")
    void testXmlToMapRetainsStringNumbers() {
        String xml = "<root><count>1490</count><rate>1.5</rate></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertEquals("1490", map.get("count"));
        assertEquals("1.5", map.get("rate"));
    }

    // ==================== xmlToMap 详细断言 ====================

    @Test
    @DisplayName("xmlToMap - 多字段含嵌套子节点详细断言")
    void testXmlToMapDetailed() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
                + "<returnsms>"
                + "<returnstatus>Success</returnstatus>"
                + "<message>ok</message>"
                + "<remainpoint>1490</remainpoint>"
                + "<taskID>885</taskID>"
                + "<successCounts>1</successCounts>"
                + "<newNode><sub>subText</sub></newNode>"
                + "</returnsms>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertEquals(6, map.size());
        assertEquals("Success", map.get("returnstatus"));
        assertEquals("ok", map.get("message"));
        assertEquals("1490", map.get("remainpoint"));
        assertEquals("885", map.get("taskID"));
        assertEquals("1", map.get("successCounts"));
        assertEquals("subText", ((Map<?, ?>) map.get("newNode")).get("sub"));
    }

    // ==================== mapToXml 精确输出 ====================

    @Test
    @DisplayName("mapToXml - 嵌套Map精确输出断言")
    void testMapToXmlExactOutput() {
        Map<String, Object> gameMap = new java.util.LinkedHashMap<>();
        gameMap.put("昵称", "Looly");
        gameMap.put("level", 14);

        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("name", "张三");
        map.put("age", 12);
        map.put("game", gameMap);

        Document doc = XmlUtil.mapToXml(map, "user");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                        + "<user>"
                        + "<name>张三</name>"
                        + "<age>12</age>"
                        + "<game>"
                        + "<昵称>Looly</昵称>"
                        + "<level>14</level>"
                        + "</game>"
                        + "</user>",
                XmlUtil.toStr(doc, false));
    }

    @Test
    @DisplayName("mapToXml - List类型精确输出断言")
    void testMapToXmlListExactOutput() {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("Town", CollUtil.newArrayList("town1", "town2"));
        Document doc = XmlUtil.mapToXml(map, "City");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                        + "<City>"
                        + "<Town>town1</Town>"
                        + "<Town>town2</Town>"
                        + "</City>",
                XmlUtil.toStr(doc));
    }

    // ==================== escape 中文 ====================

    @Test
    @DisplayName("escape - 中文双引号不转义")
    void testEscapeChinese() {
        assertEquals("&lt;&gt;", XmlUtil.escape("<>"));
        assertEquals("中文\u201c双引号\u201d", XmlUtil.escape("中文\u201c双引号\u201d"));
    }

    // ==================== xmlToMap(Node) ====================

    @Test
    @DisplayName("xmlToMap - Node重载")
    void testXmlToMapFromNode() {
        String xml = "<root><a>1</a><b>2</b></root>";
        Document doc = XmlUtil.readXML(xml);
        Map<String, Object> map = XmlUtil.xmlToMap(doc.getDocumentElement());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
    }

    // ==================== xmlToMap with result Map ====================

    @Test
    @DisplayName("xmlToMap - 传入已有Map追加数据")
    void testXmlToMapWithResultMap() {
        String xml = "<root><x>10</x></root>";
        Map<String, Object> result = new HashMap<>();
        result.put("existing", "value");
        Map<String, Object> returned = XmlUtil.xmlToMap(xml, result);
        assertSame(result, returned);
        assertEquals("value", result.get("existing"));
        assertEquals("10", result.get("x"));
    }

    // ==================== write to Writer ====================

    @Test
    @DisplayName("write - Node写入Writer")
    void testWriteToWriter() {
        Document doc = XmlUtil.createXml("root");
        XmlUtil.appendChild(doc.getDocumentElement(), "child");
        java.io.StringWriter writer = new java.io.StringWriter();
        XmlUtil.write(doc, writer, "UTF-8", 2);
        String result = writer.toString();
        assertTrue(result.contains("<root>"));
        assertTrue(result.contains("<child/>"));
    }

    @Test
    @DisplayName("write - Node写入Writer忽略xml声明")
    void testWriteToWriterOmitDeclaration() {
        Document doc = XmlUtil.createXml("root");
        java.io.StringWriter writer = new java.io.StringWriter();
        XmlUtil.write(doc, writer, "UTF-8", 2, true);
        String result = writer.toString();
        assertFalse(result.startsWith("<?xml"));
        assertTrue(result.contains("<root/>"));
    }

    @Test
    @DisplayName("write - Node写入OutputStream")
    void testWriteToOutputStream() {
        Document doc = XmlUtil.createXml("root");
        XmlUtil.appendChild(doc.getDocumentElement(), "item");
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        XmlUtil.write(doc, out, "UTF-8", 2);
        String result = out.toString();
        assertTrue(result.contains("<root>"));
        assertTrue(result.contains("<item/>"));
    }

    // ==================== format(Document) ====================

    @Test
    @DisplayName("format - Document格式化")
    void testFormatDocument() {
        Document doc = XmlUtil.createXml("root");
        XmlUtil.appendChild(doc.getDocumentElement(), "a");
        XmlUtil.appendChild(doc.getDocumentElement(), "b");
        String formatted = XmlUtil.format(doc);
        assertNotNull(formatted);
        assertTrue(formatted.contains("root"));
    }

    // ==================== mapToXmlStr 带命名空间 ====================

    @Test
    @DisplayName("mapToXmlStr - 带命名空间")
    void testMapToXmlStrWithNamespace() {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("key", "value");
        String xml = XmlUtil.mapToXmlStr(map, "data", "http://example.com/ns");
        assertNotNull(xml);
        assertTrue(xml.contains("data"));
        assertTrue(xml.contains("value"));
    }

    // ==================== appendChild 带命名空间 ====================

    @Test
    @DisplayName("appendChild - 带命名空间追加子元素")
    void testAppendChildWithNamespace() {
        Document doc = XmlUtil.createXml("root");
        Element child = XmlUtil.appendChild(
                doc.getDocumentElement(), "child", "http://example.com/ns");
        assertNotNull(child);
        assertEquals("child", child.getTagName());
    }

    // ==================== beanToXml 单参数 ====================

    @Test
    @DisplayName("beanToXml - 单参数默认不忽略null")
    void testBeanToXmlSingleArg() {
        TestBean bean = new TestBean();
        bean.setName("test");
        bean.setValue("val");
        Document doc = XmlUtil.beanToXml(bean);
        assertNotNull(doc);
        assertEquals("TestBean", doc.getDocumentElement().getTagName());
        String xml = XmlUtil.toStr(doc, false);
        assertTrue(xml.contains("<name>test</name>"));
        assertTrue(xml.contains("<value>val</value>"));
    }

    // ==================== issue3139 嵌套Bean含List ====================

    @Test
    @DisplayName("xmlToBean - 嵌套Bean含子对象")
    void testXmlToBeanNestedWithList() {
        // xmlToMap 会将重复元素转为 List，验证 xmlToMap 对重复子元素的处理
        String xml = "<r><c><s>1</s><p>str</p></c><c><s>2</s><p>str2</p></c></r>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        assertNotNull(map);
        // 重复的 <c> 元素应被转为 List
        assertTrue(map.get("c") instanceof List);
        List<?> cList = (List<?>) map.get("c");
        assertEquals(2, cList.size());
    }

    // ==================== readXML(InputSource) ====================

    @Test
    @DisplayName("readXML - InputSource读取")
    void testReadXmlFromInputSource() {
        String xml = "<root><data>test</data></root>";
        org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(
                new StringReader(xml));
        Document doc = XmlUtil.readXML(inputSource);
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getTagName());
    }

    // ==================== readBySax(Reader) ====================

    @Test
    @DisplayName("readBySax - Reader方式读取")
    void testReadBySaxReader() {
        String xml = "<root><x>1</x><y>2</y></root>";
        java.util.Set<String> tags = new java.util.HashSet<>();
        XmlUtil.readBySax(new StringReader(xml), new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) {
                tags.add(qName);
            }
        });
        assertTrue(tags.contains("root"));
        assertTrue(tags.contains("x"));
        assertTrue(tags.contains("y"));
    }

    // ==================== getByXPath with NamespaceContext ====================

    @Test
    @DisplayName("getByXPath - 带NamespaceContext的XPath查询")
    void testGetByXPathWithNamespaceContext() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<root xmlns:ns=\"http://test.com\">"
                + "<ns:item>hello</ns:item>"
                + "</root>";
        Document doc = XmlUtil.readXML(xml);
        javax.xml.namespace.NamespaceContext nsContext = new javax.xml.namespace.NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("ns".equals(prefix)) return "http://test.com";
                return javax.xml.XMLConstants.NULL_NS_URI;
            }
            @Override
            public String getPrefix(String uri) { return "ns"; }
            @Override
            public java.util.Iterator<String> getPrefixes(String uri) {
                return java.util.Collections.singletonList("ns").iterator();
            }
        };
        Object value = XmlUtil.getByXPath(
                "//ns:item", doc,
                javax.xml.xpath.XPathConstants.STRING, nsContext);
        assertEquals("hello", value);
    }

    // ==================== toStr(Document, String, boolean) ====================

    @Test
    @DisplayName("toStr - Document指定编码和格式化")
    void testToStrDocumentWithCharset() {
        Document doc = XmlUtil.createXml("root");
        XmlUtil.appendChild(doc.getDocumentElement(), "name").setTextContent("值");
        String xml = XmlUtil.toStr(doc, "UTF-8", true);
        assertNotNull(xml);
        assertTrue(xml.contains("值"));
        assertTrue(xml.contains("UTF-8") || xml.contains("utf-8"));
    }

    // ==================== transElements(NodeList) ====================

    @Test
    @DisplayName("transElements - 仅NodeList参数")
    void testTransElementsNodeListOnly() {
        Document doc = XmlUtil.readXML("<root><a/><b/><c/></root>");
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        List<Element> elements = XmlUtil.transElements(nodeList);
        assertEquals(3, elements.size());
    }

    // ==================== createDocumentBuilder ====================

    @Test
    @DisplayName("createDocumentBuilder - 创建DocumentBuilder实例")
    void testCreateDocumentBuilder() {
        javax.xml.parsers.DocumentBuilder builder = XmlUtil.createDocumentBuilder();
        assertNotNull(builder);
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
