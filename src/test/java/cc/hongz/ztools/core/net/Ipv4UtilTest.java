package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ipv4Util 单元测试.
 */
class Ipv4UtilTest {

    @Test
    @DisplayName("ipv4ToLong IP 转 long")
    void ipv4ToLongTest() {
        assertEquals(3232235777L, Ipv4Util.ipv4ToLong("192.168.1.1"));
        assertEquals(0L, Ipv4Util.ipv4ToLong("0.0.0.0"));
        assertEquals(2130706433L, Ipv4Util.ipv4ToLong("127.0.0.1"));
        assertEquals(4294967295L, Ipv4Util.ipv4ToLong("255.255.255.255"));
    }

    @Test
    @DisplayName("ipv4ToLong 无效 IP 返回默认值")
    void ipv4ToLongWithDefaultTest() {
        assertEquals(3232235777L, Ipv4Util.ipv4ToLong("192.168.1.1", -1L));
        assertEquals(-1L, Ipv4Util.ipv4ToLong("invalid_ip", -1L));
        assertEquals(0L, Ipv4Util.ipv4ToLong("abc.def.ghi.jkl", 0L));
    }

    @Test
    @DisplayName("ipv4ToLong 非法 IP 抛异常")
    void ipv4ToLongInvalidTest() {
        assertThrows(IllegalArgumentException.class,
                () -> Ipv4Util.ipv4ToLong("invalid"));
        assertThrows(IllegalArgumentException.class,
                () -> Ipv4Util.ipv4ToLong("256.1.1.1"));
    }

    @Test
    @DisplayName("longToIpv4 long 转 IP")
    void longToIpv4Test() {
        assertEquals("192.168.1.1", Ipv4Util.longToIpv4(3232235777L));
        assertEquals("0.0.0.0", Ipv4Util.longToIpv4(0L));
        assertEquals("127.0.0.1", Ipv4Util.longToIpv4(2130706433L));
        assertEquals("255.255.255.255", Ipv4Util.longToIpv4(4294967295L));
    }

    @Test
    @DisplayName("isMaskBitValid 掩码位有效性")
    void isMaskBitValidTest() {
        assertTrue(Ipv4Util.isMaskBitValid(1));
        assertTrue(Ipv4Util.isMaskBitValid(8));
        assertTrue(Ipv4Util.isMaskBitValid(16));
        assertTrue(Ipv4Util.isMaskBitValid(24));
        assertTrue(Ipv4Util.isMaskBitValid(32));
        assertFalse(Ipv4Util.isMaskBitValid(0));
        assertFalse(Ipv4Util.isMaskBitValid(-1));
        assertFalse(Ipv4Util.isMaskBitValid(33));
    }

    @Test
    @DisplayName("isMaskValid 掩码有效性")
    void isMaskValidTest() {
        assertTrue(Ipv4Util.isMaskValid("255.255.255.0"));
        assertTrue(Ipv4Util.isMaskValid("255.255.0.0"));
        assertTrue(Ipv4Util.isMaskValid("255.0.0.0"));
        assertFalse(Ipv4Util.isMaskValid("255.255.255.1"));
        assertFalse(Ipv4Util.isMaskValid("invalid"));
    }

    @Test
    @DisplayName("getBeginIpStr 获取起始IP字符串")
    void getBeginIpStrTest() {
        assertEquals("192.168.1.0",
                Ipv4Util.getBeginIpStr("192.168.1.100", 24));
        assertEquals("10.0.0.0", Ipv4Util.getBeginIpStr("10.1.2.3", 8));
        assertEquals("172.16.0.0", Ipv4Util.getBeginIpStr("172.16.5.10", 16));
    }

    @Test
    @DisplayName("getEndIpStr 获取结束IP字符串")
    void getEndIpStrTest() {
        assertEquals("192.168.1.255",
                Ipv4Util.getEndIpStr("192.168.1.100", 24));
        assertEquals("10.255.255.255", Ipv4Util.getEndIpStr("10.1.2.3", 8));
        assertEquals("172.16.255.255", Ipv4Util.getEndIpStr("172.16.5.10", 16));
    }

    @Test
    @DisplayName("getMaskBitByMask 掩码转掩码位")
    void getMaskBitByMaskTest() {
        assertEquals(24, Ipv4Util.getMaskBitByMask("255.255.255.0"));
        assertEquals(16, Ipv4Util.getMaskBitByMask("255.255.0.0"));
        assertEquals(8, Ipv4Util.getMaskBitByMask("255.0.0.0"));
        assertEquals(32, Ipv4Util.getMaskBitByMask("255.255.255.255"));
        assertThrows(IllegalArgumentException.class,
                () -> Ipv4Util.getMaskBitByMask("invalid"));
    }

    @Test
    @DisplayName("formatIpBlock 格式化IP段")
    void formatIpBlockTest() {
        assertEquals("192.168.1.0/24",
                Ipv4Util.formatIpBlock("192.168.1.0", "255.255.255.0"));
        assertEquals("10.0.0.0/8",
                Ipv4Util.formatIpBlock("10.0.0.0", "255.0.0.0"));
    }

    @Test
    @DisplayName("countByMaskBit 计算子网大小")
    void countByMaskBitTest() {
        assertEquals(256, Ipv4Util.countByMaskBit(24, true));
        assertEquals(65536, Ipv4Util.countByMaskBit(16, true));
        assertEquals(1, Ipv4Util.countByMaskBit(32, true));
        assertEquals(254, Ipv4Util.countByMaskBit(24, false));
        assertEquals(65534, Ipv4Util.countByMaskBit(16, false));
        assertEquals(0, Ipv4Util.countByMaskBit(32, false));
        assertEquals(0, Ipv4Util.countByMaskBit(0, false));
    }

    @Test
    @DisplayName("getMaskByMaskBit 掩码位转掩码")
    void getMaskByMaskBitTest() {
        assertEquals("255.255.255.0", Ipv4Util.getMaskByMaskBit(24));
        assertEquals("255.255.0.0", Ipv4Util.getMaskByMaskBit(16));
        assertEquals("255.0.0.0", Ipv4Util.getMaskByMaskBit(8));
    }

    @Test
    @DisplayName("isInnerIP 判断内网IP")
    void isInnerIPTest() {
        assertTrue(Ipv4Util.isInnerIP("10.0.0.1"));
        assertTrue(Ipv4Util.isInnerIP("10.255.255.255"));
        assertTrue(Ipv4Util.isInnerIP("172.16.0.1"));
        assertTrue(Ipv4Util.isInnerIP("172.31.255.255"));
        assertTrue(Ipv4Util.isInnerIP("192.168.0.1"));
        assertTrue(Ipv4Util.isInnerIP("192.168.255.255"));
        assertTrue(Ipv4Util.isInnerIP("127.0.0.1"));
        assertFalse(Ipv4Util.isInnerIP("8.8.8.8"));
        assertFalse(Ipv4Util.isInnerIP("114.114.114.114"));
    }

    @Test
    @DisplayName("matches 通配符IP匹配")
    void matchesTest() {
        assertTrue(Ipv4Util.matches("192.168.*.*", "192.168.1.1"));
        assertTrue(Ipv4Util.matches("*.*.*.*", "10.0.0.1"));
        assertTrue(Ipv4Util.matches("192.168.1.1", "192.168.1.1"));
        assertFalse(Ipv4Util.matches("192.168.1.*", "192.168.2.1"));
        assertFalse(Ipv4Util.matches("10.*.*.*", "192.168.1.1"));
        assertFalse(Ipv4Util.matches("*.*.*.*", "invalid"));
    }

    @Test
    @DisplayName("list 单IP列表")
    void listSingleIpTest() {
        List<String> ips = Ipv4Util.list("192.168.1.1", true);
        assertEquals(1, ips.size());
        assertEquals("192.168.1.1", ips.get(0));
    }

    @Test
    @DisplayName("list 掩码位IP列表")
    void listByMaskBitTest() {
        List<String> ips = Ipv4Util.list("192.168.1.0", 30, true);
        assertEquals(4, ips.size());
        assertEquals("192.168.1.0", ips.get(0));
        assertEquals("192.168.1.3", ips.get(3));
        List<String> usableIps = Ipv4Util.list("192.168.1.0", 30, false);
        assertEquals(2, usableIps.size());
        assertEquals("192.168.1.1", usableIps.get(0));
        assertEquals("192.168.1.2", usableIps.get(1));
    }

    @Test
    @DisplayName("list IP区间列表")
    void listByRangeTest() {
        List<String> ips = Ipv4Util.list("192.168.1.1", "192.168.1.5");
        assertEquals(5, ips.size());
        assertEquals("192.168.1.1", ips.get(0));
        assertEquals("192.168.1.5", ips.get(4));
    }

    @Test
    @DisplayName("countByIpRange IP区间IP数量")
    void countByIpRangeTest() {
        assertEquals(5, Ipv4Util.countByIpRange("192.168.1.1", "192.168.1.5"));
        assertEquals(256, Ipv4Util.countByIpRange("192.168.1.0", "192.168.1.255"));
        assertEquals(1, Ipv4Util.countByIpRange("10.0.0.1", "10.0.0.1"));
    }

    @Test
    @DisplayName("getMaskByIpRange 根据IP区间计算掩码")
    void getMaskByIpRangeTest() {
        assertEquals("255.255.255.0",
                Ipv4Util.getMaskByIpRange("192.168.1.0", "192.168.1.255"));
    }
}
