package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.io.resource.Resource;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.ZipUtil;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip生成封装.
 *
 * @author looly
 * @since 5.7.8
 */
@SuppressWarnings("resource")
public class ZipWriter implements Closeable {

    /**
     * 创建ZipWriter.
     *
     * @param zipFile 生成的Zip文件
     * @param charset 编码
     * @return ZipWriter
     */
    public static ZipWriter of(final File zipFile,
            final Charset charset) {
        return new ZipWriter(zipFile, charset);
    }

    /**
     * 创建ZipWriter.
     *
     * @param out     Zip输出的流，一般为输出文件流
     * @param charset 编码
     * @return ZipWriter
     */
    public static ZipWriter of(final OutputStream out,
            final Charset charset) {
        return new ZipWriter(out, charset);
    }

    private File zipFile;
    private final ZipOutputStream out;

    /**
     * 构造.
     *
     * @param zipFile 生成的Zip文件
     * @param charset 编码
     */
    public ZipWriter(final File zipFile,
            final Charset charset) {
        this.zipFile = zipFile;
        this.out = getZipOutputStream(zipFile, charset);
    }

    /**
     * 构造.
     *
     * @param out     {@link ZipOutputStream}
     * @param charset 编码
     */
    public ZipWriter(final OutputStream out,
            final Charset charset) {
        this.out = ZipUtil.getZipOutputStream(out, charset);
    }

    /**
     * 构造.
     *
     * @param out {@link ZipOutputStream}
     */
    public ZipWriter(final ZipOutputStream out) {
        this.out = out;
    }

    /**
     * 设置压缩级别，可选1~9，-1表示默认.
     *
     * @param level 压缩级别
     * @return this
     */
    public ZipWriter setLevel(final int level) {
        this.out.setLevel(level);
        return this;
    }

    /**
     * 设置注释.
     *
     * @param comment 注释
     * @return this
     */
    public ZipWriter setComment(final String comment) {
        this.out.setComment(comment);
        return this;
    }

    /**
     * 获取原始的{@link ZipOutputStream}.
     *
     * @return {@link ZipOutputStream}
     */
    public ZipOutputStream getOut() {
        return this.out;
    }

    /**
     * 对文件或文件目录进行压缩.
     *
     * @param withSrcDir 是否包含被打包目录
     * @param filter     文件过滤器，{@code null}表示不过滤
     * @param files      要压缩的源文件或目录
     * @return this
     * @throws IORuntimeException IO异常
     */
    public ZipWriter add(final boolean withSrcDir,
            final FileFilter filter, final File... files)
            throws IORuntimeException {
        for (final File file : files) {
            String srcRootDir;
            try {
                srcRootDir = file.getCanonicalPath();
                if ((!file.isDirectory()) || withSrcDir) {
                    srcRootDir = file.getCanonicalFile()
                            .getParentFile().getCanonicalPath();
                }
            } catch (final IOException e) {
                throw new IORuntimeException(e);
            }
            addFile(file, srcRootDir, filter);
        }
        return this;
    }

    /**
     * 添加资源到压缩包，添加后关闭资源流.
     *
     * @param resources 需要压缩的资源
     * @return this
     * @throws IORuntimeException IO异常
     */
    public ZipWriter add(final Resource... resources)
            throws IORuntimeException {
        for (final Resource resource : resources) {
            if (null != resource) {
                add(resource.getName(), resource.getStream());
            }
        }
        return this;
    }

    /**
     * 添加文件流到压缩包，添加后关闭输入文件流<br>
     * 如果输入流为{@code null}，则只创建空目录.
     *
     * @param path 压缩的路径, {@code null}和""表示根目录下
     * @param in   需要压缩的输入流
     * @return this
     * @throws IORuntimeException IO异常
     */
    public ZipWriter add(final String path, final InputStream in)
            throws IORuntimeException {
        String p = StrUtil.nullToEmpty(path);
        if (null == in) {
            p = StrUtil.addSuffixIfNot(p, StrUtil.SLASH);
            if (StrUtil.isBlank(p)) {
                return this;
            }
        }
        return putEntry(p, in);
    }

    /**
     * 对流中的数据加入到压缩文件<br>
     * 路径列表和流列表长度必须一致.
     *
     * @param paths 流数据在压缩文件中的路径或文件名
     * @param ins   要压缩的源
     * @return 压缩文件
     * @throws IORuntimeException IO异常
     */
    public ZipWriter add(final String[] paths,
            final InputStream[] ins) throws IORuntimeException {
        if (ArrayUtil.isEmpty(paths)
                || ArrayUtil.isEmpty(ins)) {
            throw new IllegalArgumentException(
                    "Paths or ins is empty !");
        }
        if (paths.length != ins.length) {
            throw new IllegalArgumentException(
                    "Paths length is not equals to ins length !");
        }
        for (int i = 0; i < paths.length; i++) {
            add(paths[i], ins[i]);
        }
        return this;
    }

    @Override
    public void close() throws IORuntimeException {
        try {
            out.finish();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(this.out);
        }
    }

    /**
     * 获得 {@link ZipOutputStream}.
     *
     * @param zipFile 压缩文件
     * @param charset 编码
     * @return {@link ZipOutputStream}
     */
    private static ZipOutputStream getZipOutputStream(
            final File zipFile, final Charset charset) {
        return ZipUtil.getZipOutputStream(
                FileUtil.getOutputStream(zipFile), charset);
    }

    /**
     * 递归压缩文件夹或压缩文件.
     *
     * @param srcRootDir 被压缩的文件夹根目录
     * @param file       当前递归压缩的文件或目录对象
     * @param filter     文件过滤器
     * @return this
     * @throws IORuntimeException IO异常
     */
    private ZipWriter addFile(final File file,
            final String srcRootDir, final FileFilter filter)
            throws IORuntimeException {
        if (null == file || (null != filter
                && !filter.accept(file))) {
            return this;
        }
        final String subPath =
                FileUtil.subPath(srcRootDir, file);
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (ArrayUtil.isEmpty(files)) {
                add(subPath, null);
            } else {
                for (final File childFile : files) {
                    addFile(childFile, srcRootDir, filter);
                }
            }
        } else {
            if (null != this.zipFile
                    && FileUtil.equals(file, zipFile)) {
                return this;
            }
            putEntry(subPath, FileUtil.getInputStream(file));
        }
        return this;
    }

    /**
     * 添加文件流到压缩包，添加后关闭输入文件流.
     *
     * @param path 压缩的路径
     * @param in   需要压缩的输入流
     * @return this
     * @throws IORuntimeException IO异常
     */
    private ZipWriter putEntry(final String path,
            final InputStream in) throws IORuntimeException {
        try {
            out.putNextEntry(new ZipEntry(path));
            if (null != in) {
                IoUtil.copy(in, out);
            }
            out.closeEntry();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
        IoUtil.flush(this.out);
        return this;
    }
}
