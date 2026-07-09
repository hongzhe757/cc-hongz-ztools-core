package cc.hongz.ztools.core.codec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link BCD} 单元测试.
 * <p>
 * 覆盖 ASCII 字符串与 BCD 字节数组互转.
 *
 * @author hongzhe.qin
 */
class BCDTest {

    /**
     * 验证字符串转 BCD 再转回字符串.
     */
    @Test
    void strToBcdAndBack() {
        final String text = "123456ABCDEF";
        final byte[] bcd = BCD.strToBcd(text);
        assertEquals(text, BCD.bcdToStr(bcd));
    }
}
