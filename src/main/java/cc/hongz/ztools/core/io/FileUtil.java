package cc.hongz.ztools.core.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 文件工具类（文件读写 + 类型识别，对应 Hutool {@code cn.hutool.core.io.FileUtil} 子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，封装文件创建与输出流获取.
 * @author hongzhe.qin
 */
public final class FileUtil {

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
}
