package cc.hongz.ztools.core.math;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * 单币种货币类，处理货币算术、币种和取整.
 *
 * <p>
 * 货币类中封装了货币金额和币种。目前金额在内部是long类型表示，
 * 单位是所属币种的最小货币单位（对人民币是分）.
 *
 * @author looly
 * @since 5.0.4
 */
public class Money implements Serializable, Comparable<Money> {
    private static final long serialVersionUID = -1004117971993390293L;

    /**
     * 缺省的币种代码，为CNY（人民币）.
     */
    public static final String DEFAULT_CURRENCY_CODE = "CNY";

    /**
     * 缺省的取整模式，为{@link RoundingMode#HALF_EVEN}.
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE =
            RoundingMode.HALF_EVEN;

    /**
     * 一组可能的元/分换算比例.
     */
    private static final int[] CENT_FACTORS = new int[]{1, 10, 100, 1000};

    /**
     * 金额，以分为单位.
     */
    private long cent;

    /**
     * 币种.
     */
    private final Currency currency;

    /**
     * 缺省构造器.
     */
    public Money() {
        this(0);
    }

    /**
     * 构造器.
     *
     * @param yuan 金额元数
     * @param cent 金额分数
     */
    public Money(final long yuan, final int cent) {
        this(yuan, cent, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器.
     *
     * @param yuan     金额元数
     * @param cent     金额分数
     * @param currency 货币单位
     */
    public Money(final long yuan, final int cent, final Currency currency) {
        this.currency = currency;

        if (0 == yuan) {
            this.cent = cent;
        } else {
            this.cent = (yuan * getCentFactor())
                    + (cent % getCentFactor());
        }
    }

    /**
     * 构造器.
     *
     * @param amount 金额，以元为单位
     */
    public Money(final String amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器.
     *
     * @param amount   金额，以元为单位
     * @param currency 币种
     */
    public Money(final String amount, final Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    /**
     * 构造器.
     *
     * @param amount       金额，以元为单位
     * @param currency     币种
     * @param roundingMode 取整模式
     */
    public Money(final String amount, final Currency currency,
            final RoundingMode roundingMode) {
        this(new BigDecimal(amount), currency, roundingMode);
    }

    /**
     * 构造器.
     *
     * @param amount 金额，以元为单位
     */
    public Money(final double amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器.
     *
     * @param amount   金额，以元为单位
     * @param currency 币种
     */
    public Money(final double amount, final Currency currency) {
        this.currency = currency;
        this.cent = Math.round(amount * getCentFactor());
    }

    /**
     * 构造器.
     *
     * @param amount 金额，以元为单位
     */
    public Money(final BigDecimal amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器.
     *
     * @param amount       金额，以元为单位
     * @param roundingMode 取整模式
     */
    public Money(final BigDecimal amount, final RoundingMode roundingMode) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE),
                roundingMode);
    }

    /**
     * 构造器.
     *
     * @param amount   金额，以元为单位
     * @param currency 币种
     */
    public Money(final BigDecimal amount, final Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 构造器.
     *
     * @param amount       金额，以元为单位
     * @param currency     币种
     * @param roundingMode 取整模式
     */
    public Money(final BigDecimal amount, final Currency currency,
            final RoundingMode roundingMode) {
        this.currency = currency;
        this.cent = rounding(
                amount.movePointRight(currency.getDefaultFractionDigits()),
                roundingMode);
    }

    /**
     * 获取本货币对象代表的金额数.
     *
     * @return 金额数，以元为单位
     */
    public BigDecimal getAmount() {
        return BigDecimal.valueOf(cent,
                currency.getDefaultFractionDigits());
    }

    /**
     * 设置本货币对象代表的金额数.
     *
     * @param amount 金额数，以元为单位
     */
    public void setAmount(final BigDecimal amount) {
        if (amount != null) {
            cent = rounding(
                    amount.movePointRight(currency.getDefaultFractionDigits()),
                    DEFAULT_ROUNDING_MODE);
        }
    }

    /**
     * 获取本货币对象代表的金额数.
     *
     * @return 金额数，以分为单位
     */
    public long getCent() {
        return cent;
    }

    /**
     * 获取本货币对象代表的币种.
     *
     * @return 本货币对象所代表的币种
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * 获取本货币币种的元/分换算比率.
     *
     * @return 本货币币种的元/分换算比率
     */
    public int getCentFactor() {
        return CENT_FACTORS[currency.getDefaultFractionDigits()];
    }

    /**
     * 判断本货币对象与另一对象是否相等.
     *
     * @param other 待比较的另一对象
     * @return {@code true}表示相等
     */
    @Override
    public boolean equals(final Object other) {
        return (other instanceof Money) && equals((Money) other);
    }

    /**
     * 判断本货币对象与另一货币对象是否相等.
     *
     * @param other 待比较的另一货币对象
     * @return {@code true}表示相等
     */
    public boolean equals(final Money other) {
        return currency.equals(other.currency) && (cent == other.cent);
    }

    /**
     * 计算本货币对象的杂凑值.
     *
     * @return 本货币对象的杂凑值
     */
    @Override
    public int hashCode() {
        return (int) (cent ^ (cent >>> Integer.SIZE));
    }

    /**
     * 货币比较.
     *
     * @param other 另一对象
     * @return -1表示小于，0表示等于，1表示大于
     * @throws IllegalArgumentException 待比较货币对象与本货币对象的币种不同
     */
    @Override
    public int compareTo(final Money other) {
        assertSameCurrencyAs(other);
        return Long.compare(cent, other.cent);
    }

    /**
     * 判断本货币对象是否大于另一货币对象.
     *
     * @param other 另一对象
     * @return true表示大于，false表示不大于
     * @throws IllegalArgumentException 待比较货币对象与本货币对象的币种不同
     */
    public boolean greaterThan(final Money other) {
        return compareTo(other) > 0;
    }

    /**
     * 货币加法.
     *
     * @param other 作为加数的货币对象
     * @return 相加后的结果
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     */
    public Money add(final Money other) {
        assertSameCurrencyAs(other);
        return newMoneyWithSameCurrency(cent + other.cent);
    }

    /**
     * 货币累加.
     *
     * @param other 作为加数的货币对象
     * @return 累加后的本货币对象
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     */
    public Money addTo(final Money other) {
        assertSameCurrencyAs(other);
        this.cent += other.cent;
        return this;
    }

    /**
     * 货币减法.
     *
     * @param other 作为减数的货币对象
     * @return 相减后的结果
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     */
    public Money subtract(final Money other) {
        assertSameCurrencyAs(other);
        return newMoneyWithSameCurrency(cent - other.cent);
    }

    /**
     * 货币累减.
     *
     * @param other 作为减数的货币对象
     * @return 累减后的本货币对象
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     */
    public Money subtractFrom(final Money other) {
        assertSameCurrencyAs(other);
        this.cent -= other.cent;
        return this;
    }

    /**
     * 货币乘法.
     *
     * @param val 乘数
     * @return 乘法后的结果
     */
    public Money multiply(final long val) {
        return newMoneyWithSameCurrency(cent * val);
    }

    /**
     * 货币累乘.
     *
     * @param val 乘数
     * @return 累乘后的本货币对象
     */
    public Money multiplyBy(final long val) {
        this.cent *= val;
        return this;
    }

    /**
     * 货币乘法.
     *
     * @param val 乘数
     * @return 相乘后的结果
     */
    public Money multiply(final double val) {
        return newMoneyWithSameCurrency(Math.round(cent * val));
    }

    /**
     * 货币累乘.
     *
     * @param val 乘数
     * @return 累乘后的本货币对象
     */
    public Money multiplyBy(final double val) {
        this.cent = Math.round(this.cent * val);
        return this;
    }

    /**
     * 货币乘法.
     *
     * @param val 乘数
     * @return 相乘后的结果
     */
    public Money multiply(final BigDecimal val) {
        return multiply(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币累乘.
     *
     * @param val 乘数
     * @return 累乘后的结果
     */
    public Money multiplyBy(final BigDecimal val) {
        return multiplyBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币乘法.
     *
     * @param val          乘数
     * @param roundingMode 取整方式
     * @return 相乘后的结果
     */
    public Money multiply(final BigDecimal val,
            final RoundingMode roundingMode) {
        final BigDecimal newCent =
                BigDecimal.valueOf(cent).multiply(val);
        return newMoneyWithSameCurrency(rounding(newCent, roundingMode));
    }

    /**
     * 货币累乘.
     *
     * @param val          乘数
     * @param roundingMode 取整方式
     * @return 累乘后的结果
     */
    public Money multiplyBy(final BigDecimal val,
            final RoundingMode roundingMode) {
        final BigDecimal newCent =
                BigDecimal.valueOf(cent).multiply(val);
        this.cent = rounding(newCent, roundingMode);
        return this;
    }

    /**
     * 货币除法.
     *
     * @param val 除数
     * @return 相除后的结果
     */
    public Money divide(final double val) {
        if (val == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return newMoneyWithSameCurrency(Math.round(cent / val));
    }

    /**
     * 货币累除.
     *
     * @param val 除数
     * @return 累除后的结果
     */
    public Money divideBy(final double val) {
        if (val == 0) {
            throw new ArithmeticException("Division by zero");
        }
        this.cent = Math.round(this.cent / val);
        return this;
    }

    /**
     * 货币除法.
     *
     * @param val 除数
     * @return 相除后的结果
     */
    public Money divide(final BigDecimal val) {
        return divide(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币除法.
     *
     * @param val          除数
     * @param roundingMode 取整模式
     * @return 相除后的结果
     */
    public Money divide(final BigDecimal val,
            final RoundingMode roundingMode) {
        final BigDecimal newCent =
                BigDecimal.valueOf(cent).divide(val, roundingMode);
        return newMoneyWithSameCurrency(newCent.longValue());
    }

    /**
     * 货币累除.
     *
     * @param val 除数
     * @return 累除后的结果
     */
    public Money divideBy(final BigDecimal val) {
        return divideBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币累除.
     *
     * @param val          除数
     * @param roundingMode 取整模式
     * @return 累除后的结果
     */
    public Money divideBy(final BigDecimal val,
            final RoundingMode roundingMode) {
        final BigDecimal newCent =
                BigDecimal.valueOf(cent).divide(val, roundingMode);
        this.cent = newCent.longValue();
        return this;
    }

    /**
     * 货币分配.
     *
     * @param targets 待分配的份数
     * @return 货币对象数组
     */
    public Money[] allocate(final int targets) {
        final Money[] results = new Money[targets];
        final Money lowResult = newMoneyWithSameCurrency(cent / targets);
        final Money highResult =
                newMoneyWithSameCurrency(lowResult.cent + 1);
        final int remainder = (int) (cent % targets);

        for (int i = 0; i < remainder; i++) {
            results[i] = highResult;
        }

        for (int i = remainder; i < targets; i++) {
            results[i] = lowResult;
        }

        return results;
    }

    /**
     * 货币分配.
     *
     * @param ratios 分配比例数组
     * @return 货币对象数组
     */
    public Money[] allocate(final long[] ratios) {
        final Money[] results = new Money[ratios.length];

        long total = 0;
        for (final long element : ratios) {
            total += element;
        }

        long remainder = cent;
        for (int i = 0; i < results.length; i++) {
            results[i] = newMoneyWithSameCurrency(
                    (cent * ratios[i]) / total);
            remainder -= results[i].cent;
        }

        for (int i = 0; i < remainder; i++) {
            results[i].cent++;
        }

        return results;
    }

    /**
     * 生成本对象的缺省字符串表示.
     *
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return getAmount().toString();
    }

    /**
     * 断言本货币对象与另一货币对象是否具有相同的币种.
     *
     * @param other 另一货币对象
     * @throws IllegalArgumentException 如果币种不同
     */
    protected void assertSameCurrencyAs(final Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Money math currency mismatch.");
        }
    }

    /**
     * 对BigDecimal型的值按指定取整方式取整.
     *
     * @param val          待取整的BigDecimal值
     * @param roundingMode 取整方式
     * @return 取整后的long型值
     */
    protected long rounding(final BigDecimal val,
            final RoundingMode roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    /**
     * 创建一个币种相同，具有指定金额的货币对象.
     *
     * @param cent 金额，以分为单位
     * @return 一个新建的货币对象
     */
    protected Money newMoneyWithSameCurrency(final long newCent) {
        final Money money = new Money(0, currency);
        money.cent = newCent;
        return money;
    }

    /**
     * 生成本对象内部变量的字符串表示，用于调试.
     *
     * @return 本对象内部变量的字符串表示
     */
    public String dump() {
        return new StringBuilder()
                .append("cent = ")
                .append(this.cent)
                .append(File.separatorChar)
                .append("currency = ")
                .append(this.currency)
                .toString();
    }

    /**
     * 设置货币的分值.
     *
     * @param cent 分值
     */
    public void setCent(final long cent) {
        this.cent = cent;
    }
}
