package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.PasswordAuthentication;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link UserPassAuthenticator} 单元测试.
 */
class UserPassAuthenticatorTest {

    @Test
    @DisplayName("构造并获取密码认证测试")
    void authenticateTest() {
        final UserPassAuthenticator authenticator = new UserPassAuthenticator(
                "testUser", "testPass".toCharArray());
        final PasswordAuthentication auth = authenticator.getPasswordAuthentication();
        assertEquals("testUser", auth.getUserName());
        assertArrayEquals("testPass".toCharArray(), auth.getPassword());
    }

    @Test
    @DisplayName("空密码测试")
    void emptyPasswordTest() {
        final UserPassAuthenticator authenticator = new UserPassAuthenticator(
                "user", new char[0]);
        final PasswordAuthentication auth = authenticator.getPasswordAuthentication();
        assertEquals("user", auth.getUserName());
        assertEquals(0, auth.getPassword().length);
    }
}
