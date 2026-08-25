package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.ObjectId;
import cc.hongz.ztools.core.lang.Singleton;
import cc.hongz.ztools.core.lang.Snowflake;
import cc.hongz.ztools.core.lang.UUID;
import cc.hongz.ztools.core.lang.id.NanoId;
import cc.hongz.ztools.core.net.NetUtil;

/**
 * ID生成器工具类.
 * <p>
 * 此工具类中主要封装：
 * <pre>
 * 1. 唯一性ID生成器：UUID、ObjectId（MongoDB）、Snowflake、NanoId
 * </pre>
 * <p>
 * 迁移自 hutool {@code cc.hongz.ztools.core.util.IdUtil}。
 *
 * @author looly
 */
public final class IdUtil {

    /**
     * 低位字节掩码.
     */
    private static final int MASK_BYTE = 0x000000FF;
    /**
     * 高位字节掩码.
     */
    private static final int MASK_HIGH_BYTE = 0x0000FF00;
    /**
     * 16位掩码.
     */
    private static final int MASK_16BIT = 0xffff;
    /**
     * 字节左移位数.
     */
    private static final int BYTE_SHIFT = 8;
    /**
     * MAC地址右移位数.
     */
    private static final int MAC_SHIFT = 6;

    private IdUtil() {
    }

    // ------------------------------------------------------------------- UUID

    /**
     * 获取随机UUID.
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线.
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID.
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID.
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    // ------------------------------------------------------------------- ObjectId

    /**
     * 创建MongoDB ID生成策略实现.
     *
     * @return ObjectId
     */
    public static String objectId() {
        return ObjectId.next();
    }

    // ------------------------------------------------------------------- Snowflake

    /**
     * 创建Twitter的Snowflake 算法生成器.
     *
     * @param workerId     终端ID
     * @param datacenterId 数据中心ID
     * @return {@link Snowflake}
     * @deprecated 请使用 {@link #getSnowflake(long, long)}
     */
    @Deprecated
    public static Snowflake createSnowflake(final long workerId, final long datacenterId) {
        return new Snowflake(workerId, datacenterId);
    }

    /**
     * 获取单例的Twitter的Snowflake 算法生成器对象.
     *
     * @param workerId     终端ID
     * @param datacenterId 数据中心ID
     * @return {@link Snowflake}
     */
    public static Snowflake getSnowflake(final long workerId, final long datacenterId) {
        return Singleton.get(Snowflake.class, workerId, datacenterId);
    }

    /**
     * 获取单例的Twitter的Snowflake 算法生成器对象.
     *
     * @param workerId 终端ID
     * @return {@link Snowflake}
     */
    public static Snowflake getSnowflake(final long workerId) {
        return Singleton.get(Snowflake.class, workerId);
    }

    /**
     * 获取单例的Twitter的Snowflake 算法生成器对象.
     *
     * @return {@link Snowflake}
     */
    public static Snowflake getSnowflake() {
        return Singleton.get(Snowflake.class);
    }

    /**
     * 获取数据中心ID.
     * <p>
     * 数据中心ID依赖于本地网卡MAC地址。
     * 此算法来自于mybatis-plus#Sequence
     *
     * @param maxDatacenterId 最大的中心ID
     * @return 数据中心ID
     */
    public static long getDataCenterId(final long maxDatacenterId) {
        Assert.isTrue(maxDatacenterId > 0, "maxDatacenterId must be > 0");
        long maxDcId = maxDatacenterId;
        if (maxDcId == Long.MAX_VALUE) {
            maxDcId -= 1;
        }
        long id = 1L;
        byte[] mac = null;
        try {
            mac = NetUtil.getLocalHardwareAddress();
        } catch (final UtilException ignore) {
            // ignore
        }
        if (null != mac) {
            id = ((MASK_BYTE & (long) mac[mac.length - 2])
                    | (MASK_HIGH_BYTE & (((long) mac[mac.length - 1]) << BYTE_SHIFT))) >> MAC_SHIFT;
            id = id % (maxDcId + 1);
        }
        return id;
    }

    /**
     * 获取机器ID.
     * <p>
     * 机器依赖于本进程ID或进程名的Hash值。
     * 此算法来自于mybatis-plus#Sequence
     *
     * @param datacenterId 数据中心ID
     * @param maxWorkerId  最大的机器节点ID
     * @return ID
     */
    public static long getWorkerId(final long datacenterId, final long maxWorkerId) {
        final StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        try {
            mpid.append(RuntimeUtil.getPid());
        } catch (final UtilException ignore) {
            // ignore
        }
        return (mpid.toString().hashCode() & MASK_16BIT) % (maxWorkerId + 1);
    }

    // ------------------------------------------------------------------- NanoId

    /**
     * 获取随机NanoId.
     *
     * @return 随机NanoId
     */
    public static String nanoId() {
        return NanoId.randomNanoId();
    }

    /**
     * 获取随机NanoId.
     *
     * @param size ID中的字符数量
     * @return 随机NanoId
     */
    public static String nanoId(final int size) {
        return NanoId.randomNanoId(size);
    }

    // ------------------------------------------------------------------- Snowflake quick access

    /**
     * 简单获取Snowflake 的 nextId.
     *
     * @return nextId
     */
    public static long getSnowflakeNextId() {
        return getSnowflake().nextId();
    }

    /**
     * 简单获取Snowflake 的 nextId.
     *
     * @return nextIdStr
     */
    public static String getSnowflakeNextIdStr() {
        return getSnowflake().nextIdStr();
    }
}
