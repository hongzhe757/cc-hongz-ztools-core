package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.lang.Assert;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 按照数组的顺序正序排列，数组的元素位置决定了对象的排序先后。
 * <p>
 * 默认的，如果参与排序的元素并不在数组中，则排序在前（可以通过atEndIfMiss设置）。
 *
 * @param <T> 被排序元素类型
 * @author looly
 */
public class IndexedComparator<T> implements Comparator<T> {

    private final boolean atEndIfMiss;
    private final Map<? super T, Integer> map;

    /**
     * 构造.
     *
     * @param objs 参与排序的数组，数组的元素位置决定了对象的排序先后
     */
    @SuppressWarnings("unchecked")
    public IndexedComparator(final T... objs) {
        this(false, objs);
    }

    /**
     * 构造.
     *
     * @param atEndIfMiss 如果不在列表中是否排在后边
     * @param map         参与排序的map，map中的value值大小决定了对象的排序先后
     */
    @SuppressWarnings("unchecked")
    private IndexedComparator(final boolean atEndIfMiss, final Map<? super T, Integer> map) {
        this.atEndIfMiss = atEndIfMiss;
        this.map = map;
    }

    /**
     * 构造.
     *
     * @param atEndIfMiss 如果不在列表中是否排在后边
     * @param objs        参与排序的数组
     */
    @SuppressWarnings("unchecked")
    public IndexedComparator(final boolean atEndIfMiss, final T... objs) {
        Assert.notNull(objs, "'objs' array must not be null");
        this.atEndIfMiss = atEndIfMiss;
        map = new HashMap<>(objs.length, 1);
        for (int i = 0; i < objs.length; i++) {
            map.put(objs[i], i);
        }
    }

    @Override
    public int compare(final T o1, final T o2) {
        final int index1 = getOrder(o1);
        final int index2 = getOrder(o2);

        if (index1 == index2) {
            if (index1 < 0 || index1 == this.map.size()) {
                return 1;
            }
            return 0;
        }

        return Integer.compare(index1, index2);
    }

    private int getOrder(final T object) {
        Integer order = map.get(object);
        if (order == null) {
            order = this.atEndIfMiss ? this.map.size() : -1;
        }
        return order;
    }
}
