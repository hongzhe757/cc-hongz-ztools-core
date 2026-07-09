package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link IoUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
class IoUtilTest {

    /**
     * 验证 {@link IoUtil#toStream(byte[])} 对 null 返回 null.
     */
    @Test
    void toStreamNullReturnsNull() {
        assertNull(IoUtil.toStream((byte[]) null));
    }

    /**
     * 验证 {@link IoUtil#toStream(byte[])} 可读回原始字节.
     */
    @Test
    void toStreamBytes() throws Exception {
        final byte[] data = {1, 2, 3};
        assertArrayEquals(data, IoUtil.toStream(data).readAllBytes());
    }

    /**
     * 验证 {@link IoUtil#write(OutputStream, boolean, byte[])} 不关闭流时写入内容.
     */
    @Test
    void writeWithoutClose() throws Exception {
        final byte[] data = "abc".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IoUtil.write(out, false, data);
        assertArrayEquals(data, out.toByteArray());
    }

    /**
     * 验证 {@link IoUtil#write(OutputStream, boolean, byte[])} 关闭流选项.
     */
    @Test
    void writeWithClose() throws Exception {
        final byte[] data = "x".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        final CloseTrackingOutputStream out = new CloseTrackingOutputStream();
        IoUtil.write(out, true, data);
        assertArrayEquals(data, out.toByteArray());
        assertTrue(out.isClosed());
    }

    /**
     * 验证 {@link IoUtil#toBuffered(OutputStream)} 包装普通输出流.
     */
    @Test
    void toBufferedWrapsStream() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(IoUtil.toBuffered(out));
    }

    /**
     * 验证 {@link IoUtil#toBuffered(OutputStream)} 对已缓冲流直接返回原实例.
     */
    @Test
    void toBufferedReturnsSameWhenAlreadyBuffered() {
        final ByteArrayOutputStream base = new ByteArrayOutputStream();
        final java.io.BufferedOutputStream buffered = IoUtil.toBuffered(base);
        assertSame(buffered, IoUtil.toBuffered(buffered));
    }

    /**
     * 验证写入失败时抛出 {@link IORuntimeException}.
     */
    @Test
    void writeThrowsIoRuntimeException() {
        final OutputStream broken = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                throw new IOException("broken");
            }
        };
        assertThrows(IORuntimeException.class,
                () -> IoUtil.write(broken, false, new byte[]{1}));
    }

    /**
     * 用于验证 close 是否被调用的输出流.
     */
    private static final class CloseTrackingOutputStream extends ByteArrayOutputStream {
        private boolean closed;

        @Override
        public void close() {
            closed = true;
        }

        boolean isClosed() {
            return closed;
        }
    }
}
