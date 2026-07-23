package cc.hongz.ztools.core.lang.loader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link AtomicLoader} 单元测试.
 */
class AtomicLoaderTest {

	@Test
	void testGetReturnsInitializedObject() {
		AtomicLoader<String> loader = new AtomicLoader<String>() {
			@Override
			protected String init() {
				return "loaded";
			}
		};

		assertEquals("loaded", loader.get());
	}

	@Test
	void testGetReturnsSameObjectOnMultipleCalls() {
		AtomicInteger initCount = new AtomicInteger(0);
		AtomicLoader<StringBuilder> loader = new AtomicLoader<StringBuilder>() {
			@Override
			protected StringBuilder init() {
				initCount.incrementAndGet();
				return new StringBuilder("hello");
			}
		};

		StringBuilder first = loader.get();
		StringBuilder second = loader.get();
		assertSame(first, second);
		// AtomicLoader 在并发场景下 init 可能被多次调用，但单线程下只调用一次
		assertEquals(1, initCount.get());
	}

	@Test
	void testConcurrentGet() throws InterruptedException {
		AtomicInteger initCount = new AtomicInteger(0);
		AtomicLoader<String> loader = new AtomicLoader<String>() {
			@Override
			protected String init() {
				initCount.incrementAndGet();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				return "concurrent-result";
			}
		};

		final String[] results = new String[10];
		Thread[] threads = new Thread[10];
		for (int i = 0; i < 10; i++) {
			final int index = i;
			threads[i] = new Thread(() -> {
				results[index] = loader.get();
			});
			threads[i].start();
		}
		for (Thread t : threads) {
			t.join();
		}

		// 所有线程应获得相同结果
		for (String result : results) {
			assertEquals("concurrent-result", result);
		}
	}
}
