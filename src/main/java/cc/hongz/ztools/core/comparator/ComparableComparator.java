package cc.hongz.ztools.core.comparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 针对 {@link Comparable}对象的默认比较器.
 *
 * @param <E> 比较对象类型
 * @author Looly
 */
public class ComparableComparator<E extends Comparable<? super E>> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = 3020871676147289162L;

    /** 单例。 */
    @SuppressWarnings("rawtypes")
    public static final ComparableComparator INSTANCE = new ComparableComparator<>();

    @Override
    public int compare(final E obj1, final E obj2) {
        return obj1.compareTo(obj2);
    }

    @Override
    public int hashCode() {
        return "ComparableComparator".hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || null != object && object.getClass().equals(this.getClass());
    }
}
