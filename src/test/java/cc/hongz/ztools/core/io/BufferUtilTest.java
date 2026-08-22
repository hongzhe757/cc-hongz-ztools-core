package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BufferUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("BufferUtil ByteBuffer 工具类")
class BufferUtilTest {

    @Test
    @DisplayName("copy 拷贝 ByteBuffer")
    void copyTest() {
        final ByteBuffer src = ByteBuffer.wrap(new byte[]{1, 2, 3, 4});
        final ByteBuffer dest = BufferUtil.copy(src, 1, 3);
        assertArrayEquals(new byte[]{2, 3}, dest.array());
    }

    @Test
    @DisplayName("create 与 readStr")
    void createAndReadStr() {
        final ByteBuffer buffer = BufferUtil.create("hello", StandardCharsets.UTF_8);
        assertEquals("hello", BufferUtil.readStr(buffer, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("readBytes 读取剩余字节")
    void readBytesTest() {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, BufferUtil.readBytes(buffer));
    }
}
