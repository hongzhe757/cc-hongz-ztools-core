package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.comparator.PinyinComparator;
import cc.hongz.ztools.core.comparator.PropertyComparator;
import cc.hongz.ztools.core.exceptions.ValidateException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Matcher;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.PageUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * List相关工具类.
 *
 * @author looly
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class ListUtil {

    /**
     * 新建一个空List.
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @return List对象
     */
    public static <T> List<T> list(final boolean isLinked) {
        return isLinked ? new LinkedList<>() : new ArrayList<>();
    }

    /**
     * 新建一个List.
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param values   数组
     * @return List对象
     */
    @SafeVarargs
    public static <T> List<T> list(final boolean isLinked, final T... values) {
        if (ArrayUtil.isEmpty(values)) {
            return list(isLinked);
        }
        final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
        Collections.addAll(arrayList, values);
        return arrayList;
    }

    /**
     * 新建一个List.
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param collection 集合
     * @return List对象
     */
    public static <T> List<T> list(final boolean isLinked, final Collection<T> collection) {
        if (null == collection) {
            return list(isLinked);
        }
        return isLinked ? new LinkedList<>(collection) : new ArrayList<>(collection);
    }

    /**
     * 新建一个List.
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iterable {@link Iterable}
     * @return List对象
     */
    public static <T> List<T> list(final boolean isLinked, final Iterable<T> iterable) {
        if (null == iterable) {
            return list(isLinked);
        }
        return list(isLinked, iterable.iterator());
    }

    /**
     * 新建一个List.
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param iter     {@link Iterator}
     * @return ArrayList对象
     */
    public static <T> List<T> list(final boolean isLinked, final Iterator<T> iter) {
        final List<T> list = list(isLinked);
        if (null != iter) {
            while (iter.hasNext()) {
                list.add(iter.next());
            }
        }
        return list;
    }

    /**
     * 新建一个List.
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param enumration {@link Enumeration}
     * @return ArrayList对象
     */
    public static <T> List<T> list(final boolean isLinked, final Enumeration<T> enumration) {
        final List<T> list = list(isLinked);
        if (null != enumration) {
            while (enumration.hasMoreElements()) {
                list.add(enumration.nextElement());
            }
        }
        return list;
    }

    /**
     * 新建一个ArrayList.
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(final T... values) {
        return (ArrayList<T>) list(false, values);
    }

    /**
     * 新建LinkedList.
     *
     * @param values 数组
     * @param <T>    类型
     * @return LinkedList
     */
    @SafeVarargs
    public static <T> LinkedList<T> toLinkedList(final T... values) {
        return (LinkedList<T>) list(true, values);
    }

    /**
     * 数组转为一个不可变List.
     *
     * @param ts  对象
     * @param <T> 对象类型
     * @return 不可修改List
     */
    @SafeVarargs
    public static <T> List<T> of(final T... ts) {
        if (ArrayUtil.isEmpty(ts)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(toList(ts));
    }

    /**
     * 新建一个CopyOnWriteArrayList.
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link CopyOnWriteArrayList}
     */
    public static <T> CopyOnWriteArrayList<T> toCopyOnWriteArrayList(final Collection<T> collection) {
        return (null == collection) ? (new CopyOnWriteArrayList<>()) : (new CopyOnWriteArrayList<>(collection));
    }

    /**
     * 新建一个ArrayList.
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(final Collection<T> collection) {
        return (ArrayList<T>) list(false, collection);
    }

    /**
     * 新建一个ArrayList.
     *
     * @param <T>      集合元素类型
     * @param iterable {@link Iterable}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(final Iterable<T> iterable) {
        return (ArrayList<T>) list(false, iterable);
    }

    /**
     * 新建一个ArrayList.
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(final Iterator<T> iterator) {
        return (ArrayList<T>) list(false, iterator);
    }

    /**
     * 新建一个ArrayList.
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList(final Enumeration<T> enumeration) {
        return (ArrayList<T>) list(false, enumeration);
    }

    /**
     * 对指定List分页取值.
     *
     * @param <T>      集合元素类型
     * @param pageNo   页码
     * @param pageSize 每页的条目数
     * @param list     列表
     * @return 分页后的段落内容
     */
    public static <T> List<T> page(final int pageNo, final int pageSize, final List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>(0);
        }

        int resultSize = list.size();
        if (resultSize <= pageSize) {
            if (pageNo < 1) {
                return unmodifiable(list);
            } else {
                return new ArrayList<>(0);
            }
        }
        if (((long) (pageNo - 1) * pageSize) > resultSize) {
            return new ArrayList<>(0);
        }

        final int start = (pageNo - 1) * pageSize;
        int end = start + pageSize;
        if (end > resultSize) {
            end = resultSize;
        }
        if (start > end) {
            return new ArrayList<>(0);
        }

        return sub(list, start, end);
    }

    /**
     * 对指定List进行分页，逐页返回数据.
     *
     * @param <T>              集合元素类型
     * @param list             源数据列表
     * @param pageSize         每页的条目数
     * @param pageListConsumer 单页数据函数式返回
     */
    public static <T> void page(final List<T> list, final int pageSize, final Consumer<List<T>> pageListConsumer) {
        if (list == null || list.isEmpty() || pageSize <= 0) {
            return;
        }

        final int total = list.size();
        final int totalPage = PageUtil.totalPage(total, pageSize);
        for (int pageNo = PageUtil.getFirstPageNo(); pageNo < totalPage + PageUtil.getFirstPageNo(); pageNo++) {
            final int[] startEnd = PageUtil.transToStartEnd(pageNo, pageSize);
            if (startEnd[1] > total) {
                startEnd[1] = total;
            }
            pageListConsumer.accept(sub(list, startEnd[0], startEnd[1]));
        }
    }

    /**
     * 针对List排序，排序会修改原List.
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     */
    public static <T> List<T> sort(final List<T> list, final Comparator<? super T> c) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        list.sort(c);
        return list;
    }

    /**
     * 根据Bean的属性排序.
     *
     * @param <T>      元素类型
     * @param list     List
     * @param property 属性名
     * @return 排序后的List
     */
    public static <T> List<T> sortByProperty(final List<T> list, final String property) {
        return sort(list, new PropertyComparator<>(property));
    }

    /**
     * 根据汉字的拼音顺序排序.
     *
     * @param list List
     * @return 排序后的List
     */
    public static List<String> sortByPinyin(final List<String> list) {
        return sort(list, new PinyinComparator());
    }

    /**
     * 反序给定List，会在原List基础上直接修改.
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     */
    public static <T> List<T> reverse(final List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 反序给定List，会创建一个新的List，原List数据不变.
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     */
    public static <T> List<T> reverseNew(final List<T> list) {
        if (list == null) {
            return null;
        }
        List<T> list2 = ObjectUtil.clone(list);
        if (null == list2) {
            list2 = new ArrayList<>(list);
        }
        try {
            return reverse(list2);
        } catch (final UnsupportedOperationException e) {
            return reverse(list(false, list));
        }
    }

    /**
     * 设置或增加元素。当index小于List的长度时，替换指定位置的值，否则在尾部追加.
     *
     * @param <T>     元素类型
     * @param list    List列表
     * @param index   位置
     * @param element 新元素
     * @return 原List
     */
    public static <T> List<T> setOrAppend(final List<T> list, final int index, final T element) {
        Assert.notNull(list, "List must be not null !");
        if (index < list.size()) {
            list.set(index, element);
        } else {
            list.add(element);
        }
        return list;
    }

    /**
     * 在指定位置设置元素.
     *
     * @param <T>     元素类型
     * @param list    List列表
     * @param index   位置
     * @param element 新元素
     * @return 原List
     */
    public static <T> List<T> setOrPadding(final List<T> list, final int index, final T element) {
        return setOrPadding(list, index, element, null);
    }

    /**
     * 在指定位置设置元素.
     *
     * @param <T>            元素类型
     * @param list           List列表
     * @param index          位置
     * @param element        新元素
     * @param paddingElement 填充的值
     * @return 原List
     */
    public static <T> List<T> setOrPadding(final List<T> list, final int index, final T element,
            final T paddingElement) {
        return setOrPadding(list, index, element, paddingElement, (list.size() + 1) * 10);
    }

    /**
     * 在指定位置设置元素.
     *
     * @param <T>            元素类型
     * @param list           List列表
     * @param index          位置
     * @param element        新元素
     * @param paddingElement 填充的值
     * @param indexLimit     最大索引限制
     * @return 原List
     */
    public static <T> List<T> setOrPadding(final List<T> list, final int index, final T element, final T paddingElement,
            final int indexLimit) {
        Assert.notNull(list, "List must be not null !");
        final int size = list.size();
        if (index < size) {
            list.set(index, element);
        } else {
            if (indexLimit > 0) {
                if (index > indexLimit) {
                    throw new ValidateException("Index [{}] is too large for limit: [{}]", index, indexLimit);
                }
            }
            for (int i = size; i < index; i++) {
                list.add(paddingElement);
            }
            list.add(element);
        }
        return list;
    }

    /**
     * 截取集合的部分.
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组
     */
    public static <T> List<T> sub(final List<T> list, final int start, final int end) {
        return sub(list, start, end, 1);
    }

    /**
     * 截取集合的部分.
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     * @return 截取后的数组
     */
    public static <T> List<T> sub(final List<T> list, final int start, final int end, final int step) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new ArrayList<>(0);
        }

        final int size = list.size();
        int startIndex = start;
        int endIndex = end;
        int stepSize = step;
        if (startIndex < 0) {
            startIndex += size;
        }
        if (endIndex < 0) {
            endIndex += size;
        }
        if (startIndex == size) {
            return new ArrayList<>(0);
        }
        if (startIndex > endIndex) {
            int tmp = startIndex;
            startIndex = endIndex;
            endIndex = tmp;
        }
        if (endIndex > size) {
            if (startIndex >= size) {
                return new ArrayList<>(0);
            }
            endIndex = size;
        }

        if (stepSize < 1) {
            stepSize = 1;
        }

        final List<T> result = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i += stepSize) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * 获取匹配规则定义中匹配到元素的最后位置.
     *
     * @param <T>     元素类型
     * @param list    List集合
     * @param matcher 匹配器，为空则全部匹配
     * @return 最后一个位置
     */
    public static <T> int lastIndexOf(final List<T> list, final Matcher<T> matcher) {
        if (null != list) {
            final int size = list.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    if (null == matcher || matcher.match(list.get(i))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 获取匹配规则定义中匹配到元素的所有位置.
     *
     * @param <T>     元素类型
     * @param list    列表
     * @param matcher 匹配器，为空则全部匹配
     * @return 位置数组
     */
    public static <T> int[] indexOfAll(final List<T> list, final Matcher<T> matcher) {
        if (list == null || list.isEmpty()) {
            return new int[0];
        }
        final List<Integer> positions = new ArrayList<>();
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            if (null == matcher || matcher.match(list.get(i))) {
                positions.add(i);
            }
        }
        final int[] result = new int[positions.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = positions.get(i);
        }
        return result;
    }

    /**
     * 将对应List转换为不可修改的List.
     *
     * @param list List
     * @param <T>  元素类型
     * @return 不可修改List
     */
    public static <T> List<T> unmodifiable(final List<T> list) {
        if (null == list) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 获取一个空List，这个空List不可变.
     *
     * @param <T> 元素类型
     * @return 空的List
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }

    /**
     * 通过传入分区长度，将指定列表分区为不同的块.
     *
     * @param <T>  集合元素类型
     * @param list 列表
     * @param size 每个段的长度
     * @return 分段列表
     */
    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        if (list == null || list.isEmpty()) {
            return empty();
        }
        return (list instanceof RandomAccess)
                ? new RandomAccessPartition<>(list, size)
                : new Partition<>(list, size);
    }

    /**
     * 对集合按照指定长度分段.
     *
     * @param <T>  集合元素类型
     * @param list 列表
     * @param size 每个段的长度
     * @return 分段列表
     */
    public static <T> List<List<T>> split(final List<T> list, final int size) {
        return partition(list, size);
    }

    /**
     * 将集合平均分成多个list.
     *
     * @param <T>   集合元素类型
     * @param list  集合
     * @param limit 要均分成几个list
     * @return 分段列表
     */
    public static <T> List<List<T>> splitAvg(final List<T> list, final int limit) {
        if (list == null || list.isEmpty()) {
            return empty();
        }
        return (list instanceof RandomAccess)
                ? new RandomAccessAvgPartition<>(list, limit)
                : new AvgPartition<>(list, limit);
    }

    /**
     * 将指定元素交换到指定索引位置.
     *
     * @param <T>         元素类型
     * @param list        列表
     * @param element     需交换元素
     * @param targetIndex 目标索引
     */
    public static <T> void swapTo(final List<T> list, final T element, final Integer targetIndex) {
        if (list != null && !list.isEmpty()) {
            final int index = list.indexOf(element);
            if (index >= 0) {
                Collections.swap(list, index, targetIndex);
            }
        }
    }

    /**
     * 将指定元素交换到指定元素位置.
     *
     * @param <T>           元素类型
     * @param list          列表
     * @param element       需交换元素
     * @param targetElement 目标元素
     */
    public static <T> void swapElement(final List<T> list, final T element, final T targetElement) {
        if (list != null && !list.isEmpty()) {
            final int targetIndex = list.indexOf(targetElement);
            if (targetIndex >= 0) {
                swapTo(list, element, targetIndex);
            }
        }
    }

    /**
     * 将元素移动到指定列表的新位置.
     *
     * @param list        原始列表
     * @param element     需要移动的元素
     * @param newPosition 元素的新位置
     * @param <T>         列表和元素的通用类型
     * @return 更新后的列表
     */
    public static <T> List<T> move(final List<T> list, final T element, final int newPosition) {
        Assert.notNull(list);
        if (!list.contains(element)) {
            list.add(newPosition, element);
        } else {
            list.remove(element);
            list.add(newPosition, element);
        }
        return list;
    }

    /**
     * 将两个列表的元素按照索引一一配对.
     *
     * @param <A>    第一个列表的元素类型
     * @param <B>    第二个列表的元素类型
     * @param <R>    结果列表的元素类型
     * @param listA  第一个列表
     * @param listB  第二个列表
     * @param zipper 合并函数
     * @return 合并后的新列表
     */
    public static <A, B, R> List<R> zip(final List<A> listA, final List<B> listB, final BiFunction<A, B, R> zipper) {
        if ((listA == null || listA.isEmpty()) || (listB == null || listB.isEmpty())) {
            return new ArrayList<>();
        }
        Assert.notNull(zipper, "Zipper function must not be null");

        final int size = Math.min(listA.size(), listB.size());
        final List<R> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(zipper.apply(listA.get(i), listB.get(i)));
        }
        return result;
    }
}
