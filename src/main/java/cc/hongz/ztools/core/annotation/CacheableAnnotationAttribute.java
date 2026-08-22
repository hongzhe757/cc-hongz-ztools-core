package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@link AnnotationAttribute}的基本实现.
 *
 * @author huangchengxing
 */
public class CacheableAnnotationAttribute implements AnnotationAttribute {

    private volatile boolean valueInvoked;
    private volatile Object value;

    private boolean defaultValueInvoked;
    private Object defaultValue;

    private final Annotation annotation;
    private final Method attribute;

    /**
     * 构造.
     *
     * @param annotation 注解
     * @param attribute  注解属性
     */
    public CacheableAnnotationAttribute(final Annotation annotation,  final Method attribute) {
        Assert.notNull(annotation, "annotation must not null");
        Assert.notNull(attribute, "attribute must not null");
        this.annotation = annotation;
        this.attribute = attribute;
        this.valueInvoked = false;
        this.defaultValueInvoked = false;
    }

    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }

    @Override
    public Method getAttribute() {
        return this.attribute;
    }

    @Override
    public Object getValue() {
        if (!valueInvoked) {
            synchronized (this) {
                if (!valueInvoked) {
                    valueInvoked = true;
                    value = ReflectUtil.invoke(annotation, attribute);
                }
            }
        }
        return value;
    }

    @Override
    public boolean isValueEquivalentToDefaultValue() {
        if (!defaultValueInvoked) {
            defaultValue = attribute.getDefaultValue();
            defaultValueInvoked = true;
        }
        return ObjectUtil.equals(getValue(), defaultValue);
    }

}
