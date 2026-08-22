package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanInfoCache} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("BeanInfoCache 属性描述缓存")
class BeanInfoCacheTest {

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
     * 写入缓存后按相同 Class 与忽略大小写标记读取到同一个 Map.
     */
    @Test
    @DisplayName("缓存写入与读取")
    void putAndGet() {
        Map<String, PropertyDescriptor> map = new HashMap<>();
        BeanInfoCache.INSTANCE.putPropertyDescriptorMap(User.class, map, false);
        assertSame(map, BeanInfoCache.INSTANCE.getPropertyDescriptorMap(User.class, false));
    }

    /**
     * 忽略大小写与不忽略使用各自独立的缓存.
     */
    @Test
    @DisplayName("忽略大小写独立缓存")
    void ignoreCase_separateCache() {
        Map<String, PropertyDescriptor> map = new HashMap<>();
        BeanInfoCache.INSTANCE.putPropertyDescriptorMap(User.class, map, false);
        assertNull(BeanInfoCache.INSTANCE.getPropertyDescriptorMap(User.class, true));
    }

    /**
     * 清空缓存后读取返回 null.
     */
    @Test
    @DisplayName("清空缓存")
    void clear_clears() {
        Map<String, PropertyDescriptor> map = new HashMap<>();
        BeanInfoCache.INSTANCE.putPropertyDescriptorMap(User.class, map, false);
        BeanInfoCache.INSTANCE.clear();
        assertNull(BeanInfoCache.INSTANCE.getPropertyDescriptorMap(User.class, false));
    }
}
