package cc.hongz.ztools.core.io;

import cc.hongz.ztools.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 编码探测器.
 * @author looly
 * @author hongzhe.qin (enhanced)
 * @since 5.4.7
 */
public final class CharsetDetector {

    /**
     * 默认的参与测试的编码.
     */
    private static final Charset[] DEFAULT_CHARSETS;

    static {
        final String[] names = {
                "UTF-8",
                "GBK",
                "GB2312",
                "GB18030",
                "UTF-16BE",
                "UTF-16LE",
                "UTF-16",
                "BIG5",
                "UNICODE",
                "US-ASCII"
        };
        final Charset[] charsets = new Charset[names.length];
        for (int i = 0; i < names.length; i++) {
            charsets[i] = Charset.forName(names[i]);
        }
        DEFAULT_CHARSETS = charsets;
    }

    /**
     * 私有构造，禁止实例化.
     */
    private CharsetDetector() {
    }

    // --------------------------------------- Static method start

    /**
     * 探测文件编码.
     *
     * @param file     文件
     * @param charsets 需要测试用的编码，null 或空使用默认的编码数组
     * @return 编码
     * @since 5.6.7
     */
    public static Charset detect(final File file, final Charset... charsets) {
        return detect(IoUtil.toStream(file), charsets);
    }

    /**
     * 探测编码.
     * <p>
     * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持 reset 方法的流。
     *
     * @param in       流，使用后关闭此流
     * @param charsets 需要测试用的编码，null 或空使用默认的编码数组
     * @return 编码
     */
    public static Charset detect(final InputStream in, final Charset... charsets) {
        return detect(IoUtil.DEFAULT_LARGE_BUFFER_SIZE, in, charsets);
    }

    /**
     * 探测编码.
     * <p>
     * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持 reset 方法的流。
     *
     * @param bufferSize 自定义缓存大小，即每次检查的长度
     * @param in         流，使用后关闭此流
     * @param charsets   需要测试用的编码，null 或空使用默认的编码数组
     * @return 编码
     * @since 5.7.10
     */
    public static Charset detect(final int bufferSize, final InputStream in, final Charset... charsets) {
        final Charset[] cs = ArrayUtil.isEmpty(charsets) ? DEFAULT_CHARSETS : charsets;

        final byte[] buffer = new byte[bufferSize];
        try {
            while (in.read(buffer) > -1) {
                for (Charset charset : cs) {
                    final CharsetDecoder decoder = charset.newDecoder();
                    if (identify(buffer, decoder)) {
                        return charset;
                    }
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
        return null;
    }

    // --------------------------------------- Static method end

    /**
     * 通过 try 的方式测试指定 bytes 是否可以被解码，从而判断是否为指定编码.
     *
     * @param bytes   测试的 bytes
     * @param decoder 解码器
     * @return 是否是指定编码
     */
    private static boolean identify(final byte[] bytes, final CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
}
