package cc.hongz.ztools.core.img.gif;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AnimatedGifEncoder} 单元测试.
 * <p>
 * 覆盖全部 public 方法.
 *
 * @author hongzhe.qin
 */
@DisplayName("AnimatedGifEncoder GIF 编码器单元测试")
class AnimatedGifEncoderTest {

    private static BufferedImage blueFrame() {
        final BufferedImage frame = new BufferedImage(20, 10, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = frame.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 20, 10);
        g.dispose();
        return frame;
    }

    /**
     * 验证 {@link AnimatedGifEncoder#start(java.io.OutputStream)}、{@link AnimatedGifEncoder#setDelay(int)}、
     * {@link AnimatedGifEncoder#addFrame(BufferedImage)}、{@link AnimatedGifEncoder#finish()} 输出合法 GIF.
     */
    @DisplayName("编码单帧 GIF")
    @Test
    void encodeSingleFrameGif() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        assertTrue(encoder.start(out));
        assertTrue(encoder.isStarted());
        encoder.setDelay(100);
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.finish());
        assertFalse(encoder.isStarted());
        assertTrue(out.size() > 6);
        assertTrue(new String(out.toByteArray(), 0, 3).startsWith("GIF"));
    }

    /**
     * 验证 {@link AnimatedGifEncoder#start(String)} 写入文件路径.
     *
     * @param tempDir JUnit 临时目录
     */
    @DisplayName("通过文件路径启动编码")
    @Test
    void startWithFilePath(@TempDir final Path tempDir) throws Exception {
        final String path = tempDir.resolve("out.gif").toString();
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        assertTrue(encoder.start(path));
        encoder.setDelay(50);
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.finish());
        assertTrue(java.nio.file.Files.size(tempDir.resolve("out.gif")) > 6);
    }

    /**
     * 验证 {@link AnimatedGifEncoder#setSize(int, int)} 在首帧前设定画布尺寸.
     */
    @DisplayName("首帧前设定画布尺寸")
    @Test
    void setSizeBeforeFirstFrame() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setSize(30, 15);
        assertTrue(encoder.start(out));
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.finish());
    }

    /**
     * 验证 {@link AnimatedGifEncoder#setRepeat(int)}、{@link AnimatedGifEncoder#setFrameRate(float)}、
     * {@link AnimatedGifEncoder#setQuality(int)} 等配置方法不抛异常.
     */
    @DisplayName("配置方法不抛异常")
    @Test
    void configurationMethods() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(2);
        encoder.setFrameRate(10f);
        encoder.setQuality(0);
        encoder.setDispose(1);
        encoder.setTransparent(Color.WHITE);
        encoder.setTransparent(Color.RED, true);
        encoder.setBackground(Color.BLACK);
        assertTrue(encoder.start(out));
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.finish());
    }

    /**
     * 验证多帧 GIF 编码.
     */
    @DisplayName("编码多帧 GIF")
    @Test
    void encodeMultipleFrames() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        assertTrue(encoder.start(out));
        encoder.setDelay(50);
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.addFrame(blueFrame()));
        assertTrue(encoder.finish());
        assertTrue(out.size() > 10);
    }

    /**
     * 验证未调用 start 时 {@link AnimatedGifEncoder#addFrame(BufferedImage)} 返回 false.
     */
    @DisplayName("未 start 时 addFrame 返回 false")
    @Test
    void addFrameBeforeStartFails() {
        final AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        assertFalse(encoder.addFrame(blueFrame()));
    }

    /**
     * 验证 {@link AnimatedGifEncoder#start(java.io.OutputStream)} 对 null 返回 false.
     */
    @DisplayName("null 流 start 返回 false")
    @Test
    void startNullStreamFails() {
        assertFalse(new AnimatedGifEncoder().start((java.io.OutputStream) null));
    }

    /**
     * 验证未 start 时 {@link AnimatedGifEncoder#finish()} 返回 false.
     */
    @DisplayName("未 start 时 finish 返回 false")
    @Test
    void finishWithoutStartFails() {
        assertFalse(new AnimatedGifEncoder().finish());
    }
}
