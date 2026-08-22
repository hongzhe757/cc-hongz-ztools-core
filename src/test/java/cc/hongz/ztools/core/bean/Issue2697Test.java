package cc.hongz.ztools.core.bean;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue2697Test {

    /**
     * 验证 Map 拷贝为 Map 时忽略指定键.
     */
    @Test
    @DisplayName("忽略指定键拷贝")
    void mapToMapTest(){
        final Map<String, String> mapA = new HashMap<>(16);
        mapA.put("12", "21");
        mapA.put("121", "21");
        mapA.put("122", "21");
        final Map<String, String> mapB = new HashMap<>(16);
        BeanUtil.copyProperties(mapA, mapB, "12");

        assertEquals(2, mapB.size());
        assertFalse(mapB.containsKey("12"));
    }
}
