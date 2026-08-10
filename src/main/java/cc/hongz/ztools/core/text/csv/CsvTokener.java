package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * CSV解析器，用于解析CSV文件.
 *
 * @author looly
 * @since 5.8.0
 */
public class CsvTokener implements Closeable {

    /**
     * 读取器.
     */
    private final BufferedReader raw;
    /**
     * 当前索引.
     */
    private long index;
    /**
     * 前一个字符.
     */
    private int prev;
    /**
     * 是否使用前一个字符.
     */
    private boolean usePrev;

    /**
     * 构造.
     *
     * @param reader 读取器
     */
    public CsvTokener(final Reader reader) {
        this.raw = (reader instanceof BufferedReader)
                ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * 读取下一个字符.
     *
     * @return 下一个字符
     */
    public int next() {
        if (this.usePrev) {
            this.usePrev = false;
        } else {
            try {
                this.prev = this.raw.read();
            } catch (final IOException e) {
                throw new IORuntimeException(e);
            }
        }
        this.index++;
        return this.prev;
    }

    /**
     * 回退一个字符.
     *
     * @throws IllegalStateException 如果连续回退两次
     */
    public void back() throws IllegalStateException {
        if (this.usePrev || this.index <= 0) {
            throw new IllegalStateException("Stepping back two steps is not supported");
        }
        this.index--;
        this.usePrev = true;
    }

    /**
     * 获取当前索引.
     *
     * @return 当前索引
     */
    public long getIndex() {
        return this.index;
    }

    @Override
    public void close() throws IOException {
        IoUtil.close(this.raw);
    }
}
