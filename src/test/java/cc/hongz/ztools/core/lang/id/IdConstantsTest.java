package cc.hongz.ztools.core.lang.id;

import cc.hongz.ztools.core.lang.Snowflake;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("IdConstants 单元测试")
class IdConstantsTest {

    @DisplayName("数据中心 ID 范围")
    @Test
    public void datacenterIdRangeTest() {
        // 数据中心ID应在 Snowflake 允许范围内
        assertTrue(IdConstants.DEFAULT_DATACENTER_ID >= 0);
        assertTrue(IdConstants.DEFAULT_DATACENTER_ID <= Snowflake.MAX_DATA_CENTER_ID);
    }

    @DisplayName("Worker ID 范围")
    @Test
    public void workerIdRangeTest() {
        // Worker ID应在 Snowflake 允许范围内
        assertTrue(IdConstants.DEFAULT_WORKER_ID >= 0);
        assertTrue(IdConstants.DEFAULT_WORKER_ID <= Snowflake.MAX_WORKER_ID);
    }

    @DisplayName("默认 Snowflake 非 null")
    @Test
    public void defaultSnowflakeNotNullTest() {
        assertNotNull(IdConstants.DEFAULT_SNOWFLAKE);
    }

    @DisplayName("默认 Snowflake 生成 ID")
    @Test
    public void defaultSnowflakeGenerateIdTest() {
        // 使用默认Snowflake实例生成ID应为正数
        long id = IdConstants.DEFAULT_SNOWFLAKE.nextId();
        assertTrue(id > 0);
    }
}
