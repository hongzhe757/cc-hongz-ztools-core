package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BackgroundRemoval} 单元测试.
 * <p>
 * 覆盖 public 方法: hexToRgb, areColorsWithinTolerance, getMainColor, backgroundRemoval.
 *
 * @author hongzhe.qin
 */
class BackgroundRemovalTest {

	// -------------------------------------------------------------------- hexToRgb

	@Test
	void hexToRgbBlack() {
		Color c = BackgroundRemoval.hexToRgb("#000000");
		assertEquals(0, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(0, c.getBlue());
	}

	@Test
	void hexToRgbWhite() {
		Color c = BackgroundRemoval.hexToRgb("#ffffff");
		assertEquals(255, c.getRed());
		assertEquals(255, c.getGreen());
		assertEquals(255, c.getBlue());
	}

	@Test
	void hexToRgbRed() {
		Color c = BackgroundRemoval.hexToRgb("#ff0000");
		assertEquals(255, c.getRed());
		assertEquals(0, c.getGreen());
		assertEquals(0, c.getBlue());
	}

	// -------------------------------------------------------------------- areColorsWithinTolerance

	@Test
	void areColorsWithinTolerance_sameColor_smallTolerance() {
		assertTrue(BackgroundRemoval.areColorsWithinTolerance(Color.RED, Color.RED, 1));
	}

	@Test
	void areColorsWithinTolerance_similarColors_smallTolerance() {
		Color c1 = new Color(100, 100, 100);
		Color c2 = new Color(110, 110, 110);
		assertTrue(BackgroundRemoval.areColorsWithinTolerance(c1, c2, 20));
	}

	@Test
	void areColorsWithinTolerance_differentColors_smallTolerance() {
		Color c1 = Color.RED;
		Color c2 = Color.BLUE;
		assertFalse(BackgroundRemoval.areColorsWithinTolerance(c1, c2, 10));
	}

	@Test
	void areColorsWithinTolerance_withColorTolerance() {
		Color c1 = new Color(50, 50, 50);
		Color c2 = new Color(55, 55, 55);
		Color tolerance = new Color(10, 10, 10);
		assertTrue(BackgroundRemoval.areColorsWithinTolerance(c1, c2, tolerance));
	}

	// -------------------------------------------------------------------- getMainColor

	@Test
	void getMainColor_solidColorImage() {
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				img.setRGB(x, y, Color.RED.getRGB());
			}
		}
		String mainColor = BackgroundRemoval.getMainColor(img);
		assertEquals("#FF0000", mainColor);
	}

	@Test
	void getMainColor_nullImage_throwsException() {
		assertThrows(IllegalArgumentException.class, () -> BackgroundRemoval.getMainColor((BufferedImage) null));
	}

	// -------------------------------------------------------------------- backgroundRemoval (BufferedImage)

	@Test
	void backgroundRemoval_solidImage_transparentBg() {
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				img.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		BufferedImage result = BackgroundRemoval.backgroundRemoval(img, null, 0);
		assertNotNull(result);
		assertEquals(img.getWidth(), result.getWidth());
		assertEquals(img.getHeight(), result.getHeight());
	}

	@Test
	void backgroundRemoval_solidImage_withOverrideColor() {
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				img.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		BufferedImage result = BackgroundRemoval.backgroundRemoval(img, Color.BLACK, 10);
		assertNotNull(result);
	}

	@Test
	void backgroundRemoval_toleranceClamped() {
		BufferedImage img = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				img.setRGB(x, y, Color.BLUE.getRGB());
			}
		}
		// tolerance > 255 should be clamped to 255
		BufferedImage result = BackgroundRemoval.backgroundRemoval(img, null, 999);
		assertNotNull(result);
	}

	// -------------------------------------------------------------------- IMAGES_TYPE

	@Test
	void imagesType_containsJpgAndPng() {
		assertEquals(2, BackgroundRemoval.IMAGES_TYPE.length);
		assertEquals("jpg", BackgroundRemoval.IMAGES_TYPE[0]);
		assertEquals("png", BackgroundRemoval.IMAGES_TYPE[1]);
	}
}
