package cc.hongz.ztools.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 对象工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，提供空值默认值处理.
 * @author hongzhe.qin
 */
public final class ObjectUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private ObjectUtil() {
    }

    /**
     * 对象为 null 时返回默认值.
     *
     * @param object       原对象
     * @param defaultValue 默认值
     * @param <T>          对象类型
     * @return 原对象或默认值
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * 对象是否为 null.
     *
     * @param object 对象
     * @return 是否为 null
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

    /**
     * 对象是否不为 null.
     *
     * @param object 对象
     * @return 是否不为 null
     */
    public static boolean isNotNull(final Object object) {
        return object != null;
    }

    /**
     * 判断对象是否为空（null、空字符串、空集合、空 Map、空数组等）.
     *
     * @param object 对象
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(final Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        }
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map) object).isEmpty();
        }
        if (object.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(object) == 0;
        }
        return false;
    }

    /**
     * 比较两个对象是否相等.
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equal(final Object obj1, final Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 比较两个对象是否不相等.
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否不相等
     */
    public static boolean notEqual(final Object obj1, final Object obj2) {
        return !Objects.equals(obj1, obj2);
    }
}
