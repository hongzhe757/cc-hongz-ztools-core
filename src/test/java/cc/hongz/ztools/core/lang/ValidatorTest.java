package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validator 单元测试.
 */
public class ValidatorTest {

    @Test
    @DisplayName("isBirthday 字符串格式生日验证")
    public void isBirthdayStringTest() {
        assertTrue(Validator.isBirthday("2000-01-01"));
        assertTrue(Validator.isBirthday("20000101"));
        assertTrue(Validator.isBirthday("2000/01/01"));
        assertFalse(Validator.isBirthday("2000-13-01"));
        assertFalse(Validator.isBirthday("invalid"));
    }

    @Test
    @DisplayName("isBirthday null 或空返回 false")
    public void isBirthdayBlankTest() {
        assertFalse(Validator.isBirthday(null));
        assertFalse(Validator.isBirthday(""));
    }

    @Test
    @DisplayName("isBirthday 年月日验证")
    public void isBirthdayIntTest() {
        assertTrue(Validator.isBirthday(2000, 1, 1));
        assertTrue(Validator.isBirthday(2000, 2, 29)); // 闰年
        assertFalse(Validator.isBirthday(2001, 2, 29)); // 非闰年
        assertFalse(Validator.isBirthday(2000, 13, 1));
        assertFalse(Validator.isBirthday(2000, 1, 32));
        assertFalse(Validator.isBirthday(1899, 1, 1));
    }
}
