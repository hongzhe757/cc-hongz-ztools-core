package cc.hongz.ztools.core.img.gif;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link GifDecoder} 单元测试.
 * <p>
 * 覆盖基本初始化和状态常量.
 *
 * @author hongzhe.qin
 */
class GifDecoderTest {

	@Test
	void newInstance_hasZeroFrames() {
		GifDecoder decoder = new GifDecoder();
		assertEquals(0, decoder.getFrameCount());
	}

	@Test
	void newInstance_getImageReturnsNull() {
		GifDecoder decoder = new GifDecoder();
		assertNull(decoder.getImage());
	}

	@Test
	void getDelay_invalidIndex_returnsNegative() {
		GifDecoder decoder = new GifDecoder();
		assertEquals(-1, decoder.getDelay(0));
	}

	@Test
	void getDelay_negativeIndex_returnsNegative() {
		GifDecoder decoder = new GifDecoder();
		assertEquals(-1, decoder.getDelay(-1));
	}

	@Test
	void statusConstants() {
		assertEquals(0, GifDecoder.STATUS_OK);
		assertEquals(1, GifDecoder.STATUS_FORMAT_ERROR);
		assertEquals(2, GifDecoder.STATUS_OPEN_ERROR);
	}

	@Test
	void getLoopCount_defaultIsOne() {
		GifDecoder decoder = new GifDecoder();
		assertEquals(1, decoder.getLoopCount());
	}
}
