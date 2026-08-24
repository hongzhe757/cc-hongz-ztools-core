package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.RandomUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Base62} 单元测试.
 * <p>
 * 覆盖标准与倒序字母表两种编解码模式.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base62 编码")
class Base62Test {

    /**
     * 验证标准 Base62 往返编解码.
     */
    @DisplayName("验证标准 Base62 往返编解码")
    @Test
    void encodeAndDecode() {
        final String text = "伦家是一个非常长的字符串66";
        final String encode = Base62.encode(text);
        assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);
        assertEquals(text, Base62.decodeStr(encode));
    }

    /**
     * 验证倒序字母表 Base62 往返编解码.
     */
    @DisplayName("验证倒序字母表 Base62 往返编解码")
    @Test
    void encodeAndDecodeInverted() {
        final String text = "伦家是一个非常长的字符串66";
        final String encode = Base62.encodeInverted(text);
        assertEquals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);
        assertEquals(text, Base62.decodeStrInverted(encode));
    }

    /**
     * 验证随机字符串标准模式往返编解码.
     */
    @DisplayName("验证随机字符串标准模式往返编解码")
    @Test
    void encodeAndDecodeRandom() {
        final String text = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER,
                RandomUtil.randomInt(1, 1001));
        assertEquals(text, Base62.decodeStr(Base62.encode(text)));
    }

    /**
     * 验证随机字符串倒序模式往返编解码.
     */
    @DisplayName("验证随机字符串倒序模式往返编解码")
    @Test
    void encodeAndDecodeInvertedRandom() {
        final String text = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER,
                RandomUtil.randomInt(1, 1001));
        assertEquals(text, Base62.decodeStrInverted(Base62.encodeInverted(text)));
    }
}
