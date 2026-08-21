package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link FastByteArrayOutputStream} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("FastByteArrayOutputStream 快速字节数组输出流")
class FastByteArrayOutputStreamTest {

    @Test
    @DisplayName("write 与 toByteArray")
    void writeAndToByteArray() {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write(new byte[]{1, 2, 3}, 0, 3);
        out.write(4);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, out.toByteArray());
        assertEquals(4, out.size());
    }

    @Test
    @DisplayName("toString 转字符串")
    void toStringTest() {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("hello".getBytes(StandardCharsets.UTF_8), 0, 5);
        assertEquals("hello", out.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("reset 重置")
    void resetTest() {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write(new byte[]{1, 2}, 0, 2);
        out.reset();
        assertEquals(0, out.size());
    }
}
