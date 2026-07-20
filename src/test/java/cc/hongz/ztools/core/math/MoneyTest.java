package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("货币类")
class MoneyTest {

	@Test
	@DisplayName("默认构造 - 金额为0")
	void defaultConstructor() {
		Money money = new Money();
		assertEquals(0, money.getCent());
		assertEquals("CNY", money.getCurrency().getCurrencyCode());
	}

	@Test
	@DisplayName("String构造")
	void stringConstructor() {
		Money money = new Money("10.50");
		assertEquals(1050, money.getCent());
		assertEquals(new BigDecimal("10.50"), money.getAmount());
	}

	@Test
	@DisplayName("double构造")
	void doubleConstructor() {
		Money money = new Money(10.50);
		assertEquals(1050, money.getCent());
	}

	@Test
	@DisplayName("BigDecimal构造")
	void bigDecimalConstructor() {
		Money money = new Money(new BigDecimal("10.50"));
		assertEquals(1050, money.getCent());
	}

	@Test
	@DisplayName("yuan+cent构造")
	void yuanCentConstructor() {
		Money money = new Money(10, 50);
		assertEquals(1050, money.getCent());
	}

	@Test
	@DisplayName("equals和hashCode")
	void equalsAndHashCode() {
		Money m1 = new Money("10.50");
		Money m2 = new Money("10.50");
		assertEquals(m1, m2);
		assertEquals(m1.hashCode(), m2.hashCode());
	}

	@Test
	@DisplayName("compareTo")
	void compareTo() {
		Money m1 = new Money("10.50");
		Money m2 = new Money("20.00");
		assertTrue(m1.compareTo(m2) < 0);
		assertTrue(m2.greaterThan(m1));
	}

	@Test
	@DisplayName("add - 加法")
	void add() {
		Money m1 = new Money("10.00");
		Money m2 = new Money("5.50");
		Money result = m1.add(m2);
		assertEquals(1550, result.getCent());
		// 原值不变
		assertEquals(1000, m1.getCent());
	}

	@Test
	@DisplayName("addTo - 累加")
	void addTo() {
		Money m1 = new Money("10.00");
		Money m2 = new Money("5.50");
		m1.addTo(m2);
		assertEquals(1550, m1.getCent());
	}

	@Test
	@DisplayName("subtract - 减法")
	void subtract() {
		Money m1 = new Money("10.00");
		Money m2 = new Money("3.50");
		Money result = m1.subtract(m2);
		assertEquals(650, result.getCent());
	}

	@Test
	@DisplayName("multiply(long)")
	void multiplyLong() {
		Money money = new Money("10.00");
		Money result = money.multiply(3);
		assertEquals(3000, result.getCent());
	}

	@Test
	@DisplayName("multiply(double)")
	void multiplyDouble() {
		Money money = new Money("10.00");
		Money result = money.multiply(1.5);
		assertEquals(1500, result.getCent());
	}

	@Test
	@DisplayName("multiply(BigDecimal)")
	void multiplyBigDecimal() {
		Money money = new Money("10.00");
		Money result = money.multiply(new BigDecimal("2.5"));
		assertEquals(2500, result.getCent());
	}

	@Test
	@DisplayName("divide(double)")
	void divideDouble() {
		Money money = new Money("10.00");
		Money result = money.divide(2.0);
		assertEquals(500, result.getCent());
	}

	@Test
	@DisplayName("divide - 除以0抛异常")
	void divideByZero() {
		Money money = new Money("10.00");
		assertThrows(ArithmeticException.class, () -> money.divide(0));
	}

	@Test
	@DisplayName("allocate(int) - 平均分配")
	void allocateInt() {
		Money money = new Money("10.00");
		Money[] results = money.allocate(3);
		assertEquals(3, results.length);
		// 1000分 / 3 = 333余1，第一份多1分
		assertEquals(334, results[0].getCent());
		assertEquals(333, results[1].getCent());
		assertEquals(333, results[2].getCent());
	}

	@Test
	@DisplayName("allocate(long[]) - 按比例分配")
	void allocateRatios() {
		Money money = new Money("10.00");
		Money[] results = money.allocate(new long[]{1, 2});
		assertEquals(2, results.length);
		// 1000 * 1/3 = 333, 1000 * 2/3 = 666, 余1分给第一份
		assertEquals(334, results[0].getCent());
		assertEquals(666, results[1].getCent());
	}

	@Test
	@DisplayName("toString")
	void toStringTest() {
		Money money = new Money("10.50");
		assertEquals("10.50", money.toString());
	}

	@Test
	@DisplayName("币种不匹配抛异常")
	void currencyMismatch() {
		Money cny = new Money("10.00");
		Money usd = new Money("10.00", Currency.getInstance("USD"));
		assertThrows(IllegalArgumentException.class, () -> cny.add(usd));
	}
}
