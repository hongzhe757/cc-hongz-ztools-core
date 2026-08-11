package cc.hongz.ztools.core.net.multipart;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 上传的文件的头部信息.<br>
 * 来自Jodd.
 *
 * @author jodd.org
 * @since 5.7.0
 */
public class UploadFileHeader {

    /**
     * 表单字段名.
     */
    String formFieldName;

    /**
     * 表单中的文件名.
     */
    String formFileName;
    /**
     * 文件路径.
     */
    String path;
    /**
     * 文件名，不包括路径.
     */
    String fileName;

    /**
     * 是否为文件.
     */
    boolean isFile;
    /**
     * 内容类型.
     */
    String contentType;
    /**
     * MIME类型.
     */
    String mimeType;
    /**
     * MIME子类型.
     */
    String mimeSubtype;
    /**
     * 内容处置方式.
     */
    String contentDisposition;

    /**
     * 构造.
     *
     * @param dataHeader 数据头信息
     */
    UploadFileHeader(final String dataHeader) {
        processHeaderString(dataHeader);
    }

    /**
     * 是否为文件.<br>
     * 如果头信息中包含 'filename' 字符串则为true.
     *
     * @return 是否为文件
     */
    public boolean isFile() {
        return isFile;
    }

    /**
     * 返回表单字段名.
     *
     * @return 表单字段名
     */
    public String getFormFieldName() {
        return formFieldName;
    }

    /**
     * 返回表单中的文件名，来自客户端传入.
     *
     * @return 表单文件名
     */
    public String getFormFileName() {
        return formFileName;
    }

    /**
     * 获取文件名，不包括路径.
     *
     * @return 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 返回上传的内容类型，通常格式为 mime_type/mime_subtype.
     *
     * @return content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 返回文件 MIME 类型.
     *
     * @return MIME 类型
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * 返回文件 MIME 子类型.
     *
     * @return MIME 子类型
     */
    public String getMimeSubtype() {
        return mimeSubtype;
    }

    /**
     * 返回内容处置方式，通常为 'form-data'.
     *
     * @return 内容处置方式
     */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /**
     * 获得头信息字符串中指定的值.
     *
     * @param dataHeader 头信息
     * @param fieldName  字段名
     * @return 字段值
     */
    private String getDataFieldValue(final String dataHeader,
            final String fieldName) {
        String value = null;
        final String token = StrUtil.format("{}=\"", fieldName);
        final int pos = dataHeader.indexOf(token);
        if (pos > 0) {
            final int start = pos + token.length();
            final int end = dataHeader.indexOf('"', start);
            if ((start > 0) && (end > 0)) {
                value = dataHeader.substring(start, end);
            }
        }
        return value;
    }

    /**
     * 从头信息中获得 content type.
     *
     * @param dataHeader 数据头信息
     * @return content type，如果未定义则返回空字符串
     */
    private String getContentType(final String dataHeader) {
        final String token = "Content-Type:";
        final int start = dataHeader.indexOf(token);
        if (start == -1) {
            return StrUtil.EMPTY;
        }
        return dataHeader.substring(start + token.length());
    }

    /**
     * 从头信息中获得内容处置方式.
     *
     * @param dataHeader 数据头信息
     * @return 内容处置方式
     */
    private String getContentDisposition(final String dataHeader) {
        final int start = dataHeader.indexOf(':') + 1;
        final int end = dataHeader.indexOf(';');
        return dataHeader.substring(start, end);
    }

    /**
     * 从 content type 中获得 MIME 类型.
     *
     * @param contentType content type
     * @return MIME 类型
     */
    private String getMimeType(final String contentType) {
        final int pos = contentType.indexOf('/');
        if (pos == -1) {
            return contentType;
        }
        return contentType.substring(1, pos);
    }

    /**
     * 从 content type 中获得 MIME 子类型.
     *
     * @param contentType content type
     * @return MIME 子类型
     */
    private String getMimeSubtype(final String contentType) {
        final int start = contentType.indexOf('/');
        if (start == -1) {
            return contentType;
        }
        return contentType.substring(start + 1);
    }

    /**
     * 处理头字符串，使之转化为字段.
     *
     * @param dataHeader 头字符串
     */
    private void processHeaderString(final String dataHeader) {
        isFile = dataHeader.indexOf("filename") > 0;
        formFieldName = getDataFieldValue(dataHeader, "name");
        if (isFile) {
            formFileName = getDataFieldValue(dataHeader, "filename");
            if (formFileName == null) {
                return;
            }
            if (formFileName.length() == 0) {
                path = StrUtil.EMPTY;
                fileName = StrUtil.EMPTY;
            }
            final int ls = FileUtil.lastIndexOfSeparator(formFileName);
            if (ls == -1) {
                path = StrUtil.EMPTY;
                fileName = formFileName;
            } else {
                path = formFileName.substring(0, ls);
                fileName = formFileName.substring(ls);
            }
            if (fileName.length() > 0) {
                this.contentType = getContentType(dataHeader);
                mimeType = getMimeType(contentType);
                mimeSubtype = getMimeSubtype(contentType);
                contentDisposition = getContentDisposition(dataHeader);
            }
        }
    }
}
