package cc.hongz.ztools.core.io.file;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * {@link FileSystem}相关工具类封装.
 *
 * @author looly
 * @since 5.7.15
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class FileSystemUtil {

    /**
     * 创建 Zip的{@link FileSystem}，默认UTF-8编码。
     *
     * @param path 文件路径
     * @return {@link FileSystem}
 */
    public static FileSystem createZip(final String path) {
        return createZip(path, null);
    }

    /**
     * 创建 Zip的{@link FileSystem}。
     *
     * @param path    文件路径
     * @param charset 编码
     * @return {@link FileSystem}
 */
    public static FileSystem createZip(final String path, final Charset charset) {
        final Charset resolvedCharset = null == charset ? CharsetUtil.CHARSET_UTF_8 : charset;
        final HashMap<String, String> env = new HashMap<>();
        env.put("create", "true");
        env.put("encoding", resolvedCharset.name());

        try {
            return FileSystems.newFileSystem(
                    URI.create("jar:" + Paths.get(path).toUri()), env);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取目录的根路径。
     *
     * @param fileSystem {@link FileSystem}
     * @return 根 {@link Path}
 */
    public static Path getRoot(final FileSystem fileSystem) {
        return fileSystem.getPath(StrUtil.SLASH);
    }
}
