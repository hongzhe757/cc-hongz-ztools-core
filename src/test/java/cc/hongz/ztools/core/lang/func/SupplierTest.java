package cc.hongz.ztools.core.lang.func;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * {@link Supplier1}~{@link Supplier5} 单元测试
 */
public class SupplierTest {

	@Test
	public void supplier1Test() {
		Supplier1<String, Integer> s1 = p -> "value:" + p;
		assertEquals("value:1", s1.get(1));

		Supplier<String> supplier = s1.toSupplier(42);
		assertEquals("value:42", supplier.get());
	}

	@Test
	public void supplier2Test() {
		Supplier2<String, Integer, String> s2 = (p1, p2) -> p1 + ":" + p2;
		assertEquals("1:hello", s2.get(1, "hello"));

		Supplier<String> supplier = s2.toSupplier(2, "world");
		assertEquals("2:world", supplier.get());
	}

	@Test
	public void supplier3Test() {
		Supplier3<String, Integer, String, Boolean> s3 = (p1, p2, p3) -> p1 + ":" + p2 + ":" + p3;
		assertEquals("1:a:true", s3.get(1, "a", true));

		Supplier<String> supplier = s3.toSupplier(2, "b", false);
		assertEquals("2:b:false", supplier.get());
	}

	@Test
	public void supplier4Test() {
		Supplier4<String, Integer, String, Boolean, Double> s4 =
				(p1, p2, p3, p4) -> p1 + ":" + p2 + ":" + p3 + ":" + p4;
		assertEquals("1:a:true:3.14", s4.get(1, "a", true, 3.14));

		Supplier<String> supplier = s4.toSupplier(2, "b", false, 2.71);
		assertEquals("2:b:false:2.71", supplier.get());
	}

	@Test
	public void supplier5Test() {
		Supplier5<String, Integer, String, Boolean, Double, Long> s5 =
				(p1, p2, p3, p4, p5) -> p1 + ":" + p2 + ":" + p3 + ":" + p4 + ":" + p5;
		assertEquals("1:a:true:3.14:100", s5.get(1, "a", true, 3.14, 100L));

		Supplier<String> supplier = s5.toSupplier(2, "b", false, 2.71, 200L);
		assertEquals("2:b:false:2.71:200", supplier.get());
	}
}
