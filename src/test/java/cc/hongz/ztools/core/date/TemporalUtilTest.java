package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TemporalUtil 单元测试.
 */
@DisplayName("TemporalUtil 时间工具")
class TemporalUtilTest {

    @Test
    @DisplayName("两个时间相差 Duration")
    void between() {
        final LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        final LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        assertEquals(Duration.ofDays(1), TemporalUtil.between(start, end));
    }

    @Test
    @DisplayName("两个时间相差指定单位")
    void betweenWithUnit() {
        final LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        final LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        assertEquals(24, TemporalUtil.between(start, end, ChronoUnit.HOURS));
        assertEquals(-24, TemporalUtil.between(end, start, ChronoUnit.HOURS));
    }

    @Test
    @DisplayName("TimeUnit 转 ChronoUnit")
    void toChronoUnit() {
        assertEquals(ChronoUnit.NANOS, TemporalUtil.toChronoUnit(TimeUnit.NANOSECONDS));
        assertEquals(ChronoUnit.MILLIS, TemporalUtil.toChronoUnit(TimeUnit.MILLISECONDS));
        assertEquals(ChronoUnit.SECONDS, TemporalUtil.toChronoUnit(TimeUnit.SECONDS));
        assertNull(TemporalUtil.toChronoUnit(null));
    }

    @Test
    @DisplayName("ChronoUnit 转 TimeUnit")
    void toTimeUnit() {
        assertEquals(TimeUnit.NANOSECONDS, TemporalUtil.toTimeUnit(ChronoUnit.NANOS));
        assertEquals(TimeUnit.SECONDS, TemporalUtil.toTimeUnit(ChronoUnit.SECONDS));
        assertEquals(TimeUnit.DAYS, TemporalUtil.toTimeUnit(ChronoUnit.DAYS));
        assertNull(TemporalUtil.toTimeUnit(null));
    }

    @Test
    @DisplayName("日期偏移")
    void offset() {
        final LocalDate date = LocalDate.of(2024, 1, 1);
        assertEquals(LocalDate.of(2024, 1, 3), TemporalUtil.offset(date, 2, ChronoUnit.DAYS));
        assertNull(TemporalUtil.offset(null, 1, ChronoUnit.DAYS));
    }

    @Test
    @DisplayName("偏移到指定周几")
    void offsetToDayOfWeek() {
        // 2024-01-01 是周一，向后偏移到下一个周一为 2024-01-08
        final LocalDate date = LocalDate.of(2024, 1, 1);
        assertEquals(LocalDate.of(2024, 1, 8), TemporalUtil.offset(date, DayOfWeek.MONDAY, false));
        assertNull(TemporalUtil.offset(null, DayOfWeek.MONDAY, false));
    }
}
