package cc.hongz.ztools.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类（codec 最小子集）.
 *
 * @author xiaoleilu
 */
public final class CharsetUtil {

    /**
     * ISO-8859-1 名称.
     */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * UTF-8 名称.
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * GBK 名称.
     */
    public static final String GBK = "GBK";

    /**
     * ISO-8859-1 字符集.
     */
    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;

    /**
     * UTF-8 字符集.
     */
    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    /**
     * GBK 字符集（不支持 GBK 的环境为 null）.
     */
    public static final Charset CHARSET_GBK;

    static {
        Charset charsetGbk = null;
        try {
            charsetGbk = Charset.forName(GBK);
        } catch (UnsupportedCharsetException ignored) {
            // ignore
        }
        CHARSET_GBK = charsetGbk;
    }

    /**
     * 私有构造，禁止实例化.
     */
    private CharsetUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 转换为 {@link Charset} 对象.
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charset
     * @throws UnsupportedCharsetException 编码不支持
     */
    public static Charset charset(final String charsetName) throws UnsupportedCharsetException {
        return StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    /**
     * 转换字符串的字符集编码.
     *
     * @param source      字符串
     * @param srcCharset  源字符集名称，默认 ISO-8859-1
     * @param destCharset 目标字符集名称，默认 UTF-8
     * @return 转换后的字符串
     */
    public static String convert(final String source, final String srcCharset, final String destCharset) {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * 转换字符串的字符集编码.
     *
     * @param source      字符串
     * @param srcCharset  源字符集，默认 ISO-8859-1
     * @param destCharset 目标字符集，默认 UTF-8
     * @return 转换后的字符串
     */
    public static String convert(final String source, final Charset srcCharset, final Charset destCharset) {
        Charset src = srcCharset;
        Charset dest = destCharset;
        if (source == null) {
            return null;
        }
        if (src == null) {
            src = StandardCharsets.ISO_8859_1;
        }
        if (dest == null) {
            dest = StandardCharsets.UTF_8;
        }
        if (StrUtil.isBlank(source) || src.equals(dest)) {
            return source;
        }
        return new String(source.getBytes(src), dest);
    }

    /**
     * @return 系统字符集编码名称
     */
    public static String systemCharset() {
        return Charset.defaultCharset().name();
    }

    // --------------------------------------- Static method end
}
