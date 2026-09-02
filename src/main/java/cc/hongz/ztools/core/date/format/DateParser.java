package cc.hongz.ztools.core.date.format;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期解析接口，用于解析日期字符串为 {@link Date} 对象。 <br>
 * 感谢 Apache Commons Lang 3.5
 * @author looly (原作者)
 * @since 2.16.2
 */
public interface DateParser extends DateBasic {

    /**
     * 将日期字符串解析并转换为  {@link Date} 对象。 <br>
     * 等价于 {@link java.text.DateFormat#parse(String)}
     *
     * @param source 日期字符串
     * @return {@link Date}
     * @throws ParseException 转换异常，被转换的字符串格式错误。
     */
    Date parse(String source) throws ParseException;

    /**
     * 将日期字符串解析并转换为  {@link Date} 对象。 <br>
     * 等价于 {@link java.text.DateFormat#parse(String, ParsePosition)}
     *
     * @param source 日期字符串
     * @param pos {@link ParsePosition}
     * @return {@link Date}
     */
    Date parse(String source, ParsePosition pos);

    /**
     * 根据给定格式更新{@link Calendar}。
     * 成功后，ParsePosition 索引更新为已消费的源文本长度。
     * 并非所有源文本都需要被消费。
     * 解析失败时，ParsePosition 错误索引更新为与提供格式不匹配的源文本偏移量。
     *
     * @param source   被转换的日期字符串
     * @param pos      定义开始转换的位置，转换结束后更新转换到的位置
     * @param calendar 要设置解析字段的日历。
     * @return 如果源已解析则返回 true（pos parsePosition 已更新）；否则返回 false（pos errorIndex 已更新）
     * @throws IllegalArgumentException 当 Calendar 设置为非宽松模式，且解析字段超出范围时。
     */
    boolean parse(String source, ParsePosition pos, Calendar calendar);

    /**
     * 将日期字符串解析并转换为  {@link Date} 对象。 <br>
     *
     * @param source 要解析开头的 {@code String}。
     * @return {@code java.util.Date} 对象
     * @throws ParseException 如果指定字符串的开头无法解析。
     * @see java.text.DateFormat#parseObject(String)
     */
    default Object parseObject(String source) throws ParseException {
        return parse(source);
    }

    /**
     * 根据 {@link ParsePosition} 给定将日期字符串解析并转换为  {@link Date} 对象。 <br>
     *
     * @param source 要解析开头的 {@code String}。
     * @param pos    解析位置
     * @return {@code java.util.Date} 对象
     * @see java.text.DateFormat#parseObject(String, ParsePosition)
     */
    default Object parseObject(String source, ParsePosition pos) {
        return parse(source, pos);
    }
}
