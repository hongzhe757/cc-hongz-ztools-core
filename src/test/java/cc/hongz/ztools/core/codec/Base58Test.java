package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Base58} 单元测试.
 * <p>
 * 覆盖标准编解码及带版本校验位的 Checked 模式.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base58 编码")
class Base58Test {

    /**
     * 验证标准 Base58 编解码.
     */
    @DisplayName("验证标准 Base58 编解码")
    @Test
    void encodeAndDecode() {
        final byte[] raw = "hello world".getBytes(StandardCharsets.UTF_8);
        assertEquals("StV1DL6CwTryKyV", Base58.encode(raw));
        assertArrayEquals(raw, Base58.decode("StV1DL6CwTryKyV"));
    }

    /**
     * 验证带版本前缀的 Checked 编码.
     */
    @DisplayName("验证带版本前缀的 Checked 编码")
    @Test
    void encodeCheckedWithVersion() {
        final byte[] raw = "hello world".getBytes(StandardCharsets.UTF_8);
        assertEquals("13vQB7B6MrGQZaxCuFg4oh", Base58.encodeChecked(0, raw));
    }

    /**
     * 验证无版本位的 Checked 编码.
     */
    @DisplayName("验证无版本位的 Checked 编码")
    @Test
    void encodeCheckedWithoutVersion() {
        final byte[] raw = "hello world".getBytes(StandardCharsets.UTF_8);
        assertEquals("3vQB7B6MrGQZaxCuFg4oh", Base58.encodeChecked(null, raw));
    }

    /**
     * 验证 Checked 解码（含与不含版本前缀）.
     */
    @DisplayName("验证 Checked 解码")
    @Test
    void decodeChecked() {
        final byte[] expected = "hello world".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, Base58.decodeChecked("13vQB7B6MrGQZaxCuFg4oh"));
        assertArrayEquals(expected, Base58.decodeChecked("3vQB7B6MrGQZaxCuFg4oh"));
    }
}
