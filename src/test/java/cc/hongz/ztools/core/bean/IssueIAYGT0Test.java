package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class IssueIAYGT0Test {

    /**
     * 验证 setProperty 通过 BeanPath 直接给字段赋值，而非调用 setter 方法.
     */
    @Test
    @DisplayName("直接字段赋值")
    void setPropertyTest() {
        Cat cat = new Cat();
        BeanUtil.setProperty(cat, "name", "Kitty");
        Assertions.assertEquals("Kitty", cat.getName());
    }

    static class Cat {
        private String name;

        public void setName(String name) {
            this.name = "Red" + name;
        }

        public String getName() {
            return name;
        }
    }

}
