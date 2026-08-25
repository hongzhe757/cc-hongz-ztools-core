package cc.hongz.ztools.core.date.format;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化输出接口。 <br>
 * 感谢 Apache Commons Lang 3.5
 * @author Looly
 * @since 2.16.2
 */
public interface DatePrinter extends DateBasic {

    /**
     * 格式化日期表示的毫秒数。
     *
     * @param millis 日期毫秒数
     * @return 格式化后的字符串
     * @since 2.1
     */
    String format(long millis);

    /**
     * 使用 {@code GregorianCalendar} 格式化 {@code Date}。
     *
     * @param date 日期 {@link Date}
     * @return 格式化后的字符串
     */
    String format(Date date);

    /**
     * <p>
     * 格式化 {@code Calendar} 对象。
     * </p>
     * 格式化 {@link Calendar}
     *
     * @param calendar {@link Calendar}
     * @return 格式化后的字符串
     */
    String format(Calendar calendar);

    /**
     * <p>
     * 将毫秒 {@code long} 值格式化到提供的 {@code Appendable}。
     * </p>
     *
     * @param millis 要格式化的毫秒值
     * @param buf 要格式化到的缓冲区
     * @param <B> Appendable 类类型，通常是 StringBuilder 或 StringBuffer。
     * @return 指定的字符串缓冲区
     */
    <B extends Appendable> B format(long millis, B buf);

    /**
     * <p>
     * 使用 {@code GregorianCalendar} 将 {@code Date} 对象格式化到提供的 {@code Appendable}。
     * </p>
     *
     * @param date 要格式化的日期
     * @param buf 要格式化到的缓冲区
     * @param <B> Appendable 类类型，通常是 StringBuilder 或 StringBuffer。
     * @return 指定的字符串缓冲区
     */
    <B extends Appendable> B format(Date date, B buf);

    /**
     * <p>
     * 将 {@code Calendar} 对象格式化到提供的 {@code Appendable}。
     * </p>
     * Calendar 上设置的 TimeZone 仅用于调整时间偏移。构造 Parser 时指定的 TimeZone 将决定格式化字符串中使用的 TimeZone。
     *
     * @param calendar 要格式化的日历
     * @param buf 要格式化到的缓冲区
     * @param <B> Appendable 类类型，通常是 StringBuilder 或 StringBuffer。
     * @return 指定的字符串缓冲区
     */
    <B extends Appendable> B format(Calendar calendar, B buf);
}
