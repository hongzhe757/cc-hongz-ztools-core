package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * UUencode 编码（Unix-to-Unix，早期邮件/Usenet 附件）.
 *
 * <p>
 * 每行首字符表示本行字节数；3 字节一组映射为 4 个可打印字符（+32）. 完整格式含 {@code begin} / {@code end} 行.
 *
 * @author hongzhe.qin
 */
public final class UuCodec {

    /**
     * 每行最多编码字节数.
     */
    private static final int MAX_BYTES_PER_LINE = 45;

    /**
     * 默认文件权限模式.
     */
    private static final String DEFAULT_MODE = "644";

    /**
     * 默认文件名.
     */
    private static final String DEFAULT_NAME = "file";

    /**
     * 私有构造，禁止实例化.
     */
    private UuCodec() {
    }

    // --------------------------------------- Static method start

    /**
     * 编码为完整 UU 文件文本（含 {@code begin}/{@code end}）.
     *
     * @param data 原始字节
     * @return UU 文本，data 为 null 时返回 null
     */
    public static String encode(final byte[] data) {
        return encode(data, DEFAULT_NAME);
    }

    /**
     * 编码为完整 UU 文件文本.
     *
     * @param data     原始字节
     * @param fileName 文件名
     * @return UU 文本，data 为 null 时返回 null
     */
    public static String encode(final byte[] data, final String fileName) {
        if (data == null) {
            return null;
        }
        final String name = StrUtil.isBlank(fileName) ? DEFAULT_NAME : fileName.trim();
        final StringBuilder out = new StringBuilder(data.length * 4 / 3 + 64);
        out.append("begin ").append(DEFAULT_MODE).append(' ').append(name).append('\n');
        out.append(encodeBody(data));
        out.append("`\nend\n");
        return out.toString();
    }

    /**
     * 仅编码数据体（不含 {@code begin}/{@code end} 行）.
     *
     * @param data 原始字节
     * @return 行式 UU 数据
     */
    public static String encodeBody(final byte[] data) {
        if (data == null || data.length == 0) {
            return " \n";
        }
        final StringBuilder out = new StringBuilder(data.length * 4 / 3 + 16);
        for (int offset = 0; offset < data.length; offset += MAX_BYTES_PER_LINE) {
            final int chunkLen = Math.min(MAX_BYTES_PER_LINE, data.length - offset);
            out.append((char) (chunkLen + 32));
            appendChunk(out, data, offset, chunkLen);
            out.append('\n');
        }
        return out.toString();
    }

    /**
     * 解码完整 UU 文本或纯数据体.
     *
     * @param uuText UU 文本
     * @return 原始字节，uuText 为 null 时返回 null
     */
    public static byte[] decode(final CharSequence uuText) {
        if (uuText == null) {
            return null;
        }
        final java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        for (String line : uuText.toString().split("\\R")) {
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("begin ") || "end".equals(line.trim())) {
                continue;
            }
            decodeLine(line, out);
        }
        return out.toByteArray();
    }

    /**
     * 解码 UU 数据体.
     *
     * @param body 行式 UU 数据
     * @return 原始字节
     */
    public static byte[] decodeBody(final CharSequence body) {
        return decode(body);
    }

    // --------------------------------------- Static method end

    private static void appendChunk(final StringBuilder out, final byte[] data, final int offset, final int len) {
        int i = 0;
        while (i < len) {
            final int b1 = data[offset + i] & 0xFF;
            final int b2 = i + 1 < len ? data[offset + i + 1] & 0xFF : 0;
            final int b3 = i + 2 < len ? data[offset + i + 2] & 0xFF : 0;
            final int c1 = b1 >> 2;
            final int c2 = ((b1 & 0x03) << 4) | (b2 >> 4);
            final int c3 = ((b2 & 0x0F) << 2) | (b3 >> 6);
            final int c4 = b3 & 0x3F;
            out.append((char) (c1 + 32));
            out.append((char) (c2 + 32));
            out.append((char) (c3 + 32));
            out.append((char) (c4 + 32));
            i += 3;
        }
    }

    private static void decodeLine(final String line, final java.io.ByteArrayOutputStream out) {
        final int byteCount = line.charAt(0) - 32;
        if (byteCount <= 0) {
            return;
        }
        final String body = line.substring(1);
        int produced = 0;
        int i = 0;
        while (produced < byteCount && i + 3 < body.length()) {
            final int c1 = body.charAt(i) - 32;
            final int c2 = body.charAt(i + 1) - 32;
            final int c3 = body.charAt(i + 2) - 32;
            final int c4 = body.charAt(i + 3) - 32;
            out.write((c1 << 2) | ((c2 >> 4) & 0x03));
            produced++;
            if (produced < byteCount) {
                out.write(((c2 & 0x0F) << 4) | (c3 >> 2));
                produced++;
            }
            if (produced < byteCount) {
                out.write(((c3 & 0x03) << 6) | c4);
                produced++;
            }
            i += 4;
        }
    }
}
