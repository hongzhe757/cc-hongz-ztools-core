package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.RandomUtil;
import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Base32} 单元测试.
 * <p>
 * 覆盖标准 Base32、Hex 变体及小写解码.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base32 编码")
class Base32Test {

    /**
     * 验证 UTF-8 字符串 Base32 往返编解码.
     */
    @DisplayName("验证 UTF-8 字符串 Base32 往返编解码")
    @Test
    void encodeAndDecode() {
        final String text = "伦家是一个非常长的字符串";
        final String encode = Base32.encode(text);
        assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI======", encode);
        assertEquals(text, Base32.decodeStr(encode));
        assertEquals(text, Base32.decodeStr(encode.toLowerCase()));
    }

    /**
     * 验证 Hex 变体 Base32 往返编解码.
     */
    @DisplayName("验证 Hex 变体 Base32 往返编解码")
    @Test
    void hexEncodeAndDecode() {
        final String text = "伦家是一个非常长的字符串";
        final String encode = Base32.encodeHex(StrUtil.utf8Bytes(text));
        assertEquals("SIUADPDEMRJ9HBV4N20E9E5AT6EPTPDON3KPBFV7JA2EBBCNSUMADP5OM8======", encode);
        assertEquals(text, Base32.decodeStrHex(encode));
        assertEquals(text, Base32.decodeStrHex(encode.toLowerCase()));
    }

    /**
     * 验证随机长度字符串往返编解码.
     */
    @DisplayName("验证随机长度字符串往返编解码")
    @Test
    void encodeAndDecodeRandom() {
        final String text = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER,
                RandomUtil.randomInt(1, 1001));
        assertEquals(text, Base32.decodeStr(Base32.encode(text)));
    }

    /**
     * 验证无 padding 的 Base32 字符串可解码.
     */
    @DisplayName("验证无 padding 的 Base32 字符串可解码")
    @Test
    void decodeWithoutPadding() {
        final String text = "伦家是一个非常长的字符串";
        assertEquals(text, Base32.decodeStr("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI"));
    }
}
