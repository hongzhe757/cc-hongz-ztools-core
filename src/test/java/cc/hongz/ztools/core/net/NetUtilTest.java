package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.TreeSet;

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
	void localIpsTest() {
		LinkedHashSet<String> ips = NetUtil.localIps();
		assertNotNull(ips);
		assertFalse(ips.isEmpty());
	}

	@Test
	void isUsableLocalPortTest() {
		assertTrue(NetUtil.isUsableLocalPort(0));
		assertFalse(NetUtil.isUsableLocalPort(-1));
		assertFalse(NetUtil.isUsableLocalPort(65536));
	}

	@Test
	void isValidPortTest() {
		assertTrue(NetUtil.isValidPort(0));
		assertTrue(NetUtil.isValidPort(80));
		assertTrue(NetUtil.isValidPort(8080));
		assertTrue(NetUtil.isValidPort(65535));
		assertFalse(NetUtil.isValidPort(-1));
		assertFalse(NetUtil.isValidPort(65536));
	}

	@Test
	void getLocalhostStrTest() {
		String localAddress = NetUtil.getLocalhostStr();
		assertNotNull(localAddress);
	}

	@Test
	void isInnerIPTest() {
		assertTrue(NetUtil.isInnerIP("192.168.1.1"));
		assertTrue(NetUtil.isInnerIP("10.0.0.1"));
		assertTrue(NetUtil.isInnerIP("172.16.0.1"));
		assertTrue(NetUtil.isInnerIP("127.0.0.1"));
		assertFalse(NetUtil.isInnerIP("8.8.8.8"));
	}

	@Test
	void hideIpPartTest() {
		assertEquals("192.168.1.*", NetUtil.hideIpPart("192.168.1.100"));
		assertEquals("10.0.0.*", NetUtil.hideIpPart("10.0.0.1"));
	}

	@Test
	void hideIpPartLongTest() {
		String result = NetUtil.hideIpPart(3232235777L); // 192.168.1.1
		assertEquals("192.168.1.*", result);
	}

	@Test
	void buildInetSocketAddressTest() {
		InetSocketAddress addr = NetUtil.buildInetSocketAddress("127.0.0.1:8080", 9090);
		assertEquals(8080, addr.getPort());
		assertEquals("/127.0.0.1", addr.getAddress().toString());

		// 无端口形式，使用默认端口
		InetSocketAddress addr2 = NetUtil.buildInetSocketAddress("127.0.0.1", 9090);
		assertEquals(9090, addr2.getPort());

		// 空host，使用本地地址
		InetSocketAddress addr3 = NetUtil.buildInetSocketAddress("", 8080);
		assertEquals(8080, addr3.getPort());
	}

	@Test
	void getIpByHostTest() {
		// 127.0.0.1 应该返回自身
		String ip = NetUtil.getIpByHost("127.0.0.1");
		assertEquals("127.0.0.1", ip);

		// 无法解析的域名返回原始hostName
		String unknown = NetUtil.getIpByHost("unknown.host.invalid");
		assertEquals("unknown.host.invalid", unknown);
	}

	@Test
	void toAbsoluteUrlTest() {
		// URL会规范化路径中的..
		String url = NetUtil.toAbsoluteUrl("http://example.com/path/", "../other");
		assertEquals("http://example.com/other", url);

		String url2 = NetUtil.toAbsoluteUrl("http://example.com/a/b/", "c/d");
		assertEquals("http://example.com/a/b/c/d", url2);
	}

	@Test
	void getUsableLocalPortTest() {
		int port = NetUtil.getUsableLocalPort();
		assertTrue(port >= NetUtil.PORT_RANGE_MIN);
		assertTrue(port <= NetUtil.PORT_RANGE_MAX);
	}

	@Test
	void getUsableLocalPortsTest() {
		TreeSet<Integer> ports = NetUtil.getUsableLocalPorts(3, 10000, 60000);
		assertEquals(3, ports.size());
		for (int port : ports) {
			assertTrue(port >= 10000);
			assertTrue(port <= 60000);
		}
	}

	@Test
	void longToIpv4AndBackTest() {
		assertEquals("192.168.1.1", NetUtil.longToIpv4(3232235777L));
		assertEquals(3232235777L, NetUtil.ipv4ToLong("192.168.1.1"));
	}

	@Test
	void getNetworkInterfacesTest() {
		var interfaces = NetUtil.getNetworkInterfaces();
		assertNotNull(interfaces);
		assertFalse(interfaces.isEmpty());
	}
}
