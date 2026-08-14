package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.date.SystemClock;
import cc.hongz.ztools.core.lang.id.IdConstants;
import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Twitter的Snowflake 算法.
 * <p>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，
 * 并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):
 * <pre>
 * 符号位（1bit）- 时间戳相对值（41bit）- 数据中心标志（5bit）- 机器标志（5bit）- 递增序号（12bit）
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Snowflake}。
 *
 * @author Looly
 */
public class Snowflake implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 默认起始时间戳的值，为Thu, 04 Nov 2010 01:42:54 GMT.
     */
    private static final long DEFAULT_TWEPOCH_VALUE = 1288834974657L;
    /**
     * 默认回拨时间（毫秒）的值，2S.
     */
    private static final long DEFAULT_TIME_OFFSET_VALUE = 2000L;

    /**
     * 默认的起始时间，为Thu, 04 Nov 2010 01:42:54 GMT
     */
    public static long DEFAULT_TWEPOCH = DEFAULT_TWEPOCH_VALUE;
    /**
     * 默认回拨时间，2S
     */
    public static long DEFAULT_TIME_OFFSET = DEFAULT_TIME_OFFSET_VALUE;

    private static final long WORKER_ID_BITS = 5L;
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    public static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    private static final long DATA_CENTER_ID_BITS = 5L;
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    public static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;
    private static final long TIMESTAMP_BITS = 41L;
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 初始化时间点 */
    private final long twepoch;
    private final long workerId;
    private final long dataCenterId;
    private final boolean useSystemClock;
    /** 允许的时钟回拨毫秒数 */
    private final long timeOffset;
    /**
     * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数。
     * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。
     */
    private final long randomSequenceLimit;

    /** 自增序号 */
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * 构造，使用自动生成的工作节点ID和数据中心ID.
     */
    public Snowflake() {
        this(IdConstants.DEFAULT_WORKER_ID);
    }

    /**
     * 构造.
     *
     * @param workerId 终端ID
     */
    public Snowflake(final long workerId) {
        this(workerId, IdConstants.DEFAULT_DATACENTER_ID);
    }

    /**
     * 构造.
     *
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     */
    public Snowflake(final long workerId, final long dataCenterId) {
        this(workerId, dataCenterId, false);
    }

    /**
     * 构造.
     *
     * @param workerId         终端ID
     * @param dataCenterId     数据中心ID
     * @param isUseSystemClock 是否使用 {@link SystemClock} 获取当前时间戳
     */
    public Snowflake(final long workerId, final long dataCenterId, final boolean isUseSystemClock) {
        this(null, workerId, dataCenterId, isUseSystemClock);
    }

    /**
     * 构造.
     *
     * @param epochDate        初始化时间起点（null表示默认起始日期）
     * @param workerId         工作机器节点id
     * @param dataCenterId     数据中心id
     * @param isUseSystemClock 是否使用 {@link SystemClock} 获取当前时间戳
     */
    public Snowflake(final Date epochDate, final long workerId, final long dataCenterId, final boolean isUseSystemClock) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, DEFAULT_TIME_OFFSET);
    }

    /**
     * 构造.
     *
     * @param epochDate        初始化时间起点（null表示默认起始日期）
     * @param workerId         工作机器节点id
     * @param dataCenterId     数据中心id
     * @param isUseSystemClock 是否使用 {@link SystemClock} 获取当前时间戳
     * @param timeOffset       允许时间回拨的毫秒数
     */
    public Snowflake(final Date epochDate, final long workerId, final long dataCenterId,
                     final boolean isUseSystemClock, final long timeOffset) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, timeOffset, 0);
    }

    /**
     * 构造.
     *
     * @param epochDate           初始化时间起点（null表示默认起始日期）
     * @param workerId            工作机器节点id
     * @param dataCenterId        数据中心id
     * @param isUseSystemClock    是否使用 {@link SystemClock} 获取当前时间戳
     * @param timeOffset          允许时间回拨的毫秒数
     * @param randomSequenceLimit 限定一个随机上限，0表示无随机
     */
    public Snowflake(final Date epochDate, final long workerId, final long dataCenterId,
                     final boolean isUseSystemClock, final long timeOffset, final long randomSequenceLimit) {
        this.twepoch = (null != epochDate) ? epochDate.getTime() : DEFAULT_TWEPOCH;
        this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKER_ID);
        this.dataCenterId = Assert.checkBetween(dataCenterId, 0, MAX_DATA_CENTER_ID);
        this.useSystemClock = isUseSystemClock;
        this.timeOffset = timeOffset;
        this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK);
    }

    /**
     * 根据Snowflake的ID，获取机器id.
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(final long id) {
        return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id.
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(final long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取生成时间.
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(final long id) {
        return (id >> TIMESTAMP_LEFT_SHIFT & ~(-1L << TIMESTAMP_BITS)) + twepoch;
    }

    /**
     * 根据传入时间戳-计算ID起终点.
     *
     * @param timestampStart 开始时间戳
     * @param timestampEnd   结束时间戳
     * @return key-ID起点，Value-ID终点
     */
    public Pair<Long, Long> getIdScopeByTimestamp(final long timestampStart, final long timestampEnd) {
        return getIdScopeByTimestamp(timestampStart, timestampEnd, true);
    }

    /**
     * 根据传入时间戳-计算ID起终点.
     *
     * @param timestampStart        开始时间戳
     * @param timestampEnd          结束时间戳
     * @param ignoreCenterAndWorker 是否忽略数据中心和机器节点的占位
     * @return key-ID起点，Value-ID终点
     */
    public Pair<Long, Long> getIdScopeByTimestamp(final long timestampStart, final long timestampEnd,
                                                   final boolean ignoreCenterAndWorker) {
        final long startTimeMinId = (timestampStart - twepoch) << TIMESTAMP_LEFT_SHIFT;
        final long endTimeMinId = (timestampEnd - twepoch) << TIMESTAMP_LEFT_SHIFT;
        if (ignoreCenterAndWorker) {
            final long endId = endTimeMinId | ~(-1 << TIMESTAMP_LEFT_SHIFT);
            return Pair.of(startTimeMinId, endId);
        } else {
            final long startId = startTimeMinId | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT);
            final long endId = endTimeMinId | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | SEQUENCE_MASK;
            return Pair.of(startId, endId);
        }
    }

    /**
     * 下一个ID.
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = genTime();
        if (timestamp < this.lastTimestamp) {
            if (this.lastTimestamp - timestamp < timeOffset) {
                timestamp = lastTimestamp;
            } else {
                throw new IllegalStateException(
                        StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
            }
        }

        if (timestamp == this.lastTimestamp) {
            final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
            this.sequence = sequence;
        } else {
            if (randomSequenceLimit > 1) {
                sequence = RandomUtil.randomLong(randomSequenceLimit);
            } else {
                sequence = 0L;
            }
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 下一个ID（字符串形式）.
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }

    /**
     * 循环等待下一个时间.
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = genTime();
        while (timestamp == lastTimestamp) {
            timestamp = genTime();
        }
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException(
                    StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
        }
        return timestamp;
    }

    /**
     * 生成时间戳.
     *
     * @return 时间戳
     */
    private long genTime() {
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }
}
