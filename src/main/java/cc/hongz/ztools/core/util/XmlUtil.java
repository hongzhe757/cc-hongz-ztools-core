package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.bean.BeanUtil;
import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.map.BiMap;
import cc.hongz.ztools.core.map.MapUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * XML工具类<br>
 * 此工具使用w3c dom工具，不需要依赖第三方包。<br>
 * 工具类封装了XML文档的创建、读取、写出和部分XML操作.
 *
 * <p>迁移自 hutool {@code cn.hutool.core.util.XmlUtil}.
 *
 * @author xiaoleilu
 * @see JAXBUtil
 */
public final class XmlUtil {

    /**
     * 字符串常量：XML 不间断空格转义 {@code "&nbsp;" -> " "}.
     */
    public static final String NBSP = "&nbsp;";
    /**
     * 字符串常量：XML And 符转义 {@code "&amp;" -> "&"}.
     */
    public static final String AMP = "&amp;";
    /**
     * 字符串常量：XML 双引号转义 {@code "&quot;" -> "\""}.
     */
    public static final String QUOTE = "&quot;";
    /**
     * 字符串常量：XML 单引号转义 {@code "&apos" -> "'"}.
     */
    public static final String APOS = "&apos;";
    /**
     * 字符串常量：XML 小于号转义 {@code "&lt;" -> "<"}.
     */
    public static final String LT = "&lt;";
    /**
     * 字符串常量：XML 大于号转义 {@code "&gt;" -> ">"}.
     */
    public static final String GT = "&gt;";
    /**
     * 在XML中无效的字符 正则.
     */
    public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
    /**
     * 在XML中注释的内容 正则.
     */
    public static final String COMMENT_REGEX = "(?s)<!--.+?-->";
    /**
     * XML格式化输出默认缩进量.
     */
    public static final int INDENT_DEFAULT = 2;

    /**
     * 默认的DocumentBuilderFactory实现.
     */
    private static String defaultDocumentBuilderFactory = "com.sun.org.apache.xerces.internal.jaxp" + ".DocumentBuilderFactoryImpl";
    /**
     * 是否打开命名空间支持.
     */
    private static boolean namespaceAware = true;
    /**
     * Sax读取器工厂缓存.
     */
    private static SAXParserFactory saxFactory;

    private XmlUtil() {
    }

    /**
     * 禁用默认的DocumentBuilderFactory，
     * 禁用后如果有第三方的实现，将会自动加载实现.
     */
    public static synchronized void disableDefaultDocumentBuilderFactory() {
        defaultDocumentBuilderFactory = null;
    }

    /**
     * 设置是否打开命名空间支持，默认打开.
     *
     * @param isNamespaceAware 是否命名空间支持
     */
    public static synchronized void setNamespaceAware(final boolean isNamespaceAware) {
        namespaceAware = isNamespaceAware;
    }

    // ----------------------------------------------------------------- Read

    /**
     * 读取解析XML文件.
     *
     * @param file XML文件
     * @return XML文档对象
     */
    public static Document readXML(final File file) {
        Assert.notNull(file, "Xml file is null !");
        final String absolutePath = file.getAbsolutePath();
        if (!file.exists()) {
            throw new UtilException("File [{}] not a exist!", absolutePath);
        }
        if (!file.isFile()) {
            throw new UtilException("[{}] not a file!", absolutePath);
        }

        File fileToRead;
        try {
            fileToRead = file.getCanonicalFile();
        } catch (final IOException e) {
            fileToRead = file;
        }

        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(FileUtil.getInputStream(fileToRead));
            return readXML(in);
        } finally {
            IoUtil.close(in);
        }
    }

    /**
     * 读取解析XML文件<br>
     * 如果给定内容以"&lt;"开头，表示这是一个XML内容，直接读取，
     * 否则按照路径处理<br>
     * 路径可以为相对路径，也可以是绝对路径，相对路径相对于ClassPath.
     *
     * @param pathOrContent 内容或路径
     * @return XML文档对象
     */
    public static Document readXML(final String pathOrContent) {
        if (StrUtil.startWith(pathOrContent, '<')) {
            return parseXml(pathOrContent);
        }
        return readXML(FileUtil.file(pathOrContent));
    }

    /**
     * 读取解析XML文件<br>
     * 编码在XML中定义.
     *
     * @param inputStream XML流
     * @return XML文档对象
     * @throws UtilException IO异常或转换异常
     */
    public static Document readXML(final InputStream inputStream) throws UtilException {
        return readXML(new InputSource(inputStream));
    }

    /**
     * 读取解析XML文件.
     *
     * @param reader XML流
     * @return XML文档对象
     * @throws UtilException IO异常或转换异常
     */
    public static Document readXML(final Reader reader) throws UtilException {
        return readXML(new InputSource(reader));
    }

    /**
     * 读取解析XML文件<br>
     * 编码在XML中定义.
     *
     * @param source {@link InputSource}
     * @return XML文档对象
     */
    public static Document readXML(final InputSource source) {
        final DocumentBuilder builder = createDocumentBuilder();
        try {
            return builder.parse(source);
        } catch (final Exception e) {
            throw new UtilException(e, "Parse XML from stream error!");
        }
    }

    /**
     * 使用Sax方式读取指定的XML<br>
     * 如果用户传入的contentHandler为{@link DefaultHandler}，
     * 则其接口都会被处理.
     *
     * @param file           XML源文件,使用后自动关闭
     * @param contentHandler XML流处理器
     */
    public static void readBySax(final File file, final ContentHandler contentHandler) {
        InputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            readBySax(new InputSource(in), contentHandler);
        } finally {
            IoUtil.close(in);
        }
    }

    /**
     * 使用Sax方式读取指定的XML<br>
     * 如果用户传入的contentHandler为{@link DefaultHandler}，
     * 则其接口都会被处理.
     *
     * @param reader         XML源Reader,使用后自动关闭
     * @param contentHandler XML流处理器
     */
    public static void readBySax(final Reader reader, final ContentHandler contentHandler) {
        try {
            readBySax(new InputSource(reader), contentHandler);
        } finally {
            IoUtil.close(reader);
        }
    }

    /**
     * 使用Sax方式读取指定的XML<br>
     * 如果用户传入的contentHandler为{@link DefaultHandler}，
     * 则其接口都会被处理.
     *
     * @param source         XML源流,使用后自动关闭
     * @param contentHandler XML流处理器
     */
    public static void readBySax(final InputStream source, final ContentHandler contentHandler) {
        try {
            readBySax(new InputSource(source), contentHandler);
        } finally {
            IoUtil.close(source);
        }
    }

    /**
     * 使用Sax方式读取指定的XML<br>
     * 如果用户传入的contentHandler为{@link DefaultHandler}，
     * 则其接口都会被处理.
     *
     * @param source         XML源
     * @param contentHandler XML流处理器
     */
    public static void readBySax(final InputSource source, final ContentHandler contentHandler) {
        if (null == saxFactory) {
            saxFactory = SAXParserFactory.newInstance();
            saxFactory.setValidating(false);
            saxFactory.setNamespaceAware(namespaceAware);
            try {
                saxFactory.setFeature("http://apache.org/xml/features/" + "disallow-doctype-decl", true);
                saxFactory.setFeature("http://xml.org/sax/features/" + "external-general-entities", false);
                saxFactory.setFeature("http://xml.org/sax/features/" + "external-parameter-entities", false);
                saxFactory.setFeature("http://apache.org/xml/features/" + "nonvalidating/load-external-dtd", false);
            } catch (final Exception ignore) {
                // ignore
            }
        }
        final SAXParser parse;
        XMLReader reader;
        try {
            parse = saxFactory.newSAXParser();
            if (contentHandler instanceof DefaultHandler) {
                parse.parse(source, (DefaultHandler) contentHandler);
                return;
            }
            reader = parse.getXMLReader();
            reader.setFeature("http://apache.org/xml/features/" + "disallow-doctype-decl", true);
            reader.setFeature("http://apache.org/xml/features/" + "nonvalidating/load-external-dtd", false);
            reader.setFeature("http://xml.org/sax/features/" + "external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/" + "external-parameter-entities", false);
            reader.setContentHandler(contentHandler);
            reader.parse(source);
        } catch (final ParserConfigurationException | SAXException e) {
            throw new UtilException(e);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将String类型的XML转换为XML文档.
     *
     * @param xmlStr XML字符串
     * @return XML文档
     */
    public static Document parseXml(final String xmlStr) {
        if (StrUtil.isBlank(xmlStr)) {
            throw new IllegalArgumentException("XML content string is empty !");
        }
        final String cleaned = cleanInvalid(xmlStr);
        return readXML(StrUtil.getReader(cleaned));
    }

    // ----------------------------------------------------------------- Write

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8<br>
     * 默认非格式化输出，若想格式化请使用{@link #format(Document)}.
     *
     * @param doc XML文档
     * @return XML字符串
     */
    public static String toStr(final Node doc) {
        return toStr(doc, false);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8<br>
     * 默认非格式化输出，若想格式化请使用{@link #format(Document)}.
     *
     * @param doc XML文档
     * @return XML字符串
     */
    public static String toStr(final Document doc) {
        return toStr((Node) doc);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8.
     *
     * @param doc      XML文档
     * @param isPretty 是否格式化输出
     * @return XML字符串
     */
    public static String toStr(final Node doc, final boolean isPretty) {
        return toStr(doc, CharsetUtil.UTF_8, isPretty);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8.
     *
     * @param doc      XML文档
     * @param isPretty 是否格式化输出
     * @return XML字符串
     */
    public static String toStr(final Document doc, final boolean isPretty) {
        return toStr((Node) doc, isPretty);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8.
     *
     * @param doc      XML文档
     * @param charset  编码
     * @param isPretty 是否格式化输出
     * @return XML字符串
     */
    public static String toStr(final Node doc, final String charset, final boolean isPretty) {
        return toStr(doc, charset, isPretty, false);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8.
     *
     * @param doc      XML文档
     * @param charset  编码
     * @param isPretty 是否格式化输出
     * @return XML字符串
     */
    public static String toStr(final Document doc, final String charset, final boolean isPretty) {
        return toStr((Node) doc, charset, isPretty);
    }

    /**
     * 将XML文档转换为String<br>
     * 字符编码使用XML文档中的编码，获取不到则使用UTF-8.
     *
     * @param doc                XML文档
     * @param charset            编码
     * @param isPretty           是否格式化输出
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     * @return XML字符串
     */
    public static String toStr(final Node doc, final String charset, final boolean isPretty, final boolean omitXmlDeclaration) {
        final StringWriter writer = StrUtil.getWriter();
        try {
            final int indent = isPretty ? INDENT_DEFAULT : 0;
            write(doc, writer, charset, indent, omitXmlDeclaration);
        } catch (final Exception e) {
            throw new UtilException(e, "Trans xml document to string error!");
        }
        return writer.toString();
    }

    /**
     * 格式化XML输出.
     *
     * @param doc {@link Document} XML文档
     * @return 格式化后的XML字符串
     */
    public static String format(final Document doc) {
        return toStr(doc, true);
    }

    /**
     * 格式化XML输出.
     *
     * @param xmlStr XML字符串
     * @return 格式化后的XML字符串
     */
    public static String format(final String xmlStr) {
        return format(parseXml(xmlStr));
    }

    /**
     * 将XML文档写入到文件<br>
     * 使用Document中的编码.
     *
     * @param doc          XML文档
     * @param absolutePath 文件绝对路径，不存在会自动创建
     */
    public static void toFile(final Document doc, final String absolutePath) {
        toFile(doc, absolutePath, null);
    }

    /**
     * 将XML文档写入到文件.
     *
     * @param doc         XML文档
     * @param path        文件路径
     * @param charsetName 自定义XML文件的编码，可为{@code null}
     */
    public static void toFile(final Document doc, final String path, final String charsetName) {
        String charset = charsetName;
        if (StrUtil.isBlank(charset)) {
            charset = doc.getXmlEncoding();
        }
        if (StrUtil.isBlank(charset)) {
            charset = CharsetUtil.UTF_8;
        }
        BufferedWriter writer = null;
        try {
            writer = FileUtil.getWriter(path, CharsetUtil.charset(charset), false);
            write(doc, writer, charset, INDENT_DEFAULT);
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * 将XML文档写出.
     *
     * @param node    {@link Node} XML文档节点或文档本身
     * @param writer  写出的Writer
     * @param charset 编码
     * @param indent  缩进量，小于1表示不格式化输出
     */
    public static void write(final Node node, final Writer writer, final String charset, final int indent) {
        transform(new DOMSource(node), new StreamResult(writer), charset, indent);
    }

    /**
     * 将XML文档写出.
     *
     * @param node               {@link Node} XML节点或文档本身
     * @param writer             写出的Writer
     * @param charset            编码
     * @param indent             缩进量
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     */
    public static void write(final Node node, final Writer writer, final String charset, final int indent, final boolean omitXmlDeclaration) {
        transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
    }

    /**
     * 将XML文档写出.
     *
     * @param node    {@link Node} XML文档节点或文档本身
     * @param out     写出的OutputStream
     * @param charset 编码
     * @param indent  缩进量
     */
    public static void write(final Node node, final OutputStream out, final String charset, final int indent) {
        transform(new DOMSource(node), new StreamResult(out), charset, indent);
    }

    /**
     * 将XML文档写出.
     *
     * @param node               {@link Node} XML节点或文档本身
     * @param out                写出的OutputStream
     * @param charset            编码
     * @param indent             缩进量
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     */
    public static void write(final Node node, final OutputStream out, final String charset, final int indent, final boolean omitXmlDeclaration) {
        transform(new DOMSource(node), new StreamResult(out), charset, indent, omitXmlDeclaration);
    }

    /**
     * 将XML文档写出.
     *
     * @param source  源
     * @param result  目标
     * @param charset 编码
     * @param indent  缩进量
     */
    public static void transform(final Source source, final Result result, final String charset, final int indent) {
        transform(source, result, charset, indent, false);
    }

    /**
     * 将XML文档写出.
     *
     * @param source             源
     * @param result             目标
     * @param charset            编码
     * @param indent             缩进量
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     */
    public static void transform(final Source source, final Result result, final String charset, final int indent, final boolean omitXmlDeclaration) {
        final TransformerFactory tf = TransformerFactory.newInstance();
        try {
            final Transformer xformer = tf.newTransformer();
            if (indent > 0) {
                xformer.setOutputProperty(OutputKeys.INDENT, "yes");
                xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
                xformer.setOutputProperty("{http://xml.apache.org/xslt}" + "indent-amount", String.valueOf(indent));
            }
            if (StrUtil.isNotBlank(charset)) {
                xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            }
            if (omitXmlDeclaration) {
                xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            xformer.transform(source, result);
        } catch (final Exception e) {
            throw new UtilException(e, "Trans xml document to string error!");
        }
    }

    // ----------------------------------------------------------------- Create

    /**
     * 创建XML文档<br>
     * 创建的XML默认是utf8编码.
     *
     * @return XML文档
     */
    public static Document createXml() {
        return createDocumentBuilder().newDocument();
    }

    /**
     * 创建 DocumentBuilder.
     *
     * @return DocumentBuilder
     */
    public static DocumentBuilder createDocumentBuilder() {
        try {
            return createDocumentBuilderFactory().newDocumentBuilder();
        } catch (final Exception e) {
            throw new UtilException(e, "Create xml document error!");
        }
    }

    /**
     * 创建{@link DocumentBuilderFactory}.
     *
     * @return {@link DocumentBuilderFactory}
     */
    public static DocumentBuilderFactory createDocumentBuilderFactory() {
        final DocumentBuilderFactory dbf;
        if (StrUtil.isNotEmpty(defaultDocumentBuilderFactory)) {
            dbf = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
        } else {
            dbf = DocumentBuilderFactory.newInstance();
        }
        dbf.setNamespaceAware(namespaceAware);
        return disableXXE(dbf);
    }

    /**
     * 创建XML文档<br>
     * 创建的XML默认是utf8编码.
     *
     * @param rootElementName 根节点名称
     * @return XML文档
     */
    public static Document createXml(final String rootElementName) {
        return createXml(rootElementName, null);
    }

    /**
     * 创建XML文档<br>
     * 创建的XML默认是utf8编码.
     *
     * @param rootElementName 根节点名称
     * @param namespace       命名空间，无则传null
     * @return XML文档
     */
    public static Document createXml(final String rootElementName, final String namespace) {
        final Document doc = createXml();
        if (null == namespace) {
            doc.appendChild(doc.createElement(rootElementName));
        } else {
            doc.appendChild(doc.createElementNS(namespace, rootElementName));
        }
        return doc;
    }

    // --------------------------------------------------------------- Function

    /**
     * 获得XML文档根节点.
     *
     * @param doc {@link Document}
     * @return 根节点
     * @see Document#getDocumentElement()
     */
    public static Element getRootElement(final Document doc) {
        return (null == doc) ? null : doc.getDocumentElement();
    }

    /**
     * 获取节点所在的Document.
     *
     * @param node 节点
     * @return {@link Document}
     */
    public static Document getOwnerDocument(final Node node) {
        return (node instanceof Document) ? (Document) node : node.getOwnerDocument();
    }

    /**
     * 去除XML文本中的无效字符.
     *
     * @param xmlContent XML文本
     * @return 当传入为null时返回null
     */
    public static String cleanInvalid(final String xmlContent) {
        if (xmlContent == null) {
            return null;
        }
        return xmlContent.replaceAll(INVALID_REGEX, "");
    }

    /**
     * 去除XML文本中的注释内容.
     *
     * @param xmlContent XML文本
     * @return 当传入为null时返回null
     */
    public static String cleanComment(final String xmlContent) {
        if (xmlContent == null) {
            return null;
        }
        return xmlContent.replaceAll(COMMENT_REGEX, StrUtil.EMPTY);
    }

    /**
     * 根据节点名获得子节点列表.
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点列表
     */
    public static List<Element> getElements(final Element element, final String tagName) {
        final NodeList nodeList = StrUtil.isBlank(tagName) ? element.getChildNodes() : element.getElementsByTagName(tagName);
        return transElements(element, nodeList);
    }

    /**
     * 根据节点名获得第一个子节点.
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点
     */
    public static Element getElement(final Element element, final String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        final int length = nodeList.getLength();
        if (length < 1) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            final Element childEle = (Element) nodeList.item(i);
            if (childEle == null || childEle.getParentNode() == element) {
                return childEle;
            }
        }
        return null;
    }

    /**
     * 根据节点名获得第一个子节点的文本值.
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点中的值
     */
    public static String elementText(final Element element, final String tagName) {
        final Element child = getElement(element, tagName);
        return child == null ? null : child.getTextContent();
    }

    /**
     * 根据节点名获得第一个子节点的文本值.
     *
     * @param element      节点
     * @param tagName      节点名
     * @param defaultValue 默认值
     * @return 节点中的值
     */
    public static String elementText(final Element element, final String tagName, final String defaultValue) {
        final Element child = getElement(element, tagName);
        return child == null ? defaultValue : child.getTextContent();
    }

    /**
     * 将NodeList转换为Element列表.
     *
     * @param nodeList NodeList
     * @return Element列表
     */
    public static List<Element> transElements(final NodeList nodeList) {
        return transElements(null, nodeList);
    }

    /**
     * 将NodeList转换为Element列表<br>
     * 非Element节点将被忽略.
     *
     * @param parentEle 父节点
     * @param nodeList  NodeList
     * @return Element列表
     */
    public static List<Element> transElements(final Element parentEle, final NodeList nodeList) {
        final int length = nodeList.getLength();
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

    /**
     * 将可序列化的对象转换为XML写入文件.
     *
     * @param dest 目标文件
     * @param bean 对象
     */
    public static void writeObjectAsXml(final File dest, final Object bean) {
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

    /**
     * 创建XPath.
     *
     * @return {@link XPath}
     */
    public static XPath createXPath() {
        return XPathFactory.newInstance().newXPath();
    }

    /**
     * 通过XPath方式读取XML节点等信息.
     *
     * @param expression XPath表达式
     * @param source     资源
     * @return 匹配返回类型的值
     */
    public static Element getElementByXPath(final String expression, final Object source) {
        return (Element) getNodeByXPath(expression, source);
    }

    /**
     * 通过XPath方式读取XML的NodeList.
     *
     * @param expression XPath表达式
     * @param source     资源
     * @return NodeList
     */
    public static NodeList getNodeListByXPath(final String expression, final Object source) {
        return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
    }

    /**
     * 通过XPath方式读取XML节点等信息.
     *
     * @param expression XPath表达式
     * @param source     资源
     * @return 匹配返回类型的值
     */
    public static Node getNodeByXPath(final String expression, final Object source) {
        return (Node) getByXPath(expression, source, XPathConstants.NODE);
    }

    /**
     * 通过XPath方式读取XML节点等信息.
     *
     * @param expression XPath表达式
     * @param source     资源
     * @param returnType 返回类型
     * @return 匹配返回类型的值
     */
    public static Object getByXPath(final String expression, final Object source, final QName returnType) {
        NamespaceContext nsContext = null;
        if (source instanceof Node) {
            nsContext = new UniversalNamespaceCache((Node) source, false);
        }
        return getByXPath(expression, source, returnType, nsContext);
    }

    /**
     * 通过XPath方式读取XML节点等信息.
     *
     * @param expression XPath表达式
     * @param source     资源
     * @param returnType 返回类型
     * @param nsContext  {@link NamespaceContext}
     * @return 匹配返回类型的值
     */
    public static Object getByXPath(final String expression, final Object source, final QName returnType, final NamespaceContext nsContext) {
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
        } catch (final XPathExpressionException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 转义XML特殊字符.
     *
     * @param string 被替换的字符串
     * @return 替换后的字符串
     * @see EscapeUtil#escapeXml(CharSequence)
     */
    public static String escape(final String string) {
        return EscapeUtil.escapeXml(string);
    }

    /**
     * 反转义XML特殊字符.
     *
     * @param string 被替换的字符串
     * @return 替换后的字符串
     * @see EscapeUtil#unescapeXml(CharSequence)
     */
    public static String unescape(final String string) {
        return EscapeUtil.unescapeXml(string);
    }

    // ---------------------------------------------------- XML <-> Map/Bean

    /**
     * XML格式字符串转换为Map.
     *
     * @param xmlStr XML字符串
     * @return XML数据转换后的Map
     */
    public static Map<String, Object> xmlToMap(final String xmlStr) {
        return xmlToMap(xmlStr, new HashMap<>());
    }

    /**
     * XML转Java Bean.
     *
     * @param <T>  bean类型
     * @param node XML节点
     * @param bean bean类
     * @return bean
     * @see JAXBUtil#xmlToBean(String, Class)
     */
    public static <T> T xmlToBean(final Node node, final Class<T> bean) {
        return xmlToBean(node, bean, CopyOptions.create());
    }

    /**
     * XML转Java Bean.
     *
     * @param <T>         bean类型
     * @param node        XML节点
     * @param bean        bean类
     * @param copyOptions 拷贝选项
     * @return bean
     * @see JAXBUtil#xmlToBean(String, Class)
     */
    public static <T> T xmlToBean(final Node node, final Class<T> bean, final CopyOptions copyOptions) {
        final Map<String, Object> map = xmlToMap(node);
        if (null != map && map.size() == 1) {
            final String simpleName = bean.getSimpleName();
            final String nodeName = CollUtil.getFirst(map.keySet());
            if (simpleName.equalsIgnoreCase(nodeName)) {
                return BeanUtil.toBean(map.get(nodeName), bean, copyOptions);
            }
        }
        return BeanUtil.toBean(map, bean, copyOptions);
    }

    /**
     * XML格式字符串转换为Map.
     *
     * @param node XML节点
     * @return XML数据转换后的Map
     */
    public static Map<String, Object> xmlToMap(final Node node) {
        return xmlToMap(node, new HashMap<>());
    }

    /**
     * XML格式字符串转换为Map<br>
     * 只支持第一级别的XML，不支持多级XML.
     *
     * @param xmlStr XML字符串
     * @param result 结果Map类型
     * @return XML数据转换后的Map
     */
    public static Map<String, Object> xmlToMap(final String xmlStr, final Map<String, Object> result) {
        final Document doc = parseXml(xmlStr);
        final Element root = getRootElement(doc);
        root.normalize();
        return xmlToMap(root, result);
    }

    /**
     * XML节点转换为Map.
     *
     * @param node   XML节点
     * @param result 结果Map类型
     * @return XML数据转换后的Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> xmlToMap(final Node node, final Map<String, Object> result) {
        Map<String, Object> map = result;
        if (null == map) {
            map = new HashMap<>();
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
            final Object value = map.get(childEle.getNodeName());
            Object newValue;
            if (childEle.hasChildNodes()) {
                final Map<String, Object> childMap = xmlToMap(childEle);
                if (MapUtil.isNotEmpty(childMap)) {
                    newValue = childMap;
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
                        map.put(childEle.getNodeName(), CollUtil.newArrayList(value, newValue));
                    }
                } else {
                    map.put(childEle.getNodeName(), newValue);
                }
            }
        }
        return map;
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data) {
        return toStr(mapToXml(data, "xml"));
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data               Map类型数据
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, "xml"), CharsetUtil.UTF_8, false, omitXmlDeclaration);
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data     Map类型数据
     * @param rootName 根节点名
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final String rootName) {
        return toStr(mapToXml(data, rootName));
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data      Map类型数据
     * @param rootName  根节点名
     * @param namespace 命名空间，可以为null
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final String rootName, final String namespace) {
        return toStr(mapToXml(data, rootName, namespace));
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data               Map类型数据
     * @param rootName           根节点名
     * @param namespace          命名空间，可以为null
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final String rootName, final String namespace, final boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, false, omitXmlDeclaration);
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data               Map类型数据
     * @param rootName           根节点名
     * @param namespace          命名空间，可以为null
     * @param isPretty           是否格式化输出
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final String rootName, final String namespace, final boolean isPretty, final boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, isPretty, omitXmlDeclaration);
    }

    /**
     * 将Map转换为XML格式的字符串.
     *
     * @param data               Map类型数据
     * @param rootName           根节点名
     * @param namespace          命名空间，可以为null
     * @param charset            编码
     * @param isPretty           是否格式化输出
     * @param omitXmlDeclaration 是否忽略 xml Declaration
     * @return XML格式的字符串
     * @see JAXBUtil#beanToXml(Object)
     */
    public static String mapToXmlStr(final Map<?, ?> data, final String rootName, final String namespace, final String charset, final boolean isPretty, final boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), charset, isPretty, omitXmlDeclaration);
    }

    /**
     * 将Map转换为XML.
     *
     * @param data     Map类型数据
     * @param rootName 根节点名
     * @return XML
     * @see JAXBUtil#beanToXml(Object)
     */
    public static Document mapToXml(final Map<?, ?> data, final String rootName) {
        return mapToXml(data, rootName, null);
    }

    /**
     * 将Map转换为XML.
     *
     * @param data      Map类型数据
     * @param rootName  根节点名
     * @param namespace 命名空间，可以为null
     * @return XML
     * @see JAXBUtil#beanToXml(Object)
     */
    public static Document mapToXml(final Map<?, ?> data, final String rootName, final String namespace) {
        final Document doc = createXml();
        final Element root = appendChild(doc, rootName, namespace);
        appendMap(doc, root, data);
        return doc;
    }

    /**
     * 将Bean转换为XML.
     *
     * @param bean Bean对象
     * @return XML
     * @see JAXBUtil#beanToXml(Object)
     */
    public static Document beanToXml(final Object bean) {
        return beanToXml(bean, null);
    }

    /**
     * 将Bean转换为XML.
     *
     * @param bean      Bean对象
     * @param namespace 命名空间，可以为null
     * @return XML
     * @see JAXBUtil#beanToXml(Object)
     */
    public static Document beanToXml(final Object bean, final String namespace) {
        return beanToXml(bean, namespace, false);
    }

    /**
     * 将Bean转换为XML.
     *
     * @param bean       Bean对象
     * @param namespace  命名空间，可以为null
     * @param ignoreNull 忽略值为{@code null}的属性
     * @return XML
     * @see JAXBUtil#beanToXml(Object)
     */
    public static Document beanToXml(final Object bean, final String namespace, final boolean ignoreNull) {
        if (null == bean) {
            return null;
        }
        return mapToXml(BeanUtil.beanToMap(bean, false, ignoreNull), bean.getClass().getSimpleName(), namespace);
    }

    /**
     * 给定节点是否为{@link Element} 类型节点.
     *
     * @param node 节点
     * @return 是否为{@link Element} 类型节点
     */
    public static boolean isElement(final Node node) {
        return (null != node) && Node.ELEMENT_NODE == node.getNodeType();
    }

    /**
     * 在已有节点上创建子节点.
     *
     * @param node    节点
     * @param tagName 标签名
     * @return 子节点
     */
    public static Element appendChild(final Node node, final String tagName) {
        return appendChild(node, tagName, null);
    }

    /**
     * 在已有节点上创建子节点.
     *
     * @param node      节点
     * @param tagName   标签名
     * @param namespace 命名空间，无传null
     * @return 子节点
     */
    public static Element appendChild(final Node node, final String tagName, final String namespace) {
        final Document doc = getOwnerDocument(node);
        final Element child;
        if (null == namespace) {
            child = doc.createElement(tagName);
        } else {
            child = doc.createElementNS(namespace, tagName);
        }
        node.appendChild(child);
        return child;
    }

    /**
     * 创建文本子节点.
     *
     * @param node 节点
     * @param text 文本
     * @return 子节点
     */
    public static Node appendText(final Node node, final CharSequence text) {
        return appendText(getOwnerDocument(node), node, text);
    }

    /**
     * 追加数据子节点，可以是Map、集合、文本.
     *
     * @param node 节点
     * @param data 数据
     */
    public static void append(final Node node, final Object data) {
        append(getOwnerDocument(node), node, data);
    }

    // -------------------------------------------------- Private method start

    /**
     * 追加数据子节点.
     *
     * @param doc  {@link Document}
     * @param node 节点
     * @param data 数据
     */
    @SuppressWarnings("rawtypes")
    private static void append(final Document doc, final Node node, final Object data) {
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

    /**
     * 追加Map数据子节点.
     *
     * @param doc  {@link Document}
     * @param node 当前节点
     * @param data Map类型数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void appendMap(final Document doc, final Node node, final Map data) {
        data.forEach((key, value) -> {
            if (null != key) {
                final Element child = appendChild(node, key.toString());
                if (null != value) {
                    append(doc, child, value);
                }
            }
        });
    }

    /**
     * 追加集合节点.
     *
     * @param doc  {@link Document}
     * @param node 节点
     * @param data 数据
     */
    @SuppressWarnings("rawtypes")
    private static void appendIterator(final Document doc, final Node node, final Iterator data) {
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

    /**
     * 追加文本节点.
     *
     * @param doc  {@link Document}
     * @param node 节点
     * @param text 文本内容
     * @return 增加的子节点
     */
    private static Node appendText(final Document doc, final Node node, final CharSequence text) {
        return node.appendChild(doc.createTextNode(StrUtil.str(text)));
    }

    /**
     * 关闭XXE，避免漏洞攻击.
     *
     * @param dbf DocumentBuilderFactory
     * @return DocumentBuilderFactory
     */
    private static DocumentBuilderFactory disableXXE(final DocumentBuilderFactory dbf) {
        String feature;
        try {
            feature = "http://apache.org/xml/features/" + "disallow-doctype-decl";
            dbf.setFeature(feature, true);
            feature = "http://xml.org/sax/features/" + "external-general-entities";
            dbf.setFeature(feature, false);
            feature = "http://xml.org/sax/features/" + "external-parameter-entities";
            dbf.setFeature(feature, false);
            feature = "http://apache.org/xml/features/" + "nonvalidating/load-external-dtd";
            dbf.setFeature(feature, false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
        } catch (final ParserConfigurationException e) {
            // ignore
        }
        return dbf;
    }

    // --------------------------------------------------- Private method end

    /**
     * 全局命名空间上下文.<br>
     * 见：https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/
     */
    public static class UniversalNamespaceCache implements NamespaceContext {
        private static final String DEFAULT_NS = "DEFAULT";
        private final BiMap<String, String> prefixUri = new BiMap<>(new HashMap<>());

        /**
         * 构造.
         *
         * @param node         source Node
         * @param toplevelOnly 是否仅顶层
         */
        public UniversalNamespaceCache(final Node node, final boolean toplevelOnly) {
            examineNode(node.getFirstChild(), toplevelOnly);
        }

        private void examineNode(final Node node, final boolean attributesOnly) {
            final NamedNodeMap attributes = node.getAttributes();
            if (null != attributes) {
                final int len = attributes.getLength();
                for (int i = 0; i < len; i++) {
                    final Node attribute = attributes.item(i);
                    storeAttribute(attribute);
                }
            }
            if (false == attributesOnly) {
                final NodeList childNodes = node.getChildNodes();
                if (null != childNodes) {
                    Node item;
                    final int childLen = childNodes.getLength();
                    for (int i = 0; i < childLen; i++) {
                        item = childNodes.item(i);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            examineNode(item, false);
                        }
                    }
                }
            }
        }

        private void storeAttribute(final Node attribute) {
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
        public String getNamespaceURI(final String prefix) {
            if (prefix == null || XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
                return prefixUri.get(DEFAULT_NS);
            } else {
                return prefixUri.get(prefix);
            }
        }

        @Override
        public String getPrefix(final String namespaceURI) {
            return prefixUri.getInverse().get(namespaceURI);
        }

        @Override
        public Iterator<String> getPrefixes(final String namespaceURI) {
            return null;
        }
    }
}
