package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.map.BiMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * XML工具类<br>
 * 此工具使用w3c dom工具，不需要依赖第三方包。<br>
 * 工具类封装了XML文档的创建、读取、写出和部分XML操作
 *
 * <p>迁移自 hutool {@code cn.hutool.core.util.XmlUtil}.
 *
 * @author xiaoleilu
 */
public class XmlUtil {

	/** 字符串常量：XML 不间断空格转义 {@code "&nbsp;" -> " "} */
	public static final String NBSP = "&nbsp;";
	/** 字符串常量：XML And 符转义 {@code "&amp;" -> "&"} */
	public static final String AMP = "&amp;";
	/** 字符串常量：XML 双引号转义 {@code "&quot;" -> "\""} */
	public static final String QUOTE = "&quot;";
	/** 字符串常量：XML 单引号转义 {@code "&apos" -> "'"} */
	public static final String APOS = "&apos;";
	/** 字符串常量：XML 小于号转义 {@code "&lt;" -> "<"} */
	public static final String LT = "&lt;";
	/** 字符串常量：XML 大于号转义 {@code "&gt;" -> ">"} */
	public static final String GT = "&gt;";
	/** 在XML中无效的字符 正则 */
	public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
	/** 在XML中注释的内容 正则 */
	public static final String COMMENT_REGEX = "(?s)<!--.+?-->";
	/** XML格式化输出默认缩进量 */
	public static final int INDENT_DEFAULT = 2;

	private static String defaultDocumentBuilderFactory = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
	private static boolean namespaceAware = true;
	private static SAXParserFactory factory;

	synchronized public static void disableDefaultDocumentBuilderFactory() {
		defaultDocumentBuilderFactory = null;
	}

	synchronized public static void setNamespaceAware(boolean isNamespaceAware) {
		namespaceAware = isNamespaceAware;
	}

	// -------------------------------------------------------------------------------------- Read

	public static Document readXML(File file) {
		Assert.notNull(file, "Xml file is null !");
		if (false == file.exists()) {
			throw new UtilException("File [{}] not a exist!", file.getAbsolutePath());
		}
		if (false == file.isFile()) {
			throw new UtilException("[{}] not a file!", file.getAbsolutePath());
		}
		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			// ignore
		}
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(FileUtil.getInputStream(file));
			return readXML(in);
		} finally {
			IoUtil.close(in);
		}
	}

	public static Document readXML(String pathOrContent) {
		if (StrUtil.startWith(pathOrContent, '<')) {
			return parseXml(pathOrContent);
		}
		return readXML(new File(pathOrContent));
	}

	public static Document readXML(InputStream inputStream) throws UtilException {
		return readXML(new InputSource(inputStream));
	}

	public static Document readXML(Reader reader) throws UtilException {
		return readXML(new InputSource(reader));
	}

	public static Document readXML(InputSource source) {
		final DocumentBuilder builder = createDocumentBuilder();
		try {
			return builder.parse(source);
		} catch (Exception e) {
			throw new UtilException(e, "Parse XML from stream error!");
		}
	}

	public static void readBySax(File file, ContentHandler contentHandler) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			readBySax(new InputSource(in), contentHandler);
		} finally {
			IoUtil.close(in);
		}
	}

	public static void readBySax(Reader reader, ContentHandler contentHandler) {
		try {
			readBySax(new InputSource(reader), contentHandler);
		} finally {
			IoUtil.close(reader);
		}
	}

	public static void readBySax(InputStream source, ContentHandler contentHandler) {
		try {
			readBySax(new InputSource(source), contentHandler);
		} finally {
			IoUtil.close(source);
		}
	}

	public static void readBySax(InputSource source, ContentHandler contentHandler) {
		if (null == factory) {
			factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(namespaceAware);
			try {
				factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			} catch (final Exception ignore) {
				// ignore
			}
		}
		final SAXParser parse;
		XMLReader reader;
		try {
			parse = factory.newSAXParser();
			if (contentHandler instanceof DefaultHandler) {
				parse.parse(source, (DefaultHandler) contentHandler);
				return;
			}
			reader = parse.getXMLReader();
			reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
			reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			reader.setContentHandler(contentHandler);
			reader.parse(source);
		} catch (ParserConfigurationException | SAXException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	public static Document parseXml(String xmlStr) {
		if (StrUtil.isBlank(xmlStr)) {
			throw new IllegalArgumentException("XML content string is empty !");
		}
		xmlStr = cleanInvalid(xmlStr);
		return readXML(StrUtil.getReader(xmlStr));
	}

	// -------------------------------------------------------------------------------------- Write

	public static String toStr(Node doc) {
		return toStr(doc, false);
	}

	public static String toStr(Document doc) {
		return toStr((Node) doc);
	}

	public static String toStr(Node doc, boolean isPretty) {
		return toStr(doc, CharsetUtil.UTF_8, isPretty);
	}

	public static String toStr(Document doc, boolean isPretty) {
		return toStr((Node) doc, isPretty);
	}

	public static String toStr(Node doc, String charset, boolean isPretty) {
		return toStr(doc, charset, isPretty, false);
	}

	public static String toStr(Document doc, String charset, boolean isPretty) {
		return toStr((Node) doc, charset, isPretty);
	}

	public static String toStr(Node doc, String charset, boolean isPretty, boolean omitXmlDeclaration) {
		final StringWriter writer = StrUtil.getWriter();
		try {
			write(doc, writer, charset, isPretty ? INDENT_DEFAULT : 0, omitXmlDeclaration);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return writer.toString();
	}

	public static String format(Document doc) {
		return toStr(doc, true);
	}

	public static String format(String xmlStr) {
		return format(parseXml(xmlStr));
	}

	public static void toFile(Document doc, String absolutePath) {
		toFile(doc, absolutePath, null);
	}

	public static void toFile(Document doc, String path, String charsetName) {
		if (StrUtil.isBlank(charsetName)) {
			charsetName = doc.getXmlEncoding();
		}
		if (StrUtil.isBlank(charsetName)) {
			charsetName = CharsetUtil.UTF_8;
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new java.io.OutputStreamWriter(
				new java.io.FileOutputStream(FileUtil.touch(path)), CharsetUtil.charset(charsetName)));
			write(doc, writer, charsetName, INDENT_DEFAULT);
		} catch (IOException e) {
			throw new UtilException(e, "Write xml to file error!");
		} finally {
			IoUtil.close(writer);
		}
	}

	public static void write(Node node, Writer writer, String charset, int indent) {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent);
	}

	public static void write(Node node, Writer writer, String charset, int indent, boolean omitXmlDeclaration) {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
	}

	public static void write(Node node, OutputStream out, String charset, int indent) {
		transform(new DOMSource(node), new StreamResult(out), charset, indent);
	}

	public static void write(Node node, OutputStream out, String charset, int indent, boolean omitXmlDeclaration) {
		transform(new DOMSource(node), new StreamResult(out), charset, indent, omitXmlDeclaration);
	}

	public static void transform(Source source, Result result, String charset, int indent) {
		transform(source, result, charset, indent, false);
	}

	public static void transform(Source source, Result result, String charset, int indent, boolean omitXmlDeclaration) {
		final TransformerFactory factory = TransformerFactory.newInstance();
		try {
			final Transformer xformer = factory.newTransformer();
			if (indent > 0) {
				xformer.setOutputProperty(OutputKeys.INDENT, "yes");
				xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
			}
			if (StrUtil.isNotBlank(charset)) {
				xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			}
			if (omitXmlDeclaration) {
				xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			xformer.transform(source, result);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
	}

	// -------------------------------------------------------------------------------------- Create

	public static Document createXml() {
		return createDocumentBuilder().newDocument();
	}

	public static DocumentBuilder createDocumentBuilder() {
		DocumentBuilder builder;
		try {
			builder = createDocumentBuilderFactory().newDocumentBuilder();
		} catch (Exception e) {
			throw new UtilException(e, "Create xml document error!");
		}
		return builder;
	}

	public static DocumentBuilderFactory createDocumentBuilderFactory() {
		final DocumentBuilderFactory factory;
		if (StrUtil.isNotEmpty(defaultDocumentBuilderFactory)) {
			factory = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
		} else {
			factory = DocumentBuilderFactory.newInstance();
		}
		factory.setNamespaceAware(namespaceAware);
		return disableXXE(factory);
	}

	public static Document createXml(String rootElementName) {
		return createXml(rootElementName, null);
	}

	public static Document createXml(String rootElementName, String namespace) {
		final Document doc = createXml();
		doc.appendChild(null == namespace ? doc.createElement(rootElementName) : doc.createElementNS(namespace, rootElementName));
		return doc;
	}

	// -------------------------------------------------------------------------------------- Function

	public static Element getRootElement(Document doc) {
		return (null == doc) ? null : doc.getDocumentElement();
	}

	public static Document getOwnerDocument(Node node) {
		return (node instanceof Document) ? (Document) node : node.getOwnerDocument();
	}

	public static String cleanInvalid(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(INVALID_REGEX, "");
	}

	public static String cleanComment(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(COMMENT_REGEX, StrUtil.EMPTY);
	}

	public static List<Element> getElements(Element element, String tagName) {
		final NodeList nodeList = StrUtil.isBlank(tagName) ? element.getChildNodes() : element.getElementsByTagName(tagName);
		return transElements(element, nodeList);
	}

	public static Element getElement(Element element, String tagName) {
		final NodeList nodeList = element.getElementsByTagName(tagName);
		final int length = nodeList.getLength();
		if (length < 1) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			Element childEle = (Element) nodeList.item(i);
			if (childEle == null || childEle.getParentNode() == element) {
				return childEle;
			}
		}
		return null;
	}

	public static String elementText(Element element, String tagName) {
		Element child = getElement(element, tagName);
		return child == null ? null : child.getTextContent();
	}

	public static String elementText(Element element, String tagName, String defaultValue) {
		Element child = getElement(element, tagName);
		return child == null ? defaultValue : child.getTextContent();
	}

	public static List<Element> transElements(NodeList nodeList) {
		return transElements(null, nodeList);
	}

	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		int length = nodeList.getLength();
		final ArrayList<Element> elements = new ArrayList<>(length);
		Node node;
		Element element;
		for (int i = 0; i < length; i++) {
			node = nodeList.item(i);
			if (Node.ELEMENT_NODE == node.getNodeType()) {
				element = (Element) nodeList.item(i);
				if (parentEle == null || element.getParentNode() == parentEle) {
					elements.add(element);
				}
			}
		}
		return elements;
	}

	public static void writeObjectAsXml(File dest, Object bean) {
		XMLEncoder xmlenc = null;
		try {
			xmlenc = new XMLEncoder(FileUtil.getOutputStream(dest));
			xmlenc.writeObject(bean);
		} finally {
			if (xmlenc != null) {
				xmlenc.close();
			}
		}
	}

	public static XPath createXPath() {
		return XPathFactory.newInstance().newXPath();
	}

	public static Element getElementByXPath(String expression, Object source) {
		return (Element) getNodeByXPath(expression, source);
	}

	public static NodeList getNodeListByXPath(String expression, Object source) {
		return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
	}

	public static Node getNodeByXPath(String expression, Object source) {
		return (Node) getByXPath(expression, source, XPathConstants.NODE);
	}

	public static Object getByXPath(String expression, Object source, QName returnType) {
		NamespaceContext nsContext = null;
		if (source instanceof Node) {
			nsContext = new UniversalNamespaceCache((Node) source, false);
		}
		return getByXPath(expression, source, returnType, nsContext);
	}

	public static Object getByXPath(String expression, Object source, QName returnType, NamespaceContext nsContext) {
		final XPath xPath = createXPath();
		if (null != nsContext) {
			xPath.setNamespaceContext(nsContext);
		}
		try {
			if (source instanceof InputSource) {
				return xPath.evaluate(expression, (InputSource) source, returnType);
			} else {
				return xPath.evaluate(expression, source, returnType);
			}
		} catch (XPathExpressionException e) {
			throw new UtilException(e);
		}
	}

	public static String escape(String string) {
		return EscapeUtil.escapeXml(string);
	}

	public static String unescape(String string) {
		return EscapeUtil.unescapeXml(string);
	}

	// -------------------------------------------------------------------------------------- XML <-> Map/Bean

	public static Map<String, Object> xmlToMap(String xmlStr) {
		return xmlToMap(xmlStr, new HashMap<>());
	}

	/**
	 * XML转Java Bean（使用Java原生Introspector实现，不依赖hutool BeanUtil）
	 *
	 * @param <T>  bean类型
	 * @param node XML节点
	 * @param bean bean类
	 * @return bean
	 */
	public static <T> T xmlToBean(Node node, Class<T> bean) {
		return xmlToBean(node, bean, false);
	}

	/**
	 * XML转Java Bean
	 *
	 * @param <T>         bean类型
	 * @param node        XML节点
	 * @param bean        bean类
	 * @param ignoreError 是否忽略转换错误
	 * @return bean
	 */
	public static <T> T xmlToBean(Node node, Class<T> bean, boolean ignoreError) {
		final Map<String, Object> map = xmlToMap(node);
		if (null != map && map.size() == 1) {
			final String simpleName = bean.getSimpleName();
			final String nodeName = CollUtil.getFirst(map.keySet());
			if (simpleName.equalsIgnoreCase(nodeName)) {
				return simpleMapToBean(map.get(nodeName), bean, ignoreError);
			}
		}
		return simpleMapToBean(map, bean, ignoreError);
	}

	public static Map<String, Object> xmlToMap(Node node) {
		return xmlToMap(node, new HashMap<>());
	}

	public static Map<String, Object> xmlToMap(String xmlStr, Map<String, Object> result) {
		final Document doc = parseXml(xmlStr);
		final Element root = getRootElement(doc);
		root.normalize();
		return xmlToMap(root, result);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(Node node, Map<String, Object> result) {
		if (null == result) {
			result = new HashMap<>();
		}
		final NodeList nodeList = node.getChildNodes();
		final int length = nodeList.getLength();
		Node childNode;
		Element childEle;
		for (int i = 0; i < length; ++i) {
			childNode = nodeList.item(i);
			if (false == isElement(childNode)) {
				continue;
			}
			childEle = (Element) childNode;
			final Object value = result.get(childEle.getNodeName());
			Object newValue;
			if (childEle.hasChildNodes()) {
				final Map<String, Object> map = xmlToMap(childEle);
				if (MapUtil.isNotEmpty(map)) {
					newValue = map;
				} else {
					newValue = childEle.getTextContent();
				}
			} else {
				newValue = childEle.getTextContent();
			}
			if (null != newValue) {
				if (null != value) {
					if (value instanceof List) {
						((List<Object>) value).add(newValue);
					} else {
						result.put(childEle.getNodeName(), CollUtil.newArrayList(value, newValue));
					}
				} else {
					result.put(childEle.getNodeName(), newValue);
				}
			}
		}
		return result;
	}

	public static String mapToXmlStr(Map<?, ?> data) {
		return toStr(mapToXml(data, "xml"));
	}

	public static String mapToXmlStr(Map<?, ?> data, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, "xml"), CharsetUtil.UTF_8, false, omitXmlDeclaration);
	}

	public static String mapToXmlStr(Map<?, ?> data, String rootName) {
		return toStr(mapToXml(data, rootName));
	}

	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace) {
		return toStr(mapToXml(data, rootName, namespace));
	}

	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, false, omitXmlDeclaration);
	}

	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean isPretty, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, isPretty, omitXmlDeclaration);
	}

	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, String charset, boolean isPretty, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), charset, isPretty, omitXmlDeclaration);
	}

	public static Document mapToXml(Map<?, ?> data, String rootName) {
		return mapToXml(data, rootName, null);
	}

	public static Document mapToXml(Map<?, ?> data, String rootName, String namespace) {
		final Document doc = createXml();
		final Element root = appendChild(doc, rootName, namespace);
		appendMap(doc, root, data);
		return doc;
	}

	/**
	 * 将Bean转换为XML（使用Java原生Introspector实现，不依赖hutool BeanUtil）
	 *
	 * @param bean Bean对象
	 * @return XML
	 */
	public static Document beanToXml(Object bean) {
		return beanToXml(bean, null);
	}

	public static Document beanToXml(Object bean, String namespace) {
		return beanToXml(bean, namespace, false);
	}

	public static Document beanToXml(Object bean, String namespace, boolean ignoreNull) {
		if (null == bean) {
			return null;
		}
		return mapToXml(simpleBeanToMap(bean, ignoreNull), bean.getClass().getSimpleName(), namespace);
	}

	public static boolean isElement(Node node) {
		return (null != node) && Node.ELEMENT_NODE == node.getNodeType();
	}

	public static Element appendChild(Node node, String tagName) {
		return appendChild(node, tagName, null);
	}

	public static Element appendChild(Node node, String tagName, String namespace) {
		final Document doc = getOwnerDocument(node);
		final Element child = (null == namespace) ? doc.createElement(tagName) : doc.createElementNS(namespace, tagName);
		node.appendChild(child);
		return child;
	}

	public static Node appendText(Node node, CharSequence text) {
		return appendText(getOwnerDocument(node), node, text);
	}

	public static void append(Node node, Object data) {
		append(getOwnerDocument(node), node, data);
	}

	// ---------------------------------------------------------------------------------------- Private method start

	@SuppressWarnings("rawtypes")
	private static void append(Document doc, Node node, Object data) {
		if (data instanceof Map) {
			appendMap(doc, node, (Map) data);
		} else if (data instanceof Iterator) {
			appendIterator(doc, node, (Iterator) data);
		} else if (data instanceof Iterable) {
			appendIterator(doc, node, ((Iterable) data).iterator());
		} else {
			appendText(doc, node, data.toString());
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void appendMap(Document doc, Node node, Map data) {
		data.forEach((key, value) -> {
			if (null != key) {
				final Element child = appendChild(node, key.toString());
				if (null != value) {
					append(doc, child, value);
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private static void appendIterator(Document doc, Node node, Iterator data) {
		final Node parentNode = node.getParentNode();
		boolean isFirst = true;
		Object eleData;
		while (data.hasNext()) {
			eleData = data.next();
			if (isFirst) {
				append(doc, node, eleData);
				isFirst = false;
			} else {
				final Node cloneNode = node.cloneNode(false);
				parentNode.appendChild(cloneNode);
				append(doc, cloneNode, eleData);
			}
		}
	}

	private static Node appendText(Document doc, Node node, CharSequence text) {
		return node.appendChild(doc.createTextNode(StrUtil.str(text)));
	}

	private static DocumentBuilderFactory disableXXE(DocumentBuilderFactory dbf) {
		String feature;
		try {
			feature = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(feature, true);
			feature = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(feature, false);
			feature = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(feature, false);
			feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(feature, false);
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			// ignore
		}
		return dbf;
	}

	// ---------------------------------------------------------------------------------------- Simple Bean <-> Map (替代hutool BeanUtil)

	/**
	 * 简单Map转Bean（使用Java原生Introspector，替代hutool BeanUtil.toBean）
	 */
	@SuppressWarnings("unchecked")
	private static <T> T simpleMapToBean(Object source, Class<T> beanClass, boolean ignoreError) {
		if (null == source) {
			return null;
		}
		try {
			final T bean = beanClass.getDeclaredConstructor().newInstance();
			if (source instanceof Map) {
				final Map<String, Object> map = (Map<String, Object>) source;
				final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
				final PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					final String name = pd.getName();
					if ("class".equals(name)) {
						continue;
					}
					final Object value = map.get(name);
					if (null != value) {
						final Method setter = pd.getWriteMethod();
						if (null != setter) {
							try {
								setter.invoke(bean, value);
							} catch (Exception e) {
								if (!ignoreError) {
									throw new UtilException(e, "Set bean property [{}] error!", name);
								}
							}
						}
					}
				}
			}
			return bean;
		} catch (UtilException e) {
			throw e;
		} catch (Exception e) {
			throw new UtilException(e, "Convert map to bean error!");
		}
	}

	/**
	 * 简单Bean转Map（使用Java原生Introspector，替代hutool BeanUtil.beanToMap）
	 */
	private static Map<String, Object> simpleBeanToMap(Object bean, boolean ignoreNullValue) {
		final Map<String, Object> result = new HashMap<>();
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			final PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				final String name = pd.getName();
				if ("class".equals(name)) {
					continue;
				}
				final Method getter = pd.getReadMethod();
				if (null != getter) {
					final Object value = getter.invoke(bean);
					if (null != value || !ignoreNullValue) {
						result.put(name, value);
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e, "Convert bean to map error!");
		}
		return result;
	}

	// ---------------------------------------------------------------------------------------- Private method end

	/**
	 * 全局命名空间上下文<br>
	 * 见：https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/
	 */
	public static class UniversalNamespaceCache implements NamespaceContext {
		private static final String DEFAULT_NS = "DEFAULT";
		private final BiMap<String, String> prefixUri = new BiMap<>(new HashMap<>());

		public UniversalNamespaceCache(Node node, boolean toplevelOnly) {
			examineNode(node.getFirstChild(), toplevelOnly);
		}

		private void examineNode(Node node, boolean attributesOnly) {
			final NamedNodeMap attributes = node.getAttributes();
			if (null != attributes) {
				final int length = attributes.getLength();
				for (int i = 0; i < length; i++) {
					Node attribute = attributes.item(i);
					storeAttribute(attribute);
				}
			}
			if (false == attributesOnly) {
				final NodeList childNodes = node.getChildNodes();
				if (null != childNodes) {
					Node item;
					final int childLength = childNodes.getLength();
					for (int i = 0; i < childLength; i++) {
						item = childNodes.item(i);
						if (item.getNodeType() == Node.ELEMENT_NODE)
							examineNode(item, false);
					}
				}
			}
		}

		private void storeAttribute(Node attribute) {
			if (null == attribute) {
				return;
			}
			if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
				if (XMLConstants.XMLNS_ATTRIBUTE.equals(attribute.getNodeName())) {
					prefixUri.put(DEFAULT_NS, attribute.getNodeValue());
				} else {
					prefixUri.put(attribute.getLocalName(), attribute.getNodeValue());
				}
			}
		}

		@Override
		public String getNamespaceURI(String prefix) {
			if (prefix == null || XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
				return prefixUri.get(DEFAULT_NS);
			} else {
				return prefixUri.get(prefix);
			}
		}

		@Override
		public String getPrefix(String namespaceURI) {
			return prefixUri.getInverse().get(namespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			return null;
		}
	}
}
