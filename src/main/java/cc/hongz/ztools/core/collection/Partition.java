package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.lang.Assert;

import java.util.AbstractList;
import java.util.List;

/**
 * 列表分区或分段.<br>
 * 通过传入分区长度，将指定列表分区为不同的块，每块区域的长度相同（最后一块可能小于长度）.<br>
 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
 *
 * @param <T> 元素类型
 * @author looly, guava
 */
public class Partition<T> extends AbstractList<List<T>> {

    protected final List<T> list;
    protected final int size;

    /**
     * 列表分区.
     *
     * @param list 被分区的列表，非空
     * @param size 每个分区的长度，必须&gt;0
     */
    public Partition(List<T> list, int size) {
        this.list = Assert.notNull(list);
        this.size = Math.min(list.size(), size);
    }

    @Override
    public List<T> get(int index) {
        final int start = index * size;
        final int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        final int size = this.size;
        if (0 == size) {
            return 0;
        }
        final int total = list.size();
        return (total + size - 1) / size;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
