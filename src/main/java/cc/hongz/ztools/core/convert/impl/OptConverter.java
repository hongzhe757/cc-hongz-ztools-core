package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.lang.Opt;

/**
 *
 * {@link Opt}对象转换器。
 *
 * @author Looly
 * @since 5.7.16
 */
public class OptConverter extends AbstractConverter<Opt<?>> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected Opt<?> convertInternal(final Object value) {
        return Opt.ofNullable(value);
    }

}
