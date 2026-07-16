package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 脱敏工具类 DesensitizedUtils 安全测试
 *
 * @author dazer and nuesoft
 * @see DesensitizedUtil
 */
@DisplayName("DesensitizedUtil 脱敏工具")
class DesensitizedUtilTest {

    @Test
    @DisplayName("默认脱敏策略")
    void desensitizedTest() {
        assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
        assertEquals("", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.CLEAR_TO_EMPTY));
        assertNull(DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.CLEAR_TO_NULL));
        assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
        assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
        assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
        assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
        assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
        assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
        assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));

        assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
        assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
        assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
        assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
        assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
        assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
        assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
        assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));
        assertEquals("1101 **** **** **** 3256", DesensitizedUtil.desensitized("11011111222233333256", DesensitizedUtil.DesensitizedType.BANK_CARD));
        assertEquals("6227 **** **** **** 123", DesensitizedUtil.desensitized("6227880100100105123", DesensitizedUtil.DesensitizedType.BANK_CARD));
        assertEquals("192.*.*.*", DesensitizedUtil.desensitized("192.168.1.1", DesensitizedUtil.DesensitizedType.IPV4));
        assertEquals("2001:*:*:*:*:*:*:*", DesensitizedUtil.desensitized("2001:0db8:86a3:08d3:1319:8a2e:0370:7344", DesensitizedUtil.DesensitizedType.IPV6));
    }

    @Test
    @DisplayName("用户ID脱敏")
    void userIdTest() {
        assertEquals(Long.valueOf(0L), DesensitizedUtil.userId());
    }

    @Test
    @DisplayName("中文姓名脱敏")
    void chineseNameTest() {
        assertEquals("段**", DesensitizedUtil.chineseName("段正淳"));
    }

    @Test
    @DisplayName("身份证号脱敏")
    void idCardNumTest() {
        assertEquals("5***************1X", DesensitizedUtil.idCardNum("51343620000320711X", 1, 2));
    }

    @Test
    @DisplayName("固定电话脱敏")
    void fixedPhoneTest() {
        assertEquals("0915*****79", DesensitizedUtil.fixedPhone("09157518479"));
    }

    @Test
    @DisplayName("手机号脱敏")
    void mobilePhoneTest() {
        assertEquals("180****1999", DesensitizedUtil.mobilePhone("18049531999"));
    }

    @Test
    @DisplayName("地址脱敏")
    void addressTest() {
        assertEquals("北京市海淀区马连洼街*****", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 5));
        assertEquals("***************", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 50));
        assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 0));
        assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", -1));
    }

    @Test
    @DisplayName("电子邮箱脱敏")
    void emailTest() {
        assertEquals("d********@126.com", DesensitizedUtil.email("duandazhi@126.com"));
        assertEquals("d********@gmail.com.cn", DesensitizedUtil.email("duandazhi@gmail.com.cn"));
        assertEquals("d*************@gmail.com.cn", DesensitizedUtil.email("duandazhi-jack@gmail.com.cn"));
    }

    @Test
    @DisplayName("密码脱敏")
    void passwordTest() {
        assertEquals("**********", DesensitizedUtil.password("1234567890"));
    }

    @Test
    @DisplayName("车牌号脱敏")
    void carLicenseTest() {
        assertEquals("", DesensitizedUtil.carLicense(null));
        assertEquals("", DesensitizedUtil.carLicense(""));
        assertEquals("苏D4***0", DesensitizedUtil.carLicense("苏D40000"));
        assertEquals("陕A1****D", DesensitizedUtil.carLicense("陕A12345D"));
        assertEquals("京A123", DesensitizedUtil.carLicense("京A123"));
    }

    @Test
    @DisplayName("银行卡号脱敏")
    void bankCardTest() {
        assertEquals(null, DesensitizedUtil.bankCard(null));
        assertEquals("", DesensitizedUtil.bankCard(""));
        assertEquals("1234 **** **** **** **** 9", DesensitizedUtil.bankCard("1234 2222 3333 4444 6789 9"));
        assertEquals("1234 **** **** **** **** 91", DesensitizedUtil.bankCard("1234 2222 3333 4444 6789 91"));
        assertEquals("1234 **** **** **** 6789", DesensitizedUtil.bankCard("1234 2222 3333 4444 6789"));
        assertEquals("1234 **** **** **** 678", DesensitizedUtil.bankCard("1234 2222 3333 4444 678"));
    }

    @Test
    @DisplayName("护照号脱敏")
    void passportTest() {
        assertEquals(null, DesensitizedUtil.passport(null));
        assertEquals("", DesensitizedUtil.passport(""));
        assertEquals("EM*****67", DesensitizedUtil.passport("EM1234567"));
        assertEquals("*", DesensitizedUtil.passport("3"));
    }

    @Test
    @DisplayName("统一社会信用代码脱敏")
    void creditCodeTest() {
        assertEquals(null, DesensitizedUtil.creditCode(null));
        assertEquals("", DesensitizedUtil.creditCode(""));
        assertEquals("9111**********CDE7", DesensitizedUtil.creditCode("91110108MA01ABCDE7"));
    }
}
