package cc.hongz.ztools.core.img;

import org.junit.jupiter.api.Test;

import java.awt.Image;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ScaleType} 单元测试.
 *
 * @author hongzhe.qin
 */
class ScaleTypeTest {

	@Test
	void defaultValue() {
		assertEquals(Image.SCALE_DEFAULT, ScaleType.DEFAULT.getValue());
	}

	@Test
	void fastValue() {
		assertEquals(Image.SCALE_FAST, ScaleType.FAST.getValue());
	}

	@Test
	void smoothValue() {
		assertEquals(Image.SCALE_SMOOTH, ScaleType.SMOOTH.getValue());
	}

	@Test
	void replicateValue() {
		assertEquals(Image.SCALE_REPLICATE, ScaleType.REPLICATE.getValue());
	}

	@Test
	void areaAveragingValue() {
		assertEquals(Image.SCALE_AREA_AVERAGING, ScaleType.AREA_AVERAGING.getValue());
	}

	@Test
	void enumCount() {
		assertEquals(5, ScaleType.values().length);
	}

	@Test
	void valueOf() {
		assertEquals(ScaleType.DEFAULT, ScaleType.valueOf("DEFAULT"));
		assertEquals(ScaleType.SMOOTH, ScaleType.valueOf("SMOOTH"));
	}
}
