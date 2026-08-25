package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@DisplayName("Issue3608 解析日期")

public class Issue3608Test {
    @Test
    @Disabled
    @DisplayName("解析日期")
    public void parseTest() throws ParseException {
        String dateTime = "1940-06-01 00:00:00";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Date parse = simpleDateFormat.parse(dateTime);
        Console.log(parse);
    }
}
