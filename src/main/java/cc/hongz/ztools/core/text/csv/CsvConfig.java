package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.util.CharUtil;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CSV基础配置项.
 *
 * @param <T> 继承子类类型，用于this返回
 * @author looly
 * @since 4.0.5
 */
@SuppressWarnings("unchecked")
public class CsvConfig<T extends CsvConfig<T>> implements Serializable {
    private static final long serialVersionUID = -8069578249066158459L;

    /**
     * 字段分隔符，默认逗号.
     */
    protected char fieldSeparator = CharUtil.COMMA;
    /**
     * 文本限定符，默认双引号.
     */
    protected char textDelimiter = CharUtil.DOUBLE_QUOTES;
    /**
     * 注释字符，默认#.
     */
    protected Character commentCharacter = '#';
    /**
     * 表头别名.
     */
    protected Map<String, String> headerAlias = new LinkedHashMap<>();

    /**
     * 设置字段分隔符.
     *
     * @param fieldSeparator 字段分隔符
     * @return this
     */
    public T setFieldSeparator(final char fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
        return (T) this;
    }

    /**
     * 设置文本限定符.
     *
     * @param textDelimiter 文本限定符
     * @return this
     */
    public T setTextDelimiter(final char textDelimiter) {
        this.textDelimiter = textDelimiter;
        return (T) this;
    }

    /**
     * 禁用注释.
     *
     * @return this
     */
    public T disableComment() {
        return setCommentCharacter(null);
    }

    /**
     * 设置注释字符.
     *
     * @param commentCharacter 注释字符
     * @return this
     */
    public T setCommentCharacter(final Character commentCharacter) {
        this.commentCharacter = commentCharacter;
        return (T) this;
    }

    /**
     * 设置表头别名.
     *
     * @param headerAlias 表头别名
     * @return this
     */
    public T setHeaderAlias(final Map<String, String> headerAlias) {
        this.headerAlias = headerAlias;
        return (T) this;
    }

    /**
     * 添加表头别名.
     *
     * @param header 原表头
     * @param alias  别名
     * @return this
     */
    public T addHeaderAlias(final String header, final String alias) {
        this.headerAlias.put(header, alias);
        return (T) this;
    }

    /**
     * 移除表头别名.
     *
     * @param header 表头
     * @return this
     */
    public T removeHeaderAlias(final String header) {
        this.headerAlias.remove(header);
        return (T) this;
    }
}
