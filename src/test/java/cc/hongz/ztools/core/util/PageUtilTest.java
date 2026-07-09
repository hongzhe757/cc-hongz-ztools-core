package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Segment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link PageUtil} 单元测试.
 * <p>
 * 覆盖分页起止下标、总页数、彩虹分页条及 {@link Segment} 转换.
 *
 * @author hongzhe.qin
 */
@DisplayName("PageUtil 分页计算")
class PageUtilTest {

    /**
     * 验证 {@link PageUtil#transToStartEnd(int, int)} 在默认首页为 0 时的起止下标.
     */
    @Test
    @DisplayName("transToStartEnd：首页为 0")
    void transToStartEndFirstPageZero() {
        final int[] startEnd1 = PageUtil.transToStartEnd(0, 10);
        assertEquals(0, startEnd1[0]);
        assertEquals(10, startEnd1[1]);

        final int[] startEnd2 = PageUtil.transToStartEnd(1, 10);
        assertEquals(10, startEnd2[0]);
        assertEquals(20, startEnd2[1]);
    }

    /**
     * 验证 {@link PageUtil#getStart(int, int)} 在页码小于首页时按首页处理.
     */
    @Test
    @DisplayName("getStart：负页码按首页处理")
    void getStartNegativePageClamped() {
        assertEquals(0, PageUtil.getStart(-1, 10));
    }

    /**
     * 验证 {@link PageUtil#totalPage(int, int)} 向上取整计算总页数.
     */
    @Test
    @DisplayName("totalPage：余数非零时页数 +1")
    void totalPageWithRemainder() {
        assertEquals(7, PageUtil.totalPage(20, 3));
    }

    /**
     * 验证 {@link PageUtil#toSegment(int, int)} 返回的区间长度等于 pageSize.
     */
    @Test
    @DisplayName("toSegment：区间长度等于 pageSize")
    void toSegmentLength() {
        final Segment<Integer> segment = PageUtil.toSegment(1, 10);
        assertEquals(10, segment.getStartIndex());
        assertEquals(20, segment.getEndIndex());
        assertEquals(10, segment.length());
    }

    /**
     * 验证 {@link PageUtil#rainbow(int, int, int)} 居中窗口分页条.
     */
    @Test
    @DisplayName("rainbow：居中 6 页窗口")
    void rainbowCenterWindow() {
        final int[] rainbow = PageUtil.rainbow(5, 20, 6);
        assertArrayEquals(new int[]{3, 4, 5, 6, 7, 8}, rainbow);
    }
}
