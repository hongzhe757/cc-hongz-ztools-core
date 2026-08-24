package cc.hongz.ztools.core.comparator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean属性排序器.
 * <p>
 * 支持读取Bean多层次下的属性.
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class PropertyComparator<T> extends FuncComparator<T> {
    private static final long serialVersionUID = 9157326766723846313L;

    /**
     * 构造.
     *
     * @param property 属性名
     */
    public PropertyComparator(final String property) {
        this(property, true);
    }

    /**
     * 构造.
     *
     * @param property      属性名
     * @param isNullGreater null值是否排在后（从小到大排序）
     */
    public PropertyComparator(final String property, final boolean isNullGreater) {
        this(property, true, isNullGreater);
    }

    /**
     * 构造.
     *
     * @param property      属性名
     * @param compareSelf   在字段值相同情况下，是否比较对象本身
     * @param isNullGreater null值是否排在后（从小到大排序）
     */
    public PropertyComparator(final String property, final boolean compareSelf, final boolean isNullGreater) {
        super(isNullGreater, compareSelf, (bean) -> getProperty(bean, property));
    }

    /**
     * 获取Bean的属性值，支持嵌套属性（用"."分隔）.
     *
     * @param bean     Bean对象，支持Map
     * @param property 属性名，支持嵌套如 "friend.name"
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    private static Comparable<?> getProperty(final Object bean, final String property) {
        if (null == bean || null == property || property.isEmpty()) {
            return null;
        }

        // 支持Map类型
        if (bean instanceof java.util.Map) {
            return (Comparable<?>) ((java.util.Map<?, ?>) bean).get(property);
        }

        // 支持嵌套属性
        if (property.contains(".")) {
            final String first = property.substring(0, property.indexOf('.'));
            final String rest = property.substring(property.indexOf('.') + 1);
            final Object nested = getProperty(bean, first);
            return getProperty(nested, rest);
        }

        // 尝试通过getter方法获取
        try {
            final String getterName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
            final Method method = bean.getClass().getMethod(getterName);
            return (Comparable<?>) method.invoke(bean);
        } catch (final Exception ignored) {
            // getter不存在，尝试isXxx
        }

        try {
            final String getterName = "is" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
            final Method method = bean.getClass().getMethod(getterName);
            return (Comparable<?>) method.invoke(bean);
        } catch (final Exception ignored) {
            // is getter也不存在，尝试直接字段访问
        }

        // 直接字段访问
        try {
            final Field field = bean.getClass().getDeclaredField(property);
            field.setAccessible(true);
            return (Comparable<?>) field.get(bean);
        } catch (final Exception e) {
            throw new ComparatorException(e);
        }
    }
}
