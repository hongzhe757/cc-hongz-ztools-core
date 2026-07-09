package cc.hongz.ztools.core.lang;

/**
 * {@link Segment} 默认实现（Hutool 阶段 1 迁入）.
 *
 * @param <T> 下标数字类型
 * @author looly
 * @since 5.5.3
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	/**
	 * 起始下标.
	 */
	protected T startIndex;

	/**
	 * 结束下标.
	 */
	protected T endIndex;

	/**
	 * 构造区间片段.
	 *
	 * @param startIndex 起始下标
	 * @param endIndex   结束下标
	 */
	public DefaultSegment(T startIndex, T endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public T getStartIndex() {
		return this.startIndex;
	}

	@Override
	public T getEndIndex() {
		return this.endIndex;
	}
}
