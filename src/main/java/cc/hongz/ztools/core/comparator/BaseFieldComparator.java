package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.ReflectUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Bean字段排序器.
 * <p>
 * 参阅feilong-core中的PropertyComparator。
 *
 * @param <T> 被比较的Bean
 * @author jiangzeyin
 * @deprecated 此类不再需要，使用FuncComparator代替更加灵活
 */
@Deprecated
public abstract class BaseFieldComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = -3482464782340308755L;

    /**
     * 比较两个对象的同一个字段值。
     *
     * @param o1    对象1
     * @param o2    对象2
     * @param field 字段
     * @return 比较结果
     */
    protected int compareItem(final T o1, final T o2, final Field field) {
        if (o1 == o2) {
            return 0;
        } else if (null == o1) { // null 排在后面
            return 1;
        } else if (null == o2) {
            return -1;
        }

        Comparable<?> v1;
        Comparable<?> v2;
        try {
            v1 = (Comparable<?>) ReflectUtil.getFieldValue(o1, field);
            v2 = (Comparable<?>) ReflectUtil.getFieldValue(o2, field);
        } catch (Exception e) {
            throw new ComparatorException(e);
        }

        return compare(o1, o2, v1, v2);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private int compare(final T o1, final T o2, final Comparable fieldValue1, final Comparable fieldValue2) {
        int result = ObjectUtil.compare(fieldValue1, fieldValue2);
        if (0 == result) {
            //避免TreeSet / TreeMap 过滤掉排序字段相同但是对象不相同的情况
            result = CompareUtil.compare(o1, o2, true);
        }
        return result;
    }
}
