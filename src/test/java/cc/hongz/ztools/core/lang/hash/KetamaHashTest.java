package cc.hongz.ztools.core.lang.hash;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("KetamaHash 单元测试")
class KetamaHashTest {

    @DisplayName("64位 Hash")
    @Test
    public void hash64Test() {
        KetamaHash hash = new KetamaHash();
        long h1 = hash.hash64("server1");
        long h2 = hash.hash64("server2");
        // 不同key应产生不同hash值
        assertNotEquals(h1, h2);
    }

    @DisplayName("32位 Hash")
    @Test
    public void hash32Test() {
        KetamaHash hash = new KetamaHash();
        int h1 = hash.hash32("server1");
        int h2 = hash.hash32("server2");
        assertNotEquals(h1, h2);
    }

    @DisplayName("Hash 接口")
    @Test
    public void hashInterfaceTest() {
        KetamaHash hash = new KetamaHash();
        // 通过 Hash 接口调用
        Number result = hash.hash("testKey");
        assertNotNull(result);
        assertEquals(hash.hash64("testKey"), result.longValue());
    }

    @DisplayName("一致性")
    @Test
    public void consistencyTest() {
        KetamaHash hash = new KetamaHash();
        // 相同key应产生相同hash值
        long h1 = hash.hash64("consistentKey");
        long h2 = hash.hash64("consistentKey");
        assertEquals(h1, h2);
    }

    @DisplayName("hash32 是 hash64 低32位")
    @Test
    public void hash32IsLower32OfHash64() {
        KetamaHash hash = new KetamaHash();
        long h64 = hash.hash64("test");
        int h32 = hash.hash32("test");
        // hash32 应是 hash64 的低32位
        assertEquals((int) (h64 & 0xffffffffL), h32);
    }
}
