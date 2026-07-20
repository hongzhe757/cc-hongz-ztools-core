package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.util.CharUtil;

import java.io.Serializable;

/**
 * CSV写出配置项
 *
 * @author looly
 */
public class CsvWriteConfig extends CsvConfig<CsvWriteConfig> implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	protected boolean alwaysDelimitText;
	protected char[] lineDelimiter = {CharUtil.CR, CharUtil.LF};
	protected boolean endingLineBreak;

	public static CsvWriteConfig defaultConfig() { return new CsvWriteConfig(); }

	public CsvWriteConfig setAlwaysDelimitText(boolean alwaysDelimitText) {
		this.alwaysDelimitText = alwaysDelimitText;
		return this;
	}

	public CsvWriteConfig setLineDelimiter(char[] lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
		return this;
	}

	public CsvWriteConfig setEndingLineBreak(boolean endingLineBreak) {
		this.endingLineBreak = endingLineBreak;
		return this;
	}
}
