package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RecordUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("RecordUtil Record 工具")
class RecordUtilTest {

    record Person(String name, int age) {
    }

    static class NormalBean {
    }

    /**
     * Record 类返回 true，普通类返回 false.
     */
    @Test
    @DisplayName("判断 Record 类")
    void isRecord() {
        assertTrue(RecordUtil.isRecord(Person.class));
        assertFalse(RecordUtil.isRecord(NormalBean.class));
    }

    /**
     * 获取 Record 的全部组件名与类型.
     */
    @Test
    @DisplayName("获取 Record 组件")
    void getRecordComponents() {
        Map.Entry<String, Type>[] components = RecordUtil.getRecordComponents(Person.class);
        assertEquals(2, components.length);
        assertEquals("name", components[0].getKey());
        assertEquals("age", components[1].getKey());
    }
}
