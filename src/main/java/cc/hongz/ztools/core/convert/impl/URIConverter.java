package cc.hongz.ztools.core.convert.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;

import cc.hongz.ztools.core.convert.AbstractConverter;

/**
 * URI对象转换器。
 * @author Looly
 *
 */
public class URIConverter extends AbstractConverter<URI> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected URI convertInternal(final Object value) {
        try {
            if (value instanceof File) {
                return ((File) value).toURI();
            }

            if (value instanceof URL) {
                return ((URL) value).toURI();
            }
            return new URI(convertToStr(value));
        } catch (Exception e) {
            // Ignore Exception
        }
        return null;
    }

}
