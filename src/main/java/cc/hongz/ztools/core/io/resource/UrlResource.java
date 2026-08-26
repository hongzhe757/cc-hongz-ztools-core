package cc.hongz.ztools.core.io.resource;

import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.URLUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 * URL资源访问类。
 * @author Looly
 *
 */
public class UrlResource implements Resource, Serializable {
    private static final long serialVersionUID = 1L;

    protected URL url;
    private long lastModified = 0;
    protected String name;

    //-------------------------------------------------------------------------------------- Constructor start
    /**
     * 构造。
     * @param uri URI
     * @since 5.7.21
     */
    public UrlResource(final URI uri) {
        this(URLUtil.url(uri), null);
    }

    /**
     * 构造。
     * @param url URL
     */
    public UrlResource(final URL url) {
        this(url, null);
    }

    /**
     * 构造。
     * @param url URL，允许为空
     * @param name 资源名称
     */
    public UrlResource(final URL url, final String name) {
        this.url = url;
        if (null != url && URLUtil.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
            this.lastModified = FileUtil.file(url).lastModified();
        }
        this.name = ObjectUtil.defaultIfNull(name, () -> (null != url ? FileUtil.getName(url.getPath()) : null));
    }

    /**
     * 构造。
     * @param file 文件路径
     * @deprecated Please use {@link FileResource}
     */
    @Deprecated
    public UrlResource(final File file) {
        this.url = URLUtil.getURL(file);
    }
    //-------------------------------------------------------------------------------------- Constructor end

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public InputStream getStream() throws NoResourceException {
        if (null == this.url) {
            throw new NoResourceException("Resource URL is null!");
        }
        return URLUtil.getStream(url);
    }

    @Override
    public boolean isModified() {
        return (0 != this.lastModified) && this.lastModified != getFile().lastModified();
    }

    /**
     * 获得File。
     * @return {@link File}
     */
    public File getFile() {
        return FileUtil.file(this.url);
    }

    /**
     * 返回路径。
     * @return 返回URL路径
     */
    @Override
    public String toString() {
        return (null == this.url) ? "null" : this.url.toString();
    }

    /**
     * 获取资源长度。
     *
     * @return 资源长度
     * @since 5.8.21
     */
    public long size() {
        return URLUtil.size(this.url);
    }
}
