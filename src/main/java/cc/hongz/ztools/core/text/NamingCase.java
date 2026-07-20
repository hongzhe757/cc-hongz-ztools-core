package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 命名规则封装，主要是针对驼峰风格命名、连接符命名等的封装
 *
 * @author looly
 * @since 5.7.10
 */
public class NamingCase {

	public static String toUnderlineCase(CharSequence str) {
		return toSymbolCase(str, CharUtil.UNDERLINE);
	}

	public static String toKebabCase(CharSequence str) {
		return toSymbolCase(str, CharUtil.DASHED);
	}

	public static String toSymbolCase(CharSequence str, char symbol) {
		if (str == null) {
			return null;
		}

		final int length = str.length();
		final StrBuilder sb = new StrBuilder();
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
				final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;

				if (null != preChar) {
					if (symbol == preChar) {
						if (null == nextChar || Character.isLowerCase(nextChar)) {
							c = Character.toLowerCase(c);
						}
					} else if (Character.isLowerCase(preChar)) {
						sb.append(symbol);
						if (null == nextChar || Character.isLowerCase(nextChar) || CharUtil.isNumber(nextChar)) {
							c = Character.toLowerCase(c);
						}
					} else {
						if (null != nextChar && Character.isLowerCase(nextChar)) {
							sb.append(symbol);
							c = Character.toLowerCase(c);
						}
					}
				} else {
					if (null == nextChar || Character.isLowerCase(nextChar)) {
						c = Character.toLowerCase(c);
					}
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static String toPascalCase(CharSequence name) {
		return StrUtil.upperFirst(toCamelCase(name));
	}

	public static String toCamelCase(CharSequence name) {
		return toCamelCase(name, CharUtil.UNDERLINE);
	}

	public static String toCamelCase(CharSequence name, char symbol) {
		return toCamelCase(name, symbol, true);
	}

	public static String toCamelCase(CharSequence name, char symbol, boolean otherCharToLower) {
		if (null == name) {
			return null;
		}

		final String name2 = name.toString();
		if (StrUtil.contains(name2, symbol)) {
			final int length = name2.length();
			final StringBuilder sb = new StringBuilder(length);
			boolean upperCase = false;
			for (int i = 0; i < length; i++) {
				char c = name2.charAt(i);

				if (c == symbol) {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(otherCharToLower ? Character.toLowerCase(c) : c);
				}
			}
			return sb.toString();
		} else {
			return name2;
		}
	}

}
