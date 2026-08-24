package cc.hongz.ztools.core.convert.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.util.BooleanUtil;

/**
 * {@link AtomicBoolean}转换器。
 *
 * @author Looly
 * @since 3.0.8
 */
public class AtomicBooleanConverter extends AbstractConverter<AtomicBoolean> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected AtomicBoolean convertInternal(final Object value) {
        if (value instanceof Boolean) {
            return new AtomicBoolean((Boolean) value);
        }
        final String valueStr = convertToStr(value);
        return new AtomicBoolean(BooleanUtil.toBoolean(valueStr));
    }

}
