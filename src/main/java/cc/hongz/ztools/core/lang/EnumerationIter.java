package cc.hongz.ztools.core.lang;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 将 {@link Enumeration} 包装为 {@link Iterator}，从而支持 for-each 循环.
 *
 * @param <E> 元素类型
 * @author looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 */
public class EnumerationIter<E> implements Iterator<E>, Iterable<E> {

    private final Enumeration<E> enumeration;

    /**
     * 构造.
     *
     * @param enumeration 被包装的 Enumeration
     */
    public EnumerationIter(final Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return enumeration.nextElement();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}
