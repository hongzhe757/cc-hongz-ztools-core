package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link PasswdStrength} 单元测试.
 */
class PasswdStrengthTest {

	@Test
	void strengthTest() {
		String passwd = "2hAj5#mne-ix.86H";
		assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	void strengthNumberTest() {
		String passwd = "9999999999999";
		assertEquals(0, PasswdStrength.check(passwd));
	}

	@Test
	void consecutiveLettersTest() {
		assertEquals(0, PasswdStrength.check("abcdefghijklmn"));
		assertEquals(0, PasswdStrength.check("ABCDEFGHIJKLMN"));
	}

	@Test
	void dictionaryWeakPasswordTest() {
		assertEquals(0, PasswdStrength.check("password"));
		assertEquals(3, PasswdStrength.check("password2"));
	}

	@Test
	void numericSequenceTest() {
		assertEquals(0, PasswdStrength.check("01234567890"));
		assertEquals(0, PasswdStrength.check("09876543210"));
	}
}
