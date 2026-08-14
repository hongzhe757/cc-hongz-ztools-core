package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.util.CharUtil;

import java.io.Serializable;

/**
 * CSV写出配置项.
 *
 * @author looly
 * @since 5.7.14
 */
public class CsvWriteConfig extends CsvConfig<CsvWriteConfig> implements Serializable {
    private static final long serialVersionUID = 5396453565371560052L;

    /**
     * 是否始终使用文本限定符.
     */
    protected boolean alwaysDelimitText;
    /**
     * 行分隔符，默认CR+LF.
     */
    protected char[] lineDelimiter = {CharUtil.CR, CharUtil.LF};
    /**
     * 结尾是否添加换行符.
     */
    protected boolean endingLineBreak;

    /**
     * 获取默认配置.
     *
     * @return 默认配置
     */
    public static CsvWriteConfig defaultConfig() {
        return new CsvWriteConfig();
    }

    /**
     * 设置是否始终使用文本限定符.
     *
     * @param alwaysDelimitText 是否始终使用文本限定符
     * @return this
     */
    public CsvWriteConfig setAlwaysDelimitText(final boolean newAlwaysDelimitText) {
        this.alwaysDelimitText = newAlwaysDelimitText;
        return this;
    }

    /**
     * 设置行分隔符.
     *
     * @param lineDelimiter 行分隔符
     * @return this
     */
    public CsvWriteConfig setLineDelimiter(final char[] newLineDelimiter) {
        this.lineDelimiter = newLineDelimiter;
        return this;
    }

    /**
     * 设置结尾是否添加换行符.
     *
     * @param endingLineBreak 是否添加换行符
     * @return this
     */
    public CsvWriteConfig setEndingLineBreak(final boolean newEndingLineBreak) {
        this.endingLineBreak = newEndingLineBreak;
        return this;
    }
}
