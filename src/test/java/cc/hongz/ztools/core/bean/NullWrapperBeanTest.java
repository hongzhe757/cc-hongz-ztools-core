package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link NullWrapperBean} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("NullWrapperBean 空值包装")
class NullWrapperBeanTest {

    @Test
    void getWrappedClassTest() {
        final NullWrapperBean<String> bean = new NullWrapperBean<>(String.class);
        assertNotNull(bean);
        assertEquals(String.class, bean.getWrappedClass());
    }
}
