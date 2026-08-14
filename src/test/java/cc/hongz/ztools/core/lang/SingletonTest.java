package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Singleton} 单元测试.
 * <p>
 * 覆盖单例获取、Supplier 缓存、不同键隔离、put/get 往返、exists/remove/destroy.
 *
 * @author hongzhe.qin
 */
@DisplayName("Singleton 单例池")
class SingletonTest {

    /**
     * 每个用例后清空单例池，避免相互影响.
     */
    @AfterEach
    void tearDown() {
        Singleton.destroy();
    }

    /**
     * 验证 {@link Singleton#get(Class, Object...)} 对同一类返回同一实例.
     */
    @Test
    @DisplayName("get(Class) 返回同一实例")
    void getByClassSingleton() {
        final List<?> list1 = Singleton.get(ArrayList.class);
        final List<?> list2 = Singleton.get(ArrayList.class);
        assertSame(list1, list2);
    }

    /**
     * 验证 {@link Singleton#get(String, java.util.function.Supplier)} 对同一 key 缓存实例.
     */
    @Test
    @DisplayName("get(Supplier) 缓存实例")
    void getBySupplierCached() {
        final Object obj1 = Singleton.get("cache-key", Object::new);
        final Object obj2 = Singleton.get("cache-key", Object::new);
        assertSame(obj1, obj2);
    }

    /**
     * 验证不同 key 得到不同实例.
     */
    @Test
    @DisplayName("不同 key 不同实例")
    void differentKeyDifferentInstance() {
        final Object obj1 = Singleton.get("key-a", Object::new);
        final Object obj2 = Singleton.get("key-b", Object::new);
        assertNotSame(obj1, obj2);
    }

    /**
     * 验证 {@link Singleton#put(Object)} 后 {@link Singleton#get(Class, Object...)} 返回同一对象.
     */
    @Test
    @DisplayName("put 后 get 返回同一对象")
    void putThenGetRoundTrip() {
        final Object obj = new Object();
        Singleton.put(obj);
        assertSame(obj, Singleton.get(Object.class));
    }

    /**
     * 验证 {@link Singleton#exists(Class, Object...)} 判断对象是否存在.
     */
    @Test
    @DisplayName("exists 存在判断")
    void exists() {
        final Object obj = new Object();
        Singleton.put(obj);
        assertTrue(Singleton.exists(Object.class));
        assertFalse(Singleton.exists(ArrayList.class));
    }

    /**
     * 验证 {@link Singleton#remove(Class)} 移除后对象不存在.
     */
    @Test
    @DisplayName("remove 移除")
    void remove() {
        final Object obj = new Object();
        Singleton.put(obj);
        Singleton.remove(Object.class);
        assertFalse(Singleton.exists(Object.class));
    }

    /**
     * 验证 {@link Singleton#destroy()} 清空单例池且 {@link Singleton#getExistClass()} 反映池内容.
     */
    @Test
    @DisplayName("destroy 清空与 getExistClass")
    void destroyAndGetExistClass() {
        Singleton.put(new Object());
        assertTrue(Singleton.getExistClass().contains(Object.class));
        Singleton.destroy();
        assertTrue(Singleton.getExistClass().isEmpty());
    }
}
