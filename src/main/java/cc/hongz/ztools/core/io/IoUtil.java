package cc.hongz.ztools.core.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import cc.hongz.ztools.core.util.HexUtil;

/**
 * IO 流工具类（captcha / file-type 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，静态方法聚合 IO 能力.
 * @author hongzhe.qin
 */
public final class IoUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private IoUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 将字节数组转为 {@link ByteArrayInputStream}.
     *
     * @param content 字节内容
     * @return 字节输入流，content 为 null 时返回 null
     */
    public static ByteArrayInputStream toStream(final byte[] content) {
        return content == null ? null : new ByteArrayInputStream(content);
    }

    /**
     * 转换为 {@link BufferedOutputStream}.
     *
     * @param out 原始输出流
     * @return 缓冲输出流
     */
    public static BufferedOutputStream toBuffered(final OutputStream out) {
        return out instanceof BufferedOutputStream
                ? (BufferedOutputStream) out
                : new BufferedOutputStream(out);
    }

    /**
     * 从输入流读取全部字节并关闭流.
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IORuntimeException IO 异常
     */
    public static byte[] readBytes(final InputStream in) throws IORuntimeException {
        return readBytes(in, true);
    }

    /**
     * 从输入流读取全部字节.
     *
     * @param in      输入流
     * @param isClose 读取后是否关闭流
     * @return 字节数组
     * @throws IORuntimeException IO 异常
     */
    public static byte[] readBytes(final InputStream in, final boolean isClose) throws IORuntimeException {
        if (in == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (isClose) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        }
    }

    /**
     * 向输出流写入字节数组.
     *
     * @param out        输出流
     * @param isCloseOut 写入完毕后是否关闭输出流
     * @param content    写入内容
     * @throws IORuntimeException IO 异常
     */
    public static void write(final OutputStream out,
                             final boolean isCloseOut,
                             final byte[] content) throws IORuntimeException {
        try {
            out.write(content);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (isCloseOut) {
                close(out);
            }
        }
    }

    /**
     * 从输入流读取指定长度字节（不关闭流）.
     *
     * @param in     输入流
     * @param length 最大读取长度
     * @return 字节数组
     */
    public static byte[] readBytes(final InputStream in, final int length) throws IORuntimeException {
        return readBytes(in, length, false);
    }

    /**
     * 从输入流读取指定长度字节.
     *
     * @param in      输入流
     * @param length  最大读取长度
     * @param isClose 读取后是否关闭流
     * @return 字节数组
     */
    public static byte[] readBytes(final InputStream in, final int length, final boolean isClose)
            throws IORuntimeException {
        if (in == null) {
            return null;
        }
        if (length <= 0) {
            return new byte[0];
        }
        try {
            final byte[] buffer = new byte[length];
            int read = 0;
            while (read < length) {
                final int n = in.read(buffer, read, length - read);
                if (n < 0) {
                    break;
                }
                read += n;
            }
            if (read == length) {
                return buffer;
            }
            final byte[] actual = new byte[read];
            System.arraycopy(buffer, 0, actual, 0, read);
            return actual;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (isClose) {
                close(in);
            }
        }
    }

    /**
     * 读取流头部并转为十六进制字符串.
     *
     * @param in          输入流
     * @param length      读取长度
     * @param toLowerCase true 小写，false 大写
     * @return 十六进制字符串
     */
    public static String readHex(final InputStream in, final int length, final boolean toLowerCase)
            throws IORuntimeException {
        return HexUtil.encodeHexStr(readBytes(in, length, false), toLowerCase);
    }

    /**
     * 读取流前 64 字节的大写十六进制.
     *
     * @param in 输入流
     * @return 十六进制字符串
     */
    public static String readHex64Upper(final InputStream in) throws IORuntimeException {
        return readHex(in, 64, false);
    }

    /**
     * 读取流前 8192 字节的大写十六进制.
     *
     * @param in 输入流
     * @return 十六进制字符串
     */
    public static String readHex8192Upper(final InputStream in) throws IORuntimeException {
        try {
            final int available = in.available();
            return readHex(in, Math.min(8192, Math.max(available, 64)), false);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 打开文件输入流.
     *
     * @param file 文件
     * @return 输入流
     */
    public static FileInputStream toStream(final File file) throws IORuntimeException {
        if (file == null) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取 {@link BufferedReader}.
     *
     * @param in      输入流
     * @param charset 字符集
     * @return {@link BufferedReader}
     */
    public static BufferedReader getReader(final InputStream in, final Charset charset) {
        if (in == null) {
            return null;
        }
        final Charset cs = charset == null ? Charset.defaultCharset() : charset;
        final Reader reader = new InputStreamReader(in, cs);
        return new BufferedReader(reader);
    }

    /**
     * 静默关闭 {@link Closeable}.
     *
     * @param closeable 可关闭对象
     */
    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                // ignore
            }
        }
    }

    // --------------------------------------- Static method end
}
