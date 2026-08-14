package cc.hongz.ztools.core.math;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 组合，即C(n, m).
 * 排列组合相关类，参考：http://cgs1999.iteye.com/blog/2327664
 *
 * @author looly
 * @since 4.0.6
 */
public class Combination implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用于组合的数据.
     */
    private final String[] datas;

    /**
     * 组合，即C(n, m).
     * 排列组合相关类，参考：http://cgs1999.iteye.com/blog/2327664
     *
     * @param datas 用于组合的数据
     */
    public Combination(final String[] datas) {
        this.datas = datas;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!).
     * <p>注意：此方法内部使用 BigInteger 修复了旧版 factorial 的计算错误，
     * 但最终仍以 long 返回，因此当结果超过 long 范围时仍会溢出。</p>
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 组合数
     * @deprecated 建议使用 {@link #countBig(int, int)} 或 {@link #countSafe(int, int)}
     */
    @Deprecated
    public static long count(final int n, final int m) {
        final BigInteger big = countBig(n, m);
        return big.longValue();
    }

    /**
     * 计算组合数 C(n, m) 的 BigInteger 精确版本.
     *
     * @param n 总数 n（必须大于等于 0）
     * @param m 取出 m（必须大于等于 0）
     * @return C(n, m) 的 BigInteger 精确值；当 m 大于 n 时返回 BigInteger.ZERO
     */
    public static BigInteger countBig(final int n, final int m) {
        if (n < 0 || m < 0) {
            throw new IllegalArgumentException(
                    "n and m must be non-negative. got n=" + n + ", m=" + m);
        }
        if (m > n) {
            return BigInteger.ZERO;
        }
        if (m == 0 || n == m) {
            return BigInteger.ONE;
        }
        final int effectiveM = Math.min(m, n - m);
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= effectiveM; i++) {
            final int numerator = n - effectiveM + i;
            result = result.multiply(BigInteger.valueOf(numerator))
                    .divide(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * 安全组合数 long 版本.
     * <p>若结果超出 long 范围，会抛 ArithmeticException，而非溢出。</p>
     *
     * @param n 总数 n（必须大于等于 0）
     * @param m 取出 m（必须大于等于 0）
     * @return C(n, m) 的 long 精确值；当 m 大于 n 时返回 0L
     */
    public static long countSafe(final int n, final int m) {
        final BigInteger big = countBig(n, m);
        return big.longValueExact();
    }

    /**
     * 计算组合总数，即C(n, 1) + C(n, 2) + C(n, 3)...
     *
     * @param n 总数
     * @return 组合数
     */
    public static long countAll(final int n) {
        if (n < 0 || n > Long.SIZE - 1) {
            throw new IllegalArgumentException(String.format(
                    "countAll must have n >= 0 and n <= 63, but got n=%d", n));
        }
        return n == Long.SIZE - 1 ? Long.MAX_VALUE : (1L << n) - 1;
    }

    /**
     * 组合选择（从列表中选择m个组合）.
     *
     * @param m 选择个数
     * @return 组合结果
     */
    public List<String[]> select(final int m) {
        final List<String[]> result = new ArrayList<>(
                (int) count(this.datas.length, m));
        select(0, new String[m], 0, result);
        return result;
    }

    /**
     * 全组合.
     *
     * @return 全排列结果
     */
    public List<String[]> selectAll() {
        final List<String[]> result = new ArrayList<>(
                (int) countAll(this.datas.length));
        for (int i = 1; i <= this.datas.length; i++) {
            result.addAll(select(i));
        }
        return result;
    }

    /**
     * 组合选择.
     *
     * @param dataIndex   待选开始索引
     * @param resultList  前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     * @param result      结果集
     */
    private void select(final int dataIndex, final String[] resultList,
            final int resultIndex, final List<String[]> result) {
        final int resultLen = resultList.length;
        final int resultCount = resultIndex + 1;
        if (resultCount > resultLen) {
            result.add(Arrays.copyOf(resultList, resultList.length));
            return;
        }

        for (int i = dataIndex; i < datas.length + resultCount - resultLen;
                i++) {
            resultList[resultIndex] = datas[i];
            select(i + 1, resultList, resultIndex + 1, result);
        }
    }
}
