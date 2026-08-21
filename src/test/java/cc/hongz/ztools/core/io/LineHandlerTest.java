package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link LineHandler} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("LineHandler 行处理器")
class LineHandlerTest {

    @Test
    @DisplayName("handle 处理行")
    void handleTest() {
        final List<String> lines = new ArrayList<>();
        final LineHandler handler = lines::add;
        handler.handle("hello");
        handler.handle("world");
        assertEquals(List.of("hello", "world"), lines);
    }
}
