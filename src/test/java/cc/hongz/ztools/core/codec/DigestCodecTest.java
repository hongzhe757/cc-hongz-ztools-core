package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link DigestCodec} 单元测试.
 *
 * @author hongzhe.qin
 */
class DigestCodecTest {

    /**
     * 验证 {@link DigestCodec#md5Hex(byte[])} 对 null 返回 null.
     */
    @Test
    void md5NullReturnsNull() {
        assertNull(DigestCodec.md5Hex((byte[]) null));
        assertNull(DigestCodec.md5Hex((CharSequence) null));
    }

    /**
     * 验证空串 MD5 Hex.
     */
    @Test
    void md5Empty() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", DigestCodec.md5Hex(""));
    }

    /**
     * 验证 {@code hello} MD5 Hex.
     */
    @Test
    void md5Hello() {
        assertEquals("5d41402abc4b2a76b9719d911017c592", DigestCodec.md5Hex("hello"));
    }

    /**
     * 验证 {@code hello} SHA-256 Hex.
     */
    @Test
    void sha256Hello() {
        assertEquals(
                "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
                DigestCodec.sha256Hex("hello"));
    }

    /**
     * 验证字节数组 SHA-256 长度为 64 位 Hex.
     */
    @Test
    void sha256Bytes() {
        final byte[] raw = {0x01, 0x02, 0x03};
        assertEquals(64, DigestCodec.sha256Hex(raw).length());
        assertEquals(DigestCodec.digestHex("SHA-256", raw), DigestCodec.sha256Hex(raw));
    }
}
