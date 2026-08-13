package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WeightRandom 单元测试.
 */
public class WeightRandomTest {

    @Test
    @DisplayName("next 随机返回权重对象")
    public void weightRandomTest() {
        final WeightRandom<String> random = WeightRandom.create();
        random.add("A", 10);
        random.add("B", 50);
        random.add("C", 100);

        final String result = random.next();
        assertTrue(new ArrayList<>(List.of("A", "B", "C")).contains(result));
    }

    @Test
    @DisplayName("空权重表 next 返回 null")
    public void emptyNextTest() {
        final WeightRandom<String> random = WeightRandom.create();
        assertNull(random.next());
    }

    @Test
    @DisplayName("clear 清空后 next 返回 null")
    public void clearTest() {
        final WeightRandom<String> random = WeightRandom.create();
        random.add("A", 10);
        random.clear();
        assertNull(random.next());
    }

    @Test
    @DisplayName("权重 <= 0 不加入")
    public void nonPositiveWeightTest() {
        final WeightRandom<String> random = WeightRandom.create();
        random.add("A", 0);
        random.add("B", -5);
        assertNull(random.next());
    }

    @Test
    @DisplayName("WeightObj 构造器 add")
    public void weightObjConstructorTest() {
        final WeightRandom<String> random =
                new WeightRandom<>(new WeightRandom.WeightObj<>("A", 10));
        assertEquals("A", random.next());
    }

    @Test
    @DisplayName("Iterable 构造器")
    public void iterableConstructorTest() {
        final List<WeightRandom.WeightObj<String>> objs = new ArrayList<>();
        objs.add(new WeightRandom.WeightObj<>("A", 10));
        objs.add(new WeightRandom.WeightObj<>("B", 20));
        final WeightRandom<String> random = new WeightRandom<>(objs);
        final String result = random.next();
        assertTrue(new ArrayList<>(List.of("A", "B")).contains(result));
    }

    @Test
    @DisplayName("WeightObj getter/setter")
    public void weightObjTest() {
        final WeightRandom.WeightObj<String> obj =
                new WeightRandom.WeightObj<>("A", 10);
        assertEquals("A", obj.getObj());
        assertEquals(10.0, obj.getWeight());
        obj.setObj("B");
        assertEquals("B", obj.getObj());
    }

    @Test
    @DisplayName("WeightObj equals/hashCode")
    public void weightObjEqualsTest() {
        final WeightRandom.WeightObj<String> obj1 =
                new WeightRandom.WeightObj<>("A", 10);
        final WeightRandom.WeightObj<String> obj2 =
                new WeightRandom.WeightObj<>("A", 10);
        final WeightRandom.WeightObj<String> obj3 =
                new WeightRandom.WeightObj<>("B", 10);
        assertEquals(obj1, obj2);
        assertNotEquals(obj1, obj3);
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }
}
