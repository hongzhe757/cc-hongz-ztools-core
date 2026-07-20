package cc.hongz.ztools.core.stream;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SimpleCollector} 单元测试.
 */
class SimpleCollectorTest {

	@Test
	void fiveArgsConstructorTest() {
		// 使用5参数构造方法：将字符串拼接为一个大字符串
		Collector<String, StringBuilder, String> collector = new SimpleCollector<>(
			StringBuilder::new,
			(sb, s) -> sb.append(s),
			(sb1, sb2) -> sb1.append(sb2),
			StringBuilder::toString,
			Set.of()
		);

		String result = Stream.of("a", "b", "c").collect(collector);
		assertEquals("abc", result);
	}

	@Test
	void fourArgsConstructorTest() {
		// 使用4参数构造方法：finisher为identity，结果容器类型即最终类型
		Collector<String, List<String>, List<String>> collector = new SimpleCollector<>(
			ArrayList::new,
			List::add,
			(l1, l2) -> { l1.addAll(l2); return l1; },
			Set.of()
		);

		List<String> result = Stream.of("a", "b", "c").collect(collector);
		assertEquals(List.of("a", "b", "c"), result);
	}

	@Test
	void accumulatorTest() {
		Collector<Integer, List<Integer>, List<Integer>> collector = new SimpleCollector<>(
			ArrayList::new,
			List::add,
			(l1, l2) -> { l1.addAll(l2); return l1; },
			Set.of()
		);

		List<Integer> container = collector.supplier().get();
		collector.accumulator().accept(container, 1);
		collector.accumulator().accept(container, 2);
		assertEquals(List.of(1, 2), container);
	}

	@Test
	void combinerTest() {
		Collector<Integer, List<Integer>, List<Integer>> collector = new SimpleCollector<>(
			ArrayList::new,
			List::add,
			(l1, l2) -> { l1.addAll(l2); return l1; },
			Set.of()
		);

		List<Integer> list1 = new ArrayList<>(List.of(1, 2));
		List<Integer> list2 = new ArrayList<>(List.of(3, 4));
		List<Integer> combined = collector.combiner().apply(list1, list2);
		assertEquals(List.of(1, 2, 3, 4), combined);
	}

	@Test
	void finisherTest() {
		Collector<String, StringBuilder, String> collector = new SimpleCollector<>(
			StringBuilder::new,
			(sb, s) -> sb.append(s),
			(sb1, sb2) -> sb1.append(sb2),
			StringBuilder::toString,
			Set.of()
		);

		StringBuilder sb = new StringBuilder("hello");
		String result = collector.finisher().apply(sb);
		assertEquals("hello", result);
	}

	@Test
	void characteristicsTest() {
		Set<Collector.Characteristics> chars = Set.of(Collector.Characteristics.UNORDERED);
		Collector<String, List<String>, List<String>> collector = new SimpleCollector<>(
			ArrayList::new,
			List::add,
			(l1, l2) -> { l1.addAll(l2); return l1; },
			chars
		);

		assertEquals(chars, collector.characteristics());
	}

	@Test
	void fourArgsConstructorFinisherIsIdentityTest() {
		// 4参数构造方法的finisher应该是identity函数
		Collector<String, List<String>, List<String>> collector = new SimpleCollector<>(
			ArrayList::new,
			List::add,
			(l1, l2) -> { l1.addAll(l2); return l1; },
			Set.of()
		);

		List<String> input = new ArrayList<>(List.of("x"));
		// finisher应该直接返回输入（identity）
		assertSame(input, collector.finisher().apply(input));
	}
}
