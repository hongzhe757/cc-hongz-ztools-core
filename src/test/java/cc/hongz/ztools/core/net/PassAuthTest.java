package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.PasswordAuthentication;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PassAuth 单元测试.
 */
class PassAuthTest {

    @Test
    @DisplayName("of 静态工厂方法")
    void ofTest() {
        final PassAuth auth = PassAuth.of("user", "pass".toCharArray());
        assertNotNull(auth);
        final PasswordAuthentication pa = auth.getPasswordAuthentication();
        assertEquals("user", pa.getUserName());
        assertArrayEquals("pass".toCharArray(), pa.getPassword());
    }

    @Test
    @DisplayName("构造方法")
    void constructorTest() {
        final PassAuth auth = new PassAuth("admin", "123".toCharArray());
        final PasswordAuthentication pa = auth.getPasswordAuthentication();
        assertEquals("admin", pa.getUserName());
        assertEquals("123", new String(pa.getPassword()));
    }
}
