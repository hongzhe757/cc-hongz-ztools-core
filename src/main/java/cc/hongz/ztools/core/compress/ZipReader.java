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
 * Zip文件或流读取器，一般用于Zip文件解压
 *
 * @author looly
 * @since 5.7.8
 */
public class ZipReader implements Closeable {

	private static final int DEFAULT_MAX_SIZE_DIFF = 100;

	private ZipFile zipFile;
	private ZipInputStream in;
	private int maxSizeDiff = DEFAULT_MAX_SIZE_DIFF;

	public static ZipReader of(File zipFile, Charset charset) {
		return new ZipReader(zipFile, charset);
	}

	public static ZipReader of(InputStream in, Charset charset) {
		return new ZipReader(in, charset);
	}

	public ZipReader(File zipFile, Charset charset) {
		this.zipFile = ZipUtil.toZipFile(zipFile, charset);
	}

	public ZipReader(ZipFile zipFile) {
		this.zipFile = zipFile;
	}

	public ZipReader(InputStream in, Charset charset) {
		this.in = new ZipInputStream(in, charset);
	}

	public ZipReader(ZipInputStream zin) {
		this.in = zin;
	}

	public ZipReader setMaxSizeDiff(final int maxSizeDiff) {
		this.maxSizeDiff = maxSizeDiff;
		return this;
	}

	public InputStream get(String path) {
		if (null != this.zipFile) {
			final ZipFile zipFile = this.zipFile;
			final ZipEntry entry = zipFile.getEntry(path);
			if (null != entry) {
				return ZipUtil.getStream(zipFile, entry);
			}
		} else {
			try {
				ZipEntry zipEntry;
				while (null != (zipEntry = in.getNextEntry())) {
					if (zipEntry.getName().equals(path)) {
						return this.in;
					}
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return null;
	}

	public File readTo(File outFile) throws IORuntimeException {
		return readTo(outFile, null);
	}

	public File readTo(File outFile, Filter<ZipEntry> entryFilter) throws IORuntimeException {
		read((zipEntry) -> {
			if (null == entryFilter || entryFilter.accept(zipEntry)) {
				String path = zipEntry.getName();
				if (FileUtil.isWindows()) {
					path = StrUtil.replace(path, "*", "_");
				}
				final File outItemFile = FileUtil.file(outFile, path);
				if (zipEntry.isDirectory()) {
					outItemFile.mkdirs();
				} else {
					InputStream inputStream;
					if (null != this.zipFile) {
						inputStream = ZipUtil.getStream(this.zipFile, zipEntry);
					} else {
						inputStream = this.in;
					}
					FileUtil.writeFromStream(inputStream, outItemFile, false);
				}
			}
		});
		return outFile;
	}

	public ZipReader read(Consumer<ZipEntry> consumer) throws IORuntimeException {
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

	private void readFromZipFile(Consumer<ZipEntry> consumer) {
		final Enumeration<? extends ZipEntry> em = zipFile.entries();
		while (em.hasMoreElements()) {
			consumer.accept(checkZipBomb(em.nextElement()));
		}
	}

	private void readFromStream(Consumer<ZipEntry> consumer) throws IORuntimeException {
		try {
			ZipEntry zipEntry;
			while (null != (zipEntry = in.getNextEntry())) {
				consumer.accept(zipEntry);
				checkZipBomb(zipEntry);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	private ZipEntry checkZipBomb(ZipEntry entry) {
		if (null == entry) {
			return null;
		}
		if (maxSizeDiff < 0 || entry.isDirectory()) {
			return entry;
		}
		final long compressedSize = entry.getCompressedSize();
		final long uncompressedSize = entry.getSize();
		if (compressedSize < 0 || uncompressedSize < 0 ||
				compressedSize * maxSizeDiff < uncompressedSize) {
			throw new UtilException("Zip bomb attack detected, invalid sizes: compressed {}, uncompressed {}, name {}",
					compressedSize, uncompressedSize, entry.getName());
		}
		return entry;
	}
}
