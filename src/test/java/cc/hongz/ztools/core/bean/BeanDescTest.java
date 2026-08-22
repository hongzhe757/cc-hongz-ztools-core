package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanDesc} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("属性描述")
class BeanDescTest {

    @Test
    @DisplayName("propDesc age field getter setter resolved")
    void propDescTes() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);
        assertEquals("User", desc.getSimpleName());

        assertEquals("age", desc.getField("age").getName());
        assertEquals("getAge", desc.getGetter("age").getName());
        assertEquals("setAge", desc.getSetter("age").getName());
        assertEquals(1, desc.getSetter("age").getParameterTypes().length);
        assertSame(int.class, desc.getSetter("age").getParameterTypes()[0]);

    }

    @Test
    @DisplayName("propDesc name prop resolvesGetterSetter")
    void propDescTes2() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        PropDesc prop = desc.getProp("name");
        assertEquals("name", prop.getFieldName());
        assertEquals("getName", prop.getGetter().getName());
        assertEquals("setName", prop.getSetter().getName());
        assertEquals(1, prop.getSetter().getParameterTypes().length);
        assertSame(String.class, prop.getSetter().getParameterTypes()[0]);
    }

    @Test
    @DisplayName("propDesc boolean getters isAdmin isGender")
    void propDescOfBooleanTest() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        assertEquals("isAdmin", desc.getGetter("isAdmin").getName());
        assertEquals("setAdmin", desc.getSetter("isAdmin").getName());
        assertEquals("isGender", desc.getGetter("gender").getName());
        assertEquals("setGender", desc.getSetter("gender").getName());
    }

    @Test
    @DisplayName("propDesc isSuper resolves isIsSuper")
    void propDescOfBooleanTest2() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        assertEquals("isIsSuper", desc.getGetter("isSuper").getName());
        assertEquals("setIsSuper", desc.getSetter("isSuper").getName());
    }

    @Test
    @DisplayName("getSet name prop getSetValue")
    void getSetTest() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        User user = new User();
        desc.getProp("name").setValue(user, "张三");
        assertEquals("张三", user.getName());

        Object value = desc.getProp("name").getValue(user);
        assertEquals("张三", value);
    }

    @Test
    @Disabled
    @DisplayName("propDesc boolean lastPage setters")
    void propDescOfBooleanTest3() {
        BeanDesc desc = BeanUtil.getBeanDesc(User.class);

        assertEquals("setLastPage", desc.getSetter("lastPage").getName());
        assertEquals("setIsLastPage", desc.getSetter("isLastPage").getName());
    }

    public static class User {
        private String name;
        private int age;
        private boolean isAdmin;
        private boolean isSuper;
        private boolean gender;
        private Boolean lastPage;
        private Boolean isLastPage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public String testMethod() {
            return "test for " + this.name;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public boolean isIsSuper() {
            return isSuper;
        }

        public void setIsSuper(boolean isSuper) {
            this.isSuper = isSuper;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public Boolean getLastPage() {
            return this.lastPage;
        }

        public void setLastPage(final Boolean lastPage) {
            this.lastPage = lastPage;
        }

        public Boolean getIsLastPage() {
            return this.isLastPage;
        }

        public void setIsLastPage(final Boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        @Override
        public String toString() {
            return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
        }
    }
}
