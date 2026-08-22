package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩后再 Base64 编码（常见配置/数据传输格式）.
 *
 * @author hongzhe.qin
 */
public final class GzipBase64 {

    /**
     * 私有构造，禁止实例化.
     */
    private GzipBase64() {
    }

    /**
     * 解压缓冲区大小.
     */
    private static final int BUFFER_SIZE = 4096;

    // --------------------------------------- Static method start

    /**
     * GZIP 压缩后 Base64 编码.
     *
     * @param data 原始字节
     * @return Base64 字符串，data 为 null 时返回 null
     */
    public static String encode(final byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.encode(gzip(data));
    }

    /**
     * 字符串 GZIP 压缩后 Base64 编码（UTF-8）.
     *
     * @param text 文本
     * @return Base64 字符串
     */
    public static String encode(final CharSequence text) {
        return encode(text, StandardCharsets.UTF_8);
    }

    /**
     * 字符串 GZIP 压缩后 Base64 编码.
     *
     * @param text    文本
     * @param charset 字符集
     * @return Base64 字符串
     */
    public static String encode(final CharSequence text, final Charset charset) {
        if (text == null) {
            return null;
        }
        return encode(StrUtil.bytes(text, charset));
    }

    /**
     * Base64 解码后 GZIP 解压.
     *
     * @param base64 Base64 字符串
     * @return 原始字节，base64 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence base64) {
        if (base64 == null) {
            return null;
        }
        return gunzip(Base64.decode(base64));
    }

    /**
     * Base64 解码后 GZIP 解压为字符串（UTF-8）.
     *
     * @param base64 Base64 字符串
     * @return 解压后的文本
     */
    public static String decodeStr(final CharSequence base64) {
        return decodeStr(base64, StandardCharsets.UTF_8);
    }

    /**
     * Base64 解码后 GZIP 解压为字符串.
     *
     * @param base64  Base64 字符串
     * @param charset 字符集
     * @return 解压后的文本
     */
    public static String decodeStr(final CharSequence base64, final Charset charset) {
        final byte[] bytes = decode(base64);
        if (bytes == null) {
            return null;
        }
        return StrUtil.str(bytes, charset == null ? StandardCharsets.UTF_8 : charset);
    }

    // --------------------------------------- Static method end

    private static byte[] gzip(final byte[] data) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(data);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new UtilException(e);
        }
    }

    private static byte[] gunzip(final byte[] data) {
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data));
             ByteArrayOutputStream out = new ByteArrayOutputStream(data.length)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new UtilException(e);
        }
    }
}
