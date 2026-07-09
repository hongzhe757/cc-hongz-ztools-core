package cc.hongz.ztools.core.lang;

/**
 * 过滤器函数式接口（Hutool 阶段 1 迁入）.
 * <p>
 * 用于集合、IO 等场景的谓词式过滤.
 *
 * @author Looly
 */
@FunctionalInterface
public interface Filter<T> {

	/**
	 * 是否接受给定对象.
	 *
	 * @param t 待检查对象
	 * @return 接受返回 true，否则 false
	 */
	boolean accept(T t);
}