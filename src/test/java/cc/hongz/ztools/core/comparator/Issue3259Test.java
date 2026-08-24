package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.collection.ListUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FieldsComparator} 多字段比较 issue#3259 测试.
 */
@DisplayName("FieldsComparator 多字段比较")
public class Issue3259Test {

    @Test
    @DisplayName("多字段比较")
    public void fieldsComparatorTest() {
        Model x = new Model(1, 1);
        Model y = new Model(1, 2);

        assertTrue(new FieldsComparator<>(Model.class, "a", "b").compare(x, y) < 0);
    }

    @Test
    @Disabled
    @DisplayName("排序测试")
    public void sortTest() {
        for (int i = 2; i < 5; i++) {
            Model x = new Model(1, 1);
            Model y = new Model(1, i);

            List<Model> all = ListUtil.of(x, y);
            all = CollUtil.sort(new ArrayList<>(all), new FieldsComparator<>(Model.class, "a", "b"));
            System.out.println(all);
        }
    }

    public static class Model {
        public int a;
        public int b;

        public Model(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "Model{" + "a=" + a + ", b=" + b + '}';
        }
    }
}
