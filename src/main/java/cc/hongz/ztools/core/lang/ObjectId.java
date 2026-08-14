package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.DateUtil;
import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.RuntimeUtil;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB ID生成策略实现.
 * <p>
 * ObjectId由以下几部分组成：
 * <pre>
 * 1. Time 时间戳。
 * 2. Machine 所在主机的唯一标识符，一般是机器主机名的散列值。
 * 3. PID 进程ID。确保同一机器中不冲突
 * 4. INC 自增计数器。确保同一秒内产生objectId的唯一性。
 * </pre>
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.ObjectId}。
 *
 * @author looly
 */
public class ObjectId {

    /** 线程安全的下一个随机数,每次生成自增+1 */
    private static final AtomicInteger NEXT_INC = new AtomicInteger(RandomUtil.randomInt());
    /** 机器信息 */
    private static final int MACHINE = getMachinePiece() | getProcessPiece();
    /** ObjectId 十六进制字符串长度. */
    private static final int OBJECT_ID_LENGTH = 24;
    /** ObjectId 字节数组长度. */
    private static final int OBJECT_ID_BYTES = 12;
    /** ObjectId 带连字符的字符串长度. */
    private static final int OBJECT_ID_LENGTH_WITH_HYPHEN = 26;
    /** 单字节掩码. */
    private static final int BYTE_MASK = 0xff;
    /** 十六进制基数. */
    private static final int HEX_RADIX = 16;
    /** 机器码片段左移位数. */
    private static final int MACHINE_PIECE_SHIFT = 16;
    /** 进程码片段掩码. */
    private static final int PROCESS_PIECE_MASK = 0xFFFF;

    /**
     * 给定的字符串是否为有效的ObjectId.
     *
     * @param s 字符串
     * @return 是否为有效的ObjectId
     */
    public static boolean isValid(final String s) {
        if (s == null) {
            return false;
        }
        final String cleaned = s.replace("-", "");
        final int len = cleaned.length();
        if (len != OBJECT_ID_LENGTH) {
            return false;
        }

        char c;
        for (int i = 0; i < len; i++) {
            c = cleaned.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 获取一个objectId的bytes表现形式.
     *
     * @return objectId
     */
    public static byte[] nextBytes() {
        final ByteBuffer bb = ByteBuffer.wrap(new byte[OBJECT_ID_BYTES]);
        bb.putInt((int) DateUtil.currentSeconds());// 4位
        bb.putInt(MACHINE);// 4位
        bb.putInt(NEXT_INC.getAndIncrement());// 4位

        return bb.array();
    }

    /**
     * 获取一个objectId用下划线分割.
     *
     * @return objectId
     */
    public static String next() {
        return next(false);
    }

    /**
     * 获取一个objectId.
     *
     * @param withHyphen 是否包含分隔符
     * @return objectId
     */
    public static String next(final boolean withHyphen) {
        final byte[] array = nextBytes();
        final StringBuilder buf = new StringBuilder(withHyphen ? OBJECT_ID_LENGTH_WITH_HYPHEN : OBJECT_ID_LENGTH);
        int t;
        for (int i = 0; i < array.length; i++) {
            if (withHyphen && i % 4 == 0 && i != 0) {
                buf.append("-");
            }
            t = array[i] & BYTE_MASK;
            if (t < HEX_RADIX) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(t));
        }
        return buf.toString();
    }

    /**
     * 获取机器码片段.
     *
     * @return 机器码片段
     */
    private static int getMachinePiece() {
        int machinePiece;
        try {
            final StringBuilder netSb = new StringBuilder();
            final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                final NetworkInterface ni = e.nextElement();
                netSb.append(ni.toString());
            }
            machinePiece = netSb.toString().hashCode() << MACHINE_PIECE_SHIFT;
        } catch (final Throwable e) {
            machinePiece = (RandomUtil.randomInt()) << MACHINE_PIECE_SHIFT;
        }
        return machinePiece;
    }

    /**
     * 获取进程码片段.
     *
     * @return 进程码片段
     */
    private static int getProcessPiece() {
        final int processPiece;
        int processId;
        try {
            processId = RuntimeUtil.getPid();
        } catch (final Throwable t) {
            processId = RandomUtil.randomInt();
        }

        final ClassLoader loader = ClassLoaderUtil.getClassLoader();
        final int loaderId = (loader != null) ? System.identityHashCode(loader) : 0;

        final String processSb = Integer.toHexString(processId) + Integer.toHexString(loaderId);
        processPiece = processSb.hashCode() & PROCESS_PIECE_MASK;

        return processPiece;
    }
}
