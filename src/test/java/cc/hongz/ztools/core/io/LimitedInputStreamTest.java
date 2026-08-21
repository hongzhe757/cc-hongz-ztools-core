package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link LimitedInputStream} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("LimitedInputStream 限制读取流")
class LimitedInputStreamTest {

    @Test
    @DisplayName("限制读取长度")
    void readLimited() throws Exception {
        final byte[] data = {1, 2, 3, 4, 5};
        try (LimitedInputStream in = new LimitedInputStream(new ByteArrayInputStream(data), 3)) {
            assertEquals(1, in.read());
            assertEquals(2, in.read());
            assertEquals(3, in.read());
            // 超过限制读取，抛出 IllegalStateException
            assertThrows(IllegalStateException.class, in::read);
        }
    }
}
