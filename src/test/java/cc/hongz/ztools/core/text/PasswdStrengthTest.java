package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link PasswdStrength} 单元测试.
 *
 * @author venshine
 */
public class PasswdStrengthTest {

	@Test
	@DisplayName("测试复杂密码强度")
	public void strengthTest() {
		String passwd = "2hAj5#mne-ix.86H";
		assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	@DisplayName("测试纯数字密码强度")
	public void strengthNumberTest() {
		String passwd = "9999999999999";
		assertEquals(0, PasswdStrength.check(passwd));
	}

	@Test
	@DisplayName("测试连续字母被识别为弱密码")
	public void consecutiveLettersTest() {
		assertEquals(0, PasswdStrength.check("abcdefghijklmn"));
		assertEquals(0, PasswdStrength.check("ABCDEFGHIJKLMN"));
	}

	@Test
	@DisplayName("测试字典弱密码被识别")
	public void dictionaryWeakPasswordTest() {
		assertEquals(0, PasswdStrength.check("password"));
		assertEquals(3, PasswdStrength.check("password2"));
	}

	@Test
	@DisplayName("测试数字序列被识别为弱密码")
	public void numericSequenceTest() {
		assertEquals(0, PasswdStrength.check("01234567890"));
		assertEquals(0, PasswdStrength.check("09876543210"));
	}

	@Test
	@DisplayName("测试空密码抛异常")
	public void nullPasswordTest() {
		assertThrows(IllegalArgumentException.class, () -> PasswdStrength.check(null));
	}

	@Test
	@DisplayName("测试getLevel方法返回正确的等级枚举")
	public void getLevelTest() {
		assertEquals(PasswdStrength.PASSWD_LEVEL.EASY, PasswdStrength.getLevel("123"));
		assertEquals(PasswdStrength.PASSWD_LEVEL.STRONG, PasswdStrength.getLevel("Abc123!@"));
		assertEquals(PasswdStrength.PASSWD_LEVEL.EXTREMELY_STRONG, PasswdStrength.getLevel("2hAj5#mne-ix.86H"));
	}

	@Test
	@DisplayName("测试键盘连续序列被识别为弱密码")
	public void keyboardPatternTest() {
		assertEquals(0, PasswdStrength.check("qwertyuiop"));
		assertEquals(0, PasswdStrength.check("asdfghjkl"));
		assertEquals(0, PasswdStrength.check("zxcvbnm"));
	}

	@Test
	@DisplayName("测试日期格式密码被降级")
	public void datePasswordTest() {
		// 8位日期格式如 19881010
		assertEquals(0, PasswdStrength.check("19881010"));
		// 6位日期格式如 881010
		assertEquals(0, PasswdStrength.check("881010"));
	}

	@Test
	@DisplayName("测试重复模式(分割相等)被降级")
	public void repeatedPatternTest() {
		// 两段完全相同: abcabc
		assertEquals(0, PasswdStrength.check("abcabc"));
		// 三段完全相同: ababab
		assertEquals(0, PasswdStrength.check("ababab"));
	}

	@Test
	@DisplayName("测试全相同字符被识别为弱密码")
	public void allSameCharTest() {
		assertEquals(0, PasswdStrength.check("aaaaaa"));
		assertEquals(0, PasswdStrength.check("111111"));
	}

	@Test
	@DisplayName("测试极短密码")
	public void veryShortPasswordTest() {
		// ≤3 必然为 0
		assertEquals(0, PasswdStrength.check("ab"));
		// ≤4 进一步降级
		assertEquals(0, PasswdStrength.check("abc1"));
		// ≤6 再降级
		assertEquals(0, PasswdStrength.check("abcdef"));
	}

	@Test
	@DisplayName("测试字典中包含弱密码片段被降级")
	public void dictionaryContainsTest() {
		// "abc123" 是字典中的弱密码，"123" 包含在 "abc123" 中
		assertEquals(0, PasswdStrength.check("123"));
	}

	@Test
	@DisplayName("测试中等强度密码")
	public void mediumPasswordTest() {
		// 包含大小写字母+数字，长度适中
		assertEquals(PasswdStrength.PASSWD_LEVEL.MIDIUM, PasswdStrength.getLevel("Abc12345"));
	}

	@Test
	@DisplayName("测试强密码")
	public void strongPasswordTest() {
		// 大小写字母+数字+特殊字符，长度10+
		assertEquals(PasswdStrength.PASSWD_LEVEL.STRONG, PasswdStrength.getLevel("Abc12#xyZ89"));
	}

	@Test
	@DisplayName("测试非常强密码")
	public void veryStrongPasswordTest() {
		// 大小写字母+数字+特殊字符，长度12+
		assertEquals(PasswdStrength.PASSWD_LEVEL.VERY_STRONG, PasswdStrength.getLevel("MyP@ssw0rd!202"));
	}
}
