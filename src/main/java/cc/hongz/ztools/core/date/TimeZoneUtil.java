package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.util.JdkUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.SystemPropsUtil;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * 时间时区工具类。
 *
 * @author looly
 * @since 5.8.44
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class TimeZoneUtil {
    /**
     * {@link java.util.TimeZone} 的包私有 {@code GMT_ID} 字段的公共版本。
     */
    public static final String GMT_ID = "GMT";

    /**
     * GMT 时区。
     */
    public static final TimeZone GMT = getTimeZone(GMT_ID);

    /**
     * 如果 ID 在 {@link ZoneId#SHORT_IDS} 中，则先映射 ID 再委托给 {@link TimeZone#getTimeZone(String)}。
     * <p>
     * On Java 25, calling {@link TimeZone#getTimeZone(String)} with an ID in {@link ZoneId#SHORT_IDS}
     * writes a message to {@link System#err} in the form:
     * </p>
     *
     * <pre>
     * WARNING: Use of the three-letter time zone ID "the-short-id" is deprecated
     * and it will be removed in a future release
     * </pre>
     * <p>
     * You can disable mapping from {@link ZoneId#SHORT_IDS} by setting the system property
     * {@code "TimeZones.mapShortIDs=false"}.
     * </p>
     *
     * @param id Same as {@link TimeZone#getTimeZone(String)}.
     * @return Same as {@link TimeZone#getTimeZone(String)}.
     */
    public static TimeZone getTimeZone(final String id) {
        return TimeZone.getTimeZone(JdkUtil.IS_AT_LEAST_JDK25 && mapShortIDs()
                ? ZoneId.SHORT_IDS.getOrDefault(id, id) : id);
    }

    private static boolean mapShortIDs() {
        return SystemPropsUtil.getBoolean("TimeZones.mapShortIDs", true);
    }

    /**
     * 如果给定时区非 {@code null} 则返回它，否则返回 {@link TimeZone#getDefault()}。
     *
     * @param timeZone a locale or {@code null}.
     * @return the given locale if non-{@code null}, otherwise {@link TimeZone#getDefault()}.
     */
    public static TimeZone toTimeZone(final TimeZone timeZone) {
        return ObjectUtil.defaultIfNull(timeZone, TimeZone::getDefault);
    }
}
