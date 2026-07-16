package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Snowflake;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * {@link IdUtil} 单元测试.
 */
public class IdUtilTest {

    @Test
    public void randomUUIDTest() {
        final String simpleUUID = IdUtil.simpleUUID();
        assertEquals(32, simpleUUID.length());

        final String randomUUID = IdUtil.randomUUID();
        assertEquals(36, randomUUID.length());
    }

    @Test
    public void fastUUIDTest() {
        final String simpleUUID = IdUtil.fastSimpleUUID();
        assertEquals(32, simpleUUID.length());

        final String randomUUID = IdUtil.fastUUID();
        assertEquals(36, randomUUID.length());
    }

    @Test
    public void objectIdTest() {
        final String id = IdUtil.objectId();
        assertEquals(24, id.length());
    }

    @Test
    public void getSnowflakeTest() {
        final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        final long id = snowflake.nextId();
        assertTrue(id > 0);
    }

    @Test
    public void getDataCenterIdTest() {
        final long dataCenterId = IdUtil.getDataCenterId(Long.MAX_VALUE);
        assertTrue(dataCenterId >= 0);
    }

    @Test
    public void getWorkerIdTest() {
        final long workerId = IdUtil.getWorkerId(1, 31);
        assertTrue(workerId >= 0 && workerId <= 31);
    }

    @Test
    public void nanoIdTest() {
        final String nanoId = IdUtil.nanoId();
        assertEquals(21, nanoId.length());
    }

    @Test
    public void nanoIdCustomSizeTest() {
        final String nanoId = IdUtil.nanoId(10);
        assertEquals(10, nanoId.length());
    }

    @Test
    public void snowflakeNextIdStrTest() {
        final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        final String idStr = snowflake.nextIdStr();
        assertNotNull(idStr);
        assertTrue(Long.parseLong(idStr) > 0);
    }

    @Test
    public void getSnowflakeNextIdTest() {
        final long id = IdUtil.getSnowflakeNextId();
        assertTrue(id > 0);
    }

    @Test
    public void getSnowflakeNextIdStrTest() {
        final String idStr = IdUtil.getSnowflakeNextIdStr();
        assertNotNull(idStr);
        assertTrue(Long.parseLong(idStr) > 0);
    }

    @Test
    public void snowflakeSingletonTest() {
        final Snowflake s1 = IdUtil.getSnowflake(1, 1);
        final Snowflake s2 = IdUtil.getSnowflake(1, 1);
        assertSame(s1, s2);
    }

    @Test
    public void snowflakeGetWorkerIdFromIdTest() {
        final Snowflake snowflake = IdUtil.getSnowflake(5, 10);
        final long id = snowflake.nextId();
        assertEquals(5, snowflake.getWorkerId(id));
        assertEquals(10, snowflake.getDataCenterId(id));
    }
}
