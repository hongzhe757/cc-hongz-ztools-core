package cc.hongz.ztools.core.bean;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue3645Test {
    @Test
    @DisplayName("copyProperties mapGenericDeepCopy")
    void copyPropertiesTest() {
        User p = new User();
        p.setUserId(123L);

        Map<Long, User> map = new HashMap<>();
        map.put(123L,p);

        Map<Long, User> m = new HashMap<>();
        BeanUtil.copyProperties(map, m);
        User u = m.get(123L);
        assertNotNull(u);
    }

    @Data
    static class User{
        private Long userId;
    }
}
