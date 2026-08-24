package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PrimitiveConvert 原始类型转换")
public class PrimitiveConvertTest {

    @Test
    @DisplayName("转 int")
    public void toIntTest() {
        final int convert = Convert.convert(int.class, "123");
        assertEquals(123, convert);
    }

    @Test
    @DisplayName("非法输入抛异常")
    public void toIntErrorTest() {
        assertThrows(NumberFormatException.class, () -> {
            Convert.convert(int.class, "aaaa");
        });
    }
}
