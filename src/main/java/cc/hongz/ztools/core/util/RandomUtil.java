package cc.hongz.ztools.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import cc.hongz.ztools.core.date.DateField;
import cc.hongz.ztools.core.date.DateTime;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.WeightRandom;
import cc.hongz.ztools.core.lang.WeightRandom.WeightObj;

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
     * 获取SHA1PRNG的{@link SecureRandom}.
     *
     * @param seed 随机数种子
     * @return {@link SecureRandom}
     */
    public static SecureRandom getSHA1PRNGRandom(final byte[] seed) {
        final SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (final NoSuchAlgorithmException e) {
            throw new UtilException(e);
        }
        if (null != seed) {
            random.setSeed(seed);
        }
        return random;
    }

    /**
     * 获取强安全随机生成器.
     *
     * @return {@link SecureRandom}
     */
    public static SecureRandom getSecureRandomStrong() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 获得随机Boolean值.
     *
     * @return true or false
     */
    public static boolean randomBoolean() {
        return 0 == randomInt(2);
    }

    /**
     * 随机bytes.
     *
     * @param length 长度
     * @return bytes
     */
    public static byte[] randomBytes(final int length) {
        final byte[] bytes = new byte[length];
        getRandom().nextBytes(bytes);
        return bytes;
    }

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
     * 获得指定范围内的随机数.
     *
     * @param min        最小数
     * @param max        最大数
     * @param includeMin 是否包含最小值
     * @param includeMax 是否包含最大值
     * @return 随机数
     */
    public static long randomLong(final long min, final long max, final boolean includeMin, final boolean includeMax) {
        long adjustedMin = min;
        long adjustedMax = max;
        if (!includeMin) {
            adjustedMin++;
        }
        if (includeMax) {
            adjustedMax++;
        }
        return getRandom().nextLong(adjustedMin, adjustedMax);
    }

    // --------------------------------------- float/double

    /**
     * 获得随机数[0, 1).
     *
     * @return 随机数
     */
    public static float randomFloat() {
        return getRandom().nextFloat();
    }

    /**
     * 获得指定范围内的随机数 [0,limit).
     *
     * @param limitExclude 上限（不含）
     * @return 随机数
     */
    public static float randomFloat(final float limitExclude) {
        return randomFloat(0, limitExclude);
    }

    /**
     * 获得指定范围内的随机数[min, max).
     *
     * @param minInclude 下限（含）
     * @param maxExclude 上限（不含）
     * @return 随机数
     */
    public static float randomFloat(final float minInclude, final float maxExclude) {
        if (minInclude == maxExclude) {
            return minInclude;
        }
        return minInclude + ((maxExclude - minInclude) * getRandom().nextFloat());
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
     * 获得指定范围内的随机数.
     *
     * @param minInclude 最小数（包含）
     * @param maxExclude 最大数（不包含）
     * @return 随机数
     */
    public static double randomDouble(final double minInclude, final double maxExclude) {
        return getRandom().nextDouble(minInclude, maxExclude);
    }

    /**
     * 获得指定范围内的随机数.
     *
     * @param minInclude   最小数（包含）
     * @param maxExclude   最大数（不包含）
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return 随机数
     */
    public static double randomDouble(final double minInclude, final double maxExclude, final int scale,
                                      final RoundingMode roundingMode) {
        return NumberUtil.round(randomDouble(minInclude, maxExclude), scale, roundingMode).doubleValue();
    }

    /**
     * 获得指定范围内的随机数.
     *
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return 随机数
     */
    public static double randomDouble(final int scale, final RoundingMode roundingMode) {
        return NumberUtil.round(randomDouble(), scale, roundingMode).doubleValue();
    }

    /**
     * 获得指定范围内的随机数 [0,limit).
     *
     * @param limit 上限（不含）
     * @return 随机数
     */
    public static double randomDouble(final double limit) {
        return getRandom().nextDouble(limit);
    }

    /**
     * 获得指定范围内的随机数.
     *
     * @param limit        上限（不含）
     * @param scale        保留小数位数
     * @param roundingMode 舍入模式
     * @return 随机数
     */
    public static double randomDouble(final double limit, final int scale, final RoundingMode roundingMode) {
        return NumberUtil.round(randomDouble(limit), scale, roundingMode).doubleValue();
    }

    // --------------------------------------- BigDecimal

    /**
     * 获得指定范围内的随机数[0, 1).
     *
     * @return 随机数
     */
    public static BigDecimal randomBigDecimal() {
        return NumberUtil.toBigDecimal(getRandom().nextDouble());
    }

    /**
     * 获得指定范围内的随机数 [0,limit).
     *
     * @param limit 最大数（不包含）
     * @return 随机数
     */
    public static BigDecimal randomBigDecimal(final BigDecimal limit) {
        return NumberUtil.toBigDecimal(getRandom().nextDouble(limit.doubleValue()));
    }

    /**
     * 获得指定范围内的随机数.
     *
     * @param minInclude 最小数（包含）
     * @param maxExclude 最大数（不包含）
     * @return 随机数
     */
    public static BigDecimal randomBigDecimal(final BigDecimal minInclude, final BigDecimal maxExclude) {
        return NumberUtil.toBigDecimal(getRandom().nextDouble(minInclude.doubleValue(), maxExclude.doubleValue()));
    }

    // --------------------------------------- randomEle

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
     * 获得指定范围内的随机数.
     *
     * @param min        最小数
     * @param max        最大数
     * @param includeMin 是否包含最小值
     * @param includeMax 是否包含最大值
     * @return 随机数
     */
    public static int randomInt(final int min, final int max, final boolean includeMin, final boolean includeMax) {
        int adjustedMin = min;
        int adjustedMax = max;
        if (!includeMin) {
            adjustedMin++;
        }
        if (includeMax) {
            adjustedMax++;
        }
        return getRandom().nextInt(adjustedMin, adjustedMax);
    }

    /**
     * 创建指定长度的随机索引.
     *
     * @param length 长度
     * @return 随机索引
     */
    public static int[] randomInts(final int length) {
        final int[] range = PrimitiveArrayUtil.range(length);
        for (int i = 0; i < length; i++) {
            final int random = randomInt(i, length);
            PrimitiveArrayUtil.swap(range, i, random);
        }
        return range;
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

    /**
     * 随机获得列表中的元素.
     *
     * @param <T>  元素类型
     * @param list 列表
     * @return 随机元素
     */
    public static <T> T randomEle(final List<T> list) {
        return randomEle(list, list.size());
    }

    /**
     * 随机获得列表中的元素.
     *
     * @param <T>   元素类型
     * @param list  列表
     * @param limit 限制列表的前N项
     * @return 随机元素
     */
    public static <T> T randomEle(final List<T> list, final int limit) {
        final int effectiveLimit = Math.min(limit, list.size());
        return list.get(randomInt(effectiveLimit));
    }

    /**
     * 随机获得数组中的元素.
     *
     * @param <T>   元素类型
     * @param array 列表
     * @return 随机元素
     */
    public static <T> T randomEle(final T[] array) {
        return randomEle(array, array.length);
    }

    /**
     * 随机获得数组中的元素.
     *
     * @param <T>   元素类型
     * @param array 列表
     * @param limit 限制列表的前N项
     * @return 随机元素
     */
    public static <T> T randomEle(final T[] array, final int limit) {
        final int effectiveLimit = Math.min(limit, array.length);
        return array[randomInt(effectiveLimit)];
    }

    /**
     * 随机获得列表中的一定量元素.
     *
     * @param <T>   元素类型
     * @param list  列表
     * @param count 随机取出的个数
     * @return 随机元素
     */
    public static <T> List<T> randomEles(final List<T> list, final int count) {
        final List<T> result = new ArrayList<>(count);
        final int limit = list.size();
        while (result.size() < count) {
            result.add(randomEle(list, limit));
        }
        return result;
    }

    /**
     * 随机获得列表中的一定量的不重复元素.
     *
     * @param source 列表
     * @param count  随机取出的个数
     * @param <T>    元素类型
     * @return 随机列表
     */
    public static <T> List<T> randomEleList(final List<T> source, final int count) {
        if (count >= source.size()) {
            return new ArrayList<>(source);
        }
        final int[] randomList = ArrayUtil.sub(randomInts(source.size()), 0, count);
        final List<T> result = new ArrayList<>();
        for (final int e : randomList) {
            result.add(source.get(e));
        }
        return result;
    }

    /**
     * 随机获得列表中的一定量的不重复元素，返回Set.
     *
     * @param <T>        元素类型
     * @param collection 列表
     * @param count      随机取出的个数
     * @return 随机元素
     */
    public static <T> Set<T> randomEleSet(final Collection<T> collection, final int count) {
        final ArrayList<T> source = new ArrayList<>(new LinkedHashSet<>(collection));
        if (count > source.size()) {
            throw new IllegalArgumentException("Count is larger than collection distinct size !");
        }
        final Set<T> result = new LinkedHashSet<>(count);
        final int limit = source.size();
        while (result.size() < count) {
            result.add(randomEle(source, limit));
        }
        return result;
    }

    // --------------------------------------- randomString

    /**
     * 获得一个随机的字符串（只包含数字和字符）.
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomString(final int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * 获得一个随机的字符串（只包含数字和小写字母）.
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomStringLower(final int length) {
        return randomString(BASE_CHAR_NUMBER_LOWER, length);
    }

    /**
     * 获得一个随机的字符串（只包含数字和大写字符）.
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomStringUpper(final int length) {
        return randomString(BASE_CHAR_NUMBER, length).toUpperCase();
    }

    /**
     * 获得一个随机的字符串并排除指定字符串.
     *
     * @param length   字符串的长度
     * @param elemData 要排除的字符串
     * @return 随机字符串
     */
    public static String randomStringWithoutStr(final int length, final String elemData) {
        String baseStr = BASE_CHAR_NUMBER;
        baseStr = StrUtil.removeAll(baseStr, elemData.toCharArray());
        return randomString(baseStr, length);
    }

    /**
     * 获得一个随机的字符串（小写）并排除指定字符串.
     *
     * @param length   字符串的长度
     * @param elemData 要排除的字符串
     * @return 随机字符串
     */
    public static String randomStringLowerWithoutStr(final int length, final String elemData) {
        String baseStr = BASE_CHAR_NUMBER_LOWER;
        baseStr = StrUtil.removeAll(baseStr, elemData.toLowerCase().toCharArray());
        return randomString(baseStr, length);
    }

    /**
     * 获得一个只包含数字的字符串.
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomNumbers(final int length) {
        return randomString(BASE_NUMBER, length);
    }

    // --------------------------------------- randomChar

    /**
     * 随机汉字.
     *
     * @return 随机的汉字字符
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static char randomChinese() {
        return (char) randomInt('\u4E00', '\u9FFF');
    }

    /**
     * 随机数字，数字为0~9单个数字.
     *
     * @return 随机数字字符
     */
    public static char randomNumber() {
        return randomChar(BASE_NUMBER);
    }

    /**
     * 随机字母或数字，小写.
     *
     * @return 随机字符
     */
    public static char randomChar() {
        return randomChar(BASE_CHAR_NUMBER);
    }

    // --------------------------------------- weightRandom

    /**
     * 带有权重的随机生成器.
     *
     * @param <T>        随机对象类型
     * @param weightObjs 带有权重的对象列表
     * @return {@link WeightRandom}
     * @since 4.0.3
     */
    public static <T> WeightRandom<T> weightRandom(final WeightObj<T>[] weightObjs) {
        return new WeightRandom<>(weightObjs);
    }

    /**
     * 带有权重的随机生成器.
     *
     * @param <T>        随机对象类型
     * @param weightObjs 带有权重的对象列表
     * @return {@link WeightRandom}
     * @since 4.0.3
     */
    public static <T> WeightRandom<T> weightRandom(final Iterable<WeightObj<T>> weightObjs) {
        return new WeightRandom<>(weightObjs);
    }

    // --------------------------------------- randomDate

    /**
     * 以当天为基准，随机产生一个日期.
     *
     * @param min 偏移最小天，可以为负数表示过去的时间（包含）
     * @param max 偏移最大天，可以为负数表示过去的时间（不包含）
     * @return 随机日期（随机天，其它时间不变）
     * @since 4.0.8
     */
    public static DateTime randomDay(final int min, final int max) {
        return randomDate(new DateTime(), DateField.DAY_OF_YEAR, min, max);
    }

    /**
     * 以给定日期为基准，随机产生一个日期.
     *
     * @param baseDate  基准日期
     * @param dateField 偏移的时间字段，例如时、分、秒等
     * @param min       偏移最小量，可以为负数表示过去的时间（包含）
     * @param max       偏移最大量，可以为负数表示过去的时间（不包含）
     * @return 随机日期
     * @since 4.5.8
     */
    public static DateTime randomDate(final Date baseDate, final DateField dateField, final int min, final int max) {
        Date date = (baseDate != null) ? baseDate : new DateTime();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dateField.getValue(), randomInt(min, max));
        return new DateTime(calendar.getTime());
    }

    // --------------------------------------- Static method end
}
