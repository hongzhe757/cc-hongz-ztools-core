package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.builder.GenericBuilder;
import cc.hongz.ztools.core.date.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import static cc.hongz.ztools.core.lang.OptTest.User;

/**
 * {@link Dict} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Dict 字典")
class DictTest {

    /**
     * 链式 set 后按类型取值.
     */
    @Test
    @DisplayName("链式 set 后 getLong 取值")
    void dictTest() {
        Dict dict = Dict.create()
                .set("key1", 1)//int
                .set("key2", 1000L)//long
                .set("key3", DateTime.now());//Date

        Long v2 = dict.getLong("key2");
        assertEquals(Long.valueOf(1000L), v2);
    }

    /**
     * 忽略大小写的 Dict，putAll 后大小写不敏感取值.
     */
    @Test
    @DisplayName("忽略大小写取值")
    void dictTest2() {
        final Dict dict = new Dict(true);
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);

        dict.putAll(map);

        assertEquals(1, dict.get("A"));
        assertEquals(1, dict.get("a"));
    }

    /**
     * 静态工厂 of 按键值对创建.
     */
    @Test
    @DisplayName("of 创建")
    void ofTest() {
        Dict dict = Dict.of(
                "RED", "#FF0000",
                "GREEN", "#00FF00",
                "BLUE", "#0000FF"
        );

        assertEquals("#FF0000", dict.get("RED"));
        assertEquals("#00FF00", dict.get("GREEN"));
        assertEquals("#0000FF", dict.get("BLUE"));
    }

    /**
     * removeEqual 移除与另一个 Dict 值相同的键.
     */
    @Test
    @DisplayName("removeEqual 移除相同项")
    void removeEqualTest() {
        Dict dict = Dict.of(
            "key1", null
        );

        Dict dict2 = Dict.of(
            "key1", null
        );

        dict.removeEqual(dict2);

        assertTrue(dict.isEmpty());
    }

    /**
     * setFields 通过 lambda 方法引用批量设置字段.
     */
    @Test
    @DisplayName("setFields 批量设置")
    void setFieldsTest() {
        User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
        Dict dict = Dict.create();
        dict.setFields(user::getNickname, user::getUsername);
        assertEquals("hutool", dict.get("username"));
        assertNull(dict.get("nickname"));
    }
}
