package cc.hongz.ztools.core.io.resource;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * {@link CharSequence}资源，字符串做为资源。
 *
 * @author looly
 * @since 5.5.2
 */
public class CharSequenceResource implements Resource, Serializable {
    private static final long serialVersionUID = 1L;

    private final CharSequence data;
    private final CharSequence name;
    private final Charset charset;

    /**
     * 构造，使用UTF8编码。
     *
     * @param data 资源数据
     */
    public CharSequenceResource(final CharSequence data) {
        this(data, null);
    }

    /**
     * 构造，使用UTF8编码。
     *
     * @param data 资源数据
     * @param name 资源名称
     */
    public CharSequenceResource(final CharSequence data, final String name) {
        this(data, name, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 构造。
     *
     * @param data 资源数据
     * @param name 资源名称
     * @param charset 编码
     */
    public CharSequenceResource(final CharSequence data, final CharSequence name, final Charset charset) {
        this.data = data;
        this.name = name;
        this.charset = charset;
    }

    @Override
    public String getName() {
        return StrUtil.str(this.name);
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(readBytes());
    }

    @Override
    public BufferedReader getReader(final Charset newCharset) {
        return IoUtil.getReader(new StringReader(this.data.toString()));
    }

    @Override
    public String readStr(final Charset newCharset) throws IORuntimeException {
        return this.data.toString();
    }

    @Override
    public byte[] readBytes() throws IORuntimeException {
        return this.data.toString().getBytes(this.charset);
    }

}
