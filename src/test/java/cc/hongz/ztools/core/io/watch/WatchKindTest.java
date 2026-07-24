package cc.hongz.ztools.core.io.watch;

import org.junit.jupiter.api.Test;

import java.nio.file.StandardWatchEventKinds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link WatchKind} 单元测试.
 */
class WatchKindTest {

    @Test
    void overflowMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.OVERFLOW, WatchKind.OVERFLOW.getValue());
    }

    @Test
    void modifyMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_MODIFY, WatchKind.MODIFY.getValue());
    }

    @Test
    void createMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_CREATE, WatchKind.CREATE.getValue());
    }

    @Test
    void deleteMapsToStandardKind() {
        assertSame(StandardWatchEventKinds.ENTRY_DELETE, WatchKind.DELETE.getValue());
    }

    @Test
    void allContainsFourKinds() {
        assertEquals(4, WatchKind.ALL.length);
    }

    @Test
    void valuesContainsAllEnumConstants() {
        assertEquals(4, WatchKind.values().length);
    }

    @Test
    void valueOfReturnsCorrectEnum() {
        assertEquals(WatchKind.CREATE, WatchKind.valueOf("CREATE"));
    }
}
