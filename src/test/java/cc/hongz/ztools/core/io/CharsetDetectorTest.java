package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CharsetDetector} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("CharsetDetector 编码探测器")
class CharsetDetectorTest {

    // ==================================================================== detect(InputStream)

    /**
     * 探测 UTF-8 编码的输入流.
     */
    @Test
    @DisplayName("detect UTF-8 输入流")
    void detectUtf8() {
        final byte[] bytes = "Hello World UTF-8".getBytes(StandardCharsets.UTF_8);
        final InputStream in = new ByteArrayInputStream(bytes);
        final Charset detected = CharsetDetector.detect(in);
        assertNotNull(detected);
    }

    /**
     * 探测 ASCII 内容匹配多个编码（至少返回非 null）.
     */
    @Test
    @DisplayName("detect ASCII 内容返回非 null")
    void detectAscii() {
        final byte[] bytes = "Simple ASCII text".getBytes(StandardCharsets.US_ASCII);
        final InputStream in = new ByteArrayInputStream(bytes);
        final Charset detected = CharsetDetector.detect(in);
        assertNotNull(detected);
    }

    // ==================================================================== detect(InputStream, Charset...)

    /**
     * 指定候选编码探测.
     */
    @Test
    @DisplayName("detect 指定候选编码")
    void detectWithCandidates() {
        final byte[] bytes = "Test Content".getBytes(StandardCharsets.UTF_8);
        final InputStream in = new ByteArrayInputStream(bytes);
        final Charset detected = CharsetDetector.detect(in, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
        assertNotNull(detected);
    }

    /**
     * 空输入流返回 null.
     */
    @Test
    @DisplayName("detect 空输入流返回 null")
    void detectEmptyStream() {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final Charset detected = CharsetDetector.detect(in);
        assertNull(detected);
    }

    // ==================================================================== detect(int, InputStream, Charset...)

    /**
     * 自定义缓冲区大小探测.
     */
    @Test
    @DisplayName("detect 自定义缓冲区大小")
    void detectWithCustomBufferSize() {
        final byte[] bytes = "Custom buffer test".getBytes(StandardCharsets.UTF_8);
        final InputStream in = new ByteArrayInputStream(bytes);
        final Charset detected = CharsetDetector.detect(1024, in);
        assertNotNull(detected);
    }

    // ==================================================================== detect(File)

    /**
     * 探测 UTF-8 文件编码.
     */
    @Test
    @DisplayName("detect UTF-8 文件编码")
    void detectFileEncoding(@TempDir Path tempDir) {
        final File file = tempDir.resolve("test.txt").toFile();
        final String content = "这是一个测试文件内容";
        FileUtil.writeString(content, file, StandardCharsets.UTF_8);
        final Charset detected = CharsetDetector.detect(file);
        assertNotNull(detected);
    }

    /**
     * 探测 GBK 文件编码.
     */
    @Test
    @DisplayName("detect GBK 文件编码")
    void detectGbkFileEncoding(@TempDir Path tempDir) {
        final File file = tempDir.resolve("gbk_test.txt").toFile();
        final String content = "这是一个GBK编码的测试文件";
        FileUtil.writeString(content, file, Charset.forName("GBK"));
        final Charset detected = CharsetDetector.detect(file, Charset.forName("GBK"));
        assertNotNull(detected);
    }
}
