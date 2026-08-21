package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FastByteBuffer} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("FastByteBuffer 快速缓冲")
class FastByteBufferTest {

    @Test
    @DisplayName("append 与 toArray")
    void appendAndToArray() {
        final FastByteBuffer buffer = new FastByteBuffer();
        buffer.append(new byte[]{1, 2, 3});
        buffer.append((byte) 4);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, buffer.toArray());
        assertEquals(4, buffer.size());
    }

    @Test
    @DisplayName("isEmpty 判空")
    void isEmptyTest() {
        final FastByteBuffer buffer = new FastByteBuffer();
        assertTrue(buffer.isEmpty());
        buffer.append((byte) 1);
        assertFalse(buffer.isEmpty());
    }

    @Test
    @DisplayName("get 按索引取字节")
    void getTest() {
        final FastByteBuffer buffer = new FastByteBuffer();
        buffer.append(new byte[]{10, 20, 30});
        assertEquals(10, buffer.get(0));
        assertEquals(20, buffer.get(1));
        assertEquals(30, buffer.get(2));
    }

    @Test
    @DisplayName("reset 重置")
    void resetTest() {
        final FastByteBuffer buffer = new FastByteBuffer();
        buffer.append(new byte[]{1, 2, 3});
        buffer.reset();
        assertEquals(0, buffer.size());
        assertTrue(buffer.isEmpty());
    }
}
