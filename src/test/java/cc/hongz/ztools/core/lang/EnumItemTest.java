package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link EnumItem} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("EnumItem 枚举元素")
class EnumItemTest {

    enum TestEnum implements EnumItem<TestEnum> {
        A(1, "甲"),
        B(2, "乙");

        private final int val;
        private final String text;

        TestEnum(final int val, final String text) {
            this.val = val;
            this.text = text;
        }

        @Override
        public int intVal() {
            return val;
        }

        @Override
        public String text() {
            return text;
        }
    }

    @Test
    @DisplayName("fromInt 整数转枚举")
    void fromIntTest() {
        assertEquals(TestEnum.A, TestEnum.A.fromInt(1));
        assertEquals(TestEnum.B, TestEnum.A.fromInt(2));
        assertNull(TestEnum.A.fromInt(3));
        assertNull(TestEnum.A.fromInt(null));
    }

    @Test
    @DisplayName("fromStr 字符串转枚举")
    void fromStrTest() {
        assertEquals(TestEnum.A, TestEnum.A.fromStr("A"));
        assertNull(TestEnum.A.fromStr("C"));
        assertNull(TestEnum.A.fromStr(null));
    }

    @Test
    @DisplayName("textAndItems 文本与枚举项")
    void textAndItemsTest() {
        assertEquals("甲", TestEnum.A.text());
        assertEquals(2, TestEnum.A.items().length);
    }
}
