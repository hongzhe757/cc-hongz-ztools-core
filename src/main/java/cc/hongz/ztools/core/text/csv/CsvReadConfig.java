package cc.hongz.ztools.core.text.csv;

import java.io.Serializable;

/**
 * CSV读取配置项.
 *
 * @author looly
 * @since 5.7.14
 */
public class CsvReadConfig extends CsvConfig<CsvReadConfig> implements Serializable {
    private static final long serialVersionUID = 5396453565371560052L;

    /**
     * 表头行号，-1表示无表头.
     */
    protected long headerLineNo = -1;
    /**
     * 是否跳过空行.
     */
    protected boolean skipEmptyRows = true;
    /**
     * 字段数不一致时是否报错.
     */
    protected boolean errorOnDifferentFieldCount;
    /**
     * 起始行号.
     */
    protected long beginLineNo;
    /**
     * 结束行号.
     */
    protected long endLineNo = Long.MAX_VALUE - 1;
    /**
     * 是否去除字段两边空格.
     */
    protected boolean trimField;

    /**
     * 获取默认配置.
     *
     * @return 默认配置
     */
    public static CsvReadConfig defaultConfig() {
        return new CsvReadConfig();
    }

    /**
     * 设置是否包含表头.
     *
     * @param containsHeader 是否包含表头
     * @return this
     */
    public CsvReadConfig setContainsHeader(final boolean containsHeader) {
        return setHeaderLineNo(containsHeader ? beginLineNo : -1);
    }

    /**
     * 设置表头行号.
     *
     * @param headerLineNo 表头行号
     * @return this
     */
    public CsvReadConfig setHeaderLineNo(final long newHeaderLineNo) {
        this.headerLineNo = newHeaderLineNo;
        return this;
    }

    /**
     * 设置是否跳过空行.
     *
     * @param skipEmptyRows 是否跳过空行
     * @return this
     */
    public CsvReadConfig setSkipEmptyRows(final boolean newSkipEmptyRows) {
        this.skipEmptyRows = newSkipEmptyRows;
        return this;
    }

    /**
     * 设置字段数不一致时是否报错.
     *
     * @param errorOnDifferentFieldCount 是否报错
     * @return this
     */
    public CsvReadConfig setErrorOnDifferentFieldCount(
            final boolean newErrorOnDifferentFieldCount) {
        this.errorOnDifferentFieldCount = newErrorOnDifferentFieldCount;
        return this;
    }

    /**
     * 设置起始行号.
     *
     * @param beginLineNo 起始行号
     * @return this
     */
    public CsvReadConfig setBeginLineNo(final long newBeginLineNo) {
        this.beginLineNo = newBeginLineNo;
        return this;
    }

    /**
     * 设置结束行号.
     *
     * @param endLineNo 结束行号
     * @return this
     */
    public CsvReadConfig setEndLineNo(final long newEndLineNo) {
        this.endLineNo = newEndLineNo;
        return this;
    }

    /**
     * 设置是否去除字段两边空格.
     *
     * @param trimField 是否去除空格
     * @return this
     */
    public CsvReadConfig setTrimField(final boolean newTrimField) {
        this.trimField = newTrimField;
        return this;
    }
}
