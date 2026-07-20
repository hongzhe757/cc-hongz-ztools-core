package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ipv4Util 单元测试
 */
class Ipv4UtilTest {

	@Test
	void ipv4ToLongTest() {
		assertEquals(3232235777L, Ipv4Util.ipv4ToLong("192.168.1.1"));
		assertEquals(0L, Ipv4Util.ipv4ToLong("0.0.0.0"));
	}

	@Test
	void longToIpv4Test() {
		assertEquals("192.168.1.1", Ipv4Util.longToIpv4(3232235777L));
		assertEquals("0.0.0.0", Ipv4Util.longToIpv4(0L));
	}

	@Test
	void isMaskBitValidTest() {
		assertTrue(Ipv4Util.isMaskBitValid(1));
		assertTrue(Ipv4Util.isMaskBitValid(24));
		assertTrue(Ipv4Util.isMaskBitValid(32));
		assertFalse(Ipv4Util.isMaskBitValid(0));
		assertFalse(Ipv4Util.isMaskBitValid(-1));
		assertFalse(Ipv4Util.isMaskBitValid(33));
	}

	@Test
	void getBeginIpStrTest() {
		assertEquals("192.168.1.0", Ipv4Util.getBeginIpStr("192.168.1.100", 24));
	}

	@Test
	void getEndIpStrTest() {
		assertEquals("192.168.1.255", Ipv4Util.getEndIpStr("192.168.1.100", 24));
	}
}
