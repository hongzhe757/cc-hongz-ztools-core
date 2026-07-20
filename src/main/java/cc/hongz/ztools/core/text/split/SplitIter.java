package cc.hongz.ztools.core.text.split;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.text.finder.TextFinder;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * 字符串切分迭代器<br>
 * 此迭代器是字符串切分的懒模式实现，实例化后不完成切分，只有调用{@link #hasNext()}或遍历时才完成切分<br>
 * 此迭代器非线程安全
 *
 * @author looly
 * @since 5.7.14
 */
public class SplitIter implements Iterator<String>, Serializable {
	private static final long serialVersionUID = 1L;

	private final String text;
	private final TextFinder finder;
	private final int limit;
	private final boolean ignoreEmpty;

	/**
	 * 上一次的结束位置
	 */
	private int offset;
	/**
	 * 计数器，用于判断是否超过limit
	 */
	private int count;

	private String next;
	private boolean finished;

	/**
	 * 构造
	 *
	 * @param text            文本，不能为{@code null}
	 * @param separatorFinder 分隔符匹配器
	 * @param limit           限制数量，小于等于0表示无限制
	 * @param ignoreEmpty     是否忽略""
	 */
	public SplitIter(CharSequence text, TextFinder separatorFinder, int limit, boolean ignoreEmpty) {
		Assert.notNull(text, "Text must be not null!");
		this.text = text.toString();
		this.finder = separatorFinder.setText(text);
		this.limit = limit > 0 ? limit : Integer.MAX_VALUE;
		this.ignoreEmpty = ignoreEmpty;
	}

	/**
	 * 计算新的节点
	 *
	 * @return 节点值
	 */
	protected String computeNext() {
		// 达到数量上限或末尾，结束
		if (count >= limit || offset > text.length()) {
			return null;
		}

		// 达到数量上限
		if (count == (limit - 1)) {
			// 当到达限制次数时，最后一个元素为剩余部分
			if (ignoreEmpty && offset == text.length()) {
				// 最后一个是空串
				return null;
			}

			// 结尾整个作为一个元素
			count++;
			return text.substring(offset);
		}

		String result = null;
		int start;
		do {
			start = finder.start(offset);
			// 无分隔符，结束
			if (start < 0) {
				// 如果不再有分隔符，但是遗留了字符，则单独作为一个段
				if (offset <= text.length()) {
					result = text.substring(offset);
					if (!ignoreEmpty || !result.isEmpty()) {
						// 返回非空串
						offset = Integer.MAX_VALUE;
						return result;
					}
				}
				return null;
			}

			// 找到新的分隔符位置
			result = text.substring(offset, start);
			offset = finder.end(start);
		} while (ignoreEmpty && result.isEmpty()); // 空串则继续循环

		count++;
		return result;
	}

	@Override
	public boolean hasNext() {
		if (null != next) {
			return true;
		} else if (finished) {
			return false;
		}

		String result = computeNext();
		if (null == result) {
			this.finished = true;
			return false;
		} else {
			this.next = result;
			return true;
		}
	}

	@Override
	public String next() {
		if (false == hasNext()) {
			throw new NoSuchElementException("No more lines");
		}

		String result = this.next;
		this.next = null;
		return result;
	}

	/**
	 * 手动结束遍历器
	 */
	public void finish(){
		this.finished = true;
		this.next = null;
	}

	/**
	 * 重置状态，用于再次遍历
	 */
	public void resetState() {
		this.finished = false;
		this.next = null;
	}

	/**
	 * 重置
	 */
	public void reset() {
		this.finder.reset();
		this.offset = 0;
		this.count = 0;
		resetState();
	}

	/**
	 * 获取切分后的对象数组
	 *
	 * @param trim 是否去除元素两边空格
	 * @return 切分后的列表
	 */
	public String[] toArray(boolean trim) {
		return toList(trim).toArray(new String[0]);
	}

	/**
	 * 获取切分后的对象列表
	 *
	 * @param trim 是否去除元素两边空格
	 * @return 切分后的列表
	 */
	public List<String> toList(boolean trim) {
		return toList((str) -> trim ? StrUtil.trim(str) : str);
	}

	/**
	 * 获取切分后的对象列表
	 *
	 * @param <T>     元素类型
	 * @param mapping 字符串映射函数
	 * @return 切分后的列表
	 */
	public <T> List<T> toList(Function<String, T> mapping) {
		final List<T> result = new ArrayList<>();
		while (this.hasNext()) {
			final T apply = mapping.apply(this.next());
			if (ignoreEmpty && StrUtil.isEmptyIfStr(apply)) {
				// 对于mapping之后依旧是String的情况，ignoreEmpty依旧有效
				continue;
			}
			result.add(apply);
		}
		if (result.isEmpty()) {
			return new ArrayList<>(0);
		}
		return result;
	}
}
