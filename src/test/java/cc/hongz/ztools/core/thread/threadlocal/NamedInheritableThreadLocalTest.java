package cc.hongz.ztools.core.thread.threadlocal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NamedInheritableThreadLocal 命名可继承ThreadLocal")
class NamedInheritableThreadLocalTest {

    @Test
    @DisplayName("toString - 返回name")
    void toStringReturnsName() {
        NamedInheritableThreadLocal<String> tl = new NamedInheritableThreadLocal<>("myInheritableTl");
        assertEquals("myInheritableTl", tl.toString());
    }

    @Test
    @DisplayName("set/get - 正常存取")
    void setAndGet() {
        NamedInheritableThreadLocal<String> tl = new NamedInheritableThreadLocal<>("test");
        tl.set("world");
        assertEquals("world", tl.get());
        tl.remove();
    }

    @Test
    @DisplayName("子线程继承父线程值")
    void childThreadInherits() throws InterruptedException {
        NamedInheritableThreadLocal<String> tl = new NamedInheritableThreadLocal<>("inherit-test");
        tl.set("parentValue");

        final String[] childValue = new String[1];
        Thread child = new Thread(() -> childValue[0] = tl.get());
        child.start();
        child.join();

        assertEquals("parentValue", childValue[0]);
        tl.remove();
    }
}
