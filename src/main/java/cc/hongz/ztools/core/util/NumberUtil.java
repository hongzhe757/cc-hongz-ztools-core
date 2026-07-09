package cc.hongz.ztools.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字计算工具类（captcha 最小子集 + 阶段 1 Segment 依赖）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，BigDecimal 精确四则运算.
 * @author hongzhe.qin
 */
public final class NumberUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private NumberUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 字符串转 {@link BigDecimal}.
     *
     * @param numberStr 数字字符串
     * @return BigDecimal，空串时返回 0
     */
    public static BigDecimal toBigDecimal(final String numberStr) {
        if (numberStr == null || numberStr.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(numberStr);
    }

    /**
     * 数字转 {@link BigDecimal}，null 返回 0.
     *
     * @param number 数字
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(final Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof Long) {
            return new BigDecimal((Long) number);
        }
        if (number instanceof Integer) {
            return new BigDecimal((Integer) number);
        }
        if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }
        return new BigDecimal(number.toString());
    }

    /**
     * 精确减法（Number 类型）.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static BigDecimal sub(final Number v1, final Number v2) {
        return sub(new Number[]{v1, v2});
    }

    /**
     * 多值连减；空或全 null 时返回 0.
     *
     * @param values 被减值序列
     * @return 差
     */
    public static BigDecimal sub(final Number... values) {
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
     * 精确加法.
     *
     * @param v1 加数1
     * @param v2 加数2
     * @return 和
     */
    public static BigDecimal add(final String v1, final String v2) {
        return toBigDecimal(v1).add(toBigDecimal(v2));
    }

    /**
     * 精确减法.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 差
     */
    public static BigDecimal sub(final String v1, final String v2) {
        return toBigDecimal(v1).subtract(toBigDecimal(v2));
    }

    /**
     * 精确乘法.
     *
     * @param v1 乘数1
     * @param v2 乘数2
     * @return 积
     */
    public static BigDecimal mul(final String v1, final String v2) {
        return toBigDecimal(v1).multiply(toBigDecimal(v2));
    }

    /**
     * 精确除法.
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 商
     */
    public static BigDecimal div(final String v1, final String v2) {
        return toBigDecimal(v1).divide(toBigDecimal(v2));
    }

    /**
     * 多值连乘.
     *
     * @param values 乘数数组
     * @return 积
     */
    public static BigDecimal mul(final String... values) {
        if (values == null || values.length == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal result = toBigDecimal(values[0]);
        for (int i = 1; i < values.length; i++) {
            result = result.multiply(toBigDecimal(values[i]));
        }
        return result;
    }

    // --------------------------------------- Static method end
}
