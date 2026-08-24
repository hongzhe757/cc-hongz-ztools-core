package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * yEnc 编码（Usenet 二进制附件，比 UUencode 更高效）.
 *
 * <p>
 * 常规字节 {@code (b + 42) mod 256}；NUL/LF/CR/TAB/= 及行首 {@code .} 等用 {@code =XX} 转义（XX 为 {@code b + 64}）.
 * 完整格式含 {@code =ybegin} / {@code =yend} 头尾行及 CRC32.
 *
 * @author hongzhe.qin
 */
@SuppressWarnings("checkstyle:MagicNumber")
public final class YEnc {

    /**
     * 默认每行最大字符数.
     */
    private static final int DEFAULT_LINE_LENGTH = 128;

    /**
     * 默认文件名.
     */
    private static final String DEFAULT_NAME = "file";

    /**
     * {@code =ybegin} 行解析.
     */
    private static final Pattern BEGIN_PATTERN = Pattern.compile(
            "=ybegin\\s+line=(\\d+)\\s+size=(\\d+)\\s+name=(\\S+)", Pattern.CASE_INSENSITIVE);

    /**
     * {@code =yend} 行解析.
     */
    private static final Pattern END_PATTERN = Pattern.compile(
            "=yend\\s+size=(\\d+)\\s+crc=(\\p{XDigit}+)", Pattern.CASE_INSENSITIVE);

    /**
     * 私有构造，禁止实例化.
     */
    private YEnc() {
    }

    // --------------------------------------- Static method start

    /**
     * 编码为完整 yEnc 文本（含头尾与 CRC）.
     *
     * @param data 原始字节
     * @return yEnc 文本，data 为 null 时返回 null
     */
    public static String encode(final byte[] data) {
        return encode(data, DEFAULT_NAME, DEFAULT_LINE_LENGTH);
    }

    /**
     * 编码为完整 yEnc 文本.
     *
     * @param data     原始字节
     * @param fileName 文件名
     * @return yEnc 文本，data 为 null 时返回 null
     */
    public static String encode(final byte[] data, final String fileName) {
        return encode(data, fileName, DEFAULT_LINE_LENGTH);
    }

    /**
     * 编码为完整 yEnc 文本.
     *
     * @param data       原始字节
     * @param fileName   文件名
     * @param lineLength 每行最大字符数
     * @return yEnc 文本，data 为 null 时返回 null
     */
    public static String encode(final byte[] data, final String fileName, final int lineLength) {
        if (data == null) {
            return null;
        }
        final String name = StrUtil.isBlank(fileName) ? DEFAULT_NAME : fileName.trim();
        final int lineLen = lineLength > 0 ? lineLength : DEFAULT_LINE_LENGTH;
        final long crc = crc32(data);
        final StringBuilder out = new StringBuilder(data.length + 128);
        out.append(String.format(Locale.ROOT, "=ybegin line=%d size=%d name=%s%n", lineLen, data.length, name));
        out.append(encodeBody(data, lineLen));
        if (out.charAt(out.length() - 1) != '\n') {
            out.append('\n');
        }
        out.append(String.format(Locale.ROOT, "=yend size=%d crc=%08X%n", data.length, crc));
        return out.toString();
    }

    /**
     * 仅编码 yEnc 数据体.
     *
     * @param data 原始字节
     * @return 编码数据体
     */
    public static String encodeBody(final byte[] data) {
        return encodeBody(data, DEFAULT_LINE_LENGTH);
    }

    /**
     * 仅编码 yEnc 数据体.
     *
     * @param data       原始字节
     * @param lineLength 每行最大字符数
     * @return 编码数据体
     */
    public static String encodeBody(final byte[] data, final int lineLength) {
        if (data == null || data.length == 0) {
            return "";
        }
        final int lineLen = lineLength > 0 ? lineLength : DEFAULT_LINE_LENGTH;
        final StringBuilder out = new StringBuilder(data.length + 16);
        int linePos = 0;
        for (final byte b : data) {
            appendEncodedByte(out, b & 0xFF, linePos == 0);
            linePos++;
            if (linePos >= lineLen) {
                out.append('\n');
                linePos = 0;
            }
        }
        return out.toString();
    }

    /**
     * 解码完整 yEnc 文本或纯数据体.
     *
     * @param yencText yEnc 文本
     * @return 原始字节，yencText 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence yencText) {
        if (yencText == null) {
            return null;
        }
        final String text = yencText.toString();
        String body = text;
        Integer expectedSize = null;
        Long expectedCrc = null;

        final Matcher beginMatcher = BEGIN_PATTERN.matcher(text);
        if (beginMatcher.find()) {
            expectedSize = Integer.parseInt(beginMatcher.group(2));
            final int headerEnd = beginMatcher.end();
            final int endIndex = text.indexOf("=yend", headerEnd);
            body = endIndex >= 0 ? text.substring(headerEnd, endIndex) : text.substring(headerEnd);
            final Matcher endMatcher = END_PATTERN.matcher(text);
            if (endMatcher.find()) {
                expectedSize = Integer.parseInt(endMatcher.group(1));
                expectedCrc = Long.parseLong(endMatcher.group(2), 16);
            }
        }

        final byte[] decoded = decodeBody(body);
        if (expectedSize != null && decoded.length != expectedSize) {
            throw new UtilException("yEnc size mismatch: expected {}, got {}", expectedSize, decoded.length);
        }
        if (expectedCrc != null && crc32(decoded) != expectedCrc) {
            throw new UtilException("yEnc CRC mismatch: expected {}, got {}", String.format("%08X", expectedCrc),
                    String.format("%08X", crc32(decoded)));
        }
        return decoded;
    }

    /**
     * 解码 yEnc 数据体.
     *
     * @param body 编码数据体
     * @return 原始字节
     */
    public static byte[] decodeBody(final CharSequence body) {
        if (body == null) {
            return null;
        }
        final String compact = body.toString().replace("\r", "").replace("\n", "");
        final ByteArrayOutputStream out = new ByteArrayOutputStream(compact.length());
        for (int i = 0; i < compact.length(); i++) {
            final char c = compact.charAt(i);
            if (c == '=') {
                if (i + 1 >= compact.length()) {
                    throw new UtilException("Invalid yEnc escape at end of input");
                }
                out.write((compact.charAt(i + 1) - 64) & 0xFF);
                i++;
            } else {
                out.write((c - 42) & 0xFF);
            }
        }
        return out.toByteArray();
    }

    /**
     * 计算 CRC32（yEnc 头尾使用）.
     *
     * @param data 数据
     * @return CRC32 值
     */
    public static long crc32(final byte[] data) {
        final CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    // --------------------------------------- Static method end

    private static void appendEncodedByte(final StringBuilder out, final int b, final boolean lineStart) {
        final int normal = (b + 42) & 0xFF;
        final boolean escape = b == 0 || b == 9 || b == 10 || b == 13 || b == 61
                || normal == 0 || normal == 10 || normal == 13 || normal == 61
                || (lineStart && normal == 46);
        if (escape) {
            out.append('=').append((char) (b + 64));
        } else {
            out.append((char) normal);
        }
    }
}
