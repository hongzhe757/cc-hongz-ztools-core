package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Caesar} 单元测试.
 * <p>
 * 覆盖指定偏移量的凯撒编解码.
 *
 * @author hongzhe.qin
 */
@DisplayName("凯撒密码")
class CaesarTest {

    /**
     * 验证偏移 3 的凯撒编解码往返.
     */
    @DisplayName("验证偏移 3 的凯撒编解码往返")
    @Test
    void encodeAndDecodeOffset3() {
        final String plain = "1f2e9df6131b480b9fdddc633cf24996";
        final String encoded = Caesar.encode(plain, 3);
        assertEquals("1H2G9FH6131D480D9HFFFE633EH24996", encoded);
        assertEquals(plain, Caesar.decode(encoded, 3));
    }
}
