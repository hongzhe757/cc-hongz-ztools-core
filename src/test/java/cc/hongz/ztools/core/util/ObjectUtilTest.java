package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ObjectUtil} 单元测试.
 */
@DisplayName("ObjectUtil 工具类")
class ObjectUtilTest {

	// ---- defaultIfNull ----

	@Test
	@DisplayName("defaultIfNull：空值时返回默认值")
	void defaultIfNull() {
		assertEquals("d", ObjectUtil.defaultIfNull((String) null, "d"));
		assertEquals("v", ObjectUtil.defaultIfNull("v", "d"));
		assertNull(ObjectUtil.defaultIfNull((String) null, (String) null));
	}

	@Test
	@DisplayName("defaultIfNull：Supplier提供默认值")
	void defaultIfNullWithSupplier() {
		java.util.function.Supplier<String> supplier = () -> "default";
		assertEquals("default", ObjectUtil.<String>defaultIfNull(null, supplier));
		assertEquals("value", ObjectUtil.<String>defaultIfNull("value", supplier));
	}

	@Test
	@DisplayName("defaultIfNull：Function转换默认值")
	void defaultIfNullWithFunction() {
		java.util.function.Function<String, String> func = s -> "transformed";
		assertEquals("value", ObjectUtil.<String>defaultIfNull("value", func));
		java.util.function.Function<String, String> nullFunc = s -> null;
		assertNull(ObjectUtil.<String>defaultIfNull(null, nullFunc));
	}

	// ---- equal / notEqual ----

	@Test
	@DisplayName("equal：数字类型相等判断")
	void equalWithNumbers() {
		assertTrue(ObjectUtil.equal(1, 1));
		assertTrue(ObjectUtil.equal(1L, 1L));
		assertTrue(ObjectUtil.equal(new BigDecimal("2.0"), new BigDecimal("2.00")));
		assertFalse(ObjectUtil.equal(1, 2));
		// 不同类型（Long vs Integer）Objects.equals 返回 false
		assertFalse(ObjectUtil.equal(1L, 1));
	}

	@Test
	@DisplayName("equal：字符串相等判断")
	void equalWithStrings() {
		assertTrue(ObjectUtil.equal("abc", "abc"));
		assertFalse(ObjectUtil.equal("abc", "def"));
		assertFalse(ObjectUtil.equal("abc", null));
	}

	@Test
	@DisplayName("equals：equal的别名方法")
	void equalsAlias() {
		assertTrue(ObjectUtil.equals("a", "a"));
		assertFalse(ObjectUtil.equals("a", "b"));
	}

	@Test
	@DisplayName("notEqual：不相等判断")
	void notEqual() {
		assertTrue(ObjectUtil.notEqual("a", "b"));
		assertFalse(ObjectUtil.notEqual("a", "a"));
	}

	// ---- isNull / isNotNull ----

	@Test
	@DisplayName("isNull：空值判断")
	void isNull() {
		assertTrue(ObjectUtil.isNull(null));
		assertFalse(ObjectUtil.isNull("abc"));
	}

	@Test
	@DisplayName("isNotNull：非空判断")
	void isNotNull() {
		assertTrue(ObjectUtil.isNotNull("abc"));
		assertFalse(ObjectUtil.isNotNull(null));
	}

	// ---- isEmpty / isNotEmpty ----

	@Test
	@DisplayName("isEmpty：空值或空集合判断")
	void isEmpty() {
		assertTrue(ObjectUtil.isEmpty(null));
		assertTrue(ObjectUtil.isEmpty(""));
		assertTrue(ObjectUtil.isEmpty(new ArrayList<>()));
		assertTrue(ObjectUtil.isEmpty(new HashMap<>()));
		assertTrue(ObjectUtil.isEmpty(new int[0]));
		assertFalse(ObjectUtil.isEmpty("abc"));
		assertFalse(ObjectUtil.isEmpty(Arrays.asList(1, 2)));
	}

	@Test
	@DisplayName("isNotEmpty：非空判断")
	void isNotEmpty() {
		assertTrue(ObjectUtil.isNotEmpty("abc"));
		assertFalse(ObjectUtil.isNotEmpty(null));
		assertFalse(ObjectUtil.isNotEmpty(""));
	}

	// ---- length ----

	@Test
	@DisplayName("length：获取对象长度")
	void length() {
		assertEquals(0, ObjectUtil.length(null));
		assertEquals(3, ObjectUtil.length("abc"));
		assertEquals(2, ObjectUtil.length(Arrays.asList(1, 2)));
		assertEquals(0, ObjectUtil.length(new ArrayList<>()));
		assertEquals(3, ObjectUtil.length(new int[]{1, 2, 3}));
		assertEquals(2, ObjectUtil.length(new HashMap<String, String>() {{
			put("a", "1");
			put("b", "2");
		}}));
	}

	// ---- contains ----

	@Test
	@DisplayName("contains：是否包含元素")
	void contains() {
		assertFalse(ObjectUtil.contains(null, "a"));
		assertTrue(ObjectUtil.contains("hello world", "world"));
		assertFalse(ObjectUtil.contains("hello", 123));
		assertTrue(ObjectUtil.contains(Arrays.asList(1, 2, 3), 2));
		assertFalse(ObjectUtil.contains(Arrays.asList(1, 2, 3), 4));
		assertTrue(ObjectUtil.contains(new int[]{1, 2, 3}, 2));
	}

	// ---- defaultIfEmpty / defaultIfBlank ----

	@Test
	@DisplayName("defaultIfEmpty：空序列时返回默认值")
	void defaultIfEmptyCharSequence() {
		assertEquals("default", ObjectUtil.defaultIfEmpty(null, "default"));
		assertEquals("default", ObjectUtil.defaultIfEmpty("", "default"));
		assertEquals("value", ObjectUtil.defaultIfEmpty("value", "default"));
	}

	@Test
	@DisplayName("defaultIfBlank：空白时返回默认值")
	void defaultIfBlank() {
		assertEquals("default", ObjectUtil.defaultIfBlank(null, "default"));
		assertEquals("default", ObjectUtil.defaultIfBlank("", "default"));
		assertEquals("default", ObjectUtil.defaultIfBlank("  ", "default"));
		assertEquals("value", ObjectUtil.defaultIfBlank("value", "default"));
	}

	// ---- clone ----

	@Test
	@DisplayName("clone：克隆可克隆对象")
	void cloneCloneable() {
		String[] arr = {"a", "b"};
		String[] cloned = ObjectUtil.clone(arr);
		assertArrayEquals(arr, cloned);
		assertNotSame(arr, cloned);
	}

	@Test
	@DisplayName("cloneByStream：序列化流克隆")
	void cloneByStreamSerializable() {
		ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
		ArrayList<String> cloned = ObjectUtil.clone(list);
		assertEquals(list, cloned);
		assertNotSame(list, cloned);
	}

	@Test
	@DisplayName("cloneIfPossible：尝试克隆")
	void cloneIfPossible() {
		String[] arr = {"x", "y"};
		String[] cloned = ObjectUtil.cloneIfPossible(arr);
		assertArrayEquals(arr, cloned);
	}

	// ---- serialize / deserialize ----

	@Test
	@DisplayName("serialize/deserialize：序列化与反序列化")
	void serializeAndDeserialize() {
		String original = "hello";
		byte[] bytes = ObjectUtil.serialize(original);
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);
		String restored = ObjectUtil.deserialize(bytes);
		assertEquals(original, restored);
	}

	// ---- compare ----

	@Test
	@DisplayName("compare：对象比较")
	void compare() {
		assertTrue(ObjectUtil.compare(1, 2) < 0);
		assertEquals(0, ObjectUtil.compare(1, 1));
		assertTrue(ObjectUtil.compare(2, 1) > 0);
	}

	@Test
	@DisplayName("compare：含null的比较")
	void compareWithNull() {
		// 默认null排在前面（null小于任何值）
		assertTrue(ObjectUtil.compare(null, "a") < 0);
		assertTrue(ObjectUtil.compare("a", null) > 0);
		assertEquals(0, ObjectUtil.compare(null, null));
	}

	@Test
	@DisplayName("compare：null视为更大值")
	void compareNullGreater() {
		// nullGreater=true时null排在后面（null视为更大）
		assertTrue(ObjectUtil.compare(null, "a", true) > 0);
		assertTrue(ObjectUtil.compare("a", null, true) < 0);
	}

	// ---- isBasicType ----

	@Test
	@DisplayName("isBasicType：基本类型判断")
	void isBasicType() {
		assertTrue(ObjectUtil.isBasicType(1));
		assertTrue(ObjectUtil.isBasicType(true));
		assertFalse(ObjectUtil.isBasicType("abc")); // String不是基本类型
		assertFalse(ObjectUtil.isBasicType(null));
		assertFalse(ObjectUtil.isBasicType(new ArrayList<>()));
	}

	// ---- isValidIfNumber ----

	@Test
	@DisplayName("isValidIfNumber：有效数字判断")
	void isValidIfNumber() {
		assertTrue(ObjectUtil.isValidIfNumber(123));
		assertTrue(ObjectUtil.isValidIfNumber("abc")); // 非数字类型返回true
		assertTrue(ObjectUtil.isValidIfNumber(null)); // null返回true
		assertFalse(ObjectUtil.isValidIfNumber(Double.POSITIVE_INFINITY));
		assertFalse(ObjectUtil.isValidIfNumber(Float.NaN));
	}

	// ---- toString ----

	@Test
	@DisplayName("toString：对象转字符串")
	void toStringTest() {
		assertEquals("null", ObjectUtil.toString(null));
		assertEquals("abc", ObjectUtil.toString("abc"));
		assertEquals("123", ObjectUtil.toString(123));
	}

	// ---- emptyCount / hasNull / hasEmpty / isAllEmpty / isAllNotEmpty ----

	@Test
	@DisplayName("emptyCount：统计空值数量")
	void emptyCount() {
		assertEquals(2, ObjectUtil.emptyCount("a", null, "", "b"));
		assertEquals(0, ObjectUtil.emptyCount("a", "b"));
	}

	@Test
	@DisplayName("hasNull：是否包含null")
	void hasNull() {
		assertTrue(ObjectUtil.hasNull("a", null, "b"));
		assertFalse(ObjectUtil.hasNull("a", "b"));
	}

	@Test
	@DisplayName("hasEmpty：是否包含空值")
	void hasEmpty() {
		assertTrue(ObjectUtil.hasEmpty("a", null));
		assertTrue(ObjectUtil.hasEmpty("a", ""));
		assertFalse(ObjectUtil.hasEmpty("a", "b"));
	}

	@Test
	@DisplayName("isAllEmpty：是否全部为空")
	void isAllEmpty() {
		assertTrue(ObjectUtil.isAllEmpty(null, ""));
		assertFalse(ObjectUtil.isAllEmpty("a", null));
	}

	@Test
	@DisplayName("isAllNotEmpty：是否全部非空")
	void isAllNotEmpty() {
		assertTrue(ObjectUtil.isAllNotEmpty("a", "b"));
		assertFalse(ObjectUtil.isAllNotEmpty("a", null));
	}

	// ---- apply / accept ----

	@Test
	@DisplayName("apply：应用函数并返回结果")
	void apply() {
		assertEquals(3, ObjectUtil.apply("abc", String::length));
		assertNull(ObjectUtil.apply(null, (String s) -> s));
	}

	@Test
	@DisplayName("accept：消费操作")
	void accept() {
		StringBuilder sb = new StringBuilder();
		ObjectUtil.accept("hello", sb::append);
		assertEquals("hello", sb.toString());

		// null 不执行
		ObjectUtil.accept(null, (String s) -> sb.append("world"));
		assertEquals("hello", sb.toString());
	}

	// ---- getTypeArgument ----

	@Test
	@DisplayName("getTypeArgument：获取泛型参数类型")
	void getTypeArgument() {
		// ArrayList<String> 的第一个泛型参数应为 String
		ArrayList<String> list = new ArrayList<>();
		// 由于类型擦除，运行时无法获取泛型参数，此处仅验证不抛异常
		assertDoesNotThrow(() -> ObjectUtil.getTypeArgument(list));
	}
}
