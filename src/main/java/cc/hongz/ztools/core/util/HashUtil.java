package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.hash.CityHash;
import cc.hongz.ztools.core.lang.hash.MetroHash;
import cc.hongz.ztools.core.lang.hash.MurmurHash;
import cc.hongz.ztools.core.lang.hash.Number128;

/**
 * Hash算法大全.
 * 推荐使用FNV1算法
 *
 * @author Goodzzp, Looly
 */
public final class HashUtil {

    // ==================== 通用常量 ====================

    /**
     * 确保hash值为正数的掩码.
     */
    private static final int HASH_POSITIVE_MASK = 0x7FFFFFFF;

    // ==================== rotatingHash 常量 ====================

    /**
     * 旋转hash左移位数.
     */
    private static final int ROTATING_SHIFT_LEFT = 4;
    /**
     * 旋转hash右移位数.
     */
    private static final int ROTATING_SHIFT_RIGHT = 28;

    // ==================== oneByOneHash 常量 ====================

    /**
     * 一次一个hash左移位数10.
     */
    private static final int ONE_BY_ONE_SHIFT_10 = 10;
    /**
     * 一次一个hash右移位数6.
     */
    private static final int ONE_BY_ONE_SHIFT_6 = 6;
    /**
     * 一次一个hash左移位数3.
     */
    private static final int ONE_BY_ONE_SHIFT_3 = 3;
    /**
     * 一次一个hash右移位数11.
     */
    private static final int ONE_BY_ONE_SHIFT_11 = 11;
    /**
     * 一次一个hash左移位数15.
     */
    private static final int ONE_BY_ONE_SHIFT_15 = 15;

    // ==================== bernstein 常量 ====================

    /**
     * Bernstein算法乘数.
     */
    private static final int BERNSTEIN_MULTIPLIER = 33;

    // ==================== universal 常量 ====================

    /**
     * 每字节位数.
     */
    private static final int BITS_PER_BYTE = 8;
    /**
     * Universal hash字节索引位移量.
     */
    private static final int UNIVERSAL_BYTE_SHIFT = 3;

    // ==================== fnvHash 常量 ====================

    /**
     * FNV-1 32位质数.
     */
    private static final int FNV_32_PRIME = 16777619;
    /**
     * FNV-1 32位初始偏移量.
     */
    private static final long FNV_32_OFFSET_BASIS = 2166136261L;
    /**
     * FNV左移位数13.
     */
    private static final int FNV_SHIFT_13 = 13;
    /**
     * FNV右移位数7.
     */
    private static final int FNV_SHIFT_7 = 7;
    /**
     * FNV左移位数3.
     */
    private static final int FNV_SHIFT_3 = 3;
    /**
     * FNV右移位数17.
     */
    private static final int FNV_SHIFT_17 = 17;
    /**
     * FNV左移位数5.
     */
    private static final int FNV_SHIFT_5 = 5;

    // ==================== intHash 常量 ====================

    /**
     * 整数hash左移位数15.
     */
    private static final int INT_HASH_SHIFT_15 = 15;
    /**
     * 整数hash右移位数10.
     */
    private static final int INT_HASH_SHIFT_10 = 10;
    /**
     * 整数hash左移位数3.
     */
    private static final int INT_HASH_SHIFT_3 = 3;
    /**
     * 整数hash右移位数6.
     */
    private static final int INT_HASH_SHIFT_6 = 6;
    /**
     * 整数hash左移位数11.
     */
    private static final int INT_HASH_SHIFT_11 = 11;
    /**
     * 整数hash右移位数16.
     */
    private static final int INT_HASH_SHIFT_16 = 16;

    // ==================== rsHash 常量 ====================

    /**
     * RS算法系数b.
     */
    private static final int RS_B_CONSTANT = 378551;
    /**
     * RS算法系数a.
     */
    private static final int RS_A_CONSTANT = 63689;

    // ==================== jsHash 常量 ====================

    /**
     * JS算法初始hash值.
     */
    private static final int JS_HASH_INIT = 1315423911;
    /**
     * JS算法左移位数.
     */
    private static final int JS_SHIFT_LEFT = 5;
    /**
     * JS算法右移位数.
     */
    private static final int JS_SHIFT_RIGHT = 2;

    // ==================== pjwHash 常量 ====================

    /**
     * 无符号整型的位数.
     */
    private static final int PJW_BITS_IN_INT = 32;
    /**
     * 四分之三计算分子.
     */
    private static final int PJW_THREE_QUARTERS_NUM = 3;
    /**
     * 四分之三计算分母.
     */
    private static final int PJW_DENOMINATOR_4 = 4;
    /**
     * 八分之一计算分母.
     */
    private static final int PJW_ONE_EIGHTH_DENOM = 8;
    /**
     * PJW算法高位掩码.
     */
    private static final int PJW_HIGH_BITS_MASK = -1;

    // ==================== elfHash 常量 ====================

    /**
     * ELF算法左移位数.
     */
    private static final int ELF_SHIFT_LEFT = 4;
    /**
     * ELF算法高位半字节掩码.
     */
    private static final long ELF_HIGH_NIBBLE_MASK = 0xF0000000L;
    /**
     * ELF算法高位右移位数.
     */
    private static final int ELF_HIGH_BITS_SHIFT = 24;

    // ==================== bkdrHash 常量 ====================

    /**
     * BKDR算法种子值.
     */
    private static final int BKDR_SEED = 131;

    // ==================== sdbmHash 常量 ====================

    /**
     * SDBM算法左移位数6.
     */
    private static final int SDBM_SHIFT_6 = 6;
    /**
     * SDBM算法左移位数16.
     */
    private static final int SDBM_SHIFT_16 = 16;

    // ==================== djbHash 常量 ====================

    /**
     * DJB算法初始hash值.
     */
    private static final int DJB_INIT = 5381;
    /**
     * DJB算法左移位数.
     */
    private static final int DJB_SHIFT_LEFT = 5;

    // ==================== dekHash 常量 ====================

    /**
     * DEK算法左移位数.
     */
    private static final int DEK_SHIFT_LEFT = 5;
    /**
     * DEK算法右移位数.
     */
    private static final int DEK_SHIFT_RIGHT = 27;

    // ==================== apHash 常量 ====================

    /**
     * AP算法最低有效位判断掩码.
     */
    private static final int AP_PARITY_CHECK = 1;
    /**
     * AP算法偶数索引左移位数.
     */
    private static final int AP_SHIFT_EVEN_LEFT = 7;
    /**
     * AP算法偶数索引右移位数.
     */
    private static final int AP_SHIFT_EVEN_RIGHT = 3;
    /**
     * AP算法奇数索引左移位数.
     */
    private static final int AP_SHIFT_ODD_LEFT = 11;
    /**
     * AP算法奇数索引右移位数.
     */
    private static final int AP_SHIFT_ODD_RIGHT = 5;

    // ==================== tianlHash 常量 ====================

    /**
     * TianL算法短字符串长度阈值.
     */
    private static final int TIANL_THRESHOLD = 256;
    /**
     * TianL算法基础乘数.
     */
    private static final long TIANL_BASE_MULTIPLIER = 16777216L;
    /**
     * TianL算法长字符串初始值.
     */
    private static final long TIANL_LARGE_INIT = 4278190080L;
    /**
     * TianL算法采样字符数阈值.
     */
    private static final int TIANL_CHAR_THRESHOLD = 96;
    /**
     * 大写字母与小写字母ASCII差值.
     */
    private static final int CASE_DIFF = 32;
    /**
     * TianL算法多项式系数3.
     */
    private static final long TIANL_COEFF_3 = 3L;
    /**
     * TianL算法多项式系数5.
     */
    private static final long TIANL_COEFF_5 = 5L;
    /**
     * TianL算法多项式系数7.
     */
    private static final long TIANL_COEFF_7 = 7L;
    /**
     * TianL算法多项式系数11.
     */
    private static final long TIANL_COEFF_11 = 11L;

    // ==================== javaDefaultHash 常量 ====================

    /**
     * Java String hashCode乘数.
     */
    private static final int JAVA_HASH_MULTIPLIER = 31;

    // ==================== mixHash 常量 ====================

    /**
     * 混合hash左移位数.
     */
    private static final int MIX_HASH_SHIFT = 32;

    // ==================== hfHash 常量 ====================

    /**
     * HF算法乘数.
     */
    private static final int HF_HASH_MULTIPLIER = 3;

    // ==================== hfIpHash 常量 ====================

    /**
     * HFIP算法取模值.
     */
    private static final int HF_IP_HASH_MOD = 4;

    // ==================== zobrist 常量 ====================

    /**
     * 位掩码 bit 3.
     */
    private static final int ZOBRIST_BIT_3 = 0x08;
    /**
     * 位掩码 bit 4.
     */
    private static final int ZOBRIST_BIT_4 = 0x10;
    /**
     * 位掩码 bit 5.
     */
    private static final int ZOBRIST_BIT_5 = 0x20;

    private HashUtil() {
    }

    /**
     * 加法hash.
     *
     * @param key   字符串
     * @param prime 一个质数
     * @return hash结果
     */
    public static int additiveHash(final String key, final int prime) {
        int hash, i; for (hash = key.length(), i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
        } return hash % prime;
    }

    /**
     * 旋转hash.
     *
     * @param key   输入字符串
     * @param prime 质数
     * @return hash值
     */
    public static int rotatingHash(final String key, final int prime) {
        int hash, i; for (hash = key.length(), i = 0; i < key.length(); ++i) {
            hash = (hash << ROTATING_SHIFT_LEFT) ^ (hash >> ROTATING_SHIFT_RIGHT) ^ key.charAt(i);
        } return hash % prime;
    }

    /**
     * 一次一个hash.
     *
     * @param key 输入字符串
     * @return 输出hash值
     */
    public static int oneByOneHash(final String key) {
        int hash, i; for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i); hash += (hash << ONE_BY_ONE_SHIFT_10); hash ^= (hash >> ONE_BY_ONE_SHIFT_6);
        } hash += (hash << ONE_BY_ONE_SHIFT_3); hash ^= (hash >> ONE_BY_ONE_SHIFT_11);
        hash += (hash << ONE_BY_ONE_SHIFT_15); return hash;
    }

    /**
     * Bernstein's hash.
     *
     * @param key 输入字节数组
     * @return 结果hash
     */
    public static int bernstein(final String key) {
        int hash = 0; int i; for (i = 0; i < key.length(); ++i) {
            hash = BERNSTEIN_MULTIPLIER * hash + key.charAt(i);
        } return hash;
    }

    /**
     * Universal Hashing.
     *
     * @param key  字节数组
     * @param mask 掩码
     * @param tab  tab
     * @return hash值
     */
    public static int universal(final char[] key, final int mask, final int[] tab) {
        int hash = key.length, i, len = key.length; for (i = 0; i < (len << UNIVERSAL_BYTE_SHIFT); i += BITS_PER_BYTE) {
            char k = key[i >> UNIVERSAL_BYTE_SHIFT]; if ((k & 0x01) == 0) {
                hash ^= tab[i];
            } if ((k & 0x02) == 0) {
                hash ^= tab[i + 1];
            } if ((k & 0x04) == 0) {
                hash ^= tab[i + 2];
            } if ((k & ZOBRIST_BIT_3) == 0) {
                hash ^= tab[i + 3];
            } if ((k & ZOBRIST_BIT_4) == 0) {
                hash ^= tab[i + 4];
            } if ((k & ZOBRIST_BIT_5) == 0) {
                hash ^= tab[i + 5];
            } if ((k & 0x40) == 0) {
                hash ^= tab[i + 6];
            } if ((k & 0x80) == 0) {
                hash ^= tab[i + 7];
            }
        } return (hash & mask);
    }

    /**
     * Zobrist Hashing.
     *
     * @param key  字节数组
     * @param mask 掩码
     * @param tab  tab
     * @return hash值
     */
    public static int zobrist(final char[] key, final int mask, final int[][] tab) {
        int hash, i; for (hash = key.length, i = 0; i < key.length; ++i) {
            hash ^= tab[i][key[i]];
        } return (hash & mask);
    }

    /**
     * 改进的32位FNV算法1.
     *
     * @param data 数组
     * @return hash结果
     */
    public static int fnvHash(final byte[] data) {
        final int p = FNV_32_PRIME; int hash = (int) FNV_32_OFFSET_BASIS; for (byte b : data) {
            hash = (hash ^ b) * p;
        } hash += hash << FNV_SHIFT_13; hash ^= hash >> FNV_SHIFT_7; hash += hash << FNV_SHIFT_3;
        hash ^= hash >> FNV_SHIFT_17; hash += hash << FNV_SHIFT_5; return Math.abs(hash);
    }

    /**
     * 改进的32位FNV算法1.
     *
     * @param data 字符串
     * @return hash结果
     */
    public static int fnvHash(final String data) {
        final int p = FNV_32_PRIME; int hash = (int) FNV_32_OFFSET_BASIS; for (int i = 0; i < data.length(); i++) {
            hash = (hash ^ data.charAt(i)) * p;
        } hash += hash << FNV_SHIFT_13; hash ^= hash >> FNV_SHIFT_7; hash += hash << FNV_SHIFT_3;
        hash ^= hash >> FNV_SHIFT_17; hash += hash << FNV_SHIFT_5; return Math.abs(hash);
    }

    /**
     * Thomas Wang的算法，整数hash.
     *
     * @param key 整数
     * @return hash值
     */
    public static int intHash(final int key) {
        int hash = key; hash += ~(hash << INT_HASH_SHIFT_15); hash ^= (hash >>> INT_HASH_SHIFT_10);
        hash += (hash << INT_HASH_SHIFT_3); hash ^= (hash >>> INT_HASH_SHIFT_6); hash += ~(hash << INT_HASH_SHIFT_11);
        hash ^= (hash >>> INT_HASH_SHIFT_16); return hash;
    }

    /**
     * RS算法hash.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int rsHash(final String str) {
        int b = RS_B_CONSTANT; int a = RS_A_CONSTANT; int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i); a = a * b;
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * JS算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int jsHash(final String str) {
        int hash = JS_HASH_INIT;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << JS_SHIFT_LEFT) + str.charAt(i) + (hash >> JS_SHIFT_RIGHT));
        }

        return Math.abs(hash) & HASH_POSITIVE_MASK;
    }

    /**
     * PJW算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int pjwHash(final String str) {
        int bitsInUnsignedInt = PJW_BITS_IN_INT;
        int threeQuarters = (bitsInUnsignedInt * PJW_THREE_QUARTERS_NUM) / PJW_DENOMINATOR_4;
        int oneEighth = bitsInUnsignedInt / PJW_ONE_EIGHTH_DENOM;
        int highBits = PJW_HIGH_BITS_MASK << (bitsInUnsignedInt - oneEighth); int hash = 0; int test;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << oneEighth) + str.charAt(i);

            if ((test = hash & highBits) != 0) {
                hash = ((hash ^ (test >> threeQuarters)) & (~highBits));
            }
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * ELF算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int elfHash(final String str) {
        int hash = 0; int x;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << ELF_SHIFT_LEFT) + str.charAt(i); if ((x = (int) (hash & ELF_HIGH_NIBBLE_MASK)) != 0) {
                hash ^= (x >> ELF_HIGH_BITS_SHIFT); hash &= ~x;
            }
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * BKDR算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int bkdrHash(final String str) {
        int seed = BKDR_SEED; int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash * seed) + str.charAt(i);
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * SDBM算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int sdbmHash(final String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + (hash << SDBM_SHIFT_6) + (hash << SDBM_SHIFT_16) - hash;
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * DJB算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int djbHash(final String str) {
        int hash = DJB_INIT;

        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << DJB_SHIFT_LEFT) + hash) + str.charAt(i);
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * DEK算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int dekHash(final String str) {
        int hash = str.length();

        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << DEK_SHIFT_LEFT) ^ (hash >> DEK_SHIFT_RIGHT)) ^ str.charAt(i);
        }

        return hash & HASH_POSITIVE_MASK;
    }

    /**
     * AP算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int apHash(final String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((i & AP_PARITY_CHECK) == 0) ? ((hash << AP_SHIFT_EVEN_LEFT) ^ str.charAt(i) ^ (hash >> AP_SHIFT_EVEN_RIGHT)) : (~((hash << AP_SHIFT_ODD_LEFT) ^ str.charAt(i) ^ (hash >> AP_SHIFT_ODD_RIGHT)));
        }

        return hash;
    }

    /**
     * TianL Hash算法.
     *
     * @param str 字符串
     * @return Hash值
     */
    public static long tianlHash(final String str) {
        long hash;

        int iLength = str.length(); if (iLength == 0) {
            return 0;
        }

        if (iLength <= TIANL_THRESHOLD) {
            hash = TIANL_BASE_MULTIPLIER * (iLength - 1);
        } else {
            hash = TIANL_LARGE_INIT;
        }

        int i;

        char ucChar;

        if (iLength <= TIANL_CHAR_THRESHOLD) {
            for (i = 1; i <= iLength; i++) {
                ucChar = str.charAt(i - 1); if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (ucChar + CASE_DIFF);
                }
                hash += (TIANL_COEFF_3 * i * ucChar * ucChar + TIANL_COEFF_5 * i * ucChar + TIANL_COEFF_7 * i + TIANL_COEFF_11 * ucChar) % TIANL_BASE_MULTIPLIER;
            }
        } else {
            for (i = 1; i <= TIANL_CHAR_THRESHOLD; i++) {
                ucChar = str.charAt(i + iLength - TIANL_CHAR_THRESHOLD - 1); if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (ucChar + CASE_DIFF);
                }
                hash += (TIANL_COEFF_3 * i * ucChar * ucChar + TIANL_COEFF_5 * i * ucChar + TIANL_COEFF_7 * i + TIANL_COEFF_11 * ucChar) % TIANL_BASE_MULTIPLIER;
            }
        } if (hash < 0) {
            hash = -hash;
        } return hash;
    }

    /**
     * JAVA自己带的算法.
     *
     * @param str 字符串
     * @return hash值
     */
    public static int javaDefaultHash(final String str) {
        int h = 0; int off = 0; int len = str.length(); for (int i = 0; i < len; i++) {
            h = JAVA_HASH_MULTIPLIER * h + str.charAt(off++);
        } return h;
    }

    /**
     * 混合hash算法，输出64位的值.
     *
     * @param str 字符串
     * @return hash值
     */
    public static long mixHash(final String str) {
        long hash = str.hashCode(); hash <<= MIX_HASH_SHIFT; hash |= fnvHash(str); return hash;
    }

    /**
     * 根据对象的内存地址生成相应的Hash值.
     *
     * @param obj 对象
     * @return hash值
     * @since 4.2.2
     */
    public static int identityHashCode(final Object obj) {
        return System.identityHashCode(obj);
    }

    /**
     * MurmurHash算法32-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static int murmur32(final byte[] data) {
        return MurmurHash.hash32(data);
    }

    /**
     * MurmurHash算法64-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static long murmur64(final byte[] data) {
        return MurmurHash.hash64(data);
    }

    /**
     * MurmurHash算法128-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static long[] murmur128(final byte[] data) {
        return MurmurHash.hash128(data);
    }

    /**
     * CityHash算法32-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 5.2.5
     */
    public static int cityHash32(final byte[] data) {
        return CityHash.hash32(data);
    }

    /**
     * CityHash算法64-bit实现，种子1使用默认的CityHash#k2.
     *
     * @param data 数据
     * @param seed 种子2
     * @return hash值
     * @since 5.2.5
     */
    public static long cityHash64(final byte[] data, final long seed) {
        return CityHash.hash64(data, seed);
    }

    /**
     * CityHash算法64-bit实现，种子1使用默认的CityHash#k2.
     *
     * @param data  数据
     * @param seed0 种子1
     * @param seed1 种子2
     * @return hash值
     * @since 5.2.5
     */
    public static long cityHash64(final byte[] data, final long seed0, final long seed1) {
        return CityHash.hash64(data, seed0, seed1);
    }

    /**
     * CityHash算法64-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 5.2.5
     */
    public static long cityHash64(final byte[] data) {
        return CityHash.hash64(data);
    }

    /**
     * CityHash算法128-bit实现.
     *
     * @param data 数据
     * @return hash值
     * @since 5.2.5
     */
    public static long[] cityHash128(final byte[] data) {
        return CityHash.hash128(data).getLongArray();
    }

    /**
     * CityHash算法128-bit实现.
     *
     * @param data 数据
     * @param seed 种子
     * @return hash值，long[0]：低位，long[1]：高位
     * @since 5.2.5
     */
    public static long[] cityHash128(final byte[] data, final Number128 seed) {
        return CityHash.hash128(data, seed).getLongArray();
    }

    /**
     * MetroHash 算法64-bit实现.
     *
     * @param data 数据
     * @param seed 种子
     * @return hash值
     */
    public static long metroHash64(final byte[] data, final long seed) {
        return MetroHash.hash64(data, seed);
    }

    /**
     * MetroHash 算法64-bit实现.
     *
     * @param data 数据
     * @return hash值
     */
    public static long metroHash64(final byte[] data) {
        return MetroHash.hash64(data);
    }

    /**
     * MetroHash 算法128-bit实现.
     *
     * @param data 数据
     * @param seed 种子
     * @return hash值，long[0]：低位，long[1]：高位
     */
    public static long[] metroHash128(final byte[] data, final long seed) {
        return MetroHash.hash128(data, seed).getLongArray();
    }

    /**
     * MetroHash 算法128-bit实现.
     *
     * @param data 数据
     * @return hash值，long[0]：低位，long[1]：高位
     */
    public static long[] metroHash128(final byte[] data) {
        return MetroHash.hash128(data).getLongArray();
    }

    /**
     * HF Hash算法.
     *
     * @param data 字符串
     * @return hash结果
     * @since 5.8.0
     */
    public static long hfHash(final String data) {
        int length = data.length(); long hash = 0;

        for (int i = 0; i < length; i++) {
            hash += (long) data.charAt(i) * HF_HASH_MULTIPLIER * i;
        }

        if (hash < 0) {
            hash = -hash;
        }

        return hash;
    }

    /**
     * HFIP Hash算法.
     *
     * @param data 字符串
     * @return hash结果
     * @since 5.8.0
     */
    public static long hfIpHash(final String data) {
        int length = data.length(); long hash = 0; for (int i = 0; i < length; i++) {
            hash += data.charAt(i % HF_IP_HASH_MOD) ^ data.charAt(i);
        } return hash;
    }
}
