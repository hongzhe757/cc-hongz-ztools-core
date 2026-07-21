package cc.hongz.ztools.core.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MaskBit 单元测试
 */
class MaskBitTest {

	@Test
	void getTest() {
		assertEquals("255.255.255.0", MaskBit.get(24));
		assertEquals("255.255.0.0", MaskBit.get(16));
		assertEquals("255.0.0.0", MaskBit.get(8));
		assertEquals("255.255.255.255", MaskBit.get(32));
		assertEquals("128.0.0.0", MaskBit.get(1));
		assertNull(MaskBit.get(0));
		assertNull(MaskBit.get(33));
		assertNull(MaskBit.get(-1));
	}

	@Test
	void getMaskBitTest() {
		assertEquals(24, MaskBit.getMaskBit("255.255.255.0"));
		assertEquals(16, MaskBit.getMaskBit("255.255.0.0"));
		assertEquals(8, MaskBit.getMaskBit("255.0.0.0"));
		assertEquals(32, MaskBit.getMaskBit("255.255.255.255"));
		assertEquals(1, MaskBit.getMaskBit("128.0.0.0"));
		assertNull(MaskBit.getMaskBit("255.255.255.1"));
		assertNull(MaskBit.getMaskBit("invalid"));
	}
}
