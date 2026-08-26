package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.LineHandler;
import cc.hongz.ztools.core.io.watch.SimpleWatcher;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 行处理的Watcher实现。
 *
 * @author looly
 * @since 4.5.2
 */
public class LineReadWatcher extends SimpleWatcher implements Runnable {

    private final RandomAccessFile randomAccessFile;
    private final Charset charset;
    private final LineHandler lineHandler;

    /**
     * 构造。
     *
     * @param randomAccessFile {@link RandomAccessFile}
     * @param charset 编码
     * @param lineHandler 行处理器{@link LineHandler}实现
 */
    public LineReadWatcher(final RandomAccessFile randomAccessFile, final Charset charset,
            final LineHandler lineHandler) {
        this.randomAccessFile = randomAccessFile;
        this.charset = charset;
        this.lineHandler = lineHandler;
    }

    @Override
    public void run() {
        onModify(null, null);
    }

    @Override
    public void onModify(final WatchEvent<?> event, final Path currentPath) {
        final RandomAccessFile raf = this.randomAccessFile;
        final Charset resolvedCharset = this.charset;
        final LineHandler resolvedLineHandler = this.lineHandler;

        try {
            final long currentLength = raf.length();
            final long position = raf.getFilePointer();
            if (position == currentLength) {
                // 内容长度不变时忽略此次事件
                return;
            } else if (currentLength < position) {
                // 如果内容变短或变0，说明文件做了删改或清空，回到内容末尾或0
                raf.seek(currentLength);
                return;
            }

            // 读取行
            FileUtil.readLines(raf, resolvedCharset, resolvedLineHandler);

            // 记录当前读到的位置
            raf.seek(currentLength);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
