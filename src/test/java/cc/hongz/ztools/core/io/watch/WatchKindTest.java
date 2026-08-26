package cc.hongz.ztools.core.io.watch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.StandardWatchEventKinds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link WatchKind} 单元测试.
 */
@DisplayName("WatchKind 单元测试")
class WatchKindTest {

    @DisplayName("OVERFLOW 映射标准类型")
    @Test
    void overflowMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.OVERFLOW, WatchKind.OVERFLOW.getValue());
    }

    @DisplayName("MODIFY 映射标准类型")
    @Test
    void modifyMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_MODIFY, WatchKind.MODIFY.getValue());
    }

    @DisplayName("CREATE 映射标准类型")
    @Test
    void createMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_CREATE, WatchKind.CREATE.getValue());
    }

    @DisplayName("DELETE 映射标准类型")
    @Test
    void deleteMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_DELETE, WatchKind.DELETE.getValue());
    }

    @DisplayName("ALL 包含四种类型")
    @Test
    void allContainsFourKinds() {
        assertEquals(4, WatchKind.ALL.length);
    }

    @DisplayName("values 包含所有枚举常量")
    @Test
    void valuesContainsAllEnumConstants() {
        assertEquals(4, WatchKind.values().length);
    }

    @DisplayName("valueOf 返回正确枚举")
    @Test
    void valueOfReturnsCorrectEnum() {
        assertEquals(WatchKind.CREATE, WatchKind.valueOf("CREATE"));
    }
}
