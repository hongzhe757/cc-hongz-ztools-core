package cc.hongz.ztools.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 基于快速缓冲 {@link FastByteBuffer} 的 OutputStream，随着数据的增长自动扩充缓冲区.
 *
 * <p>
 * 可以通过 {@link #toByteArray()} 和 {@link #toString()} 来获取数据.
 * {@link #close()} 方法无任何效果，当流被关闭后不会抛出 IOException.
 *
 * <p>
 * 设计模式：适配器（Adapter），将 {@link FastByteBuffer} 适配为 {@link OutputStream} 接口.
 *
 * @author biezhi
 */
public class FastByteArrayOutputStream extends OutputStream {

    /**
     * 内部快速缓冲.
     */
    private final FastByteBuffer buffer;

    /**
     * 默认构造，预估大小 1024.
     */
    public FastByteArrayOutputStream() {
        this(1024);
    }

    /**
     * 构造.
     *
     * @param size 预估大小
     */
    public FastByteArrayOutputStream(final int size) {
        buffer = new FastByteBuffer(size);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) {
        buffer.append(b, off, len);
    }

    @Override
    public void write(final int b) {
        buffer.append((byte) b);
    }

    /**
     * 获取缓冲字节总数.
     *
     * @return 字节数
     */
    public int size() {
        return buffer.size();
    }

    /**
     * 此方法无任何效果，当流被关闭后不会抛出 IOException.
     */
    @Override
    public void close() {
        // nop
    }

    /**
     * 重置缓冲.
     */
    public void reset() {
        buffer.reset();
    }

    /**
     * 写出到指定输出流.
     *
     * @param out 输出流
     * @throws IORuntimeException IO 异常
     */
    public void writeTo(final OutputStream out) throws IORuntimeException {
        final int index = buffer.index();
        if (index < 0) {
            return;
        }
        try {
            for (int i = 0; i < index; i++) {
                out.write(buffer.array(i));
            }
            out.write(buffer.array(index), 0, buffer.offset());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 转为字节数组.
     *
     * @return 字节数组
     */
    public byte[] toByteArray() {
        return buffer.toArray();
    }

    @Override
    public String toString() {
        return new String(toByteArray(), Charset.defaultCharset());
    }

    /**
     * 转为字符串.
     *
     * @param charset 字符集，null 时使用默认字符集
     * @return 字符串
     */
    public String toString(final Charset charset) {
        final Charset cs = charset == null ? Charset.defaultCharset() : charset;
        return new String(toByteArray(), cs);
    }
}
