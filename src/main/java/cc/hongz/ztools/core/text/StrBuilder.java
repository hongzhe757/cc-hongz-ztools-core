package cc.hongz.ztools.core.text;

import java.io.Serializable;

/**
 * 可复用的字符串生成器，非线程安全.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.text.StrBuilder}（精简版），
 * 实现 {@link CharSequence} 和 {@link Appendable}，供 replacer/escape 体系使用.
 *
 * @author looly
 * @author hongzhe.qin (migrated)
 */
public class StrBuilder implements CharSequence, Appendable, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 默认容量.
     */
    public static final int DEFAULT_CAPACITY = 16;

    /**
     * 内部字符数组.
     */
    private char[] value;

    /**
     * 当前写入位置.
     */
    private int position;

    /**
     * 创建字符串构建器.
     *
     * @return StrBuilder
     */
    public static StrBuilder create() {
        return new StrBuilder();
    }

    /**
     * 创建字符串构建器.
     *
     * @param initialCapacity 初始容量
     * @return StrBuilder
     */
    public static StrBuilder create(int initialCapacity) {
        return new StrBuilder(initialCapacity);
    }

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
    public StrBuilder(int initialCapacity) {
        value = new char[initialCapacity];
    }

    /**
     * 追加对象.
     *
     * @param obj 对象
     * @return this
     */
    public StrBuilder append(Object obj) {
        if (obj == null) {
            return this;
        }
        if (obj instanceof CharSequence) {
            return append((CharSequence) obj);
        }
        return append(obj.toString());
    }

    /**
     * 追加字符.
     *
     * @param c 字符
     * @return this
     */
    public StrBuilder append(char c) {
        ensureCapacity(position + 1);
        value[position++] = c;
        return this;
    }

    /**
     * 追加字符序列.
     *
     * @param csq 字符序列
     * @return this
     */
    @Override
    public StrBuilder append(CharSequence csq) {
        if (csq == null) {
            return this;
        }
        return append(csq, 0, csq.length());
    }

    /**
     * 追加字符序列的子序列.
     *
     * @param csq   字符序列
     * @param start 起始位置（含）
     * @param end   结束位置（不含）
     * @return this
     */
    @Override
    public StrBuilder append(CharSequence csq, int start, int end) {
        if (csq == null) {
            return this;
        }
        final int len = end - start;
        if (len <= 0) {
            return this;
        }
        ensureCapacity(position + len);
        for (int i = start; i < end; i++) {
            value[position++] = csq.charAt(i);
        }
        return this;
    }

    /**
     * 追加字符数组.
     *
     * @param src 字符数组
     * @return this
     */
    public StrBuilder append(char[] src) {
        if (src == null || src.length == 0) {
            return this;
        }
        return append(src, 0, src.length);
    }

    /**
     * 追加字符数组的一部分.
     *
     * @param src    字符数组
     * @param srcPos 起始位置
     * @param length 长度
     * @return this
     */
    public StrBuilder append(char[] src, int srcPos, int length) {
        if (src == null || src.length == 0 || length <= 0) {
            return this;
        }
        ensureCapacity(position + length);
        System.arraycopy(src, srcPos, value, position, length);
        position += length;
        return this;
    }

    @Override
    public int length() {
        return position;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= position) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new String(value, start, end - start);
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
     * 重置.
     *
     * @return this
     */
    public StrBuilder reset() {
        this.position = 0;
        return this;
    }

    @Override
    public String toString() {
        return new String(value, 0, position);
    }

    /**
     * 确保容量.
     *
     * @param minimumCapacity 最小容量
     */
    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > value.length) {
            int newCapacity = (value.length << 1) + 2;
            if (newCapacity < minimumCapacity) {
                newCapacity = minimumCapacity;
            }
            char[] newValue = new char[newCapacity];
            System.arraycopy(value, 0, newValue, 0, position);
            value = newValue;
        }
    }
}
