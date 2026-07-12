package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.IORuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * {@link URLUtil} 单元测试.
 *
 * <p>
 * 覆盖 Data URI、URL 编码/解码、normalize、toURI、isFileURL/isJarURL、buildQuery 等方法。
 *
 * @author hongzhe.qin
 */
class URLUtilTest {

    @TempDir
    Path tempDir;

    // --------------------------------------- Data URI

    /**
     * 验证 {@link URLUtil#getDataUriBase64(String, String)} 标准 MIME 类型拼接.
     */
    @Test
    void getDataUriBase64() {
        assertEquals("data:image/png;base64,abc", URLUtil.getDataUriBase64("image/png", "abc"));
    }

    /**
     * 验证空 MIME 类型时仍生成合法 Data URI 前缀.
     */
    @Test
    void getDataUriBase64BlankMime() {
        assertEquals("data:;base64,abc", URLUtil.getDataUriBase64("", "abc"));
    }

    /**
     * 验证 {@link URLUtil#getDataUri(String, String, String)} 不带 charset 的 Data URI.
     */
    @Test
    void getDataUri() {
        assertEquals("data:text/plain;base64,SGVsbG8=",
                URLUtil.getDataUri("text/plain", "base64", "SGVsbG8="));
    }

    /**
     * 验证 {@link URLUtil#getDataUri(String, java.nio.charset.Charset, String, String)} 带 charset 的 Data URI.
     */
    @Test
    void getDataUriWithCharset() {
        assertEquals("data:text/plain;charset=UTF-8;base64,SGVsbG8=",
                URLUtil.getDataUri("text/plain", StandardCharsets.UTF_8, "base64", "SGVsbG8="));
    }

    /**
     * 验证 null mimeType 和 null encoding 时仅输出 data:,data.
     */
    @Test
    void getDataUriWithNulls() {
        assertEquals("data:,hello", URLUtil.getDataUri(null, null, null, "hello"));
    }

    // --------------------------------------- encode / decode

    /**
     * 验证 {@link URLUtil#encode(String)} 对中文编码.
     */
    @Test
    void encode() {
        final String encoded = URLUtil.encode("测试");
        assertEquals("%E6%B5%8B%E8%AF%95", encoded);
    }

    /**
     * 验证 encode 保留 URL path 合法字符（/ : @ 等）.
     */
    @Test
    void encodePreservesPathChars() {
        final String encoded = URLUtil.encode("http://example.com/path");
        assertEquals("http://example.com/path", encoded);
    }

    /**
     * 验证 encode 空字符串和 null.
     */
    @Test
    void encodeEmpty() {
        assertNull(URLUtil.encode(null));
        assertEquals("", URLUtil.encode(""));
    }

    /**
     * 验证 encode 将空格编码为 %20.
     */
    @Test
    void encodeSpace() {
        assertEquals("hello%20world", URLUtil.encode("hello world"));
    }

    /**
     * 验证 {@link URLUtil#decode(String)} 解码.
     */
    @Test
    void decode() {
        assertEquals("测试", URLUtil.decode("%E6%B5%8B%E8%AF%95"));
    }

    /**
     * 验证 decode null 返回 null.
     */
    @Test
    void decodeNull() {
        assertNull(URLUtil.decode(null));
    }

    /**
     * 验证 {@link URLUtil#decode(String, java.nio.charset.Charset)} 指定字符集解码.
     */
    @Test
    void decodeWithCharset() {
        assertEquals("hello world", URLUtil.decode("hello+world", StandardCharsets.UTF_8));
    }

    /**
     * 验证 {@link URLUtil#decode(String, java.nio.charset.Charset, boolean)} isPlusToSpace=false 时 + 保留.
     */
    @Test
    void decodeWithoutPlusToSpace() {
        final String result = URLUtil.decode("a+b", StandardCharsets.UTF_8, false);
        assertEquals("a+b", result);
    }

    /**
     * 验证 {@link URLUtil#decode(String, String)} 指定字符集名称解码.
     */
    @Test
    void decodeWithCharsetName() {
        assertEquals("测试", URLUtil.decode("%E6%B5%8B%E8%AF%95", "UTF-8"));
    }

    /**
     * 验证 encode 后 decode 可还原.
     */
    @Test
    void encodeDecodeRoundTrip() {
        final String original = "Hello 世界! @test/path";
        final String encoded = URLUtil.encode(original);
        final String decoded = URLUtil.decode(encoded);
        assertEquals(original, decoded);
    }

    // ---- hutool 对齐：encode/decode 边界场景

    /**
     * 验证 encode 对文件名（空格 + 中文）编码.
     */
    @Test
    void encodeFilename() {
        final String body = "366466 - 副本.jpg";
        final String encode = URLUtil.encode(body);
        assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
        assertEquals(body, URLUtil.decode(encode));
    }

    /**
     * 验证 encodeQuery 对 + 不编码.
     */
    @Test
    void encodeQueryPlus() {
        final String body = "+";
        final String encode = URLUtil.encodeQuery(body);
        assertEquals("+", encode);
    }

    /**
     * 验证 encodeQuery 对文件名编码，结果与 encode 一致.
     */
    @Test
    void encodeQueryFilename() {
        final String body = "366466 - 副本.jpg";
        final String encode = URLUtil.encodeQuery(body);
        assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
    }

    /**
     * 验证 encodeQuery null/空字符串.
     */
    @Test
    void encodeQueryEmpty() {
        assertNull(URLUtil.encodeQuery(null));
        assertEquals("", URLUtil.encodeQuery(""));
    }

    /**
     * 验证 encodeAll 仅保留 UNRESERVED 字符，编码 / 和 :.
     */
    @Test
    void encodeAll() {
        final String encoded = URLUtil.encodeAll("http://example.com/path");
        assertEquals("http%3A%2F%2Fexample.com%2Fpath", encoded);
    }

    /**
     * 验证 encodeAll 保留 - . _ ~.
     */
    @Test
    void encodeAllUnreserved() {
        final String encoded = URLUtil.encodeAll("a-b.c_d~e");
        assertEquals("a-b.c_d~e", encoded);
    }

    /**
     * 验证 encodeAll 编码 *.
     */
    @Test
    void encodeAllStar() {
        assertEquals("%2A", URLUtil.encodeAll("*"));
    }

    /**
     * 验证 encodeAll null/空字符串.
     */
    @Test
    void encodeAllEmpty() {
        assertNull(URLUtil.encodeAll(null));
        assertEquals("", URLUtil.encodeAll(""));
    }

    // ---- RFC3986 编码层级对比测试

    /**
     * 验证 encode(PATH) 保留 + 号（sub-delim）.
     */
    @Test
    void encodePlus() {
        assertEquals("+", URLUtil.encode("+"));
    }

    /**
     * 验证 encode(PATH) 保留 / 号.
     */
    @Test
    void encodeSlash() {
        assertEquals("a/b", URLUtil.encode("a/b"));
    }

    /**
     * 验证 encodePathSegment(SEGMENT) 编码 / 号.
     */
    @Test
    void encodePathSegmentSlash() {
        assertEquals("a%2Fb", URLUtil.encodePathSegment("a/b"));
    }

    /**
     * 验证 encodePathSegment(SEGMENT) 保留 + 号和 : 号.
     */
    @Test
    void encodePathSegmentPlusAndColon() {
        assertEquals("a+b:c", URLUtil.encodePathSegment("a+b:c"));
    }

    /**
     * 验证 encodePathSegment 对中文编码.
     */
    @Test
    void encodePathSegmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLUtil.encodePathSegment("测试"));
    }

    /**
     * 验证 encodePathSegment null/空字符串.
     */
    @Test
    void encodePathSegmentEmpty() {
        assertNull(URLUtil.encodePathSegment(null));
        assertEquals("", URLUtil.encodePathSegment(""));
    }

    /**
     * 验证 encodeFragment(FRAGMENT) 保留 ? 和 / 号.
     */
    @Test
    void encodeFragmentQueryAndSlash() {
        assertEquals("a/b?c", URLUtil.encodeFragment("a/b?c"));
    }

    /**
     * 验证 encodeFragment(FRAGMENT) 保留 + 号.
     */
    @Test
    void encodeFragmentPlus() {
        assertEquals("+", URLUtil.encodeFragment("+"));
    }

    /**
     * 验证 encodeFragment 对中文编码.
     */
    @Test
    void encodeFragmentChinese() {
        assertEquals("%E6%B5%8B%E8%AF%95", URLUtil.encodeFragment("测试"));
    }

    /**
     * 验证 encodeFragment null/空字符串.
     */
    @Test
    void encodeFragmentEmpty() {
        assertNull(URLUtil.encodeFragment(null));
        assertEquals("", URLUtil.encodeFragment(""));
    }

    /**
     * 验证 encode 与 encodePathSegment 对 / 的处理差异.
     */
    @Test
    void encodeVsPathSegmentSlash() {
        // PATH 保留 /
        assertEquals("path/to", URLUtil.encode("path/to"));
        // SEGMENT 编码 /
        assertEquals("path%2Fto", URLUtil.encodePathSegment("path/to"));
    }

    /**
     * 验证 encodeQuery 与 encodeFragment 结果一致（RFC3986 QUERY == FRAGMENT）.
     */
    @Test
    void encodeQueryEqualsFragment() {
        final String input = "a/b?c+d=e&f";
        assertEquals(URLUtil.encodeQuery(input), URLUtil.encodeFragment(input));
    }

    /**
     * 验证 decode(+) 转为空格.
     */
    @Test
    void decodePlusToSpace() {
        assertEquals(" ", URLUtil.decode("+"));
    }

    /**
     * 验证 decode(+, isPlusToSpace=false) 保留 +.
     */
    @Test
    void decodePlusForPath() {
        assertEquals("+", URLUtil.decode("+", StandardCharsets.UTF_8, false));
    }

    /**
     * 验证 encode/decode UTF-16 编码.
     */
    @Test
    void encodeDecodeUtf16() {
        final String s = "测试";
        final String encoded = URLUtil.encode(s, StandardCharsets.UTF_16);
        assertEquals("%FE%FF%6D%4B%8B%D5", encoded);
        assertEquals(s, URLUtil.decode(encoded, StandardCharsets.UTF_16));
    }

    /**
     * 验证 decode 混合编码与非编码字符串.
     */
    @Test
    void decodeMixedEncoded() {
        final String mixed = "%E6%B5%8B%E8%AF%95你好";
        assertEquals("测试你好", URLUtil.decode(mixed));
    }

    // --------------------------------------- encodeBlank

    /**
     * 验证 {@link URLUtil#encodeBlank(CharSequence)} 将空格编码为 %20.
     */
    @Test
    void encodeBlank() {
        assertEquals("hello%20world", URLUtil.encodeBlank("hello world"));
    }

    /**
     * 验证 encodeBlank 对 null 返回 null.
     */
    @Test
    void encodeBlankNull() {
        assertNull(URLUtil.encodeBlank(null));
    }

    /**
     * 验证 encodeBlank 不改变无空格的字符串.
     */
    @Test
    void encodeBlankNoChange() {
        assertEquals("hello", URLUtil.encodeBlank("hello"));
    }

    // --------------------------------------- normalize

    /**
     * 验证 {@link URLUtil#normalize(String)} 自动补齐 http://.
     */
    @Test
    void normalizeAutoProtocol() {
        assertEquals("http://example.com/path", URLUtil.normalize("example.com/path"));
    }

    /**
     * 验证 normalize 保留已有协议.
     */
    @Test
    void normalizeKeepProtocol() {
        assertEquals("https://example.com/path", URLUtil.normalize("https://example.com/path"));
    }

    /**
     * 验证 normalize 替换反斜杠为正斜杠.
     */
    @Test
    void normalizeReplaceBackslash() {
        assertEquals("http://example.com/path/to", URLUtil.normalize("example.com\\path\\to"));
    }

    /**
     * 验证 normalize 去除开头的 / 或 \.
     */
    @Test
    void normalizeRemoveLeadingSlash() {
        assertEquals("http://example.com/path", URLUtil.normalize("/example.com/path"));
        assertEquals("http://example.com/path", URLUtil.normalize("\\example.com/path"));
    }

    /**
     * 验证 normalize 保留查询参数.
     */
    @Test
    void normalizeKeepParams() {
        assertEquals("http://example.com/path?key=value", URLUtil.normalize("example.com/path?key=value"));
    }

    /**
     * 验证 normalize with replaceSlash 替换多个斜杠.
     */
    @Test
    void normalizeReplaceSlash() {
        assertEquals("http://example.com/path/to",
                URLUtil.normalize("example.com//path///to", false, true));
    }

    /**
     * 验证 normalize 默认不替换多个斜杠.
     */
    @Test
    void normalizeKeepDoubleSlash() {
        assertEquals("http://example.com//path", URLUtil.normalize("example.com//path"));
    }

    /**
     * 验证 normalize 空字符串返回空.
     */
    @Test
    void normalizeBlank() {
        assertEquals("", URLUtil.normalize(""));
        assertNull(URLUtil.normalize(null));
    }

    /**
     * 验证 normalize with isEncodePath 对路径编码.
     */
    @Test
    void normalizeWithEncodePath() {
        final String result = URLUtil.normalize("example.com/测试", true);
        assertEquals("http://example.com/%E6%B5%8B%E8%AF%95", result);
    }

    // ---- hutool 对齐：normalize 边界场景

    /**
     * issue#I25MZL，多个 / 被允许.
     */
    @Test
    void normalizeMultiSlashInPath() {
        String url = "http://www.hutool.cn//aaa/bbb";
        String normalize = URLUtil.normalize(url);
        assertEquals("http://www.hutool.cn//aaa/bbb", normalize);

        url = "www.hutool.cn//aaa/bbb";
        normalize = URLUtil.normalize(url);
        assertEquals("http://www.hutool.cn//aaa/bbb", normalize);
    }

    /**
     * normalize 反斜杠 + 查询参数.
     */
    @Test
    void normalizeBackslashWithQuery() {
        String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url);
        assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

        url = "www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url);
        assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
    }

    /**
     * normalize with isEncodePath=true + 反斜杠前缀 \\.
     */
    @Test
    void normalizeBackslashPrefix() {
        String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        String normalize = URLUtil.normalize(url, true);
        assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

        url = "www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url, true);
        assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);

        url = "\\/www.hutool.cn//aaa/bbb?a=1&b=2";
        normalize = URLUtil.normalize(url, true);
        assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
    }

    /**
     * normalize IPv6 URL.
     */
    @Test
    void normalizeIpv6() {
        final String url = "http://[fe80::8f8:2022:a603:d180]:9439";
        final String normalize = URLUtil.normalize(url, true);
        assertEquals(url, normalize);
    }

    /**
     * normalize 以 // 开头（无协议）.
     */
    @Test
    void normalizeDoubleSlashPrefix() {
        final String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
        final String normalize = URLUtil.normalize(url);
        assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);
    }

    // --------------------------------------- toURI

    /**
     * 验证 {@link URLUtil#toURI(String)} 字符串转 URI.
     */
    @Test
    void toURIFromString() {
        final URI uri = URLUtil.toURI("http://example.com/path");
        assertNotNull(uri);
        assertEquals("http://example.com/path", uri.toString());
    }

    /**
     * 验证 {@link URLUtil#toURI(String)} 去除首尾空白.
     */
    @Test
    void toURITrim() {
        final URI uri = URLUtil.toURI("  http://example.com  ");
        assertEquals("http://example.com", uri.toString());
    }

    /**
     * 验证 toURI null 返回 null.
     */
    @Test
    void toURINull() {
        assertNull(URLUtil.toURI((String) null));
    }

    /**
     * 验证 toURI 非法 URI 抛出 UtilException.
     */
    @Test
    void toURISyntaxError() {
        assertThrows(UtilException.class, () -> URLUtil.toURI("http://exa mple.com"));
    }

    /**
     * 验证 {@link URLUtil#toURI(URL)} URL 转 URI.
     */
    @Test
    void toURIFromURL() throws Exception {
        final URL url = new URL("http://example.com/path");
        final URI uri = URLUtil.toURI(url);
        assertNotNull(uri);
        assertEquals("/path", uri.getPath());
    }

    /**
     * 验证 toURI 简单路径后 resolve(".").
     */
    @Test
    void toURIAndResolve() {
        final String fileFullName = "/Uploads/20240601/aaaa.txt";
        final URI uri = URLUtil.toURI(fileFullName);
        final URI resolve = uri.resolve(".");
        assertEquals("/Uploads/20240601/", resolve.toString());
    }

    // --------------------------------------- getPath / getDecodedPath

    /**
     * 验证 {@link URLUtil#getPath(String)} 获取路径部分.
     */
    @Test
    void getPath() {
        assertEquals("/path/to/resource", URLUtil.getPath("http://example.com/path/to/resource"));
    }

    /**
     * 验证 getPath 带前导空格和查询参数.
     */
    @Test
    void getPathWithLeadingSpaceAndQuery() {
        final String url = " http://www.aaa.bbb/search?scope=ccc&q=ddd";
        assertEquals("/search", URLUtil.getPath(url));
    }

    /**
     * 验证 {@link URLUtil#getDecodedPath(URL)} 解码路径.
     */
    @Test
    void getDecodedPath() throws Exception {
        final URL url = new URL("http://example.com/path%20with%20space");
        final String path = URLUtil.getDecodedPath(url);
        assertNotNull(path);
        assertTrue(path.contains("path"));
    }

    /**
     * 验证 getDecodedPath null 返回 null.
     */
    @Test
    void getDecodedPathNull() {
        assertNull(URLUtil.getDecodedPath(null));
    }

    // --------------------------------------- getHost

    /**
     * 验证 {@link URLUtil#getHost(URL)} 提取域名部分.
     */
    @Test
    void getHost() throws Exception {
        final URL url = new URL("http://example.com:8080/path?key=value");
        final URI host = URLUtil.getHost(url);
        assertNotNull(host);
        assertEquals("http", host.getScheme());
        assertEquals("example.com", host.getHost());
    }

    /**
     * 验证 getHost null 返回 null.
     */
    @Test
    void getHostNull() {
        assertNull(URLUtil.getHost(null));
    }

    /**
     * 验证 normalize 后 getHost 提取域名.
     */
    @Test
    void getHostAfterNormalize() throws Exception {
        final String url = "https://www.hutool.cn//aaa/\\bbb?a=1&b=2";
        final String normalize = URLUtil.normalize(url);
        final URI host = URLUtil.getHost(new URL(normalize));
        assertEquals("https://www.hutool.cn", host.toString());
    }

    // --------------------------------------- completeUrl

    /**
     * 验证 {@link URLUtil#completeUrl(String, String)} 补全相对路径.
     */
    @Test
    void completeUrl() {
        final String result = URLUtil.completeUrl("http://example.com/base/", "../path");
        assertEquals("http://example.com/path", result);
    }

    /**
     * 验证 completeUrl 空基准 URL 返回 null.
     */
    @Test
    void completeUrlBlankBase() {
        assertNull(URLUtil.completeUrl("", "path"));
    }

    // --------------------------------------- isFileURL / isJarURL / isJarFileURL

    /**
     * 验证 {@link URLUtil#isFileURL(URL)} 判断 file 协议.
     */
    @Test
    void isFileURL() throws Exception {
        final URL fileUrl = new File("/tmp/test.txt").toURI().toURL();
        assertTrue(URLUtil.isFileURL(fileUrl));
    }

    /**
     * 验证 isFileURL 对 http 协议返回 false.
     */
    @Test
    void isFileURLNotFile() throws Exception {
        final URL httpUrl = new URL("http://example.com/path");
        assertFalse(URLUtil.isFileURL(httpUrl));
    }

    /**
     * 验证 isFileURL null 抛出异常.
     */
    @Test
    void isFileURLNull() {
        assertThrows(IllegalArgumentException.class, () -> URLUtil.isFileURL(null));
    }

    /**
     * 验证 {@link URLUtil#isJarURL(URL)} 判断 jar 协议.
     */
    @Test
    void isJarURL() throws Exception {
        final URL jarUrl = new URL("jar:file:/test.jar!/com/test.class");
        assertTrue(URLUtil.isJarURL(jarUrl));
    }

    /**
     * 验证 isJarURL 对 file 协议返回 false.
     */
    @Test
    void isJarURLNotJar() throws Exception {
        final URL fileUrl = new URL("file:/test.txt");
        assertFalse(URLUtil.isJarURL(fileUrl));
    }

    /**
     * 验证 {@link URLUtil#isJarFileURL(URL)} 判断 jar 文件 URL.
     */
    @Test
    void isJarFileURL() throws Exception {
        final URL jarFileUrl = new URL("file:/test/test.jar");
        assertTrue(URLUtil.isJarFileURL(jarFileUrl));
    }

    /**
     * 验证 isJarFileURL 对非 .jar 文件返回 false.
     */
    @Test
    void isJarFileURLNotJar() throws Exception {
        final URL txtUrl = new URL("file:/test/test.txt");
        assertFalse(URLUtil.isJarFileURL(txtUrl));
    }

    // --------------------------------------- url 创建

    /**
     * 验证 {@link URLUtil#url(URI)} URI 转 URL.
     */
    @Test
    void urlFromURI() throws Exception {
        final URI uri = new URI("http://example.com/path");
        final URL url = URLUtil.url(uri);
        assertNotNull(url);
        assertEquals("http://example.com/path", url.toString());
    }

    /**
     * 验证 url(URI) null 返回 null.
     */
    @Test
    void urlFromURINull() {
        assertNull(URLUtil.url((URI) null));
    }

    /**
     * 验证 {@link URLUtil#url(String)} 字符串创建 URL.
     */
    @Test
    void urlFromString() {
        final URL url = URLUtil.url("http://example.com/path");
        assertNotNull(url);
        assertEquals("http://example.com/path", url.toString());
    }

    /**
     * 验证 url(null) 返回 null.
     */
    @Test
    void urlFromStringNull() {
        assertNull(URLUtil.url((String) null));
    }

    /**
     * 验证 {@link URLUtil#url(String)} classpath 协议.
     */
    @Test
    void urlClasspath() {
        final URL url = URLUtil.url("classpath:cc/hongz/ztools/core/util/URLUtil.class");
        assertNotNull(url);
        assertTrue(url.toString().startsWith("file:") || url.toString().contains("URLUtil.class"));
    }

    /**
     * 验证 {@link URLUtil#url(String)} 文件路径自动转 file URL.
     */
    @Test
    void urlFilePath() {
        final URL url = URLUtil.url("/tmp/nonexistent-file-test");
        assertNotNull(url);
        assertEquals("file", url.getProtocol());
    }

    // --------------------------------------- getStringURI

    /**
     * 验证 {@link URLUtil#getStringURI(CharSequence)} 创建 string 协议 URI.
     */
    @Test
    void getStringURI() {
        final URI uri = URLUtil.getStringURI("hello_world");
        assertNotNull(uri);
        assertEquals("string:///hello_world", uri.toString());
    }

    /**
     * 验证 getStringURI 已有前缀时不重复添加.
     */
    @Test
    void getStringURIWithPrefix() {
        final URI uri = URLUtil.getStringURI("string:///test");
        assertEquals("string:///test", uri.toString());
    }

    /**
     * 验证 getStringURI null 返回 null.
     */
    @Test
    void getStringURINull() {
        assertNull(URLUtil.getStringURI(null));
    }

    // --------------------------------------- toUrlForHttp

    /**
     * 验证 {@link URLUtil#toUrlForHttp(String)} 创建 HTTP URL.
     */
    @Test
    void toUrlForHttp() {
        final URL url = URLUtil.toUrlForHttp("http://example.com/path");
        assertNotNull(url);
        assertEquals("http://example.com/path", url.toString());
    }

    /**
     * 验证 toUrlForHttp 编码空格.
     */
    @Test
    void toUrlForHttpEncodeBlank() {
        final URL url = URLUtil.toUrlForHttp("http://example.com/path with space");
        assertNotNull(url);
        assertTrue(url.toString().contains("%20"));
        assertFalse(url.toString().contains(" "));
    }

    /**
     * 验证 toUrlForHttp 空字符串抛出异常.
     */
    @Test
    void toUrlForHttpBlank() {
        assertThrows(IllegalArgumentException.class, () -> URLUtil.toUrlForHttp(""));
    }

    // --------------------------------------- getURL (File)

    /**
     * 验证 {@link URLUtil#getURL(File)} 从文件获取 URL.
     */
    @Test
    void getURLFromFile() throws IOException {
        final File file = tempDir.resolve("test.txt").toFile();
        Files.write(file.toPath(), "hello".getBytes());
        final URL url = URLUtil.getURL(file);
        assertNotNull(url);
        assertEquals("file", url.getProtocol());
    }

    /**
     * 验证 getURL(null) 抛出异常.
     */
    @Test
    void getURLFromFileNull() {
        assertThrows(IllegalArgumentException.class, () -> URLUtil.getURL((File) null));
    }

    /**
     * 验证 {@link URLUtil#getURLs(File...)} 批量获取 URL.
     */
    @Test
    void getURLs() throws IOException {
        final File file1 = tempDir.resolve("a.txt").toFile();
        final File file2 = tempDir.resolve("b.txt").toFile();
        Files.write(file1.toPath(), "a".getBytes());
        Files.write(file2.toPath(), "b".getBytes());
        final URL[] urls = URLUtil.getURLs(file1, file2);
        assertEquals(2, urls.length);
        assertNotNull(urls[0]);
        assertNotNull(urls[1]);
    }

    // --------------------------------------- getStream / getReader

    /**
     * 验证 {@link URLUtil#getStream(URL)} 从文件 URL 获取流.
     */
    @Test
    void getStream() throws IOException {
        final File file = tempDir.resolve("stream.txt").toFile();
        Files.write(file.toPath(), "hello".getBytes());
        final URL url = file.toURI().toURL();
        try (InputStream is = URLUtil.getStream(url)) {
            final byte[] bytes = is.readAllBytes();
            assertEquals("hello", new String(bytes));
        }
    }

    /**
     * 验证 getStream(null) 抛出异常.
     */
    @Test
    void getStreamNull() {
        assertThrows(IllegalArgumentException.class, () -> URLUtil.getStream(null));
    }

    /**
     * 验证 {@link URLUtil#getReader(URL, java.nio.charset.Charset)} 获取 Reader.
     */
    @Test
    void getReader() throws IOException {
        final File file = tempDir.resolve("reader.txt").toFile();
        Files.write(file.toPath(), "hello world".getBytes());
        final URL url = file.toURI().toURL();
        try (BufferedReader reader = URLUtil.getReader(url, StandardCharsets.UTF_8)) {
            final String line = reader.readLine();
            assertEquals("hello world", line);
        }
    }

    // --------------------------------------- buildQuery

    /**
     * 验证 {@link URLUtil#buildQuery(Map, java.nio.charset.Charset)} 构建 URL 查询参数.
     */
    @Test
    void buildQuery() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", null);
        assertEquals("key1=value1&key2=value2&key3=", URLUtil.buildQuery(params, StandardCharsets.UTF_8));
    }

    /**
     * 验证 buildQuery 跳过空 key.
     */
    @Test
    void buildQuerySkipEmptyKey() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("", "value1");
        params.put("key2", "value2");
        assertEquals("key2=value2", URLUtil.buildQuery(params, StandardCharsets.UTF_8));
    }

    /**
     * 验证 buildQuery 对中文编码.
     */
    @Test
    void buildQueryEncode() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", "测试");
        assertEquals("name=%E6%B5%8B%E8%AF%95", URLUtil.buildQuery(params, StandardCharsets.UTF_8));
    }

    /**
     * 验证 buildQuery 空参数返回空字符串.
     */
    @Test
    void buildQueryEmpty() {
        assertEquals("", URLUtil.buildQuery(null, StandardCharsets.UTF_8));
    }

    // --------------------------------------- getContentLength / size

    /**
     * 验证 {@link URLUtil#getContentLength(URL)} 获取文件长度.
     */
    @Test
    void getContentLength() throws Exception {
        // 使用模块根目录下的 pom.xml，避免 classpath 和 TempDir 文件锁问题
        final File pomFile = new File("pom.xml");
        assumeTrue(pomFile.exists(), "pom.xml should exist in module working directory");
        final URL url = pomFile.toURI().toURL();
        final long length = URLUtil.getContentLength(url);
        assertTrue(length > 0);
    }

    /**
     * 验证 getContentLength(null) 返回 -1.
     */
    @Test
    void getContentLengthNull() {
        assertEquals(-1, URLUtil.getContentLength(null));
    }

    /**
     * 验证 {@link URLUtil#size(URL)} 获取文件大小.
     */
    @Test
    void size() throws IOException {
        final File file = tempDir.resolve("size.txt").toFile();
        Files.write(file.toPath(), "hello".getBytes());
        final URL url = file.toURI().toURL();
        assertEquals(5, URLUtil.size(url));
    }

    // --------------------------------------- useCachesIfNecessary

    /**
     * 验证 {@link URLUtil#useCachesIfNecessary(java.net.URLConnection)} 非 JNLP 连接设置 useCache=false.
     */
    @Test
    void useCachesIfNecessary() throws IOException {
        final File file = tempDir.resolve("cache.txt").toFile();
        Files.write(file.toPath(), "hello".getBytes());
        final URL url = file.toURI().toURL();
        final java.net.URLConnection conn = url.openConnection();
        URLUtil.useCachesIfNecessary(conn);
        assertFalse(conn.getUseCaches());
        conn.getInputStream().close();
    }

    // --------------------------------------- 常量

    /**
     * 验证 URL 常量值.
     */
    @Test
    void constants() {
        assertEquals("classpath:", URLUtil.CLASSPATH_URL_PREFIX);
        assertEquals("file:", URLUtil.FILE_URL_PREFIX);
        assertEquals("jar:", URLUtil.JAR_URL_PREFIX);
        assertEquals("war:", URLUtil.WAR_URL_PREFIX);
        assertEquals("file", URLUtil.URL_PROTOCOL_FILE);
        assertEquals("jar", URLUtil.URL_PROTOCOL_JAR);
        assertEquals("zip", URLUtil.URL_PROTOCOL_ZIP);
        assertEquals("wsjar", URLUtil.URL_PROTOCOL_WSJAR);
        assertEquals("vfszip", URLUtil.URL_PROTOCOL_VFSZIP);
        assertEquals("vfsfile", URLUtil.URL_PROTOCOL_VFSFILE);
        assertEquals("vfs", URLUtil.URL_PROTOCOL_VFS);
        assertEquals("!/", URLUtil.JAR_URL_SEPARATOR);
        assertEquals("*/", URLUtil.WAR_URL_SEPARATOR);
    }
}
