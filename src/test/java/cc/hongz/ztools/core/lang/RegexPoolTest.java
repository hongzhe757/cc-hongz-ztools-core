package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link RegexPool} 单元测试.
 * <p>
 * 覆盖全部常用正则常量的匹配.
 *
 * @author hongzhe.qin
 */
@DisplayName("RegexPool 常用正则")
class RegexPoolTest {

    /**
     * 验证 {@link RegexPool#GENERAL}、{@link RegexPool#NUMBERS}、{@link RegexPool#WORD}.
     */
    @Test
    @DisplayName("数字、字母、下划线")
    void generalNumbersWord() {
        assertTrue("abc_123".matches(RegexPool.GENERAL));
        assertFalse("abc-123".matches(RegexPool.GENERAL));
        assertTrue("123".matches(RegexPool.NUMBERS));
        assertTrue("abc".matches(RegexPool.WORD));
        assertFalse("abc1".matches(RegexPool.WORD));
    }

    /**
     * 验证 {@link RegexPool#GROUP_VAR}.
     */
    @Test
    @DisplayName("分组变量")
    void groupVar() {
        assertTrue("$123".matches(RegexPool.GROUP_VAR));
        assertFalse("123".matches(RegexPool.GROUP_VAR));
    }

    /**
     * 验证 {@link RegexPool#CHINESE}、{@link RegexPool#CHINESES}、{@link RegexPool#GENERAL_WITH_CHINESE}.
     */
    @Test
    @DisplayName("中文")
    void chinese() {
        assertTrue("中".matches(RegexPool.CHINESE));
        assertTrue("中文".matches(RegexPool.CHINESES));
        assertFalse("abc".matches(RegexPool.CHINESE));
        assertTrue("中文abc_123".matches(RegexPool.GENERAL_WITH_CHINESE));
    }

    /**
     * 验证 {@link RegexPool#IPV4}、{@link RegexPool#IPV6}.
     */
    @Test
    @DisplayName("IPv4 与 IPv6")
    void ip() {
        assertTrue("192.168.1.1".matches(RegexPool.IPV4));
        assertFalse("256.1.1.1".matches(RegexPool.IPV4));
        assertTrue("::1".matches(RegexPool.IPV6));
        assertTrue("2001:db8::1".matches(RegexPool.IPV6));
    }

    /**
     * 验证 {@link RegexPool#MONEY}.
     */
    @Test
    @DisplayName("货币")
    void money() {
        assertTrue("100".matches(RegexPool.MONEY));
        assertTrue("12.5".matches(RegexPool.MONEY));
        assertFalse("abc".matches(RegexPool.MONEY));
    }

    /**
     * 验证 {@link RegexPool#EMAIL}、{@link RegexPool#EMAIL_WITH_CHINESE}.
     */
    @Test
    @DisplayName("邮箱")
    void email() {
        assertTrue("test@example.com".matches(RegexPool.EMAIL));
        assertFalse("not-an-email".matches(RegexPool.EMAIL));
        assertTrue("中文@test.com".matches(RegexPool.EMAIL_WITH_CHINESE));
    }

    /**
     * 验证 {@link RegexPool#MOBILE}、{@link RegexPool#MOBILE_HK}、{@link RegexPool#MOBILE_TW}、{@link RegexPool#MOBILE_MO}.
     */
    @Test
    @DisplayName("手机号")
    void mobile() {
        assertTrue("13900221432".matches(RegexPool.MOBILE));
        assertFalse("12345".matches(RegexPool.MOBILE));
        assertTrue("51004810".matches(RegexPool.MOBILE_HK));
        assertTrue("+8860960000000".matches(RegexPool.MOBILE_TW));
        assertTrue("68000000".matches(RegexPool.MOBILE_MO));
    }

    /**
     * 验证 {@link RegexPool#TEL}、{@link RegexPool#TEL_400_800}.
     */
    @Test
    @DisplayName("座机与 400/800")
    void tel() {
        assertTrue("010-12345678".matches(RegexPool.TEL));
        assertTrue("4001234567".matches(RegexPool.TEL_400_800));
    }

    /**
     * 验证 {@link RegexPool#CITIZEN_ID}.
     */
    @Test
    @DisplayName("身份证")
    void citizenId() {
        assertTrue("110101199003074477".matches(RegexPool.CITIZEN_ID));
        assertFalse("123".matches(RegexPool.CITIZEN_ID));
    }

    /**
     * 验证 {@link RegexPool#BIRTHDAY}.
     */
    @Test
    @DisplayName("生日")
    void birthday() {
        assertTrue("19900307".matches(RegexPool.BIRTHDAY));
        assertTrue("1990-03-07".matches(RegexPool.BIRTHDAY));
    }

    /**
     * 验证 {@link RegexPool#URI}、{@link RegexPool#URL}、{@link RegexPool#URL_HTTP}.
     */
    @Test
    @DisplayName("URI 与 URL")
    void url() {
        assertTrue("http://example.com".matches(RegexPool.URI));
        assertTrue("http://example.com".matches(RegexPool.URL));
        assertTrue("https://example.com".matches(RegexPool.URL_HTTP));
        assertTrue("ftp://example.com".matches(RegexPool.URL_HTTP));
        assertFalse("ws://example.com".matches(RegexPool.URL_HTTP));
    }

    /**
     * 验证 {@link RegexPool#URL_SCHEME} 覆盖全部常用协议.
     */
    @Test
    @DisplayName("URL 协议列表")
    void urlScheme() {
        final String[] schemes = {"http", "https", "ftp", "ftps", "file", "ws", "wss",
                "rtsp", "rtmp", "mms", "git", "svn", "ssh", "sftp", "ldap", "ldaps",
                "telnet", "nfs", "smb", "irc", "ircs", "xmpp"};
        for (final String scheme : schemes) {
            assertTrue((scheme + "://example.com").matches(RegexPool.URL_SCHEME), scheme);
        }
        assertFalse("mailto:user@example.com".matches(RegexPool.URL_SCHEME));
        assertFalse("tel:13800000000".matches(RegexPool.URL_SCHEME));
        assertFalse("data:text/plain,hello".matches(RegexPool.URL_SCHEME));
    }

    /**
     * 验证 {@link RegexPool#URI_SCHEME} 覆盖使用 ":"（不带 "//"）的常用协议.
     */
    @Test
    @DisplayName("冒号协议列表")
    void uriScheme() {
        final String[] schemes = {"mailto", "tel", "fax", "sms", "data", "blob", "geo", "magnet",
                "sip", "sips", "webcal", "javascript", "about", "view-source", "urn", "mid",
                "cid", "doi", "isbn", "issn"};
        for (final String scheme : schemes) {
            assertTrue((scheme + ":").matches(RegexPool.URI_SCHEME), scheme);
        }
        assertFalse("http:".matches(RegexPool.URI_SCHEME));
        assertFalse("telnet:".matches(RegexPool.URI_SCHEME));
    }

    /**
     * 验证 {@link RegexPool#UUID}、{@link RegexPool#UUID_SIMPLE}.
     */
    @Test
    @DisplayName("UUID")
    void uuid() {
        assertTrue("123e4567-e89b-12d3-a456-426614174000".matches(RegexPool.UUID));
        assertTrue("123e4567e89b12d3a456426614174000".matches(RegexPool.UUID_SIMPLE));
        assertFalse("not-a-uuid".matches(RegexPool.UUID));
    }

    /**
     * 验证 {@link RegexPool#MAC_ADDRESS}、{@link RegexPool#HEX}.
     */
    @Test
    @DisplayName("MAC 与十六进制")
    void macAndHex() {
        assertTrue("00:11:22:33:44:55".matches(RegexPool.MAC_ADDRESS));
        assertTrue("1a2b3c".matches(RegexPool.HEX));
        assertFalse("xyz".matches(RegexPool.HEX));
    }

    /**
     * 验证 {@link RegexPool#TIME}.
     */
    @Test
    @DisplayName("时间")
    void time() {
        assertTrue("12:30".matches(RegexPool.TIME));
        assertTrue("12时30分30".matches(RegexPool.TIME));
    }

    /**
     * 验证 {@link RegexPool#PLATE_NUMBER}.
     */
    @Test
    @DisplayName("车牌号")
    void plateNumber() {
        assertTrue("粤BA03205".matches(RegexPool.PLATE_NUMBER));
    }

    /**
     * 验证 {@link RegexPool#CREDIT_CODE}.
     */
    @Test
    @DisplayName("统一社会信用代码")
    void creditCode() {
        assertTrue("91350100M000100Y43".matches(RegexPool.CREDIT_CODE));
    }

    /**
     * 验证 {@link RegexPool#CAR_VIN}、{@link RegexPool#CAR_DRIVING_LICENCE}.
     */
    @Test
    @DisplayName("车架号与驾驶证")
    void carVinAndLicence() {
        assertTrue("LSJA24U62JG269225".matches(RegexPool.CAR_VIN));
        assertTrue("430101758218".matches(RegexPool.CAR_DRIVING_LICENCE));
    }

    /**
     * 验证 {@link RegexPool#CHINESE_NAME}.
     */
    @Test
    @DisplayName("中文姓名")
    void chineseName() {
        assertTrue("段正淳".matches(RegexPool.CHINESE_NAME));
        assertFalse("李".matches(RegexPool.CHINESE_NAME));
    }

    /**
     * 验证 {@link RegexPool#ZIP_CODE}.
     */
    @Test
    @DisplayName("邮编")
    void zipCode() {
        assertTrue("100000".matches(RegexPool.ZIP_CODE));
        assertFalse("123".matches(RegexPool.ZIP_CODE));
    }
}
