package cc.hongz.ztools.core.img.gif;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link GifDecoder} 单元测试.
 * <p>
 * 覆盖基本初始化和状态常量.
 *
 * @author hongzhe.qin
 */
@DisplayName("GifDecoder GIF 解码器单元测试")
class GifDecoderTest {

    @DisplayName("新实例帧数为 0")
    @Test
    void newInstance_hasZeroFrames() {
        GifDecoder decoder = new GifDecoder();
        assertEquals(0, decoder.getFrameCount());
    }

    @DisplayName("新实例 getImage 返回 null")
    @Test
    void newInstance_getImageReturnsNull() {
        GifDecoder decoder = new GifDecoder();
        assertNull(decoder.getImage());
    }

    @DisplayName("非法索引 getDelay 返回负数")
    @Test
    void getDelay_invalidIndex_returnsNegative() {
        GifDecoder decoder = new GifDecoder();
        assertEquals(-1, decoder.getDelay(0));
    }

    @DisplayName("负索引 getDelay 返回负数")
    @Test
    void getDelay_negativeIndex_returnsNegative() {
        GifDecoder decoder = new GifDecoder();
        assertEquals(-1, decoder.getDelay(-1));
    }

    @DisplayName("状态常量值")
    @Test
    void statusConstants() {
        assertEquals(0, GifDecoder.STATUS_OK);
        assertEquals(1, GifDecoder.STATUS_FORMAT_ERROR);
        assertEquals(2, GifDecoder.STATUS_OPEN_ERROR);
    }

    @DisplayName("默认循环次数为 1")
    @Test
    void getLoopCount_defaultIsOne() {
        GifDecoder decoder = new GifDecoder();
        assertEquals(1, decoder.getLoopCount());
    }
}
