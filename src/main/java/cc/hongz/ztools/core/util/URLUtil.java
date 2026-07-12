package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.net.URLEncodeUtil;
import cc.hongz.ztools.core.net.URLDecoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * URL（Uniform Resource Locator）统一资源定位符相关工具类.
 *
 * <p>
 * 统一资源定位符，描述了一台特定服务器上某资源的特定位置。
 * </p>
 * URL组成：
 * <pre>
 *   协议://主机名[:端口]/ 路径/[:参数] [?查询]#Fragment
 *   protocol :// hostname[:port] / path / [:parameters][?query]#fragment
 * </pre>
 *
 * <p>
 * 对齐 Hutool {@code cn.hutool.core.util.URLUtil}，encode/decode 委托给
 * {@link URLEncodeUtil} 和 {@link URLDecoder}，基于 RFC3986 + {@link cc.hongz.ztools.core.codec.PercentCodec} 实现。
 * </p>
 *
 * @author xiaoleilu
 * @author hongzhe.qin
 */
public final class URLUtil {

    /**
     * 针对ClassPath路径的伪协议前缀（兼容Spring）: "classpath:"
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    /**
     * URL 前缀表示文件: "file:"
     */
    public static final String FILE_URL_PREFIX = "file:";
    /**
     * URL 前缀表示jar: "jar:"
     */
    public static final String JAR_URL_PREFIX = "jar:";
    /**
     * URL 前缀表示war: "war:"
     */
    public static final String WAR_URL_PREFIX = "war:";
    /**
     * URL 协议表示文件: "file"
     */
    public static final String URL_PROTOCOL_FILE = "file";
    /**
     * URL 协议表示Jar文件: "jar"
     */
    public static final String URL_PROTOCOL_JAR = "jar";
    /**
     * URL 协议表示zip文件: "zip"
     */
    public static final String URL_PROTOCOL_ZIP = "zip";
    /**
     * URL 协议表示WebSphere文件: "wsjar"
     */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";
    /**
     * URL 协议表示JBoss zip文件: "vfszip"
     */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";
    /**
     * URL 协议表示JBoss文件: "vfsfile"
     */
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
    /**
     * URL 协议表示JBoss VFS资源: "vfs"
     */
    public static final String URL_PROTOCOL_VFS = "vfs";
    /**
     * Jar路径以及内部文件路径的分界符: "!/"
     */
    public static final String JAR_URL_SEPARATOR = "!/";
    /**
     * WAR路径及内部文件路径分界符
     */
    public static final String WAR_URL_SEPARATOR = "*/";

    /**
     * 私有构造，禁止实例化.
     */
    private URLUtil() {
    }

    // --------------------------------------- URL 创建

    /**
     * 将{@link URI}转换为{@link URL}
     *
     * @param uri {@link URI}
     * @return URL对象
     * @throws UtilException {@link MalformedURLException}包装，URI格式有问题时抛出
     */
    public static URL url(final URI uri) throws UtilException {
        if (null == uri) {
            return null;
        }
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 通过一个字符串形式的URL地址创建URL对象
     *
     * @param url URL
     * @return URL对象
     */
    public static URL url(final String url) {
        return url(url, null);
    }

    /**
     * 通过一个字符串形式的URL地址创建URL对象
     *
     * @param url     URL
     * @param handler {@link URLStreamHandler}
     * @return URL对象
     */
    public static URL url(final String url, final URLStreamHandler handler) {
        if (null == url) {
            return null;
        }

        // 兼容Spring的ClassPath路径
        if (url.startsWith(CLASSPATH_URL_PREFIX)) {
            final String resourcePath = url.substring(CLASSPATH_URL_PREFIX.length());
            return Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        }

        try {
            return new URL(null, url, handler);
        } catch (MalformedURLException e) {
            // issue#I8PY3Y
            if (e.getMessage().contains("Accessing an URL protocol that was not enabled")) {
                // Graalvm打包需要手动指定参数开启协议：
                // --enable-url-protocols=http
                // --enable-url-protocols=https
                throw new UtilException(e);
            }

            // 尝试文件路径
            try {
                return new File(url).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new UtilException(e);
            }
        }
    }

    /**
     * 获取string协议的URL，类似于string:///xxxxx
     *
     * @param content 正文
     * @return URL
     */
    public static URI getStringURI(final CharSequence content) {
        if (null == content) {
            return null;
        }
        final String contentStr = StrUtil.addPrefixIfNot(content, "string:///");
        return URI.create(contentStr);
    }

    /**
     * 将URL字符串转换为URL对象，并做必要验证
     *
     * @param urlStr URL字符串
     * @return URL
     */
    public static URL toUrlForHttp(final String urlStr) {
        return toUrlForHttp(urlStr, null);
    }

    /**
     * 将URL字符串转换为URL对象，并做必要验证
     *
     * @param urlStr  URL字符串
     * @param handler {@link URLStreamHandler}
     * @return URL
     */
    public static URL toUrlForHttp(final String urlStr, final URLStreamHandler handler) {
        Assert.notBlank(urlStr, "Url is blank !");
        // 编码空白符，防止空格引起的请求异常
        final String encodedUrl = encodeBlank(urlStr);
        try {
            return new URL(null, encodedUrl, handler);
        } catch (MalformedURLException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 单独编码URL中的空白符，空白符编码为%20
     *
     * @param urlStr URL字符串
     * @return 编码后的字符串
     */
    public static String encodeBlank(final CharSequence urlStr) {
        if (urlStr == null) {
            return null;
        }

        final int len = urlStr.length();
        final StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = urlStr.charAt(i);
            if (CharUtil.isBlankChar(c)) {
                sb.append("%20");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // --------------------------------------- 资源URL获取

    /**
     * 获得URL
     *
     * @param pathBaseClassLoader 相对路径（相对于classes）
     * @return URL
     */
    public static URL getURL(final String pathBaseClassLoader) {
        if (pathBaseClassLoader == null) {
            return null;
        }
        return Thread.currentThread().getContextClassLoader().getResource(pathBaseClassLoader);
    }

    /**
     * 获得URL
     *
     * @param path  相对给定 class所在的路径
     * @param clazz 指定class
     * @return URL
     */
    public static URL getURL(final String path, final Class<?> clazz) {
        if (path == null || clazz == null) {
            return null;
        }
        return clazz.getResource(path);
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param file URL对应的文件对象
     * @return URL
     * @throws UtilException MalformedURLException
     */
    public static URL getURL(final File file) {
        Assert.notNull(file, "File is null !");
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UtilException(e, "Error occurred when get URL!");
        }
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param files URL对应的文件对象
     * @return URL
     * @throws UtilException MalformedURLException
     */
    public static URL[] getURLs(final File... files) {
        final URL[] urls = new URL[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                urls[i] = files[i].toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new UtilException(e, "Error occurred when get URL!");
        }

        return urls;
    }

    // --------------------------------------- URL 信息提取

    /**
     * 获取URL中域名部分，只保留URL中的协议（Protocol）、Host，其它为null。
     *
     * @param url URL
     * @return 域名的URI
     */
    public static URI getHost(final URL url) {
        if (null == url) {
            return null;
        }

        try {
            return new URI(url.getProtocol(), url.getHost(), null, null);
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 补全相对路径
     *
     * @param baseUrl      基准URL
     * @param relativePath 相对URL
     * @return 相对路径
     * @throws UtilException MalformedURLException
     */
    public static String completeUrl(final String baseUrl, final String relativePath) {
        final String normalizedBaseUrl = normalize(baseUrl, false);
        if (StrUtil.isBlank(normalizedBaseUrl)) {
            return null;
        }

        try {
            final URL absoluteUrl = new URL(normalizedBaseUrl);
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            throw new UtilException(e);
        }
    }

    // --------------------------------------- decode

    /**
     * 解码URL<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param url URL
     * @return 解码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String decode(final String url) throws UtilException {
        return URLDecoder.decode(url, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。<br>
     * 规则见：https://url.spec.whatwg.org/#urlencoded-parsing
     *
     * @param content 被解码内容
     * @param charset 编码，null表示不解码
     * @return 编码后的字符
     */
    public static String decode(final String content, final Charset charset) {
        return URLDecoder.decode(content, charset);
    }

    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param content       被解码内容
     * @param charset       编码，null表示不解码
     * @param isPlusToSpace 是否+转换为空格
     * @return 编码后的字符
     */
    public static String decode(final String content, final Charset charset, final boolean isPlusToSpace) {
        return URLDecoder.decode(content, charset, isPlusToSpace);
    }

    /**
     * 解码application/x-www-form-urlencoded字符<br>
     * 将%开头的16进制表示的内容解码。
     *
     * @param content URL
     * @param charset 编码
     * @return 解码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String decode(final String content, final String charset) throws UtilException {
        return decode(content, StrUtil.isEmpty(charset) ? null : CharsetUtil.charset(charset));
    }

    // --------------------------------------- path / URI

    /**
     * 获得path部分<br>
     *
     * @param uriStr URI路径
     * @return path
     * @throws UtilException 包装URISyntaxException
     */
    public static String getPath(final String uriStr) {
        return toURI(uriStr).getPath();
    }

    /**
     * 从URL对象中获取不被编码的路径Path<br>
     * 对于本地路径，URL对象的getPath方法对于包含中文或空格时会被编码，导致本读路径读取错误。<br>
     * 此方法将URL转为URI后获取路径用于解决路径被编码的问题
     *
     * @param url {@link URL}
     * @return 路径
     */
    public static String getDecodedPath(final URL url) {
        if (null == url) {
            return null;
        }

        String path = null;
        try {
            // URL对象的getPath方法对于包含中文或空格的问题
            path = toURI(url).getPath();
        } catch (UtilException e) {
            // ignore
        }
        return (null != path) ? path : url.getPath();
    }

    /**
     * 转URL为URI
     *
     * @param url URL
     * @return URI
     * @throws UtilException 包装URISyntaxException
     */
    public static URI toURI(final URL url) throws UtilException {
        return toURI(url, false);
    }

    /**
     * 转URL为URI
     *
     * @param url      URL
     * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
     * @return URI
     * @throws UtilException 包装URISyntaxException
     */
    public static URI toURI(final URL url, final boolean isEncode) throws UtilException {
        if (null == url) {
            return null;
        }

        return toURI(url.toString(), isEncode);
    }

    /**
     * 转字符串为URI
     *
     * @param location 字符串路径
     * @return URI
     * @throws UtilException 包装URISyntaxException
     */
    public static URI toURI(final String location) throws UtilException {
        return toURI(location, false);
    }

    /**
     * 转字符串为URI
     *
     * @param location 字符串路径
     * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
     * @return URI
     * @throws UtilException 包装URISyntaxException
     */
    public static URI toURI(final String location, final boolean isEncode) throws UtilException {
        if (location == null) {
            return null;
        }
        String uriStr = location;
        if (isEncode) {
            uriStr = encode(uriStr);
        }
        try {
            return new URI(StrUtil.trim(uriStr));
        } catch (URISyntaxException e) {
            throw new UtilException(e);
        }
    }

    // --------------------------------------- URL 类型判断

    /**
     * 提供的URL是否为文件<br>
     * 文件协议包括"file", "vfsfile" 或 "vfs".
     *
     * @param url {@link URL}
     * @return 是否为文件
     */
    public static boolean isFileURL(final URL url) {
        Assert.notNull(url, "URL must be not null");
        final String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || //
                URL_PROTOCOL_VFSFILE.equals(protocol) || //
                URL_PROTOCOL_VFS.equals(protocol));
    }

    /**
     * 提供的URL是否为jar包URL 协议包括： "jar", "zip", "vfszip" 或 "wsjar".
     *
     * @param url {@link URL}
     * @return 是否为jar包URL
     */
    public static boolean isJarURL(final URL url) {
        Assert.notNull(url, "URL must be not null");
        final String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || //
                URL_PROTOCOL_ZIP.equals(protocol) || //
                URL_PROTOCOL_VFSZIP.equals(protocol) || //
                URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * 提供的URL是否为Jar文件URL 判断依据为file协议且扩展名为.jar
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR file URL
     */
    public static boolean isJarFileURL(final URL url) {
        Assert.notNull(url, "URL must be not null");
        return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && //
                url.getPath().toLowerCase().endsWith(FileUtil.JAR_FILE_EXT));
    }

    // --------------------------------------- URL 流操作

    /**
     * 从URL中获取流
     *
     * @param url {@link URL}
     * @return InputStream流
     */
    public static InputStream getStream(final URL url) {
        Assert.notNull(url, "URL must be not null");
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得Reader
     *
     * @param url     {@link URL}
     * @param charset 编码
     * @return {@link BufferedReader}
     */
    public static BufferedReader getReader(final URL url, final Charset charset) {
        return IoUtil.getReader(getStream(url), charset);
    }

    /**
     * 从URL中获取JarFile
     *
     * @param url URL
     * @return JarFile
     */
    public static JarFile getJarFile(final URL url) {
        try {
            final JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
            return urlConnection.getJarFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    // --------------------------------------- normalize

    /**
     * 标准化URL字符串，包括：
     *
     * <ol>
     *     <li>自动补齐"http://"头</li>
     *     <li>去除开头的\或者/</li>
     *     <li>替换\为/</li>
     * </ol>
     *
     * @param url URL字符串
     * @return 标准化后的URL字符串
     */
    public static String normalize(final String url) {
        return normalize(url, false);
    }

    /**
     * 标准化URL字符串，包括：
     *
     * <ol>
     *     <li>自动补齐"http://"头</li>
     *     <li>去除开头的\或者/</li>
     *     <li>替换\为/</li>
     * </ol>
     *
     * @param url          URL字符串
     * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
     * @return 标准化后的URL字符串
     */
    public static String normalize(final String url, final boolean isEncodePath) {
        return normalize(url, isEncodePath, false);
    }

    /**
     * 标准化URL字符串，包括：
     *
     * <ol>
     *     <li>自动补齐"http://"头</li>
     *     <li>去除开头的\或者/</li>
     *     <li>替换\为/</li>
     *     <li>如果replaceSlash为true，则替换多个/为一个</li>
     * </ol>
     *
     * @param url          URL字符串
     * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
     * @param replaceSlash 是否替换url body中的 //
     * @return 标准化后的URL字符串
     */
    public static String normalize(final String url, final boolean isEncodePath, final boolean replaceSlash) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        final int sepIndex = url.indexOf("://");
        String protocol;
        String body;
        if (sepIndex > 0) {
            protocol = StrUtil.subPre(url, sepIndex + 3);
            body = StrUtil.subSuf(url, sepIndex + 3);
        } else {
            protocol = "http://";
            body = url;
        }

        final int paramsSepIndex = StrUtil.indexOf(body, '?');
        String params = null;
        if (paramsSepIndex > 0) {
            params = StrUtil.subSuf(body, paramsSepIndex);
            body = StrUtil.subPre(body, paramsSepIndex);
        }

        if (StrUtil.isNotEmpty(body)) {
            // 去除开头的\或者/
            body = body.replaceAll("^[\\\\/]+", StrUtil.EMPTY);
            // 替换\为/
            body = body.replace("\\", "/");
            if (replaceSlash) {
                //issue#I25MZL@Gitee，双斜杠在URL中是允许存在的，默认不做替换
                body = body.replaceAll("//+", "/");
            }
        }

        final int pathSepIndex = StrUtil.indexOf(body, '/');
        String domain = body;
        String path = null;
        if (pathSepIndex > 0) {
            domain = StrUtil.subPre(body, pathSepIndex);
            path = StrUtil.subSuf(body, pathSepIndex);
        }
        if (isEncodePath) {
            path = encode(path);
        }
        return protocol + domain + StrUtil.nullToEmpty(path) + StrUtil.nullToEmpty(params);
    }

    // --------------------------------------- buildQuery

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
     * 会自动url编码键和值
     *
     * <pre>
     * key1=v1&amp;key2=&amp;key3=v3
     * </pre>
     *
     * @param paramMap 表单数据
     * @param charset  编码，编码为null表示不编码
     * @return url参数
     */
    public static String buildQuery(final Map<String, ?> paramMap, final Charset charset) {
        if (paramMap == null || paramMap.isEmpty()) {
            return StrUtil.EMPTY;
        }
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final Map.Entry<String, ?> entry : paramMap.entrySet()) {
            if (StrUtil.isEmpty(entry.getKey())) {
                continue;
            }
            if (!first) {
                sb.append("&");
            }
            first = false;
            sb.append(encode(entry.getKey(), charset));
            sb.append("=");
            final Object value = entry.getValue();
            sb.append(value == null ? StrUtil.EMPTY : encode(value.toString(), charset));
        }
        return sb.toString();
    }

    // --------------------------------------- content length / size

    /**
     * 获取指定URL对应资源的内容长度，对于Http，其长度使用Content-Length头决定。
     *
     * @param url URL
     * @return 内容长度，未知返回-1
     * @throws IORuntimeException IO异常
     */
    public static long getContentLength(final URL url) throws IORuntimeException {
        if (null == url) {
            return -1;
        }

        URLConnection conn = null;
        try {
            conn = url.openConnection();
            return conn.getContentLengthLong();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    /**
     * 获取URL对应数据长度
     * <ul>
     *     <li>如果URL为文件，转换为文件获取文件长度。</li>
     *     <li>其它情况获取{@link URLConnection#getContentLengthLong()}</li>
     * </ul>
     *
     * @param url URL
     * @return 长度
     */
    public static long size(final URL url) {
        if (URLUtil.isFileURL(url)) {
            // 如果资源以独立文件形式存在，尝试获取文件长度
            final File file = FileUtil.file(url);
            final long length = file.length();
            if (length == 0L && !file.exists()) {
                throw new IORuntimeException("File not exist or size is zero!");
            }
            return length;
        } else {
            // 如果资源打在jar包中或来自网络，使用网络请求长度
            // issue#3226, 来自Spring的AbstractFileResolvingResource
            URLConnection con = null;
            try {
                con = url.openConnection();
                useCachesIfNecessary(con);
                if (con instanceof HttpURLConnection) {
                    final HttpURLConnection httpCon = (HttpURLConnection) con;
                    httpCon.setRequestMethod("HEAD");
                }
                return con.getContentLengthLong();
            } catch (final IOException e) {
                throw new IORuntimeException(e);
            } finally {
                if (con instanceof HttpURLConnection) {
                    ((HttpURLConnection) con).disconnect();
                }
            }
        }
    }

    /**
     * 如果连接为JNLP方式，则打开缓存
     *
     * @param con {@link URLConnection}
     */
    public static void useCachesIfNecessary(final URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }

    // --------------------------------------- Data URI

    /**
     * Data URI Scheme封装，数据格式为Base64。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
     * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
     *
     * <p>
     * Data URI的格式规范：
     * <pre>
     *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
     * </pre>
     *
     * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
     * @param data     编码后的数据
     * @return Data URI字符串
     */
    public static String getDataUriBase64(final String mimeType, final String data) {
        return getDataUri(mimeType, null, "base64", data);
    }

    /**
     * Data URI Scheme封装。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
     * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
     *
     * <p>
     * Data URI的格式规范：
     * <pre>
     *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
     * </pre>
     *
     * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
     * @param encoding 数据编码方式（US-ASCII，BASE64等）
     * @param data     编码后的数据
     * @return Data URI字符串
     */
    public static String getDataUri(final String mimeType, final String encoding, final String data) {
        return getDataUri(mimeType, null, encoding, data);
    }

    /**
     * Data URI Scheme封装。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
     * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
     *
     * <p>
     * Data URI的格式规范：
     * <pre>
     *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
     * </pre>
     *
     * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
     * @param charset  可选项（null表示无），源文本的字符集编码方式
     * @param encoding 数据编码方式（US-ASCII，BASE64等）
     * @param data     编码后的数据
     * @return Data URI字符串
     */
    public static String getDataUri(final String mimeType, final Charset charset,
                                    final String encoding, final String data) {
        final StringBuilder builder = new StringBuilder("data:");
        if (StrUtil.isNotBlank(mimeType)) {
            builder.append(mimeType);
        }
        if (null != charset) {
            builder.append(";charset=").append(charset.name());
        }
        if (StrUtil.isNotBlank(encoding)) {
            builder.append(';').append(encoding);
        }
        builder.append(',').append(data);

        return builder.toString();
    }

    // --------------------------------------- encode
    //
    // RFC3986 编码层级关系：
    //   UNRESERVED  = ALPHA / DIGIT / "-" / "." / "_" / "~"
    //   SUB_DELIMS  = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
    //   PCHAR       = UNRESERVED + SUB_DELIMS + ":" + "@"
    //   SEGMENT     = PCHAR                                  （不含 "/"）
    //   PATH        = SEGMENT + "/"                          （= PCHAR + "/"）
    //   QUERY       = PATH + "?"                              （= PCHAR + "/" + "?"）
    //   FRAGMENT    = QUERY                                  （同 QUERY）
    //
    // 对应方法：encodeAll(UNRESERVED) / encode(PATH) / encodePathSegment(SEGMENT)
    //           / encodeQuery(QUERY) / encodeFragment(FRAGMENT)
    //
    // 实现委托给 URLEncodeUtil，基于 RFC3986 + PercentCodec（BitSet）逐字符判断

    /**
     * 编码URL，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于"/"的字符不再编码<br>
     * 遵循 RFC3986 PATH 规范，保留 pchar + "/" 字符。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encode(final String url) throws UtilException {
        return URLEncodeUtil.encode(url);
    }

    /**
     * 编码URL<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于"/"的字符不再编码<br>
     * 遵循 RFC3986 PATH 规范，保留 pchar + "/" 字符。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encode(final String url, final Charset charset) {
        return URLEncodeUtil.encode(url, charset);
    }

    /**
     * 编码URL中查询语句，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法用于URL查询参数编码，转义大部分特殊字符，但保留 RFC3986 QUERY 允许的字符。<br>
     * 与 {@link #encode(String)} 的区别：此方法不编码 {@code +}（sub-delim）和 {@code ?}
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeQuery(final String url) throws UtilException {
        return URLEncodeUtil.encodeQuery(url);
    }

    /**
     * 编码字符为URL中查询语句<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法用于URL查询参数编码，转义大部分特殊字符，但保留 RFC3986 QUERY 允许的字符。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodeQuery(final String url, final Charset charset) {
        return URLEncodeUtil.encodeQuery(url, charset);
    }

    /**
     * 编码URL的Segment，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 SEGMENT 规范，保留 pchar 字符，但 {@code /} 会被编码。<br>
     * 与 {@link #encode(String)} 的区别：此方法编码 {@code /}（路径分隔符）
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodePathSegment(final String url) throws UtilException {
        return URLEncodeUtil.encodePathSegment(url);
    }

    /**
     * 编码URL的Segment<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 SEGMENT 规范，保留 pchar 字符，但 {@code /} 会被编码。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodePathSegment(final String url, final Charset charset) {
        return URLEncodeUtil.encodePathSegment(url, charset);
    }

    /**
     * 编码URL的Fragment，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 FRAGMENT 规范，保留 pchar + "/" + "?" 字符。<br>
     * FRAGMENT 与 QUERY 允许的字符集相同。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeFragment(final String url) throws UtilException {
        return URLEncodeUtil.encodeFragment(url);
    }

    /**
     * 编码URL的Fragment<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 FRAGMENT 规范，保留 pchar + "/" + "?" 字符。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodeFragment(final String url, final Charset charset) {
        return URLEncodeUtil.encodeFragment(url, charset);
    }

    /**
     * 编码全部URL，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法仅保留 RFC3986 UNRESERVED 字符（ALPHA / DIGIT / "-" / "." / "_" / "~"），其余全部编码。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeAll(final String url) throws UtilException {
        return URLEncodeUtil.encodeAll(url);
    }

    /**
     * 编码全部URL<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法仅保留 RFC3986 UNRESERVED 字符（ALPHA / DIGIT / "-" / "." / "_" / "~"），其余全部编码。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodeAll(final String url, final Charset charset) {
        return URLEncodeUtil.encodeAll(url, charset);
    }
}
