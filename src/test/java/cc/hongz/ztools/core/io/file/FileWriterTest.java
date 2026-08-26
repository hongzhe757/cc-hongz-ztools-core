package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FileWriter} 单元测试.
 */
@DisplayName("FileWriter 单元测试")
class FileWriterTest {

    @DisplayName("写字符串")
    @Test
    void writeStringTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "write.txt");
        final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        writer.write("hello writer");

        assertEquals("hello writer", FileUtil.readString(file, StandardCharsets.UTF_8));
    }

    @DisplayName("写行")
    @Test
    void writeLinesTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "lines.txt");
        final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        final List<String> lines = java.util.Arrays.asList("line1", "line2", "line3");
        writer.writeLines(lines);

        final List<String> result = FileUtil.readLines(file, StandardCharsets.UTF_8);
        assertEquals(lines, result);
    }

    @DisplayName("追加字符串")
    @Test
    void appendStringTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "append.txt");
        FileUtil.writeString("first", file, StandardCharsets.UTF_8);

        final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        writer.append(" second");

        assertEquals("first second", FileUtil.readString(file, StandardCharsets.UTF_8));
    }

    @DisplayName("写字节")
    @Test
    void writeBytesTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "bytes.txt");
        final FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        final byte[] data = {10, 20, 30};
        writer.write(data, 0, data.length);

        assertArrayEquals(data, java.nio.file.Files.readAllBytes(file.toPath()));
    }

    @DisplayName("从路径写")
    @Test
    void writeFromPathTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "frompath.txt");
        final FileWriter writer = new FileWriter(file.getAbsolutePath(), StandardCharsets.UTF_8);
        writer.write("path writer");

        assertEquals("path writer", FileUtil.readString(file, StandardCharsets.UTF_8));
    }

    @DisplayName("创建")
    @Test
    void createTest(@TempDir final Path tempDir) throws Exception {
        final File file = new File(tempDir.toFile(), "create.txt");
        assertFalse(file.exists());

        final FileWriter writer = FileWriter.create(file, StandardCharsets.UTF_8);
        writer.write("created");
        assertTrue(file.exists());
        assertEquals("created", FileUtil.readString(file, StandardCharsets.UTF_8));
    }
}
