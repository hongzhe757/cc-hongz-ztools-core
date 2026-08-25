package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Image;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ScaleType} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ScaleType 缩放类型枚举单元测试")
class ScaleTypeTest {

    @DisplayName("DEFAULT 对应 SCALE_DEFAULT")
    @Test
    void defaultValue() {
        assertEquals(Image.SCALE_DEFAULT, ScaleType.DEFAULT.getValue());
    }

    @DisplayName("FAST 对应 SCALE_FAST")
    @Test
    void fastValue() {
        assertEquals(Image.SCALE_FAST, ScaleType.FAST.getValue());
    }

    @DisplayName("SMOOTH 对应 SCALE_SMOOTH")
    @Test
    void smoothValue() {
        assertEquals(Image.SCALE_SMOOTH, ScaleType.SMOOTH.getValue());
    }

    @DisplayName("REPLICATE 对应 SCALE_REPLICATE")
    @Test
    void replicateValue() {
        assertEquals(Image.SCALE_REPLICATE, ScaleType.REPLICATE.getValue());
    }

    @DisplayName("AREA_AVERAGING 对应 SCALE_AREA_AVERAGING")
    @Test
    void areaAveragingValue() {
        assertEquals(Image.SCALE_AREA_AVERAGING, ScaleType.AREA_AVERAGING.getValue());
    }

    @DisplayName("枚举数量为 5")
    @Test
    void enumCount() {
        assertEquals(5, ScaleType.values().length);
    }

    @DisplayName("valueOf 解析枚举值")
    @Test
    void valueOf() {
        assertEquals(ScaleType.DEFAULT, ScaleType.valueOf("DEFAULT"));
        assertEquals(ScaleType.SMOOTH, ScaleType.valueOf("SMOOTH"));
    }
}
