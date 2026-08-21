package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link FastStringWriter} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("FastStringWriter 快速字符串写出")
class FastStringWriterTest {

    @Test
    @DisplayName("write 与 toString")
    void writeAndToString() {
        final FastStringWriter writer = new FastStringWriter();
        writer.write('h');
        writer.write("ello");
        assertEquals("hello", writer.toString());
    }

    @Test
    @DisplayName("write 部分字符数组")
    void writePartialCharArray() {
        final FastStringWriter writer = new FastStringWriter();
        writer.write(new char[]{'a', 'b', 'c', 'd'}, 1, 2);
        assertEquals("bc", writer.toString());
    }
}
