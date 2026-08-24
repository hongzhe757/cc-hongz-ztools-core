package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.convert.Converter;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 各种集合类转换器。
 *
 * @author Looly
 * @since 3.0.8
 */
public class CollectionConverter implements Converter<Collection<?>> {

    private final Type collectionType;
    private final Type elementType;

    /**
     * 构造。
     */
    public CollectionConverter() {
        this(Collection.class);
    }

    /**
     * 构造。
     *
     * @param collectionType 集合类型
     */
    public CollectionConverter(final Type collectionType) {
        this(collectionType, TypeUtil.getTypeArgument(collectionType));
    }

    /**
     * 构造。
     *
     * @param collectionType 集合类型
     */
    public CollectionConverter(final Class<?> collectionType) {
        this(collectionType, TypeUtil.getTypeArgument(collectionType));
    }

    /**
     * 构造。
     *
     * @param collectionType 集合类型
     * @param elementType    元素类型
     */
    public CollectionConverter(final Type collectionType, final Type elementType) {
        this.collectionType = collectionType;
        this.elementType = elementType;
    }

    /**
     * 转换。
     */
    @Override
    public Collection<?> convert(final Object value, final Collection<?> defaultValue) throws IllegalArgumentException {
        final Collection<?> result = convertInternal(value);
        return ObjectUtil.defaultIfNull(result, defaultValue);
    }

    /**
     * 内部转换。
     */
    @SuppressWarnings("unchecked")
    protected Collection<?> convertInternal(final Object value) {
        final Class<?> collectionClass = TypeUtil.getClass(this.collectionType);
        final Class<?> elementClass = TypeUtil.getClass(this.elementType);
        final Collection collection = CollUtil.create(collectionClass, elementClass);

        if (value instanceof Collection) {
            collection.addAll((Collection) value);
        } else if (ArrayUtil.isArray(value)) {
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                collection.add(Array.get(value, i));
            }
        } else if (value instanceof CharSequence) {
            java.util.List<String> parts = StrUtil.split(value.toString(), ',');
            for (String part : parts) {
                collection.add(part);
            }
        } else {
            collection.add(value);
        }
        return collection;
    }
}
