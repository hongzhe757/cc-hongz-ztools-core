package cc.hongz.ztools.core.lang.intern;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public @DisplayName("JdkStringInterner 单元测试")
class JdkStringInternerTest {

    @DisplayName("普通字符串规范化")
    @Test
    public void internNormalStringTest() {
        JdkStringInterner interner = new JdkStringInterner();
        String s1 = new String("hello");
        String s2 = new String("hello");
        assertNotSame(s1, s2);
        assertSame(interner.intern(s1), interner.intern(s2));
    }

    @DisplayName("null 规范化")
    @Test
    public void internNullTest() {
        JdkStringInterner interner = new JdkStringInterner();
        assertNull(interner.intern(null));
    }

    @DisplayName("同一实例")
    @Test
    public void internSameInstanceTest() {
        JdkStringInterner interner = new JdkStringInterner();
        String s = "testSame";
        assertSame(s, interner.intern(s));
    }

    @DisplayName("与 JDK intern 一致")
    @Test
    public void internConsistentWithJdkTest() {
        JdkStringInterner interner = new JdkStringInterner();
        String s = new String("consistentTest");
        // JdkStringInterner 内部调用 String.intern()，结果应与 JDK intern 一致
        assertSame(s.intern(), interner.intern(s));
    }
}
