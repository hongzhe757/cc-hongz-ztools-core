package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ConvertToBoolean 转布尔")
public class ConvertToBooleanTest {

    @Test
    @DisplayName("int 转 boolean")
    public void intToBooleanTest() {
        int a = 100;
        final Boolean aBoolean = Convert.toBool(a);
        assertTrue(aBoolean);

        int b = 0;
        final Boolean bBoolean = Convert.toBool(b);
        assertFalse(bBoolean);
    }

    @Test
    @DisplayName("带默认值转 boolean")
    void toBooleanWithDefaultTest() {
        Assertions.assertFalse(Convert.toBool("ddddd", false));
    }
}
