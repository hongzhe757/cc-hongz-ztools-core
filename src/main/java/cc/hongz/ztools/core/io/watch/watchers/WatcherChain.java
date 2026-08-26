package cc.hongz.ztools.core.io.watch.watchers;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.io.watch.Watcher;
import cc.hongz.ztools.core.lang.Chain;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Iterator;
import java.util.List;

/**
 * 观察者链。 <br>
 * 用于加入多个观察者
 *
 * @author Looly
 * @since 3.1.0
 */
public class WatcherChain implements Watcher, Chain<Watcher, WatcherChain> {

    /**
     * 观察者列表。
     */
    private final List<Watcher> chain;

    /**
     * 创建观察者链{@link WatcherChain}。
     * @param watchers  观察者列表
     * @return {@link WatcherChain}
     */
    public static WatcherChain create(final Watcher... watchers) {
        return new WatcherChain(watchers);
    }

    /**
     * 构造。
     * @param watchers 观察者列表
     */
    public WatcherChain(final Watcher... watchers) {
        chain = CollUtil.newArrayList(watchers);
    }

    @Override
    public void onCreate(final WatchEvent<?> event, final Path currentPath) {
        for (Watcher watcher : chain) {
            watcher.onCreate(event, currentPath);
        }
    }

    @Override
    public void onModify(final WatchEvent<?> event, final Path currentPath) {
        for (Watcher watcher : chain) {
            watcher.onModify(event, currentPath);
        }
    }

    @Override
    public void onDelete(final WatchEvent<?> event, final Path currentPath) {
        for (Watcher watcher : chain) {
            watcher.onDelete(event, currentPath);
        }
    }

    @Override
    public void onOverflow(final WatchEvent<?> event, final Path currentPath) {
        for (Watcher watcher : chain) {
            watcher.onOverflow(event, currentPath);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Watcher> iterator() {
        return this.chain.iterator();
    }

    @Override
    public WatcherChain addChain(final Watcher element) {
        this.chain.add(element);
        return this;
    }

}
