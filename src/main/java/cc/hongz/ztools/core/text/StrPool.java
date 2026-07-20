package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.XmlUtil;

/**
 * 常用字符串常量定义
 * @see CharPool
 *
 * @author looly
 * @since 5.6.3
 */
public interface StrPool {

	char C_SPACE = CharPool.SPACE;
	char C_TAB = CharPool.TAB;
	char C_DOT = CharPool.DOT;
	char C_SLASH = CharPool.SLASH;
	char C_BACKSLASH = CharPool.BACKSLASH;
	char C_CR = CharPool.CR;
	char C_LF = CharPool.LF;
	char C_UNDERLINE = CharPool.UNDERLINE;
	char C_COMMA = CharPool.COMMA;
	char C_DELIM_START = CharPool.DELIM_START;
	char C_DELIM_END = CharPool.DELIM_END;
	char C_BRACKET_START = CharPool.BRACKET_START;
	char C_BRACKET_END = CharPool.BRACKET_END;
	char C_COLON = CharPool.COLON;
	char C_AT = CharPool.AT;

	String TAB = "	";
	String DOT = ".";
	String DOUBLE_DOT = "..";
	String SLASH = "/";
	String BACKSLASH = "\\";
	String CR = "\r";
	String LF = "\n";
	String CRLF = "\r\n";
	String UNDERLINE = "_";
	String DASHED = "-";
	String COMMA = ",";
	String DELIM_START = "{";
	String DELIM_END = "}";
	String BRACKET_START = "[";
	String BRACKET_END = "]";
	String COLON = ":";
	String AT = "@";

	String HTML_NBSP = XmlUtil.NBSP;
	String HTML_AMP = XmlUtil.AMP;
	String HTML_QUOTE = XmlUtil.QUOTE;
	String HTML_APOS = XmlUtil.APOS;
	String HTML_LT = XmlUtil.LT;
	String HTML_GT = XmlUtil.GT;

	String EMPTY_JSON = "{}";
}
