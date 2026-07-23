package cc.hongz.ztools.core.lang.id;

import cc.hongz.ztools.core.lang.Snowflake;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IdConstantsTest {

	@Test
	public void datacenterIdRangeTest() {
		// 数据中心ID应在 Snowflake 允许范围内
		assertTrue(IdConstants.DEFAULT_DATACENTER_ID >= 0);
		assertTrue(IdConstants.DEFAULT_DATACENTER_ID <= Snowflake.MAX_DATA_CENTER_ID);
	}

	@Test
	public void workerIdRangeTest() {
		// Worker ID应在 Snowflake 允许范围内
		assertTrue(IdConstants.DEFAULT_WORKER_ID >= 0);
		assertTrue(IdConstants.DEFAULT_WORKER_ID <= Snowflake.MAX_WORKER_ID);
	}

	@Test
	public void defaultSnowflakeNotNullTest() {
		assertNotNull(IdConstants.DEFAULT_SNOWFLAKE);
	}

	@Test
	public void defaultSnowflakeGenerateIdTest() {
		// 使用默认Snowflake实例生成ID应为正数
		long id = IdConstants.DEFAULT_SNOWFLAKE.nextId();
		assertTrue(id > 0);
	}
}
