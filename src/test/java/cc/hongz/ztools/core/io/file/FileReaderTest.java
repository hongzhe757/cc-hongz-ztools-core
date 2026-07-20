package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.LineHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileReader} 单元测试.
 */
class FileReaderTest {

    @Test
    void readStringTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "test.txt");
        FileUtil.writeString("hello world", file, StandardCharsets.UTF_8);

        final FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        assertEquals("hello world", reader.readString());
    }

    @Test
    void readLinesTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "lines.txt");
        final List<String> lines = java.util.Arrays.asList("line1", "line2", "line3");
        FileUtil.writeLines(lines, file, StandardCharsets.UTF_8);

        final FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        final List<String> result = reader.readLines();
        assertEquals(lines, result);
    }

    @Test
    void readBytesTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "bytes.txt");
        final byte[] data = {1, 2, 3, 4, 5};
        java.nio.file.Files.write(file.toPath(), data);

        final FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        assertArrayEquals(data, reader.readBytes());
    }

    @Test
    void readFromPathTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "path.txt");
        FileUtil.writeString("path content", file, StandardCharsets.UTF_8);

        final FileReader reader = new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8);
        assertEquals("path content", reader.readString());
    }

    @Test
    void readLinesWithHandlerTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "handler.txt");
        final List<String> lines = java.util.Arrays.asList("a", "b", "c");
        FileUtil.writeLines(lines, file, StandardCharsets.UTF_8);

        final FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        final StringBuilder sb = new StringBuilder();
        reader.readLines((LineHandler) line -> sb.append(line).append(","));
        assertEquals("a,b,c,", sb.toString());
    }
}
