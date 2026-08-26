package cc.hongz.ztools.core.io.resource;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 资源接口定义.
 *
 * @author looly
 * @since 3.2.1
 */
public interface Resource {

    /**
     * 获取资源名。
     *
     * @return 资源名
     */
    String getName();

    /**
     * 获得解析后的{@link URL}。
     *
     * @return {@link URL}
     */
    URL getUrl();

    /**
     * 获得 {@link InputStream}。
     *
     * @return {@link InputStream}
     */
    InputStream getStream();

    /**
     * 检查资源是否变更。
     *
     * @return 是否变更
     */
    default boolean isModified() {
        return false;
    }

    /**
     * 将资源内容写出到流。
     *
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    default void writeTo(OutputStream out) throws IORuntimeException {
        try (InputStream in = getStream()) {
            IoUtil.copy(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得Reader。
     *
     * @param charset 编码
     * @return {@link BufferedReader}
     */
    default BufferedReader getReader(Charset charset) {
        return IoUtil.getReader(getStream(), charset);
    }

    /**
     * 读取资源内容。
     *
     * @param charset 编码
     * @return 读取资源内容。
     * @throws IORuntimeException IO异常
     */
    default String readStr(Charset charset) throws IORuntimeException {
        return IoUtil.read(getReader(charset));
    }

    /**
     * 读取资源内容（UTF-8）。
     *
     * @return 读取资源内容。
     * @throws IORuntimeException IO异常
     */
    default String readUtf8Str() throws IORuntimeException {
        return readStr(CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 读取资源内容为字节数组。
     *
     * @return 字节数组
     * @throws IORuntimeException IO异常
     */
    default byte[] readBytes() throws IORuntimeException {
        return IoUtil.readBytes(getStream());
    }
}
