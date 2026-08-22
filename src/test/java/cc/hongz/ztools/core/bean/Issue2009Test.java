package cc.hongz.ztools.core.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue2009Test {

    @SuppressWarnings("InnerClassMayBeStatic")
    public class BaseA {
        private String paPss;

        public String getPaPss() {
            return paPss;
        }

        public void setPaPss(String paPss) {
            this.paPss = paPss;
        }
    }


    public class A extends BaseA {
        private String papss;

        public String getPapss() {
            return papss;
        }

        public void setPapss(String papss) {
            this.papss = papss;
        }

        @Override
        public String toString() {
            return "A{" +
                    "papss='" + papss + '\'' +
                    '}';
        }
    }


    public class B extends BaseA {
        private String papss;

        public B(String papss) {
            this.papss = papss;
        }

        public String getPapss() {
            return papss;
        }

        public void setPapss(String papss) {
            this.papss = papss;
        }

        @Override
        public String toString() {
            return "B{" +
                    "papss='" + papss + '\'' +
                    '}';
        }
    }

    /**
     * 验证继承父类的字段可正确拷贝.
     */
    @Test
    @DisplayName("继承字段拷贝")
    void test() {
        B b = new B("a string text");
        A a = new A();
        BeanUtil.copyProperties(b, a);

        assertEquals(b.getPapss(), a.getPapss());
    }
}
