package cc.hongz.ztools.core.util;

/**
 * 对象工具类（captcha 最小子集）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，提供空值默认值处理.
 * @author hongzhe.qin
 */
public final class ObjectUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private ObjectUtil() {
    }

    /**
     * 对象为 null 时返回默认值.
     *
     * @param object       原对象
     * @param defaultValue 默认值
     * @param <T>          对象类型
     * @return 原对象或默认值
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * 对象是否为 null.
     *
     * @param object 对象
     * @return 是否为 null
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }
}
