package cc.hongz.ztools.core.convert;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 类型转换工具单元测试。
 * <p>
 * 全角半角转换。
 *
 * @author Looly
 */
@DisplayName("ConvertToSBCAndDBC 全角半角转换")
public class ConvertToSBCAndDBCTest {

    @Test
    @DisplayName("转全角")
    public void toSBCTest() {
        String a = "123456789";
        String sbc = Convert.toSBC(a);
        assertEquals("１２３４５６７８９", sbc);
    }

    @Test
    @DisplayName("转半角")
    public void toDBCTest() {
        String a = "１２３４５６７８９";
        String dbc = Convert.toDBC(a);
        assertEquals("123456789", dbc);
    }
}
