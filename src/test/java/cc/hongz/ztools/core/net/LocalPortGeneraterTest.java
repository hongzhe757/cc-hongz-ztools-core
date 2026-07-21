package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalPortGenerater 单元测试
 */
class LocalPortGeneraterTest {

	@Test
	void generateTest() {
		LocalPortGenerater generater = new LocalPortGenerater(10000);
		int port = generater.generate();
		assertTrue(port >= 10000);
		assertTrue(NetUtil.isValidPort(port));
	}

	@Test
	void generateMultipleTest() {
		LocalPortGenerater generater = new LocalPortGenerater(20000);
		int port1 = generater.generate();
		int port2 = generater.generate();
		// 第二次调用应返回 >= 第一次的值（递增）
		assertTrue(port2 >= port1);
	}
}
