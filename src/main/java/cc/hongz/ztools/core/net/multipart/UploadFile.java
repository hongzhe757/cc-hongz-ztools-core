package cc.hongz.ztools.core.net.multipart;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

/**
 * 上传的文件对象.
 *
 * @author looly
 * @since 5.7.0
 */
public class UploadFile {

    /**
     * 临时文件前缀.
     */
    private static final String TMP_FILE_PREFIX = "hutool-";
    /**
     * 临时文件后缀.
     */
    private static final String TMP_FILE_SUFFIX = ".upload.tmp";

    /**
     * 上传文件头部信息.
     */
    private final UploadFileHeader header;
    /**
     * 上传设置.
     */
    private final UploadSetting setting;

    /**
     * 文件大小，-1 表示未上传.
     */
    private long size = -1;

    /**
     * 文件数据（小文件位于内存中）.
     */
    private byte[] data;
    /**
     * 临时文件（大文件位于临时文件夹中）.
     */
    private File tempFile;

    /**
     * 构造.
     *
     * @param header  头部信息
     * @param setting 上传设置
     */
    public UploadFile(final UploadFileHeader header,
            final UploadSetting setting) {
        this.header = header;
        this.setting = setting;
    }

    /**
     * 从磁盘或者内存中删除这个文件.
     */
    public void delete() {
        if (tempFile != null) {
            tempFile.delete();
        }
        if (data != null) {
            data = null;
        }
    }

    /**
     * 将上传的文件写入指定的目标文件路径，自动创建文件.<br>
     * 写入后原临时文件会被删除.
     *
     * @param destPath 目标文件路径
     * @return 目标文件
     * @throws IOException IO异常
     */
    public File write(final String destPath) throws IOException {
        if (data != null || tempFile != null) {
            return write(FileUtil.file(destPath));
        }
        return null;
    }

    /**
     * 将上传的文件写入目标文件.<br>
     * 写入后原临时文件会被删除.
     *
     * @param destination 目标文件
     * @return 目标文件
     * @throws IOException IO异常
     */
    public File write(final File destination) throws IOException {
        assertValid();

        if (destination.isDirectory()) {
            return write(new File(destination, this.header.getFileName()));
        }
        if (data != null) {
            FileUtil.writeBytes(data, destination);
            data = null;
        } else {
            if (null == this.tempFile) {
                throw new NullPointerException("Temp file is null !");
            }
            if (false == this.tempFile.exists()) {
                throw new NoSuchFileException("Temp file: ["
                        + this.tempFile.getAbsolutePath() + "] not exist!");
            }
            FileUtil.move(tempFile, destination, true);
        }
        return destination;
    }

    /**
     * 获得文件字节流.
     *
     * @return 文件字节流
     * @throws IOException IO异常
     */
    public byte[] getFileContent() throws IOException {
        assertValid();

        if (data != null) {
            return data;
        }
        if (tempFile != null) {
            return FileUtil.readBytes(tempFile);
        }
        return null;
    }

    /**
     * 获得文件流.
     *
     * @return 文件流
     * @throws IOException IO异常
     */
    public InputStream getFileInputStream() throws IOException {
        assertValid();

        if (data != null) {
            return IoUtil.toBuffered(IoUtil.toStream(this.data));
        }
        if (tempFile != null) {
            return IoUtil.toBuffered(IoUtil.toStream(this.tempFile));
        }
        return null;
    }

    /**
     * 获得上传文件头部信息.
     *
     * @return 上传文件头部信息
     */
    public UploadFileHeader getHeader() {
        return header;
    }

    /**
     * 获得文件名.
     *
     * @return 文件名
     */
    public String getFileName() {
        return header == null ? null : header.getFileName();
    }

    /**
     * 获得上传文件的大小，-1 表示未上传.
     *
     * @return 文件大小
     */
    public long size() {
        return size;
    }

    /**
     * 是否上传成功.
     *
     * @return 是否上传成功
     */
    public boolean isUploaded() {
        return size > 0;
    }

    /**
     * 文件是否在内存中.
     *
     * @return 是否在内存中
     */
    public boolean isInMemory() {
        return data != null;
    }

    /**
     * 处理上传表单流，提取出文件.
     *
     * @param input 上传表单的流
     * @return 是否成功
     * @throws IOException IO异常
     */
    protected boolean processStream(final MultipartRequestInputStream input)
            throws IOException {
        if (!isAllowedExtension()) {
            size = input.skipToBoundary();
            return false;
        }
        size = 0;

        final int memoryThreshold = setting.memoryThreshold;
        if (memoryThreshold > 0) {
            final ByteArrayOutputStream baos =
                    new ByteArrayOutputStream(memoryThreshold);
            final long written = input.copy(baos, memoryThreshold);
            data = baos.toByteArray();
            if (written <= memoryThreshold) {
                size = data.length;
                return true;
            }
        }

        tempFile = FileUtil.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX,
                FileUtil.touch(setting.tmpUploadPath), false);
        final BufferedOutputStream out =
                FileUtil.getOutputStream(this.tempFile);
        if (data != null) {
            size = data.length;
            out.write(data);
            data = null;
        }
        final long maxFileSize = setting.maxFileSize;
        try {
            if (maxFileSize == -1) {
                size += input.copy(out);
                return true;
            }
            size += input.copy(out, maxFileSize - size + 1);
            if (size > maxFileSize) {
                tempFile.delete();
                tempFile = null;
                input.skipToBoundary();
                return false;
            }
        } finally {
            IoUtil.close(out);
        }
        return true;
    }

    /**
     * 是否为允许的扩展名.
     *
     * @return 是否为允许的扩展名
     */
    private boolean isAllowedExtension() {
        final String[] exts = setting.fileExts;
        final boolean isAllow = setting.isAllowFileExts;
        if (exts == null || exts.length == 0) {
            return isAllow;
        }

        final String fileNameExt = FileUtil.extName(this.getFileName());
        for (final String fileExtension : setting.fileExts) {
            if (fileNameExt.equalsIgnoreCase(fileExtension)) {
                return isAllow;
            }
        }
        return !isAllow;
    }

    /**
     * 断言是否文件流可用.
     *
     * @throws IOException IO异常
     */
    private void assertValid() throws IOException {
        if (false == isUploaded()) {
            throw new IOException(StrUtil.format(
                    "File [{}] upload fail", getFileName()));
        }
    }
}
