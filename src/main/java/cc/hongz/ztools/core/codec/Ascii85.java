package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;

import java.io.ByteArrayOutputStream;

/**
 * ASCII85（Base85）编码，Adobe PDF / PostScript 常用格式.
 *
 * <p>
 * 每 4 字节编码为 5 个可打印字符（范围 {@code !}～{@code u}）；连续 4 个零字节编码为 {@code z}.
 * 与 Python {@code base64.a85encode} / {@code a85decode} 兼容.
 *
 * @author hongzhe.qin
 */
public final class Ascii85 {

    /**
     * 进制.
     */
    private static final int BASE = 85;

    /**
     * 首字符 {@code '!'}.
     */
    private static final char FIRST = '!';

    /**
     * {@code 85^4}.
     */
    private static final long POWER_4 = (long) BASE * BASE * BASE * BASE;

    /**
     * 私有构造，禁止实例化.
     */
    private Ascii85() {
    }

    // --------------------------------------- Static method start

    /**
     * 编码字节数组为 ASCII85 字符串.
     *
     * @param data 原始数据
     * @return 编码结果，data 为 null 或空时返回空串
     */
    public static String encode(final byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        final StringBuilder out = new StringBuilder(data.length * 5 / 4 + 8);
        for (int i = 0; i < data.length; i += 4) {
            final int chunkLen = Math.min(4, data.length - i);
            long value = 0;
            for (int j = 0; j < 4; j++) {
                value <<= 8;
                if (j < chunkLen) {
                    value |= data[i + j] & 0xFFL;
                }
            }
            if (chunkLen == 4 && value == 0) {
                out.append('z');
                continue;
            }
            final char[] chars = new char[5];
            long v = value;
            long divisor = POWER_4;
            for (int k = 0; k < 5; k++) {
                chars[k] = (char) (FIRST + v / divisor);
                v %= divisor;
                divisor /= BASE;
            }
            out.append(chars, 0, chunkLen + 1);
        }
        return out.toString();
    }

    /**
     * 解码 ASCII85 字符串为字节数组.
     *
     * @param encoded 编码文本
     * @return 原始字节，encoded 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence encoded) {
        if (encoded == null) {
            return null;
        }
        final String text = stripWrappers(encoded.toString().trim());
        if (text.isEmpty()) {
            return new byte[0];
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream(text.length());
        long tuple = 0;
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c == 'z') {
                if (count != 0) {
                    throw new UtilException("Invalid 'z' in ASCII85 at index {}", i);
                }
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(0);
                continue;
            }
            if (c < FIRST || c > 'u') {
                throw new UtilException("Invalid ASCII85 character '{}' at index {}", c, i);
            }
            tuple = tuple * BASE + (c - FIRST);
            count++;
            if (count == 5) {
                writeFullTuple(out, tuple);
                tuple = 0;
                count = 0;
            }
        }
        if (count > 0) {
            for (int j = count; j < 5; j++) {
                tuple = tuple * BASE + (BASE - 1);
            }
            writePartialTuple(out, tuple, count - 1);
        }
        return out.toByteArray();
    }

    // --------------------------------------- Static method end

    private static String stripWrappers(final String text) {
        if (text.startsWith("<~") && text.endsWith("~>")) {
            return text.substring(2, text.length() - 2);
        }
        return text;
    }

    private static void writeFullTuple(final ByteArrayOutputStream out, final long tuple) {
        out.write((int) (tuple >> 24) & 0xFF);
        out.write((int) (tuple >> 16) & 0xFF);
        out.write((int) (tuple >> 8) & 0xFF);
        out.write((int) tuple & 0xFF);
    }

    private static void writePartialTuple(final ByteArrayOutputStream out, final long tuple, final int bytes) {
        if (bytes >= 1) {
            out.write((int) (tuple >> 24) & 0xFF);
        }
        if (bytes >= 2) {
            out.write((int) (tuple >> 16) & 0xFF);
        }
        if (bytes >= 3) {
            out.write((int) (tuple >> 8) & 0xFF);
        }
    }
}
