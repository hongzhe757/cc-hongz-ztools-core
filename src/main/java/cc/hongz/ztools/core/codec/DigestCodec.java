package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要 Hex 编码（MD5 / SHA 系列）.
 *
 * <p>
 * 摘要结果以 {@link Base16Codec} 小写 Hex 输出.
 *
 * @author hongzhe.qin
 */
public final class DigestCodec {

    /**
     * 私有构造，禁止实例化.
     */
    private DigestCodec() {
    }

    // --------------------------------------- Static method start

    /**
     * MD5 摘要 Hex（小写）.
     *
     * @param data 原始字节
     * @return Hex 字符串，data 为 null 时返回 null
     */
    public static String md5Hex(final byte[] data) {
        return digestHex("MD5", data);
    }

    /**
     * MD5 摘要 Hex（小写，UTF-8 文本）.
     *
     * @param text 文本
     * @return Hex 字符串
     */
    public static String md5Hex(final CharSequence text) {
        return md5Hex(text, StandardCharsets.UTF_8);
    }

    /**
     * MD5 摘要 Hex（小写）.
     *
     * @param text    文本
     * @param charset 字符集
     * @return Hex 字符串
     */
    public static String md5Hex(final CharSequence text, final Charset charset) {
        if (text == null) {
            return null;
        }
        return md5Hex(StrUtil.bytes(text, charset));
    }

    /**
     * SHA-256 摘要 Hex（小写）.
     *
     * @param data 原始字节
     * @return Hex 字符串，data 为 null 时返回 null
     */
    public static String sha256Hex(final byte[] data) {
        return digestHex("SHA-256", data);
    }

    /**
     * SHA-256 摘要 Hex（小写，UTF-8 文本）.
     *
     * @param text 文本
     * @return Hex 字符串
     */
    public static String sha256Hex(final CharSequence text) {
        return sha256Hex(text, StandardCharsets.UTF_8);
    }

    /**
     * SHA-256 摘要 Hex（小写）.
     *
     * @param text    文本
     * @param charset 字符集
     * @return Hex 字符串
     */
    public static String sha256Hex(final CharSequence text, final Charset charset) {
        if (text == null) {
            return null;
        }
        return sha256Hex(StrUtil.bytes(text, charset));
    }

    /**
     * 指定算法摘要 Hex（小写）.
     *
     * @param algorithm 算法名（如 {@code MD5}、{@code SHA-256}）
     * @param data      原始字节
     * @return Hex 字符串，data 为 null 时返回 null
     */
    public static String digestHex(final String algorithm, final byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            return StrUtil.str(new String(Base16Codec.CODEC_LOWER.encode(digest.digest(data))));
        } catch (NoSuchAlgorithmException e) {
            throw new UtilException(e);
        }
    }

    // --------------------------------------- Static method end
}
