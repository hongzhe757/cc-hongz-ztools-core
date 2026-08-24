package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Base64} 单元测试.
 * <p>
 * 覆盖标准/URL Safe 编解码、padding、GBK 及 {@link Base64#isBase64(CharSequence)} 校验.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base64 编码")
class Base64Test {

    /**
     * 验证 {@link Base64#encode(byte[])} 对 null 返回 null.
     */
    @DisplayName("验证 encode 对 null 返回 null")
    @Test
    void encodeNullReturnsNull() {
        assertNull(Base64.encode((byte[]) null));
    }

    /**
     * 验证字节数组往返编解码.
     */
    @DisplayName("验证字节数组往返编解码")
    @Test
    void encodeBytesRoundTrip() {
        final byte[] raw = "hello".getBytes(StandardCharsets.UTF_8);
        final String encoded = Base64.encode(raw);
        assertEquals("aGVsbG8=", encoded);
        assertArrayEquals(raw, Base64.decode(encoded));
    }

    /**
     * 验证 URL Safe 编码不含 {@code +} 与 {@code /}.
     */
    @DisplayName("验证 URL Safe 编码不含 + 与 /")
    @Test
    void encodeUrlSafe() {
        final byte[] raw = new byte[] {(byte) 0xFB, (byte) 0xEF, (byte) 0xBE};
        final String encoded = Base64.encodeUrlSafe(raw);
        assertFalse(encoded.contains("+"));
        assertFalse(encoded.contains("/"));
    }

    /**
     * 验证 {@link Base64#isBase64(CharSequence)} 对合法 Base64 返回 true.
     */
    @DisplayName("验证 isBase64 对合法 Base64 返回 true")
    @Test
    void isBase64Valid() {
        final String sample = Base64.encode(
                RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER, RandomUtil.randomInt(1, 1001)));
        assertTrue(Base64.isBase64(sample));
    }

    /**
     * 验证 {@link Base64#isBase64(CharSequence)} 对非法 padding 位置返回 false.
     */
    @DisplayName("验证 isBase64 对非法 padding 位置返回 false")
    @Test
    void isBase64InvalidPadding() {
        final String valid = "dW1kb3MzejR3bmljM2J6=djAyZzcwbWk5M213Nnk3cWQ3eDJwOHFuNXJsYmMwaXhxbmg0dmxrcmN0anRkbmd3"
                + "ZzcyZWFwanI2NWNneTg2dnp6cmJoMHQ4MHpxY2R6c3pjazZtaQ=";
        assertFalse(Base64.isBase64(valid));
    }

    /**
     * 验证 UTF-8 字符串编解码.
     */
    @DisplayName("验证 UTF-8 字符串编解码")
    @Test
    void encodeAndDecodeUtf8() {
        final String text = "伦家是一个非常长的字符串66";
        final String encode = Base64.encode(text);
        assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY=", encode);
        assertEquals(text, Base64.decodeStr(encode));
    }

    /**
     * 验证无 padding 编解码.
     */
    @DisplayName("验证无 padding 编解码")
    @Test
    void encodeAndDecodeWithoutPadding() {
        final String text = "伦家是一个非常长的字符串66";
        final String encode = Base64.encodeWithoutPadding(StrUtil.utf8Bytes(text));
        assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY", encode);
        assertEquals(text, Base64.decodeStr(encode));
    }

    /**
     * 验证单字符 {@code :} 编解码.
     */
    @DisplayName("验证单字符编解码")
    @Test
    void encodeAndDecodeSingleChar() {
        final String text = ":";
        assertEquals("Og==", Base64.encode(text));
        assertEquals(text, Base64.decodeStr(Base64.encode(text)));
    }

    /**
     * 验证 GBK 编解码.
     */
    @DisplayName("验证 GBK 编解码")
    @Test
    void encodeAndDecodeGbk() {
        final String text = "订购成功立即生效，30天内可观看专区中除单独计费影片外的所有内容，到期自动取消。";
        final String encoded = Base64.encode(text, "gbk");
        assertEquals(text, Base64.decodeStr(encoded, "gbk"));
    }

    /**
     * 验证 emoji 编解码.
     */
    @DisplayName("验证 emoji 编解码")
    @Test
    void encodeAndDecodeEmoji() {
        final String text = "😄";
        final String encoded = Base64.encode(text);
        assertEquals(text, Base64.decodeStr(encoded));
    }

    /**
     * 验证与 JDK {@link java.util.Base64} 编码结果一致.
     */
    @DisplayName("验证与 JDK Base64 编码结果一致")
    @Test
    void encodeMatchesJdk() {
        final String jdk = java.util.Base64.getEncoder().encodeToString("111".getBytes(StandardCharsets.UTF_8));
        assertEquals(jdk, Base64.encode("111"));
    }
}
