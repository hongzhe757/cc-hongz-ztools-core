package cc.hongz.ztools.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

@DisplayName("ZoneUtil 时区工具")

public class ZoneUtilTest {
    @Test
    @DisplayName("转时区")
    public void toTest() {
        assertEquals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
        assertEquals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
    }
}
