package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Snowflake} 单元测试.
 * <p>
 * 覆盖 ID 唯一性与递增性、字符串形式、机器/数据中心/时间戳的往返解析及 ID 起终点范围.
 *
 * @author hongzhe.qin
 */
@DisplayName("Snowflake 雪花算法")
class SnowflakeTest {

    /**
     * 验证 {@link Snowflake#nextId()} 连续生成大量 ID 不重复.
     */
    @Test
    @DisplayName("nextId 唯一性")
    void nextIdUnique() {
        final Snowflake snowflake = new Snowflake();
        final Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            ids.add(snowflake.nextId());
        }
        assertEquals(10000, ids.size());
    }

    /**
     * 验证 {@link Snowflake#nextId()} 生成的 ID 单调递增.
     */
    @Test
    @DisplayName("nextId 递增")
    void nextIdIncreasing() {
        final Snowflake snowflake = new Snowflake();
        long prev = snowflake.nextId();
        for (int i = 0; i < 1000; i++) {
            final long id = snowflake.nextId();
            assertTrue(id > prev);
            prev = id;
        }
    }

    /**
     * 验证 {@link Snowflake#nextIdStr()} 返回数字字符串.
     */
    @Test
    @DisplayName("nextIdStr 数字字符串")
    void nextIdStr() {
        final Snowflake snowflake = new Snowflake();
        assertTrue(snowflake.nextIdStr().matches("\\d+"));
    }

    /**
     * 验证 {@link Snowflake#getWorkerId(long)} 与 {@link Snowflake#getDataCenterId(long)} 能还原构造时指定的值.
     */
    @Test
    @DisplayName("机器与数据中心往返解析")
    void workerAndDataCenterRoundTrip() {
        final Snowflake snowflake = new Snowflake(1, 2);
        final long id = snowflake.nextId();
        assertEquals(1, snowflake.getWorkerId(id));
        assertEquals(2, snowflake.getDataCenterId(id));
    }

    /**
     * 验证 {@link Snowflake#getGenerateDateTime(long)} 返回的时间接近生成时间.
     */
    @Test
    @DisplayName("生成时间往返解析")
    void generateDateTimeRoundTrip() {
        final Snowflake snowflake = new Snowflake();
        final long id = snowflake.nextId();
        final long timestamp = snowflake.getGenerateDateTime(id);
        assertTrue(Math.abs(System.currentTimeMillis() - timestamp) < 10000);
    }

    /**
     * 验证 {@link Snowflake#getIdScopeByTimestamp(long, long)} 返回的起点 ID 小于终点 ID.
     */
    @Test
    @DisplayName("ID 起终点范围")
    void idScope() {
        final Snowflake snowflake = new Snowflake();
        final Pair<Long, Long> scope = snowflake.getIdScopeByTimestamp(1000, 2000);
        assertTrue(scope.getKey() < scope.getValue());
    }
}
