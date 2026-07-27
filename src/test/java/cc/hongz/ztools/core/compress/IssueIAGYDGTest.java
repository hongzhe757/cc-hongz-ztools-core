package cc.hongz.ztools.core.compress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://gitee.com/chinabugotech/hutool/issues/IAGYDG
 * 压缩时zip输出文件本身不应被添加到zip中，避免死循环.
 *
 * @author hongzhe.qin
 */
class IssueIAGYDGTest {

    @Test
    @DisplayName("IAGYDG - zip输出文件不应包含自身")
    void zipShouldNotContainSelf(@TempDir final Path tempDir)
            throws IOException {
        // 创建源文件
        Path srcDir = tempDir.resolve("src");
        Files.createDirectories(srcDir);
        Files.write(srcDir.resolve("a.txt"),
                "content a".getBytes());

        // 将zip输出到src目录下（与源文件同目录）
        File zipFile = tempDir.resolve("src/result.zip").toFile();

        ZipWriter writer = ZipWriter.of(
                zipFile, StandardCharsets.UTF_8);
        // 压缩src目录，zipFile本身也在src下，应被自动排除
        writer.add(false, null, srcDir.toFile());
        writer.close();

        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);

        // 验证zip中不包含result.zip自身
        try (ZipFile zf = new ZipFile(zipFile)) {
            boolean hasSelf = false;
            final Enumeration<? extends ZipEntry> entries =
                    zf.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (entry.getName().contains("result.zip")) {
                    hasSelf = true;
                }
            }
            assertFalse(hasSelf,
                "zip should not contain itself");
        }
    }
}
