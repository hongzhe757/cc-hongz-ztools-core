package cc.hongz.ztools.core.compress;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.util.StrUtil;
import cc.hongz.ztools.core.util.ZipUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Zip文件或流读取器，一般用于Zip文件解压.
 *
 * @author looly
 * @since 5.7.8
 */
public class ZipReader implements Closeable {

    private static final int DEFAULT_MAX_SIZE_DIFF = 100;

    private ZipFile zipFile;
    private ZipInputStream in;
    /**
     * 检查ZipBomb文件差异倍数，-1表示不检查ZipBomb.
     */
    private int maxSizeDiff = DEFAULT_MAX_SIZE_DIFF;

    /**
     * 创建ZipReader.
     *
     * @param zipFile 生成的Zip文件
     * @param charset 编码
     * @return ZipReader
     */
    public static ZipReader of(final File zipFile,
            final Charset charset) {
        return new ZipReader(zipFile, charset);
    }

    /**
     * 创建ZipReader.
     *
     * @param in      Zip输入的流，一般为输入文件流
     * @param charset 编码
     * @return ZipReader
     */
    public static ZipReader of(final InputStream in,
            final Charset charset) {
        return new ZipReader(in, charset);
    }

    /**
     * 构造.
     *
     * @param zipFile 读取的的Zip文件
     * @param charset 编码
     */
    public ZipReader(final File zipFile, final Charset charset) {
        this.zipFile = ZipUtil.toZipFile(zipFile, charset);
    }

    /**
     * 构造.
     *
     * @param zipFile 读取的的Zip文件
     */
    public ZipReader(final ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * 构造.
     *
     * @param in      读取的的Zip文件流
     * @param charset 编码
     */
    public ZipReader(final InputStream in, final Charset charset) {
        this.in = new ZipInputStream(in, charset);
    }

    /**
     * 构造.
     *
     * @param zin 读取的的Zip文件流
     */
    public ZipReader(final ZipInputStream zin) {
        this.in = zin;
    }

    /**
     * 设置检查ZipBomb文件差异倍数，-1表示不检查ZipBomb.
     *
     * @param maxSizeDiff 检查ZipBomb文件差异倍数
     * @return this
     */
    public ZipReader setMaxSizeDiff(final int newMaxSizeDiff) {
        this.maxSizeDiff = newMaxSizeDiff;
        return this;
    }

    /**
     * 获取指定路径的文件流<br>
     * 如果是文件模式，则直接获取Entry对应的流，
     * 如果是流模式，则遍历entry后，找到对应流返回.
     *
     * @param path 路径
     * @return 文件流
     */
    public InputStream get(final String path) {
        if (null != this.zipFile) {
            final ZipFile theZipFile = this.zipFile;
            final ZipEntry entry = theZipFile.getEntry(path);
            if (null != entry) {
                return ZipUtil.getStream(theZipFile, entry);
            }
        } else {
            try {
                ZipEntry zipEntry;
                while (null != (zipEntry = in.getNextEntry())) {
                    if (zipEntry.getName().equals(path)) {
                        return this.in;
                    }
                }
            } catch (final IOException e) {
                throw new IORuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 解压到指定目录中.
     *
     * @param outFile 解压到的目录
     * @return 解压的目录
     * @throws IORuntimeException IO异常
     */
    public File readTo(final File outFile)
            throws IORuntimeException {
        return readTo(outFile, null);
    }

    /**
     * 解压到指定目录中.
     *
     * @param outFile     解压到的目录
     * @param entryFilter 过滤器，排除不需要的文件
     * @return 解压的目录
     * @throws IORuntimeException IO异常
     */
    public File readTo(final File outFile,
            final Filter<ZipEntry> entryFilter)
            throws IORuntimeException {
        read((zipEntry) -> {
            if (null == entryFilter
                    || entryFilter.accept(zipEntry)) {
                String path = zipEntry.getName();
                if (FileUtil.isWindows()) {
                    path = StrUtil.replace(path, "*", "_");
                }
                final File outItemFile =
                        FileUtil.file(outFile, path);
                if (zipEntry.isDirectory()) {
                    outItemFile.mkdirs();
                } else {
                    final InputStream inputStream;
                    if (null != this.zipFile) {
                        inputStream = ZipUtil.getStream(
                                this.zipFile, zipEntry);
                    } else {
                        inputStream = this.in;
                    }
                    FileUtil.writeFromStream(
                            inputStream, outItemFile, false);
                }
            }
        });
        return outFile;
    }

    /**
     * 读取并处理Zip文件中的每一个{@link ZipEntry}.
     *
     * @param consumer {@link ZipEntry}处理器
     * @return this
     * @throws IORuntimeException IO异常
     */
    public ZipReader read(final Consumer<ZipEntry> consumer)
            throws IORuntimeException {
        if (null != this.zipFile) {
            readFromZipFile(consumer);
        } else {
            readFromStream(consumer);
        }
        return this;
    }

    @Override
    public void close() throws IORuntimeException {
        if (null != this.zipFile) {
            IoUtil.close(this.zipFile);
        } else {
            IoUtil.close(this.in);
        }
    }

    /**
     * 读取并处理Zip文件中的每一个{@link ZipEntry}.
     *
     * @param consumer {@link ZipEntry}处理器
     */
    private void readFromZipFile(
            final Consumer<ZipEntry> consumer) {
        final Enumeration<? extends ZipEntry> em =
                zipFile.entries();
        while (em.hasMoreElements()) {
            consumer.accept(checkZipBomb(em.nextElement()));
        }
    }

    /**
     * 读取并处理Zip流中的每一个{@link ZipEntry}.
     *
     * @param consumer {@link ZipEntry}处理器
     * @throws IORuntimeException IO异常
     */
    private void readFromStream(
            final Consumer<ZipEntry> consumer)
            throws IORuntimeException {
        try {
            ZipEntry zipEntry;
            while (null != (zipEntry = in.getNextEntry())) {
                consumer.accept(zipEntry);
                checkZipBomb(zipEntry);
            }
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 检查Zip bomb漏洞.
     *
     * @param entry {@link ZipEntry}
     * @return 检查后的{@link ZipEntry}
     */
    private ZipEntry checkZipBomb(final ZipEntry entry) {
        if (null == entry) {
            return null;
        }
        if (maxSizeDiff < 0 || entry.isDirectory()) {
            return entry;
        }
        final long compressedSize = entry.getCompressedSize();
        final long uncompressedSize = entry.getSize();
        if (compressedSize < 0 || uncompressedSize < 0
                || compressedSize * maxSizeDiff
                        < uncompressedSize) {
            throw new UtilException(
                "Zip bomb attack detected, "
                    + "invalid sizes: compressed {}, "
                    + "uncompressed {}, name {}",
                compressedSize, uncompressedSize,
                entry.getName());
        }
        return entry;
    }
}
