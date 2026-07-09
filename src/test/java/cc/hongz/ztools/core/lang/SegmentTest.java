package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Segment}、{@link DefaultSegment}、{@link Filter} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Segment / Filter 区间与过滤")
class SegmentTest {

    /**
     * 验证 {@link DefaultSegment} 保存的起止下标.
     */
    @Test
    @DisplayName("DefaultSegment 起止下标")
    void defaultSegmentIndexes() {
        final Segment<Integer> segment = new DefaultSegment<>(3, 13);
        assertEquals(3, segment.getStartIndex());
        assertEquals(13, segment.getEndIndex());
    }

    /**
     * 验证 {@link Segment#length()} 返回起止之差的绝对值.
     */
    @Test
    @DisplayName("Segment#length 区间长度")
    void segmentLength() {
        final Segment<Integer> segment = new DefaultSegment<>(10, 25);
        assertEquals(15, segment.length());
    }

    /**
     * 验证 {@link Filter} 可作为 lambda 用于谓词过滤.
     */
    @Test
    @DisplayName("Filter lambda 接受偶数")
    void filterAcceptEven() {
        final Filter<Integer> even = value -> value % 2 == 0;
        assertTrue(even.accept(4));
    }
}
