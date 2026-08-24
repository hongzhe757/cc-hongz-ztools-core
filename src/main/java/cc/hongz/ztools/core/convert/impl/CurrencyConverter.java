package cc.hongz.ztools.core.convert.impl;

import java.util.Currency;

import cc.hongz.ztools.core.convert.AbstractConverter;

/**
 * 货币{@link Currency} 转换器。
 *
 * @author Looly
 * @since 3.0.8
 */
public class CurrencyConverter extends AbstractConverter<Currency> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected Currency convertInternal(final Object value) {
        return Currency.getInstance(convertToStr(value));
    }

}
