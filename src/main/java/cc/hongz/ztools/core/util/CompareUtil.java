package cc.hongz.ztools.core.util;

/**
 * 比较工具类，提供null安全的对象比较.
 *
 * @author looly
 */
public class CompareUtil {

	/**
	 * {@code null}安全的对象比较，{@code null}对象排在末尾
	 *
	 * @param <T> 被比较对象类型
	 * @param c1  对象1，可以为{@code null}
	 * @param c2  对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T> int compare(T c1, T c2) {
		return compare(c1, c2, false);
	}

	/**
	 * {@code null}安全的对象比较
	 *
	 * @param <T>           被比较对象类型
	 * @param c1            对象1，可以为{@code null}
	 * @param c2            对象2，可以为{@code null}
	 * @param nullGreater  当被比较对象为null时是否排在前面
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T> int compare(T c1, T c2, boolean nullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return nullGreater ? -1 : 1;
		} else if (c2 == null) {
			return nullGreater ? 1 : -1;
		} else if (c1 instanceof Comparable) {
			return ((Comparable) c1).compareTo(c2);
		} else {
			throw new IllegalArgumentException("Both objects must implement Comparable or be null");
		}
	}
}
