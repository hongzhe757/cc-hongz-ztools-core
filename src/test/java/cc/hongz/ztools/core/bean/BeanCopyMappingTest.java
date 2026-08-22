package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.map.MapUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("字段映射拷贝")
class BeanCopyMappingTest {

    /**
     * https://gitee.com/chinabugotech/hutool/issues/I4C48U <br>
     * 传递复制不要用注解别名，应该用动态映射
     */
    @Test
    @DisplayName("copyProperties fieldMapping mapsCarToCarNo")
    void copyPropertiesTest() {
        final CopyOptions copyOptions = CopyOptions.create()
                .setFieldMapping(MapUtil.of("car", "carNo"));

        B b = B.builder().car("12312312").build();
        A a = A.builder().build();
        C c = C.builder().build();
        BeanUtil.copyProperties(b, a, copyOptions);
        BeanUtil.copyProperties(a, c);

        assertEquals("12312312", c.getCarNo());
    }

    @Data
    @Builder
    public static class A {
        private String carNo;
    }

    @Data
    @Builder
    public static class B {
        private String car;
    }

    @Data
    @Builder
    public static class C {
        private String carNo;
    }
}
