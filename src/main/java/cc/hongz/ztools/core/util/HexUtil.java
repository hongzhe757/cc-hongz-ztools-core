package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.codec.Base16Codec;
import cc.hongz.ztools.core.exceptions.UtilException;

import java.awt.Color;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * 十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）.<br>
 * 例如十进制数57，在二进制写作111001，在16进制写作39.<br>
 * 像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20<br>
 * <p>
 * 参考：https://my.oschina.net/xinxingegeya/blog/287476
 *
 * @author Looly
 */
public final class HexUtil {

    /**
     * 十六进制基数.
     */
    private static final int HEX_RADIX = 16;

    /**
     * 十六进制前缀长度，如0x、0X.
     */
    private static final int HEX_PREFIX_LEN = 2;

    /**
     * Unicode十六进制表示的长度.
     */
    private static final int UNICODE_HEX_LEN = 4;

    /**
     * Unicode转义序列的字符串构建器初始容量.
     */
    private static final int UNICODE_BUILDER_SIZE = 6;

    /**
     * 单个十六进制字符的长度.
     */
    private static final int SINGLE_HEX_LEN = 1;

    private HexUtil() {
    }

    /**
     * 判断给定字符串是否为16进制数.<br>
     * 如果是，需要使用对应数字类型对象的{@code decode}方法解码<br>
     * 例如：{@code Integer.decode}方法解码int类型的16进制数字
     *
     * @param value 值
     * @return 是否为16进制
     */
    public static boolean isHexNumber(final String value) {
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        if (StrUtil.startWith(value, '-')) {
            // issue#2875
            return false;
        }
        int index = 0;
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += HEX_PREFIX_LEN;
        } else if (value.startsWith("#", index)) {
            index++;
        }
        try {
            new BigInteger(value.substring(index), HEX_RADIX);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------------- encode

    /**
     * 将字节数组转换为十六进制字符数组.
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组.
     *
     * @param str     字符串
     * @param charset 编码
     * @return 十六进制char[]
     */
    public static char[] encodeHex(final String str, final Charset charset) {
        return encodeHex(StrUtil.bytes(str, charset), true);
    }

    /**
     * 将字节数组转换为十六进制字符数组.
     *
     * @param data        byte[]
     * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return (toLowerCase ? Base16Codec.CODEC_LOWER : Base16Codec.CODEC_UPPER).encode(data);
    }

    /**
     * 将字节数组转换为十六进制字符串.
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(final byte[] data) {
        return encodeHexStr(data, true);
    }

    /**
     * 将字符串转换为十六进制字符串，结果为小写.
     *
     * @param data    需要被编码的字符串
     * @param charset 编码
     * @return 十六进制String
     */
    public static String encodeHexStr(final String data, final Charset charset) {
        return encodeHexStr(StrUtil.bytes(data, charset), true);
    }

    /**
     * 将字符串转换为十六进制字符串，结果为小写，默认编码是UTF-8.
     *
     * @param data 被编码的字符串
     * @return 十六进制String
     */
    public static String encodeHexStr(final String data) {
        return encodeHexStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将字节数组转换为十六进制字符串.
     *
     * @param data        byte[]
     * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(final byte[] data, final boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    // ---------------------------------------------------------------------------------------------------- decode

    /**
     * 将十六进制字符数组转换为字符串，默认编码UTF-8.
     *
     * @param hexStr 十六进制String
     * @return 字符串
     */
    public static String decodeHexStr(final String hexStr) {
        return decodeHexStr(hexStr, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将十六进制字符数组转换为字符串.
     *
     * @param hexStr  十六进制String
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(final String hexStr, final Charset charset) {
        if (StrUtil.isEmpty(hexStr)) {
            return hexStr;
        }
        return StrUtil.str(decodeHex(hexStr), charset);
    }

    /**
     * 将十六进制字符数组转换为字符串.
     *
     * @param hexData 十六进制char[]
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(final char[] hexData, final Charset charset) {
        return StrUtil.str(decodeHex(hexData), charset);
    }

    /**
     * 将十六进制字符串解码为byte[].
     *
     * @param hexStr 十六进制String
     * @return byte[]
     */
    public static byte[] decodeHex(final String hexStr) {
        return decodeHex((CharSequence) hexStr);
    }

    /**
     * 将十六进制字符数组转换为字节数组.
     *
     * @param hexData 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(final char[] hexData) {
        return decodeHex(String.valueOf(hexData));
    }

    /**
     * 将十六进制字符数组转换为字节数组.
     *
     * @param hexData 十六进制字符串
     * @return byte[]
     * @throws UtilException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     * @since 5.6.6
     */
    public static byte[] decodeHex(final CharSequence hexData) {
        return Base16Codec.CODEC_LOWER.decode(hexData);
    }

    // ---------------------------------------------------------------------------------------- Color

    /**
     * 将{@link Color}编码为Hex形式.
     *
     * @param color {@link Color}
     * @return Hex字符串
     * @since 3.0.8
     */
    public static String encodeColor(final Color color) {
        return encodeColor(color, "#");
    }

    /**
     * 将{@link Color}编码为Hex形式.
     *
     * @param color  {@link Color}
     * @param prefix 前缀字符串，可以是#、0x等
     * @return Hex字符串
     * @since 3.0.8
     */
    public static String encodeColor(final Color color, final String prefix) {
        final StringBuilder builder = new StringBuilder(prefix);
        String colorHex;
        colorHex = Integer.toHexString(color.getRed());
        if (SINGLE_HEX_LEN == colorHex.length()) {
            builder.append('0');
        }
        builder.append(colorHex);
        colorHex = Integer.toHexString(color.getGreen());
        if (SINGLE_HEX_LEN == colorHex.length()) {
            builder.append('0');
        }
        builder.append(colorHex);
        colorHex = Integer.toHexString(color.getBlue());
        if (SINGLE_HEX_LEN == colorHex.length()) {
            builder.append('0');
        }
        builder.append(colorHex);
        return builder.toString();
    }

    /**
     * 将Hex颜色值转为.
     *
     * @param hexColor 16进制颜色值，可以以#开头，也可以用0x开头
     * @return {@link Color}
     * @since 3.0.8
     */
    public static Color decodeColor(final String hexColor) {
        return Color.decode(hexColor);
    }

    /**
     * 将指定int值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式.<br>
     * 转换的字符串如果u后不足4位，则前面用0填充，例如：
     *
     * <pre>
     * '你' =》\u4f60
     * </pre>
     *
     * @param value int值，也可以是char
     * @return Unicode表现形式
     */
    public static String toUnicodeHex(final int value) {
        final StringBuilder builder = new StringBuilder(UNICODE_BUILDER_SIZE);

        builder.append("\\u");
        String hex = toHex(value);
        int len = hex.length();
        if (len < UNICODE_HEX_LEN) {
            builder.append("0000", 0, UNICODE_HEX_LEN - len); // 不足4位补0
        }
        builder.append(hex);

        return builder.toString();
    }

    /**
     * 将指定char值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式.<br>
     * 转换的字符串如果u后不足4位，则前面用0填充，例如：
     *
     * <pre>
     * '你' =》'\u4f60'
     * </pre>
     *
     * @param ch char值
     * @return Unicode表现形式
     * @since 4.0.1
     */
    public static String toUnicodeHex(final char ch) {
        return Base16Codec.CODEC_LOWER.toUnicodeHex(ch);
    }

    /**
     * 转为16进制字符串.
     *
     * @param value int值
     * @return 16进制字符串
     * @since 4.4.1
     */
    public static String toHex(final int value) {
        return Integer.toHexString(value);
    }

    /**
     * 16进制字符串转为int.
     *
     * @param value 16进制字符串
     * @return 16进制字符串int值
     * @since 5.7.4
     */
    public static int hexToInt(final String value) {
        return Integer.parseInt(removeHexPrefix(value), HEX_RADIX);
    }

    /**
     * 转为16进制字符串.
     *
     * @param value int值
     * @return 16进制字符串
     * @since 4.4.1
     */
    public static String toHex(final long value) {
        return Long.toHexString(value);
    }

    /**
     * 16进制字符串转为long.
     *
     * @param value 16进制字符串
     * @return long值
     * @since 5.7.4
     */
    public static long hexToLong(final String value) {
        return Long.parseLong(removeHexPrefix(value), HEX_RADIX);
    }

    /**
     * 转为16进制字符串.
     *
     * @param value float值
     * @return 16进制字符串
     */
    public static String toHex(final float value) {
        return Integer.toHexString(Float.floatToIntBits(value));
    }

    /**
     * 16进制字符串转为float.
     *
     * @param value 16进制字符串
     * @return 16进制字符串float值
     */
    public static float hexToFloat(final String value) {
        return Float.intBitsToFloat(Integer.parseUnsignedInt(removeHexPrefix(value), HEX_RADIX));
    }

    /**
     * 转为16进制字符串.
     *
     * @param value double值
     * @return 16进制字符串
     */
    public static String toHex(final double value) {
        return Long.toHexString(Double.doubleToLongBits(value));
    }

    /**
     * 16进制字符串转为double.
     *
     * @param value 16进制字符串
     * @return 16进制字符串double值
     */
    public static double hexToDouble(final String value) {
        return Double.longBitsToDouble(Long.parseUnsignedLong(removeHexPrefix(value), HEX_RADIX));
    }

    /**
     * 将byte值转为16进制并添加到{@link StringBuilder}中.
     *
     * @param builder     {@link StringBuilder}
     * @param b           byte
     * @param toLowerCase 是否使用小写
     * @since 4.4.1
     */
    public static void appendHex(final StringBuilder builder, final byte b, final boolean toLowerCase) {
        (toLowerCase ? Base16Codec.CODEC_LOWER : Base16Codec.CODEC_UPPER).appendHex(builder, b);
    }

    /**
     * Hex（16进制）字符串转为BigInteger.
     *
     * @param hexStr Hex(16进制字符串)
     * @return {@link BigInteger}
     * @since 5.2.0
     */
    public static BigInteger toBigInteger(final String hexStr) {
        if (null == hexStr) {
            return null;
        }
        return new BigInteger(removeHexPrefix(hexStr), HEX_RADIX);
    }

    /**
     * 格式化Hex字符串，结果为每2位加一个空格.
     * <p>
     * <pre>
     *     e8 8c 67 03 80 cb 22 00 95 26 8f
     * </pre>
     *
     * @param hexStr Hex字符串
     * @return 格式化后的字符串
     */
    public static String format(final String hexStr) {
        return format(hexStr, StrUtil.EMPTY);
    }

    /**
     * 格式化Hex字符串，结果为每2位加一个空格.
     * <p>
     * <pre>
     *     e8 8c 67 03 80 cb 22 00 95 26 8f
     * </pre>
     *
     * @param hexStr Hex字符串
     * @param prefix 自定义前缀，如0x
     * @return 格式化后的字符串
     */
    public static String format(final String hexStr, final String prefix) {
        if (StrUtil.isEmpty(hexStr)) {
            return StrUtil.EMPTY;
        }
        final String prefixNorm = (null == prefix) ? StrUtil.EMPTY : prefix;

        final int length = hexStr.length();
        final StringBuilder builder = StrUtil.builder(
                length + length / HEX_PREFIX_LEN + (length / HEX_PREFIX_LEN * prefixNorm.length()));

        for (int i = 0; i < length; i++) {
            if (i % HEX_PREFIX_LEN == 0) {
                if (i != 0) {
                    builder.append(CharUtil.SPACE);
                }
                builder.append(prefixNorm);
            }
            builder.append(hexStr.charAt(i));
        }
        return builder.toString();
    }

    /**
     * 去除十六进制字符串的常见前缀 0x、0X、#.
     *
     * @param hexStr 十六进制字符串
     * @return 去除前缀后的字符串
     */
    private static String removeHexPrefix(final String hexStr) {
        if (StrUtil.length(hexStr) > SINGLE_HEX_LEN) {
            final char c0 = hexStr.charAt(0);
            switch (c0) {
                case '0':
                    if (hexStr.charAt(SINGLE_HEX_LEN) == 'x' || hexStr.charAt(SINGLE_HEX_LEN) == 'X') {
                        return hexStr.substring(HEX_PREFIX_LEN);
                    }
                case '#':
                    return hexStr.substring(SINGLE_HEX_LEN);
            }
        }
        return hexStr;
    }
}
