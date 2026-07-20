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
 * Zip生成封装
 *
 * @author looly
 * @since 5.7.8
 */
@SuppressWarnings("resource")
public class ZipWriter implements Closeable {

	public static ZipWriter of(File zipFile, Charset charset) {
		return new ZipWriter(zipFile, charset);
	}

	public static ZipWriter of(OutputStream out, Charset charset) {
		return new ZipWriter(out, charset);
	}

	private File zipFile;
	private final ZipOutputStream out;

	public ZipWriter(File zipFile, Charset charset) {
		this.zipFile = zipFile;
		this.out = getZipOutputStream(zipFile, charset);
	}

	public ZipWriter(OutputStream out, Charset charset) {
		this.out = ZipUtil.getZipOutputStream(out, charset);
	}

	public ZipWriter(ZipOutputStream out) {
		this.out = out;
	}

	public ZipWriter setLevel(int level) {
		this.out.setLevel(level);
		return this;
	}

	public ZipWriter setComment(String comment) {
		this.out.setComment(comment);
		return this;
	}

	public ZipOutputStream getOut() {
		return this.out;
	}

	public ZipWriter add(boolean withSrcDir, FileFilter filter, File... files) throws IORuntimeException {
		for (File file : files) {
			String srcRootDir;
			try {
				srcRootDir = file.getCanonicalPath();
				if ((!file.isDirectory()) || withSrcDir) {
					srcRootDir = file.getCanonicalFile().getParentFile().getCanonicalPath();
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
			_add(file, srcRootDir, filter);
		}
		return this;
	}

	public ZipWriter add(Resource... resources) throws IORuntimeException {
		for (Resource resource : resources) {
			if (null != resource) {
				add(resource.getName(), resource.getStream());
			}
		}
		return this;
	}

	public ZipWriter add(String path, InputStream in) throws IORuntimeException {
		path = StrUtil.nullToEmpty(path);
		if (null == in) {
			path = StrUtil.addSuffixIfNot(path, StrUtil.SLASH);
			if (StrUtil.isBlank(path)) {
				return this;
			}
		}
		return putEntry(path, in);
	}

	public ZipWriter add(String[] paths, InputStream[] ins) throws IORuntimeException {
		if (ArrayUtil.isEmpty(paths) || ArrayUtil.isEmpty(ins)) {
			throw new IllegalArgumentException("Paths or ins is empty !");
		}
		if (paths.length != ins.length) {
			throw new IllegalArgumentException("Paths length is not equals to ins length !");
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
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(this.out);
		}
	}

	private static ZipOutputStream getZipOutputStream(File zipFile, Charset charset) {
		return ZipUtil.getZipOutputStream(FileUtil.getOutputStream(zipFile), charset);
	}

	private ZipWriter _add(File file, String srcRootDir, FileFilter filter) throws IORuntimeException {
		if (null == file || (null != filter && !filter.accept(file))) {
			return this;
		}
		final String subPath = FileUtil.subPath(srcRootDir, file);
		if (file.isDirectory()) {
			final File[] files = file.listFiles();
			if (ArrayUtil.isEmpty(files)) {
				add(subPath, null);
			} else {
				for (File childFile : files) {
					_add(childFile, srcRootDir, filter);
				}
			}
		} else {
			if (null != this.zipFile && FileUtil.equals(file, zipFile)) {
				return this;
			}
			putEntry(subPath, FileUtil.getInputStream(file));
		}
		return this;
	}

	private ZipWriter putEntry(String path, InputStream in) throws IORuntimeException {
		try {
			out.putNextEntry(new ZipEntry(path));
			if (null != in) {
				IoUtil.copy(in, out);
			}
			out.closeEntry();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
		IoUtil.flush(this.out);
		return this;
	}
}
