package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.convert.Converter;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 各种集合类转换器
 *
 * @author Looly
 * @since 3.0.8
 */
public class CollectionConverter implements Converter<Collection<?>> {

	private final Type collectionType;
	private final Type elementType;

	public CollectionConverter() {
		this(Collection.class);
	}

	public CollectionConverter(Type collectionType) {
		this(collectionType, TypeUtil.getTypeArgument(collectionType));
	}

	public CollectionConverter(Class<?> collectionType) {
		this(collectionType, TypeUtil.getTypeArgument(collectionType));
	}

	public CollectionConverter(Type collectionType, Type elementType) {
		this.collectionType = collectionType;
		this.elementType = elementType;
	}

	@Override
	public Collection<?> convert(Object value, Collection<?> defaultValue) throws IllegalArgumentException {
		final Collection<?> result = convertInternal(value);
		return ObjectUtil.defaultIfNull(result, defaultValue);
	}

	protected Collection<?> convertInternal(Object value) {
		final Class<?> collectionClass = TypeUtil.getClass(this.collectionType);
		final Class<?> elementClass = TypeUtil.getClass(this.elementType);
		final Collection collection = CollUtil.create(collectionClass, elementClass);
		if (value instanceof Collection) {
			collection.addAll((Collection) value);
		} else {
			collection.add(value);
		}
		return collection;
	}
}
