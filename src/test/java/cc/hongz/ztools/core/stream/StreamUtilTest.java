package cc.hongz.ztools.core.stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StreamUtil} 单元测试.
 */
class StreamUtilTest {

    @Test
    @DisplayName("数组转 Stream 测试")
    void ofArrayTest() {
        List<String> result = StreamUtil.of("a", "b", "c").collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    @DisplayName("Iterable 转 Stream 测试")
    void ofIterableTest() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = StreamUtil.of(list).collect(Collectors.toList());
        assertEquals(list, result);
    }

    @Test
    @DisplayName("Iterable 并行 Stream 测试")
    void ofIterableParallelTest() {
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream = StreamUtil.of(list, true);
        assertTrue(stream.isParallel());
    }

    @Test
    @DisplayName("Iterator 转 Stream 测试")
    void ofIteratorTest() {
        Iterator<String> iterator = Arrays.asList("a", "b", "c").iterator();
        List<String> result = StreamUtil.of(iterator).collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    @DisplayName("文件转 Stream 测试")
    void ofFileTest() throws IOException {
        Path tempFile = Files.createTempFile("stream-test", ".txt");
        Files.writeString(tempFile, "line1\nline2\nline3");

        List<String> lines = StreamUtil.of(tempFile.toFile()).collect(Collectors.toList());
        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
        assertEquals("line3", lines.get(2));

        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("文件转 Stream 指定编码测试")
    void ofFileWithCharsetTest() throws IOException {
        Path tempFile = Files.createTempFile("stream-test-charset", ".txt");
        Files.writeString(tempFile, "hello\nworld", StandardCharsets.UTF_8);

        List<String> lines = StreamUtil.of(tempFile.toFile(), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
        assertEquals(2, lines.size());
        assertEquals("hello", lines.get(0));

        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("Path 转 Stream 测试")
    void ofPathTest() throws IOException {
        Path tempFile = Files.createTempFile("stream-path-test", ".txt");
        Files.writeString(tempFile, "aaa\nbbb");

        List<String> lines = StreamUtil.of(tempFile).collect(Collectors.toList());
        assertEquals(2, lines.size());
        assertEquals("aaa", lines.get(0));

        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("seed 生成 Stream 测试")
    void ofSeedTest() {
        List<Integer> result = StreamUtil.of(1, n -> n + 1, 5).collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    @DisplayName("join 拼接测试")
    void joinTest() {
        String result = StreamUtil.join(Stream.of("a", "b", "c"), ",");
        assertEquals("a,b,c", result);
    }

    @Test
    @DisplayName("join 自定义函数测试")
    void joinWithFuncTest() {
        String result = StreamUtil.join(Stream.of(1, 2, 3), "-", Object::toString);
        assertEquals("1-2-3", result);
    }
}
