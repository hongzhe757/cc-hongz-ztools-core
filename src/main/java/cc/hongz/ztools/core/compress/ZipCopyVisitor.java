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
 * Zip文件拷贝的FileVisitor实现，zip中追加文件，此类非线程安全.
 *
 * @author looly
 * @since 5.7.15
 */
public class ZipCopyVisitor extends SimpleFileVisitor<Path> {

	private final Path source;
	private final FileSystem fileSystem;
	private final CopyOption[] copyOptions;

	public ZipCopyVisitor(Path source, FileSystem fileSystem, CopyOption... copyOptions) {
		this.source = source;
		this.fileSystem = fileSystem;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		final Path targetDir = resolveTarget(dir);
		if (StrUtil.isNotEmpty(targetDir.toString())) {
			try {
				Files.copy(dir, targetDir, copyOptions);
			} catch (final DirectoryNotEmptyException ignore) {
				// 目录已经存在，则跳过
			} catch (FileAlreadyExistsException e) {
				if (!Files.isDirectory(targetDir)) {
					throw e;
				}
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.copy(file, resolveTarget(file), copyOptions);
		return FileVisitResult.CONTINUE;
	}

	private Path resolveTarget(Path file) {
		return fileSystem.getPath(source.relativize(file).toString());
	}
}
