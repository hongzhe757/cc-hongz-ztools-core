package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link NullOutputStream} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("NullOutputStream 空输出流")
class NullOutputStreamTest {

    @Test
    @DisplayName("write 不抛异常")
    void writeNoOp() {
        final NullOutputStream out = NullOutputStream.NULL_OUTPUT_STREAM;
        assertDoesNotThrow(() -> out.write(new byte[]{1, 2, 3}));
        assertDoesNotThrow(() -> out.write(1));
        assertDoesNotThrow(() -> out.write(new byte[]{1}, 0, 1));
    }

    @Test
    @DisplayName("单例非空")
    void singleton() {
        assertNotNull(NullOutputStream.NULL_OUTPUT_STREAM);
    }
}
