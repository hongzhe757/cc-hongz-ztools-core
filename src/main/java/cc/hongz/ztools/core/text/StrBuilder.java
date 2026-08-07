package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 可复用的字符串生成器，非线程安全. <br>
 * TODO 6.x移除此类，java8的StringBuilder非常完善了，无需重写.
 *
 * @author Looly
 * @since 4.0.0
 */
public class StrBuilder implements CharSequence, Appendable, Serializable {
    private static final long serialVersionUID = 6341229705927508451L;

    /**
     * 默认容量.
     */
    public static final int DEFAULT_CAPACITY = 16;

    /**
     * 存放的字符数组.
     */
    private char[] value;
    /**
     * 当前指针位置，或者叫做已经加入的字符数，此位置总在最后一个字符之后.
     */
    private int position;

    /**
     * 创建字符串构建器.
     *
     * @return this
     */
    public static StrBuilder create() {
        return new StrBuilder();
    }

    /**
     * 创建字符串构建器.
     *
     * @param initialCapacity 初始容量
     * @return this
     */
    public static StrBuilder create(final int initialCapacity) {
        return new StrBuilder(initialCapacity);
    }

    /**
     * 创建字符串构建器.
     *
     * @param strs 初始字符串
     * @return this
     * @since 4.0.1
     */
    public static StrBuilder create(final CharSequence... strs) {
        return new StrBuilder(strs);
    }

    // ------------------------------------------------------------------------------------ Constructor start

    /**
     * 构造.
     */
    public StrBuilder() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 构造.
     *
     * @param initialCapacity 初始容量
     */
    public StrBuilder(final int initialCapacity) {
        value = new char[initialCapacity];
    }

    /**
     * 构造.
     *
     * @param strs 初始字符串
     * @since 4.0.1
     */
    public StrBuilder(final CharSequence... strs) {
        this(ArrayUtil.isEmpty(strs) ? DEFAULT_CAPACITY : (totalLength(strs) + DEFAULT_CAPACITY));
        for (CharSequence str : strs) {
            append(str);
        }
    }
    // ------------------------------------------------------------------------------------ Constructor end

    // ------------------------------------------------------------------------------------ Append

    /**
     * 追加对象，对象会被转换为字符串.
     *
     * @param obj 对象
     * @return this
     */
    public StrBuilder append(final Object obj) {
        return insert(this.position, obj);
    }

    /**
     * 追加一个字符.
     *
     * @param c 字符
     * @return this
     */
    @Override
    public StrBuilder append(final char c) {
        return insert(this.position, c);
    }

    /**
     * 追加一个字符数组.
     *
     * @param src 字符数组
     * @return this
     */
    public StrBuilder append(final char[] src) {
        if (ArrayUtil.isEmpty(src)) {
            return this;
        }
        return append(src, 0, src.length);
    }

    /**
     * 追加一个字符数组.
     *
     * @param src    字符数组
     * @param srcPos 开始位置（包括）
     * @param length 长度
     * @return this
     */
    public StrBuilder append(final char[] src, final int srcPos, final int length) {
        return insert(this.position, src, srcPos, length);
    }

    @Override
    public StrBuilder append(final CharSequence csq) {
        return insert(this.position, csq);
    }

    @Override
    public StrBuilder append(final CharSequence csq, final int start, final int end) {
        return insert(this.position, csq, start, end);
    }

    // ------------------------------------------------------------------------------------ Insert

    /**
     * 追加对象，对象会被转换为字符串.
     *
     * @param index 插入位置
     * @param obj   对象
     * @return this
     */
    public StrBuilder insert(final int index, final Object obj) {
        if (obj instanceof CharSequence) {
            return insert(index, (CharSequence) obj);
        }
        return insert(index, obj == null ? null : obj.toString());
    }

    /**
     * 插入指定字符.
     *
     * @param index 位置
     * @param c     字符
     * @return this
     */
    public StrBuilder insert(final int index, final char c) {
        int idx = index;
        if (idx < 0) {
            idx = this.position + idx;
        }
        if ((idx < 0)) {
            throw new StringIndexOutOfBoundsException(idx);
        }

        moveDataAfterIndex(idx, 1);
        value[idx] = c;
        this.position = Math.max(this.position, idx) + 1;
        return this;
    }

    /**
     * 指定位置插入数据. <br>
     * 如果插入位置为当前位置，则定义为追加<br>
     * 如果插入位置大于当前位置，则中间部分补充空格.
     *
     * @param index 插入位置
     * @param src   源数组
     * @return this
     */
    public StrBuilder insert(final int index, final char[] src) {
        if (ArrayUtil.isEmpty(src)) {
            return this;
        }
        return insert(index, src, 0, src.length);
    }

    /**
     * 指定位置插入数据. <br>
     * 如果插入位置为当前位置，则定义为追加<br>
     * 如果插入位置大于当前位置，则中间部分补充空格.
     *
     * @param index  插入位置
     * @param src    源数组
     * @param srcPos 位置
     * @param length 长度
     * @return this
     */
    public StrBuilder insert(final int index, final char[] src, final int srcPos, final int length) {
        if (ArrayUtil.isEmpty(src) || srcPos > src.length || length <= 0) {
            return this;
        }
        int idx = index;
        if (idx < 0) {
            idx = this.position + idx;
        }
        if ((idx < 0)) {
            throw new StringIndexOutOfBoundsException(idx);
        }

        int sPos = srcPos;
        int len = length;
        if (sPos < 0) {
            sPos = 0;
        } else if (sPos + len > src.length) {
            // 长度越界，只截取最大长度
            len = src.length - sPos;
        }

        moveDataAfterIndex(idx, len);
        // 插入数据
        System.arraycopy(src, sPos, value, idx, len);
        this.position = Math.max(this.position, idx) + len;
        return this;
    }

    /**
     * 指定位置插入字符串的某个部分. <br>
     * 如果插入位置为当前位置，则定义为追加<br>
     * 如果插入位置大于当前位置，则中间部分补充空格.
     *
     * @param index 位置
     * @param csq   字符串
     * @return this
     */
    public StrBuilder insert(final int index, final CharSequence csq) {
        int idx = index;
        if (idx < 0) {
            idx = this.position + idx;
        }
        if ((idx < 0)) {
            throw new StringIndexOutOfBoundsException(idx);
        }

        CharSequence localCsq = csq;
        if (null == localCsq) {
            localCsq = StrUtil.EMPTY;
        }
        int len = localCsq.length();
        moveDataAfterIndex(idx, localCsq.length());
        if (localCsq instanceof String) {
            ((String) localCsq).getChars(0, len, this.value, idx);
        } else if (localCsq instanceof StringBuilder) {
            ((StringBuilder) localCsq).getChars(0, len, this.value, idx);
        } else if (localCsq instanceof StringBuffer) {
            ((StringBuffer) localCsq).getChars(0, len, this.value, idx);
        } else if (localCsq instanceof StrBuilder) {
            ((StrBuilder) localCsq).getChars(0, len, this.value, idx);
        } else {
            for (int i = 0, j = idx; i < len; i++, j++) {
                this.value[j] = localCsq.charAt(i);
            }
        }
        this.position = Math.max(this.position, idx) + len;
        return this;
    }

    /**
     * 指定位置插入字符串的某个部分. <br>
     * 如果插入位置为当前位置，则定义为追加<br>
     * 如果插入位置大于当前位置，则中间部分补充空格.
     *
     * @param index 位置
     * @param csq   字符串
     * @param start 字符串开始位置（包括）
     * @param end   字符串结束位置（不包括）
     * @return this
     */
    public StrBuilder insert(final int index, final CharSequence csq, final int start, final int end) {
        CharSequence localCsq = csq;
        if (localCsq == null) {
            localCsq = "null";
        }
        final int csqLen = localCsq.length();
        int s = start;
        int e = end;
        if (s > csqLen) {
            return this;
        }
        if (s < 0) {
            s = 0;
        }
        if (e > csqLen) {
            e = csqLen;
        }
        if (s >= e) {
            return this;
        }
        int idx = index;
        if (idx < 0) {
            idx = this.position + idx;
        }
        if ((idx < 0)) {
            throw new StringIndexOutOfBoundsException(idx);
        }

        final int length = e - s;
        moveDataAfterIndex(idx, length);
        for (int i = s, j = idx; i < e; i++, j++) {
            value[j] = localCsq.charAt(i);
        }
        this.position = Math.max(this.position, idx) + length;
        return this;
    }

    // ------------------------------------------------------------------------------------ Others

    /**
     * 将指定段的字符列表写出到目标字符数组中.
     *
     * @param srcBegin 起始位置（包括）
     * @param srcEnd   结束位置（不包括）
     * @param dst      目标数组
     * @param dstBegin 目标起始位置（包括）
     * @return this
     */
    public StrBuilder getChars(final int srcBegin, final int srcEnd, final char[] dst, final int dstBegin) {
        int sBegin = srcBegin;
        int sEnd = srcEnd;
        if (sBegin < 0) {
            sBegin = 0;
        }
        if (sEnd < 0) {
            sEnd = 0;
        } else if (sEnd > this.position) {
            sEnd = this.position;
        }
        if (sBegin > sEnd) {
            throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
        }
        System.arraycopy(value, sBegin, dst, dstBegin, sEnd - sBegin);
        return this;
    }

    /**
     * 是否有内容.
     *
     * @return 是否有内容
     */
    public boolean hasContent() {
        return position > 0;
    }

    /**
     * 是否为空.
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return position == 0;
    }

    /**
     * 删除全部字符，位置归零.
     *
     * @return this
     */
    public StrBuilder clear() {
        return reset();
    }

    /**
     * 删除全部字符，位置归零.
     *
     * @return this
     */
    public StrBuilder reset() {
        this.position = 0;
        return this;
    }

    /**
     * 删除到指定位置. <br>
     * 如果新位置小于等于0，则删除全部.
     *
     * @param newPosition 新的位置，不包括这个位置
     * @return this
     */
    public StrBuilder delTo(final int newPosition) {
        int np = newPosition;
        if (np < 0) {
            np = 0;
        }
        return del(np, this.position);
    }

    /**
     * 删除指定长度的字符.
     *
     * <pre>
     * 1、end大于等于最大长度，结束按照最大长度计算，相当于删除start之后虽有部分（性能最好）
     * 2、end小于start时，抛出StringIndexOutOfBoundsException
     * 3、start小于0 按照0处理
     * 4、start等于end不处理
     * 5、start和end都位于长度区间内，删除这段内容（内存拷贝）
     * </pre>
     *
     * @param start 开始位置，负数按照0处理（包括）
     * @param end   结束位置，超出最大长度按照最大长度处理（不包括）
     * @return this
     * @throws StringIndexOutOfBoundsException 当start &gt; end抛出此异常
     */
    public StrBuilder del(final int start, final int end) throws StringIndexOutOfBoundsException {
        int s = start;
        if (s < 0) {
            s = 0;
        }

        int e = end;
        if (e >= this.position) {
            // end在边界及以外，相当于删除后半部分
            this.position = s;
            return this;
        } else if (e < 0) {
            // start和end都为0的情况下表示删除全部
            e = 0;
        }

        int len = e - s;
        // 截取中间部分，需要将后半部分复制到删除的开始位置
        if (len > 0) {
            System.arraycopy(value, s + len, value, s, this.position - e);
            this.position -= len;
        } else if (len < 0) {
            throw new StringIndexOutOfBoundsException("Start is greater than End.");
        }
        return this;
    }

    /**
     * 生成字符串.
     *
     * @param isReset 是否重置，重置后相当于空的构建器
     * @return 生成的字符串
     */
    public String toString(final boolean isReset) {
        if (position > 0) {
            final String s = new String(value, 0, position);
            if (isReset) {
                reset();
            }
            return s;
        }
        return StrUtil.EMPTY;
    }

    /**
     * 重置并返回生成的字符串.
     *
     * @return 字符串
     */
    public String toStringAndReset() {
        return toString(true);
    }

    /**
     * 生成字符串.
     */
    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public int length() {
        return this.position;
    }

    @Override
    public char charAt(final int index) {
        int idx = index;
        if (idx < 0) {
            idx = this.position + idx;
        }
        if ((idx < 0) || (idx >= this.position)) {
            throw new StringIndexOutOfBoundsException(idx);
        }
        return this.value[idx];
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return subString(start, end);
    }

    /**
     * 返回自定段的字符串.
     *
     * @param start 开始位置（包括）
     * @return this
     */
    public String subString(final int start) {
        return subString(start, this.position);
    }

    /**
     * 返回自定段的字符串.
     *
     * @param start 开始位置（包括）
     * @param end   结束位置（不包括）
     * @return this
     */
    public String subString(final int start, final int end) {
        return new String(this.value, start, end - start);
    }

    // ------------------------------------------------------------------------------------ Private method start

    /**
     * 指定位置之后的数据后移指定长度.
     *
     * @param index  位置
     * @param length 位移长度
     */
    private void moveDataAfterIndex(final int index, final int length) {
        ensureCapacity(Math.max(this.position, index) + length);
        if (index < this.position) {
            // 插入位置在已有数据范围内，后移插入位置之后的数据
            System.arraycopy(this.value, index, this.value, index + length, this.position - index);
        } else if (index > this.position) {
            // 插入位置超出范围，则当前位置到index清除为空格
            Arrays.fill(this.value, this.position, index, StrUtil.C_SPACE);
        }
        // 不位移
    }

    /**
     * 确认容量是否够用，不够用则扩展容量.
     *
     * @param minimumCapacity 最小容量
     */
    private void ensureCapacity(final int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0) {
            expandCapacity(minimumCapacity);
        }
    }

    /**
     * 扩展容量. <br>
     * 首先对容量进行二倍扩展，如果小于最小容量，则扩展为最小容量.
     *
     * @param minimumCapacity 需要扩展的最小容量
     */
    private void expandCapacity(final int minimumCapacity) {
        int newCapacity = (value.length << 1) + 2;
        // overflow-conscious code
        if (newCapacity - minimumCapacity < 0) {
            newCapacity = minimumCapacity;
        }
        if (newCapacity < 0) {
            throw new OutOfMemoryError("Capacity is too long and max than Integer.MAX");
        }
        value = Arrays.copyOf(value, newCapacity);
    }

    /**
     * 给定字符串数组的总长度. <br>
     * null字符长度定义为0.
     *
     * @param strs 字符串数组
     * @return 总长度
     * @since 4.0.1
     */
    private static int totalLength(final CharSequence... strs) {
        int totalLength = 0;
        for (CharSequence str : strs) {
            totalLength += (null == str ? 0 : str.length());
        }
        return totalLength;
    }
    // ------------------------------------------------------------------------------------ Private method end
}
