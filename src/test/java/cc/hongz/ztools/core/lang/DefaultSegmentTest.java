package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link DefaultSegment} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("DefaultSegment 片段")
class DefaultSegmentTest {

    @Test
    void getIndexTest() {
        final DefaultSegment<Integer> segment = new DefaultSegment<>(1, 10);
        assertEquals(1, segment.getStartIndex());
        assertEquals(10, segment.getEndIndex());
    }
}
