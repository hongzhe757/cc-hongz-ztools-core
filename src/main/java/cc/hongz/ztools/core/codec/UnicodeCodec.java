package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * Unicode {@code \\uXXXX} 转义编解码（Java / JavaScript 风格）.
 *
 * @author hongzhe.qin
 */
@SuppressWarnings("checkstyle:MagicNumber")
public final class UnicodeCodec {

    /**
     * 私有构造，禁止实例化.
     */
    private UnicodeCodec() {
    }

    // --------------------------------------- Static method start

    /**
     * 将字符串编码为 {@code \\uXXXX} 序列（BMP 字符；增补字符拆为两个 surrogate）.
     *
     * @param text 原文
     * @return 转义文本，text 为 null 时返回 null
     */
    public static String encode(final CharSequence text) {
        if (text == null) {
            return null;
        }
        if (StrUtil.isEmpty(text)) {
            return text.toString();
        }
        final String s = text.toString();
        final StringBuilder sb = new StringBuilder(s.length() * 6);
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c <= 0x7F && c != '\\') {
                sb.append(c);
            } else if (c == '\\') {
                sb.append("\\\\");
            } else if (Character.isHighSurrogate(c) && i + 1 < s.length()) {
                final char low = s.charAt(i + 1);
                if (Character.isLowSurrogate(low)) {
                    sb.append(Base16Codec.CODEC_LOWER.toUnicodeHex(c));
                    sb.append(Base16Codec.CODEC_LOWER.toUnicodeHex(low));
                    i++;
                    continue;
                }
                sb.append(Base16Codec.CODEC_LOWER.toUnicodeHex(c));
            } else {
                sb.append(Base16Codec.CODEC_LOWER.toUnicodeHex(c));
            }
        }
        return sb.toString();
    }

    /**
     * 解码 {@code \\uXXXX} 序列.
     *
     * @param encoded 转义文本
     * @return 原文，encoded 为 null 时返回 null
     */
    public static String decode(final CharSequence encoded) {
        if (encoded == null) {
            return null;
        }
        final String s = encoded.toString();
        final StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                final char next = s.charAt(i + 1);
                if (next == '\\') {
                    sb.append('\\');
                    i++;
                    continue;
                }
                if (next == 'u' && i + 5 < s.length()) {
                    final int code = Integer.parseInt(s.substring(i + 2, i + 6), 16);
                    sb.append((char) code);
                    i += 5;
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    // --------------------------------------- Static method end
}
