package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.collection.EnumerationIter;
import cc.hongz.ztools.core.compress.Deflate;
import cc.hongz.ztools.core.compress.Gzip;
import cc.hongz.ztools.core.compress.ZipCopyVisitor;
import cc.hongz.ztools.core.compress.ZipReader;
import cc.hongz.ztools.core.compress.ZipWriter;
import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FastByteArrayOutputStream;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.io.LimitedInputStream;
import cc.hongz.ztools.core.io.file.FileSystemUtil;
import cc.hongz.ztools.core.io.file.PathUtil;
import cc.hongz.ztools.core.io.resource.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类，提供 Zip、Gzip、Zlib 的压缩与解压方法.
 *
 * @author Looly
 * @see ZipWriter
 */
public final class ZipUtil {

	/**
	 * 默认字节数组长度.
	 */
	private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

	/**
	 * 默认编码，使用平台相关编码.
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

	private ZipUtil() {
		// 工具类，禁止实例化
	}

	/**
	 * 将Zip文件转换为 {@link ZipFile}.
	 *
	 * @param file    zip文件
	 * @param charset 解析zip文件的编码，null表示 {@link CharsetUtil#CHARSET_UTF_8}
	 * @return {@link ZipFile}
	 */
	public static ZipFile toZipFile(final File file, final Charset charset) {
		try {
			return new ZipFile(file, ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8));
		} catch (IOException e) {
			if (e instanceof ZipException) {
				if (e.getMessage().contains("invalid CEN header")) {
					final Charset fallback = CharsetUtil.CHARSET_UTF_8.equals(charset) ? CharsetUtil.CHARSET_GBK : CharsetUtil.CHARSET_UTF_8;
					try {
						return new ZipFile(file, fallback);
					} catch (IOException ex) {
						throw new IORuntimeException(ex);
					}
				}
			} throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取指定 {@link ZipEntry} 的流.
	 *
	 * @param zipFile  {@link ZipFile}
	 * @param zipEntry {@link ZipEntry}
	 * @return 流
	 */
	public static InputStream getStream(final ZipFile zipFile, final ZipEntry zipEntry) {
		try {
			return new LimitedInputStream(zipFile.getInputStream(zipEntry), zipEntry.getSize());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得 {@link ZipOutputStream}.
	 *
	 * @param out     压缩文件流
	 * @param charset 编码
	 * @return {@link ZipOutputStream}
	 */
	public static ZipOutputStream getZipOutputStream(final OutputStream out, final Charset charset) {
		if (out instanceof ZipOutputStream) {
			return (ZipOutputStream) out;
		} return new ZipOutputStream(out, charset);
	}

	/**
	 * 在zip文件中添加新文件或目录.
	 *
	 * @param zipPath        zip文件的Path
	 * @param appendFilePath 待添加文件Path
	 * @param options        拷贝选项
	 * @throws IORuntimeException IO异常
	 */
	public static void append(final Path zipPath, final Path appendFilePath, final CopyOption... options) throws IORuntimeException {
		try (FileSystem zipFileSystem = FileSystemUtil.createZip(zipPath.toString())) {
			if (Files.isDirectory(appendFilePath)) {
				Path source = appendFilePath.getParent(); if (null == source) {
					source = appendFilePath;
				} Files.walkFileTree(appendFilePath, new ZipCopyVisitor(source, zipFileSystem, options));
			} else {
				Files.copy(appendFilePath, zipFileSystem.getPath(PathUtil.getName(appendFilePath)), options);
			}
		} catch (FileAlreadyExistsException ignored) {
			// 不覆盖情况下，文件已存在
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// --------------------------------------------------------------------- Zip

	/**
	 * 压缩文件或目录为同目录下的 .zip 文件.
	 *
	 * @param srcPath 源文件路径
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final String srcPath) throws UtilException {
		return zip(srcPath, DEFAULT_CHARSET);
	}

	/**
	 * 压缩文件或目录为同目录下的 .zip 文件.
	 *
	 * @param srcPath 源文件路径
	 * @param charset 编码
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final String srcPath, final Charset charset) throws UtilException {
		return zip(FileUtil.file(srcPath), charset);
	}

	/**
	 * 压缩文件或目录为同目录下的 .zip 文件.
	 *
	 * @param srcFile 源文件
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File srcFile) throws UtilException {
		return zip(srcFile, DEFAULT_CHARSET);
	}

	/**
	 * 压缩文件或目录为同目录下的 .zip 文件.
	 *
	 * @param srcFile 源文件
	 * @param charset 编码
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File srcFile, final Charset charset) throws UtilException {
		final File zipFile = FileUtil.file(srcFile.getParentFile(), FileUtil.mainName(srcFile) + ".zip");
		zip(zipFile, charset, false, srcFile); return zipFile;
	}

	/**
	 * 压缩文件或目录到指定路径.
	 *
	 * @param srcPath 源文件路径
	 * @param zipPath 目标zip文件路径
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final String srcPath, final String zipPath) throws UtilException {
		return zip(srcPath, zipPath, false);
	}

	/**
	 * 压缩文件或目录到指定路径.
	 *
	 * @param srcPath    源文件路径
	 * @param zipPath    目标zip文件路径
	 * @param withSrcDir 是否包含源目录自身
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final String srcPath, final String zipPath, final boolean withSrcDir) throws UtilException {
		return zip(srcPath, zipPath, DEFAULT_CHARSET, withSrcDir);
	}

	/**
	 * 压缩文件或目录到指定路径.
	 *
	 * @param srcPath    源文件路径
	 * @param zipPath    目标zip文件路径
	 * @param charset    编码
	 * @param withSrcDir 是否包含源目录自身
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final String srcPath, final String zipPath, final Charset charset, final boolean withSrcDir) throws UtilException {
		final File srcFile = FileUtil.file(srcPath); final File zipFile = FileUtil.file(zipPath);
		zip(zipFile, charset, withSrcDir, srcFile); return zipFile;
	}

	/**
	 * 压缩文件或目录.
	 *
	 * @param zipFile    目标zip文件
	 * @param withSrcDir 是否包含源目录自身
	 * @param srcFiles   源文件列表
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final boolean withSrcDir, final File... srcFiles) throws UtilException {
		return zip(zipFile, DEFAULT_CHARSET, withSrcDir, srcFiles);
	}

	/**
	 * 压缩文件或目录.
	 *
	 * @param zipFile    目标zip文件
	 * @param charset    编码
	 * @param withSrcDir 是否包含源目录自身
	 * @param srcFiles   源文件列表
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final Charset charset, final boolean withSrcDir, final File... srcFiles) throws UtilException {
		return zip(zipFile, charset, withSrcDir, null, srcFiles);
	}

	/**
	 * 压缩文件或目录.
	 *
	 * @param zipFile    目标zip文件
	 * @param charset    编码
	 * @param withSrcDir 是否包含源目录自身
	 * @param filter     文件过滤器
	 * @param srcFiles   源文件列表
	 * @return 生成的zip文件
	 * @throws IORuntimeException IO异常时抛出
	 */
	public static File zip(final File zipFile, final Charset charset, final boolean withSrcDir, final FileFilter filter, final File... srcFiles) throws IORuntimeException {
		validateFiles(zipFile, srcFiles); ZipWriter.of(zipFile, charset).add(withSrcDir, filter, srcFiles).close();
		return zipFile;
	}

	/**
	 * 压缩文件或目录到输出流.
	 *
	 * @param out        输出流
	 * @param charset    编码
	 * @param withSrcDir 是否包含源目录自身
	 * @param filter     文件过滤器
	 * @param srcFiles   源文件列表
	 * @throws IORuntimeException IO异常时抛出
	 */
	public static void zip(final OutputStream out, final Charset charset, final boolean withSrcDir, final FileFilter filter, final File... srcFiles) throws IORuntimeException {
		ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
	}

	/**
	 * 压缩文件或目录到 {@link ZipOutputStream}.
	 *
	 * @param zipOutputStream {@link ZipOutputStream}
	 * @param withSrcDir      是否包含源目录自身
	 * @param filter          文件过滤器
	 * @param srcFiles        源文件列表
	 * @throws IORuntimeException IO异常时抛出
	 * @deprecated 请使用 zip(OutputStream, Charset, ...) 替代.
	 */
	@Deprecated
	public static void zip(final ZipOutputStream zipOutputStream, final boolean withSrcDir, final FileFilter filter, final File... srcFiles) throws IORuntimeException {
		try (ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(withSrcDir, filter, srcFiles);
		}
	}

	/**
	 * 压缩字符串数据到指定路径.
	 *
	 * @param zipFile 目标zip文件
	 * @param path    zip中文件路径
	 * @param data    数据
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String path, final String data) throws UtilException {
		return zip(zipFile, path, data, DEFAULT_CHARSET);
	}

	/**
	 * 压缩字符串数据到指定路径.
	 *
	 * @param zipFile 目标zip文件
	 * @param path    zip中文件路径
	 * @param data    数据
	 * @param charset 编码
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String path, final String data, final Charset charset) throws UtilException {
		return zip(zipFile, path, IoUtil.toStream(data, charset), charset);
	}

	/**
	 * 压缩输入流到指定路径.
	 *
	 * @param zipFile 目标zip文件
	 * @param path    zip中文件路径
	 * @param in      输入流
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String path, final InputStream in) throws UtilException {
		return zip(zipFile, path, in, DEFAULT_CHARSET);
	}

	/**
	 * 压缩输入流到指定路径.
	 *
	 * @param zipFile 目标zip文件
	 * @param path    zip中文件路径
	 * @param in      输入流
	 * @param charset 编码
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String path, final InputStream in, final Charset charset) throws UtilException {
		return zip(zipFile, new String[]{path}, new InputStream[]{in}, charset);
	}

	/**
	 * 压缩多个输入流到zip文件.
	 *
	 * @param zipFile 目标zip文件
	 * @param paths   zip中路径数组
	 * @param ins     输入流数组
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String[] paths, final InputStream[] ins) throws UtilException {
		return zip(zipFile, paths, ins, DEFAULT_CHARSET);
	}

	/**
	 * 压缩多个输入流到zip文件.
	 *
	 * @param zipFile 目标zip文件
	 * @param paths   zip中路径数组
	 * @param ins     输入流数组
	 * @param charset 编码
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final String[] paths, final InputStream[] ins, final Charset charset) throws UtilException {
		try (ZipWriter zipWriter = ZipWriter.of(zipFile, charset)) {
			zipWriter.add(paths, ins);
		} return zipFile;
	}

	/**
	 * 压缩多个输入流到输出流.
	 *
	 * @param out   输出流
	 * @param paths zip中路径数组
	 * @param ins   输入流数组
	 */
	public static void zip(final OutputStream out, final String[] paths, final InputStream[] ins) {
		zip(getZipOutputStream(out, DEFAULT_CHARSET), paths, ins);
	}

	/**
	 * 压缩多个输入流到 {@link ZipOutputStream}.
	 *
	 * @param zipOutputStream {@link ZipOutputStream}
	 * @param paths           zip中路径数组
	 * @param ins             输入流数组
	 * @throws IORuntimeException IO异常时抛出
	 */
	public static void zip(final ZipOutputStream zipOutputStream, final String[] paths, final InputStream[] ins) throws IORuntimeException {
		try (ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(paths, ins);
		}
	}

	/**
	 * 压缩 {@link Resource} 资源到zip文件.
	 *
	 * @param zipFile   目标zip文件
	 * @param charset   编码
	 * @param resources 资源数组
	 * @return 生成的zip文件
	 * @throws UtilException IO异常时抛出
	 */
	public static File zip(final File zipFile, final Charset charset, final Resource... resources) throws UtilException {
		ZipWriter.of(zipFile, charset).add(resources).close(); return zipFile;
	}

	// ------------------------------------------------------------------- Unzip

	/**
	 * 解压zip文件到同目录.
	 *
	 * @param zipFilePath zip文件路径
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final String zipFilePath) throws UtilException {
		return unzip(zipFilePath, DEFAULT_CHARSET);
	}

	/**
	 * 解压zip文件到同目录.
	 *
	 * @param zipFilePath zip文件路径
	 * @param charset     编码
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final String zipFilePath, final Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), charset);
	}

	/**
	 * 解压zip文件到同目录.
	 *
	 * @param zipFile zip文件
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final File zipFile) throws UtilException {
		return unzip(zipFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压zip文件到同目录.
	 *
	 * @param zipFile zip文件
	 * @param charset 编码
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final File zipFile, final Charset charset) throws UtilException {
		final File destDir = FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile));
		return unzip(zipFile, destDir, charset);
	}

	/**
	 * 解压zip文件到指定目录.
	 *
	 * @param zipFilePath zip文件路径
	 * @param outFileDir  输出目录路径
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final String zipFilePath, final String outFileDir) throws UtilException {
		return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
	}

	/**
	 * 解压zip文件到指定目录.
	 *
	 * @param zipFilePath zip文件路径
	 * @param outFileDir  输出目录路径
	 * @param charset     编码
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final String zipFilePath, final String outFileDir, final Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
	}

	/**
	 * 解压zip文件到指定目录.
	 *
	 * @param zipFile zip文件
	 * @param outFile 输出目录
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final File zipFile, final File outFile) throws UtilException {
		return unzip(zipFile, outFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压zip文件到指定目录.
	 *
	 * @param zipFile zip文件
	 * @param outFile 输出目录
	 * @param charset 编码
	 * @return 解压后的目录
	 */
	public static File unzip(final File zipFile, final File outFile, final Charset charset) {
		return unzip(toZipFile(zipFile, charset), outFile);
	}

	/**
	 * 解压 {@link ZipFile} 到指定目录.
	 *
	 * @param zipFile {@link ZipFile}
	 * @param outFile 输出目录
	 * @return 解压后的目录
	 * @throws IORuntimeException IO异常时抛出
	 */
	public static File unzip(final ZipFile zipFile, final File outFile) throws IORuntimeException {
		return unzip(zipFile, outFile, -1);
	}

	/**
	 * 解压 {@link ZipFile} 到指定目录，支持文件大小限制.
	 *
	 * @param zipFile {@link ZipFile}
	 * @param outFile 输出目录
	 * @param limit   文件大小限制（字节），-1 表示不限制
	 * @return 解压后的目录
	 * @throws IORuntimeException IO异常时抛出
	 */
	public static File unzip(final ZipFile zipFile, final File outFile, final long limit) throws IORuntimeException {
		if (outFile.exists() && outFile.isFile()) {
			throw new IllegalArgumentException(StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
		} if (limit > 0) {
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries(); long zipFileSize = 0L;
			while (zipEntries.hasMoreElements()) {
				final ZipEntry zipEntry = zipEntries.nextElement(); zipFileSize += zipEntry.getSize();
				if (zipFileSize > limit) {
					throw new IllegalArgumentException("The file size exceeds the limit");
				}
			}
		} try (ZipReader reader = new ZipReader(zipFile)) {
			reader.readTo(outFile);
		} return outFile;
	}

	/**
	 * 从zip文件中读取指定路径的流.
	 *
	 * @param zipFile zip文件
	 * @param charset 编码
	 * @param path    zip中文件路径
	 * @return 输入流，未找到返回 null
	 */
	public static InputStream get(final File zipFile, final Charset charset, final String path) {
		return get(toZipFile(zipFile, charset), path);
	}

	/**
	 * 从 {@link ZipFile} 中读取指定路径的流.
	 *
	 * @param zipFile {@link ZipFile}
	 * @param path    zip中文件路径
	 * @return 输入流，未找到返回 null
	 */
	public static InputStream get(final ZipFile zipFile, final String path) {
		final ZipEntry entry = zipFile.getEntry(path); if (null != entry) {
			return getStream(zipFile, entry);
		} return null;
	}

	/**
	 * 读取并处理 {@link ZipFile} 中的每个条目.
	 *
	 * @param zipFile  {@link ZipFile}
	 * @param consumer 消费者函数
	 */
	public static void read(final ZipFile zipFile, final Consumer<ZipEntry> consumer) {
		try (ZipReader reader = new ZipReader(zipFile)) {
			reader.read(consumer);
		}
	}

	/**
	 * 从输入流解压.
	 *
	 * @param in      输入流
	 * @param outFile 输出目录
	 * @param charset 编码
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final InputStream in, final File outFile, final Charset charset) throws UtilException {
		Charset actualCharset = charset; if (null == actualCharset) {
			actualCharset = DEFAULT_CHARSET;
		} return unzip(new ZipInputStream(in, actualCharset), outFile);
	}

	/**
	 * 从 {@link ZipInputStream} 解压到指定目录.
	 *
	 * @param zipStream {@link ZipInputStream}
	 * @param outFile   输出目录
	 * @return 解压后的目录
	 * @throws UtilException IO异常时抛出
	 */
	public static File unzip(final ZipInputStream zipStream, final File outFile) throws UtilException {
		try (ZipReader reader = new ZipReader(zipStream)) {
			reader.readTo(outFile);
		} return outFile;
	}

	/**
	 * 读取并处理 {@link ZipInputStream} 中的每个条目.
	 *
	 * @param zipStream {@link ZipInputStream}
	 * @param consumer  消费者函数
	 */
	public static void read(final ZipInputStream zipStream, final Consumer<ZipEntry> consumer) {
		try (ZipReader reader = new ZipReader(zipStream)) {
			reader.read(consumer);
		}
	}

	/**
	 * 从zip文件中读取指定条目的字节内容.
	 *
	 * @param zipFilePath zip文件路径
	 * @param name        zip中条目名
	 * @return 字节内容
	 */
	public static byte[] unzipFileBytes(final String zipFilePath, final String name) {
		return unzipFileBytes(zipFilePath, DEFAULT_CHARSET, name);
	}

	/**
	 * 从zip文件中读取指定条目的字节内容.
	 *
	 * @param zipFilePath zip文件路径
	 * @param charset     编码
	 * @param name        zip中条目名
	 * @return 字节内容
	 */
	public static byte[] unzipFileBytes(final String zipFilePath, final Charset charset, final String name) {
		return unzipFileBytes(FileUtil.file(zipFilePath), charset, name);
	}

	/**
	 * 从zip文件中读取指定条目的字节内容.
	 *
	 * @param zipFile zip文件
	 * @param name    zip中条目名
	 * @return 字节内容
	 */
	public static byte[] unzipFileBytes(final File zipFile, final String name) {
		return unzipFileBytes(zipFile, DEFAULT_CHARSET, name);
	}

	/**
	 * 从zip文件中读取指定条目的字节内容.
	 *
	 * @param zipFile zip文件
	 * @param charset 编码
	 * @param name    zip中条目名
	 * @return 字节内容
	 */
	public static byte[] unzipFileBytes(final File zipFile, final Charset charset, final String name) {
		try (ZipReader reader = ZipReader.of(zipFile, charset)) {
			return IoUtil.readBytes(reader.get(name));
		}
	}

	// ------------------------------------------------------------------- Gzip

	/**
	 * Gzip 压缩字符串.
	 *
	 * @param content 字符串内容
	 * @param charset 编码
	 * @return 压缩后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] gzip(final String content, final String charset) throws UtilException {
		return gzip(StrUtil.bytes(content, charset));
	}

	/**
	 * Gzip 压缩字节数组.
	 *
	 * @param buf 字节数组
	 * @return 压缩后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] gzip(final byte[] buf) throws UtilException {
		return gzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * Gzip 压缩文件.
	 *
	 * @param file 文件
	 * @return 压缩后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] gzip(final File file) throws UtilException {
		InputStream in = null; try {
			in = FileUtil.getInputStream(file); return gzip(in, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * Gzip 压缩输入流.
	 *
	 * @param in 输入流
	 * @return 压缩后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] gzip(final InputStream in) throws UtilException {
		return gzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Gzip 压缩输入流.
	 *
	 * @param in     输入流
	 * @param length 预估长度
	 * @return 压缩后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] gzip(final InputStream in, final int length) throws UtilException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(length); Gzip.of(in, bos).gzip().close();
		return bos.toByteArray();
	}

	/**
	 * 解 Gzip 压缩，返回字符串.
	 *
	 * @param buf     压缩的字节数组
	 * @param charset 编码
	 * @return 解压后的字符串
	 * @throws UtilException IO异常时抛出
	 */
	public static String unGzip(final byte[] buf, final String charset) throws UtilException {
		return StrUtil.str(unGzip(buf), Charset.forName(charset));
	}

	/**
	 * 解 Gzip 压缩字节数组.
	 *
	 * @param buf 压缩的字节数组
	 * @return 解压后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] unGzip(final byte[] buf) throws UtilException {
		return unGzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * 解 Gzip 压缩输入流.
	 *
	 * @param in 输入流
	 * @return 解压后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] unGzip(final InputStream in) throws UtilException {
		return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * 解 Gzip 压缩输入流.
	 *
	 * @param in     输入流
	 * @param length 预估长度
	 * @return 解压后的字节数组
	 * @throws UtilException IO异常时抛出
	 */
	public static byte[] unGzip(final InputStream in, final int length) throws UtilException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(length); Gzip.of(in, bos).unGzip().close();
		return bos.toByteArray();
	}

	// ------------------------------------------------------------------- Zlib

	/**
	 * Zlib 压缩字符串.
	 *
	 * @param content 字符串内容
	 * @param charset 编码
	 * @param level   压缩级别
	 * @return 压缩后的字节数组
	 */
	public static byte[] zlib(final String content, final String charset, final int level) {
		return zlib(StrUtil.bytes(content, charset), level);
	}

	/**
	 * Zlib 压缩文件.
	 *
	 * @param file  文件
	 * @param level 压缩级别
	 * @return 压缩后的字节数组
	 */
	public static byte[] zlib(final File file, final int level) {
		InputStream in = null; try {
			in = FileUtil.getInputStream(file); return zlib(in, level, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * Zlib 压缩字节数组.
	 *
	 * @param buf   字节数组
	 * @param level 压缩级别
	 * @return 压缩后的字节数组
	 */
	public static byte[] zlib(final byte[] buf, final int level) {
		return zlib(new ByteArrayInputStream(buf), level, buf.length);
	}

	/**
	 * Zlib 压缩输入流.
	 *
	 * @param in    输入流
	 * @param level 压缩级别
	 * @return 压缩后的字节数组
	 */
	public static byte[] zlib(final InputStream in, final int level) {
		return zlib(in, level, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Zlib 压缩输入流.
	 *
	 * @param in     输入流
	 * @param level  压缩级别
	 * @param length 预估长度
	 * @return 压缩后的字节数组
	 */
	public static byte[] zlib(final InputStream in, final int level, final int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).deflater(level).close(); return out.toByteArray();
	}

	/**
	 * 解 Zlib 压缩，返回字符串.
	 *
	 * @param buf     压缩的字节数组
	 * @param charset 编码
	 * @return 解压后的字符串
	 */
	public static String unZlib(final byte[] buf, final String charset) {
		return StrUtil.str(unZlib(buf), Charset.forName(charset));
	}

	/**
	 * 解 Zlib 压缩字节数组.
	 *
	 * @param buf 压缩的字节数组
	 * @return 解压后的字节数组
	 */
	public static byte[] unZlib(final byte[] buf) {
		return unZlib(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * 解 Zlib 压缩输入流.
	 *
	 * @param in 输入流
	 * @return 解压后的字节数组
	 */
	public static byte[] unZlib(final InputStream in) {
		return unZlib(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * 解 Zlib 压缩输入流.
	 *
	 * @param in     输入流
	 * @param length 预估长度
	 * @return 解压后的字节数组
	 */
	public static byte[] unZlib(final InputStream in, final int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).inflater().close(); return out.toByteArray();
	}

	/**
	 * 获取Zip文件中指定目录下的所有文件.
	 *
	 * @param zipFile {@link ZipFile}
	 * @param dir     目录前缀
	 * @return 文件列表
	 */
	public static List<String> listFileNames(final ZipFile zipFile, final String dir) {
		String dirStr = dir; if (StrUtil.isNotBlank(dirStr)) {
			dirStr = StrUtil.addSuffixIfNot(dirStr, StrUtil.SLASH);
		} final List<String> fileNames = new ArrayList<>(); String name;
		for (ZipEntry entry : new EnumerationIter<>(zipFile.entries())) {
			name = entry.getName(); if (StrUtil.isEmpty(dirStr) || name.startsWith(dirStr)) {
				final String nameSuffix = StrUtil.removePrefix(name, dirStr);
				if (StrUtil.isNotEmpty(nameSuffix) && !StrUtil.contains(nameSuffix, CharUtil.SLASH)) {
					fileNames.add(nameSuffix);
				}
			}
		} return fileNames;
	}

	// ------------------------------------------------------------ Private end

	/**
	 * 验证文件是否合法.
	 *
	 * @param zipFile  目标zip文件
	 * @param srcFiles 源文件列表
	 * @throws UtilException 文件不合法时抛出
	 */
	private static void validateFiles(final File zipFile, final File... srcFiles) throws UtilException {
		if (zipFile.isDirectory()) {
			throw new UtilException(StrUtil.format("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile()));
		} for (File srcFile : srcFiles) {
			if (null == srcFile) {
				continue;
			} if (!srcFile.exists()) {
				throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			} File parentFile; try {
				parentFile = zipFile.getCanonicalFile().getParentFile();
			} catch (IOException e) {
				parentFile = zipFile.getParentFile();
			} if (srcFile.isDirectory() && FileUtil.isSub(srcFile, parentFile)) {
				throw new UtilException(StrUtil.format("Zip file path [{}] must not be " + "the child directory of [{}] !", zipFile.getPath(), srcFile.getPath()));
			}
		}
	}
}
