package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.lang.Assert;

import java.util.List;

/**
 * 列表分区或分段.<br>
 * 通过传入分区个数，将指定列表分区为不同的块，每块区域的长度均匀分布（个数差不超过1）.
 *
 * @param <T> 元素类型
 * @author looly
 */
public class AvgPartition<T> extends Partition<T> {

    final int limit;
    final int remainder;

    /**
     * 列表分区.
     *
     * @param list  被分区的列表
     * @param limit 分区个数
     */
    public AvgPartition(final List<T> list, final int limit) {
        super(list, list.size() / (limit <= 0 ? 1 : limit));
        Assert.isTrue(limit > 0, "Partition limit must be > 0");
        this.limit = limit;
        this.remainder = list.size() % limit;
    }

    @Override
    public List<T> get(final int index) {
        final int size = this.size;
        final int remainderValue = this.remainder;
        int start = index * size + Math.min(index, remainderValue);
        int end = start + size;
        if (index + 1 <= remainderValue) {
            end += 1;
        }
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return limit;
    }
}
