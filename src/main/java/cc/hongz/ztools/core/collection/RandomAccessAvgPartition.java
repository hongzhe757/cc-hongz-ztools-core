package cc.hongz.ztools.core.collection;

import java.util.List;
import java.util.RandomAccess;

/**
 * 列表分区或分段（可随机访问列表）.
 *
 * @param <T> 元素类型
 * @author looly
 */
public class RandomAccessAvgPartition<T> extends AvgPartition<T> implements RandomAccess {

    /**
     * 列表分区.
     *
     * @param list  被分区的列表
     * @param limit 分区个数
     */
    public RandomAccessAvgPartition(final List<T> list, final int limit) {
        super(list, limit);
    }
}
