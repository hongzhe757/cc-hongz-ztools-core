package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link TypeReference} 单元测试.
 * <p>
 * 覆盖泛型参数类型解析、类型缓存、嵌套泛型解析及 toString.
 *
 * @author hongzhe.qin
 */
@DisplayName("TypeReference 泛型类型参考")
class TypeReferenceTest {

    /**
     * 验证 {@link TypeReference#getType()} 返回泛型参数类型 {@code List<String>}.
     */
    @Test
    @DisplayName("getType 返回泛型参数类型")
    void getTypeReturnsParameterizedType() {
        final Type type = new TypeReference<List<String>>() {}.getType();
        assertTrue(type instanceof ParameterizedType);
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(List.class, parameterizedType.getRawType());
        assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
    }

    /**
     * 验证多次调用 {@link TypeReference#getType()} 返回同一实例.
     */
    @Test
    @DisplayName("getType 多次调用返回同一实例")
    void getTypeCached() {
        final TypeReference<List<String>> reference = new TypeReference<List<String>>() {};
        assertSame(reference.getType(), reference.getType());
    }

    /**
     * 验证嵌套泛型 {@code Map<String, List<Integer>>} 能正确解析.
     */
    @Test
    @DisplayName("嵌套泛型解析")
    void nestedGenericType() {
        final Type type = new TypeReference<Map<String, List<Integer>>>() {}.getType();
        assertTrue(type instanceof ParameterizedType);
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(Map.class, parameterizedType.getRawType());
        assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
        final ParameterizedType valueType = (ParameterizedType) parameterizedType.getActualTypeArguments()[1];
        assertEquals(List.class, valueType.getRawType());
        assertEquals(Integer.class, valueType.getActualTypeArguments()[0]);
    }

    /**
     * 验证 {@link TypeReference#toString()} 返回类型字符串.
     */
    @Test
    @DisplayName("toString 返回类型字符串")
    void toStringReturnsTypeString() {
        final TypeReference<List<String>> reference = new TypeReference<List<String>>() {};
        final String str = reference.toString();
        assertTrue(str.contains("List"));
        assertTrue(str.contains("String"));
    }
}
