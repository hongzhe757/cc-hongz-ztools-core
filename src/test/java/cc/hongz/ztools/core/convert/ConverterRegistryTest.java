package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.convert.impl.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ConverterRegistry} 单元测试.
 */
class ConverterRegistryTest {

    private final ConverterRegistry registry = new ConverterRegistry();

    @Test
    void getInstance_returnsSingleton() {
        assertSame(ConverterRegistry.getInstance(), ConverterRegistry.getInstance());
    }

    @Test
    void convert_nullValue_returnsNull() {
        assertNull(registry.convert(String.class, null));
    }

    @Test
    void convert_stringToInt() {
        assertEquals(Integer.valueOf(123), registry.convert(Integer.class, "123"));
    }

    @Test
    void convert_stringToLong() {
        assertEquals(Long.valueOf(456L), registry.convert(Long.class, "456"));
    }

    @Test
    void convert_stringToDouble() {
        Object result = registry.convert(Double.class, "3.14");
        assertEquals(Double.valueOf(3.14), result);
    }

    @Test
    void convert_stringToBoolean() {
        Object result = registry.convert(Boolean.class, "true");
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    void convert_numberToString() {
        assertEquals("42", registry.convert(String.class, 42));
    }

    @Test
    void convert_intToBigDecimal() {
        assertEquals(new BigDecimal("100"), registry.convert(BigDecimal.class, 100));
    }

    @Test
    void convert_customConverter() {
        registry.putCustom(String.class, (Converter<String>) (value, defaultValue) -> "[custom]" + value);
        assertEquals("[custom]test", registry.convert(String.class, "test"));
    }

    @Test
    void convert_defaultValue_onFailure() {
        // NumberConverter throws NumberFormatException for invalid input
        assertThrows(NumberFormatException.class, () ->
            registry.convert(Integer.class, "not-a-number", -1));
    }

    @Test
    void convert_collection() {
        List<String> input = Arrays.asList("1", "2", "3");
        Collection<Integer> result = registry.convert(Collection.class, input);
        assertNotNull(result);
    }

    @Test
    void convert_dateTypes() {
        long now = System.currentTimeMillis();
        Date date = registry.convert(Date.class, now);
        assertNotNull(date);
        assertEquals(now, date.getTime());

        Calendar cal = registry.convert(Calendar.class, now);
        assertNotNull(cal);
        assertEquals(now, cal.getTimeInMillis());
    }

    @Test
    void convert_localDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime result = registry.convert(LocalDateTime.class, now);
        assertEquals(now, result);
    }

    @Test
    void convert_localDate() {
        LocalDate now = LocalDate.now();
        LocalDate result = registry.convert(LocalDate.class, now);
        assertEquals(now, result);
    }

    @Test
    void convert_uuid() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, registry.convert(UUID.class, uuid));
    }

    @Test
    void convert_optional() {
        Optional<String> opt = Optional.of("hello");
        assertEquals("hello", registry.convert(String.class, opt));
    }
}
