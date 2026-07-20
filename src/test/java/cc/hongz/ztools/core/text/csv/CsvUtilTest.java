package cc.hongz.ztools.core.text.csv;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CsvUtil} / {@link CsvReader} / {@link CsvWriter} 单元测试.
 */
class CsvUtilTest {

	@Test
	void readCsvStrTest() {
		CsvData data = CsvUtil.getReader().readFromStr(
				"# 这是一行注释，读取时应忽略\n" +
				"\"sss,sss\",姓名,\"性别\",关注\"对象\",年龄,\"\",\"\"\"\n");
		List<CsvRow> rows = data.getRows();
		final CsvRow row0 = rows.get(0);
		assertEquals("sss,sss", row0.get(0));
		assertEquals("姓名", row0.get(1));
		assertEquals("性别", row0.get(2));
		assertEquals("关注\"对象\"", row0.get(3));
		assertEquals("年龄", row0.get(4));
		assertEquals("", row0.get(5));
		assertEquals("\"\n", row0.get(6));
	}

	@Test
	void readCsvStrWithHandlerTest() {
		CsvUtil.getReader().readFromStr(
				"# 这是一行注释，读取时应忽略\n" +
				"\"sss,sss\",姓名,\"性别\",关注\"对象\",年龄,\"\",\"\"\"\n",
				csvRow -> {
					assertEquals("sss,sss", csvRow.get(0));
					assertEquals("姓名", csvRow.get(1));
					assertEquals("性别", csvRow.get(2));
				});
	}

	@Test
	void readWithHeaderTest() {
		String csv = "name,age,gender\n小明,19,男\n小红,20,女\n";
		CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().setContainsHeader(true));
		CsvData data = reader.readFromStr(csv);
		List<CsvRow> rows = data.getRows();
		assertEquals(2, rows.size());
		assertEquals("小明", rows.get(0).getByName("name"));
		assertEquals("20", rows.get(1).getByName("age"));
	}

	@Test
	void readMapListTest() {
		String csv = "name,age\n小明,19\n";
		CsvReader reader = CsvUtil.getReader();
		java.util.List<java.util.Map<String, String>> list = reader.readMapList(new StringReader(csv));
		assertEquals(1, list.size());
		assertEquals("小明", list.get(0).get("name"));
		assertEquals("19", list.get(0).get("age"));
	}

	@Test
	void writeTest() {
		StringWriter sw = new StringWriter();
		CsvWriter writer = new CsvWriter(sw);
		writer.write(
				new String[]{"a1", "b1", "c1"},
				new String[]{"a2", "b2", "c2"}
		);
		String result = sw.toString();
		assertTrue(result.contains("a1"));
		assertTrue(result.contains("b2"));
	}

	@Test
	void writeCsvDataTest() {
		String csv = "name,age\n小明,19\n";
		CsvData data = CsvUtil.getReader().readFromStr(csv);

		StringWriter sw = new StringWriter();
		CsvWriter writer = new CsvWriter(sw);
		writer.write(data);
		String result = sw.toString();
		assertTrue(result.contains("name"));
		assertTrue(result.contains("小明"));
	}

	@Test
	void csvRowGetFieldMapTest() {
		String csv = "name,age\n小明,19\n";
		CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().setContainsHeader(true));
		CsvData data = reader.readFromStr(csv);
		CsvRow row = data.getRows().get(0);
		java.util.Map<String, String> fieldMap = row.getFieldMap();
		assertEquals("小明", fieldMap.get("name"));
		assertEquals("19", fieldMap.get("age"));
	}

	@Test
	void csvRowToStringTest() {
		String csv = "name,age\n小明,19\n";
		CsvReader reader = CsvUtil.getReader(CsvReadConfig.defaultConfig().setContainsHeader(true));
		CsvData data = reader.readFromStr(csv);
		CsvRow row = data.getRows().get(0);
		String str = row.toString();
		assertTrue(str.contains("CsvRow"));
		assertTrue(str.contains("originalLineNumber"));
	}
}
