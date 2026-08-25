package cc.hongz.ztools.core.date;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cc.hongz.ztools.core.date.format.FastDateFormat;

@DisplayName("时区转换")

public class TimeZoneTest {

    @Test
    @DisplayName("时区转换")
    public void timeZoneConvertTest() {
        DateTime dt = DateUtil.parse("2018-07-10 21:44:32", //
                FastDateFormat.getInstance(DatePattern.NORM_DATETIME_PATTERN, TimeZone.getTimeZone("GMT+8:00")));
        assertEquals("2018-07-10 21:44:32", dt.toString());

        dt.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        int hour = dt.getField(DateField.HOUR_OF_DAY);
        assertEquals(14, hour);
        assertEquals("2018-07-10 14:44:32", dt.toString());
    }
}
