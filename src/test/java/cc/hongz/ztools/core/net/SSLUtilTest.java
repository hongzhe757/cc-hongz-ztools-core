package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SSLUtil 单元测试.
 */
class SSLUtilTest {

    @Test
    @DisplayName("创建默认信任全部证书的 SSLContext")
    void createSSLContextTest() {
        SSLContext sslContext = SSLUtil.createSSLContext("TLS");
        assertNotNull(sslContext);
        assertEquals("TLS", sslContext.getProtocol());
    }

    @Test
    @DisplayName("创建带 KeyManager/TrustManager 的 SSLContext")
    void createSSLContextWithManagersTest() {
        SSLContext sslContext = SSLUtil.createSSLContext("TLS",
                (javax.net.ssl.KeyManager) null,
                (javax.net.ssl.TrustManager) null);
        assertNotNull(sslContext);
        assertEquals("TLS", sslContext.getProtocol());
    }

    @Test
    @DisplayName("创建带数组参数 SSLContext")
    void createSSLContextWithArraysTest() {
        SSLContext sslContext = SSLUtil.createSSLContext("TLS",
                (javax.net.ssl.KeyManager[]) null,
                (javax.net.ssl.TrustManager[]) null);
        assertNotNull(sslContext);
    }
}
