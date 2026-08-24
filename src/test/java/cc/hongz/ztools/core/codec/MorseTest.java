package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Morse} 单元测试.
 * <p>
 * 覆盖英文、中文及日文摩尔斯编解码.
 *
 * @author hongzhe.qin
 */
@DisplayName("摩尔斯电码")
class MorseTest {

    private final Morse morseCoder = new Morse();

    /**
     * 验证英文文本摩尔斯编解码（解码为大写）.
     */
    @DisplayName("英文文本摩尔斯编解码")
    @Test
    void encodeDecodeEnglish() {
        final String text = "Hello World!";
        final String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
        assertEquals(morse, morseCoder.encode(text));
        assertEquals(text.toUpperCase(), morseCoder.decode(morse));
    }

    /**
     * 验证中文文本摩尔斯编解码.
     */
    @DisplayName("中文文本摩尔斯编解码")
    @Test
    void encodeDecodeChinese() {
        final String text = "你好，世界！";
        final String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
        assertEquals(morse, morseCoder.encode(text));
        assertEquals(text, morseCoder.decode(morse));
    }

    /**
     * 验证日文文本摩尔斯编解码.
     */
    @DisplayName("日文文本摩尔斯编解码")
    @Test
    void encodeDecodeJapanese() {
        final String text = "こんにちは";
        final String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
        assertEquals(morse, morseCoder.encode(text));
        assertEquals(text, morseCoder.decode(morse));
    }
}
