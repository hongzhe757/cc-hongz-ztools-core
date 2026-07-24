package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ConcurrentModificationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * resource 子包缺失测试
 * 覆盖：InputStreamResource、MultiResource
 */
class ResourceMiscTest {

	// ============================== InputStreamResource ==============================

	/**
	 * 测试 InputStreamResource 基本功能
	 */
	@Test
	void inputStreamResourceBasicTest() {
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		InputStream in = new ByteArrayInputStream(data);
		InputStreamResource resource = new InputStreamResource(in, "test.txt");

		assertEquals("test.txt", resource.getName());
		assertNull(resource.getUrl());
		assertSame(in, resource.getStream());
	}

	/**
	 * 测试 InputStreamResource 无名称构造
	 */
	@Test
	void inputStreamResourceNoNameTest() {
		InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});
		InputStreamResource resource = new InputStreamResource(in);

		assertNull(resource.getName());
		assertNull(resource.getUrl());
	}

	/**
	 * 测试 InputStreamResource 读取流内容
	 */
	@Test
	void inputStreamResourceReadStreamTest() throws IOException {
		byte[] data = "TestData".getBytes(StandardCharsets.UTF_8);
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(data));

		byte[] result = resource.getStream().readAllBytes();
		assertArrayEquals(data, result);
	}

	// ============================== MultiResource ==============================

	/**
	 * 测试 MultiResource 构造和迭代
	 */
	@Test
	void multiResourceIteratorTest() {
		StringResource r1 = new StringResource("AAA");
		StringResource r2 = new StringResource("BBB");
		MultiResource multi = new MultiResource(r1, r2);

		assertTrue(multi.hasNext());
		int count = 0;
		for (Resource r : multi) {
			assertNotNull(r);
			count++;
		}
		assertEquals(2, count);
	}

	/**
	 * 测试 MultiResource next 和 getName
	 */
	@Test
	void multiResourceNextTest() {
		StringResource r1 = new StringResource("AAA", "res1");
		StringResource r2 = new StringResource("BBB", "res2");
		MultiResource multi = new MultiResource(r1, r2);

		multi.next();
		assertEquals("res1", multi.getName());
		multi.next();
		assertEquals("res2", multi.getName());
	}

	/**
	 * 测试 MultiResource reset
	 */
	@Test
	void multiResourceResetTest() {
		StringResource r1 = new StringResource("AAA", "res1");
		StringResource r2 = new StringResource("BBB", "res2");
		MultiResource multi = new MultiResource(r1, r2);

		// 使用 next 遍历
		multi.next();
		assertEquals("res1", multi.getName());
		multi.next();
		assertEquals("res2", multi.getName());

		// 重置游标后重新遍历
		multi.reset();
		multi.next();
		assertEquals("res1", multi.getName());
	}

	/**
	 * 测试 MultiResource add 方法
	 */
	@Test
	void multiResourceAddTest() {
		StringResource r1 = new StringResource("AAA");
		MultiResource multi = new MultiResource(r1);

		StringResource r2 = new StringResource("BBB");
		multi.add(r2);

		int count = 0;
		for (Resource r : multi) {
			count++;
		}
		assertEquals(2, count);
	}

	/**
	 * 测试 MultiResource 迭代器遍历
	 */
	@Test
	void multiResourceIteratorTraverseTest() {
		StringResource r1 = new StringResource("AAA");
		StringResource r2 = new StringResource("BBB");
		MultiResource multi = new MultiResource(r1, r2);

		// 通过 iterator() 遍历
		int count = 0;
		for (Resource r : multi) {
			assertNotNull(r);
			count++;
		}
		assertEquals(2, count);
	}
}
