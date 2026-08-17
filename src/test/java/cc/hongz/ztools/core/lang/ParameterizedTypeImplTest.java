package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link ParameterizedTypeImpl} 单元测试.
 * <p>
 * 覆盖泛型类型构造与字符串表示.
 *
 * @author hongzhe.qin
 */
@DisplayName("ParameterizedTypeImpl 泛型类型")
class ParameterizedTypeImplTest {

    /**
     * 验证构造与 getter.
     */
    @Test
    @DisplayName("构造与 getter")
    void getters() {
        final Type[] typeArgs = {String.class};
        final ParameterizedTypeImpl type = new ParameterizedTypeImpl(typeArgs, null, List.class);

        assertEquals(List.class, type.getRawType());
        assertNull(type.getOwnerType());
        assertArrayEquals(typeArgs, type.getActualTypeArguments());
    }

    /**
     * 验证 {@link ParameterizedTypeImpl#toString()} 输出泛型字符串.
     */
    @Test
    @DisplayName("toString 泛型字符串")
    void toStringTest() {
        final ParameterizedTypeImpl type = new ParameterizedTypeImpl(
                new Type[]{String.class}, null, List.class);
        assertEquals("java.util.List<java.lang.String>", type.toString());
    }
}
