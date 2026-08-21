package cc.hongz.ztools.core.io;

/**
 * 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组.
 *
 * @author biezhi, looly
 * @since 1.0
 */
public class FastByteBuffer {

    private static final int DEFAULT_BUFFERS_COUNT = 16;
    private static final int DEFAULT_CHUNK_LEN = 1024;

    /**
     * 缓冲集.
     */
    private byte[][] buffers = new byte[DEFAULT_BUFFERS_COUNT][];
    /**
     * 缓冲数.
     */
    private int buffersCount;
    /**
     * 当前缓冲索引.
     */
    private int currentBufferIndex = -1;
    /**
     * 当前缓冲.
     */
    private byte[] currentBuffer;
    /**
     * 当前缓冲偏移量.
     */
    private int offset;
    /**
     * 缓冲字节数.
     */
    private int size;

    /**
     * 一个缓冲区的最小字节数.
     */
    private final int minChunkLen;

    /**
     * 构造，默认缓冲字节数.
     */
    public FastByteBuffer() {
        this(DEFAULT_CHUNK_LEN);
    }

    /**
     * 构造.
     *
     * @param size 缓冲字节数
     */
    public FastByteBuffer(final int size) {
        final int actualSize = size <= 0 ? DEFAULT_CHUNK_LEN : size;
        this.minChunkLen = Math.abs(actualSize);
    }

    /**
     * 分配下一个缓冲区，不会小于1024.
     *
     * @param newSize 理想缓冲区字节数
     */
    private void needNewBuffer(final int newSize) {
        int delta = newSize - size;
        int newBufferSize = Math.max(minChunkLen, delta);

        currentBufferIndex++;
        currentBuffer = new byte[newBufferSize];
        offset = 0;

        // add buffer
        if (currentBufferIndex >= buffers.length) {
            int newLen = buffers.length << 1;
            byte[][] newBuffers = new byte[newLen][];
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffers[currentBufferIndex] = currentBuffer;
        buffersCount++;
    }

    /**
     * 向快速缓冲加入数据.
     *
     * @param array 数据
     * @param off   偏移量
     * @param len   字节数
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte[] array, final int off, final int len) {
        int end = off + len;
        if ((off < 0) || (len < 0) || (end > array.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return this;
        }
        int newSize = size + len;
        int remaining = len;

        if (currentBuffer != null) {
            // first try to fill current buffer
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            remaining -= part;
            offset += part;
            size += part;
        }

        if (remaining > 0) {
            // still some data left
            // ask for new buffer
            needNewBuffer(newSize);

            // then copy remaining
            // but this time we are sure that it will fit
            int part = Math.min(remaining, currentBuffer.length - offset);
            System.arraycopy(array, end - remaining, currentBuffer, offset, part);
            offset += part;
            size += part;
        }

        return this;
    }

    /**
     * 向快速缓冲加入数据.
     *
     * @param array 数据
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte[] array) {
        return append(array, 0, array.length);
    }

    /**
     * 向快速缓冲加入一个字节.
     *
     * @param element 一个字节的数据
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte element) {
        if ((currentBuffer == null) || (offset == currentBuffer.length)) {
            needNewBuffer(size + 1);
        }

        currentBuffer[offset] = element;
        offset++;
        size++;

        return this;
    }

    /**
     * 将另一个快速缓冲加入到自身.
     *
     * @param buff 快速缓冲
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final FastByteBuffer buff) {
        if (buff.size == 0) {
            return this;
        }
        for (int i = 0; i < buff.currentBufferIndex; i++) {
            append(buff.buffers[i]);
        }
        append(buff.currentBuffer, 0, buff.offset);
        return this;
    }

    /**
     * 获取缓冲字节数.
     *
     * @return 缓冲字节数
     */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 当前缓冲位于缓冲区的索引位.
     *
     * @return {@link #currentBufferIndex}
     */
    public int index() {
        return currentBufferIndex;
    }

    /**
     * 获取当前缓冲偏移量.
     *
     * @return 当前缓冲偏移量
     */
    public int offset() {
        return offset;
    }

    /**
     * 根据索引位返回缓冲集中的缓冲.
     *
     * @param index 索引位
     * @return 缓冲
     */
    public byte[] array(final int index) {
        return buffers[index];
    }

    /**
     * 重置缓冲区.
     */
    public void reset() {
        size = 0;
        offset = 0;
        currentBufferIndex = -1;
        currentBuffer = null;
        buffersCount = 0;
    }

    /**
     * 返回快速缓冲中的数据.
     *
     * @return 快速缓冲中的数据
     */
    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[size];

        if (currentBufferIndex == -1) {
            return array;
        }

        for (int i = 0; i < currentBufferIndex; i++) {
            int len = buffers[i].length;
            System.arraycopy(buffers[i], 0, array, pos, len);
            pos += len;
        }

        System.arraycopy(buffers[currentBufferIndex], 0, array, pos, offset);

        return array;
    }

    /**
     * 返回快速缓冲中的数据.
     *
     * @param start 逻辑起始位置
     * @param len   逻辑字节长
     * @return 快速缓冲中的数据
     */
    public byte[] toArray(final int start, final int len) {
        int remaining = len;
        int pos = 0;
        byte[] array = new byte[len];
        int start0 = start;

        if (len == 0) {
            return array;
        }

        int i = 0;
        while (start0 >= buffers[i].length) {
            start0 -= buffers[i].length;
            i++;
        }

        while (i < buffersCount) {
            byte[] buf = buffers[i];
            int c = Math.min(buf.length - start0, remaining);
            System.arraycopy(buf, start0, array, pos, c);
            pos += c;
            remaining -= c;
            if (remaining == 0) {
                break;
            }
            start0 = 0;
            i++;
        }
        return array;
    }

    /**
     * 根据索引位返回一个字节.
     *
     * @param index 索引位
     * @return 一个字节
     */
    public byte get(final int index) {
        if ((index >= size) || (index < 0)) {
            throw new IndexOutOfBoundsException();
        }
        int index0 = index;
        int ndx = 0;
        while (true) {
            byte[] b = buffers[ndx];
            if (index0 < b.length) {
                return b[index0];
            }
            ndx++;
            index0 -= b.length;
        }
    }

}
