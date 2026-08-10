package cc.hongz.ztools.core.text.csv;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * CSV数据，包括头部信息和行数据.
 *
 * @author looly
 * @since 5.7.14
 */
public class CsvData implements Iterable<CsvRow>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表头列表.
     */
    private final List<String> header;
    /**
     * 行数据列表.
     */
    private final List<CsvRow> rows;

    /**
     * 构造.
     *
     * @param header 表头列表
     * @param rows   行数据列表
     */
    public CsvData(final List<String> header, final List<CsvRow> rows) {
        this.header = header;
        this.rows = rows;
    }

    /**
     * 获取行数.
     *
     * @return 行数
     */
    public int getRowCount() {
        return this.rows.size();
    }

    /**
     * 获取表头列表.
     *
     * @return 表头列表
     */
    public List<String> getHeader() {
        if (null == this.header) {
            return null;
        }
        return Collections.unmodifiableList(this.header);
    }

    /**
     * 获取指定行.
     *
     * @param index 行索引
     * @return 行数据
     */
    public CsvRow getRow(final int index) {
        return this.rows.get(index);
    }

    /**
     * 获取所有行.
     *
     * @return 行列表
     */
    public List<CsvRow> getRows() {
        return this.rows;
    }

    @Override
    public Iterator<CsvRow> iterator() {
        return this.rows.iterator();
    }

    @Override
    public String toString() {
        return "CsvData{" + "header=" + header + ", rows=" + rows + '}';
    }
}
