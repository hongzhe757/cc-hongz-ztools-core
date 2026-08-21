package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link StreamProgress} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("StreamProgress 流进度")
class StreamProgressTest {

    @Test
    @DisplayName("start/progress/finish 回调")
    void progressCallbackTest() {
        final boolean[] started = {false};
        final long[] totalArr = {0};
        final long[] sizeArr = {0};
        final boolean[] finished = {false};
        final StreamProgress progress = new StreamProgress() {
            @Override
            public void start() {
                started[0] = true;
            }

            @Override
            public void progress(final long total, final long progressSize) {
                totalArr[0] = total;
                sizeArr[0] = progressSize;
            }

            @Override
            public void finish() {
                finished[0] = true;
            }
        };
        progress.start();
        progress.progress(100, 50);
        progress.finish();
        assertTrue(started[0]);
        assertEquals(100, totalArr[0]);
        assertEquals(50, sizeArr[0]);
        assertTrue(finished[0]);
    }
}
