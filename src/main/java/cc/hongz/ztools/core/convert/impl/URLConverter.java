package cc.hongz.ztools.core.convert.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;

import cc.hongz.ztools.core.convert.AbstractConverter;

/**
 * URL对象转换器。
 * @author Looly
 *
 */
public class URLConverter extends AbstractConverter<URL> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected URL convertInternal(final Object value) {
        try {
            if (value instanceof File) {
                return ((File) value).toURI().toURL();
            }

            if (value instanceof URI) {
                return ((URI) value).toURL();
            }
            return new URL(convertToStr(value));
        } catch (Exception e) {
            // Ignore Exception
        }
        return null;
    }

}
