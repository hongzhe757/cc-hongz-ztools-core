package cc.hongz.ztools.core.thread.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SegmentLock 分段锁")
class SegmentLockTest {

    @Test
    @DisplayName("lock - 创建强引用分段锁")
    void lock() {
        SegmentLock<Lock> lock = SegmentLock.lock(8);
        assertNotNull(lock);
        assertEquals(8, lock.size());
    }

    @Test
    @DisplayName("lock - 相同key返回相同锁")
    void lockSameKey() {
        SegmentLock<Lock> lock = SegmentLock.lock(8);
        Lock l1 = lock.get("key1");
        Lock l2 = lock.get("key1");
        assertSame(l1, l2);
    }

    @Test
    @DisplayName("lock - getAt获取指定索引锁")
    void lockGetAt() {
        SegmentLock<Lock> lock = SegmentLock.lock(4);
        Lock l = lock.getAt(0);
        assertNotNull(l);
    }

    @Test
    @DisplayName("readWriteLock - 创建分段读写锁")
    void readWriteLock() {
        SegmentLock<ReadWriteLock> lock = SegmentLock.readWriteLock(4);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("semaphore - 创建分段信号量")
    void semaphore() {
        SegmentLock<Semaphore> lock = SegmentLock.semaphore(4, 3);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("lazyWeakLock - 创建弱引用分段锁")
    void lazyWeakLock() {
        SegmentLock<Lock> lock = SegmentLock.lazyWeakLock(4);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("lazyWeakReadWriteLock - 创建弱引用分段读写锁")
    void lazyWeakReadWriteLock() {
        SegmentLock<ReadWriteLock> lock = SegmentLock.lazyWeakReadWriteLock(4);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("lazyWeakSemaphore - 创建弱引用分段信号量")
    void lazyWeakSemaphore() {
        SegmentLock<Semaphore> lock = SegmentLock.lazyWeakSemaphore(4, 2);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("bulkGet - 批量获取锁")
    void bulkGet() {
        SegmentLock<Lock> lock = SegmentLock.lock(4);
        Iterable<Lock> locks = lock.bulkGet(java.util.Arrays.asList("a", "b", "c"));
        assertNotNull(locks);
    }

    @Test
    @DisplayName("custom - 自定义分段锁")
    void custom() {
        SegmentLock<Lock> lock = SegmentLock.custom(4, () -> new java.util.concurrent.locks.ReentrantLock());
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }
}
