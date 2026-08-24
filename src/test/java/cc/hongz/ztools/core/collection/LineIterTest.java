package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LineIter 单元测试.
 */
@DisplayName("LineIter 行迭代器")
class LineIterTest {

    @Test
    @DisplayName("基本按行读取")
    void basicReadLines() {
        StringReader reader = new StringReader("line1\nline2\nline3");
        LineIter iter = new LineIter(reader);

        List<String> lines = new ArrayList<>();
        while (iter.hasNext()) {
            lines.add(iter.next());
        }
        iter.close();

        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
        assertEquals("line3", lines.get(2));
    }

    @Test
    @DisplayName("空输入")
    void emptyInput() {
        StringReader reader = new StringReader("");
        LineIter iter = new LineIter(reader);
        assertFalse(iter.hasNext());
        iter.close();
    }

    @Test
    @DisplayName("单行输入")
    void singleLine() {
        StringReader reader = new StringReader("hello");
        LineIter iter = new LineIter(reader);
        assertTrue(iter.hasNext());
        assertEquals("hello", iter.next());
        assertFalse(iter.hasNext());
        iter.close();
    }

    @Test
    @DisplayName("null reader 抛异常")
    void nullReaderThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LineIter((java.io.Reader) null));
    }

    @Test
    @DisplayName("从 InputStream 读取")
    void fromInputStream() {
        byte[] data = "a\nb\nc".getBytes(StandardCharsets.UTF_8);
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
        LineIter iter = new LineIter(bais, StandardCharsets.UTF_8);

        List<String> lines = new ArrayList<>();
        while (iter.hasNext()) {
            lines.add(iter.next());
        }
        iter.close();

        assertEquals(3, lines.size());
    }

    @Test
    @DisplayName("作为 Iterable 遍历")
    void asIterable() {
        StringReader reader = new StringReader("x\ny");
        LineIter iter = new LineIter(reader);
        List<String> lines = new ArrayList<>();
        for (String line : iter) {
            lines.add(line);
        }
        iter.close();
        assertEquals(2, lines.size());
    }
}
