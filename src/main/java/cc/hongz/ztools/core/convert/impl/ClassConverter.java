package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.util.ClassLoaderUtil;

/**
 * 类转换器。
 * 将类名转换为类，默认初始化这个类（执行static块）
 *
 * @author Looly
 */
public class ClassConverter extends AbstractConverter<Class<?>> {
    private static final long serialVersionUID = 1L;

    private final boolean isInitialized;

    /**
     * 构造。
     */
    public ClassConverter() {
        this(true);
    }

    /**
     * 构造。
     *
     * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
     * @since 5.5.0
     */
    public ClassConverter(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * 内部转换。
     */
    @Override
    protected Class<?> convertInternal(final Object value) {
        return ClassLoaderUtil.loadClass(convertToStr(value), isInitialized);
    }

}
