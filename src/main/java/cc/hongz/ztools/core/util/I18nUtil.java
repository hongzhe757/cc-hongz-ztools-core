package cc.hongz.ztools.core.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 国际化消息工具。
 * <p>
 * 从 classpath 加载消息资源（如 {@code messages_zh_CN.properties}），按 key + locale 取消息，支持占位符参数替换。
 * 纯 JDK 实现（{@link ResourceBundle} + {@link MessageFormat}），不依赖 Spring。
 *
 * @author hongz
 */
public final class I18nUtil {

    private I18nUtil() {
    }

    /**
     * 按默认 locale 取消息。
     *
     * @param baseName 资源基名（如 messages）
     * @param key      消息键
     * @return 消息文本，未找到时返回 key 本身
     */
    public static String getMessage(final String baseName, final String key) {
        return getMessage(baseName, key, Locale.getDefault());
    }

    /**
     * 按指定 locale 取消息。
     *
     * @param baseName 资源基名
     * @param key      消息键
     * @param locale   语言环境
     * @return 消息文本，未找到时返回 key 本身
     */
    public static String getMessage(final String baseName, final String key, final Locale locale) {
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * 按默认 locale 取消息并替换占位符参数。
     *
     * @param baseName 资源基名
     * @param key      消息键
     * @param params   占位符参数（{0} {1} ...）
     * @return 替换后的消息文本
     */
    public static String getMessage(final String baseName, final String key, final Object... params) {
        return format(getMessage(baseName, key), params);
    }

    /**
     * 按指定 locale 取消息并替换占位符参数。
     *
     * @param baseName 资源基名
     * @param key      消息键
     * @param locale   语言环境
     * @param params   占位符参数（{0} {1} ...）
     * @return 替换后的消息文本
     */
    public static String getMessage(final String baseName, final String key, final Locale locale,
                                    final Object... params) {
        return format(getMessage(baseName, key, locale), params);
    }

    /**
     * 占位符替换（{@link MessageFormat} 的 {0} {1} 占位符）。
     *
     * @param template 模板
     * @param params   参数
     * @return 替换后的文本
     */
    private static String format(final String template, final Object... params) {
        if (params == null || params.length == 0) {
            return template;
        }
        return MessageFormat.format(template, params);
    }
}
