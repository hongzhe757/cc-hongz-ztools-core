package cc.hongz.ztools.core.convert.impl;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.convert.ConvertException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * 字符串转换器，提供各种对象转换为字符串的逻辑封装。
 *
 * @author Looly
 */
public class StringConverter extends AbstractConverter<String> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部转换。
     */
    @Override
    protected String convertInternal(final Object value) {
        if (value instanceof TimeZone) {
            return ((TimeZone) value).getID();
        } else if (value instanceof org.w3c.dom.Node) {
            return nodeToStr((org.w3c.dom.Node) value);
        } else if (value instanceof Clob) {
            return clobToStr((Clob) value);
        } else if (value instanceof Blob) {
            return blobToStr((Blob) value);
        } else if (value instanceof Type) {
            return ((Type) value).getTypeName();
        }

        return convertToStr(value);
    }

    /**
     * Clob 转字符串。
     */
    private static String clobToStr(final Clob clob) {
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream();
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (SQLException | java.io.IOException e) {
            throw new ConvertException(e);
        }
        return sb.toString();
    }

    /**
     * Blob 转字符串。
     */
    private static String blobToStr(final Blob blob) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = blob.getBinaryStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (SQLException | java.io.IOException e) {
            throw new ConvertException(e);
        }
        return sb.toString();
    }

    /**
     * Node 转字符串。
     */
    private static String nodeToStr(final org.w3c.dom.Node node) {
        return node.getTextContent();
    }
}
