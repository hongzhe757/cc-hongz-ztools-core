package cc.hongz.ztools.core.util;

/**
 * URL 与 Data URI 工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，生成 Data URI 字符串.
 * @author hongzhe.qin
 */
public final class URLUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private URLUtil() {
    }

    /**
     * 生成 Base64 形式的 Data URI.
     *
     * @param mimeType MIME 类型，如 image/png
     * @param data     Base64 编码后的数据
     * @return Data URI 字符串
     */
    public static String getDataUriBase64(final String mimeType, final String data) {
        final StringBuilder builder = new StringBuilder("data:");
        if (StrUtil.isNotBlank(mimeType)) {
            builder.append(mimeType);
        }
        builder.append(";base64,").append(data);
        return builder.toString();
    }
}
