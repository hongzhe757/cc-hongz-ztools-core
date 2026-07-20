package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CsvParser} 单元测试.
 */
class CsvParserTest {

	@Test
	void parseTest1() {
		StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals("b\"bba\"", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	void parseTest2() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals("\"bba\"bbb", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	void parseTest3() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals("bba", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	void parseTest4() {
		StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals("", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	void parseEscapeTest() {
		StringReader reader = StrUtil.getReader("\"b\"\"bb\"");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals(1, row.size());
		assertEquals("b\"bb", row.get(0));
	}
}
