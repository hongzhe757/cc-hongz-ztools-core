package cc.hongz.ztools.core.text.csv;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * CSV数据，包括头部信息和行数据
 *
 * @author Looly
 */
public class CsvData implements Iterable<CsvRow>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<String> header;
	private final List<CsvRow> rows;

	public CsvData(final List<String> header, final List<CsvRow> rows) {
		this.header = header;
		this.rows = rows;
	}

	public int getRowCount() { return this.rows.size(); }

	public List<String> getHeader() {
		if(null == this.header){ return null; }
		return Collections.unmodifiableList(this.header);
	}

	public CsvRow getRow(final int index) { return this.rows.get(index); }
	public List<CsvRow> getRows() { return this.rows; }

	@Override
	public Iterator<CsvRow> iterator() { return this.rows.iterator(); }

	@Override
	public String toString() {
		return "CsvData{" + "header=" + header + ", rows=" + rows + '}';
	}
}
