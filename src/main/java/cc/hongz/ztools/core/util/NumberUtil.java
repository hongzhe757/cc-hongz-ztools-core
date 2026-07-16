package cc.hongz.ztools.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

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

    // --------------------------------------- equals

    /**
     * 比较大小，值相等 返回true.
     * <p>
     * 通过调用 {@link Double#doubleToLongBits(double)} 方法来判断是否相等，
     * 此方法判断值相等时忽略精度，即 0.00 == 0.
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 是否相等
     */
    public static boolean equals(final double num1, final double num2) {
        return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
    }

    /**
     * 比较大小，值相等 返回true.
     * <p>
     * 通过调用 {@link Float#floatToIntBits(float)} 方法来判断是否相等，
     * 此方法判断值相等时忽略精度，即 0.00f == 0f.
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 是否相等
     */
    public static boolean equals(final float num1, final float num2) {
        return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
    }

    /**
     * 比较大小，值相等 返回true.
     * <p>
     * 此方法修复传入long型数据由于没有本类型重载方法,导致数据精度丢失
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 是否相等
     */
    public static boolean equals(final long num1, final long num2) {
        return num1 == num2;
    }

    /**
     * 比较数字值是否相等，相等返回{@code true}.
     * <p>
     * 需要注意的是{@link BigDecimal}需要特殊处理，
     * BigDecimal使用compareTo方式判断，因为使用equals方法也判断小数位数，如2.0和2.00就不相等，
     * 此方法判断值相等时忽略精度的，即0.00 == 0
     *
     * @param number1 数字1
     * @param number2 数字2
     * @return 是否相等
     */
    public static boolean equals(final Number number1, final Number number2) {
        if (number1 instanceof BigDecimal && number2 instanceof BigDecimal) {
            return equals((BigDecimal) number1, (BigDecimal) number2);
        }
        if (number1 == null || number2 == null) {
            return number1 == number2;
        }
        return toBigDecimal(number1).compareTo(toBigDecimal(number2)) == 0;
    }

    /**
     * 比较大小，值相等 返回true.
     * <p>
     * 此方法通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等，
     * 此方法判断值相等时忽略精度的，即0.00 == 0
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否相等
     */
    public static boolean equals(final BigDecimal bigNum1, final BigDecimal bigNum2) {
        if (bigNum1 == bigNum2) {
            return true;
        }
        if (bigNum1 == null || bigNum2 == null) {
            return false;
        }
        return 0 == bigNum1.compareTo(bigNum2);
    }

    /**
     * 检查是否为有效的数字.
     * <p>
     * 检查Double和Float是否为无限大，或者Not a Number，非数字类型和Null将返回true
     *
     * @param number 被检查类型
     * @return 检查结果，非数字类型和Null将返回true
     */
    /**
     * 四舍五入.
     *
     * @param number       数字
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal round(final BigDecimal number, final int scale, final RoundingMode roundingMode) {
        if (null == number) {
            return BigDecimal.ZERO;
        }
        if (scale < 0) {
            return number.setScale(0, roundingMode == null ? RoundingMode.HALF_UP : roundingMode);
        }
        return number.setScale(scale, roundingMode == null ? RoundingMode.HALF_UP : roundingMode);
    }

    /**
     * 四舍五入.
     *
     * @param v            数值
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal round(final double v, final int scale, final RoundingMode roundingMode) {
        return round(toBigDecimal(v), scale, roundingMode);
    }

    public static boolean isValidNumber(final Number number) {
        if (null == number) {
            return false;
        }
        if (number instanceof Double) {
            return (!(((Double) number).isInfinite())) && (!(((Double) number).isNaN()));
        } else if (number instanceof Float) {
            return (!(((Float) number).isInfinite())) && (!(((Float) number).isNaN()));
        }
        return true;
    }

    // --------------------------------------- Static method end
}
