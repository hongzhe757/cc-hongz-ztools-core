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
 * Base64 工具类，提供 Base64 的编码和解码方案.
 *
 * <p>
 * base64 编码是用 64（2 的 6 次方）个 ASCII 字符来表示 256（2 的 8 次方）个 ASCII 字符，
 * 也就是三位二进制数组经过编码后变为四位的 ASCII 字符显示，长度比原来增加 1/3。
 *
 * @author Looly
 */
public final class Base64 {

    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    /**
     * 私有构造，禁止实例化.
     */
    private Base64() {
    }

    // -------------------------------------------------------------------- encode

    /**
     * 编码为 Base64，非 URL 安全的.
     *
     * @param arr     被编码的数组
     * @param lineSep 在 76 个 char 之后是 CRLF 还是 EOF
     * @return 编码后的 bytes
     */
    public static byte[] encode(final byte[] arr, final boolean lineSep) {
        if (arr == null) {
            return null;
        }
        return lineSep ?
                java.util.Base64.getMimeEncoder().encode(arr) :
                java.util.Base64.getEncoder().encode(arr);
    }

    /**
     * 编码为 Base64，URL 安全的.
     *
     * @param arr     被编码的数组
     * @param lineSep 在 76 个 char 之后是 CRLF 还是 EOF
     * @return 编码后的 bytes
     * @since 3.0.6
     * @deprecated 按照 RFC2045 规范，URL 安全的 Base64 无需换行
     */
    @Deprecated
    public static byte[] encodeUrlSafe(final byte[] arr, final boolean lineSep) {
        return Base64Encoder.encodeUrlSafe(arr, lineSep);
    }

    /**
     * base64 编码.
     *
     * @param source 被编码的 base64 字符串
     * @return 被加密后的字符串
     */
    public static String encode(final CharSequence source) {
        return encode(source, DEFAULT_CHARSET);
    }

    /**
     * base64 编码，URL 安全.
     *
     * @param source 被编码的 base64 字符串
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final CharSequence source) {
        return encodeUrlSafe(source, DEFAULT_CHARSET);
    }

    /**
     * base64 编码.
     *
     * @param source  被编码的 base64 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encode(final CharSequence source, final String charset) {
        return encode(source, CharsetUtil.charset(charset));
    }

    /**
     * base64 编码，不进行 padding（末尾不会填充 '='）.
     *
     * @param source  被编码的 base64 字符串
     * @param charset 编码
     * @return 被加密后的字符串
     * @since 5.5.2
     */
    public static String encodeWithoutPadding(final CharSequence source, final String charset) {
        return encodeWithoutPadding(StrUtil.bytes(source, charset));
    }

    /**
     * base64 编码，URL 安全.
     *
     * @param source  被编码的 base64 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     * @since 3.0.6
     * @deprecated 请使用 {@link #encodeUrlSafe(CharSequence, Charset)}
     */
    @Deprecated
    public static String encodeUrlSafe(final CharSequence source, final String charset) {
        return encodeUrlSafe(source, CharsetUtil.charset(charset));
    }

    /**
     * base64 编码.
     *
     * @param source  被编码的 base64 字符串
     * @param charset 字符集
     * @return 被编码后的字符串
     */
    public static String encode(final CharSequence source, final Charset charset) {
        return encode(StrUtil.bytes(source, charset));
    }

    /**
     * base64 编码，URL 安全的.
     *
     * @param source  被编码的 base64 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final CharSequence source, final Charset charset) {
        return encodeUrlSafe(StrUtil.bytes(source, charset));
    }

    /**
     * base64 编码.
     *
     * @param source 被编码的 base64 字符串
     * @return 被加密后的字符串
     */
    public static String encode(final byte[] source) {
        if (source == null) {
            return null;
        }
        return java.util.Base64.getEncoder().encodeToString(source);
    }

    /**
     * base64 编码，不进行 padding（末尾不会填充 '='）.
     *
     * @param source 被编码的 base64 字符串
     * @return 被加密后的字符串
     * @since 5.5.2
     */
    public static String encodeWithoutPadding(final byte[] source) {
        if (source == null) {
            return null;
        }
        return java.util.Base64.getEncoder().withoutPadding().encodeToString(source);
    }

    /**
     * base64 编码，URL 安全的.
     *
     * @param source 被编码的 base64 字符串
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final byte[] source) {
        if (source == null) {
            return null;
        }
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(source);
    }

    /**
     * base64 编码.
     *
     * @param in 被编码 base64 的流（一般为图片流或者文件流）
     * @return 被加密后的字符串
     * @since 4.0.9
     */
    public static String encode(final InputStream in) {
        return encode(IoUtil.readBytes(in));
    }

    /**
     * base64 编码，URL 安全的.
     *
     * @param in 被编码 base64 的流（一般为图片流或者文件流）
     * @return 被加密后的字符串
     * @since 4.0.9
     */
    public static String encodeUrlSafe(final InputStream in) {
        return encodeUrlSafe(IoUtil.readBytes(in));
    }

    /**
     * base64 编码.
     *
     * @param file 被编码 base64 的文件
     * @return 被加密后的字符串
     * @since 4.0.9
     */
    public static String encode(final File file) {
        return encode(FileUtil.readBytes(file));
    }

    /**
     * base64 编码，URL 安全的.
     *
     * @param file 被编码 base64 的文件
     * @return 被加密后的字符串
     * @since 4.0.9
     */
    public static String encodeUrlSafe(final File file) {
        return encodeUrlSafe(FileUtil.readBytes(file));
    }

    /**
     * 编码为 Base64 字符串.
     *
     * <p>
     * 如果 isMultiLine 为 {@code true}，则每 76 个字符一个换行符，否则在一行显示。
     *
     * @param arr         被编码的数组
     * @param isMultiLine 在 76 个 char 之后是 CRLF 还是 EOF
     * @param isUrlSafe   是否使用 URL 安全字符，一般为 {@code false}
     * @return 编码后的 bytes
     * @since 5.7.2
     */
    public static String encodeStr(final byte[] arr, final boolean isMultiLine, final boolean isUrlSafe) {
        return StrUtil.str(encode(arr, isMultiLine, isUrlSafe), DEFAULT_CHARSET);
    }

    /**
     * 编码为 Base64.
     *
     * <p>
     * 如果 isMultiLine 为 {@code true}，则每 76 个字符一个换行符，否则在一行显示。
     *
     * @param arr         被编码的数组
     * @param isMultiLine 在 76 个 char 之后是 CRLF 还是 EOF
     * @param isUrlSafe   是否使用 URL 安全字符，一般为 {@code false}
     * @return 编码后的 bytes
     */
    public static byte[] encode(final byte[] arr, final boolean isMultiLine, final boolean isUrlSafe) {
        return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
    }

    // -------------------------------------------------------------------- decode

    /**
     * base64 解码（GBK 字符集）.
     *
     * @param source 被解码的 base64 字符串
     * @return 密文解密的结果
     * @since 4.3.2
     */
    public static String decodeStrGbk(final CharSequence source) {
        return Base64Decoder.decodeStr(source, CharsetUtil.CHARSET_GBK);
    }

    /**
     * base64 解码.
     *
     * @param source 被解码的 base64 字符串
     * @return 密文解密的结果
     */
    public static String decodeStr(final CharSequence source) {
        return Base64Decoder.decodeStr(source);
    }

    /**
     * base64 解码.
     *
     * @param source  被解码的 base64 字符串
     * @param charset 字符集
     * @return 密文解密的结果
     */
    public static String decodeStr(final CharSequence source, final String charset) {
        return decodeStr(source, CharsetUtil.charset(charset));
    }

    /**
     * base64 解码.
     *
     * @param source  被解码的 base64 字符串
     * @param charset 字符集
     * @return 密文解密的结果
     */
    public static String decodeStr(final CharSequence source, final Charset charset) {
        return Base64Decoder.decodeStr(source, charset);
    }

    /**
     * base64 解码到文件.
     *
     * @param base64   被解码的 base64 字符串
     * @param destFile 目标文件
     * @return 目标文件
     * @since 4.0.9
     */
    public static File decodeToFile(final CharSequence base64, final File destFile) {
        return FileUtil.writeBytes(Base64Decoder.decode(base64), destFile);
    }

    /**
     * base64 解码到流.
     *
     * @param base64     被解码的 base64 字符串
     * @param out        写出到的流
     * @param isCloseOut 是否关闭输出流
     * @since 4.0.9
     */
    public static void decodeToStream(final CharSequence base64, final OutputStream out, final boolean isCloseOut) {
        IoUtil.write(out, isCloseOut, Base64Decoder.decode(base64));
    }

    /**
     * base64 解码.
     *
     * @param base64 被解码的 base64 字符串
     * @return 解码后的 bytes
     */
    public static byte[] decode(final CharSequence base64) {
        return Base64Decoder.decode(base64);
    }

    /**
     * 解码 Base64.
     *
     * @param in 输入
     * @return 解码后的 bytes
     */
    public static byte[] decode(final byte[] in) {
        return Base64Decoder.decode(in);
    }

    /**
     * 检查是否为 Base64.
     *
     * @param base64 Base64 的 bytes
     * @return 是否为 Base64
     * @since 5.7.5
     */
    public static boolean isBase64(final CharSequence base64) {
        if (base64 == null || base64.length() < 2) {
            return false;
        }

        final byte[] bytes = StrUtil.utf8Bytes(base64);

        if (bytes.length != base64.length()) {
            // 如果长度不相等，说明存在双字节字符，肯定不是 Base64，直接返回 false
            return false;
        }

        return isBase64(bytes);
    }

    /**
     * 检查是否为 Base64.
     *
     * @param base64Bytes Base64 的 bytes
     * @return 是否为 Base64
     * @since 5.7.5
     */
    public static boolean isBase64(final byte[] base64Bytes) {
        if (base64Bytes == null || base64Bytes.length < 3) {
            return false;
        }
        boolean hasPadding = false;
        for (byte base64Byte : base64Bytes) {
            if (hasPadding) {
                if ('=' != base64Byte) {
                    // 前一个字符是 '='，则后边的字符都必须是 '='，即 '=' 只能都位于结尾
                    return false;
                }
            } else if ('=' == base64Byte) {
                // 发现 '=' 标记之
                hasPadding = true;
            } else if (false == (Base64Decoder.isBase64Code(base64Byte) || isWhiteSpace(base64Byte))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为空白字符.
     *
     * @param byteToCheck 待检查字节
     * @return 是否为空白字符
     */
    private static boolean isWhiteSpace(final byte byteToCheck) {
        switch (byteToCheck) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
                return true;
            default:
                return false;
        }
    }
}
