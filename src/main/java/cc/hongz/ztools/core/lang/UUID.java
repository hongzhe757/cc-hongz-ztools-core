package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 提供通用唯一识别码（universally unique identifier）（UUID）实现，UUID表示一个128位的值。<br>
 * 此类拷贝自java.util.UUID，用于生成不带-的UUID字符串
 *
 * <p>
 * 这些通用标识符具有不同的变体。此类的方法用于操作 Leach-Salz 变体，不过构造方法允许创建任何 UUID 变体（将在下面进行描述）。
 * <p>
 * 变体 2 (Leach-Salz) UUID 的布局如下： long 型数据的最高有效位由以下无符号字段组成：
 *
 * <pre>
 * 0xFFFFFFFF00000000 time_low
 * 0x00000000FFFF0000 time_mid
 * 0x000000000000F000 version
 * 0x0000000000000FFF time_hi
 * </pre>
 * <p>
 * long 型数据的最低有效位由以下无符号字段组成：
 *
 * <pre>
 * 0xC000000000000000 variant
 * 0x3FFF000000000000 clock_seq
 * 0x0000FFFFFFFFFFFF node
 * </pre>
 *
 * <p>
 * variant 字段包含一个表示 UUID 布局的值。以上描述的位布局仅在 UUID 的 variant 值为 2（表示 Leach-Salz 变体）时才有效。 *
 * <p>
 * version 字段保存描述此 UUID 类型的值。有 4 种不同的基本 UUID 类型：基于时间的 UUID、DCE 安全 UUID、基于名称的 UUID 和随机生成的 UUID。<br>
 * 这些类型的 version 值分别为 1、2、3 和 4。
 *
 * @author looly (原作者)
 * @since 4.1.11
 */
public final class UUID implements java.io.Serializable, Comparable<UUID> {
    private static final long serialVersionUID = -1185015143654744140L;

    // -------------------------------------------------------------------------------------------------
    // RFC 4122 UUID 位布局相关常量
    // -------------------------------------------------------------------------------------------------

    /**
     * UUID 二进制总字节数.
     */
    private static final int UUID_BYTES = 16;
    /**
     * 每字节的位数.
     */
    private static final int BITS_PER_BYTE = 8;
    /**
     * 高/低 64 位各占的字节数.
     */
    private static final int HALF_BYTES = 8;
    /**
     * 单字节掩码.
     */
    private static final int BYTE_MASK = 0xff;
    /**
     * version 字段在字节数组中的下标.
     */
    private static final int VERSION_BYTE = 6;
    /**
     * variant 字段在字节数组中的下标.
     */
    private static final int VARIANT_BYTE = 8;
    /**
     * 清空 version 字段的掩码.
     */
    private static final int VERSION_MASK = 0x0f;
    /**
     * 清空 variant 字段的掩码.
     */
    private static final int VARIANT_MASK = 0x3f;
    /**
     * version 4（随机生成）标识.
     */
    private static final int VERSION_4 = 0x40;
    /**
     * version 3（基于名称/MD5）标识.
     */
    private static final int VERSION_3 = 0x30;
    /**
     * IETF（Leach-Salz）变体标识.
     */
    private static final int VARIANT_IETF = 0x80;
    /**
     * UUID 字符串按连字符分隔的段数.
     */
    private static final int COMPONENT_COUNT = 5;
    /**
     * 中间各段（time_mid/time_hi/variant+sequence）的位数.
     */
    private static final int SEGMENT_BITS = 16;
    /**
     * 32 位（半个 long / time_low 段）的位数.
     */
    private static final int HALF_BITS = 32;
    /**
     * node 字段的位数.
     */
    private static final int NODE_BITS = 48;
    /**
     * long 型的位数.
     */
    private static final int LONG_BITS = 64;
    /**
     * 变体号在最低 64 位中的位移量.
     */
    private static final int VARIANT_SHIFT = 62;
    /**
     * 符号位（最高位）的位置.
     */
    private static final int SIGN_BIT = 63;
    /**
     * version 字段在最高 64 位中的位移量.
     */
    private static final int VERSION_SHIFT = 12;
    /**
     * timestamp 低位（time_hi）掩码.
     */
    private static final long TIMESTAMP_LOW_MASK = 0x0FFFL;
    /**
     * timestamp 中位（time_mid）掩码.
     */
    private static final long TIMESTAMP_MID_MASK = 0x0FFFFL;
    /**
     * clock_seq 掩码.
     */
    private static final long CLOCK_SEQ_MASK = 0x3FFF000000000000L;
    /**
     * node 掩码.
     */
    private static final long NODE_MASK = 0x0000FFFFFFFFFFFFL;
    /**
     * 简单模式（无连字符）UUID 字符串长度.
     */
    private static final int SIMPLE_LENGTH = 32;
    /**
     * 完整模式（含连字符）UUID 字符串长度.
     */
    private static final int FULL_LENGTH = 36;
    /**
     * time_low 段的十六进制位数.
     */
    private static final int TIME_LOW_DIGITS = 8;
    /**
     * node 段的十六进制位数.
     */
    private static final int NODE_DIGITS = 12;

    /**
     * {@link SecureRandom} 的单例。
     *
     * @author looly
     */
    private static class Holder {
        static final SecureRandom NUMBER_GENERATOR = RandomUtil.getSecureRandom();
    }

    /**
     * 此UUID的最高64有效位。
     */
    private final long mostSigBits;

    /**
     * 此UUID的最低64有效位。
     */
    private final long leastSigBits;

    /**
     * 私有构造。
     *
     * @param data 数据
     */
    private UUID(final byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == UUID_BYTES : "data must be 16 bytes in length";
        for (int i = 0; i < HALF_BYTES; i++) {
            msb = (msb << BITS_PER_BYTE) | (data[i] & BYTE_MASK);
        }
        for (int i = HALF_BYTES; i < UUID_BYTES; i++) {
            lsb = (lsb << BITS_PER_BYTE) | (data[i] & BYTE_MASK);
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    /**
     * 使用指定的数据构造新的 UUID。
     *
     * @param mostSigBits  用于 {@code UUID} 的最高有效 64 位
     * @param leastSigBits 用于 {@code UUID} 的最低有效 64 位
     */
    public UUID(final long mostSigBits, final long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    /**
     * 获取类型 4（伪随机生成的）UUID 的静态工厂。 使用加密的本地线程伪随机数生成器生成该 UUID。
     *
     * @return 随机生成的 {@code UUID}
     */
    public static UUID fastUUID() {
        return randomUUID(false);
    }

    /**
     * 获取类型 4（伪随机生成的）UUID 的静态工厂。 使用加密的强伪随机数生成器生成该 UUID。
     *
     * @return 随机生成的 {@code UUID}
     */
    public static UUID randomUUID() {
        return randomUUID(true);
    }

    /**
     * 获取类型 4（伪随机生成的）UUID 的静态工厂。 使用加密的强伪随机数生成器生成该 UUID。
     *
     * @param isSecure 是否使用{@link SecureRandom}如果是可以获得更安全的随机码，否则可以得到更好的性能
     * @return 随机生成的 {@code UUID}
     */
    public static UUID randomUUID(final boolean isSecure) {
        final Random ng = isSecure ? Holder.NUMBER_GENERATOR : RandomUtil.getRandom();

        final byte[] randomBytes = new byte[UUID_BYTES];
        ng.nextBytes(randomBytes);

        randomBytes[VERSION_BYTE] &= VERSION_MASK; /* clear version */
        randomBytes[VERSION_BYTE] |= VERSION_4; /* set to version 4 */
        randomBytes[VARIANT_BYTE] &= VARIANT_MASK; /* clear variant */
        randomBytes[VARIANT_BYTE] |= VARIANT_IETF; /* set to IETF variant */

        return new UUID(randomBytes);
    }

    /**
     * 根据指定的字节数组获取类型 3（基于名称的）UUID 的静态工厂。
     *
     * @param name 用于构造 UUID 的字节数组。
     * @return 根据指定数组生成的 {@code UUID}
     */
    public static UUID nameUUIDFromBytes(final byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest(name);
        md5Bytes[VERSION_BYTE] &= VERSION_MASK; /* clear version */
        md5Bytes[VERSION_BYTE] |= VERSION_3; /* set to version 3 */
        md5Bytes[VARIANT_BYTE] &= VARIANT_MASK; /* clear variant */
        md5Bytes[VARIANT_BYTE] |= VARIANT_IETF; /* set to IETF variant */
        return new UUID(md5Bytes);
    }

    /**
     * 根据 {@link #toString()} 方法中描述的字符串标准表示形式创建{@code UUID}。
     *
     * @param name 指定 {@code UUID} 字符串
     * @return 具有指定值的 {@code UUID}
     * @throws IllegalArgumentException 如果 name 与 {@link #toString} 中描述的字符串表示形式不符抛出此异常
     */
    public static UUID fromString(final String name) {
        String[] components = name.split("-");
        if (components.length != COMPONENT_COUNT) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < COMPONENT_COUNT; i++) {
            components[i] = "0x" + components[i];
        }

        long mostSigBits = Long.decode(components[0]);
        mostSigBits <<= SEGMENT_BITS;
        mostSigBits |= Long.decode(components[1]);
        mostSigBits <<= SEGMENT_BITS;
        mostSigBits |= Long.decode(components[2]);

        long leastSigBits = Long.decode(components[3]);
        leastSigBits <<= NODE_BITS;
        leastSigBits |= Long.decode(components[4]);

        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * 返回此 UUID 的 128 位值中的最低有效 64 位。
     *
     * @return 此 UUID 的 128 位值中的最低有效 64 位。
     */
    public long getLeastSignificantBits() {
        return leastSigBits;
    }

    /**
     * 返回此 UUID 的 128 位值中的最高有效 64 位。
     *
     * @return 此 UUID 的 128 位值中最高有效 64 位。
     */
    public long getMostSignificantBits() {
        return mostSigBits;
    }

    /**
     * 与此 {@code UUID} 相关联的版本号. 版本号描述此 {@code UUID} 是如何生成的。
     * <p>
     * 版本号具有以下含意:
     * <ul>
     * <li>1 基于时间的 UUID
     * <li>2 DCE 安全 UUID
     * <li>3 基于名称的 UUID
     * <li>4 随机生成的 UUID
     * </ul>
     *
     * @return 此 {@code UUID} 的版本号
     */
    public int version() {
        // Version is bits masked by 0x000000000000F000 in MS long
        return (int) ((mostSigBits >> VERSION_SHIFT) & VERSION_MASK);
    }

    /**
     * 与此 {@code UUID} 相关联的变体号。变体号描述 {@code UUID} 的布局。
     * <p>
     * 变体号具有以下含意：
     * <ul>
     * <li>0 为 NCS 向后兼容保留
     * <li>2 <a href="http://www.ietf.org/rfc/rfc4122.txt">IETF&nbsp;RFC&nbsp;4122</a>(Leach-Salz), 用于此类
     * <li>6 保留，微软向后兼容
     * <li>7 保留供以后定义使用
     * </ul>
     *
     * @return 此 {@code UUID} 相关联的变体号
     */
    public int variant() {
        // This field is composed of a varying number of bits.
        // 0 - - Reserved for NCS backward compatibility
        // 1 0 - The IETF aka Leach-Salz variant (used by this class)
        // 1 1 0 Reserved, Microsoft backward compatibility
        // 1 1 1 Reserved for future definition.
        return (int) ((leastSigBits >>> (LONG_BITS - (leastSigBits >>> VARIANT_SHIFT))) & (leastSigBits >> SIGN_BIT));
    }

    /**
     * 与此 UUID 相关联的时间戳值。
     *
     * <p>
     * 60 位的时间戳值根据此 {@code UUID} 的 time_low、time_mid 和 time_hi 字段构造。<br>
     * 所得到的时间戳以 100 毫微秒为单位，从 UTC（通用协调时间） 1582 年 10 月 15 日零时开始。
     *
     * <p>
     * 时间戳值仅在在基于时间的 UUID（其 version 类型为 1）中才有意义。<br>
     * 如果此 {@code UUID} 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
     *
     * @return 时间戳值
     * @throws UnsupportedOperationException 如果此 {@code UUID} 不是 version 为 1 的 UUID。
     */
    public long timestamp() throws UnsupportedOperationException {
        checkTimeBase();
        return (mostSigBits & TIMESTAMP_LOW_MASK) << NODE_BITS
                | ((mostSigBits >> SEGMENT_BITS) & TIMESTAMP_MID_MASK) << HALF_BITS
                | mostSigBits >>> HALF_BITS;
    }

    /**
     * 与此 UUID 相关联的时钟序列值。
     *
     * <p>
     * 14 位的时钟序列值根据此 UUID 的 clock_seq 字段构造。clock_seq 字段用于保证在基于时间的 UUID 中的时间唯一性。
     * <p>
     * {@code clockSequence} 值仅在基于时间的 UUID（其 version 类型为 1）中才有意义。
     * 如果此 UUID 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
     *
     * @return 此 {@code UUID} 的时钟序列
     * @throws UnsupportedOperationException 如果此 UUID 的 version 不为 1
     */
    public int clockSequence() throws UnsupportedOperationException {
        checkTimeBase();
        return (int) ((leastSigBits & CLOCK_SEQ_MASK) >>> NODE_BITS);
    }

    /**
     * 与此 UUID 相关的节点值。
     *
     * <p>
     * 48 位的节点值根据此 UUID 的 node 字段构造。此字段旨在用于保存机器的 IEEE 802 地址，该地址用于生成此 UUID 以保证空间唯一性。
     * <p>
     * 节点值仅在基于时间的 UUID（其 version 类型为 1）中才有意义。<br>
     * 如果此 UUID 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
     *
     * @return 此 {@code UUID} 的节点值
     * @throws UnsupportedOperationException 如果此 UUID 的 version 不为 1
     */
    public long node() throws UnsupportedOperationException {
        checkTimeBase();
        return leastSigBits & NODE_MASK;
    }

    // Object Inherited Methods

    /**
     * 返回此{@code UUID} 的字符串表现形式。
     *
     * <p>
     * UUID 的字符串表示形式由此 BNF 描述：
     *
     * <pre>
     * {@code
     * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               = [0-9a-fA-F]
     * }
     * </pre>
     *
     * @return 此{@code UUID} 的字符串表现形式
     * @see #toString(boolean)
     */
    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * 返回此{@code UUID} 的字符串表现形式。
     *
     * <p>
     * UUID 的字符串表示形式由此 BNF 描述：
     *
     * <pre>
     * {@code
     * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               = [0-9a-fA-F]
     * }
     * </pre>
     *
     * @param isSimple 是否简单模式，简单模式为不带'-'的UUID字符串
     * @return 此{@code UUID} 的字符串表现形式
     */
    public String toString(final boolean isSimple) {
        final StringBuilder builder = StrUtil.builder(isSimple ? SIMPLE_LENGTH : FULL_LENGTH);
        // time_low
        builder.append(digits(mostSigBits >> HALF_BITS, TIME_LOW_DIGITS));
        if (false == isSimple) {
            builder.append('-');
        }
        // time_mid
        builder.append(digits(mostSigBits >> SEGMENT_BITS, 4));
        if (false == isSimple) {
            builder.append('-');
        }
        // time_high_and_version
        builder.append(digits(mostSigBits, 4));
        if (false == isSimple) {
            builder.append('-');
        }
        // variant_and_sequence
        builder.append(digits(leastSigBits >> NODE_BITS, 4));
        if (false == isSimple) {
            builder.append('-');
        }
        // node
        builder.append(digits(leastSigBits, NODE_DIGITS));

        return builder.toString();
    }

    /**
     * 返回此 UUID 的哈希码。
     *
     * @return UUID 的哈希码值。
     */
    @Override
    public int hashCode() {
        final long hilo = mostSigBits ^ leastSigBits;
        return ((int) (hilo >> HALF_BITS)) ^ (int) hilo;
    }

    /**
     * 将此对象与指定对象比较。
     * <p>
     * 当且仅当参数不为 {@code null}、而是一个 UUID 对象、具有与此 UUID 相同的 varriant、包含相同的值（每一位均相同）时，结果才为 {@code true}。
     *
     * @param obj 要与之比较的对象
     * @return 如果对象相同，则返回 {@code true}；否则返回 {@code false}
     */
    @Override
    public boolean equals(final Object obj) {
        if ((null == obj) || (obj.getClass() != UUID.class)) {
            return false;
        }
        final UUID id = (UUID) obj;
        return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
    }

    // Comparison Operations

    /**
     * 将此 UUID 与指定的 UUID 比较。
     *
     * <p>
     * 如果两个 UUID 不同，且第一个 UUID 的最高有效字段大于第二个 UUID 的对应字段，则第一个 UUID 大于第二个 UUID。
     *
     * @param val 与此 UUID 比较的 UUID
     * @return 在此 UUID 小于、等于或大于 val 时，分别返回 -1、0 或 1。
     */
    @Override
    public int compareTo(final UUID val) {
        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        int compare = Long.compare(this.mostSigBits, val.mostSigBits);
        if (0 == compare) {
            compare = Long.compare(this.leastSigBits, val.leastSigBits);
        }
        return compare;
    }

    // ---------------------------------------------------------------------------------- Private method start

    /**
     * 返回指定数字对应的hex值。
     *
     * @param val    值
     * @param digits 位
     * @return 值
     */
    private static String digits(final long val, final int digits) {
        final long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    /**
     * 检查是否为time-based版本UUID。
     */
    private void checkTimeBase() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
    }
    // ---------------------------------------------------------------------------------- Private method end
}
