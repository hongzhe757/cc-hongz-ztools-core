package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.date.DateField;
import cc.hongz.ztools.core.date.DateRange;
import cc.hongz.ztools.core.date.DateTime;
import cc.hongz.ztools.core.date.DateUtil;
import cc.hongz.ztools.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * {@link Range} 单元测试。
 *
 * @author Looly
 */
@DisplayName("Range 范围生成器")
public class RangeTest {

    @Test
    @DisplayName("dateRange 日期范围")
    public void dateRangeTest() {
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-02");

        final Range<DateTime> range = new Range<>(start, end, (current, end1, index) -> {
            if (current.isAfterOrEquals(end1)) {
                return null;
            }
            return current.offsetNew(DateField.DAY_OF_YEAR, 1);
        });

        assertTrue(range.hasNext());
        assertEquals(DateUtil.parse("2017-01-01"), range.next());
        assertTrue(range.hasNext());
        assertEquals(DateUtil.parse("2017-01-02"), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("dateRangeFunc 范围函数")
    public void dateRangeFuncTest() {
        DateTime start = DateUtil.parse("2021-01-01");
        DateTime end = DateUtil.parse("2021-01-03");

        List<Integer> dayOfMonthList = DateUtil.rangeFunc(start, end, DateField.DAY_OF_YEAR, a -> DateTime.of(a).dayOfMonth());
        assertArrayEquals(dayOfMonthList.toArray(new Integer[]{}), new Integer[]{1, 2, 3});

        List<Integer> dayOfMonthList2 = DateUtil.rangeFunc(null, null, DateField.DAY_OF_YEAR, a -> DateTime.of(a).dayOfMonth());
        assertArrayEquals(dayOfMonthList2.toArray(new Integer[]{}), new Integer[]{});
    }

    @Test
    @DisplayName("dateRangeConsume 范围消费")
    public void dateRangeConsumeTest() {
        DateTime start = DateUtil.parse("2021-01-01");
        DateTime end = DateUtil.parse("2021-01-03");

        StringBuilder sb = new StringBuilder();
        DateUtil.rangeConsume(start, end, DateField.DAY_OF_YEAR, a -> sb.append(DateTime.of(a).dayOfMonth()).append("#"));
        assertEquals(sb.toString(), "1#2#3#");

        StringBuilder sb2 = new StringBuilder();
        DateUtil.rangeConsume(null, null, DateField.DAY_OF_YEAR, a -> sb2.append(DateTime.of(a).dayOfMonth()).append("#"));
        assertEquals(sb2.toString(), StrUtil.EMPTY);
    }

    @Test
    @DisplayName("dateRange 月范围")
    public void dateRangeTest2() {
        DateTime start = DateUtil.parse("2021-01-31");
        DateTime end = DateUtil.parse("2021-03-31");

        final DateRange range = DateUtil.range(start, end, DateField.MONTH);

        assertTrue(range.hasNext());
        assertEquals(DateUtil.parse("2021-01-31"), range.next());
        assertTrue(range.hasNext());
        assertEquals(DateUtil.parse("2021-02-28"), range.next());
        assertTrue(range.hasNext());
        assertEquals(DateUtil.parse("2021-03-31"), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("intRange 整数范围")
    public void intRangeTest() {
        final Range<Integer> range = new Range<>(1, 1, (current, end, index) -> current >= end ? null : current + 10);

        assertTrue(range.hasNext());
        assertEquals(Integer.valueOf(1), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("rangeByStep 步进范围")
    public void rangeByStepTest() {
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-03");

        // 测试包含开始和结束情况下步进为1的情况
        DateRange range = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
        assertEquals(range.next(), DateUtil.parse("2017-01-01"));
        assertEquals(range.next(), DateUtil.parse("2017-01-02"));
        assertEquals(range.next(), DateUtil.parse("2017-01-03"));
        try {
            range.next();
            fail("已超过边界，下一个元素不应该存在！");
        } catch (NoSuchElementException ignored) {
        }

        // 测试多步进的情况
        range = new DateRange(start, end, DateField.DAY_OF_YEAR, 2);
        assertEquals(DateUtil.parse("2017-01-01"), range.next());
        assertEquals(DateUtil.parse("2017-01-03"), range.next());
    }

    @Test
    @DisplayName("rangeDayOfYear 不含首尾")
    public void rangeDayOfYearTest() {
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-05");

        // 测试不包含开始结束时间的情况
        DateRange range = new DateRange(start, end, DateField.DAY_OF_YEAR, 1, false, false);
        assertEquals(DateUtil.parse("2017-01-02"), range.next());
        assertEquals(DateUtil.parse("2017-01-03"), range.next());
        assertEquals(DateUtil.parse("2017-01-04"), range.next());
        try {
            range.next();
            fail("不包含结束时间情况下，下一个元素不应该存在！");
        } catch (NoSuchElementException ignored) {
        }
    }

    @Test
    @DisplayName("rangeToList 范围转列表")
    public void rangeToListTest() {
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-31");

        List<DateTime> rangeToList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
        assertEquals(DateUtil.parse("2017-01-01"), rangeToList.get(0));
        assertEquals(DateUtil.parse("2017-01-02"), rangeToList.get(1));
    }


    @Test
    @DisplayName("rangeContains 范围交集")
    public void rangeContains() {
        // 开始区间
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-31");
        DateRange startRange = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
        // 结束区间
        DateTime start1 = DateUtil.parse("2017-01-31");
        DateTime end1 = DateUtil.parse("2017-02-02");
        DateRange endRange = DateUtil.range(start1, end1, DateField.DAY_OF_YEAR);
        // 交集
        List<DateTime> dateTimes = DateUtil.rangeContains(startRange, endRange);
        assertEquals(1, dateTimes.size());
        assertEquals(DateUtil.parse("2017-01-31"), dateTimes.get(0));
    }

    @Test
    @DisplayName("rangeNotContains 范围差集")
    public void rangeNotContains() {
        // 开始区间
        DateTime start = DateUtil.parse("2017-01-01");
        DateTime end = DateUtil.parse("2017-01-30");
        DateRange startRange = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
        // 结束区间
        DateTime start1 = DateUtil.parse("2017-01-01");
        DateTime end1 = DateUtil.parse("2017-01-31");
        DateRange endRange = DateUtil.range(start1, end1, DateField.DAY_OF_YEAR);
        // 差集
        List<DateTime> dateTimes1 = DateUtil.rangeNotContains(startRange, endRange);

        assertEquals(1, dateTimes1.size());
        assertEquals(DateUtil.parse("2017-01-31"), dateTimes1.get(0));
    }

    @Test
    @DisplayName("reset 重置范围")
    public void resetTest() {
        final Range<Integer> range = new Range<>(1, 3, (current, end, index) -> current >= end ? null : current + 1);
        assertEquals(Integer.valueOf(1), range.next());
        assertEquals(Integer.valueOf(2), range.next());
        assertEquals(Integer.valueOf(3), range.next());
        assertFalse(range.hasNext());

        // reset 后应能重新遍历
        range.reset();
        assertTrue(range.hasNext());
        assertEquals(Integer.valueOf(1), range.next());
    }

    @Test
    @DisplayName("disableLock 禁用锁")
    public void disableLockTest() {
        final Range<Integer> range = new Range<>(1, 3, (current, end, index) -> current >= end ? null : current + 1);
        assertSame(range, range.disableLock());
        // 禁用锁后仍能正常遍历
        assertEquals(Integer.valueOf(1), range.next());
        assertEquals(Integer.valueOf(2), range.next());
        assertEquals(Integer.valueOf(3), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("remove 不支持异常")
    public void removeThrowsTest() {
        final Range<Integer> range = new Range<>(1, 3, (current, end, index) -> current >= end ? null : current + 1);
        assertThrows(UnsupportedOperationException.class, range::remove);
    }

    @Test
    @DisplayName("includeStartFalse 不含起始")
    public void includeStartFalseTest() {
        // 不包含起始元素
        final Range<Integer> range = new Range<>(1, 5, (current, end, index) -> current >= end ? null : current + 1,
                false, true);
        assertEquals(Integer.valueOf(2), range.next());
        assertEquals(Integer.valueOf(3), range.next());
        assertEquals(Integer.valueOf(4), range.next());
        assertEquals(Integer.valueOf(5), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("includeEndFalse 不含结束")
    public void includeEndFalseTest() {
        // 不包含结束元素
        final Range<Integer> range = new Range<>(1, 5, (current, end, index) -> current >= end ? null : current + 1,
                true, false);
        assertEquals(Integer.valueOf(1), range.next());
        assertEquals(Integer.valueOf(2), range.next());
        assertEquals(Integer.valueOf(3), range.next());
        assertEquals(Integer.valueOf(4), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("bothIncludeExcludeFalse 首尾皆不含")
    public void bothIncludeExcludeFalseTest() {
        // 既不包含起始也不包含结束
        final Range<Integer> range = new Range<>(1, 3, (current, end, index) -> current >= end ? null : current + 1,
                false, false);
        assertEquals(Integer.valueOf(2), range.next());
        assertFalse(range.hasNext());
    }

    @Test
    @DisplayName("nullStepperReturnsNull 步进返回 null")
    public void nullStepperReturnsNullTest() {
        // stepper 返回 null 时 hasNext 应为 false
        final Range<Integer> range = new Range<>(1, null, (current, end, index) -> null);
        assertTrue(range.hasNext());
        assertEquals(Integer.valueOf(1), range.next());
        assertFalse(range.hasNext());
    }
}
