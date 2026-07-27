package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.util.StrUtil;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Zip文件拷贝的FileVisitor实现，zip中追加文件，此类非线程安全<br>
 * 此类在遍历源目录并复制过程中会自动创建目标目录中不存在的上级目录.
 *
 * @author looly
 * @since 5.7.15
 */
public class ZipCopyVisitor extends SimpleFileVisitor<Path> {

    /**
     * 源Path，或基准路径，用于计算被拷贝文件的相对路径.
     */
    private final Path source;
    private final FileSystem fileSystem;
    private final CopyOption[] copyOptions;

    /**
     * 构造.
     *
     * @param source      源Path，用于计算被拷贝文件的相对路径
     * @param fileSystem  目标Zip文件
     * @param copyOptions 拷贝选项
     */
    public ZipCopyVisitor(final Path source,
            final FileSystem fileSystem,
            final CopyOption... copyOptions) {
        this.source = source;
        this.fileSystem = fileSystem;
        this.copyOptions = copyOptions;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir,
            final BasicFileAttributes attrs)
            throws IOException {
        final Path targetDir = resolveTarget(dir);
        if (StrUtil.isNotEmpty(targetDir.toString())) {
            try {
                Files.copy(dir, targetDir, copyOptions);
            } catch (final DirectoryNotEmptyException
                    ignore) {
                // 目录已经存在，则跳过
            } catch (final FileAlreadyExistsException e) {
                if (!Files.isDirectory(targetDir)) {
                    throw e;
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file,
            final BasicFileAttributes attrs)
            throws IOException {
        Files.copy(file, resolveTarget(file), copyOptions);
        return FileVisitResult.CONTINUE;
    }

    /**
     * 根据源文件或目录路径，拼接生成目标的文件或目录路径.
     *
     * @param file 需要拷贝的文件或目录Path
     * @return 目标Path
     */
    private Path resolveTarget(final Path file) {
        return fileSystem.getPath(
                source.relativize(file).toString());
    }
}
