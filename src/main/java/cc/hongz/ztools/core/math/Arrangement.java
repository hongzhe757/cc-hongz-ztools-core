package cc.hongz.ztools.core.math;

import cc.hongz.ztools.core.util.ArrayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 排列A(n, m).<br>
 * 排列组合相关类，参考：http://cgs1999.iteye.com/blog/2327664
 *
 * @author looly
 * @since 4.0.7
 */
public class Arrangement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用于排列的数据.
     */
    private final String[] datas;

    /**
     * 构造.
     *
     * @param datas 用于排列的数据
     */
    public Arrangement(final String[] datas) {
        this.datas = datas;
    }

    /**
     * 计算排列数，即A(n, n) = n!.
     *
     * @param n 总数
     * @return 排列数
     */
    public static long count(final int n) {
        return count(n, n);
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!.
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 排列数
     */
    public static long count(final int n, final int m) {
        if (m < 0 || m > n) {
            throw new IllegalArgumentException(
                    "n >= 0 && m >= 0 && m <= n required");
        }
        if (m == 0) {
            return 1;
        }
        long result = 1;
        for (int i = 0; i < m; i++) {
            final long next = result * (n - i);
            if (next < result) {
                throw new ArithmeticException(
                        "Overflow computing A(" + n + "," + m + ")");
            }
            result = next;
        }
        return result;
    }

    /**
     * 计算排列总数，即A(n, 1) + A(n, 2) + A(n, 3)...
     *
     * @param n 总数
     * @return 排列数
     */
    public static long countAll(final int n) {
        long total = 0;
        for (int i = 1; i <= n; i++) {
            total += count(n, i);
        }
        return total;
    }

    /**
     * 全排列选择（列表全部参与排列）.
     *
     * @return 所有排列列表
     */
    public List<String[]> select() {
        return select(this.datas.length);
    }

    /**
     * 从当前数据中选择 m 个元素，生成所有不重复的排列.
     *
     * @param m 选择的元素个数
     * @return 所有长度为 m 的不重复排列列表
     */
    public List<String[]> select(final int m) {
        if (m < 0 || m > datas.length) {
            return Collections.emptyList();
        }
        if (m == 0) {
            return Collections.singletonList(new String[0]);
        }

        final long estimated = count(datas.length, m);
        final int capacity = estimated > Integer.MAX_VALUE
                ? Integer.MAX_VALUE : (int) estimated;

        final List<String[]> result = new ArrayList<>(capacity);
        final boolean[] visited = new boolean[datas.length];
        dfs(new String[m], 0, visited, result);
        return result;
    }

    /**
     * 生成当前数据的全部不重复排列（长度为 1 至 n 的所有排列）.
     *
     * @return 所有不重复排列列表
     */
    public List<String[]> selectAll() {
        final List<String[]> result = new ArrayList<>();
        for (int m = 1; m <= datas.length; m++) {
            result.addAll(select(m));
        }
        return result;
    }

    /**
     * 排列选择.<br>
     * 排列方式为先从数据数组中取出一个元素，
     * 再把剩余的元素作为新的基数，依次类推，直到选择到足够的元素.
     *
     * @param datas      选择的基数
     * @param resultList 前面（resultIndex-1）个的排列结果
     * @param resultIndex 选择索引，从0开始
     * @param result     最终结果
     */
    private void select(final String[] datas, final String[] resultList,
            final int resultIndex, final List<String[]> result) {
        if (resultIndex >= resultList.length) {
            if (false == result.contains(resultList)) {
                result.add(Arrays.copyOf(resultList, resultList.length));
            }
            return;
        }

        for (int i = 0; i < datas.length; i++) {
            resultList[resultIndex] = datas[i];
            select(ArrayUtil.remove(datas, i), resultList,
                    resultIndex + 1, result);
        }
    }

    /**
     * 返回一个排列的迭代器.
     *
     * @param m 选择的元素个数
     * @return 排列迭代器
     */
    public Iterable<String[]> iterate(final int m) {
        return () -> new ArrangementIterator(datas, m);
    }

    /**
     * 排列迭代器.
     *
     * @author looly
     */
    private static class ArrangementIterator implements Iterator<String[]> {

        /**
         * 排列数据.
         */
        private final String[] datas;
        /**
         * 总数.
         */
        private final int n;
        /**
         * 选择个数.
         */
        private final int m;
        /**
         * 访问标记.
         */
        private final boolean[] visited;
        /**
         * 缓冲区.
         */
        private final String[] buffer;

        /**
         * 每一层记录当前尝试的下标，-1表示还未尝试.
         */
        private final int[] indices;
        /**
         * 当前深度.
         */
        private int depth;
        /**
         * 是否结束.
         */
        private boolean end;

        /**
         * 预取的下一个元素.
         */
        private String[] nextItem;
        /**
         * 是否已预取.
         */
        private boolean nextPrepared;

        /**
         * 构造.
         *
         * @param datas 排列数据
         * @param m     选择个数
         */
        ArrangementIterator(final String[] datas, final int m) {
            this.datas = datas;
            this.n = datas.length;
            this.m = m;
            this.visited = new boolean[n];
            this.nextItem = null;
            this.nextPrepared = false;

            if (m < 0 || m > n) {
                final int size = Math.max(1, m);
                this.indices = new int[size];
                this.buffer = new String[size];
                this.depth = -1;
                this.end = true;
            } else if (m == 0) {
                this.indices = new int[0];
                this.buffer = new String[0];
                this.depth = 0;
                this.end = false;
            } else {
                this.indices = new int[m];
                Arrays.fill(this.indices, -1);
                this.buffer = new String[m];
                this.depth = 0;
                this.end = false;
            }
        }

        @Override
        public boolean hasNext() {
            if (end) {
                return false;
            }
            if (nextPrepared) {
                return nextItem != null;
            }
            prepareNext();
            return nextItem != null;
        }

        @Override
        public String[] next() {
            if (end && !nextPrepared) {
                throw new NoSuchElementException();
            }
            if (!nextPrepared) {
                prepareNext();
            }
            if (nextItem == null) {
                throw new NoSuchElementException();
            }
            final String[] ret = nextItem;
            nextItem = null;
            nextPrepared = false;
            if (m == 0) {
                end = true;
            }
            return ret;
        }

        /**
         * 将状态推进到下一个可返回的排列.
         */
        private void prepareNext() {
            if (nextPrepared || end) {
                nextPrepared = true;
                return;
            }

            if (m == 0) {
                nextItem = new String[0];
                nextPrepared = true;
                return;
            }

            while (depth >= 0) {
                final int start = indices[depth] + 1;
                boolean found = false;
                for (int i = start; i < n; i++) {
                    if (!visited[i]) {
                        if (indices[depth] != -1) {
                            visited[indices[depth]] = false;
                        }
                        indices[depth] = i;
                        visited[i] = true;
                        buffer[depth] = datas[i];
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    if (indices[depth] != -1) {
                        visited[indices[depth]] = false;
                        indices[depth] = -1;
                    }
                    depth--;
                    continue;
                }

                if (depth == m - 1) {
                    nextItem = Arrays.copyOf(buffer, m);
                    visited[indices[depth]] = false;
                    nextPrepared = true;
                    return;
                } else {
                    depth++;
                    if (depth < m) {
                        indices[depth] = -1;
                    }
                }
            }

            end = true;
            nextItem = null;
            nextPrepared = true;
        }
    }

    /**
     * 核心递归方法（回溯算法）.
     *
     * @param current 当前构建的排列数组
     * @param depth   当前递归深度
     * @param visited 标记数组
     * @param result  结果集
     */
    private void dfs(final String[] current, final int depth,
            final boolean[] visited, final List<String[]> result) {
        if (depth == current.length) {
            result.add(Arrays.copyOf(current, current.length));
            return;
        }

        for (int i = 0; i < datas.length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                current[depth] = datas[i];
                dfs(current, depth + 1, visited, result);
                visited[i] = false;
            }
        }
    }
}
