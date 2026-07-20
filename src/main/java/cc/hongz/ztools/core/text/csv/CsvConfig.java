package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.util.CharUtil;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CSV基础配置项
 *
 * @param <T> 继承子类类型，用于this返回
 * @author looly
 * @since 4.0.5
 */
@SuppressWarnings("unchecked")
public class CsvConfig<T extends CsvConfig<T>> implements Serializable {
	private static final long serialVersionUID = -8069578249066158459L;

	protected char fieldSeparator = CharUtil.COMMA;
	protected char textDelimiter = CharUtil.DOUBLE_QUOTES;
	protected Character commentCharacter = '#';
	protected Map<String, String> headerAlias = new LinkedHashMap<>();

	public T setFieldSeparator(final char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
		return (T) this;
	}

	public T setTextDelimiter(char textDelimiter) {
		this.textDelimiter = textDelimiter;
		return (T) this;
	}

	public T disableComment() {
		return setCommentCharacter(null);
	}

	public T setCommentCharacter(Character commentCharacter) {
		this.commentCharacter = commentCharacter;
		return (T) this;
	}

	public T setHeaderAlias(Map<String, String> headerAlias) {
		this.headerAlias = headerAlias;
		return (T) this;
	}

	public T addHeaderAlias(String header, String alias) {
		this.headerAlias.put(header, alias);
		return (T) this;
	}

	public T removeHeaderAlias(String header) {
		this.headerAlias.remove(header);
		return (T) this;
	}
}
