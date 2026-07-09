package cc.hongz.ztools.core.io;

/**
 * MIME 类型与扩展名常量（自 ivics 迁入，与 {@link FileTypeUtil} 同属 IO 包）.
 *
 * @author hongzhe.qin
 */
public final class MimeTypeUtils {

    private MimeTypeUtils() {
    }

    /** PNG 图片 MIME. */
    public static final String IMAGE_PNG = "image/png";

    /** JPG 图片 MIME. */
    public static final String IMAGE_JPG = "image/jpg";

    /** JPEG 图片 MIME. */
    public static final String IMAGE_JPEG = "image/jpeg";

    /** BMP 图片 MIME. */
    public static final String IMAGE_BMP = "image/bmp";

    /** GIF 图片 MIME. */
    public static final String IMAGE_GIF = "image/gif";

    /** 允许上传的图片扩展名. */
    public static final String[] IMAGE_EXTENSION = {"jpg", "png"};

    /**
     * 根据 MIME 类型返回对应扩展名.
     *
     * @param mimeType MIME 类型（如 {@link #IMAGE_PNG}）
     * @return 扩展名，未知类型返回空字符串
     */
    public static String getExtension(final String mimeType) {
        if (mimeType == null) {
            return "";
        }
        switch (mimeType) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
