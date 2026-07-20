package cc.hongz.ztools.core.collection;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Enumeration}对象转{@link Iterator}对象.
 *
 * @param <E> 元素类型
 * @author looly
 */
public class EnumerationIter<E> implements Iterator<E>, Iterable<E> {

	private final Enumeration<E> enumeration;

	/**
	 * 构造
	 *
	 * @param enumeration {@link Enumeration}
	 */
	public EnumerationIter(final Enumeration<E> enumeration) {
		this.enumeration = enumeration;
	}

	@Override
	public boolean hasNext() {
		return enumeration != null && enumeration.hasMoreElements();
	}

	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return enumeration.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}
}
