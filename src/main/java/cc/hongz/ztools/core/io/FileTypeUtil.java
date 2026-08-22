package cc.hongz.ztools.core.io;

import cc.hongz.ztools.core.util.HexUtil;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 文件类型判断工具类（自 Hutool {@code cn.hutool.core.io.FileTypeUtil} 迁入，并合并 ivics {@code FileTypeUtils}）.
 *
 * <p>此工具根据文件的前几位 bytes 猜测文件类型，对于文本、zip 判断不准确，对于视频、图片类型判断准确。</p>
 *
 * <p>需要注意的是，xlsx、docx 等 Office 2007 格式，全部识别为 zip，因为新版采用了 OpenXML 格式，这些格式本质上是 XML 文件打包为 zip。</p>
 *
 * @author Looly
 */
public final class FileTypeUtil {

    private FileTypeUtil() {
    }

    private static final Map<String, String> FILE_TYPE_MAP = new ConcurrentSkipListMap<>();

    /**
     * 增加文件类型映射.<br>
     * 如果已经存在将覆盖之前的映射.
     *
     * @param fileStreamHexHead 文件流头部Hex信息
     * @param extName           文件扩展名
     * @return 之前已经存在的文件扩展名
     */
    public static String putFileType(final String fileStreamHexHead, final String extName) {
        return FILE_TYPE_MAP.put(fileStreamHexHead, extName);
    }

    /**
     * 移除文件类型映射.
     *
     * @param fileStreamHexHead 文件流头部Hex信息
     * @return 移除的文件扩展名
     */
    public static String removeFileType(final String fileStreamHexHead) {
        return FILE_TYPE_MAP.remove(fileStreamHexHead);
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     *
     * @param fileStreamHexHead 文件流头部16进制字符串
     * @return 文件类型，未找到为{@code null}
     */
    public static String getType(final String fileStreamHexHead) {
        if (StrUtil.isBlank(fileStreamHexHead)) {
            return null;
        }
        if (MapUtil.isNotEmpty(FILE_TYPE_MAP)) {
            for (final Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
                if (StrUtil.startWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
                    return fileTypeEntry.getValue();
                }
            }
        }
        byte[] bytes = HexUtil.decodeHex(fileStreamHexHead);
        return FileMagicNumber.getMagicNumber(bytes).getExtension();
    }

    /**
     * 根据字节内容识别文件类型.
     *
     * @param bytes 文件内容或文件头
     * @return 扩展名，未识别为 {@code null}
     */
    public static String getType(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final FileMagicNumber magic = FileMagicNumber.getMagicNumber(bytes);
        return magic == FileMagicNumber.UNKNOWN ? null : magic.getExtension();
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     *
     * @param in           文件流
     * @param fileHeadSize 自定义读取文件头部的大小
     * @return 文件类型，未找到为{@code null}
     * @throws IORuntimeException IO异常
     */
    public static String getType(final InputStream in, final int fileHeadSize) throws IORuntimeException {
        return getType((IoUtil.readHex(in, fileHeadSize, false)));
    }

    /**
     * 根据文件流的头部信息获得文件类型.<br>
     * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes.<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * @param in      {@link InputStream}
     * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
     * @return 类型，文件的扩展名，提供的in为{@code null}或未找到为{@code null}
     * @throws IORuntimeException 读取流引起的异常
     */
    public static String getType(final InputStream in, final boolean isExact) throws IORuntimeException {
        if (null == in) {
            return null;
        }
        return isExact
            ? getType(IoUtil.readHex8192Upper(in))
            : getType(IoUtil.readHex64Upper(in));
    }

    /**
     * 根据文件流的头部信息获得文件类型.<br>
     * 注意此方法会读取头部64个bytes，造成此流接下来读取时缺少部分bytes.<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * @param in {@link InputStream}
     * @return 类型，文件的扩展名，未找到为{@code null}
     * @throws IORuntimeException 读取流引起的异常
     */
    public static String getType(final InputStream in) throws IORuntimeException {
        return getType(in, false);
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     * 注意此方法会读取头部64个bytes，造成此流接下来读取时缺少部分bytes.<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别.
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分.
     *     3、zip可能为docx、xlsx、pptx、jar、war、ofd头信息无法区分，按照扩展名区分.
     * </pre>.
     *
     * @param in       {@link InputStream}
     * @param filename 文件名
     * @return 类型，文件的扩展名，未找到为{@code null}
     * @throws IORuntimeException 读取流引起的异常
     */
    public static String getType(final InputStream in, final String filename) throws IORuntimeException {
        return getType(in, filename, false);
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes.<br>
     * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别.
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分.
     *     3、zip可能为docx、xlsx、pptx、jar、war、ofd头信息无法区分，按照扩展名区分.
     * </pre>.
     *
     * @param in       {@link InputStream}
     * @param filename 文件名
     * @param isExact  是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
     * @return 类型，文件的扩展名，未找到为{@code null}
     * @throws IORuntimeException 读取流引起的异常
     */
    public static String getType(final InputStream in, final String filename, final boolean isExact)
            throws IORuntimeException {
        String typeName = getType(in, isExact);
        if (null == typeName) {
            // 未成功识别类型，扩展名辅助识别
            typeName = FileUtil.extName(filename);
        } else if ("zip".equals(typeName)) {
            // zip可能为docx、xlsx、pptx、jar、war、ofd等格式，扩展名辅助判断
            final String extName = FileUtil.extName(filename);
            if ("docx".equalsIgnoreCase(extName)) {
                typeName = "docx";
            } else if ("xlsx".equalsIgnoreCase(extName)) {
                typeName = "xlsx";
            } else if ("pptx".equalsIgnoreCase(extName)) {
                typeName = "pptx";
            } else if ("jar".equalsIgnoreCase(extName)) {
                typeName = "jar";
            } else if ("war".equalsIgnoreCase(extName)) {
                typeName = "war";
            } else if ("ofd".equalsIgnoreCase(extName)) {
                typeName = "ofd";
            } else if ("apk".equalsIgnoreCase(extName)) {
                typeName = "apk";
            }
        } else if ("jar".equals(typeName)) {
            // wps编辑过的.xlsx文件与.jar的开头相同,通过扩展名判断
            final String extName = FileUtil.extName(filename);
            if ("xlsx".equalsIgnoreCase(extName)) {
                typeName = "xlsx";
            } else if ("docx".equalsIgnoreCase(extName)) {
                // issue#I47JGH
                typeName = "docx";
            } else if ("pptx".equalsIgnoreCase(extName)) {
                // issue#I5A0GO
                typeName = "pptx";
            } else if ("zip".equalsIgnoreCase(extName)) {
                typeName = "zip";
            } else if ("apk".equalsIgnoreCase(extName)) {
                typeName = "apk";
            }
        }
        return typeName;
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别.
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分.
     *     3、zip可能为jar、war头信息无法区分，按照扩展名区分.
     * </pre>.
     *
     * @param file    文件 {@link File}
     * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
     * @return 类型，文件的扩展名，未找到为{@code null}
     * @throws IORuntimeException 读取文件引起的异常
     */
    public static String getType(final File file, final boolean isExact) throws IORuntimeException {
        if (false == FileUtil.isFile(file)) {
            throw new IllegalArgumentException("Not a regular file!");
        }
        FileInputStream in = null;
        try {
            in = IoUtil.toStream(file);
            return getType(in, file.getName(), isExact);
        } finally {
            IoUtil.close(in);
        }
    }

    /**
     * 根据文件流的头部信息获得文件类型.
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别.
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分.
     *     3、zip可能为jar、war头信息无法区分，按照扩展名区分.
     * </pre>.
     *
     * @param file 文件 {@link File}
     * @return 类型，文件的扩展名，未找到为{@code null}
     * @throws IORuntimeException 读取文件引起的异常
     */
    public static String getType(final File file) throws IORuntimeException {
        return getType(file, false);
    }

    /**
     * 通过路径获得文件类型.
     *
     * @param path    路径，绝对路径或相对ClassPath的路径
     * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
     * @return 类型
     * @throws IORuntimeException 读取文件引起的异常
     */
    public static String getTypeByPath(final String path, final boolean isExact) throws IORuntimeException {
        return getType(new File(path), isExact);
    }

    /**
     * 通过路径获得文件类型.
     *
     * @param path 路径，绝对路径或相对ClassPath的路径
     * @return 类型
     * @throws IORuntimeException 读取文件引起的异常
     */
    public static String getTypeByPath(final String path) throws IORuntimeException {
        return getTypeByPath(path, false);
    }

    /**
     * 按文件名获取扩展名（小写，不含 {@code .}）.
     *
     * @param file 文件
     * @return 扩展名，{@code null} 文件时返回空字符串
     */
    public static String getFileType(final File file) {
        if (file == null) {
            return "";
        }
        return getFileType(file.getName());
    }

    /**
     * 按文件名获取扩展名（小写，不含 {@code .}）.
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getFileType(final String fileName) {
        if (fileName == null) {
            return "";
        }
        final int separatorIndex = fileName.lastIndexOf('.');
        if (separatorIndex < 0) {
            return "";
        }
        return fileName.substring(separatorIndex + 1).toLowerCase();
    }

    /**
     * 根据文件头魔数识别类型（{@link #getType(byte[])} 别名）.
     *
     * @param bytes 文件头字节
     * @return 扩展名；无法识别时返回 {@code null}
     */
    public static String getTypeByMagic(final byte[] bytes) {
        return getType(bytes);
    }

    /**
     * 魔数识别失败时回退为文件名扩展名.
     *
     * @param bytes    文件头
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getType(final byte[] bytes, final String fileName) {
        final String type = getType(bytes);
        return type != null ? type : FileUtil.extName(fileName);
    }

    /**
     * 根据魔数猜测类型；兼容 ivics 旧 API，无法识别时默认 {@code jpg}.
     *
     * @param bytes 文件头字节
     * @return 扩展名（小写）
     * @deprecated 请使用 {@link #getType(byte[])} 或 {@link #getTypeByMagic(byte[])}
     */
    @Deprecated
    public static String getFileExtendName(final byte[] bytes) {
        final String type = getType(bytes);
        return type == null ? "jpg" : type;
    }


}
