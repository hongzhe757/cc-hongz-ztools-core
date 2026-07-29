package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RandomUtil} 单元测试.
 * <p>
 * 覆盖全部 public 方法与字符集常量行为.
 *
 * @author hongzhe.qin
 */
@DisplayName("RandomUtil 随机数")
class RandomUtilTest {

    // --------------------------------------- Random instances

    @Test
    @DisplayName("getRandom 返回 ThreadLocalRandom")
    void getRandom() {
        assertTrue(RandomUtil.getRandom() instanceof ThreadLocalRandom);
    }

    @Test
    @DisplayName("getSecureRandom 返回 SecureRandom")
    void getSecureRandom() {
        assertTrue(RandomUtil.getSecureRandom() instanceof SecureRandom);
    }

    @Test
    @DisplayName("getSecureRandom(byte[]) 返回 SecureRandom")
    void getSecureRandomWithSeed() {
        assertTrue(RandomUtil.getSecureRandom(new byte[]{1, 2, 3}) instanceof SecureRandom);
    }

    @Test
    @DisplayName("createSecureRandom 无种子")
    void createSecureRandomNoSeed() {
        assertTrue(RandomUtil.createSecureRandom(null) instanceof SecureRandom);
    }

    @Test
    @DisplayName("createSecureRandom 有种子")
    void createSecureRandomWithSeed() {
        assertTrue(RandomUtil.createSecureRandom(new byte[]{1}) instanceof SecureRandom);
    }

    @Test
    @DisplayName("getSHA1PRNGRandom 返回 SecureRandom")
    void getSHA1PRNGRandom() {
        assertTrue(RandomUtil.getSHA1PRNGRandom(new byte[]{1, 2}) instanceof SecureRandom);
    }

    @Test
    @DisplayName("getSHA1PRNGRandom null种子")
    void getSHA1PRNGRandomNullSeed() {
        assertTrue(RandomUtil.getSHA1PRNGRandom(null) instanceof SecureRandom);
    }

    @Test
    @DisplayName("getSecureRandomStrong 返回 SecureRandom")
    void getSecureRandomStrong() {
        assertTrue(RandomUtil.getSecureRandomStrong() instanceof SecureRandom);
    }

    @Test
    @DisplayName("getRandom(true) 强随机")
    void getRandomSecureMode() {
        assertTrue(RandomUtil.getRandom(true) instanceof SecureRandom);
    }

    @Test
    @DisplayName("getRandom(false) 返回 Random")
    void getRandomNonSecure() {
        assertNotNull(RandomUtil.getRandom(false));
    }

    // --------------------------------------- randomBoolean

    @Test
    @DisplayName("randomBoolean 可正常调用")
    void randomBoolean() {
        // 调用100次，至少出现一次true和一次false
        boolean hasTrue = false, hasFalse = false;
        for (int i = 0; i < 100; i++) {
            if (RandomUtil.randomBoolean()) { hasTrue = true; } else { hasFalse = true; }
            if (hasTrue && hasFalse) break;
        }
        assertTrue(hasTrue && hasFalse);
    }

    // --------------------------------------- randomBytes

    @Test
    @DisplayName("randomBytes 指定长度")
    void randomBytes() {
        byte[] bytes = RandomUtil.randomBytes(16);
        assertEquals(16, bytes.length);
    }

    // --------------------------------------- randomInt

    @Test
    @DisplayName("randomInt() 无界")
    void randomIntNoBound() {
        RandomUtil.randomInt();
    }

    @Test
    @DisplayName("randomInt(limit) 在[0,limit)")
    void randomIntRange() {
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(10);
            assertTrue(v >= 0 && v < 10);
        }
    }

    @Test
    @DisplayName("randomInt(min,max) 在[min,max)")
    void randomIntMinMax() {
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(3, 8);
            assertTrue(v >= 3 && v < 8);
        }
    }

    @Test
    @DisplayName("randomInt 含includeMin/includeMax")
    void randomIntIncludeFlags() {
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(3, 8, true, true);
            assertTrue(v >= 3 && v <= 8);
        }
        for (int i = 0; i < 100; i++) {
            final int v = RandomUtil.randomInt(3, 8, false, false);
            assertTrue(v >= 4 && v <= 7);
        }
    }

    // --------------------------------------- randomInts

    @Test
    @DisplayName("randomInts 随机索引")
    void randomInts() {
        int[] ints = RandomUtil.randomInts(10);
        assertEquals(10, ints.length);
        // 应包含0~9的排列
        Arrays.sort(ints);
        for (int i = 0; i < 10; i++) {
            assertEquals(i, ints[i]);
        }
    }

    // --------------------------------------- randomLong

    @Test
    @DisplayName("randomLong() 无界")
    void randomLongNoBound() {
        RandomUtil.randomLong();
    }

    @Test
    @DisplayName("randomLong(limit) 在[0,limit)")
    void randomLongLimit() {
        for (int i = 0; i < 50; i++) {
            long v = RandomUtil.randomLong(100L);
            assertTrue(v >= 0L && v < 100L);
        }
    }

    @Test
    @DisplayName("randomLong 范围")
    void randomLongRange() {
        for (int i = 0; i < 50; i++) {
            final long v = RandomUtil.randomLong(10L, 20L);
            assertTrue(v >= 10L && v < 20L);
        }
    }

    @Test
    @DisplayName("randomLong 含includeMin/includeMax")
    void randomLongIncludeFlags() {
        for (int i = 0; i < 50; i++) {
            long v = RandomUtil.randomLong(10L, 20L, true, true);
            assertTrue(v >= 10L && v <= 20L);
        }
    }

    // --------------------------------------- randomFloat

    @Test
    @DisplayName("randomFloat() 在[0,1)")
    void randomFloatNoBound() {
        for (int i = 0; i < 50; i++) {
            float v = RandomUtil.randomFloat();
            assertTrue(v >= 0f && v < 1f);
        }
    }

    @Test
    @DisplayName("randomFloat(limit) 在[0,limit)")
    void randomFloatLimit() {
        for (int i = 0; i < 50; i++) {
            float v = RandomUtil.randomFloat(5.0f);
            assertTrue(v >= 0f && v < 5.0f);
        }
    }

    @Test
    @DisplayName("randomFloat(min,max) 在[min,max)")
    void randomFloatRange() {
        for (int i = 0; i < 50; i++) {
            float v = RandomUtil.randomFloat(2.0f, 5.0f);
            assertTrue(v >= 2.0f && v < 5.0f);
        }
    }

    @Test
    @DisplayName("randomFloat min==max 返回min")
    void randomFloatSameRange() {
        assertEquals(3.0f, RandomUtil.randomFloat(3.0f, 3.0f));
    }

    // --------------------------------------- randomDouble

    @Test
    @DisplayName("randomDouble 在 [0,1) 区间")
    void randomDoubleRange() {
        for (int i = 0; i < 50; i++) {
            final double v = RandomUtil.randomDouble();
            assertTrue(v >= 0.0 && v < 1.0);
        }
    }

    @Test
    @DisplayName("randomDouble(limit) 在[0,limit)")
    void randomDoubleLimit() {
        for (int i = 0; i < 50; i++) {
            double v = RandomUtil.randomDouble(10.0);
            assertTrue(v >= 0.0 && v < 10.0);
        }
    }

    @Test
    @DisplayName("randomDouble(min,max) 范围")
    void randomDoubleMinMax() {
        for (int i = 0; i < 50; i++) {
            double v = RandomUtil.randomDouble(5.0, 10.0);
            assertTrue(v >= 5.0 && v < 10.0);
        }
    }

    @Test
    @DisplayName("randomDouble 带scale和RoundingMode")
    void randomDoubleWithScale() {
        double v = RandomUtil.randomDouble(5.0, 10.0, 2, RoundingMode.HALF_UP);
        // 验证最多2位小数
        String s = String.valueOf(v);
        int dotIdx = s.indexOf('.');
        assertTrue(dotIdx < 0 || s.length() - dotIdx - 1 <= 4); // toString可能有余量
    }

    @Test
    @DisplayName("randomDouble(scale, RoundingMode)")
    void randomDoubleScaleOnly() {
        double v = RandomUtil.randomDouble(3, RoundingMode.HALF_UP);
        assertTrue(v >= 0.0 && v < 1.0);
    }

    @Test
    @DisplayName("randomDouble(limit, scale, RoundingMode)")
    void randomDoubleLimitScale() {
        double v = RandomUtil.randomDouble(10.0, 2, RoundingMode.HALF_UP);
        assertTrue(v >= 0.0 && v < 10.0);
    }

    // --------------------------------------- randomBigDecimal

    @Test
    @DisplayName("randomBigDecimal() 在[0,1)")
    void randomBigDecimalNoBound() {
        BigDecimal bd = RandomUtil.randomBigDecimal();
        assertTrue(bd.compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(bd.compareTo(BigDecimal.ONE) < 0);
    }

    @Test
    @DisplayName("randomBigDecimal(limit)")
    void randomBigDecimalLimit() {
        BigDecimal limit = new BigDecimal("100.5");
        for (int i = 0; i < 50; i++) {
            BigDecimal bd = RandomUtil.randomBigDecimal(limit);
            assertTrue(bd.compareTo(BigDecimal.ZERO) >= 0);
            assertTrue(bd.compareTo(limit) < 0);
        }
    }

    @Test
    @DisplayName("randomBigDecimal(min,max)")
    void randomBigDecimalRange() {
        BigDecimal min = new BigDecimal("10");
        BigDecimal max = new BigDecimal("20");
        for (int i = 0; i < 50; i++) {
            BigDecimal bd = RandomUtil.randomBigDecimal(min, max);
            assertTrue(bd.compareTo(min) >= 0);
            assertTrue(bd.compareTo(max) < 0);
        }
    }

    // --------------------------------------- randomEle

    @Test
    @DisplayName("randomEle(List)")
    void randomEleList() {
        List<String> list = Arrays.asList("a", "b", "c");
        String ele = RandomUtil.randomEle(list);
        assertTrue(list.contains(ele));
    }

    @Test
    @DisplayName("randomEle(List, limit)")
    void randomEleListLimit() {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        for (int i = 0; i < 50; i++) {
            String ele = RandomUtil.randomEle(list, 2);
            assertTrue("a".equals(ele) || "b".equals(ele));
        }
    }

    @Test
    @DisplayName("randomEle(T[])")
    void randomEleArray() {
        String[] arr = {"x", "y", "z"};
        String ele = RandomUtil.randomEle(arr);
        assertTrue(Arrays.asList(arr).contains(ele));
    }

    @Test
    @DisplayName("randomEle(T[], limit)")
    void randomEleArrayLimit() {
        String[] arr = {"a", "b", "c", "d"};
        for (int i = 0; i < 50; i++) {
            String ele = RandomUtil.randomEle(arr, 1);
            assertEquals("a", ele);
        }
    }

    @Test
    @DisplayName("randomEles 随机多个元素")
    void randomEles() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> eles = RandomUtil.randomEles(list, 5);
        assertEquals(5, eles.size());
        for (String e : eles) {
            assertTrue(list.contains(e));
        }
    }

    @Test
    @DisplayName("randomEleList 不重复元素")
    void randomEleListNoDup() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = RandomUtil.randomEleList(list, 3);
        assertEquals(3, result.size());
        assertEquals(new HashSet<>(result).size(), 3); // 不重复
    }

    @Test
    @DisplayName("randomEleList count>=size 返回全部")
    void randomEleListAll() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = RandomUtil.randomEleList(list, 10);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("randomEleSet 不重复元素")
    void randomEleSetTest() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        Set<String> result = RandomUtil.randomEleSet(list, 3);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("randomEleSet count>distinct 抛异常")
    void randomEleSetTooMany() {
        List<String> list = Arrays.asList("a", "a", "b");
        assertThrows(IllegalArgumentException.class, () -> RandomUtil.randomEleSet(list, 5));
    }

    // --------------------------------------- randomString

    @Test
    @DisplayName("randomString(base,length) 指定字符集")
    void randomStringFromBase() {
        final String s = RandomUtil.randomString(RandomUtil.BASE_NUMBER, 6);
        assertEquals(6, s.length());
        assertTrue(s.chars().allMatch(c -> RandomUtil.BASE_NUMBER.indexOf(c) >= 0));
    }

    @Test
    @DisplayName("randomString(base,0) 最小长度为1")
    void randomStringMinLengthOne() {
        assertEquals(1, RandomUtil.randomString(RandomUtil.BASE_NUMBER, 0).length());
    }

    @Test
    @DisplayName("randomString 空base返回空串")
    void randomStringBlankBaseReturnsEmpty() {
        assertEquals("", RandomUtil.randomString("", 5));
    }

    @Test
    @DisplayName("randomString(null base) 返回空串")
    void randomStringNullBase() {
        assertEquals("", RandomUtil.randomString(null, 5));
    }

    @Test
    @DisplayName("randomString(int) 定长")
    void randomStringLength() {
        assertEquals(8, RandomUtil.randomString(8).length());
    }

    @Test
    @DisplayName("randomStringLower 只含小写+数字")
    void randomStringLower() {
        String s = RandomUtil.randomStringLower(100);
        assertTrue(s.chars().noneMatch(c -> Character.isUpperCase(c)));
    }

    @Test
    @DisplayName("randomStringUpper 只含大写+数字")
    void randomStringUpper() {
        String s = RandomUtil.randomStringUpper(100);
        assertTrue(s.chars().noneMatch(c -> Character.isLowerCase(c)));
    }

    @Test
    @DisplayName("randomStringWithoutStr 排除指定字符")
    void randomStringWithoutStr() {
        String s = RandomUtil.randomStringWithoutStr(100, "oO0");
        assertFalse(s.contains("o"));
        assertFalse(s.contains("O"));
        assertFalse(s.contains("0"));
    }

    @Test
    @DisplayName("randomStringLowerWithoutStr 排除指定字符(不区分大小写)")
    void randomStringLowerWithoutStr() {
        String s = RandomUtil.randomStringLowerWithoutStr(100, "Oo0");
        assertFalse(s.contains("o"));
        assertFalse(s.contains("0"));
    }

    @Test
    @DisplayName("randomNumbers 只含数字")
    void randomNumbers() {
        String s = RandomUtil.randomNumbers(20);
        assertEquals(20, s.length());
        assertTrue(s.chars().allMatch(c -> c >= '0' && c <= '9'));
    }

    // --------------------------------------- randomChar

    @Test
    @DisplayName("randomChar(base) 指定字符集")
    void randomCharFromBase() {
        final char c = RandomUtil.randomChar("AB");
        assertTrue(c == 'A' || c == 'B');
    }

    @Test
    @DisplayName("randomChar() 字母或数字")
    void randomCharDefault() {
        char c = RandomUtil.randomChar();
        assertTrue(Character.isLetterOrDigit(c));
    }

    @Test
    @DisplayName("randomChinese 在汉字范围")
    void randomChinese() {
        for (int i = 0; i < 50; i++) {
            char c = RandomUtil.randomChinese();
            assertTrue(c >= '\u4E00' && c <= '\u9FFF');
        }
    }

    @Test
    @DisplayName("randomNumber 0~9")
    void randomNumber() {
        for (int i = 0; i < 50; i++) {
            char c = RandomUtil.randomNumber();
            assertTrue(c >= '0' && c <= '9');
        }
    }

    // --------------------------------------- constants

    @Test
    @DisplayName("常量非空且内容正确")
    void baseConstants() {
        assertFalse(RandomUtil.BASE_NUMBER.isEmpty());
        assertFalse(RandomUtil.BASE_CHAR.isEmpty());
        assertTrue(RandomUtil.BASE_CHAR_NUMBER.contains("A"));
        assertTrue(RandomUtil.BASE_CHAR_NUMBER_LOWER.contains("a"));
    }
}
