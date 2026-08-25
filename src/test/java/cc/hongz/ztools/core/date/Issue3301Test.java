package cc.hongz.ztools.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@DisplayName("Issue3301 of 解析")

public class Issue3301Test {
    @Test
    @DisplayName("of 解析")
    public void ofTest() {
        final ZonedDateTime now = ZonedDateTime.now();
        // 获得一个特殊的 temporal
        String text = DateTimeFormatter.ISO_INSTANT.format(now);
        TemporalAccessor temporal = DateTimeFormatter.ISO_INSTANT.parse(text);

        LocalDateTime actual = LocalDateTimeUtil.of(temporal);
        assertEquals(now.toLocalDateTime().toString(), actual.toString());
    }
}
