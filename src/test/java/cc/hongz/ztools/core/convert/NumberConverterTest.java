package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.convert.impl.NumberConverter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NumberConverter 数字转换器")
public class NumberConverterTest {

    @Test
    @DisplayName("转 double（带千分位）")
    public void toDoubleTest() {
        final NumberConverter numberConverter = new NumberConverter(Double.class);
        final Number convert = numberConverter.convert("1,234.55", null);
        assertEquals(1234.55D, convert);
    }

    @Test
    @DisplayName("转 int（带千分位）")
    public void toIntegerTest() {
        final NumberConverter numberConverter = new NumberConverter(Integer.class);
        final Number convert = numberConverter.convert("1,234.55", null);
        assertEquals(1234, convert);
    }
}
