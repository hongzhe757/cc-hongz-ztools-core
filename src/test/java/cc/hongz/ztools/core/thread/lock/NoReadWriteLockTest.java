package cc.hongz.ztools.core.thread.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NoReadWriteLock 无锁读写锁")
class NoReadWriteLockTest {

	@Test
	@DisplayName("readLock - 返回NoLock.INSTANCE")
	void readLock() {
		NoReadWriteLock lock = new NoReadWriteLock();
		assertSame(NoLock.INSTANCE, lock.readLock());
	}

	@Test
	@DisplayName("writeLock - 返回NoLock.INSTANCE")
	void writeLock() {
		NoReadWriteLock lock = new NoReadWriteLock();
		assertSame(NoLock.INSTANCE, lock.writeLock());
	}
}
