package cc.hongz.ztools.core.lang.hash;

import cc.hongz.ztools.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("CityHash 单元测试")
class CityHashTest {

    @DisplayName("32位 Hash")
    @Test
    public void hash32Test() {
        int hv = CityHash.hash32(StrUtil.utf8Bytes("你"));
        assertEquals(1290029860, hv);

        hv = CityHash.hash32(StrUtil.utf8Bytes("你好"));
        assertEquals(1374181357, hv);

        hv = CityHash.hash32(StrUtil.utf8Bytes("见到你很高兴"));
        assertEquals(1475516842, hv);
        hv = CityHash.hash32(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        assertEquals(0x51020cae, hv);
    }

    @DisplayName("64位 Hash")
    @Test
    public void hash64Test() {
        long hv = CityHash.hash64(StrUtil.utf8Bytes("你"));
        assertEquals(-4296898700418225525L, hv);

        hv = CityHash.hash64(StrUtil.utf8Bytes("你好"));
        assertEquals(-4294276205456761303L, hv);

        hv = CityHash.hash64(StrUtil.utf8Bytes("见到你很高兴"));
        assertEquals(272351505337503793L, hv);
        hv = CityHash.hash64(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        assertEquals(-8234735310919228703L, hv);
    }

    @DisplayName("128位 Hash")
    @Test
    public void hash128Test() {
        Number128 result = CityHash.hash128(StrUtil.utf8Bytes("你好"));
        assertNotNull(result);
        // 128位hash值不应为全零
        assertFalse(result.getLowValue() == 0 && result.getHighValue() == 0);
    }

    @DisplayName("带种子 64位 Hash")
    @Test
    public void hash64WithSeedTest() {
        long hv1 = CityHash.hash64(StrUtil.utf8Bytes("test"));
        long hv2 = CityHash.hash64(StrUtil.utf8Bytes("test"), 12345L);
        // 不同种子应产生不同hash值
        assertNotEquals(hv1, hv2);
    }
}
