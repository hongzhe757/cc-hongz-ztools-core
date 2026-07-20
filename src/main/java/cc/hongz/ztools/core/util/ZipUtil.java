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
import java.util.zip.*;

/**
 * 压缩工具类
 *
 * @author Looly
 * @see ZipWriter
 */
public class ZipUtil {

	private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

	/**
	 * 默认编码，使用平台相关编码
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

	/**
	 * 将Zip文件转换为{@link ZipFile}
	 *
	 * @param file    zip文件
	 * @param charset 解析zip文件的编码，null表示{@link CharsetUtil#CHARSET_UTF_8}
	 * @return {@link ZipFile}
	 */
	public static ZipFile toZipFile(File file, Charset charset) {
		try {
			return new ZipFile(file, ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8));
		} catch (IOException e) {
			if (e instanceof ZipException) {
				if (e.getMessage().contains("invalid CEN header")) {
					try {
						return new ZipFile(file, CharsetUtil.CHARSET_UTF_8.equals(charset) ? CharsetUtil.CHARSET_GBK : CharsetUtil.CHARSET_UTF_8);
					} catch (final IOException ex) {
						throw new IORuntimeException(ex);
					}
				}
			}
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取指定{@link ZipEntry}的流
	 *
	 * @param zipFile  {@link ZipFile}
	 * @param zipEntry {@link ZipEntry}
	 * @return 流
	 */
	public static InputStream getStream(ZipFile zipFile, ZipEntry zipEntry) {
		try {
			return new LimitedInputStream(zipFile.getInputStream(zipEntry), zipEntry.getSize());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得 {@link ZipOutputStream}
	 *
	 * @param out     压缩文件流
	 * @param charset 编码
	 * @return {@link ZipOutputStream}
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out, Charset charset) {
		if (out instanceof ZipOutputStream) {
			return (ZipOutputStream) out;
		}
		return new ZipOutputStream(out, charset);
	}

	/**
	 * 在zip文件中添加新文件或目录
	 *
	 * @param zipPath        zip文件的Path
	 * @param appendFilePath 待添加文件Path
	 * @param options        拷贝选项
	 * @throws IORuntimeException IO异常
	 */
	public static void append(Path zipPath, Path appendFilePath, CopyOption... options) throws IORuntimeException {
		try (FileSystem zipFileSystem = FileSystemUtil.createZip(zipPath.toString())) {
			if (Files.isDirectory(appendFilePath)) {
				Path source = appendFilePath.getParent();
				if (null == source) {
					source = appendFilePath;
				}
				Files.walkFileTree(appendFilePath, new ZipCopyVisitor(source, zipFileSystem, options));
			} else {
				Files.copy(appendFilePath, zipFileSystem.getPath(PathUtil.getName(appendFilePath)), options);
			}
		} catch (FileAlreadyExistsException ignored) {
			// 不覆盖情况下，文件已存在
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------- Zip

	public static File zip(String srcPath) throws UtilException {
		return zip(srcPath, DEFAULT_CHARSET);
	}

	public static File zip(String srcPath, Charset charset) throws UtilException {
		return zip(FileUtil.file(srcPath), charset);
	}

	public static File zip(File srcFile) throws UtilException {
		return zip(srcFile, DEFAULT_CHARSET);
	}

	public static File zip(File srcFile, Charset charset) throws UtilException {
		final File zipFile = FileUtil.file(srcFile.getParentFile(), FileUtil.mainName(srcFile) + ".zip");
		zip(zipFile, charset, false, srcFile);
		return zipFile;
	}

	public static File zip(String srcPath, String zipPath) throws UtilException {
		return zip(srcPath, zipPath, false);
	}

	public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws UtilException {
		return zip(srcPath, zipPath, DEFAULT_CHARSET, withSrcDir);
	}

	public static File zip(String srcPath, String zipPath, Charset charset, boolean withSrcDir) throws UtilException {
		final File srcFile = FileUtil.file(srcPath);
		final File zipFile = FileUtil.file(zipPath);
		zip(zipFile, charset, withSrcDir, srcFile);
		return zipFile;
	}

	public static File zip(File zipFile, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, DEFAULT_CHARSET, withSrcDir, srcFiles);
	}

	public static File zip(File zipFile, Charset charset, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, charset, withSrcDir, null, srcFiles);
	}

	public static File zip(File zipFile, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		validateFiles(zipFile, srcFiles);
		ZipWriter.of(zipFile, charset).add(withSrcDir, filter, srcFiles).close();
		return zipFile;
	}

	public static void zip(OutputStream out, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
	}

	@Deprecated
	public static void zip(ZipOutputStream zipOutputStream, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		try (final ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(withSrcDir, filter, srcFiles);
		}
	}

	public static File zip(File zipFile, String path, String data) throws UtilException {
		return zip(zipFile, path, data, DEFAULT_CHARSET);
	}

	public static File zip(File zipFile, String path, String data, Charset charset) throws UtilException {
		return zip(zipFile, path, IoUtil.toStream(data, charset), charset);
	}

	public static File zip(File zipFile, String path, InputStream in) throws UtilException {
		return zip(zipFile, path, in, DEFAULT_CHARSET);
	}

	public static File zip(File zipFile, String path, InputStream in, Charset charset) throws UtilException {
		return zip(zipFile, new String[]{path}, new InputStream[]{in}, charset);
	}

	public static File zip(File zipFile, String[] paths, InputStream[] ins) throws UtilException {
		return zip(zipFile, paths, ins, DEFAULT_CHARSET);
	}

	public static File zip(File zipFile, String[] paths, InputStream[] ins, Charset charset) throws UtilException {
		try (final ZipWriter zipWriter = ZipWriter.of(zipFile, charset)) {
			zipWriter.add(paths, ins);
		}
		return zipFile;
	}

	public static void zip(OutputStream out, String[] paths, InputStream[] ins) {
		zip(getZipOutputStream(out, DEFAULT_CHARSET), paths, ins);
	}

	public static void zip(ZipOutputStream zipOutputStream, String[] paths, InputStream[] ins) throws IORuntimeException {
		try (final ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(paths, ins);
		}
	}

	public static File zip(File zipFile, Charset charset, Resource... resources) throws UtilException {
		ZipWriter.of(zipFile, charset).add(resources).close();
		return zipFile;
	}

	// ---------------------------------------------------------------------------------------------- Unzip

	public static File unzip(String zipFilePath) throws UtilException {
		return unzip(zipFilePath, DEFAULT_CHARSET);
	}

	public static File unzip(String zipFilePath, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), charset);
	}

	public static File unzip(File zipFile) throws UtilException {
		return unzip(zipFile, DEFAULT_CHARSET);
	}

	public static File unzip(File zipFile, Charset charset) throws UtilException {
		final File destDir = FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile));
		return unzip(zipFile, destDir, charset);
	}

	public static File unzip(String zipFilePath, String outFileDir) throws UtilException {
		return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
	}

	public static File unzip(String zipFilePath, String outFileDir, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
	}

	public static File unzip(File zipFile, File outFile) throws UtilException {
		return unzip(zipFile, outFile, DEFAULT_CHARSET);
	}

	public static File unzip(File zipFile, File outFile, Charset charset) {
		return unzip(toZipFile(zipFile, charset), outFile);
	}

	public static File unzip(ZipFile zipFile, File outFile) throws IORuntimeException {
		return unzip(zipFile, outFile, -1);
	}

	public static File unzip(ZipFile zipFile, File outFile, long limit) throws IORuntimeException {
		if (outFile.exists() && outFile.isFile()) {
			throw new IllegalArgumentException(
					StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
		}
		if (limit > 0) {
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			long zipFileSize = 0L;
			while (zipEntries.hasMoreElements()) {
				final ZipEntry zipEntry = zipEntries.nextElement();
				zipFileSize += zipEntry.getSize();
				if (zipFileSize > limit) {
					throw new IllegalArgumentException("The file size exceeds the limit");
				}
			}
		}
		try (final ZipReader reader = new ZipReader(zipFile)) {
			reader.readTo(outFile);
		}
		return outFile;
	}

	public static InputStream get(File zipFile, Charset charset, String path) {
		return get(toZipFile(zipFile, charset), path);
	}

	public static InputStream get(ZipFile zipFile, String path) {
		final ZipEntry entry = zipFile.getEntry(path);
		if (null != entry) {
			return getStream(zipFile, entry);
		}
		return null;
	}

	public static void read(ZipFile zipFile, Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipFile)) {
			reader.read(consumer);
		}
	}

	public static File unzip(InputStream in, File outFile, Charset charset) throws UtilException {
		if (null == charset) {
			charset = DEFAULT_CHARSET;
		}
		return unzip(new ZipInputStream(in, charset), outFile);
	}

	public static File unzip(ZipInputStream zipStream, File outFile) throws UtilException {
		try (final ZipReader reader = new ZipReader(zipStream)) {
			reader.readTo(outFile);
		}
		return outFile;
	}

	public static void read(ZipInputStream zipStream, Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipStream)) {
			reader.read(consumer);
		}
	}

	public static byte[] unzipFileBytes(String zipFilePath, String name) {
		return unzipFileBytes(zipFilePath, DEFAULT_CHARSET, name);
	}

	public static byte[] unzipFileBytes(String zipFilePath, Charset charset, String name) {
		return unzipFileBytes(FileUtil.file(zipFilePath), charset, name);
	}

	public static byte[] unzipFileBytes(File zipFile, String name) {
		return unzipFileBytes(zipFile, DEFAULT_CHARSET, name);
	}

	public static byte[] unzipFileBytes(File zipFile, Charset charset, String name) {
		try (final ZipReader reader = ZipReader.of(zipFile, charset)) {
			return IoUtil.readBytes(reader.get(name));
		}
	}

	// ----------------------------------------------------------------------------- Gzip

	public static byte[] gzip(String content, String charset) throws UtilException {
		return gzip(StrUtil.bytes(content, charset));
	}

	public static byte[] gzip(byte[] buf) throws UtilException {
		return gzip(new ByteArrayInputStream(buf), buf.length);
	}

	public static byte[] gzip(File file) throws UtilException {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return gzip(in, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	public static byte[] gzip(InputStream in) throws UtilException {
		return gzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	public static byte[] gzip(InputStream in, int length) throws UtilException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
		Gzip.of(in, bos).gzip().close();
		return bos.toByteArray();
	}

	public static String unGzip(byte[] buf, String charset) throws UtilException {
		return StrUtil.str(unGzip(buf), Charset.forName(charset));
	}

	public static byte[] unGzip(byte[] buf) throws UtilException {
		return unGzip(new ByteArrayInputStream(buf), buf.length);
	}

	public static byte[] unGzip(InputStream in) throws UtilException {
		return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	public static byte[] unGzip(InputStream in, int length) throws UtilException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(length);
		Gzip.of(in, bos).unGzip().close();
		return bos.toByteArray();
	}

	// ----------------------------------------------------------------------------- Zlib

	public static byte[] zlib(String content, String charset, int level) {
		return zlib(StrUtil.bytes(content, charset), level);
	}

	public static byte[] zlib(File file, int level) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return zlib(in, level, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	public static byte[] zlib(byte[] buf, int level) {
		return zlib(new ByteArrayInputStream(buf), level, buf.length);
	}

	public static byte[] zlib(InputStream in, int level) {
		return zlib(in, level, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	public static byte[] zlib(InputStream in, int level, int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).deflater(level).close();
		return out.toByteArray();
	}

	public static String unZlib(byte[] buf, String charset) {
		return StrUtil.str(unZlib(buf), Charset.forName(charset));
	}

	public static byte[] unZlib(byte[] buf) {
		return unZlib(new ByteArrayInputStream(buf), buf.length);
	}

	public static byte[] unZlib(InputStream in) {
		return unZlib(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	public static byte[] unZlib(InputStream in, int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).inflater().close();
		return out.toByteArray();
	}

	/**
	 * 获取Zip文件中指定目录下的所有文件
	 *
	 * @param zipFile Zip文件
	 * @param dir     目录前缀
	 * @return 文件列表
	 */
	public static List<String> listFileNames(ZipFile zipFile, String dir) {
		if (StrUtil.isNotBlank(dir)) {
			dir = StrUtil.addSuffixIfNot(dir, StrUtil.SLASH);
		}
		final List<String> fileNames = new ArrayList<>();
		String name;
		for (ZipEntry entry : new EnumerationIter<>(zipFile.entries())) {
			name = entry.getName();
			if (StrUtil.isEmpty(dir) || name.startsWith(dir)) {
				final String nameSuffix = StrUtil.removePrefix(name, dir);
				if (StrUtil.isNotEmpty(nameSuffix) && !StrUtil.contains(nameSuffix, CharUtil.SLASH)) {
					fileNames.add(nameSuffix);
				}
			}
		}
		return fileNames;
	}

	// ---------------------------------------------------------------------------------------------- Private method start

	private static void validateFiles(File zipFile, File... srcFiles) throws UtilException {
		if (zipFile.isDirectory()) {
			throw new UtilException(StrUtil.format("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile()));
		}
		for (File srcFile : srcFiles) {
			if (null == srcFile) {
				continue;
			}
			if (!srcFile.exists()) {
				throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			}
			File parentFile;
			try {
				parentFile = zipFile.getCanonicalFile().getParentFile();
			} catch (IOException e) {
				parentFile = zipFile.getParentFile();
			}
			if (srcFile.isDirectory() && FileUtil.isSub(srcFile, parentFile)) {
				throw new UtilException(StrUtil.format("Zip file path [{}] must not be the child directory of [{}] !", zipFile.getPath(), srcFile.getPath()));
			}
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end
}
