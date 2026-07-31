package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PhoneUtil} 单元测试.
 */
@DisplayName("PhoneUtil 电话号码工具")
class PhoneUtilTest {

	// ---- isMobile ----

	@Test
	@DisplayName("isMobile：大陆手机号判断")
	void isMobile() {
		assertTrue(PhoneUtil.isMobile("13900001234"));
		assertTrue(PhoneUtil.isMobile("18612345678"));
		assertTrue(PhoneUtil.isMobile("8613900001234"));
		assertFalse(PhoneUtil.isMobile("12345678901"));
		assertFalse(PhoneUtil.isMobile("abc"));
		assertFalse(PhoneUtil.isMobile(null));
	}

	@Test
	@DisplayName("isMobileHk：香港手机号判断")
	void isMobileHk() {
		assertTrue(PhoneUtil.isMobileHk("98765432"));
		assertTrue(PhoneUtil.isMobileHk("85298765432"));
		assertFalse(PhoneUtil.isMobileHk("12345"));
		assertFalse(PhoneUtil.isMobileHk(null));
	}

	@Test
	@DisplayName("isMobileTw：台湾手机号判断")
	void isMobileTw() {
		assertTrue(PhoneUtil.isMobileTw("0912345678"));
		assertTrue(PhoneUtil.isMobileTw("8860912345678"));
		assertFalse(PhoneUtil.isMobileTw("12345"));
		assertFalse(PhoneUtil.isMobileTw(null));
	}

	@Test
	@DisplayName("isMobileMo：澳门手机号判断")
	void isMobileMo() {
		assertTrue(PhoneUtil.isMobileMo("61234567"));
		assertTrue(PhoneUtil.isMobileMo("85361234567"));
		assertFalse(PhoneUtil.isMobileMo("12345"));
		assertFalse(PhoneUtil.isMobileMo(null));
	}

	// ---- isTel ----

	@Test
	@DisplayName("isTel：固话号码判断")
	void isTel() {
		assertTrue(PhoneUtil.isTel("010-12345678"));
		assertTrue(PhoneUtil.isTel("02112345678"));
		assertTrue(PhoneUtil.isTel("0755-123456"));
		assertFalse(PhoneUtil.isTel("12345"));
		assertFalse(PhoneUtil.isTel(null));
	}

	@Test
	@DisplayName("isTel400800：400/800电话判断")
	void isTel400800() {
		assertTrue(PhoneUtil.isTel400800("400-123-4567"));
		assertTrue(PhoneUtil.isTel400800("8001234567"));
		assertFalse(PhoneUtil.isTel400800("12345"));
		assertFalse(PhoneUtil.isTel400800(null));
	}

	// ---- isPhone ----

	@Test
	@DisplayName("isPhone：电话号码综合判断")
	void isPhone() {
		assertTrue(PhoneUtil.isPhone("13900001234"));
		assertTrue(PhoneUtil.isPhone("010-12345678"));
		assertFalse(PhoneUtil.isPhone("abc"));
		assertFalse(PhoneUtil.isPhone(null));
	}

	// ---- hide ----

	@Test
	@DisplayName("hideBefore：隐藏号码前部分")
	void hideBefore() {
		assertEquals("*******1234", PhoneUtil.hideBefore("13900001234").toString());
		assertNull(PhoneUtil.hideBefore(null));
	}

	@Test
	@DisplayName("hideBetween：隐藏号码中间部分")
	void hideBetween() {
		assertEquals("139****1234", PhoneUtil.hideBetween("13900001234").toString());
	}

	@Test
	@DisplayName("hideAfter：隐藏号码后部分")
	void hideAfter() {
		assertEquals("1390000****", PhoneUtil.hideAfter("13900001234").toString());
	}

	// ---- sub ----

	@Test
	@DisplayName("subBefore：截取号码前部分")
	void subBefore() {
		assertEquals("139", PhoneUtil.subBefore("13900001234").toString());
		assertNull(PhoneUtil.subBefore(null));
	}

	@Test
	@DisplayName("subBetween：截取号码中间部分")
	void subBetween() {
		assertEquals("0000", PhoneUtil.subBetween("13900001234").toString());
	}

	@Test
	@DisplayName("subAfter：截取号码后部分")
	void subAfter() {
		assertEquals("1234", PhoneUtil.subAfter("13900001234").toString());
	}

	// ---- subTel ----

	@Test
	@DisplayName("subTelBefore：截取固话区号")
	void subTelBefore() {
		assertEquals("010", PhoneUtil.subTelBefore("010-12345678").toString());
		assertNull(PhoneUtil.subTelBefore(null));
	}

	@Test
	@DisplayName("subTelAfter：截取固话号码")
	void subTelAfter() {
		assertEquals("12345678", PhoneUtil.subTelAfter("010-12345678").toString());
	}
}
