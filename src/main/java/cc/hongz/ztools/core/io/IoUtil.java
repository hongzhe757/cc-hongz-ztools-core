package cc.hongz.ztools.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.CharsetUtil;
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
     * 默认缓存大小 8192.
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    /**
     * 默认中等缓存大小 16384.
     */
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 2 << 13;

    /**
     * 默认大缓冲区大小（32KB）.
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 2 << 14;

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
     * 转换为 {@link BufferedInputStream}.
     *
     * @param in 原始输入流
     * @return 缓冲输入流
     */
    public static BufferedInputStream toBuffered(final InputStream in) {
        return in instanceof BufferedInputStream
                ? (BufferedInputStream) in
                : new BufferedInputStream(in);
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
     * 获取 {@link BufferedReader}，使用UTF-8编码.
     *
     * @param in 输入流
     * @return {@link BufferedReader}
     */
    public static BufferedReader getReader(final InputStream in) {
        return getReader(in, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 从{@link BOMInputStream}中获取Reader.
     *
     * @param in {@link BOMInputStream}
     * @return {@link BufferedReader}
     */
    public static BufferedReader getReader(final BOMInputStream in) {
        return getReader((InputStream) in, CharsetUtil.charset(in.getCharset()));
    }

    /**
     * 获取 {@link BufferedReader}.<br>
     * 如果是 {@link BufferedReader} 强转返回，否则新建。如果提供的Reader为null返回null
     *
     * @param reader 普通Reader，如果为null返回null
     * @return {@link BufferedReader} or null
     */
    public static BufferedReader getReader(final Reader reader) {
        if (null == reader) {
            return null;
        }
        return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
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

    /**
     * 从输入流读取全部内容为字符串.
     *
     * @param in      输入流
     * @param charset 字符集
     * @return 字符串内容
     * @throws IORuntimeException IO 异常
     */
    public static String read(final InputStream in, final Charset charset) throws IORuntimeException {
        if (in == null) {
            return null;
        }
        final Charset cs = charset == null ? Charset.defaultCharset() : charset;
        try {
            final byte[] bytes = readBytes(in, true);
            return new String(bytes, cs);
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 从输入流按行读取内容到集合中.
     *
     * @param in         输入流
     * @param charset    字符集
     * @param collection 结果集合
     * @param <T>        集合类型
     * @return 结果集合
     * @throws IORuntimeException IO 异常
     */
    public static <T extends Collection<String>> T readLines(final InputStream in, final Charset charset, final T collection) throws IORuntimeException {
        final BufferedReader reader = getReader(in, charset);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                collection.add(line);
            }
            return collection;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(reader);
        }
    }

    /**
     * 将多部分内容写到流中.
     *
     * @param out        输出流
     * @param isCloseOut 写入完毕是否关闭输出流
     * @param contents   写入的可序列化对象
     * @throws IORuntimeException IO异常
     */
    public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
        ObjectOutputStream osw = null;
        try {
            if (ArrayUtil.isEmpty(contents)) {
                return;
            }
            osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
            for (Object content : contents) {
                if (content != null) {
                    osw.writeObject(content);
                }
            }
            osw.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (isCloseOut) {
                close(osw);
            }
        }
    }

    /**
     * 从流中读取对象，即对象的反序列化.
     *
     * @param <T> 读取对象的类型
     * @param in  输入流
     * @return 反序列化后的对象
     * @throws IORuntimeException IO异常
     * @throws UtilException      ClassNotFoundException包装
     */
    public static <T> T readObj(InputStream in) throws IORuntimeException, UtilException {
        return readObj(in, null);
    }

    /**
     * 从流中读取对象，即对象的反序列化，读取后不关闭流.
     *
     * <p>
     * 此方法使用了{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞
     * </p>
     *
     * @param <T>   读取对象的类型
     * @param in    输入流
     * @param clazz 读取对象类型
     * @return 反序列化后的对象
     * @throws IORuntimeException IO异常
     * @throws UtilException      ClassNotFoundException包装
     */
    public static <T> T readObj(InputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
        try {
            ValidateObjectInputStream validateIn;
            if (in instanceof ValidateObjectInputStream) {
                validateIn = (ValidateObjectInputStream) in;
            } else {
                validateIn = new ValidateObjectInputStream(in);
            }
            // 将类及其继承链全部加入白名单
            if (clazz != null) {
                addClassHierarchy(validateIn, clazz);
            }
            return readObj(validateIn, null);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将类及其所有父类、接口加入白名单.
     *
     * @param stream ValidateObjectInputStream
     * @param clazz  要加入的类
     */
    private static void addClassHierarchy(ValidateObjectInputStream stream, Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return;
        }
        stream.accept(clazz);
        addClassHierarchy(stream, clazz.getSuperclass());
        for (Class<?> iface : clazz.getInterfaces()) {
            addClassHierarchy(stream, iface);
        }
    }

    /**
     * 从流中读取对象，即对象的反序列化.
     *
     * @param <T>   读取对象的类型
     * @param in    输入流，使用{@link ValidateObjectInputStream}中的黑白名单方式过滤类
     * @param clazz 读取对象类型
     * @return 反序列化后的对象
     * @throws IORuntimeException IO异常
     * @throws UtilException      ClassNotFoundException包装
     */
    public static <T> T readObj(ValidateObjectInputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
        if (in == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        if (null != clazz) {
            addClassHierarchy(in, clazz);
        }
        try {
            //noinspection unchecked
            return (T) in.readObject();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 将字符串转为输入流.
     *
     * @param str     字符串
     * @param charset 字符集
     * @return 输入流
     */
    public static InputStream toStream(final String str, final Charset charset) {
        if (str == null) {
            return null;
        }
        final Charset cs = charset == null ? Charset.defaultCharset() : charset;
        return new ByteArrayInputStream(str.getBytes(cs));
    }

    /**
     * 拷贝流.
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    public static long copy(final InputStream in, final OutputStream out) throws IORuntimeException {
        if (in == null || out == null) {
            return 0;
        }
        try {
            final byte[] buffer = new byte[DEFAULT_LARGE_BUFFER_SIZE];
            long count = 0;
            int n;
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 刷缓冲.
     *
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    public static void flush(final OutputStream out) throws IORuntimeException {
        if (out != null) {
            try {
                out.flush();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }

    /**
     * 刷缓冲.
     *
     * @param flushable 可刷缓冲的对象
     * @throws IORuntimeException IO异常
     */
    public static void flush(final Flushable flushable) throws IORuntimeException {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }

    /**
     * 从Reader读取全部内容.
     *
     * @param reader Reader
     * @return 字符串
     * @throws IORuntimeException IO异常
     */
    public static String read(final BufferedReader reader) throws IORuntimeException {
        if (reader == null) {
            return null;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            char[] buf = new char[DEFAULT_LARGE_BUFFER_SIZE];
            int n;
            while (-1 != (n = reader.read(buf))) {
                sb.append(buf, 0, n);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(reader);
        }
    }

    /**
     * 按行读取，每行处理.
     *
     * @param reader      {@link BufferedReader}
     * @param lineHandler 行处理器
     * @throws IORuntimeException IO异常
     */
    public static void readLines(final BufferedReader reader, final LineHandler lineHandler) throws IORuntimeException {
        if (reader == null || lineHandler == null) {
            return;
        }
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lineHandler.handle(line);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * EOF标记.
     */
    private static final int EOF = -1;

    // --------------------------------------- Static method start

    /**
     * 对比两个流内容是否相同.
     *
     * @param input1 输入流1
     * @param input2 输入流2
     * @return 内容是否相同
     * @throws IORuntimeException IO异常
     */
    public static boolean contentEquals(final InputStream input1, final InputStream input2) throws IORuntimeException {
        final BufferedInputStream bis1 = toBuffered(input1);
        final BufferedInputStream bis2 = toBuffered(input2);
        try {
            int ch = bis1.read();
            while (EOF != ch) {
                final int ch2 = bis2.read();
                if (ch != ch2) {
                    return false;
                }
                ch = bis1.read();
            }
            return bis2.read() == EOF;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 对比两个Reader内容是否相同，忽略EOL.
     *
     * @param input1 Reader1
     * @param input2 Reader2
     * @return 内容是否相同
     * @throws IORuntimeException IO异常
     */
    public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2) throws IORuntimeException {
        final BufferedReader br1 = getReader(input1);
        final BufferedReader br2 = getReader(input2);
        try {
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            while (line1 != null && line1.equals(line2)) {
                line1 = br1.readLine();
                line2 = br2.readLine();
            }
            return Objects.equals(line1, line2);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 计算流的校验码.
     *
     * @param in       流
     * @param checksum {@link Checksum}
     * @return Checksum
     * @throws IORuntimeException IO异常
     */
    public static Checksum checksum(final InputStream in, final Checksum checksum) throws IORuntimeException {
        Assert.notNull(in, "InputStream is null !");
        Checksum ck = checksum;
        if (null == ck) {
            ck = new CRC32();
        }
        try {
            final CheckedInputStream cis = new CheckedInputStream(in, ck);
            copy(cis, new NullOutputStream());
        } finally {
            close(in);
        }
        return ck;
    }

    // --------------------------------------- Static method end
}
