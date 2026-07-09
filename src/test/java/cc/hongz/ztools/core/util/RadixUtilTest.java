package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link RadixUtil} 单元测试.
 * <p>
 * 覆盖自定义进制 encode/decode 往返、预设字符表（34/59 及打乱版）及非法输入校验.
 *
 * @author hongzhe.qin
 */
@DisplayName("RadixUtil 任意进制编解码")
class RadixUtilTest {

    /**
     * 验证文档示例：{@code encode("AB", 10)} 为 2 进制 {@code "BABA"}，往返解码为 10.
     */
    @Test
    @DisplayName("自定义 2 进制：encode(\"AB\", 10) → BABA")
    void encodeBinaryExampleFromDoc() {
        assertEquals("BABA", RadixUtil.encode("AB", 10));
        assertEquals(10, RadixUtil.decodeToInt("AB", "BABA"));
    }

    /**
     * 验证文档示例：{@code encode("VIP", 21)} 为 3 进制 {@code "PIV"}，往返解码为 21.
     */
    @Test
    @DisplayName("自定义 3 进制：encode(\"VIP\", 21) → PIV")
    void encodeTernaryExampleFromDoc() {
        assertEquals("PIV", RadixUtil.encode("VIP", 21));
        assertEquals(21, RadixUtil.decodeToInt("VIP", "PIV"));
    }

    /**
     * 验证 {@link RadixUtil#encode(String, int)} 与 {@link RadixUtil#decodeToInt(String, String)} 往返一致.
     */
    @Test
    @DisplayName("int 值 encode → decodeToInt 往返")
    void encodeDecodeIntRoundTrip() {
        final String radixs = "AB";
        final int num = 10;
        final String encoded = RadixUtil.encode(radixs, num);
        assertEquals(num, RadixUtil.decodeToInt(radixs, encoded));
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_34} 基数为 34（10 位数字 + 24 位大写字母去 I/O）.
     */
    @Test
    @DisplayName("RADIXS_34 字符表长度 = 34")
    void radixs34CharsetLength() {
        assertEquals(34, RadixUtil.RADIXS_34.length());
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_59} 基数为 59（10 数字 + 25 小写去 l + 24 大写去 I/O）.
     */
    @Test
    @DisplayName("RADIXS_59 字符表长度 = 59")
    void radixs59CharsetLength() {
        assertEquals(59, RadixUtil.RADIXS_59.length());
    }

    /**
     * 验证打乱版与标准版字符集合相同、仅顺序不同（基数不变）.
     */
    @Test
    @DisplayName("打乱版与标准版字符集合一致")
    void shufflePreservesCharsetSize() {
        assertEquals(RadixUtil.RADIXS_34.length(), RadixUtil.RADIXS_SHUFFLE_34.length());
        assertEquals(RadixUtil.RADIXS_59.length(), RadixUtil.RADIXS_SHUFFLE_59.length());
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_34} 邀请码场景：长 ID 编码后比十进制更短且可往返.
     */
    @Test
    @DisplayName("RADIXS_34：邀请码式往返")
    void radixs34InviteCodeRoundTrip() {
        final long userId = 9_876_543L;
        final String code = RadixUtil.encode(RadixUtil.RADIXS_34, userId);
        assertTrue(code.length() < String.valueOf(userId).length());
        assertEquals(userId, RadixUtil.decode(RadixUtil.RADIXS_34, code));
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_SHUFFLE_34} 与标准 34 进制对同一 ID 产生不同码（打乱防规律）.
     */
    @Test
    @DisplayName("RADIXS_SHUFFLE_34：同 ID 与标准 34 进制结果不同")
    void radixsShuffle34DiffersFromStandard() {
        final long id = 1001L;
        final String standard = RadixUtil.encode(RadixUtil.RADIXS_34, id);
        final String shuffled = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_34, id);
        assertNotEquals(standard, shuffled);
        assertEquals(id, RadixUtil.decode(RadixUtil.RADIXS_SHUFFLE_34, shuffled));
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_59} 短链场景：同等数值编码不长于 34 进制.
     */
    @Test
    @DisplayName("RADIXS_59：短链编码往返且不长于 34 进制")
    void radixs59ShortLinkRoundTrip() {
        final long articleId = 12345L;
        final String code59 = RadixUtil.encode(RadixUtil.RADIXS_59, articleId);
        final String code34 = RadixUtil.encode(RadixUtil.RADIXS_34, articleId);
        assertTrue(code59.length() <= code34.length());
        assertEquals(articleId, RadixUtil.decode(RadixUtil.RADIXS_59, code59));
    }

    /**
     * 验证 {@link RadixUtil#RADIXS_SHUFFLE_59} 打乱 59 进制往返一致.
     */
    @Test
    @DisplayName("RADIXS_SHUFFLE_59：打乱 59 进制往返")
    void radixsShuffle59RoundTrip() {
        final long id = 99_999_999L;
        final String code = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_59, id);
        assertEquals(id, RadixUtil.decode(RadixUtil.RADIXS_SHUFFLE_59, code));
    }

    /**
     * 验证连续自增 ID 在打乱字符表下编码结果不相邻重复（防一眼猜规律）.
     */
    @Test
    @DisplayName("连续 ID 打乱编码结果互不相同")
    void consecutiveIdsProduceDistinctShuffleCodes() {
        final String a = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_34, 1000L);
        final String b = RadixUtil.encode(RadixUtil.RADIXS_SHUFFLE_34, 1001L);
        assertNotEquals(a, b);
    }

    /**
     * 验证 {@link RadixUtil#encode(String, long)} 对负数抛出异常.
     */
    @Test
    @DisplayName("encode(long) 负数应抛异常")
    void encodeLongNegativeThrows() {
        assertThrows(RuntimeException.class, () -> RadixUtil.encode(RadixUtil.RADIXS_34, -1L));
    }

    /**
     * 验证 decode 遇到不在 radix 字符集中的字符时抛出 {@link IllegalArgumentException}.
     */
    @Test
    @DisplayName("decode 非法字符应抛 IllegalArgumentException")
    void decodeIllegalCharacter() {
        final String radixs = "0123456789ABC";
        final String bad = "1X3";
        assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode(radixs, bad));
    }

    /**
     * 验证 radix 字符集长度不足 2 时 decode 抛出异常.
     */
    @Test
    @DisplayName("radix 长度不足应抛 IllegalArgumentException")
    void decodeRadixTooShort() {
        assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode("A", "A"));
    }

    /**
     * 验证 decode 对 null 或空编码串抛出异常.
     */
    @Test
    @DisplayName("decode 空编码串应抛 IllegalArgumentException")
    void decodeEmptyEncodeStr() {
        assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode(RadixUtil.RADIXS_34, ""));
        assertThrows(IllegalArgumentException.class, () -> RadixUtil.decode(RadixUtil.RADIXS_34, null));
    }
}
