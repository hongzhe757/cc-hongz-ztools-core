package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Rot} 单元测试.
 * <p>
 * 覆盖 ROT13 编解码.
 *
 * @author hongzhe.qin
 */
class RotTest {

    /**
     * 验证 ROT13 往返编解码.
     */
    @Test
    void rot13RoundTrip() {
        final String plain = "1f2e9df6131b480b9fdddc633cf24996";
        final String encoded = Rot.encode13(plain);
        assertEquals("4s5r2qs9464o713o2sqqqp966ps57229", encoded);
        assertEquals(plain, Rot.decode13(encoded));
    }
}
