package cc.hongz.ztools.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类（captcha 最小子集 + 阶段 1 UUID/ByteUtil 测试依赖）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，基于 ThreadLocalRandom 生成随机值.
 * @author hongzhe.qin
 */
public final class RandomUtil {

    /**
     * 数字字符集.
     */
    public static final String BASE_NUMBER = "0123456789";
    /**
     * 小写字母字符集.
     */
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    /**
     * 小写字母与数字字符集.
     */
    public static final String BASE_CHAR_NUMBER_LOWER = BASE_CHAR + BASE_NUMBER;
    /**
     * 大小写字母与数字字符集.
     */
    public static final String BASE_CHAR_NUMBER = BASE_CHAR.toUpperCase() + BASE_CHAR_NUMBER_LOWER;

    /**
     * 私有构造，禁止实例化.
     */
    private RandomUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 获取 {@link ThreadLocalRandom} 实例.
     *
     * @return 随机数生成器
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 创建 {@link SecureRandom}.
     *
     * @param seed 种子，null 时使用默认
     * @return SecureRandom
     */
    public static SecureRandom createSecureRandom(final byte[] seed) {
        return (null == seed) ? new SecureRandom() : new SecureRandom(seed);
    }

    /**
     * 获取 {@link SecureRandom}.
     *
     * @return SecureRandom
     */
    public static SecureRandom getSecureRandom() {
        return getSecureRandom(null);
    }

    /**
     * 获取带种子的 {@link SecureRandom}.
     *
     * @param seed 种子
     * @return SecureRandom
     */
    public static SecureRandom getSecureRandom(final byte[] seed) {
        return createSecureRandom(seed);
    }

    /**
     * 按是否强随机返回 {@link Random}.
     *
     * @param isSecure 是否强随机
     * @return Random
     */
    public static Random getRandom(final boolean isSecure) {
        return isSecure ? getSecureRandom() : getRandom();
    }

    /**
     * 随机 long 值.
     *
     * @return 随机 long
     */
    public static long randomLong() {
        return getRandom().nextLong();
    }

    /**
     * 随机 long 值，范围 [0, limitExclude).
     *
     * @param limitExclude 上限（不含）
     * @return 随机 long
     */
    public static long randomLong(final long limitExclude) {
        return getRandom().nextLong(limitExclude);
    }

    /**
     * 随机 long 值，范围 [minInclude, maxExclude).
     *
     * @param minInclude 下限（含）
     * @param maxExclude 上限（不含）
     * @return 随机 long
     */
    public static long randomLong(final long minInclude, final long maxExclude) {
        return getRandom().nextLong(minInclude, maxExclude);
    }

    /**
     * 随机 double 值.
     *
     * @return 随机 double
     */
    public static double randomDouble() {
        return getRandom().nextDouble();
    }

    /**
     * 随机 int 值.
     *
     * @return 随机整数
     */
    public static int randomInt() {
        return getRandom().nextInt();
    }

    /**
     * 随机 int 值，范围 [0, limitExclude).
     *
     * @param limitExclude 上限（不含）
     * @return 随机整数
     */
    public static int randomInt(final int limitExclude) {
        return getRandom().nextInt(limitExclude);
    }

    /**
     * 随机 int 值，范围 [minInclude, maxExclude).
     *
     * @param minInclude 下限（含）
     * @param maxExclude 上限（不含）
     * @return 随机整数
     */
    public static int randomInt(final int minInclude, final int maxExclude) {
        return getRandom().nextInt(minInclude, maxExclude);
    }

    /**
     * 从指定字符集中随机生成字符串.
     *
     * @param baseString 字符样本
     * @param length     长度
     * @return 随机字符串
     */
    public static String randomString(final String baseString, final int length) {
        if (StrUtil.isBlank(baseString)) {
            return StrUtil.EMPTY;
        }
        int len = length < 1 ? 1 : length;
        final StringBuilder sb = new StringBuilder(len);
        final int baseLength = baseString.length();
        for (int i = 0; i < len; i++) {
            sb.append(baseString.charAt(randomInt(baseLength)));
        }
        return sb.toString();
    }

    /**
     * 从指定字符集中随机取一个字符.
     *
     * @param baseString 字符样本
     * @return 随机字符
     */
    public static char randomChar(final String baseString) {
        return baseString.charAt(randomInt(baseString.length()));
    }

    // --------------------------------------- Static method end
}
