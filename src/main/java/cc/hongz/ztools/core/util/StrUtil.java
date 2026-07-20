package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.comparator.VersionComparator;
import cc.hongz.ztools.core.text.Convert;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.lang.Matcher;
import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.text.StrBuilder;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 瀛楃涓插伐鍏风被.
 *
 * <p>杩佺Щ鑷?hutool {@code cn.hutool.core.text.CharSequenceUtil} + {@code cn.hutool.core.util.StrUtil}锛? * 鍚堝苟浜?StrPool 甯搁噺銆?/p>
 *
 * @author looly, xiaoleilu
 */
public class StrUtil {

	/**
	 * 绱㈠紩鍊硷細{@code -1}
	 */
	public static final int INDEX_NOT_FOUND = -1;

	// ==================== StrPool constants (from hutool StrPool) ====================
	public static final char C_SPACE = ' ';
	public static final char C_TAB = '\t';
	public static final char C_DOT = '.';
	public static final char C_SLASH = '/';
	public static final char C_BACKSLASH = '\\';
	public static final char C_CR = '\r';
	public static final char C_LF = '\n';
	public static final char C_UNDERLINE = '_';
	public static final char C_COMMA = ',';
	public static final char C_DELIM_START = '{';
	public static final char C_DELIM_END = '}';
	public static final char C_BRACKET_START = '[';
	public static final char C_BRACKET_END = ']';
	public static final char C_COLON = ':';
	public static final char C_AT = '@';

	public static final String TAB = "	";
	public static final String DOT = ".";
	public static final String DOUBLE_DOT = "..";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";
	public static final String UNDERLINE = "_";
	public static final String DASHED = "-";
	public static final String COMMA = ",";
	public static final String DELIM_START = "{";
	public static final String DELIM_END = "}";
	public static final String BRACKET_START = "[";
	public static final String BRACKET_END = "]";
	public static final String COLON = ":";
	public static final String AT = "@";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp;";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_APOS = "&apos;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";

	// ==================== CharSequenceUtil methods ====================



















/**
 * {@link CharSequence} 鐩稿叧宸ュ叿绫诲皝瑁?
 *
 * @author looly
 * @since 5.5.3
 */


	/**
	 * 瀛楃涓插父閲忥細{@code "null"} <br>
	 * 娉ㄦ剰锛歿@code "null" != null}
	 */
	public static final String NULL = "null";

	/**
	 * 瀛楃涓插父閲忥細绌哄瓧绗︿覆 {@code ""}
	 */
	public static final String EMPTY = "";

	/**
	 * 瀛楃涓插父閲忥細绌烘牸绗?{@code " "}
	 */
	public static final String SPACE = " ";

	/**
	 * <p>瀛楃涓叉槸鍚︿负绌虹櫧锛岀┖鐧界殑瀹氫箟濡備笅锛?/p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>绌哄瓧绗︿覆锛歿@code ""}</li>
	 *     <li>绌烘牸銆佸叏瑙掔┖鏍笺€佸埗琛ㄧ銆佹崲琛岀锛岀瓑涓嶅彲瑙佸瓧绗?/li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isBlank(null)     // true}</li>
	 *     <li>{@code isBlank("")       // true}</li>
	 *     <li>{@code isBlank(" \t\n")  // true}</li>
	 *     <li>{@code isBlank("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isEmpty(CharSequence)} 鐨勫尯鍒槸锛?
	 * 璇ユ柟娉曚細鏍￠獙绌虹櫧瀛楃锛屼笖鎬ц兘鐩稿浜?{@link #isEmpty(CharSequence)} 鐣ユ參銆?/p>
	 * <br>
	 *
	 * <p>寤鸿锛?/p>
	 * <ul>
	 *     <li>璇ユ柟娉曞缓璁粎瀵逛簬瀹㈡埛绔紙鎴栫涓夋柟鎺ュ彛锛変紶鍏ョ殑鍙傛暟浣跨敤璇ユ柟娉曘€?/li>
	 *     <li>闇€瑕佸悓鏃舵牎楠屽涓瓧绗︿覆鏃讹紝寤鸿閲囩敤 {@link #hasBlank(CharSequence...)} 鎴?{@link #isAllBlank(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 琚娴嬬殑瀛楃涓?
	 * @return 鑻ヤ负绌虹櫧锛屽垯杩斿洖 true
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isBlank(CharSequence str) {
		final int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			// 鍙鏈変竴涓潪绌哄瓧绗﹀嵆涓洪潪绌哄瓧绗︿覆
			if (false == CharUtil.isBlankChar(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>瀛楃涓叉槸鍚︿负闈炵┖鐧斤紝闈炵┖鐧界殑瀹氫箟濡備笅锛?</p>
	 * <ol>
	 *     <li>涓嶄负 {@code null}</li>
	 *     <li>涓嶄负绌哄瓧绗︿覆锛歿@code ""}</li>
	 *     <li>涓嶄负绌烘牸銆佸叏瑙掔┖鏍笺€佸埗琛ㄧ銆佹崲琛岀锛岀瓑涓嶅彲瑙佸瓧绗?/li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isNotBlank(null)     // false}</li>
	 *     <li>{@code isNotBlank("")       // false}</li>
	 *     <li>{@code isNotBlank(" \t\n")  // false}</li>
	 *     <li>{@code isNotBlank("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isNotEmpty(CharSequence)} 鐨勫尯鍒槸锛?
	 * 璇ユ柟娉曚細鏍￠獙绌虹櫧瀛楃锛屼笖鎬ц兘鐩稿浜?{@link #isNotEmpty(CharSequence)} 鐣ユ參銆?/p>
	 * <p>寤鸿锛氫粎瀵逛簬瀹㈡埛绔紙鎴栫涓夋柟鎺ュ彛锛変紶鍏ョ殑鍙傛暟浣跨敤璇ユ柟娉曘€?/p>
	 *
	 * @param str 琚娴嬬殑瀛楃涓?
	 * @return 鏄惁涓洪潪绌?
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isNotBlank(CharSequence str) {
		return !isBlank(str);
	}

	/**
	 * <p>鎸囧畾瀛楃涓叉暟缁勪腑锛屾槸鍚﹀寘鍚┖瀛楃涓层€?/p>
	 * <p>濡傛灉鎸囧畾鐨勫瓧绗︿覆鏁扮粍鐨勯暱搴︿负 0锛屾垨鑰呭叾涓殑浠绘剰涓€涓厓绱犳槸绌哄瓧绗︿覆锛屽垯杩斿洖 true銆?/p>
	 * <br>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code hasBlank()                  // true}</li>
	 *     <li>{@code hasBlank("", null, " ")     // true}</li>
	 *     <li>{@code hasBlank("123", " ")        // true}</li>
	 *     <li>{@code hasBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isAllBlank(CharSequence...)} 鐨勫尯鍒湪浜庯細</p>
	 * <ul>
	 *     <li>hasBlank(CharSequence...)            绛変环浜?{@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>{@link #isAllBlank(CharSequence...)} 绛変环浜?{@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 瀛楃涓插垪琛?
	 * @return 鏄惁鍖呭惈绌哄瓧绗︿覆
	 */
	public static boolean hasBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>鎸囧畾瀛楃涓叉暟缁勪腑鐨勫厓绱狅紝鏄惁鍏ㄩ儴涓虹┖瀛楃涓层€?/p>
	 * <p>濡傛灉鎸囧畾鐨勫瓧绗︿覆鏁扮粍鐨勯暱搴︿负 0锛屾垨鑰呮墍鏈夊厓绱犻兘鏄┖瀛楃涓诧紝鍒欒繑鍥?true銆?/p>
	 * <br>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isAllBlank()                  // true}</li>
	 *     <li>{@code isAllBlank("", null, " ")     // true}</li>
	 *     <li>{@code isAllBlank("123", " ")        // false}</li>
	 *     <li>{@code isAllBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #hasBlank(CharSequence...)} 鐨勫尯鍒湪浜庯細</p>
	 * <ul>
	 *     <li>{@link #hasBlank(CharSequence...)}   绛変环浜?{@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>isAllBlank(CharSequence...)          绛変环浜?{@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 瀛楃涓插垪琛?
	 * @return 鎵€鏈夊瓧绗︿覆鏄惁涓虹┖鐧?
	 */
	public static boolean isAllBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>瀛楃涓叉槸鍚︿负绌猴紝绌虹殑瀹氫箟濡備笅锛?/p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>绌哄瓧绗︿覆锛歿@code ""}</li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isEmpty(null)     // true}</li>
	 *     <li>{@code isEmpty("")       // true}</li>
	 *     <li>{@code isEmpty(" \t\n")  // false}</li>
	 *     <li>{@code isEmpty("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isBlank(CharSequence)} 鐨勫尯鍒槸锛氳鏂规硶涓嶆牎楠岀┖鐧藉瓧绗︺€?/p>
	 * <p>寤鸿锛?/p>
	 * <ul>
	 *     <li>璇ユ柟娉曞缓璁敤浜庡伐鍏风被鎴栦换浣曞彲浠ラ鏈熺殑鏂规硶鍙傛暟鐨勬牎楠屼腑銆?/li>
	 *     <li>闇€瑕佸悓鏃舵牎楠屽涓瓧绗︿覆鏃讹紝寤鸿閲囩敤 {@link #hasEmpty(CharSequence...)} 鎴?{@link #isAllEmpty(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 琚娴嬬殑瀛楃涓?
	 * @return 鏄惁涓虹┖
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>瀛楃涓叉槸鍚︿负闈炵┖鐧斤紝闈炵┖鐧界殑瀹氫箟濡備笅锛?</p>
	 * <ol>
	 *     <li>涓嶄负 {@code null}</li>
	 *     <li>涓嶄负绌哄瓧绗︿覆锛歿@code ""}</li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isNotEmpty(null)     // false}</li>
	 *     <li>{@code isNotEmpty("")       // false}</li>
	 *     <li>{@code isNotEmpty(" \t\n")  // true}</li>
	 *     <li>{@code isNotEmpty("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isNotBlank(CharSequence)} 鐨勫尯鍒槸锛氳鏂规硶涓嶆牎楠岀┖鐧藉瓧绗︺€?/p>
	 * <p>寤鸿锛氳鏂规硶寤鸿鐢ㄤ簬宸ュ叿绫绘垨浠讳綍鍙互棰勬湡鐨勬柟娉曞弬鏁扮殑鏍￠獙涓€?/p>
	 *
	 * @param str 琚娴嬬殑瀛楃涓?
	 * @return 鏄惁涓洪潪绌?
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
	}

	/**
	 * 褰撶粰瀹氬瓧绗︿覆涓簄ull鏃讹紝杞崲涓篍mpty
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鍘熷瓧绗︿覆鎴栬€呯┖涓?
	 * @see #nullToEmpty(CharSequence)
	 * @since 4.6.3
	 */
	public static String emptyIfNull(CharSequence str) {
		return nullToEmpty(str);
	}

	/**
	 * 褰撶粰瀹氬瓧绗︿覆涓簄ull鏃讹紝杞崲涓篍mpty
	 *
	 * @param str 琚浆鎹㈢殑瀛楃涓?
	 * @return 杞崲鍚庣殑瀛楃涓?
	 */
	public static String nullToEmpty(CharSequence str) {
		return nullToDefault(str, EMPTY);
	}

	/**
	 * 濡傛灉瀛楃涓叉槸 {@code null}锛屽垯杩斿洖鎸囧畾榛樿瀛楃涓诧紝鍚﹀垯杩斿洖瀛楃涓叉湰韬€?
	 *
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        瑕佽浆鎹㈢殑瀛楃涓?
	 * @param defaultStr 榛樿瀛楃涓?
	 * @return 瀛楃涓叉湰韬垨鎸囧畾鐨勯粯璁ゅ瓧绗︿覆
	 */
	public static String nullToDefault(CharSequence str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 濡傛灉瀛楃涓叉槸{@code null}鎴栬€?quot;&quot;锛屽垯杩斿洖鎸囧畾榛樿瀛楃涓诧紝鍚﹀垯杩斿洖瀛楃涓叉湰韬€?
	 *
	 * <pre>
	 * emptyToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * emptyToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * emptyToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * emptyToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        瑕佽浆鎹㈢殑瀛楃涓?
	 * @param defaultStr 榛樿瀛楃涓?
	 * @return 瀛楃涓叉湰韬垨鎸囧畾鐨勯粯璁ゅ瓧绗︿覆
	 * @since 4.1.0
	 */
	public static String emptyToDefault(CharSequence str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str.toString();
	}

	/**
	 * 濡傛灉瀛楃涓叉槸{@code null}鎴栬€?quot;&quot;鎴栬€呯┖鐧斤紝鍒欒繑鍥炴寚瀹氶粯璁ゅ瓧绗︿覆锛屽惁鍒欒繑鍥炲瓧绗︿覆鏈韩銆?
	 *
	 * <pre>
	 * blankToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * blankToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * blankToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
	 * blankToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        瑕佽浆鎹㈢殑瀛楃涓?
	 * @param defaultStr 榛樿瀛楃涓?
	 * @return 瀛楃涓叉湰韬垨鎸囧畾鐨勯粯璁ゅ瓧绗︿覆
	 * @since 4.1.0
	 */
	public static String blankToDefault(CharSequence str, String defaultStr) {
		return isBlank(str) ? defaultStr : str.toString();
	}

	/**
	 * 褰撶粰瀹氬瓧绗︿覆涓虹┖瀛楃涓叉椂锛岃浆鎹负{@code null}
	 *
	 * @param str 琚浆鎹㈢殑瀛楃涓?
	 * @return 杞崲鍚庣殑瀛楃涓?
	 */
	public static String emptyToNull(CharSequence str) {
		return isEmpty(str) ? null : str.toString();
	}

	/**
	 * <p>鏄惁鍖呭惈绌哄瓧绗︿覆銆?/p>
	 * <p>濡傛灉鎸囧畾鐨勫瓧绗︿覆鏁扮粍鐨勯暱搴︿负 0锛屾垨鑰呭叾涓殑浠绘剰涓€涓厓绱犳槸绌哄瓧绗︿覆锛屽垯杩斿洖 true銆?/p>
	 * <br>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code hasEmpty()                  // true}</li>
	 *     <li>{@code hasEmpty("", null)          // true}</li>
	 *     <li>{@code hasEmpty("123", "")         // true}</li>
	 *     <li>{@code hasEmpty("123", "abc")      // false}</li>
	 *     <li>{@code hasEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isAllEmpty(CharSequence...)} 鐨勫尯鍒湪浜庯細</p>
	 * <ul>
	 *     <li>hasEmpty(CharSequence...)            绛変环浜?{@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>{@link #isAllEmpty(CharSequence...)} 绛変环浜?{@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 瀛楃涓插垪琛?
	 * @return 鏄惁鍖呭惈绌哄瓧绗︿覆
	 */
	public static boolean hasEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>鎸囧畾瀛楃涓叉暟缁勪腑鐨勫厓绱狅紝鏄惁鍏ㄩ儴涓虹┖瀛楃涓层€?/p>
	 * <p>濡傛灉鎸囧畾鐨勫瓧绗︿覆鏁扮粍鐨勯暱搴︿负 0锛屾垨鑰呮墍鏈夊厓绱犻兘鏄┖瀛楃涓诧紝鍒欒繑鍥?true銆?/p>
	 * <br>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isAllEmpty()                  // true}</li>
	 *     <li>{@code isAllEmpty("", null)          // true}</li>
	 *     <li>{@code isAllEmpty("123", "")         // false}</li>
	 *     <li>{@code isAllEmpty("123", "abc")      // false}</li>
	 *     <li>{@code isAllEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #hasEmpty(CharSequence...)} 鐨勫尯鍒湪浜庯細</p>
	 * <ul>
	 *     <li>{@link #hasEmpty(CharSequence...)}   绛変环浜?{@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>isAllEmpty(CharSequence...)          绛変环浜?{@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 瀛楃涓插垪琛?
	 * @return 鎵€鏈夊瓧绗︿覆鏄惁涓虹┖鐧?
	 */
	public static boolean isAllEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>鎸囧畾瀛楃涓叉暟缁勪腑鐨勫厓绱狅紝鏄惁閮戒笉涓虹┖瀛楃涓层€?/p>
	 * <p>濡傛灉鎸囧畾鐨勫瓧绗︿覆鏁扮粍鐨勯暱搴︿笉涓?0锛屾垨鑰呮墍鏈夊厓绱犻兘涓嶆槸绌哄瓧绗︿覆锛屽垯杩斿洖 true銆?/p>
	 * <br>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code isAllNotEmpty()                  // false}</li>
	 *     <li>{@code isAllNotEmpty("", null)          // false}</li>
	 *     <li>{@code isAllNotEmpty("123", "")         // false}</li>
	 *     <li>{@code isAllNotEmpty("123", "abc")      // true}</li>
	 *     <li>{@code isAllNotEmpty(" ", "\t", "\n")   // true}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isAllEmpty(CharSequence...)} 鐨勫尯鍒湪浜庯細</p>
	 * <ul>
	 *     <li>{@link #isAllEmpty(CharSequence...)}    绛変环浜?{@code isEmpty(...) && isEmpty(...) && ...}</li>
	 *     <li>isAllNotEmpty(CharSequence...)          绛変环浜?{@code !isEmpty(...) && !isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param args 瀛楃涓叉暟缁?
	 * @return 鎵€鏈夊瓧绗︿覆鏄惁閮戒笉涓轰负绌虹櫧
	 * @since 5.3.6
	 */
	public static boolean isAllNotEmpty(CharSequence... args) {
		return false == hasEmpty(args);
	}

	/**
	 * 鏄惁鍏ㄩ兘涓嶄负{@code null}鎴栫┖瀵硅薄鎴栫┖鐧界鐨勫璞★紝閫氳繃{@link #hasBlank(CharSequence...)} 鍒ゆ柇鍏冪礌
	 *
	 * @param args 琚鏌ョ殑瀵硅薄,涓€涓垨鑰呭涓?
	 * @return 鏄惁閮戒笉涓虹┖
	 * @since 5.3.6
	 */
	public static boolean isAllNotBlank(CharSequence... args) {
		return false == hasBlank(args);
	}

	/**
	 * 妫€鏌ュ瓧绗︿覆鏄惁涓簄ull銆佲€渘ull鈥濄€佲€渦ndefined鈥?
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鏄惁涓簄ull銆佲€渘ull鈥濄€佲€渦ndefined鈥?
	 * @since 4.0.10
	 */
	public static boolean isNullOrUndefined(CharSequence str) {
		if (null == str) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 妫€鏌ュ瓧绗︿覆鏄惁涓簄ull銆佲€溾€濄€佲€渘ull鈥濄€佲€渦ndefined鈥?
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鏄惁涓簄ull銆佲€溾€濄€佲€渘ull鈥濄€佲€渦ndefined鈥?
	 * @since 4.0.10
	 */
	public static boolean isEmptyOrUndefined(CharSequence str) {
		if (isEmpty(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 妫€鏌ュ瓧绗︿覆鏄惁涓簄ull銆佺┖鐧戒覆銆佲€渘ull鈥濄€佲€渦ndefined鈥?
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鏄惁涓簄ull銆佺┖鐧戒覆銆佲€渘ull鈥濄€佲€渦ndefined鈥?
	 * @since 4.0.10
	 */
	public static boolean isBlankOrUndefined(CharSequence str) {
		if (isBlank(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 鏄惁涓衡€渘ull鈥濄€佲€渦ndefined鈥濓紝涓嶅仛绌烘寚閽堟鏌?
	 *
	 * @param str 瀛楃涓?
	 * @return 鏄惁涓衡€渘ull鈥濄€佲€渦ndefined鈥?
	 */
	private static boolean isNullOrUndefinedStr(CharSequence str) {
		String strString = str.toString().trim();
		return NULL.equals(strString) || "undefined".equals(strString);
	}

	// ------------------------------------------------------------------------ Trim

	/**
	 * 闄ゅ幓瀛楃涓插ご灏鹃儴鐨勭┖鐧斤紝濡傛灉瀛楃涓叉槸{@code null}锛屼緷鐒惰繑鍥瀧@code null}銆?
	 *
	 * <p>
	 * 娉ㄦ剰锛屽拰{@link String#trim()}涓嶅悓锛屾鏂规硶浣跨敤{@link CharUtil#isBlankChar(char)} 鏉ュ垽瀹氱┖鐧斤紝 鍥犺€屽彲浠ラ櫎鍘昏嫳鏂囧瓧绗﹂泦涔嬪鐨勫叾瀹冪┖鐧斤紝濡備腑鏂囩┖鏍笺€?
	 *
	 * <pre>
	 * trim(null)          = null
	 * trim(&quot;&quot;)            = &quot;&quot;
	 * trim(&quot;     &quot;)       = &quot;&quot;
	 * trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 *
	 * @param str 瑕佸鐞嗙殑瀛楃涓?
	 * @return 闄ゅ幓澶村熬绌虹櫧鐨勫瓧绗︿覆锛屽鏋滃師瀛椾覆涓簕@code null}锛屽垯杩斿洖{@code null}
	 */
	public static String trim(CharSequence str) {
		return (null == str) ? null : trim(str, 0);
	}

	/**
	 * 闄ゅ幓瀛楃涓插ご灏鹃儴鐨勭┖鐧斤紝濡傛灉瀛楃涓叉槸{@code null}锛岃繑鍥瀧@code ""}銆?
	 *
	 * <pre>
	 * trimToEmpty(null)          = ""
	 * trimToEmpty("")            = ""
	 * trimToEmpty("     ")       = ""
	 * trimToEmpty("abc")         = "abc"
	 * trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 瀛楃涓?
	 * @return 鍘婚櫎涓よ竟绌虹櫧绗﹀悗鐨勫瓧绗︿覆, 濡傛灉涓簄ull杩斿洖""
	 * @since 3.1.1
	 */
	public static String trimToEmpty(CharSequence str) {
		return str == null ? EMPTY : trim(str);
	}

	/**
	 * 闄ゅ幓瀛楃涓插ご灏鹃儴鐨勭┖鐧斤紝濡傛灉瀛楃涓叉槸{@code null}鎴栬€?"锛岃繑鍥瀧@code null}銆?
	 *
	 * <pre>
	 * trimToNull(null)          = null
	 * trimToNull("")            = null
	 * trimToNull("     ")       = null
	 * trimToNull("abc")         = "abc"
	 * trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 瀛楃涓?
	 * @return 鍘婚櫎涓よ竟绌虹櫧绗﹀悗鐨勫瓧绗︿覆, 濡傛灉涓虹┖杩斿洖null
	 * @since 3.2.1
	 */
	public static String trimToNull(CharSequence str) {
		final String trimStr = trim(str);
		return EMPTY.equals(trimStr) ? null : trimStr;
	}

	/**
	 * 闄ゅ幓瀛楃涓插ご閮ㄧ殑绌虹櫧锛屽鏋滃瓧绗︿覆鏄瘂@code null}锛屽垯杩斿洖{@code null}銆?
	 *
	 * <p>
	 * 娉ㄦ剰锛屽拰{@link String#trim()}涓嶅悓锛屾鏂规硶浣跨敤{@link CharUtil#isBlankChar(char)} 鏉ュ垽瀹氱┖鐧斤紝 鍥犺€屽彲浠ラ櫎鍘昏嫳鏂囧瓧绗﹂泦涔嬪鐨勫叾瀹冪┖鐧斤紝濡備腑鏂囩┖鏍笺€?
	 *
	 * <pre>
	 * trimStart(null)         = null
	 * trimStart(&quot;&quot;)           = &quot;&quot;
	 * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
	 * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
	 * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 *
	 * @param str 瑕佸鐞嗙殑瀛楃涓?
	 * @return 闄ゅ幓绌虹櫧鐨勫瓧绗︿覆锛屽鏋滃師瀛椾覆涓簕@code null}鎴栫粨鏋滃瓧绗︿覆涓簕@code ""}锛屽垯杩斿洖 {@code null}
	 */
	public static String trimStart(CharSequence str) {
		return trim(str, -1);
	}

	/**
	 * 闄ゅ幓瀛楃涓插熬閮ㄧ殑绌虹櫧锛屽鏋滃瓧绗︿覆鏄瘂@code null}锛屽垯杩斿洖{@code null}銆?
	 *
	 * <p>
	 * 娉ㄦ剰锛屽拰{@link String#trim()}涓嶅悓锛屾鏂规硶浣跨敤{@link CharUtil#isBlankChar(char)} 鏉ュ垽瀹氱┖鐧斤紝 鍥犺€屽彲浠ラ櫎鍘昏嫳鏂囧瓧绗﹂泦涔嬪鐨勫叾瀹冪┖鐧斤紝濡備腑鏂囩┖鏍笺€?
	 *
	 * <pre>
	 * trimEnd(null)       = null
	 * trimEnd(&quot;&quot;)         = &quot;&quot;
	 * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
	 * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
	 * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 *
	 * @param str 瑕佸鐞嗙殑瀛楃涓?
	 * @return 闄ゅ幓绌虹櫧鐨勫瓧绗︿覆锛屽鏋滃師瀛椾覆涓簕@code null}鎴栫粨鏋滃瓧绗︿覆涓簕@code ""}锛屽垯杩斿洖 {@code null}
	 */
	public static String trimEnd(CharSequence str) {
		return trim(str, 1);
	}

	/**
	 * 闄ゅ幓瀛楃涓插ご灏鹃儴鐨勭┖鐧界锛屽鏋滃瓧绗︿覆鏄瘂@code null}锛屼緷鐒惰繑鍥瀧@code null}銆?
	 *
	 * @param str  瑕佸鐞嗙殑瀛楃涓?
	 * @param mode {@code -1}琛ㄧずtrimStart锛寋@code 0}琛ㄧずtrim鍏ㄩ儴锛?{@code 1}琛ㄧずtrimEnd
	 * @return 闄ゅ幓鎸囧畾瀛楃鍚庣殑鐨勫瓧绗︿覆锛屽鏋滃師瀛椾覆涓簕@code null}锛屽垯杩斿洖{@code null}
	 */
	public static String trim(CharSequence str, int mode) {
		return trim(str, mode, CharUtil::isBlankChar);
	}

	/**
	 * 鎸夌収鏂█锛岄櫎鍘诲瓧绗︿覆澶村熬閮ㄧ殑鏂█涓虹湡鐨勫瓧绗︼紝濡傛灉瀛楃涓叉槸{@code null}锛屼緷鐒惰繑鍥瀧@code null}銆?
	 *
	 * @param str       瑕佸鐞嗙殑瀛楃涓?
	 * @param mode      {@code -1}琛ㄧずtrimStart锛寋@code 0}琛ㄧずtrim鍏ㄩ儴锛?{@code 1}琛ㄧずtrimEnd
	 * @param predicate 鏂█鏄惁杩囨帀瀛楃锛岃繑鍥瀧@code true}琛ㄨ堪杩囨护鎺夛紝{@code false}琛ㄧず涓嶈繃婊?
	 * @return 闄ゅ幓鎸囧畾瀛楃鍚庣殑鐨勫瓧绗︿覆锛屽鏋滃師瀛椾覆涓簕@code null}锛屽垯杩斿洖{@code null}
	 * @since 5.7.4
	 */
	public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
		String result;
		if (str == null) {
			result = null;
		} else {
			int length = str.length();
			int start = 0;
			int end = length;// 鎵弿瀛楃涓插ご閮?
			if (mode <= 0) {
				while ((start < end) && (predicate.test(str.charAt(start)))) {
					start++;
				}
			}// 鎵弿瀛楃涓插熬閮?
			if (mode >= 0) {
				while ((start < end) && (predicate.test(str.charAt(end - 1)))) {
					end--;
				}
			}
			if ((start > 0) || (end < length)) {
				result = str.toString().substring(start, end);
			} else {
				result = str.toString();
			}
		}

		return result;
	}

	// ------------------------------------------------------------------------ startWith

	/**
	 * 瀛楃涓叉槸鍚︿互缁欏畾瀛楃寮€濮?
	 *
	 * @param str 瀛楃涓?
	 * @param c   瀛楃
	 * @return 鏄惁寮€濮?
	 */
	public static boolean startWith(CharSequence str, char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(0);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆鍜屽紑澶村瓧绗︿覆閮戒负null鍒欒繑鍥瀟rue锛屽惁鍒欎换鎰忎竴涓€间负null杩斿洖false
	 *
	 * @param str        琚洃娴嬪瓧绗︿覆
	 * @param prefix     寮€澶村瓧绗︿覆
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?
	 * @since 5.4.3
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
		return startWith(str, prefix, ignoreCase, false);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆鍜屽紑澶村瓧绗︿覆閮戒负null鍒欒繑鍥瀟rue锛屽惁鍒欎换鎰忎竴涓€间负null杩斿洖false<br>
	 * <pre>
	 *     startWith("123", "123", false, true);   -- false
	 *     startWith("ABCDEF", "abc", true, true); -- true
	 *     startWith("abc", "abc", true, true);    -- false
	 * </pre>
	 *
	 * @param str          琚洃娴嬪瓧绗︿覆
	 * @param prefix       寮€澶村瓧绗︿覆
	 * @param ignoreCase   鏄惁蹇界暐澶у皬鍐?
	 * @param ignoreEquals 鏄惁蹇界暐瀛楃涓茬浉绛夌殑鎯呭喌
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?
	 * @since 5.4.3
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == prefix) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == prefix;
		}

		boolean isStartWith = str.toString()
			.regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());

		if (isStartWith) {
			return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
		}
		return false;
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?
	 *
	 * @param str    琚洃娴嬪瓧绗︿覆
	 * @param prefix 寮€澶村瓧绗︿覆
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶达紝蹇界暐鐩哥瓑瀛楃涓茬殑鎯呭喌
	 *
	 * @param str    琚洃娴嬪瓧绗︿覆
	 * @param prefix 寮€澶村瓧绗︿覆
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶村苟涓斾袱涓瓧绗︿覆涓嶇浉绛?
	 */
	public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false, true);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶达紝蹇界暐澶у皬鍐?
	 *
	 * @param str    琚洃娴嬪瓧绗︿覆
	 * @param prefix 寮€澶村瓧绗︿覆
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆寮€澶?
	 */
	public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, true);
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆寮€濮?br>
	 * 缁欏畾瀛楃涓插拰鏁扮粍涓虹┖閮借繑鍥瀎alse
	 *
	 * @param str      缁欏畾瀛楃涓?
	 * @param prefixes 闇€瑕佹娴嬬殑寮€濮嬪瓧绗︿覆
	 * @return 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆寮€濮?
	 * @since 3.0.6
	 */
	public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (CharSequence prefix : prefixes) {
			if (startWith(str, prefix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆寮€濮嬶紙蹇界暐澶у皬鍐欙級<br>
	 * 缁欏畾瀛楃涓插拰鏁扮粍涓虹┖閮借繑鍥瀎alse
	 *
	 * @param str      缁欏畾瀛楃涓?
	 * @param prefixes 闇€瑕佹娴嬬殑寮€濮嬪瓧绗︿覆
	 * @return 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆寮€濮?
	 * @since 5.8.1
	 */
	public static boolean startWithAnyIgnoreCase(final CharSequence str, final CharSequence... prefixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (final CharSequence prefix : prefixes) {
			if (startWith(str, prefix, true)) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------ endWith

	/**
	 * 瀛楃涓叉槸鍚︿互缁欏畾瀛楃缁撳熬
	 *
	 * @param str 瀛楃涓?
	 * @param c   瀛楃
	 * @return 鏄惁缁撳熬
	 */
	public static boolean endWith(CharSequence str, char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(str.length() - 1);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆鍜屽紑澶村瓧绗︿覆閮戒负null鍒欒繑鍥瀟rue锛屽惁鍒欎换鎰忎竴涓€间负null杩斿洖false
	 *
	 * @param str        琚洃娴嬪瓧绗︿覆
	 * @param suffix     缁撳熬瀛楃涓?
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
		return endWith(str, suffix, ignoreCase, false);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆鍜屽紑澶村瓧绗︿覆閮戒负null鍒欒繑鍥瀟rue锛屽惁鍒欎换鎰忎竴涓€间负null杩斿洖false
	 *
	 * @param str          琚洃娴嬪瓧绗︿覆
	 * @param suffix       缁撳熬瀛楃涓?
	 * @param ignoreCase   鏄惁蹇界暐澶у皬鍐?
	 * @param ignoreEquals 鏄惁蹇界暐瀛楃涓茬浉绛夌殑鎯呭喌
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬
	 * @since 5.8.0
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == suffix) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == suffix;
		}

		final int strOffset = str.length() - suffix.length();
		boolean isEndWith = str.toString()
			.regionMatches(ignoreCase, strOffset, suffix.toString(), 0, suffix.length());

		if (isEndWith) {
			return (false == ignoreEquals) || (false == equals(str, suffix, ignoreCase));
		}
		return false;
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬
	 *
	 * @param str    琚洃娴嬪瓧绗︿覆
	 * @param suffix 缁撳熬瀛楃涓?
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬锛屽拷鐣ュぇ灏忓啓
	 *
	 * @param str    琚洃娴嬪瓧绗︿覆
	 * @param suffix 缁撳熬瀛楃涓?
	 * @return 鏄惁浠ユ寚瀹氬瓧绗︿覆缁撳熬
	 */
	public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, true);
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆缁撳熬<br>
	 * 缁欏畾瀛楃涓插拰鏁扮粍涓虹┖閮借繑鍥瀎alse
	 *
	 * @param str      缁欏畾瀛楃涓?
	 * @param suffixes 闇€瑕佹娴嬬殑缁撳熬瀛楃涓?
	 * @return 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆缁撳熬
	 * @since 3.0.6
	 */
	public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (CharSequence suffix : suffixes) {
			if (endWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆缁撳熬锛堝拷鐣ュぇ灏忓啓锛?br>
	 * 缁欏畾瀛楃涓插拰鏁扮粍涓虹┖閮借繑鍥瀎alse
	 *
	 * @param str      缁欏畾瀛楃涓?
	 * @param suffixes 闇€瑕佹娴嬬殑缁撳熬瀛楃涓?
	 * @return 缁欏畾瀛楃涓叉槸鍚︿互浠讳綍涓€涓瓧绗︿覆缁撳熬
	 * @since 5.5.9
	 */
	public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (CharSequence suffix : suffixes) {
			if (endWith(str, suffix, true)) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------ contains

	/**
	 * 鎸囧畾瀛楃鏄惁鍦ㄥ瓧绗︿覆涓嚭鐜拌繃
	 *
	 * @param str        瀛楃涓?
	 * @param searchChar 琚煡鎵剧殑瀛楃
	 * @return 鏄惁鍖呭惈
	 * @since 3.1.2
	 */
	public static boolean contains(CharSequence str, char searchChar) {
		return indexOf(str, searchChar) > -1;
	}

	/**
	 * 鎸囧畾瀛楃涓叉槸鍚﹀湪瀛楃涓蹭腑鍑虹幇杩?
	 *
	 * @param str       瀛楃涓?
	 * @param searchStr 琚煡鎵剧殑瀛楃涓?
	 * @return 鏄惁鍖呭惈
	 * @since 5.1.1
	 */
	public static boolean contains(CharSequence str, CharSequence searchStr) {
		if (null == str || null == searchStr) {
			return false;
		}
		return str.toString().contains(searchStr);
	}

	/**
	 * 鏌ユ壘鎸囧畾瀛楃涓叉槸鍚﹀寘鍚寚瀹氬瓧绗︿覆鍒楄〃涓殑浠绘剰涓€涓瓧绗︿覆
	 *
	 * @param str      鎸囧畾瀛楃涓?
	 * @param testStrs 闇€瑕佹鏌ョ殑瀛楃涓叉暟缁?
	 * @return 鏄惁鍖呭惈浠绘剰涓€涓瓧绗︿覆
	 * @since 3.2.0
	 */
	public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
		return null != getContainsStr(str, testStrs);
	}

	/**
	 * 鏌ユ壘鎸囧畾瀛楃涓叉槸鍚﹀寘鍚寚瀹氬瓧绗﹀垪琛ㄤ腑鐨勪换鎰忎竴涓瓧绗?
	 *
	 * @param str       鎸囧畾瀛楃涓?
	 * @param testChars 闇€瑕佹鏌ョ殑瀛楃鏁扮粍
	 * @return 鏄惁鍖呭惈浠绘剰涓€涓瓧绗?
	 * @since 4.1.11
	 */
	public static boolean containsAny(CharSequence str, char... testChars) {
		if (false == isEmpty(str)) {
			int len = str.length();
			for (int i = 0; i < len; i++) {
				if (ArrayUtil.contains(testChars, str.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 妫€鏌ユ寚瀹氬瓧绗︿覆涓槸鍚﹀彧鍖呭惈缁欏畾鐨勫瓧绗?br>
	 * 杩欓噷鐨刢ontainsOnly骞朵笉鏄繀椤诲叏閮ㄧ粰瀹氱殑testChars閮介渶瑕佹湁锛岃€屾槸涓€涓瓙闆嗐€倀estChars鏄釜闄愬畾闆嗗悎锛屾鏌ュ瓧绗︿覆涓殑瀛楃鏄惁鍦ㄨ繖涓檺瀹氶泦鍚堜腑銆?br>
	 * <pre>{@code
	 *   StrUtil.containsOnly("asdas", 'a', 'd', 's','l');   --> true
	 * }</pre>
	 *
	 * @param str       瀛楃涓?
	 * @param testChars 妫€鏌ョ殑瀛楃
	 * @return 瀛楃涓插惈鏈夐潪妫€鏌ョ殑瀛楃锛岃繑鍥瀎alse
	 * @since 4.4.1
	 */
	public static boolean containsOnly(CharSequence str, char... testChars) {
		if (false == isEmpty(str)) {
			int len = str.length();
			for (int i = 0; i < len; i++) {
				if (false == ArrayUtil.contains(testChars, str.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 妫€鏌ユ寚瀹氬瓧绗︿覆涓槸鍚﹀惈缁欏畾鐨勬墍鏈夊瓧绗︿覆
	 *
	 * @param str       瀛楃涓?
	 * @param testChars 妫€鏌ョ殑瀛楃
	 * @return 瀛楃涓插惈鏈夐潪妫€鏌ョ殑瀛楃锛岃繑鍥瀎alse
	 * @since 4.4.1
	 */
	public static boolean containsAll(CharSequence str, CharSequence... testChars) {
		if (isBlank(str) || ArrayUtil.isEmpty(testChars)) {
			return false;
		}
		for (CharSequence testChar : testChars) {
			if (false == contains(str, testChar)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚﹀寘鍚┖鐧界锛堢┖鐧界鍖呮嫭绌烘牸銆佸埗琛ㄧ銆佸叏瑙掔┖鏍煎拰涓嶉棿鏂┖鏍硷級<br>
	 * 濡傛灉缁欏畾瀛楃涓蹭负null鎴栬€?"锛屽垯杩斿洖false
	 *
	 * @param str 瀛楃涓?
	 * @return 鏄惁鍖呭惈绌虹櫧绗?
	 * @since 4.0.8
	 */
	public static boolean containsBlank(CharSequence str) {
		if (null == str) {
			return false;
		}
		final int length = str.length();
		if (0 == length) {
			return false;
		}

		for (int i = 0; i < length; i += 1) {
			if (CharUtil.isBlankChar(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 鏌ユ壘鎸囧畾瀛楃涓叉槸鍚﹀寘鍚寚瀹氬瓧绗︿覆鍒楄〃涓殑浠绘剰涓€涓瓧绗︿覆锛屽鏋滃寘鍚繑鍥炴壘鍒扮殑绗竴涓瓧绗︿覆
	 *
	 * @param str      鎸囧畾瀛楃涓?
	 * @param testStrs 闇€瑕佹鏌ョ殑瀛楃涓叉暟缁?
	 * @return 琚寘鍚殑绗竴涓瓧绗︿覆
	 * @since 3.2.0
	 */
	public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence checkStr : testStrs) {
			if (null != checkStr && str.toString().contains(checkStr)) {
				return checkStr.toString();
			}
		}
		return null;
	}

	/**
	 * 鏄惁鍖呭惈鐗瑰畾瀛楃锛屽拷鐣ュぇ灏忓啓锛屽鏋滅粰瀹氫袱涓弬鏁伴兘涓簕@code null}锛岃繑鍥瀟rue
	 *
	 * @param str     琚娴嬪瓧绗︿覆
	 * @param testStr 琚祴璇曟槸鍚﹀寘鍚殑瀛楃涓?
	 * @return 鏄惁鍖呭惈
	 */
	public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		if (null == str) {
			// 濡傛灉琚洃娴嬪瓧绗︿覆鍜?
			return null == testStr;
		}
		return indexOfIgnoreCase(str, testStr) > -1;
	}

	/**
	 * 鏌ユ壘鎸囧畾瀛楃涓叉槸鍚﹀寘鍚寚瀹氬瓧绗︿覆鍒楄〃涓殑浠绘剰涓€涓瓧绗︿覆<br>
	 * 蹇界暐澶у皬鍐?
	 *
	 * @param str      鎸囧畾瀛楃涓?
	 * @param testStrs 闇€瑕佹鏌ョ殑瀛楃涓叉暟缁?
	 * @return 鏄惁鍖呭惈浠绘剰涓€涓瓧绗︿覆
	 * @since 3.2.0
	 */
	public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
		return null != getContainsStrIgnoreCase(str, testStrs);
	}

	/**
	 * 鏌ユ壘鎸囧畾瀛楃涓叉槸鍚﹀寘鍚寚瀹氬瓧绗︿覆鍒楄〃涓殑浠绘剰涓€涓瓧绗︿覆锛屽鏋滃寘鍚繑鍥炴壘鍒扮殑绗竴涓瓧绗︿覆<br>
	 * 蹇界暐澶у皬鍐?
	 *
	 * @param str      鎸囧畾瀛楃涓?
	 * @param testStrs 闇€瑕佹鏌ョ殑瀛楃涓叉暟缁?
	 * @return 琚寘鍚殑绗竴涓瓧绗︿覆
	 * @since 3.2.0
	 */
	public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------ indexOf

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵炬寚瀹氬瓧绗?
	 *
	 * @param str        瀛楃涓?
	 * @param searchChar 琚煡鎵剧殑瀛楃
	 * @return 浣嶇疆
	 */
	public static int indexOf(CharSequence str, char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵炬寚瀹氬瓧绗?
	 *
	 * @param str        瀛楃涓?
	 * @param searchChar 琚煡鎵剧殑瀛楃
	 * @param start      璧峰浣嶇疆锛屽鏋滃皬浜?锛屼粠0寮€濮嬫煡鎵?
	 * @return 浣嶇疆
	 */
	public static int indexOf(CharSequence str, char searchChar, int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵炬寚瀹氬瓧绗?
	 *
	 * @param text       瀛楃涓?
	 * @param searchChar 琚煡鎵剧殑瀛楃
	 * @param start      璧峰浣嶇疆锛屽鏋滃皬浜?锛屼粠0寮€濮嬫煡鎵?
	 * @param end        缁堟浣嶇疆锛屽鏋滆秴杩噑tr.length()鍒欓粯璁ゆ煡鎵惧埌瀛楃涓叉湯灏?
	 * @return 浣嶇疆
	 */
	public static int indexOf(CharSequence text, char searchChar, int start, int end) {
		if (isEmpty(text)) {
			return INDEX_NOT_FOUND;
		}
		if (text == null) return -1;
		final int len = text.length();
		final int validEnd = (end < 0 || end > len) ? len : end;
		for (int i = start; i < validEnd; i++) {
			if (text.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆锛屽拷鐣ュぇ灏忓啓<br>
	 *
	 * <pre>
	 * indexOfIgnoreCase(null, *, *)          = -1
	 * indexOfIgnoreCase(*, null, *)          = -1
	 * indexOfIgnoreCase("", "", 0)           = 0
	 * indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       瀛楃涓?
	 * @param searchStr 闇€瑕佹煡鎵句綅缃殑瀛楃涓?
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆
	 *
	 * <pre>
	 * indexOfIgnoreCase(null, *, *)          = -1
	 * indexOfIgnoreCase(*, null, *)          = -1
	 * indexOfIgnoreCase("", "", 0)           = 0
	 * indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       瀛楃涓?
	 * @param searchStr 闇€瑕佹煡鎵句綅缃殑瀛楃涓?
	 * @param fromIndex 璧峰浣嶇疆
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
		return indexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆
	 *
	 * @param text       瀛楃涓诧紝绌哄垯杩斿洖-1
	 * @param searchStr  闇€瑕佹煡鎵句綅缃殑瀛楃涓诧紝绌哄垯杩斿洖-1
	 * @param from       璧峰浣嶇疆锛堝寘鍚級
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int indexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		if (text == null || searchStr == null) return -1;
		final String textStr = text.toString();
		final String searchStr2 = searchStr.toString();
		if (ignoreCase) {
			return textStr.toLowerCase().indexOf(searchStr2.toLowerCase(), from);
		}
		return textStr.indexOf(searchStr2, from);
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆锛屽拷鐣ュぇ灏忓啓
	 *
	 * @param str       瀛楃涓?
	 * @param searchStr 闇€瑕佹煡鎵句綅缃殑瀛楃涓?
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆锛屽拷鐣ュぇ灏忓啓<br>
	 * fromIndex 涓烘悳绱㈣捣濮嬩綅缃紝浠庡悗寰€鍓嶈鏁?
	 *
	 * @param str       瀛楃涓?
	 * @param searchStr 闇€瑕佹煡鎵句綅缃殑瀛楃涓?
	 * @param fromIndex 璧峰浣嶇疆锛屼粠鍚庡線鍓嶈鏁?
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
		return lastIndexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 鎸囧畾鑼冨洿鍐呮煡鎵惧瓧绗︿覆<br>
	 * fromIndex 涓烘悳绱㈣捣濮嬩綅缃紝浠庡悗寰€鍓嶈鏁?
	 *
	 * @param text       瀛楃涓?
	 * @param searchStr  闇€瑕佹煡鎵句綅缃殑瀛楃涓?
	 * @param from       璧峰浣嶇疆锛屼粠鍚庡線鍓嶈鏁?
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 浣嶇疆
	 * @since 3.2.1
	 */
	public static int lastIndexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		if (text == null || searchStr == null) return -1;
		final String textStr = text.toString();
		final String searchStr2 = searchStr.toString();
		final int textLen = textStr.length();
		final int searchLen = searchStr2.length();
		if (searchLen == 0) return from;
		int maxIndex = textLen - searchLen;
		if (from > maxIndex) return -1;
		if (ignoreCase) {
			final String lowerText = textStr.toLowerCase();
			final String lowerSearch = searchStr2.toLowerCase();
			int idx = lowerText.lastIndexOf(lowerSearch, from);
			return idx;
		}
		return textStr.lastIndexOf(searchStr2, from);
	}

	/**
	 * 杩斿洖瀛楃涓?searchStr 鍦ㄥ瓧绗︿覆 str 涓 ordinal 娆″嚭鐜扮殑浣嶇疆銆?
	 *
	 * <p>
	 * 濡傛灉 str=null 鎴?searchStr=null 鎴?ordinal&le;0 鍒欒繑鍥?1<br>
	 * 姝ゆ柟娉曟潵鑷細Apache-Commons-Lang
	 * <p>
	 * 渚嬪瓙锛?浠ｈ〃浠绘剰瀛楃锛夛細
	 *
	 * <pre>
	 * ordinalIndexOf(null, *, *)          = -1
	 * ordinalIndexOf(*, null, *)          = -1
	 * ordinalIndexOf("", "", *)           = 0
	 * ordinalIndexOf("aabaabaa", "a", 1)  = 0
	 * ordinalIndexOf("aabaabaa", "a", 2)  = 1
	 * ordinalIndexOf("aabaabaa", "b", 1)  = 2
	 * ordinalIndexOf("aabaabaa", "b", 2)  = 5
	 * ordinalIndexOf("aabaabaa", "ab", 1) = 1
	 * ordinalIndexOf("aabaabaa", "ab", 2) = 4
	 * ordinalIndexOf("aabaabaa", "", 1)   = 0
	 * ordinalIndexOf("aabaabaa", "", 2)   = 0
	 * </pre>
	 *
	 * @param str       琚鏌ョ殑瀛楃涓诧紝鍙互涓簄ull
	 * @param searchStr 琚煡鎵剧殑瀛楃涓诧紝鍙互涓簄ull
	 * @param ordinal   绗嚑娆″嚭鐜扮殑浣嶇疆
	 * @return 鏌ユ壘鍒扮殑浣嶇疆
	 * @since 3.2.3
	 */
	public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return 0;
		}
		int found = 0;
		int index = INDEX_NOT_FOUND;
		do {
			index = indexOf(str, searchStr, index + 1, false);
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}

	// ------------------------------------------------------------------------ remove

	/**
	 * 绉婚櫎瀛楃涓蹭腑鎵€鏈夌粰瀹氬瓧绗︿覆<br>
	 * 渚嬶細removeAll("aa-bb-cc-dd", "-") =銆?aabbccdd
	 *
	 * @param str         瀛楃涓?
	 * @param strToRemove 琚Щ闄ょ殑瀛楃涓?
	 * @return 绉婚櫎鍚庣殑瀛楃涓?
	 */
	public static String removeAll(CharSequence str, CharSequence strToRemove) {
		// strToRemove濡傛灉涓虹┖锛?涔熶笉鐢ㄧ户缁悗闈㈢殑閫昏緫
		if (isEmpty(str) || isEmpty(strToRemove)) {
			return str(str);
		}
		return str.toString().replace(strToRemove, EMPTY);
	}

	/**
	 * 绉婚櫎瀛楃涓蹭腑鎵€鏈夌粰瀹氬瓧绗︿覆锛屽綋鏌愪釜瀛楃涓插嚭鐜板娆★紝鍒欏叏閮ㄧЩ闄?br>
	 * 渚嬶細removeAny("aa-bb-cc-dd", "a", "b") =銆?--cc-dd
	 *
	 * @param str          瀛楃涓?
	 * @param strsToRemove 琚Щ闄ょ殑瀛楃涓?
	 * @return 绉婚櫎鍚庣殑瀛楃涓?
	 * @since 5.3.8
	 */
	public static String removeAny(CharSequence str, CharSequence... strsToRemove) {
		String result = str(str);
		if (isNotEmpty(str)) {
			for (CharSequence strToRemove : strsToRemove) {
				result = removeAll(result, strToRemove);
			}
		}
		return result;
	}

	/**
	 * 鍘婚櫎瀛楃涓蹭腑鎸囧畾鐨勫涓瓧绗︼紝濡傛湁澶氫釜鍒欏叏閮ㄥ幓闄?
	 *
	 * @param str   瀛楃涓?
	 * @param chars 瀛楃鍒楄〃
	 * @return 鍘婚櫎鍚庣殑瀛楃
	 * @since 4.2.2
	 */
	public static String removeAll(CharSequence str, char... chars) {
		if (null == str || ArrayUtil.isEmpty(chars)) {
			return str(str);
		}
		final int len = str.length();
		if (0 == len) {
			return str(str);
		}
		final StringBuilder builder = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (false == ArrayUtil.contains(chars, c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * 鍘婚櫎鎵€鏈夋崲琛岀锛屽寘鎷細
	 *
	 * <pre>
	 * 1. \r
	 * 1. \n
	 * </pre>
	 *
	 * @param str 瀛楃涓?
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 4.2.2
	 */
	public static String removeAllLineBreaks(CharSequence str) {
		return removeAll(str, CharUtil.CR, CharUtil.LF);
	}

	/**
	 * 鍘绘帀棣栭儴鎸囧畾闀垮害鐨勫瓧绗︿覆骞跺皢鍓╀綑瀛楃涓查瀛楁瘝灏忓啓<br>
	 * 渚嬪锛歴tr=setName, preLength=3 =銆?return name
	 *
	 * @param str       琚鐞嗙殑瀛楃涓?
	 * @param preLength 鍘绘帀鐨勯暱搴?
	 * @return 澶勭悊鍚庣殑瀛楃涓诧紝涓嶇鍚堣鑼冭繑鍥瀗ull
	 */
	public static String removePreAndLowerFirst(CharSequence str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.toString().substring(preLength + 1);
			}
			return String.valueOf(first);
		} else {
			return str.toString();
		}
	}

	/**
	 * 鍘绘帀棣栭儴鎸囧畾闀垮害鐨勫瓧绗︿覆骞跺皢鍓╀綑瀛楃涓查瀛楁瘝灏忓啓<br>
	 * 渚嬪锛歴tr=setName, prefix=set =銆?return name
	 *
	 * @param str    琚鐞嗙殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @return 澶勭悊鍚庣殑瀛楃涓诧紝涓嶇鍚堣鑼冭繑鍥瀗ull
	 */
	public static String removePreAndLowerFirst(CharSequence str, CharSequence prefix) {
		return lowerFirst(removePrefix(str, prefix));
	}

	/**
	 * 鍘绘帀鎸囧畾鍓嶇紑
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @return 鍒囨帀鍚庣殑瀛楃涓诧紝鑻ュ墠缂€涓嶆槸 preffix锛?杩斿洖鍘熷瓧绗︿覆
	 */
	public static String removePrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return subSuf(str2, prefix.length());// 鎴彇鍚庡崐娈?
		}
		return str2;
	}

	/**
	 * 鍘绘帀鎸囧畾鎵€鏈夊墠缂€锛屽锛?
	 * <pre>{@code
	 *     str=abcdef, prefix=ab => return cdef
	 *     str=ababcdef, prefix=ab => return cdef
	 *     str=ababcdef, prefix="" => return ababcdef
	 *     str=ababcdef, prefix=null => return ababcdef
	 * }</pre>
	 *
	 * @param str    瀛楃涓诧紝绌鸿繑鍥炲師瀛楃涓?
	 * @param prefix 鍓嶇紑锛岀┖杩斿洖鍘熷瓧绗︿覆
	 * @return 鍘绘帀鎵€鏈夊墠缂€鐨勫瓧绗︿覆锛岃嫢鍓嶇紑涓嶆槸 preffix锛?杩斿洖鍘熷瓧绗︿覆
	 * @since 5.8.30
	 */
	public static String removeAllPrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String prefixStr = prefix.toString();
		final int prefixLength = prefixStr.length();

		final String str2 = str.toString();
		int toIndex = 0;
		while (str2.startsWith(prefixStr, toIndex)) {
			toIndex += prefixLength;
		}
		return subSuf(str2, toIndex);
	}

	/**
	 * 蹇界暐澶у皬鍐欏幓鎺夋寚瀹氬墠缂€
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @return 鍒囨帀鍚庣殑瀛楃涓诧紝鑻ュ墠缂€涓嶆槸 prefix锛?杩斿洖鍘熷瓧绗︿覆
	 */
	public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (startWithIgnoreCase(str, prefix)) {
			return subSuf(str2, prefix.length());// 鎴彇鍚庡崐娈?
		}
		return str2;
	}

	/**
	 * 鍘绘帀鎸囧畾鍚庣紑
	 *
	 * @param str    瀛楃涓?
	 * @param suffix 鍚庣紑
	 * @return 鍒囨帀鍚庣殑瀛楃涓诧紝鑻ュ悗缂€涓嶆槸 suffix锛?杩斿洖鍘熷瓧绗︿覆
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 鎴彇鍓嶅崐娈?
		}
		return str2;
	}

	/**
	 * 鍘绘帀鎸囧畾鎵€鏈夊悗缂€锛屽锛?
	 * <pre>{@code
	 *     str=11abab, suffix=ab => return 11
	 *     str=11ab, suffix=ab => return 11
	 *     str=11ab, suffix="" => return 11ab
	 *     str=11ab, suffix=null => return 11ab
	 * }</pre>
	 *
	 * @param str    瀛楃涓诧紝绌鸿繑鍥炲師瀛楃涓?
	 * @param suffix 鍚庣紑瀛楃涓诧紝绌鸿繑鍥炲師瀛楃涓?
	 * @return 鍘绘帀鎵€鏈夊悗缂€鐨勫瓧绗︿覆锛岃嫢鍚庣紑涓嶆槸 suffix锛?杩斿洖鍘熷瓧绗︿覆
	 * @since 5.8.30
	 */
	public static String removeAllSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String suffixStr = suffix.toString();
		final int suffixLength = suffixStr.length();

		final String str2 = str.toString();
		int toIndex = str2.length();
		while (str2.startsWith(suffixStr, toIndex - suffixLength)) {
			toIndex -= suffixLength;
		}
		return subPre(str2, toIndex);
	}


	/**
	 * 鍘绘帀鎸囧畾鍚庣紑锛屽苟灏忓啓棣栧瓧姣?
	 *
	 * @param str    瀛楃涓?
	 * @param suffix 鍚庣紑
	 * @return 鍒囨帀鍚庣殑瀛楃涓诧紝鑻ュ悗缂€涓嶆槸 suffix锛?杩斿洖鍘熷瓧绗︿覆
	 */
	public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
		return lowerFirst(removeSuffix(str, suffix));
	}

	/**
	 * 蹇界暐澶у皬鍐欏幓鎺夋寚瀹氬悗缂€
	 *
	 * @param str    瀛楃涓?
	 * @param suffix 鍚庣紑
	 * @return 鍒囨帀鍚庣殑瀛楃涓诧紝鑻ュ悗缂€涓嶆槸 suffix锛?杩斿洖鍘熷瓧绗︿覆
	 */
	public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (endWithIgnoreCase(str, suffix)) {
			return subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * 娓呯悊绌虹櫧瀛楃
	 *
	 * @param str 琚竻鐞嗙殑瀛楃涓?
	 * @return 娓呯悊鍚庣殑瀛楃涓?
	 */
	public static String cleanBlank(CharSequence str) {
		return filter(str, c -> false == CharUtil.isBlankChar(c));
	}

	// ------------------------------------------------------------------------ strip

	/**
	 * 鍘婚櫎涓よ竟鐨勬寚瀹氬瓧绗︿覆锛屽拷鐣ュぇ灏忓啓
	 *
	 * @param str            琚鐞嗙殑瀛楃涓?
	 * @param prefixOrSuffix 鍓嶇紑鎴栧悗缂€
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(final CharSequence str, final CharSequence prefixOrSuffix) {
		return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 鍘婚櫎涓よ竟鐨勬寚瀹氬瓧绗︿覆锛屽拷鐣ュぇ灏忓啓
	 *
	 * @param str    琚鐞嗙殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		return strip(str, prefix, suffix, true);
	}

	/**
	 * 鍘婚櫎涓よ竟鐨勬寚瀹氬瓧绗︿覆
	 *
	 * @param str            琚鐞嗙殑瀛楃涓?
	 * @param prefixOrSuffix 鍓嶇紑鎴栧悗缂€
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String strip(CharSequence str, CharSequence prefixOrSuffix) {
		if (equals(str, prefixOrSuffix)) {
			// 瀵逛簬鍘婚櫎鐩稿悓瀛楃鐨勬儏鍐靛崟鐙鐞?
			return EMPTY;
		}
		return strip(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 鍘婚櫎涓よ竟鐨勬寚瀹氬瓧绗︿覆<br>
	 * 涓よ竟瀛楃濡傛灉瀛樺湪锛屽垯鍘婚櫎锛屼笉瀛樺湪涓嶅仛澶勭悊
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "aa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, null  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", ""  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", "a", ""  -> "aa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", null  -> "aa_STRIPPED_bbb"
	 *
	 * "a", "a", "a"  -> ""
	 * }
	 * </pre>
	 *
	 * @param str    琚鐞嗙殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix) {
		return strip(str, prefix, suffix, false);
	}

	/**
	 * 鍘婚櫎涓よ竟鐨勬寚瀹氬瓧绗︿覆<br>
	 * 涓よ竟瀛楃濡傛灉瀛樺湪锛屽垯鍘婚櫎锛屼笉瀛樺湪涓嶅仛澶勭悊
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "aa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, null  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", ""  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", "a", ""  -> "aa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", null  -> "aa_STRIPPED_bbb"
	 *
	 * "a", "a", "a"  -> ""
	 * }
	 * </pre>
	 *
	 * @param str        琚鐞嗙殑瀛楃涓?
	 * @param prefix     鍓嶇紑
	 * @param suffix     鍚庣紑
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix, boolean ignoreCase) {
		if (isEmpty(str)) {
			return str(str);
		}

		final String str2 = str.toString();
		int from = 0;
		int to = str2.length();

		if (startWith(str2, prefix, ignoreCase)) {
			from = prefix.length();
			if (from == to) {
				// "a", "a", "a"  -> ""
				return EMPTY;
			}
		}
		if (endWith(str2, suffix, ignoreCase)) {
			to -= suffix.length();
			if (from == to) {
				// "a", "a", "a"  -> ""
				return EMPTY;
			} else if (to < from) {
				// pre鍘婚櫎鍚庡拰suffix鏈夐噸鍙狅紝濡?("aba", "ab", "ba") -> "a"
				to += suffix.length();
			}
		}

		return str2.substring(from, to);
	}

	/**
	 * 鍘婚櫎涓よ竟<u><b>鎵€鏈?/b></u>鐨勬寚瀹氬瓧绗︿覆
	 *
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a"  -> "_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "_STRIPPED_"
	 * "aaa_STRIPPED_bbb", ""  -> "aaa_STRIPPED_bbb"
	 * }
	 * </pre>
	 *
	 * @param str            琚鐞嗙殑瀛楃涓?
	 * @param prefixOrSuffix 鍓嶇紑鎴栧悗缂€
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 5.8.30
	 */
	public static String stripAll(final CharSequence str, final CharSequence prefixOrSuffix) {
		if (equals(str, prefixOrSuffix)) {
			return EMPTY;
		}
		return stripAll(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 鍘婚櫎涓よ竟<u><b>鎵€鏈?/b></u>鐨勬寚瀹氬瓧绗︿覆
	 *
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "_STRIPPED_"
	 * "aaa_STRIPPED_bbb", null, null  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", ""  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", "b"  -> "aaa_STRIPPED_"
	 * "aaa_STRIPPED_bbb", null, "b"  -> "aaa_STRIPPED_"
	 * "aaa_STRIPPED_bbb", "a", ""  -> "_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", null  -> "_STRIPPED_bbb"
	 *
	 * // special test
	 * "aaaaaabbb", "aaa", null  -> "bbb"
	 * "aaaaaaabbb", "aa", null  -> "abbb"
	 *
	 * "aaaaaaaaa", "aaa", "aa"  -> ""
	 * "a", "a", "a"  -> ""
	 * }
	 * </pre>
	 *
	 * @param str    琚鐞嗙殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 * @since 5.8.30
	 */
	public static String stripAll(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		if (isEmpty(str)) {
			return str(str);
		}

		final String prefixStr = emptyIfNull(prefix);
		final String suffixStr = emptyIfNull(suffix);

		final String str2 = str.toString();
		int from = 0;
		int to = str2.length();

		if (!prefixStr.isEmpty()) {
			while (str2.startsWith(prefixStr, from)) {
				from += prefix.length();
				if (from == to) {
					// "a", "a", "a"  -> ""
					return EMPTY;
				}
			}
		}
		if (!suffixStr.isEmpty()) {
			while (str2.startsWith(suffixStr, to - suffixStr.length())) {
				to -= suffixStr.length();
				if (from == to) {
					// "a", "a", "a"  -> ""
					return EMPTY;
				} else if (to < from) {
					// pre鍘婚櫎鍚庡拰suffix鏈夐噸鍙狅紝濡?("aba", "ab", "ba") -> "a"
					to += suffixStr.length();
					break;
				}
			}
		}

		return str2.substring(from, to);
	}

	// ------------------------------------------------------------------------ add

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互prefix寮€澶寸殑锛屽湪寮€澶磋ˉ鍏?prefix
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @see #prependIfMissing(CharSequence, CharSequence, CharSequence...)
	 */
	public static String addPrefixIfNot(CharSequence str, CharSequence prefix) {
		return prependIfMissing(str, prefix, prefix);
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互suffix缁撳熬鐨勶紝鍦ㄥ熬閮ㄨˉ鍏?suffix
	 *
	 * @param str    瀛楃涓?
	 * @param suffix 鍚庣紑
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @see #appendIfMissing(CharSequence, CharSequence, CharSequence...)
	 */
	public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
		return appendIfMissing(str, suffix, suffix);
	}

	// ------------------------------------------------------------------------ split

	/**
	 * 鍒囧垎瀛楃涓蹭负long鏁扮粍
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗?
	 * @return 鍒囧垎鍚巐ong鏁扮粍
	 * @since 4.0.6
	 */
	public static long[] splitToLong(CharSequence str, char separator) {
		return Convert.convert(long[].class, splitTrim(str, separator));
	}

	/**
	 * 鍒囧垎瀛楃涓蹭负long鏁扮粍
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗︿覆
	 * @return 鍒囧垎鍚巐ong鏁扮粍
	 * @since 4.0.6
	 */
	public static long[] splitToLong(CharSequence str, CharSequence separator) {
		return Convert.convert(long[].class, splitTrim(str, separator));
	}

	/**
	 * 鍒囧垎瀛楃涓蹭负int鏁扮粍
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗?
	 * @return 鍒囧垎鍚巐ong鏁扮粍
	 * @since 4.0.6
	 */
	public static int[] splitToInt(CharSequence str, char separator) {
		return Convert.convert(int[].class, splitTrim(str, separator));
	}

	/**
	 * 鍒囧垎瀛楃涓蹭负int鏁扮粍
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗︿覆
	 * @return 鍒囧垎鍚巐ong鏁扮粍
	 * @since 4.0.6
	 */
	public static int[] splitToInt(CharSequence str, CharSequence separator) {
		return Convert.convert(int[].class, splitTrim(str, separator));
	}

	/**
	 * 鍒囧垎瀛楃涓?br>
	 * a#b#c =銆?[a,b,c] <br>
	 * a##b#c =銆?[a,"",b,c]
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 */
	public static List<String> split(CharSequence str, char separator) {
		return split(str, separator, 0);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝濡傛灉鍒嗛殧绗︿笉瀛樺湪鍒欒繑鍥炲師瀛楃涓?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗?
	 * @return 瀛楃涓?
	 * @since 5.6.7
	 */
	public static String[] splitToArray(CharSequence str, CharSequence separator) {
		if (str == null) {
			return new String[]{};
		}

		final String sepStr = str(separator);
		if (sepStr == null || sepStr.isEmpty()) {
			return new String[]{str.toString()};
		}
		final List<String> list = new java.util.ArrayList<>();
		int start = 0;
		int idx;
		while ((idx = str.toString().indexOf(sepStr, start)) != -1) {
			list.add(str.toString().substring(start, idx));
			start = idx + sepStr.length();
		}
		list.add(str.toString().substring(start));
		return list.toArray(new String[0]);
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @return 鍒囧垎鍚庣殑鏁扮粍
	 */
	public static String[] splitToArray(CharSequence str, char separator) {
		return splitToArray(str, separator, 0);
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param text      琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @param limit     闄愬埗鍒嗙墖鏁?
	 * @return 鍒囧垎鍚庣殑鏁扮粍
	 */
	public static String[] splitToArray(CharSequence text, char separator, int limit) {
		Assert.notNull(text, "Text must be not null!");
		final String textStr = text.toString();
		final String sepStr = String.valueOf(separator);
		if (limit == 0) {
			return textStr.split(java.util.regex.Pattern.quote(sepStr));
		}
		return textStr.split(java.util.regex.Pattern.quote(sepStr), limit);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝涓嶅幓闄ゅ垏鍒嗗悗姣忎釜鍏冪礌涓よ竟鐨勭┖鐧界锛屼笉鍘婚櫎绌虹櫧椤?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @param limit     闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 */
	public static List<String> split(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, false, false);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝鍘婚櫎鍒囧垎鍚庢瘡涓厓绱犱袱杈圭殑绌虹櫧绗︼紝鍘婚櫎绌虹櫧椤?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.1.2
	 */
	public static List<String> splitTrim(CharSequence str, char separator) {
		return splitTrim(str, separator, -1);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝鍘婚櫎鍒囧垎鍚庢瘡涓厓绱犱袱杈圭殑绌虹櫧绗︼紝鍘婚櫎绌虹櫧椤?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.2.0
	 */
	public static List<String> splitTrim(CharSequence str, CharSequence separator) {
		return splitTrim(str, separator, -1);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝鍘婚櫎鍒囧垎鍚庢瘡涓厓绱犱袱杈圭殑绌虹櫧绗︼紝鍘婚櫎绌虹櫧椤?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @param limit     闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.1.0
	 */
	public static List<String> splitTrim(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, true, true);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝鍘婚櫎鍒囧垎鍚庢瘡涓厓绱犱袱杈圭殑绌虹櫧绗︼紝鍘婚櫎绌虹櫧椤?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗﹀瓧绗?
	 * @param limit     闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.2.0
	 */
	public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
		return split(str, separator, limit, true, true);
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝涓嶉檺鍒跺垎鐗囨暟閲?
	 *
	 * @param str         琚垏鍒嗙殑瀛楃涓?
	 * @param separator   鍒嗛殧绗﹀瓧绗?
	 * @param isTrim      鏄惁鍘婚櫎鍒囧垎瀛楃涓插悗姣忎釜鍏冪礌涓よ竟鐨勭┖鏍?
	 * @param ignoreEmpty 鏄惁蹇界暐绌轰覆
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.0.8
	 */
	public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param str         琚垏鍒嗙殑瀛楃涓?
	 * @param separator   鍒嗛殧绗﹀瓧绗?
	 * @param limit       闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @param isTrim      鏄惁鍘婚櫎鍒囧垎瀛楃涓插悗姣忎釜鍏冪礌涓よ竟鐨勭┖鏍?
	 * @param ignoreEmpty 鏄惁蹇界暐绌轰覆
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.0.8
	 */
	public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		final String str2 = str.toString();
		final String sepStr = String.valueOf(separator);
		final java.util.List<String> result = new java.util.ArrayList<>();
		if (str2.isEmpty()) {
			if (!ignoreEmpty) result.add(str2);
			return result;
		}
		int start = 0;
		int idx;
		while ((idx = str2.indexOf(sepStr, start)) != -1) {
			String token = str2.substring(start, idx);
			if (isTrim) token = token.trim();
			if (!ignoreEmpty || !token.isEmpty()) result.add(token);
			start = idx + sepStr.length();
		}
		String last = str2.substring(start);
		if (isTrim) last = last.trim();
		if (!ignoreEmpty || !last.isEmpty()) result.add(last);
		if (limit > 0 && result.size() > limit) {
			final java.util.List<String> limited = new java.util.ArrayList<>(result.subList(0, limit - 1));
			final StringBuilder sb = new StringBuilder();
			for (int i = limit - 1; i < result.size(); i++) {
				if (sb.length() > 0) sb.append(sepStr);
				sb.append(result.get(i));
			}
			limited.add(sb.toString());
			return limited;
		}
		return result;
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param <R>         鍒囧垎鍚庡厓绱犵被鍨?
	 * @param str         琚垏鍒嗙殑瀛楃涓?
	 * @param separator   鍒嗛殧绗﹀瓧绗?
	 * @param limit       闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @param ignoreEmpty 鏄惁蹇界暐绌轰覆
	 * @param mapping     鍒囧垎鍚庣殑瀛楃涓插厓绱犵殑杞崲鏂规硶
	 * @return 鍒囧垎鍚庣殑闆嗗悎锛屽厓绱犵被鍨嬫槸缁忚繃 mapping 杞崲鍚庣殑
	 * @since 5.7.14
	 */
	public static <R> List<R> split(CharSequence str, char separator, int limit, boolean ignoreEmpty, Function<String, R> mapping) {
		final java.util.List<String> rawSplit = split(str, separator, limit, false, ignoreEmpty);
		final java.util.List<R> result = new java.util.ArrayList<>(rawSplit.size());
		for (String s : rawSplit) {
			result.add(mapping.apply(s));
		}
		return result;
	}

	/**
	 * 鍒囧垎瀛楃涓诧紝濡傛灉鍒嗛殧绗︿笉瀛樺湪鍒欒繑鍥炲師瀛楃涓?
	 *
	 * @param str       琚垏鍒嗙殑瀛楃涓?
	 * @param separator 鍒嗛殧绗?
	 * @return 瀛楃涓?
	 * @since 5.7.1
	 */
	public static List<String> split(CharSequence str, CharSequence separator) {
		return split(str, separator, false, false);
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param str         琚垏鍒嗙殑瀛楃涓?
	 * @param separator   鍒嗛殧绗﹀瓧绗?
	 * @param isTrim      鏄惁鍘婚櫎鍒囧垎瀛楃涓插悗姣忎釜鍏冪礌涓よ竟鐨勭┖鏍?
	 * @param ignoreEmpty 鏄惁蹇界暐绌轰覆
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 5.6.7
	 */
	public static List<String> split(CharSequence str, CharSequence separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 鍒囧垎瀛楃涓?
	 *
	 * @param str         琚垏鍒嗙殑瀛楃涓?
	 * @param separator   鍒嗛殧绗﹀瓧绗?
	 * @param limit       闄愬埗鍒嗙墖鏁帮紝-1涓嶉檺鍒?
	 * @param isTrim      鏄惁鍘婚櫎鍒囧垎瀛楃涓插悗姣忎釜鍏冪礌涓よ竟鐨勭┖鏍?
	 * @param ignoreEmpty 鏄惁蹇界暐绌轰覆
	 * @return 鍒囧垎鍚庣殑闆嗗悎
	 * @since 3.2.0
	 */
	public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		final String separatorStr = (null == separator) ? null : separator.toString();
		final String str2 = str.toString();
		final java.util.List<String> result = new java.util.ArrayList<>();
		if (str2.isEmpty()) {
			if (!ignoreEmpty) result.add(str2);
			return result;
		}
		if (separatorStr == null || separatorStr.isEmpty()) {
			if (!ignoreEmpty || !str2.isEmpty()) result.add(str2);
			return result;
		}
		int start = 0;
		int idx;
		while ((idx = str2.indexOf(separatorStr, start)) != -1) {
			String token = str2.substring(start, idx);
			if (isTrim) token = token.trim();
			if (!ignoreEmpty || !token.isEmpty()) result.add(token);
			start = idx + separatorStr.length();
		}
		String last = str2.substring(start);
		if (isTrim) last = last.trim();
		if (!ignoreEmpty || !last.isEmpty()) result.add(last);
		if (limit > 0 && result.size() > limit) {
			final java.util.List<String> limited = new java.util.ArrayList<>(result.subList(0, limit - 1));
			final StringBuilder sb = new StringBuilder();
			for (int i = limit - 1; i < result.size(); i++) {
				if (sb.length() > 0) sb.append(separatorStr);
				sb.append(result.get(i));
			}
			limited.add(sb.toString());
			return limited;
		}
		return result;
	}

	/**
	 * 鏍规嵁缁欏畾闀垮害锛屽皢缁欏畾瀛楃涓叉埅鍙栦负澶氫釜閮ㄥ垎
	 *
	 * @param str 瀛楃涓?
	 * @param len 姣忎竴涓皬鑺傜殑闀垮害
	 * @return 鎴彇鍚庣殑瀛楃涓叉暟缁?
	 * @see StrSplitter#splitByLength(CharSequence, int)
	 */
	public static String[] split(CharSequence str, int len) {
		if (str == null) {
			return new String[0];
		}
		final String s = str.toString();
		final int len2 = s.length();
		if (len2 <= len) {
			return new String[]{s};
		}
		final int count = (len2 + len - 1) / len;
		final String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			int start2 = i * len;
			int end2 = Math.min(start2 + len, len2);
			result[i] = s.substring(start2, end2);
		}
		return result;
	}

	/**
	 * 灏嗗瓧绗︿覆鍒囧垎涓篘绛変唤
	 *
	 * @param str        瀛楃涓?
	 * @param partLength 姣忕瓑浠界殑闀垮害
	 * @return 鍒囧垎鍚庣殑鏁扮粍
	 * @since 3.0.6
	 */
	public static String[] cut(CharSequence str, int partLength) {
		if (null == str) {
			return null;
		}
		int len = str.length();
		if (len < partLength) {
			return new String[]{str.toString()};
		}
		int part = NumberUtil.count(len, partLength);
		final String[] array = new String[part];

		final String str2 = str.toString();
		for (int i = 0; i < part; i++) {
			array[i] = str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
		}
		return array;
	}

	// ------------------------------------------------------------------------ sub

	/**
	 * 鏀硅繘JDK subString<br>
	 * index浠?寮€濮嬭绠楋紝鏈€鍚庝竴涓瓧绗︿负-1<br>
	 * 濡傛灉from鍜宼o浣嶇疆涓€鏍凤紝杩斿洖 "" <br>
	 * 濡傛灉from鎴杢o涓鸿礋鏁帮紝鍒欐寜鐓ength浠庡悗鍚戝墠鏁颁綅缃紝濡傛灉缁濆鍊煎ぇ浜庡瓧绗︿覆闀垮害锛屽垯from褰掑埌0锛宼o褰掑埌length<br>
	 * 濡傛灉缁忚繃淇鐨刬ndex涓璮rom澶т簬to锛屽垯浜掓崲from鍜宼o example: <br>
	 * abcdefgh 2 3 =銆?c <br>
	 * abcdefgh 2 -3 =銆?cde <br>
	 *
	 * @param str              String
	 * @param fromIndexInclude 寮€濮嬬殑index锛堝寘鎷級
	 * @param toIndexExclude   缁撴潫鐨刬ndex锛堜笉鍖呮嫭锛?
	 * @return 瀛椾覆
	 */
	public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
		if (isEmpty(str)) {
			return str(str);
		}
		int len = str.length();

		if (fromIndexInclude < 0) {
			fromIndexInclude = len + fromIndexInclude;
			if (fromIndexInclude < 0) {
				fromIndexInclude = 0;
			}
		} else if (fromIndexInclude > len) {
			fromIndexInclude = len;
		}

		if (toIndexExclude < 0) {
			toIndexExclude = len + toIndexExclude;
			if (toIndexExclude < 0) {
				toIndexExclude = len;
			}
		} else if (toIndexExclude > len) {
			toIndexExclude = len;
		}

		if (toIndexExclude < fromIndexInclude) {
			int tmp = fromIndexInclude;
			fromIndexInclude = toIndexExclude;
			toIndexExclude = tmp;
		}

		if (fromIndexInclude == toIndexExclude) {
			return EMPTY;
		}

		return str.toString().substring(fromIndexInclude, toIndexExclude);
	}

	/**
	 * 閫氳繃CodePoint鎴彇瀛楃涓诧紝鍙互鎴柇Emoji
	 *
	 * @param str       String
	 * @param fromIndex 寮€濮嬬殑index锛堝寘鎷級
	 * @param toIndex   缁撴潫鐨刬ndex锛堜笉鍖呮嫭锛?
	 * @return 瀛椾覆
	 */
	public static String subByCodePoint(CharSequence str, int fromIndex, int toIndex) {
		if (isEmpty(str)) {
			return str(str);
		}

		if (fromIndex < 0 || fromIndex > toIndex) {
			throw new IllegalArgumentException();
		}

		if (fromIndex == toIndex) {
			return EMPTY;
		}

		final StringBuilder sb = new StringBuilder();
		final int subLen = toIndex - fromIndex;
		str.toString().codePoints().skip(fromIndex).limit(subLen).forEach(v -> sb.append(Character.toChars(v)));
		return sb.toString();
	}

	/**
	 * 鎴彇閮ㄥ垎瀛楃涓诧紝杩欓噷涓€涓眽瀛楃殑闀垮害璁や负鏄?
	 *
	 * @param str    瀛楃涓?
	 * @param len    bytes鍒囧壊鍒扮殑浣嶇疆锛堝寘鍚級
	 * @param suffix 鍒囧壊鍚庡姞涓婂悗缂€
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 3.1.1
	 */
	public static String subPreGbk(CharSequence str, int len, CharSequence suffix) {
		return subPreGbk(str, len, true) + suffix;
	}

	/**
	 * 鎴彇閮ㄥ垎瀛楃涓诧紝杩欓噷涓€涓眽瀛楃殑闀垮害璁や负鏄?<br>
	 * 鍙互鑷畾涔塰alfUp锛屽len涓?0锛屽鏋滄埅鍙栧悗鏈€鍚庝竴涓瓧绗︽槸鍗婁釜瀛楃锛寋@code true}琛ㄧず淇濈暀锛屽垯闀垮害鏄?1锛屽惁鍒欓暱搴?
	 *
	 * @param str    瀛楃涓?
	 * @param len    bytes鍒囧壊鍒扮殑浣嶇疆锛堝寘鍚級
	 * @param halfUp 閬囧埌鎴彇涓€鍗婄殑GBK瀛楃锛屾槸鍚︿繚鐣欍€?
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 5.7.17
	 */
	public static String subPreGbk(CharSequence str, int len, boolean halfUp) {
		if (isEmpty(str)) {
			return str(str);
		}

		int counterOfDoubleByte = 0;
		final byte[] b = bytes(str, CharsetUtil.CHARSET_GBK);
		if (b.length <= len) {
			return str.toString();
		}
		for (int i = 0; i < len; i++) {
			if (b[i] < 0) {
				counterOfDoubleByte++;
			}
		}

		if (counterOfDoubleByte % 2 != 0) {
			if (halfUp) {
				len += 1;
			} else {
				len -= 1;
			}
		}
		return new String(b, 0, len, CharsetUtil.CHARSET_GBK);
	}

	/**
	 * 鍒囧壊鎸囧畾浣嶇疆涔嬪墠閮ㄥ垎鐨勫瓧绗︿覆
	 *
	 * @param string         瀛楃涓?
	 * @param toIndexExclude 鍒囧壊鍒扮殑浣嶇疆锛堜笉鍖呮嫭锛?
	 * @return 鍒囧壊鍚庣殑鍓╀綑鐨勫墠鍗婇儴鍒嗗瓧绗︿覆
	 */
	public static String subPre(CharSequence string, int toIndexExclude) {
		return sub(string, 0, toIndexExclude);
	}

	/**
	 * 鍒囧壊鎸囧畾浣嶇疆涔嬪悗閮ㄥ垎鐨勫瓧绗︿覆
	 *
	 * @param string    瀛楃涓?
	 * @param fromIndex 鍒囧壊寮€濮嬬殑浣嶇疆锛堝寘鎷級
	 * @return 鍒囧壊鍚庡悗鍓╀綑鐨勫悗鍗婇儴鍒嗗瓧绗︿覆
	 */
	public static String subSuf(CharSequence string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 鍒囧壊鎸囧畾闀垮害鐨勫悗閮ㄥ垎鐨勫瓧绗︿覆
	 *
	 * <pre>
	 * subSufByLength("abcde", 3)      =    "cde"
	 * subSufByLength("abcde", 0)      =    ""
	 * subSufByLength("abcde", -5)     =    ""
	 * subSufByLength("abcde", -1)     =    ""
	 * subSufByLength("abcde", 5)       =    "abcde"
	 * subSufByLength("abcde", 10)     =    "abcde"
	 * subSufByLength(null, 3)               =    null
	 * </pre>
	 *
	 * @param string 瀛楃涓?
	 * @param length 鍒囧壊闀垮害
	 * @return 鍒囧壊鍚庡悗鍓╀綑鐨勫悗鍗婇儴鍒嗗瓧绗︿覆
	 * @since 4.0.1
	 */
	public static String subSufByLength(CharSequence string, int length) {
		if (isEmpty(string)) {
			return null;
		}
		if (length <= 0) {
			return EMPTY;
		}
		return sub(string, -length, string.length());
	}

	/**
	 * 鎴彇瀛楃涓?浠庢寚瀹氫綅缃紑濮?鎴彇鎸囧畾闀垮害鐨勫瓧绗︿覆<br>
	 * 濡傛灉fromIndex涓烘鏁帮紝鍒欏悜鍚庢埅鍙栨寚瀹歭ength闀垮害锛屽鏋滀负璐熸暟锛屽垯鍚戝墠鎴彇length闀垮害銆?
	 *
	 * @param input     鍘熷瀛楃涓?
	 * @param fromIndex 寮€濮嬬殑index,鍖呮嫭
	 * @param length    瑕佹埅鍙栫殑闀垮害
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 * @author weibaohui
	 */
	public static String subWithLength(String input, int fromIndex, int length) {
		final int toIndex;
		if (fromIndex < 0) {
			toIndex = fromIndex - length;
		} else {
			toIndex = fromIndex + length;
		}
		return sub(input, fromIndex, toIndex);
	}

	/**
	 * 鎴彇鍒嗛殧瀛楃涓蹭箣鍓嶇殑瀛楃涓诧紝涓嶅寘鎷垎闅斿瓧绗︿覆<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆涓虹┖涓诧紙null鎴?"锛夋垨鑰呭垎闅斿瓧绗︿覆涓簄ull锛岃繑鍥炲師瀛楃涓?br>
	 * 濡傛灉鍒嗛殧瀛楃涓蹭负绌轰覆""锛屽垯杩斿洖绌轰覆锛屽鏋滃垎闅斿瓧绗︿覆鏈壘鍒帮紝杩斿洖鍘熷瓧绗︿覆锛屼妇渚嬪涓嬶細
	 *
	 * <pre>
	 * subBefore(null, *, false)      = null
	 * subBefore("", *, false)        = ""
	 * subBefore("abc", "a", false)   = ""
	 * subBefore("abcba", "b", false) = "a"
	 * subBefore("abc", "c", false)   = "ab"
	 * subBefore("abc", "d", false)   = "abc"
	 * subBefore("abc", "", false)    = ""
	 * subBefore("abc", null, false)  = "abc"
	 * </pre>
	 *
	 * @param string          琚煡鎵剧殑瀛楃涓?
	 * @param separator       鍒嗛殧瀛楃涓诧紙涓嶅寘鎷級
	 * @param isLastSeparator 鏄惁鏌ユ壘鏈€鍚庝竴涓垎闅斿瓧绗︿覆锛堝娆″嚭鐜板垎闅斿瓧绗︿覆鏃堕€夊彇鏈€鍚庝竴涓級锛宼rue涓洪€夊彇鏈€鍚庝竴涓?
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 3.1.1
	 */
	public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string) || separator == null) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final String sep = separator.toString();
		if (sep.isEmpty()) {
			return EMPTY;
		}
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 鎴彇鍒嗛殧瀛楃涓蹭箣鍓嶇殑瀛楃涓诧紝涓嶅寘鎷垎闅斿瓧绗︿覆<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆涓虹┖涓诧紙null鎴?"锛夋垨鑰呭垎闅斿瓧绗︿覆涓簄ull锛岃繑鍥炲師瀛楃涓?br>
	 * 濡傛灉鍒嗛殧瀛楃涓叉湭鎵惧埌锛岃繑鍥炲師瀛楃涓诧紝涓句緥濡備笅锛?
	 *
	 * <pre>
	 * subBefore(null, *, false)      = null
	 * subBefore("", *, false)        = ""
	 * subBefore("abc", 'a', false)   = ""
	 * subBefore("abcba", 'b', false) = "a"
	 * subBefore("abc", 'c', false)   = "ab"
	 * subBefore("abc", 'd', false)   = "abc"
	 * </pre>
	 *
	 * @param string          琚煡鎵剧殑瀛楃涓?
	 * @param separator       鍒嗛殧瀛楃涓诧紙涓嶅寘鎷級
	 * @param isLastSeparator 鏄惁鏌ユ壘鏈€鍚庝竴涓垎闅斿瓧绗︿覆锛堝娆″嚭鐜板垎闅斿瓧绗︿覆鏃堕€夊彇鏈€鍚庝竴涓級锛宼rue涓洪€夊彇鏈€鍚庝竴涓?
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 4.1.15
	 */
	public static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}

		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 鎴彇鍒嗛殧瀛楃涓蹭箣鍚庣殑瀛楃涓诧紝涓嶅寘鎷垎闅斿瓧绗︿覆<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆涓虹┖涓诧紙null鎴?"锛夛紝杩斿洖鍘熷瓧绗︿覆<br>
	 * 濡傛灉鍒嗛殧瀛楃涓蹭负绌轰覆锛坣ull鎴?"锛夛紝鍒欒繑鍥炵┖涓诧紝濡傛灉鍒嗛殧瀛楃涓叉湭鎵惧埌锛岃繑鍥炵┖涓诧紝涓句緥濡備笅锛?
	 *
	 * <pre>
	 * subAfter(null, *, false)      = null
	 * subAfter("", *, false)        = ""
	 * subAfter(*, null, false)      = ""
	 * subAfter("abc", "a", false)   = "bc"
	 * subAfter("abcba", "b", false) = "cba"
	 * subAfter("abc", "c", false)   = ""
	 * subAfter("abc", "d", false)   = ""
	 * subAfter("abc", "", false)    = "abc"
	 * </pre>
	 *
	 * @param string          琚煡鎵剧殑瀛楃涓?
	 * @param separator       鍒嗛殧瀛楃涓诧紙涓嶅寘鎷級
	 * @param isLastSeparator 鏄惁鏌ユ壘鏈€鍚庝竴涓垎闅斿瓧绗︿覆锛堝娆″嚭鐜板垎闅斿瓧绗︿覆鏃堕€夊彇鏈€鍚庝竴涓級锛宼rue涓洪€夊彇鏈€鍚庝竴涓?
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 3.1.1
	 */
	public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		if (separator == null) {
			return EMPTY;
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 鎴彇鍒嗛殧瀛楃涓蹭箣鍚庣殑瀛楃涓诧紝涓嶅寘鎷垎闅斿瓧绗︿覆<br>
	 * 濡傛灉缁欏畾鐨勫瓧绗︿覆涓虹┖涓诧紙null鎴?"锛夛紝杩斿洖鍘熷瓧绗︿覆<br>
	 * 濡傛灉鍒嗛殧瀛楃涓蹭负绌轰覆锛坣ull鎴?"锛夛紝鍒欒繑鍥炵┖涓诧紝濡傛灉鍒嗛殧瀛楃涓叉湭鎵惧埌锛岃繑鍥炵┖涓诧紝涓句緥濡備笅锛?
	 *
	 * <pre>
	 * subAfter(null, *, false)      = null
	 * subAfter("", *, false)        = ""
	 * subAfter("abc", 'a', false)   = "bc"
	 * subAfter("abcba", 'b', false) = "cba"
	 * subAfter("abc", 'c', false)   = ""
	 * subAfter("abc", 'd', false)   = ""
	 * </pre>
	 *
	 * @param string          琚煡鎵剧殑瀛楃涓?
	 * @param separator       鍒嗛殧瀛楃涓诧紙涓嶅寘鎷級
	 * @param isLastSeparator 鏄惁鏌ユ壘鏈€鍚庝竴涓垎闅斿瓧绗︿覆锛堝娆″嚭鐜板垎闅斿瓧绗︿覆鏃堕€夊彇鏈€鍚庝竴涓級锛宼rue涓洪€夊彇鏈€鍚庝竴涓?
	 * @return 鍒囧壊鍚庣殑瀛楃涓?
	 * @since 4.1.15
	 */
	public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * 鎴彇鎸囧畾瀛楃涓蹭腑闂撮儴鍒嗭紝涓嶅寘鎷爣璇嗗瓧绗︿覆<br>
	 * <p>
	 * 鏍楀瓙锛?
	 *
	 * <pre>
	 * subBetween("wx[b]yz", "[", "]") = "b"
	 * subBetween(null, *, *)          = null
	 * subBetween(*, null, *)          = null
	 * subBetween(*, *, null)          = null
	 * subBetween("", "", "")          = ""
	 * subBetween("", "", "]")         = null
	 * subBetween("", "[", "]")        = null
	 * subBetween("yabcz", "", "")     = ""
	 * subBetween("yabcz", "y", "z")   = "abc"
	 * subBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 *
	 * @param str    琚垏鍓茬殑瀛楃涓?
	 * @param before 鎴彇寮€濮嬬殑瀛楃涓叉爣璇?
	 * @param after  鎴彇鍒扮殑瀛楃涓叉爣璇?
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 * @since 3.1.1
	 */
	public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
		if (str == null || before == null || after == null) {
			return null;
		}

		final String str2 = str.toString();
		final String before2 = before.toString();
		final String after2 = after.toString();

		final int start = str2.indexOf(before2);
		if (start != INDEX_NOT_FOUND) {
			final int end = str2.indexOf(after2, start + before2.length());
			if (end != INDEX_NOT_FOUND) {
				return str2.substring(start + before2.length(), end);
			}
		}
		return null;
	}

	/**
	 * 鎴彇鎸囧畾瀛楃涓蹭腑闂撮儴鍒嗭紝涓嶅寘鎷爣璇嗗瓧绗︿覆<br>
	 * <p>
	 * 鏍楀瓙锛?
	 *
	 * <pre>
	 * subBetween(null, *)            = null
	 * subBetween("", "")             = ""
	 * subBetween("", "tag")          = null
	 * subBetween("tagabctag", null)  = null
	 * subBetween("tagabctag", "")    = ""
	 * subBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 *
	 * @param str            琚垏鍓茬殑瀛楃涓?
	 * @param beforeAndAfter 鎴彇寮€濮嬪拰缁撴潫鐨勫瓧绗︿覆鏍囪瘑
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 * @since 3.1.1
	 */
	public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
		return subBetween(str, beforeAndAfter, beforeAndAfter);
	}

	/**
	 * 鎴彇鎸囧畾瀛楃涓插娈典腑闂撮儴鍒嗭紝涓嶅寘鎷爣璇嗗瓧绗︿覆<br>
	 * <p>
	 * 鏍楀瓙锛?
	 *
	 * <pre>
	 * subBetweenAll("wx[b]y[z]", "[", "]") 		= ["b","z"]
	 * subBetweenAll(null, *, *)          			= []
	 * subBetweenAll(*, null, *)          			= []
	 * subBetweenAll(*, *, null)          			= []
	 * subBetweenAll("", "", "")          			= []
	 * subBetweenAll("", "", "]")         			= []
	 * subBetweenAll("", "[", "]")        			= []
	 * subBetweenAll("yabcz", "", "")     			= []
	 * subBetweenAll("yabcz", "y", "z")   			= ["abc"]
	 * subBetweenAll("yabczyabcz", "y", "z")   		= ["abc","abc"]
	 * subBetweenAll("[yabc[zy]abcz]", "[", "]");   = ["zy"]           閲嶅彔鏃跺彧鎴彇鍐呴儴锛?
	 * </pre>
	 *
	 * @param str    琚垏鍓茬殑瀛楃涓?
	 * @param prefix 鎴彇寮€濮嬬殑瀛楃涓叉爣璇?
	 * @param suffix 鎴彇鍒扮殑瀛楃涓叉爣璇?
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 * @author dahuoyzs
	 * @since 5.2.5
	 */
	public static String[] subBetweenAll(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (hasEmpty(str, prefix, suffix) ||
			// 涓嶅寘鍚捣濮嬪瓧绗︿覆锛屽垯鑲畾娌℃湁瀛愪覆
			false == contains(str, prefix)) {
			return new String[0];
		}

		final List<String> result = new LinkedList<>();
		final String[] split = splitToArray(str, prefix);
		if (prefix.equals(suffix)) {
			// 鍓嶅悗缂€瀛楃鐩稿悓锛屽崟鐙鐞?
			for (int i = 1, length = split.length - 1; i < length; i += 2) {
				result.add(split[i]);
			}
		} else {
			int suffixIndex;
			String fragment;
			for (int i = 1; i < split.length; i++) {
				fragment = split[i];
				suffixIndex = fragment.indexOf(suffix.toString());
				if (suffixIndex > 0) {
					result.add(fragment.substring(0, suffixIndex));
				}
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * 鎴彇鎸囧畾瀛楃涓插娈典腑闂撮儴鍒嗭紝涓嶅寘鎷爣璇嗗瓧绗︿覆<br>
	 * <p>
	 * 鏍楀瓙锛?
	 *
	 * <pre>
	 * subBetweenAll(null, *)          			= []
	 * subBetweenAll(*, null)          			= []
	 * subBetweenAll(*, *)          			= []
	 * subBetweenAll("", "")          			= []
	 * subBetweenAll("", "#")         			= []
	 * subBetweenAll("gotanks", "")     		= []
	 * subBetweenAll("#gotanks#", "#")   		= ["gotanks"]
	 * subBetweenAll("#hello# #world#!", "#")   = ["hello", "world"]
	 * subBetweenAll("#hello# world#!", "#");   = ["hello"]
	 * </pre>
	 *
	 * @param str             琚垏鍓茬殑瀛楃涓?
	 * @param prefixAndSuffix 鎴彇寮€濮嬪拰缁撴潫鐨勫瓧绗︿覆鏍囪瘑
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 * @author gotanks
	 * @since 5.5.0
	 */
	public static String[] subBetweenAll(CharSequence str, CharSequence prefixAndSuffix) {
		return subBetweenAll(str, prefixAndSuffix, prefixAndSuffix);
	}

	// ------------------------------------------------------------------------ repeat

	/**
	 * 閲嶅鏌愪釜瀛楃
	 *
	 * <pre>
	 * repeat('e', 0)  = ""
	 * repeat('e', 3)  = "eee"
	 * repeat('e', -2) = ""
	 * </pre>
	 *
	 * @param c     琚噸澶嶇殑瀛楃
	 * @param count 閲嶅鐨勬暟鐩紝濡傛灉灏忎簬绛変簬0鍒欒繑鍥?"
	 * @return 閲嶅瀛楃瀛楃涓?
	 */
	public static String repeat(char c, int count) {
		if (count <= 0) {
			return EMPTY;
		}

		char[] result = new char[count];
		Arrays.fill(result, c);
		return new String(result);
	}

	/**
	 * 閲嶅鏌愪釜瀛楃涓?
	 *
	 * @param str   琚噸澶嶇殑瀛楃
	 * @param count 閲嶅鐨勬暟鐩?
	 * @return 閲嶅瀛楃瀛楃涓?
	 */
	public static String repeat(CharSequence str, int count) {
		if (null == str) {
			return null;
		}
		if (count <= 0 || str.length() == 0) {
			return EMPTY;
		}
		if (count == 1) {
			return str.toString();
		}

		// 妫€鏌?
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.toString().getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1鐩稿綋浜巒 *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 閲嶅鏌愪釜瀛楃涓插埌鎸囧畾闀垮害
	 *
	 * @param str    琚噸澶嶇殑瀛楃
	 * @param padLen 鎸囧畾闀垮害
	 * @return 閲嶅瀛楃瀛楃涓?
	 * @since 4.3.2
	 */
	public static String repeatByLength(CharSequence str, int padLen) {
		if (null == str) {
			return null;
		}
		if (padLen <= 0) {
			return EMPTY;
		}
		final int strLen = str.length();
		if (strLen == padLen) {
			return str.toString();
		} else if (strLen > padLen) {
			return subPre(str, padLen);
		}

		// 閲嶅锛岀洿鍒拌揪鍒版寚瀹氶暱搴?
		final char[] padding = new char[padLen];
		for (int i = 0; i < padLen; i++) {
			padding[i] = str.charAt(i % strLen);
		}
		return new String(padding);
	}

	/**
	 * 閲嶅鏌愪釜瀛楃涓插苟閫氳繃鍒嗙晫绗﹁繛鎺?
	 *
	 * <pre>
	 * repeatAndJoin("?", 5, ",")   = "?,?,?,?,?"
	 * repeatAndJoin("?", 0, ",")   = ""
	 * repeatAndJoin("?", 5, null) = "?????"
	 * </pre>
	 *
	 * @param str       琚噸澶嶇殑瀛楃涓?
	 * @param count     鏁伴噺
	 * @param delimiter 鍒嗙晫绗?
	 * @return 杩炴帴鍚庣殑瀛楃涓?
	 * @since 4.0.1
	 */
	public static String repeatAndJoin(CharSequence str, int count, CharSequence delimiter) {
		if (count <= 0) {
			return EMPTY;
		}
		final StringBuilder builder = new StringBuilder(str.length() * count);
		builder.append(str);
		count--;

		final boolean isAppendDelimiter = isNotEmpty(delimiter);
		while (count-- > 0) {
			if (isAppendDelimiter) {
				builder.append(delimiter);
			}
			builder.append(str);
		}
		return builder.toString();
	}

	// ------------------------------------------------------------------------ equals

	/**
	 * 姣旇緝涓や釜瀛楃涓诧紙澶у皬鍐欐晱鎰燂級銆?
	 *
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 *
	 * @param str1 瑕佹瘮杈冪殑瀛楃涓?
	 * @param str2 瑕佹瘮杈冪殑瀛楃涓?
	 * @return 濡傛灉涓や釜瀛楃涓茬浉鍚岋紝鎴栬€呴兘鏄瘂@code null}锛屽垯杩斿洖{@code true}
	 */
	public static boolean equals(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, false);
	}

	/**
	 * 姣旇緝涓や釜瀛楃涓诧紙澶у皬鍐欎笉鏁忔劅锛夈€?
	 *
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 *
	 * @param str1 瑕佹瘮杈冪殑瀛楃涓?
	 * @param str2 瑕佹瘮杈冪殑瀛楃涓?
	 * @return 濡傛灉涓や釜瀛楃涓茬浉鍚岋紝鎴栬€呴兘鏄瘂@code null}锛屽垯杩斿洖{@code true}
	 */
	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, true);
	}

	/**
	 * 姣旇緝涓や釜瀛楃涓叉槸鍚︾浉绛夛紝瑙勫垯濡備笅
	 * <ul>
	 *     <li>str1鍜宻tr2閮戒负{@code null}</li>
	 *     <li>蹇界暐澶у皬鍐欎娇鐢▄@link String#equalsIgnoreCase(String)}鍒ゆ柇鐩哥瓑</li>
	 *     <li>涓嶅拷鐣ュぇ灏忓啓浣跨敤{@link String#contentEquals(CharSequence)}鍒ゆ柇鐩哥瓑</li>
	 * </ul>
	 *
	 * @param str1       瑕佹瘮杈冪殑瀛楃涓?
	 * @param str2       瑕佹瘮杈冪殑瀛楃涓?
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 濡傛灉涓や釜瀛楃涓茬浉鍚岋紝鎴栬€呴兘鏄瘂@code null}锛屽垯杩斿洖{@code true}
	 * @since 3.2.0
	 */
	public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
		if (null == str1) {
			// 鍙湁涓や釜閮戒负null鎵嶅垽鏂浉绛?
			return str2 == null;
		}
		if (null == str2) {
			// 瀛楃涓?绌猴紝瀛楃涓?闈炵┖锛岀洿鎺alse
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿笌鎻愪緵鐨勪腑浠讳竴瀛楃涓茬浉鍚岋紙蹇界暐澶у皬鍐欙級锛岀浉鍚屽垯杩斿洖{@code true}锛屾病鏈夌浉鍚岀殑杩斿洖{@code false}<br>
	 * 濡傛灉鍙備笌姣斿鐨勫瓧绗︿覆鍒楄〃涓虹┖锛岃繑鍥瀧@code false}
	 *
	 * @param str1 缁欏畾闇€瑕佹鏌ョ殑瀛楃涓?
	 * @param strs 闇€瑕佸弬涓庢瘮瀵圭殑瀛楃涓插垪琛?
	 * @return 鏄惁鐩稿悓
	 * @since 4.3.2
	 */
	public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
		return equalsAny(str1, true, strs);
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿笌鎻愪緵鐨勪腑浠讳竴瀛楃涓茬浉鍚岋紝鐩稿悓鍒欒繑鍥瀧@code true}锛屾病鏈夌浉鍚岀殑杩斿洖{@code false}<br>
	 * 濡傛灉鍙備笌姣斿鐨勫瓧绗︿覆鍒楄〃涓虹┖锛岃繑鍥瀧@code false}
	 *
	 * @param str1 缁欏畾闇€瑕佹鏌ョ殑瀛楃涓?
	 * @param strs 闇€瑕佸弬涓庢瘮瀵圭殑瀛楃涓插垪琛?
	 * @return 鏄惁鐩稿悓
	 * @since 4.3.2
	 */
	public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
		return equalsAny(str1, false, strs);
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚︿笌鎻愪緵鐨勪腑浠讳竴瀛楃涓茬浉鍚岋紝鐩稿悓鍒欒繑鍥瀧@code true}锛屾病鏈夌浉鍚岀殑杩斿洖{@code false}<br>
	 * 濡傛灉鍙備笌姣斿鐨勫瓧绗︿覆鍒楄〃涓虹┖锛岃繑鍥瀧@code false}
	 *
	 * @param str1       缁欏畾闇€瑕佹鏌ョ殑瀛楃涓?
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @param strs       闇€瑕佸弬涓庢瘮瀵圭殑瀛楃涓插垪琛?
	 * @return 鏄惁鐩稿悓
	 * @since 4.3.2
	 */
	public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return false;
		}

		for (CharSequence str : strs) {
			if (equals(str1, str, ignoreCase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 瀛楃涓叉寚瀹氫綅缃殑瀛楃鏄惁涓庣粰瀹氬瓧绗︾浉鍚?br>
	 * 濡傛灉瀛楃涓蹭负null锛岃繑鍥瀎alse<br>
	 * 濡傛灉缁欏畾鐨勪綅缃ぇ浜庡瓧绗︿覆闀垮害锛岃繑鍥瀎alse<br>
	 * 濡傛灉缁欏畾鐨勪綅缃皬浜?锛岃繑鍥瀎alse
	 *
	 * @param str      瀛楃涓?
	 * @param position 浣嶇疆
	 * @param c        闇€瑕佸姣旂殑瀛楃
	 * @return 瀛楃涓叉寚瀹氫綅缃殑瀛楃鏄惁涓庣粰瀹氬瓧绗︾浉鍚?
	 * @since 3.3.1
	 */
	public static boolean equalsCharAt(CharSequence str, int position, char c) {
		if (null == str || position < 0) {
			return false;
		}
		return str.length() > position && c == str.charAt(position);
	}

	/**
	 * 鎴彇绗竴涓瓧涓茬殑閮ㄥ垎瀛楃锛屼笌绗簩涓瓧绗︿覆姣旇緝锛堥暱搴︿竴鑷达級锛屽垽鏂埅鍙栫殑瀛愪覆鏄惁鐩稿悓<br>
	 * 浠绘剰涓€涓瓧绗︿覆涓簄ull杩斿洖false
	 *
	 * @param str1       绗竴涓瓧绗︿覆
	 * @param start1     绗竴涓瓧绗︿覆寮€濮嬬殑浣嶇疆
	 * @param str2       绗簩涓瓧绗︿覆
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 瀛愪覆鏄惁鐩稿悓
	 * @since 3.2.1
	 */
	public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, boolean ignoreCase) {
		return isSubEquals(str1, start1, str2, 0, str2.length(), ignoreCase);
	}

	/**
	 * 鎴彇涓や釜瀛楃涓茬殑涓嶅悓閮ㄥ垎锛堥暱搴︿竴鑷达級锛屽垽鏂埅鍙栫殑瀛愪覆鏄惁鐩稿悓<br>
	 * 浠绘剰涓€涓瓧绗︿覆涓簄ull杩斿洖false
	 *
	 * @param str1       绗竴涓瓧绗︿覆
	 * @param start1     绗竴涓瓧绗︿覆寮€濮嬬殑浣嶇疆
	 * @param str2       绗簩涓瓧绗︿覆
	 * @param start2     绗簩涓瓧绗︿覆寮€濮嬬殑浣嶇疆
	 * @param length     鎴彇闀垮害
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 瀛愪覆鏄惁鐩稿悓
	 * @since 3.2.1
	 */
	public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
		if (null == str1 || null == str2) {
			return false;
		}

		return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
	}

	// ------------------------------------------------------------------------ format

	/**
	 * 鏍煎紡鍖栨枃鏈? {} 琛ㄧず鍗犱綅绗?br>
	 * 姝ゆ柟娉曞彧鏄畝鍗曞皢鍗犱綅绗?{} 鎸夌収椤哄簭鏇挎崲涓哄弬鏁?br>
	 * 濡傛灉鎯宠緭鍑?{} 浣跨敤 \\杞箟 { 鍗冲彲锛屽鏋滄兂杈撳嚭 {} 涔嬪墠鐨?\ 浣跨敤鍙岃浆涔夌 \\\\ 鍗冲彲<br>
	 * 渚嬶細<br>
	 * 閫氬父浣跨敤锛歠ormat("this is {} for {}", "a", "b") =銆?this is a for b<br>
	 * 杞箟{}锛?format("this is \\{} for {}", "a", "b") =銆?this is {} for a<br>
	 * 杞箟\锛?format("this is \\\\{} for {}", "a", "b") =銆?this is \a for b<br>
	 *
	 * @param template 鏂囨湰妯℃澘锛岃鏇挎崲鐨勯儴鍒嗙敤 {} 琛ㄧず锛屽鏋滄ā鏉夸负null锛岃繑鍥?null"
	 * @param params   鍙傛暟鍊?
	 * @return 鏍煎紡鍖栧悗鐨勬枃鏈紝濡傛灉妯℃澘涓簄ull锛岃繑鍥?null"
	 */
	public static String format(CharSequence template, Object... params) {
		if (null == template) {
			return NULL;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		if (template == null) return null;
		String result = template.toString();
		if (params != null) {
			for (Object param : params) {
				int idx = result.indexOf("{}");
				if (idx >= 0) {
					result = result.substring(0, idx) + (param != null ? param.toString() : "null") + result.substring(idx + 2);
				}
			}
		}
		return result;
	}

	/**
	 * 鏈夊簭鐨勬牸寮忓寲鏂囨湰锛屼娇鐢▄number}鍋氫负鍗犱綅绗?br>
	 * 閫氬父浣跨敤锛歠ormat("this is {0} for {1}", "a", "b") =銆?this is a for b<br>
	 *
	 * @param pattern   鏂囨湰鏍煎紡
	 * @param arguments 鍙傛暟
	 * @return 鏍煎紡鍖栧悗鐨勬枃鏈?
	 */
	public static String indexedFormat(CharSequence pattern, Object... arguments) {
		return MessageFormat.format(pattern.toString(), arguments);
	}
	// ------------------------------------------------------------------------ bytes

	/**
	 * 缂栫爜瀛楃涓诧紝缂栫爜涓篣TF-8
	 *
	 * @param str 瀛楃涓?
	 * @return 缂栫爜鍚庣殑瀛楄妭鐮?
	 */
	public static byte[] utf8Bytes(CharSequence str) {
		return bytes(str, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 缂栫爜瀛楃涓?br>
	 * 浣跨敤绯荤粺榛樿缂栫爜
	 *
	 * @param str 瀛楃涓?
	 * @return 缂栫爜鍚庣殑瀛楄妭鐮?
	 */
	public static byte[] bytes(CharSequence str) {
		return bytes(str, Charset.defaultCharset());
	}

	/**
	 * 缂栫爜瀛楃涓?
	 *
	 * @param str     瀛楃涓?
	 * @param charset 瀛楃闆嗭紝濡傛灉姝ゅ瓧娈典负绌猴紝鍒欒В鐮佺殑缁撴灉鍙栧喅浜庡钩鍙?
	 * @return 缂栫爜鍚庣殑瀛楄妭鐮?
	 */
	public static byte[] bytes(CharSequence str, String charset) {
		return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 缂栫爜瀛楃涓?
	 *
	 * @param str     瀛楃涓?
	 * @param charset 瀛楃闆嗭紝濡傛灉姝ゅ瓧娈典负绌猴紝鍒欒В鐮佺殑缁撴灉鍙栧喅浜庡钩鍙?
	 * @return 缂栫爜鍚庣殑瀛楄妭鐮?
	 */
	public static byte[] bytes(CharSequence str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * 瀛楃涓茶浆鎹负byteBuffer
	 *
	 * @param str     瀛楃涓?
	 * @param charset 缂栫爜
	 * @return byteBuffer
	 */
	public static ByteBuffer byteBuffer(CharSequence str, String charset) {
		return ByteBuffer.wrap(bytes(str, charset));
	}

	// ------------------------------------------------------------------------ wrap

	/**
	 * 鍖呰鎸囧畾瀛楃涓?br>
	 * 褰撳墠缂€鍜屽悗缂€涓€鑷存椂浣跨敤姝ゆ柟娉?
	 *
	 * @param str             琚寘瑁呯殑瀛楃涓?
	 * @param prefixAndSuffix 鍓嶇紑鍜屽悗缂€
	 * @return 鍖呰鍚庣殑瀛楃涓?
	 * @since 3.1.0
	 */
	public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
		return wrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 鍖呰鎸囧畾瀛楃涓?
	 *
	 * @param str    琚寘瑁呯殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 鍖呰鍚庣殑瀛楃涓?
	 */
	public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
		return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
	}

	/**
	 * 浣跨敤鍗曚釜瀛楃鍖呰澶氫釜瀛楃涓?
	 *
	 * @param prefixAndSuffix 鍓嶇紑鍜屽悗缂€
	 * @param strs            澶氫釜瀛楃涓?
	 * @return 鍖呰鐨勫瓧绗︿覆鏁扮粍
	 * @since 5.4.1
	 */
	public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
		return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 鍖呰澶氫釜瀛楃涓?
	 *
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @param strs   澶氫釜瀛楃涓?
	 * @return 鍖呰鐨勫瓧绗︿覆鏁扮粍
	 * @since 4.0.7
	 */
	public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrap(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 鍖呰鎸囧畾瀛楃涓诧紝濡傛灉鍓嶇紑鎴栧悗缂€宸茬粡鍖呭惈瀵瑰簲鐨勫瓧绗︿覆锛屽垯涓嶅啀鍖呰
	 *
	 * @param str    琚寘瑁呯殑瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 鍖呰鍚庣殑瀛楃涓?
	 */
	public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
		int len = 0;
		if (isNotEmpty(str)) {
			len += str.length();
		}
		if (isNotEmpty(prefix)) {
			len += prefix.length();
		}
		if (isNotEmpty(suffix)) {
			len += suffix.length();
		}
		StringBuilder sb = new StringBuilder(len);
		if (isNotEmpty(prefix) && false == startWith(str, prefix)) {
			sb.append(prefix);
		}
		if (isNotEmpty(str)) {
			sb.append(str);
		}
		if (isNotEmpty(suffix) && false == endWith(str, suffix)) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * 浣跨敤鎴愬鐨勫瓧绗﹀寘瑁呭涓瓧绗︿覆锛屽鏋滃凡缁忓寘瑁咃紝鍒欎笉鍐嶅寘瑁?
	 *
	 * @param prefixAndSuffix 鍓嶇紑鍜屽悗缂€
	 * @param strs            澶氫釜瀛楃涓?
	 * @return 鍖呰鐨勫瓧绗︿覆鏁扮粍
	 * @since 5.4.1
	 */
	public static String[] wrapAllWithPairIfMissing(CharSequence prefixAndSuffix, CharSequence... strs) {
		return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 鍖呰澶氫釜瀛楃涓诧紝濡傛灉宸茬粡鍖呰锛屽垯涓嶅啀鍖呰
	 *
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @param strs   澶氫釜瀛楃涓?
	 * @return 鍖呰鐨勫瓧绗︿覆鏁扮粍
	 * @since 4.0.7
	 */
	public static String[] wrapAllIfMissing(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrapIfMissing(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 鍘绘帀瀛楃鍖呰锛屽鏋滄湭琚寘瑁呭垯杩斿洖鍘熷瓧绗︿覆
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇疆瀛楃涓?
	 * @param suffix 鍚庣疆瀛楃涓?
	 * @return 鍘绘帀鍖呰瀛楃鐨勫瓧绗︿覆
	 * @since 4.0.1
	 */
	public static String unWrap(CharSequence str, String prefix, String suffix) {
		if (isWrap(str, prefix, suffix)) {
			return sub(str, prefix.length(), str.length() - suffix.length());
		}
		return str.toString();
	}

	/**
	 * 鍘绘帀瀛楃鍖呰锛屽鏋滄湭琚寘瑁呭垯杩斿洖鍘熷瓧绗︿覆
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇疆瀛楃
	 * @param suffix 鍚庣疆瀛楃
	 * @return 鍘绘帀鍖呰瀛楃鐨勫瓧绗︿覆
	 * @since 4.0.1
	 */
	public static String unWrap(CharSequence str, char prefix, char suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
			return sub(str, 1, str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * 鍘绘帀瀛楃鍖呰锛屽鏋滄湭琚寘瑁呭垯杩斿洖鍘熷瓧绗︿覆
	 *
	 * @param str             瀛楃涓?
	 * @param prefixAndSuffix 鍓嶇疆鍜屽悗缃瓧绗?
	 * @return 鍘绘帀鍖呰瀛楃鐨勫瓧绗︿覆
	 * @since 4.0.1
	 */
	public static String unWrap(CharSequence str, char prefixAndSuffix) {
		return unWrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 鎸囧畾瀛楃涓叉槸鍚﹁鍖呰
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 鏄惁琚寘瑁?
	 */
	public static boolean isWrap(CharSequence str, String prefix, String suffix) {
		if (ArrayUtil.hasNull(str, prefix, suffix)) {
			return false;
		}
		final String str2 = str.toString();
		return str2.startsWith(prefix) && str2.endsWith(suffix);
	}

	/**
	 * 鎸囧畾瀛楃涓叉槸鍚﹁鍚屼竴瀛楃鍖呰锛堝墠鍚庨兘鏈夎繖浜涘瓧绗︿覆锛?
	 *
	 * @param str     瀛楃涓?
	 * @param wrapper 鍖呰瀛楃涓?
	 * @return 鏄惁琚寘瑁?
	 */
	public static boolean isWrap(CharSequence str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 鎸囧畾瀛楃涓叉槸鍚﹁鍚屼竴瀛楃鍖呰锛堝墠鍚庨兘鏈夎繖浜涘瓧绗︿覆锛?
	 *
	 * @param str     瀛楃涓?
	 * @param wrapper 鍖呰瀛楃
	 * @return 鏄惁琚寘瑁?
	 */
	public static boolean isWrap(CharSequence str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 鎸囧畾瀛楃涓叉槸鍚﹁鍖呰
	 *
	 * @param str        瀛楃涓?
	 * @param prefixChar 鍓嶇紑
	 * @param suffixChar 鍚庣紑
	 * @return 鏄惁琚寘瑁?
	 */
	public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
		if (null == str || str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}

	// ------------------------------------------------------------------------ pad

	/**
	 * 琛ュ厖瀛楃涓蹭互婊¤冻鎸囧畾闀垮害锛屽鏋滄彁渚涚殑瀛楃涓插ぇ浜庢寚瀹氶暱搴︼紝鎴柇涔?
	 * 鍚岋細leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * padPre(null, *, *);//null
	 * padPre("1", 3, "ABC");//"AB1"
	 * padPre("123", 2, "ABC");//"12"
	 * padPre("1039", -1, "0");//"103"
	 * </pre>
	 *
	 * @param str    瀛楃涓?
	 * @param length 闀垮害
	 * @param padStr 琛ュ厖鐨勫瓧绗?
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 */
	public static String padPre(CharSequence str, int length, CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
			return subPre(str, length);
		}

		return repeatByLength(padStr, length - strLen).concat(str.toString());
	}

	/**
	 * 琛ュ厖瀛楃涓蹭互婊¤冻鏈€灏忛暱搴︼紝濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
	 * 鍚岋細leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * padPre(null, *, *);//null
	 * padPre("1", 3, '0');//"001"
	 * padPre("123", 2, '0');//"12"
	 * </pre>
	 *
	 * @param str     瀛楃涓?
	 * @param length  闀垮害
	 * @param padChar 琛ュ厖鐨勫瓧绗?
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 */
	public static String padPre(CharSequence str, int length, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
			return subPre(str, length);
		}

		return repeat(padChar, length - strLen).concat(str.toString());
	}

	/**
	 * 琛ュ厖瀛楃涓蹭互婊¤冻鏈€灏忛暱搴︼紝濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
	 *
	 * <pre>
	 * padAfter(null, *, *);//null
	 * padAfter("1", 3, '0');//"100"
	 * padAfter("123", 2, '0');//"23"
	 * padAfter("123", -1, '0')//"" 绌轰覆
	 * </pre>
	 *
	 * @param str     瀛楃涓诧紝濡傛灉涓簕@code null}锛岀洿鎺ヨ繑鍥瀗ull
	 * @param length  闀垮害
	 * @param padChar 琛ュ厖鐨勫瓧绗?
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 */
	public static String padAfter(CharSequence str, int length, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
			return sub(str, strLen - length, strLen);
		}

		return str.toString().concat(repeat(padChar, length - strLen));
	}

	/**
	 * 琛ュ厖瀛楃涓蹭互婊¤冻鏈€灏忛暱搴?
	 *
	 * <pre>
	 * padAfter(null, *, *);//null
	 * padAfter("1", 3, "ABC");//"1AB"
	 * padAfter("123", 2, "ABC");//"23"
	 * </pre>
	 *
	 * @param str    瀛楃涓诧紝濡傛灉涓簕@code null}锛岀洿鎺ヨ繑鍥瀗ull
	 * @param length 闀垮害
	 * @param padStr 琛ュ厖鐨勫瓧绗?
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @since 4.3.2
	 */
	public static String padAfter(CharSequence str, int length, CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//濡傛灉鎻愪緵鐨勫瓧绗︿覆澶т簬鎸囧畾闀垮害锛屾埅鏂箣
			return subSufByLength(str, length);
		}

		return str.toString().concat(repeatByLength(padStr, length - strLen));
	}

	// ------------------------------------------------------------------------ center

	/**
	 * 灞呬腑瀛楃涓诧紝涓よ竟琛ュ厖绌烘牸绗︼紝濡傛灉鎸囧畾闀垮害灏忎簬瀛楃涓诧紝鍒欒繑鍥炲師瀛楃涓?
	 *
	 * <pre>
	 * center(null, *)   = null
	 * center("", 4)     = "    "
	 * center("ab", -1)  = "ab"
	 * center("ab", 4)   = " ab "
	 * center("abcd", 2) = "abcd"
	 * center("a", 4)    = " a  "
	 * </pre>
	 *
	 * @param str  瀛楃涓?
	 * @param size 鎸囧畾闀垮害
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @since 4.3.2
	 */
	public static String center(CharSequence str, final int size) {
		return center(str, size, CharUtil.SPACE);
	}

	/**
	 * 灞呬腑瀛楃涓诧紝涓よ竟琛ュ厖鎸囧畾瀛楃锛屽鏋滄寚瀹氶暱搴﹀皬浜庡瓧绗︿覆锛屽垯杩斿洖鍘熷瓧绗︿覆
	 *
	 * <pre>
	 * center(null, *, *)     = null
	 * center("", 4, ' ')     = "    "
	 * center("ab", -1, ' ')  = "ab"
	 * center("ab", 4, ' ')   = " ab "
	 * center("abcd", 2, ' ') = "abcd"
	 * center("a", 4, ' ')    = " a  "
	 * center("a", 4, 'y')   = "yayy"
	 * center("abc", 7, ' ')   = "  abc  "
	 * </pre>
	 *
	 * @param str     瀛楃涓?
	 * @param size    鎸囧畾闀垮害
	 * @param padChar 涓よ竟琛ュ厖鐨勫瓧绗?
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @since 4.3.2
	 */
	public static String center(CharSequence str, final int size, char padChar) {
		if (str == null || size <= 0) {
			return str(str);
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padChar);
		str = padAfter(str, size, padChar);
		return str.toString();
	}

	/**
	 * 灞呬腑瀛楃涓诧紝涓よ竟琛ュ厖鎸囧畾瀛楃涓诧紝濡傛灉鎸囧畾闀垮害灏忎簬瀛楃涓诧紝鍒欒繑鍥炲師瀛楃涓?
	 *
	 * <pre>
	 * center(null, *, *)     = null
	 * center("", 4, " ")     = "    "
	 * center("ab", -1, " ")  = "ab"
	 * center("ab", 4, " ")   = " ab "
	 * center("abcd", 2, " ") = "abcd"
	 * center("a", 4, " ")    = " a  "
	 * center("a", 4, "yz")   = "yayz"
	 * center("abc", 7, null) = "  abc  "
	 * center("abc", 7, "")   = "  abc  "
	 * </pre>
	 *
	 * @param str    瀛楃涓?
	 * @param size   鎸囧畾闀垮害
	 * @param padStr 涓よ竟琛ュ厖鐨勫瓧绗︿覆
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 */
	public static String center(CharSequence str, final int size, CharSequence padStr) {
		if (str == null || size <= 0) {
			return str(str);
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padStr);
		str = padAfter(str, size, padStr);
		return str.toString();
	}

	// ------------------------------------------------------------------------ str

	/**
	 * {@link CharSequence} 杞负瀛楃涓诧紝null瀹夊叏
	 *
	 * @param cs {@link CharSequence}
	 * @return 瀛楃涓?
	 */
	public static String str(CharSequence cs) {
		return null == cs ? null : cs.toString();
	}

	// ------------------------------------------------------------------------ count

	/**
	 * 缁熻鎸囧畾鍐呭涓寘鍚寚瀹氬瓧绗︿覆鐨勬暟閲?br>
	 * 鍙傛暟涓?{@code null} 鎴栬€?"" 杩斿洖 {@code 0}.
	 *
	 * <pre>
	 * count(null, *)       = 0
	 * count("", *)         = 0
	 * count("abba", null)  = 0
	 * count("abba", "")    = 0
	 * count("abba", "a")   = 2
	 * count("abba", "ab")  = 1
	 * count("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param content      琚煡鎵剧殑瀛楃涓?
	 * @param strForSearch 闇€瑕佹煡鎵剧殑瀛楃涓?
	 * @return 鏌ユ壘鍒扮殑涓暟
	 */
	public static int count(CharSequence content, CharSequence strForSearch) {
		if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		final String content2 = content.toString();
		final String strForSearch2 = strForSearch.toString();
		while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
			count++;
			idx += strForSearch.length();
		}
		return count;
	}

	/**
	 * 缁熻鎸囧畾鍐呭涓寘鍚寚瀹氬瓧绗︾殑鏁伴噺
	 *
	 * @param content       鍐呭
	 * @param charForSearch 琚粺璁＄殑瀛楃
	 * @return 鍖呭惈鏁伴噺
	 */
	public static int count(CharSequence content, char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	// ------------------------------------------------------------------------ compare

	/**
	 * 姣旇緝涓や釜瀛楃涓诧紝鐢ㄤ簬鎺掑簭
	 *
	 * <pre>
	 * compare(null, null, *)     = 0
	 * compare(null , "a", true)  &lt; 0
	 * compare(null , "a", false) &gt; 0
	 * compare("a", null, true)   &gt; 0
	 * compare("a", null, false)  &lt; 0
	 * compare("abc", "abc", *)   = 0
	 * compare("a", "b", *)       &lt; 0
	 * compare("b", "a", *)       &gt; 0
	 * compare("a", "B", *)       &gt; 0
	 * compare("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       瀛楃涓?
	 * @param str2       瀛楃涓?
	 * @param nullIsLess {@code null} 鍊兼槸鍚︽帓鍦ㄥ墠锛坣ull鏄惁灏忎簬闈炵┖鍊硷級
	 * @return 鎺掑簭鍊笺€傝礋鏁帮細str1 &lt; str2锛屾鏁帮細str1 &gt; str2, 0锛歴tr1 == str2
	 */
	public static int compare(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareTo(str2.toString());
	}

	/**
	 * 姣旇緝涓や釜瀛楃涓诧紝鐢ㄤ簬鎺掑簭锛屽ぇ灏忓啓涓嶆晱鎰?
	 *
	 * <pre>
	 * compareIgnoreCase(null, null, *)     = 0
	 * compareIgnoreCase(null , "a", true)  &lt; 0
	 * compareIgnoreCase(null , "a", false) &gt; 0
	 * compareIgnoreCase("a", null, true)   &gt; 0
	 * compareIgnoreCase("a", null, false)  &lt; 0
	 * compareIgnoreCase("abc", "abc", *)   = 0
	 * compareIgnoreCase("abc", "ABC", *)   = 0
	 * compareIgnoreCase("a", "b", *)       &lt; 0
	 * compareIgnoreCase("b", "a", *)       &gt; 0
	 * compareIgnoreCase("a", "B", *)       &lt; 0
	 * compareIgnoreCase("A", "b", *)       &lt; 0
	 * compareIgnoreCase("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       瀛楃涓?
	 * @param str2       瀛楃涓?
	 * @param nullIsLess {@code null} 鍊兼槸鍚︽帓鍦ㄥ墠锛坣ull鏄惁灏忎簬闈炵┖鍊硷級
	 * @return 鎺掑簭鍊笺€傝礋鏁帮細str1 &lt; str2锛屾鏁帮細str1 &gt; str2, 0锛歴tr1 == str2
	 */
	public static int compareIgnoreCase(CharSequence str1, CharSequence str2, boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareToIgnoreCase(str2.toString());
	}

	/**
	 * 姣旇緝涓や釜鐗堟湰<br>
	 * null鐗堟湰鎺掑湪鏈€灏忥細鍗筹細
	 *
	 * <pre>
	 * compareVersion(null, "v1") &lt; 0
	 * compareVersion("v1", "v1")  = 0
	 * compareVersion(null, null)   = 0
	 * compareVersion("v1", null) &gt; 0
	 * compareVersion("1.0.0", "1.0.2") &lt; 0
	 * compareVersion("1.0.2", "1.0.2a") &lt; 0
	 * compareVersion("1.13.0", "1.12.1c") &gt; 0
	 * compareVersion("V0.0.20170102", "V0.0.20170101") &gt; 0
	 * </pre>
	 *
	 * @param version1 鐗堟湰1
	 * @param version2 鐗堟湰2
	 * @return 鎺掑簭鍊笺€傝礋鏁帮細version1 &lt; version2锛屾鏁帮細version1 &gt; version2, 0锛歷ersion1 == version2
	 * @since 4.0.2
	 */
	public static int compareVersion(CharSequence version1, CharSequence version2) {
		return VersionComparator.INSTANCE.compare(str(version1), str(version2));
	}

	// ------------------------------------------------------------------------ append and prepend

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负缁撳熬锛屽垯鍦ㄥ熬閮ㄦ坊鍔犵粨灏惧瓧绗︿覆<br>
	 * 涓嶅拷鐣ュぇ灏忓啓
	 *
	 * @param str      琚鏌ョ殑瀛楃涓?
	 * @param suffix   闇€瑕佹坊鍔犲埌缁撳熬鐨勫瓧绗︿覆
	 * @param suffixes 闇€瑕侀澶栨鏌ョ殑缁撳熬瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负缁撳熬锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String appendIfMissing(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负缁撳熬锛屽垯鍦ㄥ熬閮ㄦ坊鍔犵粨灏惧瓧绗︿覆<br>
	 * 蹇界暐澶у皬鍐?
	 *
	 * @param str      琚鏌ョ殑瀛楃涓?
	 * @param suffix   闇€瑕佹坊鍔犲埌缁撳熬鐨勫瓧绗︿覆
	 * @param suffixes 闇€瑕侀澶栨鏌ョ殑缁撳熬瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负缁撳熬锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String appendIfMissingIgnoreCase(CharSequence str, CharSequence suffix, CharSequence... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负缁撳熬锛屽垯鍦ㄥ熬閮ㄦ坊鍔犵粨灏惧瓧绗︿覆
	 *
	 * @param str          琚鏌ョ殑瀛楃涓?
	 * @param suffix       闇€瑕佹坊鍔犲埌缁撳熬鐨勫瓧绗︿覆锛屼笉鍙備笌妫€鏌ュ尮閰?
	 * @param ignoreCase   妫€鏌ョ粨灏炬椂鏄惁蹇界暐澶у皬鍐?
	 * @param testSuffixes 闇€瑕侀澶栨鏌ョ殑缁撳熬瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负缁撳熬锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String appendIfMissing(CharSequence str, CharSequence suffix, boolean ignoreCase, CharSequence... testSuffixes) {
		if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
			return str(str);
		}
		if (ArrayUtil.isNotEmpty(testSuffixes)) {
			for (final CharSequence testSuffix : testSuffixes) {
				if (endWith(str, testSuffix, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return str.toString().concat(suffix.toString());
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负寮€澶达紝鍒欏湪棣栭儴娣诲姞璧峰瀛楃涓?br>
	 * 涓嶅拷鐣ュぇ灏忓啓
	 *
	 * @param str      琚鏌ョ殑瀛楃涓?
	 * @param prefix   闇€瑕佹坊鍔犲埌棣栭儴鐨勫瓧绗︿覆
	 * @param prefixes 闇€瑕侀澶栨鏌ョ殑棣栭儴瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负璧峰锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String prependIfMissing(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负寮€澶达紝鍒欏湪棣栭儴娣诲姞璧峰瀛楃涓?br>
	 * 蹇界暐澶у皬鍐?
	 *
	 * @param str      琚鏌ョ殑瀛楃涓?
	 * @param prefix   闇€瑕佹坊鍔犲埌棣栭儴鐨勫瓧绗︿覆
	 * @param prefixes 闇€瑕侀澶栨鏌ョ殑棣栭儴瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负璧峰锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String prependIfMissingIgnoreCase(CharSequence str, CharSequence prefix, CharSequence... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}

	/**
	 * 濡傛灉缁欏畾瀛楃涓蹭笉鏄互缁欏畾鐨勪竴涓垨澶氫釜瀛楃涓蹭负寮€澶达紝鍒欏湪棣栭儴娣诲姞璧峰瀛楃涓?
	 *
	 * @param str        琚鏌ョ殑瀛楃涓?
	 * @param prefix     闇€瑕佹坊鍔犲埌棣栭儴鐨勫瓧绗︿覆
	 * @param ignoreCase 妫€鏌ョ粨灏炬椂鏄惁蹇界暐澶у皬鍐?
	 * @param prefixes   闇€瑕侀澶栨鏌ョ殑棣栭儴瀛楃涓诧紝濡傛灉浠ヨ繖浜涗腑鐨勪竴涓负璧峰锛屽垯涓嶅啀娣诲姞
	 * @return 濡傛灉宸茬粡缁撳熬锛岃繑鍥炲師瀛楃涓诧紝鍚﹀垯杩斿洖娣诲姞缁撳熬鐨勫瓧绗︿覆
	 * @since 3.0.7
	 */
	public static String prependIfMissing(CharSequence str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase)) {
			return str(str);
		}
		if (prefixes != null) {
			for (final CharSequence s : prefixes) {
				if (startWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return prefix.toString().concat(str.toString());
	}

	// ------------------------------------------------------------------------ replace

	/**
	 * 鏇挎崲瀛楃涓蹭腑鐨勬寚瀹氬瓧绗︿覆锛屽拷鐣ュぇ灏忓啓
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacement 琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 4.0.3
	 */
	public static String replaceIgnoreCase(CharSequence str, CharSequence searchStr, CharSequence replacement) {
		return replace(str, 0, searchStr, replacement, true);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑鐨勬寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacement 琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 4.0.3
	 */
	public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
		return replace(str, 0, searchStr, replacement, false);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑鐨勬寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacement 琚浛鎹㈢殑瀛楃涓?
	 * @param ignoreCase  鏄惁蹇界暐澶у皬鍐?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 4.0.3
	 */
	public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
		return replace(str, 0, searchStr, replacement, ignoreCase);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑鐨勬寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param fromIndex   寮€濮嬩綅缃紙鍖呮嫭锛?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacement 琚浛鎹㈢殑瀛楃涓?
	 * @param ignoreCase  鏄惁蹇界暐澶у皬鍐?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 4.0.3
	 */
	public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
		if (isEmpty(str) || isEmpty(searchStr)) {
			return str(str);
		}
		if (null == replacement) {
			replacement = EMPTY;
		}

		final int strLength = str.length();
		final int searchStrLength = searchStr.length();
		if (strLength < searchStrLength) {
			// issue#I4M16G@Gitee
			return str(str);
		}

		if (fromIndex > strLength) {
			return str(str);
		} else if (fromIndex < 0) {
			fromIndex = 0;
		}

		final StringBuilder result = new StringBuilder(strLength - searchStrLength + replacement.length());
		if (0 != fromIndex) {
			result.append(str.subSequence(0, fromIndex));
		}

		int preIndex = fromIndex;
		int index;
		while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
			result.append(str.subSequence(preIndex, index));
			result.append(replacement);
			preIndex = index + searchStrLength;
		}

		if (preIndex < strLength) {
			// 缁撳熬閮ㄥ垎
			result.append(str.subSequence(preIndex, strLength));
		}
		return result.toString();
	}

	/**
	 * 鏇挎崲鎸囧畾瀛楃涓茬殑鎸囧畾鍖洪棿鍐呭瓧绗︿负鍥哄畾瀛楃<br>
	 * 姝ゆ柟娉曚娇鐢▄@link String#codePoints()}瀹屾垚鎷嗗垎鏇挎崲
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 寮€濮嬩綅缃紙鍖呭惈锛?
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呭惈锛?
	 * @param replacedChar 琚浛鎹㈢殑瀛楃
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 3.2.1
	 * @deprecated 姝т箟锛岃浣跨敤{@link #replaceByCodePoint(CharSequence, int, int, char)}
	 */
	@Deprecated
	public static String replace(CharSequence str, int startInclude, int endExclude, char replacedChar) {
		return replaceByCodePoint(str, startInclude, endExclude, replacedChar);
	}

	/**
	 * 鏇挎崲鎸囧畾瀛楃涓茬殑鎸囧畾鍖洪棿鍐呭瓧绗︿负鍥哄畾瀛楃<br>
	 * 姝ゆ柟娉曚娇鐢▄@link String#codePoints()}瀹屾垚鎷嗗垎鏇挎崲
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 寮€濮嬩綅缃紙鍖呭惈锛?
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呭惈锛?
	 * @param replacedChar 琚浛鎹㈢殑瀛楃
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 5.8.27
	 */
	public static String replaceByCodePoint(CharSequence str, int startInclude, int endExclude, char replacedChar) {
		if (isEmpty(str)) {
			return str(str);
		}
		final String originalStr = str(str);
		int[] strCodePoints = originalStr.codePoints().toArray();
		final int strLength = strCodePoints.length;
		if (startInclude > strLength) {
			return originalStr;
		}
		if (endExclude > strLength) {
			endExclude = strLength;
		}
		if (startInclude > endExclude) {
			// 濡傛灉璧峰浣嶇疆澶т簬缁撴潫浣嶇疆锛屼笉鏇挎崲
			return originalStr;
		}

		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strLength; i++) {
			if (i >= startInclude && i < endExclude) {
				stringBuilder.append(replacedChar);
			} else {
				stringBuilder.append(new String(strCodePoints, i, 1));
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * 鏇挎崲鎸囧畾瀛楃涓茬殑鎸囧畾鍖洪棿鍐呭瓧绗︿负鎸囧畾瀛楃涓诧紝瀛楃涓插彧閲嶅涓€娆?br>
	 * 姝ゆ柟娉曚娇鐢▄@link String#codePoints()}瀹屾垚鎷嗗垎鏇挎崲
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 寮€濮嬩綅缃紙鍖呭惈锛?
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呭惈锛?
	 * @param replacedStr  琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 3.2.1
	 * @deprecated 姝т箟锛岃浣跨敤{@link #replaceByCodePoint(CharSequence, int, int, CharSequence)}
	 */
	@Deprecated
	public static String replace(CharSequence str, int startInclude, int endExclude, CharSequence replacedStr) {
		return replaceByCodePoint(str, startInclude, endExclude, replacedStr);
	}

	/**
	 * 鏇挎崲鎸囧畾瀛楃涓茬殑鎸囧畾鍖洪棿鍐呭瓧绗︿负鎸囧畾瀛楃涓诧紝瀛楃涓插彧閲嶅涓€娆?br>
	 * 姝ゆ柟娉曚娇鐢▄@link String#codePoints()}瀹屾垚鎷嗗垎鏇挎崲
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 寮€濮嬩綅缃紙鍖呭惈锛?
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呭惈锛?
	 * @param replacedStr  琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 5.8.27
	 */
	public static String replaceByCodePoint(CharSequence str, int startInclude, int endExclude, CharSequence replacedStr) {
		if (isEmpty(str)) {
			return str(str);
		}
		final String originalStr = str(str);
		int[] strCodePoints = originalStr.codePoints().toArray();
		final int strLength = strCodePoints.length;
		if (startInclude > strLength) {
			return originalStr;
		}
		if (endExclude > strLength) {
			endExclude = strLength;
		}
		if (startInclude > endExclude) {
			// 濡傛灉璧峰浣嶇疆澶т簬缁撴潫浣嶇疆锛屼笉鏇挎崲
			return originalStr;
		}

		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < startInclude; i++) {
			stringBuilder.append(new String(strCodePoints, i, 1));
		}
		stringBuilder.append(replacedStr);
		for (int i = endExclude; i < strLength; i++) {
			stringBuilder.append(new String(strCodePoints, i, 1));
		}
		return stringBuilder.toString();
	}

	/**
	 * 鏇挎崲鎵€鏈夋鍒欏尮閰嶇殑鏂囨湰锛屽苟浣跨敤鑷畾涔夊嚱鏁板喅瀹氬浣曟浛鎹?br>
	 * replaceFun鍙互閫氳繃{@link Matcher}鎻愬彇鍑哄尮閰嶅埌鐨勫唴瀹圭殑涓嶅悓閮ㄥ垎锛岀劧鍚庣粡杩囬噸鏂板鐞嗐€佺粍瑁呭彉鎴愭柊鐨勫唴瀹规斁鍥炲師浣嶃€?
	 * <pre class="code">
	 *     replace(this.content, "(\\d+)", parameters -&gt; "-" + parameters.group(1) + "-")
	 *     // 缁撴灉涓猴細"ZZZaaabbbccc涓枃-1234-"
	 * </pre>
	 *
	 * @param str        瑕佹浛鎹㈢殑瀛楃涓?
	 * @param pattern    鐢ㄤ簬鍖归厤鐨勬鍒欏紡
	 * @param replaceFun 鍐冲畾濡備綍鏇挎崲鐨勫嚱鏁?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @see ReUtil#replaceAll(CharSequence, java.util.regex.Pattern, Func1)
	 * @since 4.2.2
	 */
	public static String replace(CharSequence str, java.util.regex.Pattern pattern, Func1<java.util.regex.Matcher, String> replaceFun) {
		return ReUtil.replaceAll(str, pattern, replaceFun);
	}

	/**
	 * 鏇挎崲鎵€鏈夋鍒欏尮閰嶇殑鏂囨湰锛屽苟浣跨敤鑷畾涔夊嚱鏁板喅瀹氬浣曟浛鎹?
	 *
	 * @param str        瑕佹浛鎹㈢殑瀛楃涓?
	 * @param regex      鐢ㄤ簬鍖归厤鐨勬鍒欏紡
	 * @param replaceFun 鍐冲畾濡備綍鏇挎崲鐨勫嚱鏁?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @see ReUtil#replaceAll(CharSequence, String, Func1)
	 * @since 4.2.2
	 */
	public static String replace(CharSequence str, String regex, Func1<java.util.regex.Matcher, String> replaceFun) {
		return ReUtil.replaceAll(str, regex, replaceFun);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑鏈€鍚庝竴涓寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacedStr 琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 */
	public static String replaceLast(CharSequence str, CharSequence searchStr, CharSequence replacedStr) {
		return replaceLast(str, searchStr, replacedStr, false);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑鏈€鍚庝竴涓寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacedStr 琚浛鎹㈢殑瀛楃涓?
	 * @param ignoreCase  鏄惁蹇界暐澶у皬鍐?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 */
	public static String replaceLast(CharSequence str, CharSequence searchStr, CharSequence replacedStr, boolean ignoreCase) {
		if (isEmpty(str)) {
			return str(str);
		}
		int lastIndex = lastIndexOf(str, searchStr, str.length(), ignoreCase);
		if (INDEX_NOT_FOUND == lastIndex) {
			return str(str);
		}
		return replace(str, lastIndex, searchStr, replacedStr, ignoreCase);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑绗竴涓寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacedStr 琚浛鎹㈢殑瀛楃涓?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 */
	public static String replaceFirst(CharSequence str, CharSequence searchStr, CharSequence replacedStr) {
		return replaceFirst(str, searchStr, replacedStr, false);
	}

	/**
	 * 鏇挎崲瀛楃涓蹭腑绗竴涓寚瀹氬瓧绗︿覆
	 *
	 * @param str         瀛楃涓?
	 * @param searchStr   琚煡鎵剧殑瀛楃涓?
	 * @param replacedStr 琚浛鎹㈢殑瀛楃涓?
	 * @param ignoreCase  鏄惁蹇界暐澶у皬鍐?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 */
	public static String replaceFirst(CharSequence str, CharSequence searchStr, CharSequence replacedStr, boolean ignoreCase) {
		if (isEmpty(str)) {
			return str(str);
		}
		int startInclude = indexOf(str, searchStr, 0, ignoreCase);
		if (INDEX_NOT_FOUND == startInclude) {
			return str(str);
		}
		return replaceByCodePoint(str, startInclude, startInclude + searchStr.length(), replacedStr);
	}

	/**
	 * 鏇挎崲鎸囧畾瀛楃涓茬殑鎸囧畾鍖洪棿鍐呭瓧绗︿负"*"
	 * 淇楃О锛氳劚鏁忓姛鑳斤紝鍚庨潰鍏朵粬鍔熻兘锛屽彲浠ヨ锛欴esensitizedUtil(鑴辨晱宸ュ叿绫?
	 *
	 * <pre>
	 * hide(null,*,*)=null
	 * hide("",0,*)=""
	 * hide("jackduan@163.com",-1,4)   ****duan@163.com
	 * hide("jackduan@163.com",2,3)    ja*kduan@163.com
	 * hide("jackduan@163.com",3,2)    jackduan@163.com
	 * hide("jackduan@163.com",16,16)  jackduan@163.com
	 * hide("jackduan@163.com",16,17)  jackduan@163.com
	 * </pre>
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 寮€濮嬩綅缃紙鍖呭惈锛?
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呭惈锛?
	 * @return 鏇挎崲鍚庣殑瀛楃涓?
	 * @since 4.1.14
	 */
	public static String hide(CharSequence str, int startInclude, int endExclude) {
		return replaceByCodePoint(str, startInclude, endExclude, '*');
	}

	/**
	 * 鑴辨晱锛屼娇鐢ㄩ粯璁ょ殑鑴辨晱绛栫暐
	 *
	 * <pre>
	 * desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID)) =  "0"
	 * desensitized("娈垫娣?, DesensitizedUtil.DesensitizedType.CHINESE_NAME)) = "娈?*"
	 * desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD)) = "5***************1X"
	 * desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE)) = "0915*****79"
	 * desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE)) = "180****1999"
	 * desensitized("鍖椾含甯傛捣娣€鍖洪┈杩炴醇琛楅亾289鍙?, DesensitizedUtil.DesensitizedType.ADDRESS)) = "鍖椾含甯傛捣娣€鍖洪┈********"
	 * desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL)) = "d*************@gmail.com.cn"
	 * desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD)) = "**********"
	 * desensitized("鑻廌40000", DesensitizedUtil.DesensitizedType.CAR_LICENSE)) = "鑻廌4***0"
	 * desensitized("11011111222233333256", DesensitizedType.BANK_CARD)) = "1101 **** **** **** 3256"
	 * </pre>
	 *
	 * @param str              瀛楃涓?
	 * @param desensitizedType 鑴辨晱绫诲瀷;鍙互鑴辨晱锛氱敤鎴穒d銆佷腑鏂囧悕銆佽韩浠借瘉鍙枫€佸骇鏈哄彿銆佹墜鏈哄彿銆佸湴鍧€銆佺數瀛愰偖浠躲€佸瘑鐮?
	 * @return 鑴辨晱涔嬪悗鐨勫瓧绗︿覆
	 * @author dazer and neusoft and qiaomu
	 * @see DesensitizedUtil 濡傛灉闇€瑕佽嚜瀹氫箟锛岃劚鏁忚鍒欙紝璇蜂娇鐢ㄨ宸ュ叿绫伙紱
	 * @since 5.6.2
	 */
	public static String desensitized(CharSequence str, DesensitizedUtil.DesensitizedType desensitizedType) {
		return DesensitizedUtil.desensitized(str, desensitizedType);
	}

	/**
	 * 鏇挎崲瀛楃瀛楃鏁扮粍涓墍鏈夌殑瀛楃涓簉eplacedStr<br>
	 * 鎻愪緵鐨刢hars涓烘墍鏈夐渶瑕佽鏇挎崲鐨勫瓧绗︼紝渚嬪锛?\r\n"锛屽垯"\r"鍜?\n"閮戒細琚浛鎹紝鍝€曚粬浠崟鐙瓨鍦?
	 *
	 * @param str         琚鏌ョ殑瀛楃涓?
	 * @param chars       闇€瑕佹浛鎹㈢殑瀛楃鍒楄〃锛岀敤涓€涓瓧绗︿覆琛ㄧず杩欎釜瀛楃鍒楄〃
	 * @param replacedStr 鏇挎崲鎴愮殑瀛楃涓?
	 * @return 鏂板瓧绗︿覆
	 * @since 3.2.2
	 */
	public static String replaceChars(CharSequence str, String chars, CharSequence replacedStr) {
		if (isEmpty(str) || isEmpty(chars)) {
			return str(str);
		}
		return replaceChars(str, chars.toCharArray(), replacedStr);
	}

	/**
	 * 鏇挎崲瀛楃瀛楃鏁扮粍涓墍鏈夌殑瀛楃涓簉eplacedStr
	 *
	 * @param str         琚鏌ョ殑瀛楃涓?
	 * @param chars       闇€瑕佹浛鎹㈢殑瀛楃鍒楄〃
	 * @param replacedStr 鏇挎崲鎴愮殑瀛楃涓?
	 * @return 鏂板瓧绗︿覆
	 * @since 3.2.2
	 */
	public static String replaceChars(CharSequence str, char[] chars, CharSequence replacedStr) {
		if (isEmpty(str) || ArrayUtil.isEmpty(chars)) {
			return str(str);
		}

		final Set<Character> set = new HashSet<>(chars.length);
		for (char c : chars) {
			set.add(c);
		}
		int strLen = str.length();
		final StringBuilder builder = new StringBuilder();
		char c;
		for (int i = 0; i < strLen; i++) {
			c = str.charAt(i);
			builder.append(set.contains(c) ? replacedStr : c);
		}
		return builder.toString();
	}

	// ------------------------------------------------------------------------ length

	/**
	 * 鑾峰彇瀛楃涓茬殑闀垮害锛屽鏋滀负null杩斿洖0
	 *
	 * @param cs a 瀛楃涓?
	 * @return 瀛楃涓茬殑闀垮害锛屽鏋滀负null杩斿洖0
	 * @since 4.3.2
	 */
	public static int length(CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 缁欏畾瀛楃涓茶浆涓篵ytes鍚庣殑byte鏁帮紙byte闀垮害锛?
	 *
	 * @param cs      瀛楃涓?
	 * @param charset 缂栫爜
	 * @return byte闀垮害
	 * @since 4.5.2
	 */
	public static int byteLength(CharSequence cs, Charset charset) {
		return cs == null ? 0 : cs.toString().getBytes(charset).length;
	}

	/**
	 * 缁欏畾瀛楃涓叉暟缁勭殑鎬婚暱搴?br>
	 * null瀛楃闀垮害瀹氫箟涓?
	 *
	 * @param strs 瀛楃涓叉暟缁?
	 * @return 鎬婚暱搴?
	 * @since 4.0.1
	 */
	public static int totalLength(CharSequence... strs) {
		int totalLength = 0;
		for (CharSequence str : strs) {
			totalLength += (null == str ? 0 : str.length());
		}
		return totalLength;
	}

	/**
	 * 闄愬埗瀛楃涓查暱搴︼紝濡傛灉瓒呰繃鎸囧畾闀垮害锛屾埅鍙栨寚瀹氶暱搴﹀苟鍦ㄦ湯灏惧姞"..."
	 *
	 * @param string 瀛楃涓?
	 * @param length 鏈€澶ч暱搴?
	 * @return 鍒囧壊鍚庣殑鍓╀綑鐨勫墠鍗婇儴鍒嗗瓧绗︿覆+"..."
	 * @since 4.0.10
	 */
	public static String maxLength(CharSequence string, int length) {
		Assert.isTrue(length > 0);
		if (null == string) {
			return null;
		}
		if (string.length() <= length) {
			return string.toString();
		}
		return sub(string, 0, length) + "...";
	}

	// ------------------------------------------------------------------------ firstXXX

	/**
	 * 杩斿洖绗竴涓潪{@code null} 鍏冪礌
	 *
	 * @param strs 澶氫釜鍏冪礌
	 * @param <T>  鍏冪礌绫诲瀷
	 * @return 绗竴涓潪绌哄厓绱狅紝濡傛灉缁欏畾鐨勬暟缁勪负绌烘垨鑰呴兘涓虹┖锛岃繑鍥瀧@code null}
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonNull(T... strs) {
		return ArrayUtil.firstNonNull(strs);
	}

	/**
	 * 杩斿洖绗竴涓潪empty 鍏冪礌
	 *
	 * @param strs 澶氫釜鍏冪礌
	 * @param <T>  鍏冪礌绫诲瀷
	 * @return 绗竴涓潪绌哄厓绱狅紝濡傛灉缁欏畾鐨勬暟缁勪负绌烘垨鑰呴兘涓虹┖锛岃繑鍥瀧@code null}
	 * @see #isNotEmpty(CharSequence)
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonEmpty(T... strs) {
		return ArrayUtil.firstMatch(StrUtil::isNotEmpty, strs);
	}

	/**
	 * 杩斿洖绗竴涓潪blank 鍏冪礌
	 *
	 * @param strs 澶氫釜鍏冪礌
	 * @param <T>  鍏冪礌绫诲瀷
	 * @return 绗竴涓潪绌哄厓绱狅紝濡傛灉缁欏畾鐨勬暟缁勪负绌烘垨鑰呴兘涓虹┖锛岃繑鍥瀧@code null}
	 * @see #isNotBlank(CharSequence)
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonBlank(T... strs) {
		return ArrayUtil.firstMatch(StrUtil::isNotBlank, strs);
	}

	// ------------------------------------------------------------------------ lower and upper

	/**
	 * 灏嗗瓧绗︿覆杞负灏忓啓
	 *
	 * @param str 琚浆鐨勫瓧绗︿覆
	 * @return 杞崲鍚庣殑瀛楃涓?
	 * @see String#toLowerCase()
	 * @since 5.8.38
	 * @deprecated 鎷煎啓閿欒锛岃浣跨敤 {@link #toLowerCase(CharSequence)}
	 */
	@Deprecated
	public static String toLoweCase(final CharSequence str) {
		return toLowerCase(str);
	}

	/**
	 * 灏嗗瓧绗︿覆杞负灏忓啓
	 *
	 * @param str 琚浆鐨勫瓧绗︿覆
	 * @return 杞崲鍚庣殑瀛楃涓?
	 * @see String#toLowerCase()
	 * @since 5.8.39
	 */
	public static String toLowerCase(final CharSequence str) {
		if (null == str) {
			return null;
		}
		if (0 == str.length()) {
			return EMPTY;
		}
		return str.toString().toLowerCase();
	}

	/**
	 * 灏嗗瓧绗︿覆杞负澶у啓
	 *
	 * @param str 琚浆鐨勫瓧绗︿覆
	 * @return 杞崲鍚庣殑瀛楃涓?
	 * @see String#toUpperCase()
	 * @since 5.8.38
	 */
	public static String toUpperCase(final CharSequence str) {
		if (null == str) {
			return null;
		}
		if (0 == str.length()) {
			return EMPTY;
		}
		return str.toString().toUpperCase();
	}

	/**
	 * 鍘熷瓧绗︿覆棣栧瓧姣嶅ぇ鍐欏苟鍦ㄥ叾棣栭儴娣诲姞鎸囧畾瀛楃涓?渚嬪锛歴tr=name, preString=get =銆?return getName
	 *
	 * @param str       琚鐞嗙殑瀛楃涓?
	 * @param preString 娣诲姞鐨勯閮?
	 * @return 澶勭悊鍚庣殑瀛楃涓?
	 */
	public static String upperFirstAndAddPre(CharSequence str, String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}

	/**
	 * 澶у啓棣栧瓧姣?br>
	 * 渚嬪锛歴tr = name, return Name
	 *
	 * @param str 瀛楃涓?
	 * @return 瀛楃涓?
	 */
	public static String upperFirst(CharSequence str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isLowerCase(firstChar)) {
				return Character.toUpperCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	/**
	 * 灏忓啓棣栧瓧姣?br>
	 * 渚嬪锛歴tr = Name, return name
	 *
	 * @param str 瀛楃涓?
	 * @return 瀛楃涓?
	 */
	public static String lowerFirst(CharSequence str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isUpperCase(firstChar)) {
				return Character.toLowerCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	// ------------------------------------------------------------------------ filter

	/**
	 * 杩囨护瀛楃涓?
	 *
	 * @param str    瀛楃涓?
	 * @param filter 杩囨护鍣紝{@link Filter#accept(Object)}杩斿洖涓簕@code true}鐨勪繚鐣欏瓧绗?
	 * @return 杩囨护鍚庣殑瀛楃涓?
	 * @since 5.4.0
	 */
	public static String filter(CharSequence str, final Filter<Character> filter) {
		if (str == null || filter == null) {
			return str(str);
		}

		int len = str.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (filter.accept(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	// ------------------------------------------------------------------------ case

	/**
	 * 缁欏畾瀛楃涓蹭腑鐨勫瓧姣嶆槸鍚﹀叏閮ㄤ负澶у啓锛屽垽鏂緷鎹涓嬶細
	 *
	 * <pre>
	 * 1. 澶у啓瀛楁瘝鍖呮嫭A-Z
	 * 2. 鍏跺畠闈炲瓧姣嶇殑Unicode绗﹂兘绠椾綔澶у啓
	 * </pre>
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鏄惁鍏ㄩ儴涓哄ぇ鍐?
	 * @since 4.2.2
	 */
	public static boolean isUpperCase(CharSequence str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isLowerCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 缁欏畾瀛楃涓蹭腑鐨勫瓧姣嶆槸鍚﹀叏閮ㄤ负灏忓啓锛屽垽鏂緷鎹涓嬶細
	 *
	 * <pre>
	 * 1. 灏忓啓瀛楁瘝鍖呮嫭a-z
	 * 2. 鍏跺畠闈炲瓧姣嶇殑Unicode绗﹂兘绠椾綔灏忓啓
	 * </pre>
	 *
	 * @param str 琚鏌ョ殑瀛楃涓?
	 * @return 鏄惁鍏ㄩ儴涓哄皬鍐?
	 * @since 4.2.2
	 */
	public static boolean isLowerCase(CharSequence str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 鍒囨崲缁欏畾瀛楃涓蹭腑鐨勫ぇ灏忓啓銆傚ぇ鍐欒浆灏忓啓锛屽皬鍐欒浆澶у啓銆?
	 *
	 * <pre>
	 * swapCase(null)                 = null
	 * swapCase("")                   = ""
	 * swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 *
	 * @param str 瀛楃涓?
	 * @return 浜ゆ崲鍚庣殑瀛楃涓?
	 * @since 4.3.2
	 */
	public static String swapCase(final String str) {
		if (isEmpty(str)) {
			return str;
		}

		final char[] buffer = str.toCharArray();

		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}

	/**
	 * 灏嗛┘宄板紡鍛藉悕鐨勫瓧绗︿覆杞崲涓轰笅鍒掔嚎鏂瑰紡銆傚鏋滆浆鎹㈠墠鐨勯┘宄板紡鍛藉悕鐨勫瓧绗︿覆涓虹┖锛屽垯杩斿洖绌哄瓧绗︿覆銆?br>
	 * 渚嬪锛?
	 *
	 * <pre>
	 * HelloWorld=銆媓ello_world
	 * Hello_World=銆媓ello_world
	 * HelloWorld_test=銆媓ello_world_test
	 * </pre>
	 *
	 * @param str 杞崲鍓嶇殑椹煎嘲寮忓懡鍚嶇殑瀛楃涓诧紝涔熷彲浠ヤ负涓嬪垝绾垮舰寮?
	 * @return 杞崲鍚庝笅鍒掔嚎鏂瑰紡鍛藉悕鐨勫瓧绗︿覆
	 * @see NamingCase#toUnderlineCase(CharSequence)
	 */
	public static String toUnderlineCase(CharSequence str) {
		return toSymbolCase(str, '_');
	}

	/**
	 * 灏嗛┘宄板紡鍛藉悕鐨勫瓧绗︿覆杞崲涓轰娇鐢ㄧ鍙疯繛鎺ユ柟寮忋€傚鏋滆浆鎹㈠墠鐨勯┘宄板紡鍛藉悕鐨勫瓧绗︿覆涓虹┖锛屽垯杩斿洖绌哄瓧绗︿覆銆?br>
	 *
	 * @param str    杞崲鍓嶇殑椹煎嘲寮忓懡鍚嶇殑瀛楃涓诧紝涔熷彲浠ヤ负绗﹀彿杩炴帴褰㈠紡
	 * @param symbol 杩炴帴绗?
	 * @return 杞崲鍚庣鍙疯繛鎺ユ柟寮忓懡鍚嶇殑瀛楃涓?
	 * @see NamingCase#toSymbolCase(CharSequence, char)
	 * @since 4.0.10
	 */
	public static String toSymbolCase(CharSequence str, char symbol) {
		if (str == null) {
			return null;
		}
		final int length = str.length();
		final StringBuilder sb = new StringBuilder();
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
						if (null == nextChar || Character.isLowerCase(nextChar) || Character.isDigit(nextChar)) {
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

	/**
	 * 灏嗕笅鍒掔嚎鏂瑰紡鍛藉悕鐨勫瓧绗︿覆杞崲涓洪┘宄板紡銆傚鏋滆浆鎹㈠墠鐨勪笅鍒掔嚎澶у啓鏂瑰紡鍛藉悕鐨勫瓧绗︿覆涓虹┖锛屽垯杩斿洖绌哄瓧绗︿覆銆?br>
	 * 渚嬪锛歨ello_world=銆媓elloWorld
	 *
	 * @param name 杞崲鍓嶇殑涓嬪垝绾垮ぇ鍐欐柟寮忓懡鍚嶇殑瀛楃涓?
	 * @return 杞崲鍚庣殑椹煎嘲寮忓懡鍚嶇殑瀛楃涓?
	 * @see NamingCase#toCamelCase(CharSequence)
	 */
	public static String toCamelCase(CharSequence name) {
		return toCamelCase(name, '_');
	}

	/**
	 * 灏嗚繛鎺ョ鏂瑰紡鍛藉悕鐨勫瓧绗︿覆杞崲涓洪┘宄板紡銆傚鏋滆浆鎹㈠墠鐨勪笅鍒掔嚎澶у啓鏂瑰紡鍛藉悕鐨勫瓧绗︿覆涓虹┖锛屽垯杩斿洖绌哄瓧绗︿覆銆?br>
	 * 渚嬪锛歨ello_world=銆媓elloWorld; hello-world=銆媓elloWorld
	 *
	 * @param name   杞崲鍓嶇殑涓嬪垝绾垮ぇ鍐欐柟寮忓懡鍚嶇殑瀛楃涓?
	 * @param symbol 杩炴帴绗?
	 * @return 杞崲鍚庣殑椹煎嘲寮忓懡鍚嶇殑瀛楃涓?
	 * @see NamingCase#toCamelCase(CharSequence, char)
	 */
	public static String toCamelCase(CharSequence name, char symbol) {
		return toCamelCaseInternal(name, symbol);
	}

	// ------------------------------------------------------------------------ isSurround

	/**
	 * 缁欏畾瀛楃涓叉槸鍚﹁瀛楃鍖呭洿
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 鏄惁鍖呭洿锛岀┖涓蹭笉鍖呭洿
	 */
	public static boolean isSurround(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (isBlank(str)) {
			return false;
		}
		if (str.length() < (prefix.length() + suffix.length())) {
			return false;
		}

		final String str2 = str.toString();
		return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
	}

	/**
	 * 缁欏畾瀛楃涓叉槸鍚﹁瀛楃鍖呭洿
	 *
	 * @param str    瀛楃涓?
	 * @param prefix 鍓嶇紑
	 * @param suffix 鍚庣紑
	 * @return 鏄惁鍖呭洿锛岀┖涓蹭笉鍖呭洿
	 */
	public static boolean isSurround(CharSequence str, char prefix, char suffix) {
		if (isBlank(str)) {
			return false;
		}
		if (str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
	}

	// ------------------------------------------------------------------------ builder

	/**
	 * 鍒涘缓StringBuilder瀵硅薄
	 *
	 * @param strs 鍒濆瀛楃涓插垪琛?
	 * @return StringBuilder瀵硅薄
	 */
	public static StringBuilder builder(CharSequence... strs) {
		return builder(Function.identity(), strs);
	}

	/**
	 * 鍒涘缓StringBuilder瀵硅薄
	 *
	 * @param strEditor 缂栬緫鍣紝鐢ㄤ簬瀵规瘡涓瓧绗︿覆杩涜缂栬緫
	 * @param strs      寰呭鐞嗙殑瀛楃涓插垪琛?
	 * @return StringBuilder瀵硅薄
	 * @since 5.8.42
	 */
	public static StringBuilder builder(Function<CharSequence, CharSequence> strEditor, final CharSequence... strs) {
		final StringBuilder sb = new StringBuilder();
		for (final CharSequence str : strs) {
			sb.append(strEditor.apply(str));
		}
		return sb;
	}

	/**
	 * 鍒涘缓StrBuilder瀵硅薄
	 *
	 * @param strs 鍒濆瀛楃涓插垪琛?
	 * @return StrBuilder瀵硅薄
	 */
	public static StrBuilder strBuilder(CharSequence... strs) {
		final StrBuilder sb = new StrBuilder();
		for (CharSequence str2 : strs) {
			sb.append(str2);
		}
		return sb;
	}

	// ------------------------------------------------------------------------ getter and setter

	/**
	 * 鑾峰緱set鎴杇et鎴杋s鏂规硶瀵瑰簲鐨勬爣鍑嗗睘鎬у悕<br>
	 * 渚嬪锛歴etName 杩斿洖 name
	 *
	 * <pre>
	 * getName =銆媙ame
	 * setName =銆媙ame
	 * isName  =銆媙ame
	 * </pre>
	 *
	 * @param getOrSetMethodName Get鎴朣et鏂规硶鍚?
	 * @return 濡傛灉鏄痵et鎴杇et鏂规硶鍚嶏紝杩斿洖field锛?鍚﹀垯null
	 */
	public static String getGeneralField(CharSequence getOrSetMethodName) {
		final String getOrSetMethodNameStr = getOrSetMethodName.toString();
		if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
			return removePreAndLowerFirst(getOrSetMethodName, 3);
		} else if (getOrSetMethodNameStr.startsWith("is")) {
			return removePreAndLowerFirst(getOrSetMethodName, 2);
		}
		return null;
	}

	/**
	 * 鐢熸垚set鏂规硶鍚?br>
	 * 渚嬪锛歯ame 杩斿洖 setName
	 *
	 * @param fieldName 灞炴€у悕
	 * @return setXxx
	 */
	public static String genSetter(CharSequence fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}

	/**
	 * 鐢熸垚get鏂规硶鍚?
	 *
	 * @param fieldName 灞炴€у悕
	 * @return getXxx
	 */
	public static String genGetter(CharSequence fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}

	// ------------------------------------------------------------------------ other

	/**
	 * 杩炴帴澶氫釜瀛楃涓蹭负涓€涓?
	 *
	 * @param isNullToEmpty 鏄惁null杞负""
	 * @param strs          瀛楃涓叉暟缁?
	 * @return 杩炴帴鍚庣殑瀛楃涓?
	 * @since 4.1.0
	 */
	public static String concat(boolean isNullToEmpty, CharSequence... strs) {
		final StrBuilder sb = new StrBuilder();
		for (CharSequence str : strs) {
			sb.append(isNullToEmpty ? nullToEmpty(str) : str);
		}
		return sb.toString();
	}

	/**
	 * 灏嗙粰瀹氬瓧绗︿覆锛屽彉鎴?"xxx...xxx" 褰㈠紡鐨勫瓧绗︿覆
	 *
	 * <ul>
	 *     <li>abcdefgh  9 -銆?abcdefgh</li>
	 *     <li>abcdefgh  8 -銆?abcdefgh</li>
	 *     <li>abcdefgh  7 -銆?ab...gh</li>
	 *     <li>abcdefgh  6 -銆?ab...h</li>
	 *     <li>abcdefgh  5 -銆?a...h</li>
	 *     <li>abcdefgh  4 -銆?a..h</li>
	 *     <li>abcdefgh  3 -銆?a.h</li>
	 *     <li>abcdefgh  2 -銆?a.</li>
	 *     <li>abcdefgh  1 -銆?a</li>
	 *     <li>abcdefgh  0 -銆?abcdefgh</li>
	 *     <li>abcdefgh -1 -銆?abcdefgh</li>
	 * </ul>
	 *
	 * @param str       瀛楃涓?
	 * @param maxLength 缁撴灉鐨勬渶澶ч暱搴?
	 * @return 鎴彇鍚庣殑瀛楃涓?
	 */
	public static String brief(CharSequence str, int maxLength) {
		if (null == str) {
			return null;
		}
		final int strLength = str.length();
		if (maxLength <= 0 || strLength <= maxLength) {
			return str.toString();
		}

		// since 5.7.5锛岀壒娈婇暱搴?
		switch (maxLength) {
			case 1:
				return String.valueOf(str.charAt(0));
			case 2:
				return str.charAt(0) + ".";
			case 3:
				return str.charAt(0) + "." + str.charAt(strLength - 1);
			case 4:
				return str.charAt(0) + ".." + str.charAt(strLength - 1);
		}

		final int suffixLength = (maxLength - 3) / 2;
		final int preLength = suffixLength + (maxLength - 3) % 2; // suffixLength 鎴?suffixLength + 1
		final String str2 = str.toString();
		return format("{}...{}",
			str2.substring(0, preLength),
			str2.substring(strLength - suffixLength));
	}

	/**
	 * 浠?conjunction 涓哄垎闅旂灏嗗涓璞¤浆鎹负瀛楃涓?
	 *
	 * @param conjunction 鍒嗛殧绗?{@link StrPool#COMMA}
	 * @param objs        鏁扮粍
	 * @return 杩炴帴鍚庣殑瀛楃涓?
	 * @see ArrayUtil#join(Object, CharSequence)
	 */
	public static String join(CharSequence conjunction, Object... objs) {
		return ArrayUtil.join(objs, conjunction);
	}

	/**
	 * 浠?conjunction 涓哄垎闅旂灏嗗涓璞¤浆鎹负瀛楃涓?
	 *
	 * @param <T>         鍏冪礌绫诲瀷
	 * @param conjunction 鍒嗛殧绗?{@link StrPool#COMMA}
	 * @param iterable    闆嗗悎
	 * @return 杩炴帴鍚庣殑瀛楃涓?
	 * @see CollUtil#join(Iterable, CharSequence)
	 * @since 5.6.6
	 */
	public static <T> String join(CharSequence conjunction, Iterable<T> iterable) {
		final java.util.List<Object> list = new java.util.ArrayList<>();
		for (T item : iterable) {
			list.add(item);
		}
		return CollUtil.join(list, conjunction);
	}

	/**
	 * 瀛楃涓茬殑姣忎竴涓瓧绗︽槸鍚﹂兘涓庡畾涔夌殑鍖归厤鍣ㄥ尮閰?
	 *
	 * @param value   瀛楃涓?
	 * @param matcher 鍖归厤鍣?
	 * @return 鏄惁鍏ㄩ儴鍖归厤
	 * @since 3.2.3
	 */
	public static boolean isAllCharMatch(CharSequence value, Matcher<Character> matcher) {
		if (isBlank(value)) {
			return false;
		}
		for (int i = value.length(); --i >= 0; ) {
			if (false == matcher.match(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 妫€鏌ュ瓧绗︿覆鏄惁閮戒负鏁板瓧缁勬垚
	 *
	 * @param str 瀛楃涓?
	 * @return 鏄惁閮戒负鏁板瓧缁勬垚
	 * @since 5.7.3
	 */
	public static boolean isNumeric(CharSequence str) {
		return isAllCharMatch(str, Character::isDigit);
	}

	/**
	 * 寰幆浣嶇Щ鎸囧畾浣嶇疆鐨勫瓧绗︿覆涓烘寚瀹氳窛绂?br>
	 * 褰搈oveLength澶т簬0鍚戝彸浣嶇Щ锛屽皬浜?鍚戝乏浣嶇Щ锛?涓嶄綅绉?br>
	 * 褰搈oveLength澶т簬瀛楃涓查暱搴︽椂閲囧彇寰幆浣嶇Щ绛栫暐锛屽嵆浣嶇Щ鍒板ご鍚庝粠澶达紙灏撅級浣嶇Щ锛屼緥濡傞暱搴︿负10锛屼綅绉?3鍒欒〃绀轰綅绉?
	 *
	 * @param str          瀛楃涓?
	 * @param startInclude 璧峰浣嶇疆锛堝寘鎷級
	 * @param endExclude   缁撴潫浣嶇疆锛堜笉鍖呮嫭锛?
	 * @param moveLength   绉诲姩璺濈锛岃礋鏁拌〃绀哄乏绉伙紝姝ｆ暟涓哄彸绉?
	 * @return 浣嶇Щ鍚庣殑瀛楃涓?
	 * @since 4.0.7
	 */
	public static String move(CharSequence str, int startInclude, int endExclude, int moveLength) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int len = str.length();
		// 鍙傛暟鏍￠獙
		if (startInclude < 0 || endExclude > len || startInclude > endExclude) {
			throw new IndexOutOfBoundsException("Invalid range: [" + startInclude + ", " + endExclude + ")");
		}

		if (Math.abs(moveLength) > len) {
			// 寰幆浣嶇Щ锛屽綋瓒婄晫鏃跺惊鐜?
			moveLength = moveLength % len;
		}
		// 鍒嗙鈥滅Щ鍔ㄥ潡鈥濆拰鈥滃墿浣欒建閬撯€?
		final String block = str.subSequence(startInclude, endExclude).toString();
		final String rest = new StringBuilder(str).delete(startInclude, endExclude).toString();

		final int restLen = rest.length();
		if (restLen == 0) {
			return str.toString(); // 鍏ㄩ€夋椂浣嶇Щ鏃犳剰涔?
		}
		// 璁＄畻寰幆鍛ㄦ湡锛氬墿浣欏瓧绗︽暟 + 1锛堜唬琛ㄥ潡鍙互瀛樺湪鐨勪笉鍚屼綅缃偣锛?
		final int totalPositions = restLen + 1;
		// 璁＄畻绉诲姩鍚庣殑鏂颁綅缃?(澶勭悊姝ｈ礋鏁?
		final int newPos = (startInclude + moveLength % totalPositions + totalPositions) % totalPositions;
		// 閲嶆柊缁勮
		return rest.substring(0, newPos) + // 鏀惧叆鏂颁綅缃墠鐨勫墿浣欏瓧绗?
			block +                     // 鏀惧叆鏁翠綋鍧?
			rest.substring(newPos); // 鏀惧叆鍓╀笅鐨勫墿浣欏瓧绗?
	}

	/**
	 * 妫€鏌ョ粰瀹氬瓧绗︿覆鐨勬墍鏈夊瓧绗︽槸鍚﹂兘涓€鏍?
	 *
	 * @param str 瀛楃鍑哄晩
	 * @return 缁欏畾瀛楃涓茬殑鎵€鏈夊瓧绗︽槸鍚﹂兘涓€鏍?
	 * @since 5.7.3
	 */
	public static boolean isCharEquals(CharSequence str) {
		Assert.isTrue(isNotEmpty(str), "Str to check must be not empty!");
		return count(str, str.charAt(0)) == str.length();
	}

	/**
	 * 瀵瑰瓧绗︿覆褰掍竴鍖栧鐞嗭紝濡?"脕" 鍙互浣跨敤 "u00C1"鎴?"u0041u0301"琛ㄧず锛屽疄闄呮祴璇曚腑涓や釜瀛楃涓插苟涓峞quals<br>
	 * 鍥犳浣跨敤姝ゆ柟娉曞綊涓€涓轰竴绉嶈〃绀哄舰寮忥紝榛樿鎸夌収W3C閫氬父寤鸿鐨勶紝鍦∟FC涓氦鎹㈡枃鏈€?
	 *
	 * @param str 褰掍竴鍖栫殑瀛楃涓?
	 * @return 褰掍竴鍖栧悗鐨勫瓧绗︿覆
	 * @see Normalizer#normalize(CharSequence, Normalizer.Form)
	 * @since 5.7.16
	 */
	public static String normalize(CharSequence str) {
		return Normalizer.normalize(str, Normalizer.Form.NFC);
	}

	/**
	 * 鍦ㄧ粰瀹氬瓧绗︿覆鏈熬濉厖鎸囧畾瀛楃锛屼互杈惧埌缁欏畾闀垮害<br>
	 * 濡傛灉瀛楃涓叉湰韬殑闀垮害澶т簬绛変簬length锛岃繑鍥炲師瀛楃涓?
	 *
	 * @param str       瀛楃涓?
	 * @param fixedChar 琛ュ厖鐨勫瓧绗?
	 * @param length    琛ュ厖鍒扮殑闀垮害
	 * @return 琛ュ厖鍚庣殑瀛楃涓?
	 * @since 5.8.0
	 */
	public static String fixLength(CharSequence str, char fixedChar, int length) {
		final int fixedLength = length - str.length();
		if (fixedLength <= 0) {
			return str.toString();
		}
		return str + repeat(fixedChar, fixedLength);
	}

	/**
	 * <p>鎸囧畾瀛楃涓叉暟缁勪腑锛屾槸鍚﹀寘鍚┖瀛楃涓层€?/p>
	 * <p>濡傛灉浼犲叆鍙傛暟瀵硅薄涓嶆槸涓虹┖,鍒欒繑鍥瀎alse銆傚鏋滃瓧绗︿覆鍖呭惈瀛楁瘝,涓嶅尯鍒嗗ぇ灏忓啓,鍒欒繑鍥瀟rue</p>
	 *
	 * @param str 瀵硅薄
	 * @return 濡傛灉涓哄瓧绗︿覆, 鏄惁鏈夊瓧姣?
	 */
	public static boolean hasLetter(CharSequence str) {
		if (null == str) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (CharUtil.isLetter(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 瀛楃涓?鍜屽瓧绗︿覆2鐨勫叕鍏卞墠缂€
	 *
	 * @param str1 瀛楃涓?
	 * @param str2 瀛楃涓?
	 * @return 瀛楃涓?鍜屽瓧绗︿覆2鐨勫叕鍏卞墠缂€
	 */
	public static CharSequence commonPrefix(CharSequence str1, CharSequence str2) {
		if (isEmpty(str1) || isEmpty(str2)) {
			return EMPTY;
		}
		final int minLength = Math.min(str1.length(), str2.length());

		int index = 0;

		for (; index < minLength; index++) {

			if (str1.charAt(index) != str2.charAt(index)) {
				break;
			}
		}
		return str1.subSequence(0, index);
	}

	/**
	 * 瀛楃涓?鍜屽瓧绗︿覆2鐨勫叕鍏卞悗缂€
	 *
	 * @param str1 瀛楃涓?
	 * @param str2 瀛楃涓?
	 * @return 瀛楃涓?鍜屽瓧绗︿覆2鐨勫叕鍏卞悗缂€
	 */
	public static CharSequence commonSuffix(CharSequence str1, CharSequence str2) {
		if (isEmpty(str1) || isEmpty(str2)) {
			return EMPTY;
		}
		int str1Index = str1.length() - 1;
		int str2Index = str2.length() - 1;
		for (; str1Index >= 0 && str2Index >= 0; str1Index--, str2Index--) {
			if (str1.charAt(str1Index) != str2.charAt(str2Index)) {
				break;
			}
		}
		return str1.subSequence(str1Index + 1, str1.length());
	}


	// ==================== StrUtil methods ====================












/**
 * 瀛楃涓插伐鍏风被
 *
 * @author xiaoleilu
 */


	// ------------------------------------------------------------------------ Blank

	/**
	 * <p>濡傛灉瀵硅薄鏄瓧绗︿覆鏄惁涓虹┖鐧斤紝绌虹櫧鐨勫畾涔夊涓嬶細</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>绌哄瓧绗︿覆锛歿@code ""}</li>
	 *     <li>绌烘牸銆佸叏瑙掔┖鏍笺€佸埗琛ㄧ銆佹崲琛岀锛岀瓑涓嶅彲瑙佸瓧绗?/li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code StrUtil.isBlankIfStr(null)     // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr("")       // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isBlankIfStr("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isEmptyIfStr(Object)} 鐨勫尯鍒槸锛?
	 * 璇ユ柟娉曚細鏍￠獙绌虹櫧瀛楃锛屼笖鎬ц兘鐩稿浜?{@link #isEmptyIfStr(Object)} 鐣ユ參銆?/p>
	 *
	 * @param obj 瀵硅薄
	 * @return 濡傛灉涓哄瓧绗︿覆鏄惁涓虹┖涓?
	 * @see StrUtil#isBlank(CharSequence)
	 * @since 3.3.0
	 */
	public static boolean isBlankIfStr(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj instanceof CharSequence) {
			return isBlank((CharSequence) obj);
		}
		return false;
	}
	// ------------------------------------------------------------------------ Empty

	/**
	 * <p>濡傛灉瀵硅薄鏄瓧绗︿覆鏄惁涓虹┖涓诧紝绌虹殑瀹氫箟濡備笅锛?/p><br>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>绌哄瓧绗︿覆锛歿@code ""}</li>
	 * </ol>
	 *
	 * <p>渚嬶細</p>
	 * <ul>
	 *     <li>{@code StrUtil.isEmptyIfStr(null)     // true}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr("")       // true}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isEmptyIfStr("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>娉ㄦ剰锛氳鏂规硶涓?{@link #isBlankIfStr(Object)} 鐨勫尯鍒槸锛氳鏂规硶涓嶆牎楠岀┖鐧藉瓧绗︺€?/p>
	 *
	 * @param obj 瀵硅薄
	 * @return 濡傛灉涓哄瓧绗︿覆鏄惁涓虹┖涓?
	 * @since 3.3.0
	 */
	public static boolean isEmptyIfStr(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj instanceof CharSequence) {
			return 0 == ((CharSequence) obj).length();
		}
		return false;
	}

	// ------------------------------------------------------------------------ Trim

	/**
	 * 缁欏畾瀛楃涓叉暟缁勫叏閮ㄥ仛鍘婚灏剧┖鏍?
	 *
	 * @param strs 瀛楃涓叉暟缁?
	 */
	public static void trim(String[] strs) {
		if (null == strs) {
			return;
		}
		String str;
		for (int i = 0; i < strs.length; i++) {
			str = strs[i];
			if (null != str) {
				strs[i] = trim(str);
			}
		}
	}

	/**
	 * 灏嗗璞¤浆涓哄瓧绗︿覆<br>
	 *
	 * <pre>
	 * 1銆丅yte鏁扮粍鍜孊yteBuffer浼氳杞崲涓哄搴斿瓧绗︿覆鐨勬暟缁?
	 * 2銆佸璞℃暟缁勪細璋冪敤Arrays.toString鏂规硶
	 * </pre>
	 *
	 * @param obj 瀵硅薄
	 * @return 瀛楃涓?
	 */
	public static String utf8Str(Object obj) {
		return str(obj, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 灏嗗璞¤浆涓哄瓧绗︿覆
	 *
	 * <pre>
	 * 1銆丅yte鏁扮粍鍜孊yteBuffer浼氳杞崲涓哄搴斿瓧绗︿覆鐨勬暟缁?
	 * 2銆佸璞℃暟缁勪細璋冪敤Arrays.toString鏂规硶
	 * </pre>
	 *
	 * @param obj         瀵硅薄
	 * @param charsetName 瀛楃闆?
	 * @return 瀛楃涓?
	 * @deprecated 璇蜂娇鐢?{@link #str(Object, Charset)}
	 */
	@Deprecated
	public static String str(Object obj, String charsetName) {
		return str(obj, Charset.forName(charsetName));
	}

	/**
	 * 灏嗗璞¤浆涓哄瓧绗︿覆
	 * <pre>
	 * 	 1銆丅yte鏁扮粍鍜孊yteBuffer浼氳杞崲涓哄搴斿瓧绗︿覆鐨勬暟缁?
	 * 	 2銆佸璞℃暟缁勪細璋冪敤Arrays.toString鏂规硶
	 * </pre>
	 *
	 * @param obj     瀵硅薄
	 * @param charset 瀛楃闆?
	 * @return 瀛楃涓?
	 */
	public static String str(Object obj, Charset charset) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return str((byte[]) obj, charset);
		} else if (obj instanceof Byte[]) {
			return str((Byte[]) obj, charset);
		} else if (obj instanceof ByteBuffer) {
			return str((ByteBuffer) obj, charset);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.toString(obj);
		}

		return obj.toString();
	}

	/**
	 * 灏哹yte鏁扮粍杞负瀛楃涓?
	 *
	 * @param bytes   byte鏁扮粍
	 * @param charset 瀛楃闆?
	 * @return 瀛楃涓?
	 */
	public static String str(byte[] bytes, String charset) {
		return str(bytes, CharsetUtil.charset(charset));
	}

	/**
	 * 瑙ｇ爜瀛楄妭鐮?
	 *
	 * @param data    byte鏁扮粍
	 * @param charset 瀛楃闆嗭紝濡傛灉姝ゅ瓧娈典负绌猴紝鍒欒В鐮佺殑缁撴灉鍙栧喅浜庡钩鍙?
	 * @return 瑙ｇ爜鍚庣殑瀛楃涓?
	 */
	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * 灏咮yte鏁扮粍杞负瀛楃涓?
	 *
	 * @param bytes   byte鏁扮粍
	 * @param charset 瀛楃闆?
	 * @return 瀛楃涓?
	 */
	public static String str(Byte[] bytes, String charset) {
		return str(bytes, CharsetUtil.charset(charset));
	}

	/**
	 * 瑙ｇ爜瀛楄妭鐮?
	 *
	 * @param data    Byte鏁扮粍
	 * @param charset 瀛楃闆嗭紝濡傛灉姝ゅ瓧娈典负绌猴紝鍒欒В鐮佺殑缁撴灉鍙栧喅浜庡钩鍙?
	 * @return 瑙ｇ爜鍚庣殑瀛楃涓?
	 */
	public static String str(Byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		byte[] bytes = new byte[data.length];
		Byte dataByte;
		for (int i = 0; i < data.length; i++) {
			dataByte = data[i];
			bytes[i] = (null == dataByte) ? -1 : dataByte;
		}

		return str(bytes, charset);
	}

	/**
	 * 灏嗙紪鐮佺殑byteBuffer鏁版嵁杞崲涓哄瓧绗︿覆
	 *
	 * @param data    鏁版嵁
	 * @param charset 瀛楃闆嗭紝濡傛灉涓虹┖浣跨敤褰撳墠绯荤粺瀛楃闆?
	 * @return 瀛楃涓?
	 */
	public static String str(ByteBuffer data, String charset) {
		if (data == null) {
			return null;
		}

		return str(data, CharsetUtil.charset(charset));
	}

	/**
	 * 灏嗙紪鐮佺殑byteBuffer鏁版嵁杞崲涓哄瓧绗︿覆
	 *
	 * @param data    鏁版嵁
	 * @param charset 瀛楃闆嗭紝濡傛灉涓虹┖浣跨敤褰撳墠绯荤粺瀛楃闆?
	 * @return 瀛楃涓?
	 */
	public static String str(ByteBuffer data, Charset charset) {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		return charset.decode(data.duplicate()).toString();
	}

	/**
	 * 璋冪敤瀵硅薄鐨則oString鏂规硶锛宯ull浼氳繑鍥炩€渘ull鈥?
	 *
	 * @param obj 瀵硅薄
	 * @return 瀛楃涓?
	 * @see String#valueOf(Object)
	 * @since 4.1.3
	 */
	public static String toString(Object obj) {
		return String.valueOf(obj);
	}

	/**
	 * 璋冪敤瀵硅薄鐨則oString鏂规硶锛宯ull浼氳繑鍥瀧@code null}
	 *
	 * @param obj 瀵硅薄
	 * @return 瀛楃涓?or {@code null}
	 * @since 5.7.17
	 */
	public static String toStringOrNull(Object obj) {
		return null == obj ? null : obj.toString();
	}

	/**
	 * 璋冪敤瀵硅薄鐨則oString鏂规硶锛宯ull浼氳繑鍥炵┖瀛楃涓?""
	 *
	 * @param obj 瀵硅薄
	 * @return {@link String }
	 * @author Junwei Xu
	 */
	public static String toStringOrEmpty(Object obj) {
		// obj涓虹┖鏃? 杩斿洖 null 鎴?"null" 閮戒笉閫傜敤閮ㄥ垎鍦烘櫙, 姝ゅ杩斿洖 "" 绌哄瓧绗︿覆
		return null == obj ? EMPTY : obj.toString();
	}

	/**
	 * 鍒涘缓StringBuilder瀵硅薄
	 *
	 * @return StringBuilder瀵硅薄
	 */
	public static StringBuilder builder() {
		return new StringBuilder();
	}

	/**
	 * 鍒涘缓StrBuilder瀵硅薄
	 *
	 * @return StrBuilder瀵硅薄
	 * @since 4.0.1
	 */
	public static StrBuilder strBuilder() {
		return StrBuilder.create();
	}

	/**
	 * 鍒涘缓StringBuilder瀵硅薄
	 *
	 * @param capacity 鍒濆澶у皬
	 * @return StringBuilder瀵硅薄
	 */
	public static StringBuilder builder(int capacity) {
		return new StringBuilder(capacity);
	}

	/**
	 * 鍒涘缓StrBuilder瀵硅薄
	 *
	 * @param capacity 鍒濆澶у皬
	 * @return StrBuilder瀵硅薄
	 * @since 4.0.1
	 */
	public static StrBuilder strBuilder(int capacity) {
		return StrBuilder.create(capacity);
	}

	/**
	 * 鑾峰緱StringReader
	 *
	 * @param str 瀛楃涓?
	 * @return StringReader
	 */
	public static StringReader getReader(CharSequence str) {
		if (null == str) {
			return null;
		}
		return new StringReader(str.toString());
	}

	/**
	 * 鑾峰緱StringWriter
	 *
	 * @return StringWriter
	 */
	public static StringWriter getWriter() {
		return new StringWriter();
	}

	/**
	 * 鍙嶈浆瀛楃涓?br>
	 * 渚嬪锛歛bcd =銆媎cba
	 *
	 * @param str 琚弽杞殑瀛楃涓?
	 * @return 鍙嶈浆鍚庣殑瀛楃涓?
	 * @since 3.0.9
	 */
	public static String reverse(final String str) {
		if (isBlank(str)) {
			return str;
		}
		return new String(ArrayUtil.reverse(str.toCharArray()));
	}

	/**
	 * 鍙嶈浆瀛楃涓?br>
	 * 渚嬪锛歛bcd =銆媎cba
	 * <p>
	 * 璇ユ柟娉曟寜Unicode code point杩涜鍙嶈浆锛屾敮鎸乁nicode瀛楃鐨勬纭弽杞?
	 * 纭繚澶嶆潅瀛楃涓嶄細琚媶鍒嗭紝濡傝〃鎯呯鍙风瓑澶氬瓧鑺傚瓧绗?
	 * </p>
	 *
	 * @param str 琚弽杞殑瀛楃涓?
	 * @return 鍙嶈浆鍚庣殑瀛楃涓诧紝濡傛灉杈撳叆涓簄ull鍒欒繑鍥瀗ull
	 * @since 5.8.43
	 */
	public static String reverseByCodePoint(String str) {
		if (null == str) {
			return null;
		}

		//鎸塙nicode code point鏂瑰紡杩涜鍙嶈浆澶勭悊
		StringBuilder result = new StringBuilder();
		for (int i = str.length(); i > 0; ) {
			//鑾峰彇鎸囧畾浣嶇疆鍓嶇殑code point
			int codePoint = str.codePointBefore(i);
			//鏍规嵁code point鐨勫瓧绗︽暟閲忚皟鏁寸储寮曚綅缃?
			i -= Character.charCount(codePoint);
			//灏哻ode point杩藉姞鍒扮粨鏋滀腑
			result.appendCodePoint(codePoint);
		}

		return result.toString();
	}

	// ------------------------------------------------------------------------ fill

	/**
	 * 灏嗗凡鏈夊瓧绗︿覆濉厖涓鸿瀹氶暱搴︼紝濡傛灉宸叉湁瀛楃涓茶秴杩囪繖涓暱搴﹀垯杩斿洖杩欎釜瀛楃涓?br>
	 * 瀛楃濉厖浜庡瓧绗︿覆鍓?
	 *
	 * @param str        琚～鍏呯殑瀛楃涓?
	 * @param filledChar 濉厖鐨勫瓧绗?
	 * @param len        濉厖闀垮害
	 * @return 濉厖鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String fillBefore(String str, char filledChar, int len) {
		return fill(str, filledChar, len, true);
	}

	/**
	 * 灏嗗凡鏈夊瓧绗︿覆濉厖涓鸿瀹氶暱搴︼紝濡傛灉宸叉湁瀛楃涓茶秴杩囪繖涓暱搴﹀垯杩斿洖杩欎釜瀛楃涓?br>
	 * 瀛楃濉厖浜庡瓧绗︿覆鍚?
	 *
	 * @param str        琚～鍏呯殑瀛楃涓?
	 * @param filledChar 濉厖鐨勫瓧绗?
	 * @param len        濉厖闀垮害
	 * @return 濉厖鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String fillAfter(String str, char filledChar, int len) {
		return fill(str, filledChar, len, false);
	}

	/**
	 * 灏嗗凡鏈夊瓧绗︿覆濉厖涓鸿瀹氶暱搴︼紝濡傛灉宸叉湁瀛楃涓茶秴杩囪繖涓暱搴﹀垯杩斿洖杩欎釜瀛楃涓?
	 *
	 * @param str        琚～鍏呯殑瀛楃涓?
	 * @param filledChar 濉厖鐨勫瓧绗?
	 * @param len        濉厖闀垮害
	 * @param isPre      鏄惁濉厖鍦ㄥ墠
	 * @return 濉厖鍚庣殑瀛楃涓?
	 * @since 3.1.2
	 */
	public static String fill(String str, char filledChar, int len, boolean isPre) {
		if (null == str) {
			str = "";
		}
		final int strLen = str.length();
		if (strLen > len) {
			return str;
		}

		String filledStr = StrUtil.repeat(filledChar, len - strLen);
		return isPre ? filledStr.concat(str) : str.concat(filledStr);
	}

	/**
	 * 璁＄畻涓や釜瀛楃涓茬殑鐩镐技搴?
	 *
	 * @param str1 瀛楃涓?
	 * @param str2 瀛楃涓?
	 * @return 鐩镐技搴?
	 * @since 3.2.3
	 */
	public static double similar(String str1, String str2) {
		if (str1 == null || str2 == null) return 0;
		if (str1.equals(str2)) return 1.0;
		final int maxLen = Math.max(str1.length(), str2.length());
		if (maxLen == 0) return 1.0;
		int distance = 0;
		final char[] chars1 = str1.toCharArray();
		final char[] chars2 = str2.toCharArray();
		final int minLen = Math.min(chars1.length, chars2.length);
		for (int i = 0; i < minLen; i++) {
			if (chars1[i] != chars2[i]) distance++;
		}
		distance += Math.abs(chars1.length - chars2.length);
		return 1.0 - (double) distance / maxLen;
	}

	/**
	 * 璁＄畻涓や釜瀛楃涓茬殑鐩镐技搴︾櫨鍒嗘瘮
	 *
	 * @param str1  瀛楃涓?
	 * @param str2  瀛楃涓?
	 * @param scale 鐩镐技搴?
	 * @return 鐩镐技搴︾櫨鍒嗘瘮
	 * @since 3.2.3
	 */
	public static String similar(String str1, String str2, int scale) {
		final double similar = similar(str1, str2);
		return String.format("%." + scale + "f", similar * 100) + "%";
	}

	/**
	 * 鐢熸垚闅忔満UUID
	 *
	 * @return UUID瀛楃涓?
	 * @see IdUtil#randomUUID()
	 * @since 4.0.10
	 */
	public static String uuid() {
		return IdUtil.randomUUID();
	}

	/**
	 * 鏍煎紡鍖栨枃鏈紝浣跨敤 {varName} 鍗犱綅<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=銆?aValue and bValue
	 *
	 * @param template 鏂囨湰妯℃澘锛岃鏇挎崲鐨勯儴鍒嗙敤 {key} 琛ㄧず
	 * @param map      鍙傛暟鍊煎
	 * @return 鏍煎紡鍖栧悗鐨勬枃鏈?
	 */
	public static String format(CharSequence template, Map<?, ?> map) {
		return format(template, map, true);
	}

	/**
	 * 鏍煎紡鍖栨枃鏈紝浣跨敤 {varName} 鍗犱綅<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=銆?aValue and bValue
	 *
	 * @param template   鏂囨湰妯℃澘锛岃鏇挎崲鐨勯儴鍒嗙敤 {key} 琛ㄧず
	 * @param map        鍙傛暟鍊煎
	 * @param ignoreNull 鏄惁蹇界暐 {@code null} 鍊硷紝蹇界暐鍒?{@code null} 鍊煎搴旂殑鍙橀噺涓嶈鏇挎崲锛屽惁鍒欐浛鎹负""
	 * @return 鏍煎紡鍖栧悗鐨勬枃鏈?
	 * @since 5.4.3
	 */
	public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
		if (template == null) return null;
		String result = template.toString();
		if (map != null) {
			for (java.util.Map.Entry<?, ?> entry : map.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (ignoreNull && value == null) continue;
				String placeholder = "{" + key + "}";
				result = result.replace(placeholder, value != null ? value.toString() : "");
			}
		}
		return result;
	}

	/**
	 * 鎴柇瀛楃涓诧紝浣跨敤鍏舵寜鐓TF-8缂栫爜涓哄瓧鑺傚悗涓嶈秴杩噈axBytes闀垮害銆傛埅鏂悗鑷姩杩藉姞鐪佺暐鍙?...)
	 * 鐢ㄤ簬瀛樺偍鏁版嵁搴搗archar涓旂紪鐮佷负UTF-8鐨勫瓧娈?
	 *
	 * @param str      java瀛楃涓?
	 * @param maxBytes 鏈€澶у瓧鑺傞暱搴?
	 * @return 鎴柇鍚庣殑瀛楃
	 */
	public static String truncateUtf8(String str, int maxBytes) {
		Charset charset = StandardCharsets.UTF_8;
		//UTF-8缂栫爜鍗曚釜瀛楃鏈€澶ч暱搴?
		return truncateByByteLength(str, charset, maxBytes, 4, true);
	}

	/**
	 * 鎴柇瀛楃涓诧紝浣跨敤鍏舵寜鐓ф寚瀹氱紪鐮佷负瀛楄妭鍚庝笉瓒呰繃maxBytes闀垮害<br>
	 * 姝ゆ柟娉曠敤浜庢埅鍙栨€籦ytes鏁颁笉瓒呰繃鎸囧畾闀垮害锛屽鏋滃瓧绗﹀嚭娌℃湁瓒呭嚭鍘熸牱杈撳嚭锛屽鏋滆秴鍑轰簡锛屽垯鎴彇鎺夎秴鍑洪儴鍒嗭紝骞跺彲閫夋坊鍔?..锛?
	 * 浣嗘槸娣诲姞鈥?..鈥濆悗鎬婚暱搴︿篃涓嶈秴杩囬檺鍒堕暱搴︺€?
	 *
	 * @param str            鍘熷瀛楃涓?
	 * @param charset        鎸囧畾缂栫爜
	 * @param maxBytesLength 鏈€澶у瓧鑺傛暟
	 * @param factor         閫熺畻鍥犲瓙锛屽彇璇ョ紪鐮佷笅鍗曚釜瀛楃鐨勬渶澶у彲鑳藉瓧鑺傛暟
	 * @param appendDots     鎴柇鍚庢槸鍚﹁拷鍔犵渷鐣ュ彿...锛屽鏋渕axBytesLength灏忎簬鐪佺暐鍙烽暱搴︼紝鍒欎笉娣诲姞...
	 * @return 鎴柇鍚庣殑瀛楃涓?
	 */
	public static String truncateByByteLength(String str, Charset charset, int maxBytesLength, int factor,
											  boolean appendDots) {
		//瀛楃鏁?閫熺畻鍥犲瓙<=鏈€澶у瓧鑺傛暟
		if (str == null || str.length() * factor <= maxBytesLength) {
			return str;
		}
		final byte[] sba = str.getBytes(charset);
		if (sba.length <= maxBytesLength) {
			return str;
		}
		//闄愬埗瀛楄妭鏁?
		final int dotsBytesLength = "...".getBytes(charset).length;
		final int limitBytes;
		// issue#IDFTJS 淇鎴柇鍚庤拷鍔犵渷鐣ュ彿...瀵艰嚧瓒呭嚭闄愬埗闀垮害鐨勯棶棰?
		if (appendDots && maxBytesLength > dotsBytesLength) {
			limitBytes = maxBytesLength - "...".getBytes(charset).length;
		} else {
			limitBytes = maxBytesLength;
		}
		final ByteBuffer bb = ByteBuffer.wrap(sba, 0, limitBytes);
		final CharBuffer cb = CharBuffer.allocate(limitBytes);
		final CharsetDecoder decoder = charset.newDecoder();
		//蹇界暐琚埅鏂殑瀛楃
		decoder.onMalformedInput(CodingErrorAction.IGNORE);
		decoder.decode(bb, cb, true);
		decoder.flush(cb);
		final String result = new String(cb.array(), 0, cb.position());
		if (appendDots && maxBytesLength > dotsBytesLength) {
			return result + "...";
		}
		return result;
	}

	// NamingCase toSymbolCase implementation moved to public toSymbolCase above

	/**
	 * 将连接符方式命名的字符串转换为驼峰式。
	 *
	 * @param name   转换前的自定义方式命名的字符串
	 * @param symbol 原字符串中的连接符
	 * @return 转换后的驼峰式命名的字符串
	 */
	private static String toCamelCaseInternal(CharSequence name, char symbol) {
		if (null == name) {
			return null;
		}
		final String name2 = name.toString();
		if (name2.indexOf(symbol) != -1) {
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
					sb.append(Character.toLowerCase(c));
				}
			}
			return sb.toString();
		} else {
			return name2;
		}
	}

	// ==================== stringBuilder methods ====================

	/**
	 * 创建StringBuilder对象
	 *
	 * @return StringBuilder对象
	 */
	public static StringBuilder stringBuilder() {
		return new StringBuilder();
	}

	/**
	 * 创建StringBuilder对象
	 *
	 * @param capacity 初始容量
	 * @return StringBuilder对象
	 */
	public static StringBuilder stringBuilder(int capacity) {
		return new StringBuilder(capacity);
	}

	/**
	 * char[] 转 String
	 *
	 * @param chars 字符数组
	 * @return 字符串
	 */
	public static String str(char[] chars) {
		return chars == null ? null : new String(chars);
	}


}
