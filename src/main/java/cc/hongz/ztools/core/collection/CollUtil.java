package cc.hongz.ztools.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 集合工具类.
 *
 * @author looly
 */
public final class CollUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private CollUtil() {
    }

    /**
     * 集合是否为空.
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 获取集合的第一个元素.
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return 第一个元素，集合为空返回null
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        if (iterable instanceof Collection) {
            Collection<T> collection = (Collection<T>) iterable;
            if (collection.isEmpty()) {
                return null;
            }
            return collection.iterator().next();
        }
        Iterator<T> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * 获取集合的第一个元素.
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return 第一个元素，迭代器为空返回null
     */
    public static <T> T getFirst(Iterator<T> iterator) {
        return (iterator != null && iterator.hasNext()) ? iterator.next() : null;
    }

    /**
     * 数组转为ArrayList.
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... values) {
        ArrayList<T> list = new ArrayList<>(values.length);
        for (T value : values) {
            list.add(value);
        }
        return list;
    }

    /**
     * 将集合中的元素拼接为字符串.
     *
     * @param collection  集合
     * @param conjunction 分隔符
     * @return 拼接后的字符串
     */
    public static String join(final Collection<?> collection, final CharSequence conjunction) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final Iterator<?> iterator = collection.iterator();
        boolean first = true;
        while (iterator.hasNext()) {
            final Object item = iterator.next();
            if (item == null) {
                continue;
            }
            if (!first) {
                if (conjunction != null) {
                    sb.append(conjunction);
                }
            } else {
                first = false;
            }
            sb.append(item);
        }
        return sb.toString();
    }
}
