package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Quoted-Printable 编码（RFC 2045 / MIME）.
 *
 * <p>
 * 将 8 位字节流编码为可打印 ASCII，非可打印字节以 {@code =XX} 表示；软换行以 {@code =} 加 CRLF 表示.
 *
 * @author hongzhe.qin
 */
public final class QuotedPrintable {

    /**
     * 每行最大长度（含软换行符 {@code =}）.
     */
    private static final int MAX_LINE_LENGTH = 76;

    /**
     * 私有构造，禁止实例化.
     */
    private QuotedPrintable() {
    }

    // --------------------------------------- Static method start

    /**
     * 编码字节数组为 Quoted-Printable 字符串（UTF-8）.
     *
     * @param data 原始字节
     * @return 编码结果，data 为 null 时返回 null
     */
    public static String encode(final byte[] data) {
        return encode(data, StandardCharsets.UTF_8);
    }

    /**
     * 编码字节数组为 Quoted-Printable 字符串.
     *
     * @param data    原始字节
     * @param charset 字符集
     * @return 编码结果，data 为 null 时返回 null
     */
    public static String encode(final byte[] data, final Charset charset) {
        if (data == null) {
            return null;
        }
        return encode(new String(data, charset == null ? StandardCharsets.UTF_8 : charset));
    }

    /**
     * 编码字符串为 Quoted-Printable（UTF-8 字节语义）.
     *
     * @param text 文本
     * @return 编码结果，text 为 null 时返回 null
     */
    public static String encode(final CharSequence text) {
        return encode(text, StandardCharsets.UTF_8);
    }

    /**
     * 编码字符串为 Quoted-Printable.
     *
     * @param text    文本
     * @param charset 字符集
     * @return 编码结果，text 为 null 时返回 null
     */
    public static String encode(final CharSequence text, final Charset charset) {
        if (text == null) {
            return null;
        }
        final Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        final byte[] bytes = StrUtil.bytes(text, cs);
        final StringBuilder out = new StringBuilder(bytes.length * 3);
        int lineLen = 0;
        for (final byte b : bytes) {
            final int unsigned = b & 0xFF;
            final String encodedChar = encodeByte(unsigned);
            if (lineLen + encodedChar.length() > MAX_LINE_LENGTH) {
                out.append("=\r\n");
                lineLen = 0;
            }
            out.append(encodedChar);
            lineLen += encodedChar.length();
        }
        return out.toString();
    }

    /**
     * 解码 Quoted-Printable 为字节数组（UTF-8）.
     *
     * @param encoded 编码文本
     * @return 原始字节，encoded 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence encoded) {
        return decode(encoded, StandardCharsets.UTF_8);
    }

    /**
     * 解码 Quoted-Printable 为字节数组.
     *
     * @param encoded 编码文本
     * @param charset 字符集（仅用于返回字符串时的语义，解码本身为字节级）
     * @return 原始字节，encoded 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence encoded, final Charset charset) {
        if (encoded == null) {
            return null;
        }
        final String text = encoded.toString();
        final byte[] buf = new byte[text.length()];
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '=') {
                if (i + 1 < text.length() && text.charAt(i + 1) == '\r') {
                    i++;
                    if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                        i++;
                    }
                    continue;
                }
                if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                    i++;
                    continue;
                }
                if (i + 2 < text.length()) {
                    final int hi = hexDigit(text.charAt(i + 1));
                    final int lo = hexDigit(text.charAt(i + 2));
                    buf[pos++] = (byte) ((hi << 4) + lo);
                    i += 2;
                    continue;
                }
            }
            buf[pos++] = (byte) c;
        }
        final byte[] result = new byte[pos];
        System.arraycopy(buf, 0, result, 0, pos);
        return result;
    }

    /**
     * 解码为字符串（UTF-8）.
     *
     * @param encoded 编码文本
     * @return 解码字符串
     */
    public static String decodeStr(final CharSequence encoded) {
        return decodeStr(encoded, StandardCharsets.UTF_8);
    }

    /**
     * 解码为字符串.
     *
     * @param encoded 编码文本
     * @param charset 字符集
     * @return 解码字符串
     */
    public static String decodeStr(final CharSequence encoded, final Charset charset) {
        final byte[] bytes = decode(encoded, charset);
        if (bytes == null) {
            return null;
        }
        return StrUtil.str(bytes, charset == null ? StandardCharsets.UTF_8 : charset);
    }

    // --------------------------------------- Static method end

    private static String encodeByte(final int unsigned) {
        if (unsigned == ' ') {
            return "=20";
        }
        if (unsigned >= 33 && unsigned <= 60 || unsigned >= 62 && unsigned <= 126) {
            return String.valueOf((char) unsigned);
        }
        if (unsigned == '\t') {
            return "=09";
        }
        if (unsigned == '\r') {
            return "=0D";
        }
        if (unsigned == '\n') {
            return "=0A";
        }
        return String.format("=%02X", unsigned);
    }

    private static int hexDigit(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException("Invalid hex digit: " + c);
    }
}
