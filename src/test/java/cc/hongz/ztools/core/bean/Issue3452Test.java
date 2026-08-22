package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue3452Test {

    /**
     * 验证通过下划线键填充 Bean 中对应的驼峰字段.
     */
    @Test
    @DisplayName("下划线键填充")
    void fillBeanWithMapTest() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("name", "JohnDoe");
        properties.put("user_age", 25);
        final User user = BeanUtil.fillBeanWithMap(
            properties, new User(), CopyOptions.create());
        assertEquals("JohnDoe", user.getName());
        assertEquals(25, user.getUserAge());
    }

    @Data
    static class User {
        private String name;
        private int userAge;
    }
}
