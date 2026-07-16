package cc.hongz.ztools.core.lang.id;

import cc.hongz.ztools.core.lang.Snowflake;
import cc.hongz.ztools.core.util.IdUtil;

/**
 * ID相关常量.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.id.IdConstants}。
 *
 * @author Looly
 */
public class IdConstants {

    /**
     * 默认的数据中心ID.
     */
    public static final long DEFAULT_DATACENTER_ID = IdUtil.getDataCenterId(Snowflake.MAX_DATA_CENTER_ID);

    /**
     * 默认的Worker ID.
     */
    public static final long DEFAULT_WORKER_ID = IdUtil.getWorkerId(DEFAULT_DATACENTER_ID, Snowflake.MAX_WORKER_ID);

    /**
     * 默认的Snowflake单例.
     */
    public static final Snowflake DEFAULT_SNOWFLAKE = new Snowflake(DEFAULT_WORKER_ID, DEFAULT_DATACENTER_ID);
}
