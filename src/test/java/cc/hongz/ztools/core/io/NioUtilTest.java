package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link NioUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("NioUtil NIO 工具类")
class NioUtilTest {

    @Test
    @DisplayName("copy 拷贝通道")
    void copyTest() {
        final ReadableByteChannel in = Channels.newChannel(
                new ByteArrayInputStream("hello".getBytes(StandardCharsets.UTF_8)));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableByteChannel out = Channels.newChannel(baos);
        final long copied = NioUtil.copy(in, out);
        assertEquals(5, copied);
        assertEquals("hello", baos.toString(StandardCharsets.UTF_8));
    }
}
