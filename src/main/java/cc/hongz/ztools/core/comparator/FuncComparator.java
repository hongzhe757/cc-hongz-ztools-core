package cc.hongz.ztools.core.comparator;

import java.util.function.Function;

/**
 * 指定函数排序器.
 *
 * @param <T> 被比较的对象
 * @author looly
 */
public class FuncComparator<T> extends NullComparator<T> {
    private static final long serialVersionUID = 1L;

    /**
     * 构造.
     *
     * @param nullGreater 是否{@code null}在后
     * @param func        比较项获取函数
     */
    public FuncComparator(final boolean nullGreater, final Function<T, Comparable<?>> func) {
        this(nullGreater, true, func);
    }

    /**
     * 构造.
     *
     * @param nullGreater 是否{@code null}在后
     * @param compareSelf 在字段值相同情况下，是否比较对象本身
     * @param func        比较项获取函数
     */
    public FuncComparator(final boolean nullGreater, final boolean compareSelf, final Function<T, Comparable<?>> func) {
        super(nullGreater, (a, b) -> {
            final Comparable<?> v1;
            final Comparable<?> v2;
            try {
                v1 = func.apply(a);
                v2 = func.apply(b);
            } catch (final Exception e) {
                throw new ComparatorException(e);
            }

            int result = CompareUtil.compare(v1, v2, nullGreater);
            if (compareSelf && 0 == result) {
                result = CompareUtil.compare(a, b, nullGreater);
            }
            return result;
        });
    }
}
