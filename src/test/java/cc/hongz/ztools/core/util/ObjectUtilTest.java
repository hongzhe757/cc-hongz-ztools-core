package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link ObjectUtil} 单元测试.
 * <p>
 * 覆盖空值默认值处理.
 *
 * @author hongzhe.qin
 */
class ObjectUtilTest {

    /**
     * 验证 {@link ObjectUtil#defaultIfNull(Object, Object)} 在 null 与非 null 时的返回值.
     */
    @Test
    void defaultIfNull() {
        assertEquals("d", ObjectUtil.defaultIfNull(null, "d"));
        assertEquals("v", ObjectUtil.defaultIfNull("v", "d"));
        assertNull(ObjectUtil.defaultIfNull(null, null));
    }
}
