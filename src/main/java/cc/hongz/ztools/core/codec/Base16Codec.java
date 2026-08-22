package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * Base16（Hex）编码解码器.
 *
 * <p>
 * 十六进制（简写为 hex 或下标 16）在数学中是一种逢 16 进 1 的进位制，一般用数字 0 到 9 和字母 A 到 F 表示（其中 A~F 即 10~15）。
 * 例如十进制数 57，在二进制写作 111001，在 16 进制写作 39。
 *
 * @author looly
 * @since 5.7.23
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class Base16Codec implements Encoder<byte[], char[]>, Decoder<CharSequence, byte[]> {

    /**
     * 小写字母表.
     */
    public static final Base16Codec CODEC_LOWER = new Base16Codec(true);

    /**
     * 大写字母表.
     */
    public static final Base16Codec CODEC_UPPER = new Base16Codec(false);

    private final char[] alphabets;

    /**
     * 构造.
     *
     * @param lowerCase 是否小写
     */
    public Base16Codec(final boolean lowerCase) {
        this.alphabets = (lowerCase ? "0123456789abcdef" : "0123456789ABCDEF").toCharArray();
    }

    @Override
    public char[] encode(final byte[] data) {
        final int len = data.length;
        final char[] out = new char[len << 1];
        // 每字节编码为两个十六进制字符
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = alphabets[(0xF0 & data[i]) >>> 4];
            out[j++] = alphabets[0x0F & data[i]];
        }
        return out;
    }

    @Override
    public byte[] decode(final CharSequence encoded) {
        if (StrUtil.isEmpty(encoded)) {
            return null;
        }

        String target = StrUtil.cleanBlank(encoded);
        int len = target.length();

        if ((len & 0x01) != 0) {
            // 如果提供的数据是奇数长度，则前面补 0 凑偶数
            target = "0" + target;
            len = target.length();
        }

        final byte[] out = new byte[len >> 1];

        // 每两个字符组成一个十六进制值
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(target.charAt(j), j) << 4;
            j++;
            f = f | toDigit(target.charAt(j), j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将指定 char 值转换为 Unicode 字符串形式，常用于特殊字符（例如汉字）转 Unicode 形式.
     *
     * <p>
     * 转换的字符串如果 u 后不足 4 位，则前面用 0 填充，例如：
     *
     * <pre>
     * '你' =》'你'
     * </pre>
     *
     * @param ch char 值
     * @return Unicode 表现形式
     */
    public String toUnicodeHex(final char ch) {
        return "\\u" +
                alphabets[(ch >> 12) & 15] +
                alphabets[(ch >> 8) & 15] +
                alphabets[(ch >> 4) & 15] +
                alphabets[ch & 15];
    }

    /**
     * 将 byte 值转为 16 进制并添加到 {@link StringBuilder} 中.
     *
     * @param builder {@link StringBuilder}
     * @param b       byte
     */
    public void appendHex(final StringBuilder builder, final byte b) {
        int high = (b & 0xf0) >>> 4;
        int low = b & 0x0f;
        builder.append(alphabets[high]);
        builder.append(alphabets[low]);
    }

    /**
     * 将十六进制字符转换成一个整数.
     *
     * @param ch    十六进制 char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws UtilException 当 ch 不是一个合法的十六进制字符时，抛出运行时异常
     */
    private static int toDigit(final char ch, final int index) {
        int digit = Character.digit(ch, 16);
        if (digit < 0) {
            throw new UtilException("Illegal hexadecimal character {} at index {}", ch, index);
        }
        return digit;
    }
}
