package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BoundedPriorityQueue 单元测试.
 */
class BoundedPriorityQueueTest {

	@Test
	void basicOrder() {
		BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(3);
		queue.offer(1);
		queue.offer(5);
		queue.offer(3);
		assertEquals(3, queue.size());
	}

	@Test
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
	void addAllArray() {
		BoundedPriorityQueue<Integer> queue = new BoundedPriorityQueue<>(10);
		queue.addAll(new Integer[]{1, 2, 3, 4, 5});
		assertEquals(5, queue.size());
	}

	@Test
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
