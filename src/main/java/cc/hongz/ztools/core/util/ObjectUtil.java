package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.comparator.CompareUtil;
import cc.hongz.ztools.core.exceptions.UtilException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 对象工具类，包括判空、克隆、序列化等操作
 *
 * @author Looly
 */
public class ObjectUtil {

	/**
	 * 比较两个对象是否相等，此方法是 {@link #equal(Object, Object)}的别名方法。
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 * @see #equal(Object, Object)
	 */
	public static boolean equals(Object obj1, Object obj2) {
		return equal(obj1, obj2);
	}

	/**
	 * 比较两个对象是否相等。
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 */
	public static boolean equal(Object obj1, Object obj2) {
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return NumberUtil.equals((Number) obj1, (Number) obj2);
		}
		return Objects.equals(obj1, obj2);
	}

	/**
	 * 比较两个对象是否不相等。
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否不等
	 */
	public static boolean notEqual(Object obj1, Object obj2) {
		return false == equal(obj1, obj2);
	}

	/**
	 * 计算对象长度。
	 *
	 * @param obj 被计算长度的对象
	 * @return 长度
	 */
	public static int length(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).size();
		}

		int count;
		if (obj instanceof Iterator) {
			final Iterator<?> iter = (Iterator<?>) obj;
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj instanceof Enumeration) {
			final Enumeration<?> enumeration = (Enumeration<?>) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj);
		}
		return -1;
	}

	/**
	 * 对象中是否包含元素。
	 *
	 * @param obj     对象
	 * @param element 元素
	 * @return 是否包含
	 */
	public static boolean contains(Object obj, Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof CharSequence) {
			if (!(element instanceof CharSequence)) {
				return false;
			}
			String elementStr;
			try {
				elementStr = element.toString();
				if (elementStr == null) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
			return obj.toString().contains(elementStr);
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).containsValue(element);
		}

		if (obj instanceof Iterator) {
			final Iterator<?> iter = (Iterator<?>) obj;
			while (iter.hasNext()) {
				final Object o = iter.next();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			final Enumeration<?> enumeration = (Enumeration<?>) obj;
			while (enumeration.hasMoreElements()) {
				final Object o = enumeration.nextElement();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj.getClass().isArray()) {
			final int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				final Object o = Array.get(obj, i);
				if (equal(o, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查对象是否为null。
	 *
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNull(Object obj) {
		return null == obj || obj.equals(null);
	}

	/**
	 * 检查对象是否不为null。
	 *
	 * @param obj 对象
	 * @return 是否为非null
	 */
	public static boolean isNotNull(Object obj) {
		return null != obj && false == obj.equals(null);
	}

	/**
	 * 判断指定对象是否为空。
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}

		if (obj instanceof CharSequence) {
			return StrUtil.isEmpty((CharSequence) obj);
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		} else if (obj instanceof Iterable) {
			return !((Iterable) obj).iterator().hasNext();
		} else if (obj instanceof Iterator) {
			return !((Iterator) obj).hasNext();
		} else if (obj.getClass().isArray()) {
			return 0 == Array.getLength(obj);
		}

		return false;
	}

	/**
	 * 判断指定对象是否为非空。
	 *
	 * @param obj 被判断的对象
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Object obj) {
		return false == isEmpty(obj);
	}

	/**
	 * 如果给定对象为{@code null}返回默认值。
	 *
	 * @param <T>          对象类型
	 * @param object       被检查对象
	 * @param defaultValue 默认值
	 * @return 默认值或原值
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isNull(object) ? defaultValue : object;
	}

	/**
	 * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）。
	 *
	 * @param source               被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 */
	public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
		if (isNull(source)) {
			return defaultValueSupplier.get();
		}
		return source;
	}

	/**
	 * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）。
	 *
	 * @param source               被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 */
	public static <T> T defaultIfNull(T source, Function<T, ? extends T> defaultValueSupplier) {
		if (isNull(source)) {
			return defaultValueSupplier.apply(null);
		}
		return source;
	}

	/**
	 * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值。
	 *
	 * @param source       对象
	 * @param handle       非空时自定义的处理方法
	 * @param defaultValue 默认为空的返回值
	 * @param <T>          返回类型
	 * @param <R>          被检查的对象类型
	 * @return 处理后的返回值
	 */
	public static <T, R> T defaultIfNull(R source, Function<R, ? extends T> handle, final T defaultValue) {
		if (isNotNull(source)) {
			return handle.apply(source);
		}
		return defaultValue;
	}

	/**
	 * 如果指定的对象不为 {@code null},则应用提供的映射函数并返回结果,否则返回 {@code null}。
	 *
	 * @param source  要检查的对象
	 * @param handler 要应用的映射函数
	 * @param <T>     输入对象的类型
	 * @param <R>     映射函数的返回类型
	 * @return 映射函数的结果
	 */
	public static <T, R> R apply(final T source, final Function<T, R> handler) {
		return defaultIfNull(source, handler, null);
	}

	/**
	 * 如果指定的对象不为 {@code null},则执行{@link Consumer}处理source。
	 *
	 * @param source   要检查的对象
	 * @param consumer source处理逻辑
	 * @param <T>      输入对象的类型
	 */
	public static <T> void accept(final T source, final Consumer<T> consumer) {
		if (null != source) {
			consumer.accept(source);
		}
	}

	/**
	 * 如果给定对象为{@code null}或者""返回默认值。
	 *
	 * @param str          String 类型
	 * @param handle       自定义的处理方法
	 * @param defaultValue 默认为空的返回值
	 * @param <T>          返回类型
	 * @return 处理后的返回值
	 */
	public static <T> T defaultIfEmpty(String str, Function<CharSequence, ? extends T> handle, final T defaultValue) {
		if (StrUtil.isNotEmpty(str)) {
			return handle.apply(str);
		}
		return defaultValue;
	}

	/**
	 * 如果给定对象为{@code null}或者 "" 返回默认值。
	 *
	 * @param <T>          对象类型
	 * @param str          被检查对象
	 * @param defaultValue 默认值
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultValue) {
		return StrUtil.isEmpty(str) ? defaultValue : str;
	}

	/**
	 * 如果被检查对象为 {@code null} 或 "" 时，返回默认值（由 defaultValueSupplier 提供）。
	 *
	 * @param str                  被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfEmpty(T str, Supplier<? extends T> defaultValueSupplier) {
		if (StrUtil.isEmpty(str)) {
			return defaultValueSupplier.get();
		}
		return str;
	}

	/**
	 * 如果被检查对象为 {@code null} 或 "" 时，返回默认值（由 defaultValueSupplier 提供）。
	 *
	 * @param str                  被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfEmpty(T str, Function<T, ? extends T> defaultValueSupplier) {
		if (StrUtil.isEmpty(str)) {
			return defaultValueSupplier.apply(null);
		}
		return str;
	}

	/**
	 * 如果给定对象为{@code null}或者""或者空白符返回默认值。
	 *
	 * @param <T>          对象类型
	 * @param str          被检查对象
	 * @param defaultValue 默认值
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultValue) {
		return StrUtil.isBlank(str) ? defaultValue : str;
	}

	/**
	 * 如果被检查对象为 {@code null} 或 "" 或 空白字符串时，返回默认值。
	 *
	 * @param str                  被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfBlank(T str, Supplier<? extends T> defaultValueSupplier) {
		if (StrUtil.isBlank(str)) {
			return defaultValueSupplier.get();
		}
		return str;
	}

	/**
	 * 如果被检查对象为 {@code null} 或 "" 或 空白字符串时，返回默认值。
	 *
	 * @param str                  被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 默认值或原值
	 */
	public static <T extends CharSequence> T defaultIfBlank(T str, Function<T, ? extends T> defaultValueSupplier) {
		if (StrUtil.isBlank(str)) {
			return defaultValueSupplier.apply(null);
		}
		return str;
	}

	/**
	 * 克隆对象。
	 * 如果对象实现Cloneable接口，调用其clone方法；
	 * 如果实现Serializable接口，执行深度克隆；
	 * 否则返回{@code null}
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 */
	public static <T> T clone(T obj) {
		T result = ArrayUtil.clone(obj);
		if (null == result) {
			if (obj instanceof Cloneable) {
				result = ReflectUtil.invoke(obj, "clone");
			} else {
				result = cloneByStream(obj);
			}
		}
		return result;
	}

	/**
	 * 返回克隆后的对象，如果克隆失败，返回原对象。
	 *
	 * @param <T> 对象类型
	 * @param obj 对象
	 * @return 克隆后或原对象
	 */
	public static <T> T cloneIfPossible(final T obj) {
		T clone = null;
		try {
			clone = clone(obj);
		} catch (Exception e) {
			// pass
		}
		return clone == null ? obj : clone;
	}

	/**
	 * 序列化后拷贝流的方式克隆。
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneByStream(T obj) {
		return (T) deserialize(serialize(obj));
	}

	/**
	 * 序列化。
	 *
	 * @param <T> 对象类型
	 * @param obj 要被序列化的对象
	 * @return 序列化后的字节码
	 */
	public static <T> byte[] serialize(T obj) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
			oos.flush();
			return bos.toByteArray();
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 反序列化。
	 *
	 * @param <T>   对象类型
	 * @param bytes 反序列化的字节码
	 * @return 反序列化后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInputStream ois = new ObjectInputStream(bis)) {
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 是否为基本类型，包括包装类型和非包装类型。
	 *
	 * @param object 被检查对象
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Object object) {
		if (null == object) {
			return false;
		}
		return ClassUtil.isBasicType(object.getClass());
	}

	/**
	 * 检查是否为有效的数字。
	 *
	 * @param obj 被检查类型
	 * @return 检查结果
	 */
	public static boolean isValidIfNumber(Object obj) {
		if (obj instanceof Number) {
			return NumberUtil.isValidNumber((Number) obj);
		}
		return true;
	}

	/**
	 * {@code null}安全的对象比较，{@code null}对象排在末尾。
	 *
	 * @param <T> 被比较对象类型
	 * @param c1  对象1
	 * @param c2  对象2
	 * @return 比较结果
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
		return CompareUtil.compare(c1, c2);
	}

	/**
	 * {@code null}安全的对象比较。
	 *
	 * @param <T>           被比较对象类型
	 * @param c1            对象1
	 * @param c2            对象2
	 * @param nullGreater  当被比较对象为null时是否排在前面
	 * @return 比较结果
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
		return CompareUtil.compare(c1, c2, nullGreater);
	}

	/**
	 * 获得给定类的第一个泛型参数。
	 *
	 * @param obj 被检查的对象
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Object obj) {
		return getTypeArgument(obj, 0);
	}

	/**
	 * 获得给定类的泛型参数。
	 *
	 * @param obj   被检查的对象
	 * @param index 泛型类型的索引号
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Object obj, int index) {
		return ClassUtil.getTypeArgument(obj.getClass(), index);
	}

	/**
	 * 将Object转为String。
	 *
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return StrUtil.NULL;
		}
		if (obj instanceof Map) {
			return obj.toString();
		}

		return cc.hongz.ztools.core.text.Convert.toStr(obj);
	}

	/**
	 * 存在多少个{@code null}或空对象。
	 *
	 * @param objs 被检查的对象
	 * @return 存在{@code null}的数量
	 */
	public static int emptyCount(Object... objs) {
		return ArrayUtil.emptyCount(objs);
	}

	/**
	 * 是否存在{@code null}对象。
	 *
	 * @param objs 被检查对象
	 * @return 是否存在
	 */
	public static boolean hasNull(Object... objs) {
		return ArrayUtil.hasNull(objs);
	}

	/**
	 * 是否存在{@code null}或空对象。
	 *
	 * @param objs 被检查对象
	 * @return 是否存在
	 */
	public static boolean hasEmpty(Object... objs) {
		return ArrayUtil.hasEmpty(objs);
	}

	/**
	 * 是否全都为{@code null}或空对象。
	 *
	 * @param objs 被检查的对象
	 * @return 是否都为空
	 */
	public static boolean isAllEmpty(Object... objs) {
		return ArrayUtil.isAllEmpty(objs);
	}

	/**
	 * 是否全都不为{@code null}或空对象。
	 *
	 * @param objs 被检查的对象
	 * @return 是否都不为空
	 */
	public static boolean isAllNotEmpty(Object... objs) {
		return ArrayUtil.isAllNotEmpty(objs);
	}
}
