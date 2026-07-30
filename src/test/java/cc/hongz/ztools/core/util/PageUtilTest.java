package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.lang.Segment;
import org.junit.jupiter.api.AfterEach;
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
     * 每个测试后恢复 firstPageNo 为默认值 0.
     */
    @AfterEach
    void resetFirstPageNo() {
        PageUtil.setFirstPageNo(0);
    }

    // ---- getFirstPageNo / setFirstPageNo / setOneAsFirstPageNo ----

    /**
     * 验证默认首页页码为 0.
     */
    @Test
    @DisplayName("getFirstPageNo：默认值为 0")
    void getFirstPageNoDefault() {
        assertEquals(0, PageUtil.getFirstPageNo());
    }

    /**
     * 验证设置首页页码为 1 后生效.
     */
    @Test
    @DisplayName("setFirstPageNo：设置为 1")
    void setFirstPageNoToOne() {
        PageUtil.setFirstPageNo(1);
        assertEquals(1, PageUtil.getFirstPageNo());
    }

    /**
     * 验证 setOneAsFirstPageNo 将首页设为 1.
     */
    @Test
    @DisplayName("setOneAsFirstPageNo：首页设为 1")
    void setOneAsFirstPageNoTest() {
        PageUtil.setOneAsFirstPageNo();
        assertEquals(1, PageUtil.getFirstPageNo());
    }

    // ---- getStart ----

    /**
     * 验证默认首页为 0 时 getStart 计算正确.
     */
    @Test
    @DisplayName("getStart：默认首页为 0")
    void getStartDefaultFirstPage() {
        assertEquals(0, PageUtil.getStart(0, 10));
        assertEquals(10, PageUtil.getStart(1, 10));
        assertEquals(20, PageUtil.getStart(2, 10));
    }

    /**
     * 验证首页为 1 时 getStart 计算正确.
     */
    @Test
    @DisplayName("getStart：首页为 1 时偏移计算")
    void getStartFirstPageOne() {
        PageUtil.setFirstPageNo(1);
        assertEquals(0, PageUtil.getStart(1, 10));
        assertEquals(10, PageUtil.getStart(2, 10));
    }

    /**
     * 验证页码小于首页时按首页处理.
     */
    @Test
    @DisplayName("getStart：负页码按首页处理")
    void getStartNegativePageClamped() {
        assertEquals(0, PageUtil.getStart(-1, 10));
    }

    /**
     * 验证 pageSize 为 0 时返回 0.
     */
    @Test
    @DisplayName("getStart：pageSize 为 0 时返回 0")
    void getStartZeroPageSize() {
        assertEquals(0, PageUtil.getStart(5, 0));
    }

    // ---- getEnd ----

    /**
     * 验证 getEnd 返回结束位置.
     */
    @Test
    @DisplayName("getEnd：计算结束位置")
    void getEndTest() {
        assertEquals(10, PageUtil.getEnd(0, 10));
        assertEquals(20, PageUtil.getEnd(1, 10));
    }

    // ---- transToStartEnd ----

    /**
     * 验证 transToStartEnd 在默认首页为 0 时的起止下标.
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

    // ---- toSegment ----

    /**
     * 验证 toSegment 返回的区间长度等于 pageSize.
     */
    @Test
    @DisplayName("toSegment：区间长度等于 pageSize")
    void toSegmentLength() {
        final Segment<Integer> segment = PageUtil.toSegment(1, 10);
        assertEquals(10, segment.getStartIndex());
        assertEquals(20, segment.getEndIndex());
        assertEquals(10, segment.length());
    }

    // ---- totalPage ----

    /**
     * 验证 totalPage(int, int) 向上取整计算总页数.
     */
    @Test
    @DisplayName("totalPage(int, int)：余数非零时页数 +1")
    void totalPageWithRemainder() {
        assertEquals(7, PageUtil.totalPage(20, 3));
    }

    /**
     * 验证 totalPage(int, int) 整除时页数正确.
     */
    @Test
    @DisplayName("totalPage(int, int)：整除时页数正确")
    void totalPageExact() {
        assertEquals(5, PageUtil.totalPage(10, 2));
    }

    /**
     * 验证 totalPage(int, int) pageSize 为 0 时返回 0.
     */
    @Test
    @DisplayName("totalPage(int, int)：pageSize 为 0 返回 0")
    void totalPageZeroPageSize() {
        assertEquals(0, PageUtil.totalPage(100, 0));
    }

    /**
     * 验证 totalPage(long, int) 大数计算.
     */
    @Test
    @DisplayName("totalPage(long, int)：大数计算")
    void totalPageLong() {
        assertEquals(1000000001, PageUtil.totalPage(10000000001L, 10));
    }

    // ---- rainbow ----

    /**
     * 验证 rainbow 居中窗口分页条.
     */
    @Test
    @DisplayName("rainbow：居中 6 页窗口")
    void rainbowCenterWindow() {
        final int[] rainbow = PageUtil.rainbow(5, 20, 6);
        assertArrayEquals(new int[]{3, 4, 5, 6, 7, 8}, rainbow);
    }

    /**
     * 验证 rainbow 起始端分页条.
     */
    @Test
    @DisplayName("rainbow：起始端分页条")
    void rainbowStartWindow() {
        final int[] rainbow = PageUtil.rainbow(1, 20, 10);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, rainbow);
    }

    /**
     * 验证 rainbow 末端分页条.
     */
    @Test
    @DisplayName("rainbow：末端分页条")
    void rainbowEndWindow() {
        final int[] rainbow = PageUtil.rainbow(20, 20, 10);
        assertArrayEquals(new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, rainbow);
    }

    /**
     * 验证 rainbow 总页数小于展示数时返回全部页码.
     */
    @Test
    @DisplayName("rainbow：总页数小于展示数")
    void rainbowTotalLessThanDisplay() {
        final int[] rainbow = PageUtil.rainbow(1, 3, 10);
        assertArrayEquals(new int[]{1, 2, 3}, rainbow);
    }

    /**
     * 验证 rainbow 默认 10 页版本.
     */
    @Test
    @DisplayName("rainbow(int, int)：默认 10 页")
    void rainbowDefault() {
        final int[] rainbow = PageUtil.rainbow(5, 20);
        assertEquals(10, rainbow.length);
    }
}
