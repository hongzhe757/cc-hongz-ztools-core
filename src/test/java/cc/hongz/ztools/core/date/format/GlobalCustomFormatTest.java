package cc.hongz.ztools.core.date.format;

import cc.hongz.ztools.core.date.DateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GlobalCustomFormat 单元测试.
 */
@DisplayName("GlobalCustomFormat 全局自定义格式")
class GlobalCustomFormatTest {

    @Test
    @DisplayName("是否自定义格式")
    void isCustomFormat() {
        assertTrue(GlobalCustomFormat.isCustomFormat(GlobalCustomFormat.FORMAT_SECONDS));
        assertTrue(GlobalCustomFormat.isCustomFormat(GlobalCustomFormat.FORMAT_MILLISECONDS));
        assertFalse(GlobalCustomFormat.isCustomFormat("yyyy-MM-dd"));
    }

    @Test
    @DisplayName("格式化秒时间戳")
    void formatSeconds() {
        final Date date = DateUtil.date(1704067200000L);
        assertEquals("1704067200", GlobalCustomFormat.format(date, GlobalCustomFormat.FORMAT_SECONDS));
    }

    @Test
    @DisplayName("格式化毫秒时间戳")
    void formatMilliseconds() {
        final Date date = DateUtil.date(1704067200000L);
        assertEquals("1704067200000", GlobalCustomFormat.format(date, GlobalCustomFormat.FORMAT_MILLISECONDS));
    }

    @Test
    @DisplayName("解析秒时间戳")
    void parseSeconds() {
        final Date date = GlobalCustomFormat.parse("1704067200", GlobalCustomFormat.FORMAT_SECONDS);
        assertNotNull(date);
        assertEquals(1704067200000L, date.getTime());
    }

    @Test
    @DisplayName("解析毫秒时间戳")
    void parseMilliseconds() {
        final Date date = GlobalCustomFormat.parse("1704067200000", GlobalCustomFormat.FORMAT_MILLISECONDS);
        assertNotNull(date);
        assertEquals(1704067200000L, date.getTime());
    }
}
