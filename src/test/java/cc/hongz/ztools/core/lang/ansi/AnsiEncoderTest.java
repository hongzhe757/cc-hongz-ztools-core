package cc.hongz.ztools.core.lang.ansi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ansi 包单元测试.
 */
@DisplayName("ANSI 转义序列")
class AnsiEncoderTest {

	@Test
	@DisplayName("encode - 单颜色+文本")
	void encodeTest() {
		final String encode = AnsiEncoder.encode(AnsiColor.GREEN, "Hutool test");
		assertEquals("\u001B[32mHutool test\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 前景+背景+文本")
	void encodeWithForegroundAndBackground() {
		final String encode = AnsiEncoder.encode(AnsiColor.RED, AnsiBackground.WHITE, "text");
		assertEquals("\u001B[31;47mtext\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 样式+颜色+文本")
	void encodeWithStyle() {
		final String encode = AnsiEncoder.encode(AnsiStyle.BOLD, AnsiColor.BLUE, "bold blue");
		assertEquals("\u001B[1;34mbold blue\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 无AnsiElement时直接拼接")
	void encodeNoAnsiElement() {
		final String encode = AnsiEncoder.encode("plain text");
		assertEquals("plain text", encode);
	}

	@Test
	@DisplayName("encode - null元素被跳过")
	void encodeWithNull() {
		final String encode = AnsiEncoder.encode(AnsiColor.GREEN, null, "text");
		// null is skipped, but GREEN triggers ANSI encoding
		assertEquals("\u001B[32mtext\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 多个AnsiElement用分号连接")
	void encodeMultipleAnsiElements() {
		final String encode = AnsiEncoder.encode(AnsiStyle.BOLD, AnsiColor.RED, AnsiBackground.BLACK, "x");
		assertEquals("\u001B[1;31;40mx\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 8bit颜色")
	void encodeWith8BitColor() {
		Ansi8BitColor fg = Ansi8BitColor.foreground(123);
		final String encode = AnsiEncoder.encode(fg, "hello");
		assertEquals("\u001B[38;5;123mhello\u001B[0;39m", encode);
	}

	@Test
	@DisplayName("encode - 空参数")
	void encodeEmpty() {
		final String encode = AnsiEncoder.encode();
		assertEquals("", encode);
	}

	// ===== AnsiColor =====

	@Nested
	@DisplayName("AnsiColor 枚举")
	class AnsiColorTest {

		@Test
		@DisplayName("getCode - 各颜色代码正确")
		void getCode() {
			assertEquals(39, AnsiColor.DEFAULT.getCode());
			assertEquals(30, AnsiColor.BLACK.getCode());
			assertEquals(31, AnsiColor.RED.getCode());
			assertEquals(32, AnsiColor.GREEN.getCode());
			assertEquals(33, AnsiColor.YELLOW.getCode());
			assertEquals(34, AnsiColor.BLUE.getCode());
			assertEquals(35, AnsiColor.MAGENTA.getCode());
			assertEquals(36, AnsiColor.CYAN.getCode());
			assertEquals(37, AnsiColor.WHITE.getCode());
			assertEquals(90, AnsiColor.BRIGHT_BLACK.getCode());
			assertEquals(97, AnsiColor.BRIGHT_WHITE.getCode());
		}

		@Test
		@DisplayName("toString - 返回颜色代码字符串")
		void toStringTest() {
			assertEquals("32", AnsiColor.GREEN.toString());
			assertEquals("39", AnsiColor.DEFAULT.toString());
			assertEquals("91", AnsiColor.BRIGHT_RED.toString());
		}
	}

	// ===== AnsiBackground =====

	@Nested
	@DisplayName("AnsiBackground 枚举")
	class AnsiBackgroundTest {

		@Test
		@DisplayName("getCode - 各背景色代码正确")
		void getCode() {
			assertEquals(49, AnsiBackground.DEFAULT.getCode());
			assertEquals(40, AnsiBackground.BLACK.getCode());
			assertEquals(41, AnsiBackground.RED.getCode());
			assertEquals(42, AnsiBackground.GREEN.getCode());
			assertEquals(100, AnsiBackground.BRIGHT_BLACK.getCode());
			assertEquals(107, AnsiBackground.BRIGHT_WHITE.getCode());
		}

		@Test
		@DisplayName("toString - 返回背景色代码字符串")
		void toStringTest() {
			assertEquals("42", AnsiBackground.GREEN.toString());
			assertEquals("49", AnsiBackground.DEFAULT.toString());
		}
	}

	// ===== AnsiStyle =====

	@Nested
	@DisplayName("AnsiStyle 枚举")
	class AnsiStyleTest {

		@Test
		@DisplayName("getCode - 各样式代码正确")
		void getCode() {
			assertEquals(0, AnsiStyle.NORMAL.getCode());
			assertEquals(1, AnsiStyle.BOLD.getCode());
			assertEquals(2, AnsiStyle.FAINT.getCode());
			assertEquals(3, AnsiStyle.ITALIC.getCode());
			assertEquals(4, AnsiStyle.UNDERLINE.getCode());
		}

		@Test
		@DisplayName("toString - 返回样式代码字符串")
		void toStringTest() {
			assertEquals("0", AnsiStyle.NORMAL.toString());
			assertEquals("1", AnsiStyle.BOLD.toString());
		}
	}

	// ===== Ansi8BitColor =====

	@Nested
	@DisplayName("Ansi8BitColor 8位颜色")
	class Ansi8BitColorTest {

		@Test
		@DisplayName("foreground - 前景色toString")
		void foreground() {
			Ansi8BitColor fg = Ansi8BitColor.foreground(0);
			assertEquals("38;5;0", fg.toString());
			assertEquals(0, fg.getCode());

			Ansi8BitColor fg123 = Ansi8BitColor.foreground(123);
			assertEquals("38;5;123", fg123.toString());
			assertEquals(123, fg123.getCode());

			Ansi8BitColor fg255 = Ansi8BitColor.foreground(255);
			assertEquals("38;5;255", fg255.toString());
		}

		@Test
		@DisplayName("background - 背景色toString")
		void background() {
			Ansi8BitColor bg = Ansi8BitColor.background(100);
			assertEquals("48;5;100", bg.toString());
			assertEquals(100, bg.getCode());
		}

		@Test
		@DisplayName("code越界抛异常")
		void codeOutOfRange() {
			assertThrows(IllegalArgumentException.class, () -> Ansi8BitColor.foreground(-1));
			assertThrows(IllegalArgumentException.class, () -> Ansi8BitColor.foreground(256));
			assertThrows(IllegalArgumentException.class, () -> Ansi8BitColor.background(300));
		}

		@Test
		@DisplayName("equals/hashCode - 相同前缀和代码则相等")
		void equalsAndHashCode() {
			Ansi8BitColor a = Ansi8BitColor.foreground(42);
			Ansi8BitColor b = Ansi8BitColor.foreground(42);
			Ansi8BitColor c = Ansi8BitColor.background(42);
			Ansi8BitColor d = Ansi8BitColor.foreground(43);

			assertEquals(a, b);
			assertEquals(a.hashCode(), b.hashCode());
			assertNotEquals(a, c); // different prefix
			assertNotEquals(a, d); // different code
			assertNotEquals(a, null);
			assertNotEquals(a, "string");
		}
	}

	// ===== AnsiColors =====

	@Nested
	@DisplayName("AnsiColors 颜色映射")
	class AnsiColorsTest {

		@Test
		@DisplayName("findClosest 4-bit - 纯红匹配到RED")
		void findClosest4BitRed() {
			AnsiColors colors = new AnsiColors(AnsiColors.BitDepth.FOUR);
			AnsiColorWrapper closest = colors.findClosest(Color.RED);
			// RED(30) in 4-bit maps to 0xAA0000, BRIGHT_RED(91) maps to 0xFF5555
			// Color.RED = 0xFF0000, should be closest to BRIGHT_RED(91)
			assertNotNull(closest);
		}

		@Test
		@DisplayName("findClosest 4-bit - 纯黑匹配到BLACK")
		void findClosest4BitBlack() {
			AnsiColors colors = new AnsiColors(AnsiColors.BitDepth.FOUR);
			AnsiColorWrapper closest = colors.findClosest(Color.BLACK);
			assertNotNull(closest);
			// Black should map to BLACK (code 30)
			assertEquals(30, closest.toAnsiElement(ForeOrBack.FORE).getCode());
		}

		@Test
		@DisplayName("findClosest 8-bit - 基本功能")
		void findClosest8Bit() {
			AnsiColors colors = new AnsiColors(AnsiColors.BitDepth.EIGHT);
			AnsiColorWrapper closest = colors.findClosest(Color.BLUE);
			assertNotNull(closest);
		}

		@Test
		@DisplayName("BitDepth.of - 有效值")
		void bitDepthOf() {
			assertEquals(AnsiColors.BitDepth.FOUR, AnsiColors.BitDepth.of(4));
			assertEquals(AnsiColors.BitDepth.EIGHT, AnsiColors.BitDepth.of(8));
		}

		@Test
		@DisplayName("BitDepth.of - 无效值抛异常")
		void bitDepthOfInvalid() {
			assertThrows(IllegalArgumentException.class, () -> AnsiColors.BitDepth.of(16));
			assertThrows(IllegalArgumentException.class, () -> AnsiColors.BitDepth.of(0));
		}
	}

	// ===== AnsiColorWrapper =====

	@Nested
	@DisplayName("AnsiColorWrapper 颜色包装")
	class AnsiColorWrapperTest {

		@Test
		@DisplayName("toAnsiElement 4-bit 前景")
		void toAnsiElement4BitFore() {
			AnsiColorWrapper wrapper = new AnsiColorWrapper(32, AnsiColors.BitDepth.FOUR);
			AnsiElement element = wrapper.toAnsiElement(ForeOrBack.FORE);
			assertInstanceOf(AnsiColor.class, element);
			assertEquals(32, element.getCode());
		}

		@Test
		@DisplayName("toAnsiElement 4-bit 背景")
		void toAnsiElement4BitBack() {
			AnsiColorWrapper wrapper = new AnsiColorWrapper(32, AnsiColors.BitDepth.FOUR);
			AnsiElement element = wrapper.toAnsiElement(ForeOrBack.BACK);
			assertInstanceOf(AnsiBackground.class, element);
			// GREEN foreground code=32, background code=42 (32+10)
			assertEquals(42, element.getCode());
		}

		@Test
		@DisplayName("toAnsiElement 8-bit 前景")
		void toAnsiElement8BitFore() {
			AnsiColorWrapper wrapper = new AnsiColorWrapper(123, AnsiColors.BitDepth.EIGHT);
			AnsiElement element = wrapper.toAnsiElement(ForeOrBack.FORE);
			assertInstanceOf(Ansi8BitColor.class, element);
			assertEquals("38;5;123", element.toString());
		}

		@Test
		@DisplayName("toAnsiElement 8-bit 背景")
		void toAnsiElement8BitBack() {
			AnsiColorWrapper wrapper = new AnsiColorWrapper(200, AnsiColors.BitDepth.EIGHT);
			AnsiElement element = wrapper.toAnsiElement(ForeOrBack.BACK);
			assertInstanceOf(Ansi8BitColor.class, element);
			assertEquals("48;5;200", element.toString());
		}

		@Test
		@DisplayName("4-bit code越界抛异常")
		void invalid4BitCode() {
			assertThrows(IllegalArgumentException.class,
					() -> new AnsiColorWrapper(50, AnsiColors.BitDepth.FOUR));
		}

		@Test
		@DisplayName("equals/hashCode")
		void equalsAndHashCode() {
			AnsiColorWrapper a = new AnsiColorWrapper(32, AnsiColors.BitDepth.FOUR);
			AnsiColorWrapper b = new AnsiColorWrapper(32, AnsiColors.BitDepth.FOUR);
			AnsiColorWrapper c = new AnsiColorWrapper(31, AnsiColors.BitDepth.FOUR);

			assertEquals(a, b);
			assertEquals(a.hashCode(), b.hashCode());
			assertNotEquals(a, c);
		}
	}

	// ===== ForeOrBack =====

	@Test
	@DisplayName("ForeOrBack - 枚举值")
	void foreOrBackValues() {
		assertEquals(2, ForeOrBack.values().length);
		assertNotNull(ForeOrBack.valueOf("FORE"));
		assertNotNull(ForeOrBack.valueOf("BACK"));
	}

	// ===== AnsiElement =====

	@Test
	@DisplayName("AnsiElement - 默认getCode返回-1")
	void ansiElementDefaultCode() {
		AnsiElement element = new AnsiElement() {
			@Override
			public String toString() {
				return "test";
			}
		};
		assertEquals(-1, element.getCode());
		assertEquals("test", element.toString());
	}
}
