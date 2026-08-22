package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue3497Test {
    @Test
    @DisplayName("toBean map fieldNameEditor camelCase")
    void mapToMapTest() {
        final Map<String, String> aB = MapUtil.builder("a_b", "1").build();
        final Map<?, ?> bean = BeanUtil.toBean(aB, Map.class, CopyOptions.create().setFieldNameEditor(StrUtil::toCamelCase));
        assertEquals(bean.toString(), "{aB=1}");
    }
}
