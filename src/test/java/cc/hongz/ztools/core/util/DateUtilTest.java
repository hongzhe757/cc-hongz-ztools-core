package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link DateUtil} 单元测试.
 */
class DateUtilTest {

    @Test
    void getNowDateNotNull() {
        assertNotNull(DateUtil.getNowDate());
    }

    @Test
    void parseDateStandardFormat() {
        final Date date = DateUtil.parseDate("2024-01-15");
        assertNotNull(date);
        assertEquals("2024-01-15", DateUtil.parseDateToStr("yyyy-MM-dd", date));
    }

    @Test
    void parseDateNullReturnsNull() {
        assertNull(DateUtil.parseDate(null));
    }

    @Test
    void dateTimeNowHasDigits() {
        assertEquals(14, DateUtil.dateTimeNow().length());
    }
}
