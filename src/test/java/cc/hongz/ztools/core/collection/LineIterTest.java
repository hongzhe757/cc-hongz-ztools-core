package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LineIter 单元测试.
 */
class LineIterTest {

	@Test
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
	void emptyInput() {
		StringReader reader = new StringReader("");
		LineIter iter = new LineIter(reader);
		assertFalse(iter.hasNext());
		iter.close();
	}

	@Test
	void singleLine() {
		StringReader reader = new StringReader("hello");
		LineIter iter = new LineIter(reader);
		assertTrue(iter.hasNext());
		assertEquals("hello", iter.next());
		assertFalse(iter.hasNext());
		iter.close();
	}

	@Test
	void nullReaderThrows() {
		assertThrows(IllegalArgumentException.class, () -> new LineIter((java.io.Reader) null));
	}

	@Test
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
