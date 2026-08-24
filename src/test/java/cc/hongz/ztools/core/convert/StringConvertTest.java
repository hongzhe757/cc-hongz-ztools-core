package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.date.TimeZoneUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("StringConvert 字符串转换")
public class StringConvertTest {

    @Test
    @DisplayName("时区转字符串")
    public void timezoneToStrTest() {
        final String s = Convert.toStr(TimeZoneUtil.getTimeZone("Asia/Shanghai"));
        assertEquals("Asia/Shanghai", s);
    }
}
