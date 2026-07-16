package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.ref.Ref;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ReferenceUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ReferenceUtil 引用工具")
class ReferenceUtilTest {

    // --------------------------------------- create

    @Test
    @DisplayName("create WEAK 返回 WeakReference")
    void createWeak() {
        Reference<Integer> ref = ReferenceUtil.create(ReferenceUtil.ReferenceType.WEAK, 1);
        assertInstanceOf(WeakReference.class, ref);
        assertEquals(1, ref.get());
    }

    @Test
    @DisplayName("create SOFT 返回 SoftReference")
    void createSoft() {
        Reference<Integer> ref = ReferenceUtil.create(ReferenceUtil.ReferenceType.SOFT, 42);
        assertInstanceOf(SoftReference.class, ref);
        assertEquals(42, ref.get());
    }

    @Test
    @DisplayName("create PHANTOM 返回 PhantomReference，get 永远为 null")
    void createPhantom() {
        Reference<Integer> ref = ReferenceUtil.create(ReferenceUtil.ReferenceType.PHANTOM, 1);
        assertInstanceOf(PhantomReference.class, ref);
        // PhantomReference.get() 永远返回 null
        assertNull(ref.get());
    }

    @Test
    @DisplayName("create 无队列版本")
    void createNoQueue() {
        Reference<String> ref = ReferenceUtil.create(ReferenceUtil.ReferenceType.SOFT, "hello");
        assertNotNull(ref);
        assertEquals("hello", ref.get());
    }

    @Test
    @DisplayName("create null referent")
    void createNullReferent() {
        Reference<Object> ref = ReferenceUtil.create(ReferenceUtil.ReferenceType.WEAK, null);
        assertNotNull(ref);
        assertNull(ref.get());
    }

    // --------------------------------------- get(Reference)

    @Test
    @DisplayName("get(Reference) 正常解包")
    void getReference() {
        Reference<String> ref = new SoftReference<>("test");
        assertEquals("test", ReferenceUtil.get(ref));
    }

    @Test
    @DisplayName("get(Reference) null 入参返回 null")
    void getReferenceNull() {
        assertNull(ReferenceUtil.get((Reference<String>) null));
    }

    // --------------------------------------- get(Ref)

    @Test
    @DisplayName("get(Ref) 正常解包")
    void getRef() {
        Ref<String> ref = () -> "hello";
        assertEquals("hello", ReferenceUtil.get(ref));
    }

    @Test
    @DisplayName("get(Ref) null 入参返回 null")
    void getRefNull() {
        assertNull(ReferenceUtil.get((Ref<String>) null));
    }

    @Test
    @DisplayName("get(Ref) Lambda 表达式")
    void getRefLambda() {
        Ref<Integer> ref = () -> 100;
        assertEquals(100, ReferenceUtil.get(ref));
    }

    // --------------------------------------- ReferenceType enum

    @Test
    @DisplayName("ReferenceType 枚举值完整")
    void referenceTypeValues() {
        ReferenceUtil.ReferenceType[] values = ReferenceUtil.ReferenceType.values();
        assertEquals(3, values.length);
        assertNotNull(ReferenceUtil.ReferenceType.valueOf("SOFT"));
        assertNotNull(ReferenceUtil.ReferenceType.valueOf("WEAK"));
        assertNotNull(ReferenceUtil.ReferenceType.valueOf("PHANTOM"));
    }
}
