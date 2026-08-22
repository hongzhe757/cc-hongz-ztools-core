package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

import java.util.Arrays;

/**
 * Base58 编码器.
 *
 * <p>
 * 此编码器不包括校验码、版本等信息.
 *
 * @author lin， looly
 * @since 5.7.22
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class Base58Codec implements Encoder<byte[], String>, Decoder<CharSequence, byte[]> {

    /**
     * 单例.
     */
    public static final Base58Codec INSTANCE = new Base58Codec();

    /**
     * Base58 编码.
     *
     * @param data 被编码的数据，不带校验和。
     * @return 编码后的字符串
     */
    @Override
    public String encode(final byte[] data) {
        return Base58Encoder.ENCODER.encode(data);
    }

    /**
     * 解码给定的 Base58 字符串.
     *
     * @param encoded Base58 编码字符串
     * @return 解码后的 bytes
     * @throws IllegalArgumentException 非标准 Base58 字符串
     */
    @Override
    public byte[] decode(final CharSequence encoded) throws IllegalArgumentException {
        return Base58Decoder.DECODER.decode(encoded);
    }

    /**
     * Base58 编码器.
     *
     * @since 5.8.0
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static class Base58Encoder implements Encoder<byte[], String> {
        private static final String DEFAULT_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

        /**
         * 默认字母表编码器.
         */
        public static final Base58Encoder ENCODER = new Base58Encoder(DEFAULT_ALPHABET.toCharArray());

        private final char[] alphabet;
        private final char alphabetZero;

        /**
         * 构造.
         *
         * @param alphabet 编码字母表
         */
        public Base58Encoder(final char[] alphabet) {
            this.alphabet = alphabet;
            alphabetZero = alphabet[0];
        }

        @Override
        public String encode(final byte[] data) {
            if (null == data) {
                return null;
            }
            if (data.length == 0) {
                return StrUtil.EMPTY;
            }
            // 计算开头 0 的个数
            int zeroCount = 0;
            while (zeroCount < data.length && data[zeroCount] == 0) {
                ++zeroCount;
            }
            // 将 256 位编码转换为 58 位编码
            final byte[] target = Arrays.copyOf(data, data.length);
            final char[] encoded = new char[target.length * 2];
            int outputStart = encoded.length;
            for (int inputStart = zeroCount; inputStart < target.length; ) {
                encoded[--outputStart] = alphabet[divmod(target, inputStart, 256, 58)];
                if (target[inputStart] == 0) {
                    ++inputStart;
                }
            }
            // 保留输入中前导零的个数
            while (outputStart < encoded.length && encoded[outputStart] == alphabetZero) {
                ++outputStart;
            }
            while (--zeroCount >= 0) {
                encoded[--outputStart] = alphabetZero;
            }
            return new String(encoded, outputStart, encoded.length - outputStart);
        }
    }

    /**
     * Base58 解码器.
     *
     * @since 5.8.0
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static class Base58Decoder implements Decoder<CharSequence, byte[]> {

        /**
         * 默认字母表解码器.
         */
        public static final Base58Decoder DECODER = new Base58Decoder(Base58Encoder.DEFAULT_ALPHABET);

        private final byte[] lookupTable;

        /**
         * 构造.
         *
         * @param alphabet 编码字符表
         */
        public Base58Decoder(final String alphabet) {
            final byte[] table = new byte['z' + 1];
            Arrays.fill(table, (byte) -1);

            final int length = alphabet.length();
            for (int i = 0; i < length; i++) {
                table[alphabet.charAt(i)] = (byte) i;
            }
            this.lookupTable = table;
        }

        @Override
        public byte[] decode(final CharSequence encoded) {
            if (encoded.length() == 0) {
                return new byte[0];
            }
            // 将 base58 编码的 ASCII 字符转换为 base58 字节序列（base58 数字）
            final byte[] input58 = new byte[encoded.length()];
            for (int i = 0; i < encoded.length(); ++i) {
                char c = encoded.charAt(i);
                int digit = c < 128 ? lookupTable[c] : -1;
                if (digit < 0) {
                    throw new IllegalArgumentException(StrUtil.format("Invalid char '{}' at [{}]", c, i));
                }
                input58[i] = (byte) digit;
            }
            // 计算前导零个数
            int zeros = 0;
            while (zeros < input58.length && input58[zeros] == 0) {
                ++zeros;
            }
            // 将 base-58 数字转换为 base-256 数字
            byte[] decoded = new byte[encoded.length()];
            int outputStart = decoded.length;
            for (int inputStart = zeros; inputStart < input58.length; ) {
                decoded[--outputStart] = divmod(input58, inputStart, 58, 256);
                if (input58[inputStart] == 0) {
                    ++inputStart;
                }
            }
            // 忽略计算过程中额外增加的前导零
            while (outputStart < decoded.length && decoded[outputStart] == 0) {
                ++outputStart;
            }
            return Arrays.copyOfRange(decoded, outputStart - zeros, decoded.length);
        }
    }

    /**
     * 将给定数字（以指定进制表示，每个字节一个数字）除以给定除数.
     *
     * <p>
     * 给定的数字会被原地修改为商，返回值是余数。
     *
     * @param number     the number to divide
     * @param firstDigit the index within the array of the first non-zero digit
     * @param base       the base in which the number's digits are represented (up to 256)
     * @param divisor    the number to divide by (up to 256)
     * @return the remainder of the division operation
     */
    private static byte divmod(final byte[] number, final int firstDigit, final int base, final int divisor) {
        int remainder = 0;
        for (int i = firstDigit; i < number.length; i++) {
            int digit = (int) number[i] & 0xFF;
            int temp = remainder * base + digit;
            number[i] = (byte) (temp / divisor);
            remainder = temp % divisor;
        }
        return (byte) remainder;
    }
}
