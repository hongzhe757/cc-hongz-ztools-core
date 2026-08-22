package cc.hongz.ztools.core.bean;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue2082Test {

    /**
     * 验证存在重载 setter 时转换不会误调用，id 保持 null.
     */
    @Test
    @DisplayName("重载 setter 转换")
    void toBeanTest() {
        TestBean2 testBean2 = new TestBean2();
        TestBean test = BeanUtil.toBean(testBean2, TestBean.class);
        assertNull(test.getId());
    }

    @Data
    static class TestBean {
        private Long id;

        public void setId(String id) {
            this.id = Long.valueOf(id);
        }
    }

    @Data
    static class TestBean2 {
        private String id;
    }
}
