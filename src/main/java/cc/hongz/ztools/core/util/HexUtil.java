package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.codec.Base16Codec;

/**
 * 十六进制工具类（codec 最小子集）.
 *
 * @author looly
 */
public final class HexUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private HexUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 将 byte 值转为 16 进制并追加到 {@link StringBuilder}.
     *
     * @param builder     构建器
     * @param b           字节
     * @param toLowerCase 是否小写
     */
    public static void appendHex(final StringBuilder builder, final byte b, final boolean toLowerCase) {
        (toLowerCase ? Base16Codec.CODEC_LOWER : Base16Codec.CODEC_UPPER).appendHex(builder, b);
    }

    /**
     * 将字节数组转为十六进制字符串.
     *
     * @param data        字节数组
     * @param toLowerCase true 小写，false 大写
     * @return 十六进制字符串
     */
    public static String encodeHexStr(final byte[] data, final boolean toLowerCase) {
        if (data == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            appendHex(sb, b, toLowerCase);
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串解码为字节数组.
     *
     * @param hexStr 十六进制字符串
     * @return 字节数组
     */
    public static byte[] decodeHex(final CharSequence hexStr) {
        return Base16Codec.CODEC_LOWER.decode(hexStr);
    }

    // --------------------------------------- Static method end
}
