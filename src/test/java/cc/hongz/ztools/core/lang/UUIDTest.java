package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UUID} 单元测试.
 * <p>
 * 覆盖 fastUUID/randomUUID 的格式、版本与变体，nameUUIDFromBytes 的确定性，
 * fromString 往返，toString 长度，equals/hashCode/compareTo，
 * 以及非时间 UUID 调用时间访问器抛出异常.
 *
 * @author hongzhe.qin
 */
@DisplayName("UUID 生成")
class UUIDTest {

    /**
     * 验证 {@link UUID#fastUUID()} 简单模式为 32 位无连字符十六进制串.
     */
    @Test
    @DisplayName("fastUUID 简单模式格式")
    void fastUuidSimpleFormat() {
        final String uuid = UUID.fastUUID().toString(true);
        assertEquals(32, uuid.length());
        assertTrue(uuid.matches("[0-9a-f]{32}"));
    }

    /**
     * 验证连续生成的 fastUUID 字符串在小样本内不重复.
     */
    @Test
    @DisplayName("fastUUID 小样本唯一性")
    void fastUuidUniqueInSample() {
        final Set<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(UUID.fastUUID().toString());
        }
        assertEquals(100, set.size());
    }

    /**
     * 验证 {@link UUID#randomUUID()} 与 {@link UUID#fastUUID()} 单次调用结果不同.
     */
    @Test
    @DisplayName("randomUUID 与 fastUUID 实例不同")
    void randomAndFastDiffer() {
        assertNotEquals(UUID.randomUUID().toString(), UUID.fastUUID().toString());
    }

    /**
     * 验证 {@link UUID#randomUUID()} 为 version 4（随机生成）且为 IETF 变体.
     */
    @Test
    @DisplayName("randomUUID 版本与变体")
    void randomUuidVersionAndVariant() {
        final UUID uuid = UUID.randomUUID();
        assertEquals(4, uuid.version());
        assertEquals(2, uuid.variant());
    }

    /**
     * 验证 {@link UUID#nameUUIDFromBytes(byte[])} 为 version 3 且同输入结果确定.
     */
    @Test
    @DisplayName("nameUUIDFromBytes 版本与确定性")
    void nameUuidVersionAndDeterministic() {
        final byte[] name = "hutool".getBytes(StandardCharsets.UTF_8);
        final UUID uuid = UUID.nameUUIDFromBytes(name);
        assertEquals(3, uuid.version());
        assertEquals(2, uuid.variant());
        assertEquals(uuid, UUID.nameUUIDFromBytes(name));
    }

    /**
     * 验证 {@link UUID#fromString(String)} 解析后经 {@link UUID#toString()} 能还原为原串.
     */
    @Test
    @DisplayName("fromString 与 toString 往返一致")
    void fromStringRoundtrip() {
        final String str = "123e4567-e89b-12d3-a456-426614174000";
        final UUID uuid = UUID.fromString(str);
        assertEquals(str, uuid.toString());
    }

    /**
     * 验证完整模式为 36 位、简单模式为 32 位.
     */
    @Test
    @DisplayName("toString 完整/简单模式长度")
    void toStringLength() {
        final UUID uuid = UUID.randomUUID();
        assertEquals(36, uuid.toString().length());
        assertEquals(32, uuid.toString(true).length());
    }

    /**
     * 验证相同位值的 UUID 相等且哈希码一致，compareTo 返回 0.
     */
    @Test
    @DisplayName("equals/hashCode/compareTo")
    void equalsHashCodeCompareTo() {
        final UUID uuid = UUID.randomUUID();
        final UUID same = new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        assertEquals(uuid, same);
        assertEquals(uuid.hashCode(), same.hashCode());
        assertEquals(0, uuid.compareTo(same));
        assertNotEquals(uuid, UUID.randomUUID());
    }

    /**
     * 验证非时间（version 4）UUID 调用 timestamp/clockSequence/node 抛出异常.
     */
    @Test
    @DisplayName("非时间 UUID 的时间访问器抛异常")
    void timeBasedAccessorsThrow() {
        final UUID uuid = UUID.randomUUID();
        assertThrows(UnsupportedOperationException.class, uuid::timestamp);
        assertThrows(UnsupportedOperationException.class, uuid::clockSequence);
        assertThrows(UnsupportedOperationException.class, uuid::node);
    }
}
