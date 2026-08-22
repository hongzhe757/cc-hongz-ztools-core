package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.lang.test.bean.ExamInfoDict;
import cc.hongz.ztools.core.lang.test.bean.UserInfoDict;
import cc.hongz.ztools.core.util.ArrayUtil;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BeanPath} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("路径表达式")
class BeanPathTest {

    Map<String, Object> tempMap;

    @BeforeEach
    void init() {
        // ------------------------------------------------- 考试信息列表
        final ExamInfoDict examInfoDict = new ExamInfoDict();
        examInfoDict.setId(1);
        examInfoDict.setExamType(0);
        examInfoDict.setAnswerIs(1);

        final ExamInfoDict examInfoDict1 = new ExamInfoDict();
        examInfoDict1.setId(2);
        examInfoDict1.setExamType(0);
        examInfoDict1.setAnswerIs(0);

        final ExamInfoDict examInfoDict2 = new ExamInfoDict();
        examInfoDict2.setId(3);
        examInfoDict2.setExamType(1);
        examInfoDict2.setAnswerIs(0);

        final List<ExamInfoDict> examInfoDicts = new ArrayList<>();
        examInfoDicts.add(examInfoDict);
        examInfoDicts.add(examInfoDict1);
        examInfoDicts.add(examInfoDict2);

        // ------------------------------------------------- 用户信息
        final UserInfoDict userInfoDict = new UserInfoDict();
        userInfoDict.setId(1);
        userInfoDict.setPhotoPath("yx.mm.com");
        userInfoDict.setRealName("张三");
        userInfoDict.setExamInfoDict(examInfoDicts);

        tempMap = new HashMap<>();
        tempMap.put("userInfo", userInfoDict);
        tempMap.put("flag", 1);
    }

    /**
     * 验证点号分隔的路径表达式可正确解析出各段路径.
     */
    @Test
    @DisplayName("点号路径解析")
    void beanPathTest1() {
        final BeanPath pattern = new BeanPath("userInfo.examInfoDict[0].id");
        assertEquals("userInfo", pattern.patternParts.get(0));
        assertEquals("examInfoDict", pattern.patternParts.get(1));
        assertEquals("0", pattern.patternParts.get(2));
        assertEquals("id", pattern.patternParts.get(3));

    }

    /**
     * 验证方括号形式的路径表达式可正确解析出各段路径.
     */
    @Test
    @DisplayName("方括号路径解析")
    void beanPathTest2() {
        final BeanPath pattern = new BeanPath("[userInfo][examInfoDict][0][id]");
        assertEquals("userInfo", pattern.patternParts.get(0));
        assertEquals("examInfoDict", pattern.patternParts.get(1));
        assertEquals("0", pattern.patternParts.get(2));
        assertEquals("id", pattern.patternParts.get(3));
    }

    /**
     * 验证带单引号的方括号路径表达式可正确解析出各段路径.
     */
    @Test
    @DisplayName("引号路径解析")
    void beanPathTest3() {
        final BeanPath pattern = new BeanPath("['userInfo']['examInfoDict'][0]['id']");
        assertEquals("userInfo", pattern.patternParts.get(0));
        assertEquals("examInfoDict", pattern.patternParts.get(1));
        assertEquals("0", pattern.patternParts.get(2));
        assertEquals("id", pattern.patternParts.get(3));
    }

    /**
     * 验证点号路径可读取嵌套 Map 中的值.
     */
    @Test
    @DisplayName("点号路径取值")
    void getTest() {
        final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[0].id");
        final Object result = pattern.get(tempMap);
        assertEquals(1, result);
    }

    /**
     * 验证点号路径可向嵌套 Map 中写入值.
     */
    @Test
    @DisplayName("点号路径赋值")
    void setTest() {
        final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[0].id");
        pattern.set(tempMap, 2);
        final Object result = pattern.get(tempMap);
        assertEquals(2, result);
    }

    /**
     * 验证多属性取值可返回包含多个字段的 Map.
     */
    @Test
    @DisplayName("多属性取值")
    void getMapTest() {
        final BeanPath pattern = BeanPath.create("userInfo[id, photoPath]");
        @SuppressWarnings("unchecked") final Map<String, Object> result = (Map<String, Object>) pattern.get(tempMap);
        assertEquals(1, result.get("id"));
        assertEquals("yx.mm.com", result.get("photoPath"));
    }

    /**
     * 验证通过索引路径赋值时可自动创建 List 并填充元素.
     */
    @Test
    @DisplayName("索引自动建列表")
    void issue2362Test() {
        final Map<String, Object> map = new HashMap<>();

        BeanPath beanPath = BeanPath.create("list[0].name");
        beanPath.set(map, "张三");
        assertEquals("{list=[{name=张三}]}", map.toString());

        map.clear();
        beanPath = BeanPath.create("list[1].name");
        beanPath.set(map, "张三");
        assertEquals("{list=[null, {name=张三}]}", map.toString());

        map.clear();
        beanPath = BeanPath.create("list[0].1.name");
        beanPath.set(map, "张三");
        assertEquals("{list=[[null, {name=张三}]]}", map.toString());
    }

    /**
     * 验证按索引赋值可自动追加数组元素.
     */
    @Test
    @DisplayName("索引追加数组")
    void appendArrayTest(){
        // issue#3008@Github
        final MyUser myUser = new MyUser();
        BeanPath.create("hobby[0]").set(myUser, "LOL");
        BeanPath.create("hobby[1]").set(myUser, "KFC");
        BeanPath.create("hobby[2]").set(myUser, "COFFE");

        assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getHobby()));
    }

    /**
     * 验证通配符 * 语法可读取列表中所有元素的指定字段.
     */
    @Test
    @DisplayName("通配符取值")
    void wildcardTest() {
        // 测试通配符 * 语法
        final BeanPath pattern = BeanPath.create("userInfo.examInfoDict[*].id");
        final Object result = pattern.get(tempMap);
        
        // 应该返回一个包含所有 examInfoDict 元素的 id 的列表
        assertEquals("[1, 2, 3]", result.toString());
    }

    @Data
    static class MyUser {
        private String[] hobby;
    }
}
