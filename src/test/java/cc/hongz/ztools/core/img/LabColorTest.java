package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link LabColor} 单元测试.
 * <p>
 * 覆盖构造方法和 getDistance 方法.
 *
 * @author hongzhe.qin
 */
@DisplayName("LabColor 颜色空间单元测试")
class LabColorTest {

    @DisplayName("从 Color 构造")
    @Test
    void constructFromColor() {
        LabColor lab = new LabColor(Color.RED);
        assertNotNull(lab);
    }

    @DisplayName("从 RGB 整数构造")
    @Test
    void constructFromRgbInteger() {
        LabColor lab = new LabColor(0xFF0000);
        assertNotNull(lab);
    }

    @DisplayName("null Color 构造抛异常")
    @Test
    void constructFromNullColor_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new LabColor((Color) null));
    }

    @DisplayName("null RGB 构造抛异常")
    @Test
    void constructFromNullRgb_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new LabColor((Integer) null));
    }

    @DisplayName("同色距离为 0")
    @Test
    void getDistance_sameColor_returnsZero() {
        LabColor c1 = new LabColor(Color.RED);
        LabColor c2 = new LabColor(Color.RED);
        assertEquals(0.0, c1.getDistance(c2), 0.001);
    }

    @DisplayName("不同色距离为正")
    @Test
    void getDistance_differentColors_returnsPositive() {
        LabColor red = new LabColor(Color.RED);
        LabColor blue = new LabColor(Color.BLUE);
        double distance = red.getDistance(blue);
        assertTrue(distance > 0);
    }

    @DisplayName("不同颜色对距离为正")
    @Test
    void getDistance_differentPairs_returnsPositive() {
        LabColor c1 = new LabColor(Color.GREEN);
        LabColor c2 = new LabColor(Color.YELLOW);
        assertTrue(c1.getDistance(c2) > 0);
        assertTrue(c2.getDistance(c1) > 0);
    }

    @DisplayName("相近色距离小")
    @Test
    void getDistance_similarColors_smallDistance() {
        LabColor c1 = new LabColor(new Color(100, 100, 100));
        LabColor c2 = new LabColor(new Color(105, 105, 105));
        double distance = c1.getDistance(c2);
        assertTrue(distance < 10);
    }

    @DisplayName("黑白距离大")
    @Test
    void getDistance_blackAndWhite_largeDistance() {
        LabColor black = new LabColor(Color.BLACK);
        LabColor white = new LabColor(Color.WHITE);
        double distance = black.getDistance(white);
        assertTrue(distance > 50);
    }
}
