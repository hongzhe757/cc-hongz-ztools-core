package cc.hongz.ztools.core.lang.id;

import cc.hongz.ztools.core.util.RandomUtil;

import java.security.SecureRandom;
import java.util.Random;

/**
 * NanoId，一个小型、安全、对 URL友好的唯一字符串 ID 生成器.
 * <p>
 * 此实现的逻辑基于JavaScript的NanoId实现，见：https://github.com/ai/nanoid
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.id.NanoId}。
 *
 * @author David Klebanoff
 */
public final class NanoId {

    /**
     * 私有构造，禁止实例化.
     */
    private NanoId() {
    }

    /**
     * 默认随机数生成器，使用 {@link SecureRandom} 确保健壮性.
     */
    private static final SecureRandom DEFAULT_NUMBER_GENERATOR = RandomUtil.getSecureRandom();

    /**
     * 默认随机字母表，使用URL安全的Base64字符.
     */
    private static final char[] DEFAULT_ALPHABET =
            "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 字母表最大长度（随机字节按 8 位掩码映射到字母表，故上限为 256）.
     */
    private static final int MAX_ALPHABET_SIZE = 256;

    /**
     * 默认长度.
     */
    public static final int DEFAULT_SIZE = 21;

    /**
     * 生成伪随机的NanoId字符串，长度为默认的 {@link #DEFAULT_SIZE}.
     *
     * @return 伪随机的NanoId字符串
     */
    public static String randomNanoId() {
        return randomNanoId(DEFAULT_SIZE);
    }

    /**
     * 生成伪随机的NanoId字符串.
     *
     * @param size ID长度
     * @return 伪随机的NanoId字符串
     */
    public static String randomNanoId(final int size) {
        return randomNanoId(null, null, size);
    }

    /**
     * 生成伪随机的NanoId字符串.
     *
     * @param random   随机数生成器
     * @param alphabet 随机字母表
     * @param size     ID长度
     * @return 伪随机的NanoId字符串
     */
    public static String randomNanoId(final Random random, final char[] alphabet, final int size) {
        final Random actualRandom = null == random ? DEFAULT_NUMBER_GENERATOR : random;
        final char[] actualAlphabet = null == alphabet ? DEFAULT_ALPHABET : alphabet;

        if (actualAlphabet.length == 0 || actualAlphabet.length >= MAX_ALPHABET_SIZE) {
            throw new IllegalArgumentException("Alphabet must contain between 1 and 255 symbols.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }

        final int mask = (2 << (int) Math.floor(Math.log(actualAlphabet.length - 1) / Math.log(2))) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / actualAlphabet.length);

        final StringBuilder idBuilder = new StringBuilder();

        while (true) {
            final byte[] bytes = new byte[step];
            actualRandom.nextBytes(bytes);
            for (int i = 0; i < step; i++) {
                final int alphabetIndex = bytes[i] & mask;
                if (alphabetIndex < actualAlphabet.length) {
                    idBuilder.append(actualAlphabet[alphabetIndex]);
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }
            }
        }
    }
}
