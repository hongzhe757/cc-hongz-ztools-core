package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SSLUtil 单元测试
 */
class SSLUtilTest {

	@Test
	void createSSLContextTest() {
		SSLContext sslContext = SSLUtil.createSSLContext("TLS");
		assertNotNull(sslContext);
		assertEquals("TLS", sslContext.getProtocol());
	}

	@Test
	void createSSLContextWithManagersTest() {
		// keyManager和trustManager都为null，需显式转型避免歧义
		SSLContext sslContext = SSLUtil.createSSLContext("TLS", (javax.net.ssl.KeyManager) null, (javax.net.ssl.TrustManager) null);
		assertNotNull(sslContext);
		assertEquals("TLS", sslContext.getProtocol());
	}

	@Test
	void createSSLContextWithArraysTest() {
		SSLContext sslContext = SSLUtil.createSSLContext("TLS", (javax.net.ssl.KeyManager[]) null, (javax.net.ssl.TrustManager[]) null);
		assertNotNull(sslContext);
	}
}
