package cc.hongz.ztools.core.collection;

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
