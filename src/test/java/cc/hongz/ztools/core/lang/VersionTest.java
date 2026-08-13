package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Version 单元测试.
 */
public class VersionTest {

    @Test
    @DisplayName("of 解析版本号")
    public void ofTest() {
        final Version v = Version.of("1.2.3");
        assertEquals("1.2.3", v.toString());
    }

    @Test
    @DisplayName("compareTo 数字版本比较")
    public void compareToTest() {
        assertTrue(Version.of("1.2.3").compareTo(Version.of("1.2.2")) > 0);
        assertTrue(Version.of("1.2.3").compareTo(Version.of("1.2.4")) < 0);
        assertEquals(0, Version.of("1.2.3").compareTo(Version.of("1.2.3")));
    }

    @Test
    @DisplayName("compareTo 字符串版本比较")
    public void compareToStringTest() {
        assertTrue(Version.of("1.0.beta").compareTo(Version.of("1.0.alpha")) > 0);
        assertTrue(Version.of("1.0.alpha").compareTo(Version.of("1.0.beta")) < 0);
    }

    @Test
    @DisplayName("equals/hashCode 相等性")
    public void equalsTest() {
        assertEquals(Version.of("1.2.3"), Version.of("1.2.3"));
        assertNotEquals(Version.of("1.2.3"), Version.of("1.2.4"));
        assertEquals(Version.of("1.2.3").hashCode(), Version.of("1.2.3").hashCode());
    }

    @Test
    @DisplayName("预发布版本比较")
    public void preReleaseTest() {
        // 1.0.0-alpha < 1.0.0
        assertTrue(Version.of("1.0.0-alpha").compareTo(Version.of("1.0.0")) < 0);
        assertTrue(Version.of("1.0.0").compareTo(Version.of("1.0.0-alpha")) > 0);
    }

    @Test
    @DisplayName("build 版本解析")
    public void buildVersionTest() {
        final Version v = Version.of("1.0.0+build.123");
        assertNotNull(v.toString());
    }
}
