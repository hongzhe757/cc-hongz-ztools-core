package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UUID} 单元测试.
 * <p>
 * 覆盖 fastUUID 格式、唯一性及与 randomUUID 的差异.
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
}
