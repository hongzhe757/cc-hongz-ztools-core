package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.io.CharsetDetector;
import cc.hongz.ztools.core.io.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类.
 * <p>
 * 提供字符集的常量定义、解析、字符串编码转换及系统编码探测等功能。
 *
 * @author xiaoleilu
 * @author hongzhe.qin (enhanced)
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
     * <p>空或 null 时返回系统默认字符集；不支持的编码抛出异常。
     *
     * @param charsetName 字符集名称
     * @return Charset
     * @throws UnsupportedCharsetException 编码不支持
     */
    public static Charset charset(final String charsetName) throws UnsupportedCharsetException {
        return StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    /**
     * 解析字符串编码为 Charset 对象，解析失败返回系统默认编码.
     *
     * @param charsetName 字符集名称
     * @return Charset，不支持时返回默认编码
     * @since 5.2.6
     */
    public static Charset parse(final String charsetName) {
        return parse(charsetName, Charset.defaultCharset());
    }

    /**
     * 解析字符串编码为 Charset 对象，解析失败返回指定默认编码.
     *
     * @param charsetName    字符集名称
     * @param defaultCharset 解析失败时使用的默认编码
     * @return Charset
     * @since 5.2.6
     */
    public static Charset parse(final String charsetName, final Charset defaultCharset) {
        if (StrUtil.isBlank(charsetName)) {
            return defaultCharset;
        }
        try {
            return Charset.forName(charsetName);
        } catch (UnsupportedCharsetException e) {
            return defaultCharset;
        }
    }

    /**
     * 转换字符串的字符集编码.
     * <p>
     * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br>
     * 此方法用于纠正因读取使用编码错误导致的乱码问题。<br>
     * 例如，在 Servlet 请求中客户端用 GBK 编码了请求参数，
     * 我们使用 UTF-8 读取到的是乱码，此时使用此方法即可还原原编码的内容。
     * <pre>
     * 客户端 → GBK 编码 → Servlet 容器 → UTF-8 解码 → 乱码
     * 乱码 → UTF-8 编码 → GBK 解码 → 正确内容
     * </pre>
     *
     * @param source      字符串
     * @param srcCharset  源字符集名称
     * @param destCharset 目标字符集名称
     * @return 转换后的字符串
     */
    public static String convert(final String source, final String srcCharset, final String destCharset) {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * 转换字符串的字符集编码.
     * <p>
     * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br>
     * 此方法用于纠正因读取使用编码错误导致的乱码问题。
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
     * 转换文件的编码.
     * <p>
     * 以源编码读取文件，以目标编码写入文件，从而改变文件编码。
     *
     * @param file        文件
     * @param srcCharset  源编码
     * @param destCharset 目标编码
     * @return 转换后的文件
     * @since 5.0.5
     */
    public static File convert(final File file, final Charset srcCharset, final Charset destCharset) {
        if (file == null) {
            return null;
        }
        final Charset src = srcCharset != null ? srcCharset : StandardCharsets.ISO_8859_1;
        final Charset dest = destCharset != null ? destCharset : StandardCharsets.UTF_8;
        final String content = FileUtil.readString(file, src);
        return FileUtil.writeString(content, file, dest);
    }

    // --------------------------------------- 系统编码相关

    /**
     * 系统字符集编码名称.
     * <p>Windows 系统返回 GBK，其他系统返回 {@link #defaultCharsetName()}。
     *
     * @return 系统字符集编码名称
     * @since 3.1.2
     */
    public static String systemCharsetName() {
        return systemCharset().name();
    }

    /**
     * 系统字符集编码.
     * <p>Windows 系统返回 GBK（若不支持则回退到默认编码），其他系统返回 {@link #defaultCharset()}。
     *
     * @return 系统字符集编码
     * @since 3.1.2
     */
    public static Charset systemCharset() {
        if (isWindows()) {
            return CHARSET_GBK != null ? CHARSET_GBK : defaultCharset();
        }
        return defaultCharset();
    }

    /**
     * 系统默认字符集编码名称.
     *
     * @return 默认字符集名称
     */
    public static String defaultCharsetName() {
        return defaultCharset().name();
    }

    /**
     * 系统默认字符集编码.
     *
     * @return 默认 Charset
     */
    public static Charset defaultCharset() {
        return Charset.defaultCharset();
    }

    /**
     * 探测输入流编码.
     * <p>
     * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持 reset 方法的流。
     *
     * @param in              输入流，使用后关闭此流
     * @param candidateCharsets 候选编码，null 或空时使用默认探测编码
     * @return 编码，探测不到时返回 null
     */
    public static Charset defaultCharset(final InputStream in, final Charset... candidateCharsets) {
        return CharsetDetector.detect(in, candidateCharsets);
    }

    /**
     * 判断当前操作系统是否为 Windows.
     *
     * @return true 如果 os.name 以 "windows" 开头（忽略大小写）
     */
    private static boolean isWindows() {
        final String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().startsWith("windows");
    }

    // --------------------------------------- Static method end
}
