package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.nio.charset.Charset;

/**
 * Base32 - encodes and decodes RFC4648 Base32 (see https://datatracker.ietf.org/doc/html/rfc4648#section-6 ).
 *
 * <p>
 * base32 就是用 32（2 的 5 次方）个特定 ASCII 码来表示 256 个 ASCII 码。
 * 所以，5 个 ASCII 字符经过 base32 编码后会变为 8 个字符（公约数为 40），长度增加 3/5，不足 8n 用“=”补足。
 * 根据 RFC4648 Base32 规范，支持两种模式：
 * <ul>
 *     <li>Base 32 Alphabet                 (ABCDEFGHIJKLMNOPQRSTUVWXYZ234567)</li>
 *     <li>"Extended Hex" Base 32 Alphabet  (0123456789ABCDEFGHIJKLMNOPQRSTUV)</li>
 * </ul>
 *
 * @author Looly
 */
public final class Base32 {

    /**
     * 私有构造，禁止实例化.
     */
    private Base32() {
    }

    //----------------------------------------------------------------------------------------- encode

    /**
     * 编码.
     *
     * @param bytes 数据
     * @return base32
     */
    public static String encode(final byte[] bytes) {
        return Base32Codec.INSTANCE.encode(bytes);
    }

    /**
     * base32 编码.
     *
     * @param source 被编码的 base32 字符串
     * @return 被加密后的字符串
     */
    public static String encode(final String source) {
        return encode(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base32 编码.
     *
     * @param source  被编码的 base32 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encode(final String source, final Charset charset) {
        return encode(StrUtil.bytes(source, charset));
    }

    /**
     * 编码（Hex 模式）.
     *
     * @param bytes 数据
     * @return base32
     */
    public static String encodeHex(final byte[] bytes) {
        return Base32Codec.INSTANCE.encode(bytes, true);
    }

    /**
     * base32 编码（Hex 模式）.
     *
     * @param source 被编码的 base32 字符串
     * @return 被加密后的字符串
     */
    public static String encodeHex(final String source) {
        return encodeHex(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base32 编码（Hex 模式）.
     *
     * @param source  被编码的 base32 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encodeHex(final String source, final Charset charset) {
        return encodeHex(StrUtil.bytes(source, charset));
    }

    //----------------------------------------------------------------------------------------- decode

    /**
     * 解码.
     *
     * @param base32 base32 编码
     * @return 数据
     */
    public static byte[] decode(final String base32) {
        return Base32Codec.INSTANCE.decode(base32);
    }

    /**
     * base32 解码.
     *
     * @param source 被解码的 base32 字符串
     * @return 被加密后的字符串
     */
    public static String decodeStr(final String source) {
        return decodeStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base32 解码.
     *
     * @param source  被解码的 base32 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String decodeStr(final String source, final Charset charset) {
        return StrUtil.str(decode(source), charset);
    }

    /**
     * 解码（Hex 模式）.
     *
     * @param base32 base32 编码
     * @return 数据
     */
    public static byte[] decodeHex(final String base32) {
        return Base32Codec.INSTANCE.decode(base32, true);
    }

    /**
     * base32 解码（Hex 模式）.
     *
     * @param source 被解码的 base32 字符串
     * @return 被加密后的字符串
     */
    public static String decodeStrHex(final String source) {
        return decodeStrHex(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base32 解码（Hex 模式）.
     *
     * @param source  被解码的 base32 字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String decodeStrHex(final String source, final Charset charset) {
        return StrUtil.str(decodeHex(source), charset);
    }
}
