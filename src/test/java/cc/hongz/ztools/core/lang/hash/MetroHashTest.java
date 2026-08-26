package cc.hongz.ztools.core.lang.hash;

import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.HexUtil;
import cc.hongz.ztools.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * https://gitee.com/chinabugotech/hutool/pulls/532
 */
public @DisplayName("MetroHash 单元测试")
class MetroHashTest {

    @DisplayName("空数据")
    @Test
    public void testEmpty() {
        assertEquals("31290877cceaea29", HexUtil.toHex(MetroHash.hash64(StrUtil.utf8Bytes(""), 0)));
    }

    @DisplayName("64位 Hash")
    @Test
    public void metroHash64Test() {
        byte[] str = "我是一段测试123".getBytes(CharsetUtil.CHARSET_UTF_8);
        final long hash64 = MetroHash.hash64(str);
        assertEquals(62920234463891865L, hash64);
    }

    @DisplayName("128位 Hash")
    @Test
    public void metroHash128Test() {
        byte[] str = "我是一段测试123".getBytes(CharsetUtil.CHARSET_UTF_8);
        final long[] hash128 = MetroHash.hash128(str).getLongArray();
        assertEquals(4956592424592439349L, hash128[0]);
        assertEquals(6301214698325086246L, hash128[1]);
    }

    @DisplayName("默认种子 64位")
    @Test
    public void hash64DefaultSeedTest() {
        byte[] str = "test".getBytes(CharsetUtil.CHARSET_UTF_8);
        // 默认种子与指定种子1337应一致
        assertEquals(MetroHash.hash64(str, 1337), MetroHash.hash64(str));
    }

    @DisplayName("默认种子 128位")
    @Test
    public void hash128DefaultSeedTest() {
        byte[] str = "test".getBytes(CharsetUtil.CHARSET_UTF_8);
        Number128 result1 = MetroHash.hash128(str, 1337);
        Number128 result2 = MetroHash.hash128(str);
        assertEquals(result1, result2);
    }
}
