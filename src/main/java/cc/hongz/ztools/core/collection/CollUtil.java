package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.bean.BeanUtil;
import cc.hongz.ztools.core.collection.ArrayIter;
import cc.hongz.ztools.core.collection.EnumerationIter;
import cc.hongz.ztools.core.collection.IterUtil;
import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.comparator.CompareUtil;
import cc.hongz.ztools.core.comparator.PinyinComparator;
import cc.hongz.ztools.core.comparator.PropertyComparator;
import cc.hongz.ztools.core.convert.Convert;
import cc.hongz.ztools.core.convert.ConverterRegistry;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Editor;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.lang.Matcher;
import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.lang.hash.Hash32;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 集合相关工具类
 * <p>
 * 此工具方法针对{@link Collection}及其实现类封装的工具。
 *
 * @author xiaoleilu
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class CollUtil {

    /**
     * 默认加载因子。
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 默认初始容量。
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // ---------------------------------------------------------------------- emptyIfNull

    /**
     * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合。
     * 空集合使用{@link Collections#emptySet()}
     *
     * @param <T> 集合元素类型
     * @param set 提供的集合，可能为null
     * @return 原集合，若为null返回空集合
     */
    public static <T> Set<T> emptyIfNull(final Set<T> set) {
        return (null == set) ? Collections.emptySet() : set;
    }

    /**
     * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合。
     * 空集合使用{@link Collections#emptyList()}
     *
     * @param <T>  集合元素类型
     * @param list 提供的集合，可能为null
     * @return 原集合，若为null返回空集合
     */
    public static <T> List<T> emptyIfNull(final List<T> list) {
        return (null == list) ? Collections.emptyList() : list;
    }

    // ---------------------------------------------------------------------- union

    /**
     * 两个集合的并集。
     * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数
     *
     * @param <T>   集合元素类型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 并集的集合，返回{@link ArrayList}
     */
    public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2) {
        if (isEmpty(coll1) && isEmpty(coll2)) {
            return new ArrayList<>();
        }
        if (isEmpty(coll1)) {
            return new ArrayList<>(coll2);
        } else if (isEmpty(coll2)) {
            return new ArrayList<>(coll1);
        }

        final ArrayList<T> list = new ArrayList<>(Math.max(coll1.size(), coll2.size()));
        final Map<T, Integer> map1 = countMap(coll1);
        final Map<T, Integer> map2 = countMap(coll2);
        final Set<T> elts = newHashSet(coll2);
        elts.addAll(coll1);
        int m;
        for (T t : elts) {
            m = Math.max(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0));
            for (int i = 0; i < m; i++) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 多个集合的并集。
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 并集的集合，返回{@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2,
            final Collection<T>... otherColls) {
        Collection<T> union = union(coll1, coll2);
        for (Collection<T> coll : otherColls) {
            if (isEmpty(coll)) {
                continue;
            }
            union = union(union, coll);
        }
        return union;
    }

    /**
     * 多个集合的非重复并集。
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 并集的集合，返回{@link LinkedHashSet}
     */
    @SafeVarargs
    public static <T> Set<T> unionDistinct(final Collection<T> coll1, final Collection<T> coll2,
            final Collection<T>... otherColls) {
        final Set<T> result;
        if (isEmpty(coll1)) {
            result = new LinkedHashSet<>();
        } else {
            result = new LinkedHashSet<>(coll1);
        }
        if (isNotEmpty(coll2)) {
            result.addAll(coll2);
        }
        if (ArrayUtil.isNotEmpty(otherColls)) {
            for (Collection<T> otherColl : otherColls) {
                if (isEmpty(otherColl)) {
                    continue;
                }
                result.addAll(otherColl);
            }
        }
        return result;
    }

    /**
     * 多个集合的完全并集。
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 并集的集合，返回{@link ArrayList}
     */
    @SafeVarargs
    public static <T> List<T> unionAll(final Collection<T> coll1, final Collection<T> coll2,
            final Collection<T>... otherColls) {
        if (isEmpty(coll1) && isEmpty(coll2) && ArrayUtil.isEmpty(otherColls)) {
            return new ArrayList<>(0);
        }
        int totalSize = 0;
        totalSize += size(coll1);
        totalSize += size(coll2);
        if (otherColls != null) {
            for (final Collection<T> otherColl : otherColls) {
                totalSize += size(otherColl);
            }
        }
        final List<T> res = new ArrayList<>(totalSize);
        if (coll1 != null) {
            res.addAll(coll1);
        }
        if (coll2 != null) {
            res.addAll(coll2);
        }
        if (otherColls == null) {
            return res;
        }
        for (final Collection<T> otherColl : otherColls) {
            if (otherColl != null) {
                res.addAll(otherColl);
            }
        }
        return res;
    }

    // ---------------------------------------------------------------------- intersection

    /**
     * 两个集合的交集。
     *
     * @param <T>   集合元素类型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 交集的集合，返回{@link ArrayList}
     */
    public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2) {
        if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
            final ArrayList<T> list = new ArrayList<>(Math.min(coll1.size(), coll2.size()));
            final Map<T, Integer> map1 = countMap(coll1);
            final Map<T, Integer> map2 = countMap(coll2);
            final Set<T> elts = newHashSet(coll2);
            int m;
            for (T t : elts) {
                m = Math.min(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0));
                for (int i = 0; i < m; i++) {
                    list.add(t);
                }
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 多个集合的交集。
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 交集的集合，返回{@link ArrayList}
     */
    @SafeVarargs
    public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2,
            final Collection<T>... otherColls) {
        Collection<T> intersection = intersection(coll1, coll2);
        if (isEmpty(intersection)) {
            return intersection;
        }
        for (Collection<T> coll : otherColls) {
            intersection = intersection(intersection, coll);
            if (isEmpty(intersection)) {
                return intersection;
            }
        }
        return intersection;
    }

    /**
     * 多个集合的交集，只保留不重复元素。
     *
     * @param <T>        集合元素类型
     * @param coll1      集合1
     * @param coll2      集合2
     * @param otherColls 其它集合
     * @return 交集的集合，返回{@link LinkedHashSet}
     */
    @SafeVarargs
    public static <T> Set<T> intersectionDistinct(final Collection<T> coll1, final Collection<T> coll2,
            final Collection<T>... otherColls) {
        final Set<T> result;
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return new LinkedHashSet<>();
        } else {
            result = new LinkedHashSet<>(coll1);
        }
        if (ArrayUtil.isNotEmpty(otherColls)) {
            for (Collection<T> otherColl : otherColls) {
                if (isNotEmpty(otherColl)) {
                    result.retainAll(otherColl);
                } else {
                    return new LinkedHashSet<>();
                }
            }
        }
        result.retainAll(coll2);
        return result;
    }

    // ---------------------------------------------------------------------- disjunction / subtract

    /**
     * 两个集合的对称差集 (A-B)∪(B-A)。
     *
     * @param <T>   集合元素类型
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 差集的集合，返回{@link ArrayList}
     */
    public static <T> Collection<T> disjunction(final Collection<T> coll1, final Collection<T> coll2) {
        if (isEmpty(coll1)) {
            return coll2;
        }
        if (isEmpty(coll2)) {
            return coll1;
        }
        final List<T> result = new ArrayList<>();
        final Map<T, Integer> map1 = countMap(coll1);
        final Map<T, Integer> map2 = countMap(coll2);
        final Set<T> elts = newHashSet(coll2);
        elts.addAll(coll1);
        int m;
        for (T t : elts) {
            m = Math.abs(Convert.toInt(map1.get(t), 0) - Convert.toInt(map2.get(t), 0));
            for (int i = 0; i < m; i++) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 计算集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素。
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <T>   元素类型
     * @return 单差集
     */
    public static <T> Collection<T> subtract(final Collection<T> coll1, final Collection<T> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return coll1;
        }
        Collection<T> result = ObjectUtil.clone(coll1);
        try {
            if (null == result) {
                result = create(coll1.getClass());
                result.addAll(coll1);
            }
            result.removeAll(coll2);
        } catch (UnsupportedOperationException e) {
            result = create(AbstractCollection.class);
            result.addAll(coll1);
            result.removeAll(coll2);
        }
        return result;
    }

    /**
     * 计算集合的单差集，返回List。
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @param <T>   元素类型
     * @return 单差集
     */
    public static <T> List<T> subtractToList(final Collection<T> coll1, final Collection<T> coll2) {
        return subtractToList(coll1, coll2, true);
    }

    /**
     * 计算集合的单差集，返回List。
     *
     * @param coll1    集合1
     * @param coll2    集合2
     * @param isLinked {@code true}返回{@link LinkedList}，{@code false}返回{@link ArrayList}
     * @param <T>      元素类型
     * @return 单差集
     */
    public static <T> List<T> subtractToList(final Collection<T> coll1, final Collection<T> coll2,
            final boolean isLinked) {
        if (isEmpty(coll1)) {
            return ListUtil.empty();
        }
        if (isEmpty(coll2)) {
            return ListUtil.list(isLinked, coll1);
        }
        final List<T> result = isLinked ? new LinkedList<>() : new ArrayList<>(coll1.size());
        Set<T> set = new HashSet<>(coll2);
        for (T t : coll1) {
            if (!set.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    // ---------------------------------------------------------------------- contains

    /**
     * 判断指定集合是否包含指定值。
     *
     * @param collection 集合
     * @param value      需要查找的值
     * @return 是否包含
     */
    public static boolean contains(final Collection<?> collection, final Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    /**
     * 判断指定集合是否包含指定值，安全版本，不抛出类型转换异常。
     *
     * @param collection 集合
     * @param value      需要查找的值
     * @return 是否包含
     */
    public static boolean safeContains(final Collection<?> collection, final Object value) {
        try {
            return contains(collection, value);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    /**
     * 自定义函数判断集合是否包含某类值。
     *
     * @param collection  集合
     * @param containFunc 自定义判断函数
     * @param <T>         值类型
     * @return 是否包含
     */
    public static <T> boolean contains(final Collection<T> collection, final Predicate<? super T> containFunc) {
        if (isEmpty(collection)) {
            return false;
        }
        for (T t : collection) {
            if (containFunc.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素。
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 是否至少有一个共同元素
     */
    public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 集合1中是否包含集合2中所有的元素。
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 是否包含全部元素
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public static boolean containsAll(final Collection<?> coll1, final Collection<?> coll2) {
        if (isEmpty(coll1)) {
            return isEmpty(coll2);
        }
        if (isEmpty(coll2)) {
            return true;
        }
        if (coll1 instanceof Set) {
            return coll1.containsAll(coll2);
        }
        final Iterator<?> it = coll1.iterator();
        final Set<Object> elementsAlreadySeen = new HashSet<>(coll1.size(), 1);
        for (final Object nextElement : coll2) {
            if (elementsAlreadySeen.contains(nextElement)) {
                continue;
            }
            boolean foundCurrentElement = false;
            while (it.hasNext()) {
                final Object p = it.next();
                elementsAlreadySeen.add(p);
                if (Objects.equals(nextElement, p)) {
                    foundCurrentElement = true;
                    break;
                }
            }
            if (!foundCurrentElement) {
                return false;
            }
        }
        return true;
    }

    // ---------------------------------------------------------------------- countMap / size

    /**
     * 根据集合返回一个元素计数的 {@link Map}。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link Map}
     */
    public static <T> Map<T, Integer> countMap(final Iterable<T> collection) {
        return IterUtil.countMap(null == collection ? null : collection.iterator());
    }

    /**
     * 获取Collection或者iterator的大小。
     *
     * @param object 可以为空的对象
     * @return 如果object为空则返回0
     */
    public static int size(final Object object) {
        if (object == null) {
            return 0;
        }
        int total = 0;
        if (object instanceof Map<?, ?>) {
            total = ((Map<?, ?>) object).size();
        } else if (object instanceof Collection<?>) {
            total = ((Collection<?>) object).size();
        } else if (object instanceof Iterable<?>) {
            total = IterUtil.size((Iterable<?>) object);
        } else if (object instanceof Iterator<?>) {
            total = IterUtil.size((Iterator<?>) object);
        } else if (object instanceof Enumeration<?>) {
            final Enumeration<?> it = (Enumeration<?>) object;
            while (it.hasMoreElements()) {
                total++; it.nextElement();
            }
        } else if (ArrayUtil.isArray(object)) {
            total = ArrayUtil.length(object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
        return total;
    }

    // ---------------------------------------------------------------------- join

    /**
     * 以 conjunction 为分隔符将集合转换为字符串。
     *
     * @param <T>         集合元素类型
     * @param iterable {@link Iterable}
     * @param conjunction 分隔符
     * @param func        集合元素转换器
     * @return 连接后的字符串
     */
    public static <T> String join(final Iterable<T> iterable, final CharSequence conjunction, final Function<T,
            ? extends CharSequence> func) {
        if (null == iterable) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction, func);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串。
     *
     * @param <T>         集合元素类型
     * @param iterable {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(final Iterable<T> iterable, final CharSequence conjunction) {
        if (null == iterable) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串。
     *
     * @param <T>         集合元素类型
     * @param iterable {@link Iterable}
     * @param conjunction 分隔符
     * @param prefix      每个元素添加的前缀
     * @param suffix      每个元素添加的后缀
     * @return 连接后的字符串
     */
    public static <T> String join(final Iterable<T> iterable, final CharSequence conjunction, final String prefix,
            final String suffix) {
        if (null == iterable) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction, prefix, suffix);
    }

    // ---------------------------------------------------------------------- popPart / match

    /**
     * 切取部分数据。
     *
     * @param <T>             集合元素类型
     * @param surplusAlaDatas 原数据
     * @param partSize        每部分数据的长度
     * @return 切取出的数据
     */
    public static <T> List<T> popPart(final Stack<T> surplusAlaDatas, final int partSize) {
        if (isEmpty(surplusAlaDatas)) {
            return ListUtil.empty();
        }
        final List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i = 0; i < size; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    /**
     * 切取部分数据。
     *
     * @param <T>             集合元素类型
     * @param surplusAlaDatas 原数据
     * @param partSize        每部分数据的长度
     * @return 切取出的数据
     */
    public static <T> List<T> popPart(final Deque<T> surplusAlaDatas, final int partSize) {
        if (isEmpty(surplusAlaDatas)) {
            return ListUtil.empty();
        }
        final List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i = 0; i < size; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    /**
     * 是否至少有一个符合判断条件。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param predicate  自定义判断函数
     * @return 是否有一个值匹配
     */
    public static <T> boolean anyMatch(final Collection<T> collection, final Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return Boolean.FALSE;
        }
        return collection.stream().anyMatch(predicate);
    }

    /**
     * 是否全部匹配判断条件。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param predicate  自定义判断函数
     * @return 是否全部匹配
     */
    public static <T> boolean allMatch(final Collection<T> collection, final Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return Boolean.FALSE;
        }
        return collection.stream().allMatch(predicate);
    }

    // ---------------------------------------------------------------------- new HashSet

    /**
     * 新建一个HashSet。
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(final T... ts) {
        return set(false, ts);
    }

    /**
     * 新建一个HashSet。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(final Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 新建一个HashSet。
     *
     * @param <T>        集合元素类型
     * @param isSorted   是否有序
     * @param collection 集合
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final Collection<T> collection) {
        return isSorted ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
    }

    /**
     * 新建一个HashSet。
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序
     * @param iter {@link Iterator}
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final Iterator<T> iter) {
        if (null == iter) {
            return set(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    /**
     * 新建一个HashSet。
     *
     * @param <T>         集合元素类型
     * @param isSorted    是否有序
     * @param enumeration {@link Enumeration}
     * @return HashSet对象
     */
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final Enumeration<T> enumeration) {
        if (null == enumeration) {
            return set(isSorted, (T[]) null);
        }
        final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }

    /**
     * 新建一个HashSet。
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
     * @param ts       元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> set(final boolean isSorted, final T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<>() : new HashSet<>();
        }
        int initialCapacity = Math.max((int) (ts.length / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY);
        final HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    // ---------------------------------------------------------------------- List

    /** 新建一个空List。 */
    public static <T> List<T> list(final boolean isLinked) {
        return ListUtil.list(isLinked);
    }

    /** 新建一个List。 */
    @SafeVarargs
    public static <T> List<T> list(final boolean isLinked, final T... values) {
        return ListUtil.list(isLinked, values);
    }

    /** 新建一个List。 */
    public static <T> List<T> list(final boolean isLinked, final Collection<T> collection) {
        return ListUtil.list(isLinked, collection);
    }

    /** 新建一个List。 */
    public static <T> List<T> list(final boolean isLinked, final Iterable<T> iterable) {
        return ListUtil.list(isLinked, iterable);
    }

    /** 新建一个List。 */
    public static <T> List<T> list(final boolean isLinked, final Iterator<T> iter) {
        return ListUtil.list(isLinked, iter);
    }

    /** 新建一个List。 */
    public static <T> List<T> list(final boolean isLinked, final Enumeration<T> enumeration) {
        return ListUtil.list(isLinked, enumeration);
    }

    /** 新建一个ArrayList。 */
    public static <T> ArrayList<T> newArrayList(final Collection<T> collection) {
        return ListUtil.toList(collection);
    }

    /** 新建一个ArrayList。 */
    public static <T> ArrayList<T> newArrayList(final Iterator<T> iterator) {
        return ListUtil.toList(iterator);
    }

    /** 新建一个ArrayList。 */
    public static <T> ArrayList<T> newArrayList(final Enumeration<T> enumeration) {
        return ListUtil.toList(enumeration);
    }

    /** 新建一个CopyOnWriteArrayList。 */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(final Collection<T> collection) {
        return ListUtil.toCopyOnWriteArrayList(collection);
    }

    /** 新建{@link BlockingQueue}。 */
    public static <T> BlockingQueue<T> newBlockingQueue(final int capacity, final boolean isLinked) {
        return isLinked ? new LinkedBlockingDeque<>(capacity) : new ArrayBlockingQueue<>(capacity);
    }

    // ---------------------------------------------------------------------- create

    /**
     * 创建新的集合对象。
     *
     * @param <T>            集合类型
     * @param collectionType 集合类型
     * @return 集合类型对应的实例
     */
    public static <T> Collection<T> create(final Class<?> collectionType) {
        return create(collectionType, null);
    }

    /**
     * 创建新的集合对象。
     *
     * @param <T>            集合元素类型
     * @param collectionType 集合类型
     * @param elementType    集合元素类型
     * @return 集合类型对应的实例
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> create(final Class<?> collectionType, final Class<T> elementType) {
        final Collection<T> list;
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet<>();
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet<>((o1, o2) -> {
                if (o1 instanceof Comparable) {
                    return ((Comparable<T>) o1).compareTo(o2);
                }
                return CompareUtil.compare(o1.toString(), o2.toString());
            });
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            list = (Collection<T>) EnumSet.noneOf(Assert.notNull((Class<Enum>) elementType));
        } else if (collectionType.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<>();
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            list = new LinkedList<>();
        } else {
            try {
                list = (Collection<T>) ReflectUtil.newInstance(collectionType);
            } catch (final Exception e) {
                final Class<?> superclass = collectionType.getSuperclass();
                if (null != superclass && collectionType != superclass) {
                    return create(superclass);
                }
                throw new UtilException(e);
            }
        }
        return list;
    }

    // ---------------------------------------------------------------------- distinct / sub / split

    /**
     * 去重集合。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return {@link ArrayList}
     */
    public static <T> ArrayList<T> distinct(final Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        } else if (collection instanceof Set) {
            return new ArrayList<>(collection);
        } else {
            return new ArrayList<>(new LinkedHashSet<>(collection));
        }
    }

    /**
     * 根据函数生成的KEY去重集合。
     *
     * @param <T>             集合元素类型
     * @param <K>             唯一键类型
     * @param collection      集合
     * @param uniqueGenerator 唯一键生成器
     * @param override        是否覆盖模式
     * @return {@link ArrayList}
     */
    public static <T, K> List<T> distinct(final Collection<T> collection, final Function<T, K> uniqueGenerator,
            final boolean override) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        final UniqueKeySet<K, T> set = new UniqueKeySet<>(true, uniqueGenerator);
        if (override) {
            for (T t : collection) {
                set.add(t);
            }
        } else {
            set.addAllIfAbsent(collection);
        }
        return new ArrayList<>(set);
    }

    /** 截取列表的部分。 */
    public static <T> List<T> sub(final List<T> list, final int start, final int end) {
        return ListUtil.sub(list, start, end);
    }

    /** 截取列表的部分。 */
    public static <T> List<T> sub(final List<T> list, final int start, final int end, final int step) {
        return ListUtil.sub(list, start, end, step);
    }

    /** 截取集合的部分。 */
    public static <T> List<T> sub(final Collection<T> collection, final int start, final int end) {
        return sub(collection, start, end, 1);
    }

    /** 截取集合的部分。 */
    public static <T> List<T> sub(final Collection<T> collection, final int start, final int end, final int step) {
        if (isEmpty(collection)) {
            return ListUtil.empty();
        }
        final List<T> list = collection instanceof List ? (List<T>) collection : ListUtil.toList(collection);
        return sub(list, start, end, step);
    }

    /** 对集合按照指定长度分段。 */
    @Deprecated
    public static <T> List<List<T>> splitList(final List<T> list, final int size) {
        return ListUtil.partition(list, size);
    }

    /** 对集合按照指定长度分段。 */
    public static <T> List<List<T>> split(final Collection<T> collection, final int size) {
        final List<List<T>> result = new ArrayList<>();
        if (isEmpty(collection)) {
            return result;
        }
        final int initSize = Math.min(collection.size(), size);
        List<T> subList = new ArrayList<>(initSize);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(initSize);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

    // ---------------------------------------------------------------------- edit / filter

    /**
     * 编辑，此方法产生一个新集合。
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param editor     编辑器接口
     * @return 过滤后的集合
     */
    public static <T> Collection<T> edit(final Collection<T> collection, final Editor<T> editor) {
        if (null == collection || null == editor) {
            return collection;
        }
        final Collection<T> collection2 = create(collection.getClass());
        if (isEmpty(collection)) {
            return collection2;
        }
        T modified;
        for (T t : collection) {
            modified = editor.edit(t);
            if (null != modified) {
                collection2.add(modified);
            }
        }
        return collection2;
    }

    /** 过滤出新集合。 */
    public static <T> Collection<T> filterNew(final Collection<T> collection, final Filter<T> filter) {
        if (null == collection || null == filter) {
            return collection;
        }
        return edit(collection, t -> filter.accept(t) ? t : null);
    }

    /** 去掉集合中的多个元素。 */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<E>, E> T removeAny(final T collection, final E... elesRemoved) {
        collection.removeAll(newHashSet(elesRemoved));
        return collection;
    }

    /** 去除指定元素，此方法直接修改原集合。 */
    public static <T extends Collection<E>, E> T filter(final T collection, final Filter<E> filter) {
        return IterUtil.filter(collection, filter);
    }

    /** 去除null元素。 */
    public static <T extends Collection<E>, E> T removeNull(final T collection) {
        return filter(collection, Objects::nonNull);
    }

    /** 去除null或""元素。 */
    public static <T extends Collection<E>, E extends CharSequence> T removeEmpty(final T collection) {
        return filter(collection, StrUtil::isNotEmpty);
    }

    /** 去除null或空白字符串元素。 */
    public static <T extends Collection<E>, E extends CharSequence> T removeBlank(final T collection) {
        return filter(collection, StrUtil::isNotBlank);
    }

    /** 移除满足条件的元素到结果集合。 */
    public static <T extends Collection<E>, E> T removeWithAddIf(final T targetCollection, final T resultCollection,
            final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        final Iterator<E> each = targetCollection.iterator();
        while (each.hasNext()) {
            E next = each.next();
            if (predicate.test(next)) {
                resultCollection.add(next);
                each.remove();
            }
        }
        return resultCollection;
    }

    /** 移除满足条件的元素到新集合。 */
    public static <T extends Collection<E>, E> List<E> removeWithAddIf(final T targetCollection,
            final Predicate<? super E> predicate) {
        final List<E> removed = new ArrayList<>();
        removeWithAddIf(targetCollection, removed, predicate);
        return removed;
    }

    // ---------------------------------------------------------------------- map / extract

    /**
     * 通过func自定义一个规则，将原集合中的元素转换成新的元素。
     *
     * @param <T>        集合元素类型
     * @param <R>        返回集合元素类型
     * @param collection 原集合
     * @param func       编辑函数
     * @param ignoreNull 是否忽略空值
     * @return 抽取后的新列表
     */
    public static <T, R> List<R> map(final Iterable<T> collection, final Function<? super T, ? extends R> func,
            final boolean ignoreNull) {
        final List<R> fieldValueList = new ArrayList<>();
        if (null == collection) {
            return fieldValueList;
        }
        R value;
        for (T t : collection) {
            if (null == t && ignoreNull) {
                continue;
            }
            value = func.apply(t);
            if (null == value && ignoreNull) {
                continue;
            }
            fieldValueList.add(value);
        }
        return fieldValueList;
    }

    /** 通过Editor抽取集合元素中的某些值返回为新列表。 */
    public static List<Object> extract(final Iterable<?> collection, final Editor<Object> editor) {
        return extract(collection, editor, false);
    }

    /** 通过Editor抽取集合元素中的某些值返回为新列表。 */
    public static List<Object> extract(final Iterable<?> collection, final Editor<Object> editor,
            final boolean ignoreNull) {
        return map(collection, editor::edit, ignoreNull);
    }

    /** 获取给定Bean列表中指定字段名对应字段值的列表。 */
    public static List<Object> getFieldValues(final Iterable<?> collection, final String fieldName) {
        return getFieldValues(collection, fieldName, false);
    }

    /** 获取给定Bean列表中指定字段名对应字段值的列表。 */
    public static List<Object> getFieldValues(final Iterable<?> collection, final String fieldName,
            final boolean ignoreNull) {
        return map(collection, bean -> {
            if (bean instanceof Map) {
                return ((Map<?, ?>) bean).get(fieldName);
            } else {
                return ReflectUtil.getFieldValue(bean, fieldName);
            }
        }, ignoreNull);
    }

    /** 字段值与列表值对应的Map。 */
    public static <K, V> Map<K, V> fieldValueMap(final Iterable<V> iterable, final String fieldName) {
        return IterUtil.fieldValueMap(IterUtil.getIter(iterable), fieldName);
    }

    /** 两个字段值组成新的Map。 */
    public static <K, V> Map<K, V> fieldValueAsMap(final Iterable<?> iterable, final String fieldNameForKey,
            final String fieldNameForValue) {
        return IterUtil.fieldValueAsMap(IterUtil.getIter(iterable), fieldNameForKey, fieldNameForValue);
    }

    // ---------------------------------------------------------------------- findOne / count / indexOf

    /** 查找第一个匹配元素对象。 */
    public static <T> T findOne(final Iterable<T> collection, final Filter<T> filter) {
        if (null != collection) {
            for (T t : collection) {
                if (filter.accept(t)) {
                    return t;
                }
            }
        }
        return null;
    }

    /** 查找第一个匹配元素对象（按字段值）。 */
    public static <T> T findOneByField(final Iterable<T> collection, final String fieldName, final Object fieldValue) {
        return findOne(collection, t -> {
            if (t instanceof Map) {
                return ObjectUtil.equal(((Map<?, ?>) t).get(fieldName), fieldValue);
            }
            return ObjectUtil.equal(ReflectUtil.getFieldValue(t, fieldName), fieldValue);
        });
    }

    /** 集合中匹配规则的数量。 */
    public static <T> int count(final Iterable<T> iterable, final Matcher<T> matcher) {
        int count = 0;
        if (null != iterable) {
            for (T t : iterable) {
                if (null == matcher || matcher.match(t)) {
                    count++;
                }
            }
        }
        return count;
    }

    /** 获取匹配规则定义中匹配到元素的第一个位置。 */
    public static <T> int indexOf(final Collection<T> collection, final Matcher<T> matcher) {
        if (isNotEmpty(collection)) {
            int index = 0;
            for (T t : collection) {
                if (null == matcher || matcher.match(t)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    /** 获取匹配规则定义中匹配到元素的最后位置。 */
    public static <T> int lastIndexOf(final Collection<T> collection, final Matcher<T> matcher) {
        if (collection instanceof List) {
            return ListUtil.lastIndexOf((List<T>) collection, matcher);
        }
        int matchIndex = -1;
        if (isNotEmpty(collection)) {
            int index = 0;
            for (T t : collection) {
                if (null == matcher || matcher.match(t)) {
                    matchIndex = index;
                }
                index++;
            }
        }
        return matchIndex;
    }

    /** 获取匹配规则定义中匹配到元素的所有位置。 */
    public static <T> int[] indexOfAll(final Collection<T> collection, final Matcher<T> matcher) {
        if (collection instanceof List) {
            return ListUtil.indexOfAll((List<T>) collection, matcher);
        }
        final List<Integer> indexList = new ArrayList<>();
        if (null != collection) {
            int index = 0;
            for (T t : collection) {
                if (null == matcher || matcher.match(t)) {
                    indexList.add(index);
                }
                index++;
            }
        }
        return Convert.convert(int[].class, indexList);
    }

    // ---------------------------------------------------------------------- isEmpty / isNotEmpty

    /** 集合是否为空。 */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /** Iterable是否为空。 */
    public static boolean isEmpty(final Iterable<?> iterable) {
        return IterUtil.isEmpty(iterable);
    }

    /** Iterator是否为空。 */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return IterUtil.isEmpty(iterator);
    }

    /** Enumeration是否为空。 */
    public static boolean isEmpty(final Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }

    /** Map是否为空。 */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /** 集合是否为非空。 */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /** Iterable是否为非空。 */
    public static boolean isNotEmpty(final Iterable<?> iterable) {
        return IterUtil.isNotEmpty(iterable);
    }

    /** Iterator是否为非空。 */
    public static boolean isNotEmpty(final Iterator<?> iterator) {
        return IterUtil.isNotEmpty(iterator);
    }

    /** Enumeration是否为非空。 */
    public static boolean isNotEmpty(final Enumeration<?> enumeration) {
        return null != enumeration && enumeration.hasMoreElements();
    }

    /** Map是否为非空。 */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /** 是否包含null元素。 */
    public static boolean hasNull(final Iterable<?> iterable) {
        return IterUtil.hasNull(iterable);
    }

    // ---------------------------------------------------------------------- defaultIfEmpty

    /** 如果给定集合为空，返回默认集合。 */
    public static <T extends Collection<E>, E> T defaultIfEmpty(final T collection, final T defaultCollection) {
        return isEmpty(collection) ? defaultCollection : collection;
    }

    /** 如果给定集合为空，返回默认集合（懒加载）。 */
    public static <T extends Collection<E>, E> T defaultIfEmpty(final T collection,
            final Supplier<? extends T> supplier) {
        return isEmpty(collection) ? supplier.get() : collection;
    }

    // ---------------------------------------------------------------------- zip / toMap

    /** 映射键值（参考Python的zip()函数）。 */
    public static Map<String, String> zip(final String keys, final String values, final String delimiter,
            final boolean isOrder) {
        return ArrayUtil.zip(StrUtil.splitToArray(keys, delimiter), StrUtil.splitToArray(values, delimiter), isOrder);
    }

    /** 映射键值。 */
    public static Map<String, String> zip(final String keys, final String values, final String delimiter) {
        return zip(keys, values, delimiter, false);
    }

    /** 映射键值。 */
    public static <K, V> Map<K, V> zip(final Collection<K> keys, final Collection<V> values) {
        if (isEmpty(keys) || isEmpty(values)) {
            return MapUtil.empty();
        }
        int entryCount = Math.min(keys.size(), values.size());
        final Map<K, V> map = MapUtil.newHashMap(entryCount);
        final Iterator<K> keyIterator = keys.iterator();
        final Iterator<V> valueIterator = values.iterator();
        while (entryCount > 0) {
            map.put(keyIterator.next(), valueIterator.next());
            entryCount--;
        }
        return map;
    }

    /** 将Entry集合转换为HashMap。 */
    public static <K, V> HashMap<K, V> toMap(final Iterable<Entry<K, V>> entryIter) {
        return IterUtil.toMap(entryIter);
    }

    /** 将数组转换为Map。 */
    public static HashMap<Object, Object> toMap(final Object[] array) {
        return MapUtil.of(array);
    }

    /** 将集合转换为排序后的TreeSet。 */
    public static <T> TreeSet<T> toTreeSet(final Collection<T> collection, final Comparator<T> comparator) {
        final TreeSet<T> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection);
        return treeSet;
    }

    /** Iterator转换为Enumeration。 */
    public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
        return new Enumeration<E>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            @Override
            public E nextElement() {
                return iter.next();
            }
        };
    }

    /** Enumeration转换为Iterator。 */
    public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
        return IterUtil.asIterator(e);
    }

    /** Iterator转为Iterable。 */
    public static <E> Iterable<E> asIterable(final Iterator<E> iter) {
        return IterUtil.asIterable(iter);
    }

    /** Iterable转为Collection。 */
    public static <E> Collection<E> toCollection(final Iterable<E> iterable) {
        return (iterable instanceof Collection) ? (Collection<E>) iterable : newArrayList(iterable.iterator());
    }

    /** 行转列。 */
    public static <K, V> Map<K, List<V>> toListMap(final Iterable<? extends Map<K, V>> mapList) {
        return MapUtil.toListMap(mapList);
    }

    /** 列转行。 */
    public static <K, V> List<Map<K, V>> toMapList(final Map<K, ? extends Iterable<V>> listMap) {
        return MapUtil.toMapList(listMap);
    }

    /** 集合转换为Map。 */
    public static <K, V> Map<K, V> toMap(final Iterable<V> values, final Map<K, V> map, final Func1<V, K> keyFunc) {
        return IterUtil.toMap(null == values ? null : values.iterator(), map, keyFunc);
    }

    /** 集合转换为Map。 */
    public static <K, V, E> Map<K, V> toMap(final Iterable<E> values, final Map<K, V> map, final Func1<E, K> keyFunc,
            final Func1<E, V> valueFunc) {
        return IterUtil.toMap(null == values ? null : values.iterator(), map, keyFunc, valueFunc);
    }

    // ---------------------------------------------------------------------- addIfAbsent / addAll

    /** 一个对象不为空且不存在于该集合中时，加入到该集合中。 */
    public static <T, S extends T> boolean addIfAbsent(final Collection<T> collection, final S object) {
        if (object == null || collection == null || collection.contains(object)) {
            return false;
        }
        return collection.add(object);
    }

    /** 将另一个列表中的元素加入到列表中，如果列表中已经存在此元素则忽略之。 */
    public static <T> List<T> addAllIfNotContains(final List<T> list, final List<T> otherList) {
        for (T t : otherList) {
            if (!list.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    // ---------------------------------------------------------------------- get / getFirst / getLast

    /** 获取集合中指定下标的元素值。 */
    public static <T> T get(final Collection<T> collection, final int index) {
        if (null == collection) {
            return null;
        }
        final int size = collection.size();
        if (0 == size) {
            return null;
        }
        int idx = index;
        if (idx < 0) {
            idx += size;
        }
        if (idx >= size || idx < 0) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(idx);
        } else {
            return IterUtil.get(collection.iterator(), idx);
        }
    }

    /** 获取集合的第一个元素。 */
    public static <T> T getFirst(final Iterable<T> iterable) {
        return IterUtil.getFirst(iterable);
    }

    /** 获取集合的第一个元素。 */
    public static <T> T getFirst(final Iterator<T> iterator) {
        return IterUtil.getFirst(iterator);
    }

    /** 获取集合的最后一个元素。 */
    public static <T> T getLast(final Collection<T> collection) {
        return get(collection, -1);
    }

    /** 获得Iterable对象的元素类型。 */
    @Deprecated
    public static Class<?> getElementType(final Iterable<?> iterable) {
        return IterUtil.getElementType(iterable);
    }

    /** 获得Iterator对象的元素类型。 */
    @Deprecated
    public static Class<?> getElementType(final Iterator<?> iterator) {
        return IterUtil.getElementType(iterator);
    }

    // ---------------------------------------------------------------------- valuesOfKeys

    /** 从Map中获取指定键列表对应的值列表。 */
    @SuppressWarnings("unchecked")
    public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final K... keys) {
        return MapUtil.valuesOfKeys(map, new ArrayIter<>(keys));
    }

    /** 从Map中获取指定键列表对应的值列表。 */
    public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final Iterable<K> keys) {
        return valuesOfKeys(map, keys.iterator());
    }

    /** 从Map中获取指定键列表对应的值列表。 */
    public static <K, V> ArrayList<V> valuesOfKeys(final Map<K, V> map, final Iterator<K> keys) {
        return MapUtil.valuesOfKeys(map, keys);
    }

    // ---------------------------------------------------------------------- sort

    /** 将多个集合排序并显示不同的段落。 */
    @SafeVarargs
    public static <T> List<T> sortPageAll(final int pageNo, final int pageSize, final Comparator<T> comparator,
            final Collection<T>... colls) {
        final List<T> list = new ArrayList<>(pageNo * pageSize);
        for (Collection<T> coll : colls) {
            list.addAll(coll);
        }
        if (null != comparator) {
            list.sort(comparator);
        }
        return page(pageNo, pageSize, list);
    }

    /** 对指定List分页取值。 */
    public static <T> List<T> page(final int pageNo, final int pageSize, final List<T> list) {
        return ListUtil.page(pageNo, pageSize, list);
    }

    /** 排序集合，排序不会修改原集合。 */
    public static <T> List<T> sort(final Collection<T> collection, final Comparator<? super T> comparator) {
        List<T> list = new ArrayList<>(collection);
        list.sort(comparator);
        return list;
    }

    /** 针对List排序。 */
    public static <T> List<T> sort(final List<T> list, final Comparator<? super T> c) {
        return ListUtil.sort(list, c);
    }

    /** 根据Bean的属性排序。 */
    public static <T> List<T> sortByProperty(final Collection<T> collection, final String property) {
        return sort(collection, new PropertyComparator<>(property));
    }

    /** 根据Bean的属性排序。 */
    public static <T> List<T> sortByProperty(final List<T> list, final String property) {
        return ListUtil.sortByProperty(list, property);
    }

    /** 根据汉字的拼音顺序排序。 */
    public static List<String> sortByPinyin(final Collection<String> collection) {
        return sort(collection, new PinyinComparator());
    }

    /** 根据汉字的拼音顺序排序。 */
    public static List<String> sortByPinyin(final List<String> list) {
        return ListUtil.sortByPinyin(list);
    }

    /** 排序Map。 */
    public static <K, V> TreeMap<K, V> sort(final Map<K, V> map, final Comparator<? super K> comparator) {
        final TreeMap<K, V> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    /** 通过Entry排序。 */
    public static <K, V> LinkedHashMap<K, V> sortToMap(final Collection<Map.Entry<K, V>> entryCollection,
            final Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> list = new LinkedList<>(entryCollection);
        list.sort(comparator);
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /** 通过Entry排序。 */
    public static <K, V> LinkedHashMap<K, V> sortByEntry(final Map<K, V> map, final Comparator<Map.Entry<K,
            V>> comparator) {
        return sortToMap(map.entrySet(), comparator);
    }

    /** 将Entry集合排序后返回List。 */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> List<Entry<K, V>> sortEntryToList(final Collection<Entry<K, V>> collection) {
        List<Entry<K, V>> list = new LinkedList<>(collection);
        list.sort((o1, o2) -> {
            V v1 = o1.getValue();
            V v2 = o2.getValue();
            if (v1 instanceof Comparable) {
                return ((Comparable) v1).compareTo(v2);
            } else {
                return v1.toString().compareTo(v2.toString());
            }
        });
        return list;
    }

    // ---------------------------------------------------------------------- forEach

    /** 循环遍历Iterable。 */
    public static <T> void forEach(final Iterable<T> iterable, final Consumer<T> consumer) {
        if (iterable == null) {
            return;
        }
        forEach(iterable.iterator(), consumer);
    }

    /** 循环遍历Iterator。 */
    public static <T> void forEach(final Iterator<T> iterator, final Consumer<T> consumer) {
        if (iterator == null) {
            return;
        }
        int index = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next(), index);
            index++;
        }
    }

    /** 循环遍历Enumeration。 */
    public static <T> void forEach(final Enumeration<T> enumeration, final Consumer<T> consumer) {
        if (enumeration == null) {
            return;
        }
        int index = 0;
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement(), index);
            index++;
        }
    }

    // ---------------------------------------------------------------------- group / reverse

    /** 分组。 */
    public static <T> List<List<T>> group(final Collection<T> collection, final Hash32<T> hash) {
        final List<List<T>> result = new ArrayList<>();
        if (isEmpty(collection)) {
            return result;
        }
        Hash32<T> hashFunc = hash;
        if (null == hashFunc) {
            hashFunc = t -> (null == t) ? 0 : t.hashCode();
        }
        int index;
        List<T> subList;
        for (T t : collection) {
            index = hashFunc.hash32(t);
            if (result.size() - 1 < index) {
                while (result.size() - 1 < index) {
                    result.add(null);
                }
                result.set(index, newArrayList(t));
            } else {
                subList = result.get(index);
                if (null == subList) {
                    result.set(index, newArrayList(t));
                } else {
                    subList.add(t);
                }
            }
        }
        return result;
    }

    /** 反序给定List，会在原List基础上直接修改。 */
    public static <T> List<T> reverse(final List<T> list) {
        return ListUtil.reverse(list);
    }

    /** 反序给定List，会创建一个新的List。 */
    public static <T> List<T> reverseNew(final List<T> list) {
        return ListUtil.reverseNew(list);
    }

    /** 设置或增加元素。 */
    public static <T> List<T> setOrAppend(final List<T> list, final int index, final T element) {
        return ListUtil.setOrAppend(list, index, element);
    }

    // ---------------------------------------------------------------------- keySet / values / max / min

    /** 获取指定Map列表中所有的Key。 */
    public static <K> Set<K> keySet(final Collection<Map<K, ?>> mapCollection) {
        if (isEmpty(mapCollection)) {
            return new HashSet<>();
        }
        final HashSet<K> set = new HashSet<>(mapCollection.size() * 16);
        for (Map<K, ?> map : mapCollection) {
            set.addAll(map.keySet());
        }
        return set;
    }

    /** 获取指定Map列表中所有的Value。 */
    public static <V> List<V> values(final Collection<Map<?, V>> mapCollection) {
        final List<V> values = new ArrayList<>();
        for (Map<?, V> map : mapCollection) {
            values.addAll(map.values());
        }
        return values;
    }

    /** 取最大值。 */
    public static <T extends Comparable<? super T>> T max(final Collection<T> coll) {
        return isEmpty(coll) ? null : Collections.max(coll);
    }

    /** 取最小值。 */
    public static <T extends Comparable<? super T>> T min(final Collection<T> coll) {
        return isEmpty(coll) ? null : Collections.min(coll);
    }

    /** 转为只读集合。 */
    public static <T> Collection<T> unmodifiable(final Collection<? extends T> c) {
        return Collections.unmodifiableCollection(c);
    }

    /** 根据给定的集合类型，返回对应的空集合。 */
    @SuppressWarnings("unchecked")
    public static <E, T extends Collection<E>> T empty(final Class<?> collectionClass) {
        if (null == collectionClass) {
            return (T) Collections.emptyList();
        }
        if (Set.class.isAssignableFrom(collectionClass)) {
            if (NavigableSet.class == collectionClass) {
                return (T) Collections.emptyNavigableSet();
            } else if (SortedSet.class == collectionClass) {
                return (T) Collections.emptySortedSet();
            } else {
                return (T) Collections.emptySet();
            }
        } else if (List.class.isAssignableFrom(collectionClass)) {
            return (T) Collections.emptyList();
        }
        throw new IllegalArgumentException(StrUtil.format("[{}] is not support to get empty!", collectionClass));
    }

    /** 清除一个或多个集合内的元素。 */
    public static void clear(final Collection<?>... collections) {
        for (Collection<?> collection : collections) {
            if (isNotEmpty(collection)) {
                collection.clear();
            }
        }
    }

    /** 填充List，以达到最小长度（左填充）。 */
    public static <T> void padLeft(final List<T> list, final int minLen, final T padObj) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            padRight(list, minLen, padObj); return;
        }
        for (int i = list.size(); i < minLen; i++) {
            list.add(0, padObj);
        }
    }

    /** 填充List，以达到最小长度（右填充）。 */
    public static <T> void padRight(final Collection<T> list, final int minLen, final T padObj) {
        Objects.requireNonNull(list);
        for (int i = list.size(); i < minLen; i++) {
            list.add(padObj);
        }
    }

    /** 使用给定的转换函数，转换源集合为新类型的集合。 */
    public static <F, T> List<T> trans(final Collection<F> collection, final Function<? super F,
            ? extends T> function) {
        if (null == collection) {
            return null;
        }
        final List<T> result = new ArrayList<>(collection.size());
        for (F f : collection) {
            result.add(function.apply(f));
        }
        return result;
    }

    /** 使用给定的map将集合中的元素进行重新设定。 */
    public static <E, K, V> void setValueByMap(final Iterable<E> iterable, final Map<K, V> map, final Function<E,
            K> keyGenerate, final BiConsumer<E, V> biConsumer) {
        iterable.forEach(x -> Optional.ofNullable(map.get(keyGenerate.apply(x))).ifPresent(y -> biConsumer.accept(x,
                y)));
    }

    /** 判断两个Collection是否元素和顺序相同。 */
    public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        return IterUtil.isEqualList(list1, list2);
    }

    // ---------------------------------------------------------------------- existing methods (kept from original)

    /**
     * 将集合中的元素转换后放入新的List.
     */
    public static <T, R> List<R> trans(final Set<T> set, final Function<T, R> function) {
        final List<R> result = new ArrayList<>();
        if (set == null || set.isEmpty()) {
            return result;
        }
        for (final T t : set) {
            result.add(function.apply(t));
        }
        return result;
    }



    /**
     * 数组转为ArrayList.
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList(final T... values) {
        if (values == null) {
            return new ArrayList<>();
        }
        final ArrayList<T> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        return list;
    }

    /**
     * 获取集合中指定多个下标的元素值.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getAny(final Collection<T> collection, final int... indexes) {
        if (null == collection) {
            return new ArrayList<>();
        }
        final int size = collection.size();
        final ArrayList<T> result = new ArrayList<>();
        if (collection instanceof List) {
            final List<T> list = ((List<T>) collection);
            for (int index : indexes) {
                int actualIndex = index;
                if (actualIndex < 0) {
                    actualIndex += size;
                }
                result.add(list.get(actualIndex));
            }
        } else {
            final Object[] array = collection.toArray();
            for (int index : indexes) {
                int actualIndex = index;
                if (actualIndex < 0) {
                    actualIndex += size;
                }
                result.add((T) array[actualIndex]);
            }
        }
        return result;
    }

    /**
     * 加入全部.
     */
    public static <T> Collection<T> addAll(final Collection<T> collection, final Iterator<T> iterator) {
        if (null != collection && null != iterator) {
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        return collection;
    }

    /**
     * 加入全部.
     */
    public static <T> Collection<T> addAll(final Collection<T> collection, final Iterable<T> iterable) {
        if (iterable == null) {
            return collection;
        }
        return addAll(collection, iterable.iterator());
    }

    /**
     * 加入全部.
     */
    public static <T> Collection<T> addAll(final Collection<T> collection, final Enumeration<T> enumeration) {
        if (null != collection && null != enumeration) {
            while (enumeration.hasMoreElements()) {
                collection.add(enumeration.nextElement());
            }
        }
        return collection;
    }

    /**
     * 加入全部.
     */
    public static <T> Collection<T> addAll(final Collection<T> collection, final T[] values) {
        if (null != collection && null != values) {
            Collections.addAll(collection, values);
        }
        return collection;
    }

    /**
     * 新建一个LinkedHashSet.
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(final T... ts) {
        final LinkedHashSet<T> set = new LinkedHashSet<>();
        if (ts != null) {
            Collections.addAll(set, ts);
        }
        return set;
    }

    /**
     * 新建LinkedList.
     */
    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(final T... values) {
        final LinkedList<T> list = new LinkedList<>();
        if (values != null) {
            for (T value : values) {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * 新建一个ArrayList.
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(final T... values) {
        ArrayList<T> list = new ArrayList<>(values.length);
        for (T value : values) {
            list.add(value);
        }
        return list;
    }

    /**
     * 新建一个ArrayList.
     */
    public static <T> ArrayList<T> newArrayList(final Iterable<T> iterable) {
        ArrayList<T> list = new ArrayList<>();
        if (iterable != null) {
            for (T item : iterable) {
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串。 <br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T>         集合元素类型
     * @param iterator    集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @deprecated 请使用IterUtil#join(Iterator, CharSequence)
     */
    @Deprecated
    public static <T> String join(final Iterator<T> iterator, final CharSequence conjunction) {
        return IterUtil.join(iterator, conjunction);
    }

    /**
     * 获取集合中指定字段值列表。
     *
     * @param <T>         元素类型
     * @param collection  Bean集合或Map集合
     * @param fieldName   字段名或map的键
     * @param elementType 元素类型类
     * @return 字段值列表
     * @since 4.5.6
     */
    public static <T> List<T> getFieldValues(final Iterable<?> collection, final String fieldName,
            final Class<T> elementType) {
        final List<Object> fieldValues = getFieldValues(collection, fieldName);
        return Convert.toList(elementType, fieldValues);
    }

    /**
     * 将指定对象全部加入到集合中。 <br>
     * 提供的对象如果为集合类型，会自动转换为目标元素类型
     *
     * @param <T>        元素类型
     * @param collection 被加入的集合
     * @param value      对象，可能为Iterator、Iterable、Enumeration、Array
     * @return 被加入集合
     */
    public static <T> Collection<T> addAll(final Collection<T> collection, final Object value) {
        return addAll(collection, value, TypeUtil.getTypeArgument(collection.getClass()));
    }

    /**
     * 将指定对象全部加入到集合中。 <br>
     * 提供的对象如果为集合类型，会自动转换为目标元素类型。 <br>
     * 如果为String，支持类似于[1,2,3,4] 或者 1,2,3,4 这种格式
     *
     * @param <T>         元素类型
     * @param collection  被加入的集合
     * @param value       对象，可能为Iterator、Iterable、Enumeration、Array，或者与集合元素类型一致
     * @param elementType 元素类型，为空时，使用Object类型来接纳所有类型
     * @return 被加入集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Collection<T> addAll(final Collection<T> collection, final Object value, final Type elementType) {
        if (null == collection || null == value) {
            return collection;
        }
        Type resolvedElementType = elementType;
        if (TypeUtil.isUnknown(resolvedElementType)) {
            // 元素类型为空时，使用Object类型来接纳所有类型
            resolvedElementType = Object.class;
        }

        Iterator iter;
        if (value instanceof Iterator) {
            iter = (Iterator) value;
        } else if (value instanceof Iterable) {
            if (value instanceof Map && BeanUtil.isBean(TypeUtil.getClass(resolvedElementType))) {
                // 如果值为Map，而目标为一个Bean，则Map应整体转换为Bean，而非拆分成Entry转换
                iter = new ArrayIter<>(new Object[]{value});
            } else {
                iter = ((Iterable) value).iterator();
            }
        } else if (value instanceof Enumeration) {
            iter = new EnumerationIter<>((Enumeration) value);
        } else if (ArrayUtil.isArray(value)) {
            iter = new ArrayIter<>(value);
        } else if (value instanceof CharSequence) {
            // String按照逗号分隔的列表对待
            final String arrayStr = StrUtil.unWrap((CharSequence) value, '[', ']');
            iter = StrUtil.splitTrim(arrayStr, CharUtil.COMMA).iterator();
        } else {
            // 其它类型按照单一元素处理
            iter = CollUtil.newArrayList(value).iterator();
        }

        final ConverterRegistry convert = ConverterRegistry.getInstance();
        while (iter.hasNext()) {
            collection.add(convert.convert(resolvedElementType, iter.next()));
        }

        return collection;
    }

    /**
     * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理。 <br>
     * 和JDK8中的map.forEach不同的是，此方法支持index
     *
     * @param <K>        Key类型
     * @param <V>        Value类型
     * @param map        {@link Map}
     * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
     */
    public static <K, V> void forEach(final Map<K, V> map, final KVConsumer<K, V> kvConsumer) {
        if (map == null) {
            return;
        }
        int index = 0;
        for (final Entry<K, V> entry : map.entrySet()) {
            kvConsumer.accept(entry.getKey(), entry.getValue(), index);
            index++;
        }
    }

    /**
     * 根据元素的指定字段名分组，非Bean都放在第一个分组中。
     *
     * @param <T>        元素类型
     * @param collection 集合
     * @param fieldName  元素Bean中的字段名，非Bean都放在第一个分组中
     * @return 分组列表
     */
    public static <T> List<List<T>> groupByField(final Collection<T> collection, final String fieldName) {
        return group(collection, new Hash32<T>() {
            private final List<Object> fieldNameList = new ArrayList<>();

            @Override
            public int hash32(final T t) {
                if (null == t || !BeanUtil.isBean(t.getClass())) {
                    // 非Bean放在同一子分组中
                    return 0;
                }
                final Object value = ReflectUtil.getFieldValue(t, fieldName);
                final int hash = fieldNameList.indexOf(value);
                if (hash < 0) {
                    fieldNameList.add(value);
                    return fieldNameList.size() - 1;
                } else {
                    return hash;
                }
            }
        });
    }

    // ---------------------------------------------------------------------- Consumer interface

    /**
     * 针对一个参数做相应的操作<br>
     * 此函数接口与JDK8中Consumer不同是多提供了index参数，用于标记遍历对象是第几个。
     *
     * @param <T> 处理参数类型
     */
    @FunctionalInterface
    public interface Consumer<T> extends Serializable {
        /**
         * 接受并处理一个参数。
         *
         * @param value 参数值
         * @param index 参数在集合中的索引
         */
        void accept(T value, int index);
    }

    /**
     * 针对两个参数做相应的操作，例如Map中的KEY和VALUE。
     *
     * @param <K> KEY类型
     * @param <V> VALUE类型
     */
    @FunctionalInterface
    public interface KVConsumer<K, V> extends Serializable {
        /**
         * 接受并处理一对参数。
         *
         * @param key   键
         * @param value 值
         * @param index 参数在集合中的索引
         */
        void accept(K key, V value, int index);
    }
}
