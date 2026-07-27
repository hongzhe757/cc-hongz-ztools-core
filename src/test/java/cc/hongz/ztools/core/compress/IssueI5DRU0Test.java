package cc.hongz.ztools.core.compress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://gitee.com/chinabugotech/hutool/issues/I5DRU0
 * 向zip中添加文件时，如果父目录已存在，应忽略而非报错.
 *
 * @author hongzhe.qin
 */
class IssueI5DRU0Test {

    @Test
    @DisplayName("I5DRU0 - 向已有zip追加文件（目录已存在应忽略）")
    void appendToExistingZip(@TempDir final Path tempDir)
            throws IOException {
        // 创建源目录结构
        Path srcDir = tempDir.resolve("src");
        Files.createDirectories(srcDir.resolve("sub"));
        Files.write(srcDir.resolve("sub/a.txt"),
                "file A".getBytes());
        Files.write(srcDir.resolve("sub/b.txt"),
                "file B".getBytes());

        // 第一次：创建zip
        Path zipPath = tempDir.resolve("append.zip");
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create("jar:" + zipPath.toUri());
        try (java.nio.file.FileSystem fs =
                java.nio.file.FileSystems.newFileSystem(
                        uri, env)) {
            // 先添加 sub/a.txt
            Files.walkFileTree(srcDir,
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(
                                final Path file,
                                final BasicFileAttributes a)
                                throws IOException {
                            String rel = srcDir.relativize(
                                    file).toString();
                            Path target = fs.getPath(rel);
                            if (target.getParent() != null) {
                                try {
                                    Files.createDirectories(
                                            target.getParent());
                                } catch (final Exception e) {
                                    // 目录已存在，忽略
                                }
                            }
                            Files.copy(file, target,
                                    StandardCopyOption
                                            .REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    });
        }

        // 第二次：再次添加 sub/b.txt（sub目录已存在）
        try (java.nio.file.FileSystem fs =
                java.nio.file.FileSystems.newFileSystem(
                        uri, env)) {
            Files.walkFileTree(srcDir,
                    new ZipCopyVisitor(srcDir, fs,
                            StandardCopyOption.REPLACE_EXISTING));
        }
        // 不抛异常即通过

        // 验证两个文件都在
        java.util.zip.ZipFile zf =
                new java.util.zip.ZipFile(zipPath.toFile(),
                        StandardCharsets.UTF_8);
        long count = zf.stream()
                .filter(e -> !e.isDirectory())
                .count();
        zf.close();
        assertEquals(2, count);
    }
}
