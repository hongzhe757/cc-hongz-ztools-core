package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.URLUtil;

import javax.tools.SimpleJavaFileObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Java 源码文件对象.
 *
 * @author lzpeng
 * @since 5.5.2
 */
class JavaSourceFileObject extends SimpleJavaFileObject {

    private InputStream inputStream;
    private String sourceCode;

    /**
     * 构造，支持File等路径类型的源码.
     *
     * @param uri 需要编译的文件uri
     */
    protected JavaSourceFileObject(final URI uri) {
        super(uri, Kind.SOURCE);
    }

    /**
     * 构造，支持String类型的源码.
     *
     * @param className 需要编译的类名
     * @param code      需要编译的类源码
     * @param charset   编码
     */
    protected JavaSourceFileObject(final String className,
            final String code, final Charset charset) {
        this(className, IoUtil.toStream(code, charset));
    }

    /**
     * 构造，支持流中读取源码.
     *
     * @param name        需要编译的文件名
     * @param inputStream 输入流
     */
    protected JavaSourceFileObject(final String name,
            final InputStream inputStream) {
        this(URLUtil.getStringURI(
                name.replace(CharUtil.DOT, CharUtil.SLASH)
                        + Kind.SOURCE.extension));
        this.inputStream = inputStream;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = toUri().toURL().openStream();
        }
        return new BufferedInputStream(inputStream);
    }

    @Override
    public CharSequence getCharContent(
            final boolean ignoreEncodingErrors) throws IOException {
        if (sourceCode == null) {
            try (InputStream in = openInputStream()) {
                sourceCode = IoUtil.read(in,
                        java.nio.charset.StandardCharsets.UTF_8);
            }
        }
        return sourceCode;
    }
}
