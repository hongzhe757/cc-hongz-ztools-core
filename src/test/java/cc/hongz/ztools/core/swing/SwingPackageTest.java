package cc.hongz.ztools.core.swing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import cc.hongz.ztools.core.swing.clipboard.ClipboardUtil;

import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.StringSelection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * swing 包单元测试.
 * 注意：swing 包依赖 AWT，部分测试在无头(headless)环境下会跳过.
 *
 * @author hongzhe.qin
 */
class SwingPackageTest {

	private static boolean isNotHeadless() {
		return !GraphicsEnvironment.isHeadless();
	}

	// ==================== DesktopUtil ====================

	@Test
	@EnabledIf("isNotHeadless")
	void desktopUtil_getDesktop() {
		assertNotNull(DesktopUtil.getDsktop());
	}

	// ==================== ScreenUtil ====================

	@Test
	@EnabledIf("isNotHeadless")
	void screenUtil_getWidth() {
		assertTrue(ScreenUtil.getWidth() > 0);
	}

	@Test
	@EnabledIf("isNotHeadless")
	void screenUtil_getHeight() {
		assertTrue(ScreenUtil.getHeight() > 0);
	}

	@Test
	@EnabledIf("isNotHeadless")
	void screenUtil_getRectangle() {
		assertNotNull(ScreenUtil.getRectangle());
		assertTrue(ScreenUtil.getRectangle().getWidth() > 0);
		assertTrue(ScreenUtil.getRectangle().getHeight() > 0);
	}

	// ==================== RobotUtil ====================

	@Test
	@EnabledIf("isNotHeadless")
	void robotUtil_getRobot() {
		assertNotNull(RobotUtil.getRobot());
	}

	@Test
	@EnabledIf("isNotHeadless")
	void robotUtil_delay() {
		RobotUtil.setDelay(0);
		assertEquals(0, RobotUtil.getDelay());
		RobotUtil.setDelay(10);
		assertEquals(10, RobotUtil.getDelay());
		RobotUtil.setDelay(0); // reset
	}

	// ==================== ClipboardUtil ====================

	@Test
	@EnabledIf("isNotHeadless")
	void clipboardUtil_setAndGetStr() {
		try {
			String testText = "ClipboardUtil test " + System.nanoTime();
			ClipboardUtil.setStr(testText);
			assertEquals(testText, ClipboardUtil.getStr());
		} catch (IllegalStateException e) {
			// clipboard not available in this environment, skip
			org.junit.jupiter.api.Assumptions.assumeTrue(false, "System clipboard not available");
		}
	}

	@Test
	@EnabledIf("isNotHeadless")
	void clipboardUtil_getStrFromTransferable() {
		String testText = "Transferable test " + System.nanoTime();
		StringSelection selection = new StringSelection(testText);
		assertEquals(testText, ClipboardUtil.getStr(selection));
	}

	@Test
	@EnabledIf("isNotHeadless")
	void clipboardUtil_getWithNullContent() {
		assertNull(ClipboardUtil.get(null, java.awt.datatransfer.DataFlavor.stringFlavor));
	}

	@Test
	@EnabledIf("isNotHeadless")
	void clipboardUtil_getClipboard() {
		try {
			assertNotNull(ClipboardUtil.getClipboard());
		} catch (Exception e) {
			org.junit.jupiter.api.Assumptions.assumeTrue(false, "System clipboard not available");
		}
	}
}
