package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.date.TimeZoneUtil;

import java.util.TimeZone;

/**
 * TimeZone转换器。
 * @author Looly
 *
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected TimeZone convertInternal(final Object value) {
        return TimeZoneUtil.getTimeZone(convertToStr(value));
    }

}
