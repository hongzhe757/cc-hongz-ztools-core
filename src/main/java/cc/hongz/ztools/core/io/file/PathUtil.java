package cc.hongz.ztools.core.io.file;

import java.nio.file.Path;

/**
 * NIO中Path对象操作封装（精简版，仅包含ZipUtil所需方法）.
 *
 * @author looly
 * @since 5.4.1
 */
public class PathUtil {

	/**
	 * 获取{@link Path}文件名
	 *
	 * @param path {@link Path}
	 * @return 文件名
	 * @since 5.7.15
	 */
	public static String getName(Path path) {
		if (null == path) {
			return null;
		}
		return path.getFileName().toString();
	}
}
