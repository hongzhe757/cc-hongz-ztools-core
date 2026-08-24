package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.date.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

@DisplayName("TemporalAccessorConverter java.time 转换器")
public class TemporalAccessorConverterTest {

    @Test
    @DisplayName("转 Instant")
    public void toInstantTest() {
        String dateStr = "2019-02-18";

        // 通过转换获取的Instant为UTC时间
        Instant instant = Convert.convert(Instant.class, dateStr);
        Instant instant1 = DateUtil.parse(dateStr).toInstant();
        assertEquals(instant1, instant);
    }

    @Test
    @DisplayName("转 LocalDateTime")
    public void toLocalDateTimeTest() {
        LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, "2019-02-18");
        assertEquals("2019-02-18T00:00", localDateTime.toString());
    }

    @Test
    @DisplayName("转 LocalDate")
    public void toLocalDateTest() {
        LocalDate localDate = Convert.convert(LocalDate.class, "2019-02-18");
        assertEquals("2019-02-18", localDate.toString());
    }

    @Test
    @DisplayName("转 LocalTime")
    public void toLocalTimeTest() {
        LocalTime localTime = Convert.convert(LocalTime.class, "2019-02-18");
        assertEquals("00:00", localTime.toString());
    }

    @Test
    @DisplayName("转 ZonedDateTime")
    public void toZonedDateTimeTest() {
        ZonedDateTime zonedDateTime = Convert.convert(ZonedDateTime.class, "2019-02-18");
        assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
    }

    @Test
    @DisplayName("转 OffsetDateTime")
    public void toOffsetDateTimeTest() {
        OffsetDateTime zonedDateTime = Convert.convert(OffsetDateTime.class, "2019-02-18");
        assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
    }

    @Test
    @DisplayName("转 OffsetTime")
    public void toOffsetTimeTest() {
        OffsetTime offsetTime = Convert.convert(OffsetTime.class, "2019-02-18");
        assertEquals("00:00+08:00", offsetTime.toString());
    }
}
