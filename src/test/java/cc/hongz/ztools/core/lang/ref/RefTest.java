package cc.hongz.ztools.core.lang.ref;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.ref.ReferenceQueue;

/**
 * {@link StrongObj}、{@link SoftObj}、{@link WeakObj}、{@link PhantomObj}、{@link ReferenceType} 单元测试.
 */
@DisplayName("Ref 引用对象")
class RefTest {

    // ==================== StrongObj ====================

    @Test
    @DisplayName("StrongObj 获取对象")
    void strongObjGet() {
        StrongObj<String> ref = new StrongObj<>("hello");
        assertEquals("hello", ref.get());
    }

    @Test
    @DisplayName("StrongObj 相等判断")
    void strongObjEquals() {
        StrongObj<String> a = new StrongObj<>("hello");
        StrongObj<String> b = new StrongObj<>("hello");
        StrongObj<String> c = new StrongObj<>("world");

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }

    @Test
    @DisplayName("StrongObj hashCode")
    void strongObjHashCode() {
        StrongObj<String> a = new StrongObj<>("hello");
        StrongObj<String> b = new StrongObj<>("hello");
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("hello".hashCode(), a.hashCode());
    }

    // ==================== SoftObj ====================

    @Test
    @DisplayName("SoftObj 获取对象")
    void softObjGet() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        SoftObj<String> ref = new SoftObj<>("hello", queue);
        assertEquals("hello", ref.get());
    }

    @Test
    @DisplayName("SoftObj 相等判断")
    void softObjEquals() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        SoftObj<String> a = new SoftObj<>("hello", queue);
        SoftObj<String> b = new SoftObj<>("hello", queue);
        SoftObj<String> c = new SoftObj<>("world", queue);

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }

    @Test
    @DisplayName("SoftObj hashCode")
    void softObjHashCode() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        SoftObj<String> ref = new SoftObj<>("hello", queue);
        assertEquals("hello".hashCode(), ref.hashCode());
    }

    // ==================== WeakObj ====================

    @Test
    @DisplayName("WeakObj 获取对象")
    void weakObjGet() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        WeakObj<String> ref = new WeakObj<>("hello", queue);
        assertEquals("hello", ref.get());
    }

    @Test
    @DisplayName("WeakObj 相等判断")
    void weakObjEquals() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        WeakObj<String> a = new WeakObj<>("hello", queue);
        WeakObj<String> b = new WeakObj<>("hello", queue);
        WeakObj<String> c = new WeakObj<>("world", queue);

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }

    @Test
    @DisplayName("WeakObj hashCode")
    void weakObjHashCode() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        WeakObj<String> ref = new WeakObj<>("hello", queue);
        assertEquals("hello".hashCode(), ref.hashCode());
    }

    // ==================== PhantomObj ====================

    @Test
    @DisplayName("PhantomObj 获取对象")
    void phantomObjGet() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        PhantomObj<String> ref = new PhantomObj<>("hello", queue);
        // PhantomReference.get() 始终返回 null
        assertNull(ref.get());
    }

    @Test
    @DisplayName("PhantomObj 相等判断")
    void phantomObjEquals() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        PhantomObj<String> a = new PhantomObj<>("hello", queue);
        PhantomObj<String> b = new PhantomObj<>("world", queue);

        // PhantomReference.get() 始终返回 null，因此两个 PhantomObj 总是 equals
        assertEquals(a, b);
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "hello");
    }

    @Test
    @DisplayName("PhantomObj hashCode")
    void phantomObjHashCode() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        PhantomObj<String> a = new PhantomObj<>("hello", queue);
        PhantomObj<String> b = new PhantomObj<>("hello", queue);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("hello".hashCode(), a.hashCode());
    }

    // ==================== ReferenceType ====================

    @Test
    @DisplayName("ReferenceType 枚举值")
    void referenceTypeValues() {
        ReferenceType[] types = ReferenceType.values();
        assertEquals(4, types.length);
        assertEquals(ReferenceType.STRONG, ReferenceType.valueOf("STRONG"));
        assertEquals(ReferenceType.SOFT, ReferenceType.valueOf("SOFT"));
        assertEquals(ReferenceType.WEAK, ReferenceType.valueOf("WEAK"));
        assertEquals(ReferenceType.PHANTOM, ReferenceType.valueOf("PHANTOM"));
    }

    // ==================== Ref 接口 lambda ====================

    @Test
    @DisplayName("Ref 函数式接口")
    void refFunctionalInterface() {
        Ref<String> ref = () -> "lambda";
        assertEquals("lambda", ref.get());
    }
}
