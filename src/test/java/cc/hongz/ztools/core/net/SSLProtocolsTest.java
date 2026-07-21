package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SSLProtocols 单元测试
 */
class SSLProtocolsTest {

	@Test
	void constantsTest() {
		assertEquals("SSL", SSLProtocols.SSL);
		assertEquals("SSLv2", SSLProtocols.SSLv2);
		assertEquals("SSLv3", SSLProtocols.SSLv3);
		assertEquals("TLS", SSLProtocols.TLS);
		assertEquals("TLSv1", SSLProtocols.TLSv1);
		assertEquals("TLSv1.1", SSLProtocols.TLSv11);
		assertEquals("TLSv1.2", SSLProtocols.TLSv12);
	}
}
