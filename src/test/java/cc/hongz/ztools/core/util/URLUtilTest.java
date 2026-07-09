package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link URLUtil} 单元测试.
 * <p>
 * 覆盖 Data URI Base64 字符串生成.
 *
 * @author hongzhe.qin
 */
class URLUtilTest {

    /**
     * 验证 {@link URLUtil#getDataUriBase64(String, String)} 标准 MIME 类型拼接.
     */
    @Test
    void getDataUriBase64() {
        assertEquals("data:image/png;base64,abc", URLUtil.getDataUriBase64("image/png", "abc"));
    }

    /**
     * 验证空 MIME 类型时仍生成合法 Data URI 前缀.
     */
    @Test
    void getDataUriBase64BlankMime() {
        assertEquals("data:;base64,abc", URLUtil.getDataUriBase64("", "abc"));
    }
}
