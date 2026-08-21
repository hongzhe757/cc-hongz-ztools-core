package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AppendableWriter} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("AppendableWriter 聚合写出器")
class AppendableWriterTest {

    @Test
    @DisplayName("append 到 StringBuilder")
    void appendTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        final AppendableWriter writer = new AppendableWriter(sb);
        writer.append('h');
        writer.append("ello");
        assertEquals("hello", sb.toString());
    }

    @Test
    @DisplayName("write 字符串")
    void writeTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        final AppendableWriter writer = new AppendableWriter(sb);
        writer.write("world");
        assertEquals("world", sb.toString());
    }
}
