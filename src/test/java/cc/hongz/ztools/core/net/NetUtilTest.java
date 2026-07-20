package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NetUtil 单元测试
 */
class NetUtilTest {

	@Test
	void getLocalhostTest() {
		InetAddress localhost = NetUtil.getLocalhost();
		assertNotNull(localhost);
	}

	@Test
	void localAddressListTest() {
		LinkedHashSet<InetAddress> addresses = NetUtil.localAddressList(null);
		assertNotNull(addresses);
		assertFalse(addresses.isEmpty());
	}

	@Test
	void isUsableLocalPortTest() {
		assertTrue(NetUtil.isUsableLocalPort(0));
		assertFalse(NetUtil.isUsableLocalPort(-1));
	}

	@Test
	void getLocalhostStrTest() {
		String localAddress = NetUtil.getLocalhostStr();
		assertNotNull(localAddress);
	}
}
