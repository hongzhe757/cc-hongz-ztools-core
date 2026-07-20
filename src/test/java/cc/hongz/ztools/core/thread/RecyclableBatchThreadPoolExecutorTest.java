package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RecyclableBatchThreadPoolExecutor 可召回批处理线程池")
class RecyclableBatchThreadPoolExecutorTest {

	@Test
	@DisplayName("process - 分批处理数据")
	void process() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(2);
		try {
			List<Integer> data = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
			List<Integer> result = executor.process(data, 3, x -> x * 2);
			assertEquals(10, result.size());
			assertTrue(result.containsAll(IntStream.rangeClosed(1, 10).map(x -> x * 2).boxed().collect(Collectors.toList())));
		} finally {
			executor.shutdown();
		}
	}

	@Test
	@DisplayName("process - batchSize < 1 抛异常")
	void processInvalidBatchSize() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(2);
		try {
			assertThrows(IllegalArgumentException.class,
				() -> executor.process(Arrays.asList(1, 2), 0, x -> x));
		} finally {
			executor.shutdown();
		}
	}

	@Test
	@DisplayName("process - 过滤null结果")
	void processFilterNulls() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(2);
		try {
			List<Integer> data = Arrays.asList(1, 2, 3, 4);
			List<Integer> result = executor.process(data, 2, x -> x % 2 == 0 ? x : null);
			assertEquals(2, result.size());
			assertTrue(result.contains(2));
			assertTrue(result.contains(4));
		} finally {
			executor.shutdown();
		}
	}

	@Test
	@DisplayName("processByWarp - 处理Warp数组")
	void processByWarp() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(2);
		try {
			RecyclableBatchThreadPoolExecutor.Warp<String> warp1 = RecyclableBatchThreadPoolExecutor.Warp.of(() -> "result1");
			RecyclableBatchThreadPoolExecutor.Warp<Integer> warp2 = RecyclableBatchThreadPoolExecutor.Warp.of(() -> 42);
			executor.processByWarp(warp1, warp2);
			assertEquals("result1", warp1.get());
			assertEquals(42, warp2.get());
		} finally {
			executor.shutdown();
		}
	}

	@Test
	@DisplayName("getExecutor - 获取线程池")
	void getExecutor() {
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(2);
		assertNotNull(executor.getExecutor());
		assertFalse(executor.getExecutor().isShutdown());
		executor.shutdown();
	}

	@Test
	@DisplayName("构造 - 自定义线程池")
	void constructWithExecutor() {
		java.util.concurrent.ExecutorService custom = java.util.concurrent.Executors.newFixedThreadPool(2);
		RecyclableBatchThreadPoolExecutor executor = new RecyclableBatchThreadPoolExecutor(custom);
		assertSame(custom, executor.getExecutor());
		executor.shutdown();
	}
}
