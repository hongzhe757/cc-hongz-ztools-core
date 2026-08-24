package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BasicType} 单元测试.
 */
@DisplayName("BasicType 基本类型")
class BasicTypeTest {

    @Test
    @DisplayName("wrap - 原始类型转包装类")
    void wrap_primitive_returnsWrapper() {
        assertEquals(Integer.class, BasicType.wrap(int.class));
        assertEquals(Long.class, BasicType.wrap(long.class));
        assertEquals(Boolean.class, BasicType.wrap(boolean.class));
        assertEquals(Byte.class, BasicType.wrap(byte.class));
        assertEquals(Short.class, BasicType.wrap(short.class));
        assertEquals(Float.class, BasicType.wrap(float.class));
        assertEquals(Double.class, BasicType.wrap(double.class));
        assertEquals(Character.class, BasicType.wrap(char.class));
    }

    @Test
    @DisplayName("wrap - 非原始类型返回原类")
    void wrap_nonPrimitive_returnsSame() {
        assertEquals(String.class, BasicType.wrap(String.class));
        assertEquals(Integer.class, BasicType.wrap(Integer.class));
    }

    @Test
    @DisplayName("wrap - null 返回 null")
    void wrap_null_returnsNull() {
        assertNull(BasicType.wrap(null));
    }

    @Test
    @DisplayName("unWrap - 包装类转原始类型")
    void unWrap_wrapper_returnsPrimitive() {
        assertEquals(int.class, BasicType.unWrap(Integer.class));
        assertEquals(long.class, BasicType.unWrap(Long.class));
        assertEquals(boolean.class, BasicType.unWrap(Boolean.class));
        assertEquals(byte.class, BasicType.unWrap(Byte.class));
    }

    @Test
    @DisplayName("unWrap - 原始类型返回原类")
    void unWrap_primitive_returnsSame() {
        assertEquals(int.class, BasicType.unWrap(int.class));
        assertEquals(long.class, BasicType.unWrap(long.class));
    }

    @Test
    @DisplayName("unWrap - 非包装类返回原类")
    void unWrap_nonWrapper_returnsSame() {
        assertEquals(String.class, BasicType.unWrap(String.class));
    }

    @Test
    @DisplayName("unWrap - null 返回 null")
    void unWrap_null_returnsNull() {
        assertNull(BasicType.unWrap(null));
    }
}
