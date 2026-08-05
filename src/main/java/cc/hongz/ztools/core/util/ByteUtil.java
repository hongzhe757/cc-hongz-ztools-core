package cc.hongz.ztools.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * 字节与基本类型互转工具类（Hutool 阶段 1 迁入）.
 * <p>
 * 支持大端/小端序；与 {@link HexUtil}、codec 包配套使用.
 *
 * @author looly, hanlp, FULaBUla
 * @since 5.6.3
 */
public final class ByteUtil {

    /**
     * 字节掩码，用于获取无符号值.
     */
    private static final int BYTE_MASK = 0xFF;

    /**
     * 单字节位移量.
     */
    private static final int ONE_BYTE_SHIFT = 8;

    /**
     * 双字节位移量.
     */
    private static final int TWO_BYTE_SHIFT = 16;

    /**
     * 三字节位移量.
     */
    private static final int THREE_BYTE_SHIFT = 24;

    /**
     * 私有构造，禁止实例化.
     */
    private ByteUtil() {
    }

    /**
     * 默认字节序（小端）.
     */
    public static final ByteOrder DEFAULT_ORDER = ByteOrder.LITTLE_ENDIAN;

    /**
     * 当前 CPU 字节序.
     */
    public static final ByteOrder CPU_ENDIAN = "little".equals(System.getProperty("sun.cpu.endian"))
            ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

    /**
     * int转byte.
     *
     * @param intValue int值
     * @return byte值
     */
    public static byte intToByte(final int intValue) {
        return (byte) intValue;
    }

    /**
     * byte转无符号int.
     *
     * @param byteValue byte值
     * @return 无符号int值
     * @since 3.2.0
     */
    public static int byteToUnsignedInt(final byte byteValue) {
        // Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return byteValue & BYTE_MASK;
    }

    /**
     * byte数组转short.<br>
     * 默认以小端序转换
     *
     * @param bytes byte数组
     * @return short值
     */
    public static short bytesToShort(final byte[] bytes) {
        return bytesToShort(bytes, DEFAULT_ORDER);
    }

    /**
     * byte数组转short.<br>
     * 自定义端序
     *
     * @param bytes     byte数组，长度必须为2
     * @param byteOrder 端序
     * @return short值
     */
    public static short bytesToShort(final byte[] bytes, final ByteOrder byteOrder) {
        return bytesToShort(bytes, 0, byteOrder);
    }

    /**
     * byte数组转short.<br>
     * 自定义端序
     *
     * @param bytes     byte数组，长度必须大于2
     * @param start     开始位置
     * @param byteOrder 端序
     * @return short值
     */
    public static short bytesToShort(final byte[] bytes, final int start, final ByteOrder byteOrder) {
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            //小端模式，数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中
            return (short) (bytes[start] & BYTE_MASK | (bytes[start + 1] & BYTE_MASK) << Byte.SIZE);
        } else {
            return (short) (bytes[start + 1] & BYTE_MASK | (bytes[start] & BYTE_MASK) << Byte.SIZE);
        }
    }

    /**
     * short转byte数组.<br>
     * 默认以小端序转换
     *
     * @param shortValue short值
     * @return byte数组
     */
    public static byte[] shortToBytes(final short shortValue) {
        return shortToBytes(shortValue, DEFAULT_ORDER);
    }

    /**
     * short转byte数组.<br>
     * 自定义端序
     *
     * @param shortValue short值
     * @param byteOrder  端序
     * @return byte数组
     */
    public static byte[] shortToBytes(final short shortValue, final ByteOrder byteOrder) {
        byte[] b = new byte[Short.BYTES];
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            b[0] = (byte) (shortValue & BYTE_MASK);
            b[1] = (byte) ((shortValue >> Byte.SIZE) & BYTE_MASK);
        } else {
            b[1] = (byte) (shortValue & BYTE_MASK);
            b[0] = (byte) ((shortValue >> Byte.SIZE) & BYTE_MASK);
        }
        return b;
    }

    /**
     * byte[]转int值.<br>
     * 默认以小端序转换
     *
     * @param bytes byte数组
     * @return int值
     */
    public static int bytesToInt(final byte[] bytes) {
        return bytesToInt(bytes, DEFAULT_ORDER);
    }

    /**
     * byte[]转int值.<br>
     * 自定义端序
     *
     * @param bytes     byte数组
     * @param byteOrder 端序
     * @return int值
     */
    public static int bytesToInt(final byte[] bytes, final ByteOrder byteOrder) {
        return bytesToInt(bytes, 0, byteOrder);
    }

    /**
     * byte[]转int值.<br>
     * 自定义端序
     *
     * @param bytes     byte数组
     * @param start     开始位置（包含）
     * @param byteOrder 端序
     * @return int值
     * @since 5.7.21
     */
    public static int bytesToInt(final byte[] bytes, final int start, final ByteOrder byteOrder) {
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            return bytes[start] & BYTE_MASK | //
                    (bytes[1 + start] & BYTE_MASK) << ONE_BYTE_SHIFT | //
                    (bytes[2 + start] & BYTE_MASK) << TWO_BYTE_SHIFT | //
                    (bytes[3 + start] & BYTE_MASK) << THREE_BYTE_SHIFT; //
        } else {
            return bytes[3 + start] & BYTE_MASK | //
                    (bytes[2 + start] & BYTE_MASK) << ONE_BYTE_SHIFT | //
                    (bytes[1 + start] & BYTE_MASK) << TWO_BYTE_SHIFT | //
                    (bytes[start] & BYTE_MASK) << THREE_BYTE_SHIFT; //
        }

    }

    /**
     * int转byte数组.<br>
     * 默认以小端序转换
     *
     * @param intValue int值
     * @return byte数组
     */
    public static byte[] intToBytes(final int intValue) {
        return intToBytes(intValue, DEFAULT_ORDER);
    }

    /**
     * int转byte数组.<br>
     * 自定义端序
     *
     * @param intValue  int值
     * @param byteOrder 端序
     * @return byte数组
     */
    public static byte[] intToBytes(final int intValue, final ByteOrder byteOrder) {

        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            return new byte[]{ //
                    (byte) (intValue & BYTE_MASK), //
                    (byte) ((intValue >> ONE_BYTE_SHIFT) & BYTE_MASK), //
                    (byte) ((intValue >> TWO_BYTE_SHIFT) & BYTE_MASK), //
                    (byte) ((intValue >> THREE_BYTE_SHIFT) & BYTE_MASK) //
            };

        } else {
            return new byte[]{ //
                    (byte) ((intValue >> THREE_BYTE_SHIFT) & BYTE_MASK), //
                    (byte) ((intValue >> TWO_BYTE_SHIFT) & BYTE_MASK), //
                    (byte) ((intValue >> ONE_BYTE_SHIFT) & BYTE_MASK), //
                    (byte) (intValue & BYTE_MASK) //
            };
        }

    }

    /**
     * long转byte数组.<br>
     * 默认以小端序转换<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param longValue long值
     * @return byte数组
     */
    public static byte[] longToBytes(final long longValue) {
        return longToBytes(longValue, DEFAULT_ORDER);
    }

    /**
     * long转byte数组.<br>
     * 自定义端序<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param longValue long值
     * @param byteOrder 端序
     * @return byte数组
     */
    public static byte[] longToBytes(final long longValue, final ByteOrder byteOrder) {
        byte[] result = new byte[Long.BYTES];
        long value = longValue;
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            for (int i = 0; i < result.length; i++) {
                result[i] = (byte) (value & BYTE_MASK);
                value >>= Byte.SIZE;
            }
        } else {
            for (int i = (result.length - 1); i >= 0; i--) {
                result[i] = (byte) (value & BYTE_MASK);
                value >>= Byte.SIZE;
            }
        }
        return result;
    }

    /**
     * byte数组转long.<br>
     * 默认以小端序转换<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param bytes byte数组
     * @return long值
     */
    public static long bytesToLong(final byte[] bytes) {
        return bytesToLong(bytes, DEFAULT_ORDER);
    }

    /**
     * byte数组转long.<br>
     * 自定义端序<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param bytes     byte数组
     * @param byteOrder 端序
     * @return long值
     */
    public static long bytesToLong(final byte[] bytes, final ByteOrder byteOrder) {
        return bytesToLong(bytes, 0, byteOrder);
    }

    /**
     * byte数组转long.<br>
     * 自定义端序<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param bytes     byte数组
     * @param start     计算数组开始位置
     * @param byteOrder 端序
     * @return long值
     * @since 5.7.21
     */
    public static long bytesToLong(final byte[] bytes, final int start, final ByteOrder byteOrder) {
        long values = 0;
        if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
            for (int i = (Long.BYTES - 1); i >= 0; i--) {
                values <<= Byte.SIZE;
                values |= (bytes[i + start] & BYTE_MASK);
            }
        } else {
            for (int i = 0; i < Long.BYTES; i++) {
                values <<= Byte.SIZE;
                values |= (bytes[i + start] & BYTE_MASK);
            }
        }

        return values;
    }

    /**
     * float转byte数组，默认以小端序转换.<br>
     *
     * @param floatValue float值
     * @return byte数组
     * @since 5.7.18
     */
    public static byte[] floatToBytes(final float floatValue) {
        return floatToBytes(floatValue, DEFAULT_ORDER);
    }

    /**
     * float转byte数组，自定义端序.<br>
     *
     * @param floatValue float值
     * @param byteOrder  端序
     * @return byte数组
     * @since 5.7.18
     */
    public static byte[] floatToBytes(final float floatValue, final ByteOrder byteOrder) {
        return intToBytes(Float.floatToIntBits(floatValue), byteOrder);
    }

    /**
     * byte数组转float.<br>
     * 默认以小端序转换<br>
     *
     * @param bytes byte数组
     * @return float值
     * @since 5.7.18
     */
    public static float bytesToFloat(final byte[] bytes) {
        return bytesToFloat(bytes, DEFAULT_ORDER);
    }

    /**
     * byte数组转float.<br>
     * 自定义端序<br>
     *
     * @param bytes     byte数组
     * @param byteOrder 端序
     * @return float值
     * @since 5.7.18
     */
    public static float bytesToFloat(final byte[] bytes, final ByteOrder byteOrder) {
        return Float.intBitsToFloat(bytesToInt(bytes, byteOrder));
    }

    /**
     * double转byte数组.<br>
     * 默认以小端序转换<br>
     *
     * @param doubleValue double值
     * @return byte数组
     */
    public static byte[] doubleToBytes(final double doubleValue) {
        return doubleToBytes(doubleValue, DEFAULT_ORDER);
    }

    /**
     * double转byte数组.<br>
     * 自定义端序<br>
     * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
     *
     * @param doubleValue double值
     * @param byteOrder   端序
     * @return byte数组
     */
    public static byte[] doubleToBytes(final double doubleValue, final ByteOrder byteOrder) {
        return longToBytes(Double.doubleToLongBits(doubleValue), byteOrder);
    }

    /**
     * byte数组转Double.<br>
     * 默认以小端序转换<br>
     *
     * @param bytes byte数组
     * @return long值
     */
    public static double bytesToDouble(final byte[] bytes) {
        return bytesToDouble(bytes, DEFAULT_ORDER);
    }

    /**
     * byte数组转double.<br>
     * 自定义端序<br>
     *
     * @param bytes     byte数组
     * @param byteOrder 端序
     * @return long值
     */
    public static double bytesToDouble(final byte[] bytes, final ByteOrder byteOrder) {
        return Double.longBitsToDouble(bytesToLong(bytes, byteOrder));
    }

    /**
     * 将{@link Number}转换为byte数组.
     *
     * @param number 数字
     * @return bytes
     */
    public static byte[] numberToBytes(final Number number) {
        return numberToBytes(number, DEFAULT_ORDER);
    }

    /**
     * 将{@link Number}转换为byte数组.
     *
     * @param number    数字
     * @param byteOrder 端序
     * @return bytes
     */
    public static byte[] numberToBytes(final Number number, final ByteOrder byteOrder) {
        if (number instanceof Byte) {
            return new byte[]{number.byteValue()};
        } else if (number instanceof Double) {
            return doubleToBytes((Double) number, byteOrder);
        } else if (number instanceof Long) {
            return longToBytes((Long) number, byteOrder);
        } else if (number instanceof Integer) {
            return intToBytes((Integer) number, byteOrder);
        } else if (number instanceof Short) {
            return shortToBytes((Short) number, byteOrder);
        } else if (number instanceof Float) {
            return floatToBytes((Float) number, byteOrder);
        } else {
            return doubleToBytes(number.doubleValue(), byteOrder);
        }
    }

    /**
     * byte数组转换为指定类型数字.
     *
     * @param <T>         数字类型
     * @param bytes       byte数组
     * @param targetClass 目标数字类型
     * @param byteOrder   端序
     * @return 转换后的数字
     * @throws IllegalArgumentException 不支持的数字类型，如用户自定义数字类型
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T bytesToNumber(final byte[] bytes, final Class<T> targetClass,
            final ByteOrder byteOrder) throws IllegalArgumentException {
        Number number;
        if (Byte.class == targetClass) {
            number = bytes[0];
        } else if (Short.class == targetClass) {
            number = bytesToShort(bytes, byteOrder);
        } else if (Integer.class == targetClass) {
            number = bytesToInt(bytes, byteOrder);
        } else if (AtomicInteger.class == targetClass) {
            number = new AtomicInteger(bytesToInt(bytes, byteOrder));
        } else if (Long.class == targetClass) {
            number = bytesToLong(bytes, byteOrder);
        } else if (AtomicLong.class == targetClass) {
            number = new AtomicLong(bytesToLong(bytes, byteOrder));
        } else if (LongAdder.class == targetClass) {
            final LongAdder longValue = new LongAdder();
            longValue.add(bytesToLong(bytes, byteOrder));
            number = longValue;
        } else if (Float.class == targetClass) {
            number = bytesToFloat(bytes, byteOrder);
        } else if (Double.class == targetClass) {
            number = bytesToDouble(bytes, byteOrder);
        } else if (DoubleAdder.class == targetClass) {
            final DoubleAdder doubleAdder = new DoubleAdder();
            doubleAdder.add(bytesToDouble(bytes, byteOrder));
            number = doubleAdder;
        } else if (BigDecimal.class == targetClass) {
            number = NumberUtil.toBigDecimal(bytesToDouble(bytes, byteOrder));
        } else if (BigInteger.class == targetClass) {
            number = BigInteger.valueOf(bytesToLong(bytes, byteOrder));
        } else if (Number.class == targetClass) {
            // 用户没有明确类型具体类型，默认Double
            number = bytesToDouble(bytes, byteOrder);
        } else {
            // 用户自定义类型不支持
            throw new IllegalArgumentException("Unsupported Number type: " + targetClass.getName());
        }

        return (T) number;
    }
}
