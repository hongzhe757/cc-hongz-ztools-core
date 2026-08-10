package cc.hongz.ztools.core.swing.clipboard;

import cc.hongz.ztools.core.thread.ThreadUtil;
import cc.hongz.ztools.core.util.ObjectUtil;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.io.Closeable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 剪贴板监听.
 *
 * @author looly
 * @since 4.5.6
 */
public enum ClipboardMonitor implements ClipboardOwner, Runnable, Closeable {
    INSTANCE;

    /**
     * 默认重试次数.
     */
    public static final int DEFAULT_TRY_COUNT = 10;
    /**
     * 默认重试等待（毫秒）.
     */
    public static final long DEFAULT_DELAY = 100;

    /**
     * 重试次数.
     */
    private int tryCount;
    /**
     * 重试等待（毫秒）.
     */
    private long delay;
    /**
     * 系统剪贴板对象.
     */
    private final Clipboard clipboard;
    /**
     * 监听事件处理集合.
     */
    private final Set<ClipboardListener> listenerSet = new LinkedHashSet<>();
    /**
     * 是否正在监听.
     */
    private boolean isRunning;

    /**
     * 构造，使用默认参数.
     */
    ClipboardMonitor() {
        this(DEFAULT_TRY_COUNT, DEFAULT_DELAY);
    }

    /**
     * 构造.
     *
     * @param tryCount 尝试获取剪贴板内容的次数
     * @param delay    响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取
     */
    ClipboardMonitor(final int tryCount, final long delay) {
        this(tryCount, delay, ClipboardUtil.getClipboard());
    }

    /**
     * 构造.
     *
     * @param tryCount  尝试获取剪贴板内容的次数
     * @param delay     响应延迟
     * @param clipboard 剪贴板对象
     */
    ClipboardMonitor(final int tryCount, final long delay,
            final Clipboard clipboard) {
        this.tryCount = tryCount;
        this.delay = delay;
        this.clipboard = clipboard;
    }

    /**
     * 设置重试次数.
     *
     * @param tryCount 重试次数
     * @return this
     */
    public ClipboardMonitor setTryCount(final int tryCount) {
        this.tryCount = tryCount;
        return this;
    }

    /**
     * 设置重试等待.
     *
     * @param delay 重试等待（毫秒）
     * @return this
     */
    public ClipboardMonitor setDelay(final long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * 设置监听事件处理.
     *
     * @param listener 监听事件处理
     * @return this
     */
    public ClipboardMonitor addListener(final ClipboardListener listener) {
        this.listenerSet.add(listener);
        return this;
    }

    /**
     * 去除指定监听.
     *
     * @param listener 监听
     * @return this
     */
    public ClipboardMonitor removeListener(final ClipboardListener listener) {
        this.listenerSet.remove(listener);
        return this;
    }

    /**
     * 清空监听.
     *
     * @return this
     */
    public ClipboardMonitor clearListener() {
        this.listenerSet.clear();
        return this;
    }

    @Override
    public void lostOwnership(final Clipboard clipboard,
            final Transferable contents) {
        Transferable newContents;
        try {
            newContents = tryGetContent(clipboard);
        } catch (final InterruptedException e) {
            // 中断后结束监听
            return;
        }

        Transferable transferable = null;
        for (final ClipboardListener listener : listenerSet) {
            try {
                transferable = listener.onChange(clipboard,
                        ObjectUtil.defaultIfNull(transferable, newContents));
            } catch (final Throwable e) {
                // 忽略事件处理异常，保证所有监听正常执行
            }
        }

        if (isRunning) {
            // 继续监听
            clipboard.setContents(ObjectUtil.defaultIfNull(transferable,
                    ObjectUtil.defaultIfNull(newContents, contents)), this);
        }
    }

    @Override
    public synchronized void run() {
        if (false == isRunning) {
            final Clipboard cb = this.clipboard;
            cb.setContents(cb.getContents(null), this);
            isRunning = true;
        }
    }

    /**
     * 开始监听.
     *
     * @param sync 是否阻塞
     */
    public void listen(final boolean sync) {
        run();

        if (sync) {
            ThreadUtil.sync(this);
        }
    }

    /**
     * 关闭（停止）监听.
     */
    @Override
    public void close() {
        this.isRunning = false;
    }

    /**
     * 尝试获取剪贴板内容.
     *
     * @param clipboard 剪贴板
     * @return 剪贴板内容，{@code null} 表示未获取到
     * @throws InterruptedException 线程中断
     */
    private Transferable tryGetContent(final Clipboard clipboard)
            throws InterruptedException {
        Transferable newContents = null;
        for (int i = 0; i < this.tryCount; i++) {
            if (this.delay > 0 && i > 0) {
                Thread.sleep(this.delay);
            }

            try {
                newContents = clipboard.getContents(null);
            } catch (final IllegalStateException e) {
                // ignore
            }
            if (null != newContents) {
                return newContents;
            }
        }
        return null;
    }
}
