package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TimeZoneUtil 单元测试.
 */
@DisplayName("TimeZoneUtil 时区工具")
class TimeZoneUtilTest {

    @Test
    @DisplayName("获取时区")
    void getTimeZone() {
        assertEquals(TimeZone.getTimeZone("GMT+8:00"), TimeZoneUtil.getTimeZone("GMT+8:00"));
        assertEquals("GMT", TimeZoneUtil.GMT.getID());
    }

    @Test
    @DisplayName("转时区")
    void toTimeZone() {
        assertEquals(TimeZone.getDefault(), TimeZoneUtil.toTimeZone(null));
        final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        assertEquals(tz, TimeZoneUtil.toTimeZone(tz));
    }
}
