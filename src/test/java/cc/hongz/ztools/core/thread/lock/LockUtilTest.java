package cc.hongz.ztools.core.thread.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LockUtil 锁工具类")
class LockUtilTest {

	@Test
	@DisplayName("createStampLock - 创建StampedLock")
	void createStampLock() {
		StampedLock lock = LockUtil.createStampLock();
		assertNotNull(lock);
	}

	@Test
	@DisplayName("createReadWriteLock - 创建非公平读写锁")
	void createReadWriteLock() {
		ReentrantReadWriteLock lock = LockUtil.createReadWriteLock(false);
		assertNotNull(lock);
		assertFalse(lock.isFair());
	}

	@Test
	@DisplayName("createReadWriteLock - 创建公平读写锁")
	void createFairReadWriteLock() {
		ReentrantReadWriteLock lock = LockUtil.createReadWriteLock(true);
		assertNotNull(lock);
		assertTrue(lock.isFair());
	}

	@Test
	@DisplayName("getNoLock - 获取单例无锁")
	void getNoLock() {
		NoLock lock = LockUtil.getNoLock();
		assertNotNull(lock);
		assertSame(LockUtil.getNoLock(), lock);
	}

	@Test
	@DisplayName("createSegmentLock - 创建分段锁")
	void createSegmentLock() {
		SegmentLock<Lock> lock = LockUtil.createSegmentLock(4);
		assertNotNull(lock);
		assertEquals(4, lock.size());
	}

	@Test
	@DisplayName("createSegmentReadWriteLock - 创建分段读写锁")
	void createSegmentReadWriteLock() {
		SegmentLock<ReadWriteLock> lock = LockUtil.createSegmentReadWriteLock(4);
		assertNotNull(lock);
		assertEquals(4, lock.size());
	}

	@Test
	@DisplayName("createSegmentSemaphore - 创建分段信号量")
	void createSegmentSemaphore() {
		SegmentLock<Semaphore> lock = LockUtil.createSegmentSemaphore(4, 1);
		assertNotNull(lock);
		assertEquals(4, lock.size());
	}

	@Test
	@DisplayName("getSegmentLock - 根据key获取分段锁")
	void getSegmentLock() {
		Lock lock = LockUtil.getSegmentLock(4, "key1");
		assertNotNull(lock);
	}

	@Test
	@DisplayName("getSegmentReadLock - 根据key获取分段读锁")
	void getSegmentReadLock() {
		Lock lock = LockUtil.getSegmentReadLock(4, "key1");
		assertNotNull(lock);
	}

	@Test
	@DisplayName("getSegmentWriteLock - 根据key获取分段写锁")
	void getSegmentWriteLock() {
		Lock lock = LockUtil.getSegmentWriteLock(4, "key1");
		assertNotNull(lock);
	}

    @Test
    @DisplayName("getLazySegmentLock - 根据key获取弱引用分段锁")
    void getLazySegmentLock() {
        Lock lock = LockUtil.getLazySegmentLock(4, "key1");
        assertNotNull(lock);
    }

    @Test
    @DisplayName("createLazySegmentLock - 创建弱引用分段锁")
    void createLazySegmentLock() {
        SegmentLock<Lock> lock = LockUtil.createLazySegmentLock(4);
        assertNotNull(lock);
        assertEquals(4, lock.size());
    }

    @Test
    @DisplayName("getSegmentSemaphore - 根据key获取分段信号量")
    void getSegmentSemaphore() {
        Semaphore semaphore = LockUtil.getSegmentSemaphore(4, 1, "key1");
        assertNotNull(semaphore);
    }
}
