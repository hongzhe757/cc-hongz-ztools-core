package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
