package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.file.visitor.MoveVisitor;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.ObjUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.IOException;
import java.nio.file.*;

/**
 * 文件移动封装
 *
 * @author looly
 * @since 5.8.14
 */
public class PathMover {

	/**
	 * 创建文件或目录移动器
	 *
	 * @param src        源文件或目录
	 * @param target     目标文件或目录
	 * @param isOverride 是否覆盖目标文件
	 * @return {@code PathMover}
	 */
	public static PathMover of(final Path src, final Path target, final boolean isOverride) {
		return of(src, target, isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
	}

	/**
	 * 创建文件或目录移动器
	 *
	 * @param src     源文件或目录
	 * @param target  目标文件或目录
	 * @param options 移动参数
	 * @return {@code PathMover}
	 */
	public static PathMover of(final Path src, final Path target, final CopyOption[] options) {
		return new PathMover(src, target, options);
	}

	private final Path src;
	private final Path target;
	private final CopyOption[] options;

	/**
	 * 构造
	 *
	 * @param src     源文件或目录，不能为{@code null}且必须存在
	 * @param target  目标文件或目录
	 * @param options 移动参数
	 */
	public PathMover(final Path src, final Path target, final CopyOption[] options) {
		Assert.notNull(target, "Src path must be not null !");
		if(false == PathUtil.exists(src, false)){
			throw new IllegalArgumentException("Src path is not exist!");
		}
		this.src = src;
		this.target = Assert.notNull(target, "Target path must be not null !");
		this.options = ObjUtil.defaultIfNull(options, () -> new CopyOption[]{});
	}

	/**
	 * 移动文件或目录到目标中
	 *
	 * @return 目标文件Path
	 */
	public Path move() {
		final Path src = this.src;
		Path target = this.target;
		final CopyOption[] options = this.options;

		if (PathUtil.isSub(src, target)) {
			if(Files.exists(target) && PathUtil.equals(src, target)){
				return target;
			}
			throw new IllegalArgumentException(StrUtil.format("Target [{}] is sub path of src [{}]!", target, src));
		}

		if (PathUtil.isDirectory(target)) {
			target = target.resolve(src.getFileName());
		}

		PathUtil.mkParentDirs(target);
		try {
			return Files.move(src, target, options);
		} catch (final IOException e) {
			if (e instanceof FileAlreadyExistsException || e instanceof AccessDeniedException) {
				throw new IORuntimeException(e);
			}
			walkMove(src, target, options);
			PathUtil.del(src);
			return target;
		}
	}

	/**
	 * 移动文件或目录内容到目标中
	 *
	 * @return 目标文件Path
	 */
	public Path moveContent() {
		final Path src = this.src;
		final Path target = this.target;
		if (PathUtil.isExistsAndNotDirectory(target, false)) {
			return move();
		}

		if (Files.exists(target) && PathUtil.equals(src, target)) {
			return target;
		}

		final CopyOption[] options = this.options;
		PathUtil.mkParentDirs(target);
		walkMove(src, target, options);
		return target;
	}

	/**
	 * 递归移动
	 *
	 * @param src     源目录
	 * @param target  目标目录
	 * @param options 移动参数
	 */
	private static void walkMove(final Path src, final Path target, final CopyOption... options) {
		try {
			Files.walkFileTree(src, new MoveVisitor(src, target, options));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
