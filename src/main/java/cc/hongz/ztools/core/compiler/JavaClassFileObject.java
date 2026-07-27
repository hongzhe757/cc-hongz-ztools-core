package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.URLUtil;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Java 字节码文件对象，用于在内存中暂存class字节码.
 *
 * @author lzpeng
 * @since 5.5.2
 */
class JavaClassFileObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream byteArrayOutputStream;

    /**
     * 构造.
     *
     * @param className 编译后的class文件的类名
     */
    protected JavaClassFileObject(final String className) {
        super(URLUtil.getStringURI(
                className.replace(CharUtil.DOT, CharUtil.SLASH)
                        + Kind.CLASS.extension),
                Kind.CLASS);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(
                byteArrayOutputStream.toByteArray());
    }

    @Override
    public OutputStream openOutputStream() {
        return this.byteArrayOutputStream;
    }
}
