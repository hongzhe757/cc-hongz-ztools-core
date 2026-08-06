package cc.hongz.ztools.core.thread.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NoLock 无锁实现")
class NoLockTest {

    @Test
    @DisplayName("lockInterruptibly - 不抛异常")
    void lockInterruptibly() {
        NoLock lock = new NoLock();
        assertDoesNotThrow(lock::lockInterruptibly);
    }

    @Test
    @DisplayName("lock/unlock - 不抛异常")
    void lockUnlock() {
        NoLock lock = new NoLock();
        assertDoesNotThrow(() -> {
            lock.lock();
            lock.unlock();
        });
    }

    @Test
    @DisplayName("tryLock - 始终返回true")
    void tryLock() {
        NoLock lock = new NoLock();
        assertTrue(lock.tryLock());
    }

    @Test
    @DisplayName("tryLock with time - 始终返回true")
    void tryLockWithTime() throws InterruptedException {
        NoLock lock = new NoLock();
        assertTrue(lock.tryLock(1, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("newCondition - 抛出不支持异常")
    void newCondition() {
        NoLock lock = new NoLock();
        assertThrows(UnsupportedOperationException.class, lock::newCondition);
    }

    @Test
    @DisplayName("INSTANCE - 单例可用")
    void instance() {
        assertNotNull(NoLock.INSTANCE);
    }
}
