package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ObjectId} 单元测试。
 *
 * @author looly
 */
@DisplayName("ObjectId 生成")
class ObjectIdTest {

    @Test
    @DisplayName("distinct 唯一性")
    void distinctTest() {
        // 生成10000个id测试是否重复
        final HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            set.add(ObjectId.next());
        }
        assertEquals(10000, set.size());
    }

    @Test
    @Disabled
    @DisplayName("next 手动验证")
    void nextTest() {
        Console.log(ObjectId.next());
    }
}
