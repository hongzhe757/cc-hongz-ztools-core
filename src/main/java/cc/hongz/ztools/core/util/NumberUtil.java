package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.text.Convert;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.math.Calculator;
import cc.hongz.ztools.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 鏁板瓧宸ュ叿绫?br>
 * 瀵逛簬绮剧‘鍊艰绠楀簲璇ヤ娇鐢?{@link BigDecimal}<br>
 * JDK7涓?strong>BigDecimal(double val)</strong>鏋勯€犳柟娉曠殑缁撴灉鏈変竴瀹氱殑涓嶅彲棰勭煡鎬э紝渚嬪锛?
 *
 * <pre>
 * new BigDecimal(0.1)
 * </pre>
 * <p>
 * 琛ㄧず鐨勪笉鏄?strong>0.1</strong>鑰屾槸<strong>0.1000000000000000055511151231257827021181583404541015625</strong>
 *
 * <p>
 * 杩欐槸鍥犱负0.1鏃犳硶鍑嗙‘鐨勮〃绀轰负double銆傚洜姝ゅ簲璇ヤ娇鐢?strong>new BigDecimal(String)</strong>銆?
 * </p>
 * 鐩稿叧浠嬬粛锛?
 * <ul>
 * <li>http://www.oschina.net/code/snippet_563112_25237</li>
 * <li>https://github.com/venusdrogon/feilong-core/wiki/one-jdk7-bug-thinking</li>
 * </ul>
 *
 * @author Looly
 */
public class NumberUtil {

	/**
	 * 榛樿闄ゆ硶杩愮畻绮惧害
	 */
	private static final int DEFAULT_DIV_SCALE = 10;

	/**
	 * 0-20瀵瑰簲鐨勯樁涔橈紝瓒呰繃20鐨勯樁涔樹細瓒呰繃Long.MAX_VALUE
	 */
	private static final long[] FACTORIALS = new long[]{
		1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L,
		87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L,
		2432902008176640000L};

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(float v1, float v2) {
		return add(Float.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(float v1, double v2) {
		return add(Float.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(double v1, float v2) {
		return add(Double.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(double v1, double v2) {
		return add(Double.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(long v1, double v2) {
		return add(Long.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(double v1, long v2) {
		return add(Double.toString(v1), Long.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static double add(long v1, long v2) {
		return add(Long.toString(v1), Long.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 * @since 3.1.1
	 */
	public static double add(Double v1, Double v2) {
		//noinspection RedundantCast
		return add((Number) v1, (Number) v2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * <p>
	 * 闇€瑕佹敞鎰忕殑鏄紝鍦ㄤ笉鍚孡ocale涓嬶紝鏁板瓧鐨勮〃绀哄舰寮忎篃鏄笉鍚岀殑锛屼緥濡傦細<br>
	 * 寰峰浗銆佽嵎鍏般€佹瘮鍒╂椂銆佷腹楹︺€佹剰澶у埄銆佺綏椹凹浜氬拰娆ф床澶у鍦板尯浣跨敤`,`鍖哄垎灏忔暟<br>
	 * 涔熷氨鏄锛屽湪杩欎簺鍥藉鍦板尯锛?.20琛ㄧず120锛岃€岄潪1.2銆?
	 * </p>
	 *
	 * @param v1 琚姞鏁?
	 * @param v2 鍔犳暟
	 * @return 鍜?
	 */
	public static BigDecimal add(Number v1, Number v2) {
		return add(new Number[]{v1, v2});
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * <p>
	 * 闇€瑕佹敞鎰忕殑鏄紝鍦ㄤ笉鍚孡ocale涓嬶紝鏁板瓧鐨勮〃绀哄舰寮忎篃鏄笉鍚岀殑锛屼緥濡傦細<br>
	 * 寰峰浗銆佽嵎鍏般€佹瘮鍒╂椂銆佷腹楹︺€佹剰澶у埄銆佺綏椹凹浜氬拰娆ф床澶у鍦板尯浣跨敤`,`鍖哄垎灏忔暟<br>
	 * 涔熷氨鏄锛屽湪杩欎簺鍥藉鍦板尯锛?.20琛ㄧず120锛岃€岄潪1.2銆?
	 * </p>
	 *
	 * @param values 澶氫釜琚姞鍊?
	 * @return 鍜?
	 * @since 4.0.0
	 */
	public static BigDecimal add(Number... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.add(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚姞鍊?
	 * @return 鍜?
	 * @since 4.0.0
	 */
	public static BigDecimal add(String... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		String value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (StrUtil.isNotBlank(value)) {
				result = result.add(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚姞鍊?
	 * @return 鍜?
	 * @since 4.0.0
	 */
	public static BigDecimal add(BigDecimal... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		BigDecimal value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.add(value);
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static double sub(float v1, float v2) {
		return sub(Float.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static double sub(float v1, double v2) {
		return sub(Float.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static double sub(double v1, float v2) {
		return sub(Double.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static double sub(double v1, double v2) {
		return sub(Double.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static double sub(Double v1, Double v2) {
		//noinspection RedundantCast
		return sub((Number) v1, (Number) v2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param v1 琚噺鏁?
	 * @param v2 鍑忔暟
	 * @return 宸?
	 */
	public static BigDecimal sub(Number v1, Number v2) {
		return sub(new Number[]{v1, v2});
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚噺鍊?
	 * @return 宸?
	 * @since 4.0.0
	 */
	public static BigDecimal sub(Number... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.subtract(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚噺鍊?
	 * @return 宸?
	 * @since 4.0.0
	 */
	public static BigDecimal sub(String... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		String value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (StrUtil.isNotBlank(value)) {
				result = result.subtract(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚噺鍊?
	 * @return 宸?
	 * @since 4.0.0
	 */
	public static BigDecimal sub(BigDecimal... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		BigDecimal value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.subtract(value);
			}
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static double mul(float v1, float v2) {
		return mul(Float.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static double mul(float v1, double v2) {
		return mul(Float.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static double mul(double v1, float v2) {
		return mul(Double.toString(v1), Float.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static double mul(double v1, double v2) {
		return mul(Double.toString(v1), Double.toString(v2)).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static double mul(Double v1, Double v2) {
		//noinspection RedundantCast
		return mul((Number) v1, (Number) v2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 */
	public static BigDecimal mul(Number v1, Number v2) {
		return mul(new Number[]{v1, v2});
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚箻鍊?
	 * @return 绉?
	 * @since 4.0.0
	 */
	public static BigDecimal mul(Number... values) {
		if (ArrayUtil.isEmpty(values) || ArrayUtil.hasNull(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = toBigDecimal(value.toString());
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			result = result.multiply(toBigDecimal(value.toString()));
		}
		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?
	 *
	 * @param v1 琚箻鏁?
	 * @param v2 涔樻暟
	 * @return 绉?
	 * @since 3.0.8
	 */
	public static BigDecimal mul(String v1, String v2) {
		return mul(toBigDecimal(v1), toBigDecimal(v2));
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚箻鍊?
	 * @return 绉?
	 * @since 4.0.0
	 */
	public static BigDecimal mul(String... values) {
		if (ArrayUtil.isEmpty(values) || ArrayUtil.hasNull(values)) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = toBigDecimal(values[0]);
		for (int i = 1; i < values.length; i++) {
			result = result.multiply(toBigDecimal(values[i]));
		}

		return result;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠?br>
	 * 濡傛灉浼犲叆澶氫釜鍊间负null鎴栬€呯┖锛屽垯杩斿洖0
	 *
	 * @param values 澶氫釜琚箻鍊?
	 * @return 绉?
	 * @since 4.0.0
	 */
	public static BigDecimal mul(BigDecimal... values) {
		if (ArrayUtil.isEmpty(values) || ArrayUtil.hasNull(values)) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = values[0];
		for (int i = 1; i < values.length; i++) {
			result = result.multiply(values[i]);
		}
		return result;
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, float v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, float v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(Double v1, Double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 * @since 3.1.0
	 */
	public static BigDecimal div(Number v1, Number v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐电殑鏃跺€?绮剧‘鍒板皬鏁扮偣鍚?0浣?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static BigDecimal div(String v1, String v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, float v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, double v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, float v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, double v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(Double v1, Double v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 * @since 3.1.0
	 */
	public static BigDecimal div(Number v1, Number v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?鍚庨潰鐨勫洓鑸嶄簲鍏?
	 *
	 * @param v1    琚櫎鏁?
	 * @param v2    闄ゆ暟
	 * @param scale 绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static BigDecimal div(String v1, String v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
		return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(float v1, double v2, int scale, RoundingMode roundingMode) {
		return div(Float.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
		return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
		return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(Double v1, Double v2, int scale, RoundingMode roundingMode) {
		//noinspection RedundantCast
		return div((Number) v1, (Number) v2, scale, roundingMode).doubleValue();
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 * @since 3.1.0
	 */
	public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
		if (v1 instanceof BigDecimal && v2 instanceof BigDecimal) {
			return div((BigDecimal) v1, (BigDecimal) v2, scale, roundingMode);
		}
		return div(StrUtil.toStringOrNull(v1), StrUtil.toStringOrNull(v2), scale, roundingMode);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
		return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
	}

	/**
	 * 鎻愪緵(鐩稿)绮剧‘鐨勯櫎娉曡繍绠?褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂,鐢眘cale鎸囧畾绮剧‘搴?
	 *
	 * @param v1           琚櫎鏁?
	 * @param v2           闄ゆ暟
	 * @param scale        绮剧‘搴︼紝濡傛灉涓鸿礋鍊硷紝鍙栫粷瀵瑰€?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 涓や釜鍙傛暟鐨勫晢
	 * @since 3.0.9
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
		Assert.notNull(v2, "Divisor must be not null !");
		if (null == v1) {
			return BigDecimal.ZERO;
		}
		if (scale < 0) {
			scale = -scale;
		}
		return v1.divide(v2, scale, roundingMode);
	}

	/**
	 * 琛ュ厖Math.ceilDiv() JDK8涓坊鍔犱簡鍜孧ath.floorDiv()浣嗗嵈娌℃湁ceilDiv()
	 *
	 * @param v1 琚櫎鏁?
	 * @param v2 闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 * @since 5.3.3
	 */
	public static int ceilDiv(int v1, int v2) {
		return (int) Math.ceil((double) v1 / v2);
	}

	// ------------------------------------------------------------------------------------------- round

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 閲囩敤鍥涜垗浜斿叆绛栫暐 {@link RoundingMode#HALF_UP}<br>
	 * 渚嬪淇濈暀2浣嶅皬鏁帮細123.456789 =銆?123.46
	 *
	 * @param v     鍊?
	 * @param scale 淇濈暀灏忔暟浣嶆暟
	 * @return 鏂板€?
	 */
	public static BigDecimal round(double v, int scale) {
		return round(v, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 閲囩敤鍥涜垗浜斿叆绛栫暐 {@link RoundingMode#HALF_UP}<br>
	 * 渚嬪淇濈暀2浣嶅皬鏁帮細123.456789 =銆?123.46
	 *
	 * @param v     鍊?
	 * @param scale 淇濈暀灏忔暟浣嶆暟
	 * @return 鏂板€?
	 */
	public static String roundStr(double v, int scale) {
		return round(v, scale).toPlainString();
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 閲囩敤鍥涜垗浜斿叆绛栫暐 {@link RoundingMode#HALF_UP}<br>
	 * 渚嬪淇濈暀2浣嶅皬鏁帮細123.456789 =銆?123.46
	 *
	 * @param numberStr 鏁板瓧鍊肩殑瀛楃涓茶〃鐜板舰寮?
	 * @param scale     淇濈暀灏忔暟浣嶆暟
	 * @return 鏂板€?
	 */
	public static BigDecimal round(String numberStr, int scale) {
		return round(numberStr, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 閲囩敤鍥涜垗浜斿叆绛栫暐 {@link RoundingMode#HALF_UP}<br>
	 * 渚嬪淇濈暀2浣嶅皬鏁帮細123.456789 =銆?123.46
	 *
	 * @param number 鏁板瓧鍊?
	 * @param scale  淇濈暀灏忔暟浣嶆暟
	 * @return 鏂板€?
	 * @since 4.1.0
	 */
	public static BigDecimal round(BigDecimal number, int scale) {
		return round(number, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 閲囩敤鍥涜垗浜斿叆绛栫暐 {@link RoundingMode#HALF_UP}<br>
	 * 渚嬪淇濈暀2浣嶅皬鏁帮細123.456789 =銆?123.46
	 *
	 * @param numberStr 鏁板瓧鍊肩殑瀛楃涓茶〃鐜板舰寮?
	 * @param scale     淇濈暀灏忔暟浣嶆暟
	 * @return 鏂板€?
	 * @since 3.2.2
	 */
	public static String roundStr(String numberStr, int scale) {
		return round(numberStr, scale).toPlainString();
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 渚嬪淇濈暀鍥涗綅灏忔暟锛?23.456789 =銆?123.4567
	 *
	 * @param v            鍊?
	 * @param scale        淇濈暀灏忔暟浣嶆暟
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 鏂板€?
	 */
	public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
		return round(Double.toString(v), scale, roundingMode);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 渚嬪淇濈暀鍥涗綅灏忔暟锛?23.456789 =銆?123.4567
	 *
	 * @param v            鍊?
	 * @param scale        淇濈暀灏忔暟浣嶆暟
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 鏂板€?
	 * @since 3.2.2
	 */
	public static String roundStr(double v, int scale, RoundingMode roundingMode) {
		return round(v, scale, roundingMode).toPlainString();
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 渚嬪淇濈暀鍥涗綅灏忔暟锛?23.456789 =銆?123.4567
	 *
	 * @param numberStr    鏁板瓧鍊肩殑瀛楃涓茶〃鐜板舰寮?
	 * @param scale        淇濈暀灏忔暟浣嶆暟锛屽鏋滀紶鍏ュ皬浜?锛屽垯榛樿0
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}锛屽鏋滀紶鍏ull鍒欓粯璁ゅ洓鑸嶄簲鍏?
	 * @return 鏂板€?
	 */
	public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
		Assert.notBlank(numberStr);
		if (scale < 0) {
			scale = 0;
		}
		return round(toBigDecimal(numberStr), scale, roundingMode);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 渚嬪淇濈暀鍥涗綅灏忔暟锛?23.456789 =銆?123.4567
	 *
	 * @param number       鏁板瓧鍊?
	 * @param scale        淇濈暀灏忔暟浣嶆暟锛屽鏋滀紶鍏ュ皬浜?锛屽垯榛樿0
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}锛屽鏋滀紶鍏ull鍒欓粯璁ゅ洓鑸嶄簲鍏?
	 * @return 鏂板€?
	 */
	public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
		if (null == number) {
			number = BigDecimal.ZERO;
		}
		if (scale < 0) {
			scale = 0;
		}
		if (null == roundingMode) {
			roundingMode = RoundingMode.HALF_UP;
		}

		return number.setScale(scale, roundingMode);
	}

	/**
	 * 淇濈暀鍥哄畾浣嶆暟灏忔暟<br>
	 * 渚嬪淇濈暀鍥涗綅灏忔暟锛?23.456789 =銆?123.4567
	 *
	 * @param numberStr    鏁板瓧鍊肩殑瀛楃涓茶〃鐜板舰寮?
	 * @param scale        淇濈暀灏忔暟浣嶆暟
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode}
	 * @return 鏂板€?
	 * @since 3.2.2
	 */
	public static String roundStr(String numberStr, int scale, RoundingMode roundingMode) {
		return round(numberStr, scale, roundingMode).toPlainString();
	}

	/**
	 * 鍥涜垗鍏叆浜旀垚鍙岃绠楁硶
	 * <p>
	 * 鍥涜垗鍏叆浜旀垚鍙屾槸涓€绉嶆瘮杈冪簿纭瘮杈冪瀛︾殑璁℃暟淇濈暀娉曪紝鏄竴绉嶆暟瀛椾慨绾﹁鍒欍€?
	 * </p>
	 *
	 * <pre>
	 * 绠楁硶瑙勫垯:
	 * 鍥涜垗鍏叆浜旇€冭檻锛?
	 * 浜斿悗闈為浂灏辫繘涓€锛?
	 * 浜斿悗鐨嗛浂鐪嬪鍋讹紝
	 * 浜斿墠涓哄伓搴旇垗鍘伙紝
	 * 浜斿墠涓哄瑕佽繘涓€銆?
	 * </pre>
	 *
	 * @param number 闇€瑕佺瀛﹁绠楃殑鏁版嵁
	 * @param scale  淇濈暀鐨勫皬鏁颁綅
	 * @return 缁撴灉
	 * @since 4.1.0
	 */
	public static BigDecimal roundHalfEven(Number number, int scale) {
		return roundHalfEven(toBigDecimal(number), scale);
	}

	/**
	 * 鍥涜垗鍏叆浜旀垚鍙岃绠楁硶
	 * <p>
	 * 鍥涜垗鍏叆浜旀垚鍙屾槸涓€绉嶆瘮杈冪簿纭瘮杈冪瀛︾殑璁℃暟淇濈暀娉曪紝鏄竴绉嶆暟瀛椾慨绾﹁鍒欍€?
	 * </p>
	 *
	 * <pre>
	 * 绠楁硶瑙勫垯:
	 * 鍥涜垗鍏叆浜旇€冭檻锛?
	 * 浜斿悗闈為浂灏辫繘涓€锛?
	 * 浜斿悗鐨嗛浂鐪嬪鍋讹紝
	 * 浜斿墠涓哄伓搴旇垗鍘伙紝
	 * 浜斿墠涓哄瑕佽繘涓€銆?
	 * </pre>
	 *
	 * @param value 闇€瑕佺瀛﹁绠楃殑鏁版嵁
	 * @param scale 淇濈暀鐨勫皬鏁颁綅
	 * @return 缁撴灉
	 * @since 4.1.0
	 */
	public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
		return round(value, scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 淇濈暀鍥哄畾灏忔暟浣嶆暟锛岃垗鍘诲浣欎綅鏁?
	 *
	 * @param number 闇€瑕佺瀛﹁绠楃殑鏁版嵁
	 * @param scale  淇濈暀鐨勫皬鏁颁綅
	 * @return 缁撴灉
	 * @since 4.1.0
	 */
	public static BigDecimal roundDown(Number number, int scale) {
		return roundDown(toBigDecimal(number), scale);
	}

	/**
	 * 淇濈暀鍥哄畾灏忔暟浣嶆暟锛岃垗鍘诲浣欎綅鏁?
	 *
	 * @param value 闇€瑕佺瀛﹁绠楃殑鏁版嵁
	 * @param scale 淇濈暀鐨勫皬鏁颁綅
	 * @return 缁撴灉
	 * @since 4.1.0
	 */
	public static BigDecimal roundDown(BigDecimal value, int scale) {
		return round(value, scale, RoundingMode.DOWN);
	}

	// ------------------------------------------------------------------------------------------- decimalFormat

	/**
	 * 鏍煎紡鍖杁ouble<br>
	 * 瀵?{@link DecimalFormat} 鍋氬皝瑁?br>
	 *
	 * @param pattern 鏍煎紡 鏍煎紡涓富瑕佷互 # 鍜?0 涓ょ鍗犱綅绗﹀彿鏉ユ寚瀹氭暟瀛楅暱搴︺€? 琛ㄧず濡傛灉浣嶆暟涓嶈冻鍒欎互 0 濉厖锛? 琛ㄧず鍙鏈夊彲鑳藉氨鎶婃暟瀛楁媺涓婅繖涓綅缃€?br>
	 *                <ul>
	 *                <li>0 =銆?鍙栦竴浣嶆暣鏁?/li>
	 *                <li>0.00 =銆?鍙栦竴浣嶆暣鏁板拰涓や綅灏忔暟</li>
	 *                <li>00.000 =銆?鍙栦袱浣嶆暣鏁板拰涓変綅灏忔暟</li>
	 *                <li># =銆?鍙栨墍鏈夋暣鏁伴儴鍒?/li>
	 *                <li>#.##% =銆?浠ョ櫨鍒嗘瘮鏂瑰紡璁℃暟锛屽苟鍙栦袱浣嶅皬鏁?/li>
	 *                <li>#.#####E0 =銆?鏄剧ず涓虹瀛﹁鏁版硶锛屽苟鍙栦簲浣嶅皬鏁?/li>
	 *                <li>,### =銆?姣忎笁浣嶄互閫楀彿杩涜鍒嗛殧锛屼緥濡傦細299,792,458</li>
	 *                <li>鍏夐€熷ぇ灏忎负姣忕,###绫?=銆?灏嗘牸寮忓祵鍏ユ枃鏈?/li>
	 *                </ul>
	 * @param value   鍊?
	 * @return 鏍煎紡鍖栧悗鐨勫€?
	 */
	public static String decimalFormat(String pattern, double value) {
		Assert.isTrue(isValid(value), "value is NaN or Infinite!");
		return new DecimalFormat(pattern).format(value);
	}

	/**
	 * 鏍煎紡鍖杁ouble<br>
	 * 瀵?{@link DecimalFormat} 鍋氬皝瑁?br>
	 *
	 * @param pattern 鏍煎紡 鏍煎紡涓富瑕佷互 # 鍜?0 涓ょ鍗犱綅绗﹀彿鏉ユ寚瀹氭暟瀛楅暱搴︺€? 琛ㄧず濡傛灉浣嶆暟涓嶈冻鍒欎互 0 濉厖锛? 琛ㄧず鍙鏈夊彲鑳藉氨鎶婃暟瀛楁媺涓婅繖涓綅缃€?br>
	 *                <ul>
	 *                <li>0 =銆?鍙栦竴浣嶆暣鏁?/li>
	 *                <li>0.00 =銆?鍙栦竴浣嶆暣鏁板拰涓や綅灏忔暟</li>
	 *                <li>00.000 =銆?鍙栦袱浣嶆暣鏁板拰涓変綅灏忔暟</li>
	 *                <li># =銆?鍙栨墍鏈夋暣鏁伴儴鍒?/li>
	 *                <li>#.##% =銆?浠ョ櫨鍒嗘瘮鏂瑰紡璁℃暟锛屽苟鍙栦袱浣嶅皬鏁?/li>
	 *                <li>#.#####E0 =銆?鏄剧ず涓虹瀛﹁鏁版硶锛屽苟鍙栦簲浣嶅皬鏁?/li>
	 *                <li>,### =銆?姣忎笁浣嶄互閫楀彿杩涜鍒嗛殧锛屼緥濡傦細299,792,458</li>
	 *                <li>鍏夐€熷ぇ灏忎负姣忕,###绫?=銆?灏嗘牸寮忓祵鍏ユ枃鏈?/li>
	 *                </ul>
	 * @param value   鍊?
	 * @return 鏍煎紡鍖栧悗鐨勫€?
	 * @since 3.0.5
	 */
	public static String decimalFormat(String pattern, long value) {
		return new DecimalFormat(pattern).format(value);
	}

	/**
	 * 鏍煎紡鍖杁ouble<br>
	 * 瀵?{@link DecimalFormat} 鍋氬皝瑁?br>
	 *
	 * @param pattern 鏍煎紡 鏍煎紡涓富瑕佷互 # 鍜?0 涓ょ鍗犱綅绗﹀彿鏉ユ寚瀹氭暟瀛楅暱搴︺€? 琛ㄧず濡傛灉浣嶆暟涓嶈冻鍒欎互 0 濉厖锛? 琛ㄧず鍙鏈夊彲鑳藉氨鎶婃暟瀛楁媺涓婅繖涓綅缃€?br>
	 *                <ul>
	 *                <li>0 =銆?鍙栦竴浣嶆暣鏁?/li>
	 *                <li>0.00 =銆?鍙栦竴浣嶆暣鏁板拰涓や綅灏忔暟</li>
	 *                <li>00.000 =銆?鍙栦袱浣嶆暣鏁板拰涓変綅灏忔暟</li>
	 *                <li># =銆?鍙栨墍鏈夋暣鏁伴儴鍒?/li>
	 *                <li>#.##% =銆?浠ョ櫨鍒嗘瘮鏂瑰紡璁℃暟锛屽苟鍙栦袱浣嶅皬鏁?/li>
	 *                <li>#.#####E0 =銆?鏄剧ず涓虹瀛﹁鏁版硶锛屽苟鍙栦簲浣嶅皬鏁?/li>
	 *                <li>,### =銆?姣忎笁浣嶄互閫楀彿杩涜鍒嗛殧锛屼緥濡傦細299,792,458</li>
	 *                <li>鍏夐€熷ぇ灏忎负姣忕,###绫?=銆?灏嗘牸寮忓祵鍏ユ枃鏈?/li>
	 *                </ul>
	 * @param value   鍊硷紝鏀寔BigDecimal銆丅igInteger銆丯umber绛夌被鍨?
	 * @return 鏍煎紡鍖栧悗鐨勫€?
	 * @since 5.1.6
	 */
	public static String decimalFormat(String pattern, Object value) {
		return decimalFormat(pattern, value, null);
	}

	/**
	 * 鏍煎紡鍖杁ouble<br>
	 * 瀵?{@link DecimalFormat} 鍋氬皝瑁?br>
	 *
	 * @param pattern      鏍煎紡 鏍煎紡涓富瑕佷互 # 鍜?0 涓ょ鍗犱綅绗﹀彿鏉ユ寚瀹氭暟瀛楅暱搴︺€? 琛ㄧず濡傛灉浣嶆暟涓嶈冻鍒欎互 0 濉厖锛? 琛ㄧず鍙鏈夊彲鑳藉氨鎶婃暟瀛楁媺涓婅繖涓綅缃€?br>
	 *                     <ul>
	 *                     <li>0 =銆?鍙栦竴浣嶆暣鏁?/li>
	 *                     <li>0.00 =銆?鍙栦竴浣嶆暣鏁板拰涓や綅灏忔暟</li>
	 *                     <li>00.000 =銆?鍙栦袱浣嶆暣鏁板拰涓変綅灏忔暟</li>
	 *                     <li># =銆?鍙栨墍鏈夋暣鏁伴儴鍒?/li>
	 *                     <li>#.##% =銆?浠ョ櫨鍒嗘瘮鏂瑰紡璁℃暟锛屽苟鍙栦袱浣嶅皬鏁?/li>
	 *                     <li>#.#####E0 =銆?鏄剧ず涓虹瀛﹁鏁版硶锛屽苟鍙栦簲浣嶅皬鏁?/li>
	 *                     <li>,### =銆?姣忎笁浣嶄互閫楀彿杩涜鍒嗛殧锛屼緥濡傦細299,792,458</li>
	 *                     <li>鍏夐€熷ぇ灏忎负姣忕,###绫?=銆?灏嗘牸寮忓祵鍏ユ枃鏈?/li>
	 *                     </ul>
	 * @param value        鍊硷紝鏀寔BigDecimal銆丅igInteger銆丯umber绛夌被鍨?
	 * @param roundingMode 淇濈暀灏忔暟鐨勬柟寮忔灇涓?
	 * @return 鏍煎紡鍖栧悗鐨勫€?
	 * @since 5.6.5
	 */
	public static String decimalFormat(String pattern, Object value, RoundingMode roundingMode) {
		if (value instanceof Number) {
			Assert.isTrue(isValidNumber((Number) value), "value is NaN or Infinite!");
		}
		final DecimalFormat decimalFormat = new DecimalFormat(pattern);
		if (null != roundingMode) {
			decimalFormat.setRoundingMode(roundingMode);
		}
		return decimalFormat.format(value);
	}

	/**
	 * 鏍煎紡鍖栭噾棰濊緭鍑猴紝姣忎笁浣嶇敤閫楀彿鍒嗛殧
	 *
	 * @param value 閲戦
	 * @return 鏍煎紡鍖栧悗鐨勫€?
	 * @since 3.0.9
	 */
	public static String decimalFormatMoney(double value) {
		return decimalFormat(",##0.00", value);
	}

	/**
	 * 鏍煎紡鍖栫櫨鍒嗘瘮锛屽皬鏁伴噰鐢ㄥ洓鑸嶄簲鍏ユ柟寮?
	 *
	 * @param number 鍊?
	 * @param scale  淇濈暀灏忔暟浣嶆暟
	 * @return 鐧惧垎姣?
	 * @since 3.2.3
	 */
	public static String formatPercent(double number, int scale) {
		final NumberFormat format = NumberFormat.getPercentInstance();
		format.setMaximumFractionDigits(scale);
		return format.format(number);
	}

	// ------------------------------------------------------------------------------------------- isXXX

	/**
	 * 鏄惁涓烘暟瀛楋紝鏀寔鍖呮嫭锛?
	 *
	 * <pre>
	 * 1銆?0杩涘埗
	 * 2銆?6杩涘埗鏁板瓧锛?x寮€澶达級
	 * 3銆佺瀛﹁鏁版硶褰㈠紡锛?234E3锛?
	 * 4銆佺被鍨嬫爣璇嗗舰寮忥紙123D锛?
	 * 5銆佹璐熸暟鏍囪瘑褰㈠紡锛?123銆?234锛?
	 * </pre>
	 *
	 * @param str 瀛楃涓插€?
	 * @return 鏄惁涓烘暟瀛?
	 */
	public static boolean isNumber(CharSequence str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		char[] chars = str.toString().toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (false == foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp && !hasDecPoint;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return false == allowSigns && foundDigit;
	}

	/**
	 * 鍒ゆ柇String鏄惁鏄暣鏁?br>
	 * 鏀寔10杩涘埗
	 *
	 * @param s String
	 * @return 鏄惁涓烘暣鏁?
	 */
	public static boolean isInteger(String s) {
		if (StrUtil.isBlank(s)) {
			return false;
		}
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︽槸Long绫诲瀷<br>
	 * 鏀寔10杩涘埗
	 *
	 * @param s String
	 * @return 鏄惁涓簕@link Long}绫诲瀷
	 * @since 4.0.0
	 */
	public static boolean isLong(String s) {
		if (StrUtil.isBlank(s)) {
			return false;
		}
		try {
			Long.parseLong(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︽槸娴偣鏁?
	 *
	 * @param s String
	 * @return 鏄惁涓簕@link Double}绫诲瀷
	 */
	public static boolean isDouble(String s) {
		if (StrUtil.isBlank(s)) {
			return false;
		}
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException ignore) {
			return false;
		}
		return s.contains(".");
	}

	/**
	 * 鏄惁鏄川鏁帮紙绱犳暟锛?br>
	 * 璐ㄦ暟琛ㄧ殑璐ㄦ暟鍙堢О绱犳暟銆傛寚鏁存暟鍦ㄤ竴涓ぇ浜?鐨勮嚜鐒舵暟涓?闄や簡1鍜屾鏁存暟鑷韩澶?娌℃硶琚叾浠栬嚜鐒舵暟鏁撮櫎鐨勬暟銆?
	 *
	 * @param n 鏁板瓧
	 * @return 鏄惁鏄川鏁?
	 */
	public static boolean isPrimes(int n) {
		Assert.isTrue(n > 1, "The number must be > 1");
		if (n <= 3) {
			return true;
		} else if ((n & 1) == 0) {
			return false;
		} else if (n % 3 == 0) {
			return false;
		}
		for (int i = 5; i <= n / i; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------------------------- generateXXX

	/**
	 * 鐢熸垚涓嶉噸澶嶉殢鏈烘暟 鏍规嵁缁欏畾鐨勬渶灏忔暟瀛楀拰鏈€澶ф暟瀛楋紝浠ュ強闅忔満鏁扮殑涓暟锛屼骇鐢熸寚瀹氱殑涓嶉噸澶嶇殑鏁扮粍
	 *
	 * @param begin 鏈€灏忔暟瀛楋紙鍖呭惈璇ユ暟锛?
	 * @param end   鏈€澶ф暟瀛楋紙涓嶅寘鍚鏁帮級
	 * @param size  鎸囧畾浜х敓闅忔満鏁扮殑涓暟
	 * @return 闅忔満int鏁扮粍
	 */
	public static int[] generateRandomNumber(int begin, int end, int size) {
		// 绉嶅瓙浣犲彲浠ラ殢鎰忕敓鎴愶紝浣嗕笉鑳介噸澶?
		final int[] seed = ArrayUtil.range(begin, end);
		return generateRandomNumber(begin, end, size, seed);
	}

	/**
	 * 鐢熸垚涓嶉噸澶嶉殢鏈烘暟 鏍规嵁缁欏畾鐨勬渶灏忔暟瀛楀拰鏈€澶ф暟瀛楋紝浠ュ強闅忔満鏁扮殑涓暟锛屼骇鐢熸寚瀹氱殑涓嶉噸澶嶇殑鏁扮粍
	 *
	 * @param begin 鏈€灏忔暟瀛楋紙鍖呭惈璇ユ暟锛?
	 * @param end   鏈€澶ф暟瀛楋紙涓嶅寘鍚鏁帮級
	 * @param size  鎸囧畾浜х敓闅忔満鏁扮殑涓暟
	 * @param seed  绉嶅瓙锛岀敤浜庡彇闅忔満鏁扮殑int姹?
	 * @return 闅忔満int鏁扮粍
	 * @since 5.4.5
	 */
	public static int[] generateRandomNumber(int begin, int end, int size, int[] seed) {
		if (begin > end) {
			int temp = begin;
			begin = end;
			end = temp;
		}
		// 鍔犲叆閫昏緫鍒ゆ柇锛岀‘淇漛egin<end骞朵笖size涓嶈兘澶т簬璇ヨ〃绀鸿寖鍥?
		Assert.isTrue((end - begin) >= size, "Size is larger than range between begin and end!");
		Assert.isTrue(seed.length >= size, "Size is larger than seed size!");

		final int[] ranArr = new int[size];
		// 鏁伴噺浣犲彲浠ヨ嚜宸卞畾涔夈€?
		for (int i = 0; i < size; i++) {
			// 寰楀埌涓€涓綅缃?
			int j = RandomUtil.randomInt(seed.length - i);
			// 寰楀埌閭ｄ釜浣嶇疆鐨勬暟鍊?
			ranArr[i] = seed[j];
			// 灏嗘渶鍚庝竴涓湭鐢ㄧ殑鏁板瓧鏀惧埌杩欓噷
			seed[j] = seed[seed.length - 1 - i];
		}
		return ranArr;
	}

	/**
	 * 鐢熸垚涓嶉噸澶嶉殢鏈烘暟 鏍规嵁缁欏畾鐨勬渶灏忔暟瀛楀拰鏈€澶ф暟瀛楋紝浠ュ強闅忔満鏁扮殑涓暟锛屼骇鐢熸寚瀹氱殑涓嶉噸澶嶇殑鏁扮粍
	 *
	 * @param begin 鏈€灏忔暟瀛楋紙鍖呭惈璇ユ暟锛?
	 * @param end   鏈€澶ф暟瀛楋紙涓嶅寘鍚鏁帮級
	 * @param size  鎸囧畾浜х敓闅忔満鏁扮殑涓暟
	 * @return 闅忔満int鏁扮粍
	 */
	public static Integer[] generateBySet(int begin, int end, int size) {
		if (begin > end) {
			int temp = begin;
			begin = end;
			end = temp;
		}
		// 鍔犲叆閫昏緫鍒ゆ柇锛岀‘淇漛egin<end骞朵笖size涓嶈兘澶т簬璇ヨ〃绀鸿寖鍥?
		if ((end - begin) < size) {
			throw new UtilException("Size is larger than range between begin and end!");
		}

		Set<Integer> set = new HashSet<>(size, 1);
		while (set.size() < size) {
			set.add(begin + RandomUtil.randomInt(end - begin));
		}

		return set.toArray(new Integer[0]);
	}

	// ------------------------------------------------------------------------------------------- range

	/**
	 * 浠?寮€濮嬬粰瀹氳寖鍥村唴鐨勬暣鏁板垪琛紝姝ヨ繘涓?
	 *
	 * @param stop 缁撴潫锛堝寘鍚級
	 * @return 鏁存暟鍒楄〃
	 * @since 3.3.1
	 */
	public static int[] range(int stop) {
		return range(0, stop);
	}

	/**
	 * 缁欏畾鑼冨洿鍐呯殑鏁存暟鍒楄〃锛屾杩涗负1
	 *
	 * @param start 寮€濮嬶紙鍖呭惈锛?
	 * @param stop  缁撴潫锛堝寘鍚級
	 * @return 鏁存暟鍒楄〃
	 */
	public static int[] range(int start, int stop) {
		return range(start, stop, 1);
	}

	/**
	 * 缁欏畾鑼冨洿鍐呯殑鏁存暟鍒楄〃
	 *
	 * @param start 寮€濮嬶紙鍖呭惈锛?
	 * @param stop  缁撴潫锛堝寘鍚級
	 * @param step  姝ヨ繘
	 * @return 鏁存暟鍒楄〃
	 */
	public static int[] range(int start, int stop, int step) {
		if (start < stop) {
			step = Math.abs(step);
		} else if (start > stop) {
			step = -Math.abs(step);
		} else {// start == end
			return new int[]{start};
		}

		int size = Math.abs((stop - start) / step) + 1;
		int[] values = new int[size];
		int index = 0;
		for (int i = start; (step > 0) ? i <= stop : i >= stop; i += step) {
			values[index] = i;
			index++;
		}
		return values;
	}

	/**
	 * 灏嗙粰瀹氳寖鍥村唴鐨勬暣鏁版坊鍔犲埌宸叉湁闆嗗悎涓紝姝ヨ繘涓?
	 *
	 * @param start  寮€濮嬶紙鍖呭惈锛?
	 * @param stop   缁撴潫锛堝寘鍚級
	 * @param values 闆嗗悎
	 * @return 闆嗗悎
	 */
	public static Collection<Integer> appendRange(int start, int stop, Collection<Integer> values) {
		return appendRange(start, stop, 1, values);
	}

	/**
	 * 灏嗙粰瀹氳寖鍥村唴鐨勬暣鏁版坊鍔犲埌宸叉湁闆嗗悎涓?
	 *
	 * @param start  寮€濮嬶紙鍖呭惈锛?
	 * @param stop   缁撴潫锛堝寘鍚級
	 * @param step   姝ヨ繘
	 * @param values 闆嗗悎
	 * @return 闆嗗悎
	 */
	public static Collection<Integer> appendRange(int start, int stop, int step, Collection<Integer> values) {
		if (start < stop) {
			step = Math.abs(step);
		} else if (start > stop) {
			step = -Math.abs(step);
		} else {// start == end
			values.add(start);
			return values;
		}

		for (int i = start; (step > 0) ? i <= stop : i >= stop; i += step) {
			values.add(i);
		}
		return values;
	}

	// ------------------------------------------------------------------------------------------- others

	/**
	 * 璁＄畻闃朵箻
	 * <p>
	 * n! = n * (n-1) * ... * 2 * 1
	 * </p>
	 *
	 * @param n 闃朵箻璧峰
	 * @return 缁撴灉
	 * @since 5.6.0
	 */
	public static BigInteger factorial(BigInteger n) {
		if (n.equals(BigInteger.ZERO)) {
			return BigInteger.ONE;
		}
		return factorial(n, BigInteger.ZERO);
	}

	/**
	 * 璁＄畻鑼冨洿闃朵箻
	 * <p>
	 * factorial(start, end) = start * (start - 1) * ... * (end + 1)
	 * </p>
	 *
	 * @param start 闃朵箻璧峰锛堝寘鍚級
	 * @param end   闃朵箻缁撴潫锛屽繀椤诲皬浜庤捣濮嬶紙涓嶅寘鎷級
	 * @return 缁撴灉
	 * @since 5.6.0
	 */
	public static BigInteger factorial(BigInteger start, BigInteger end) {
		Assert.notNull(start, "Factorial start must be not null!");
		Assert.notNull(end, "Factorial end must be not null!");
		if (start.compareTo(BigInteger.ZERO) < 0 || end.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException(StrUtil.format("Factorial start and end both must be > 0, but got start={}, end={}", start, end));
		}

		if (start.equals(BigInteger.ZERO)) {
			start = BigInteger.ONE;
		}

		if (end.compareTo(BigInteger.ONE) < 0) {
			end = BigInteger.ONE;
		}

		BigInteger result = start;
		end = end.add(BigInteger.ONE);
		while (start.compareTo(end) > 0) {
			start = start.subtract(BigInteger.ONE);
			result = result.multiply(start);
		}
		return result;
	}

	/**
	 * 璁＄畻鑼冨洿闃朵箻
	 * <p>
	 * factorial(start, end) = start * (start - 1) * ... * (end + 1)
	 * </p>
	 *
	 * @param start 闃朵箻璧峰锛堝寘鍚級
	 * @param end   闃朵箻缁撴潫锛屽繀椤诲皬浜庤捣濮嬶紙涓嶅寘鎷級
	 * @return 缁撴灉
	 * @since 4.1.0
	 */
	public static long factorial(long start, long end) {
		// 璐熸暟娌℃湁闃朵箻
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException(StrUtil.format("Factorial start and end both must be >= 0, but got start={}, end={}", start, end));
		}
		if (0L == start || start == end) {
			return 1L;
		}
		if (start < end) {
			return 0L;
		}
		return factorialMultiplyAndCheck(start, factorial(start - 1, end));
	}

	/**
	 * 璁＄畻鑼冨洿闃朵箻涓牎楠屼腑闂寸殑璁＄畻鏄惁瀛樺湪婧㈠嚭锛宖actorial鎻愬墠鍋氫簡璐熸暟鍜?鐨勬牎楠岋紝鍥犳杩欓噷娌℃湁鏍￠獙鏁板瓧鐨勬璐?
	 *
	 * @param a 涔樻暟
	 * @param b 琚箻鏁?
	 * @return 濡傛灉 a * b鐨勭粨鏋滄病鏈夋孩鍑虹洿鎺ヨ繑鍥烇紝鍚﹀垯鎶涘嚭寮傚父
	 */
	private static long factorialMultiplyAndCheck(long a, long b) {
		if (a <= Long.MAX_VALUE / b) {
			return a * b;
		}
		throw new IllegalArgumentException(StrUtil.format("Overflow in multiplication: {} * {}", a, b));
	}

	/**
	 * 璁＄畻闃朵箻
	 * <p>
	 * n! = n * (n-1) * ... * 2 * 1
	 * </p>
	 *
	 * @param n 闃朵箻璧峰
	 * @return 缁撴灉
	 */
	public static long factorial(long n) {
		if (n < 0 || n > 20) {
			throw new IllegalArgumentException(StrUtil.format("Factorial must have n >= 0 and n <= 20 for n!, but got n = {}", n));
		}
		return FACTORIALS[(int) n];
	}

	/**
	 * 骞虫柟鏍圭畻娉?br>
	 * 鎺ㄨ崘浣跨敤 {@link Math#sqrt(double)}
	 *
	 * @param x 鍊?
	 * @return 骞虫柟鏍?
	 */
	public static long sqrt(long x) {
		long y = 0;
		long b = (~Long.MAX_VALUE) >>> 1;
		while (b > 0) {
			if (x >= y + b) {
				x -= y + b;
				y >>= 1;
				y += b;
			} else {
				y >>= 1;
			}
			b >>= 2;
		}
		return y;
	}

	/**
	 * 鍙互鐢ㄤ簬璁＄畻鍙岃壊鐞冦€佸ぇ涔愰€忔敞鏁扮殑鏂规硶<br>
	 * 姣斿澶т箰閫?5閫?鍙互杩欐牱璋冪敤processMultiple(7,5); 灏辨槸鏁板涓殑锛欳75=7*6/2*1
	 *
	 * @param selectNum 閫変腑灏忕悆涓暟
	 * @param minNum    鏈€灏戣閫変腑澶氬皯涓皬鐞?
	 * @return 娉ㄦ暟
	 */
	public static int processMultiple(int selectNum, int minNum) {
		int result;
		result = mathSubNode(selectNum, minNum) / mathNode(selectNum - minNum);
		return result;
	}

	/**
	 * 鏈€澶у叕绾︽暟
	 *
	 * @param m 绗竴涓€?
	 * @param n 绗簩涓€?
	 * @return 鏈€澶у叕绾︽暟
	 */
	public static int divisor(int m, int n) {
		while (m % n != 0) {
			int temp = m % n;
			m = n;
			n = temp;
		}
		return n;
	}

	/**
	 * 鏈€灏忓叕鍊嶆暟
	 *
	 * @param m 绗竴涓€?
	 * @param n 绗簩涓€?
	 * @return 鏈€灏忓叕鍊嶆暟
	 */
	public static int multiple(int m, int n) {
		// 鍏堣绠楁渶澶у叕绾︽暟
		int gcd = divisor(m, n);
		// 浣跨敤闀挎暣鍨嬮伩鍏嶆孩鍑猴紝鍐嶈浆鎹㈠洖鏁村瀷
		long result = (long) m / gcd * (long) n;
		// 妫€鏌ョ粨鏋滄槸鍚﹀湪int鑼冨洿鍐?
		if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
			throw new ArithmeticException("Integer overflow: " + m + " * " + n + " / " + gcd);
		}
		return (int) result;
	}

	/**
	 * 鑾峰緱鏁板瓧瀵瑰簲鐨勪簩杩涘埗瀛楃涓?
	 * <ul>
	 *     <li>Integer/Long锛氱洿鎺ヤ娇鐢?JDK 鍐呯疆鏂规硶杞崲</li>
	 *     <li>Byte/Short锛氳浆鎹负鏃犵鍙锋暣鏁板悗琛ュ厖鍓嶅闆惰嚦瀵瑰簲浣嶆暟锛圔yte=8浣嶏紝Short=16浣嶏級</li>
	 *     <li>Float/Double锛氫娇鐢?IEEE 754 鏍囧噯鏍煎紡杞崲锛孎loat=32浣嶏紝Double=64浣?/li>
	 * </ul>
	 *
	 * @param number 鏁板瓧
	 * @return 浜岃繘鍒跺瓧绗︿覆
	 */
	public static String getBinaryStr(Number number) {
		Assert.notNull(number, "Number must be not null!");

		if (number instanceof Double) {
			// 澶勭悊double绫诲瀷
			long bits = Double.doubleToLongBits((Double) number);
			return String.format("%64s", Long.toBinaryString(bits)).replace(' ', '0');
		} else if (number instanceof Float) {
			// 澶勭悊float绫诲瀷
			int bits = Float.floatToIntBits((Float) number);
			return String.format("%32s", Integer.toBinaryString(bits)).replace(' ', '0');
		}else if (number instanceof Long) {
			return Long.toBinaryString((Long) number);
		} else if (number instanceof Integer) {
			return Integer.toBinaryString((Integer) number);
		} else if (number instanceof Byte) {
			// Byte鏄?浣嶏紝琛ュ墠瀵?鑷?浣?
			return String.format("%8s", Integer.toBinaryString(number.byteValue() & 0xFF)).replace(' ', '0');
		} else if (number instanceof Short) {
			// Short鏄?6浣嶏紝琛ュ墠瀵?鑷?6浣?
			return String.format("%16s", Integer.toBinaryString(number.shortValue() & 0xFFFF)).replace(' ', '0');
		} else if (number instanceof BigInteger) {
			// 澶ф暟鏁存暟绫诲瀷
			return ((BigInteger) number).toString(2);
		} else {
			return Long.toBinaryString(number.longValue());
		}
	}

	/**
	 * 浜岃繘鍒惰浆int
	 *
	 * @param binaryStr 浜岃繘鍒跺瓧绗︿覆
	 * @return int
	 */
	public static int binaryToInt(String binaryStr) {
		return Integer.parseInt(binaryStr, 2);
	}

	/**
	 * 浜岃繘鍒惰浆long
	 *
	 * @param binaryStr 浜岃繘鍒跺瓧绗︿覆
	 * @return long
	 */
	public static long binaryToLong(String binaryStr) {
		return Long.parseLong(binaryStr, 2);
	}

	// ------------------------------------------------------------------------------------------- compare

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖灏忎簬0鐨勬暟锛寈&gt;y杩斿洖澶т簬0鐨勬暟
	 * @see Character#compare(char, char)
	 * @since 3.0.1
	 */
	public static int compare(char x, char y) {
		return Character.compare(x, y);
	}

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖灏忎簬0鐨勬暟锛寈&gt;y杩斿洖澶т簬0鐨勬暟
	 * @see Double#compare(double, double)
	 * @since 3.0.1
	 */
	public static int compare(double x, double y) {
		return Double.compare(x, y);
	}

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖灏忎簬0鐨勬暟锛寈&gt;y杩斿洖澶т簬0鐨勬暟
	 * @see Integer#compare(int, int)
	 * @since 3.0.1
	 */
	public static int compare(int x, int y) {
		return Integer.compare(x, y);
	}

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖灏忎簬0鐨勬暟锛寈&gt;y杩斿洖澶т簬0鐨勬暟
	 * @see Long#compare(long, long)
	 * @since 3.0.1
	 */
	public static int compare(long x, long y) {
		return Long.compare(x, y);
	}

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖灏忎簬0鐨勬暟锛寈&gt;y杩斿洖澶т簬0鐨勬暟
	 * @see Short#compare(short, short)
	 * @since 3.0.1
	 */
	public static int compare(short x, short y) {
		return Short.compare(x, y);
	}

	/**
	 * 姣旇緝涓や釜鍊肩殑澶у皬
	 *
	 * @param x 绗竴涓€?
	 * @param y 绗簩涓€?
	 * @return x==y杩斿洖0锛寈&lt;y杩斿洖-1锛寈&gt;y杩斿洖1
	 * @see Byte#compare(byte, byte)
	 * @since 3.0.1
	 */
	public static int compare(byte x, byte y) {
		return Byte.compare(x, y);
	}

	/**
	 * 姣旇緝澶у皬锛屽弬鏁? &gt; 鍙傛暟2 杩斿洖true
	 *
	 * @param bigNum1 鏁板瓧1
	 * @param bigNum2 鏁板瓧2
	 * @return 鏄惁澶т簬
	 * @since 3.0.9
	 */
	public static boolean isGreater(BigDecimal bigNum1, BigDecimal bigNum2) {
		Assert.notNull(bigNum1);
		Assert.notNull(bigNum2);
		return bigNum1.compareTo(bigNum2) > 0;
	}

	/**
	 * 姣旇緝澶у皬锛屽弬鏁? &gt;= 鍙傛暟2 杩斿洖true
	 *
	 * @param bigNum1 鏁板瓧1
	 * @param bigNum2 鏁板瓧2
	 * @return 鏄惁澶т簬绛変簬
	 * @since 3.0.9
	 */
	public static boolean isGreaterOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
		Assert.notNull(bigNum1);
		Assert.notNull(bigNum2);
		return bigNum1.compareTo(bigNum2) >= 0;
	}

	/**
	 * 姣旇緝澶у皬锛屽弬鏁? &lt; 鍙傛暟2 杩斿洖true
	 *
	 * @param bigNum1 鏁板瓧1
	 * @param bigNum2 鏁板瓧2
	 * @return 鏄惁灏忎簬
	 * @since 3.0.9
	 */
	public static boolean isLess(BigDecimal bigNum1, BigDecimal bigNum2) {
		Assert.notNull(bigNum1);
		Assert.notNull(bigNum2);
		return bigNum1.compareTo(bigNum2) < 0;
	}

	/**
	 * 姣旇緝澶у皬锛屽弬鏁?&lt;=鍙傛暟2 杩斿洖true
	 *
	 * @param bigNum1 鏁板瓧1
	 * @param bigNum2 鏁板瓧2
	 * @return 鏄惁灏忎簬绛変簬
	 * @since 3.0.9
	 */
	public static boolean isLessOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
		Assert.notNull(bigNum1);
		Assert.notNull(bigNum2);
		return bigNum1.compareTo(bigNum2) <= 0;
	}

	/**
	 * 妫€鏌ュ€兼槸鍚﹀湪鎸囧畾鑼冨洿鍐?
	 *
	 * @param value      鍊?
	 * @param minInclude 鏈€灏忓€硷紙鍖呭惈锛?
	 * @param maxInclude 鏈€澶у€硷紙鍖呭惈锛?
	 * @return 缁忚繃妫€鏌ュ悗鐨勫€?
	 * @since 5.8.5
	 */
	public static boolean isIn(final BigDecimal value, final BigDecimal minInclude, final BigDecimal maxInclude) {
		Assert.notNull(value);
		Assert.notNull(minInclude);
		Assert.notNull(maxInclude);
		return isGreaterOrEqual(value, minInclude) && isLessOrEqual(value, maxInclude);
	}

	/**
	 * 姣旇緝澶у皬锛屽€肩浉绛?杩斿洖true<br>
	 * 姝ゆ柟娉曢€氳繃璋冪敤{@link Double#doubleToLongBits(double)}鏂规硶鏉ュ垽鏂槸鍚︾浉绛?br>
	 * 姝ゆ柟娉曞垽鏂€肩浉绛夋椂蹇界暐绮惧害鐨勶紝鍗?.00 == 0
	 *
	 * @param num1 鏁板瓧1
	 * @param num2 鏁板瓧2
	 * @return 鏄惁鐩哥瓑
	 * @since 5.4.2
	 */
	public static boolean equals(double num1, double num2) {
		return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
	}

	/**
	 * 姣旇緝澶у皬锛屽€肩浉绛?杩斿洖true<br>
	 * 姝ゆ柟娉曢€氳繃璋冪敤{@link Float#floatToIntBits(float)}鏂规硶鏉ュ垽鏂槸鍚︾浉绛?br>
	 * 姝ゆ柟娉曞垽鏂€肩浉绛夋椂蹇界暐绮惧害鐨勶紝鍗?.00 == 0
	 *
	 * @param num1 鏁板瓧1
	 * @param num2 鏁板瓧2
	 * @return 鏄惁鐩哥瓑
	 * @since 5.4.5
	 */
	public static boolean equals(float num1, float num2) {
		return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
	}

	/**
	 * 姣旇緝澶у皬锛屽€肩浉绛?杩斿洖true<br>
	 * 姝ゆ柟娉曚慨澶嶄紶鍏ong鍨嬫暟鎹敱浜庢病鏈夋湰绫诲瀷閲嶈浇鏂规硶,瀵艰嚧鏁版嵁绮惧害涓㈠け
	 *
	 * @param num1 鏁板瓧1
	 * @param num2 鏁板瓧2
	 * @return 鏄惁鐩哥瓑
	 * @since 5.7.19
	 */
	public static boolean equals(long num1, long num2) {
		return num1 == num2;
	}

	/**
	 * 姣旇緝鏁板瓧鍊兼槸鍚︾浉绛夛紝鐩哥瓑杩斿洖{@code true}<br>
	 * 闇€瑕佹敞鎰忕殑鏄瘂@link BigDecimal}闇€瑕佺壒娈婂鐞?br>
	 * BigDecimal浣跨敤compareTo鏂瑰紡鍒ゆ柇锛屽洜涓轰娇鐢╡quals鏂规硶涔熷垽鏂皬鏁颁綅鏁帮紝濡?.0鍜?.00灏变笉鐩哥瓑锛?br>
	 * 姝ゆ柟娉曞垽鏂€肩浉绛夋椂蹇界暐绮惧害鐨勶紝鍗?.00 == 0
	 *
	 * <ul>
	 *     <li>濡傛灉鐢ㄦ埛鎻愪緵涓や釜Number閮芥槸{@link BigDecimal}锛屽垯閫氳繃璋冪敤{@link BigDecimal#compareTo(BigDecimal)}鏂规硶鏉ュ垽鏂槸鍚︾浉绛?/li>
	 *     <li>鍏朵粬鎯呭喌璋冪敤{@link Number#equals(Object)}姣旇緝</li>
	 * </ul>
	 *
	 * @param number1 鏁板瓧1
	 * @param number2 鏁板瓧2
	 * @return 鏄惁鐩哥瓑
	 * @see Objects#equals(Object, Object)
	 * @since 5.8.17
	 */
	public static boolean equals(final Number number1, final Number number2) {
		if (number1 instanceof BigDecimal && number2 instanceof BigDecimal) {
			// BigDecimal浣跨敤compareTo鏂瑰紡鍒ゆ柇锛屽洜涓轰娇鐢╡quals鏂规硶涔熷垽鏂皬鏁颁綅鏁帮紝濡?.0鍜?.00灏变笉鐩哥瓑
			return equals((BigDecimal) number1, (BigDecimal) number2);
		}
		return Objects.equals(number1, number2);
	}

	/**
	 * 姣旇緝澶у皬锛屽€肩浉绛?杩斿洖true<br>
	 * 姝ゆ柟娉曢€氳繃璋冪敤{@link BigDecimal#compareTo(BigDecimal)}鏂规硶鏉ュ垽鏂槸鍚︾浉绛?br>
	 * 姝ゆ柟娉曞垽鏂€肩浉绛夋椂蹇界暐绮惧害鐨勶紝鍗?.00 == 0
	 *
	 * @param bigNum1 鏁板瓧1
	 * @param bigNum2 鏁板瓧2
	 * @return 鏄惁鐩哥瓑
	 */
	public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
		//noinspection NumberEquality
		if (bigNum1 == bigNum2) {
			// 濡傛灉鐢ㄦ埛浼犲叆鍚屼竴瀵硅薄锛岀渷鐣ompareTo浠ユ彁楂樻€ц兘銆?
			return true;
		}
		if (bigNum1 == null || bigNum2 == null) {
			return false;
		}
		return 0 == bigNum1.compareTo(bigNum2);
	}

	/**
	 * 姣旇緝涓や釜瀛楃鏄惁鐩稿悓
	 *
	 * @param c1         瀛楃1
	 * @param c2         瀛楃2
	 * @param ignoreCase 鏄惁蹇界暐澶у皬鍐?
	 * @return 鏄惁鐩稿悓
	 * @see CharUtil#equals(char, char, boolean)
	 * @since 3.2.1
	 */
	public static boolean equals(char c1, char c2, boolean ignoreCase) {
		return CharUtil.equals(c1, c2, ignoreCase);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param <T>         鍏冪礌绫诲瀷
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(Comparable[])
	 * @since 4.0.7
	 */
	public static <T extends Comparable<? super T>> T min(T[] numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(long...)
	 * @since 4.0.7
	 */
	public static long min(long... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(int...)
	 * @since 4.0.7
	 */
	public static int min(int... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(short...)
	 * @since 4.0.7
	 */
	public static short min(short... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(double...)
	 * @since 4.0.7
	 */
	public static double min(double... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(float...)
	 * @since 4.0.7
	 */
	public static float min(float... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶灏忓€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€灏忓€?
	 * @see ArrayUtil#min(Comparable[])
	 * @since 5.0.8
	 */
	public static BigDecimal min(BigDecimal... numberArray) {
		return ArrayUtil.min(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param <T>         鍏冪礌绫诲瀷
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(Comparable[])
	 * @since 4.0.7
	 */
	public static <T extends Comparable<? super T>> T max(T[] numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(long...)
	 * @since 4.0.7
	 */
	public static long max(long... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(int...)
	 * @since 4.0.7
	 */
	public static int max(int... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(short...)
	 * @since 4.0.7
	 */
	public static short max(short... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(double...)
	 * @since 4.0.7
	 */
	public static double max(double... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(float...)
	 * @since 4.0.7
	 */
	public static float max(float... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鍙栨渶澶у€?
	 *
	 * @param numberArray 鏁板瓧鏁扮粍
	 * @return 鏈€澶у€?
	 * @see ArrayUtil#max(Comparable[])
	 * @since 5.0.8
	 */
	public static BigDecimal max(BigDecimal... numberArray) {
		return ArrayUtil.max(numberArray);
	}

	/**
	 * 鏁板瓧杞瓧绗︿覆<br>
	 * 璋冪敤{@link Number#toString()}锛屽苟鍘婚櫎灏惧皬鏁扮偣鍎垮悗澶氫綑鐨?
	 *
	 * @param number       A Number
	 * @param defaultValue 濡傛灉number鍙傛暟涓簕@code null}锛岃繑鍥炴榛樿鍊?
	 * @return A String.
	 * @since 3.0.9
	 */
	public static String toStr(Number number, String defaultValue) {
		return (null == number) ? defaultValue : toStr(number);
	}

	/**
	 * 鏁板瓧杞瓧绗︿覆<br>
	 * 璋冪敤{@link Number#toString()}鎴?{@link BigDecimal#toPlainString()}锛屽苟鍘婚櫎灏惧皬鏁扮偣鍎垮悗澶氫綑鐨?
	 *
	 * @param number A Number
	 * @return A String.
	 */
	public static String toStr(Number number) {
		return toStr(number, true);
	}

	/**
	 * 鏁板瓧杞瓧绗︿覆<br>
	 * 璋冪敤{@link Number#toString()}鎴?{@link BigDecimal#toPlainString()}锛屽苟鍘婚櫎灏惧皬鏁扮偣鍎垮悗澶氫綑鐨?
	 *
	 * @param number               A Number
	 * @param isStripTrailingZeros 鏄惁鍘婚櫎鏈熬澶氫綑0锛屼緥濡?.0杩斿洖5
	 * @return A String.
	 */
	public static String toStr(Number number, boolean isStripTrailingZeros) {
		Assert.notNull(number, "Number is null !");

		// BigDecimal鍗曠嫭澶勭悊锛屼娇鐢ㄩ潪绉戝璁℃暟娉?
		if (number instanceof BigDecimal) {
			return toStr((BigDecimal) number, isStripTrailingZeros);
		}
		Assert.isTrue(isValidNumber(number), "Number is non-finite!");

		// 鍘绘帀灏忔暟鐐瑰効鍚庡浣欑殑0
		String string = number.toString();
		if (isStripTrailingZeros) {
			if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string.indexOf('E') < 0) {
				while (string.endsWith("0")) {
					string = string.substring(0, string.length() - 1);
				}
				if (string.endsWith(".")) {
					string = string.substring(0, string.length() - 1);
				}
			}
		}
		return string;
	}

	/**
	 * {@link BigDecimal}鏁板瓧杞瓧绗︿覆<br>
	 * 璋冪敤{@link BigDecimal#toPlainString()}锛屽苟鍘婚櫎灏惧皬鏁扮偣鍎垮悗澶氫綑鐨?
	 *
	 * @param bigDecimal A {@link BigDecimal}
	 * @return A String.
	 * @since 5.4.6
	 */
	public static String toStr(BigDecimal bigDecimal) {
		return toStr(bigDecimal, true);
	}

	/**
	 * {@link BigDecimal}鏁板瓧杞瓧绗︿覆<br>
	 * 璋冪敤{@link BigDecimal#toPlainString()}锛屽彲閫夊幓闄ゅ熬灏忔暟鐐瑰効鍚庡浣欑殑0
	 *
	 * @param bigDecimal           A {@link BigDecimal}
	 * @param isStripTrailingZeros 鏄惁鍘婚櫎鏈熬澶氫綑0锛屼緥濡?.0杩斿洖5
	 * @return A String.
	 * @since 5.4.6
	 */
	public static String toStr(BigDecimal bigDecimal, boolean isStripTrailingZeros) {
		Assert.notNull(bigDecimal, "BigDecimal is null !");
		if (isStripTrailingZeros) {
			bigDecimal = bigDecimal.stripTrailingZeros();
		}
		return bigDecimal.toPlainString();
	}

	/**
	 * 鏁板瓧杞瑊@link BigDecimal}<br>
	 * Float銆丏ouble绛夋湁绮惧害闂锛岃浆鎹负瀛楃涓插悗鍐嶈浆鎹?br>
	 * null杞崲涓?
	 *
	 * @param number 鏁板瓧
	 * @return {@link BigDecimal}
	 * @since 4.0.9
	 */
	public static BigDecimal toBigDecimal(Number number) {
		if (null == number) {
			return BigDecimal.ZERO;
		}
		// issue#3423@Github of CVE-2023-51080
		Assert.isTrue(isValidNumber(number), "Number is invalid!");

		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof Long) {
			return new BigDecimal((Long) number);
		} else if (number instanceof Integer) {
			return new BigDecimal((Integer) number);
		} else if (number instanceof BigInteger) {
			return new BigDecimal((BigInteger) number);
		}

		// Float銆丏ouble绛夋湁绮惧害闂锛岃浆鎹负瀛楃涓插悗鍐嶈浆鎹?
		return new BigDecimal(number.toString());
	}

	/**
	 * 鏁板瓧杞瑊@link BigDecimal}<br>
	 * null鎴?"鎴栫┖鐧界杞崲涓?
	 *
	 * @param numberStr 鏁板瓧瀛楃涓?
	 * @return {@link BigDecimal}
	 * @since 4.0.9
	 */
	public static BigDecimal toBigDecimal(String numberStr) {
		if (StrUtil.isBlank(numberStr)) {
			return BigDecimal.ZERO;
		}

		try {
			return new BigDecimal(numberStr);
		} catch (Exception ignore) {
			// 蹇界暐瑙ｆ瀽閿欒
		}

		// 鏀寔绫讳技浜?1,234.55 鏍煎紡鐨勬暟瀛?
		final Number number = parseNumber(numberStr);
		return toBigDecimal(number);
	}

	/**
	 * 鏁板瓧杞瑊@link BigInteger}<br>
	 * null杞崲涓?
	 *
	 * @param number 鏁板瓧
	 * @return {@link BigInteger}
	 * @since 5.4.5
	 */
	public static BigInteger toBigInteger(Number number) {
		if (null == number) {
			return BigInteger.ZERO;
		}

		if (number instanceof BigInteger) {
			return (BigInteger) number;
		} else if (number instanceof Long) {
			return BigInteger.valueOf((Long) number);
		}

		Assert.isTrue(isValidNumber(number), "Number is invalid!");
		return toBigInteger(number.longValue());
	}

	/**
	 * 鏁板瓧杞瑊@link BigInteger}<br>
	 * null鎴?"鎴栫┖鐧界杞崲涓?
	 *
	 * @param number 鏁板瓧瀛楃涓?
	 * @return {@link BigInteger}
	 * @since 5.4.5
	 */
	public static BigInteger toBigInteger(String number) {
		return StrUtil.isBlank(number) ? BigInteger.ZERO : new BigInteger(number);
	}

	/**
	 * 璁＄畻绛変唤涓暟
	 *
	 * @param total 鎬绘暟
	 * @param part  姣忎唤鐨勪釜鏁?
	 * @return 鍒嗘垚浜嗗嚑浠?
	 * @since 3.0.6
	 */
	public static int count(int total, int part) {
		return total == 0 ? 0 : (total - 1) / part + 1;
	}

	/**
	 * 绌鸿浆0
	 *
	 * @param decimal {@link BigDecimal}锛屽彲浠ヤ负{@code null}
	 * @return {@link BigDecimal}鍙傛暟涓虹┖鏃惰繑鍥?鐨勫€?
	 * @since 3.0.9
	 * @deprecated 璇蜂娇鐢?{@link #nullToZero(BigDecimal)}
	 */
	@Deprecated
	public static BigDecimal null2Zero(BigDecimal decimal) {
		return decimal == null ? BigDecimal.ZERO : decimal;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负0锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param value 鍊?
	 * @return 1鎴栭潪0鍊?
	 * @since 3.1.2
	 */
	public static int zero2One(int value) {
		return 0 == value ? 1 : value;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static int nullToZero(Integer number) {
		return number == null ? 0 : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static long nullToZero(Long number) {
		return number == null ? 0L : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static double nullToZero(Double number) {
		return number == null ? 0.0 : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static float nullToZero(Float number) {
		return number == null ? 0.0f : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static short nullToZero(Short number) {
		return number == null ? (short) 0 : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static byte nullToZero(Byte number) {
		return number == null ? (byte) 0 : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static BigDecimal nullToZero(BigDecimal number) {
		return number == null ? BigDecimal.ZERO : number;
	}

	/**
	 * 濡傛灉缁欏畾鍊间负{@code null}锛岃繑鍥?锛屽惁鍒欒繑鍥炲師鍊?
	 *
	 * @param number 鍊?
	 * @return 0鎴栭潪0鍊?
	 */
	public static BigInteger nullToZero(BigInteger number) {
		return number == null ? BigInteger.ZERO : number;
	}

	/**
	 * 鍒涘缓{@link BigInteger}锛屾敮鎸?6杩涘埗銆?0杩涘埗鍜?杩涘埗锛屽鏋滀紶鍏ョ┖鐧戒覆杩斿洖null<br>
	 * from Apache Common Lang
	 *
	 * @param str 鏁板瓧瀛楃涓?
	 * @return {@link BigInteger}
	 * @since 3.2.1
	 */
	public static BigInteger newBigInteger(String str) {
		str = StrUtil.trimToNull(str);
		if (null == str) {
			return null;
		}

		int pos = 0; // 鏁板瓧瀛楃涓蹭綅缃?
		int radix = 10;
		boolean negate = false; // 璐熸暟涓庡惁
		if (str.startsWith("-")) {
			negate = true;
			pos = 1;
		}
		if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
			// hex
			radix = 16;
			pos += 2;
		} else if (str.startsWith("#", pos)) {
			// alternative hex (allowed by Long/Integer)
			radix = 16;
			pos++;
		} else if (str.startsWith("0", pos) && str.length() > pos + 1) {
			// octal; so long as there are additional digits
			radix = 8;
			pos++;
		} // default is to treat as decimal

		if (pos > 0) {
			str = str.substring(pos);
		}
		final BigInteger value = new BigInteger(str, radix);
		return negate ? value.negate() : value;
	}

	/**
	 * 鍒ゆ柇涓や釜鏁板瓧鏄惁鐩搁偦锛屼緥濡?鍜?鐩搁偦锛?鍜?涓嶇浉閭?br>
	 * 鍒ゆ柇鏂规硶涓哄仛宸彇缁濆鍊煎垽鏂槸鍚︿负1
	 *
	 * @param number1 鏁板瓧1
	 * @param number2 鏁板瓧2
	 * @return 鏄惁鐩搁偦
	 * @since 4.0.7
	 */
	public static boolean isBeside(long number1, long number2) {
		return Math.abs(number1 - number2) == 1;
	}

	/**
	 * 鍒ゆ柇涓や釜鏁板瓧鏄惁鐩搁偦锛屼緥濡?鍜?鐩搁偦锛?鍜?涓嶇浉閭?br>
	 * 鍒ゆ柇鏂规硶涓哄仛宸彇缁濆鍊煎垽鏂槸鍚︿负1
	 *
	 * @param number1 鏁板瓧1
	 * @param number2 鏁板瓧2
	 * @return 鏄惁鐩搁偦
	 * @since 4.0.7
	 */
	public static boolean isBeside(int number1, int number2) {
		return Math.abs(number1 - number2) == 1;
	}

	/**
	 * 鎶婄粰瀹氱殑鎬绘暟骞冲潎鍒嗘垚N浠斤紝杩斿洖姣忎唤鐨勪釜鏁?br>
	 * 褰撻櫎浠ュ垎鏁版湁浣欐暟鏃舵瘡浠?1
	 *
	 * @param total     鎬绘暟
	 * @param partCount 浠芥暟
	 * @return 姣忎唤鐨勪釜鏁?
	 * @since 4.0.7
	 */
	public static int partValue(int total, int partCount) {
		return partValue(total, partCount, true);
	}

	/**
	 * 鎶婄粰瀹氱殑鎬绘暟骞冲潎鍒嗘垚N浠斤紝杩斿洖姣忎唤鐨勪釜鏁?br>
	 * 濡傛灉isPlusOneWhenHasRem涓簍rue锛屽垯褰撻櫎浠ュ垎鏁版湁浣欐暟鏃舵瘡浠?1锛屽惁鍒欎涪寮冧綑鏁伴儴鍒?
	 *
	 * @param total               鎬绘暟
	 * @param partCount           浠芥暟
	 * @param isPlusOneWhenHasRem 鍦ㄦ湁浣欐暟鏃舵槸鍚︽瘡浠?1
	 * @return 姣忎唤鐨勪釜鏁?
	 * @since 4.0.7
	 */
	public static int partValue(int total, int partCount, boolean isPlusOneWhenHasRem) {
		int partValue = total / partCount;
		if (isPlusOneWhenHasRem && total % partCount > 0) {
			partValue++;
		}
		return partValue;
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫箓杩愮畻
	 *
	 * @param number 搴曟暟
	 * @param n      鎸囨暟
	 * @return 骞傜殑绉?
	 * @since 4.1.0
	 */
	public static BigDecimal pow(Number number, int n) {
		return pow(toBigDecimal(number), n);
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫箓杩愮畻<br>
	 * 濡傛灉n涓鸿礋鏁帮紝鍒欒繑鍥?/a鐨?n娆℃柟锛岄粯璁ゅ洓鑸嶄簲鍏ワ紝淇濈暀涓や綅灏忔暟
	 *
	 * @param number 搴曟暟
	 * @param n      鎸囨暟锛屽鏋滀负璐熸暟锛屽垯杩斿洖1/a鐨?n娆℃柟
	 * @return 骞傜殑绉?
	 * @since 4.1.0
	 */
	public static BigDecimal pow(BigDecimal number, int n) {
		return pow(number, n, 2, RoundingMode.HALF_UP);
	}


	/**
	 * 鎻愪緵绮剧‘鐨勫箓杩愮畻<br>
	 * 濡傛灉n涓鸿礋鏁帮紝鍒欒繑鍥?/a鐨?n娆℃柟锛岄粯璁ゅ洓鑸嶄簲鍏?
	 *
	 * @param number       搴曟暟
	 * @param n            鎸囨暟锛屽鏋滀负璐熸暟锛屽垯杩斿洖1/a鐨?n娆℃柟
	 * @param scale        淇濈暀鐨勫皬鏁颁綅 (鎸囨暟涓鸿礋鏁版椂鐢熸晥)
	 * @param roundingMode 淇濈暀灏忔暟鐨勬ā寮?{@link RoundingMode} (鎸囨暟涓鸿礋鏁版椂鐢熸晥)
	 * @return 骞傜殑绉?
	 * @since 5.8.41
	 */
	public static BigDecimal pow(BigDecimal number, int n, int scale, RoundingMode roundingMode) {
		if (n < 0) {
			// a鐨刵娆℃柟锛屽鏋渘涓鸿礋鏁帮紝鍒欒繑鍥?/a鐨?n娆℃柟
			return BigDecimal.ONE.divide(pow(number, -n), scale, roundingMode);
		}
		return number.pow(n);
	}


	/**
	 * 鍒ゆ柇涓€涓暣鏁版槸鍚︽槸2鐨勫箓
	 *
	 * @param n 寰呴獙璇佺殑鏁存暟
	 * @return 濡傛灉n鏄?鐨勫箓杩斿洖true, 鍙嶄箣杩斿洖false
	 */
	public static boolean isPowerOfTwo(long n) {
		return (n > 0) && ((n & (n - 1)) == 0);
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负int鍨嬫暟瀛楋紝瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?x寮€澶寸殑瑙嗕负16杩涘埗鏁板瓧
	 * 2銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆佺┖涓茶繑鍥?
	 * 5銆?123褰㈠紡杩斿洖0锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * 6銆?23.56鎴彇灏忔暟鐐逛箣鍓嶇殑鏁板瓧锛屽拷鐣ュ皬鏁伴儴鍒?
	 * </pre>
	 *
	 * @param number 鏁板瓧锛屾敮鎸?x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @return int
	 * @throws NumberFormatException 鏁板瓧鏍煎紡寮傚父
	 * @since 4.1.4
	 */
	public static int parseInt(String number) throws NumberFormatException {
		if (StrUtil.isBlank(number)) {
			return 0;
		}

		if (StrUtil.startWithIgnoreCase(number, "0x")) {
			// 0x04琛ㄧず16杩涘埗鏁?
			return Integer.parseInt(number.substring(2), 16);
		}

		if (StrUtil.containsIgnoreCase(number, "E")) {
			// 绉戝璁℃暟娉曞拷鐣ユ敮鎸侊紝绉戝璁℃暟娉曚竴鑸敤浜庤〃绀洪潪甯稿皬鍜岄潪甯稿ぇ鐨勬暟瀛楋紝杩欑被鏁板瓧杞崲涓篿nt鍚庣簿搴︿涪澶憋紝娌℃湁鎰忎箟銆?
			throw new NumberFormatException(StrUtil.format("Unsupported int format: [{}]", number));
		}

		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return parseNumber(number).intValue();
		}
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负long鍨嬫暟瀛楋紝瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?x寮€澶寸殑瑙嗕负16杩涘埗鏁板瓧
	 * 2銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 3銆佺┖涓茶繑鍥?
	 * 4銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 5銆?123褰㈠紡杩斿洖0锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * 6銆?23.56鎴彇灏忔暟鐐逛箣鍓嶇殑鏁板瓧锛屽拷鐣ュ皬鏁伴儴鍒?
	 * </pre>
	 *
	 * @param number 鏁板瓧锛屾敮鎸?x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @return long
	 * @since 4.1.4
	 */
	public static long parseLong(String number) {
		if (StrUtil.isBlank(number)) {
			return 0L;
		}

		if (number.startsWith("0x")) {
			// 0x04琛ㄧず16杩涘埗鏁?
			return Long.parseLong(number.substring(2), 16);
		}

		try {
			return Long.parseLong(number);
		} catch (NumberFormatException e) {
			return parseNumber(number).longValue();
		}
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负long鍨嬫暟瀛楋紝瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 2銆佺┖涓茶繑鍥?
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆?123褰㈠紡杩斿洖0.123锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * </pre>
	 *
	 * @param number 鏁板瓧锛屾敮鎸?x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @return long
	 * @since 5.5.5
	 */
	public static float parseFloat(String number) {
		if (StrUtil.isBlank(number)) {
			return 0f;
		}

		try {
			return Float.parseFloat(number);
		} catch (NumberFormatException e) {
			return parseNumber(number).floatValue();
		}
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负long鍨嬫暟瀛楋紝瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 2銆佺┖涓茶繑鍥?
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆?123褰㈠紡杩斿洖0.123锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * </pre>
	 *
	 * @param number 鏁板瓧锛屾敮鎸?x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @return long
	 * @since 5.5.5
	 */
	public static double parseDouble(String number) {
		if (StrUtil.isBlank(number)) {
			return 0D;
		}

		try {
			return Double.parseDouble(number);
		} catch (NumberFormatException e) {
			return parseNumber(number).doubleValue();
		}
	}

	/**
	 * 灏嗘寚瀹氬瓧绗︿覆杞崲涓簕@link Number} 瀵硅薄<br>
	 * 姝ゆ柟娉曚笉鏀寔绉戝璁℃暟娉?
	 *
	 * <p>
	 * 闇€瑕佹敞鎰忕殑鏄紝鍦ㄤ笉鍚孡ocale涓嬶紝鏁板瓧鐨勮〃绀哄舰寮忎篃鏄笉鍚岀殑锛屼緥濡傦細<br>
	 * 寰峰浗銆佽嵎鍏般€佹瘮鍒╂椂銆佷腹楹︺€佹剰澶у埄銆佺綏椹凹浜氬拰娆ф床澶у鍦板尯浣跨敤`,`鍖哄垎灏忔暟<br>
	 * 涔熷氨鏄锛屽湪杩欎簺鍥藉鍦板尯锛?.20琛ㄧず120锛岃€岄潪1.2銆?
	 * </p>
	 *
	 * @param numberStr Number瀛楃涓?
	 * @return Number瀵硅薄
	 * @throws NumberFormatException 鍖呰浜唟@link ParseException}锛屽綋缁欏畾鐨勬暟瀛楀瓧绗︿覆鏃犳硶瑙ｆ瀽鏃舵姏鍑?
	 * @since 4.1.15
	 */
	public static Number parseNumber(String numberStr) throws NumberFormatException {
		if (StrUtil.startWithIgnoreCase(numberStr, "0x")) {
			// 0x04琛ㄧず16杩涘埗鏁?
			return Long.parseLong(numberStr.substring(2), 16);
		} else if (StrUtil.startWith(numberStr, '+')) {
			// issue#I79VS7
			numberStr = StrUtil.subSuf(numberStr, 1);
		}

		// issue@4197@Github 杞负鍗婅
		numberStr = Convert.toDBC(numberStr);

		// issue#IDJ1NS@Gitee 澶勭悊绉戝璁℃暟娉旹+鏍煎紡
		// NumberFormat瀵笶+鏍煎紡鏀寔涓嶄匠,浣跨敤BigDecimal鐩存帴瑙ｆ瀽
		if (StrUtil.containsIgnoreCase(numberStr, "e")) {
			try {
				return new BigDecimal(numberStr);
			} catch (NumberFormatException e) {
				// BigDecimal瑙ｆ瀽澶辫触,缁х画浣跨敤NumberFormat灏濊瘯
			}
		}

		try {
			final NumberFormat format = NumberFormat.getInstance();
			if (format instanceof DecimalFormat) {
				// issue#1818@Github
				// 褰撳瓧绗︿覆鏁板瓧瓒呭嚭double鐨勯暱搴︽椂锛屼細瀵艰嚧鎴柇锛屾澶勪娇鐢˙igDecimal鎺ユ敹
				((DecimalFormat) format).setParseBigDecimal(true);
			}
			return format.parse(numberStr);
		} catch (ParseException e) {
			final NumberFormatException nfe = new NumberFormatException(e.getMessage());
			nfe.initCause(e);
			throw nfe;
		}
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负 {@link java.lang.Integer } 瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?x寮€澶寸殑瑙嗕负16杩涘埗鏁板瓧
	 * 2銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆佺┖涓茶繑鍥?
	 * 5銆?123褰㈠紡杩斿洖0锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * 6銆?23.56鎴彇灏忔暟鐐逛箣鍓嶇殑鏁板瓧锛屽拷鐣ュ皬鏁伴儴鍒?
	 * 7銆佽В鏋愬け璐ヨ繑鍥為粯璁ゅ€?
	 * </pre>
	 *
	 * @param numberStr    鏁板瓧瀛楃涓诧紝鏀寔0x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @param defaultValue 濡傛灉瑙ｆ瀽澶辫触, 灏嗚繑鍥瀌efaultValue, 鍏佽null
	 * @return Integer
	 */
	public static Integer parseInt(String numberStr, Integer defaultValue) {
		if (StrUtil.isBlank(numberStr)) {
			return defaultValue;
		}

		try {
			return parseInt(numberStr);
		} catch (NumberFormatException ignore) {

		}

		return defaultValue;
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负 {@link java.lang.Long } 瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?x寮€澶寸殑瑙嗕负16杩涘埗鏁板瓧
	 * 2銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆佺┖涓茶繑鍥?
	 * 5銆?123褰㈠紡杩斿洖0锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * 6銆?23.56鎴彇灏忔暟鐐逛箣鍓嶇殑鏁板瓧锛屽拷鐣ュ皬鏁伴儴鍒?
	 * 7銆佽В鏋愬け璐ヨ繑鍥為粯璁ゅ€?
	 * </pre>
	 *
	 * @param numberStr    鏁板瓧瀛楃涓诧紝鏀寔0x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @param defaultValue 濡傛灉瑙ｆ瀽澶辫触, 灏嗚繑鍥瀌efaultValue, 鍏佽null
	 * @return Long
	 */
	public static Long parseLong(String numberStr, Long defaultValue) {
		if (StrUtil.isBlank(numberStr)) {
			return defaultValue;
		}

		try {
			return parseLong(numberStr);
		} catch (NumberFormatException ignore) {

		}

		return defaultValue;
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负 {@link java.lang.Float } 瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 2銆佺┖涓茶繑鍥?
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆?123褰㈠紡杩斿洖0.123锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * </pre>
	 *
	 * @param numberStr    鏁板瓧瀛楃涓诧紝鏀寔0x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @param defaultValue 濡傛灉瑙ｆ瀽澶辫触, 灏嗚繑鍥瀌efaultValue, 鍏佽null
	 * @return Float
	 */
	public static Float parseFloat(String numberStr, Float defaultValue) {
		if (StrUtil.isBlank(numberStr)) {
			return defaultValue;
		}

		try {
			return parseFloat(numberStr);
		} catch (NumberFormatException ignore) {

		}

		return defaultValue;
	}

	/**
	 * 瑙ｆ瀽杞崲鏁板瓧瀛楃涓蹭负 {@link java.lang.Double } 瑙勫垯濡備笅锛?
	 *
	 * <pre>
	 * 1銆?寮€澶寸殑蹇界暐寮€澶寸殑0
	 * 2銆佺┖涓茶繑鍥?
	 * 3銆佸叾瀹冩儏鍐垫寜鐓?0杩涘埗杞崲
	 * 4銆?123褰㈠紡杩斿洖0.123锛堟寜鐓у皬浜?鐨勫皬鏁板寰咃級
	 * </pre>
	 *
	 * @param numberStr    鏁板瓧瀛楃涓诧紝鏀寔0x寮€澶淬€?寮€澶村拰鏅€氬崄杩涘埗
	 * @param defaultValue 濡傛灉瑙ｆ瀽澶辫触, 灏嗚繑鍥瀌efaultValue, 鍏佽null
	 * @return Double
	 */
	public static Double parseDouble(String numberStr, Double defaultValue) {
		if (StrUtil.isBlank(numberStr)) {
			return defaultValue;
		}

		try {
			return parseDouble(numberStr);
		} catch (NumberFormatException ignore) {

		}

		return defaultValue;
	}

	/**
	 * 灏嗘寚瀹氬瓧绗︿覆杞崲涓簕@link Number }
	 * 姝ゆ柟娉曚笉鏀寔绉戝璁℃暟娉?
	 *
	 * @param numberStr    Number瀛楃涓?
	 * @param defaultValue 濡傛灉瑙ｆ瀽澶辫触, 灏嗚繑鍥瀌efaultValue, 鍏佽null
	 * @return Number瀵硅薄
	 */
	public static Number parseNumber(String numberStr, Number defaultValue) {
		if (StrUtil.isBlank(numberStr)) {
			return defaultValue;
		}

		try {
			return parseNumber(numberStr);
		} catch (NumberFormatException ignore) {

		}

		return defaultValue;
	}

	/**
	 * int鍊艰浆byte鏁扮粍锛屼娇鐢ㄥぇ绔瓧鑺傚簭锛堥珮浣嶅瓧鑺傚湪鍓嶏紝浣庝綅瀛楄妭鍦ㄥ悗锛?br>
	 * 瑙侊細<a href="http://www.ruanyifeng.com/blog/2016/11/byte-order.html">http://www.ruanyifeng.com/blog/2016/11/byte-order.html</a>
	 *
	 * @param value 鍊?
	 * @return byte鏁扮粍
	 * @since 4.4.5
	 */
	public static byte[] toBytes(int value) {
		final byte[] result = new byte[4];

		result[0] = (byte) (value >> 24);
		result[1] = (byte) (value >> 16);
		result[2] = (byte) (value >> 8);
		result[3] = (byte) (value /* >> 0 */);

		return result;
	}

	/**
	 * byte鏁扮粍杞琲nt锛屼娇鐢ㄥぇ绔瓧鑺傚簭锛堥珮浣嶅瓧鑺傚湪鍓嶏紝浣庝綅瀛楄妭鍦ㄥ悗锛?br>
	 * 瑙侊細<a href="http://www.ruanyifeng.com/blog/2016/11/byte-order.html">http://www.ruanyifeng.com/blog/2016/11/byte-order.html</a>
	 *
	 * @param bytes byte鏁扮粍
	 * @return int
	 * @since 4.4.5
	 */
	public static int toInt(byte[] bytes) {
		return (bytes[0] & 0xff) << 24//
			| (bytes[1] & 0xff) << 16//
			| (bytes[2] & 0xff) << 8//
			| (bytes[3] & 0xff);
	}

	/**
	 * 浠ユ棤绗﹀彿瀛楄妭鏁扮粍鐨勫舰寮忚繑鍥炰紶鍏ュ€笺€?
	 *
	 * @param value 闇€瑕佽浆鎹㈢殑鍊?
	 * @return 鏃犵鍙穊ytes
	 * @since 4.5.0
	 */
	public static byte[] toUnsignedByteArray(BigInteger value) {
		byte[] bytes = value.toByteArray();

		if (bytes[0] == 0) {
			byte[] tmp = new byte[bytes.length - 1];
			System.arraycopy(bytes, 1, tmp, 0, tmp.length);

			return tmp;
		}

		return bytes;
	}

	/**
	 * 浠ユ棤绗﹀彿瀛楄妭鏁扮粍鐨勫舰寮忚繑鍥炰紶鍏ュ€笺€?
	 *
	 * @param length bytes闀垮害
	 * @param value  闇€瑕佽浆鎹㈢殑鍊?
	 * @return 鏃犵鍙穊ytes
	 * @since 4.5.0
	 */
	public static byte[] toUnsignedByteArray(int length, BigInteger value) {
		byte[] bytes = value.toByteArray();
		if (bytes.length == length) {
			return bytes;
		}

		int start = bytes[0] == 0 ? 1 : 0;
		int count = bytes.length - start;

		if (count > length) {
			throw new IllegalArgumentException("standard length exceeded for value");
		}

		byte[] tmp = new byte[length];
		System.arraycopy(bytes, start, tmp, tmp.length - count, count);
		return tmp;
	}

	/**
	 * 鏃犵鍙穊ytes杞瑊@link BigInteger}
	 *
	 * @param buf buf 鏃犵鍙穊ytes
	 * @return {@link BigInteger}
	 * @since 4.5.0
	 */
	public static BigInteger fromUnsignedByteArray(byte[] buf) {
		return new BigInteger(1, buf);
	}

	/**
	 * 鏃犵鍙穊ytes杞瑊@link BigInteger}
	 *
	 * @param buf    鏃犵鍙穊ytes
	 * @param off    璧峰浣嶇疆
	 * @param length 闀垮害
	 * @return {@link BigInteger}
	 */
	public static BigInteger fromUnsignedByteArray(byte[] buf, int off, int length) {
		byte[] mag = buf;
		if (off != 0 || length != buf.length) {
			mag = new byte[length];
			System.arraycopy(buf, off, mag, 0, length);
		}
		return new BigInteger(1, mag);
	}

	/**
	 * 妫€鏌ユ槸鍚︿负鏈夋晥鐨勬暟瀛?br>
	 * 妫€鏌ouble鍜孎loat鏄惁涓烘棤闄愬ぇ锛屾垨鑰匩ot a Number<br>
	 * 闈炴暟瀛楃被鍨嬪拰Null灏嗚繑鍥瀎alse
	 *
	 * @param number 琚鏌ョ被鍨?
	 * @return 妫€鏌ョ粨鏋滐紝闈炴暟瀛楃被鍨嬪拰Null灏嗚繑鍥瀎alse
	 * @since 4.6.7
	 */
	public static boolean isValidNumber(Number number) {
		if (null == number) {
			return false;
		}
		if (number instanceof Double) {
			return (false == ((Double) number).isInfinite()) && (false == ((Double) number).isNaN());
		} else if (number instanceof Float) {
			return (false == ((Float) number).isInfinite()) && (false == ((Float) number).isNaN());
		}
		return true;
	}

	/**
	 * 妫€鏌ユ槸鍚︿负鏈夋晥鐨勬暟瀛?br>
	 * 妫€鏌ouble鍚︿负鏃犻檺澶э紝鎴栬€匩ot a Number锛圢aN锛?br>
	 *
	 * @param number 琚鏌ouble
	 * @return 妫€鏌ョ粨鏋?
	 * @since 5.7.0
	 */
	public static boolean isValid(double number) {
		return false == (Double.isNaN(number) || Double.isInfinite(number));
	}

	/**
	 * 妫€鏌ユ槸鍚︿负鏈夋晥鐨勬暟瀛?br>
	 * 妫€鏌ouble鍚︿负鏃犻檺澶э紝鎴栬€匩ot a Number锛圢aN锛?br>
	 *
	 * @param number 琚鏌ouble
	 * @return 妫€鏌ョ粨鏋?
	 * @since 5.7.0
	 */
	public static boolean isValid(float number) {
		return false == (Float.isNaN(number) || Float.isInfinite(number));
	}

	/**
	 * 璁＄畻鏁板琛ㄨ揪寮忕殑鍊硷紝鍙敮鎸佸姞鍑忎箻闄ゅ拰鍙栦綑<br>
	 * 濡傦細
	 * <pre class="code">
	 *   calculate("(0*1--3)-5/-4-(3*(-2.13))") -銆?10.64
	 * </pre>
	 *
	 * @param expression 鏁板琛ㄨ揪寮?
	 * @return 缁撴灉
	 * @since 5.7.6
	 */
	public static double calculate(String expression) {
		return Calculator.conversion(expression);
	}

	/**
	 * Number鍊艰浆鎹负double<br>
	 * float寮哄埗杞崲瀛樺湪绮惧害闂锛屾鏂规硶閬垮厤绮惧害涓㈠け
	 *
	 * @param value 琚浆鎹㈢殑float鍊?
	 * @return double鍊?
	 * @since 5.7.8
	 */
	public static double toDouble(Number value) {
		if (value instanceof Float) {
			return Double.parseDouble(value.toString());
		} else {
			return value.doubleValue();
		}
	}

	/**
	 * 妫€鏌ユ槸鍚︿负濂囨暟<br>
	 *
	 * @param num 琚垽鏂殑鏁板€?
	 * @return 鏄惁鏄鏁?
	 * @author GuoZG
	 * @since 5.7.17
	 */
	public static boolean isOdd(int num) {
		return (num & 1) == 1;
	}

	/**
	 * 妫€鏌ユ槸鍚︿负鍋舵暟<br>
	 *
	 * @param num 琚垽鏂殑鏁板€?
	 * @return 鏄惁鏄伓鏁?
	 * @author GuoZG
	 * @since 5.7.17
	 */
	public static boolean isEven(int num) {
		return false == isOdd(num);
	}

	// ------------------------------------------------------------------------------------------- Private method start
	private static int mathSubNode(int selectNum, int minNum) {
		if (selectNum == minNum) {
			return 1;
		} else {
			return selectNum * mathSubNode(selectNum - 1, minNum);
		}
	}

	private static int mathNode(int selectNum) {
		if (selectNum == 0) {
			return 1;
		} else {
			return selectNum * mathNode(selectNum - 1);
		}
	}
	// ------------------------------------------------------------------------------------------- Private method end
}
