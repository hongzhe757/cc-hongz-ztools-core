package cc.hongz.ztools.core.io.copy;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.StreamProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link StreamCopier}、{@link ReaderWriterCopier} 单元测试.
 */
@DisplayName("StreamCopier 与 ReaderWriterCopier 单元测试")
class CopierTest {

    @DisplayName("StreamCopier 拷贝字节")
    @Test
    void streamCopierCopiesBytes() {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        long copied = new StreamCopier().copy(in, out);
        assertEquals(data.length, copied);
        assertArrayEquals(data, out.toByteArray());
    }

    @DisplayName("StreamCopier 指定缓冲区大小")
    @Test
    void streamCopierWithBufferSize() {
        byte[] data = "test data for buffer".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        long copied = new StreamCopier(4).copy(in, out);
        assertEquals(data.length, copied);
        assertArrayEquals(data, out.toByteArray());
    }

    @DisplayName("StreamCopier 指定拷贝数量")
    @Test
    void streamCopierWithCount() {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        long copied = new StreamCopier(IoUtil_DEFAULT(), 5).copy(in, out);
        assertEquals(5, copied);
    }

    @DisplayName("StreamCopier 带进度回调")
    @Test
    void streamCopierWithProgress() {
        byte[] data = "progress test".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final boolean[] started = {false};
        final boolean[] finished = {false};

        StreamProgress progress = new StreamProgress() {
            @Override
            public void start() { started[0] = true; }
            @Override
            public void progress(long total, long progressCount) {}
            @Override
            public void finish() { finished[0] = true; }
        };

        new StreamCopier(-1, -1, progress).copy(in, out);
        assertEquals(true, started[0]);
        assertEquals(true, finished[0]);
    }

    @DisplayName("ReaderWriterCopier 拷贝字符")
    @Test
    void readerWriterCopierCopiesChars() {
        String text = "你好世界";
        StringReader reader = new StringReader(text);
        StringWriter writer = new StringWriter();

        long copied = new ReaderWriterCopier().copy(reader, writer);
        assertEquals(text.length(), copied);
        assertEquals(text, writer.toString());
    }

    @DisplayName("ReaderWriterCopier 指定缓冲区大小")
    @Test
    void readerWriterCopierWithBufferSize() {
        String text = "buffer size test content";
        StringReader reader = new StringReader(text);
        StringWriter writer = new StringWriter();

        long copied = new ReaderWriterCopier(4).copy(reader, writer);
        assertEquals(text.length(), copied);
        assertEquals(text, writer.toString());
    }

    @DisplayName("StreamCopier null 源抛异常")
    @Test
    void streamCopierNullSourceThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new StreamCopier().copy(null, new ByteArrayOutputStream()));
    }

    @DisplayName("ReaderWriterCopier null 源抛异常")
    @Test
    void readerWriterCopierNullSourceThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ReaderWriterCopier().copy(null, new StringWriter()));
    }

    /** 返回默认 buffer size */
    private static int IoUtil_DEFAULT() {
        return cc.hongz.ztools.core.io.IoUtil.DEFAULT_BUFFER_SIZE;
    }
}
