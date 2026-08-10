package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.convert.Convert;
import cc.hongz.ztools.core.util.NumberUtil;

import java.lang.reflect.Type;

/**
 * 区间片段接口（Hutool 阶段 1 迁入）.
 * <p>
 * 表示文本、集合等数据结构的一个下标区间；默认实现见 {@link DefaultSegment}.
 *
 * @param <T> 下标数字类型
 * @author looly
 * @since 5.5.3
 */
public interface Segment<T extends Number> {

	/**
	 * 获取起始位置
	 *
	 * @return 起始位置
	 */
	T getStartIndex();

	/**
	 * 获取结束位置
	 *
	 * @return 结束位置
	 */
	T getEndIndex();

	/**
	 * 计算片段长度，默认为 {@code |end - start|}.
	 *
	 * @return 区间长度
	 */
	default T length(){
		final T start = Assert.notNull(getStartIndex(), "Start index must be not null!");
		final T end = Assert.notNull(getEndIndex(), "End index must be not null!");
		return Convert.convert((Type) start.getClass(), NumberUtil.sub(end, start).abs());
	}
}
