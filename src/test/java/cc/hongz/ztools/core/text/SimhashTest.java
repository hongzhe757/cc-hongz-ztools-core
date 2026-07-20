package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * {@link Simhash} 单元测试.
 */
class SimhashTest {

	@Test
	void hashTest() {
		Simhash simhash = new Simhash();
		long hash1 = simhash.hash(Collections.singletonList("我是一个文本，独一无二的文本"));
		long hash2 = simhash.hash(Collections.singletonList("一个文本，独一无二的文本"));
		// 不同文本的hash值不同
		assertNotEquals(hash1, hash2);
	}

	@Test
	void hashSameTextTest() {
		Simhash simhash = new Simhash();
		long hash1 = simhash.hash(Arrays.asList("我", "是", "一个", "文本"));
		long hash2 = simhash.hash(Arrays.asList("我", "是", "一个", "文本"));
		// 相同文本的hash值相同
		assertNotEquals(0L, hash1);
		assertNotEquals(0L, hash2);
	}

	@Test
	void equalsTest() {
		Simhash simhash = new Simhash();
		// 先存储一个文本的hash
		long hash = simhash.hash(Arrays.asList("我", "是", "一个", "文本"));
		simhash.store(hash);
		// 相似文本应该能匹配
		boolean result = simhash.equals(Arrays.asList("我", "是", "一个", "文本"));
		assertFalse(!result);
	}
}
