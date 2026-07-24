package cc.hongz.ztools.core.io.watch;

import cc.hongz.ztools.core.io.watch.watchers.IgnoreWatcher;
import cc.hongz.ztools.core.io.watch.watchers.WatcherChain;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Watcher}、{@link SimpleWatcher}、{@link IgnoreWatcher}、{@link WatcherChain} 单元测试.
 */
class WatcherTest {

    private static final Path DUMMY_PATH = Paths.get(".");

    @Test
    void simpleWatcherExtendsIgnoreWatcher() {
        SimpleWatcher sw = new SimpleWatcher();
        assertInstanceOf(IgnoreWatcher.class, sw);
    }

    @Test
    void ignoreWatcherDoesNotThrow() {
        IgnoreWatcher iw = new IgnoreWatcher();
        assertDoesNotThrow(() -> iw.onCreate(null, DUMMY_PATH));
        assertDoesNotThrow(() -> iw.onModify(null, DUMMY_PATH));
        assertDoesNotThrow(() -> iw.onDelete(null, DUMMY_PATH));
        assertDoesNotThrow(() -> iw.onOverflow(null, DUMMY_PATH));
    }

    @Test
    void watcherChainDelegatesToAllWatchers() {
        AtomicInteger createCount = new AtomicInteger();
        AtomicInteger modifyCount = new AtomicInteger();

        Watcher w1 = new IgnoreWatcher() {
            @Override
            public void onCreate(java.nio.file.WatchEvent<?> event, Path currentPath) {
                createCount.incrementAndGet();
            }
        };
        Watcher w2 = new IgnoreWatcher() {
            @Override
            public void onModify(java.nio.file.WatchEvent<?> event, Path currentPath) {
                modifyCount.incrementAndGet();
            }
        };

        WatcherChain chain = WatcherChain.create(w1, w2);
        chain.onCreate(null, DUMMY_PATH);
        chain.onModify(null, DUMMY_PATH);

        assertTrue(createCount.get() >= 1);
        assertTrue(modifyCount.get() >= 1);
    }

    @Test
    void watcherChainAddChain() {
        AtomicInteger count = new AtomicInteger();
        Watcher w = new IgnoreWatcher() {
            @Override
            public void onDelete(java.nio.file.WatchEvent<?> event, Path currentPath) {
                count.incrementAndGet();
            }
        };
        WatcherChain chain = new WatcherChain();
        chain.addChain(w);
        chain.onDelete(null, DUMMY_PATH);
        assertTrue(count.get() >= 1);
    }

    @Test
    void watcherChainIterator() {
        WatcherChain chain = WatcherChain.create(new IgnoreWatcher());
        assertTrue(chain.iterator().hasNext());
    }
}
