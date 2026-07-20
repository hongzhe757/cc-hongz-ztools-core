package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link NamingCase} 单元测试.
 */
class NamingCaseTest {

	@Test
	void toCamelCaseTest() {
		assertEquals("tableTestOfDay", NamingCase.toCamelCase("Table_Test_Of_day"));
		assertEquals("TableTestOfDay", NamingCase.toCamelCase("TableTestOfDay"));
		assertEquals("abc1d", NamingCase.toCamelCase("abc_1d"));
	}

	@Test
	void toUnderLineCaseTest() {
		assertEquals("table_test_of_day", NamingCase.toUnderlineCase("Table_Test_Of_day"));
		assertEquals("_table_test_of_day_", NamingCase.toUnderlineCase("_Table_Test_Of_day_"));
		assertEquals("PN_label", NamingCase.toUnderlineCase("PNLabel"));
		assertEquals("customer_nick_v2", NamingCase.toUnderlineCase("customerNickV2"));
		assertEquals("DEPT_NAME", NamingCase.toUnderlineCase("DEPT_NAME"));
	}

	@Test
	void issue3031Test() {
		String camelCase = NamingCase.toCamelCase("user_name,BIRTHDAY");
		assertEquals("userName,birthday", camelCase);

		camelCase = NamingCase.toCamelCase("user_name,BIRTHDAY", '_', false);
		assertEquals("userName,BIRTHDAY", camelCase);
	}
}
