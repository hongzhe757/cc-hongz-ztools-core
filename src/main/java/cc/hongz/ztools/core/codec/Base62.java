package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Base62 工具类，提供 Base62 的编码和解码方案.
 *
 * @author Looly
 * @since 4.5.9
 */
public final class Base62 {

    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    /**
     * 私有构造，禁止实例化.
     */
    private Base62() {
    }

    // -------------------------------------------------------------------- encode

    /**
     * Base62 编码.
     *
     * @param source 被编码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String encode(final CharSequence source) {
        return encode(source, DEFAULT_CHARSET);
    }

    /**
     * Base62 编码.
     *
     * @param source  被编码的 Base62 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encode(final CharSequence source, final Charset charset) {
        return encode(StrUtil.bytes(source, charset));
    }

    /**
     * Base62 编码.
     *
     * @param source 被编码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String encode(final byte[] source) {
        return new String(Base62Codec.INSTANCE.encode(source));
    }

    /**
     * Base62 编码.
     *
     * @param in 被编码 Base62 的流（一般为图片流或者文件流）
     * @return 被加密后的字符串
     */
    public static String encode(final InputStream in) {
        return encode(IoUtil.readBytes(in));
    }

    /**
     * Base62 编码.
     *
     * @param file 被编码 Base62 的文件
     * @return 被加密后的字符串
     */
    public static String encode(final File file) {
        return encode(FileUtil.readBytes(file));
    }

    /**
     * Base62 编码（反转字母表模式）.
     *
     * @param source 被编码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String encodeInverted(final CharSequence source) {
        return encodeInverted(source, DEFAULT_CHARSET);
    }

    /**
     * Base62 编码（反转字母表模式）.
     *
     * @param source  被编码的 Base62 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encodeInverted(final CharSequence source, final Charset charset) {
        return encodeInverted(StrUtil.bytes(source, charset));
    }

    /**
     * Base62 编码（反转字母表模式）.
     *
     * @param source 被编码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String encodeInverted(final byte[] source) {
        return new String(Base62Codec.INSTANCE.encode(source, true));
    }

    /**
     * Base62 编码（反转字母表模式）.
     *
     * @param in 被编码 Base62 的流（一般为图片流或者文件流）
     * @return 被加密后的字符串
     */
    public static String encodeInverted(final InputStream in) {
        return encodeInverted(IoUtil.readBytes(in));
    }

    /**
     * Base62 编码（反转字母表模式）.
     *
     * @param file 被编码 Base62 的文件
     * @return 被加密后的字符串
     */
    public static String encodeInverted(final File file) {
        return encodeInverted(FileUtil.readBytes(file));
    }

    // -------------------------------------------------------------------- decode

    /**
     * Base62 解码（GBK 字符集）.
     *
     * @param source 被解码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String decodeStrGbk(final CharSequence source) {
        return decodeStr(source, CharsetUtil.CHARSET_GBK);
    }

    /**
     * Base62 解码.
     *
     * @param source 被解码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String decodeStr(final CharSequence source) {
        return decodeStr(source, DEFAULT_CHARSET);
    }

    /**
     * Base62 解码.
     *
     * @param source  被解码的 Base62 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String decodeStr(final CharSequence source, final Charset charset) {
        return StrUtil.str(decode(source), charset);
    }

    /**
     * Base62 解码到文件.
     *
     * @param base62   被解码的 Base62 字符串
     * @param destFile 目标文件
     * @return 目标文件
     */
    public static File decodeToFile(final CharSequence base62, final File destFile) {
        return FileUtil.writeBytes(decode(base62), destFile);
    }

    /**
     * Base62 解码到流.
     *
     * @param base62Str  被解码的 Base62 字符串
     * @param out        写出到的流
     * @param isCloseOut 是否关闭输出流
     */
    public static void decodeToStream(final CharSequence base62Str, final OutputStream out, final boolean isCloseOut) {
        IoUtil.write(out, isCloseOut, decode(base62Str));
    }

    /**
     * Base62 解码.
     *
     * @param base62Str 被解码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static byte[] decode(final CharSequence base62Str) {
        return decode(StrUtil.bytes(base62Str, DEFAULT_CHARSET));
    }

    /**
     * 解码 Base62.
     *
     * @param base62bytes Base62 输入
     * @return 解码后的 bytes
     */
    public static byte[] decode(final byte[] base62bytes) {
        return Base62Codec.INSTANCE.decode(base62bytes);
    }

    /**
     * Base62 解码（反转字母表模式）.
     *
     * @param source 被解码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static String decodeStrInverted(final CharSequence source) {
        return decodeStrInverted(source, DEFAULT_CHARSET);
    }

    /**
     * Base62 解码（反转字母表模式）.
     *
     * @param source  被解码的 Base62 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String decodeStrInverted(final CharSequence source, final Charset charset) {
        return StrUtil.str(decodeInverted(source), charset);
    }

    /**
     * Base62 解码到文件（反转字母表模式）.
     *
     * @param base62   被解码的 Base62 字符串
     * @param destFile 目标文件
     * @return 目标文件
     */
    public static File decodeToFileInverted(final CharSequence base62, final File destFile) {
        return FileUtil.writeBytes(decodeInverted(base62), destFile);
    }

    /**
     * Base62 解码到流（反转字母表模式）.
     *
     * @param base62Str  被解码的 Base62 字符串
     * @param out        写出到的流
     * @param isCloseOut 是否关闭输出流
     */
    public static void decodeToStreamInverted(final CharSequence base62Str, final OutputStream out,
            final boolean isCloseOut) {
        IoUtil.write(out, isCloseOut, decodeInverted(base62Str));
    }

    /**
     * Base62 解码（反转字母表模式）.
     *
     * @param base62Str 被解码的 Base62 字符串
     * @return 被加密后的字符串
     */
    public static byte[] decodeInverted(final CharSequence base62Str) {
        return decodeInverted(StrUtil.bytes(base62Str, DEFAULT_CHARSET));
    }

    /**
     * 解码 Base62（反转字母表模式）.
     *
     * @param base62bytes Base62 输入
     * @return 解码后的 bytes
     */
    public static byte[] decodeInverted(final byte[] base62bytes) {
        return Base62Codec.INSTANCE.decode(base62bytes, true);
    }
}
