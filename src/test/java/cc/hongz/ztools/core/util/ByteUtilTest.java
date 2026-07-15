package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ByteUtil} 单元测试.
 * <p>
 * 覆盖 int/long/float/short 与字节数组在大端、小端序下的互转及与 {@link ByteBuffer} 的一致性.
 *
 * @author hongzhe.qin
 */
@DisplayName("ByteUtil 字节序互转")
class ByteUtilTest {

    /**
     * 验证 {@link ByteUtil#intToBytes(int, ByteOrder)} 与 {@link ByteUtil#bytesToInt(byte[], ByteOrder)}
     * 在小端序下与 {@link ByteBuffer} 结果一致.
     */
    @Test
    @DisplayName("int ↔ bytes：小端序往返")
    void intAndBytesLittleEndian() {
        final int int1 = RandomUtil.randomInt(Integer.MAX_VALUE);

        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(int1);
        final byte[] bytesFromBuffer = buffer.array();

        final byte[] bytesInt = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
        assertArrayEquals(bytesFromBuffer, bytesInt);
        assertEquals(int1, ByteUtil.bytesToInt(bytesInt, ByteOrder.LITTLE_ENDIAN));
        assertEquals(int1, ByteUtil.bytesToInt(ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN), ByteOrder.LITTLE_ENDIAN));
    }

    /**
     * 验证 int 与大端序字节数组互转.
     */
    @Test
    @DisplayName("int ↔ bytes：大端序往返")
    void intAndBytesBigEndian() {
        final int value = RandomUtil.randomInt(Integer.MAX_VALUE);

        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(value);
        final byte[] bytesFromBuffer = buffer.array();

        final byte[] bytesInt = ByteUtil.intToBytes(value, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(bytesFromBuffer, bytesInt);
        assertEquals(value, ByteUtil.bytesToInt(bytesInt, ByteOrder.BIG_ENDIAN));
    }

    /**
     * 验证 long 与小端序字节数组互转，含默认端序重载.
     */
    @Test
    @DisplayName("long ↔ bytes：小端序往返")
    void longAndBytesLittleEndian() {
        final long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(long1);
        assertArrayEquals(buffer.array(), ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN));
        assertEquals(long1, ByteUtil.bytesToLong(ByteUtil.longToBytes(long1), ByteOrder.LITTLE_ENDIAN));
        assertEquals(long1, ByteUtil.bytesToLong(ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN)));
    }

    /**
     * 验证 long 与大端序字节数组互转.
     */
    @Test
    @DisplayName("long ↔ bytes：大端序往返")
    void longAndBytesBigEndian() {
        final long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(long1);
        assertArrayEquals(buffer.array(), ByteUtil.longToBytes(long1, ByteOrder.BIG_ENDIAN));
        assertEquals(long1, ByteUtil.bytesToLong(ByteUtil.longToBytes(long1, ByteOrder.BIG_ENDIAN), ByteOrder.BIG_ENDIAN));
    }

    /**
     * 验证 float 与小端序字节数组互转.
     */
    @Test
    @DisplayName("float ↔ bytes：小端序往返")
    void floatAndBytesLittleEndian() {
        final float f1 = (float) RandomUtil.randomDouble();
        final byte[] bytes = ByteUtil.floatToBytes(f1, ByteOrder.LITTLE_ENDIAN);
        assertEquals(f1, ByteUtil.bytesToFloat(bytes, ByteOrder.LITTLE_ENDIAN), 0);
    }

    /**
     * 验证 float 与大端序字节数组互转.
     */
    @Test
    @DisplayName("float ↔ bytes：大端序往返")
    void floatAndBytesBigEndian() {
        final float f1 = (float) RandomUtil.randomDouble();
        final byte[] bytes = ByteUtil.floatToBytes(f1, ByteOrder.BIG_ENDIAN);
        assertEquals(f1, ByteUtil.bytesToFloat(bytes, ByteOrder.BIG_ENDIAN), 0);
    }

    /**
     * 验证 short 与小端序字节数组互转，含默认端序重载.
     */
    @Test
    @DisplayName("short ↔ bytes：小端序往返")
    void shortAndBytesLittleEndian() {
        final short short1 = (short) RandomUtil.randomInt();
        assertEquals(short1, ByteUtil.bytesToShort(ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN), ByteOrder.LITTLE_ENDIAN));
        assertEquals(short1, ByteUtil.bytesToShort(ByteUtil.shortToBytes(short1), ByteOrder.LITTLE_ENDIAN));
        assertEquals(short1, ByteUtil.bytesToShort(ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN)));
    }

    /**
     * 验证 short 与大端序字节数组互转.
     */
    @Test
    @DisplayName("short ↔ bytes：大端序往返")
    void shortAndBytesBigEndian() {
        final short short1 = 122;
        assertEquals(short1, ByteUtil.bytesToShort(ByteUtil.shortToBytes(short1, ByteOrder.BIG_ENDIAN), ByteOrder.BIG_ENDIAN));
    }

    /**
     * 验证 {@link ByteUtil#longToBytes(long)} 产出字节可由 {@link ByteBuffer} 按端序正确解析.
     */
    @Test
    @DisplayName("longToBytes 与 ByteBuffer 解析一致")
    void bytesToLongViaBuffer() {
        final long a = RandomUtil.randomLong(0, Long.MAX_VALUE);

        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(a, wrap.getLong());

        wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        assertEquals(a, wrap.getLong());
    }

    /**
     * 验证 {@link ByteUtil#intToBytes(int)} 产出字节可由 {@link ByteBuffer} 按端序正确解析.
     */
    @Test
    @DisplayName("intToBytes 与 ByteBuffer 解析一致")
    void bytesToIntViaBuffer() {
        final int a = RandomUtil.randomInt(0, Integer.MAX_VALUE);

        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(a, wrap.getInt());

        wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        assertEquals(a, wrap.getInt());
    }

    /**
     * 验证 {@link ByteUtil#shortToBytes(short)} 产出字节可由 {@link ByteBuffer} 按端序正确解析.
     */
    @Test
    @DisplayName("shortToBytes 与 ByteBuffer 解析一致")
    void bytesToShortViaBuffer() {
        final short a = (short) RandomUtil.randomInt(0, Short.MAX_VALUE);

        ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a));
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(a, wrap.getShort());

        wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a, ByteOrder.BIG_ENDIAN));
        wrap.order(ByteOrder.BIG_ENDIAN);
        assertEquals(a, wrap.getShort());
    }

    // ==================================================================== intToByte / byteToUnsignedInt

    /**
     * intToByte 截断高位字节.
     */
    @Test
    @DisplayName("intToByte 截断高位")
    void intToByteTruncates() {
        assertEquals((byte) 0, ByteUtil.intToByte(256));
        assertEquals((byte) 1, ByteUtil.intToByte(1));
        assertEquals((byte) -1, ByteUtil.intToByte(255));
        assertEquals((byte) 0x7F, ByteUtil.intToByte(0x7F));
    }

    /**
     * byteToUnsignedInt 将负数 byte 正确转为无符号 int.
     */
    @Test
    @DisplayName("byteToUnsignedInt 无符号转换")
    void byteToUnsignedIntConverts() {
        assertEquals(0, ByteUtil.byteToUnsignedInt((byte) 0));
        assertEquals(127, ByteUtil.byteToUnsignedInt((byte) 127));
        assertEquals(128, ByteUtil.byteToUnsignedInt((byte) -128));
        assertEquals(255, ByteUtil.byteToUnsignedInt((byte) -1));
    }

    // ==================================================================== double ↔ bytes

    /**
     * double 与小端序字节数组互转.
     */
    @Test
    @DisplayName("double ↔ bytes：小端序往返")
    void doubleAndBytesLittleEndian() {
        final double value = 3.141592653589793;
        final byte[] bytes = ByteUtil.doubleToBytes(value, ByteOrder.LITTLE_ENDIAN);
        assertEquals(8, bytes.length);
        assertEquals(value, ByteUtil.bytesToDouble(bytes, ByteOrder.LITTLE_ENDIAN), 0);
    }

    /**
     * double 与大端序字节数组互转.
     */
    @Test
    @DisplayName("double ↔ bytes：大端序往返")
    void doubleAndBytesBigEndian() {
        final double value = -273.15;
        final byte[] bytes = ByteUtil.doubleToBytes(value, ByteOrder.BIG_ENDIAN);
        assertEquals(value, ByteUtil.bytesToDouble(bytes, ByteOrder.BIG_ENDIAN), 0);
    }

    /**
     * double 默认端序往返.
     */
    @Test
    @DisplayName("double ↔ bytes：默认端序往返")
    void doubleAndBytesDefault() {
        final double value = 1.0;
        assertEquals(value, ByteUtil.bytesToDouble(ByteUtil.doubleToBytes(value)), 0);
    }

    // ==================================================================== 带 start offset 的方法

    /**
     * bytesToInt 带偏移量.
     */
    @Test
    @DisplayName("bytesToInt 带 start 偏移")
    void bytesToIntWithOffset() {
        // 构造 8 字节数组，高 4 字节存放 int 值
        final int value = 0x12345678;
        final byte[] bytes = new byte[8];
        final byte[] intBytes = ByteUtil.intToBytes(value, ByteOrder.BIG_ENDIAN);
        System.arraycopy(intBytes, 0, bytes, 4, 4);
        assertEquals(value, ByteUtil.bytesToInt(bytes, 4, ByteOrder.BIG_ENDIAN));
    }

    /**
     * bytesToLong 带偏移量.
     */
    @Test
    @DisplayName("bytesToLong 带 start 偏移")
    void bytesToLongWithOffset() {
        final long value = 0x0102030405060708L;
        final byte[] padded = new byte[16];
        final byte[] longBytes = ByteUtil.longToBytes(value, ByteOrder.BIG_ENDIAN);
        System.arraycopy(longBytes, 0, padded, 8, 8);
        assertEquals(value, ByteUtil.bytesToLong(padded, 8, ByteOrder.BIG_ENDIAN));
    }

    /**
     * bytesToShort 带偏移量.
     */
    @Test
    @DisplayName("bytesToShort 带 start 偏移")
    void bytesToShortWithOffset() {
        final short value = (short) 0x1234;
        final byte[] padded = new byte[4];
        final byte[] shortBytes = ByteUtil.shortToBytes(value, ByteOrder.BIG_ENDIAN);
        System.arraycopy(shortBytes, 0, padded, 2, 2);
        assertEquals(value, ByteUtil.bytesToShort(padded, 2, ByteOrder.BIG_ENDIAN));
    }

    // ==================================================================== numberToBytes

    /**
     * numberToBytes 支持所有 Number 子类型.
     */
    @Test
    @DisplayName("numberToBytes 各 Number 类型")
    void numberToBytesCoversAllTypes() {
        // Byte
        assertArrayEquals(new byte[]{42}, ByteUtil.numberToBytes((byte) 42));
        // Short
        assertEquals(2, ByteUtil.numberToBytes((short) 256).length);
        // Integer
        assertEquals(4, ByteUtil.numberToBytes(12345).length);
        // Long
        assertEquals(8, ByteUtil.numberToBytes(123456789L).length);
        // Float
        assertEquals(4, ByteUtil.numberToBytes(1.5f).length);
        // Double
        assertEquals(8, ByteUtil.numberToBytes(1.5).length);
        // BigDecimal → 走 double 分支
        assertEquals(8, ByteUtil.numberToBytes(new BigDecimal("3.14")).length);
    }

    /**
     * numberToBytes 往返验证.
     */
    @Test
    @DisplayName("numberToBytes 往返一致性")
    void numberToBytesRoundTrip() {
        // Integer
        final int intVal = 98765;
        assertEquals(intVal, ByteUtil.bytesToInt(ByteUtil.numberToBytes(intVal)));
        // Long
        final long longVal = 9876543210L;
        assertEquals(longVal, ByteUtil.bytesToLong(ByteUtil.numberToBytes(longVal)));
        // Double
        final double dblVal = 2.71828;
        assertEquals(dblVal, ByteUtil.bytesToDouble(ByteUtil.numberToBytes(dblVal)), 0);
    }

    // ==================================================================== bytesToNumber

    /**
     * bytesToNumber 转为 Byte.
     */
    @Test
    @DisplayName("bytesToNumber → Byte")
    void bytesToNumberByte() {
        final byte[] bytes = {42};
        assertEquals(Byte.valueOf((byte) 42), ByteUtil.bytesToNumber(bytes, Byte.class, ByteOrder.LITTLE_ENDIAN));
    }

    /**
     * bytesToNumber 转为 Short.
     */
    @Test
    @DisplayName("bytesToNumber → Short")
    void bytesToNumberShort() {
        final byte[] bytes = ByteUtil.shortToBytes((short) 1234);
        assertEquals(Short.valueOf((short) 1234), ByteUtil.bytesToNumber(bytes, Short.class, ByteUtil.DEFAULT_ORDER));
    }

    /**
     * bytesToNumber 转为 Integer.
     */
    @Test
    @DisplayName("bytesToNumber → Integer")
    void bytesToNumberInteger() {
        final byte[] bytes = ByteUtil.intToBytes(56789);
        assertEquals(Integer.valueOf(56789), ByteUtil.bytesToNumber(bytes, Integer.class, ByteUtil.DEFAULT_ORDER));
    }

    /**
     * bytesToNumber 转为 Long.
     */
    @Test
    @DisplayName("bytesToNumber → Long")
    void bytesToNumberLong() {
        final byte[] bytes = ByteUtil.longToBytes(9876543210L);
        assertEquals(Long.valueOf(9876543210L), ByteUtil.bytesToNumber(bytes, Long.class, ByteUtil.DEFAULT_ORDER));
    }

    /**
     * bytesToNumber 转为 Float.
     */
    @Test
    @DisplayName("bytesToNumber → Float")
    void bytesToNumberFloat() {
        final byte[] bytes = ByteUtil.floatToBytes(3.14f);
        assertEquals(3.14f, ByteUtil.bytesToNumber(bytes, Float.class, ByteUtil.DEFAULT_ORDER), 0.001f);
    }

    /**
     * bytesToNumber 转为 Double.
     */
    @Test
    @DisplayName("bytesToNumber → Double")
    void bytesToNumberDouble() {
        final byte[] bytes = ByteUtil.doubleToBytes(2.71828);
        assertEquals(2.71828, ByteUtil.bytesToNumber(bytes, Double.class, ByteUtil.DEFAULT_ORDER), 0.00001);
    }

    /**
     * bytesToNumber 转为 AtomicInteger.
     */
    @Test
    @DisplayName("bytesToNumber → AtomicInteger")
    void bytesToNumberAtomicInteger() {
        final byte[] bytes = ByteUtil.intToBytes(42);
        final AtomicInteger ai = ByteUtil.bytesToNumber(bytes, AtomicInteger.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(42, ai.get());
    }

    /**
     * bytesToNumber 转为 AtomicLong.
     */
    @Test
    @DisplayName("bytesToNumber → AtomicLong")
    void bytesToNumberAtomicLong() {
        final byte[] bytes = ByteUtil.longToBytes(123456789L);
        final AtomicLong al = ByteUtil.bytesToNumber(bytes, AtomicLong.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(123456789L, al.get());
    }

    /**
     * bytesToNumber 转为 LongAdder.
     */
    @Test
    @DisplayName("bytesToNumber → LongAdder")
    void bytesToNumberLongAdder() {
        final byte[] bytes = ByteUtil.longToBytes(999L);
        final LongAdder la = ByteUtil.bytesToNumber(bytes, LongAdder.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(999L, la.sum());
    }

    /**
     * bytesToNumber 转为 DoubleAdder.
     */
    @Test
    @DisplayName("bytesToNumber → DoubleAdder")
    void bytesToNumberDoubleAdder() {
        final byte[] bytes = ByteUtil.doubleToBytes(1.5);
        final DoubleAdder da = ByteUtil.bytesToNumber(bytes, DoubleAdder.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(1.5, da.sum(), 0);
    }

    /**
     * bytesToNumber 转为 BigDecimal.
     */
    @Test
    @DisplayName("bytesToNumber → BigDecimal")
    void bytesToNumberBigDecimal() {
        final byte[] bytes = ByteUtil.doubleToBytes(3.14);
        final BigDecimal bd = ByteUtil.bytesToNumber(bytes, BigDecimal.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(3.14, bd.doubleValue(), 0.001);
    }

    /**
     * bytesToNumber 转为 BigInteger.
     */
    @Test
    @DisplayName("bytesToNumber → BigInteger")
    void bytesToNumberBigInteger() {
        final byte[] bytes = ByteUtil.longToBytes(12345L);
        final BigInteger bi = ByteUtil.bytesToNumber(bytes, BigInteger.class, ByteUtil.DEFAULT_ORDER);
        assertEquals(BigInteger.valueOf(12345L), bi);
    }

    /**
     * bytesToNumber 目标类型为 Number.class 时返回 Double.
     */
    @Test
    @DisplayName("bytesToNumber → Number（默认 Double）")
    void bytesToNumberGenericNumber() {
        final byte[] bytes = ByteUtil.doubleToBytes(6.28);
        final Number n = ByteUtil.bytesToNumber(bytes, Number.class, ByteUtil.DEFAULT_ORDER);
        assertTrue(n instanceof Double);
        assertEquals(6.28, n.doubleValue(), 0);
    }

    /**
     * bytesToNumber 不支持的类型（自定义 Number 子类）抛出 IllegalArgumentException.
     * <p>
     * 注：不能用非 Number 类型测试，因为方法签名约束了 T extends Number，
     * 编译器会直接报错。这里使用一个自定义 Number 子类来触发运行时异常.
     */
    @Test
    @DisplayName("bytesToNumber 不支持的类型抛异常")
    void bytesToNumberUnsupportedThrows() {
        final byte[] bytes = {0, 0, 0, 0};
        // 自定义 Number 子类，不在 bytesToNumber 支持列表中
        class CustomNumber extends Number {
            @Override public int intValue() { return 0; }
            @Override public long longValue() { return 0; }
            @Override public float floatValue() { return 0; }
            @Override public double doubleValue() { return 0; }
        }
        assertThrows(IllegalArgumentException.class,
                () -> ByteUtil.bytesToNumber(bytes, CustomNumber.class, ByteUtil.DEFAULT_ORDER));
    }

    // ==================================================================== 常量

    /**
     * 默认字节序为小端.
     */
    @Test
    @DisplayName("DEFAULT_ORDER 为 LITTLE_ENDIAN")
    void defaultOrderIsLittleEndian() {
        assertEquals(ByteOrder.LITTLE_ENDIAN, ByteUtil.DEFAULT_ORDER);
    }

    /**
     * CPU_ENDIAN 非 null.
     */
    @Test
    @DisplayName("CPU_ENDIAN 已初始化")
    void cpuEndianInitialized() {
        assertTrue(ByteUtil.CPU_ENDIAN == ByteOrder.LITTLE_ENDIAN
                || ByteUtil.CPU_ENDIAN == ByteOrder.BIG_ENDIAN);
    }

    // ==================================================================== numberToBytes 带 ByteOrder

    /**
     * numberToBytes 指定大端序.
     */
    @Test
    @DisplayName("numberToBytes 大端序")
    void numberToBytesBigEndian() {
        final int value = 0x01020304;
        final byte[] bytes = ByteUtil.numberToBytes(value, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, bytes);
    }

    /**
     * numberToBytes 默认端序.
     */
    @Test
    @DisplayName("numberToBytes 默认端序")
    void numberToBytesDefaultOrder() {
        final int value = 12345;
        final byte[] bytes = ByteUtil.numberToBytes(value);
        assertEquals(value, ByteUtil.bytesToInt(bytes, ByteUtil.DEFAULT_ORDER));
    }

    // ==================================================================== 边界值

    /**
     * 零值往返转换.
     */
    @Test
    @DisplayName("零值往返")
    void zeroRoundTrip() {
        assertArrayEquals(new byte[]{0, 0}, ByteUtil.shortToBytes((short) 0));
        assertArrayEquals(new byte[]{0, 0, 0, 0}, ByteUtil.intToBytes(0));
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}, ByteUtil.longToBytes(0L));
        assertEquals(0, ByteUtil.bytesToInt(ByteUtil.intToBytes(0)));
        assertEquals(0L, ByteUtil.bytesToLong(ByteUtil.longToBytes(0L)));
    }

    /**
     * 极值往返转换.
     */
    @Test
    @DisplayName("极值往返")
    void extremeValuesRoundTrip() {
        // Integer
        assertEquals(Integer.MAX_VALUE, ByteUtil.bytesToInt(ByteUtil.intToBytes(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, ByteUtil.bytesToInt(ByteUtil.intToBytes(Integer.MIN_VALUE)));
        // Long
        assertEquals(Long.MAX_VALUE, ByteUtil.bytesToLong(ByteUtil.longToBytes(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, ByteUtil.bytesToLong(ByteUtil.longToBytes(Long.MIN_VALUE)));
        // Short
        assertEquals(Short.MAX_VALUE, ByteUtil.bytesToShort(ByteUtil.shortToBytes(Short.MAX_VALUE)));
        assertEquals(Short.MIN_VALUE, ByteUtil.bytesToShort(ByteUtil.shortToBytes(Short.MIN_VALUE)));
    }

    /**
     * float 特殊值（NaN、正负无穷）.
     */
    @Test
    @DisplayName("float 特殊值往返")
    void floatSpecialValues() {
        assertEquals(Float.NaN, ByteUtil.bytesToFloat(ByteUtil.floatToBytes(Float.NaN)), 0);
        assertEquals(Float.POSITIVE_INFINITY, ByteUtil.bytesToFloat(ByteUtil.floatToBytes(Float.POSITIVE_INFINITY)), 0);
        assertEquals(Float.NEGATIVE_INFINITY, ByteUtil.bytesToFloat(ByteUtil.floatToBytes(Float.NEGATIVE_INFINITY)), 0);
    }

    /**
     * double 特殊值（NaN、正负无穷）.
     */
    @Test
    @DisplayName("double 特殊值往返")
    void doubleSpecialValues() {
        assertEquals(Double.NaN, ByteUtil.bytesToDouble(ByteUtil.doubleToBytes(Double.NaN)), 0);
        assertEquals(Double.POSITIVE_INFINITY, ByteUtil.bytesToDouble(ByteUtil.doubleToBytes(Double.POSITIVE_INFINITY)), 0);
        assertEquals(Double.NEGATIVE_INFINITY, ByteUtil.bytesToDouble(ByteUtil.doubleToBytes(Double.NEGATIVE_INFINITY)), 0);
    }
}
