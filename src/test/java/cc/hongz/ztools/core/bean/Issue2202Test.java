package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.text.NamingCase;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue2202Test {

    /**
     * https://github.com/chinabugotech/hutool/issues/2202
     */
    @Test
    @DisplayName("mapToBean fieldNameEditor camelCase")
    void mapToBeanWithFieldNameEditorTest(){
        Map<String, String> headerMap = new HashMap<>(5);
        headerMap.put("wechatpay-serial", "serial");
        headerMap.put("wechatpay-nonce", "nonce");
        headerMap.put("wechatpay-timestamp", "timestamp");
        headerMap.put("wechatpay-signature", "signature");
        ResponseSignVerifyParams case1 = BeanUtil.toBean(headerMap, ResponseSignVerifyParams.class,
                CopyOptions.create().setFieldNameEditor(field -> NamingCase.toCamelCase(field, '-')));

        assertEquals("serial", case1.getWechatpaySerial());
        assertEquals("nonce", case1.getWechatpayNonce());
        assertEquals("timestamp", case1.getWechatpayTimestamp());
        assertEquals("signature", case1.getWechatpaySignature());
    }

    @Data
    static class ResponseSignVerifyParams {
        private String wechatpaySerial;
        private String wechatpaySignature;
        private String wechatpayTimestamp;
        private String wechatpayNonce;
        private String body;
    }
}
