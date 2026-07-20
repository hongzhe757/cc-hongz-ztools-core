package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.text.finder.CharFinder;
import cc.hongz.ztools.core.text.finder.CharMatcherFinder;
import cc.hongz.ztools.core.text.finder.LengthFinder;
import cc.hongz.ztools.core.text.finder.PatternFinder;
import cc.hongz.ztools.core.text.finder.StrFinder;
import cc.hongz.ztools.core.text.split.SplitIter;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 字符串切分器，封装统一的字符串分割静态方法
 * @author Looly
 * @since 5.7.0
 */
public class StrSplitter {

	//---------------------------------------------------------------------------------------------- Split by char

	public static List<String> splitPath(CharSequence str) {
		return splitPath(str, 0);
	}

	public static String[] splitPathToArray(CharSequence str) {
		return toArray(splitPath(str));
	}

	public static List<String> splitPath(CharSequence str, int limit) {
		return split(str, StrUtil.C_SLASH, limit, true, true);
	}

	public static String[] splitPathToArray(CharSequence str, int limit) {
		return toArray(splitPath(str, limit));
	}

	public static List<String> splitTrim(CharSequence str, char separator, boolean ignoreEmpty) {
		return split(str, separator, 0, true, ignoreEmpty);
	}

	public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	public static List<String> splitTrim(CharSequence str, char separator, int limit, boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty, false);
	}

	public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}

	public static <R> List<R> split(CharSequence str, char separator, int limit, boolean ignoreEmpty, Function<String, R> mapping) {
		return split(str, separator, limit, ignoreEmpty, false, mapping);
	}

	public static List<String> splitIgnoreCase(CharSequence text, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return split(text, separator, limit, isTrim, ignoreEmpty, true);
	}

	public static List<String> split(CharSequence text, char separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
		return split(text, separator, limit, ignoreEmpty, ignoreCase, trimFunc(isTrim));
	}

	public static <R> List<R> split(CharSequence text, char separator, int limit, boolean ignoreEmpty,
									boolean ignoreCase, Function<String, R> mapping) {
		if (null == text) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new CharFinder(separator, ignoreCase), limit, ignoreEmpty);
		return splitIter.toList(mapping);
	}

	public static String[] splitToArray(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}

	//---------------------------------------------------------------------------------------------- Split by String

	public static List<String> split(CharSequence str, String separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, -1, isTrim, ignoreEmpty, false);
	}

	public static List<String> splitTrim(CharSequence str, String separator, boolean ignoreEmpty) {
		return split(str, separator, true, ignoreEmpty);
	}

	public static List<String> split(CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}

	public static List<String> splitTrim(CharSequence str, String separator, int limit, boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty);
	}

	public static List<String> splitIgnoreCase(CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, true);
	}

	public static List<String> splitTrimIgnoreCase(CharSequence str, String separator, int limit, boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty, true);
	}

	public static List<String> split(CharSequence text, String separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase) {
		if (null == text) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new StrFinder(separator, ignoreCase), limit, ignoreEmpty);
		return splitIter.toList(isTrim);
	}

	public static String[] splitToArray(CharSequence str, String separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}

	//---------------------------------------------------------------------------------------------- Split by Whitespace

	public static List<String> split(CharSequence text, int limit) {
		if (null == text) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new CharMatcherFinder(CharUtil::isBlankChar), limit, true);
		return splitIter.toList(false);
	}

	public static String[] splitToArray(String str, int limit) {
		return toArray(split(str, limit));
	}
	//---------------------------------------------------------------------------------------------- Split by regex

	public static List<String> splitByRegex(String text, String separatorRegex, int limit, boolean isTrim, boolean ignoreEmpty) {
		final Pattern pattern = Pattern.compile(separatorRegex);
		return split(text, pattern, limit, isTrim, ignoreEmpty);
	}

	public static List<String> split(String text, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
		if (null == text) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new PatternFinder(separatorPattern), limit, ignoreEmpty);
		return splitIter.toList(isTrim);
	}

	public static String[] splitToArray(String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty) {
		return toArray(split(str, separatorPattern, limit, isTrim, ignoreEmpty));
	}
	//---------------------------------------------------------------------------------------------- Split by length

	public static String[] splitByLength(CharSequence text, int len) {
		if (null == text) {
			return new String[0];
		}
		SplitIter splitIter = new SplitIter(text, new LengthFinder(len), -1, false);
		return splitIter.toArray(false);
	}
	//---------------------------------------------------------------------------------------------------------- Private method start

	private static String[] toArray(List<String> list) {
		return list.toArray(new String[0]);
	}

	private static Function<String, String> trimFunc(boolean isTrim) {
		return (str) -> isTrim ? StrUtil.trim(str) : str;
	}
	//---------------------------------------------------------------------------------------------------------- Private method end
}
