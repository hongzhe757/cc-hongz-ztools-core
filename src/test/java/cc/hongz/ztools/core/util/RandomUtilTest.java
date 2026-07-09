package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link RandomUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法与字符集常量行为；含阶段 1 UUID/ByteUtil 依赖的 SecureRandom、long/double 随机.
 *
 * @author hongzhe.qin
 */
@DisplayName("RandomUtil 随机数")
class RandomUtilTest {

    /**
     * 验证 {@link RandomUtil#getRandom()} 返回 {@link ThreadLocalRandom} 实例.
     */
    @Test
    void getRandom() {
        assertTrue(RandomUtil.getRandom() instanceof ThreadLocalRandom);
    }

    /**
     * 验证 {@link RandomUtil#randomInt()} 返回 int 值（无范围约束）.
     */
    @Test
    void randomIntNoBound() {
        RandomUtil.randomInt();
    }

    /**
     * 验证 {@link RandomUtil#randomInt(int)} 返回值落在 [0, bound) 区间.
     */
    @Test
    void randomIntRange() {
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(10);
            assertTrue(v >= 0 && v < 10);
        }
    }

    /**
     * 验证 {@link RandomUtil#randomInt(int, int)} 返回值落在 [min, max) 区间.
     */
    @Test
    void randomIntMinMax() {
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(3, 8);
            assertTrue(v >= 3 && v < 8);
        }
    }

    /**
     * 验证 {@link RandomUtil#randomString(String, int)} 从指定字符集生成定长字符串.
     */
    @Test
    void randomStringFromBase() {
        final String s = RandomUtil.randomString(RandomUtil.BASE_NUMBER, 6);
        assertEquals(6, s.length());
        assertTrue(s.chars().allMatch(c -> RandomUtil.BASE_NUMBER.indexOf(c) >= 0));
    }

    /**
     * 验证 {@link RandomUtil#randomString(String, int)} 在 length 小于 1 时按 1 处理.
     */
    @Test
    void randomStringMinLengthOne() {
        assertEquals(1, RandomUtil.randomString(RandomUtil.BASE_NUMBER, 0).length());
    }

    /**
     * 验证空字符集时 {@link RandomUtil#randomString(String, int)} 返回空串.
     */
    @Test
    void randomStringBlankBaseReturnsEmpty() {
        assertEquals(StrUtil.EMPTY, RandomUtil.randomString("", 5));
    }

    /**
     * 验证 {@link RandomUtil#randomChar(String)} 仅从给定字符集中选取.
     */
    @Test
    void randomCharFromBase() {
        final char c = RandomUtil.randomChar("AB");
        assertTrue(c == 'A' || c == 'B');
    }

    /**
     * 验证字符集常量非空且包含预期字符.
     */
    @Test
    void baseConstants() {
        assertFalse(RandomUtil.BASE_NUMBER.isEmpty());
        assertFalse(RandomUtil.BASE_CHAR.isEmpty());
        assertTrue(RandomUtil.BASE_CHAR_NUMBER.contains("A"));
        assertTrue(RandomUtil.BASE_CHAR_NUMBER_LOWER.contains("a"));
    }

    /**
     * 验证 {@link RandomUtil#getSecureRandom()} 返回 {@link SecureRandom} 实例.
     */
    @Test
    @DisplayName("getSecureRandom 返回 SecureRandom")
    void getSecureRandom() {
        assertTrue(RandomUtil.getSecureRandom() instanceof SecureRandom);
    }

    /**
     * 验证 {@link RandomUtil#getRandom(boolean)} 在强随机模式下返回 SecureRandom.
     */
    @Test
    @DisplayName("getRandom(true) 强随机")
    void getRandomSecureMode() {
        assertTrue(RandomUtil.getRandom(true) instanceof SecureRandom);
    }

    /**
     * 验证 {@link RandomUtil#randomLong(long, long)} 返回值落在 [min, max) 区间.
     */
    @Test
    @DisplayName("randomLong 范围")
    void randomLongRange() {
        for (int i = 0; i < 50; i++) {
            final long v = RandomUtil.randomLong(10L, 20L);
            assertTrue(v >= 10L && v < 20L);
        }
    }

    /**
     * 验证 {@link RandomUtil#randomDouble()} 返回 [0, 1) 区间内 double.
     */
    @Test
    @DisplayName("randomDouble 在 [0,1) 区间")
    void randomDoubleRange() {
        for (int i = 0; i < 50; i++) {
            final double v = RandomUtil.randomDouble();
            assertTrue(v >= 0.0 && v < 1.0);
        }
    }
}
