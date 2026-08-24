package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.util.StrUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * {@link Base62Codec} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Base62 编码器")
class Base62CodecTest {

    /**
     * 验证标准 Base62 字节往返.
     */
    @DisplayName("验证标准 Base62 字节往返")
    @Test
    void standardRoundTrip() {
        final byte[] raw = "Base62 raw bytes".getBytes(StandardCharsets.UTF_8);
        final byte[] encoded = Base62Codec.INSTANCE.encode(raw);
        assertArrayEquals(raw, Base62Codec.INSTANCE.decode(encoded));
    }

    /**
     * 验证倒序字母表往返.
     */
    @DisplayName("验证倒序字母表往返")
    @Test
    void invertedRoundTrip() {
        final byte[] raw = new byte[] {0, 1, 2, 127, (byte) 255};
        final byte[] encoded = Base62Codec.INSTANCE.encode(raw, true);
        assertArrayEquals(raw, Base62Codec.INSTANCE.decode(encoded, true));
    }

    /**
     * 验证与 {@link Base62} 门面字符串解码一致.
     */
    @DisplayName("验证与 Base62 门面字符串解码一致")
    @Test
    void matchesFacadeStr() {
        final String text = "facade62";
        final String encoded = Base62.encode(text);
        final byte[] decoded = Base62Codec.INSTANCE.decode(StrUtil.utf8Bytes(encoded));
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), decoded);
    }
}
