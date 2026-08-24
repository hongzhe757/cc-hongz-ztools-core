package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Hashids} 单元测试.
 * <p>
 * 覆盖 Hex 字符串与 Hashids 短码互转.
 *
 * @author hongzhe.qin
 */
@DisplayName("Hashids 编码")
class HashidsTest {

    /**
     * 验证 Hex 编码与解码（含 {@code 0x} / {@code 0X} 前缀）.
     */
    @DisplayName("Hex 编码与解码（含 0x/0X 前缀）")
    @Test
    void hexEncodeDecode() {
        final Hashids hashids = Hashids.create("my awesome salt".toCharArray());
        final String encoded1 = hashids.encodeFromHex("507f1f77bcf86cd799439011");
        final String encoded2 = hashids.encodeFromHex("0x507f1f77bcf86cd799439011");
        final String encoded3 = hashids.encodeFromHex("0X507f1f77bcf86cd799439011");

        assertEquals("R2qnd2vkOJTXm7XV7yq4", encoded1);
        assertEquals(encoded1, encoded2);
        assertEquals(encoded1, encoded3);
        assertEquals("507f1f77bcf86cd799439011", hashids.decodeToHex(encoded1));
    }
}
