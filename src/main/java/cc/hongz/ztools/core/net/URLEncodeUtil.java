package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.nio.charset.Charset;

/**
 * URL编码工具.<br>
 * 基于 RFC3986 规范，提供不同层级的 URL 编码方法。
 *
 * <pre>
 * 编码层级（允许的字符集依次扩大）：
 *   encodeAll           → UNRESERVED  仅保留 ALPHA/DIGIT/-/./_/~
 *   encodePathSegment   → SEGMENT     pchar（不含 "/"）
 *   encode              → PATH        pchar + "/"
 *   encodeQuery         → QUERY       pchar + "/" + "?"
 *   encodeFragment      → FRAGMENT    同 QUERY
 * </pre>
 *
 * @author looly
 * @since 5.7.13
 */
public class URLEncodeUtil {

    private URLEncodeUtil() {
    }

    /**
     * 编码URL，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法仅保留 RFC3986 UNRESERVED 字符，其余全部编码。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeAll(final String url) throws UtilException {
        return encodeAll(url, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 编码URL<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 此方法仅保留 RFC3986 UNRESERVED 字符，其余全部编码。
     *
     * @param url     URL
     * @param charset 编码，为null表示不编码
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeAll(final String url, final Charset charset) throws UtilException {
        return RFC3986.UNRESERVED.encode(url, charset);
    }

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
        return encode(url, CharsetUtil.CHARSET_UTF_8);
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
        return RFC3986.PATH.encode(url, charset);
    }

    /**
     * 编码URL中查询语句，默认使用UTF-8编码<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 QUERY 规范，保留 pchar + "/" + "?" 字符。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeQuery(final String url) throws UtilException {
        return encodeQuery(url, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 编码字符为URL中查询语句<br>
     * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
     * 遵循 RFC3986 QUERY 规范，保留 pchar + "/" + "?" 字符。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodeQuery(final String url, final Charset charset) {
        return RFC3986.QUERY.encode(url, charset);
    }

    /**
     * 编码URL的Segment，默认使用UTF-8编码<br>
     * 遵循 RFC3986 SEGMENT 规范，保留 pchar 字符，但 {@code /} 会被编码。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodePathSegment(final String url) throws UtilException {
        return encodePathSegment(url, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 编码URL的Segment<br>
     * 遵循 RFC3986 SEGMENT 规范，保留 pchar 字符，但 {@code /} 会被编码。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodePathSegment(final String url, final Charset charset) {
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        return RFC3986.SEGMENT.encode(url, charset);
    }

    /**
     * 编码URL的Fragment，默认使用UTF-8编码<br>
     * 遵循 RFC3986 FRAGMENT 规范，保留 pchar + "/" + "?" 字符。<br>
     * FRAGMENT 与 QUERY 允许的字符集相同。
     *
     * @param url URL
     * @return 编码后的URL
     * @throws UtilException UnsupportedEncodingException
     */
    public static String encodeFragment(final String url) throws UtilException {
        return encodeFragment(url, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 编码URL的Fragment<br>
     * 遵循 RFC3986 FRAGMENT 规范，保留 pchar + "/" + "?" 字符。
     *
     * @param url     被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encodeFragment(final String url, final Charset charset) {
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        return RFC3986.FRAGMENT.encode(url, charset);
    }
}
