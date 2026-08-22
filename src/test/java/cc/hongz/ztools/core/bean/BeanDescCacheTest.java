package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanDescCache} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("BeanDescCache Bean 描述缓存")
class BeanDescCacheTest {

    static class User {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }

    /**
     * 相同 Class 重复获取返回同一个 BeanDesc 实例.
     */
    @Test
    @DisplayName("缓存复用")
    void getBeanDesc_caches() {
        BeanDesc desc1 = BeanDescCache.INSTANCE.getBeanDesc(User.class, () -> new BeanDesc(User.class));
        BeanDesc desc2 = BeanDescCache.INSTANCE.getBeanDesc(User.class, () -> new BeanDesc(User.class));
        assertSame(desc1, desc2);
    }

    /**
     * 清空缓存后重新获取返回新的 BeanDesc 实例.
     */
    @Test
    @DisplayName("清空缓存")
    void clear_clears() {
        BeanDesc desc1 = BeanDescCache.INSTANCE.getBeanDesc(User.class, () -> new BeanDesc(User.class));
        BeanDescCache.INSTANCE.clear();
        BeanDesc desc2 = BeanDescCache.INSTANCE.getBeanDesc(User.class, () -> new BeanDesc(User.class));
        assertNotSame(desc1, desc2);
    }
}
