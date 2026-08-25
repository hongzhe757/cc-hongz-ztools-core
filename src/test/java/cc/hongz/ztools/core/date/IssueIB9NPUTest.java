package cc.hongz.ztools.core.date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

@DisplayName("IssueIB9NPU 解析日期")

public class IssueIB9NPUTest {
    @Test
    @DisplayName("解析日期")
    void parseTest() {
        DateUtil.parse("202409032400", new SimpleDateFormat("yyyyMMddHHmm"));
    }
}
