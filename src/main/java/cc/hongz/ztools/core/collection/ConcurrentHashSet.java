package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.map.SafeConcurrentHashMap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * 通过{@link SafeConcurrentHashMap}实现的线程安全HashSet。
 *
 * @author Looly
 * @since 3.1.0
 *
 * @param <E> 元素类型
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements java.io.Serializable {
    private static final long serialVersionUID = 7997886765361607470L;

    /**
     * 持有对象。如果值为此对象表示有数据，否则无数据。
     */
    private static final Boolean PRESENT = true;

    /**
     * 默认加载因子。
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final SafeConcurrentHashMap<E, Boolean> map;

    // ----------------------------------------------------------------------------------- Constructor start
    /**
     * 构造。
     *
     * <p>
     * 触发因子为默认的0.75。
     */
    public ConcurrentHashSet() {
        map = new SafeConcurrentHashMap<>();
    }

    /**
     * 构造。
     *
     * <p>
     * 触发因子为默认的0.75。
     *
     * @param initialCapacity 初始大小
     */
    public ConcurrentHashSet(final int initialCapacity) {
        map = new SafeConcurrentHashMap<>(initialCapacity);
    }

    /**
     * 构造。
     *
     * @param initialCapacity 初始大小
     * @param loadFactor 加载因子。此参数决定数据增长时触发的百分比
     */
    public ConcurrentHashSet(final int initialCapacity, final float loadFactor) {
        map = new SafeConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * 构造。
     *
     * @param initialCapacity 初始大小
     * @param loadFactor 触发因子。此参数决定数据增长时触发的百分比
     * @param concurrencyLevel 线程并发度
     */
    public ConcurrentHashSet(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        map = new SafeConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * 从已有集合中构造。
     *
     * @param iter {@link Iterable}
     */
    public ConcurrentHashSet(final Iterable<E> iter) {
        if (iter instanceof Collection) {
            final Collection<E> collection = (Collection<E>) iter;
            map = new SafeConcurrentHashMap<>((int) (collection.size() / DEFAULT_LOAD_FACTOR));
            this.addAll(collection);
        } else {
            map = new SafeConcurrentHashMap<>();
            for (E e : iter) {
                this.add(e);
            }
        }
    }
    // ----------------------------------------------------------------------------------- Constructor end

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        //noinspection SuspiciousMethodCalls
        return map.containsKey(o);
    }

    @Override
    public boolean add(final E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(final Object o) {
        return PRESENT.equals(map.remove(o));
    }

    @Override
    public void clear() {
        map.clear();
    }
}
