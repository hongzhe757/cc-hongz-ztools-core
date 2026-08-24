package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BoundedPriorityQueue 单元测试.
 */
@DisplayName("BoundedPriorityQueue 有界优先队列")
class BoundedPriorityQueueTest {

    @Test
    @DisplayName("基本入队")
    void basicOrder() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(3);
        queue.offer(1);
        queue.offer(5);
        queue.offer(3);
        assertEquals(3, queue.size());
    }

    @Test
    @DisplayName("容量限制淘汰")
    void boundedCapacity() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        // 容量为3，第4个元素会淘汰最小的
        assertEquals(3, queue.size());
    }

    @Test
    @DisplayName("转为有序列表")
    void toList() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(5, Comparator.naturalOrder());
        queue.offer(3);
        queue.offer(1);
        queue.offer(2);
        List<Integer> list = queue.toList();
        assertEquals(3, list.size());
        // 排序后应为升序
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    @DisplayName("数组批量入队")
    void addAllArray() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(10);
        queue.addAll(new Integer[]{1, 2, 3, 4, 5});
        assertEquals(5, queue.size());
    }

    @Test
    @DisplayName("迭代器遍历")
    void iterator() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(5, Comparator.naturalOrder());
        queue.offer(3);
        queue.offer(1);
        queue.offer(2);
        List<Integer> list = new ArrayList<>();
        queue.iterator().forEachRemaining(list::add);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    @DisplayName("自定义比较器降序")
    void withComparator() {
        BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(3, Comparator.reverseOrder());
        queue.offer(1);
        queue.offer(5);
        queue.offer(3);
        List<Integer> list = queue.toList();
        // reverse comparator → 降序
        assertEquals(5, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(1, list.get(2));
    }

    @Test
    @DisplayName("默认自然排序")
    void defaultComparable() {
        // 不传comparator，使用自然排序
        BoundedPriorityQueue<String> queue = new BoundedPriorityQueue<>(3);
        queue.offer("c");
        queue.offer("a");
        queue.offer("b");
        List<String> list = queue.toList();
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }
}
