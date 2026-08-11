package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalPortGenerator 单元测试.
 */
class LocalPortGeneratorTest {

    @Test
    @DisplayName("生成可用本地端口")
    void generateTest() {
        final LocalPortGenerator generator = new LocalPortGenerator(10000);
        final int port = generator.generate();
        assertTrue(port >= 10000);
        assertTrue(NetUtil.isValidPort(port));
    }

    @Test
    @DisplayName("多次调用端口递增测试")
    void generateMultipleTest() {
        final LocalPortGenerator generator = new LocalPortGenerator(20000);
        final int port1 = generator.generate();
        final int port2 = generator.generate();
        assertTrue(port2 >= port1);
    }

    @Test
    @DisplayName("旧类名 LocalPortGenerater 兼容性测试")
    void deprecatedGeneraterTest() {
        final LocalPortGenerater generater = new LocalPortGenerater(30000);
        final int port = generater.generate();
        assertTrue(port >= 30000);
    }
}
