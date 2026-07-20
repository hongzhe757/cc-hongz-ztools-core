package cc.hongz.ztools.core.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.lang.Assert;

/**
 * 文件工具类（文件读写 + 类型识别，对应 Hutool {@code cn.hutool.core.io.FileUtil} 子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，封装文件创建与输出流获取.
 * @author hongzhe.qin
 */
public final class FileUtil {

    /**
     * JAR 文件扩展名.
     */
    public static final String JAR_FILE_EXT = ".jar";

    /**
     * Class 文件扩展名.
     */
    public static final String CLASS_EXT = ".class";

    /**
     * 私有构造，禁止实例化.
     */
    private FileUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 创建文件及其父目录；若文件已存在则直接返回.
     *
     * @param path 文件路径
     * @return 文件对象，path 为 null 时返回 null
     * @throws IORuntimeException IO 异常
     */
    public static File touch(final String path) throws IORuntimeException {
        if (path == null) {
            return null;
        }
        return touch(new File(path));
    }

    /**
     * 创建文件及其父目录；若文件已存在则直接返回.
     *
     * @param file 文件对象
     * @return 文件对象，file 为 null 时返回 null
     * @throws IORuntimeException IO 异常
     */
    public static File touch(final File file) throws IORuntimeException {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            mkParentDirs(file);
            try {
                if (!file.createNewFile()) {
                    throw new IORuntimeException("Create file fail: " + file);
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
        return file;
    }

    /**
     * 读取文件全部字节.
     *
     * @param file 文件
     * @return 字节数组
     * @throws IORuntimeException IO 异常
     */
    public static byte[] readBytes(final File file) throws IORuntimeException {
        if (file == null) {
            return null;
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 写入字节到文件（覆盖）.
     *
     * @param data 数据
     * @param dest 目标文件
     * @return 目标文件
     * @throws IORuntimeException IO 异常
     */
    public static File writeBytes(final byte[] data, final File dest) throws IORuntimeException {
        if (dest == null) {
            return null;
        }
        try {
            touch(dest);
            Files.write(dest.toPath(), data == null ? new byte[0] : data);
            return dest;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得文件的缓冲输出流.
     *
     * @param file 目标文件
     * @return 缓冲输出流
     * @throws IORuntimeException IO 异常
     */
    public static BufferedOutputStream getOutputStream(final File file) throws IORuntimeException {
        try {
            return IoUtil.toBuffered(Files.newOutputStream(touch(file).toPath()));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 是否为普通文件.
     *
     * @param file 文件
     * @return 是否为文件
     */
    public static boolean isFile(final File file) {
        return file != null && file.isFile();
    }

    /**
     * 获取文件扩展名（不含 {@code .}）.
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(final String fileName) {
        if (fileName == null) {
            return null;
        }
        final int index = fileName.lastIndexOf('.');
        if (index < 0 || index >= fileName.length() - 1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    /**
     * 获取文件扩展名（不含 {@code .}）.
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(final File file) {
        if (file == null || file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 打开文件输入流.
     *
     * @param file 文件
     * @return 输入流
     */
    public static FileInputStream getInputStream(final File file) throws IORuntimeException {
        return IoUtil.toStream(file);
    }

    /**
     * 根据文件魔数识别类型.
     *
     * @param file 文件
     * @return 扩展名
     * @throws IORuntimeException 读取文件失败
     * @see FileTypeUtil#getType(File)
     */
    public static String getType(final File file) throws IORuntimeException {
        return FileTypeUtil.getType(file);
    }

    /**
     * 读取文件内容为字符串.
     *
     * @param file    文件
     * @param charset 字符集，null 时使用系统默认编码
     * @return 文件内容，file 为 null 时返回 null
     * @throws IORuntimeException IO 异常
     */
    public static String readString(final File file, final Charset charset) throws IORuntimeException {
        if (file == null) {
            return null;
        }
        try {
            final Charset cs = charset != null ? charset : Charset.defaultCharset();
            return new String(Files.readAllBytes(file.toPath()), cs);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将字符串写入文件（覆盖）.
     *
     * @param content 字符串内容
     * @param dest    目标文件
     * @param charset 字符集，null 时使用系统默认编码
     * @return 目标文件，dest 为 null 时返回 null
     * @throws IORuntimeException IO 异常
     */
    public static File writeString(final String content, final File dest, final Charset charset)
            throws IORuntimeException {
        if (dest == null) {
            return null;
        }
        try {
            final Charset cs = charset != null ? charset : Charset.defaultCharset();
            touch(dest);
            Files.write(dest.toPath(), (content == null ? "" : content).getBytes(cs));
            return dest;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 是否为 Windows 操作系统.
     *
     * @return 是否为 Windows
     */
    public static boolean isWindows() {
        final String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().startsWith("windows");
    }

    /**
     * 根据URL获取文件对象.
     *
     * @param url URL
     * @return 文件对象
     */
    public static File file(final URL url) {
        if (url == null) {
            return null;
        }
        return new File(url.getFile());
    }

    /**
     * 创建File对象.
     *
     * @param path 文件路径
     * @return File
     */
    public static File file(final String path) {
        if (null == path) {
            return null;
        }
        return new File(getAbsolutePath(path));
    }

    /**
     * 创建File对象（会检查slip漏洞）.
     *
     * @param parent 父文件对象
     * @param path   文件路径
     * @return File
     */
    public static File file(final File parent, final String path) {
        if (StrUtil.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return checkSlip(parent, buildFile(parent, path));
    }

    /**
     * 创建File对象.
     *
     * @param parent 父目录
     * @param path   文件路径
     * @return File
     */
    public static File file(final String parent, final String path) {
        return file(new File(parent), path);
    }

    /**
     * 创建文件夹，如果存在直接返回.
     *
     * @param dir 目录
     * @return 创建的目录
     */
    public static File mkdir(final File dir) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 创建文件夹.
     *
     * @param dirPath 目录路径
     * @return 创建的目录
     */
    public static File mkdir(final String dirPath) {
        if (dirPath == null) {
            return null;
        }
        return mkdir(file(dirPath));
    }

    /**
     * 返回主文件名（不含扩展名）.
     *
     * @param file 文件
     * @return 主文件名
     */
    public static String mainName(final File file) {
        if (file == null) {
            return null;
        }
        return mainName(file.getName());
    }

    /**
     * 返回主文件名（不含扩展名）.
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String mainName(final String fileName) {
        if (fileName == null) {
            return null;
        }
        final int index = fileName.lastIndexOf('.');
        if (index < 0) {
            return fileName;
        }
        return fileName.substring(0, index);
    }

    /**
     * 获得相对子路径.
     *
     * @param rootDir 父目录
     * @param file    文件
     * @return 相对子路径
     */
    public static String subPath(final String rootDir, final File file) {
        try {
            return subPath(rootDir, file.getCanonicalPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得相对子路径.
     *
     * @param dirPath  父路径
     * @param filePath 文件路径
     * @return 相对子路径
     */
    public static String subPath(final String dirPath, final String filePath) {
        if (StrUtil.isNotEmpty(dirPath) && StrUtil.isNotEmpty(filePath)) {
            String normalizedDir = normalize(dirPath);
            if (normalizedDir.endsWith("/")) {
                normalizedDir = normalizedDir.substring(0, normalizedDir.length() - 1);
            }
            final String normalizedFile = normalize(filePath);
            final String result = StrUtil.removePrefixIgnoreCase(normalizedFile, normalizedDir);
            return StrUtil.removePrefix(result, "/");
        }
        return filePath;
    }

    /**
     * 检查两个文件是否是同一个文件.
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同
     * @throws IORuntimeException IO异常
     */
    public static boolean equals(final File file1, final File file2) throws IORuntimeException {
        Assert.notNull(file1);
        Assert.notNull(file2);
        if (!file1.exists() || !file2.exists()) {
            return !file1.exists() && !file2.exists() && pathEquals(file1, file2);
        }
        try {
            return Files.isSameFile(file1.toPath(), file2.toPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将流的内容写入文件.
     *
     * @param in        输入流
     * @param dest      目标文件
     * @param isCloseIn 是否关闭输入流
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeFromStream(final InputStream in, final File dest, final boolean isCloseIn) throws IORuntimeException {
        try {
            touch(dest);
            Files.copy(in, dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (isCloseIn) {
                IoUtil.close(in);
            }
        }
        return dest;
    }

    /**
     * 判断给定目录是否为子目录.
     *
     * @param parent 父目录
     * @param sub    子目录
     * @return 是否为子目录
     */
    public static boolean isSub(final File parent, final File sub) {
        Assert.notNull(parent);
        Assert.notNull(sub);
        return sub.toPath().toAbsolutePath().normalize().startsWith(parent.toPath().toAbsolutePath().normalize());
    }

    // --------------------------------------- Static method end

    /**
     * 创建父目录.
     *
     * @param file 目标文件
     * @throws IORuntimeException 创建失败
     */
    private static void mkParentDirs(final File file) throws IORuntimeException {
        final File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IORuntimeException("Create parent dir fail: " + parent);
        }
    }

    /**
     * 获取绝对路径.
     *
     * @param path 路径
     * @return 绝对路径
     */
    private static String getAbsolutePath(final String path) {
        return new File(path).getAbsolutePath();
    }

    /**
     * 标准化路径，统一使用 '/' 分隔符.
     *
     * @param path 路径
     * @return 标准化后的路径
     */
    private static String normalize(final String path) {
        if (path == null) {
            return null;
        }
        return path.replace('\\', '/');
    }

    /**
     * 根据压缩包中的路径构建目录结构.
     *
     * @param outFile  最外部路径
     * @param fileName 文件名
     * @return 文件或目录
     */
    private static File buildFile(final File outFile, final String fileName) {
        String normalizedFileName = fileName.replace('\\', '/');
        if (!isWindows()
                && normalizedFileName.lastIndexOf(CharUtil.SLASH, normalizedFileName.length() - 2) > 0) {
            final java.util.List<String> pathParts = StrUtil.split(normalizedFileName, '/', true, true);
            final int lastPartIndex = pathParts.size() - 1;
            File current = outFile;
            for (int i = 0; i < lastPartIndex; i++) {
                current = new File(current, pathParts.get(i));
            }
            current.mkdirs();
            normalizedFileName = pathParts.get(lastPartIndex);
        }
        return new File(current(outFile, normalizedFileName), normalizedFileName);
    }

    /**
     * 获取目标文件（内部辅助）.
     */
    private static File current(final File outFile, final String fileName) {
        // 返回 outFile，供 buildFile 中最后 new File 使用
        return outFile;
    }

    /**
     * 检查slip漏洞.
     *
     * @param parent 父目录
     * @param target 目标文件
     * @return 目标文件
     */
    private static File checkSlip(final File parent, final File target) {
        if (parent == null || target == null) {
            return target;
        }
        try {
            final Path parentPath = parent.toPath().toAbsolutePath().normalize();
            final Path targetPath = target.toPath().toAbsolutePath().normalize();
            if (!targetPath.startsWith(parentPath)) {
                throw new IORuntimeException(
                        StrUtil.format("Zip slip detected: [{}] is not a child of [{}]", target, parent));
            }
        } catch (IORuntimeException e) {
            throw e;
        } catch (Exception e) {
            // ignore
        }
        return target;
    }

    /**
     * 文件路径是否相同（忽略Windows大小写）.
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 路径是否相同
     */
    private static boolean pathEquals(final File file1, final File file2) {
        try {
            if (isWindows()) {
                return file1.getCanonicalPath().equalsIgnoreCase(file2.getCanonicalPath());
            } else {
                return file1.getCanonicalPath().equals(file2.getCanonicalPath());
            }
        } catch (Exception e) {
            return file1.getAbsolutePath().equals(file2.getAbsolutePath());
        }
    }
}
