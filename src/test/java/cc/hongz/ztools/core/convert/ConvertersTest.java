package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.convert.impl.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Period;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 各 Converter impl 单元测试.
 */
class ConvertersTest {

    // ==================== PrimitiveConverter ====================

    @Test
    void primitiveConverter_int() {
        PrimitiveConverter c = new PrimitiveConverter(int.class);
        assertEquals(Integer.valueOf(42), c.convert(42L, null));
    }

    @Test
    void primitiveConverter_boolean() {
        PrimitiveConverter c = new PrimitiveConverter(boolean.class);
        assertEquals(true, c.convert(1, null));
        assertEquals(false, c.convert(0, null));
    }

    // ==================== BooleanConverter ====================

    @Test
    void booleanConverter_trueValues() {
        BooleanConverter c = new BooleanConverter();
        assertTrue(c.convert("true", null));
        assertTrue(c.convert(1, null));
    }

    @Test
    void booleanConverter_falseValues() {
        BooleanConverter c = new BooleanConverter();
        assertFalse(c.convert("false", null));
        assertFalse(c.convert(0, null));
    }

    // ==================== CharacterConverter ====================

    @Test
    void characterConverter_char() {
        CharacterConverter c = new CharacterConverter();
        assertEquals('A', c.convert("A", null));
    }

    // ==================== NumberConverter ====================

    @Test
    void numberConverter() {
        NumberConverter c = new NumberConverter(Integer.class);
        assertEquals(Integer.valueOf(123), c.convert("123", null));
    }

    @Test
    void numberConverter_decimal() {
        NumberConverter c = new NumberConverter(Double.class);
        assertEquals(3.14, ((Number) c.convert("3.14", null)).doubleValue(), 0.001);
    }

    @Test
    void numberConverter_bigDecimal() {
        NumberConverter c = new NumberConverter(BigDecimal.class);
        assertEquals(0, new BigDecimal("99.99").compareTo((BigDecimal) c.convert("99.99", null)));
    }

    // ==================== StringConverter ====================

    @Test
    void stringConverter() {
        StringConverter c = new StringConverter();
        assertEquals("hello", c.convert("hello", null));
        assertEquals("42", c.convert(42, null));
    }

    // ==================== ClassConverter ====================

    @Test
    void classConverter() {
        ClassConverter c = new ClassConverter();
        assertEquals(String.class, c.convert("java.lang.String", null));
    }

    // ==================== CharsetConverter ====================

    @Test
    void charsetConverter() {
        CharsetConverter c = new CharsetConverter();
        assertEquals(Charset.forName("UTF-8"), c.convert("UTF-8", null));
    }

    // ==================== LocaleConverter ====================

    @Test
    void localeConverter() {
        LocaleConverter c = new LocaleConverter();
        assertEquals(Locale.CHINA, c.convert("zh_CN", null));
    }

    // ==================== CurrencyConverter ====================

    @Test
    void currencyConverter() {
        CurrencyConverter c = new CurrencyConverter();
        assertEquals(Currency.getInstance("CNY"), c.convert("CNY", null));
    }

    // ==================== UUIDConverter ====================

    @Test
    void uuidConverter() {
        UUIDConverter c = new UUIDConverter();
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, c.convert(uuid.toString(), null));
    }

    // ==================== URLConverter ====================

    @Test
    void urlConverter() throws Exception {
        URLConverter c = new URLConverter();
        URL url = new URL("https://example.com");
        assertEquals(url, c.convert("https://example.com", null));
    }

    // ==================== URIConverter ====================

    @Test
    void uriConverter() throws Exception {
        URIConverter c = new URIConverter();
        URI uri = new URI("https://example.com");
        assertEquals(uri, c.convert("https://example.com", null));
    }

    // ==================== PathConverter ====================

    @Test
    void pathConverter() {
        PathConverter c = new PathConverter();
        assertEquals(Paths.get("/tmp/test"), c.convert("/tmp/test", null));
    }

    // ==================== TimeZoneConverter ====================

    @Test
    void timeZoneConverter() {
        TimeZoneConverter c = new TimeZoneConverter();
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        assertEquals(tz, c.convert("Asia/Shanghai", null));
    }

    // ==================== DurationConverter ====================

    @Test
    void durationConverter() {
        DurationConverter c = new DurationConverter();
        Duration d = Duration.ofHours(2);
        assertEquals(d, c.convert(d, null));
    }

    // ==================== PeriodConverter ====================

    @Test
    void periodConverter() {
        PeriodConverter c = new PeriodConverter();
        Period p = Period.ofMonths(3);
        assertEquals(p, c.convert(p, null));
    }

    // ==================== CalendarConverter ====================

    @Test
    void calendarConverter() {
        CalendarConverter c = new CalendarConverter();
        Date now = new Date();
        Calendar result = c.convert(now, null);
        assertNotNull(result);
        assertEquals(now.getTime(), result.getTime().getTime());
    }

    // ==================== OptConverter ====================

    @Test
    void optConverter() {
        OptConverter c = new OptConverter();
        assertNotNull(c.convert("test", null));
    }

    // ==================== OptionalConverter ====================

    @Test
    void optionalConverter() {
        OptionalConverter c = new OptionalConverter();
        assertEquals(Optional.of("test"), c.convert("test", null));
    }
}
